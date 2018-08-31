package dangerzone.biomes;
import java.util.Random;

import dangerzone.Chunk;
import dangerzone.DangerZone;
import dangerzone.Fastmath;
import dangerzone.World;

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
public class Biome {
	
	
	public String uniquename = "";
	public boolean should_add_caves = true;
	public boolean should_add_flowers = true;
	public boolean should_add_roaches = true;
	public boolean should_add_butterflies = true;
	public boolean should_add_dungeons = true;
	public boolean should_add_waterplants = true;
	public long bioRand = 0; //needed for generating biome maps
	public float rarityFactor = 1; //range 0-2. Lower = rarer. Normal = 1. USE SMALL DEVIATIONS!!!
	public float mul_red, mul_green, mul_blue; //VERY SMALL NUMBERS!!! Multiplier for colors!!!
	public float mixbiomesize = 10;//THIS DETERMINES AVERAGE BIOME SIZE! Bigger = smaller biomes! Smaller = bigger biomes!
	public int avgheight = 70;
	public int waterlevel = 60;
	
	//This is not terrain. This is for the biome mixing in the biomemanager.
	float mixfloats[] = null;
	float mixoffs[];
	float mixscalex[];
	float mixscalez[];
	int mixlen = 30; 
	
		
	public Biome(String n){
		uniquename = n;
		
		//make a randomish number out of the name!		
		char[] b = n.toCharArray();
		for(int i=0;i<b.length;i++){
			bioRand += (i+1)*b[i];
		}
		bioRand <<= 32; //have to make it a LARGE number or java kind of gets stuck in a rut with it!
		for(int i=0;i<b.length;i++){
			bioRand += (i+3)*b[i];
			bioRand += b[i];
		}
		
		if(DangerZone.server_world != null){
			bioRand ^= DangerZone.server_world.worldseed;
			mixfloats = new float[mixlen];
			mixoffs = new float[mixlen];
			mixscalex = new float[mixlen];
			mixscalez = new float[mixlen];
			
			Random myrand = new Random(DangerZone.server_world.worldseed+bioRand); //so we get same number on restart!!!

			for(int j=0;j<mixlen;j++){				
				mixfloats[j] = 1 + myrand.nextFloat(); 
				mixscalex[j] = (0.1f+myrand.nextFloat())*mixbiomesize; 
				mixscalez[j] = (0.1f+myrand.nextFloat())*mixbiomesize;
				mixoffs[j] = myrand.nextFloat()*3.1415f;		
			}
		}
		
		mul_red = mul_green = mul_blue = 1f;
		
	}
	
	public float mixgenvalue(int dx, int dz){
		int iters = mixfloats.length;
		int i;
		float fval = 0;
		
		for(i=0;i<iters;i++){
			if((i%1) == 0){
				fval += mixgetoneval(i, dx, dz);
			}else{
				fval -= mixgetoneval(i, dx, dz);
			}
		}	
		return fval;
	}
	
	public float mixgetoneval(int index, int dx, int dz){
		float fval = 0;
		fval = (float)Fastmath.sin(mixoffs[index]+Math.toRadians(dz*mixscalez[index]));
		fval += (float)Fastmath.sin(mixoffs[index]+Math.toRadians(dx*mixscalex[index]));
		fval *= mixfloats[index];
		return fval;
	}
	
	//returns the biome mix map value for chunk location.
	public float getValueForBiome(int x, int y, int z){
		float bioval = mixgenvalue(x>>4, z>>4);
		return bioval * rarityFactor;
	}
	
	//generate height maps into these arrays so the biomemanager can use them
	public void generateheightmaps(World w, int d, Chunk c, int cx, int cz, int dirtheight[][], int stoneheight[][], int baseheight[][]){
		
	}
	
	/*
	 * USE ONLY CHUNK.set/get functions!!! Otherwise infinite recursion may occur!
	 * THIS ROUTINE IS JUST FOR SUPPLYING IN-CHUNK DATA, LIKE TERRAIN!!!
	 * THINK INSIDE THE CHUNK... er... BOX!
	 * Most parameters passed in for reference only. 
	 * Mostly what you need here is just the chunk itself.
	 */
	//use adjusted maps from the biome manager to generate
	public void generate(World w, int d, Chunk c, int cx, int cz, int ditrheight[][], int stoneheight[][], int baseheight[][]){
		
	}
	
	/*
	 * You can (and probably should) use world.set/get calls here. 
	 * That's what this is for... structures that can cross chunk boundaries!
	 * Note that structures larger than 16*16 chunks (256*256 blocks) can cause slowness...
	 * Large trees and dungeons are welcome here...
	 * 
	 */
	public void decorate(World world, int d, Chunk c, int chunkx, int chunkz){

	}

}

