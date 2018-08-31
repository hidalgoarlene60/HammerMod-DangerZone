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


import dangerzone.StitchedTexture;
import dangerzone.VBOBuffer;
import dangerzone.World;
import dangerzone.WorldRenderer;
import dangerzone.entities.Entity;
import dangerzone.threads.VBODataBuilderThread;


public class BetterTorch extends LightStick {

	private static ModelBetterTorch mbt = null;
	private String fulltexturestring = null;
	private Texture fulltexture = null;
		
	public BetterTorch(String n, String flattxt, String txt){
		super(n, flattxt);
		isSolidForRendering = false;
		isSolid = false;
		hasOwnRenderer = true;
		brightness = 0.75f;
		maxdamage = 1;
		randomtick = true;
		burntime = 30;
		fulltexturestring = txt;
		if(mbt == null)mbt = new ModelBetterTorch();
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
		case 0:
			mbt.t1base.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mbt.t1flame.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			break;
		case 1:
			mbt.t2base1.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 90);
			mbt.t2base2.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 90);
			mbt.t2base3.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 90);
			mbt.t2flame.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 90);	
			break;
		case 2:
			mbt.t2base1.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 270);
			mbt.t2base2.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 270);
			mbt.t2base3.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 270);
			mbt.t2flame.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 270);	
			break;
		case 3:
			mbt.t2base1.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 180);
			mbt.t2base2.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 180);
			mbt.t2base3.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 180);
			mbt.t2flame.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 180);	
			break;
		case 4:
			mbt.t2base1.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mbt.t2base2.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mbt.t2base3.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mbt.t2flame.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);	
			break;
		case 5:
			mbt.t3base.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo,  0);
			mbt.t3base2.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mbt.t3flame.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			break;
		default:
				
		}		
		
	}
	
	public void renderMeHeld(WorldRenderer wr, Entity e, int bid, boolean isdisplay){
		if(e == null)return;
		GL11.glPushMatrix();
		GL11.glTranslatef(0,  4,  0);
		GL11.glScalef(2, 2, 2);
		renderMe(wr, e.world, e.dimension, (int)e.posx, (int)e.posy, (int)e.posz, bid, 0, 0, false);
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		GL11.glPopMatrix();
	}
	
	
	public void renderMe(WorldRenderer wr, World w, int d, int x, int y, int z, int bid, int meta, int sides, boolean focus){

		wr.loadtextureforblockside(5, bid, false); //We only have one side. Tell world renderer to load our texture.
		
		switch((meta>>8)){
		case 0:
			GL11.glTranslatef(0,  -8,  0);
			mbt.t1base.render(1);
			mbt.t1flame.render(1);
			break;
		case 1:
			GL11.glTranslatef(0,  -8,  0);
			GL11.glRotatef(270f, 0, 1, 0);
			mbt.t2base1.render(1);
			mbt.t2base2.render(1);
			mbt.t2base3.render(1);
			mbt.t2flame.render(1);			
			break;
		case 2:
			GL11.glPushMatrix();
			GL11.glTranslatef(0,  -8,  0);	
			GL11.glRotatef(90f, 0, 1, 0);
			mbt.t2base1.render(1);
			mbt.t2base2.render(1);
			mbt.t2base3.render(1);
			mbt.t2flame.render(1);			
			GL11.glPopMatrix();
			break;
		case 3:
			GL11.glPushMatrix();
			GL11.glTranslatef(0,  -8,  0);
			GL11.glRotatef(180f, 0, 1, 0);
			mbt.t2base1.render(1);
			mbt.t2base2.render(1);
			mbt.t2base3.render(1);
			mbt.t2flame.render(1);			
			GL11.glPopMatrix();
			break;
		case 4:
			GL11.glPushMatrix();
			GL11.glTranslatef(0,  -8,  0);
			mbt.t2base1.render(1);
			mbt.t2base2.render(1);
			mbt.t2base3.render(1);
			mbt.t2flame.render(1);			
			GL11.glPopMatrix();
			break;
		case 5:
			GL11.glTranslatef(0,  -8,  0);
			mbt.t3base.render(1);
			mbt.t3base2.render(1);
			mbt.t3flame.render(1);
			break;
		default:
				
		}		

	}
	


}
