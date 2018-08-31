package dangerzone.threads;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


import dangerzone.Chunk;
import dangerzone.Coords;
import dangerzone.DangerZone;
import dangerzone.Player;
import dangerzone.World;
import dangerzone.blocks.Blocks;

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

//TODO FIXME - run on server to have proper light maps there too! (spawning at night near torches, etc)

public class LightingThread implements Runnable {
	public int tickmax = 14;
	public static List<Coords> requested_list; //Don't blast server with same request over and over and over again!
	public static Lock requested_list_lock = new ReentrantLock();
	
	public LightingThread(){
		requested_list = new ArrayList<Coords>();
	}

	public void run() {
		int i, j, chance;
		long sleeper;
		int currentdimension;
		boolean restart = false;
		
		//Let things settle down a little first...
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Thread thisthread = Thread.currentThread();
		thisthread.setPriority(Thread.MIN_PRIORITY);
		
		currentdimension = DangerZone.player.dimension;

		while(DangerZone.gameover == 0){
			tickmax = DangerZone.renderdistance;
			
			for(i=-tickmax;i<=tickmax && DangerZone.gameover == 0 && !restart;i++){
				for(j=-tickmax;j<=tickmax && DangerZone.gameover == 0 && !restart;j++){ 
					if((int)Math.sqrt((i*i)+(j*j))>tickmax)continue; //Too far
					chance = (int)Math.sqrt(i*i + j*j)/2;
					if(DangerZone.world.rand.nextInt(chance+1) != 0)continue; //Do closer more often, further less often
					
					if(!DangerZone.f12_on)tickChunk(DangerZone.world, i, j);
					if(currentdimension != DangerZone.player.dimension){
						currentdimension = DangerZone.player.dimension;
						restart = true;
					}
					
					try {
						sleeper = 2;
						sleeper += ((24 - tickmax))/2;
						if(DangerZone.wr.fps < 40)sleeper += 16;
						if(DangerZone.wr.fps < 20)sleeper += 16;
						if(!DangerZone.light_speed)sleeper += 32;
						Thread.sleep(sleeper); //give it a rest! :)
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					//process the request list!
					Coords cl = null;
					
					while(true){
						requested_list_lock.lock();		
						Iterator<Coords> ii = requested_list.iterator();
						cl = null;
						if(ii.hasNext()){
							cl = (Coords)ii.next();						
							//Remove it!
							ii.remove();
						}
						requested_list_lock.unlock();
						if(cl != null){
							updateLightMaps(DangerZone.world, cl.lv, cl.d, cl.x, cl.y, cl.z);
						}else{
							break;
						}
					}
				}
			}
			
			if(restart){
				restart = false;
				//Let things settle down a little first...
				//clean up the list while we wait
				requested_list_lock.lock();		
				Iterator<Coords> ii = requested_list.iterator();
				while(ii.hasNext()){					
					//Remove it!
					ii.remove();
				}				
				requested_list_lock.unlock();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public void tickChunk(World w, int xrel, int zrel){
		int i, j, k, bid;
		short leveldata[] = null;
		short lightmap[] = null;
		short drawn[] = null;
		float currentlight;
		int iup, idown;
		boolean active = false;
	
		Player p = DangerZone.player;
		if(p == null)return;
		
		Chunk c = w.chunkcache.getDecoratedChunk(p.dimension, (xrel<<4)+(int)p.posx, 0, (zrel<<4)+(int)p.posz);
		if(c == null)return;
		drawn = c.drawn;
		if(drawn == null)return;

		//System.out.printf("Tick chunk at %d,  %d\n", c.chunkX, c.chunkZ);
		for(j=DangerZone.mindrawlevel;j<256;j++){			
			active = false;
			//Dimmer pass...
			lightmap = w.chunkcache.getDecoratedChunkLightmap(p.dimension, (c.chunkX<<4), j, (c.chunkZ<<4));
			if(lightmap != null){
				for(i=0;i<16;i++){
					for(k=0;k<16;k++){
						if(lightmap[i*16+k] > 9){
							lightmap[i*16+k] -= 10;
							if(lightmap[i*16+k] > 9)active = true;
						}else if(lightmap[i*16+k] < -9){
							lightmap[i*16+k] += 10;
							if(lightmap[i*16+k] < -9)active = true;
						}
					}
				}
				if(!active){
					w.chunkcache.clearDecoratedChunkLightmap(p.dimension, (c.chunkX<<4), j, (c.chunkZ<<4));
					lightmap = null;
				}
			}
			
			/*
			 * Quick check to see if anything was drawn around here.
			 * No sense making maps for ores that no one is anywhere near!
			 */
			iup = j+1;
			if(iup > 255)iup = 255;
			idown = j-1;
			if(idown < 0)idown = 0;
			if(drawn != null && drawn[j] == 0 && drawn[iup] == 0 && drawn[idown] == 0){
				continue;
			}
			
			//update for new/existing lights
			leveldata = c.blockdata[j];			
			if(leveldata == null){
				continue; //no sense trying if no data!
			}
			for(i=0;i<16;i++){
				for(k=0;k<16;k++){
					bid = leveldata[i*16+k];
					if(bid != 0){
						currentlight = Blocks.getLightLevel(bid, w, p.dimension, (c.chunkX<<4)+i, j, (c.chunkZ<<4)+k);
						if(currentlight != 0.0f){
							//crap... have to update lighting around this thing...
							updateLightMaps(w, currentlight, p.dimension, (c.chunkX<<4)+i, j, (c.chunkZ<<4)+k);
						}
					}
				}
			}
		}
	}
	
	public static void updateLightMaps(World w, float lv, int d, int x, int y, int z){
		int updist = 14;
		float cval;
		float cdist;
		int i, j, k;
		float prev;
		float newv = 0;
		
		if(DangerZone.renderdistance <= 20)updist = 12;
		if(DangerZone.renderdistance <= 16)updist = 10;
		if(DangerZone.renderdistance <= 12)updist = 8;
		if(DangerZone.renderdistance < 10)updist = 6;
	
		for(j=-updist;j<=updist;j++){
			if(y+j<0 || y+j>255)continue;
			for(i=-updist;i<=updist;i++){
				for(k=-updist;k<=updist;k++){
					cdist = (float) Math.sqrt((i*i)+(j*j)+(k*k));
					//cdist is now scaling factor... linear...				
					if(cdist < updist){
						cval = lv*(updist-cdist)/updist;
						prev = getLightMapValue(w, d, x+i, y+j, z+k);
						if(prev>=0&&cval>0){
							newv = prev;
							if(cval > newv)newv = cval;
						}else if(prev<=0&&cval<0){
							newv = prev;
							if(cval < newv)newv = cval;
						}else{
							//newv = (prev*99+cval)/100;
							newv = prev + (cval*cval)*Math.signum(cval);
						}
						w.chunkcache.setDecoratedChunkLightValue(d, x+i, y+j, z+k, newv);
					}
				}
			}
		}		
	}
	
	public static float getLightMapValue(World w, int d, int x, int y, int z){
		short lightmap[] = w.chunkcache.getDecoratedChunkLightmap(d, x, y, z);
		if(lightmap == null)return 0.0f;
		float tmp = lightmap[((x&0xf)*16) + (z&0x0f)];
		return tmp/1000;
	}
	
	public static void addRequest(int d, int x, int y, int z, float val){
		if(requested_list == null)return; //hasn't been started yet!
		requested_list_lock.lock();	
		
		if(requested_list.size() > 200){ //There is no reason for more! Let whatever it is (usually fire) retry...
			requested_list_lock.unlock();
			return;
		}
		
		Iterator<Coords> i = requested_list.iterator();
		while(i.hasNext()){
			Coords c = (Coords)i.next();
			if(c.d != d)continue;
			if(c.x != x)continue;
			if(c.z != z)continue;
			if(c.y != y)continue;
			if(c.lv != val)continue;
			//Have already requested this
			requested_list_lock.unlock();
			return; //it's already on the list!
		}
		Coords newc = new Coords();
		newc.d = d; newc.x = x; newc.y = y; newc.z = z; newc.lv = val;
		requested_list.add(newc);
		requested_list_lock.unlock();
	}
		
	
}