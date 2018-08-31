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
import dangerzone.entities.Entity;


public class EntityUpdateLoop  implements Runnable   {

	public  List<ListInt> entity_list; 	// a list of little things that index into
	public Entity entities[];           // an array of big things that we don't want Java to copy!

	public  Lock entity_list_lock = new ReentrantLock();

	public void run()  {
		long lasttime = System.currentTimeMillis();
		long currtime, tlong;
		long loop_interval = DangerZone.entityupdaterate; //millsecs
		float deltaT;
		int inext = 0;
		entities = new Entity[DangerZone.max_entities];
		entity_list = new ArrayList<ListInt>();
		Entity ent = null;
		int usedist = DangerZone.entityupdatedist;

		while(true){
			if(DangerZone.gameover != 0)return;
			
			currtime = System.currentTimeMillis();
			tlong = currtime - lasttime;
			tlong = loop_interval - tlong;
			if(tlong < 0)tlong = 0;
			if(tlong > loop_interval)tlong = loop_interval;
			if(tlong > 0){
				try {
					Thread.sleep(tlong);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
					System.exit(1);
				}
				currtime = System.currentTimeMillis();
			}
			tlong = currtime - lasttime;
			lasttime = currtime;
			deltaT = tlong;
			if(deltaT < 16)deltaT = 16;
			if(deltaT > 200)deltaT = 200;
			deltaT = deltaT / loop_interval; //Scale!
			usedist = DangerZone.renderdistance+4;
			if(usedist > 24)usedist = 24;
			usedist *= 16;
			
			inext = 0;
			
			if(DangerZone.f12_on)continue; //PAUSED
			
			for(inext = 0;inext < DangerZone.max_entities;inext++){
						
				ent = entities[inext];
				if(DangerZone.new_dimension != 0)break; //just wait until we are settled!
				if(DangerZone.dimension_change_in_progress)break; //just wait until we are settled! (single-player)
				
				if(ent != null){
					ent.stray_entity_ticker++;
					if(ent.deadflag && !ent.isDying()){
						//System.out.printf("dead entity removed\n");
						removeEntityByID(inext);
						
					}else{
						//update this little bugger! (maybe)
						//Entity list must be UNLOCKED during updates to prevent a deadlock with chunk loader wanting to ADD entities to the list!
						//This is a problem only in single-player because it directly uses the server chunk cache!
						if(ent.dimension == DangerZone.player.dimension && ent.stray_entity_ticker < 300 && ent.getHorizontalDistanceFromEntity(DangerZone.player) < usedist){
							if(ent.entityID != DangerZone.player.entityID){ //Do NOT update self. This is done in the main loop!
								ent.smoothMotion(); //adjust the difference between where we think it is and where the server thinks it is.
								ent.update(deltaT); //mostly all this does is pre-defined motion. Actual entity control is on the server.	
							}
						}else{
							//remove things that are too far away to care.
							//server will send us updates if we get close again.
							//give the stray ticker a little time, as there may be a teleport in progress...
							if(ent.deadflag || ent.stray_entity_ticker > 300 || (ent.dimension != DangerZone.player.dimension)){ 
								removeEntityByID(ent.entityID);	
							}
							ent.setMotion(); //just set it so that it can possibly move into range
						}
					}
				}

				if((inext%10) == 0)Thread.yield();		//Let the packet processors have a go at the list between entity updates.
				
			}
			
		}

	}
	
	//Add where server told us to!
	public void addEntity(Entity e, int entytid){
		if(entytid < 0 || entytid >= DangerZone.max_entities )return;
		entity_list_lock.lock(); 
		
		if(entities[entytid] != null){
			removeEntityByID(entytid);
		}
		entities[entytid] = e;
		e.entityID = entytid;
		entity_list.add(new ListInt(entytid));	
		
		entity_list_lock.unlock();
		return;
	}
	
	public void removeEntityByID(int eid){
		entity_list_lock.lock();
		Iterator<ListInt> ii = entity_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(entities[st.index] != null && entities[st.index].entityID == eid){
				ii.remove();
				entities[st.index] = null;
				break;
			}
		}		
		entity_list_lock.unlock();
	}
	
	public Entity findEntityByID(int eid){
		if(eid < 0 || eid >= DangerZone.max_entities )return null;
		return entities[eid];
	}
	
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
				dx =  Math.sqrt((dx*dx)+(dy*dy)+(dz*dz));
				if(dx < range){
					retlist.add(entities[st.index]);
				}
			}
		}		
		entity_list_lock.unlock();

		return retlist;
	}

}
