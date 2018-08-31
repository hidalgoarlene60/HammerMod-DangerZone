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


import org.newdawn.slick.opengl.Texture;


import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.blocks.Blocks;

public class EntityStand extends Entity {
	
	public float spinz = 0;

	public EntityStand(World w) {
		super(w);
		uniquename = "DangerZone:EntityStand";
		ignoreCollisions = true;
		width = 1.0f;
		height = 1.5f;
		has_inventory = true;
	}
	
	public void doAttackFrom(/*entity that hit me*/Entity e, /*DamageTypes*/int dt, float pain){
		if(!world.isServer)return;	
		if(world.getblock(dimension, (int)posx, (int)(posy+0.5f), (int)posz) == Blocks.stand.blockID){
			world.setblock(dimension, (int)posx, (int)(posy+0.5f), (int)posz, 0);
			Utils.doDropRand(world, Blocks.stand.blockID, 0, 0f, dimension, posx, posy, posz);
		}
		InventoryContainer ic = getInventory(0);
		if(ic != null)for(int i=0;i<ic.count;i++)Utils.doDropRand(world, ic.bid, ic.iid, 1f, dimension, posx, posy, posz);
		this.deadflag = true;
		world.playSound(Blocks.getBreakSound(Blocks.stand.blockID), dimension, (int)posx, (int)posy, (int)posz, 0.5f, 1.0f);
	}
	
	public void doEntityAction(float deltaT){
		motionx = motiony = motionz = 0;
		if(!Blocks.isSolid(world.getblock(dimension, (int)posx, (int)(posy-0.5f), (int)posz)))doAttackFrom(this, 1, 1);
		if(Blocks.isSolid(world.getblock(dimension, (int)posx, (int)(posy+1.5f), (int)posz)))doAttackFrom(this, 1, 1);
		if(world.getblock(dimension, (int)posx, (int)(posy+0.5f), (int)posz) != Blocks.stand.blockID)doAttackFrom(this, 1, 1);
		//super.doEntityAction(deltaT);
	}
	
	public Texture getTexture(){
		return null;
	}
	
	public float getSpinz(){
		spinz += 0.75f;
		spinz = spinz % 360;
		return spinz;
	}

	public boolean rightClickedByPlayer(Player p, InventoryContainer ic){	
		if(world.isServer){
			//empty hand - ride, or get off!
			if(ic == null){
				if(getInventory(0) != null){
					p.setHotbar(p.gethotbarindex(), getInventory(0));
					setInventory(0, null);
					world.playSound("DangerZone:pop", dimension, (int)posx, (int)posy, (int)posz, 0.5f, 1.0f);
				}
			}else{
				if(getInventory(0) == null){
					setInventory(0, ic);
					p.setHotbar(p.gethotbarindex(), null);
					world.playSound("DangerZone:pop", dimension, (int)posx, (int)posy, (int)posz, 0.5f, 1.0f);
				}
			}
			
		}
		return false;
	}

}
