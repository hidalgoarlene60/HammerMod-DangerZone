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

import java.util.List;
import java.util.ListIterator;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;


import dangerzone.DangerZone;
import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.StitchedTexture;
import dangerzone.VBOBuffer;
import dangerzone.World;
import dangerzone.WorldRenderer;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityLiving;
import dangerzone.items.Items;
import dangerzone.threads.FastBlockTicker;
import dangerzone.threads.VBODataBuilderThread;


public class BlockBarrier extends Block {
	
	private static ModelBetterFence mbf = null;
	private String fulltexturestring = null;
	private Texture fulltexture = null;
		
	public BlockBarrier(String n, String txt, String fulltxt){
		super(n, txt);
		isSolidForRendering = false;
		isSolid = true;
		hasOwnRenderer = true;
		renderAllSides = true;
		renderSmaller = true;
		alwaysRender = true;
		alwaystick = true;	
		maxdamage = 10;
		burntime = 30;
		fulltexturestring = fulltxt;
		if(mbf == null)mbf = new ModelBetterFence();
	}
	
	public void notifyNeighborChanged(World w, int d, int x, int y, int z){
		if(w.isServer){
			int bid = w.getblock(d, x, y-1, z);
			if(!Blocks.isSolid(bid)){
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
				w.setblock(d, x, y, z, 0);
				return;
			}
			
			int meta = 0;
			if(Blocks.isSolid(w.getblock(d, x+1, y, z)))meta |= 0x01;
			if(Blocks.isSolid(w.getblock(d, x-1, y, z)))meta |= 0x02;
			if(Blocks.isSolid(w.getblock(d, x, y, z+1)))meta |= 0x04;
			if(Blocks.isSolid(w.getblock(d, x, y, z-1)))meta |= 0x08;
			w.setblockandmeta(d, x, y, z, blockID, meta);	
			
			if(bid == Blocks.autofence.blockID)return; //i am a slave block		

			//master block!
			FastBlockTicker.addFastTick(d,  x,  y, z, 2); //ticks, in TENTHS of a second
			
			List<Entity> nearby_list = null;
			ListIterator<Entity> li;
			Entity e = null;
			int maxh = 0;
									
			nearby_list = DangerZone.server.entityManager.findEntitiesInRange(16.0f, d, (double)x+0.5f, (double)y+0.5f, (double)z+0.5f);
			if(nearby_list != null){
				if(!nearby_list.isEmpty()){
					li = nearby_list.listIterator();
					while(li.hasNext()){
						e = (Entity)li.next();
						if(e instanceof EntityLiving){
							//System.out.printf("Entity is: %s with height %f\n", e.uniquename, e.getHeight());
							if(maxh < 2)maxh = 2;
							if((e.getHeight()*2+0.9f)>maxh){
								maxh = (int)(e.getHeight()*2+0.9f);
							}
							if(e instanceof Player){
								float dist = (float)Math.sqrt((e.posx-((double)x+0.5f))*(e.posx-((double)x+0.5f)) + (e.posz-((double)z+0.5f))*(e.posz-((double)z+0.5f)));
								if(dist < 1.5){
									Player p = (Player)e;
									InventoryContainer ic = p.getHotbar(p.gethotbarindex());
									if(ic != null && ic.iid == Items.autofencekey.itemID){
										maxh = 0; //open!
										break;
									}
								}								
							}
						}
					}
				}
			}
			if(maxh > 1){
				for(int i=1;i<maxh;i++){
					if(w.getblock(d, x, y+i, z) == 0){
						w.setblock(d, x, y+i, z, this.blockID);
					}
				}
				if(w.rand.nextInt(2000) == 1)w.playSound("DangerZone:arc", d, x, y, z, 0.1f, 1);
			}else{
				if(w.getblock(d, x, y+1, z) == this.blockID){
					w.setblock(d, x, y+1, z, 0);
				}
			}			
		}
	}
	
	public float getBrightness(World w, int d, int x, int y, int z){
		if(w.getblock(d, x, y+1, z) == blockID)return 0.45f;
		return 0;
	}
	
	public int getBlockDrop(Player p, World w, int d, int x, int y, int z){
		int bid = w.getblock(d, x, y-1, z);
		if(bid == this.blockID)return 0; //slave block	
		return this.blockID;
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
		renderMe(wr, e.world, e.dimension, (int)e.posx, (int)e.posy, (int)e.posz, bid, 0, 0, false);
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
		
		mbf.post.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
		mbf.postA.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
		
		if((meta & 0x01) == 0x01){
			mbf.side1.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mbf.side2.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mbf.side3.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mbf.side1A.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
		}
		if((meta & 0x02) == 0x02){
			mbf.side1.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 180);
			mbf.side2.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 180);
			mbf.side3.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 180);
			mbf.side1A.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 180);
		}
		if((meta & 0x04) == 0x04){
			mbf.side1.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 90);
			mbf.side2.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 90);
			mbf.side3.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 90);
			mbf.side1A.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 90);
		}
		if((meta & 0x08) == 0x08){
			mbf.side1.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, -90);
			mbf.side2.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, -90);
			mbf.side3.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, -90);
			mbf.side1A.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, -90);
		}

	}
	
	public void renderMe(WorldRenderer wr, World w, int d, int x, int y, int z, int bid, int meta, int sides, boolean focus){

		wr.loadtextureforblockside(5, bid, false); //We only have one side. Tell world renderer to load our texture.
		
		GL11.glTranslatef(0,  -8,  0);
		mbf.post.render(1);
		mbf.postA.render(1);
		
		if((meta & 0x01) == 0x01){
			mbf.side1.render(1);
			mbf.side2.render(1);
			mbf.side3.render(1);
			mbf.side1A.render(1);
		}
		if((meta & 0x02) == 0x02){
			GL11.glRotatef(180, 0, 1, 0);
			mbf.side1.render(1);
			mbf.side2.render(1);
			mbf.side3.render(1);
			mbf.side1A.render(1);
			GL11.glRotatef(-180, 0, 1, 0);
		}
		if((meta & 0x04) == 0x04){
			GL11.glRotatef(-90, 0, 1, 0);
			mbf.side1.render(1);
			mbf.side2.render(1);
			mbf.side3.render(1);
			mbf.side1A.render(1);
			GL11.glRotatef(90, 0, 1, 0);
		}
		if((meta & 0x08) == 0x08){
			GL11.glRotatef(90, 0, 1, 0);
			mbf.side1.render(1);
			mbf.side2.render(1);
			mbf.side3.render(1);
			mbf.side1A.render(1);
			GL11.glRotatef(-90, 0, 1, 0);
		}

	}

}
