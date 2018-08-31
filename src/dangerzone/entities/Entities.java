package dangerzone.entities;
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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import dangerzone.DangerZone;
import dangerzone.ModelBase;
import dangerzone.World;


public class Entities {
	
	public static class ElistInfo {   
		public Class<? extends Entity> entclass;
		public ModelBase model;
		public String uniquename;
	}	
	public static List<ElistInfo> entities = new ArrayList<ElistInfo>();
	public static Lock entity_list_lock = new ReentrantLock();
	
	public static void registerEntity(Class<? extends Entity> entityClass, String name, ModelBase model){
		entity_list_lock.lock();
		ElistInfo el = new ElistInfo();
		el.uniquename = name;
		el.entclass = entityClass;
		el.model = model;
		entities.add(el);
		entity_list_lock.unlock();
	}
	
	public static Class<? extends Entity> findEntityByName(String name){
		entity_list_lock.lock();
		Iterator<ElistInfo> ii = entities.iterator();
		ElistInfo st;
		while(ii.hasNext()){
			st = ii.next();
			if(st.uniquename.equals(name)){
				entity_list_lock.unlock();
				return st.entclass;
			}
		}
		entity_list_lock.unlock();
		return null;
	}
	
	public static ElistInfo findElistInfoByName(String name){
		entity_list_lock.lock();
		Iterator<ElistInfo> ii = entities.iterator();
		ElistInfo st;
		while(ii.hasNext()){
			st = ii.next();
			if(st.uniquename.equals(name)){
				entity_list_lock.unlock();
				return st;
			}
		}
		entity_list_lock.unlock();
		return null;
	}
	
	public static Entity spawnEntityByName(String name, World w){
		ElistInfo st;
		Entity ent = null;

		st = findElistInfoByName(name);
		if(st != null && st.entclass != null){
			try {
				ent =  st.entclass.getConstructor(World.class).newInstance(w);	
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
			//Set the renderer!
			ent.model = st.model;			
		}
		return ent;

	}
	
	public static Entity random_entity(){
		Entity retent = null;
		entity_list_lock.lock();
		int len = entities.size();
		if(len <= 0){
			entity_list_lock.unlock();
			return retent;
		}
		if(len > 1)len = DangerZone.rand.nextInt(len)+1;
		retent = spawnEntityByName(entities.get(len-1).uniquename, null);
		entity_list_lock.unlock();
		return retent;
	}
	
	public static Entity random_livingentity(){
		Entity retent = null;
		entity_list_lock.lock();
		while(retent == null){
			int len = entities.size();
			if(len <= 0){
				entity_list_lock.unlock();
				return retent;
			}
			if(len > 1)len = DangerZone.rand.nextInt(len)+1;
			retent = spawnEntityByName(entities.get(len-1).uniquename, null);
			if(!(retent instanceof EntityLiving)){
				retent = null;
			}
		}
		//System.out.printf("Random Living = %s\n", retent.uniquename);
		entity_list_lock.unlock();
		return retent;
	}

}
