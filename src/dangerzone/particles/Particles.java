package dangerzone.particles;
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

import dangerzone.ModelBase;


public class Particles {
	
	public static class PlistInfo {   
		public Class<? extends Particle> entclass;
		public ModelBase model;
		public String uniquename;
	}	
	public static List<PlistInfo> particles = new ArrayList<PlistInfo>();
	public static Lock Particle_list_lock = new ReentrantLock();
	public static ModelParticle mp = new ModelParticle();

	public static void registerParticle(Class<? extends Particle> ParticleClass, String name){
		Particle_list_lock.lock();
		PlistInfo el = new PlistInfo();
		el.uniquename = name;
		el.entclass = ParticleClass;
		el.model = mp;
		particles.add(el);
		Particle_list_lock.unlock();
	}
	
	public static void registerParticle(Class<? extends Particle> ParticleClass, String name, ModelBase model){
		Particle_list_lock.lock();
		PlistInfo el = new PlistInfo();
		el.uniquename = name;
		el.entclass = ParticleClass;
		el.model = model;
		particles.add(el);
		Particle_list_lock.unlock();
	}
	
	public static Class<? extends Particle> findParticleByName(String name){
		Particle_list_lock.lock();
		Iterator<PlistInfo> ii = particles.iterator();
		PlistInfo st;
		while(ii.hasNext()){
			st = ii.next();
			if(st.uniquename.equals(name)){
				Particle_list_lock.unlock();
				return st.entclass;
			}
		}
		Particle_list_lock.unlock();
		return null;
	}
	
	public static PlistInfo findPlistInfoByName(String name){
		Particle_list_lock.lock();
		Iterator<PlistInfo> ii = particles.iterator();
		PlistInfo st;
		while(ii.hasNext()){
			st = ii.next();
			if(st.uniquename.equals(name)){
				Particle_list_lock.unlock();
				return st;
			}
		}
		Particle_list_lock.unlock();
		return null;
	}
	
	public static Particle spawnParticleByName(String name){
		PlistInfo st;
		Particle ent = null;

		st = findPlistInfoByName(name);
		if(st != null && st.entclass != null){
			try {
				//ent =  (Particle)newentClass.getConstructor(new Class[] {World.class}).newInstance(new Object[]{w});	
				ent =  st.entclass.getConstructor().newInstance();	
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

}
