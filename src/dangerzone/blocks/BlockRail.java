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
import dangerzone.threads.FastBlockTicker;
import dangerzone.threads.VBODataBuilderThread;


public class BlockRail extends Block {
	
	public ModelRail mbf = null;
	public String fulltexturestring = null;
	public Texture fulltexture = null;
		
	public BlockRail(String n, String txt, String fulltxt){
		super(n, txt);
		isSolidForRendering = false;
		isSolid = false;
		hasOwnRenderer = true;
		renderAllSides = true;
		alwaysRender = true;
		alwaystick = true;	
		maxdamage = 10;
		burntime = 30;
		fulltexturestring = fulltxt;
		if(mbf == null)mbf = new ModelRail();
	}
	
	public void notifyNeighborChanged(World w, int d, int x, int y, int z){
		if(w.isServer){
			int bid = w.getblock(d, x, y-1, z);
			if(!Blocks.isSolid(bid)){
				Blocks.doBreakBlock(blockID, w, d, x, y, z);
				w.setblock(d, x, y, z, 0);
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
			
			
			if(meta == 1 && isRailBlock(w, d, x-1, y+1, z))meta |= 0x10;
			if(meta == 2 && isRailBlock(w, d, x+1, y+1, z))meta |= 0x10;
			if(meta == 4 && isRailBlock(w, d, x, y+1, z-1))meta |= 0x10;
			if(meta == 8 && isRailBlock(w, d, x, y+1, z+1))meta |= 0x10;
						
			w.setblockandmeta(d, x, y, z, blockID, meta);		

			//tick again later!
			FastBlockTicker.addFastTick(d,  x,  y, z, 8+w.rand.nextInt(3)); //ticks, in TENTHS of a second
						
		}
	}
	
	public boolean isRailBlock(World w, int d, int x, int y, int z){
		int bid = w.getblock(d, x, y, z);
		if(bid == 0)return false;
		Block b = Blocks.getBlock(bid);
		if(b instanceof BlockRail)return true;
		return false;
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
	
	public void renderMeHeld(WorldRenderer wr, Entity e, int bid, boolean isdisplay){
		if(e == null)return;
		GL11.glPushMatrix();
		GL11.glTranslatef(0,  4,  0);
		GL11.glScalef(2, 2, 2);
		renderMe(wr, e.world, e.dimension, (int)e.posx, (int)e.posy, (int)e.posz, bid, 3, 0, false);
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		GL11.glPopMatrix();
	}
		
	
	public void renderMeToVBO(long chunkvbos[], WorldRenderer wr, World w, int d, int x, int y, int z, int bid, int meta, int sides, boolean focus, int xo, int yo, int zo){

		VBOBuffer v = null;
		StitchedTexture st = null;

		st = VBODataBuilderThread.findVBOtextureforblockside(5, bid);
		if(st == null)return;
		v = VBODataBuilderThread.findOrMakeVBOForTexture(chunkvbos, st, isTranslucent);
		if(v == null)return;
		
		if((meta & 0x10) == 0){
			mbf.railcenter.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mbf.railpost.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);

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
		}else{

			int angle = 180;
			if((meta & 0x02) != 0)angle = 0;
			if((meta & 0x04) != 0)angle = -90;
			if((meta & 0x08) != 0)angle = 90;

			mbf.railsideA.renderRotatedToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0, angle, 0);
			mbf.railsideB.renderRotatedToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0, angle, 0);
			mbf.railsideC.renderRotatedToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0, angle, 0);
			mbf.railsideD.renderRotatedToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0, angle, 0);
			mbf.railcenterA.renderRotatedToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0, angle, 0);
			mbf.railpostA.renderRotatedToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0, angle, 0);
			mbf.railpostB.renderRotatedToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0, angle, 0);
			mbf.railpostC.renderRotatedToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0, angle, 0);		

		}
	}
	
	public void renderMe(WorldRenderer wr, World w, int d, int x, int y, int z, int bid, int meta, int sides, boolean focus){

		wr.loadtextureforblockside(5, bid, false); //We only have one side. Tell world renderer to load our texture.
		
		GL11.glTranslatef(0,  -8,  0);
		
		if((meta & 0x10) == 0){
			mbf.railcenter.render(1);
			mbf.railpost.render(1);

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
		}else{

		}

	}

}
