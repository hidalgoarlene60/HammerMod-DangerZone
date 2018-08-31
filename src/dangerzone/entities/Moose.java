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
import dangerzone.blocks.Blocks;
import dangerzone.items.Items;


public class Moose extends EntityLiving {
	
	public	Moose(World w){
		super(w);
		maxrenderdist = 180; //in blocks
		this.height = 3.33f;
		this.width = 1.33f;
		uniquename = "DangerZone:Moose";
		moveSpeed = 0.33f;
		setMaxHealth(100.0f);
		setHealth(100.0f);
		setDefense(2.0f);
		setAttackDamage(8.0f);
		searchDistance = 10f;
		attackRange = 3.5f;
		movefrequency = 65;
		setExperience(99);
		canSwim = true;
		daytimespawn = true;
		nighttimespawn = true;
		setCanDespawn(false);
		temperament = Temperament.HOSTILE;
		enable_hostile = true;
		enable_findfoodblock = true;
		foodblockfreq = 60;
		foodblockdistxz = 13;
		foodblockdisty = 3;
		foodblockdisteat = 12;
		foodblockheal = 5;
		isMilkable = true;
		tower_defense_enable = true;

	}
	
	
	public boolean isFoodBlock(int bid){
		if(bid == Blocks.grass.blockID)return true;
		return false;
	}
	
	public String getLivingSound(){
		return null;
	}
	
	public String getHurtSound(){
		return "DangerZone:moose_hit";
	}
	
	public String getDeathSound(){
		return "DangerZone:moose_death";
	}
	
	public void doDeathDrops(){
		int howmany = 5+world.rand.nextInt(4);
		int i;
		for(i=0;i<howmany;i++){
			Utils.doDropRand(world, 0, Items.moosemeat.itemID, 4f, dimension, posx, posy, posz);
		}
		howmany = 1+world.rand.nextInt(4);
		for(i=0;i<howmany;i++){
			Utils.doDropRand(world, 0, Items.moosebone.itemID, 3f, dimension, posx, posy, posz);
		}
		
		if(world.rand.nextInt(5)==1)Utils.doDropRand(world, 0, Items.trophymoose.itemID, 1f, dimension, posx, posy, posz);
		if(world.rand.nextInt(5)==1)Utils.doDropRand(world, 0, Items.moosehead.itemID, 1f, dimension, posx, posy, posz);
		super.doDeathDrops();
	}

	//Override, because we attack hostiles that are NOT moose!
	public boolean isSuitableTarget(Entity e){
		if(isIgnorable(e))return false;
		if(e instanceof Moose)return false;
		if(e.temperament == Temperament.HOSTILE && CanProbablySeeEntity(e) )return true;
		if(e instanceof Player && CanProbablySeeEntity(e) )return true;
		return false;
	}
	
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/"+ "Moosetexture.png");	//this is not fast, so we keep our own pointer!
		}
		return texture;
	}
	

}
