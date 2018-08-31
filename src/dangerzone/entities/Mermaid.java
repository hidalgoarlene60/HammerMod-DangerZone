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

import dangerzone.InventoryContainer;
//import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.items.Items;


public class Mermaid extends BetterFish {

	
	public Mermaid(World w) {
		super(w);
		width = 0.65f;
		height = 1.75f;	
		uniquename = "DangerZone:Mermaid";		
		has_inventory = true;
		setMaxHealth(70);
		setHealth(70);
		setDefense(1.5f);
		setAttackDamage(10);
		setExperience(80);
		setMaxAir(40);
		setAir(40);
		searchDistance = 30f;
		attackRange = 3.0f;
		temperament = Temperament.HOSTILE;
		enable_hostile = true;
		daytimespawn = true;
		daytimedespawn = false;
		nighttimespawn = false;
		nighttimedespawn = true;
		tower_defense_enable = false;
		moveSpeed = 0.28f; 
		enable_buddy = true;
		findbuddyfrequency = 35;
		findbuddydistance = 24;
	}
	
	public boolean isBuddy(Entity e){
		if(e instanceof Mermaid)return true;
		return false;
	}

	/*
	 * Initialize our armor once and only once!
	 */
	public void init(){
		super.init();
		if(world != null){
			if(world.isServer){
				if(getInitialized() == 0){
					setInitialized(1); //we are now!
					InventoryContainer ic = new InventoryContainer();
					/*
					 * Some armor is possible...
					 */					
					if(world.rand.nextInt(6) == 0){
						ic = new InventoryContainer();
						ic.iid = Items.tiara.itemID;
						ic.count = 1;
						ic.currentuses = world.rand.nextInt(ic.getItem().maxuses/2);
						setArmor(0, ic);
					}									
				}				
			}
		}
	}
	
	
	public void doEntityAction(float deltaT){	
		super.doEntityAction(deltaT);
		if(world.rand.nextInt(100) == 1)heal(1.0f);
	}
	
	public String getLivingSound(){
		return null;
	}
	
	public String getHurtSound(){
		int which = world.rand.nextInt(9);
		if(which == 0)return "DangerZone:vixen_hurt1";
		if(which == 1)return "DangerZone:vixen_hurt2";
		if(which == 2)return "DangerZone:vixen_hurt3";
		if(which == 3)return "DangerZone:vixen_hurt4";
		if(which == 4)return "DangerZone:vixen_hurt5";
		if(which == 5)return "DangerZone:vixen_hurt6";
		if(which == 6)return "DangerZone:vixen_hurt7";
		if(which == 7)return "DangerZone:vixen_hurt8";
		if(which == 8)return "DangerZone:vixen_hurt9";
		return null;
	}
	
	public String getDeathSound(){
		return getHurtSound();
	}
	
	public String getAttackSound(){
		return null;
	}
	
	public void doDeathDrops(){		
		super.doDeathDrops();
		if(world.rand.nextInt(5)==1)Utils.doDropRand(world, 0, Items.trophymermaid.itemID, 1f, dimension, posx, posy, posz);
	}
	
	public boolean isSuitableTarget(Entity e){
		if(isIgnorable(e))return false;
		if(e instanceof TheCount)return false;
		if(e instanceof Mermaid)return false;
		if(e.temperament == Temperament.HOSTILE && CanProbablySeeEntity(e) )return true;
		if(e instanceof Player && CanProbablySeeEntity(e) ){
			InventoryContainer ic = e.getArmor(0);
			if(ic != null && ic.iid == Items.tiara.itemID)return false;
			return true;
		}
		return false;
	}
	
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/"+ "Mermaidtexture.png");	//this is not fast, so we keep our own pointer!
		}
		return texture;
	}

}
