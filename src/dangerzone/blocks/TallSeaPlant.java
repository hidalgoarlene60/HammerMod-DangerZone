package dangerzone.blocks;

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

import org.lwjgl.opengl.GL11;


import dangerzone.BreakChecks;
import dangerzone.DangerZone;
import dangerzone.StitchedTexture;
import dangerzone.VBOBuffer;
import dangerzone.World;
import dangerzone.WorldRenderer;
import dangerzone.entities.EntityBlockItem;
import dangerzone.threads.VBODataBuilderThread;


public class TallSeaPlant extends Block {
	private boolean compiled = false; //graphics outputs compiled. Or not.
	private int myrenderid = 0;
	private float blockrenderwidth = 16;
		
	public TallSeaPlant(String n, String txt){
		super(n, txt);
		breaksound = "DangerZone:leavesbreak";
		placesound = "DangerZone:leavesplace";
		hitsound =   "DangerZone:leaves_hit";
		isSolidForRendering = false;
		isSolid = false;
		hasOwnRenderer = true;
		alwaysRender = true;
		randomtick = true;	
		isLeaves = true;
		isWaterPlant = true;
		maxdamage = 1;
		burntime = 10;
	}
	
	public void notifyNeighborChanged(World w, int d, int x, int y, int z){
		int bid = w.getblock(d, x, y-1, z);
		if(bid != Blocks.stone.blockID && bid != Blocks.sand.blockID && bid != blockID){
			//Don't know what I'm on, but it's not for growing!
			w.setblock(d, x, y, z, Blocks.water.blockID); //Can't grow on anything except stone!
			//System.out.printf("Grass died 4\n");
			//sometimes drop a grass block
			if(w.rand.nextInt(4) == 1){
				EntityBlockItem e = (EntityBlockItem)w.createEntityByName(DangerZone.blockitemname, d, (double)x+0.5f, (double)y+0.5f, (double)z+0.5f);
				if(e != null){
					e.fill(this, 1); //I am a block!	
					w.spawnEntityInWorld(e);
				}
			}
		}
		bid = w.getblock(d, x, y+1, z);
		if(bid != Blocks.waterstatic.blockID && bid != Blocks.water.blockID && bid != blockID){
			//Don't know what I'm on, but it's not for growing!
			w.setblock(d, x, y, z, Blocks.water.blockID); //Can't grow under anything except waterstatic!
			//System.out.printf("Grass died 5\n");
			//sometimes drop a grass block
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
		int i, j, k;
		int bid = 0;
		
		bid = w.getblock(d, x, y+1, z);
		if(bid != Blocks.waterstatic.blockID && bid != blockID){
			w.setblock(d, x, y, z, Blocks.water.blockID); //Can't grow with anything on top of me!
			//System.out.printf("Grass died 1\n");
			return;
		}
		bid = w.getblock(d, x, y-1, z);
		if(bid != Blocks.stone.blockID && bid != Blocks.sand.blockID && bid != blockID){
			w.setblock(d, x, y, z, Blocks.water.blockID); //Can't grow on anything except grassblock!
			//System.out.printf("Grass died 2\n");
			return;
		}
		if(!BreakChecks.canChangeBlock(w, d, x, y, z, d, x, y, z, 0, 0))return;

		//Grow sideways?
		if(w.rand.nextInt(2000) == 1){
			for(i=-1;i<=1;i++){
				for(j=-1;j<=1;j++){
					for(k=-1;k<=1;k++){
						bid = w.getblock(d, x+i, y+j, z+k);
						if(bid==Blocks.stone.blockID || bid==Blocks.sand.blockID){
							if(w.getblock(d, x+i, y+j+1, z+k) == Blocks.waterstatic.blockID){
								if(w.rand.nextInt(4) == 1)w.setblock(d, x+i, y+j+1, z+k, blockID);
							}
						}
					}
				}
			}
		}
		
		if(w.rand.nextInt(200) == 1){
			j=y;
			while(j < 255){
				j++;
				bid = w.getblock(d, x, j, z);
				if(bid == blockID)continue;
				if(bid != Blocks.waterstatic.blockID)break;
				if(w.getblock(d, x, j+1, z) != Blocks.waterstatic.blockID)break;
				w.setblock(d, x, j, z, blockID);
				break;
			}
		}
		
		
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
		//GL11.glEnable(GL11.GL_BLEND);
		//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glCallList(myrenderid); //draw
		
		//GL11.glDisable(GL11.GL_BLEND);

	}

}
