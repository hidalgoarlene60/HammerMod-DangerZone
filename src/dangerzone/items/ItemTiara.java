package dangerzone.items;

import dangerzone.DamageTypes;
import dangerzone.DangerZone;
import dangerzone.InventoryContainer;
import dangerzone.entities.Entity;

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
public class ItemTiara extends ItemArmor {
	
	public ItemTiara(String n, String txt, float protvalue, int durability, int type) {
		super(n, txt, null, null, protvalue, durability, type);
		armortexturepath = "res/skins/"+ "tiara_1.png";
		burntime = 5;
		maxstack = 1;
	}
	
	public void armorTick(Entity e, InventoryContainer ic, int armorindex){	
		if(e == null)return;
		e.setAir(e.getMaxAir());
		//occasional bubble sound
		if(e.world.rand.nextInt(200) == 5) {
			if(e.getInLiquid()){
				e.world.playSound(bubbleSound(), e.dimension, e.posx, e.posy, e.posz, 0.5f, 1);
			}			
		}
		//suffer if not in the water!
		if(e.world.rand.nextInt(30) == 1) {
			if(!e.getInLiquid()){
				e.doAttackFrom(null, DamageTypes.POISON, 1.25f);
			}			
		}
		
	}
	
	public String bubbleSound(){
		int which = DangerZone.rand.nextInt(4);
		if(which == 0)return "DangerZone:martian_living1";
		if(which == 1)return "DangerZone:martian_living2";
		if(which == 2)return "DangerZone:martian_living3";
		return "DangerZone:martian_living4";
	}
	

}