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

import org.newdawn.slick.opengl.Texture;

import dangerzone.BreakChecks;
import dangerzone.DangerZone;
import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.ServerHooker;
import dangerzone.StitchedTexture;
import dangerzone.TextureMapper;
import dangerzone.ToDoList;
import dangerzone.World;
import dangerzone.WorldRenderer;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityBlockItem;
import dangerzone.entities.EntityLiving;
import dangerzone.gui.InventoryMenus;



/*
 * Metadata bits 0xfc00 are reserved for ROTATION. See Worldrender.drawTexturedCube() for usage.
 * Metadata bits 0x0300 are reserved but as yet unused.
 * Metadata bits 0x00ff are yours to use. (except for liquids, which use all of them!)
 */

public class Block {
	public int blockID;
	public String texturepath;
	public String uniquename;
	public int maxstack = 64;
	public Texture texture = null;
	public StitchedTexture stitchedtexture = new StitchedTexture();
	public boolean randomtick = false;  		//If you want random ticks, set this true
	public boolean alwaystick = false;  		//If you want to always be ticked, set this true
	public boolean hasOwnRenderer = false; 		//If you have your own renderer, set me true!
	public boolean isSolidForRendering = true; 	//If you can see through parts your block, set false! (clear, not translucent)
	public boolean renderAllSides = false;
	public boolean alwaysRender = false; 		//ladders and torches!
	public boolean renderSmaller = false; 		//just 0.01 of a pixel smaller, enough to overlay a side onto another!
	public boolean isTranslucent = false;       //If part or all of your block is translucent, set this true!
	public boolean isSolid = true; 				//If you can walk through it, set false!
	public boolean isLiquid = false; 			//If you can swim in it.
	public boolean isSquishy = false;			//Sort of both solid and liquid
	public String breaksound = null;
	public String placesound = null;
	public String hitsound = null;
	public boolean isWood = false;
	public boolean canLeavesGrow = false;
	public boolean isLeaves = false;
	public boolean isStone = false;
	public boolean isDirt = false;
	public boolean isWaterPlant = false;
	public boolean showInInventory = true;
	public int menu = InventoryMenus.GENERIC;
	public int maxdamage = 20; //total damage until breaks
	public float friction = 0.02f; //SMALL VALUES! += slower, -= faster
	public float brightness = 0.0f; //range -1.0 to about 1.0 ! less than 0 = darker, greater than 0 = brighter
	public String particlename = null;
	public int mindamage = 0; //item must do at least this amount to have an effect.
	public boolean hasFront = false;
	public int burntime = 0;
	public boolean showTop = false;
	public Block active_partner = null;
	public Block static_partner = null;

	
	public Block(String n, String txt){
		blockID = 0;
		texturepath = txt;
		uniquename = n;
		breaksound = "DangerZone:blockbreak"; //Default sound file names. Just replace them with your own names.
		placesound = "DangerZone:blockplace"; //Or null if you don't want block sounds.
		hitsound =   "DangerZone:blockhit";
		maxstack = 64;
		maxdamage = 20;
		
	}
	
	public String getBreakSound(){
		return breaksound;
	}
	
	public String getPlaceSound(){
		return placesound;
	}
	
	public String getHitSound(){
		return hitsound;
	}
	
	public String getStepSound(){
		int i = DangerZone.rand.nextInt(3);
		if(i == 0)return "DangerZone:stone1";
		if(i == 1)return "DangerZone:stone2";
		return "DangerZone:stone3";
	}
	
	public String getParticleName(){
		return particlename;
	}
	
	public void tickMe(World w, int d, int x, int y, int z){
		//Override me if you want ticks!
		//See grass for an example!
	}
	
	public void inUseTick(Entity e, InventoryContainer ic, int invindex){
		//Ticks the currently Held Block!
	}
	
	public void onCrafted(Player p, InventoryContainer ic){

	}
	
	public void inventoryTick(Entity e, InventoryContainer ic, int invindex){
		//Ticks the block anywhere in inventory
	}
	
	public void tickMeFast(World w, int d, int x, int y, int z){
		//For FAST ticks (100ms)
		//Must re-add to list every tick. FastBlockTicker.addFastTick(int d, int x, int y, int z)
	}
	
	//One of your neighbors has changed. (YOU are at d, x, y, z)
	public void notifyNeighborChanged(World w, int d, int x, int y, int z){
		
	}
	
	public float getBrightness(World w, int d, int x, int y, int z){
		return brightness;
	}
	
	/* YOU NEED TO IMPLEMENT BOTH RENDERME ROUTINES
	 * This one is called when you have focus, and you are rendered via a call-list.
	 *
	 * WHY? Because of the break-block animation is why... you can't do that from within a vbo.
	 * Well, maybe, but it's a pain in the ass.
	 */
	public void renderMe(WorldRenderer wr, World w, int d, int x, int y, int z, int bid, int meta, int side, boolean focused){
		//Override me if you have your own renderer!
		//See grass for an example!
	}
	
	/* YOU NEED TO IMPLEMENT BOTH RENDERME ROUTINES
	 * This one is called when you are rendered via a VBO.
	 */
	public void renderMeToVBO(long chunkvbos[], WorldRenderer wr, World w, int d, int x, int y, int z, int bid, int meta, int side, boolean focused, int xo, int yo, int zo){
		//Override me if you have your own renderer!
		//See grass for an example!
	}
	
	public void renderMeHeld(WorldRenderer wr, Entity e, int bid, boolean isdisplay){
		if(e == null)return;
		renderMe(wr, e.world, e.dimension, (int)e.posx, (int)e.posy, (int)e.posz, bid, 0, 0, false);
	}
	
	//side 0 = top
	//side 1 = front
	//side 2 = back
	//side 3 = left
	//side 4 = right
	//side 5 = bottom
	//standard block is all the same!
	public Texture getTexture(int side){
		if(texture == null){
			texture = initBlockTexture(texturepath);
		}
		return texture;
	}
	
	public StitchedTexture getStitchedTexture(int side){
		return stitchedtexture;
	}
	
	public String getStitchedTextureName(int side){
		return texturepath;
	}
		
	//Player right-clicked on this block
	//client-side only
	public boolean rightClickOnBlock(Player p, int dimension, int x, int y, int z){
		if(p != null && p.world.isServer){
			ServerHooker.rightClickOnBlock(p, dimension, x, y, z);
		}
		return true; //return TRUE if normal block processing should continue (block placement)
	}
	
	public boolean getIsSolid(World w, int d, int x, int y, int z){
		return isSolid;
	}
	
	public boolean isSolidThisSide(World w, int d, int x, int y, int z, int side){
		return isSolid;
	}
	
	//entity bumped this block
	public void bumpedBlock(Entity e, World w, int d, int x, int y, int z){
		
	}
	
	public void entityInLiquid(Entity e){
		
	}
	
	//Player left-clicked on this block
	public boolean leftClickOnBlock(Player p, int dimension, int x, int y, int z){
		if(p != null && p.world.isServer){
			ServerHooker.leftClickOnBlock(p, dimension, x, y, z);
		}
		return true; //return TRUE if normal block processing should continue (block damage)
	}
	
	//server-side only
	public void onBlockBroken(Player p, int dimension, int x, int y, int z){
		//Player can be NULL! Make sure you check!
		//Do any extra dropping beyond 1 item or 1 block here.
		ServerHooker.onBlockBroken(p, dimension, x, y, z);
		return; //your block is about to be set to 0.
	}
	
	public void onBlockPlaced(World w, int dimension, int x, int y, int z){		
	}
	
	//
	public int getBlockDrop(Player p, World w, int dimension, int x, int y, int z){
		//Player can be NULL! Make sure you check!
		return this.blockID; //standard default return
	}
	
	//
	public int getItemDrop(Player p, World w, int dimension, int x, int y, int z){
		//Player can be NULL! Make sure you check!
		return 0; //override this if you want to drop an item instead of a block.
	}
	
	public int getDropCount(Player p, World w, int dimension, int x, int y, int z){
		//Player can be NULL! Make sure you check!
		return 1; //standard default return
	}
	
	//server
	public void doSteppedOn(Entity e, World w, int d, int x, int y, int z){		
	}
	
	//server
	public void doIsIn(Entity e, World w, int d, int x, int y, int z){		
	}
	
	//server
	public void doEntered(Entity e, World w, int d, int x, int y, int z){		
	}
	
	//Helper for blocks.
	public Texture initBlockTexture(String tp) {		
		return TextureMapper.getTexture(tp);
	}
	
	//player (usually) clicked with a block in his hotbar
	public boolean onLeftClick(Entity holder, Entity clickedon, InventoryContainer ic){
		return true; //continue with normal left click logic, else it is handled special here
	}
	
	public boolean onRightClick(Entity holder, Entity clickedon, InventoryContainer ic){
		return false; 
	}
	
	public void onFoodEaten(Entity e){
		
	}
	
	
	//server-side only
	public boolean doPlaceBlock(int focusbid, Player p, World w, int dimension, int x, int y, int z, int side){
		int newx, newy, newz;
		int meta = 0;
		int newbid = 0;
		newx = x;
		newy = y;
		newz = z;
		
		if(hasFront && p != null){ //Try to make block front face player
			double dx, dz;
			dx = (double) ((x+0.5f) - p.posx);
			dz = (double) ((z+0.5f) - p.posz);
			if(Math.abs(dx)>Math.abs(dz)){
				if(dx > 0){
					meta = BlockRotation.Y_ROT_270;
				}else{
					meta = BlockRotation.Y_ROT_90;
				}			
			}else{
				if(dz > 0){
					meta = BlockRotation.Y_ROT_180;
				}else{
					meta = BlockRotation.Y_ROT_0;
				}				
			}
			//System.out.printf("hasFront meta = 0x%x\n", meta);
		}

		//replacing a "soft" block
		if(!Blocks.isSquishy(focusbid) && (Blocks.isLiquid(focusbid)||(Blocks.isLeaves(focusbid)&&!Blocks.isLeaves(this.blockID))||!Blocks.isSolid(focusbid, w, dimension, x, y, z)||!Blocks.isSolidThisSide(focusbid, w, dimension, x, y, z, side))){
			if(entityInBlock(w, dimension, x, y, z))return false;
			if(BreakChecks.canChangeBlock(p, dimension, x, y, z, this.blockID, meta)){
				int bbid = Blocks.getBlockDrop(focusbid, p, w, dimension, x, y, z);
				int biid = Blocks.getItemDrop(focusbid, p, w, dimension, x, y, z);
				w.setblockandmeta(dimension, x, y, z, this.blockID, meta); //just replace!
				onBlockPlaced(w, dimension, x,y,z);
				if(w.isServer && p != null){
					p.blocks_placed++;
					p.blocks_broken++;
					if(p.server_thread != null)p.server_thread.sendStatsToPlayer();
					ToDoList.onPlaced(p, dimension, x, y, z, this.blockID);
				}
				if(bbid > 0){
					EntityBlockItem eb = (EntityBlockItem)w.createEntityByName(DangerZone.blockitemname, dimension, (double)x+0.5f, (double)y+0.5f, (double)z+0.5f);
					if(eb != null){
						eb.fill(bbid, 0, 1); //make a block
						w.spawnEntityInWorld(eb);
					}
				}
				if(biid > 0){
					EntityBlockItem eb = (EntityBlockItem)w.createEntityByName(DangerZone.blockitemname, dimension, (double)x+0.5f, (double)y+0.5f, (double)z+0.5f);
					if(eb != null){
						eb.fill(0, biid, 1); //make an item!
						w.spawnEntityInWorld(eb);
					}
				}
			}else{
				return false;
			}
		}else{
			//attaching to a solid block
			switch(side){
			case 0:
				newy++;			
				break;
			case 1:
				newz++;
				break;
			case 2:
				newz--;
				break;
			case 3:
				newx--;
				break;
			case 4:
				newx++;
				break;
			case 5:
				newy--;
				break;
			}
			if(entityInBlock(w, dimension, newx, newy, newz))return false;	
			if(!BreakChecks.canChangeBlock(p, dimension, newx, newy, newz, this.blockID, meta))return false;
			
			//have to double-check where we are placing the block, because you can just squeak the pointer into the one behind and replace a block you shouldn't!
			newbid = w.getblock(dimension, newx, newy, newz); 
			if(newbid != 0){
				if(!Blocks.isSquishy(newbid) && (Blocks.isLiquid(newbid)||(Blocks.isLeaves(newbid)&&!Blocks.isLeaves(this.blockID))||!Blocks.isSolid(newbid, w, dimension, newx, newy, newz))){			
					int bbid = Blocks.getBlockDrop(newbid, p, w, dimension, newx, newy, newz);
					int biid = Blocks.getItemDrop(newbid, p, w, dimension, newx, newy, newz);
					w.setblockandmeta(dimension, newx, newy, newz, this.blockID, meta); //just replace!
					onBlockPlaced(w, dimension, newx, newy, newz);
					if(p != null)p.blocks_broken++;
					if(bbid > 0){
						EntityBlockItem eb = (EntityBlockItem)w.createEntityByName(DangerZone.blockitemname, dimension, (double)newx+0.5f, (double)newy+0.5f, (double)newz+0.5f);
						if(eb != null){
							eb.fill(bbid, 0, 1); //make a block
							w.spawnEntityInWorld(eb);
						}
					}
					if(biid > 0){
						EntityBlockItem eb = (EntityBlockItem)w.createEntityByName(DangerZone.blockitemname, dimension, (double)newx+0.5f, (double)newy+0.5f, (double)newz+0.5f);
						if(eb != null){
							eb.fill(0, biid, 1); //make an item
							w.spawnEntityInWorld(eb);
						}
					}
				}else{
					return false;
				}
			}else{			
				w.setblockandmeta(dimension, newx, newy, newz, this.blockID, meta);
				onBlockPlaced(w, dimension, newx, newy, newz);
			}
			if(w.isServer && p != null){
				p.blocks_placed++;
				if(p.server_thread != null)p.server_thread.sendStatsToPlayer();
				ToDoList.onPlaced(p, dimension, newx, newy, newz, this.blockID);
			}
		}
		w.playSound(Blocks.getPlaceSound(this.blockID), dimension, newx, newy, newz, 0.25f, 1.0f);

		return true;
	}
	
	private boolean entityInBlock(World w, int d, int x, int y, int z){
		List<Entity> nearby_list = null;
		ListIterator<Entity> li;
		
		//Get a list of entities within reach of largest mob expected because we may hit their hitbox!
		if(w.isServer){
			nearby_list = DangerZone.server.entityManager.findEntitiesInRange(16.0f, d, x, y, z);
		}else{
			nearby_list = DangerZone.entityManager.findEntitiesInRange(16.0f, d, x, y, z);
		}
		if(nearby_list != null){
			li = nearby_list.listIterator();
			Entity e;
			while(li.hasNext()){
				e = (Entity)li.next();
				if(!(e.canthitme) && ! e.ignoreCollisions){
					if(x == (int)e.posx && y == (int)e.posy && z == (int)e.posz){
						return true;
					}
					if(e instanceof EntityLiving){
						EntityLiving el = (EntityLiving)e;
						int intheight = (int)(el.getHeight()+0.995f);
						double dx, dz;
						for(int k=0;k<intheight;k++){
							if((int)el.posy+k == y){
								dx = el.posx - ((double)x + 0.5f);
								dz = el.posz - ((double)z + 0.5f);	
								if(Math.sqrt((dx*dx)+(dz*dz)) < (0.5f + (el.getWidth()/2.0f))){
									return true;
								}	
							}
						}
					}
				}
			}
		}		
		
		return false;		
	}
	
	
}
