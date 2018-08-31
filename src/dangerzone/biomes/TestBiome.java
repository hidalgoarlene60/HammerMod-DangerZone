package dangerzone.biomes;

import java.util.Random;

import dangerzone.Chunk;
import dangerzone.DangerZone;
import dangerzone.Fastmath;
import dangerzone.Ores;
import dangerzone.World;
import dangerzone.blocks.Blocks;

public class TestBiome extends Biome {

	Trees tr;
	
	float biomefloats[];
	float biomeoffs[];
	float biomescalex[];
	float biomescalez[];
	int biomepwrs[];
	int biomept[];
	int biolen = 20; //higher than 100 does not produce significantly better terrain. Stay 50-150 or so...
	
	//these two are adjustable.
	public float hilliness = 7.5f; //0.5 = plains, 3 = hilly, 10+ = mountainous	
	public float roughness = 0.75f; //0.125 = smooth, 0.25 = a little lumpy, 1.0 = really bumpy
	
	public TestBiome(String n) {
		super(n);
		
		tr = new Trees();
		
		biomefloats = new float[biolen];
		biomeoffs = new float[biolen];
		biomescalex = new float[biolen];
		biomescalez = new float[biolen];
		biomepwrs = new int[biolen];
		biomept = new int[biolen];
		int i = biomefloats.length;
		
		Random myrand;
		if(DangerZone.server_world != null){
			myrand = new Random(DangerZone.server_world.worldseed+bioRand); //so we get same number on restart!!!
		}else{
			myrand = new Random(bioRand); //probably won't get used! (shouldn't)
		}
		
		for(int j=0;j<i;j++){			
			if(myrand.nextInt(3) == 0){
				biomefloats[j] = (hilliness/2f) + myrand.nextFloat()*hilliness; 
				biomescalex[j] = (0.1f+myrand.nextFloat())*10;
				biomescalez[j] = (0.1f+myrand.nextFloat())*10;
			}else{
				biomefloats[j] = (0.1f+myrand.nextFloat())*roughness;
				biomescalex[j] = (0.1f+myrand.nextFloat())*15;
				biomescalez[j] = (0.1f+myrand.nextFloat())*15;
			}
			
			biomeoffs[j] = myrand.nextFloat()*3.1415f;		
			biomepwrs[j] = 1 + myrand.nextInt(6);
			biomept[j] = myrand.nextInt(2);
		}
	}
	
	public float genvalue(int dx, int dz){
		int iters = biomefloats.length;
		int i;
		float fval = 1;
		
		dx = dx % 1080000;
		dz = dz % 1080000;
		
		for(i=0;i<iters;i++){
			if(biomept[i] == 0){
				fval += getoneval(i, dx, dz);
			}else{
				fval -= getoneval(i, dx, dz);
			}
		}
		return fval;
	}
	
	public float getoneval(int index, int dx, int dz){
		float fval;
		int i;
		float tval;
		
		tval = (float)Fastmath.sin(biomeoffs[index]+Math.toRadians(dz*biomescalez[index]));
		fval = (float)Fastmath.sin(biomeoffs[index]+Math.toRadians(dx*biomescalex[index]));
		tval = fval = (tval+fval)/2;
		for(i=0;i<biomepwrs[index];i++){
			fval *= tval;
		}
		fval *= biomefloats[index];				
		return fval;
	}
	
	//generate height maps so the biomemanager can use them
	public void generateheightmaps(World w, int d, Chunk c, int cx, int cz, int dirtheight[][], int stoneheight[][], int baseheight[][]){
		
	}
	
	/*
	 * USE ONLY CHUNK.set/get functions!!! Otherwise infinite recursion may occur!
	 * THIS ROUTINE IS JUST FOR SUPPLYING IN-CHUNK DATA, LIKE TERRAIN!!!
	 * THINK INSIDE THE CHUNK... er... BOX!
	 * Most parameters passed in for reference only. 
	 * Mostly what you need here is just the chunk itself.
	 */
	//use adjusted maps from the biome manager to generate. it may have raised or lowered a bit!
	public void generate(World w, int d, Chunk c, int cx, int cz, int dirtheight[][], int stoneheight[][], int lm3[][]){

	int i, j, k;
	float fval;
	float tval;
	
		for(j=0;j<63;j++){
			for(i=0;i<16;i++){
				for(k=0;k<16;k++){
					if(j < 40){
						c.setblock(i, j, k, Blocks.greystone.blockID);
					}else{
						if(j == 40){
							c.setblock(i, j, k, Blocks.grassblock.blockID);
							c.setblockmeta(i, j, k, w.rand.nextInt(4) << 12);
						}
						tval = genvalue((cx<<4) + i, (cz<<4) + k);	
						if(j < 50){
							tval += 1.1f*(50-j);
						}
						if(tval > 0){
							fval = genvalue((cx<<4) + i + j, (cz<<4) + k + j) - genvalue((cx<<4) + i - j, (cz<<4) + k - j) ;
							if(j > 60){
								fval -= 10.1f*(j-60);
							}
							if(j < 50){
								fval += 10.1f*(50-j);
							}
							if(fval > 0){
								if(j > 60){
									if(j > 61){
										c.setblock(i, j, k, Blocks.grassblock.blockID);
										c.setblockmeta(i, j, k, w.rand.nextInt(4) << 12);
									}else{
										c.setblock(i, j, k, Blocks.dirt.blockID);
									}
								}else{
									c.setblock(i, j, k, Blocks.stone.blockID);
								}
							}
						}
					}
										
					if(j == 0)c.setblock(i, j, k, Blocks.stopblock.blockID);
				}
			}			
		}

		//Put in a few ores now!
		Ores.generate(w, d, this, c, cx, cz);
		
	}
			
	
	/*
	 * You can (and probably should) use world.set/get calls here. 
	 * That's what this is for... structures that can/might cross chunk boundaries!
	 * Note that structures larger than 8*8 chunks (128*128 blocks) can cause slowness...
	 * Large trees and dungeons are welcome here...
	 * 
	 */
	public void decorate(World world, int d, Chunk c, int chunkx, int chunkz){
		tr.addGenericTrees(world, d, chunkx<<4, chunkz<<4);
		tr.addGrass(world, d, chunkx<<4, chunkz<<4, c);	
	}

}
