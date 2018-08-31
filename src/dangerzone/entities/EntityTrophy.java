package dangerzone.entities;
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

import dangerzone.DamageTypes;
import dangerzone.DangerZone;
import dangerzone.InventoryContainer;
import dangerzone.ModelBase;
import dangerzone.Player;
import dangerzone.Utils;
import dangerzone.World;

public class EntityTrophy extends Entity {
	
	Entity trophyentity = null;
	ModelBase trophymodel = null;
	float trophyscale = 1.0f;
	int checker = 0;

	public EntityTrophy(World w) {
		super(w);
		uniquename = "DangerZone:EntityTrophy";
		ignoreCollisions = true;
		width = 1.0f;
		height = 1.0f;
		if(w != null){
			checker = 100+world.rand.nextInt(100); //just for fun, so they can pile up and explode into a mess...
		}
	}
	
	public void doAttackFrom(/*entity that hit me*/Entity e, /*DamageTypes*/int dt, float pain){
		if(!world.isServer)return;
		if(dt == DamageTypes.EXPLOSIVE){			
			Utils.doDropRand(world, 0, getIID(), 1f, dimension, posx, posy, posz);
			this.deadflag = true;
		}
	}
	
	public void update(float deltaT){		
		if(trophyentity == null){
			trophyentity = world.createEntityByName(getVarString(0), dimension, posx, posy, posz);
			if(trophyentity != null){
				trophyentity.rotation_yaw = 0;
				trophyentity.init();
				trophymodel = trophyentity.model;
				trophyscale = getVarFloat(0);
			}
		}
		checker--;
		if(checker <= 0 && world.isServer){
			List<Entity> nearby_list = null;
			checker = 100+world.rand.nextInt(100);
			nearby_list = DangerZone.server.entityManager.findALLEntitiesInRange(2, dimension, this.posx, this.posy, this.posz);
			if(nearby_list != null){
				if(!nearby_list.isEmpty()){
					Entity e = null;
					ListIterator<Entity> li;
					li = nearby_list.listIterator();
					while(li.hasNext()){
						e = (Entity)li.next();
						if(e instanceof EntityTrophy && e != this){ 						
							if((int)e.posx == (int)this.posx && (int)e.posy == (int)this.posy && (int)e.posz == (int)this.posz){
								rightClickedByPlayer(null, null);
								//I'm dead! Bye!
								break;
							}
						}
					}								
				}			
			}
		}
		if(world.isServer){
			int bid = world.getblock(dimension, (int)posx, (int)(posy-1), (int)posz);
			if(bid == 0){
				rightClickedByPlayer(null, null);
			}
		}
		motionx = motiony = motionz = 0;
		rotation_yaw_motion = 1;
		super.update(deltaT);
	}
	
	public Texture getTexture(){
		if(trophyentity != null){
			return trophyentity.getTexture();
		}
		return null;
	}
	
	public boolean rightClickedByPlayer(Player p, InventoryContainer ic){
		if(!world.isServer)return false;
		Utils.doDropRand(world, 0, getIID(), 1f, dimension, posx, posy, posz);
		this.deadflag = true;
		return false;
	}

}
