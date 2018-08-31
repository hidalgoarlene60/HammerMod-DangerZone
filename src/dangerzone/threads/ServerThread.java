package dangerzone.threads;
/*
 * This code is copyright Richard H. Clark, TheyCallMeDanger, OreSpawn, 2015-2020.
 * You may use this code for reference for modding the DangerZone game program,
 * and are perfectly welcome to cut'n'paste portions for your mod as well.
 * DO NOT USE THIS CODE FOR ANY PURPOSE OTHER THAN MODDING FOR THE DANGERZONE GAME.
 * DO NOT REDISTRIBUTE THIS CODE. 
 * 
 * This copyright remains in effect until January 1st, 2021. 
 * At that time, this code becomes public domain.
 * 
 * WARNING: There are bugs. Big bugs. Little bugs. Every size in-between bugs.
 * This code is NOT suitable for use in anything other than this particular game. 
 * NO GUARANTEES of any sort are given, either express or implied, and Richard H. Clark, 
 * TheyCallMeDanger, OreSpawn are not responsible for any damages, direct, indirect, or otherwise. 
 * You should have made backups. It's your own fault for not making them.
 * 
 * NO ATTEMPT AT SECURITY IS MADE. This code is USE AT YOUR OWN RISK.
 * Regardless of what you may think, the reality is, that the moment you 
 * connected your computer to the Internet, Uncle Sam, among many others, hacked it.
 * DO NOT KEEP VALUABLE INFORMATION ON INTERNET-CONNECTED COMPUTERS.
 * Or your phone...
 * 
 */

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.imageio.ImageIO;

import dangerzone.BreakChecks;
import dangerzone.Chunk;
import dangerzone.CommandHandlers;
import dangerzone.Coords;
import dangerzone.CustomPackets;
import dangerzone.DangerZone;
import dangerzone.Dimensions;
import dangerzone.Effects;
import dangerzone.GameModes;
import dangerzone.InventoryContainer;
import dangerzone.ItemAttribute;
import dangerzone.NetworkInputBuffer;
import dangerzone.NetworkOutputBuffer;
import dangerzone.PacketTypes;
import dangerzone.Player;
import dangerzone.PlayerPrivs;
import dangerzone.ServerHooker;
import dangerzone.ToDoList;
import dangerzone.Utils;
import dangerzone.blocks.Blocks;
import dangerzone.blocks.ColoringBlock;
import dangerzone.entities.Entities;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityBlock;
import dangerzone.entities.EntityBlockItem;
import dangerzone.entities.EntityExp;
import dangerzone.entities.EntityFire;
import dangerzone.entities.EntitySign;
import dangerzone.entities.Ghost;
import dangerzone.entities.GhostSkelly;
import dangerzone.items.Item;
import dangerzone.items.Items;


public class ServerThread implements Runnable {
	
	public Player p;
	
	NetworkOutputBuffer objectOutput = null;
	NetworkInputBuffer objectInput = null;
	private Lock lock = new ReentrantLock(); //send lock so we don't intertwine packets!
	private Lock lightinglock = new ReentrantLock();
	public volatile int fatal_error = 0;
	private volatile int ready = 0;
	private volatile boolean lightready = false;
	private volatile float lightvalue = 0.0f;
	private int save_ticker = 0;
	private ChunkSender chunker = null;
	private long timechecktime;
	private long averagetime;
	private int averagefps;
	private boolean anon_player = false;
	private int client_renderdistance = 24;
	
	
	ServerThread(Player pl){
		p = pl;
		p.server_thread = this;
	}
	
	public void run()  {
		int d, x, y, z, id, iid, meta, pd;
		double px, py, pz; 
		float mx, my, mz, vol, freq;
		String name = null;
		int which, what;
		InventoryContainer ic;
		int packettype = 0;
		boolean do_respawn = false;
		boolean save_player = true;
		String playername = null;
		int lastd, lastx, lasty, lastz;
		long lastmovetime; //really more like inactivity time, not just movement!
		
		lastmovetime = 0; 
		lastd = 0;
		lastx = 0;
		lasty = 0;
		lastz = 0;
		
		averagetime = 0;
		averagefps = 100; //start out nicely!
		timechecktime = System.currentTimeMillis(); 

		
		lock.lock();
		
		try {
			p.toClient.setReceiveBufferSize(1024*128);
		} catch (SocketException e2) {
			e2.printStackTrace();
		}
		
		try {
			p.toClient.setSendBufferSize(1024*1024*2);
		} catch (SocketException e2) {
			e2.printStackTrace();
		}
		
		try {
			//Shorter timeout if real server... longer if single-player...
			if(!DangerZone.start_client){
				p.toClient.setSoTimeout(20000); //20 seconds is enough. Then fail and give up! Some machines can be SLOW to load textures...
			}else{
				p.toClient.setSoTimeout(60000);
			}
		} catch (SocketException e2) {
			e2.printStackTrace();
		} 
		
		
		//we do our own buffering. It's much better to send immediately!
		try {
			p.toClient.setTcpNoDelay(true);
		} catch (SocketException e2) {
			e2.printStackTrace();
		}
		
		
		
		//System.out.printf("Got connection!\n");
		try {
			objectInput = new NetworkInputBuffer(p.toClient.getInputStream(), 1024*64);
			objectOutput = new NetworkOutputBuffer(p.toClient.getOutputStream(), 1024*128);
			
			packettype = objectInput.readInt();	
			if(packettype != PacketTypes.CONNECT){
				lock.unlock();
				objectInput.close();
				objectOutput.close();
				try {
					p.toClient.close();
				} catch (IOException e) {
				}
				DangerZone.server.removeMe(this);
				return;
			}
			
			//version check!!!
			String clientversion = objectInput.readString();
			
			//System.out.printf("Server read version string: %s\n", clientversion);
			
			if(clientversion != null && clientversion.equals(DangerZone.versionstring)){
				//System.out.printf("Server write version string: %s\n", DangerZone.versionstring);
				objectOutput.writeString(DangerZone.versionstring);
				//System.out.printf("Server flush\n");
				objectOutput.flush();
				//System.out.printf("Server wait sync\n");
				packettype = objectInput.readInt();	//fetch whether clients wants to sync up...
				playername = objectInput.readString();	//get my name!
				String playerpassword = objectInput.readString();	//get my password!
				
				//see if passwords are required, and if this one matches!
				if(!verify_playername(p, playername, playerpassword)){
					packettype = 1;
					objectOutput.writeInt(packettype);
					objectOutput.flush();
					fatal_error = 1;
					System.out.printf("Server player: %s failed verification\n", playername);
				}
				//System.out.printf("Server verified player: %s\n", playername);
				
				if(fatal_error == 0 && packettype != 0){
					//Now send back the items, blocks, and dimension IDs we are using!	
					//System.out.printf("Server get skin\n");
					getSkin();
					//System.out.printf("Server send mod names\n");
					sendModNames();
					
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// Auto-generated catch block
						e.printStackTrace();
					}
					
					//wait until it is done loading mods before sending anything else!!!
					d = objectInput.readInt();

					//System.out.printf("Server send server ids\n");
					sendServerIDs();
					
					//make sure it has gotten all the IDs!
					d = objectInput.readInt();

					//Now find a new name for this player if we need to!
					if(playername.toLowerCase().equals("player") || DangerZone.server.entityManager.findPlayerByName(playername)!= null){
						save_player = false;
						anon_player = true;
						while(true){
							String newname = String.format("Player%d", DangerZone.rand.nextInt(1000));
							if(DangerZone.server.entityManager.findPlayerByName(newname)==null){
								playername = newname;
								break;
							}
							//System.out.printf("stuck in DZ rand loop\n");
						}
					}
					//Don't ask me why... but the network seems to need these next few superfluous lines, or it gets confused and just hangs...
					//System.out.printf("Server sending back player name: %s\n", playername);
					
					objectOutput.writeString(playername);
					objectOutput.flush();

				}
				
				//System.out.printf("Server loading player: %s\n", playername);
				p.myname = playername;
				p.setPetName(playername);
				do_respawn = ServerHooker.player_load(p);
				p.myname = playername;
				p.setPetName(playername);
				//System.out.printf("Server loaded player: %s\n", playername);
				
				if(fatal_error == 0){
					//System.out.printf("Server ban check player: %s\n", playername);
					if(DangerZone.server.isBanned(playername)){
						System.out.printf("Server player: %s is banned\n", playername);
						packettype = 2;
						objectOutput.writeInt(packettype);
						objectOutput.flush();
						fatal_error = 1;
					}else{
						//if(!DangerZone.start_client)System.out.printf("Server count check player: %s\n", playername);
						if(DangerZone.server.get_player_count_unlocked() < DangerZone.max_players_on_server || ServerHooker.player_always_connect(p)){
							//System.out.printf("Player: %s is clear to continue.\n", playername);
							packettype = 0;						
							objectOutput.writeInt(packettype);
							objectOutput.flush();
						}else{
							System.out.printf("Server player: %s is disallowed for some odd reason.\n", playername);
							packettype = 3;
							objectOutput.writeInt(packettype);
							fatal_error = 1;
							objectOutput.flush();
						}
					}
				}
				
				if(fatal_error == 0){
					//System.out.printf("Player init and setup\n");
					p.world = DangerZone.server_world;
					p.init();
					if(p.getHealth() <= 0 || p.getGameMode() == GameModes.LIMBO){
						//System.out.printf("player was dead.\n");
						//p.setHealth(p.getMaxHealth()); //deadlocks because updates when shouldn't during this init!
						p.setVarFloat(1, p.getMaxHealth());
						p.setVarFloat(5, p.getMaxHunger());
						p.setVarFloat(7, p.getMaxAir());
						p.setExperience(0);
						for(int iw=0;iw<p.maxinv;iw++){
							p.setVarInventory(iw, null); //just to make sure!
						}
						do_respawn = true;
						p.setGameMode(GameModes.SURVIVAL);
					}

					//System.out.printf("Server sending player data\n");
					//Send FULL player info back to client!
					sendPlayerToPlayer(p);
					//System.out.printf("XXXXX Server send player done\n");
					//System.out.printf("player sent.\n");

					id = DangerZone.server.entityManager.addEntity(p); //get an entity ID to respond with!
					if(id <= 0){
						fatal_error = 1;
					}else{
						//System.out.printf("Server sending player ID\n");
						objectOutput.writeInt(p.entityID);
						objectOutput.flush();							
					}
				}
			}else{
				objectOutput.writeString(DangerZone.versionstring);
				objectOutput.flush();
				fatal_error = 1;
			}
		} catch (IOException e) {
			fatal_error = 1;
		}

		//Fire up a chunk sender!
		if(fatal_error == 0){
			if(!DangerZone.start_client)System.out.printf("Start chunk sender for player: %s.\n", p.myname);
			chunker = new ChunkSender(p, this);
			Thread cnk = new Thread(chunker);
			cnk.start();	
			//DangerZone.server.sendChatToAllExcept(String.format("Player: %s connected.\n", p.myname), p);
			//System.out.printf("Player: %s connected.\n", p.myname);
			
		}else{
			System.out.printf("Fatal error was found\n");
			lock.unlock();
			if(objectInput != null)objectInput.close();
			if(objectOutput != null)objectOutput.close();
			try {
				p.toClient.close();
			} catch (IOException e) {
			}
			if(p.entityID > 0){
				DangerZone.server.entityManager.removeEntityByID(p.entityID);
			}
			DangerZone.server.removeMe(this);
			return;
		}
		
		ready = 1;
		p.stray_entity_ticker = 0; //let the update thread know this entity is OK
		
		lock.unlock();
		
		if(fatal_error == 0 && DangerZone.server.addMe(this)){ //finally ready to roll!
			
			if(ServerHooker.player_logged_in(p)){
				
				do_respawn = ServerHooker.player_login_respawn(p, do_respawn);

				if(do_respawn){
					//System.out.printf("player respawn.\n");
					p.posx += (DangerZone.server_world.rand.nextFloat()-DangerZone.server_world.rand.nextFloat())*64f;
					p.posz += (DangerZone.server_world.rand.nextFloat()-DangerZone.server_world.rand.nextFloat())*64f;
					Dimensions.DimensionArray[p.dimension].teleportToDimension(p, DangerZone.server_world, p.dimension, (int)p.posx, (int)p.posy, (int)p.posz);
					sendTeleportToPlayer(p.dimension, p.posx, p.posy, p.posz); //Let him know he's been moved!
				}

				//need a place to call home!
				if(p.home_dimension == 0){
					p.home_dimension = p.dimension;
					p.home_x = p.posx;
					p.home_y = p.posy;
					p.home_z = p.posz;
				}

				//System.out.printf("player running...\n");
				DangerZone.server.sendChatToAllExcept(String.format("Player: %s connected.\n", p.myname), p);
				if(!DangerZone.start_client)System.out.printf("Player connect:: %s from %s\n", p.myname, p.toClient.getInetAddress().getHostAddress());

				//fell off the end of the earth?
				if(p.posy < 0){
					p.posy = 0;
					if(p.motiony < 0)p.motiony = - p.motiony;
				}

				lastmovetime = System.currentTimeMillis(); 
				lastd = p.dimension;
				lastx = (int)p.posx;
				lasty = (int)p.posy;
				lastz = (int)p.posz;
				
			}else{
				fatal_error = 1;
			}
		}else{
			fatal_error = 1;
		}
		
		while(DangerZone.gameover == 0 && fatal_error == 0){
			
			if(objectOutput.errorOccurred())break;
			if(objectInput.errorOccurred())break;

			//System.out.printf("Waiting for an int...\n");

			packettype = objectInput.readInt();

			//-------------------------------------------------------------------------------------
			if(packettype == PacketTypes.IDLE){ //Wants a decorated chunk!

				sendIdle();
				continue;
			}
			
			//-------------------------------------------------------------------------------------
			if(packettype == PacketTypes.GETCHUNK){ //Wants a decorated chunk!

				d = objectInput.readInt();
				x = objectInput.readInt();
				y = objectInput.readInt();
				z = objectInput.readInt();
				
				if(x < 0 || z < 0)continue;
				
				//Send chunk request off to the thread that handles them...
				//No reason to clog up and delay the network here!
				Coords cl = new Coords();
				cl.d = d;
				cl.x = x >> 4;
				cl.z = z >> 4;
				chunker.addCoords(cl);
				continue;
			}
			
			//-------------------------------------------------------------------------------------
			if(packettype == PacketTypes.BLOCK){ //Telling us a block changed
				d = objectInput.readInt();
				x = objectInput.readInt();
				y = objectInput.readInt();
				z = objectInput.readInt();
				id = objectInput.readShort();
				meta = objectInput.readShort();
				//System.out.printf("Block changed %d,  %d, %d : %d\n", x>>4, y, z>>4, id);
				if(BreakChecks.canChangeBlock(p, d, x, y, z, id, meta)){
					DangerZone.server_world.setblockandmeta(d, x, y, z, id, meta);
					DangerZone.server.sendBlockToAllExcept(this, d, x, y, z, id, meta);
					if(id != 0){
						p.blocks_placed++;
					}else{
						p.blocks_broken++;
					}
					sendStatsToPlayer();
				}else{
					//not allowed. Send back correct block.
					sendBlockToPlayer(d, x, y, z, DangerZone.server_world.getblock(d, x, y, z), DangerZone.server_world.getblockmeta(d, x, y, z));
				}
				continue;
			}
			
			//-------------------------------------------------------------------------------------
			if(packettype == PacketTypes.HANDLEINVENTORY){ //Inventory requests
				d = objectInput.readInt();
				x = objectInput.readInt();
				y = objectInput.readInt();
				z = objectInput.readInt();
				id = objectInput.readInt();
				p.handleInventory(d, x, y, z, id);
				lastmovetime = System.currentTimeMillis(); 
				continue;
			}
			
			//-------------------------------------------------------------------------------------
			if(packettype == PacketTypes.BREAKBLOCK){ //Telling us player broke a block
				d = objectInput.readInt();
				x = objectInput.readInt();
				y = objectInput.readInt();
				z = objectInput.readInt();
				//System.out.printf("Block changed %d,  %d, %d : %d\n", x>>4, y, z>>4, id);
				if(BreakChecks.canChangeBlock(p, d, x, y, z, 0, 0)){
					int tbid = DangerZone.server_world.getblock(d, x, y, z);
					DangerZone.server_world.setblockandmeta(d, x, y, z, 0, 0);
					DangerZone.server.sendBlockToAllExcept(this, d, x, y, z, 0, 0);
					Blocks.onBlockBroken(tbid, p, d, x, y, z);
					int dbid;
					int diid;
					int howmany;
					dbid = Blocks.getBlockDrop(tbid, p, DangerZone.server_world, d, x, y, z);
					diid = Blocks.getItemDrop(tbid, p, DangerZone.server_world, d, x, y, z);
					howmany = Blocks.getDropCount(tbid, p, DangerZone.server_world, d, x, y, z);
					DangerZone.server_world.setblockandmeta(d, x, y, z, 0, 0);
					if(howmany > 0){
						for(int i=0;i<howmany;i++){										
							EntityBlockItem eb = (EntityBlockItem)DangerZone.server_world.createEntityByName(DangerZone.blockitemname, d, (double)x+0.5f, (double)y+0.5f, (double)z+0.5f);
							if(eb != null){
								eb.fill(dbid, diid, 1);
								DangerZone.server_world.spawnEntityInWorld(eb);
							}
						}
					}
					ic = p.getHotbar(p.gethotbarindex());
					if(ic != null && ic.iid != 0){
						Item ict = ic.getItem();
						if(ict != null)ict.onBlockBroken(p, d, x, y, z, tbid);
					}
					ToDoList.onBroken(p, d, x, y, z, tbid);
					p.blocks_broken++;
					sendStatsToPlayer();
				}else{
					//not allowed. Send back correct block.
					sendBlockToPlayer(d, x, y, z, DangerZone.server_world.getblock(d, x, y, z), DangerZone.server_world.getblockmeta(d, x, y, z));
				}
				continue;
			}
			
			
						
			//-------------------------------------------------------------------------------------
			if(packettype == PacketTypes.DISCONNECT){
				//bye!
				break;
			}
			//-------------------------------------------------------------------------------------
			if(packettype == PacketTypes.LIGHTINGREQUEST){ //Response to a lighting request
				lightvalue = objectInput.readFloat();
				lightready = true;
				continue;
			}
			
			//-------------------------------------------------------------------------------------
			if(packettype == PacketTypes.TIMERESPONSE){ //Response to a time message
				d = objectInput.readInt(); //fps
				x = objectInput.readInt(); //renderdistance
				client_renderdistance = x;
				long mylongtime = System.currentTimeMillis(); 
				mylongtime -= timechecktime;
				averagetime = ((averagetime*3)+mylongtime)/4;
				averagefps = ((averagefps*3)+d)/4;
				if(DangerZone.show_server_stats){
					System.out.printf("player: %s, averagetime = %d, fps = %d\n", p.myname, averagetime, averagefps);
				}
				continue;
			}
			
			//-------------------------------------------------------------------------------------
			if(packettype == PacketTypes.GAMEMODECHANGE){ //Response to a gamemode change request
				d = objectInput.readInt();
				if((p.player_privs & PlayerPrivs.GAMEMODE) != 0x00 || (p.player_privs & PlayerPrivs.OPERATOR) != 0x00){
					sendNewGameMode(d);
					p.setGameMode(d);
				}
				lastmovetime = System.currentTimeMillis(); 
				continue;
			}
			
			//-------------------------------------------------------------------------------------
			if(packettype == PacketTypes.GAMEDIFFICULTYCHANGE){ //Response to a difficulty change request
				d = objectInput.readInt();
				if((p.player_privs & PlayerPrivs.GAMEMODE) != 0x00 || (p.player_privs & PlayerPrivs.OPERATOR) != 0x00){
					sendNewGameDifficulty(d);
					p.setGameDifficulty(d);
				}
				lastmovetime = System.currentTimeMillis(); 
				continue;
			}

			/*
			//-------------------------------------------------------------------------------------
			if(packettype == PacketTypes.INVENTORYUPDATE){ //Telling us inventory changed , for THIS player only.
				System.out.printf("Illegal packet INVENTORYUPDATE to server\n");
				fatal_error = 1;
				break;
			}
			*/
			/*
			//-------------------------------------------------------------------------------------
			if(packettype == PacketTypes.ENTITYINVENTORYUPDATE){ //Telling us inventory changed for arbitrary entity
				System.out.printf("Illegal packet ENTITYINVENTORYUPDATE to server\n");
				fatal_error = 1;
				break;
			}
			*/
			
			//-------------------------------------------------------------------------------------
			if(packettype == PacketTypes.PLAYERACTION){ //Telling player did something
				which = objectInput.readInt();
				what = objectInput.readInt();
				id = objectInput.readInt();
				x = objectInput.readInt();
				y = objectInput.readInt();
				z = objectInput.readInt();
				iid = objectInput.readInt();
				mx = objectInput.readFloat();
				meta = objectInput.readInt();
				
				if(which == 0){ //Player click
					DangerZone.server.sendPlayerActionToAllExcept(p, which, what);
					if(what == 0)p.leftclick(DangerZone.server_world, x, y, z, iid, id, mx, meta); //Do it! (see if we hit an ENTITY)
					if(what == 1)p.rightclick(DangerZone.server_world, x, y, z, iid, id); //Do it! (see if we hit an ENTITY)
					if(what == 2)p.doEatHeldItem();
					if(what == 3)p.rightclickup(DangerZone.server_world, 0, 0, 0, 0, 0, id);
				}
				lastmovetime = System.currentTimeMillis(); 
				continue;
			}
			
			//------------------------------------------------------------------------------------------
			if(packettype == PacketTypes.CHATMESSAGE){ //Chat!
				String s = (String) objectInput.readString();
				if(s != null && (p.player_privs & PlayerPrivs.CHAT) != 0){
					DangerZone.server.sendChatToAll(s); //got from single client, send out to all
					if(!DangerZone.start_client)System.out.printf("Chat from:: %s: %s\n", p.myname, s);
				}
				lastmovetime = System.currentTimeMillis(); 
				continue;
			}
			
			//------------------------------------------------------------------------------------------
			if(packettype == PacketTypes.PETNAME){ //name a pet
				int whichpet = objectInput.readInt();
				int whichindex = objectInput.readInt();
				String s = (String) objectInput.readString();
				Entity ent = DangerZone.server.entityManager.findEntityByID(whichpet);
				if(ent != null){					
					if(whichindex == 1){
						if(ent.getOwnerName() != null && ent.getOwnerName().equals(p.getPetName())){
							ent.setPetName(s); //its 1 anyway...
						}
					}else{
						if(ent instanceof EntitySign){
							if(BreakChecks.canChangeBlock(p, ent.dimension, (int)ent.posx, (int)ent.posy, (int)ent.posz, 0, 0)){
								ent.setVarString(whichindex, s);
							}
						}
					}					
				}	
				lastmovetime = System.currentTimeMillis(); 
				continue;
			}
			
			//------------------------------------------------------------------------------------------
			if(packettype == PacketTypes.VARINTUPDATE){ //
				int whichpet = objectInput.readInt();
				int whichindex = objectInput.readInt();
				int val = objectInput.readInt();
				Entity ent = DangerZone.server.entityManager.findEntityByID(whichpet);
				if(ent != null){
					if(ent instanceof EntitySign){
						if(BreakChecks.canChangeBlock(p, ent.dimension, (int)ent.posx, (int)ent.posy, (int)ent.posz, 0, 0)){
							ent.setVarInt(whichindex, val);
						}
					}
					if(ent == p){ //me!
						if(whichindex == 12){ //motion!
							p.setVarInt(12,  val);
							//System.out.printf("motion val = 0x%x\n", val);
						}
						if(whichindex == 5){ //setFlying! (motion)
							if((val & 0x400) != 0){
								p.setFlying(true);
								p.flights++;
								p.server_thread.sendStatsToPlayer();
							}else{
								p.setFlying(false);
							}
						}
					}
				}							
				continue;
			}
			
			//------------------------------------------------------------------------------------------
			if(packettype == PacketTypes.KEYEVENTTOENTITY){ //keyboard event
				int whichent = objectInput.readInt();
				int key = objectInput.readInt();
				int val = objectInput.readInt();
				boolean boo = false;
				if(val != 0)boo = true;
				Entity ent = DangerZone.server.entityManager.findEntityByID(whichent);
				if(ent != null){
					ent.addKeyEvent(key, boo);
				}
				lastmovetime = System.currentTimeMillis(); 
				continue;
			}
			
			//------------------------------------------------------------------------------------------
			if(packettype == PacketTypes.MOUSEEVENTTOENTITY){ //keyboard event
				int whichent = objectInput.readInt();
				int key = objectInput.readInt();
				int val = objectInput.readInt();
				boolean boo = false;
				if(val != 0)boo = true;
				Entity ent = DangerZone.server.entityManager.findEntityByID(whichent);
				if(ent != null){
					ent.addMouseEvent(key, boo);
				}	
				lastmovetime = System.currentTimeMillis(); 
				continue;
			}
			
			//------------------------------------------------------------------------------------------
			if(packettype == PacketTypes.COMMANDMESSAGE){ //Chat!
				String s = (String) objectInput.readString();
				//DO COMMAND!	
				if(s != null){
					DangerZone.server.sendCommandToAll(s); //got from single client, send out to all
					if(!DangerZone.start_client)System.out.printf("Command from:: %s: %s\n", p.myname, s);
					CommandHandlers.parseCommand(p, s);	
				}
				lastmovetime = System.currentTimeMillis(); 
				continue;
			}
			
			//------------------------------------------------------------------------------------------
			if(packettype == PacketTypes.SPAWNPARTICLES){ 
				String s = (String) objectInput.readString();
				which = objectInput.readInt(); //really how many!
				d = objectInput.readInt(); //dimension
				px = objectInput.readDouble();
				py = objectInput.readDouble();
				pz = objectInput.readDouble();	
				id = objectInput.readInt();
				DangerZone.server.sendSpawnParticleToAllExcept(p, s, which, d, px, py, pz, id); //got from single client, send out to all except self
				continue;
			}
			
			//------------------------------------------------------------------------------------------
			if(packettype == PacketTypes.PLAYERTELEPORT){ 
				d = objectInput.readInt(); //dimension
				px = objectInput.readDouble();
				py = objectInput.readDouble();
				pz = objectInput.readDouble();	
				if((p.player_privs & PlayerPrivs.TELEPORT) != 0 || (p.player_privs & PlayerPrivs.OPERATOR) != 0){	
					if(ServerHooker.player_teleport(p, d, px, py, pz)){
						Utils.doTeleport(p, d, px, py, pz);	
					}
				}
				continue;
			}
			
			//------------------------------------------------------------------------------------------
			if(packettype == PacketTypes.RESPAWN){ 
				
				if(ServerHooker.player_respawned(p)){
					sendNewGameMode(GameModes.SURVIVAL);
					p.setGameMode(GameModes.SURVIVAL);
					sendNewGameDifficulty(0);
					p.setGameDifficulty(0);
					p.setHealth(p.getMaxHealth()/2);
					p.setAir(p.getMaxAir());
					p.setHunger(p.getMaxHunger());
					p.posx += (DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat())*64f;
					p.posz += (DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat())*64f;				
					Utils.doTeleport(p, p.dimension, p.posx, p.posy, p.posz);	
				}
				continue;
			}
			
			//------------------------------------------------------------------------------------------
			if(packettype == PacketTypes.PLAYERDIEDCLIENT){ 
				p.deadflag = false;
				p.setHealth(0.01f);
				p.setGameMode(GameModes.LIMBO);
				p.doDeathDrops();
				p.onDeath();
				sendStartDeathGUI();		
				continue;
			}
			
			//-------------------------------------------------------------------------------------
			//SHOULD ONLY BE RECEIVING *THIS* PLAYER...
			if(packettype == PacketTypes.ENTITYUPDATE){ //Yay!
				objectInput.readInt(); //throw away - because it should ONLY BE ME!
				
				d = objectInput.readInt();		
				px = objectInput.readDouble();
				py = objectInput.readDouble();
				pz = objectInput.readDouble();
				mx = objectInput.readFloat();
				my = objectInput.readFloat();
				mz = objectInput.readFloat();
				double dist = 9999.0;
				if(d == p.dimension){
					dist = Math.sqrt(((px-p.posx)*(px-p.posx))+((py-p.posy)*(py-p.posy))+((pz-p.posz)*(pz-p.posz)));
				}
				if(dist < 256 || (p.player_privs & PlayerPrivs.TELEPORT) != 0 || (p.player_privs & PlayerPrivs.OPERATOR) != 0){
					p.dimension = d;		
					p.posx = px;
					p.posy = py;
					p.posz = pz;
					p.motionx = mx;
					p.motiony = my;
					p.motionz = mz;
				}else{
					p.motionx = 0;
					p.motiony = 0;
					p.motionz = 0;
					sendPositionAndVelocityUpdateToPlayer(p);
				}

				p.rotation_pitch = objectInput.readFloat();
				p.rotation_yaw = objectInput.readFloat();
				p.rotation_roll = objectInput.readFloat();
				p.rotation_pitch_head = objectInput.readFloat();
				p.rotation_yaw_head = objectInput.readFloat();
				p.rotation_roll_head = objectInput.readFloat();
				p.rotation_pitch_motion = objectInput.readFloat();
				p.rotation_yaw_motion = objectInput.readFloat();
				p.rotation_roll_motion = objectInput.readFloat();
				d = objectInput.readInt();
				if(d != 0){
					p.deadflag = true;
				}else{
					p.deadflag = false;
				}
				p.sethotbarindex(objectInput.readInt());
				//NO vars from the client. Server MUST control all of them, else massive cheating will ensue...
				d = objectInput.readInt();
				if(d != -3){
					System.out.printf("fatal error on player update from client\n");
					fatal_error = 1;
				}
				
				p.stray_entity_ticker = 0; //let the update thread know this PLAYER is OK
				
				//idle checker!
				long mylongtime = System.currentTimeMillis(); 
				if(lastd != p.dimension || lastx != (int)p.posx || lasty != (int)p.posy || lastz != (int)p.posz){
					lastmovetime = mylongtime;
					lastd = p.dimension;
					lastx = (int)p.posx;
					lasty = (int)p.posy;
					lastz = (int)p.posz;
				}
				if(!DangerZone.start_client && (mylongtime - lastmovetime) > 15*60*1000){ //can't be afk for more than 15 minutes on a SERVER!
					fatal_error = 1; //see ya!
				}
								
				save_ticker++;
				if(save_player && save_ticker > 700){ //about once a minute - player updates at around 11 times a second
					ServerHooker.player_save(p);
					save_ticker = 0;
				}

				continue;
			}
			
			/*
			//-------------------------------------------------------------------------------------
			if(packettype == PacketTypes.SPAWNENTITY){ //	
				
				name = (String) objectInput.readString();
				pd = objectInput.readInt();
				px = objectInput.readDouble();
				py = objectInput.readDouble();
				pz = objectInput.readDouble();					
				pitch = objectInput.readFloat();
				yaw = objectInput.readFloat();
				roll = objectInput.readFloat();
				mx = objectInput.readFloat();
				my = objectInput.readFloat();
				mz = objectInput.readFloat();
				Entity e = doSpawnEntity(name, pd, px, py, pz, pitch, yaw, roll, mx, my, mz);
				if(e != null){
					readVarsIntoEntity(e);
					e.init();
					if(DangerZone.server.entityManager.addEntity(e) > 0){
						DangerZone.server.sendSpawnEntityToAll(e);
					}
				}else{
					readVarsIntoNull();
				}
				
				System.out.printf("Spawn entity not allowed from client!\n");
				fatal_error = 1;
				break;
			}
			*/
			
			//-------------------------------------------------------------------------------------
			if(packettype == PacketTypes.PLAYSOUND){ //						
				name = (String) objectInput.readString();					
				pd = objectInput.readInt();
				px = objectInput.readDouble();
				py = objectInput.readDouble();
				pz = objectInput.readDouble();					
				vol = objectInput.readFloat();
				freq = objectInput.readFloat();
				DangerZone.server.sendSoundToAllExcept(p, name, pd, px, py, pz, vol, freq); //client will play his own!		
				continue;
			}
			
			//-------------------------------------------------------------------------------------
			if(packettype == PacketTypes.WHATISTHISENTITY){ //								
				id = objectInput.readInt(); //Client is asking what this is!				
				Entity e = DangerZone.server.entityManager.findEntityByID(id);
				if(e != null){
					sendSpawnEntityToPlayer(e); //Respond with a spawn command.
					if(e instanceof Player){
						sendSkinToPlayer(e);
					}
				}
				continue;
			}				
			
			//-------------------------------------------------------------------------------------
			if(packettype == PacketTypes.KILLME){ //	
				id = objectInput.readInt(); //Kill this thing...	
				Entity e = DangerZone.server.entityManager.findEntityByID(id);
				if(e != null){
					if(e instanceof Ghost || e instanceof GhostSkelly){ //only these can terminate self from client, because light levels are there...
						int ieid = DangerZone.server.entityManager.removeEntityByID(id);
						e.deadflag = true;
						if(ieid == id){
							DangerZone.server.sendEntityDeathToAll(e);
						}
					}
				}
				continue;
			}
			
			//-------------------------------------------------------------------------------------
			if(packettype == PacketTypes.COLORINGBLOCK){ //			
				iid = objectInput.readInt(); //really bid
				d = objectInput.readInt();
				x = objectInput.readInt();
				y = objectInput.readInt();
				z = objectInput.readInt();
				float colordata[][][] = new float[16][16][4];
				for(int i=0;i<16;i++){
					for(int j=0;j<16;j++){
						for(int k=0;k<4;k++){
							colordata[i][j][k] = objectInput.readFloat();
						}
					}
				}
				doSaveColoringBlock(iid, d, x, y, z, colordata);	
				lastmovetime = System.currentTimeMillis(); 
				p.blocks_colored++;
				sendStatsToPlayer();
				continue;
			}
			
			//-------------------------------------------------------------------------------------
			//hopefully it's a custom packet!
			CustomPackets.messageFromClient(packettype, this.p, objectInput);
			
		}
		
		//let the chunker know we are leaving!
		fatal_error = 1;
		if(!DangerZone.start_client)System.out.printf("Player DISconnect:: %s from %s\n", p.myname, p.toClient.getInetAddress().getHostAddress());
		
		if(save_player)ServerHooker.player_save(p);
		
		DangerZone.server.sendEntityRemoveToAllExcept(this.p, this.p);
		DangerZone.server.entityManager.removeEntityByID(this.p.entityID);		
		DangerZone.server.removeMe(this);
		
		//System.out.printf("Player being removed!\n");		
		
		if(objectInput != null)objectInput.close();
		if(objectOutput != null)objectOutput.close();

		try {
			p.toClient.close();
		} catch (IOException e) {

		}
		
		ServerHooker.player_logged_out(p);
		
	}
	
	private boolean checkReady(){
		while(fatal_error == 0 && DangerZone.gameover == 0){
			if(ready != 0)return true;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
				fatal_error = 1;
			}
		}
		return false;
	}
	
	public void sendSpawnEntityToPlayer(Entity e){
		if(!checkReady())return;
		
		if(p.getDistanceFromEntityCenter(e.posx, e.posy, e.posz) > client_renderdistance*16)return; //too far!
		
		DangerZone.packets_per_second++;
		lock.lock();	
		int pt = PacketTypes.SPAWNENTITY;
		int i;
		objectOutput.writeInt(pt);
		objectOutput.writeString(e.uniquename);
		objectOutput.writeInt(e.entityID);
		objectOutput.writeInt(e.dimension);
		objectOutput.writeDouble(e.posx);
		objectOutput.writeDouble(e.posy);
		objectOutput.writeDouble(e.posz);
		objectOutput.writeFloat(e.rotation_pitch);
		objectOutput.writeFloat(e.rotation_yaw);
		objectOutput.writeFloat(e.rotation_roll);
		objectOutput.writeFloat(e.rotation_pitch_head);
		objectOutput.writeFloat(e.rotation_yaw_head);
		objectOutput.writeFloat(e.rotation_roll_head);
		objectOutput.writeFloat(e.motionx);
		objectOutput.writeFloat(e.motiony);
		objectOutput.writeFloat(e.motionz);
		
		//bang out ints
		for(i=0;i<e.maxvars;i++){
			if(e.entity_ints[i] != 0){
				objectOutput.writeInt(i);
				objectOutput.writeInt(e.entity_ints[i]);						
			}
		}
		pt = -1; //packet separator
		objectOutput.writeInt(pt);

		//bang out floats
		for(i=0;i<e.maxvars;i++){
			if(e.entity_floats[i] != 0){
				objectOutput.writeInt(i);
				objectOutput.writeFloat(e.entity_floats[i]);						
			}
		}
		pt = -1; //packet separator
		objectOutput.writeInt(pt);

		//bang out strings
		for(i=0;i<e.maxvars;i++){
			if(e.entity_strings[i] != null){
				objectOutput.writeInt(i);
				objectOutput.writeString(e.entity_strings[i]);						
			}
		}
		pt = -1; //packet separator
		objectOutput.writeInt(pt);	
		
		InventoryContainer ic = null;
		for(i=0;i<e.maxinv;i++){
			ic = e.getVarInventory(i);			
			if(ic != null && ic.count > 0){
				objectOutput.writeInt(i);
				objectOutput.writeInt(ic.count);
				objectOutput.writeInt(ic.bid);
				objectOutput.writeInt(ic.iid);
				objectOutput.writeInt(ic.currentuses);
				objectOutput.writeString(ic.icmeta);
				if(ic.count == 1 && ic.bid == 0 && ic.iid != 0){
					int atcount = 0;
					if(ic.attributes != null)atcount = ic.attributes.size();
					objectOutput.writeInt(atcount);
					if(atcount > 0){
						ItemAttribute ia = null;
						for(int k=0;k<atcount;k++){
							ia = ic.attributes.get(k);
							objectOutput.writeInt(ia.type);
							objectOutput.writeInt(ia.value);
						}
					}
				}
				if(ic.moreInventory != null && ic.moreInventory.length > 0){
					//System.out.printf("start entity update\n");
					int mil = ic.moreInventory.length;
					int mix;
					objectOutput.writeInt(mil);
					for(mix=0;mix<mil;mix++){
						objectOutput.writeInt(mix);
						sendInventoryContainer(ic.moreInventory[mix]);
					}
				}else{
					pt = 0;
					objectOutput.writeInt(pt);
				}
			}
		}				
		pt = -1; //packet separator
		objectOutput.writeInt(pt);
		
		pt = -2; //packet terminator!
		objectOutput.writeInt(pt);
		
		if(e.effect_list != null){
			Effects ef = null;
			for(i=0;i<e.effect_list.size();i++){
				ef = e.effect_list.get(i);
				if(ef != null){
					objectOutput.writeInt(ef.effect);
					objectOutput.writeFloat(ef.amplitude);
					objectOutput.writeInt(ef.duration);
					objectOutput.writeInt(ef.duration_counter);
				}				
			}
		}
		
		pt = -3; //packet terminator!
		objectOutput.writeInt(pt);
		
		lock.unlock();
	}
	
	public void sendChunkToPlayer(Chunk c){
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		DangerZone.chunks_per_second++;
		//System.out.printf("Sending Chunk %d, %d\n", c.chunkX, c.chunkZ);
		int pt = PacketTypes.CHUNK;
		short curval;
		short curcount;
		int indx;
		int separator = -1;
		int i;
		
		lock.lock();
		objectOutput.writeInt(pt); //packet type
		if(c.ownernames == null){
			objectOutput.writeInt(0);
		}else{
			int numowners = c.ownernames.size();
			objectOutput.writeInt(numowners);
			i = 0;
			while(numowners > 0){
				objectOutput.writeString(c.ownernames.get(i));
				numowners--;
				i++;
			}
		}		
		objectOutput.writeInt(c.chunkX);
		objectOutput.writeInt(c.chunkZ);
		objectOutput.writeInt(c.dimension);
		objectOutput.writeInt(c.isDecorated);
		objectOutput.writeInt(c.isChanged);
		objectOutput.writeInt(c.isValid);
		objectOutput.writeInt(c.must_be_written);
		objectOutput.writeFloat(c.b_red);
		objectOutput.writeFloat(c.b_green);
		objectOutput.writeFloat(c.b_blue);
		
		objectOutput.writeInt(separator);
		
		//switch to SHORT separators.
		
		for(i=0;i<256;i++){
			if(c.blockdata[i] != null){
				objectOutput.writeShort((short) i); //current index
				
				curval = c.blockdata[i][0];
				curcount = 0;
				for(indx=0;indx<256;indx++){
					if(c.blockdata[i][indx] == curval){
						curcount++;
					}else{
						objectOutput.writeShort(curcount);
						objectOutput.writeShort(curval);
						curcount = 1;
						curval = c.blockdata[i][indx];
					}						
				}					
				objectOutput.writeShort(curcount);
				objectOutput.writeShort(curval);
				
			}
		}
		
		objectOutput.writeShort((short) separator);
		
		//these tend to have very different values, so RLE like block data doesn't work so well.
		for(i=0;i<256;i++){
			if(c.metadata[i] != null){
				objectOutput.writeShort((short) i);
				objectOutput.writeShortArray(c.metadata[i], c.metadata[i].length);
			}
		}
		
		objectOutput.writeShort((short) separator);

		lock.unlock();
	}
	
	public void sendStatsToPlayer(){
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		lock.lock();
		
		int pt = PacketTypes.PLAYERSTATS;
		objectOutput.writeInt(pt);
		objectOutput.writeInt(p.kills);
		objectOutput.writeInt(p.deaths);
		objectOutput.writeDouble(p.damage_taken);
		objectOutput.writeDouble(p.damage_dealt);
		objectOutput.writeInt(p.blocks_broken);
		objectOutput.writeInt(p.blocks_placed);
		objectOutput.writeInt(p.blocks_colored);
		objectOutput.writeInt(p.crafted);
		objectOutput.writeInt(p.bought);
		objectOutput.writeInt(p.sold);
		objectOutput.writeInt(p.broken);
		objectOutput.writeInt(p.traveled);
		objectOutput.writeInt(p.morphs);
		objectOutput.writeInt(p.teleports);
		objectOutput.writeInt(p.eaten);
		objectOutput.writeInt(p.roachstomps);
		objectOutput.writeInt(p.hard_landings);
		objectOutput.writeInt(p.flights);
		objectOutput.writeInt(p.spells);

		lock.unlock();
	}
	

	public void sendBlockToPlayer(int d, int x, int y, int z, int id, int meta){
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		lock.lock();
		//System.out.printf("Sending block in %d, %d\n", x>>4, z>>4);
		
		int pt = PacketTypes.BLOCK;
		objectOutput.writeInt(pt);
		objectOutput.writeInt(d);
		objectOutput.writeInt(x);
		objectOutput.writeInt(y);
		objectOutput.writeInt(z);
		objectOutput.writeShort((short) id);
		objectOutput.writeShort((short) meta);

		lock.unlock();
	}
	
	public void sendChunkMetaToPlayer(Chunk chnk){
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		lock.lock();
		//System.out.printf("Sending chunk meta update\n");		
		int pt = PacketTypes.CHUNKMETAUPDATE;
		objectOutput.writeInt(pt);
		objectOutput.writeInt(chnk.dimension);
		objectOutput.writeInt(chnk.chunkX);
		objectOutput.writeInt(chnk.chunkZ);
		if(chnk.ownernames == null){
			objectOutput.writeInt(0);
		}else{
			int numowners = chnk.ownernames.size();
			objectOutput.writeInt(numowners);
			int i = 0;
			while(numowners > 0){
				objectOutput.writeString(chnk.ownernames.get(i));
				numowners--;
				i++;
			}
		}	
		lock.unlock();
	}
	public void sendEntityDeathToPlayer(int eid){
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		lock.lock();
		int pt = PacketTypes.ENTITYDEATH;
		objectOutput.writeInt(pt);
		objectOutput.writeInt(eid);
		lock.unlock();
	}
	
	public void sendChatToPlayer(String s){
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		lock.lock();
		int pt = PacketTypes.CHATMESSAGE;
		objectOutput.writeInt(pt);
		objectOutput.writeString(s);
		flushSendLocked();
		lock.unlock();
	}
	
	public void sendIdle(){
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		lock.lock();
		int pt = PacketTypes.IDLE;
		objectOutput.writeInt(pt);
		flushSendLocked();
		lock.unlock();
	}
	
	public void sendSongToPlayer(String s){
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		lock.lock();
		int pt = PacketTypes.PLAYSONG;
		objectOutput.writeInt(pt);
		objectOutput.writeString(s);
		flushSendLocked();
		lock.unlock();
	}
	
	public void sendCommandToPlayer(String s){
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		lock.lock();
		int pt = PacketTypes.COMMANDMESSAGE;
		objectOutput.writeInt(pt);
		objectOutput.writeString(s);
		flushSendLocked();
		lock.unlock();
	}
	
	public void sendSpawnParticleToPlayer(String s, int hm, int d, double x, double y, double z){
		sendSpawnParticleToPlayer( s,  hm,  d,  x,  y,  z, 0);
	}
	public void sendSpawnParticleToPlayer(String s, int hm, int d, double x, double y, double z, int bid){
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		
		if(averagetime > 250){			
			if(DangerZone.rand.nextBoolean())return;
			if(averagetime > 500)if(DangerZone.rand.nextBoolean())return;
			if(averagetime > 1000)if(DangerZone.rand.nextBoolean())return;
			if(averagetime > 2000)return;
		}
		
		if(p.getDistanceFromEntityCenter(x, y, z) > client_renderdistance*16)return; //too far!
		
		lock.lock();

		int pt = PacketTypes.SPAWNPARTICLES;
		objectOutput.writeInt(pt);
		objectOutput.writeString(s);
		objectOutput.writeInt(hm);
		objectOutput.writeInt(d);
		objectOutput.writeDouble(x);
		objectOutput.writeDouble(y);
		objectOutput.writeDouble(z);
		objectOutput.writeInt(bid);
		//let them stack together a little....   //flushSendLocked();
		lock.unlock();
	}
	
	public void sendVelocityUpdateToPlayer(float newx, float newy, float newz){
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		lock.lock();
		int pt = PacketTypes.PLAYERVELOCITY;
		objectOutput.writeInt(pt);
		objectOutput.writeFloat(newx);
		objectOutput.writeFloat(newy);
		objectOutput.writeFloat(newz);
		lock.unlock();
	}
	
	public void sendPositionAndVelocityUpdateToPlayer(Player p){
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		lock.lock();
		int pt = PacketTypes.PLAYERPOSITIONVELOCITY;
		objectOutput.writeInt(pt);
		objectOutput.writeDouble(p.posx);
		objectOutput.writeDouble(p.posy);
		objectOutput.writeDouble(p.posz);
		objectOutput.writeFloat(p.motionx);
		objectOutput.writeFloat(p.motiony);
		objectOutput.writeFloat(p.motionz);
		objectOutput.writeFloat(p.rotation_yaw);
		objectOutput.writeFloat(p.rotation_yaw_head);
		lock.unlock();
	}
	
	public void sendTeleportToPlayer(int dim, double newx, double newy, double newz){
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		lock.lock();
		int pt = PacketTypes.PLAYERTELEPORT;
		objectOutput.writeInt(pt);
		objectOutput.writeInt(dim);
		objectOutput.writeDouble(newx);
		objectOutput.writeDouble(newy);
		objectOutput.writeDouble(newz);
		flushSendLocked();
		lock.unlock();
	}
	
	public void sendEntityRemoveToPlayer(int eid){
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		lock.lock();
		int pt = PacketTypes.ENTITYREMOVE;
		objectOutput.writeInt(pt);
		objectOutput.writeInt(eid);
		lock.unlock();
	}
	
	public float doLightingRequest(int d, int x, int y, int z){
		if(!checkReady())return 0;
		DangerZone.packets_per_second++;
		//System.out.printf("trying for light\n");
		lightinglock.lock();
		lightready = false;
		lightvalue = DangerZone.server_world.rand.nextFloat();
		lock.lock();
		int pt = PacketTypes.LIGHTINGREQUEST;
		objectOutput.writeInt(pt);
		objectOutput.writeInt(d);
		objectOutput.writeInt(x);
		objectOutput.writeInt(y);
		objectOutput.writeInt(z);
		lock.unlock();
		
		int tries = 100; //Don't try too hard!!!
		while(tries > 0 && !lightready){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tries--;
		}
		//System.out.printf("got %f after %d tries\n",  lightvalue, 2000-tries);
		
		//these don't happen very frequently, or I'd have to do something less kludgy...
		float lv = lightvalue;		
		lightready = false; //done!
		lightinglock.unlock();
		return lv;
	}
	
	public void sendEntityHitToPlayer(int eid, float newhealth){
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		lock.lock();
		int pt = PacketTypes.ENTITYHIT;
		objectOutput.writeInt(pt);
		objectOutput.writeInt(eid);
		objectOutput.writeFloat(newhealth);
		lock.unlock();
	}
	
	public void sendPlayerAction(Player p, int which, int what){
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		lock.lock();		
		int pt = PacketTypes.PLAYERACTION;
		objectOutput.writeInt(pt);
		objectOutput.writeInt(p.entityID);
		objectOutput.writeInt(which);
		objectOutput.writeInt(what);
		lock.unlock();
	}
	
	public void sendNewEffect(Effects ef){
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		lock.lock();		
		int pt = PacketTypes.NEWEFFECT;
		objectOutput.writeInt(pt);
		objectOutput.writeInt(ef.effect);
		objectOutput.writeInt(ef.duration);
		objectOutput.writeFloat(ef.amplitude);
		lock.unlock();
	}
	
	public void sendVarIntUpdate(int which, int val){
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		lock.lock();		
		int pt = PacketTypes.VARINTUPDATE;
		objectOutput.writeInt(pt);
		objectOutput.writeInt(which);
		objectOutput.writeInt(val);
		lock.unlock();
	}
	
	public void sendVarFloatUpdate(int which, float val){
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		lock.lock();		
		int pt = PacketTypes.VARFLOATUPDATE;
		objectOutput.writeInt(pt);
		objectOutput.writeInt(which);
		objectOutput.writeFloat(val);
		lock.unlock();
	}
	
	public void sendVarStringUpdate(int which, String val){
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		lock.lock();		
		int pt = PacketTypes.VARSTRINGUPDATE;
		objectOutput.writeInt(pt);
		objectOutput.writeInt(which);
		objectOutput.writeString(val);
		lock.unlock();
	}
	
	public void sendMountCommand(int which){
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		lock.lock();		
		int pt = PacketTypes.MOUNTENTITY;
		objectOutput.writeInt(pt);
		objectOutput.writeInt(which);
		lock.unlock();
	}
	
	public void sendEntityUpdateToPlayer(Entity e){
		if(!checkReady())return;
		if(e.entityID == p.entityID && p.changed == 0)return; //Don't send movement changes to self... ignored anyway...
				
		//start getting harsh!
		if(averagetime > 250){
			if(e instanceof EntityBlockItem || e instanceof EntityExp || e instanceof EntityFire || e instanceof EntityBlock){					
				if(DangerZone.rand.nextBoolean())return;
				if(averagetime > 500)if(DangerZone.rand.nextBoolean())return;
				if(averagetime > 750)if(DangerZone.rand.nextBoolean())return;
				if(averagetime > 1000)if(DangerZone.rand.nextBoolean())return;
				if(averagetime > 1500)return;
			}else{
				if(!(e instanceof Player)){
					if(e.changed == 0){ //can ignore non-player movement sometimes...
						if(averagetime > 500)if(DangerZone.rand.nextBoolean())return;
						if(averagetime > 1000)if(DangerZone.rand.nextBoolean())return;
					}
				}
			}
		}
		
		//if(e.uniquename.equals("OreSpawn:Vermin")){
		//	System.out.printf("sending %s to %d, %f, %f, %f\n", e.uniquename, e.dimension, e.posx, e.posy, e.posz);
		//}
		
		if(p.getDistanceFromEntityCenter(e.posx, e.posy, e.posz) > client_renderdistance*16)return; //too far!
		
		DangerZone.packets_per_second++;
		lock.lock();
						
		int pt = PacketTypes.ENTITYUPDATE;
		int i;
		objectOutput.writeInt(pt);
		objectOutput.writeInt(e.entityID);
		objectOutput.writeInt(e.dimension);
		objectOutput.writeDouble(e.posx);
		objectOutput.writeDouble(e.posy);
		objectOutput.writeDouble(e.posz);
		objectOutput.writeFloat(e.motionx);
		objectOutput.writeFloat(e.motiony);
		objectOutput.writeFloat(e.motionz);
		objectOutput.writeFloat(e.rotation_pitch);
		objectOutput.writeFloat(e.rotation_yaw);
		objectOutput.writeFloat(e.rotation_roll);
		objectOutput.writeFloat(e.rotation_pitch_head);
		objectOutput.writeFloat(e.rotation_yaw_head);
		objectOutput.writeFloat(e.rotation_roll_head);
		objectOutput.writeFloat(e.rotation_pitch_motion);
		objectOutput.writeFloat(e.rotation_yaw_motion);
		objectOutput.writeFloat(e.rotation_roll_motion);
		if(e.deadflag){
			pt = 1;
			objectOutput.writeInt(pt);
		}else{
			pt = 0;
			objectOutput.writeInt(pt);
		}
		
		//The *ToAll* routine will clear the changed flags when it is done.
		if(e.changed != 0){
			//bang out ints that changed
			for(i=0;i<e.maxvars;i++){
				if((e.changes[i]&0x01) == 0x01){
					objectOutput.writeInt(i);
					objectOutput.writeInt(e.entity_ints[i]);
					//if(i == 3)System.out.printf("%s changed %d to %d\n",  e.uniquename, i, e.entity_ints[i]);
				}
			}
			pt = -1; //packet separator
			objectOutput.writeInt(pt);
			
			//bang out floats that changed
			for(i=0;i<e.maxvars;i++){
				if((e.changes[i]&0x02) == 0x02){
					objectOutput.writeInt(i);
					objectOutput.writeFloat(e.entity_floats[i]);						
				}
			}
			pt = -1; //packet separator
			objectOutput.writeInt(pt);
			
			//bang out strings that changed
			for(i=0;i<e.maxvars;i++){
				if((e.changes[i]&0x04) == 0x04){
					objectOutput.writeInt(i);
					objectOutput.writeString(e.entity_strings[i]);						
				}
			}
			pt = -1; //packet separator
			objectOutput.writeInt(pt);
			
			sendChangedInventory(e);
						
		}
		
		pt = -2; //packet terminator!
		objectOutput.writeInt(pt);
		
		if(e.effect_list != null){
			Effects ef = null;
			for(i=0;i<e.effect_list.size();i++){
				ef = e.effect_list.get(i);
				if(ef != null && ef.effect > 0){
					objectOutput.writeInt(ef.effect);
					objectOutput.writeFloat(ef.amplitude);
					objectOutput.writeInt(ef.duration);
					objectOutput.writeInt(ef.duration_counter);
				}				
			}
		}
		
		pt = -3; //packet terminator!
		objectOutput.writeInt(pt);
		
		
		lock.unlock();

	}

	public void sendChangedInventory(Entity e){
		InventoryContainer ic = null;
		int i, pt;
		for(i=0;i<e.maxinv;i++){
			if((e.changes[i]&0x08) == 0x08){
				objectOutput.writeInt(i);
				ic = e.getVarInventory(i);
				if(ic != null && ic.count > 0){
					objectOutput.writeInt(ic.count);
					objectOutput.writeInt(ic.bid);
					objectOutput.writeInt(ic.iid);
					objectOutput.writeInt(ic.currentuses);
					objectOutput.writeString(ic.icmeta);
					if(ic.count == 1 && ic.bid == 0 && ic.iid != 0){
						int atcount = 0;
						if(ic.attributes != null)atcount = ic.attributes.size();
						objectOutput.writeInt(atcount);
						if(atcount > 0){
							ItemAttribute ia = null;
							for(int k=0;k<atcount;k++){
								ia = ic.attributes.get(k);
								objectOutput.writeInt(ia.type);
								objectOutput.writeInt(ia.value);
							}
						}
					}
					if(ic.moreInventory != null && ic.moreInventory.length > 0){
						//System.out.printf("start changed inventory\n");
						//ic.validate();
						int mil = ic.moreInventory.length;
						int mix;
						objectOutput.writeInt(mil);
						for(mix=0;mix<mil;mix++){
							objectOutput.writeInt(mix);
							sendInventoryContainer(ic.moreInventory[mix]);
						}
					}else{
						pt = 0;
						objectOutput.writeInt(pt);
					}
				}else{
					pt = 0;
					objectOutput.writeInt(pt);
				}
			}
		}				
		pt = -1; //packet separator
		objectOutput.writeInt(pt);
	}
	
	public void sendInventoryContainer(InventoryContainer ic){
		if(ic == null){
			//System.out.printf("ic = null\n");
			int nill = 0;
			objectOutput.writeInt(nill);
			return;
		}
		objectOutput.writeInt(ic.count);
		if(ic.count <= 0)return;
		objectOutput.writeInt(ic.bid);
		objectOutput.writeInt(ic.iid);
		objectOutput.writeInt(ic.currentuses);
		objectOutput.writeString(ic.icmeta);
		if(ic.count == 1 && ic.bid == 0 && ic.iid != 0){
			int atcount = 0;
			if(ic.attributes != null)atcount = ic.attributes.size();
			objectOutput.writeInt(atcount);
			if(atcount > 0){
				ItemAttribute ia = null;
				for(int k=0;k<atcount;k++){
					ia = ic.attributes.get(k);
					objectOutput.writeInt(ia.type);
					objectOutput.writeInt(ia.value);
				}
			}
		}
		int mix = 0;
		if(ic.moreInventory != null){
			int mil = ic.moreInventory.length;
			objectOutput.writeInt(mil);
			//System.out.printf("ic moreinventory length = %d\n", mil);
			for(mix=0;mix<mil;mix++){
				//System.out.printf("ic write mix = %d\n", mix);
				objectOutput.writeInt(mix);
				sendInventoryContainer(ic.moreInventory[mix]);
				//System.out.printf("ic write returned mix = %d\n", mix);
			}
		}else{
			//System.out.printf("ic moreinventory = null\n");
			objectOutput.writeInt(mix);
		}
	}
	
	//Server just read/created player. Send complete info back to them.
	public void sendPlayerToPlayer(Player e){

		int pt = 0;
		
		
		objectOutput.writeInt(e.dimension);
		objectOutput.writeDouble(e.posx);
		objectOutput.writeDouble(e.posy);
		objectOutput.writeDouble(e.posz);
		objectOutput.writeFloat(e.motionx);
		objectOutput.writeFloat(e.motiony);
		objectOutput.writeFloat(e.motionz);
		objectOutput.writeFloat(e.rotation_pitch);
		objectOutput.writeFloat(e.rotation_yaw);
		objectOutput.writeFloat(e.rotation_roll);
		objectOutput.writeFloat(e.rotation_pitch_head);
		objectOutput.writeFloat(e.rotation_yaw_head);
		objectOutput.writeFloat(e.rotation_roll_head);
		objectOutput.writeFloat(e.rotation_pitch_motion);
		objectOutput.writeFloat(e.rotation_yaw_motion);
		objectOutput.writeFloat(e.rotation_roll_motion);

		if(e.deadflag){
			pt = 1;
			objectOutput.writeInt(pt);
		}else{
			pt = 0;
			objectOutput.writeInt(pt);
		}
		
		//if(e.changed != 0){
			//bang out ints that changed
			for(int i=0;i<e.maxvars;i++){
				//if((e.changes[i]&0x01) == 0x01){
					objectOutput.writeInt(i);
					objectOutput.writeInt(e.entity_ints[i]);						
				//}
			}
			pt = -1; //packet separator
			objectOutput.writeInt(pt);
			
			//bang out floats that changed
			for(int i=0;i<e.maxvars;i++){
				//if((e.changes[i]&0x02) == 0x02){
					objectOutput.writeInt(i);
					objectOutput.writeFloat(e.entity_floats[i]);						
				//}
			}
			pt = -1; //packet separator
			objectOutput.writeInt(pt);
			
			//bang out strings that changed
			for(int i=0;i<e.maxvars;i++){
				//if((e.changes[i]&0x04) == 0x04){
					objectOutput.writeInt(i);
					objectOutput.writeString(e.entity_strings[i]);						
				//}
			}
			pt = -1; //packet separator
			objectOutput.writeInt(pt);
			
			InventoryContainer ic = null;
			for(int i=0;i<e.maxinv;i++){
				//if((e.changes[i]&0x08) == 0x08){
					objectOutput.writeInt(i);
					ic = e.getVarInventory(i);
					if(ic != null && ic.count > 0){
						objectOutput.writeInt(ic.count);
						objectOutput.writeInt(ic.bid);
						objectOutput.writeInt(ic.iid);
						objectOutput.writeInt(ic.currentuses);
						objectOutput.writeString(ic.icmeta);
						if(ic.count == 1 && ic.bid == 0 && ic.iid != 0){
							int atcount = 0;
							if(ic.attributes != null)atcount = ic.attributes.size();
							objectOutput.writeInt(atcount);
							if(atcount > 0){
								ItemAttribute ia = null;
								for(int k=0;k<atcount;k++){
									ia = ic.attributes.get(k);
									objectOutput.writeInt(ia.type);
									objectOutput.writeInt(ia.value);
								}
							}
						}
						if(ic.moreInventory != null && ic.moreInventory.length > 0){
							//System.out.printf("start player to player\n");
							//ic.validate();
							int mil = ic.moreInventory.length;
							int mix;
							objectOutput.writeInt(mil);
							for(mix=0;mix<mil;mix++){
								//System.out.printf("pl mix = %d\n", mix);
								objectOutput.writeInt(mix);
								sendInventoryContainer(ic.moreInventory[mix]);
								//System.out.printf("pl returned mix = %d\n", mix);
							}
						}else{
							pt = 0;
							objectOutput.writeInt(pt);
						}
					}else{
						pt = 0;
						objectOutput.writeInt(pt);
					}
				//}
			}				
			pt = -1; //packet separator
			objectOutput.writeInt(pt);
						
		//}
		
		pt = -2; //packet terminator!
		objectOutput.writeInt(pt);
		
		if(e.effect_list != null){
			Effects ef = null;
			for(int i=0;i<e.effect_list.size();i++){
				ef = e.effect_list.get(i);
				if(ef != null){
					objectOutput.writeInt(ef.effect);
					objectOutput.writeFloat(ef.amplitude);
					objectOutput.writeInt(ef.duration);
					objectOutput.writeInt(ef.duration_counter);
				}				
			}
		}
		
		pt = -3; //packet terminator!
		objectOutput.writeInt(pt);

	}
	
	public void sendTimeToPlayer(int t, int l){
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		lock.lock();
		timechecktime = System.currentTimeMillis(); //record when this was sent!
		int pt = PacketTypes.TIME;
		objectOutput.writeInt(pt);
		objectOutput.writeInt(t);
		objectOutput.writeInt(l);
		objectOutput.writeLong(averagetime);
		flushSendLocked();
		lock.unlock();
	}
	
	public void sendNewGameMode(int t){
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		lock.lock();
		int pt = PacketTypes.GAMEMODECHANGE;
		objectOutput.writeInt(pt);
		objectOutput.writeInt(t);
		flushSendLocked();
		lock.unlock();
	}
	
	public void sendNewGameDifficulty(int t){
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		lock.lock();
		int pt = PacketTypes.GAMEDIFFICULTYCHANGE;
		objectOutput.writeInt(pt);
		objectOutput.writeInt(t);
		flushSendLocked();
		lock.unlock();
	}
	
	public void sendStartDeathGUI(){
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		lock.lock();
		int pt = PacketTypes.DEATHGUI;
		objectOutput.writeInt(pt);
		flushSendLocked();
		lock.unlock();
	}
	
	//Called from outside
	public void flushSend(){
		if(!checkReady())return;
		lock.lock();
		if(objectOutput != null)objectOutput.flush();
		lock.unlock();
	}
	
	//internal
	public void flushSendLocked(){
		objectOutput.flush();
	}
	
	
	public Entity doSpawnEntity(String name, int pd, double px, double py, double pz, float pitch, float yaw, float roll, float mx, float my, float mz){
		Entity e = Entities.spawnEntityByName(name, DangerZone.server_world);
		if(e != null){
			e.posx = px;
			e.posy = py;
			e.posz = pz;
			e.rotation_pitch = pitch;
			e.rotation_yaw = yaw;
			e.rotation_roll = roll;
			e.rotation_pitch_head = pitch;
			e.rotation_yaw_head = yaw;
			e.rotation_roll_head = roll;
			e.motionx = mx;
			e.motiony = my;
			e.motionz = mz;
			e.dimension = pd;
			//e.init();
			//if(DangerZone.server.entityManager.addEntity(e) <= 0)return null; //oops! Nevermind!
		}
		return e;
	}


	public void sendSound(String name, int pd, double px, double py, double pz, float vol, float freq){
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		lock.lock();	
		int pt = PacketTypes.PLAYSOUND;
		objectOutput.writeInt(pt);
		objectOutput.writeString(name);
		objectOutput.writeInt(pd);
		objectOutput.writeDouble(px);
		objectOutput.writeDouble(py);
		objectOutput.writeDouble(pz);
		objectOutput.writeFloat(vol);
		objectOutput.writeFloat(freq);
		
		lock.unlock();
	
	}
	
	/*
	 * Makes sure Client agrees with our IDs!
	 */
	public void sendServerIDs(){
		int i, j;

		try {
			for(i=1;i<Items.itemsMAX;i++){
				if(Items.ItemArray[i] != null){
					//System.out.printf("Writing item %d\n", i);
					objectOutput.writeInt(i);
					objectOutput.writeString(Items.ItemArray[i].uniquename);
				}
			}
			i = -1;
			objectOutput.writeInt(i); //separator
			objectOutput.flush();	
			
			for(i=1;i<Blocks.blocksMAX;i++){
				if(Blocks.BlockArray[i] != null){
					//System.out.printf("Writing block %d\n", i);
					objectOutput.writeInt(i);
					objectOutput.writeString(Blocks.BlockArray[i].uniquename);
				}
			}
			i = -2;
			objectOutput.writeInt(i); //separator
			objectOutput.flush();	
			
			for(i=1;i<Dimensions.dimensionsMAX;i++){
				if(Dimensions.DimensionArray[i] != null){
					//System.out.printf("Writing dimension %d\n", i);
					objectOutput.writeInt(i);
					objectOutput.writeString(Dimensions.DimensionArray[i].uniquename);
				}
			}
			i = -3;
			objectOutput.writeInt(i); //separator	
			objectOutput.flush();	
			
			for(i=1;i<CustomPackets.CustomPacketsMAX;i++){
				if(CustomPackets.CustomPacketsArray[i] != null){
					//System.out.printf("Writing custom packet %d\n", i);
					objectOutput.writeInt(i);
					objectOutput.writeString(CustomPackets.CustomPacketsArray[i].uniquename);
				}
			}
			i = -4;
			objectOutput.writeInt(i); //separator	
			objectOutput.flush();	
			
			//Send the coloring blocks!
			String name;
			int bid;
			float colordata[][][] = new float[16][16][4];
			for(bid=1;bid<Blocks.blocksMAX;bid++){
				name = Blocks.getUniqueName(bid);
				if(name != null && name.startsWith("DangerZone:Coloring Block ")){ //notice the space!
					BufferedImage bi = ImageIO.read(new File(Blocks.BlockArray[bid].texturepath));
					if(bi != null){
						int r, g, b, color;
						for(j=0; j<16;j++){
							for(i=0;i<16;i++){
								color = bi.getRGB(i, j);
								r = (color >> 16) & 0xff;
								g = (color >> 8) & 0xff;
								b = (color) & 0xff;								
								colordata[i][15-j][0] = r; 								
								colordata[i][15-j][1] = g; 				
								colordata[i][15-j][2] = b; 
								colordata[i][15-j][3] = 255f;			
							}
						}	
						//System.out.printf("Writing coloring block %d\n", i);
						objectOutput.writeInt(bid); //yes, it gets sent twice.
						sendColoringBlockRaw(name, bid, colordata, false);						
					}
				}				
			}
			
			i = -5;
			objectOutput.writeInt(i); //separator	
			//System.out.printf("Writing done!\n");
			objectOutput.flush();			
		} catch (IOException err) {
			//System.out.printf("Server sendsound send failed.\n");
			fatal_error = 1;
		} 

	}
	
	public void sendModNames(){
		int i = DangerZone.all_the_mods.size();

		objectOutput.writeInt(i); //write number of mod names
		//System.out.printf("writing %d mod names\n", i);
		for(int k=0;k<i;k++){
			String s = DangerZone.all_the_mods.get(k).modname;				
			objectOutput.writeString(s);
			//System.out.printf("Sent mod name %s\n", s);
		}
		
		objectOutput.writeString(DangerZone.worldname);
		objectOutput.flush(); 

	}
	
	private void getSkin(){
		int seq;
		byte b[];
		
		seq = objectInput.readInt();
		if(seq != -3){
			fatal_error = 1;
			return;
		}
		
		b = (byte[]) objectInput.readByteArray();  //FIX ME! I CRASH!!!!!!
		
		//System.out.printf("Got skin len = %d\n", len);
		if(b == null || b.length < 8192){
			fatal_error = 1;
			return;
		}
		p.tdata = b; //got it! save it!!!
	}
	
	private void sendSkinToPlayer(Entity e){
		Player pe = (Player)e;
		
		if(!checkReady())return;
		DangerZone.packets_per_second++;
		lock.lock();	
		objectOutput.writeByteArray(pe.tdata, pe.tdata.length);			
		flushSendLocked(); 
		lock.unlock();
		
		
	}
	
	public NetworkOutputBuffer getOutputStream(){
		if(!checkReady())return null;
		DangerZone.packets_per_second++;		
		lock.lock();
		return objectOutput;
	}
	
	public void releaseOutputStream(){
		//objectOutput.flush();	
		lock.unlock();
	}
	
	public void sendColoringBlock(String name,  int bid, float colordata[][][], boolean check){
		if(check){
			if(!checkReady())return;
		}
		DangerZone.packets_per_second++;
		lock.lock();	
		int pt = PacketTypes.COLORINGBLOCK;
		objectOutput.writeInt(pt);
		sendColoringBlockRaw(name, bid, colordata, check);
		flushSendLocked(); 
		lock.unlock();	
	}
	
	public void sendColoringBlockRaw(String name,  int bid, float colordata[][][], boolean check){
		objectOutput.writeString(name);
		objectOutput.writeInt(bid);
		for(int i=0;i<16;i++){
			for(int j=0;j<16;j++){
				for(int k=0;k<4;k++){
					objectOutput.writeFloat(colordata[i][j][k]);
				}
			}
		}
	}
	
	private void doSaveColoringBlock(int bid, int dim, int px, int py, int pz, float colordata[][][]){
		int newbid = bid;
		String blkname = Blocks.getUniqueName(bid);
		if(blkname == null)return;
		if(!blkname.equals("DangerZone:Coloring Block")){ //existing block!
			//Overwrite existing block. Hopefully a coloring block! Doh!
			String fp = Blocks.BlockArray[bid].texturepath;
			if(fp == null)return;
			File file = new File(fp);	
			int width = 16;
			int height = 16;
			String format = "PNG"; 
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			   
			for(int x = 0; x < width; x++){
			    for(int y = 0; y < height; y++){
			        int r = ((int)colordata[x][y][0]) & 0xFF;
			        int g = ((int)colordata[x][y][1]) & 0xFF;
			        int b = ((int)colordata[x][y][2]) & 0xFF;
			        image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
			    }
			}
			   
			try {
			    ImageIO.write(image, format, file);
			} catch (IOException e) { e.printStackTrace(); }
			image.flush();
			DangerZone.server.sendColoringBlockToAll(blkname, newbid, colordata);
			return;
		}
		//Have to make a new one.
		int index = 0;
		int blkindex = 0;
		boolean ok = false;
		boolean found = false;
		for(index = 0;index < 100;index++){
			blkname = String.format("DangerZone:Coloring Block %2d", index);
			found = false;
			for(blkindex=1;blkindex<Blocks.blocksMAX;blkindex++){
				if(Blocks.BlockArray[blkindex] != null){
					if(blkname.equals(Blocks.getUniqueName(blkindex))){
						found = true;
						break;
					}
				}
			}			
			if(found)continue;
			ok = true;
			for(blkindex=1;blkindex<Blocks.blocksMAX;blkindex++){
				if(Blocks.BlockArray[blkindex] == null)break;
			}
			break;			
		}
		if(!ok)return; //too many!
		//index = color name
		//blkindex = bid
		String fp = String.format("worlds/%s/coloring/coloringblock%2d.png", DangerZone.worldname, index);
		File file = new File(fp);	
		int width = 16;
		int height = 16;
		String format = "PNG"; 
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		file.mkdirs();
		   
		for(int x = 0; x < width; x++){
		    for(int y = 0; y < height; y++){
		        int r = ((int)colordata[x][y][0]) & 0xFF;
		        int g = ((int)colordata[x][y][1]) & 0xFF;
		        int b = ((int)colordata[x][y][2]) & 0xFF;
		        image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
		    }
		}
		   
		try {
		    ImageIO.write(image, format, file);
		} catch (IOException e) { e.printStackTrace(); }
		image.flush();
		
		ColoringBlock cb = new ColoringBlock(blkname, "");
		cb.texturepath = file.getAbsolutePath();
		cb.blockID = blkindex;
		Blocks.BlockArray[blkindex] = cb;		//Hope there wasn't anything there! :)
		Blocks.addthis(blkname, blkindex);
		Blocks.save(); //Save it so we can find it when we start again next time.
		
		DangerZone.server.sendColoringBlockToAll(blkname, blkindex, colordata);
		DangerZone.server_world.setblock(dim, px, py, pz, blkindex);
	}
	
	public boolean verify_playername(Player p, String pln, String pa){

		if(!DangerZone.start_client){
			p.player_privs = DangerZone.default_privs;

			if(DangerZone.require_valid_passwords){
				if(pln == null || pa == null)return false;
				if(pln.length() < 4)return false;
				if(pln.length() > 16)return false;
				if(pa.length() != 32)return false;

				//System.out.printf("verifying playername %s with pwd %s\n", pln, pa);

				Socket nssock;
				ObjectInputStream nsobjectInput = null;
				ObjectOutputStream nsobjectOutput = null;
				BufferedOutputStream nsbufobjectOutput = null;
				BufferedInputStream nsbufobjectInput = null;
				int packettype = 0;
				boolean is_valid = false;

				if(anon_player && DangerZone.allow_anonymous)return true;

				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// Auto-generated catch block
					//e.printStackTrace();
				}

				try {
					//System.out.printf("connecting to %s:%d\n", LauncherMain.nsserver_address, LauncherMain.nsserver_port);
					nssock = new Socket(DangerZone.nsserver_address, DangerZone.nsserver_port);
					nsbufobjectOutput = new BufferedOutputStream(nssock.getOutputStream());
					nsobjectOutput = new ObjectOutputStream(nsbufobjectOutput);
					packettype = -1; //dummy just to connect...
					nsobjectOutput.writeInt(packettype);
					nsobjectOutput.flush();
					nsbufobjectInput = new BufferedInputStream(nssock.getInputStream());
					nsobjectInput = new ObjectInputStream(nsbufobjectInput);
					packettype = nsobjectInput.readInt(); //dummy just to connect...
					packettype = 0;
				} catch (UnknownHostException e) {
					System.out.printf("nssocket unknown host\n");
					return false;
				} catch (IOException e) {
					System.out.printf("nssocket ioexception\n");				
					return false;
				}

				//got a connection and something to do!
				try {
					packettype = 0;
					nsobjectOutput.writeInt(packettype);
					nsobjectOutput.writeObject(pln);
					nsobjectOutput.writeObject(pa);
					nsobjectOutput.flush();
					int response = nsobjectInput.readInt();
					if(response == 0){
						is_valid = false;
					}else{
						is_valid = true;
					}

				} catch (IOException e) {
					//e.printStackTrace();
					//return false;
					System.out.printf("Generic NS failure. Default false.\n");
				}


				try {
					if(nsbufobjectInput != null)nsbufobjectInput.close();
				} catch (IOException e) {

				}
				try {
					if(nsbufobjectOutput != null)nsbufobjectOutput.close();
				} catch (IOException e) {

				}
				
				try {
					if(nsobjectInput != null)nsobjectInput.close();
				} catch (IOException e) {

				}
				try {
					if(nsobjectOutput != null)nsobjectOutput.close();
				} catch (IOException e) {

				}
				
				try {
					nssock.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if(!is_valid)return false;

			}

			p.player_privs = DangerZone.server.find_privs(pln);

			return true;
		}
			

		//single player
		p.player_privs = 0xffffffff;
		return true;
	}
	


}
