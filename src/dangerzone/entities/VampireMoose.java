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

import dangerzone.DamageTypes;
import dangerzone.Player;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.items.Items;


public class VampireMoose extends EntityLiving {
	public int lightcount = 0;

	public VampireMoose(World w) {
		super(w);
		maxrenderdist = 180; //in blocks
		this.height = 3.33f;
		this.width = 1.33f;
		uniquename = "DangerZone:Vampire Moose";
		moveSpeed = 0.37f;
		setMaxHealth(200.0f);
		setHealth(200.0f);
		setDefense(3.0f);
		setAttackDamage(15.0f);
		searchDistance = 20f;
		attackRange = 3.5f;
		movefrequency = 65;
		setExperience(199);
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
		if(world.rand.nextInt(5) != 0)return null;
		return "DangerZone:vampire_living";
	}
	
	public String getHurtSound(){
		return "DangerZone:moose_hit";
	}
	
	public String getDeathSound(){
		return "DangerZone:moose_death";
	}
	
	public String getAttackSound(){
		return "DangerZone:werewolf_attack";
	}
	
	public void doDeathDrops(){		
		super.doDeathDrops();
		if(world.rand.nextInt(5)==1)Utils.doDropRand(world, 0, Items.trophyvampiremoose.itemID, 1f, dimension, posx, posy, posz);
		Utils.doDropRand(world, 0, Items.vampireteeth.itemID, 2f, dimension, posx, posy, posz);
	}
	
	public void update(float deltaT){
		if(this.world.isServer){
			lightcount++;
			if(world.isDaytime() && lightcount > 100){ //don't check lighting too often. asks client across network!
				lightcount = 0;
				if(getLightAtLocation(world, dimension, (int)posx, (int)posy, (int)posz) > 0.55f){
					setOnFire(50);
					if(world.rand.nextInt(20) == 0){
						doAttackFrom(null, DamageTypes.DAYTIME, 1f);
					}
				}
			}
		}
		super.update(deltaT);
	}
	
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/"+ "VampireMoosetexture.png");	//this is not fast, so we keep our own pointer!
		}
		return texture;
	}
	
	public boolean isSuitableTarget(Entity e){
		if(isIgnorable(e))return false;
		if(e instanceof TheCount)return false;
		if(e instanceof Vampire)return false;
		if(e instanceof VampireMoose)return false;
		if(e.temperament == Temperament.HOSTILE && CanProbablySeeEntity(e) )return true;
		if(e instanceof Player && CanProbablySeeEntity(e) )return true;
		return false;
	}

}
