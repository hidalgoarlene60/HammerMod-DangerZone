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
import dangerzone.Player;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.blocks.Blocks;
import dangerzone.items.Items;


public class Werewolf extends EntityLiving {
	
	public Werewolf(World w) {
		super(w);
		width = 0.75f;
		height = 1.75f;	
		uniquename = "DangerZone:Werewolf";		
		has_inventory = true;
		attackRange = 3.0f;
		setMaxHealth(40);
		setHealth(40);
		setExperience(50);
		setDefense(1.5f);
		setAttackDamage(5);
		setMaxAir(40);
		setAir(40);
		temperament = Temperament.HOSTILE;
		enable_hostile = true;
		daytimespawn = false;
		daytimedespawn = true;
		nighttimespawn = true;
		nighttimedespawn = false;
		tower_defense_enable = true;
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
					/*
					 * Give him a weapon, maybe!
					 */
					if(world.rand.nextInt(6) == 0){
						int wt = world.rand.nextInt(4);
						int itd = 0;
						InventoryContainer ic = new InventoryContainer();
						if(wt == 0)itd = Items.stoneaxe.itemID;
						if(wt == 1)itd = Items.woodenaxe.itemID;
						if(wt == 2)itd = Items.stonesword.itemID;
						if(wt == 3)itd = Items.woodensword.itemID;
						ic.iid = itd;
						ic.count = 1;
						ic.currentuses = world.rand.nextInt(ic.getItem().maxuses);
						setHotbar(0, ic);
						sethotbarindex(0);
					}										
				}				
			}
		}
	}
	
	public void jump(){
		if(getInLiquid() && canSwim){
			if(Blocks.isLiquid(world.getblock(dimension, (int)posx, (int)(posy+(height*3/4) + swimoffset),(int)posz))){				
				if(world.rand.nextInt(2) == 0){
					motiony += 0.155f;
					if(isSolidAtLevel(dimension, posx, posy-0.35f, posz))motiony += 0.55f;
				}				
			}
		}
		//Gotta be on solid ground to jump!
		if(!isSolidAtLevel(dimension, posx, posy-0.10f, posz))return;
		if(Math.abs(motiony)>0.15f)return;
		this.motiony += (0.75f + 0.15f*this.height)*2; //really good jumper!
	}
	
	public void moveTowardsTarget(float deltaT){
		super.moveTowardsTarget(deltaT);
		if(target != null && target.te != null){
			if(world.rand.nextInt(20) == 0){
				jump(); //will usually jump towards target or behind it.
			}
		}
	}
	
	public String getLivingSound(){
		if(world.rand.nextInt(5) != 0)return null;
		return "DangerZone:werewolf_living";
	}
	
	 public float getAdjustedFallDamage(float ouch){
		 float damage = ouch/2;	
		 if(damage < 3)damage = 0;
		 return damage;
	 }
	
	public String getHurtSound(){
		return "DangerZone:anteater_hit";
	}
	
	public String getDeathSound(){
		return "DangerZone:werewolf_death";
	}
	
	public String getAttackSound(){
		return "DangerZone:werewolf_attack";
	}
	
	public void doDeathDrops(){		
		super.doDeathDrops();
		if(world.rand.nextInt(10)==1)Utils.doDropRand(world, 0, Items.trophywerewolf.itemID, 1f, dimension, posx, posy, posz);
		int howmany = 5+world.rand.nextInt(5);
		int i;
		for(i=0;i<howmany;i++){
			Utils.doDropRand(world, 0, Items.furball.itemID, 3f, dimension, posx, posy, posz);
		}
	}
	
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/"+ "werewolf.png");	//this is not fast, so we keep our own pointer!
		}
		return texture;
	}
	
	public boolean isSuitableTarget(Entity e){
		if(isIgnorable(e))return false;
		if(e instanceof Werewolf)return false;
		if(e.temperament == Temperament.HOSTILE && CanProbablySeeEntity(e) )return true;
		if(e instanceof Player && CanProbablySeeEntity(e) )return true;
		return false;
	}

}
