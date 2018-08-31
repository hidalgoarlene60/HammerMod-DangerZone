package dangerzone.biomes;

import java.util.Random;

import dangerzone.Chunk;
import dangerzone.DangerZone;
import dangerzone.Fastmath;
import dangerzone.World;

public class OverWorldBiomeManager extends BiomeManager {
	
	float biomefloats[];
	float biomeoffs[];
	float biomescalex[];
	float biomescalez[];
	int biomepwrs[];
	int biomept[];
	int genlen = 40;
	
public OverWorldBiomeManager(){
	super();
	biomefloats = new float[genlen];
	biomeoffs = new float[genlen];
	biomescalex = new float[genlen];
	biomescalez = new float[genlen];
	biomepwrs = new int[genlen];
	biomept = new int[genlen];
	int i = biomefloats.length;
	Random myrand;
	if(DangerZone.server_world != null){
		myrand = new Random(DangerZone.server_world.worldseed+127); //so we get same number on restart!!!
	}else{
		myrand = new Random(127); //probably won't get used! (shouldn't)
	}
	
	for(int j=0;j<i;j++){
		biomefloats[j] = (0.1f+myrand.nextFloat())*5;
		biomeoffs[j] = myrand.nextFloat()*3.1415f;
		biomescalex[j] = (0.1f+myrand.nextFloat())*2;
		biomescalez[j] = (0.1f+myrand.nextFloat())*2;
		biomepwrs[j] = 1 + myrand.nextInt(5);
		biomept[j] = myrand.nextInt(3);
	}
}

	//make maps for all biomes THE SAME by doing generation here.
	//for example of separate generation for each biome, see the RuggedBiome and BiomeManager.
	public void generate(World w, int d, Biome b, Chunk c, int cx, int cz){
		int dirtlevelmap[][] = new int[16][16];
		int stonelevelmap[][] = new int[16][16];
		int bottomlevelmap[][] = new int[16][16];

		//easy - only one height generator - this one!
		localgenerateheightmaps( w,  d,  c,  cx,  cz, dirtlevelmap, stonelevelmap, bottomlevelmap);		
		b.generate( w,  d,  c,  cx,  cz, dirtlevelmap, stonelevelmap, bottomlevelmap);
		return;

	}
	
	/*
	 * Beautifully simple and effective terrain generation! :)
	 */
	public int genvalue(int dx, int dz){
		int val = 0;
		int iters = biomefloats.length;
		int i;
		float fval = 1;
		
		for(i=0;i<iters;i++){
			if(biomept[i] == 0){
				fval += getoneval(i, dx, dz);
			}else if(biomept[i] == 1){
				fval *= getoneval(i, dx, dz); //this makes the big spikes!
			}else{
				fval -= getoneval(i, dx, dz);
			}
		}
		val = (int)fval;
		return val;
	}
	
	public float getoneval(int index, int dx, int dz){
		float fval = 0;
		int i;
		float tval;
		tval = fval = (float)Fastmath.sin(biomeoffs[index]+Math.toRadians(dx*biomescalex[index] + dz*biomescalez[index]));
		for(i=0;i<biomepwrs[index];i++){
			fval *= tval;
		}
		fval *= biomefloats[index];
		return fval;
	}
	
	//generate height maps so the biomemanager can use them
	public void localgenerateheightmaps(World w, int d, Chunk c, int cx, int cz, int dirtheight[][], int stoneheight[][], int baseheight[][]){

		int i, k;

		float dirtamp = 0.02f;
		int dx = (cx << 4)+(int)(w.worldseed&0xffff);
		int dz = (cz << 4)+(int)((w.worldseed>>16)&0xffff);
		float t;			
		int waterlevel = 60; //we don't use a biome!
		int avgheight = 70;
		float tval = 0;
		
		dx = dx % 1080000;
		dz = dz % 1080000;
		
		for(i=0;i<16;i++){
			for(k=0;k<16;k++){

				tval = t = genvalue(dx+i, dz+k);
				t += avgheight;
				if(t < 0)t = 0;
				if(t > 255)t = 255;
				stoneheight[i][k] = (int) t;
				t = tval*dirtamp;
				dirtheight[i][k] = (int)t+stoneheight[i][k]+1;
				if(dirtheight[i][k] < 0)dirtheight[i][k] = 0;
				if(dirtheight[i][k] > 255)dirtheight[i][k] = 255;
				
				if(stoneheight[i][k] < waterlevel+10 && stoneheight[i][k] >= waterlevel){
					t = tval*(dirtamp/2);					
					dirtheight[i][k] = (int)t+stoneheight[i][k]+1;
					if(dirtheight[i][k] < 0)dirtheight[i][k] = 0;
					if(dirtheight[i][k] > 255)dirtheight[i][k] = 255;
				}
				
				
				baseheight[i][k] = 0;
			}
		}
		
	}
	
}
