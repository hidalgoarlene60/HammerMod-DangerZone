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


import dangerzone.Chunk;
import dangerzone.DangerZone;
import dangerzone.Dimensions;
import dangerzone.ListCoords;
import dangerzone.Player;
import dangerzone.ServerHooker;
import dangerzone.World;
import dangerzone.blocks.Blocks;



public class BlockTickerThread implements Runnable {
	public int tickmax = 14; //800 chunks!
	public int accumtime = 0;
	public int timetime = 0;
	public static int cycle = 0;

	public void run() {
		int i, j;
		int currentdimension = 1;
		boolean restart = false;
		int sleeper = 2;
		
		//Let things settle down a little first...
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		accumtime = 10000;
		timetime = 10000;
		Thread thisthread = Thread.currentThread();
		thisthread.setPriority(Thread.MIN_PRIORITY);
		if(DangerZone.start_client)currentdimension = DangerZone.player.dimension;
		DangerZone.server.sendTimeToAll(DangerZone.server_world.timetimer, DangerZone.server_world.lengthOfDay);
		cycle = 0;

		while(DangerZone.gameover == 0){
			
			cycle++;
			tickmax = 14;
			sleeper = 2;
			if(DangerZone.renderdistance < tickmax){
				sleeper += (tickmax - DangerZone.renderdistance);
				tickmax = DangerZone.renderdistance;
			}
			
			for(i=-tickmax;i<=tickmax && DangerZone.gameover == 0 && !restart;i++){
				for(j=-tickmax;j<=tickmax && DangerZone.gameover == 0 && !restart;j++){ 
					if(!DangerZone.f12_on)tickChunk(DangerZone.server_world, i, j);	
					if(DangerZone.start_client){ //we are single-player!
						if(currentdimension != DangerZone.player.dimension){
							currentdimension = DangerZone.player.dimension;
							restart = true;
						}
					}
					try {
						if(DangerZone.wr!= null && DangerZone.wr.fps < 40){
							Thread.sleep(8);
							accumtime += 8;
							timetime += 8;
							if(DangerZone.wr.fps < 20){
								Thread.sleep(8);
								accumtime += 8;
								timetime += 8;
							}
						}
						Thread.sleep(sleeper);
						accumtime += sleeper;
						timetime += sleeper;
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					if(DangerZone.f12_on){
						accumtime = 0;
						timetime = 0;
						continue;
					}
					//update game time!
					if(accumtime >= 8000){ //eight seconds, ish.
						DangerZone.server_world.timetimer++;
						if(DangerZone.server_world.timetimer >= DangerZone.server_world.lengthOfDay)DangerZone.server_world.timetimer = 0;
						accumtime = 0;
					}
					
					if(timetime >= 2000){ //two seconds, ish.
						DangerZone.server.sendTimeToAll(DangerZone.server_world.timetimer, DangerZone.server_world.lengthOfDay);
						timetime = 0;
					}
				}
			}

			
			if(restart){
				restart = false;
				//Let things settle down a little first...
				try {
					Thread.sleep(3000);
					accumtime += 3000;
					timetime += 3000;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void tickChunk(World w, int xrel, int zrel){
		int i, j, k, bid;
		short leveldata[] = null;
		List<ListCoords> blocklist = null;
	
		Player p = DangerZone.server.getRandomPlayer(w.rand); //Tick all active dimensions!
		if(p == null)return;
		
		Chunk c = w.serverchunkcache.getDecoratedChunk(p.dimension, (xrel<<4)+(int)p.posx, 0, (zrel<<4)+(int)p.posz);
		if(c == null)return;
		
		Dimensions.tickChunk(p, w, c.dimension, c);
		ServerHooker.tickChunk(p, w, c);
		
		//Almost no blocks ever need ticking, except leaves and grass... and water... Overall a very small percentage of the chunk.
		//Therefore, it is much more effective to build and keep a list to tick!
		//setBlock() will null the list, and we will make a new one.
		blocklist = c.tickblocks;
		if(blocklist == null){
			blocklist = new ArrayList<ListCoords>();
			c.tickblocks = blocklist; //give it a list for next time!
			//System.out.printf("Tick chunk at %d,  %d\n", c.chunkX, c.chunkZ);
			for(j=0;j<256;j++){			
				leveldata = c.blockdata[j];			
				if(leveldata == null){
					continue; //no sense trying if no data!
				}
				for(i=0;i<16;i++){
					for(k=0;k<16;k++){
						bid = leveldata[i*16+k];
						if(bid != 0 && (Blocks.alwaystick(bid) || Blocks.randomtick(bid))){
							blocklist.add(new ListCoords(p.dimension, (c.chunkX<<4)+i, j, (c.chunkZ<<4)+k));
						}
					}
				}
			}			
		}		
		Iterator<ListCoords> ib = blocklist.iterator();
		ListCoords li = null;
		while(ib.hasNext()){
			li = ib.next();
			bid = c.getblock(li.xpos, li.ypos, li.zpos);
			if(bid != 0 && (Blocks.alwaystick(bid) || (Blocks.randomtick(bid) && (w.rand.nextInt(25) == 1)))){
				Blocks.doblocktick(w, p.dimension, li.xpos, li.ypos, li.zpos, bid);
			}
		}
	}
	
	
}
