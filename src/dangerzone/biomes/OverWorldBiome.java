package dangerzone.biomes;
import dangerzone.Chunk;
import dangerzone.DangerZone;
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


//NOT a good example!!!! Go see RuggedBiome!
//This biome uses OverWorldBiomeManager, which only deals with ONE heightmap.
//Rugged Dimension uses multiple biomes with multiple heightmaps.
//That is probably what you want.

public class OverWorldBiome extends Biome {
	Trees tr;
		
	public OverWorldBiome(String n){
		super(n);
		tr = new Trees();
	}
	
	/*
	 * Height maps for this biome are generated in the biomemanager!
	 */
	
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

		for(j=0;j<200;j++){
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
									if(j < 100){
										c.setblock(i, j, k, Blocks.dirt.blockID);	
									}else{ //taper off
										if(DangerZone.rand.nextInt(150-j) > 5)c.setblock(i, j, k, Blocks.dirt.blockID);	
									}
								}
							}
							if(j == dirtheight[i][k] && c.getblock(i,  j-1,  k) == Blocks.dirt.blockID){	
								if(j < 130){
									if(j < 100){
										c.setblock(i, j, k, Blocks.grassblock.blockID);
										c.setblockmeta(i, j, k, w.rand.nextInt(4) << 12);  //random Y orientation. looks nicer!	
									}else{
										if(DangerZone.rand.nextInt(130-j) > 5){ //taper off
											c.setblock(i, j, k, Blocks.grassblock.blockID);
											c.setblockmeta(i, j, k, w.rand.nextInt(4) << 12);  //random Y orientation. looks nicer!	
										}
									}
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
	 * That's what this is for... structures that can cross chunk boundaries!
	 * Note that structures larger than 16*16 chunks (256*256 blocks) can cause slowness...
	 * Large trees and dungeons are welcome here...
	 * 
	 */
	public void decorate(World world, int d, Chunk c, int chunkx, int chunkz){
		tr.addGrass(world, d, chunkx<<4, chunkz<<4, c);
	}

}
