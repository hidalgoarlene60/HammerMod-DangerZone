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

import dangerzone.DangerZone;
import dangerzone.ListInt;
import dangerzone.Player;
import dangerzone.ServerHooker;
import dangerzone.Swarm;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityLiving;
import dangerzone.entities.Flag;


public class ServerEntityUpdateLoop  implements Runnable   {
	

	public  List<ListInt> entity_list; 	// a list of little things that index into
	public  List<ListInt> entity_free_list; 	// a list of little things that index into
	public Entity entities[];           // an array of big things that we don't want Java to copy!
	public  Lock entity_list_lock = new ReentrantLock();
	public long upticker = 1;
	public int active_entities;

	public void run()  {
		long lasttime = System.currentTimeMillis();
		long currtime, tlong;
		long loop_interval = DangerZone.serverentityupdaterate; //millisecs
		float deltaT;
		int inext = 0;
		Entity ent;
		int j;
		int ent_counter;
		//boolean doprint = false;
		
		entity_list_lock.lock();
		entities = new Entity[DangerZone.max_entities];
		entity_list = new ArrayList<ListInt>();
		entity_free_list = new ArrayList<ListInt>();
		//make a free list
		for(j=1;j<DangerZone.max_entities;j++){ //zero is not valid!
			entity_free_list.add(new ListInt(j));
		}
		entity_list_lock.unlock();
		
		while(true){
			if(DangerZone.gameover != 0)return;
			
			currtime = System.currentTimeMillis();
			tlong = currtime - lasttime;
			tlong = loop_interval - tlong;
			if(tlong < 0)tlong = 0;
			if(tlong > loop_interval)tlong = loop_interval;
			if(tlong > 0){
				try {
					//System.out.printf("tlong = %d\n",  (int)tlong);
					Thread.sleep(tlong);
				} catch (InterruptedException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
					System.exit(1);
				}
				currtime = System.currentTimeMillis();
			}
			tlong = currtime - lasttime;
			lasttime = currtime;
			deltaT = tlong;
			//System.out.printf("DeltaT = %f\n", deltaT/100f);
			if(deltaT < 100)deltaT = 100;
			if(deltaT > 500)deltaT = 500;
			deltaT = deltaT / loop_interval; //Scale!
			
			if(DangerZone.start_client){
				int tmp = DangerZone.renderdistance; //chunks
				tmp += 4;
				if(tmp > 24)tmp = 24;
				DangerZone.entityupdatedist = tmp * 16; //blocks
			}
			
			inext = 0;
			ent_counter = 0;
			
			if(DangerZone.f12_on){
				DangerZone.server.flushAll(); //flush out any stray updates!
				continue; //PAUSED
			}
			
			//Oh yeah... swarm time! I hope...
			Swarm.doSpawnSwarm();
			
			ServerHooker.entity_loop_start(deltaT);
			
			for(int i=0;i<DangerZone.server.max_players;i++){
				if(DangerZone.server.players[i] != null){
					if(DangerZone.server.players[i].p != null){
						ServerHooker.player_entity_loop_start(DangerZone.server.players[i].p);
					}
				}
			}
			
			
			for(inext = 0;inext < DangerZone.max_entities;inext++){
			
				ent = entities[inext];
				if(ent != null){
					if(ent.deadflag){
						removeEntityByID(inext);
						DangerZone.server.sendEntityRemoveToAll(ent);
						continue;
					}
				}
				
				//Inactive Entities are marked for removal from the list by the CHUNK save logic.
				if(ent != null){
					//if(DangerZone.start_client && doprint)System.out.printf("Entity %s,  dist %f\n", ent.uniquename, ent.getHorizontalDistanceFromEntity(DangerZone.player));
					if(DangerZone.server.isPlayerCloseInDimension(ent)){
						ServerHooker.entity_action(ent, deltaT);
						ent.doEntityAction(deltaT);
						ent.doEntityCollisions(deltaT);
						ent.update(deltaT);
						ent_counter++;
					}else{
						if(!DangerZone.server_chunk_cache.chunkExists(ent.dimension, (int)ent.posx, 100, (int)ent.posz)){
							//System.out.printf("stray entity needs saving: %s @ %f, %f\n", ent.uniquename, ent.posx, ent.posz);
							//let the chunkwriter READ the chunk... the cleaner thread will eventually save it along with the entity...
							DangerZone.chunkwriter.addEntity(ent);
						}
					}
					if(ent instanceof Player){
						ent.stray_entity_ticker++; //player timeout in case server thread doesn't detect disconnect
						if(ent.stray_entity_ticker > 300){ //30 seconds is more than enough!
							Player plyr = (Player)ent;
							plyr.server_thread.fatal_error = 1;
							removeEntityByID(inext);
							DangerZone.server.removeMe(plyr.server_thread);
							DangerZone.server.sendEntityRemoveToAllExcept(plyr, ent);
						}
					}
				}

			}
			active_entities = ent_counter;
			DangerZone.server.flushAll(); //flush out any stray updates!
			upticker++;
		}

	}
	
	private int find_entity_slot(){
		int retval = -1;
		if(entity_free_list.size() > 0){
			ListInt il = entity_free_list.get(0);
			retval = il.index;
			entity_free_list.remove(0);
		}
		return retval;
	}
	
	public int addEntity(Entity e){
		int i;
		entity_list_lock.lock(); 
		i = find_entity_slot();
		if(i <= 0){
			e.entityID = -1;
			entity_list_lock.unlock();
			return -1;
		}
		entities[i] = e;
		e.entityID = i;
		entity_list.add(new ListInt(i));			
		entity_list_lock.unlock();
		return i;
	}
	
	
	public Entity[] getEntitiesInChunk(int dim, int cx, int cz){
		entity_list_lock.lock();
		Entity[] retlist = new Entity[DangerZone.max_entities];
		Iterator<ListInt> ii = entity_list.iterator();
		ListInt st;
		int i;
		for(i=0;i<DangerZone.max_entities;i++){
			retlist[i] = null;
		}
		while(ii.hasNext()){
			st = ii.next();
			if(entities[st.index] != null && entities[st.index].dimension == dim && ((int)entities[st.index].posx)>>4 == cx && ((int)entities[st.index].posz)>>4 == cz){
				retlist[st.index] = entities[st.index];
			}
		}		
		entity_list_lock.unlock();

		return retlist;
	}
	
	public boolean areEntitiesInChunk(int dim, int cx, int cz){
		entity_list_lock.lock();
		Iterator<ListInt> ii = entity_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(entities[st.index] != null && entities[st.index].dimension == dim && ((int)entities[st.index].posx)>>4 == cx && ((int)entities[st.index].posz)>>4 == cz){
				entity_list_lock.unlock();
				return true;
			}
		}		
		entity_list_lock.unlock();
		return false;
	}
	
	public Entity findEntityByID(int eid){
		Entity ent = null;
		if(eid == 0)return null;
		entity_list_lock.lock();
		Iterator<ListInt> ii = entity_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(entities[st.index] != null && entities[st.index].entityID == eid){
				ent = entities[st.index];
				break;
			}
		}		
		entity_list_lock.unlock();
		return ent;
	}
	
	public Entity findPlayerByName(String playername){
		if(playername == null)return null;
		Entity ent = null;
		entity_list_lock.lock();
		Iterator<ListInt> ii = entity_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(entities[st.index] != null && entities[st.index] instanceof Player){
				Player p = (Player)entities[st.index];
				if(p.myname.toLowerCase().equals(playername.toLowerCase())){
					ent = entities[st.index];
					break;
				}
			}
		}		
		entity_list_lock.unlock();
		return ent;
	}
	
	public int removeEntityByID(int eid){
		entity_list_lock.lock();
		Iterator<ListInt> ii = entity_list.iterator();
		ListInt st;
		int retid = -1;
		while(ii.hasNext()){
			st = ii.next();
			if(entities[st.index] != null && entities[st.index].entityID == eid){
				entities[st.index] = null;
				ii.remove();
				entity_free_list.add(new ListInt(st.index));
				retid = eid;
				break;
			}
		}		
		entity_list_lock.unlock();
		return retid;
	}
	
	//Finds EntityLiving entities within range
	public List<Entity> findEntitiesInRange(float range, int dim, double x, double y, double z){
		entity_list_lock.lock();
		List<Entity> retlist = new ArrayList<Entity>();
		Iterator<ListInt> ii = entity_list.iterator();
		ListInt st;
		double dx, dy, dz;
		while(ii.hasNext()){
			st = ii.next();
			if(entities[st.index] != null && entities[st.index].dimension == dim){
				dx = x - entities[st.index].posx;
				dy = y - entities[st.index].posy;
				dz = z - entities[st.index].posz;
				dx = Math.sqrt((dx*dx)+(dy*dy)+(dz*dz));
				//Flags attract all hostiles in dimension.
				if((dx < range && entities[st.index] instanceof EntityLiving)||(entities[st.index] instanceof Flag)){
					retlist.add(entities[st.index]);
				}
			}
		}		
		entity_list_lock.unlock();

		return retlist;
	}
	
	//Finds ALL kinds of entities in range
	public List<Entity> findALLEntitiesInRange(float range, int dim, double x, double y, double z){
		entity_list_lock.lock();
		List<Entity> retlist = new ArrayList<Entity>();
		Iterator<ListInt> ii = entity_list.iterator();
		ListInt st;
		double dx, dy, dz;
		while(ii.hasNext()){
			st = ii.next();
			if(entities[st.index] != null && entities[st.index].dimension == dim){
				dx = x - entities[st.index].posx;
				dy = y - entities[st.index].posy;
				dz = z - entities[st.index].posz;
				dx = Math.sqrt((dx*dx)+(dy*dy)+(dz*dz));
				if(dx < range){
					retlist.add(entities[st.index]);
				}
			}
		}		
		entity_list_lock.unlock();

		return retlist;
	}

}
