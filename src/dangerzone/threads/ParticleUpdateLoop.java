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
import dangerzone.Utils;
import dangerzone.particles.Particle;
import dangerzone.particles.ParticleDroplet;
import dangerzone.particles.ParticleRain;



public class ParticleUpdateLoop  implements Runnable   {

	public  List<Particle> particle_list; 	// a list of little things that index into

	public  Lock particle_list_lock = new ReentrantLock();

	public void run()  {
		long lasttime = System.currentTimeMillis();
		long currtime, tlong;
		long loop_interval = DangerZone.entityupdaterate; //millsecs
		float deltaT;
		particle_list = new ArrayList<Particle>();
		Particle ent = null;
		int inext = 0;
		
		//sleep just a little!
		try {
			Thread.sleep(1500);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
			System.exit(1);
		}

		while(true){
			if(DangerZone.gameover != 0)return;
			//Cache cleanup!
			
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
			
			inext = 0;
			
			if(DangerZone.f12_on)continue; //PAUSED
			
			while(true){
				particle_list_lock.lock();			
				if(inext >= particle_list.size()){
					particle_list_lock.unlock();
					break; //done
				}			
				ent = particle_list.get(inext);				
				particle_list_lock.unlock();
				
				if(ent != null){
					ent.update(deltaT);
					if(ent.lifetimeticker > ent.maxlifetime || ent.dimension != DangerZone.player.dimension || ent.posy < 0 || ent.posy > 255){
						particle_list_lock.lock();	
						Iterator<Particle> li = particle_list.iterator();
						while(li.hasNext()){
							if(ent == li.next()){
								li.remove();
								break;
							}
						}
						particle_list_lock.unlock();
					}				
				}
				Thread.yield();		//Let the packet processors have a go at the list between entity updates.
				inext++;
			}
			
			add_background_particles();
			
		}

	}
	
	//Add a new particle!
	public void addParticle(Particle e){
		//horizontal distance only, because rain...
		float dist = (float)Math.sqrt((e.posx-DangerZone.player.posx)*(e.posx-DangerZone.player.posx) + (e.posz-DangerZone.player.posz)*(e.posz-DangerZone.player.posz));
		if(dist < e.maxrenderdist*2){ //not too far
			particle_list_lock.lock();
			int sz = particle_list.size();
			int factor = DangerZone.wr.fps;
			if(factor < 10)factor = 10; //throttle particles based on frame rate
			
			if(sz > factor*60){
				if(e instanceof ParticleDroplet){
					if(DangerZone.rand.nextInt(2)==1){
						particle_list_lock.unlock();
						return;
					}
				}
				if(DangerZone.rand.nextInt(2)==1){
					particle_list_lock.unlock();
					return;
				}
			}
			if(sz > factor*80){
				if(e instanceof ParticleDroplet){
					if(DangerZone.rand.nextInt(2)==1){
						particle_list_lock.unlock();
						return;
					}
				}
				if(e instanceof ParticleRain){
					if(DangerZone.rand.nextInt(2)==1){
						particle_list_lock.unlock();
						return;
					}
				}
				if(DangerZone.rand.nextInt(2)==1){
					particle_list_lock.unlock();
					return;
				}
			}
			if(sz > factor*100){
				if(e instanceof ParticleDroplet){
					particle_list_lock.unlock();
					return;
				}
				if(e instanceof ParticleRain){
					particle_list_lock.unlock();
					return;
				}
				if(DangerZone.rand.nextInt(2)==1){
					particle_list_lock.unlock();
					return;
				}
			}			
			if(sz < factor*120){ //not too many!		
				particle_list.add(e);			
			}		
			particle_list_lock.unlock();
		}
		return;
	}
	

	
	private void add_background_particles(){
		//add some random background eye-candy...
		if(DangerZone.rand.nextInt(25) != 0)return;

		String str = "DangerZone:ParticleSparkle";
		float dist = 8 + (DangerZone.rand.nextFloat() * DangerZone.renderdistance*12);
		float dir = (float) (DangerZone.rand.nextFloat()*Math.PI*2);
		double posx = DangerZone.player.posx + Math.sin(dir)*dist;
		double posz = DangerZone.player.posz + Math.cos(dir)*dist;
		double posy = -1;
		
		for(int i=250; i>0 ; i--){
			if(DangerZone.player.world.getblock(DangerZone.player.dimension, (int)posx, i, (int)posz) != 0){
				posy = i+1;
				break;
			}
		}
		
		if(posy > 0)Utils.spawnParticles(DangerZone.player.world, str, 5, DangerZone.player.dimension, posx, posy, posz, false);
		
		
	}
	


}
