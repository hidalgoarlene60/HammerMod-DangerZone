package dangerzone.blocks;

import org.lwjgl.opengl.GL11;

import dangerzone.StitchedTexture;
import dangerzone.VBOBuffer;
import dangerzone.World;
import dangerzone.WorldRenderer;
import dangerzone.threads.FastBlockTicker;
import dangerzone.threads.VBODataBuilderThread;

public class BlockDownRail extends BlockRail {

	public BlockDownRail(String n, String txt, String fulltxt) {
		super(n, txt, fulltxt);
	}
	
	public void notifyNeighborChanged(World w, int d, int x, int y, int z){
		if(w.isServer){
			int bid = w.getblock(d, x, y+1, z);
			int bid2 = w.getblock(d, x, y-1, z);
			if(!Blocks.isSolid(bid) || bid2 != 0){
				Blocks.doBreakBlock(blockID, w, d, x, y, z);
				w.setblock(d, x, y, z, 0);
			}
		}
	}
	
	public void tickMe(World w, int d, int x, int y, int z){
		if(w.isServer){
			int bid = w.getblock(d, x, y+1, z);
			int bid2 = w.getblock(d, x, y-1, z);
			if(!Blocks.isSolid(bid) || bid2 != 0){
				Blocks.doBreakBlock(blockID, w, d, x, y, z);
				w.setblock(d, x, y, z, 0);
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
	
	public void renderMeToVBO(long chunkvbos[], WorldRenderer wr, World w, int d, int x, int y, int z, int bid, int meta, int sides, boolean focus, int xo, int yo, int zo){

		VBOBuffer v = null;
		StitchedTexture st = null;

		st = VBODataBuilderThread.findVBOtextureforblockside(5, bid);
		if(st == null)return;
		v = VBODataBuilderThread.findOrMakeVBOForTexture(chunkvbos, st, isTranslucent);
		if(v == null)return;
		
		mbf.railcenter.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
		mbf.railpostA.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo+4, zo, 0);
		mbf.railpost.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo+14, zo, 0);
		
		if((meta & 0x01) != 0){
			mbf.railside.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
		}
		if((meta & 0x02) != 0){
			mbf.railside.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 180);
		}
		if((meta & 0x04) != 0){
			mbf.railside.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 90);
		}
		if((meta & 0x08) != 0){
			mbf.railside.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, -90);
		}

	}
	
	public void renderMe(WorldRenderer wr, World w, int d, int x, int y, int z, int bid, int meta, int sides, boolean focus){

		wr.loadtextureforblockside(5, bid, false); //We only have one side. Tell world renderer to load our texture.
		
		GL11.glTranslatef(0,  -8,  0);
		

		mbf.railcenter.render(1);
		
		GL11.glTranslatef(0,  4,  0);
		mbf.railpostA.render(1);
		GL11.glTranslatef(0,  10,  0);
		mbf.railpost.render(1);
		GL11.glTranslatef(0,  -14,  0);

		if((meta & 0x01) == 0x01){
			mbf.railside.render(1);
		}
		if((meta & 0x02) == 0x02){
			GL11.glRotatef(180, 0, 1, 0);
			mbf.railside.render(1);
			GL11.glRotatef(-180, 0, 1, 0);
		}
		if((meta & 0x04) == 0x04){
			GL11.glRotatef(-90, 0, 1, 0);
			mbf.railside.render(1);
			GL11.glRotatef(90, 0, 1, 0);
		}
		if((meta & 0x08) == 0x08){
			GL11.glRotatef(90, 0, 1, 0);
			mbf.railside.render(1);
			GL11.glRotatef(-90, 0, 1, 0);
		}

	}


}
