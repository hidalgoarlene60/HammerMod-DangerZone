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


public class Skeletorus extends EntityLiving {
	
	public	Skeletorus(World w){
		super(w);
		maxrenderdist = 64; //in blocks
		this.height = 0.65f;
		this.width = 1.25f;
		uniquename = "DangerZone:Skeletorus";
		moveSpeed = 0.36f;
		setMaxHealth(110.0f);
		setHealth(110.0f);
		setDefense(2.0f);
		setAttackDamage(5.5f);
		searchDistance = 32f;
		attackRange = 3.65f;
		movefrequency = 65;
		setExperience(153);
		canSwim = true;
		temperament = Temperament.HOSTILE;
		enable_hostile = true;
		daytimespawn = false;
		daytimedespawn = true;
		nighttimespawn = true;
		nighttimedespawn = false;
		tower_defense_enable = true;
	}
	
	
	public String getLivingSound(){
		return "DangerZone:fuzzbutt_living";
	}
	
	public String getHurtSound(){
		return "DangerZone:fuzzbutt_hit";
	}
	
	public String getDeathSound(){
		return "DangerZone:fuzzbutt_death";
	}
	
	public void doDeathDrops(){
		if(world.rand.nextInt(10)==1)Utils.doDropRand(world, 0, Items.trophyskeletorus.itemID, 1f, dimension, posx, posy, posz);
		int howmany = 5+world.rand.nextInt(5);
		int i;
		for(i=0;i<howmany;i++){
			Utils.doDropRand(world, 0, Items.string.itemID, 3f, dimension, posx, posy, posz);
		}
	}

	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/"+ "Skeletortexture.png");	//this is not fast, so we keep our own pointer!
		}
		return texture;
	}
	
	public boolean isSuitableTarget(Entity e){
		if(isIgnorable(e))return false;
		if(e instanceof Skeletorus)return false;
		if(e.temperament == Temperament.HOSTILE && CanProbablySeeEntity(e) )return true;
		if(e instanceof Player && CanProbablySeeEntity(e) )return true;
		return false;
	}
	
	public boolean getCanSpawnHereNow(World w, int dimension, int x, int y, int z){	
		if(getLightAtLocation(w, dimension, x, y, z) > 0.35f)return false;
		return true;
	}

}