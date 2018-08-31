package dangerzone.biomes;
import dangerzone.Chunk;
import dangerzone.Fastmath;
import dangerzone.Ores;
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
public class BigTreeBiome extends Biome {
	
	Trees tr;
	
	public BigTreeBiome(String s){
		super(s);
		tr = new Trees();
	}
	
	/*
	 * USE ONLY CHUNK.set/get functions!!! Otherwise infinite recursion may occur!
	 * THIS ROUTINE IS JUST FOR SUPPLYING IN-CHUNK DATA, LIKE TERRAIN!!!
	 * THINK INSIDE THE CHUNK... er... BOX!
	 * Most parameters passed in for reference only. 
	 * Mostly what you need here is just the chunk itself.
	 */
	public void generateheightmaps(World w, int d, Chunk c, int cx, int cz, int dirtheight[][], int stoneheight[][], int baseheight[][]){

		
		int i, j, k;
		float noise[][] = new float[16][16];
		float noise2[][] = new float[16][16];

		int dirtamp = 2;
		int dx = (cx << 4)+(int)(w.worldseed&0xffff);
		int dz = (cz << 4)+(int)((w.worldseed>>16)&0xffff);
		double t;	
		
		dx = dx % 1080000;
		dz = dz % 1080000;
		
		for(i=0;i<16;i++){
			for(k=0;k<16;k++){
				noise[i][k] = 1;
				noise2[i][k] = (float) Fastmath.sin((double)((dx+i)/188d)%Math.PI)*2;
				noise2[i][k] *= (float) Fastmath.sin((double)((dz+k)/137d)%Math.PI)*2;
				noise2[i][k] *= (float) Fastmath.sin((double)((dx+i)/100d)%Math.PI)*2;
				noise2[i][k] *= (float) Fastmath.sin((double)((dz+k)/61d)%Math.PI)*2;	
			}
		}
		
		for(j=1;j<4;j++){
			for(i=0;i<16;i++){
				for(k=0;k<16;k++){
					noise[i][k] += (float) Fastmath.sin(Math.toRadians((double)((dx+i)/(11d/j))));
					noise[i][k] += (float) Fastmath.sin(Math.toRadians((double)((dz+k)/(9d/j))));

					noise[i][k] *= (float) Fastmath.sin(Math.toRadians((double)((dx+i)/(7d/j))));
					noise[i][k] *= (float) Fastmath.sin(Math.toRadians((double)((dz+k)/(5d/j))));

					noise[i][k] *= (float) Fastmath.sin(Math.toRadians((double)((dx+i)/(5d/j))));
					noise[i][k] *= (float) Fastmath.sin(Math.toRadians((double)((dz+k)/(3d/j))));

					noise[i][k] *= (float) Fastmath.sin(Math.toRadians((double)((dx+i)*(2d/j))));
					noise[i][k] *= (float) Fastmath.sin(Math.toRadians((double)((dz+k)*(3/j))));

					noise[i][k] *= (float) Fastmath.sin(Math.toRadians((double)((dx+i)*(3/j))));
					noise[i][k] *= (float) Fastmath.sin(Math.toRadians((double)((dz+k)*(5/j))));



				}
			}
		}
		
		for(i=0;i<16;i++){
			for(k=0;k<16;k++){
				noise[i][k] *= 35f;
			}
		}


		for(i=0;i<16;i++){
			for(k=0;k<16;k++){
				t = noise[i][k]*2.1f + avgheight;
				stoneheight[i][k] = (int) t;

				t = Math.abs(noise2[i][k]/2*dirtamp);
				dirtheight[i][k] = (int)t;
				dirtheight[i][k] += stoneheight[i][k];
				
				if(stoneheight[i][k] < waterlevel+10 && stoneheight[i][k] >= waterlevel){
					t = Math.abs(noise2[i][k]/2*dirtamp)*((float)stoneheight[i][k]-(float)waterlevel)/10f;	
					dirtheight[i][k] = (int)t;
					dirtheight[i][k] += stoneheight[i][k];
				}
				
				baseheight[i][k] = 0;
			}
		}
	}

	//use adjusted maps from the biome manager to generate. it may have raised or lowered a bit!
	public void generate(World w, int d, Chunk c, int cx, int cz, int lm1[][], int lm2[][], int lm3[][]){

		int i, j, k;
		int waterlevel = 60;
		for(j=0;j<200;j++){
			for(i=0;i<16;i++){
				for(k=0;k<16;k++){
					if(j < lm2[i][k]){
						c.setblock(i, j, k, Blocks.stone.blockID);
					}else{
						if(j <= waterlevel){
							c.setblock(i, j, k, Blocks.waterstatic.blockID);
							if(j == waterlevel){
								if(c.getblock(i, j-1, k) == Blocks.stone.blockID){
									c.setblock(i, j-1, k, Blocks.sand.blockID);
								}
							}
						}else{
							if(j < lm1[i][k] && j < 120){
								if(c.getblock(i,  j-1,  k) != Blocks.waterstatic.blockID && c.getblock(i,  j-1,  k) != 0){
									c.setblock(i, j, k, Blocks.dirt.blockID);
								}
							}
							if(j == lm1[i][k] && j < 100 && c.getblock(i,  j-1,  k) == Blocks.dirt.blockID){
								c.setblock(i, j, k, Blocks.grassblock.blockID);
								c.setblockmeta(i, j, k, w.rand.nextInt(4) << 12);  //random Y orientation. looks nicer!
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
	 * That's what this is for... structures that can cross chunk boundaries!
	 * Note that structures larger than 16*16 chunks (256*256 blocks) can cause slowness...
	 * Large trees and dungeons are welcome here... 
	 */
	public void decorate(World world, int d, Chunk c, int chunkx, int chunkz){
		tr.addGrass(world, d, chunkx<<4, chunkz<<4, c);
	}

}
