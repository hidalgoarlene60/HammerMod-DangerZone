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

import dangerzone.DangerZone;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.items.Items;


public class Gosling extends EntityLiving {

	public int timetogrow = 2000;
	
	public Gosling(World w) {
		super(w);
		maxrenderdist = 120; //in blocks
		this.height = 0.45f;
		this.width = 0.25f;
		uniquename = "DangerZone:Gosling";
		moveSpeed = 0.175f;
		setMaxHealth(2.0f);
		setHealth(2.0f);
		setDefense(0.5f);
		setAttackDamage(0f);
		movefrequency = 25;
		setExperience(2);
		canSwim = true;
		swimoffset = -0.20f;
		setCanDespawn(false);
		takesFallDamage = false;
		setOnGround(true); //so the trophy doesn't flap!
		setBaby(true);
		enable_buddy = true;
		findbuddydistance = 16;
		findbuddyfrequency = 5;
		tower_defense_enable = false;
	}
	
	public void init(){
		super.init();
		timetogrow = 2000 + DangerZone.rand.nextInt(2000);
	}
	
	public String getLivingSound(){
		return "DangerZone:goose_living";
	}
	
	public float getLivingSoundPitch(){
		return 2.5f;
	}
	
	public float getLivingSoundVolume(){
		return 0.5f;
	}
	
	public String getHurtSound(){
		return "DangerZone:goose_hit";
	}
	
	public String getDeathSound(){
		return "DangerZone:goose_death";
	}
	
	
	public void doDeathDrops(){
		if(world.rand.nextInt(20)==1)Utils.doDropRand(world, 0, Items.trophygosling.itemID, 1f, dimension, posx, posy, posz);		
		Utils.doDropRand(world, 0, Items.feather.itemID, 1.5f, dimension, posx, posy, posz);		
	}
	
	public void update(float deltaT){
		if(!world.isServer && world.rand.nextInt(50) == 1){ //do some flapping just for fun!
			madtimer = world.rand.nextInt(25);
		}
		if(world.isServer){
			timetogrow--;
			if(timetogrow <= 0){
				this.deadflag = true; //bye bye!
				Entity newme = world.createEntityByName("DangerZone:Goose", dimension, posx, posy, posz);
				if(newme != null){
					newme.init();				
					world.spawnEntityInWorld(newme);
					world.playSound("DangerZone:pop", dimension, posx, posy, posz, 1.0f, 1.0f+(world.rand.nextFloat()-world.rand.nextFloat())*0.3f);
				}
			}
		}
		super.update(deltaT);
	}
	

	public boolean isBuddy(Entity e){
		if(e instanceof Goose)return true;
		return false;
	}
	
		
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/"+ "Goslingtexture.png");	//this is not fast, so we keep our own pointer!
		}
		return texture;
	}

}
