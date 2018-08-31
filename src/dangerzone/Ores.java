package dangerzone;
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


import dangerzone.biomes.Biome;
import dangerzone.blocks.Block;




public class Ores {
		
	public static  List<Ore> orelist; // a list
		
	Ores(){
		orelist = new ArrayList<Ore>();
	}
	
/*
 * b = your ore
 * r = block that is replaced
 * d = dimension (or null for all)
 * b = biome (or null for all)
 * mind = minimum depth
 * maxd = maximum depth (should be greater than minimum!)
 * rt = rate, change, frequency... smaller = less often
 * cs = clump size
 * alg = alogrithm, 0=random, 1=layerish, 2=dense clump
 */
	public static void registerOre(Block b, Block r, Dimension d, Biome bio, int mind, int maxd, int rt, int cs, int alg){
		if(b == null || r == null)return;
		Ore no = new Ore();
		no.theOre = b;
		no.blockToReplace = r;
		no.dimensionname = null;
		if(d != null){
			no.dimensionname = d.uniquename;
		}
		no.biomename = null;
		if(bio != null){
			no.biomename = bio.uniquename;
		}
		
		if(mind < 0 || maxd < 0 || rt < 1 || cs < 1)return;
		if(mind > maxd)return;
		
		no.mindepth = mind;
		no.maxdepth = maxd;
		no.rate = rt;
		no.clumpsize = cs;
		no.structure = alg;
		no.rate_rand = 0;
		
		orelist.add(no);

	}
	
	public static void registerOreReduced(Block b, Block r, Dimension d, Biome bio, int mind, int maxd, int rt, int cs, int alg, int slower){
		if(b == null || r == null)return;
		Ore no = new Ore();
		no.theOre = b;
		no.blockToReplace = r;
		no.dimensionname = null;
		if(d != null){
			no.dimensionname = d.uniquename;
		}
		no.biomename = null;
		if(bio != null){
			no.biomename = bio.uniquename;
		}
		
		if(mind < 0 || maxd < 0 || rt < 1 || cs < 1)return;
		if(mind > maxd)return;
		
		no.mindepth = mind;
		no.maxdepth = maxd;
		no.rate = rt;
		no.clumpsize = cs;
		no.structure = alg;
		no.rate_rand = slower;
		
		orelist.add(no);

	}
	
	public static void generate(World w, int d, Biome b, Chunk c, int cx, int cz){
		//Check if we should be generating something in this chunk!
		Iterator<Ore> ii = orelist.iterator();
		Ore st;
		if(!w.isServer)return; //should NOT be here!
		
		while(ii.hasNext()){
			st = ii.next();
			if(st.dimensionname != null){
				if(!st.dimensionname.equals(Dimensions.getName(d))){
					continue;
				}
			}
			if(st.biomename != null && b != null){
				if(!st.biomename.equals(b.uniquename)){
					continue;
				}
			}
			
			//check for extra-reduced rate
			if(st.rate_rand > 1 && w.rand.nextInt(st.rate_rand) != 0)continue;
			
			//match! Let's do this thing!
			for(int tries = 0;tries < st.rate; tries++){
				int ypos = w.rand.nextInt(256);
				int xpos = 1 + w.rand.nextInt(14); //try to stay in chunk most of the time.
				int zpos = 1 + w.rand.nextInt(14);
				if(ypos >= st.mindepth && ypos <= st.maxdepth){
					
					if(st.structure == 0){
						//kind of random 3D splatter
						for(int i=0;i<st.clumpsize;i++){
							float dx = w.rand.nextFloat() - w.rand.nextFloat();
							float dy = w.rand.nextFloat() - w.rand.nextFloat();
							float dz = w.rand.nextFloat() - w.rand.nextFloat();
							dx *= st.clumpsize;
							dy *= st.clumpsize;
							dz *= st.clumpsize;
							dx /= 4;
							dy /= 4;
							dz /= 4;
							int bid = c.getblock((int)(xpos+dx), (int)(ypos+dy), (int)(zpos+dz));
							if(bid == st.blockToReplace.blockID){
								c.setblock((int)(xpos+dx), (int)(ypos+dy), (int)(zpos+dz), st.theOre.blockID);
							}						
						}
					}
					if(st.structure == 1){
						//flat layer-splatter
						for(int i=0;i<st.clumpsize;i++){
							float dx = w.rand.nextFloat() - w.rand.nextFloat();
							float dz = w.rand.nextFloat() - w.rand.nextFloat();
							dx *= st.clumpsize;
							dz *= st.clumpsize;
							dx /= 4;
							dz /= 4;
							int bid = c.getblock((int)(xpos+dx), (int)(ypos), (int)(zpos+dz));
							if(bid == st.blockToReplace.blockID){
								c.setblock((int)(xpos+dx), (int)(ypos), (int)(zpos+dz), st.theOre.blockID);
							}						
						}
					}
					if(st.structure == 2){
						//dense solid clump
						int dx = 1;
						if(w.rand.nextInt(2) == 1)dx = -1;
						int dy = 1;
						if(w.rand.nextInt(2) == 1)dy = -1;
						int dz = 1;
						if(w.rand.nextInt(2) == 1)dz = -1;
						int countx = 1 + w.rand.nextInt(st.clumpsize/2);
						int county = 1 + w.rand.nextInt(st.clumpsize/2);
						int countz = 1 + w.rand.nextInt(st.clumpsize/2);
						for(int y = 0;y<county;y++){
							for(int x = 0;x<countx;x++){
								for(int z = 0;z<countz;z++){
									int bid = c.getblock((int)(xpos+dx*x+y*dx), (int)(ypos+dy*y), (int)(zpos+dz*z+y*dz));
									if(bid == st.blockToReplace.blockID){
										c.setblock((int)(xpos+dx*x+y*dx), (int)(ypos+dy*y), (int)(zpos+dz*z+y*dz), st.theOre.blockID);
									}	
								}
							}
						}
					}
					
					if(st.structure == 3){
						//crystal-ish!
						float dx, dz, dy;
						float rx, ry, rz;
						int length, width;
						int ix, iy, iz;
						int bid;
						
						for(int i = 0; i < st.clumpsize; i++) {
							dx = (w.rand.nextFloat()-w.rand.nextFloat());
							dz = (w.rand.nextFloat()-w.rand.nextFloat());
							dy = 0.5f + (w.rand.nextFloat()/2.0f);
							width = w.rand.nextInt(2); //stay thin!
							length = 1 + width*2 + w.rand.nextInt(5);
							length *= st.clumpsize/2;
							rx = xpos;
							ry = ypos;
							rz = zpos;
							for(iy=0;iy<=length;iy++){
								for(ix=0;ix<=width;ix++){
									for(iz=0;iz<=width;iz++){
										bid = c.getblock((int)(rx+ix), (int)ry, (int)(rz+iz));
										if(bid == st.blockToReplace.blockID){
											c.setblock((int)(rx+ix), (int)ry, (int)(rz+iz), st.theOre.blockID);
										}	
									}
								}
								ry += dy;
								rx += dx;
								rz += dz;
							}
						}
					}
				}				
			}
		}
	}
	
	public static boolean isOre(int bid){
		Iterator<Ore> ii = orelist.iterator();
		Ore st;		
		while(ii.hasNext()){
			st = ii.next();
			if(st.theOre.blockID == bid)return true;
		}
		return false;
	}


}
