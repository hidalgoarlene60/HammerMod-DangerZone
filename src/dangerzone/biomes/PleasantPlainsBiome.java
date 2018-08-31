package dangerzone.biomes;

import dangerzone.Chunk;
import dangerzone.World;

public class PleasantPlainsBiome extends RuggedBiome {
	public PleasantPlainsBiome(String n) {
		super(n);
		hilliness = 0.25f; //0.5 = plains, 3 = hilly, 10+ = mountainous	
		roughness = 0.125f; //0.125 = smooth, 0.25 = a little lumpy, 1.0 = really bumpy
	}
	
	/*
	 * You can (and probably should) use world.set/get calls here. 
	 * That's what this is for... structures that can/might cross chunk boundaries!
	 * Note that structures larger than 8*8 chunks (128*128 blocks) can cause slowness...
	 * Large trees and dungeons are welcome here...
	 * 
	 */
	public void decorate(World world, int d, Chunk c, int chunkx, int chunkz){
		if(world.rand.nextInt(4) == 1)tr.addGrass(world, d, chunkx<<4, chunkz<<4, c);	
	}

}
