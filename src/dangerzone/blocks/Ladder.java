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
import org.newdawn.slick.opengl.Texture;


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


public class Ladder extends Block {

	private static ModelLadder mdl = null;
	private String fulltexturestring = null;
	private Texture fulltexture = null;
		
	public Ladder(String n, String flattxt, String txt){
		super(n, flattxt);
		isSolidForRendering = false;
		isSolid = false;
		hasOwnRenderer = true;
		alwaysRender = true;
		maxdamage = 3;
		burntime = 90;
		friction = 0.65f;
		fulltexturestring = txt;
		breaksound = "DangerZone:woodbreak"; 
		placesound = "DangerZone:woodplace"; 
		hitsound =   "DangerZone:woodhit";
		if(mdl == null)mdl = new ModelLadder();
	}
	
	public String getStepSound(){
		int i = DangerZone.rand.nextInt(4);
		if(i == 0)return "DangerZone:wood1";
		if(i == 1)return "DangerZone:wood2";
		if(i == 2)return "DangerZone:wood3";
		return "DangerZone:wood4";
	}
	
	public void notifyNeighborChanged(World w, int d, int x, int y, int z){
		int bid = 0;
		int meta = w.getblockmeta(d, x, y, z); //get my metadata!
		int meta2 = 0;
		
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
		default:
			break;
		}
		
		if(!Blocks.isSolid(bid)){
			//Don't know what I'm on, but it's not good to stay!			
			if(!Blocks.isSolid(w.getblock(d, x, y+1, z)) && !Blocks.isSolid(w.getblock(d, x, y-1, z))){
				//can also attach to other ladder blocks, which are not solid!
				bid = w.getblock(d, x, y+1, z);
				meta2 = w.getblockmeta(d, x, y+1, z);
				if(bid != blockID && meta != meta2){ //not ladder in same orientation...
					bid = w.getblock(d, x, y-1, z);
					meta2 = w.getblockmeta(d, x, y-1, z);
					if(bid != blockID && meta != meta2){ //not ladder in same orientation...
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
			}
		}
	}
	

	public boolean doPlaceBlock(int focusbid, Player p, World w, int dimension, int x, int y, int z, int side){
		//System.out.printf("side = %d\n", side);	
		
		if(!Blocks.isSolid(focusbid))return false;
		if(Blocks.isLeaves(focusbid))return false;
		if(!BreakChecks.canChangeBlock(p, dimension, x, y, z, this.blockID, 0))return false; //close enough
		
		switch(side){
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
		default:
			return false;
		}
		
		w.playSound(Blocks.getPlaceSound(this.blockID), dimension, x, y, z, 0.5f, 1.0f);

		return true;
	}
	
	public Texture getTexture(int side){
		if(texture == null || fulltexture == null){
			texture = initBlockTexture(texturepath);
			fulltexture = initBlockTexture(fulltexturestring);
		}
		if(side == 5)return fulltexture;
		return texture;
	}
	
	public String getStitchedTextureName(int side){
		return fulltexturestring;
	}
	
	public void renderMeToVBO(long chunkvbos[], WorldRenderer wr, World w, int d, int x, int y, int z, int bid, int meta, int sides, boolean focus, int xo, int yo, int zo){
		
		VBOBuffer v = null;
		StitchedTexture st = null;

		st = VBODataBuilderThread.findVBOtextureforblockside(5, bid);
		if(st == null)return;
		v = VBODataBuilderThread.findOrMakeVBOForTexture(chunkvbos, st, isTranslucent);
		if(v == null)return;
		
		switch((meta>>8)){
		case 1:
			mdl.side1.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 90);
			mdl.side2.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 90);
			mdl.side3.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 90);
			mdl.side4.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 90);			
			mdl.rung1a.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 90);
			mdl.rung2a.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 90);
			mdl.rung3a.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 90);
			mdl.rung4a.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 90);			
			mdl.rung1b.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 90);
			mdl.rung2b.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 90);
			mdl.rung3b.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 90);
			mdl.rung4b.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 90);
			break;
		case 3:
			mdl.side1.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 180);
			mdl.side2.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 180);
			mdl.side3.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 180);
			mdl.side4.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 180);			
			mdl.rung1a.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 180);
			mdl.rung2a.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 180);
			mdl.rung3a.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 180);
			mdl.rung4a.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 180);			
			mdl.rung1b.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 180);
			mdl.rung2b.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 180);
			mdl.rung3b.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 180);
			mdl.rung4b.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 180);
			break;
		case 2:
			mdl.side1.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, -90);
			mdl.side2.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, -90);
			mdl.side3.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, -90);
			mdl.side4.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, -90);			
			mdl.rung1a.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, -90);
			mdl.rung2a.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, -90);
			mdl.rung3a.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, -90);
			mdl.rung4a.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, -90);			
			mdl.rung1b.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, -90);
			mdl.rung2b.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, -90);
			mdl.rung3b.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, -90);
			mdl.rung4b.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, -90);
			break;
		case 4:
			mdl.side1.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mdl.side2.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mdl.side3.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mdl.side4.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);			
			mdl.rung1a.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mdl.rung2a.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mdl.rung3a.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mdl.rung4a.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);			
			mdl.rung1b.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mdl.rung2b.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mdl.rung3b.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mdl.rung4b.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			break;
		default:
			break;	
		}		
		
	}
	
	public void renderMeHeld(WorldRenderer wr, Entity e, int bid, boolean isdisplay){
		if(e == null)return;
		GL11.glPushMatrix();
		GL11.glTranslatef(14,  4,  0);
		GL11.glScalef(2, 2, 2);
		GL11.glRotatef(45, 0, 1, 0);
		renderMe(wr, e.world, e.dimension, (int)e.posx, (int)e.posy, (int)e.posz, bid, 0x100, 0, false);
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		GL11.glPopMatrix();
	}
	
	
	public void renderMe(WorldRenderer wr, World w, int d, int x, int y, int z, int bid, int meta, int sides, boolean focus){

		wr.loadtextureforblockside(5, bid, false); //We only have one side. Tell world renderer to load our texture.
		
		switch((meta>>8)){
		case 0: //EntityBlockItem!
		case 1:
			GL11.glPushMatrix();
			GL11.glTranslatef(0,  -8,  0);
			GL11.glRotatef(-90f, 0, 1, 0);
			mdl.side1.render(1);
			mdl.side2.render(1);
			mdl.side3.render(1);
			mdl.side4.render(1);		
			mdl.rung1a.render(1);
			mdl.rung2a.render(1);
			mdl.rung3a.render(1);
			mdl.rung4a.render(1);		
			mdl.rung1b.render(1);
			mdl.rung2b.render(1);
			mdl.rung3b.render(1);
			mdl.rung4b.render(1);
			GL11.glPopMatrix();
			break;
		case 3:
			GL11.glPushMatrix();
			GL11.glTranslatef(0,  -8,  0);
			GL11.glRotatef(180f, 0, 1, 0);
			mdl.side1.render(1);
			mdl.side2.render(1);
			mdl.side3.render(1);
			mdl.side4.render(1);		
			mdl.rung1a.render(1);
			mdl.rung2a.render(1);
			mdl.rung3a.render(1);
			mdl.rung4a.render(1);		
			mdl.rung1b.render(1);
			mdl.rung2b.render(1);
			mdl.rung3b.render(1);
			mdl.rung4b.render(1);
			GL11.glPopMatrix();
			break;
		case 2:
			GL11.glPushMatrix();
			GL11.glTranslatef(0,  -8,  0);
			GL11.glRotatef(90f, 0, 1, 0);
			mdl.side1.render(1);
			mdl.side2.render(1);
			mdl.side3.render(1);
			mdl.side4.render(1);		
			mdl.rung1a.render(1);
			mdl.rung2a.render(1);
			mdl.rung3a.render(1);
			mdl.rung4a.render(1);		
			mdl.rung1b.render(1);
			mdl.rung2b.render(1);
			mdl.rung3b.render(1);
			mdl.rung4b.render(1);
			GL11.glPopMatrix();
			break;
		case 4:
			GL11.glPushMatrix();
			GL11.glTranslatef(0,  -8,  0);
			//GL11.glRotatef(90f, 0, 1, 0);
			mdl.side1.render(1);
			mdl.side2.render(1);
			mdl.side3.render(1);
			mdl.side4.render(1);		
			mdl.rung1a.render(1);
			mdl.rung2a.render(1);
			mdl.rung3a.render(1);
			mdl.rung4a.render(1);		
			mdl.rung1b.render(1);
			mdl.rung2b.render(1);
			mdl.rung3b.render(1);
			mdl.rung4b.render(1);
			GL11.glPopMatrix();
			break;
		default:
			break;	
		}		

	}
	


}
