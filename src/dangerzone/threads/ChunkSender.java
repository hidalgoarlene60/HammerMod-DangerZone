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


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import dangerzone.Chunk;
import dangerzone.Coords;
import dangerzone.DangerZone;
import dangerzone.Player;


/*
 * Send chunks out to the player on a server. Single-player has pass-through straight to cache.
 * Sends from a queue, a little at a time, so as to not overload the network and allow
 * player actions and entity updates plenty of time to happen as well.
 * Because it is delayed, it does a lot of cleanup work to get rid of chunks no longer needed.
 * No reason to send them if no one wants them...
 */
public class ChunkSender implements Runnable {
	
	public Player p;
	public ServerThread st;
	private Lock lock = new ReentrantLock();
	private List<Coords> requested_list;
	
	//start out slow, about 2 mbits/second, for slow connections
	//we don't want slow-connection players clogging up the network buffers
	//and causing lag for other players!
	//8k chunk * 8 bits * 40 =~ 2 mbits 
	
	public int chunksenddelay = 25;
	
	ChunkSender(Player pl, ServerThread srv){
		p = pl;
		st = srv;
		requested_list = new ArrayList<Coords>();
	}
	
	public void run()  {
		double dist, dd, dx, dz;
		Coords cl = null;
		Coords ucl = null;
		int mxdist = 24;
		
		while(DangerZone.gameover == 0 && st.fatal_error == 0){
		
			try {
				Thread.sleep(chunksenddelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			mxdist = 16;
			if(DangerZone.start_client)mxdist = DangerZone.renderdistance;
			mxdist += 4;
			
			dist = 9999.0f;
			lock.lock();
			if(!requested_list.isEmpty()){
				Iterator<Coords> ii = requested_list.iterator();
				
				//find closest to player!
				cl = null;
				ucl = null;
				while(ii.hasNext()){
					cl = (Coords)ii.next();
					dx = p.posx - (cl.x<<4);
					dz = p.posz - (cl.z<<4);
					dd = (float) Math.sqrt((dx*dx) + (dz*dz));
					if(dd < dist && cl.d == p.dimension){
						dist = dd;
						ucl = cl;
					}
				}
				
				//Send it!
				if(ucl != null){
					if(dist < (mxdist*16)){	
						lock.unlock(); //unlock because this can take a while...
						//Get or make this chunk!!!
						Chunk c = DangerZone.server_world.serverchunkcache.getDecoratedChunk(ucl.d, ucl.x<<4, 1, ucl.z<<4);
						if(c != null){ //should never be null
							st.sendChunkToPlayer(c);
						}
						lock.lock();
					}

					//remove from list
					ii = requested_list.iterator();
					while(ii.hasNext()){
						cl = (Coords)ii.next();
						if(cl == ucl){
							ii.remove();
							break;
						}
					}

				}
				
				//Knock out anything not in the player's current dimension or just too far away...
				boolean found = true;
				while(found){
					found = false;
					ii = requested_list.iterator();
					while(ii.hasNext()){
						cl = (Coords)ii.next();
						if(cl.d != p.dimension){
							ii.remove();
							found = true;
							break;
						}
						dx = p.posx - (cl.x<<4);
						dz = p.posz - (cl.z<<4);
						dd = (float) Math.sqrt((dx*dx) + (dz*dz));
						if(dd > mxdist*16){ // a little over max render dist...
							ii.remove();
							found = true;
							break;
						}						
					}					
				}
			}
			cl = null;
			ucl = null;
			lock.unlock();
			
		}
	
	}
	
	public void addCoords(Coords cl){
		lock.lock();
		//filter out duplicates!
		Iterator<Coords> ii = requested_list.iterator();
		Coords tcl = null;
		while(ii.hasNext()){
			tcl = (Coords)ii.next();
			if(tcl.d == cl.d && tcl.x == cl.x && tcl.z == cl.z){
				lock.unlock();
				return; //already on the list
			}		
		}	
		requested_list.add(cl);
		lock.unlock();
	}
	

}
