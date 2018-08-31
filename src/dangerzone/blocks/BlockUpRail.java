package dangerzone.blocks;

import dangerzone.World;
import dangerzone.threads.FastBlockTicker;

public class BlockUpRail extends BlockRail {

	public BlockUpRail(String n, String txt, String fulltxt) {
		super(n, txt, fulltxt);
	}
	
	public void notifyNeighborChanged(World w, int d, int x, int y, int z){
		if(w.isServer){
			int bid = w.getblock(d, x, y-1, z);
			int bid2 = w.getblock(d, x, y+1, z);
			if(!Blocks.isSolid(bid) || bid2 != 0){
				Blocks.doBreakBlock(blockID, w, d, x, y, z);
				w.setblock(d,  x,  y,  z,  0);
			}
		}
	}
	
	public void tickMe(World w, int d, int x, int y, int z){
		if(w.isServer){
			int bid = w.getblock(d, x, y-1, z);
			int bid2 = w.getblock(d, x, y+1, z);
			if(!Blocks.isSolid(bid) || bid2 != 0){
				Blocks.doBreakBlock(blockID, w, d, x, y, z);
				w.setblock(d,  x,  y,  z,  0);
				return;
			}
			
			int meta = 0;
			if(isRailBlock(w, d, x+1, y, z))meta |= 0x01;
			if(isRailBlock(w, d, x-1, y, z))meta |= 0x02;
			if(isRailBlock(w, d, x, y, z+1))meta |= 0x04;
			if(isRailBlock(w, d, x, y, z-1))meta |= 0x08;
			
			
			if(isRailBlock(w, d, x+1, y-1, z))meta |= 0x01;
			if(isRailBlock(w, d, x-1, y-1, z))meta |= 0x02;
			if(isRailBlock(w, d, x, y-1, z+1))meta |= 0x04;
			if(isRailBlock(w, d, x, y-1, z-1))meta |= 0x08;
						
			w.setblockandmeta(d, x, y, z, blockID, meta);		

			//tick again later!
			FastBlockTicker.addFastTick(d,  x,  y, z, 8+w.rand.nextInt(3)); //ticks, in TENTHS of a second
						
		}
	}

}
