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
import java.util.Iterator;


import dangerzone.Chunk;
import dangerzone.CreatureTypes;
import dangerzone.DangerZone;
import dangerzone.Dimensions;
import dangerzone.Player;
import dangerzone.ServerHooker;
import dangerzone.Spawnlist;
import dangerzone.SpawnlistEntry;
import dangerzone.World;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entity;


public class SpawnerThread implements Runnable {
	public int spawnmax = 12; //chunks!
	public int spawnmin = 2;

	public void run() {
		int i, j;
		Player p;
		
		//Let things settle down a little first...
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		while(DangerZone.gameover == 0){
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(DangerZone.f12_on)continue; //PAUSED		
			
			//pick a random block some distance away
			i = DangerZone.server_world.rand.nextInt(spawnmax-spawnmin);
			j = DangerZone.server_world.rand.nextInt(spawnmax-spawnmin);
			i += spawnmin;
			j += spawnmin;
			if(DangerZone.server_world.rand.nextInt(2)==0)i = -i;
			if(DangerZone.server_world.rand.nextInt(2)==0)j = -j;
			
			p = DangerZone.server.getRandomPlayer(DangerZone.server_world.rand); //Tick all active dimensions!
			if(p == null){
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			
			tickChunk(DangerZone.server_world, p.dimension, (i<<4)+(int)p.posx, (j<<4)+(int)p.posz, CreatureTypes.TRANSIENT);	//do TRANSIENT spawning only!
			
			//and now spawn a cloud or two..
			i = DangerZone.server_world.rand.nextInt((DangerZone.entityupdatedist/16)-1);
			j = DangerZone.server_world.rand.nextInt((DangerZone.entityupdatedist/16)-1);

			if(DangerZone.server_world.rand.nextBoolean())i = -i;
			if(DangerZone.server_world.rand.nextBoolean())j = -j;
			
			if(Dimensions.DimensionArray[p.dimension].cloud_enable && DangerZone.rand.nextInt(10) == 1){
				Entity eb = DangerZone.server_world.createEntityByName("DangerZone:Cloud", p.dimension, (i<<4)+(int)p.posx, 200, (j<<4)+(int)p.posz);
				if(eb != null){
					eb.init();
					DangerZone.server_world.spawnEntityInWorld(eb);
				}
			}	
			
		}
	}
	
	public void tickChunk(World w, int dim, int xrel, int zrel, int perm){		
		Chunk c = w.serverchunkcache.getDecoratedChunk(dim, xrel, 0, zrel);
		if(c == null)return;
		doSpawnChunk(c, w, dim, xrel, zrel, perm);
	}
		
	//also called by the chunk decorator!		
	public static void doSpawnChunk(Chunk c, World w, int dim, int xrel, int zrel, int perm){
		int i, j, k, bid;
		
		Iterator<SpawnlistEntry> ii = Spawnlist.spawnlist.iterator();
		SpawnlistEntry st;		
		while(ii.hasNext()){
			st = ii.next();
			if(st.permanence != perm)continue; //do the correct type!
			
			//System.out.printf("check spawning %s\n", st.whatToSpawn.uniquename);
			if(st.chance >= w.rand.nextInt(1000)){	//YES 1000!!! allows better control.			
				if(st.dimensionname != null){
					if(!st.dimensionname.equals(Dimensions.getName(c.dimension))){
						continue;
					}
				}
				if(st.biomename != null){
					if(!st.biomename.equals(w.getBiome(c.dimension, xrel, 0, zrel).uniquename)){
						continue;
					}
				}
				
				
				if(!ServerHooker.canSpawnHere(w, dim, (c.chunkX<<4),  (c.chunkZ<<4), st))continue;
				if(!Dimensions.DimensionArray[dim].canSpawnHere(w, dim, (c.chunkX<<4),  (c.chunkZ<<4), st))continue;
				
				//if(perm == CreatureTypes.TRANSIENT)System.out.printf("try spawning %s\n", st.whatToSpawn.uniquename);
				
				//match! Let's do this thing!
				for(int tries = 0;tries < st.tries; tries++){
					int iy;
					int xpos = 2 + w.rand.nextInt(14); //try to stay in chunk
					int zpos = 2 + w.rand.nextInt(14);
					
					if(st.type == CreatureTypes.LAND){ 
						int ih = (int) (st.whatToSpawn.getHeight() + 1);

						iy = st.maxy;						
						for(;iy > st.miny; iy--){
							bid = w.getblock(c.dimension, (c.chunkX<<4)+xpos, iy, (c.chunkZ<<4)+zpos);
							if(Blocks.isLiquid(bid))break;
							if(bid == 0)continue;
							if(Blocks.isLeaves(bid))continue;
							if(!Blocks.isDirt(bid) && !Blocks.isStone(bid))continue;
							if(!Blocks.isSolid(bid))continue;
							//bid = w.getblock(c.dimension, (c.chunkX<<4)+xpos, iy+1, (c.chunkZ<<4)+zpos);
							//if(bid != 0 && bid != Blocks.grass.blockID)break; //Don't go underground!
							if(isSolidAtLevel(w, c.dimension, (c.chunkX<<4)+xpos, iy, (c.chunkZ<<4)+zpos, st.whatToSpawn.getWidth())){
								if(!isSolidAtLevel(w, c.dimension, (c.chunkX<<4)+xpos, iy+1, (c.chunkZ<<4)+zpos, st.whatToSpawn.getWidth())){
									//found an empty spot over a solid. See if we fit here...
									boolean fits = true;
									if(ih > 1){
										for(j=2;j<=ih && fits;j++){
											if(isSolidAtLevel(w, c.dimension, (c.chunkX<<4)+xpos, iy+j, (c.chunkZ<<4)+zpos, st.whatToSpawn.getWidth()))fits = false;
										}
									}
									if(fits){
										//System.out.printf("entity %s fits!\n", st.whatToSpawn.uniquename);
										if(st.permanence == CreatureTypes.PERMANENT || st.whatToSpawn.getCanSpawnHereNow(w, c.dimension, (int)((c.chunkX<<4)+xpos), (int)(iy+1.01f), (int)((c.chunkZ<<4)+zpos))){
											//System.out.printf("spawning %s @ %d, %d, %d, %d\n", st.whatToSpawn.uniquename, c.dimension, (int)((c.chunkX<<4)+xpos+0.5f), (int)(iy+0.5f), (int)((c.chunkZ<<4)+zpos+0.5f));
											Entity eb = w.createEntityByName(st.whatToSpawn.uniquename, c.dimension, (c.chunkX<<4)+xpos, (double)iy+1.01f, (c.chunkZ<<4)+zpos);
											if(eb != null){
												eb.init();
												w.spawnEntityInWorld(eb);
											}
											//System.out.printf("Spawned %s at %d,  %d, %d\n", st.whatToSpawn.uniquename, (int)((c.chunkX<<4)+xpos), (int)(iy+1.01f), (int)((c.chunkZ<<4)+zpos));
											break;
										}				
									}
									
								}
								break; //we tried here once. Don't try underground...
							}
						}
					}else if(st.type == CreatureTypes.AIR){	
						iy = w.rand.nextInt(st.maxy-st.miny+1);
						iy += st.miny;
						int iw = (int) (st.whatToSpawn.getWidth()/2 + 0.5f);
						int ih = (int) (st.whatToSpawn.getHeight() + 1);
						boolean fits = true;
						for(i=-iw;i<=iw && fits;i++){
							for(k=-iw;k<=iw && fits;k++){
								for(j=0;j<=ih && fits;j++){
									bid = w.getblock(c.dimension, (c.chunkX<<4)+xpos+i, iy+j, (c.chunkZ<<4)+zpos+k);
									if(Blocks.isSolid(bid)||Blocks.isLiquid(bid))fits = false;
								}
							}
						}
						if(fits){
							//System.out.printf("entity %s fits!\n", st.whatToSpawn.uniquename);
							if(st.permanence == CreatureTypes.PERMANENT || st.whatToSpawn.getCanSpawnHereNow(w, c.dimension, (int)((c.chunkX<<4)+xpos), (int)(iy+0.5f), (int)((c.chunkZ<<4)+zpos))){
								//System.out.printf("spawning %s @ %d, %d, %d, %d\n", st.whatToSpawn.uniquename, p.dimension, (int)((c.chunkX<<4)+xpos+0.5f), (int)(iy+0.5f), (int)((c.chunkZ<<4)+zpos+0.5f));
								Entity eb = w.createEntityByName(st.whatToSpawn.uniquename, c.dimension, (c.chunkX<<4)+xpos, (double)iy+0.5f, (c.chunkZ<<4)+zpos);
								if(eb != null){
									eb.init();
									w.spawnEntityInWorld(eb);
								}
							}
						}
					}else if(st.type == CreatureTypes.WATER){ 
						int ih = (int) (st.whatToSpawn.getHeight() + 1);
						iy = st.maxy;						
						for(;iy>st.miny;iy--){							
							bid = w.getblock(c.dimension, (c.chunkX<<4)+xpos, iy, (c.chunkZ<<4)+zpos);
							if(bid != Blocks.water.blockID && bid != Blocks.waterstatic.blockID)continue;
							if(w.getblock(c.dimension, (c.chunkX<<4)+xpos, iy+1, (c.chunkZ<<4)+zpos) == 0){
								//found an empty spot over water. See if we fit here...
								boolean fits = true;
								if(ih > 1){
									for(j=2;j<=ih && fits;j++){
										if(isSolidAtLevel(w, c.dimension, (c.chunkX<<4)+xpos, iy+j, (c.chunkZ<<4)+zpos, st.whatToSpawn.getWidth()))fits = false;
									}
								}
								if(fits){
									//System.out.printf("entity %s fits!\n", st.whatToSpawn.uniquename);
									if(st.permanence == CreatureTypes.PERMANENT || st.whatToSpawn.getCanSpawnHereNow(w, c.dimension, (int)((c.chunkX<<4)+xpos), (int)(iy+1.01f), (int)((c.chunkZ<<4)+zpos))){
										//System.out.printf("spawning %s @ %d, %d, %d, %d\n", st.whatToSpawn.uniquename, p.dimension, (int)((c.chunkX<<4)+xpos+0.5f), (int)(iy+0.5f), (int)((c.chunkZ<<4)+zpos+0.5f));
										Entity eb = w.createEntityByName(st.whatToSpawn.uniquename, c.dimension, (c.chunkX<<4)+xpos, (double)iy+1.01f, (c.chunkZ<<4)+zpos);
										if(eb != null){
											eb.init();
											w.spawnEntityInWorld(eb);
										}
										break;
									}
								}
								break;
							}
						}
					}else if(st.type == CreatureTypes.UNDERGROUND){ 
						int ih = (int) (st.whatToSpawn.getHeight() + 1);
						iy = st.miny;
						//start at bottom, and go UP.
						for(;iy<st.maxy;iy++){
							if(isSolidAtLevel(w, c.dimension, (c.chunkX<<4)+xpos, iy, (c.chunkZ<<4)+zpos, st.whatToSpawn.getWidth())){
								if(!isSolidAtLevel(w, c.dimension, (c.chunkX<<4)+xpos, iy+1, (c.chunkZ<<4)+zpos, st.whatToSpawn.getWidth())
										&& !Blocks.isLiquid(w.getblock(c.dimension, (c.chunkX<<4)+xpos, iy+1, (c.chunkZ<<4)+zpos))){
									//found an empty spot over a solid. See if we fit here...
									boolean fits = true;
									if(ih > 1){
										for(j=2;j<=ih && fits;j++){
											if(isSolidAtLevel(w, c.dimension, (c.chunkX<<4)+xpos, iy+j, (c.chunkZ<<4)+zpos, st.whatToSpawn.getWidth()))fits = false;
										}
									}
									if(fits){
										//System.out.printf("entity %s fits!\n", st.whatToSpawn.uniquename);
										if(st.permanence == CreatureTypes.PERMANENT || st.whatToSpawn.getCanSpawnHereNow(w, c.dimension, (int)((c.chunkX<<4)+xpos), (int)(iy+1.01f), (int)((c.chunkZ<<4)+zpos))){
											//System.out.printf("spawning %s @ %d, %d, %d, %d\n", st.whatToSpawn.uniquename, p.dimension, (int)((c.chunkX<<4)+xpos+0.5f), (int)(iy+0.5f), (int)((c.chunkZ<<4)+zpos+0.5f));
											Entity eb = w.createEntityByName(st.whatToSpawn.uniquename, c.dimension, (c.chunkX<<4)+xpos, (double)iy+1.01f, (c.chunkZ<<4)+zpos);
											if(eb != null){
												eb.init();
												w.spawnEntityInWorld(eb);
											}
										}
										break;
									}
								}
							}
						}
					}
				}
				//System.out.printf("done trying spawning %s\n", st.whatToSpawn.uniquename);
			}
		}
	}
	
	//Check all around the entity WIDTH to see if it is solid.
	//Used for Y collisions
	public static boolean isSolidAtLevel(World w, int d, int x, int y, int z, float width){
		int intwidth = (int)((width/2.0f)+0.5f);
		int i, j;
		int itemp;
		double dx, dz;
		for(i=-intwidth;i<=intwidth;i++){
			for(j=-intwidth;j<=intwidth;j++){
				if(Blocks.isSolid(w.getblock(d, (int)x+i, (int)y, (int)z+j))){
					itemp = (int)(x)+i;
					dx = (double)x - ((double)itemp + 0.5f);
					dx = Math.abs(dx);
					if(dx < (0.49f + (width/2.0f))){
						itemp = (int)(z)+j;
						dz = (double)z - ((double)itemp + 0.5f);
						dz = Math.abs(dz);
						if(dz < (0.49f + (width/2.0f))){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	
}
