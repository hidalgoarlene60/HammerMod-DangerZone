package dangerzone.entities;

import org.newdawn.slick.opengl.Texture;

import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.entities.EntityLiving;
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

public class Fish extends EntityLiving {
	//******
	// NO! This is NOT the class you want!!!
	// Go look at class BetterFish!!!
	//******
	public Fish(World w) {
		super(w);
		maxrenderdist = 160; //in blocks
		this.height = 0.85f;
		this.width = 0.75f;
		uniquename = "DangerZone:Fish";
		moveSpeed = 0.175f;
		setMaxHealth(1.0f);
		setHealth(1.0f);
		setDefense(1.0f);
		setAttackDamage(0f);
		movefrequency = 25;
		setExperience(1);
		canSwim = true;
		takesFallDamage = true;
		canBreateUnderWater = true;
		targetLiquidOnly = true; //stay in the water!!!!
		daytimespawn = true;
		nighttimespawn = false;
		daytimedespawn = false;
		nighttimedespawn = true;
	}
	
	public String getLivingSound(){
		if(world.rand.nextBoolean())return null;
		return "DangerZone:little_splash";
	}
	
	public String getHurtSound(){
		return "DangerZone:little_splat";
	}
	
	public String getDeathSound(){
		return "DangerZone:little_splat";
	}
	
	
	public void doDeathDrops(){		
		if(world.rand.nextInt(10)==1)Utils.doDropRand(world, 0, Items.trophyfish.itemID, 1f, dimension, posx, posy, posz);				
		Utils.doDropRand(world, 0, Items.fishmeat.itemID, 1.5f, dimension, posx, posy, posz);		
	}
		
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/Fishtexture.png");	//this is not fast, so we keep our own pointer!
		}
		return texture;
	}

}
