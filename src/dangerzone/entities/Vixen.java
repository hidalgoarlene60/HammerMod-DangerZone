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
import dangerzone.items.Item;
import dangerzone.items.Items;


public class Vixen extends EntityLiving {

	
	public Vixen(World w) {
		super(w);
		width = 0.65f;
		height = 1.75f;	
		uniquename = "DangerZone:Vixen";		
		has_inventory = true;
		setMaxHealth(60);
		setHealth(60);
		setDefense(1.5f);
		setAttackDamage(10);
		setExperience(80);
		setMaxAir(40);
		setAir(40);
		searchDistance = 60f;
		attackRange = 3.0f;
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
					int itd = Items.bow.itemID;
					InventoryContainer ic = new InventoryContainer();
					ic.iid = itd;
					ic.count = 1;
					ic.currentuses = world.rand.nextInt(ic.getItem().maxuses);
					setHotbar(0, ic);
					sethotbarindex(0);
					ic = new InventoryContainer();
					ic.iid = Items.arrow.itemID;
					ic.count = 64;
					setHotbar(1, ic);
					ic = new InventoryContainer();
					ic.iid = Items.arrow.itemID;
					ic.count = 64;
					setHotbar(2, ic);
					ic = new InventoryContainer();
					ic.iid = Items.arrow.itemID;
					ic.count = 64;
					setHotbar(3, ic);
					ic = new InventoryContainer();
					ic.iid = Items.arrow.itemID;
					ic.count = 64;
					setHotbar(4, ic);
					/*
					 * Some armor is possible...
					 */
					if(world.rand.nextInt(6) == 0){
						if(world.rand.nextInt(2) == 0){
							ic = new InventoryContainer();
							ic.iid = Items.tinhelmet.itemID;
							ic.count = 1;
							ic.currentuses = world.rand.nextInt(ic.getItem().maxuses);
							setArmor(0, ic);
						}
						if(world.rand.nextInt(2) == 0){
							ic = new InventoryContainer();
							ic.iid = Items.tinchestplate.itemID;
							ic.count = 1;
							ic.currentuses = world.rand.nextInt(ic.getItem().maxuses);
							setArmor(1, ic);
						}
						if(world.rand.nextInt(2) == 0){
							ic = new InventoryContainer();
							ic.iid = Items.tinleggings.itemID;
							ic.count = 1;
							ic.currentuses = world.rand.nextInt(ic.getItem().maxuses);
							setArmor(2, ic);
							ic = new InventoryContainer();
							ic.iid = Items.tinboots.itemID;
							ic.count = 1;
							ic.currentuses = world.rand.nextInt(ic.getItem().maxuses);
							setArmor(3, ic);
						}
					}										
				}				
			}
		}
	}
	
	public void doTargetPrep(Entity ent){
		if(ent == null){
			setRightButtonDownCount(0);
			moveSpeed = 0.25f;
			setAttacking(false);
		}else{
			if(getDistanceFromEntity(ent) > attackRange){
				setRightButtonDownCount(getRightButtonDownCount()+10);//cycle in tenths, fullholdcount in 100ths...
				lookAtEntity(ent);
				setAttacking(false); //to stop the bow going up and down and looking silly
				moveSpeed = 0.05f;
			}else{
				setRightButtonDownCount(0);
				setAttacking(true);
				moveSpeed = 0.25f;
			}			
		}
	}
	
	public void doDistanceAttack(Entity victim){		
		if(world.isServer){
			InventoryContainer ic = getHotbar(0);
			if(ic != null && ic.iid == Items.bow.itemID){
				lookAtEntity(victim); //should be already, but just to be sure, and quick!
				float fullholdcount = Items.getfullholdcount(ic.iid);				
				float curcount = getRightButtonDownCount();
				if(curcount < 20)curcount = 20;
				float dist = (float) getDistanceFromEntity(victim);
				fullholdcount *= (dist/searchDistance); //fire faster as they get closer!
				if(curcount > fullholdcount){ 
					Item it = ic.getItem();
					it.rightclickup(this, ic, (int)(curcount));
					setRightButtonDownCount(0);
				}
			}
		}
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
		if(world.rand.nextInt(10)==1)Utils.doDropRand(world, 0, Items.trophyvixen.itemID, 1f, dimension, posx, posy, posz);
	}
	
	public boolean isSuitableTarget(Entity e){
		if(isIgnorable(e))return false;
		if(e instanceof TheCount)return false;
		if(e instanceof Vixen)return false;
		if(e.temperament == Temperament.HOSTILE && CanProbablySeeEntity(e) )return true;
		if(e instanceof Player && CanProbablySeeEntity(e) )return true;
		return false;
	}
	
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/"+ "vixen.png");	//this is not fast, so we keep our own pointer!
		}
		return texture;
	}

}
