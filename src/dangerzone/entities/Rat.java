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

import dangerzone.Player;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.items.Items;


public class Rat extends EntityLiving {
	
	public	Rat(World w){
		super(w);
		maxrenderdist = 64; //in blocks
		this.height = 0.35f;
		this.width = 0.45f;
		uniquename = "DangerZone:Rat";
		moveSpeed = 0.25f;
		setMaxHealth(5.0f);
		setHealth(5.0f);
		setDefense(1.25f);
		setAttackDamage(1.0f);
		searchDistance = 16f;
		attackRange = 1.5f;
		movefrequency = 25;
		setExperience(10);
		canSwim = true;
		temperament = Temperament.HOSTILE;
		enable_hostile = true;
		daytimespawn = false;
		daytimedespawn = true;
		nighttimespawn = true;
		nighttimedespawn = false;
		canridemaglevcart = true;
		tower_defense_enable = true;
	}
	
	
	public String getLivingSound(){
		return "DangerZone:ratlive";
	}
	
	public String getHurtSound(){
		return "DangerZone:rathit";
	}
	
	public String getDeathSound(){
		int i = world.rand.nextInt(3);
		if(i == 0)return "DangerZone:ratdead1";
		if(i == 1)return "DangerZone:ratdead2";
		return "DangerZone:ratdead3";
	}
	
	public void doDeathDrops(){
		if(world.rand.nextInt(20)==1)Utils.doDropRand(world, 0, Items.trophyrat.itemID, 1f, dimension, posx, posy, posz);
	}
	
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/"+ "Rattexture.png");	//this is not fast, so we keep our own pointer!
		}
		return texture;
	}
	
	public boolean isSuitableTarget(Entity e){
		if(isIgnorable(e))return false;
		if(e instanceof Rat)return false;
		if(e instanceof Flag && CanProbablySeeEntity(e) )return true;
		if(e instanceof Player && CanProbablySeeEntity(e) )return true;
		return false;
	}
	
	//only at night where it's dark
	public boolean getCanSpawnHereNow(World w, int dimension, int x, int y, int z){
		if(y > 30){
			return super.getCanSpawnHereNow( w,  dimension,  x,  y,  z);
		}else{
			if(getLightAtLocation(w, dimension, x, y, z) > 0.35f)return false;
		}
		return true;
	}
	

}
