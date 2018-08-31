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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import dangerzone.Chunk;
import dangerzone.DangerZone;
import dangerzone.ListInt;
import dangerzone.Player;
import dangerzone.ServerHooker;
import dangerzone.Utils;
import dangerzone.blocks.Blocks;
import dangerzone.blocks.ColoringBlock;
import dangerzone.entities.Entity;


//FIXME TODO - switch to SocketChannels? Faster???

public class Server implements Runnable {
	

	ServerSocket myServerSocket;
	public  List<ListInt> server_thread_list; // a list of little things that index into
	public ServerThread players[];           // an array of big things that we don't want Java to copy!
	public int max_players = 256;
	public  Lock player_list_lock = new ReentrantLock();
	public  Lock priv_lock = new ReentrantLock();
	private Player plyr;
	public ServerEntityUpdateLoop entityManager = null;

	public void run() {
		Socket sock;
		ServerThread st;
		server_thread_list = new ArrayList<ListInt>();
		
		try {
			myServerSocket = new ServerSocket(DangerZone.server_port);
		} catch (IOException e) {
			return;
		}
		
		players = new ServerThread[max_players]; //Make room for players!
		int i;
		for(i=0;i<max_players;i++){
			players[i] = null;
		}
		
		entityManager = new ServerEntityUpdateLoop();
		Thread it = new Thread(entityManager);	//Fire up the entity runner!
		//it.setPriority(Thread.MAX_PRIORITY);
		it.start();	
		
		
		//Need to register the dynamic coloring blocks before we start the block ticker, just in case it ticks something next to one...
		//Also need to send them to remote players.
		String cbname = new String();
		int bid;
		ColoringBlock cb = null;
		for(i=0;i<100;i++){
			cbname = String.format("DangerZone:Coloring Block %2d", i);
			bid = Blocks.lookup(cbname);
			if(bid > 0){
				cb = new ColoringBlock(cbname, String.format("worlds/%s/coloring/coloringblock%2d.png", DangerZone.worldname, i));
				//cb.blockID = bid;
				//Blocks.BlockArray[bid] = cb;
				Blocks.registerBlock(cb);
			}
		}

		Thread ftb = new Thread(new FastBlockTicker());	//Fire up the fast block ticker!
		ftb.start();
		
		Thread nty = new Thread(new NotifyBlockTicker());	//Fire up the notify block ticker!
		nty.start();
		
		Thread itb = new Thread(new BlockTickerThread());	//Fire up the block ticker!
		itb.start();	
		
		Thread its = new Thread(new SpawnerThread());	//Fire up the spawn ticker!
		its.start();	
		

		
		//wait for players!
		while(DangerZone.gameover == 0){
			try {
				sock = myServerSocket.accept();
			} catch (IOException e) {
				return;
			}

			player_list_lock.lock(); 
			
			i = find_player_slot();
			if(i < 0){
				try {
					sock.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				player_list_lock.unlock();
				continue;
			}
			player_list_lock.unlock();
			
			plyr = ServerHooker.getNewPlayerObject(DangerZone.server_world);
			plyr.toClient = sock;
			st = new ServerThread(plyr);
			
			Thread pit = new Thread(st);	//Send the connection off to a thread for processing!
			pit.start();				
		}
		
		
		try {
			myServerSocket.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//just hang out... don't exit...
		while(true){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public boolean addMe(ServerThread st){
		player_list_lock.lock();
		int i = find_player_slot();
		if(i < 0){
			player_list_lock.unlock();
			return false;
		}		
		players[i] = st;
		server_thread_list.add(new ListInt(i));			
		player_list_lock.unlock();
		if(DangerZone.show_server_stats){
			InetAddress ia = st.p.toClient.getInetAddress();
			String ias = ia.getHostAddress();
			System.out.printf("Connection from %s\n", ias);
		}
		return true;
	}
	
	private int find_player_slot(){
		int i;
		for(i=0;i<max_players;i++){
			if(players[i] == null)return i;
		}
		return -1;
	}
	
	public int get_player_count(){
		int i;
		int count = 0;
		player_list_lock.lock(); 
		for(i=0;i<max_players;i++){
			if(players[i] != null)count++;
		}
		player_list_lock.unlock(); 
		return count;
	}
	
	public int get_player_count_unlocked(){
		int i;
		int count = 0;
		for(i=0;i<max_players;i++){
			if(players[i] != null)count++;
		}
		return count;
	}
	
	
	public void sendChunkToAll(Chunk c){
		if(DangerZone.gameover != 0)return;
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index] != null && players[st.index].p.dimension == c.dimension){
				//send to everyone nearby in same dimension, cuz they may have an old copy in their cache...
				int dx = (int) players[st.index].p.posx;
				int dz = (int) players[st.index].p.posz;
				int x = c.chunkX<<4;
				int z = c.chunkZ<<4;
				float dist = (float) Math.sqrt((x-dx)*(x-dx)+(z-dz)*(z-dz));
				if(dist < 512){
					players[st.index].sendChunkToPlayer(c);
				}
			}
		}		
		player_list_lock.unlock();
	}
	
	public void sendBlockToAll(int d, int x, int y, int z, int id, int meta){
		if(DangerZone.gameover != 0)return;
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index] != null && players[st.index].p.dimension == d){
				//send to everyone in same dimension within a reasonable distance, cuz they may have an old copy in their cache...
				int dx = (int) players[st.index].p.posx;
				int dz = (int) players[st.index].p.posz;
				float dist = (float) Math.sqrt((x-dx)*(x-dx)+(z-dz)*(z-dz));
				if(dist < 512){
					players[st.index].sendBlockToPlayer(d, x, y, z, id, meta);
				}
			}
		}		
		player_list_lock.unlock();
	}
	
	public void sendChunkMetaUpdateToAll(Chunk chnk){
		if(DangerZone.gameover != 0)return;
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index] != null && players[st.index].p.dimension == chnk.dimension){
				//send to everyone in same dimension
				players[st.index].sendChunkMetaToPlayer(chnk);
			}
		}		
		player_list_lock.unlock();
	}
	
	public void sendEntityDeathToAll(Entity e){
		if(DangerZone.gameover != 0)return;
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index] != null && players[st.index].p.dimension == e.dimension && e.getHorizontalDistanceFromEntity(players[st.index].p) < DangerZone.entityupdatedist){
				//send to everyone
				players[st.index].sendEntityDeathToPlayer(e.entityID);
			}
		}		
		player_list_lock.unlock();
	}

	public void sendEntityDeathToAllExcept(Player pl, Entity e){
		if(DangerZone.gameover != 0)return;
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index] != null && players[st.index].p != pl && players[st.index].p.dimension == e.dimension && e.getHorizontalDistanceFromEntity(players[st.index].p) < DangerZone.entityupdatedist){
				//send to everyone
				players[st.index].sendEntityDeathToPlayer(e.entityID);
			}
		}		
		player_list_lock.unlock();
	}
	
	public void sendSpawnParticleToAllExcept(Player pl, String s, int hm, int d, double x, double y, double z){
		sendSpawnParticleToAllExcept(pl, s, hm, d, x, y, z, 0);
	}
	
	public void sendSpawnParticleToAllExcept(Player pl, String s, int hm, int d, double x, double y, double z, int bid){
		if(DangerZone.gameover != 0)return;
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index] != null && players[st.index].p != pl && players[st.index].p.dimension == d && players[st.index].p.getDistanceFromEntityCenter(x, y, z) < 256){
				//send to everyone
				players[st.index].sendSpawnParticleToPlayer(s, hm, d, x, y, z, bid);
			}
		}		
		player_list_lock.unlock();
	}
	
	public void sendEntityRemoveToAll(Entity e){
		if(DangerZone.gameover != 0)return;
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index] != null){
				//send to everyone
				players[st.index].sendEntityRemoveToPlayer(e.entityID);
			}
		}		
		player_list_lock.unlock();
	}

	public void sendEntityRemoveToAllExcept(Player pl, Entity e){
		if(DangerZone.gameover != 0)return;
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index] != null && players[st.index].p != pl){
				//send to everyone
				players[st.index].sendEntityRemoveToPlayer(e.entityID);
			}
		}		
		player_list_lock.unlock();
	}
	
	
	public void sendEntityHitToAll(Entity e){
		if(DangerZone.gameover != 0)return;
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index] != null && players[st.index].p.dimension == e.dimension && e.getHorizontalDistanceFromEntity(players[st.index].p) < DangerZone.entityupdatedist){
				//send to everyone
				players[st.index].sendEntityHitToPlayer(e.entityID, e.getHealth());
			}
		}		
		player_list_lock.unlock();
	}
	
	public void sendChatToAll(String s){
		if(DangerZone.gameover != 0)return;
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index] != null){
				//send to everyone
				players[st.index].sendChatToPlayer(s);
			}
		}		
		player_list_lock.unlock();
	}
	
	public void sendChatToAllExcept(String s, Player plr){
		if(DangerZone.gameover != 0)return;
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index] != null && players[st.index].p != plr){
				//send to everyone
				players[st.index].sendChatToPlayer(s);
			}
		}		
		player_list_lock.unlock();
	}
	
	public void sendSongToAll(String s){
		if(DangerZone.gameover != 0)return;
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index] != null){
				//send to everyone
				players[st.index].sendSongToPlayer(s);
			}
		}		
		player_list_lock.unlock();
	}
	
	public void sendCommandToAll(String s){
		if(DangerZone.gameover != 0)return;
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index] != null){
				//send to everyone
				players[st.index].sendCommandToPlayer(s);
			}
		}		
		player_list_lock.unlock();
	}
	
	public void savePlayers(){
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index] != null){
				//Save player!
				//System.out.printf("Player save\n");
				savePlayer(players[st.index].p);
			}
		}		
		player_list_lock.unlock();
	}
	
	public void saveThisPlayer(Player p){
		player_list_lock.lock();
		savePlayer(p);		
		player_list_lock.unlock();
	}
	
	public void sendStatsToPlayer(Player p){
		if(p.server_thread != null)p.server_thread.sendStatsToPlayer();
	}
	
	public boolean loadPlayer(Player p){
		InputStream input = null;
		Properties playerprop = new Properties();
		String filepath = new String();		
		filepath = String.format("worlds/%s/players/%s.dat", DangerZone.worldname, p.myname);	
		boolean isnew = false;
		try {	 
			input = new FileInputStream(filepath);
	 
			// load a properties file
			playerprop.load(input);

		} catch (IOException ex) {
			//ex.printStackTrace();
			input = null;
			isnew = true;
		}
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		p.readSelf(playerprop, "player:");
		return isnew;
	}
	
	
	public void savePlayer(Player p){
		Properties playerprop = new Properties();
		FileOutputStream output = null;
		String filepath = new String();		
		filepath = String.format("worlds/%s/players/%s.dat", DangerZone.worldname, p.myname);	
		File f = new File(filepath);		
		f.getParentFile().mkdirs();	
		
		p.writeSelf(playerprop, "player:");
		
		try {	 
			output = new FileOutputStream(filepath);	 

			// save properties
			playerprop.store(output, null);
	 
		} catch (IOException io) {
			io.printStackTrace();
		}
		if (output != null) {
			try {
				output.flush();
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	
	public void sendEntityUpdateToAll(Entity e, boolean force){
		if(DangerZone.gameover != 0)return;
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			//If close enough for the player to care...
			//if(players[st.index] != null && players[st.index].p.dimension == e.dimension && e.getDistanceFromEntity(players[st.index].p) < DangerZone.entityupdatedist){
			if(!force){
				if(players[st.index] != null && players[st.index].p.dimension == e.dimension && e.getDistanceFromEntity(players[st.index].p) < e.maxrenderdist+8){
					//send
					players[st.index].sendEntityUpdateToPlayer(e);
				}
			}else{
				if(players[st.index] != null){
					//send
					e.changed = 1; //so it gets past overload filter...
					players[st.index].sendEntityUpdateToPlayer(e);
					//System.out.printf("forced %s to %d, %f, %f, %f\n", e.uniquename, e.dimension, e.posx, e.posy, e.posz);
				}
			}
		}		
		player_list_lock.unlock();
		//Now that they are all updated, clear the entity change flags!
		if(e.changed != 0){
			e.changed = 0;
			for(int i=0;i<e.maxinv;i++){
				e.changes[i] = 0;
			}				
		}
	}
	
	public void sendPlayerUpdateToAllExcept(Entity e, Player p, boolean forceall){
		if(DangerZone.gameover != 0)return;
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(!forceall){
				if(players[st.index] != null && players[st.index].p != p && players[st.index].p.dimension == e.dimension && e.getHorizontalDistanceFromEntity(players[st.index].p) < DangerZone.entityupdatedist){
					//send
					players[st.index].sendEntityUpdateToPlayer(e);
					//players[st.index].flushSend();
				}
			}else{
				//probably a player changing dimension.... let everyone everywhere know!
				if(players[st.index] != null && players[st.index].p != p){
					//send
					e.changed = 1; //so it gets past overload filter...
					players[st.index].sendEntityUpdateToPlayer(e);
					//players[st.index].flushSend();
				}
			}
		}		
		player_list_lock.unlock();
		//Now that they are updated, clear the entity change flags!
		if(e.changed != 0){
			e.changed = 0;
			for(int i=0;i<e.maxinv;i++){
				e.changes[i] = 0;
			}				
		}
	}
	
	public void sendPlayerActionToAllExcept(Player p, int which, int what){
		if(DangerZone.gameover != 0)return;
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index] != null && players[st.index].p != p && players[st.index].p.dimension == p.dimension && p.getHorizontalDistanceFromEntity(players[st.index].p) < DangerZone.entityupdatedist){
				//send to everyone, cuz they may have an old copy in their cache...
				players[st.index].sendPlayerAction(p, which, what);
			}
		}		
		player_list_lock.unlock();
	}
	
	public void sendSpawnEntityToAll(Entity e){
		if(DangerZone.gameover != 0)return;
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index] != null && players[st.index].p.dimension == e.dimension && e.getHorizontalDistanceFromEntity(players[st.index].p) < DangerZone.entityupdatedist){
				//send to everyone in same dimension
				players[st.index].sendSpawnEntityToPlayer(e);
			}
		}		
		player_list_lock.unlock();
	}
	
	public void sendTimeToAll(int t, int l){
		if(DangerZone.gameover != 0)return;
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index] != null)players[st.index].sendTimeToPlayer(t, l);
		}		
		player_list_lock.unlock();
	}
	
	public void sendBlockToAllExcept(ServerThread stp, int d, int x, int y, int z, int id, int meta){
		if(DangerZone.gameover != 0)return;
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index] != null && players[st.index] != stp){
				if(players[st.index].p.dimension == d){
					//send to everyone in same dimension, cuz they may have an old copy in their cache...
					int dx = (int) players[st.index].p.posx;
					int dz = (int) players[st.index].p.posz;
					float dist = (float) Math.sqrt((x-dx)*(x-dx)+(z-dz)*(z-dz));
					if(dist < 512){
						players[st.index].sendBlockToPlayer(d, x, y, z, id, meta);
					}
				}
			}
		}		
		player_list_lock.unlock();
	}
	
	public void flushAll(){
		if(DangerZone.gameover != 0)return;
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		int i;
		while(ii.hasNext()){
			i = ii.next().index;
			if(players[i] != null)players[i].flushSend();
		}		
		player_list_lock.unlock();
	}
	
	public Player getRandomPlayer(Random rand){
		Player p = null;
		player_list_lock.lock();
		if(server_thread_list.size() != 0){
			if(server_thread_list.size() == 1){
				p = players[server_thread_list.get(0).index].p;
			}else{
				int i = rand.nextInt(server_thread_list.size());
				p = players[server_thread_list.get(i).index].p;
			}
		}
		player_list_lock.unlock();
		return p;		
	}
	
	public boolean isAPlayerInDimension(int d){
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index].p.dimension == d){
				player_list_lock.unlock();
				return true;
			}
		}		
		player_list_lock.unlock();
		return false;		
	}
	
	public boolean isPlayerCloseInDimension(Entity e){
		if(DangerZone.start_client){
			if(e.dimension == DangerZone.player.dimension && e.getHorizontalDistanceFromEntity(DangerZone.player) < DangerZone.entityupdatedist){
				return true;
			}
			return false;
		}
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index].p.dimension == e.dimension){
				if(e.getHorizontalDistanceFromEntity(players[st.index].p) < DangerZone.entityupdatedist){
					player_list_lock.unlock();
					return true;
				}
			}
		}		
		player_list_lock.unlock();
		return false;		
	}
	
	public boolean isPlayerCloseInDimension(Entity e, float dist){
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index].p.dimension == e.dimension){
				if(e.getDistanceFromEntity(players[st.index].p) < dist){
					player_list_lock.unlock();
					return true;
				}
			}
		}		
		player_list_lock.unlock();
		return false;		
	}
	
	public void removeMe(ServerThread stp){
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index] == stp){
				ii.remove();
				players[st.index] = null;
				break;
			}
		}		
		player_list_lock.unlock();
	}
	
	public Player findNearestPlayer(Entity e){
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		double dist = 999.0f;
		Player p = null;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index].p.dimension == e.dimension){
				if(e.getDistanceFromEntity(players[st.index].p) < dist){
					p = players[st.index].p;
					dist = e.getDistanceFromEntity(players[st.index].p);					
				}
			}
		}		
		player_list_lock.unlock();
		return p;		
	}
	
	public Player findNearestPlayerToHere(int d, int x, int y, int z){
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		double dist = 999.0f;
		Player p = null;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index].p.dimension == d){
				double dx, dy, dz;
				double curdist;
				dx = players[st.index].p.posx - x;
				dy = players[st.index].p.posy - y;
				dz = players[st.index].p.posz - z;
				curdist =  Math.sqrt((dx*dx)+(dy*dy)+(dz*dz));
				if(curdist < dist){
					p = players[st.index].p;
					dist = curdist;					
				}
			}
		}		
		player_list_lock.unlock();
		return p;		
	}
	
	public double distToNearestPlayerFromHere(int d, int x, int y, int z){
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		double dist = 999.0f;
	
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index].p.dimension == d){
				double dx, dy, dz;
				double curdist;
				dx = players[st.index].p.posx - x;
				dy = players[st.index].p.posy - y;
				dz = players[st.index].p.posz - z;
				curdist = (float) Math.sqrt((dx*dx)+(dy*dy)+(dz*dz));
				if(curdist < dist){
					dist = curdist;					
				}
			}
		}		
		player_list_lock.unlock();
		return dist;		
	}
	
	public double findNearestPlayerDistToHere(int d, int x, int z){
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		double dist = 9999.0f;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index].p.dimension == d){
				double dx, dz;
				double curdist;
				dx = players[st.index].p.posx - x;
				dz = players[st.index].p.posz - z;
				curdist = (float) Math.sqrt((dx*dx)+(dz*dz));
				if(curdist < dist){
					dist = curdist;					
				}
			}
		}		
		player_list_lock.unlock();
		return dist;		
	}
	
	public void sendSoundToAllExcept(Player pl, String name, int pd, double px, double py, double pz, float vol, float freq){
		if(DangerZone.gameover != 0)return;
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index] != null && players[st.index].p != pl && players[st.index].p.dimension == pl.dimension && pl.getDistanceFromEntity(players[st.index].p) < DangerZone.entityupdatedist){
				players[st.index].sendSound(name, pd, px, py, pz, vol, freq);
			}
		}		
		player_list_lock.unlock();
	}
	
	public void sendSoundToAll(String name, int pd, double px, double py, double pz, float vol, float freq){
		if(DangerZone.gameover != 0)return;
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index] != null && players[st.index].p.dimension == pd && players[st.index].p.getDistanceFromEntityCenter(px, py, pz) < DangerZone.entityupdatedist){
				players[st.index].sendSound(name, pd, px, py, pz, vol, freq);
			}
		}		
		player_list_lock.unlock();
	}
	
	
	public void sendColoringBlockToAll(String name, int bid, float colordata[][][]){
		if(DangerZone.gameover != 0)return;
		player_list_lock.lock();
		Iterator<ListInt> ii = server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(players[st.index] != null){
				players[st.index].sendColoringBlock(name, bid, colordata, true);
			}
		}		
		player_list_lock.unlock();
	}
	
	public int find_privs(String pln){
		priv_lock.lock();
		Properties privprop = new Properties();
		InputStream privinput = null;
		int privs = DangerZone.default_privs;
		String filepath = new String();		
		filepath = String.format("worlds/%s/player_privs.dat", DangerZone.worldname);	
		File f = new File(filepath);		
		f.getParentFile().mkdirs();	
		
		try {
			privinput = new FileInputStream(filepath);	
			privprop.load(privinput);	
			privinput.close();
			
			privs = Utils.getPropertyInt(privprop, pln.toLowerCase(), 0, 0xffffffff, DangerZone.default_privs);

		} catch (IOException io) {
			//io.printStackTrace();
		}
				
		priv_lock.unlock();
		return privs;
	}
	
	public void save_privs(String pln, int privs){
		priv_lock.lock();
		Properties privprop = new Properties();
		OutputStream privoutput = null;
		InputStream privinput = null;
		String filepath = new String();		
		filepath = String.format("worlds/%s/player_privs.dat", DangerZone.worldname);	
		File f = new File(filepath);		
		f.getParentFile().mkdirs();	
		
		try {
			privinput = new FileInputStream(filepath);	
			privprop.load(privinput);	
			privinput.close();
		} catch (IOException io) {
			//io.printStackTrace();
		}
		
		try {
			privprop.setProperty(pln.toLowerCase(), String.format("%d", privs));
			
			privoutput = new FileOutputStream(filepath);	 
			// save properties
			privprop.store(privoutput, null);	
			privoutput.close();
		} catch (IOException io) {
			io.printStackTrace();
		}
				
		priv_lock.unlock();

	}
	
	public boolean isBanned(String pln){
		priv_lock.lock();
		Properties privprop = new Properties();
		InputStream privinput = null;
		String filepath = new String();		
		filepath = String.format("worlds/%s/player_bans.dat", DangerZone.worldname);	
		File f = new File(filepath);		
		f.getParentFile().mkdirs();	
		boolean isnotok = false;
		
		try {
			privinput = new FileInputStream(filepath);	
			privprop.load(privinput);	
			privinput.close();
			
			isnotok = Utils.getPropertyBoolean(privprop, pln.toLowerCase(), false);

		} catch (IOException io) {
			//io.printStackTrace();
		}
				
		priv_lock.unlock();
		if(DangerZone.private_server)isnotok = !isnotok; //banned list is actually white list for private server!
		return isnotok;
	}
	
	//banned list is actually white list for private server!
	public void setBanned(String pln, boolean banstate){
		priv_lock.lock();
		Properties privprop = new Properties();
		OutputStream privoutput = null;
		InputStream privinput = null;
		String filepath = new String();		
		filepath = String.format("worlds/%s/player_bans.dat", DangerZone.worldname);	
		File f = new File(filepath);		
		f.getParentFile().mkdirs();	
		
		try {
			privinput = new FileInputStream(filepath);	
			privprop.load(privinput);	
			privinput.close();
		} catch (IOException io) {
			//io.printStackTrace();
		}
		
		try {
			privprop.setProperty(pln.toLowerCase(), banstate?"true":"false");
			
			privoutput = new FileOutputStream(filepath);	 
			// save properties
			privprop.store(privoutput, null);	
			privoutput.close();
		} catch (IOException io) {
			io.printStackTrace();
		}
				
		priv_lock.unlock();

	}
	
	
}
