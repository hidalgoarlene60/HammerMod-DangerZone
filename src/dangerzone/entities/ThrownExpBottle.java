package dangerzone.entities;


import dangerzone.Utils;
import dangerzone.World;
import dangerzone.items.Items;


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

public class ThrownExpBottle extends ThrownBlockItem {
	

	public ThrownExpBottle(World w) {
		super(w);
		uniquename = "DangerZone:ThrownExpBottle";
		setIID(Items.experiencebottle.itemID);
	}
	
	public void doSpecialEffects(double x, double y, double z){
		//none
	}
	
	public void doHitSomething(boolean hb, double x, double y, double z, boolean he, Entity ent){
		//we are on the server already...
		world.playSound("DangerZone:crystalblockbreak", dimension, posx+motionx, posy+motiony, posz+motionz, 1.0f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
		Utils.spawnParticlesFromServer(world, "DangerZone:ParticleSmoke", world.rand.nextInt(10)+10, dimension, posx+motionx, posy+motiony, posz+motionz);	
		Utils.spawnParticlesFromServer(world, "DangerZone:ParticleSparkle", world.rand.nextInt(6)+6, dimension, posx+motionx, posy+motiony, posz+motionz);
		Utils.spawnExperience(world.rand.nextInt(20)+2, world, dimension, posx+motionx, posy+motiony, posz+motionz);
		this.deadflag = true;
	}

}
