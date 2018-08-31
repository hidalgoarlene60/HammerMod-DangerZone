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

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.imageio.ImageIO;


import dangerzone.Chunk;
import dangerzone.Coords;
import dangerzone.CustomPackets;
import dangerzone.DangerZone;
import dangerzone.Dimensions;
import dangerzone.Effects;
import dangerzone.GameModes;
import dangerzone.InventoryContainer;
import dangerzone.NetworkInputBuffer;
import dangerzone.NetworkOutputBuffer;
import dangerzone.PacketTypes;
import dangerzone.Player;
import dangerzone.StitchedTexture;
import dangerzone.Utils;
import dangerzone.WorldRendererUtils;
import dangerzone.blocks.Block;
import dangerzone.blocks.Blocks;
import dangerzone.blocks.ColoringBlock;
import dangerzone.entities.Entities;
import dangerzone.entities.Entity;
import dangerzone.items.Items;


public class ServerConnection implements Runnable {

	Socket sock;
	
	//ObjectOutputStream objectOutput = null;
	//BufferedOutputStream bufobjectOutput = null;
	NetworkOutputBuffer objectOutput = null;
	//BufferedInputStream bufobjectInput = null;
	//ObjectInputStream objectInput = null;
	NetworkInputBuffer objectInput = null;
	public volatile int connected = 0;
	public List<Coords> requested_list; //Don't blast server with same request over and over and over again!
	public static Lock requested_list_lock = new ReentrantLock();
	private Player p;
	public static Lock output_lock = new ReentrantLock();
	private boolean shouldsync = false;
	public List<String> modnames = new ArrayList<String>();
	public volatile boolean waitformodstoload = false;
	public String worldname = null;
	public volatile boolean connectionInProgress = false;
	public long lastaverageloop = 0;

	//Random rand = new Random(1511); //for testing with a little lag
	
	public ServerConnection(Player pl, boolean ss){
		p = pl;
		shouldsync = ss;
		waitformodstoload = ss;
	}
	
	public void run()  {
		int eid, d, x, y, z, id, meta, iid, bid;
		Entity ent = null;
		double px, py, pz; 
		float fpx, fpy, fpz; 
		float mx, my, mz, rp, ry, rr, rpm, rym, rrm, vol, freq;
		float rph, ryh, rrh;
		int dead;
		String name = null;
		InventoryContainer ic;
		int which, slot, count, what, uses;		
		int packettype = 0;
		
		requested_list = new ArrayList<Coords>();
		
		try {
			if(DangerZone.start_server){ //single player!
				sock = new Socket("127.0.0.1", DangerZone.server_port);
			}else{
				sock = new Socket(DangerZone.server_address, DangerZone.server_port);
				//sock = new Socket("192.168.1.202", DangerZone.server_port);				
			}
		} catch (UnknownHostException e) {
			DangerZone.connection_msg = "Unknown Server.";
			DangerZone.connection_error = 1; 
			return;
		} catch (IOException e) {
			DangerZone.connection_msg = "Generic Error.";
			DangerZone.connection_error = 1; 
			return;
		}
		
		try {
			sock.setReceiveBufferSize(1024*1024);
		} catch (SocketException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		try {
			sock.setSendBufferSize(1024*128);
		} catch (SocketException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		

		try {
			sock.setTcpNoDelay(true);
		} catch (SocketException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		output_lock.lock();	
		try {
			//bufobjectOutput = new BufferedOutputStream(sock.getOutputStream(), 1024*128);
			//objectOutput = new ObjectOutputStream(bufobjectOutput);
			objectInput = new NetworkInputBuffer(sock.getInputStream(), 1024*64);
			objectOutput = new NetworkOutputBuffer(sock.getOutputStream(), 1024*64);
			
			packettype = PacketTypes.CONNECT;
			//System.out.printf("Client write connect\n");
			objectOutput.writeInt(packettype);
			//System.out.printf("Client write version string\n");
			objectOutput.writeString(DangerZone.versionstring);
			//System.out.printf("Client flush\n");
			objectOutput.flush();	
			
			//bufobjectInput = new BufferedInputStream(sock.getInputStream(), 1024*128);
			//objectInput = new ObjectInputStream(bufobjectInput);
			//objectInput = new NetworkInputBuffer(sock.getInputStream(), 1024*64);
			//System.out.printf("Client wait for version string\n");
			String vs = (String) objectInput.readString();
			//System.out.printf("Client got version string: %s\n", vs);
			if(vs == null || !vs.equals(DangerZone.versionstring)){
				DangerZone.connection_msg = String.format("Server is DZ verion %s", vs);
				DangerZone.connection_error = 1; //1 = version failure!
				return;
			}
			
			packettype = 0;
			if(shouldsync)packettype = 1;
			objectOutput.writeInt(packettype); // let server know if I will need to sync up other things...
			objectOutput.writeString(p.myname); //send my name!
			objectOutput.writeString(DangerZone.crypted_password); //send my password!
			objectOutput.flush();
			
			if(shouldsync){			//shouldsync = true = real client-server. shouldsync = false = singleplayer.			
				sendSkin(); //send my skin!
				//System.out.printf("sent player skin\n");
				getModNames();
				//System.out.printf("got mod names\n");
				//System.out.printf("world name = %s\n",  worldname);
				connectionInProgress = true;
				while(waitformodstoload){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// Auto-generated catch block
						//e.printStackTrace();
					}
				}
				
				d = -5;
				objectOutput.writeInt(d); //let server know we finished mod loading
				objectOutput.flush();
				
				//System.out.printf("waiting for server IDs\n");
				reSyncIDs(); //fetch IDs from server!
				//System.out.printf("got server IDs\n");
				
				d = -6;
				objectOutput.writeInt(d); //let server know we got all the ids
				objectOutput.flush();
				
				//Lastly, see if we have been renamed...
				p.myname = objectInput.readString();
				p.setPetName(p.myname);
				System.out.printf("My name from server is: %s\n", p.myname);
			}
			
			
			packettype = objectInput.readInt();
			if(packettype != 0){
				DangerZone.connection_msg = "Player is an alien!";
				if(packettype == 1)DangerZone.connection_msg = "Player Name/Password not valid.";
				if(packettype == 2)DangerZone.connection_msg = "Player banned... Go away.";
				if(packettype == 3)DangerZone.connection_msg = "Too many players already. Please try later!";
				DangerZone.connection_error = 1;
				//System.out.printf("XXXXXX got here 2\n");
				return;
			}
			
			//System.out.printf("getting initial player data\n");
			readPlayerIntoPlayer(p);
			//System.out.printf("got player data\n");
			p.init();
			
			id = objectInput.readInt();
			if(id <= 0 || id >= DangerZone.max_entities){
				DangerZone.connection_msg = "Bad Player Entity ID.";
				DangerZone.connection_error = 5;
				return;
			}
			p.entityID = id;
			//System.out.printf("XXXXXX got here 6\n");
			
		} catch (IOException e) {
			DangerZone.connection_msg = "Too many players or other comm failure.";
			DangerZone.connection_error = 1; 
			//System.out.printf("XXXXXX got here 7\n");
			return;
		}
		

		
		p.toServer = sock;
		p.server_connection = this;
		p.entityID = id;
		
		//System.out.printf("XXXXXX got here 8\n");
		connected = 1;
		output_lock.unlock();
		
		//Add self!
		DangerZone.entityManager.addEntity(p, id);
		
		
		System.out.printf("Connected!\n");
		
		try {		
			/*
			 * All set... let's go!
			 */
			while(DangerZone.gameover == 0){	
				
				packettype = objectInput.readInt();

				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.CHUNK){ //Chunk!
					//System.out.printf("Got chunk\n");
					Chunk c = new Chunk(0,0,0,0);

					receiveChunk(c);
					//System.out.printf("Got chunk %d,  %d\n", c.chunkX, c.chunkZ);

					DangerZone.world.chunkcache.addCacheChunk(c); //Client!

					requested_list_lock.lock();		
					boolean found = true;
					while(found){
						found = false;
						Iterator<Coords> ii = requested_list.iterator();
						while(ii.hasNext()){
							Coords cl = (Coords)ii.next();
							if(cl.d != c.dimension)continue;
							if(cl.x != c.chunkX)continue;
							if(cl.z != c.chunkZ)continue;
							//Remove it!
							ii.remove();
							found = true;
							break; //get out and restart the list... remove ALL of this chunk if there happens to be more than one...
						}
					}
					requested_list_lock.unlock();
					continue;
				}
				
				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.BLOCK){ //Telling us a block changed	
					//System.out.printf("Got block\n");
					d = objectInput.readInt();
					x = objectInput.readInt();
					y = objectInput.readInt();
					z = objectInput.readInt();
					id = objectInput.readShort();
					meta = objectInput.readShort();
					DangerZone.world.chunkcache.setBlockSilent(d, x, y, z, id, meta); 
					continue;
				}
				
				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.CHUNKMETAUPDATE){ //Telling us chunk meta changed	
					//System.out.printf("Got block\n");
					d = objectInput.readInt();
					x = objectInput.readInt();
					z = objectInput.readInt();
					List<String>newowners = null;				
					int nname;					
					nname = objectInput.readInt();
					if(nname > 0){
						newowners = new ArrayList<String>();
						while(nname > 0){
							newowners.add(objectInput.readString());
							nname--;
						}
					}					
					DangerZone.world.chunkcache.setChunkMeta(d, x<<4, z<<4, newowners); 
					continue;
				}
				
				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.TIME){ //Telling us time changed	
					d = objectInput.readInt();
					DangerZone.world.timetimer = d;
					d = objectInput.readInt();
					DangerZone.world.lengthOfDay = d;
					lastaverageloop = objectInput.readLong();
					respondtotime(); //for statistics and throttling slow networks...			
					continue;
				}
				
				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.VARINTUPDATE){	
					d = objectInput.readInt();
					x = objectInput.readInt();
					p.setVarInt(d, x);
					continue;
				}
				
				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.VARFLOATUPDATE){	
					d = objectInput.readInt();
					fpx = objectInput.readFloat();
					p.setVarFloat(d, fpx);
					continue;
				}
				
				//------------------------------------------------------------------------------------------				
				if(packettype == PacketTypes.VARSTRINGUPDATE){	
					d = objectInput.readInt();
					String ss = objectInput.readString();
					p.setVarString(d, ss);
					continue;
				}
				
				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.MOUNTENTITY){	
					d = objectInput.readInt();
					if(d > 0){
						ent = DangerZone.entityManager.findEntityByID(d);
						//System.out.printf("mount command to %d\n", d);
						if(ent != null){
							//System.out.printf("found %d\n", d);
							ent.setVarInt(10, p.entityID); //set rider
							p.setVarInt(11, d);
							if(ent.sit_when_riding)p.setSitting(true);
						}
					}else{
						p.setVarInt(10, 0);
						p.setVarInt(11, 0);
						p.setSitting(false);
					}

					continue;
				}
				
				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.NEWEFFECT){ 
					Effects ef = new Effects();
					ef.effect = objectInput.readInt();
					ef.duration = objectInput.readInt();
					ef.amplitude = objectInput.readFloat();
					DangerZone.player.addEffect(ef);
					continue;
				}
				
				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.CHATMESSAGE){ //Chat!
					String s = (String) objectInput.readString();
					DangerZone.chatgui.receiveMessage(s); //put into chat history
					//display it!
					if(DangerZone.current_gui == null){
						DangerZone.messagestring = s;
						DangerZone.messagetimer = 180; //60 frames per second. About 3 seconds...
					}
					continue;
				}
				
				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.PLAYSONG){ //Chat!
					String s = (String) objectInput.readString();
					DangerZone.soundmangler.playThisMusic(s);				
					continue;
				}
				
				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.COMMANDMESSAGE){ //Command being echoed back
					String s = (String) objectInput.readString();
					DangerZone.commandgui.receiveMessage(s); //put into command history
					//display it!
					if(DangerZone.current_gui == null){
						DangerZone.messagestring = s;
						DangerZone.messagetimer = 180; //60 frames per second. About 3 seconds...
					}
					continue;
				}
				
				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.SPAWNPARTICLES){ 
					String s = (String) objectInput.readString();
					which = objectInput.readInt(); //really howmany!
					d = objectInput.readInt();
					px = objectInput.readDouble(); //
					py = objectInput.readDouble(); //
					pz = objectInput.readDouble(); //
					id = objectInput.readInt();
					Utils.spawnParticles(DangerZone.player.world, s, which, d, px, py, pz, id, false); //spawn 'em, and DO NOT forward back to server
					continue;
				}
				
				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.ENTITYDEATH){ //Telling us an entity died
					d = objectInput.readInt();
					ent = DangerZone.entityManager.findEntityByID(d);
					//System.out.printf("dying entity %d, i am %d\n", d, p.entityID);
					if(ent != null){
						//System.out.printf("Found dying entity\n");
						ent.deadflag = true;
						ent.doDeathAnimation();
					}
					//System.out.printf("Entity Death received\n");
					continue;
				}
				
				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.ENTITYREMOVE){ //Telling us an entity died
					d = objectInput.readInt();
					//System.out.printf("remove entity %d, i am %d\n", d, p.entityID);
					if(d != p.entityID){ //Don't remove self!
						ent = DangerZone.entityManager.findEntityByID(d);
						if(ent != null){
							if(!ent.deadflag && !ent.isDying()){
								DangerZone.entityManager.removeEntityByID(d);
							}//else it is in the process of dying...
						}
					}
					continue;
				}
				
				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.ENTITYHIT){ 
					d = objectInput.readInt();
					fpx = objectInput.readFloat(); //new health!
					ent = DangerZone.entityManager.findEntityByID(d);
					//System.out.printf("hit entity %d, i am %d\n", d, p.entityID);
					if(ent != null){
						//System.out.printf("Found hit entity\n");
						ent.setHealth(fpx); //Other entities will update by themselves. But it might just be THIS player that got hit...
						ent.doHurtAnimation();
					}
					continue;
				}
				
				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.GAMEMODECHANGE){ 
					d = objectInput.readInt();
					p.setGameMode(d);
					if(d == GameModes.GHOST)DangerZone.messagestring = DangerZone.ghost_string;
					if(d == GameModes.SURVIVAL)DangerZone.messagestring = DangerZone.survival_string;
					if(d == GameModes.CREATIVE)DangerZone.messagestring = DangerZone.creative_string;
					if(d == GameModes.LIMBO)DangerZone.messagestring = DangerZone.limbo_string;
					DangerZone.messagetimer = 100;
					continue;
				}
				
				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.GAMEDIFFICULTYCHANGE){ 
					d = objectInput.readInt();
					p.setGameDifficulty(d);
					if(d == 0)DangerZone.messagestring = "Difficulty: Normal";
					if(d == -1)DangerZone.messagestring = "Difficulty: Easy";
					if(d == -2)DangerZone.messagestring = "Difficulty: Girly";
					if(d == 1)DangerZone.messagestring = "Difficulty: Hard";
					if(d == 2)DangerZone.messagestring = "Difficulty: Brutal";

					DangerZone.messagetimer = 100;
					continue;
				}

				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.PLAYERVELOCITY){ 
					//Usually knockback from a hit on the server
					fpx = objectInput.readFloat(); //
					fpy = objectInput.readFloat(); //
					fpz = objectInput.readFloat(); //
					p.motionx = fpx;
					p.motiony = fpy;
					p.motionz = fpz;		
					continue;
				}
				
				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.PLAYERPOSITIONVELOCITY){ 
					//Usually Kraken or something else carrying the player...
					px = objectInput.readDouble(); //
					py = objectInput.readDouble(); //
					pz = objectInput.readDouble(); //
					p.posx = px;
					p.posy = py;
					p.posz = pz;	
					fpx = objectInput.readFloat(); //
					fpy = objectInput.readFloat(); //
					fpz = objectInput.readFloat(); //
					p.motionx = fpx;
					p.motiony = fpy;
					p.motionz = fpz;	
					fpx = objectInput.readFloat(); //
					fpy = objectInput.readFloat(); //
					p.rotation_yaw = fpx;
					p.rotation_yaw_head = (360f-((fpy+180)%360));
					p.fallcount = 0; //not falling if being carried!
					continue;
				}
				
				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.PLAYERTELEPORT){ 
					//Teleport!!!
					d = objectInput.readInt();
					px = objectInput.readDouble();
					py = objectInput.readDouble();
					pz = objectInput.readDouble();
					//Move it! Move it! Move it! :)
					DangerZone.new_posx = px;
					DangerZone.new_posy = py;
					DangerZone.new_posz = pz;	
					DangerZone.new_dimension = d;
					continue;
				}
				
				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.IDLE){ 
					//do nothing!
					continue;
				}
				
				//------------------------------------------------------------------------------------------
				//Currently UNUSED, I think...
				if(packettype == PacketTypes.INVENTORYUPDATE){ //Telling us a player changed his inventory/held item...
					which = objectInput.readInt();
					slot = objectInput.readInt();
					bid = objectInput.readInt();
					iid = objectInput.readInt();
					count = objectInput.readInt();
					uses = objectInput.readInt();
					String inmeta = objectInput.readString();
					if(count == 0){
						ic = null;
					}else{
						ic = new InventoryContainer();
						ic.bid = bid;
						ic.iid = iid;
						ic.count = count;
						ic.currentuses = uses;
						ic.icmeta = inmeta;
						if(ic.count == 1 && ic.bid == 0 && ic.iid != 0){
							int atcount = objectInput.readInt();
							if(atcount > 0){
								int i, attype, atval;
								for(i=0;i<atcount;i++){
									attype = objectInput.readInt();
									atval = objectInput.readInt();
									ic.addAttribute(attype, atval);
								}
							}
						}
						//this container could be the start of a tree... read it...
						int mlength = objectInput.readInt();
						if(mlength > 0){
							ic.moreInventory = new InventoryContainer[mlength];
							for(int mix=0;mix < mlength; mix++){
								ic.moreInventory[mix] = readMoreInventory();
							}
						}
					}
					if(which == 0){
						p.setHotbar(slot, ic);
					}else if(which == 1){
						p.setInventory(slot, ic);
					}else if(which == 2){
						p.setArmor(slot, ic);
					}else{
						p.setVarInventory(slot,  ic);
					}
					continue;
				}
				
				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.PLAYERACTION){ //Telling us a player did something we should be able to see!				
					eid = objectInput.readInt();
					which = objectInput.readInt();
					what = objectInput.readInt();
					//0 = left-click, so swing the item!!!
					ent = DangerZone.entityManager.findEntityByID(eid);
					if(ent != null && ent instanceof Player){
						if(which == 0 && what == 0 && ent != DangerZone.player){ //click
							Player pp = (Player)ent;
							pp.armdir = 1;
						}
					}
					continue;
				}
				
				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.DEATHGUI){ //Telling us a player died!			
					DangerZone.doDeathGUI = true;
					continue;
				}
				
				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.PLAYERSTATS){ //Updating our stats		
					//System.out.printf("player stats update!\n");
					p.kills = objectInput.readInt();
					p.deaths = objectInput.readInt();
					p.damage_taken = objectInput.readDouble();
					p.damage_dealt = objectInput.readDouble();
					p.blocks_broken = objectInput.readInt();
					p.blocks_placed = objectInput.readInt();
					p.blocks_colored = objectInput.readInt();
					p.crafted = objectInput.readInt();
					p.bought = objectInput.readInt();
					p.sold = objectInput.readInt();
					p.broken = objectInput.readInt();
					p.traveled = objectInput.readInt();
					p.morphs = objectInput.readInt();
					p.teleports = objectInput.readInt();
					p.eaten = objectInput.readInt();
					p.roachstomps = objectInput.readInt();
					p.hard_landings = objectInput.readInt();
					p.flights = objectInput.readInt();
					p.spells = objectInput.readInt();
					continue;
				}
				
				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.ENTITYUPDATE){ //Yay!
					eid = objectInput.readInt();
					d = objectInput.readInt();					
					px = objectInput.readDouble();
					py = objectInput.readDouble();
					pz = objectInput.readDouble();
					mx = objectInput.readFloat();
					my = objectInput.readFloat();
					mz = objectInput.readFloat();
					rp = objectInput.readFloat();
					ry = objectInput.readFloat();
					rr = objectInput.readFloat();
					rph = objectInput.readFloat();
					ryh = objectInput.readFloat();
					rrh = objectInput.readFloat();
					rpm = objectInput.readFloat();
					rym = objectInput.readFloat();
					rrm = objectInput.readFloat();
					dead = objectInput.readInt();

					ent = DangerZone.entityManager.findEntityByID(eid);
					if(ent != null && d == DangerZone.player.dimension){
						if(eid != DangerZone.player.entityID){ //NOT self! Client player controls these.
							ent.diff_ticker = 6; //client updates at 60, server updates at 10. smooth it!
							ent.diff_posx = (float)(px - ent.posx);
							ent.diff_posy = (float)(py - ent.posy);
							ent.diff_posz = (float)(pz - ent.posz);							
							ent.diff_rotpitch = angdiff(rp, ent.rotation_pitch);
							ent.diff_rotyaw = angdiff(ry, ent.rotation_yaw);
							ent.diff_rotroll = angdiff(rr, ent.rotation_roll);
							ent.diff_rotpitchh = angdiff(rph, ent.rotation_pitch_head);
							ent.diff_rotyawh = angdiff(ryh, ent.rotation_yaw_head);
							ent.diff_rotrollh = angdiff(rrh, ent.rotation_roll_head);
							ent.dimension = d;
							ent.motionx = mx;
							ent.motiony = my;
							ent.motionz = mz;
							ent.rotation_pitch_motion = rpm;
							ent.rotation_yaw_motion = rym;
							ent.rotation_roll_motion = rrm;
							//if(ent == DangerZone.player){
							//	System.out.printf("Fail! %d,  %d\n", eid, DangerZone.player.entityID);
							//}
							//if(ent.uniquename.equals("OreSpawn:Vermin")){
							//	System.out.printf("got %s to %d, %f, %f, %f\n", ent.uniquename, ent.dimension, px, py, pz);
							//	System.out.printf("diff %s to %d, %f, %f, %f\n", ent.uniquename, ent.dimension, ent.diff_posx, ent.diff_posy, ent.diff_posz);
							//	System.out.printf("current %s to %d, %f, %f, %f\n", ent.uniquename, ent.dimension, ent.posx, ent.posy, ent.posz);
							//}
						}
						
						//these updates are common to all
						if(dead != 0){
							ent.deadflag = true;
						}else{
							ent.deadflag = false;
						}							
						readVarsIntoEntity(ent);
						ent.stray_entity_ticker = 0; //let the update thread know this entity is OK
					}else{
						//Ask what this entity is! Maybe...
						readVarsIntoNull();//It's a PIPE. The rest of the packet MUST be read.
						
						//System.out.printf("what is eid %d,  dim %d?\n", eid, d);
						if(d == DangerZone.player.dimension){
							double d1, d2, d3;
							d1 = DangerZone.player.posx - px;
							d2 = DangerZone.player.posy - py;
							d3 = DangerZone.player.posz - pz;
							d1 = Math.sqrt((d1*d1)+(d2*d2)+(d3*d3));
							if(d1 < DangerZone.renderdistance*16){
								if(eid != DangerZone.player.entityID){
									whatIsThisEntity(eid);
									//System.out.printf("What is this? %d\n", eid);
								}
							}
						}						
					}
					continue;
				}
				
				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.SPAWNENTITY){ //Telling us to create an entity here	
					name = (String) objectInput.readString();
					id = objectInput.readInt();
					d = objectInput.readInt();
					px = objectInput.readDouble();
					py = objectInput.readDouble();
					pz = objectInput.readDouble();
					rp = objectInput.readFloat();	
					ry = objectInput.readFloat();	
					rr = objectInput.readFloat();	
					rph = objectInput.readFloat();	
					ryh = objectInput.readFloat();	
					rrh = objectInput.readFloat();	
					mx = objectInput.readFloat();
					my = objectInput.readFloat();
					mz = objectInput.readFloat();
					//System.out.printf("Spawn entity %s\n", name);
					double d1, d2, d3;
					d1 = DangerZone.player.posx - px;
					d2 = DangerZone.player.posy - py;
					d3 = DangerZone.player.posz - pz;
					d1 = Math.sqrt((d1*d1)+(d2*d2)+(d3*d3));
					if(d1 < DangerZone.renderdistance*16 && d == DangerZone.player.dimension){											
						ent = Entities.spawnEntityByName(name, DangerZone.world);
						if(ent != null ){
							ent.entityID = id;
							ent.dimension = d;
							ent.posx = px;
							ent.posy = py;
							ent.posz = pz;
							ent.rotation_pitch = rp;
							ent.rotation_yaw = ry;
							ent.rotation_roll = rr;
							ent.rotation_pitch_head = rph;
							ent.rotation_yaw_head = ryh;
							ent.rotation_roll_head = rrh;
							ent.motionx = mx;
							ent.motiony = my;
							ent.motionz = mz;
							readVarsIntoEntity(ent);
							if(ent instanceof Player){
								getSkinData(ent);
							}
							ent.init();
							DangerZone.entityManager.addEntity(ent, id);
							if(id == DangerZone.player.entityID){
								System.out.printf("??spawn entity for self\n");
							}

						}else{	
							//System.out.printf("unwanted 1? %d\n", id);
							readVarsIntoNull();		//It's a PIPE. The rest of the packet MUST be read.
						}
					}else{
						//System.out.printf("unwanted 2? %d\n", id);
						readVarsIntoNull();		//It's a PIPE. The rest of the packet MUST be read.
					}
					continue;
				}

				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.PLAYSOUND){ //Telling us to play a sound					
					name = (String) objectInput.readString();						
					d = objectInput.readInt();
					px = objectInput.readDouble();
					py = objectInput.readDouble();
					pz = objectInput.readDouble();
					vol = objectInput.readFloat();	
					freq = objectInput.readFloat();	
					if(d == DangerZone.player.dimension)DangerZone.soundmangler.playSound(name, vol, freq, d, px, py, pz);	
					continue;
				}

				//------------------------------------------------------------------------------------------
				if(packettype == PacketTypes.LIGHTINGREQUEST){
					d = objectInput.readInt();
					x = objectInput.readInt();
					y = objectInput.readInt();
					z = objectInput.readInt();
					float lv = WorldRendererUtils.getTotalLightAt(DangerZone.world, d, x, y, z);
					sendLightResponse(lv);
					continue;
				}
				
				//-------------------------------------------------------------------------------------
				if(packettype == PacketTypes.COLORINGBLOCK){ //			
					name = (String) objectInput.readString();
					x = objectInput.readInt(); //colorbid
					float colordata[][][] = new float[16][16][4];
					for(int i=0;i<16;i++){
						for(int j=0;j<16;j++){
							for(int k=0;k<4;k++){
								colordata[i][j][k] = objectInput.readFloat();
							}
						}
					}
					doSaveColoringBlock(name, x, colordata);
					continue;
				}
				
				//Try a custom packet!
				CustomPackets.messageFromServer(packettype, objectInput);
			}

		} catch (ClassNotFoundException e) {
			DangerZone.gameover = 1;
			return;
		}
	}	
	
	public void getDecoratedChunk(int d, int x, int y, int z){
		if(connected == 0)return;
		if(x < 0 || z < 0)return;
		
		requested_list_lock.lock();		
		Iterator<Coords> i;
		long nowtime = System.currentTimeMillis();
		//first let's remove anything older than 30 seconds... 
		//it will get tried again, or player switched dimensions and doesn't want it anymore anyway...
		boolean found = true;
		while(found){
			found = false;
			i = requested_list.iterator();
			while(i.hasNext()){
				Coords c = (Coords)i.next();
				if(nowtime - c.timer > 30000){
					i.remove();
					found = true;	
					break;
				}
			}
		}
		
		boolean addit = true;
		i = requested_list.iterator();
		while(i.hasNext()){
			Coords c = (Coords)i.next();
			if(c.d != d)continue;
			if(c.x != (x>>4))continue;
			if(c.z != (z>>4))continue;
			//Have already requested this chunk!
			if(nowtime - c.timer > 5000){ //five seconds! Resend
				c.timer = nowtime;
				addit = false; //but don't add it again!
				break;
			}
			requested_list_lock.unlock();
			Thread.yield(); //slow down a little to wait for it.
			return;
		}
		DangerZone.packets_per_second++;
		if(addit){
			Coords newc = new Coords();
			newc.d = d; newc.x = x>>4; newc.y = y; newc.z = z>>4;
			newc.timer = nowtime;
			requested_list.add(newc);
		}
		requested_list_lock.unlock();
		
		//System.out.printf("Requesting chunk %d,  %d\n", newc.x, newc.z);
		
		int pt = PacketTypes.GETCHUNK;
		output_lock.lock();
		objectOutput.writeInt(pt);
		objectOutput.writeInt(d);
		objectOutput.writeInt(x);
		objectOutput.writeInt(y);
		objectOutput.writeInt(z);
		//objectOutput.flush();
		output_lock.unlock(); 
	}
	
	public void flushit(){
		output_lock.lock();
		objectOutput.flush();
		output_lock.unlock(); 
	}
	
	public void blockChanged(int d, int x, int y, int z, int id, int meta){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		int pt = PacketTypes.BLOCK;
		output_lock.lock();
		objectOutput.writeInt(pt);
		objectOutput.writeInt(d);
		objectOutput.writeInt(x);
		objectOutput.writeInt(y);
		objectOutput.writeInt(z);
		objectOutput.writeShort((short) id);
		objectOutput.writeShort((short) meta);
		objectOutput.flush();
		output_lock.unlock(); 
	}
	
	public void respondtotime(){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		int pt = PacketTypes.TIMERESPONSE;
		output_lock.lock();
		objectOutput.writeInt(pt);
		pt = DangerZone.wr.fps;
		objectOutput.writeInt(pt);
		pt = DangerZone.renderdistance;
		objectOutput.writeInt(pt);
		objectOutput.flush();
		output_lock.unlock(); 
	}
	
	public void whatIsThisEntity(int id){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		int pt = PacketTypes.WHATISTHISENTITY;
		output_lock.lock();
		objectOutput.writeInt(pt);
		objectOutput.writeInt(id);
		//objectOutput.flush();
		output_lock.unlock(); 
	}
	
	public void sendKeyEventToEntity(int id, int key, boolean updown){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		int pt = PacketTypes.KEYEVENTTOENTITY;
		output_lock.lock();
		objectOutput.writeInt(pt);
		objectOutput.writeInt(id);
		objectOutput.writeInt(key);
		pt = updown?1:0;
		objectOutput.writeInt(pt);
		objectOutput.flush(); //no delay!!!
		output_lock.unlock(); 
	}
	
	public void sendMouseEventToEntity(int id, int key, boolean updown){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		int pt = PacketTypes.MOUSEEVENTTOENTITY;
		output_lock.lock();
		objectOutput.writeInt(pt);
		objectOutput.writeInt(id);
		objectOutput.writeInt(key);
		pt = updown?1:0;
		objectOutput.writeInt(pt);
		objectOutput.flush(); //no delay!!!
		output_lock.unlock(); 
	}
	
	public void sendBreakBlock(int d, int x, int y, int z){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		int pt = PacketTypes.BREAKBLOCK;
		output_lock.lock();
		objectOutput.writeInt(pt);
		objectOutput.writeInt(d);
		objectOutput.writeInt(x);
		objectOutput.writeInt(y);
		objectOutput.writeInt(z);
		objectOutput.flush();
		output_lock.unlock(); 
	}
	
	public void sendKillMe(int id){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		int pt = PacketTypes.KILLME;
		output_lock.lock();
		objectOutput.writeInt(pt);
		objectOutput.writeInt(id);
		objectOutput.flush();
		output_lock.unlock(); 
	}
	
	public void sendRespawn(){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		int pt = PacketTypes.RESPAWN;
		output_lock.lock();
		objectOutput.writeInt(pt);
		objectOutput.flush();
		output_lock.unlock(); 
	}
	
	public void sendPlayerDied(){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		int pt = PacketTypes.PLAYERDIEDCLIENT;
		output_lock.lock();
		objectOutput.writeInt(pt);
		objectOutput.flush();
		output_lock.unlock(); 
	}
	
	public void sendPlayerTeleport(int dim){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		int pt = PacketTypes.PLAYERTELEPORT;
		output_lock.lock();
		objectOutput.writeInt(pt);
		objectOutput.writeInt(dim);
		objectOutput.writeDouble(p.posx);
		objectOutput.writeDouble(p.posy);
		objectOutput.writeDouble(p.posz);
		objectOutput.flush();
		output_lock.unlock(); 
	}
	
	public void sendLightResponse(float lv){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		int pt = PacketTypes.LIGHTINGREQUEST;
		output_lock.lock();
		objectOutput.writeInt(pt);
		objectOutput.writeFloat(lv);
		objectOutput.flush();
		output_lock.unlock(); 
	}
	
	public void sendDisconnect(){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		int pt = PacketTypes.DISCONNECT;
		output_lock.lock();
		objectOutput.writeInt(pt);
		objectOutput.flush();
		connected = 0; //no more output!
		output_lock.unlock(); 
	}
	
	public void sendChatMessage(String s){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		int pt = PacketTypes.CHATMESSAGE;
		output_lock.lock();
		objectOutput.writeInt(pt);
		objectOutput.writeString(s);
		objectOutput.flush();
		output_lock.unlock(); 
	}
	
	public void idle(){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		int pt = PacketTypes.IDLE;
		output_lock.lock();
		objectOutput.writeInt(pt);
		objectOutput.flush();
		output_lock.unlock(); 
	}
	
	public void sendPetNameMessage(int eid, int slen, String s){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		int pt = PacketTypes.PETNAME;
		output_lock.lock();
		objectOutput.writeInt(pt);
		objectOutput.writeInt(eid);
		objectOutput.writeInt(1);
		objectOutput.writeString(s);
		objectOutput.flush();
		output_lock.unlock(); 
	}
	
	public void sendVarStringMessage(int eid, int which, String s){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		int pt = PacketTypes.PETNAME;
		output_lock.lock();
		objectOutput.writeInt(pt);
		objectOutput.writeInt(eid);
		objectOutput.writeInt(which);
		objectOutput.writeString(s);
		objectOutput.flush();
		output_lock.unlock(); 
	}
	
	public void sendVarIntMessage(int eid, int which, int what){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		int pt = PacketTypes.VARINTUPDATE;
		output_lock.lock();
		objectOutput.writeInt(pt);
		objectOutput.writeInt(eid);
		objectOutput.writeInt(which);
		objectOutput.writeInt(what);
		objectOutput.flush();
		output_lock.unlock(); 
	}
	
	public void sendCommandMessage(String s){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		int pt = PacketTypes.COMMANDMESSAGE;
		output_lock.lock();
		objectOutput.writeInt(pt);
		objectOutput.writeString(s);
		objectOutput.flush();
		output_lock.unlock(); 
	}
	
	public void sendSpawnParticles(String s, int hm, int d, double x, double y, double z){
		sendSpawnParticles( s,  hm,  d,  x,  y,  z, 0);
	}

	public void sendSpawnParticles(String s, int hm, int d, double x, double y, double z, int bid){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		int pt = PacketTypes.SPAWNPARTICLES;
		output_lock.lock();
		objectOutput.writeInt(pt);
		objectOutput.writeString(s);
		objectOutput.writeInt(hm);
		objectOutput.writeInt(d);
		objectOutput.writeDouble(x);
		objectOutput.writeDouble(y);
		objectOutput.writeDouble(z);
		objectOutput.writeInt(bid);
		//objectOutput.flush();
		output_lock.unlock(); 
	}
	
	
	public void playerActionToServer(int which, int what, int eid, int x, int y, int z, int side, float magic, int magic_type){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		int pt = PacketTypes.PLAYERACTION;
		output_lock.lock();
		objectOutput.writeInt(pt);
		objectOutput.writeInt(which);
		objectOutput.writeInt(what);
		objectOutput.writeInt(eid);
		objectOutput.writeInt(x);
		objectOutput.writeInt(y);
		objectOutput.writeInt(z);
		objectOutput.writeInt(side);
		objectOutput.writeFloat(magic);
		objectOutput.writeInt(magic_type);
		objectOutput.flush();
		output_lock.unlock(); 
	}
	
	public void handleInventory(int command, int p1, int p2, int p3, int p4){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		int pt = PacketTypes.HANDLEINVENTORY;
		output_lock.lock();
		objectOutput.writeInt(pt);
		objectOutput.writeInt(command);
		objectOutput.writeInt(p1);
		objectOutput.writeInt(p2);
		objectOutput.writeInt(p3);
		objectOutput.writeInt(p4);
		objectOutput.flush();
		output_lock.unlock(); 
	}
	
	public void changeGameMode(int which){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		int pt = PacketTypes.GAMEMODECHANGE;
		output_lock.lock();
		objectOutput.writeInt(pt);
		objectOutput.writeInt(which);
		objectOutput.flush();
		output_lock.unlock(); 
	}
	
	public void changeGameDifficulty(int which){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		int pt = PacketTypes.GAMEDIFFICULTYCHANGE;
		output_lock.lock();
		objectOutput.writeInt(pt);
		objectOutput.writeInt(which);
		objectOutput.flush();
		output_lock.unlock(); 
	}
	
	
	public void sendPlayerEntityUpdate(Entity e){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		output_lock.lock();
		int pt = PacketTypes.ENTITYUPDATE;
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
		objectOutput.writeFloat(360f-((e.rotation_yaw+180)%360)); //WTF?
		objectOutput.writeFloat(e.rotation_roll);
		objectOutput.writeFloat(e.rotation_pitch_head);
		objectOutput.writeFloat(360f-((e.rotation_yaw_head+180)%360)); //WTF?
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
 
		objectOutput.writeInt(DangerZone.player.clienthotbarindex); //should be safe! only called on client for player!
		
		//SERVER controls all vars
		
		pt = -3; //packet terminator!
		objectOutput.writeInt(pt);
							
		objectOutput.flush();
		output_lock.unlock();
	}
	
	/*
	 * Because sending and receiving a chunk as an object only works MOSTLY... not always... I HATE JAVA!!!
	 */
	private void receiveChunk(Chunk c) throws ClassNotFoundException {
		int separator;
		short curcount, curval, indx;
		int nname;
		
		nname = objectInput.readInt();
		if(nname > 0){
			c.ownernames = new ArrayList<String>();
			while(nname > 0){
				c.ownernames.add(objectInput.readString());
				nname--;
			}
		}
		c.chunkX = objectInput.readInt();
		c.chunkZ = objectInput.readInt();
		c.dimension = objectInput.readInt();
		c.isDecorated = objectInput.readInt();
		c.isChanged = objectInput.readInt();
		c.isValid = objectInput.readInt();
		c.must_be_written = objectInput.readInt();
		c.b_red = objectInput.readFloat();
		c.b_green = objectInput.readFloat();
		c.b_blue = objectInput.readFloat();

		
		separator = objectInput.readInt();
		if(separator != -1){
			System.out.printf("Big oops!\n");
			DangerZone.gameover = 1;
		}
		
		//switch to SHORT separators.
		
		while(true){
			separator = objectInput.readShort();
			if(separator < 0)break;	
			c.blockdata[separator] = new short [256];
			curcount = objectInput.readShort();
			curval = objectInput.readShort();
			for(indx=0;indx<256;indx++){
				c.blockdata[separator][indx] = (short) (curval & 0xffff);
				curcount--;
				if(indx < 255 && curcount == 0){
					curcount = objectInput.readShort();
					curval = objectInput.readShort();
				}					
			}
		}
		
		while(true){
			separator = objectInput.readShort();
			if(separator < 0)break;				
			c.metadata[separator] = (short[]) objectInput.readShortArray();
		}
	}
	
	/*
	public void spawnEntity(Entity e){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		output_lock.lock();	
		int pt = PacketTypes.SPAWNENTITY;
		objectOutput.writeInt(pt);
		objectOutput.writeString(e.uniquename);
		objectOutput.writeInt(e.dimension);
		objectOutput.writeDouble(e.posx);
		objectOutput.writeDouble(e.posy);
		objectOutput.writeDouble(e.posz);
		objectOutput.writeFloat(e.rotation_pitch);
		objectOutput.writeFloat(e.rotation_yaw);
		objectOutput.writeFloat(e.rotation_roll);
		objectOutput.writeFloat(e.motionx);
		objectOutput.writeFloat(e.motiony);
		objectOutput.writeFloat(e.motionz);
		
		//bang out ints that changed
		for(int i=0;i<e.maxvars;i++){
			if(e.entity_ints[i] != 0){
				objectOutput.writeInt(i);
				objectOutput.writeInt(e.entity_ints[i]);						
			}
		}
		pt = -1; //packet separator
		objectOutput.writeInt(pt);

		//bang out floats that changed
		for(int i=0;i<e.maxvars;i++){
			if(e.entity_floats[i] != 0){
				objectOutput.writeInt(i);
				objectOutput.writeFloat(e.entity_floats[i]);						
			}
		}
		pt = -1; //packet separator
		objectOutput.writeInt(pt);

		//bang out strings that changed
		for(int i=0;i<e.maxvars;i++){
			if(e.entity_strings[i] != null){
				objectOutput.writeInt(i);
				objectOutput.writeString(e.entity_strings[i]);						
			}
		}
		pt = -1; //packet separator
		objectOutput.writeInt(pt);	
		
		InventoryContainer ic = null;
		for(int i=0;i<e.maxinv;i++){
			ic = e.getVarInventory(i);
			if(ic != null){
				objectOutput.writeInt(i);
				objectOutput.writeInt(ic.bid);
				objectOutput.writeInt(ic.iid);
				objectOutput.writeInt(ic.count);
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
				//System.out.printf("inventory at %d\n", i);
			}				
		}				
		pt = -1; //packet separator
		objectOutput.writeInt(pt);
		
		pt = -2; //packet separator!
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
		
		objectOutput.flush(); 
		output_lock.unlock();
	}
	*/
	
	
	private void readVarsIntoEntity(Entity ent){
		int index;
		index = objectInput.readInt();
		if(index != -2){ //contains vars!
			while(index >= 0){
				if(index < ent.maxvars)ent.entity_ints[index] = objectInput.readInt();
				index = objectInput.readInt();
			}
			//floats that changed
			index = objectInput.readInt();
			while(index >= 0){
				if(index < ent.maxvars)ent.entity_floats[index] = objectInput.readFloat();
				index = objectInput.readInt();
			}
			//strings that changed
			index = objectInput.readInt();
			while(index >= 0){
				if(index < ent.maxvars)ent.entity_strings[index] = (String) objectInput.readString();
				index = objectInput.readInt();
			}
			
			readChangedInventory(ent);
			
			index = objectInput.readInt();
			
		}
		
		if(index != -2){
			System.out.printf("packet separator failure on enitity update!\n");
			DangerZone.gameover = 1;
		}
		
		index = objectInput.readInt();
		if(index > 0){
			List<Effects> new_effect_list = new ArrayList <Effects>();		
			while(index > 0){
				Effects ef = new Effects();
				ef.effect = index;
				ef.amplitude = objectInput.readFloat();
				ef.duration = objectInput.readInt();
				ef.duration_counter = objectInput.readInt();
				new_effect_list.add(ef);
				index = objectInput.readInt();
			}
			ent.effect_list = new_effect_list;
		}else{
			if(ent.effect_list.size() > 0){
				ent.effect_list =  new ArrayList <Effects>();
			}
		}
		
		if(index != -3){
			System.out.printf("packet terminator failure on enitity update!\n");
			DangerZone.gameover = 1;
		}
	}
	
	public void readChangedInventory(Entity ent){
		int index = objectInput.readInt();				
		InventoryContainer ic;
		int bid, iid, count, uses;
		String inmeta = null;
		while(index >= 0){
			count = objectInput.readInt();
			if(count <= 0){
				ic = null;
			}else{
				bid = objectInput.readInt();
				iid = objectInput.readInt();
				uses = objectInput.readInt();
				inmeta = objectInput.readString();
				ic = new InventoryContainer();
				ic.bid = bid;
				ic.iid = iid;
				ic.count = count;
				ic.currentuses = uses;
				ic.icmeta = inmeta;
				if(ic.count == 1 && ic.bid == 0 && ic.iid != 0){
					int atcount = objectInput.readInt();
					if(atcount > 0){
						int i, attype, atval;
						for(i=0;i<atcount;i++){
							attype = objectInput.readInt();
							atval = objectInput.readInt();
							ic.addAttribute(attype, atval);
						}
					}
				}
				//this container could be the start of a tree... read it...
				int mlength = objectInput.readInt();
				if(mlength > 0){
					//System.out.printf("level0 len = %d\n", mlength);
					ic.moreInventory = new InventoryContainer[mlength];
					for(int mix=0;mix < mlength; mix++){
						int mindx = objectInput.readInt();
						//System.out.printf("level0 mindex = %d\n", mindx);
						ic.moreInventory[mindx] = readMoreInventory();
					}
				}
			}
			ent.setVarInventory(index, ic);
			index = objectInput.readInt();
		}
	}
	
	public InventoryContainer readMoreInventory(){
		int count = objectInput.readInt();
		if(count <= 0)return null;
		int bid = objectInput.readInt();
		int iid = objectInput.readInt();
		int uses = objectInput.readInt();
		String inmeta = objectInput.readString();
		InventoryContainer ic = new InventoryContainer();
		ic.bid = bid;
		ic.iid = iid;
		ic.count = count;
		ic.currentuses = uses;
		ic.icmeta = inmeta;
		if(ic.count == 1 && ic.bid == 0 && ic.iid != 0){
			int atcount = objectInput.readInt();
			if(atcount > 0){
				int i, attype, atval;
				for(i=0;i<atcount;i++){
					attype = objectInput.readInt();
					atval = objectInput.readInt();
					ic.addAttribute(attype, atval);
				}
			}
		}
		int mlength = objectInput.readInt();
		if(mlength > 0){
			//System.out.printf("level1 len = %d\n", mlength);
			ic.moreInventory = new InventoryContainer[mlength];
			for(int mix=0;mix < mlength; mix++){
				int mindx = objectInput.readInt();
				//System.out.printf("level1 mindx = %d\n", mindx);
				ic.moreInventory[mindx] = readMoreInventory();
			}
		}else{
			ic.moreInventory = null;
		}
		return ic;
	}
	
	private void readVarsIntoNull(){
		int index = 0;
		int bid, iid, count;
		index = objectInput.readInt();
		if(index != -2){ //contains vars!
			while(index >= 0){
				objectInput.readInt();
				index = objectInput.readInt();
			}
			//floats that changed
			index = objectInput.readInt();
			while(index >= 0){
				objectInput.readFloat();
				index = objectInput.readInt();
			}
			//strings that changed
			index = objectInput.readInt();
			while(index >= 0){
				objectInput.readString();
				index = objectInput.readInt();
			}
			index = objectInput.readInt();				

			while(index >= 0){
				count = objectInput.readInt();
				if(count > 0){
					bid = objectInput.readInt();
					iid = objectInput.readInt();
					objectInput.readInt();	
					objectInput.readString();
					if(count == 1 && bid == 0 && iid != 0){
						int atcount = objectInput.readInt();
						if(atcount > 0){							
							for(int i=0;i<atcount;i++){
								objectInput.readInt();
								objectInput.readInt();
							}
						}
					}
					int mlength = objectInput.readInt();
					if(mlength > 0){					
						for(int mix=0;mix < mlength; mix++){
							objectInput.readInt();
							readMoreInventory();
						}				
					}
				}
				index = objectInput.readInt();
			}
			index = objectInput.readInt();
		}
		
		if(index != -2){
			System.out.printf("packet separator failure on enititynull update!\n");
			DangerZone.gameover = 1;
		}
		
		index = objectInput.readInt();
		if(index > 0){
			while(index > 0){
				objectInput.readFloat();
				objectInput.readInt();
				objectInput.readInt();
				index = objectInput.readInt();
			}
		}
		if(index != -3){
			System.out.printf("packet terminator failure on enititynull update!\n");
			DangerZone.gameover = 1;
		}			
	}
	
	public void sendSound(String name, int dimension, double posx, double posy, double posz, float volume, float frequency){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		output_lock.lock();	
		int pt = PacketTypes.PLAYSOUND;
		objectOutput.writeInt(pt);
		objectOutput.writeString(name);
		objectOutput.writeInt(dimension);
		objectOutput.writeDouble(posx);
		objectOutput.writeDouble(posy);
		objectOutput.writeDouble(posz);
		objectOutput.writeFloat(volume);
		objectOutput.writeFloat(frequency);

		objectOutput.flush(); 
		output_lock.unlock();
	}

	//Gets IDs from server to make sure we agree!
	//Only affects ones we have in common.
	//Should compare mod list to make sure we have same ones too!
	public void reSyncIDs(){
		int i, x;
		String name = null;
		
		i = objectInput.readInt();
		while(i > 0){				
			name = (String) objectInput.readString();
			Items.reRegisterItemAt(name, i);
			i = objectInput.readInt();
			//System.out.printf("Item id = %d\n",  i);
		}
		
		i = objectInput.readInt();
		while(i > 0){				
			name = (String) objectInput.readString();
			Blocks.reRegisterBlockAt(name, i);
			i = objectInput.readInt();
			//System.out.printf("Block id = %d\n",  i);
		}
		
		i = objectInput.readInt();
		while(i > 0){				
			name = (String) objectInput.readString();
			Dimensions.reRegisterDimensionAt(name, i);
			i = objectInput.readInt();
			//System.out.printf("Dimension id = %d\n",  i);
		}
		
		i = objectInput.readInt();
		while(i > 0){				
			name = (String) objectInput.readString();
			CustomPackets.reRegisterCustomPacketAt(name, i);
			i = objectInput.readInt();
			//System.out.printf("Custom Packet id = %d\n",  i);
		}
		
		//receive the coloring blocks!
		i = objectInput.readInt();
		//System.out.printf("Initial coloring block id = %d\n",  i);
		while(i > 0){	
			name = (String) objectInput.readString();
			x = objectInput.readInt(); //colorbid
			//System.out.printf("real coloring block name, id = %s, %d\n",  name, x);
			float colordata[][][] = new float[16][16][4];
			for(int m=0;m<16;m++){
				for(int j=0;j<16;j++){
					for(int k=0;k<4;k++){
						colordata[m][j][k] = objectInput.readFloat();
					}
				}
			}
			doSaveColoringBlock(name, x, colordata);
		
			i = objectInput.readInt();
			//System.out.printf("next coloring block id = %d\n",  i);
		}
	}
	
	public void getModNames(){
		int i;
		String name = null;
		
		i = objectInput.readInt();
		while(i > 0){				
			name = objectInput.readString();
			modnames.add(name);
			//System.out.printf("Received mod name %s\n", name);
			i--;
		}
		
		worldname = objectInput.readString();
	}
	
	private void sendSkin(){
		int pt = -3;
		byte b[] = p.tdata;
		
		objectOutput.writeInt(pt);
		objectOutput.writeByteArray(b, b.length);
		objectOutput.flush();
		
	}
	
	private void getSkinData(Entity e){
		
		byte[] b;
		Player pe = (Player)e;

		b = (byte[]) objectInput.readByteArray(); //FIX ME! I CRASH!!!!!!		

		if(b == null || b.length < 64*32*4)return; //no texture for you!
		
		pe.tdata = b; //save new texture data
		pe.donewtexture = true;
	}
	
	public NetworkOutputBuffer getOutputStream(){
		if(connected == 0)return null;
		if(DangerZone.gameover != 0)return null;
		DangerZone.packets_per_second++;		
		output_lock.lock();
		return objectOutput;
	}
	
	public void releaseOutputStream(){
		//objectOutput.flush();	
		output_lock.unlock();
	}
	
	private void readPlayerIntoPlayer(Player ent){
		ent.dimension = objectInput.readInt();					
		ent.posx = objectInput.readDouble();
		ent.posy = objectInput.readDouble();
		ent.posz = objectInput.readDouble();
		ent.motionx = objectInput.readFloat();
		ent.motiony = objectInput.readFloat();
		ent.motionz = objectInput.readFloat();
		ent.rotation_pitch = objectInput.readFloat();
		ent.rotation_yaw = objectInput.readFloat();
		ent.rotation_roll = objectInput.readFloat();
		ent.rotation_pitch_head = objectInput.readFloat();
		ent.rotation_yaw_head = objectInput.readFloat();
		ent.rotation_roll_head = objectInput.readFloat();
		ent.rotation_pitch_motion = objectInput.readFloat();
		ent.rotation_yaw_motion = objectInput.readFloat();
		ent.rotation_roll_motion = objectInput.readFloat();
		int dead = objectInput.readInt();
		if(dead != 0){
			ent.deadflag = true;
		}else{
			ent.deadflag = false;
		}									
		readVarsIntoEntity(ent);
		ent.stray_entity_ticker = 0; //let the update thread know this entity is OK
		
		// tweak it, becaus it was saved on server...
		ent.rotation_yaw = 360f - ((ent.rotation_yaw+180)%360);
		ent.rotation_yaw_head = 360f - ((ent.rotation_yaw_head+180)%360);

	}
	
	public void sendColoringBlock(int bid, int dim, int x, int y, int z, float colordata[][][]){
		if(connected == 0)return;
		DangerZone.packets_per_second++;
		int pt = PacketTypes.COLORINGBLOCK;
		output_lock.lock();
		objectOutput.writeInt(pt);
		objectOutput.writeInt(bid);
		objectOutput.writeInt(dim);
		objectOutput.writeInt(x);
		objectOutput.writeInt(y);
		objectOutput.writeInt(z);
		for(int i=0;i<16;i++){
			for(int j=0;j<16;j++){
				for(int k=0;k<4;k++){
					objectOutput.writeFloat(colordata[i][j][k]);
				}
			}
		}
		objectOutput.flush();
		output_lock.unlock(); 
	}
	
	public void doSaveColoringBlock(String blkname, int bid, float colordata[][][]){
		File file = null;		
		if(bid < 0 || bid >= Blocks.blocksMAX)return;
		if(DangerZone.start_server){//already done by the server. Just reload the texture...
			Block blk = Blocks.BlockArray[bid];
			//Texture tx = blk.texture;
			blk.texture = null;
			//if(tx != null)tx.release(); //FIXME! SHOULD COORDINATE WITH WORLDRENDERER TO RELEASE THIS			
			blk.stitchedtexture = new StitchedTexture();		
			return; 
		}
		
		//We are single=player client!		
		//MAKE A TEMPORARY TEXTURE FILE SO WE DON'T HOSE OUR OWN!!!
		
		try {
			file = File.createTempFile("colordata", ".png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		file.deleteOnExit(); //clean up!
		//save this thing to a file!
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
		
		if(Blocks.BlockArray[bid] != null){ //existing block. Replace texture.			
			Block blk = Blocks.BlockArray[bid];
			//Texture tx = blk.texture;
			blk.texturepath = file.getAbsolutePath();
			blk.texture = null;
			//if(tx != null)tx.release(); //FIXME! SHOULD COORDINATE WITH WORLDRENDERER TO RELEASE THIS			
			blk.stitchedtexture = new StitchedTexture();					
		}else{ //Have to register a new block!
			ColoringBlock cb = new ColoringBlock(blkname, "");
			cb.texturepath = file.getAbsolutePath();
			cb.blockID = bid;
			Blocks.BlockArray[bid] = cb;		//Hope there wasn't anything there! :)
		}
				
	}
	
	//return nearest difference between two angles
	float angdiff(float a1, float a2){
		float retval;
		while(a1 > 360)a1 -= 360;
		while(a2 > 360)a2 -= 360;
		while(a1 < 0)a1 += 360;
		while(a2 < 0)a2 += 360;
		retval = a1 - a2;
		if(retval < 0)retval += 360;
		if(retval > 180)retval -= 360;
		return retval;		
	}

}
