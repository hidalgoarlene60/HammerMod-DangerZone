package dangerzone.blocks;
import java.util.List;
import java.util.ListIterator;

import dangerzone.DangerZone;
import dangerzone.World;
import dangerzone.entities.Entity;
import dangerzone.entities.EntitySpawner;
import dangerzone.gui.InventoryMenus;

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
public class BlockSpawner extends Block {
	
	String critter;
	float scale = 1.0f;
	int spawntime = 100;
	int spawncount = 1;
	float minlight = 0f;
	float maxlight = 1f;
		
	public BlockSpawner(String n, String tospawn, float sz, int sptime, int scount, float mnlight, float mxlight){
		super(n, "res/blocks/spawner.png");
		critter = tospawn;
		scale = sz;
		isSolidForRendering = false;
		isStone = true;
		renderAllSides = true;
		renderSmaller = true;
		breaksound = "DangerZone:stonebreak"; 
		placesound = "DangerZone:stoneplace"; 
		hitsound =   "DangerZone:stonehit";
		alwaystick = true;
		maxdamage = 300;
		mindamage = 20;
		maxstack = 8;
		spawntime = sptime;
		spawncount = scount;
		minlight = mnlight;
		maxlight = mxlight;
		menu = InventoryMenus.SPAWNER;
	}

	
	public void tickMe(World w, int d, int x, int y, int z){
		if(!w.isServer)return;
		List<Entity> nearby_list = null;
		EntitySpawner ec = null;
		//Find our chest entity!
		nearby_list = DangerZone.server.entityManager.findALLEntitiesInRange(2, d, x, y, z);
		if(nearby_list != null){
			if(!nearby_list.isEmpty()){
				Entity e = null;
				ListIterator<Entity> li;
				li = nearby_list.listIterator();
				while(li.hasNext()){
					e = (Entity)li.next();
					if(e instanceof EntitySpawner){ 						
						if((int)e.posx == x && (int)e.posy == y && (int)e.posz == z){
							ec = (EntitySpawner)e;
							break;
						}
					}
				}								
			}			
		}
		if(ec == null){ //where did our entity go???
			//make one!
			Entity eb = w.createEntityByName("DangerZone:EntitySpawner", d, ((double)x)+0.5f, ((double)y)+0.05f, ((double)z)+0.5f);
			if(eb != null){
				eb.init();
				eb.setVarString(0, critter);
				eb.setBID(this.blockID);
				eb.setVarFloat(0, scale);
				eb.setVarFloat(1, minlight);
				eb.setVarFloat(2, maxlight);
				eb.setVarInt(2, spawntime);
				eb.setVarInt(3, spawncount);
				w.spawnEntityInWorld(eb);
			}
		}
		
	}
	
	public void onBlockPlaced(World w, int dimension, int x, int y, int z){
		if(w.isServer){
			//System.out.printf("onBlockPlaced spawning new spawner entity\n");
			Entity eb = w.createEntityByName("DangerZone:EntitySpawner", dimension, ((double)x)+0.5f, ((double)y)+0.05f, ((double)z)+0.5f);
			if(eb != null){
				eb.init();
				eb.setVarString(0, critter);
				eb.setBID(this.blockID);
				eb.setVarFloat(0, scale);
				eb.setVarFloat(1, minlight);
				eb.setVarFloat(2, maxlight);
				eb.setVarInt(2, spawntime);
				eb.setVarInt(3, spawncount);
				w.spawnEntityInWorld(eb);
			}
		}
	}


}
