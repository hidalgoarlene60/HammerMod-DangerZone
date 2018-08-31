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

import dangerzone.DangerZone;
import dangerzone.Player;
import dangerzone.StitchedTexture;
import dangerzone.World;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityChest;



public class BlockChest extends Block {
	Texture ttop = null;
	Texture tbottom = null;
	Texture tleft = null;
	Texture tright = null;
	Texture tfront = null;
	Texture tback = null;
	String topname;
	String bottomname;
	String leftname;
	String rightname;
	String frontname;
	String backname;
	StitchedTexture sttop = new StitchedTexture();
	StitchedTexture stbottom = new StitchedTexture();
	StitchedTexture stleft = new StitchedTexture();
	StitchedTexture stright = new StitchedTexture();
	StitchedTexture stfront = new StitchedTexture();
	StitchedTexture stback = new StitchedTexture();
	
	
	public BlockChest(String n){
		super(n, "");
		topname = "res/blocks/chest_top.png";
		bottomname = "res/blocks/chest_bottom.png";
		leftname = "res/blocks/chest_left.png";
		rightname = "res/blocks/chest_right.png";
		frontname = "res/blocks/chest_front.png";
		backname = "res/blocks/chest_back.png";
		maxstack = 8;
		isWood = true;
		hasFront = true;
		burntime = 100;
	}
	
	public String getStepSound(){
		int i = DangerZone.rand.nextInt(4);
		if(i == 0)return "DangerZone:wood1";
		if(i == 1)return "DangerZone:wood2";
		if(i == 2)return "DangerZone:wood3";
		return "DangerZone:wood4";
	}
	
	//Player right-clicked on this block
	public boolean rightClickOnBlock(Player p, int dimension, int x, int y, int z){
		
		if(p == null || p.world.isServer)return false;
		
		List<Entity> nearby_list = null;
		EntityChest ec = null;
		//Find our chest entity!
		nearby_list = DangerZone.entityManager.findEntitiesInRange(2, dimension, x, y, z);
		if(nearby_list != null){
			if(!nearby_list.isEmpty()){
				Entity e = null;
				ListIterator<Entity> li;
				li = nearby_list.listIterator();
				while(li.hasNext()){
					e = (Entity)li.next();
					if(e instanceof EntityChest){ 						
						if((int)e.posx == x && (int)e.posy == y && (int)e.posz == z){
							ec = (EntityChest)e;
							break;
						}
						ec = null;
					}
				}								
			}			
		}
		if(ec == null){ //where did our entity go???
			if(!p.world.isServer){
				//System.out.printf("spawning new chest entity\n");
				Entity eb = p.world.createEntityByName("DangerZone:EntityChest", dimension, ((double)x)+0.5f, ((double)y)+0.05f, ((double)z)+0.5f);
				if(eb != null){
					eb.init();
					p.world.spawnEntityInWorld(eb);
				}
			}
			return false; //must click again... 
		}
		
		//random cockroach
		if(p.world.rand.nextInt(20) == 1){
			Entity eb = p.world.createEntityByName("DangerZone:Cockroach", dimension, ((double)x)+0.5f, ((double)y)+1.05f, ((double)z)+0.5f);
			if(eb != null){
				eb.init();
				p.world.spawnEntityInWorld(eb);
			}
		}
		
		//random rat
		if(p.world.rand.nextInt(200) == 1){
			Entity eb = p.world.createEntityByName("DangerZone:Rat", dimension, ((double)x)+0.5f, ((double)y)+1.05f, ((double)z)+0.5f);
			if(eb != null){
				eb.init();
				p.world.spawnEntityInWorld(eb);
			}
		}
		
		DangerZone.chestgui.ec = ec;
		//System.out.printf("Chest entity ID = %d\n", ec.entityID);
		DangerZone.setActiveGui(DangerZone.chestgui);
		DangerZone.world.playSound("DangerZone:chest_open", dimension, x, y, z, 0.75f, 1.0f);
		return false; //return FALSE because we kicked off a GUI! DO NOT PLACE A BLOCK!
	}
	
	public void onBlockPlaced(World w, int dimension, int x, int y, int z){
		if(w.isServer){
			//System.out.printf("onBlockPlaced spawning new chest entity\n");
			Entity eb = w.createEntityByName("DangerZone:EntityChest", dimension, ((double)x)+0.5f, ((double)y)+0.05f, ((double)z)+0.5f);
			if(eb != null){
				eb.init();
				w.spawnEntityInWorld(eb);
			}
		}
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
		if(tright == null){
			tright = initBlockTexture(rightname);
		}
		if(tfront == null){
			tfront = initBlockTexture(frontname);
		}
		if(tback == null){
			tback = initBlockTexture(backname);
		}
		
		if(side == 0)return ttop;
		if(side == 5)return tbottom;
		if(side == 3)return tleft;
		if(side == 4)return tright;
		if(side == 1)return tfront;
		if(side == 2)return tback;
		return null;
	}
	
	public StitchedTexture getStitchedTexture(int side){	
		if(side == 0)return sttop;
		if(side == 5)return stbottom;
		if(side == 3)return stleft;
		if(side == 4)return stright;
		if(side == 1)return stfront;
		return stback;
	}
	
	public String getStitchedTextureName(int side){
		if(side == 0)return topname;
		if(side == 5)return bottomname;
		if(side == 3)return leftname;
		if(side == 4)return rightname;
		if(side == 1)return frontname;
		return backname;
	}
	
}