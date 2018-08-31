package dangerzone.biomes;
import java.util.Random;

import dangerzone.Chunk;
import dangerzone.DangerZone;
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



public class RuggedBiome extends Biome {
	public Trees tr;
	
	float biomefloats[] = null;
	float biomeoffs[];
	float biomescalex[];
	float biomescalez[];
	int biomepwrs[];
	int biomept[];
	int biolen = 100; //higher than 100 does not produce significantly better terrain. Stay 50-150 or so...
	
	//these two are adjustable.
	public float hilliness = 2.5f; //0.5 = plains, 3 = hilly, 10+ = mountainous	
	public float roughness = 0.25f; //0.125 = smooth, 0.25 = a little lumpy, 1.0 = really bumpy
		
	public RuggedBiome(String n){
		super(n);		
		tr = new Trees();
	}
	
	//must be done AFTER constructor so things can override the default hilliness and roughness values!
	public void genarrays(){
		
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
			if(myrand.nextInt(3) != 0){
				biomefloats[j] = (hilliness/2f) + myrand.nextFloat()*hilliness; 
				biomescalex[j] = (0.1f+myrand.nextFloat());
				biomescalez[j] = (0.1f+myrand.nextFloat());
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
	
	public int genvalue(int dx, int dz){
		int val = 0;
		int iters = biomefloats.length;
		int i;
		float fval = 1;
		
		for(i=0;i<iters;i++){
			if(biomept[i] == 0){
				fval += getoneval(i, dx, dz);
			}else{
				fval -= getoneval(i, dx, dz);
			}
		}
		val = (int)fval;
		return val;
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

		int i, k;
		int dx = (cx << 4)+(int)(w.worldseed&0xffff);
		int dz = (cz << 4)+(int)((w.worldseed>>16)&0xffff);
		float t;
		
		dx = dx % 1080000;
		dz = dz % 1080000;
		
		if(biomefloats == null)genarrays();
		
		for(i=0;i<16;i++){
			for(k=0;k<16;k++){

				t = genvalue(dx+i, dz+k);
				t += avgheight;
				if(t < 0)t = 0;
				if(t > 255)t = 255;
				stoneheight[i][k] = (int) t;
				
				t = (150f-t)/40f;
				dirtheight[i][k] = (int)t+stoneheight[i][k]+1;
				if(dirtheight[i][k] < 0)dirtheight[i][k] = 0;
				if(dirtheight[i][k] > 255)dirtheight[i][k] = 255;
				
				if(stoneheight[i][k] < waterlevel+10 && stoneheight[i][k] >= waterlevel){
					t = t * (stoneheight[i][k]-waterlevel)/10;
					dirtheight[i][k] = (int)t+stoneheight[i][k]+1;
					if(dirtheight[i][k] < 0)dirtheight[i][k] = 0;
					if(dirtheight[i][k] > 255)dirtheight[i][k] = 255;
				}				
				
				baseheight[i][k] = 0;
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
	public void generate(World w, int d, Chunk c, int cx, int cz, int dirtheight[][], int stoneheight[][], int lm3[][]){

	int i, j, k;
	int waterlevel = 60;
	
		for(j=0;j<250;j++){
			for(i=0;i<16;i++){
				for(k=0;k<16;k++){
					if(j < stoneheight[i][k]){
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
							if(j < dirtheight[i][k] && c.getblock(i,  j-1,  k) != 0 && c.getblock(i,  j-1,  k) != Blocks.waterstatic.blockID){								
								if(j < 150){
										c.setblock(i, j, k, Blocks.dirt.blockID);	
								}
							}
							if(j == dirtheight[i][k] && c.getblock(i,  j-1,  k) == Blocks.dirt.blockID){	
								if(j < 130){
									c.setblock(i, j, k, Blocks.grassblock.blockID);
									c.setblockmeta(i, j, k, w.rand.nextInt(4) << 12);  //random Y orientation. looks nicer!	
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
