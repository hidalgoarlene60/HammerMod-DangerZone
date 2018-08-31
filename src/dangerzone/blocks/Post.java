package dangerzone.blocks;


import dangerzone.Player;
import dangerzone.World;
import dangerzone.threads.FastBlockTicker;

public class Post extends BlockBarrier {

	public Post(String n, String txt, String fulltxt) {
		super(n, txt, fulltxt);
	}
	
	public void notifyNeighborChanged(World w, int d, int x, int y, int z){
		if(w.isServer){
			int bid = w.getblock(d, x, y-1, z);
			if(!Blocks.isSolid(bid)){
				bid = w.getblock(d, x, y+1, z);
				if(!Blocks.isSolid(bid)){
					w.setblock(d, x, y, z, 0);
				}
			}
		}
	}
	
	public void tickMeFast(World w, int d, int x, int y, int z){
		tickMe(w, d, x, y, z);
	}
	
	public void tickMe(World w, int d, int x, int y, int z){
		if(w.isServer){
			int bid = w.getblock(d, x, y-1, z);
			if(!Blocks.isSolid(bid)){
				bid = w.getblock(d, x, y+1, z);
				if(!Blocks.isSolid(bid)){
					w.setblock(d, x, y, z, 0);
					return;
				}
			}
			
			int meta = 0;
			if(Blocks.isSolid(w.getblock(d, x+1, y, z)))meta |= 0x01;
			if(Blocks.isSolid(w.getblock(d, x-1, y, z)))meta |= 0x02;
			if(Blocks.isSolid(w.getblock(d, x, y, z+1)))meta |= 0x04;
			if(Blocks.isSolid(w.getblock(d, x, y, z-1)))meta |= 0x08;
			w.setblockandmeta(d, x, y, z, blockID, meta);	

			FastBlockTicker.addFastTick(d,  x,  y, z, 2); //ticks, in TENTHS of a second
		}
	}
	
	
	public float getBrightness(World w, int d, int x, int y, int z){
		return 0;
	}
	
	public int getBlockDrop(Player p, World w, int d, int x, int y, int z){
		return this.blockID;
	}

}
