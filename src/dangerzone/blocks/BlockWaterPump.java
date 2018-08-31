package dangerzone.blocks;

import java.util.List;
import java.util.ListIterator;

import org.newdawn.slick.opengl.Texture;

import dangerzone.BreakChecks;
import dangerzone.DangerZone;
import dangerzone.StitchedTexture;
import dangerzone.World;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityBlockItem;
import dangerzone.entities.EntityExp;

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
public class BlockWaterPump extends Block {
	
	Texture ttop = null;
	Texture tbottom = null;
	Texture tleft = null;
	Texture tfront = null;
	String topname;
	String bottomname;
	String leftname;
	String frontname;
	StitchedTexture sttop = new StitchedTexture();
	StitchedTexture stbottom = new StitchedTexture();
	StitchedTexture stleft = new StitchedTexture();
	StitchedTexture stfront = new StitchedTexture();

	public BlockWaterPump(String n) {
		super(n, "");
		isStone = true;
		maxdamage = 20;
		mindamage = 10; 
		breaksound = "DangerZone:stonebreak"; 
		placesound = "DangerZone:stoneplace"; 
		hitsound =   "DangerZone:stonehit";
		topname = "res/blocks/waterpumptop.png";
		bottomname = "res/blocks/waterpump.png";
		leftname = "res/blocks/waterpumpleftright.png";
		frontname = "res/blocks/waterspoutside.png";
		alwaystick = true;
		hasFront = true;
		showTop = true;
	}
	
	public void tickMe(World w, int d, int x, int y, int z){	
		int bid;
		int meta = w.getblockmeta(d, x, y, z);
		meta &= 0x3000;
		
		bid = w.getblock(d, x, y-1, z);
		if(Blocks.isLiquid(bid)){
			if(fillit(w, d, x, y+1, z, bid)){
				move_things(d, x, y, z, 0, 1, 0);
			}
		}	

		if(meta == 0){
			bid = w.getblock(d, x, y, z+1);
			if(Blocks.isLiquid(bid)){
				if(fillit(w, d, x, y, z-1, bid)){
					move_things(d, x, y, z, 0, 0, -1);
				}
			}
		}
		if(meta == 0x1000){
			bid = w.getblock(d, x+1, y, z);
			if(Blocks.isLiquid(bid)){
				if(fillit(w, d, x-1, y, z, bid)){
					move_things(d, x, y, z, -1, 0, 0);
				}
			}
		}
		if(meta == 0x2000){
			bid = w.getblock(d, x, y, z-1);
			if(Blocks.isLiquid(bid)){
				if(fillit(w, d, x, y, z+1, bid)){
					move_things(d, x, y, z, 0, 0, +1);
				}
			}
		}
		if(meta == 0x3000){
			bid = w.getblock(d, x-1, y, z);
			if(Blocks.isLiquid(bid)){			
				if(fillit(w, d, x+1, y, z, bid)){
					move_things(d, x, y, z, +1, 0, 0);
				}
			}
		}
		

	}
	
	public boolean fillit(World w, int d, int x, int y, int z, int lib){
		int bid = w.getblock(d, x, y, z);
		if(bid == 0 || Blocks.isLiquid(bid) || Blocks.isLeaves(bid)){
			int bidtoset = Blocks.getActiveBlockid(lib);
			if(bidtoset == 0)bidtoset = lib; //this must be the active one
			if(!BreakChecks.canChangeBlock(w, d, x, y, z, d, x, y, z, bidtoset, 0x01))return false;
			w.setblockandmeta(d, x, y, z, bidtoset, 0x01); //as high as it can get without being permanent so it stops when this stops
			return true;
		}
		return false;
	}
	
	public void move_things(int dimension, double posx, double posy, double posz, int xdir, int ydir, int zdir){	
		List<Entity> nearby_list = null;
		//Get a list of entities within reach of largest mob expected because we may hit their hitbox!
		nearby_list = DangerZone.server.entityManager.findALLEntitiesInRange(2.0f, dimension, (double)(posx-xdir)+0.5f, (double)(posy-ydir)+0.5f, (double)(posz-zdir)+0.5f);
		if(nearby_list != null){
			if(!nearby_list.isEmpty()){
				Entity e = null;
				ListIterator<Entity> li;
				double dist;

				li = nearby_list.listIterator();
				while(li.hasNext()){
					e = (Entity)li.next();
					dist = e.getDistanceFromEntityCenter((double)(posx-xdir)+0.5f, (double)(posy-ydir)+0.5f, (double)(posz-zdir)+0.5f);
					dist -= e.getWidth();
					if(dist < 1 && isSuitableTarget(e)){ 
						e.posx = posx+xdir+0.5f;
						e.posy = posy+ydir+0.5f;
						e.posz = posz+zdir+0.5f;
					}
				}								
			}			
		}
	}

	//default cockroaches and players only
	public boolean isSuitableTarget(Entity e){
		if(e instanceof EntityExp)return true;
		if(e instanceof EntityBlockItem)return true;
		return false;
	}

	
	//side 0 = top
	//side 1 = front
	//side 2 = back
	//side 3 = left
	//side 4 = right
	//side 5 = bottom
	public Texture getTexture(int side){

		if(ttop == null){
			ttop = initBlockTexture(topname);
		}
		if(tbottom == null){
			tbottom = initBlockTexture(bottomname);
		}
		if(tleft == null){
			tleft = initBlockTexture(leftname);
		}
		if(tfront == null){
			tfront = initBlockTexture(frontname);
		}
		
		if(side == 0)return ttop;
		if(side == 5)return tbottom;
		if(side == 3)return tleft;
		if(side == 4)return tleft;
		if(side == 1)return tfront;
		if(side == 2)return tbottom;
		return null;
	}
	
	public StitchedTexture getStitchedTexture(int side){	
		if(side == 0)return sttop;
		if(side == 5)return stbottom;
		if(side == 3)return stleft;
		if(side == 4)return stleft;
		if(side == 1)return stfront;
		return stbottom;
	}
	
	public String getStitchedTextureName(int side){
		if(side == 0)return topname;
		if(side == 5)return bottomname;
		if(side == 3)return leftname;
		if(side == 4)return leftname;
		if(side == 1)return frontname;
		return bottomname;
	}

}