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
import dangerzone.Utils;
import dangerzone.VBOBuffer;
import dangerzone.World;
import dangerzone.WorldRenderer;
import dangerzone.entities.EntityBlockItem;
import dangerzone.threads.VBODataBuilderThread;


public class LightStick extends Block {
	private boolean compiled = false; //graphics outputs compiled. Or not.
	private int myrenderid = 0;
	private float blockrenderwidth = 16;
		
	public LightStick(String n, String txt){
		super(n, txt);
		isSolidForRendering = false;
		isSolid = false;
		alwaysRender = true;
		hasOwnRenderer = true;
		brightness = 0.75f;
		maxdamage = 1;
		randomtick = true;
		burntime = 30;
	}
	
	public void notifyNeighborChanged(World w, int d, int x, int y, int z){
		int bid = 0;
		int meta = w.getblockmeta(d, x, y, z); //get my metadata!
		
		if(!w.isServer)return;
		if(w.getblock(d, x, y, z) != blockID)return;
		
		//see if i am still attached!
		switch((meta>>8)){
		case 1:
			bid = w.getblock(d, x, y, z-1);
			break;
		case 2:
			bid = w.getblock(d, x, y, z+1);
			break;
		case 3:
			bid = w.getblock(d, x+1, y, z);
			break;
		case 4:
			bid = w.getblock(d, x-1, y, z);
			break;
		case 5:
			bid = w.getblock(d, x, y+1, z);
			break;
		default:
			bid = w.getblock(d, x, y-1, z);
		}
		
		if(!Blocks.isSolid(bid)){
			//Don't know what I'm on, but it's not good to stay!
			if(!BreakChecks.canChangeBlock(w, d, x, y, z, d, x, y, z, 0, 0))return;
			w.setblock(d, x, y, z, 0); //Can't be here!
			//drop	
			EntityBlockItem e = (EntityBlockItem)w.createEntityByName(DangerZone.blockitemname, d, (double)x+0.5f, (double)y+0.5f, (double)z+0.5f);
			if(e != null){
				e.fill(this, 1); //I am a block!	
				w.spawnEntityInWorld(e);
			}			
		}
	}
	
	public void tickMe(World w, int d, int x, int y, int z){
		Utils.spawnParticlesFromServer(w, "DangerZone:ParticleFire", w.rand.nextInt(3)+3, d, x+0.5f, y+0.5f, z+0.5f);
	}

	
	/*
		This is the OLD-style torch... see BetterTorch if you want the nice new one...
	 */
	public void renderMeToVBO(long chunkvbos[], WorldRenderer wr, World w, int d, int x, int y, int z, int bid, int meta, int sides, boolean focus, int xo, int yo, int zo){
		
		VBOBuffer v = null;
		StitchedTexture st = null;
		int brw = (int) (blockrenderwidth/2);

		st = VBODataBuilderThread.findVBOtextureforblockside(0, bid);
		v = VBODataBuilderThread.findOrMakeVBOForTexture(chunkvbos, st, isTranslucent);
		if(v != null){
			switch((meta>>8)){
			case 1: //on front
				v.addVertexInfoToVBO(brw + xo, brw + yo, brw + zo, st.xoffsetmax, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 	
				v.addVertexInfoToVBO(-brw + xo, -brw + yo, brw + zo, st.xoffsetmin, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
				v.addVertexInfoToVBO(-brw + xo, -brw + yo, -brw + zo, st.xoffsetmin, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
				v.addVertexInfoToVBO(brw + xo, brw + yo, -brw + zo, st.xoffsetmax, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb);

				v.addVertexInfoToVBO(brw + xo, -brw + yo, brw + zo, st.xoffsetmax, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 	
				v.addVertexInfoToVBO(-brw + xo, brw + yo, brw + zo, st.xoffsetmin, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
				v.addVertexInfoToVBO(-brw + xo, brw + yo, -brw + zo, st.xoffsetmin, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
				v.addVertexInfoToVBO(brw + xo, -brw + yo, -brw + zo, st.xoffsetmax, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb);
				break;
			case 2: //on back
				v.addVertexInfoToVBO(brw + xo, brw + yo, -brw + zo, st.xoffsetmax, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 	
				v.addVertexInfoToVBO(-brw + xo, -brw + yo, -brw + zo, st.xoffsetmin, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
				v.addVertexInfoToVBO(-brw + xo, -brw + yo, brw + zo, st.xoffsetmin, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
				v.addVertexInfoToVBO(brw + xo, brw + yo, brw + zo, st.xoffsetmax, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb);

				v.addVertexInfoToVBO(brw + xo, -brw + yo, -brw + zo, st.xoffsetmax, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 	
				v.addVertexInfoToVBO(-brw + xo, brw + yo, -brw + zo, st.xoffsetmin, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
				v.addVertexInfoToVBO(-brw + xo, brw + yo, brw + zo, st.xoffsetmin, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
				v.addVertexInfoToVBO(brw + xo, -brw + yo, brw + zo, st.xoffsetmax, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb);
				break;
			case 3: //on left
				v.addVertexInfoToVBO(-brw + xo, brw + yo, brw + zo, st.xoffsetmax, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 	
				v.addVertexInfoToVBO(-brw + xo, -brw + yo, -brw + zo, st.xoffsetmin, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
				v.addVertexInfoToVBO(brw + xo, -brw + yo, -brw + zo, st.xoffsetmin, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
				v.addVertexInfoToVBO(brw + xo, brw + yo, brw + zo, st.xoffsetmax, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb);

				v.addVertexInfoToVBO(-brw + xo, -brw + yo, brw + zo, st.xoffsetmax, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 	
				v.addVertexInfoToVBO(-brw + xo, brw + yo, -brw + zo, st.xoffsetmin, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
				v.addVertexInfoToVBO(brw + xo, brw + yo, -brw + zo, st.xoffsetmin, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
				v.addVertexInfoToVBO(brw + xo, -brw + yo, brw + zo, st.xoffsetmax, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb);
				break;
			case 4: //on right
				v.addVertexInfoToVBO(brw + xo, brw + yo, brw + zo, st.xoffsetmax, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 	
				v.addVertexInfoToVBO(brw + xo, -brw + yo, -brw + zo, st.xoffsetmin, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
				v.addVertexInfoToVBO(-brw + xo, -brw + yo, -brw + zo, st.xoffsetmin, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
				v.addVertexInfoToVBO(-brw + xo, brw + yo, brw + zo, st.xoffsetmax, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb);

				v.addVertexInfoToVBO(brw + xo, -brw + yo, brw + zo, st.xoffsetmax, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 	
				v.addVertexInfoToVBO(brw + xo, brw + yo, -brw + zo, st.xoffsetmin, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
				v.addVertexInfoToVBO(-brw + xo, brw + yo, -brw + zo, st.xoffsetmin, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
				v.addVertexInfoToVBO(-brw + xo, -brw + yo, brw + zo, st.xoffsetmax, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb);
				break;
			case 5: //upside down
				v.addVertexInfoToVBO(brw + xo, -brw + yo, brw + zo, st.xoffsetmax, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 	
				v.addVertexInfoToVBO(-brw + xo, -brw + yo, -brw + zo, st.xoffsetmin, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
				v.addVertexInfoToVBO(-brw + xo, brw + yo, -brw + zo, st.xoffsetmin, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
				v.addVertexInfoToVBO(brw + xo, brw + yo, brw + zo, st.xoffsetmax, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb);

				v.addVertexInfoToVBO(brw + xo, -brw + yo, -brw + zo, st.xoffsetmax, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 	
				v.addVertexInfoToVBO(-brw + xo, -brw + yo, brw + zo, st.xoffsetmin, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
				v.addVertexInfoToVBO(-brw + xo, brw + yo, brw + zo, st.xoffsetmin, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
				v.addVertexInfoToVBO(brw + xo, brw + yo, -brw + zo, st.xoffsetmax, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb);
				break;
			default: //rightside up
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
		
		switch((meta>>8)){
		case 1:
			GL11.glPushMatrix();
			GL11.glRotatef(90f, 1, 0, 0);
			break;
		case 2:
			GL11.glPushMatrix();
			GL11.glRotatef(-90f, 1, 0, 0);
			break;
		case 3:
			GL11.glPushMatrix();
			GL11.glRotatef(90f, 0, 0, 1);
			break;
		case 4:
			GL11.glPushMatrix();
			GL11.glRotatef(-90f, 0, 0, 1);
			break;
		case 5:
			GL11.glPushMatrix();
			GL11.glRotatef(180f, 1, 0, 0);
			break;
		default:
				
		}		

		wr.loadtextureforblockside(0, bid, false); //We only have one side. Tell world renderer to load our texture.
		
		//We are NOT solid
		//GL11.glEnable(GL11.GL_BLEND);
		//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glCallList(myrenderid); //draw
		
		//GL11.glDisable(GL11.GL_BLEND);
				
		switch((meta>>8)){
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
			GL11.glPopMatrix();
			break;
		default:
				
		}
		

	}
	
	//client-side only
	public boolean doPlaceBlock(int focusbid, Player p, World w, int dimension, int x, int y, int z, int side){
		//System.out.printf("side = %d\n", side);	
		
		if(!Blocks.isSolid(focusbid))return false;
		if(Blocks.isLeaves(focusbid))return false;
		if(!Blocks.isSolidThisSide(focusbid, w, dimension, x, y, z, side))return false;
		if(!BreakChecks.canChangeBlock(p, dimension, x, y, z, this.blockID, 0))return false; //close enough
		
		switch(side){
		case 0:
			w.setblockandmeta(dimension, x, y+1, z, this.blockID, side<<8);
			onBlockPlaced(w, dimension, x, y+1, z);
			break;
		case 1:
			w.setblockandmeta(dimension, x, y, z+1, this.blockID, side<<8);
			onBlockPlaced(w, dimension, x, y, z+1);
			break;
		case 2:
			w.setblockandmeta(dimension, x, y, z-1, this.blockID, side<<8);
			onBlockPlaced(w, dimension, x, y, z-1);
			break;
		case 3:
			w.setblockandmeta(dimension, x-1, y, z, this.blockID, side<<8);
			onBlockPlaced(w, dimension, x-1, y, z);
			break;
		case 4:
			w.setblockandmeta(dimension, x+1, y, z, this.blockID, side<<8);
			onBlockPlaced(w, dimension, x+1, y, z);
			break;
		case 5:
			w.setblockandmeta(dimension, x, y-1, z, this.blockID, side<<8);
			onBlockPlaced(w, dimension, x, y-1, z);
			break;
		}
		
		w.playSound(Blocks.getPlaceSound(this.blockID), dimension, x, y, z, 0.5f, 1.0f);

		return true;
	}


}
