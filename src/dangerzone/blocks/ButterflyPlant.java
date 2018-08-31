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
import dangerzone.Player;
import dangerzone.StitchedTexture;
import dangerzone.VBOBuffer;
import dangerzone.World;
import dangerzone.WorldRenderer;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityBlockItem;
import dangerzone.threads.VBODataBuilderThread;


public class ButterflyPlant extends Block {
	private boolean compiled = false; //graphics outputs compiled. Or not.
	private int myrenderid = 0;
	private float blockrenderwidth = 12;
		
	public ButterflyPlant(String n, String txt){
		super(n, txt);
		breaksound = "DangerZone:leavesbreak";
		placesound = "DangerZone:leavesplace";
		hitsound =   "DangerZone:leaves_hit";
		isSolidForRendering = false;
		isSolid = false;
		hasOwnRenderer = true;
		randomtick = true;	
		isLeaves = true;
		maxdamage = 1;
		burntime = 10;
		showInInventory = true;
		if(!n.equals("DangerZone:Butterfly Plant")){
			showInInventory = false;
		}
	}
	
	public int getBlockDrop(Player p, World w, int dimension, int x, int y, int z){
		return Blocks.butterfly_plant.blockID;
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
					e.fill(Blocks.butterfly_plant, 1); //I am a block!		
					w.spawnEntityInWorld(e);
				}
			}
		}
	}
	
	public void tickMe(World w, int d, int x, int y, int z){
		if(!w.isServer)return;
		if(w.getblock(d, x, y+1, z) != 0){
			w.setblock(d, x, y, z, 0); //Can't grow with anything on top of me!
			//System.out.printf("Grass died\n");
			return;
		}
		int bid = w.getblock(d, x, y-1, z);
		if(bid != Blocks.grassblock.blockID && bid != Blocks.dirt.blockID){
			w.setblock(d, x, y, z, 0); 
			//System.out.printf("Grass died\n");
			return;
		}
		
		if(w.isDaytime() && w.rand.nextInt(15) == 1){
			int howmany = 1 + w.rand.nextInt(3);
			if(this.blockID == Blocks.butterfly_plant1.blockID)howmany += 1;
			if(this.blockID == Blocks.butterfly_plant2.blockID)howmany += 2;
			if(this.blockID == Blocks.butterfly_plant3.blockID)howmany += 3;
			for(int i=0;i<howmany;i++){
				Entity eb = w.createEntityByName("DangerZone:Butterfly", d, ((double)x)+0.5f, ((double)y)+1.05f, ((double)z)+0.5f);
				if(eb != null){
					eb.init();
					w.spawnEntityInWorld(eb);
				}
			}
			
			if(w.rand.nextInt(5) == 1){
				int growto = 0;
				if(this.blockID == Blocks.butterfly_plant.blockID)growto = Blocks.butterfly_plant1.blockID;
				if(this.blockID == Blocks.butterfly_plant1.blockID)growto = Blocks.butterfly_plant2.blockID;
				if(this.blockID == Blocks.butterfly_plant2.blockID)growto = Blocks.butterfly_plant3.blockID;
				if(growto != 0){
					if(!BreakChecks.canChangeBlock(w, d, x, y, z, d, x, y, z, 0, 0))return;
					w.setblock(d, x, y, z, growto);
				}
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
			
			v.addVertexInfoToVBO(brw + xo, brw + yo - 2, brw + zo, st.xoffsetmax, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 	
			v.addVertexInfoToVBO(-brw + xo, brw + yo - 2, brw + zo, st.xoffsetmin, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
			v.addVertexInfoToVBO(-brw + xo, -brw + yo - 2, brw + zo, st.xoffsetmin, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
			v.addVertexInfoToVBO(brw + xo, -brw + yo - 2, brw + zo, st.xoffsetmax, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb);
	
			v.addVertexInfoToVBO(-brw + xo, brw + yo - 2, -brw + zo, st.xoffsetmax, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 
			v.addVertexInfoToVBO(brw + xo, brw + yo - 2, -brw + zo, st.xoffsetmin, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 
			v.addVertexInfoToVBO(brw + xo, -brw + yo - 2, -brw + zo, st.xoffsetmin, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 
			v.addVertexInfoToVBO(-brw + xo, -brw + yo - 2, -brw + zo, st.xoffsetmax, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 

			v.addVertexInfoToVBO(-brw + xo, brw + yo - 2, brw + zo, st.xoffsetmax, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb);
			v.addVertexInfoToVBO(-brw + xo, brw + yo - 2, -brw + zo, st.xoffsetmin, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 
			v.addVertexInfoToVBO(-brw + xo, -brw + yo - 2, -brw + zo, st.xoffsetmin, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 
			v.addVertexInfoToVBO(-brw + xo, -brw + yo - 2, brw + zo, st.xoffsetmax, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb);

			v.addVertexInfoToVBO(brw + xo, brw + yo - 2, -brw + zo, st.xoffsetmax, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 
			v.addVertexInfoToVBO(brw + xo, brw + yo - 2, brw + zo, st.xoffsetmin, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 
			v.addVertexInfoToVBO(brw + xo, -brw + yo - 2, brw + zo, st.xoffsetmin, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 
			v.addVertexInfoToVBO(brw + xo, -brw + yo - 2, -brw + zo, st.xoffsetmax, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 
		
		}

	}
	
	public void renderMe(WorldRenderer wr, World w, int d, int x, int y, int z, int bid, int meta, int sides, boolean focus){

		if(!compiled){
			myrenderid = DangerZone.wr.getNextRenderID();
			GL11.glNewList(myrenderid, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
						
			GL11.glTexCoord2f(1,0);
			GL11.glVertex3f(blockrenderwidth/2, blockrenderwidth/2 - 2, blockrenderwidth/2); // Top Right Of The Quad (Front)
			GL11.glTexCoord2f(0,0);
			GL11.glVertex3f(-blockrenderwidth/2, blockrenderwidth/2 - 2, blockrenderwidth/2); // Top Left Of The Quad (Front)
			GL11.glTexCoord2f(0,1);
			GL11.glVertex3f(-blockrenderwidth/2, -blockrenderwidth/2 - 2, blockrenderwidth/2); // Bottom Left Of The Quad (Front)
			GL11.glTexCoord2f(1,1);
			GL11.glVertex3f(blockrenderwidth/2, -blockrenderwidth/2 - 2, blockrenderwidth/2); // Bottom Right Of The Quad (Front)
			
			GL11.glTexCoord2f(1,0);
			GL11.glVertex3f(-blockrenderwidth/2, blockrenderwidth/2 - 2, -blockrenderwidth/2); // Top Left Of The Quad (Back)
			GL11.glTexCoord2f(0,0);
			GL11.glVertex3f(blockrenderwidth/2, blockrenderwidth/2 - 2, -blockrenderwidth/2); // Top Right Of The Quad (Back)
			GL11.glTexCoord2f(0,1);
			GL11.glVertex3f(blockrenderwidth/2, -blockrenderwidth/2 - 2, -blockrenderwidth/2); // Bottom Right Of The Quad (Back)
			GL11.glTexCoord2f(1,1);
			GL11.glVertex3f(-blockrenderwidth/2, -blockrenderwidth/2 - 2, -blockrenderwidth/2); // Bottom Left Of The Quad (Back)
			
			GL11.glTexCoord2f(1,0);
			GL11.glVertex3f(-blockrenderwidth/2, blockrenderwidth/2 - 2, blockrenderwidth/2); // Top Right Of The Quad (Left)
			GL11.glTexCoord2f(0,0);
			GL11.glVertex3f(-blockrenderwidth/2, blockrenderwidth/2 - 2, -blockrenderwidth/2); // Top Left Of The Quad (Left)
			GL11.glTexCoord2f(0,1);
			GL11.glVertex3f(-blockrenderwidth/2, -blockrenderwidth/2 - 2, -blockrenderwidth/2); // Bottom Left Of The Quad (Left)
			GL11.glTexCoord2f(1,1);
			GL11.glVertex3f(-blockrenderwidth/2, -blockrenderwidth/2 - 2, blockrenderwidth/2); // Bottom Right Of The Quad (Left)
			
			GL11.glTexCoord2f(1,0);
			GL11.glVertex3f(blockrenderwidth/2, blockrenderwidth/2 - 2, -blockrenderwidth/2); // Top Right Of The Quad (Right)
			GL11.glTexCoord2f(0,0);
			GL11.glVertex3f(blockrenderwidth/2, blockrenderwidth/2 - 2, blockrenderwidth/2); // Top Left Of The Quad (Right)
			GL11.glTexCoord2f(0,1);
			GL11.glVertex3f(blockrenderwidth/2, -blockrenderwidth/2 - 2, blockrenderwidth/2); // Bottom Left Of The Quad (Right)
			GL11.glTexCoord2f(1,1);
			GL11.glVertex3f(blockrenderwidth/2, -blockrenderwidth/2 - 2, -blockrenderwidth/2); // Bottom Right Of The Quad (Right)			
			
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
