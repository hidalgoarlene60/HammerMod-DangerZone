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


public class Snarler extends EntityLiving {

	public float visibility = 1;
	
	public Snarler(World w) {
		super(w);
		width = 0.95f;
		height = 0.65f;	
		uniquename = "DangerZone:Snarler";		
		has_inventory = false;
		attackRange = 2.0f;
		setMaxHealth(15);
		setHealth(15);
		setDefense(1.5f);
		setAttackDamage(5);
		setExperience(80);
		setMaxAir(20);
		setAir(20);
		temperament = Temperament.HOSTILE;
		enable_hostile = true;
		daytimespawn = false;
		daytimedespawn = true;
		nighttimespawn = true;
		nighttimedespawn = false;
		tower_defense_enable = true;
		movefrequency = 40;
		visibility = 1;
		setInvisible(false);
		setVarFloat(10, 1);
	}

	public void doAttack(Entity victim){
		visibility = 1;
		setInvisible(false);
		super.doAttack(victim);
	}
	
	public void doAttackFrom(/*entity that hit me*/Entity e, /*DamageTypes*/int dt, float pain){	
		visibility = 1;
		setInvisible(false);
		super.doAttackFrom(e, dt, pain);
	}
	
	public void doEntityAction(float deltaT){			
		if(visibility > 0)visibility -= 0.01f;
		if(visibility < 0)visibility = 0;
		if(visibility == 0)setInvisible(true);
		setVarFloat(10, visibility);
		if(isSwarming())moveSpeed = 0.37f;
		super.doEntityAction(deltaT);
	}
	
	public float getVisibility(){
		return getVarFloat(10);
	}
	
	public String getLivingSound(){
		return null;
	}
	
	public String getHurtSound(){
		return "DangerZone:cryo_hurt";
	}
	
	public String getDeathSound(){
		return "DangerZone:big_splat";
	}
	
	public String getAttackSound(){
		int which = world.rand.nextInt(9);
		if(which == 0)return "DangerZone:growl1";
		if(which == 1)return "DangerZone:growl2";
		if(which == 2)return "DangerZone:growl3";
		if(which == 3)return "DangerZone:growl4";
		if(which == 4)return "DangerZone:growl5";
		if(which == 5)return "DangerZone:growl6";
		if(which == 6)return "DangerZone:growl7";
		if(which == 7)return "DangerZone:growl8";
		return "DangerZone:growl9";
	}
	
	public void update(float deltaT){
		if(world == null){
			visibility = 1;
			setInvisible(false);
		}
		super.update(deltaT);
	}
	
	public void doDeathDrops(){		
		super.doDeathDrops();
		if(world.rand.nextInt(10)==1)Utils.doDropRand(world, 0, Items.trophysnarler.itemID, 1f, dimension, posx, posy, posz);
		Utils.doDropRand(world, 0, Items.sign.itemID, 2f, dimension, posx, posy, posz);
		Utils.doDropRand(world, 0, Items.string.itemID, 2f, dimension, posx, posy, posz);
		Utils.doDropRand(world, 0, Items.moosebone.itemID, 2f, dimension, posx, posy, posz);
	}
	
	public boolean isSuitableTarget(Entity e){
		if(isIgnorable(e))return false;
		if((e instanceof Rat)||(e instanceof Cockroach)){
			if(CanProbablySeeEntity(e) )return true;
		}
		if(e instanceof Player && CanProbablySeeEntity(e) )return true;
		return false;
	}
	
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/Snarlertexture.png");	//this is not fast, so we keep our own pointer!
		}
		return texture;
	}

}
