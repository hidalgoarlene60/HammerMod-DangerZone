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

import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.items.Items;


public class Anteater extends EntityLiving {
	
	
	public	Anteater(World w){
		super(w);
		maxrenderdist = 130; //in blocks
		this.height = 1.25f;
		this.width = 0.95f;
		uniquename = "DangerZone:Anteater";
		moveSpeed = 0.25f;
		setMaxHealth(50.0f);
		setHealth(50.0f);
		setDefense(3.0f);
		setAttackDamage(5.0f);
		movefrequency = 45;
		setExperience(50);
		setCanDespawn(false);
		canSwim = true;
		
		enableHostility(14, 3.5f);
		temperament = Temperament.PASSIVE;
		enableTaming(14);
		enableFollowHeldFood(12);
		enableDroppedFood(12);
	}
	
	
	public boolean isFoodItem(int foodid){
		if(foodid == Items.deadbug.itemID)return true;
		return false;
	}
	
	
	//we attack all small things!
	public boolean isSuitableTarget(Entity e){
		if(isIgnorable(e))return false;
		if(e.getWidth()*e.getHeight() > 0.063f)return false;
		if(!CanProbablySeeEntity(e))return false;
		return true;
	}
		
	
	public String getLivingSound(){
		return null;
	}
	
	public String getHurtSound(){
		return "DangerZone:anteater_hit";
	}
	
	public String getDeathSound(){
		return null;
	}
	
	public void doDeathDrops(){		
		if(world.rand.nextInt(10)==1)Utils.doDropRand(world, 0, Items.trophyanteater.itemID, 1f, dimension, posx, posy, posz);
		super.doDeathDrops();
	}
	
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/"+ "Anteatertexture.png");	//this is not fast, so we keep our own pointer!
		}
		return texture;
	}
	

}
