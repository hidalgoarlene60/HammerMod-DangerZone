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



public class SkyIslandBiome extends Biome {
	Trees tr;
		
	public SkyIslandBiome(String n){
		super(n);
		tr = new Trees();
		should_add_caves = false;
		should_add_dungeons = false;
	}
	
	//generate height maps directly into chunk so the biomemanager can use them
	public void generateheightmaps(World w, int d, Chunk c, int cx, int cz, int dirtheight[][], int stoneheight[][], int baseheight[][]){

		int i, j, k;

		float noise[][] = new float[16][16];
		float noise2[][] = new float[16][16];
		float noise3[][] = new float[16][16];		
		float noise4[][] = new float[16][16];
		float noise5[][] = new float[16][16];
		float noise6[][] = new float[16][16];
		float noise7[][] = new float[16][16];

		int dirtamp = 2;
		int dx = (cx << 4)+(int)(w.worldseed&0xffff);
		int dz = (cz << 4)+(int)((w.worldseed>>16)&0xffff);
		double t;			

		dx = dx % 1080000;
		dz = dz % 1080000;
		
		for(i=0;i<16;i++){
			for(k=0;k<16;k++){
				noise[i][k] = 0;
				noise2[i][k] = 0;
				noise3[i][k] = 0;
				noise4[i][k] = 0;
				noise5[i][k] = 0;
				noise6[i][k] = 0;
				noise7[i][k] = 0;
			}
		}
		
		for(j=4;j<9;j++){
			for(i=0;i<16;i++){
				for(k=0;k<16;k++){
					noise[i][k] += (float) Fastmath.sin(Math.toRadians((double)((dx+i)/((double)j*j*0.01d))))*Fastmath.sin(Math.toRadians((double)((dx+i)/((double)j*j*0.01d))));
					noise[i][k] += (float) Fastmath.sin(Math.toRadians((double)((dz+k)/((double)j*j*0.03d))))*Fastmath.sin(Math.toRadians((double)((dz+k)/((double)j*j*0.03d))));

					noise[i][k] *= (float) Fastmath.sin(Math.toRadians((double)((dx+i)/((double)j*j*0.07d))));
					noise[i][k] *= (float) Fastmath.sin(Math.toRadians((double)((dz+k)/((double)j*j*0.09d))));


					noise3[i][k] += (float) Fastmath.sin(Math.toRadians((double)((dx+100+i)/((double)j*j*0.03d))))*Fastmath.sin(Math.toRadians((double)((dx+100+i)/((double)j*j*0.03d))));
					noise3[i][k] += (float) Fastmath.sin(Math.toRadians((double)((dz+100+k)/((double)j*j*0.01d))))*Fastmath.sin(Math.toRadians((double)((dz+100+k)/((double)j*j*0.01d))));

					noise3[i][k] *= (float) Fastmath.sin(Math.toRadians((double)((dx+50+i)/((double)j*j*0.09d))));
					noise3[i][k] *= (float) Fastmath.sin(Math.toRadians((double)((dz+50+k)/((double)j*j*0.07d))));

				}
			}
		}
		
		for(j=3;j<7;j++){
			for(i=0;i<16;i++){
				for(k=0;k<16;k++){
					noise6[i][k] += (float) Fastmath.sin(Math.toRadians((double)((dx+i)/((double)j*j*0.012d))))*Fastmath.sin(Math.toRadians((double)((dx+i)/((double)j*j*0.012d))));
					noise6[i][k] += (float) Fastmath.sin(Math.toRadians((double)((dz+k)/((double)j*j*0.032d))))*Fastmath.sin(Math.toRadians((double)((dz+k)/((double)j*j*0.032d))));

					noise6[i][k] *= (float) Fastmath.sin(Math.toRadians((double)((dx+i)/((double)j*j*0.073d))));
					noise6[i][k] *= (float) Fastmath.sin(Math.toRadians((double)((dz+k)/((double)j*j*0.095d))));

					noise7[i][k] += (float) Fastmath.sin(Math.toRadians((double)((dx+210+i)/((double)j*j*0.033d))))*Fastmath.sin(Math.toRadians((double)((dx+100+i)/((double)j*j*0.033d))));
					noise7[i][k] += (float) Fastmath.sin(Math.toRadians((double)((dz+210+k)/((double)j*j*0.013d))))*Fastmath.sin(Math.toRadians((double)((dz+100+k)/((double)j*j*0.013d))));

					noise7[i][k] *= (float) Fastmath.sin(Math.toRadians((double)((dx+150+i)/((double)j*j*0.094d))));
					noise7[i][k] *= (float) Fastmath.sin(Math.toRadians((double)((dz+150+k)/((double)j*j*0.075d))));

				}
			}
		}
		
		for(j=1;j<4;j++){
			for(i=0;i<16;i++){
				for(k=0;k<16;k++){
					noise4[i][k] += (float) Fastmath.sin(Math.toRadians((double)((dx+i)/((double)j*j*0.011d))))*Fastmath.sin(Math.toRadians((double)((dx+i)/((double)j*j*0.011d))));
					noise4[i][k] += (float) Fastmath.sin(Math.toRadians((double)((dz+k)/((double)j*j*0.031d))))*Fastmath.sin(Math.toRadians((double)((dz+k)/((double)j*j*0.031d))));

					noise4[i][k] *= (float) Fastmath.sin(Math.toRadians((double)((dx+i)/((double)j*j*0.071d))));
					noise4[i][k] *= (float) Fastmath.sin(Math.toRadians((double)((dz+k)/((double)j*j*0.092d))));

					noise5[i][k] += (float) Fastmath.sin(Math.toRadians((double)((dx+100+i)/((double)j*j*0.032d))))*Fastmath.sin(Math.toRadians((double)((dx+100+i)/((double)j*j*0.032d))));
					noise5[i][k] += (float) Fastmath.sin(Math.toRadians((double)((dz+100+k)/((double)j*j*0.012d))))*Fastmath.sin(Math.toRadians((double)((dz+100+k)/((double)j*j*0.012d))));

					noise5[i][k] *= (float) Fastmath.sin(Math.toRadians((double)((dx+50+i)/((double)j*j*0.093d))));
					noise5[i][k] *= (float) Fastmath.sin(Math.toRadians((double)((dz+50+k)/((double)j*j*0.074d))));

				}
			}
		}
		
		for(i=0;i<16;i++){
			for(k=0;k<16;k++){
				noise[i][k] = (noise[i][k] - noise3[i][k]) * 35f;  //amplitude
				noise[i][k] += (noise4[i][k] - noise5[i][k]) * 3f; //amplitude
				noise[i][k] += 120;
				stoneheight[i][k] = (int)noise[i][k];
				
				noise3[i][k] = (noise3[i][k] - noise4[i][k]) * 25f;  //amplitude
				noise3[i][k] += (noise6[i][k] - noise5[i][k]) * 3f; //amplitude
				noise3[i][k] += 120;
				baseheight[i][k] = (int)noise3[i][k];
				
				noise2[i][k] = (noise7[i][k] - noise6[i][k]);
				t = Math.abs(noise2[i][k]*dirtamp);
				dirtheight[i][k] = (int)t+stoneheight[i][k]+1;			
			}
		}
	}
		
		
		
	/*
	 * USE ONLY CHUNK.set/get functions!!! Otherwise infinite recursion may occur!
	 * THIS ROUTINE IS JUST FOR SUPPLYING IN-CHUNK DATA, LIKE TERRAIN!!!
	 * THINK INSIDE THE CHUNK... er... BOX!
	 * Most parameters passed in for reference only. 
	 * Mostly what you need here is just the chunk itself.
	 */
	//use adjusted maps from the biome manager to generate. it may have raised or lowered a bit!
	public void generate(World w, int d, Chunk c, int cx, int cz, int dirtheight[][], int stoneheight[][], int bottomheight[][]){

	int i, j, k;
		for(j=0;j<250;j++){
			for(i=0;i<16;i++){
				for(k=0;k<16;k++){
					if(j < stoneheight[i][k] && j > bottomheight[i][k]){
						c.setblock(i, j, k, Blocks.stone.blockID);					
					}else{
						if(j < dirtheight[i][k] && c.getblock(i,  j-1,  k) != 0){								
							c.setblock(i, j, k, Blocks.dirt.blockID);								
						}
						if(j >= dirtheight[i][k] && c.getblock(i,  j-1,  k) == Blocks.dirt.blockID){								
							c.setblock(i, j, k, Blocks.grassblock.blockID);
							c.setblockmeta(i, j, k, w.rand.nextInt(4) << 12);  //random Y orientation. looks nicer!								
						}

					}
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
		//tr.addHighTrees(world, d, chunkx<<4, chunkz<<4, c);
		tr.addGrassHigh(world, d, chunkx<<4, chunkz<<4, c);	
		
	}

}
