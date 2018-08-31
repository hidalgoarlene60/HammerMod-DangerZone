package dangerzone.blocks;


import org.lwjgl.opengl.GL11;

import dangerzone.BreakChecks;
import dangerzone.DangerZone;
import dangerzone.StitchedTexture;
import dangerzone.Utils;
import dangerzone.VBOBuffer;
import dangerzone.World;
import dangerzone.WorldRenderer;
import dangerzone.biomes.Trees;
import dangerzone.blocks.Block;
import dangerzone.blocks.Blocks;
import dangerzone.entities.EntityBlockItem;
import dangerzone.threads.VBODataBuilderThread;

public class BlockSapling extends Block {
	private boolean compiled = false; //graphics outputs compiled. Or not.
	private int myrenderid = 0;
	private float blockrenderwidth = 16;

	public BlockSapling(String n, String txt) {
		super(n, txt);
		isStone = false;
		maxdamage = 5;
		mindamage = 1;
		breaksound = "DangerZone:dirt_hit";
		placesound = "DangerZone:dirt_place";
		hitsound =   "DangerZone:dirt_hit";
		isSolidForRendering = false;
		isSolid = false;
		hasOwnRenderer = true;
		randomtick = true;	
	}
	
	public void notifyNeighborChanged(World w, int d, int x, int y, int z){
		int bid = w.getblock(d, x, y-1, z);
		if(bid != Blocks.grassblock.blockID && bid != Blocks.dirt.blockID){
			//Don't know what I'm on, but it's not for growing!
			w.setblock(d, x, y, z, 0); 
			//sometimes drop a block
			if(w.rand.nextInt(4) == 1){
				EntityBlockItem e = (EntityBlockItem)w.createEntityByName(DangerZone.blockitemname, d, (double)x+0.5f, (double)y+0.5f, (double)z+0.5f);
				if(e != null){
					e.fill(this, 1); //I am a block!		
					w.spawnEntityInWorld(e);
				}
			}
		}
	}
	
	public void tickMe(World w, int d, int x, int y, int z){
	   	int bid;
		if(w.isServer){
			Utils.spawnParticlesFromServer(w, "DangerZone:ParticleSparkle", 10, d, x + 0.5f, y + 0.25f, z + 0.5f);
		}else{
			return;
		}
		if(!BreakChecks.canChangeBlock(w, d, x, y, z, d, x, y, z, 0, 0))return;
		
		if(w.getblock(d, x, y+1, z) != 0){
			w.setblock(d, x, y, z, 0); //Can't grow with anything on top of me!
			return;
		}
		bid = w.getblock(d, x, y-1, z);
		if(bid != Blocks.grassblock.blockID && bid != Blocks.dirt.blockID){
			w.setblock(d, x, y, z, 0); 
			return;
		}
		
		if(w.rand.nextInt(15) != 1)return;
		w.setblock(d, x, y, z, 0);//Destroy self  
		
		Trees tr = new Trees();
		
		if(blockID == Blocks.sapling_tallwood.blockID)tr.TallWoodTree(w, d, x, y, z);		
		if(blockID == Blocks.sapling_cherry.blockID)tr.makeFruitTree(w, d, x, y-1, z, Blocks.cherryleaves.blockID);
		if(blockID == Blocks.sapling_peach.blockID)tr.makeFruitTree(w, d, x, y-1, z, Blocks.peachleaves.blockID);
		if(blockID == Blocks.sapling_apple.blockID)tr.makeFruitTree(w, d, x, y-1, z, Blocks.appleleaves.blockID);
		if(blockID == Blocks.sapling_scragglyredwood.blockID)tr.ScragglyRedwoodTree(w, d, x, y, z);
		if(blockID == Blocks.sapling_scraggly.blockID)tr.ScragglyTreeWithBranches(w, d, x, y, z);
		if(blockID == Blocks.sapling_bigroundredwood.blockID)tr.MakeBigRoundTree(w, d, x, y, z, Blocks.redwoodlog.blockID, Blocks.redwoodleaves.blockID, 6);
		if(blockID == Blocks.sapling_bigroundwillow.blockID)tr.MakeBigRoundTree(w, d, x, y, z, Blocks.willowlog.blockID, Blocks.willowleaves.blockID, 6);
		if(blockID == Blocks.sapling_flower.blockID)tr.flowerTree(w, d, x, y-1, z);	
		if(blockID == Blocks.sapling_flowertwo.blockID)tr.flowerTreeTwo(w, d, x, y-1, z);	
		if(blockID == Blocks.sapling_scrub.blockID)tr.scrubTree(w, d, x, y-1, z, w.rand.nextInt(4));	
		if(blockID == Blocks.sapling_flowernormal.blockID)tr.flowerTreeNormal(w, d, x, y-1, z);
		if(blockID == Blocks.sapling_umbrella.blockID)tr.umbrellaTree(w, d, x, y-1, z);
		if(blockID == Blocks.sapling_bulb.blockID)tr.bulbTree(w, d, x, y-1, z);
		if(blockID == Blocks.sapling_looplowspiral.blockID)tr.looplowspiralTree(w, d, x, y-1, z);	
		if(blockID == Blocks.sapling_loop.blockID)tr.loopTree(w, d, x, y-1, z);
		if(blockID == Blocks.sapling_generic.blockID)tr.makeGenericTree(w, d, x, y-1, z);
		if(blockID == Blocks.sapling_tallwillow.blockID)tr.TallWillowTree(w, d, x, y-1, z);
		if(blockID == Blocks.sapling_vase.blockID)tr.vaseTree(w, d, x, y-1, z);
		if(blockID == Blocks.sapling_spiral.blockID)tr.spiralTree(w, d, x, y-1, z);
		if(blockID == Blocks.sapling_loopspiral.blockID)tr.loopspiralTree(w, d, x, y-1, z);
		if(blockID == Blocks.sapling_bowl.blockID)tr.bowlTree(w, d, x, y-1, z);
		if(blockID == Blocks.sapling_doublebowl.blockID)tr.doublebowlTree(w, d, x, y-1, z);	
				
	}
	
	
	public void renderMeToVBO(long chunkvbos[], WorldRenderer wr, World w, int d, int x, int y, int z, int bid, int meta, int sides, boolean focus, int xo, int yo, int zo){

		VBOBuffer v = null;
		StitchedTexture st = null;
		float brw = blockrenderwidth/2;
		
		st = VBODataBuilderThread.findVBOtextureforblockside(0, bid); //loads us into the stitching if we are not already!
		v = VBODataBuilderThread.findOrMakeVBOForTexture(chunkvbos, st, isTranslucent);
		
		if(v != null){
			v.addVertexInfoToVBO(brw + xo, brw + yo, brw + zo, st.xoffsetmax, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 	
			v.addVertexInfoToVBO(-brw + xo, brw + yo, -brw + zo, st.xoffsetmin, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
			v.addVertexInfoToVBO(-brw + xo, -brw + yo, -brw + zo, st.xoffsetmin, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
			v.addVertexInfoToVBO(brw + xo, -brw + yo, brw + zo, st.xoffsetmax, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb);
			
			v.addVertexInfoToVBO(brw + xo, brw + yo, -brw + zo, st.xoffsetmax, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 	
			v.addVertexInfoToVBO(-brw + xo, brw + yo, brw + zo, st.xoffsetmin, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
			v.addVertexInfoToVBO(-brw + xo, -brw + yo, brw + zo, st.xoffsetmin, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
			v.addVertexInfoToVBO(brw + xo, -brw + yo, -brw + zo, st.xoffsetmax, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb);
		}

	}
	
	public void renderMe(WorldRenderer wr, World w, int d, int x, int y, int z, int bid, int meta, int sides, boolean focus){

		if(!compiled){
			myrenderid = DangerZone.wr.getNextRenderID();
			GL11.glNewList(myrenderid, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			
			GL11.glTexCoord2f(1,0);
			GL11.glVertex3f(blockrenderwidth/2, blockrenderwidth/2, blockrenderwidth/2);
			GL11.glTexCoord2f(0,0);
			GL11.glVertex3f(-blockrenderwidth/2, blockrenderwidth/2, -blockrenderwidth/2);
			GL11.glTexCoord2f(0,1);
			GL11.glVertex3f(-blockrenderwidth/2, -blockrenderwidth/2, -blockrenderwidth/2); 
			GL11.glTexCoord2f(1,1);
			GL11.glVertex3f(blockrenderwidth/2, -blockrenderwidth/2, blockrenderwidth/2); 
			
			GL11.glTexCoord2f(1,0);
			GL11.glVertex3f(blockrenderwidth/2, blockrenderwidth/2, -blockrenderwidth/2); 
			GL11.glTexCoord2f(0,0);
			GL11.glVertex3f(-blockrenderwidth/2, blockrenderwidth/2, blockrenderwidth/2); 
			GL11.glTexCoord2f(0,1);
			GL11.glVertex3f(-blockrenderwidth/2, -blockrenderwidth/2, blockrenderwidth/2);
			GL11.glTexCoord2f(1,1);
			GL11.glVertex3f(blockrenderwidth/2, -blockrenderwidth/2, -blockrenderwidth/2); 
			
			GL11.glEnd(); // Done Drawing The Quad
			GL11.glEndList();			
			compiled = true;
		}

		wr.loadtextureforblockside(0, bid, false); //We only have one side. Tell world renderer to load our texture.
		
		//We are NOT solid
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glCallList(myrenderid); //draw
		
		GL11.glDisable(GL11.GL_BLEND);

	}

}