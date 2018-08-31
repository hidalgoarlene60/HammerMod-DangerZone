package dangerzone.blocks;


import java.util.List;
import java.util.ListIterator;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;


import dangerzone.BreakChecks;
import dangerzone.DangerZone;
import dangerzone.Player;
import dangerzone.StitchedTexture;
import dangerzone.VBOBuffer;
import dangerzone.World;
import dangerzone.WorldRenderer;
import dangerzone.blocks.Block;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityBlockItem;
import dangerzone.entities.EntityStand;
import dangerzone.threads.VBODataBuilderThread;


public class BlockStand extends Block {

	public static ModelStand mdl = null;
	private String fulltexturestring = null;
	private Texture fulltexture = null;
		
	public BlockStand(String n, String flattxt, String txt){
		super(n, flattxt);
		isSolidForRendering = false;
		isSolid = false;
		hasOwnRenderer = true;
		alwaysRender = true;
		maxdamage = 5;
		burntime = 90;
		friction = 0.65f;
		fulltexturestring = txt;
		breaksound = "DangerZone:woodbreak"; 
		placesound = "DangerZone:woodplace"; 
		hitsound =   "DangerZone:woodhit";
		if(mdl == null)mdl = new ModelStand();
	}
	
	
	//Player right-clicked on this block
	public boolean rightClickOnBlock(Player p, int dimension, int x, int y, int z){
		if(p == null || p.world.isServer)return false;
		
		List<Entity> nearby_list = null;
		EntityStand ec = null;
		//Find our Furnace entity!
		nearby_list = DangerZone.entityManager.findEntitiesInRange(2, dimension, x, y, z);
		if(nearby_list != null){
			if(!nearby_list.isEmpty()){
				Entity e = null;
				ListIterator<Entity> li;
				li = nearby_list.listIterator();
				while(li.hasNext()){
					e = (Entity)li.next();
					if(e instanceof EntityStand){ 						
						if((int)e.posx == x && (int)e.posy == y && (int)e.posz == z){
							ec = (EntityStand)e;
							break;
						}
					}
				}								
			}			
		}
		if(ec == null){ //where did our entity go???
			//System.out.printf("spawning new Furnace entity\n");
			Entity eb = p.world.createEntityByName("DangerZone:EntityStand", dimension, ((double)x)+0.5f, ((double)y)+0.05f, ((double)z)+0.5f);
			if(eb != null){
				eb.init();
				p.world.spawnEntityInWorld(eb);
			}
		}
		return false; //return FALSE! DO NOT PLACE A BLOCK!
	}
	
	public void onBlockPlaced(World w, int dimension, int x, int y, int z){
		if(w.isServer){
			//System.out.printf("onBlockPlaced spawning new Furnace entity\n");
			Entity eb = w.createEntityByName("DangerZone:EntityStand", dimension, ((double)x)+0.5f, ((double)y)+0.05f, ((double)z)+0.5f);
			if(eb != null){
				eb.init();
				w.spawnEntityInWorld(eb);
			}
		}
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
		
		if(!w.isServer)return;
		if(w.getblock(d, x, y, z) != blockID)return;
		
		//see if i am still attached!
		bid = w.getblock(d, x, y-1, z);
		
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
	
	
	//client-side only
	public boolean doPlaceBlock(int focusbid, Player p, World w, int dimension, int x, int y, int z, int side){
		//System.out.printf("side = %d\n", side);	
		
		if(!Blocks.isSolid(focusbid))return false;
		if(Blocks.isLeaves(focusbid))return false;
		if(!Blocks.isSolidThisSide(focusbid, w, dimension, x, y, z, side))return false;
		if(!BreakChecks.canChangeBlock(p, dimension, x, y, z, this.blockID, 0))return false; //close enough
		if(side != 0)return false;
		
		w.setblockandmeta(dimension, x, y+1, z, this.blockID, side<<8);
		onBlockPlaced(w, dimension, x, y+1, z);
		
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
		
		//System.out.printf("renderMeToVBO %d\n", meta>>8);
		

			mdl.stem.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mdl.xbase1.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mdl.xbase2.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			for(int i=0;i<2;i++)for(int j=0;j<2;j++)mdl.top.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo+i*4, yo, zo+j*4, 0);
			mdl.zbase1.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mdl.zbase2.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mdl.xbase1a.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mdl.xbase2a.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mdl.zbase1a.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mdl.zbase2a.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mdl.frame1.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mdl.frame2.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mdl.frame4.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mdl.frame5.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mdl.frame7.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mdl.frame8.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mdl.frame10.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
			mdl.frame11.renderToVBO(v, st, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb, xo, yo, zo, 0);
		
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
		

			GL11.glPushMatrix();
			GL11.glTranslatef(0,  -8,  0);
			//GL11.glRotatef(90f, 1, 0, 0);
			mdl.stem.render(1);
			mdl.xbase1.render(1);
			mdl.xbase2.render(1);
			mdl.zbase1.render(1);
			mdl.zbase2.render(1);
			mdl.xbase1a.render(1);
			mdl.xbase2a.render(1);
			mdl.zbase1a.render(1);
			mdl.zbase2a.render(1);
			mdl.frame1.render(1);
			mdl.frame2.render(1);
			mdl.frame4.render(1);
			mdl.frame5.render(1);
			mdl.frame7.render(1);
			mdl.frame8.render(1);
			mdl.frame10.render(1);
			mdl.frame11.render(1);
			for(int i=0;i<2;i++){				
				for(int j=0;j<2;j++){					
					mdl.top.render(1);
					GL11.glTranslatef(0,  0,  -4);
				}
				GL11.glTranslatef(0,  0,  8);
				GL11.glTranslatef(4,  0,  0);
			}
			GL11.glTranslatef(-8,  0,  0);

			GL11.glPopMatrix();

	}
	
}

