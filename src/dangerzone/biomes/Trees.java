package dangerzone.biomes;
import dangerzone.Chunk;
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
public class Trees {
	
	
	public void addHighTrees(World world, int d, int cx, int cz, Chunk chunk)
	{
		
		//Make them at least a little scarce, otherwise they are everywhere!
		if(world.rand.nextInt(8) != 0)return;
		
		int howmany = 0;
		int what = world.rand.nextInt(6);
		howmany = world.rand.nextInt(3);

		if(what != 0)howmany *= 2;
		

		for(int i = 0; i < howmany; i++) {
			int posX = cx + world.rand.nextInt(16);
			int posZ = cz + world.rand.nextInt(16);    
			for(int posY = 220; posY > 100; posY--) //Start high and search down until we find a grass/dirt block.
			{
				//Must be in air (empty block), and on a grass block
				if(world.getblock(d, posX, posY, posZ) == 0 && world.getblock(d, posX, posY-1, posZ) == Blocks.grassblock.blockID)
				{					
					if(what == 0){
						this.TallWoodTree(world, d, posX, posY, posZ);
					}else if(what == 1){
						this.makeFruitTree(world, d, posX, posY-1, posZ, Blocks.cherryleaves.blockID);
					}else{
						//world.setblock(d, posX, posY, posZ, Blocks.grass.id);
						this.ScragglyRedwoodTree(world, d, posX, posY, posZ);
						//this.ScragglyTreeWithBranches(world, posX, posY, posZ, chunk);
						//if(world.rand.nextInt(20)== 0)this.MakeBigRoundTree(world, d, posX, posY, posZ, Blocks.redwood.id, Blocks.redwoodleaves.id, 6);
						//return;
					}
					break;
				}	     	
			}
		}

	}
	
	public void addTrees(World world, int d, int chunkX, int chunkZ)
	{
		
		//Make them at least a little scarce, otherwise they are everywhere!
		if(world.rand.nextInt(5) != 0)return;
		
		int howmany = 0;
		int what = world.rand.nextInt(5);
		howmany = world.rand.nextInt(5);

		if(what != 0)howmany *= 2;
		

		for(int i = 0; i < howmany; i++) {
			int posX = chunkX + world.rand.nextInt(16);
			int posZ = chunkZ + world.rand.nextInt(16);    
			for(int posY = 128; posY > 40; posY--) //Start high and search down until we find a grass/dirt block.
			{
				//Must be in air (empty block), and on a grass block
				if(world.getblock(d, posX, posY, posZ) == 0 && world.getblock(d, posX, posY-1, posZ) == Blocks.grassblock.blockID)
				{					
					if(what == 0){
						this.ScragglyTreeWithBranches(world, d, posX, posY, posZ);
					}else if(what == 1){
						this.makeFruitTree(world, d, posX, posY-1, posZ, Blocks.peachleaves.blockID);
					}else{						
						if(world.rand.nextInt(20)== 0)this.MakeBigRoundTree(world, d, posX, posY, posZ, Blocks.redwoodlog.blockID, Blocks.redwoodleaves.blockID, 6);
						return;
					}
					break;
				}	     	
			}
		}

	}
	
	public void addFlowerTrees(World world, int d, int chunkX, int chunkZ)
	{
		
		//Make them at least a little scarce, otherwise they are everywhere!
		if(world.rand.nextInt(3) != 0)return;
		
		int howmany = 0;
		howmany = world.rand.nextInt(4);
	
		for(int i = 0; i < howmany; i++) {
			int posX = chunkX + world.rand.nextInt(16);
			int posZ = chunkZ + world.rand.nextInt(16);    
			for(int posY = 128; posY > 40; posY--) //Start high and search down until we find a grass/dirt block.
			{
				//Must be in air (empty block), and on a grass block
				if(world.getblock(d, posX, posY, posZ) == 0 && world.getblock(d, posX, posY-1, posZ) == Blocks.grassblock.blockID)
				{										
					flowerTree(world, d, posX, posY-1, posZ);				
					break;
				}	     	
			}
		}

	}
	
	public void addFlowerTwoTrees(World world, int d, int chunkX, int chunkZ)
	{
		
		//Make them at least a little scarce, otherwise they are everywhere!
		if(world.rand.nextInt(3) != 0)return;
		
		int howmany = 0;
		howmany = world.rand.nextInt(6);
	
		for(int i = 0; i < howmany; i++) {
			int posX = chunkX + world.rand.nextInt(16);
			int posZ = chunkZ + world.rand.nextInt(16);    
			for(int posY = 128; posY > 40; posY--) //Start high and search down until we find a grass/dirt block.
			{
				//Must be in air (empty block), and on a grass block
				if(world.getblock(d, posX, posY, posZ) == 0 && world.getblock(d, posX, posY-1, posZ) == Blocks.grassblock.blockID)
				{										
					flowerTreeTwo(world, d, posX, posY-1, posZ);				
					break;
				}	     	
			}
		}

	}
	
	public void addScrubTrees(World world, int d, int chunkX, int chunkZ)
	{
		
		//Make them at least a little scarce, otherwise they are everywhere!
		if(world.rand.nextInt(5) != 0)return;
		
		int howmany = 0;
		howmany = world.rand.nextInt(5);
		int which = world.rand.nextInt(4);
	
		for(int i = 0; i < howmany; i++) {
			int posX = chunkX + world.rand.nextInt(16);
			int posZ = chunkZ + world.rand.nextInt(16);    
			for(int posY = 128; posY > 40; posY--) //Start high and search down until we find a grass/dirt block.
			{
				//Must be in air (empty block), and on a grass block
				if(world.getblock(d, posX, posY, posZ) == 0 && world.getblock(d, posX, posY-1, posZ) == Blocks.sand.blockID)
				{										
					scrubTree(world, d, posX, posY-1, posZ, which);				
					break;
				}	     	
			}
		}

	}
	
	public void addForestTrees(World world, int d, int chunkX, int chunkZ)
	{
				
		int howmany = 0;
		howmany = 1+world.rand.nextInt(4);
	
		for(int i = 0; i < howmany; i++) {
			int posX = chunkX + world.rand.nextInt(16);
			int posZ = chunkZ + world.rand.nextInt(16);    
			for(int posY = 128; posY > 40; posY--) //Start high and search down until we find a grass/dirt block.
			{
				//Must be in air (empty block), and on a grass block
				if(world.getblock(d, posX, posY, posZ) == 0 && world.getblock(d, posX, posY-1, posZ) == Blocks.grassblock.blockID)
				{										
					flowerTreeNormal(world, d, posX, posY-1, posZ);				
					break;
				}	     	
			}
		}

	}
	
	public void addUmbrellaTrees(World world, int d, int chunkX, int chunkZ)
	{
		
		int howmany = 0;
		howmany = world.rand.nextInt(3);
	
		for(int i = 0; i < howmany; i++) {
			int posX = chunkX + world.rand.nextInt(16);
			int posZ = chunkZ + world.rand.nextInt(16);    
			for(int posY = 128; posY > 40; posY--) //Start high and search down until we find a grass/dirt block.
			{
				//Must be in air (empty block), and on a grass block
				if(world.getblock(d, posX, posY, posZ) == 0 && world.getblock(d, posX, posY-1, posZ) == Blocks.grassblock.blockID)
				{										
					umbrellaTree(world, d, posX, posY-1, posZ);				
					break;
				}	     	
			}
		}

	}
	
	public void addBulbTrees(World world, int d, int chunkX, int chunkZ)
	{
		
		int howmany = 0;
		howmany = world.rand.nextInt(3);
	
		for(int i = 0; i < howmany; i++) {
			int posX = chunkX + world.rand.nextInt(16);
			int posZ = chunkZ + world.rand.nextInt(16);    
			for(int posY = 128; posY > 40; posY--) //Start high and search down until we find a grass/dirt block.
			{
				//Must be in air (empty block), and on a grass block
				if(world.getblock(d, posX, posY, posZ) == 0 && world.getblock(d, posX, posY-1, posZ) == Blocks.grassblock.blockID)
				{										
					bulbTree(world, d, posX, posY-1, posZ);				
					break;
				}	     	
			}
		}

	}
	
	public void addlooplowspiralTree(World world, int d, int chunkX, int chunkZ)
	{
		
		int howmany = 0;
		howmany = world.rand.nextInt(3);
	
		for(int i = 0; i < howmany; i++) {
			int posX = chunkX + world.rand.nextInt(16);
			int posZ = chunkZ + world.rand.nextInt(16);    
			for(int posY = 128; posY > 40; posY--) //Start high and search down until we find a grass/dirt block.
			{
				//Must be in air (empty block), and on a grass block
				if(world.getblock(d, posX, posY, posZ) == 0 && world.getblock(d, posX, posY-1, posZ) == Blocks.grassblock.blockID)
				{										
					looplowspiralTree(world, d, posX, posY-1, posZ);				
					break;
				}	     	
			}
		}

	}
	
	public void addloopTree(World world, int d, int chunkX, int chunkZ)
	{
		
		int howmany = 0;
		howmany = 1 + world.rand.nextInt(5);
	
		for(int i = 0; i < howmany; i++) {
			int posX = chunkX + world.rand.nextInt(16);
			int posZ = chunkZ + world.rand.nextInt(16);    
			for(int posY = 128; posY > 40; posY--) //Start high and search down until we find a grass/dirt block.
			{
				//Must be in air (empty block), and on a grass block
				if(world.getblock(d, posX, posY, posZ) == 0 && world.getblock(d, posX, posY-1, posZ) == Blocks.grassblock.blockID)
				{										
					loopTree(world, d, posX, posY-1, posZ);				
					break;
				}	     	
			}
		}

	}
	
	public boolean addBigRoundLightTree(World world, int d, int chunkX, int chunkZ)
	{

		//Make them at least a little scarce, otherwise they are everywhere!
		if(world.rand.nextInt(5) != 0)return false;

		int posX = chunkX + world.rand.nextInt(16);
		int posZ = chunkZ + world.rand.nextInt(16);    
		for(int posY = 128; posY > 40; posY--) //Start high and search down until we find a grass/dirt block.
		{
			//Must be in air (empty block), and on a grass block
			if(world.getblock(d, posX, posY, posZ) == 0 && world.getblock(d, posX, posY-1, posZ) == Blocks.grassblock.blockID)
			{					
				if(world.rand.nextInt(20)== 0){
					this.MakeBigRoundTree(world, d, posX, posY, posZ, Blocks.willowlog.blockID, Blocks.willowleaves.blockID, 6);
					return true;
				}
				return false;
			}	     	
		}
		return false;
	}
	
	public boolean addGenericTrees(World world, int d, int chunkX, int chunkZ)
	{
		
		//Make them at least a little scarce, otherwise they are everywhere!
		if(world.rand.nextInt(5) != 0)return false;
		
		int howmany = 0;
		boolean added = false;
		howmany = 2 + world.rand.nextInt(5);

		for(int i = 0; i < howmany; i++) {
			int posX = chunkX + world.rand.nextInt(16);
			int posZ = chunkZ + world.rand.nextInt(16);    
			for(int posY = 128; posY > 40; posY--) //Start high and search down until we find a grass/dirt block.
			{
				//Must be in air (empty block), and on a grass block
				if(world.getblock(d, posX, posY, posZ) == 0 && world.getblock(d, posX, posY-1, posZ) == Blocks.grassblock.blockID)
				{					
					this.makeGenericTree(world, d, posX, posY-1, posZ);
					added = true;
					break;
				}	     	
			}
		}
		return added;
	}
	
	public boolean addTallWillowTrees(World world, int d, int chunkX, int chunkZ)
	{
		
		//Make them at least a little scarce, otherwise they are everywhere!
		if(world.rand.nextInt(5) != 0)return false;
		
		int howmany = 0;
		boolean added = false;
		howmany = 2 + world.rand.nextInt(5);

		for(int i = 0; i < howmany; i++) {
			int posX = chunkX + world.rand.nextInt(16);
			int posZ = chunkZ + world.rand.nextInt(16);    
			for(int posY = 128; posY > 40; posY--) //Start high and search down until we find a grass/dirt block.
			{
				//Must be in air (empty block), and on a grass block
				if(world.getblock(d, posX, posY, posZ) == 0 && world.getblock(d, posX, posY-1, posZ) == Blocks.grassblock.blockID)
				{					
					this.TallWillowTree(world, d, posX, posY-1, posZ);
					added = true;
					break;
				}	     	
			}
		}
		return added;
	}
	
	public void addGrass(World world, int d, int chunkX, int chunkZ, Chunk chunk)
	{
		
		int howmany = 0;
		howmany = 5 + world.rand.nextInt(10);		

		for(int i = 0; i < howmany; i++) {
			int posX = chunkX + world.rand.nextInt(16);
			int posZ = chunkZ + world.rand.nextInt(16);    
			for(int posY = 128; posY > 40; posY--) //Start high and search down until we find a grass/dirt block.
			{
				//Must be in air (empty block), and on a grass block
				if(world.getblock(d, posX, posY, posZ) == 0 && world.getblock(d, posX, posY-1, posZ) == Blocks.grassblock.blockID)
				{					
					world.setblock(d, posX, posY, posZ, Blocks.grass.blockID);
					break;
				}	     	
			}
		}

	}
	
	public void addGrassHigh(World world, int d, int chunkX, int chunkZ, Chunk chunk)
	{
		
		int howmany = 0;
		howmany = 5 + world.rand.nextInt(10);		

		for(int i = 0; i < howmany; i++) {
			int posX = chunkX + world.rand.nextInt(16);
			int posZ = chunkZ + world.rand.nextInt(16);    
			for(int posY = 228; posY > 80; posY--) //Start high and search down until we find a grass/dirt block.
			{
				//Must be in air (empty block), and on a grass block
				if(world.getblock(d, posX, posY, posZ) == 0 && world.getblock(d, posX, posY-1, posZ) == Blocks.grassblock.blockID)
				{					
					world.setblock(d, posX, posY, posZ, Blocks.grass.blockID);
					break;
				}	     	
			}
		}

	}
	
	public void makeScragglyRedwoodTreeBranch(World world, int d, int x, int y, int z, int len, int biasx, int biasz)
	{
		int bid, k, ix, iy, iz;
		int m, n;	   

		for(k=0;k<len;k++){
			ix = world.rand.nextInt(2) - world.rand.nextInt(2) + biasx;
			iz = world.rand.nextInt(2) - world.rand.nextInt(2) + biasz;
			if(ix > 1)ix = 1;
			if(ix < -1)ix = -1;
			if(iz > 1)iz = 1;
			if(iz < -1)iz = -1;
			iy = (world.rand.nextInt(3))>0?1:0;
			x += ix;
			z += iz;
			y += iy;
			bid = world.getblock(d, x, y, z);
			if(bid != 0 && bid != Blocks.redwoodlog.blockID && bid != Blocks.redwoodleaves.blockID){
				return; //STOP ran into something...
			}			
			world.setblocknonotify(d,  x, y, z, Blocks.redwoodlog.blockID);
			
			for(m=-1;m<2;m++){
				for(n=-1;n<2;n++){
					if(world.rand.nextInt(2) == 1){
						bid = world.getblock(d, x+m, y, z+n);
						if(bid == 0){
							world.setblocknonotify(d,  x+m, y, z+n, Blocks.redwoodleaves.blockID);
						}
					}
				}
			}
			if(world.rand.nextInt(2) == 1){
				bid = world.getblock(d, x, y+1, z);
				if(bid == 0){
					world.setblocknonotify(d,  x, y+1, z, Blocks.redwoodleaves.blockID);
				}
			}
		}		
	}
	
	/*
	 *
	 */
	public void ScragglyRedwoodTree(World world, int d, int x, int y, int z)
	{
		int bid, i, j, k, ix, iy, iz;
		int m, n;

		
	    i = 1 + world.rand.nextInt(3);
	    j = i + world.rand.nextInt(12);
	    
	    for(k=0;k<i;k++){
			bid = world.getblock(d, x, y+k, z);
			if(k >= 1 && bid != 0 && bid != Blocks.redwoodlog.blockID && bid != Blocks.redwoodleaves.blockID){
				return; //STOP ran into something...
			}
			world.setblocknonotify(d,  x, y+k, z, Blocks.redwoodlog.blockID);
	    }
	    y+=(i-1);

		for(k = i;k < j;k++){
			ix = world.rand.nextInt(2) - world.rand.nextInt(2);
			iz = world.rand.nextInt(2) - world.rand.nextInt(2);
			iy = (world.rand.nextInt(4))>0?1:0;
			x += ix;
			z += iz;
			y += iy;
			bid = world.getblock(d, x, y, z);
			if(bid != 0 && bid != Blocks.redwoodlog.blockID && bid != Blocks.redwoodleaves.blockID){
				break; //STOP ran into something...
			}
			world.setblock(d,  x, y, z, Blocks.redwoodlog.blockID);
			if(world.rand.nextInt(4) == 1){
				makeScragglyRedwoodTreeBranch(world, d, x, y, z, world.rand.nextInt(1+j-k), world.rand.nextInt(2) - world.rand.nextInt(2), world.rand.nextInt(2) - world.rand.nextInt(2));
			}
			
			for(m=-1;m<2;m++){
				for(n=-1;n<2;n++){
					if(world.rand.nextInt(2) == 1){
						bid = world.getblock(d, x+m, y, z+n);
						if(bid == 0){
							world.setblocknonotify(d,  x+m, y, z+n, Blocks.redwoodleaves.blockID);
						}
					}
				}
			}
			if(world.rand.nextInt(2) == 1){
				bid = world.getblock(d, x, y+1, z);
				if(bid == 0){
					world.setblocknonotify(d,  x, y+1, z, Blocks.redwoodleaves.blockID);
				}
			}
		}		
	}
	
	/*
	 *
	 */
	public void TallWillowTree(World world, int d, int x, int y, int z)
	{
		int bid, i, j, k;
		int m, n;
	
	    i = 10 + world.rand.nextInt(12);

	    j = i + world.rand.nextInt(18);
	    
	    for(k=0;k<i;k++){
			bid = world.getblock(d, x, y+k, z);
			if(k >= 1 && bid != 0 && bid != Blocks.willowlog.blockID && bid != Blocks.willowleaves.blockID){
				return; //STOP ran into something...
			}
			world.setblocknonotify(d,  x, y+k, z, Blocks.willowlog.blockID);
	    }
	    y+=(i-1);

		for(k = i;k < j;k++){
			y += 1;
			bid = world.getblock(d, x, y, z);
			if(bid != 0 && bid != Blocks.willowlog.blockID && bid != Blocks.willowleaves.blockID){
				break; //STOP ran into something...
			}
			world.setblocknonotify(d,  x, y, z, Blocks.willowlog.blockID);
			
			if((k%4) == 0){
				for(m=-1;m<2;m++){
					for(n=-1;n<2;n++){
						if(world.rand.nextInt(2) == 1){
							bid = world.getblock(d, x+m, y, z+n);
							if(bid == 0){
								world.setblocknonotify(d,  x+m, y, z+n, Blocks.willowleaves.blockID);
							}
						}
					}
				}
			}			
		}
		
		y += 1;
		for(m=-1;m<2;m++){
			for(n=-1;n<2;n++){
				if(world.rand.nextInt(2) == 1){
					bid = world.getblock(d, x+m, y, z+n);
					if(bid == 0){
						world.setblocknonotify(d,  x+m, y, z+n, Blocks.willowlog.blockID);
					}
				}
			}
		}
		for(m=-3;m<4;m++){
			for(n=-3;n<4;n++){				
					bid = world.getblock(d, x+m, y, z+n);
					if(bid == 0){
						world.setblocknonotify(d,  x+m, y, z+n, Blocks.willowleaves.blockID);
					}				
			}
		}
		y += 1;
		for(m=-1;m<2;m++){
			for(n=-1;n<2;n++){				
					bid = world.getblock(d, x+m, y, z+n);
					if(bid == 0){
						world.setblocknonotify(d,  x+m, y, z+n, Blocks.willowleaves.blockID);
					}				
			}
		}
	}
	
	public void TallWoodTree(World world, int d, int x, int y, int z)
	{
		int bid, i, j, k;
		int m, n;

		 i = 5 + world.rand.nextInt(6);
			
		 j = 2 + i + world.rand.nextInt(12);
	    
	    for(k=0;k<i;k++){
			bid = world.getblock(d, x, y+k, z);
			if(k >= 1 && bid != 0 && bid != Blocks.log.blockID && bid != Blocks.leaves.blockID){
				return; //STOP ran into something...
			}
			world.setblocknonotify(d,  x, y+k, z, Blocks.log.blockID);
	    }
	    y+=(i-1);

		for(k = i;k < j;k++){
			y += 1;
			bid = world.getblock(d, x, y, z);
			if(bid != 0 && bid != Blocks.log.blockID && bid != Blocks.leaves.blockID){
				break; //STOP ran into something...
			}
			world.setblocknonotify(d,  x, y, z, Blocks.log.blockID);
			
			if((k%4) == 0){
				for(m=-1;m<2;m++){
					for(n=-1;n<2;n++){
						if(world.rand.nextInt(2) == 1){
							bid = world.getblock(d, x+m, y, z+n);
							if(bid == 0){
								world.setblocknonotify(d,  x+m, y, z+n, Blocks.leaves.blockID);
							}
						}
					}
				}
			}			
		}
		
		y += 1;
		for(m=-1;m<2;m++){
			for(n=-1;n<2;n++){
				if(world.rand.nextInt(2) == 1){
					bid = world.getblock(d, x+m, y, z+n);
					if(bid == 0){
						world.setblocknonotify(d,  x+m, y, z+n, Blocks.log.blockID);
					}
				}
			}
		}
		for(m=-3;m<4;m++){
			for(n=-3;n<4;n++){				
					bid = world.getblock(d, x+m, y, z+n);
					if(bid == 0){
						world.setblocknonotify(d,  x+m, y, z+n, Blocks.leaves.blockID);
					}				
			}
		}
		y += 1;
		for(m=-1;m<2;m++){
			for(n=-1;n<2;n++){				
					bid = world.getblock(d, x+m, y, z+n);
					if(bid == 0){
						world.setblocknonotify(d,  x+m, y, z+n, Blocks.leaves.blockID);
					}				
			}
		}
	}
	
	public void ScragglyTreeWithBranches(World world, int d, int x, int y, int z)
	{
		int bid, i, j, k, ix, iy, iz;
		int m, n;

		
	    i = 1 + world.rand.nextInt(2);
	    j = i + world.rand.nextInt(10);
	    
	    for(k=0;k<i;k++){
			bid = world.getblock(d, x, y+k, z);
			if(k >= 1 && bid != 0 && bid != Blocks.log.blockID && bid != Blocks.leaves.blockID){
				return; //STOP ran into something...
			}
			world.setblocknonotify(d,  x, y+k, z, Blocks.log.blockID);
	    }
	    y+=(i-1);

		for(k = i;k < j;k++){
			ix = world.rand.nextInt(2) - world.rand.nextInt(2);
			iz = world.rand.nextInt(2) - world.rand.nextInt(2);
			iy = (world.rand.nextInt(4))>0?1:0;
			x += ix;
			z += iz;
			y += iy;
			bid = world.getblock(d, x, y, z);
			if(bid != 0 && bid != Blocks.log.blockID && bid != Blocks.leaves.blockID){
				break; //STOP ran into something...
			}
			world.setblocknonotify(d,  x, y, z, Blocks.log.blockID);
			if(world.rand.nextInt(4) == 1){
				makeScragglyBranch(world, d, x, y, z, world.rand.nextInt(1+j-k), world.rand.nextInt(2) - world.rand.nextInt(2), world.rand.nextInt(2) - world.rand.nextInt(2));
			}
			
			for(m=-1;m<2;m++){
				for(n=-1;n<2;n++){
					if(world.rand.nextInt(2) == 1){
						bid = world.getblock(d, x+m, y, z+n);
						if(bid == 0){
							world.setblocknonotify(d,  x+m, y, z+n, Blocks.leaves.blockID);
						}
					}
				}
			}
			if(world.rand.nextInt(2) == 1){
				bid = world.getblock(d, x, y+1, z);
				if(bid == 0){
					world.setblocknonotify(d,  x, y+1, z, Blocks.leaves.blockID);
				}
			}
		}		
	}
	
	public void makeScragglyBranch(World world, int d, int x, int y, int z, int len, int biasx, int biasz)
	{
		int bid, k, ix, iy, iz;
		int m, n;	   

		for(k=0;k<len;k++){
			ix = world.rand.nextInt(2) - world.rand.nextInt(2) + biasx;
			iz = world.rand.nextInt(2) - world.rand.nextInt(2) + biasz;
			if(ix > 1)ix = 1;
			if(ix < -1)ix = -1;
			if(iz > 1)iz = 1;
			if(iz < -1)iz = -1;
			iy = (world.rand.nextInt(3))>0?1:0;
			x += ix;
			z += iz;
			y += iy;
			bid = world.getblock(d, x, y, z);
			if(bid != 0 && bid != Blocks.log.blockID && bid != Blocks.leaves.blockID){
				return; //STOP ran into something...
			}			
			world.setblocknonotify(d,  x, y, z, Blocks.log.blockID);
			
			for(m=-1;m<2;m++){
				for(n=-1;n<2;n++){
					if(world.rand.nextInt(2) == 1){
						bid = world.getblock(d, x+m, y, z+n);
						if(bid == 0){
							world.setblocknonotify(d,  x+m, y, z+n, Blocks.leaves.blockID);
						}
					}
				}
			}
			if(world.rand.nextInt(2) == 1){
				bid = world.getblock(d, x, y+1, z);
				if(bid == 0){
					world.setblocknonotify(d,  x, y+1, z, Blocks.leaves.blockID);
				}
			}
		}		
	}
	
public boolean	isBoringBaseBlock(World world, int d, int x, int y, int z)
{
	int b = world.getblock(d, x, y, z);
	if(b == 0)return true;
	if(b == Blocks.dirt.blockID)return true;
	if(b == Blocks.grassblock.blockID)return true;
	
	if(b == Blocks.stone.blockID)return false;
	return false;
}

public boolean	isBoringBlock(World world, int d, int x, int y, int z)
{
	int b = world.getblock(d, x, y, z);
	if(b == 0)return true;
	if(Blocks.isLeaves(b))return true;
	return false;
}

	
    /**
	 * Make a really big freaking awesome tree.
	 * Complete with spiral staircase,
	 * and ready-to-rent rooms!
	 * Well, no. Circular trees don't have pre-made rooms or staircases...
	 */	
    public void MakeBigRoundTree(World world, int dimension, int inx, int y, int inz, int ID, int leafID, int t_radius)
    {
    	int i, j;
    	double rad = t_radius;
    	int cury = 0;
    	double dt, dr;
    	int ibranch = 0;
    	int ibranchlen;
    	double fcurx, fcurz;
    	double fx, fz;
    	fx = inx;
    	fx += 0.5f;
    	fz = inz;
    	fz += 0.5f;

    		
    	//First thing, make sure we touch the ground everywhere...
    	//Rip along the four sides going downward until we hit something.
    	cury = y;
    	for(i = 0; i < 360 ; i++)
    	{
    		dt = (rad * Math.sin(Math.toRadians(i)));
    		fcurx = (float) dt;
    		dt = (rad * Math.cos(Math.toRadians(i)));
    		fcurz = (float) dt;
    		if(isBoringBaseBlock(world, dimension, (int)(fx+fcurx), cury, (int)(fz+fcurz))){
    			for(j=0;j<20;j++){
    				if(cury-j>0){
    					if(isBoringBaseBlock(world, dimension, (int)(fx+fcurx), cury-j, (int)(fz+fcurz)))
    						world.setblocknonotify(dimension, (int)(fx+fcurx), cury-j, (int)(fz+fcurz), ID);
    					else
    						break;
    				}
    			}	    			
    		} 		
    	}
    	
    	//Build the trunk and staircase
    	cury = 1;
    	while(rad > 0.0D){
    		for(i = 0; i < 360 ; i++)
    		{
    			dt = (rad * Math.sin(Math.toRadians(i)));
    			fcurx = (float)dt;
    			dt = (rad * Math.cos(Math.toRadians(i)));
    			fcurz = (float)dt;
    			if(isBoringBaseBlock(world, dimension, (int)(fx+fcurx), y+cury,(int)(fz+fcurz))){
    				world.setblocknonotify(dimension, (int)(fx+fcurx), y+cury, (int)(fz+fcurz), ID);		    			
    			}
    		}   		

    		//Add branches!
    		if(cury > (int)rad)
    		{
    			ibranch += 80 + world.rand.nextInt(80); //angle of where to put this branch
    			if(ibranch>360)ibranch -= 360;
    			ibranchlen = (int)(rad * 5.0D) + world.rand.nextInt((int)rad + 2);
    			dt = (rad * Math.sin(Math.toRadians(ibranch)));
    			fcurx = (float)dt;
    			dt = (rad * Math.cos(Math.toRadians(ibranch)));
    			fcurz = (float)dt;
    			//System.out.printf("Try making a branch @ %d,  %d, %d with angle %d and len %d\n", curx, cury, curz, ibranch, ibranchlen);
    			MakeRoundBranch(world, dimension, ibranch, ibranchlen, (int)rad+1, fx+fcurx, y+cury, fz+fcurz, ID, leafID );
    		}
    		
    		//Add flooring if high and wide enough
    		if((cury%6)==0 && rad > 3.0D)
    		{ 
    			dr = rad - 0.25D;
    			//circle around in ever smaller radius
    			while(dr > 0.0D){
    				for(i = 0; i < 360 ; i++)
    				{
    					dt = (dr * Math.sin(Math.toRadians(i)));
    					fcurx = (float)dt;
    					dt = (dr * Math.cos(Math.toRadians(i)));
    					fcurz = (float)dt;
    					if(isBoringBaseBlock(world, dimension, (int)(fx+fcurx), y+cury, (int)(fz+fcurz))){
    						world.setblocknonotify(dimension, (int)(fx+fcurx), y+cury, (int)(fz+fcurz), ID);		    			
    					}
    				}
    				dr -= 0.25D;
    			}
    			
    		}
    		
    		cury++;
    		rad -= (0.01D * (double)world.rand.nextInt(15)); //randomish height
    	
    	}
    }
    
    private void MakeRoundBranch(World world, int dimension, int iangle, int branchlen, int width, double startx, int starty, double startz, int ID, int leafID )
    {   
	double deltadir = 3.1415926d/50.0d; //rads!
	double deltamag = 0.35f; //block!
	double h;
	int ix, iz, id;
	int ixlast = 0, izlast = 0;
	int radius = branchlen/2;
	double centerx, centerz;
	
		centerx =  (startx + (radius * Math.sin(Math.toRadians(iangle))));
		centerz =  (startz + (radius * Math.cos(Math.toRadians(iangle))));

	//System.out.printf("x = %f,  z = %f\n", (double)this.posX, (double)this.posZ);

		ixlast = izlast = 0;
		for(double curdir = -3.1415926d; curdir < 3.1415926d; curdir += deltadir){
			//System.out.printf("angle = %f,  radius = %f\n", (double)(curdir+dir), (double)radius);
			for(h = 0.75d; h<radius; h += deltamag){
				ix = (int)(centerx+(Math.cos(curdir)*h));
				iz = (int)(centerz+(Math.sin(curdir)*h));
				if(ix == ixlast && iz == izlast)continue;
				ixlast = ix;
				izlast = iz;
				//FastSetBlock(ix, starty, iz, Block.mycelium.blockID);
				id = ID;
    			if((radius-h)<2)id = leafID;
    			if(isBoringBlock(world, dimension, ix, starty, iz)){
    				world.setblocknonotify(dimension, ix, starty, iz, id);
    			}
			}
		}
    }
    
	public void makeFruitTree(World world, int dimension, int x, int y, int z, int blkid)
	{
		int bid, i, j, k, width;
		int h1, h2, h3, h4, h5, w1, w2;
		int var = 0;
		//Only grow on grass or dirt
		bid = world.getblock(dimension, x, y, z);
		if(bid != Blocks.grassblock.blockID && bid != Blocks.dirt.blockID){
			return;
		}
		
		h1 = 12;
		h2 = 6;
		h3 = 9;
		h4 = 6;
		h5 = 14;
		w1 = 5;
		w2 = 3;
		if(blkid == Blocks.peachleaves.blockID){
			h1 = 10;
			h2 = 5;
			h3 = 7;
			h4 = 5;
			h5 = 12;
			w1 = 4;
			w2 = 2;
		}
		if(blkid == Blocks.cherryleaves.blockID){
			h1 = 8;
			h2 = 3;
			h3 = 5;
			h4 = 3;
			h5 = 10;
			w1 = 3;
			w2 = 1;
		}
		
		w2 += world.rand.nextInt(2)-world.rand.nextInt(2);
		w1 += world.rand.nextInt(3)-world.rand.nextInt(3);

		//Grow up
		for(j=1;j<h1;j++){	
			world.setblocknonotify(dimension, x, y+j, z, Blocks.log.blockID);
		}

		//Now for some branches
		var = world.rand.nextInt(2)-world.rand.nextInt(2);
		for(j=1;j<w1+var;j++) world.setblocknonotify(dimension, x+j, y+h2, z, Blocks.log.blockID);
		var = world.rand.nextInt(2)-world.rand.nextInt(2);
		for(j=1;j<w1+var;j++) world.setblocknonotify(dimension, x-j, y+h2, z, Blocks.log.blockID);
		var = world.rand.nextInt(2)-world.rand.nextInt(2);
		for(j=1;j<w1+var;j++) world.setblocknonotify(dimension, x, y+h2, z+j, Blocks.log.blockID);
		var = world.rand.nextInt(2)-world.rand.nextInt(2);
		for(j=1;j<w1+var;j++) world.setblocknonotify(dimension, x, y+h2, z-j, Blocks.log.blockID);	
		
		//And a few little branches
		var = world.rand.nextInt(2)-world.rand.nextInt(2);
		for(j=1;j<w2+var;j++) world.setblocknonotify(dimension, x+j, y+h3, z, Blocks.log.blockID);
		var = world.rand.nextInt(2)-world.rand.nextInt(2);
		for(j=1;j<w2+var;j++) world.setblocknonotify(dimension, x-j, y+h3, z, Blocks.log.blockID);
		var = world.rand.nextInt(2)-world.rand.nextInt(2);
		for(j=1;j<w2+var;j++) world.setblocknonotify(dimension, x, y+h3, z+j, Blocks.log.blockID);
		var = world.rand.nextInt(2)-world.rand.nextInt(2);
		for(j=1;j<w2+var;j++) world.setblocknonotify(dimension, x, y+h3, z-j, Blocks.log.blockID);

		//Now fill with leaves, and then let it decay however it wants
		for(i=h4; i<h5; i++){
			width = 6;
			if(i > 8)width = 5;
			if(i > 10)width = 4;
			if(blkid != Blocks.appleleaves.blockID)width--;
			for(j=-width; j<=width; j++){
				for(k=-width; k<=width; k++){
					bid = world.getblock(dimension, x+k, y+i, z+j);
					if(bid == 0 && canGrowHere(world, dimension, x+k, y+i, z+j ))world.setblocknonotify(dimension, x+k, y+i, z+j, blkid);
				}
			}
		}
		//It actually ends up looking fairly nice...
	}
	
	public static boolean canGrowHere(World w, int d, int x, int y, int z){
		int i, j, k, t;

		for(i=-2;i<=2;i++){
			for(j=-2;j<=0;j++){ //Only check Down!
				for(k=-2;k<=2;k++){
					t = Math.abs(i) + Math.abs(j) + Math.abs(k); //Make roundish corners, not square!
					if(t <= 3){ 		//Furthest a leaf is allowed to be from a block of wood!
						if(Blocks.canLeavesGrow(w.getblock(d, x+i, y+j, z+k))){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
    
    
	public void makeGenericTree(World world, int dimension, int x, int y, int z)
	{
		int bid, i, j, k, width;
		int h1, h2, h3, h4, h5, w1, w2;
		int var = 0;
		//Only grow on grass or dirt
		bid = world.getblock(dimension, x, y, z);
		if(bid != Blocks.grassblock.blockID && bid != Blocks.dirt.blockID){
			return;
		}
		int logid = Blocks.log.blockID;
		int leafid = Blocks.leaves.blockID;
		int tmp = world.rand.nextInt(3);
		if(tmp == 1){
			logid = Blocks.willowlog.blockID;
		}
		if(tmp == 2){
			logid = Blocks.redwoodlog.blockID;
		}
		tmp = world.rand.nextInt(3);
		if(tmp == 1){
			leafid = Blocks.willowleaves.blockID;
		}
		if(tmp == 2){
			leafid = Blocks.redwoodleaves.blockID;
		}
		
		h1 = 8 + world.rand.nextInt(6);
		h2 = 4 + world.rand.nextInt(2);
		h3 = 6 + world.rand.nextInt(2);
		h4 = h2;
		h5 = h1 + 2;
		w1 = 5;
		w2 = 3;
		
		
		w2 += world.rand.nextInt(2)-world.rand.nextInt(2);
		w1 += world.rand.nextInt(3)-world.rand.nextInt(3);

		//Grow up
		for(j=1;j<h1;j++){	
			world.setblocknonotify(dimension, x, y+j, z, logid);
		}

		//Now for some branches
		var = world.rand.nextInt(2)-world.rand.nextInt(2);
		for(j=1;j<w1+var;j++) world.setblocknonotify(dimension, x+j, y+h2, z, logid);
		var = world.rand.nextInt(2)-world.rand.nextInt(2);
		for(j=1;j<w1+var;j++) world.setblocknonotify(dimension, x-j, y+h2, z, logid);
		var = world.rand.nextInt(2)-world.rand.nextInt(2);
		for(j=1;j<w1+var;j++) world.setblocknonotify(dimension, x, y+h2, z+j, logid);
		var = world.rand.nextInt(2)-world.rand.nextInt(2);
		for(j=1;j<w1+var;j++) world.setblocknonotify(dimension, x, y+h2, z-j, logid);	
		
		//And a few little branches
		var = world.rand.nextInt(2)-world.rand.nextInt(2);
		for(j=1;j<w2+var;j++) world.setblocknonotify(dimension, x+j, y+h3, z, logid);
		var = world.rand.nextInt(2)-world.rand.nextInt(2);
		for(j=1;j<w2+var;j++) world.setblocknonotify(dimension, x-j, y+h3, z, logid);
		var = world.rand.nextInt(2)-world.rand.nextInt(2);
		for(j=1;j<w2+var;j++) world.setblocknonotify(dimension, x, y+h3, z+j, logid);
		var = world.rand.nextInt(2)-world.rand.nextInt(2);
		for(j=1;j<w2+var;j++) world.setblocknonotify(dimension, x, y+h3, z-j, logid);

		//Now fill with leaves
		for(i=h4; i<h5; i++){
			width = 7;
			if(i > 8)width = 6;
			if(i > 10)width = 5;
			for(j=-width; j<=width; j++){
				for(k=-width; k<=width; k++){
					bid = world.getblock(dimension, x+k, y+i, z+j);
					if(bid == 0 && canGrowHere(world, dimension, x+k, y+i, z+j ))world.setblocknonotify(dimension, x+k, y+i, z+j, leafid);
				}
			}
		}
		//It actually ends up looking fairly nice...
	}
	
	public void scrubTree(World world, int d, int x, int y, int z, int type)
	{	
		if(type == 0){
			world.setblocknonotify(d,  x, y+1, z, Blocks.willowlog.blockID);
			world.setblocknonotify(d,  x, y+2, z, Blocks.willowleaves.blockID);
		}else if(type == 1){
			world.setblocknonotify(d,  x, y+1, z, Blocks.log.blockID);
			world.setblocknonotify(d,  x, y+2, z, Blocks.leaves.blockID);
		}else if(type == 2){
			world.setblocknonotify(d,  x, y+1, z, Blocks.redwoodlog.blockID);
			world.setblocknonotify(d,  x, y+2, z, Blocks.redwoodleaves.blockID);
		}else{
			int logtype = 0;
			int lt = world.rand.nextInt(3);
			if(lt == 0)logtype = Blocks.log.blockID;
			if(lt == 1)logtype = Blocks.willowlog.blockID;
			if(lt == 2)logtype = Blocks.redwoodlog.blockID;
			int leaftype = 0;
			int lft = world.rand.nextInt(3);
			if(lft == 0)leaftype = Blocks.leaves.blockID;
			if(lft == 1)leaftype = Blocks.willowleaves.blockID;
			if(lft == 2)leaftype = Blocks.redwoodleaves.blockID;
			world.setblocknonotify(d,  x, y+1, z, logtype);
			world.setblocknonotify(d,  x, y+2, z, leaftype);
		}
	}
	
	public void bulbTree(World world, int d, int x, int y, int z)
	{
		int bid;		
		//Only grow on grass or dirt
		bid = world.getblock(d, x, y, z);
		if(bid != Blocks.grassblock.blockID && bid != Blocks.dirt.blockID){
			return;
		}
		
		float dy = world.rand.nextFloat();
		float rad = 3 + world.rand.nextInt(5);
		float ht = 1;
		double dx, dz;
		float dd;
		for(int irad = 0;irad < 180;irad+= 10){
			dd = (float) (rad * Math.sin(Math.toRadians(irad)));
			
			for(int idir=0;idir<360;idir+=10){
				dx = (float) (dd * Math.sin(Math.toRadians(idir)));
				dz = (float) (dd * Math.cos(Math.toRadians(idir)));
				world.setblocknonotify(d,  x+(int)dx, y+(int)ht, z+(int)dz, Blocks.willowlog.blockID);			
			}
			ht += dy;
		}
		
		int iht = 0;
		for(int i=0;i<(int)rad;i++){
			iht = y+(int)ht+i;
			world.setblocknonotify(d,  x, iht, z, Blocks.willowlog.blockID);	
		}
		
		world.setblocknonotify(d,  x+1, iht, z+1, Blocks.willowleaves.blockID);
		world.setblocknonotify(d,  x-1, iht, z-1, Blocks.willowleaves.blockID);
		world.setblocknonotify(d,  x+1, iht, z-1, Blocks.willowleaves.blockID);
		world.setblocknonotify(d,  x-1, iht, z+1, Blocks.willowleaves.blockID);
		
		
	}
	
	public void vaseTree(World world, int d, int x, int y, int z)
	{
		int bid;		
		//Only grow on grass or dirt
		bid = world.getblock(d, x, y, z);
		if(bid != Blocks.grassblock.blockID && bid != Blocks.dirt.blockID){
			return;
		}
		

		float dx, dz;
		float ddr = world.rand.nextFloat();
		int height = 5 + world.rand.nextInt(15);
		int i;
		float dd = 0;
		bid = Blocks.log.blockID;
		for(i=0;i<height;i++){
			if(i == height-1)bid = Blocks.leaves.blockID;		
			for(int idir=0;idir<360;idir+=10){
				dx = (float) (dd * Math.sin(Math.toRadians(idir)));
				dz = (float) (dd * Math.cos(Math.toRadians(idir)));
				world.setblocknonotify(d,  x+(int)dx, y+i+1, z+(int)dz, bid);	
				if(world.rand.nextInt(10) == 1){
					dx = (float) ((dd+1) * Math.sin(Math.toRadians(idir)));
					dz = (float) ((dd+1) * Math.cos(Math.toRadians(idir)));
					world.setblocknonotify(d,  x+(int)dx, y+i+1, z+(int)dz, Blocks.cherryleaves.blockID);	
				}
			}
			dd += ddr;
		}
		
	}
	  
	public void spiralTree(World world, int d, int x, int y, int z)
	{
		int bid;		
		//Only grow on grass or dirt
		bid = world.getblock(d, x, y, z);
		if(bid != Blocks.grassblock.blockID && bid != Blocks.dirt.blockID){
			return;
		}
		

		float dx, dz;
		float ddr = 0.05f + world.rand.nextFloat()/8;
		int height = 5 + world.rand.nextInt(30);

		float dd = 0;
		bid = Blocks.log.blockID;
		float ht = 0;
		float htx = 0.15f + world.rand.nextFloat()/3;
		int ioff = world.rand.nextInt(360);
	
		while(ht < height){				
			for(int idir=0;idir<360;idir+=5){
				if(ht > height)break;
				dx = (float) (dd * Math.sin(Math.toRadians(idir+ioff)));
				dz = (float) (dd * Math.cos(Math.toRadians(idir+ioff)));
				world.setblocknonotify(d,  x+(int)dx, y+(int)ht+1, z+(int)dz, bid);
				if(world.rand.nextInt(3) == 1){
					world.setblocknonotify(d,  x+(int)dx, y+(int)ht+2, z+(int)dz, Blocks.leaves.blockID);	
				}
				if(world.rand.nextInt(10) == 1){
					dx = (float) ((dd+1) * Math.sin(Math.toRadians(idir+ioff)));
					dz = (float) ((dd+1) * Math.cos(Math.toRadians(idir+ioff)));
					world.setblocknonotify(d,  x+(int)dx, y+(int)ht+1, z+(int)dz, Blocks.leaves.blockID);	
				}
				dd += ddr;
				ht += htx;
			}
			
		}
		
	}
	
	public void loopTree(World world, int d, int x, int y, int z)
	{
		int bid;		
		//Only grow on grass or dirt
		bid = world.getblock(d, x, y, z);
		if(bid != Blocks.grassblock.blockID && bid != Blocks.dirt.blockID){
			return;
		}

		float dy;
		float rad = 3 + world.rand.nextInt(5);
		float dx;
		float dz;
		int ioff = world.rand.nextInt(360);

		for(int idir=0;idir<360;idir+=5){
			dx = (float) (rad * Math.cos(Math.toRadians(idir)));
			dz = (float) (dx * Math.cos(Math.toRadians(ioff)));
			dx = (float) (dx * Math.sin(Math.toRadians(ioff)));				
			dy = (float) (rad * Math.sin(Math.toRadians(idir)));
			world.setblocknonotify(d,  x+(int)dx, y+(int)(dy+rad)+1, z+(int)dz, Blocks.willowlog.blockID);	

			dx = (float) ((rad+1) * Math.cos(Math.toRadians(idir)));
			dz = (float) (dx * Math.cos(Math.toRadians(ioff)));
			dx = (float) (dx * Math.sin(Math.toRadians(ioff)));	
			dy = (float) ((rad+1) * Math.sin(Math.toRadians(idir)));
			if(world.getblock(d,  x+(int)dx, y+(int)(dy+rad)+1, z+(int)dz) == 0){
				world.setblocknonotify(d,  x+(int)dx, y+(int)(dy+rad)+1, z+(int)dz, Blocks.willowleaves.blockID);
			}

		}

	}
	
	public void loopspiralTree(World world, int d, int x, int y, int z)
	{
		int bid;		
		//Only grow on grass or dirt
		bid = world.getblock(d, x, y, z);
		if(bid != Blocks.grassblock.blockID && bid != Blocks.dirt.blockID){
			return;
		}
		
		float dy;
		float rad = 8 + world.rand.nextInt(5);
		float orad = rad;
		float dx;
		float dz;
		int ioff = world.rand.nextInt(360);
		float ddr = 0.05f + world.rand.nextFloat()/50;


		for(int idir=0;rad > 3;idir+=5){
			dx = (float) (rad * Math.sin(Math.toRadians(idir)));
			dz = (float) (dx * Math.cos(Math.toRadians(ioff)));
			dx = (float) (dx * Math.sin(Math.toRadians(ioff)));				
			dy = -(float) (rad * Math.cos(Math.toRadians(idir)));
			world.setblocknonotify(d,  x+(int)dx, y+(int)(dy+orad)+1, z+(int)dz, Blocks.redwoodlog.blockID);				
			if(world.getblock(d,  x+(int)dx, y+(int)(dy+orad)+2, z+(int)dz) == 0){
				world.setblocknonotify(d,  x+(int)dx, y+(int)(dy+orad)+2, z+(int)dz, Blocks.leaves.blockID);
			}

			rad -= ddr;
		}


	}
	
	public void looplowspiralTree(World world, int d, int x, int y, int z)
	{
		int bid;		
		//Only grow on grass or dirt
		bid = world.getblock(d, x, y, z);
		if(bid != Blocks.grassblock.blockID && bid != Blocks.dirt.blockID){
			return;
		}
		
		float dy;
		float rad = 8 + world.rand.nextInt(5);

		float dx;
		float dz;
		int ioff = world.rand.nextInt(360);
		float ddr = 0.01f + world.rand.nextFloat()/50;


		for(int idir=0;rad > 3;idir+=5){
			dx = (float) (rad * Math.sin(Math.toRadians(idir)));
			dz = (float) (dx * Math.cos(Math.toRadians(ioff)));
			dx = (float) (dx * Math.sin(Math.toRadians(ioff)));				
			dy = -(float) (rad * Math.cos(Math.toRadians(idir)));
			world.setblocknonotify(d,  x+(int)dx, y+(int)(dy+rad)+1, z+(int)dz, Blocks.willowlog.blockID);	
			if(world.rand.nextInt(6) == 1){
				dx = (float) ((rad+1) * Math.sin(Math.toRadians(idir)));
				dz = (float) (dx * Math.cos(Math.toRadians(ioff)));
				dx = (float) (dx * Math.sin(Math.toRadians(ioff)));	
				dy = -(float) ((rad+1) * Math.cos(Math.toRadians(idir)));
				if(world.getblock(d,  x+(int)dx, y+(int)(dy+rad)+1, z+(int)dz) == 0){
					world.setblocknonotify(d,  x+(int)dx, y+(int)(dy+rad)+1, z+(int)dz, Blocks.redwoodleaves.blockID);
				}
			}
			rad -= ddr;
		}

	}
	
	public void bowlTree(World world, int d, int x, int y, int z)
	{
		int bid;		
		//Only grow on grass or dirt
		bid = world.getblock(d, x, y, z);
		if(bid != Blocks.grassblock.blockID && bid != Blocks.dirt.blockID){
			return;
		}
		int i;
		int howmany = 8 + world.rand.nextInt(10);
		for(i=0;i<howmany;i++){
			float dy;
			float rad = 6 + world.rand.nextInt(12);
			float dx;
			float dz;
			int ioff = world.rand.nextInt(360);

			for(int idir=0;idir < 90;idir+=5){
				dx = (float) (rad * Math.sin(Math.toRadians(idir)));
				dz = (float) (dx * Math.cos(Math.toRadians(ioff)));
				dx = (float) (dx * Math.sin(Math.toRadians(ioff)));				
				dy = -(float) (rad * Math.cos(Math.toRadians(idir)));
				world.setblocknonotify(d,  x+(int)dx, y+(int)(dy+rad)+1, z+(int)dz, Blocks.redwoodlog.blockID);	
				if(world.getblock(d,  x+(int)dx, y+(int)(dy+rad)+2, z+(int)dz) == 0){
					world.setblocknonotify(d,  x+(int)dx, y+(int)(dy+rad)+2, z+(int)dz, Blocks.willowleaves.blockID);
				}
			}
		}

	}
	
	public void doublebowlTree(World world, int d, int x, int y, int z)
	{
		int bid;		
		//Only grow on grass or dirt
		bid = world.getblock(d, x, y, z);
		if(bid != Blocks.grassblock.blockID && bid != Blocks.dirt.blockID){
			return;
		}
		int i;
		int howmany = 8 + world.rand.nextInt(10);
		for(i=0;i<howmany;i++){
			float dy;
			float rad = 6 + world.rand.nextInt(12);
			float dx;
			float dz;
			int ioff = world.rand.nextInt(360);

			for(int idir=0;idir < 90;idir+=5){
				dx = (float) (rad * Math.sin(Math.toRadians(idir)));
				dz = (float) (dx * Math.cos(Math.toRadians(ioff)));
				dx = (float) (dx * Math.sin(Math.toRadians(ioff)));				
				dy = -(float) (rad * Math.cos(Math.toRadians(idir)));
				world.setblocknonotify(d,  x+(int)dx, y+(int)(dy+rad)+1, z+(int)dz, Blocks.redwoodlog.blockID);	
				if(world.getblock(d,  x+(int)dx, y+(int)(dy+rad)+2, z+(int)dz) == 0){
					world.setblocknonotify(d,  x+(int)dx, y+(int)(dy+rad)+2, z+(int)dz, Blocks.willowleaves.blockID);
				}
				if(idir == 85){
					doublebowlTreeTop(world, d,  x+(int)dx, y+(int)(dy+rad)+2, z+(int)dz);
				}
			}
		}

	}
	
	public void doublebowlTreeTop(World world, int d, int x, int y, int z)
	{	
		int i;
		int howmany = 3 + world.rand.nextInt(6);
		for(i=0;i<howmany;i++){
			float dy;
			float rad = 3 + world.rand.nextInt(8);
			float dx;
			float dz;
			int ioff = world.rand.nextInt(360);

			for(int idir=0;idir < 90;idir+=5){
				dx = (float) (rad * Math.sin(Math.toRadians(idir)));
				dz = (float) (dx * Math.cos(Math.toRadians(ioff)));
				dx = (float) (dx * Math.sin(Math.toRadians(ioff)));				
				dy = -(float) (rad * Math.cos(Math.toRadians(idir)));
				world.setblocknonotify(d,  x+(int)dx, y+(int)(dy+rad)+1, z+(int)dz, Blocks.redwoodlog.blockID);	
				if(world.getblock(d,  x+(int)dx, y+(int)(dy+rad)+2, z+(int)dz) == 0){
					world.setblocknonotify(d,  x+(int)dx, y+(int)(dy+rad)+2, z+(int)dz, Blocks.willowleaves.blockID);
				}
			}
		}

	}

	public void flowerTree(World world, int d, int x, int y, int z)
	{
		int bid;		
		//Only grow on grass or dirt
		bid = world.getblock(d, x, y, z);
		if(bid != Blocks.grassblock.blockID && bid != Blocks.dirt.blockID){
			return;
		}
		int i;
		int leafcolor = Blocks.redwoodleaves.blockID;
		int howmany = 8 + world.rand.nextInt(10);
		for(i=0;i<howmany;i++){
			float dy;
			float rad = 6 + world.rand.nextInt(12);
			float dx;
			float dz;
			int ioff = world.rand.nextInt(360);
			float xoff, zoff;
			xoff = (world.rand.nextFloat()-world.rand.nextFloat())*2;
			zoff = (world.rand.nextFloat()-world.rand.nextFloat())*2;
			int leafstart = 35+world.rand.nextInt(20);

			for(int idir=0;idir < 90;idir+=2){
				dx = rad - (float) (rad * Math.cos(Math.toRadians(idir)));
				dz = (float) (dx * Math.cos(Math.toRadians(ioff)));
				dx = (float) (dx * Math.sin(Math.toRadians(ioff)));				
				dy = (float) (rad * Math.sin(Math.toRadians(idir)));
				world.setblocknonotify(d,  x+(int)dx+(int)xoff, y+(int)(dy)+1, z+(int)dz+(int)zoff, Blocks.log.blockID);	
				if(idir >= leafstart){
					makeflowerleaves(world, d,  x+(int)dx+(int)xoff, y+(int)(dy)+1, z+(int)dz+(int)zoff, leafcolor);
				}
			}
		}

		int lf = world.rand.nextInt(4);
		if(lf == 0)leafcolor = Blocks.greenleaves.blockID;
		if(lf == 1)leafcolor = Blocks.redleaves.blockID;
		if(lf == 2)leafcolor = Blocks.orangeleaves.blockID;
		if(lf == 3)leafcolor = Blocks.yellowleaves.blockID;

		howmany = 6 + world.rand.nextInt(6);
		for(i=0;i<howmany;i++){
			float dy;
			float rad = 4 + world.rand.nextInt(8);
			float dx;
			float dz;
			int ioff = world.rand.nextInt(360);
			float xoff, zoff;
			xoff = (world.rand.nextFloat()-world.rand.nextFloat())*2;
			zoff = (world.rand.nextFloat()-world.rand.nextFloat())*2;
			int leafstart = 25+world.rand.nextInt(20);

			for(int idir=0;idir < 90;idir+=5){
				dx = rad - (float) (rad * Math.cos(Math.toRadians(idir)));
				dz = (float) (dx * Math.cos(Math.toRadians(ioff)));
				dx = (float) (dx * Math.sin(Math.toRadians(ioff)));				
				dy = (float) (rad * Math.sin(Math.toRadians(idir)));
				world.setblocknonotify(d,  x+(int)dx+(int)xoff, y+(int)(dy)+8, z+(int)dz+(int)zoff, Blocks.log.blockID);	
				if(idir >= leafstart){
					makeflowerleaves(world, d,  x+(int)dx+(int)xoff, y+(int)(dy)+8, z+(int)dz+(int)zoff, leafcolor);
				}
			}
		}

	}
	
	public void flowerTreeNormal(World world, int d, int x, int y, int z)
	{
		int bid;		
		//Only grow on grass or dirt
		bid = world.getblock(d, x, y, z);
		if(bid != Blocks.grassblock.blockID && bid != Blocks.dirt.blockID){
			return;
		}
		int i;
		int leafcolor = Blocks.redwoodleaves.blockID;
		int howmany = 8 + world.rand.nextInt(10);
		for(i=0;i<howmany;i++){
			float dy;
			float rad = 6 + world.rand.nextInt(12);
			float dx;
			float dz;
			int ioff = world.rand.nextInt(360);
			float xoff, zoff;
			xoff = (world.rand.nextFloat()-world.rand.nextFloat())*2;
			zoff = (world.rand.nextFloat()-world.rand.nextFloat())*2;
			int leafstart = 35+world.rand.nextInt(20);

			for(int idir=0;idir < 90;idir+=2){
				dx = rad - (float) (rad * Math.cos(Math.toRadians(idir)));
				dz = (float) (dx * Math.cos(Math.toRadians(ioff)));
				dx = (float) (dx * Math.sin(Math.toRadians(ioff)));				
				dy = (float) (rad * Math.sin(Math.toRadians(idir)));
				world.setblocknonotify(d,  x+(int)dx+(int)xoff, y+(int)(dy)+1, z+(int)dz+(int)zoff, Blocks.log.blockID);	
				if(idir >= leafstart){
					makeflowerleaves(world, d,  x+(int)dx+(int)xoff, y+(int)(dy)+1, z+(int)dz+(int)zoff, leafcolor);
				}
			}
		}

	}
	
	public void makeflowerleaves(World world, int d, int x, int y, int z, int leafcolor){
		int i, j, k;		
		for(i=-1;i<=1;i++){
			for(j=0;j<=1;j++){
				for(k=-1;k<=1;k++){
					if(world.getblock(d, x+i, y+j, z+k) == 0){
						world.setblocknonotify(d, x+i, y+j, z+k, leafcolor);
					}
				}
			}
		}
	}
	
	public void flowerTreeTwo(World world, int d, int x, int y, int z)
	{
		int bid;		
		//Only grow on grass or dirt
		bid = world.getblock(d, x, y, z);
		if(bid != Blocks.grassblock.blockID && bid != Blocks.dirt.blockID){
			return;
		}
		int i;
		int leafcolor = Blocks.redwoodleaves.blockID;		
		float dy;
		float rad = 10 + world.rand.nextInt(10);
		float dx;
		float dz;
		int ioff = world.rand.nextInt(360);
		double startx, starty, startz;
		startx = x;
		starty = y;
		startz = z;

		int leafstart = 5+world.rand.nextInt(5);

		for(int idir=0;idir < rad;idir++){				
			world.setblocknonotify(d,  x, y+idir+1, z, Blocks.willowlog.blockID);	
			if(idir >= leafstart){
				makeflowerleaves(world, d,  x, y+idir+1, z, leafcolor);
			}
			starty = y+idir+1;
		}

		int howmany = 6 + world.rand.nextInt(3);
		for(i=0;i<howmany;i++){			
			rad = 4 + world.rand.nextInt(8);			
			ioff = world.rand.nextInt(360);
			for(int idir=0;idir < 50;idir+=2){
				dx = (float) (rad * Math.sin(Math.toRadians(idir)));
				dz = (float) (dx * Math.cos(Math.toRadians(ioff)));
				dx = (float) (dx * Math.sin(Math.toRadians(ioff)));				
				dy = -(float) (rad * Math.cos(Math.toRadians(idir)));
				world.setblocknonotify(d,  (int)startx+(int)dx, (int)starty+(int)(dy)+(int)rad, (int)startz+(int)dz, Blocks.willowlog.blockID);					
				makeflowerleaves(world, d,  (int)startx+(int)dx, (int)starty+(int)(dy)+(int)rad, (int)startz+(int)dz, Blocks.yellowleaves.blockID);				
			}
		}

	}
	
	public void umbrellaTree(World world, int d, int x, int y, int z)
	{
		int bid;		
		//Only grow on grass or dirt
		bid = world.getblock(d, x, y, z);
		if(bid != Blocks.grassblock.blockID && bid != Blocks.dirt.blockID){
			return;
		}
		int i;
		int howmany = 16 + world.rand.nextInt(10);
		for(i=0;i<howmany;i++){
			float dy;
			float rad = 8 + world.rand.nextInt(12);
			float dx;
			float dz;
			int ioff = world.rand.nextInt(360);
			float xoff, zoff;
			xoff = (world.rand.nextFloat()-world.rand.nextFloat())*2;
			zoff = (world.rand.nextFloat()-world.rand.nextFloat())*2;
			int leafstart = 35+world.rand.nextInt(20);

			for(int idir=0;idir < 130;idir+=2){
				dx = rad - (float) (rad * Math.cos(Math.toRadians(idir)));
				dz = (float) (dx * Math.cos(Math.toRadians(ioff)));
				dx = (float) (dx * Math.sin(Math.toRadians(ioff)));				
				dy = (float) (rad * Math.sin(Math.toRadians(idir)));
				world.setblocknonotify(d,  x+(int)dx+(int)xoff, y+(int)(dy)+1, z+(int)dz+(int)zoff, Blocks.log.blockID);	
				if(idir >= leafstart){
					makeflowerleaves(world, d,  x+(int)dx+(int)xoff, y+(int)(dy)+1, z+(int)dz+(int)zoff, Blocks.willowleaves.blockID);
				}
			}
		}


	}
	
	public void TallPineTree(World world, int d, int x, int y, int z){
		int i, j, k, bid;
		int height;
		int width;

		height = world.rand.nextInt(20) + 10;

		for(i=height;i>0;i--){
			width = (height-i)/4;
			if(width < 1){
				world.setblock(d, x, y+i, z, Blocks.pineleaves.blockID);
			}else{
				for(j=-width;j<=width;j++){
					for(k=-width;k<=width;k++){
						bid = Blocks.pineleaves.blockID;
						if(Math.abs(j) == Math.abs(k) && Math.abs(j) > 1 && Math.abs(j) < width)bid = Blocks.log.blockID;
						if(j == 0 && Math.abs(k) > 1 && Math.abs(k) < width)bid = Blocks.log.blockID;
						if(k == 0 && Math.abs(j) > 1  && Math.abs(j) < width)bid = Blocks.log.blockID;
						if(k == 0 && j == 0)bid = Blocks.log.blockID;
						world.setblock(d, x+j, y+i, z+k, bid);							
					}
				}
			}
		}

	}
	
	public void PineTree(World world, int d, int x, int y, int z){
		int i, j, k, bid;
		int height;
		int width;

		height = world.rand.nextInt(8) + 4;

		for(i=height;i>0;i--){
			width = (height-i)/2;
			if(width < 1){
				world.setblock(d, x, y+i, z, Blocks.pineleaves.blockID);
			}else{
				for(j=-width;j<=width;j++){
					for(k=-width;k<=width;k++){
						bid = Blocks.pineleaves.blockID;
						if(Math.abs(j) == Math.abs(k) && Math.abs(j) > 1 && Math.abs(j) < width)bid = Blocks.log.blockID;
						if(j == 0 && Math.abs(k) > 1 && Math.abs(k) < width)bid = Blocks.log.blockID;
						if(k == 0 && Math.abs(j) > 1  && Math.abs(j) < width)bid = Blocks.log.blockID;
						if(k == 0 && j == 0)bid = Blocks.log.blockID;
						world.setblock(d, x+j, y+i, z+k, bid);
					}
				}
			}
		}

	}
	
	public boolean addTallPineTrees(World world, int d, int chunkX, int chunkZ)
	{
		
		//Make them at least a little scarce, otherwise they are everywhere!
		if(world.rand.nextInt(5) != 0)return false;
		
		int howmany = 0;
		boolean added = false;
		howmany = 2 + world.rand.nextInt(5);

		for(int i = 0; i < howmany; i++) {
			int posX = chunkX + world.rand.nextInt(16);
			int posZ = chunkZ + world.rand.nextInt(16);    
			for(int posY = 128; posY > 40; posY--) //Start high and search down until we find a grass/dirt block.
			{
				//Must be in air (empty block), and on a grass block
				if(world.getblock(d, posX, posY, posZ) == 0 && world.getblock(d, posX, posY-1, posZ) == Blocks.grassblock.blockID)
				{					
					this.TallPineTree(world, d, posX, posY-1, posZ);
					added = true;
					break;
				}	     	
			}
		}
		return added;
	}
	
	public boolean addPineTrees(World world, int d, int chunkX, int chunkZ)
	{
		
		//Make them at least a little scarce, otherwise they are everywhere!
		if(world.rand.nextInt(5) != 0)return false;
		
		int howmany = 0;
		boolean added = false;
		howmany = 2 + world.rand.nextInt(5);

		for(int i = 0; i < howmany; i++) {
			int posX = chunkX + world.rand.nextInt(16);
			int posZ = chunkZ + world.rand.nextInt(16);    
			for(int posY = 128; posY > 40; posY--) //Start high and search down until we find a grass/dirt block.
			{
				//Must be in air (empty block), and on a grass block
				if(world.getblock(d, posX, posY, posZ) == 0 && world.getblock(d, posX, posY-1, posZ) == Blocks.grassblock.blockID)
				{					
					this.PineTree(world, d, posX, posY-1, posZ);
					added = true;
					break;
				}	     	
			}
		}
		return added;
	}


}
