package demomodcode;


import dangerzone.Dimensions;
import dangerzone.StuffList;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.WorldDecorator;
import dangerzone.biomes.Biome;
import dangerzone.blocks.Blocks;


public class DemoWorldDecorator extends WorldDecorator {
	
	//stuff you can find in a chest...
	public static StuffList[] things = new StuffList[] { 
			//Block or Item, min, max, %chance
			new StuffList(DemoModMain.prettyflower, 5, 25, 75),
			new StuffList(DemoModMain.eggdemobeast, 3, 15, 65)					
			};
	
	//This is the best and easiest place to add decorations to the given chunk.
	//It is ONLY run on the server when creating new chunks.
	//NOT dimension or biome specific. YOU need to check.
	//just make a class that inherits from WorldDecorator, and override this function!
	//Oh, and then register your class with WorldDecorators...
	//That's it! You don't even need to call super() because this one registers itself.
	public void decorate(World world, int dimension, Biome b, int chunkx, int chunkz){
		if(!world.isServer)return;
		if(b == null)return;

		if(Dimensions.getName(dimension).equals("DangerZone: Overworld Dimension")){
			if(b.should_add_flowers && world.rand.nextInt(20) == 1){
				int howmany = 1 + world.rand.nextInt(5);
				for(int m = 0;m<howmany;m++){
					int ix = (chunkx<<4)+world.rand.nextInt(16);
					int iz = (chunkz<<4)+world.rand.nextInt(16);
					int iy;
					for(iy=165;iy>50;iy--){
						if(world.getblock(dimension, ix, iy, iz) == Blocks.grassblock.blockID){
							if(world.getblock(dimension, ix, iy+1, iz) == 0){
								world.setblock(dimension, ix, iy+1, iz, DemoModMain.prettyflower.blockID);
								break;
							}
						}
					}				
				}
			}
		}
		
		if(Dimensions.getName(dimension).equals("DangerZone: Big Round Tree Dimension")){			
			if(b.uniquename.equals("DangerZone:Big Trees")){
				add_dungeon(world, dimension, chunkx, chunkz);				
			}
		}
		
		if(Dimensions.getName(dimension).equals("DemoMod: Demo Dimension")){			
			if(b.uniquename.equals("DemoMod:Hills")){
				add_dungeon(world, dimension, chunkx, chunkz);				
			}
		}
		
	}
	
	public void add_dungeon(World world, int dimension, int chunkx, int chunkz){
		if(world.rand.nextInt(64) != 0)return;
		int xoff, yoff, zoff;
		int x = chunkx << 4;
		int z = chunkz << 4;
		int i, j, k;
		int bid;
		xoff = world.rand.nextInt(16);
		zoff = world.rand.nextInt(16);
		yoff = world.rand.nextInt(40) + 5;
		
		//Let's just go ahead and assume it's a good spot. They are fairly rare...
		for(i=0;i<11;i++){
			for(j=0;j<5;j++){
				for(k=0;k<11;k++){
					bid = 0;
					if(i==0 || i == 10)bid = Blocks.whitestone.blockID;
					if(j==0 || j == 4)bid = Blocks.whitestone.blockID;
					if(k==0 || k == 10)bid = Blocks.whitestone.blockID;
					world.setblocknonotify(dimension, x+xoff+i, yoff+j, z+zoff+k, bid);
				}
			}
		}
		
		//Add a spawner
		bid = DemoModMain.demobeastspawner.blockID;
		world.setblock(dimension, x+xoff+5, yoff+1, z+zoff+5, bid);
		
		//Now add a chest and put some things in it
		Utils.add_chest(world, dimension, x+xoff+5, yoff+1, z+zoff+1, things);
		
	}
}