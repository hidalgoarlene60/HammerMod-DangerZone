package demomodcode;

import dangerzone.Chunk;
import dangerzone.Ores;
import dangerzone.World;
import dangerzone.biomes.RuggedPlainsBiome;
import dangerzone.blocks.Blocks;

public class DemoPlainsBiome extends RuggedPlainsBiome {

	public DemoPlainsBiome(String n) {
		super(n);

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
						c.setblock(i, j, k, DemoModMain.mydemoblock.blockID);
					}else{
						if(j <= waterlevel){
							c.setblock(i, j, k, Blocks.waterstatic.blockID);
							if(j == waterlevel){
								if(c.getblock(i, j-1, k) == DemoModMain.mydemoblock.blockID){
									c.setblock(i, j-1, k, Blocks.sand.blockID);
								}
							}
						}else{
							if(j < dirtheight[i][k]-((j-60)/20) && c.getblock(i,  j-1,  k) != 0 && c.getblock(i,  j-1,  k) != Blocks.waterstatic.blockID){								
								c.setblock(i, j, k, Blocks.dirt.blockID);								
							}
							if(j == dirtheight[i][k]-((j-60)/20) && c.getblock(i,  j-1,  k) == Blocks.dirt.blockID){								
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
	 * That's what this is for... structures that can/might cross chunk boundaries!
	 * Note that structures larger than 8*8 chunks (128*128 blocks) can cause slowness...
	 * Large trees and dungeons are welcome here...
	 * 
	 */
	public void decorate(World world, int d, Chunk c, int chunkx, int chunkz){
		//TODO add own grass and trees.
	}


}
