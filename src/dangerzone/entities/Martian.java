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
import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.blocks.Blocks;
import dangerzone.items.Item;
import dangerzone.items.Items;


public class Martian extends EntityLiving {

	
	public Martian(World w) {
		super(w);
		width = 0.65f;
		height = 1.55f;	
		uniquename = "DangerZone:Martian";		
		has_inventory = true;
		setMaxHealth(30);
		setHealth(30);
		setDefense(1.5f);
		setAttackDamage(5);
		setExperience(80);
		setMaxAir(400);
		setAir(400);
		searchDistance = 50f;
		attackRange = 2.0f;
		temperament = Temperament.HOSTILE;
		enable_hostile = true;
		daytimespawn = true;
		daytimedespawn = false;
		nighttimespawn = false;
		nighttimedespawn = true;
		tower_defense_enable = true;
		moveSpeed = 0.25f;
		setCanDespawn(false);
	}
	
	public boolean rightClickedByPlayer(Player p, InventoryContainer ic){
		if(!world.isServer){
			InventoryContainer icm = getHotbar(0);
			if(icm != null && icm.iid == Items.bow.itemID){
				return false; //nope, has a bow!
			}
			DangerZone.tradegui.ec = this;
			DangerZone.setActiveGui(DangerZone.tradegui);
		}else{
			InventoryContainer icm = getHotbar(0);
			if(icm != null && icm.iid == Items.bow.itemID){
				return false; //nope, has a bow!
			}
			setStaying(true); //client kicked off a GUI!
		}
		return false; //do not decrement inventory
	}
	
	public void doEntityAction(float deltaT){
		super.doEntityAction(deltaT);
		InventoryContainer ic = getHotbar(0);
		if(ic == null){
			setVarInt(30, 0);
			return;
		}
		
		int cost = 0;
		if(ic.bid == Blocks.redleaves.blockID)cost = 10;
		if(ic.bid == Blocks.yellowleaves.blockID)cost = 10;
		if(ic.bid == Blocks.orangeleaves.blockID)cost = 10;		
		if(ic.bid == Blocks.flower_red.blockID)cost = 50;
		if(ic.bid == Blocks.flower_pink.blockID)cost = 50;
		if(ic.bid == Blocks.flower_yellow.blockID)cost = 50;
		if(ic.bid == Blocks.flower_purple.blockID)cost = 300;
	
		//set offer amount
		setVarInt(30, 0);	
		setVarInt(30, cost*ic.count);		
		
	}

	/*
	 * Initialize once and only once!
	 */
	public void init(){
		super.init();
		if(world != null){
			if(world.isServer){
				if(getInitialized() == 0){
					setInitialized(1); //we are now!
					if(world.rand.nextInt(3) == 1){
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
					}else{
						if(world.rand.nextBoolean()){
							switch(world.rand.nextInt(3)){
							case 0:
								//block seller
								for(int i=0;i<8;i++){
									setInventory(i, getSomethingB());
								}
								break;
							case 1:
								//block seller
								for(int i=0;i<8;i++){
									setInventory(i, getSomethingBR());
								}
								break;
							default:
								//block seller
								for(int i=0;i<8;i++){
									setInventory(i, getSomethingBW());
								}
								break;
							}
						}else{
							switch(world.rand.nextInt(3)){
							case 0:
								//item seller
								for(int i=0;i<8;i++){
									setInventory(i, getSomethingI());
								}
								break;
							case 1:
								//item seller
								for(int i=0;i<8;i++){
									setInventory(i, getSomethingIF());
								}
								break;
							default:
								//item seller
								for(int i=0;i<8;i++){
									setInventory(i, getSomethingIS());
								}
								break;
							}

						}
						//set prices
						for(int i=0;i<8;i++){
							setVarInt(i+16, (world.rand.nextInt(300)*10) + 10);
						}
					}
				}				
			}
		}
	}
	private InventoryContainer getSomethingI(){
		int i = world.rand.nextInt(43);
		switch(i){
		case 0:
			return new InventoryContainer("DangerZone:Door", 2);
		case 1:
			return new InventoryContainer("DangerZone:Sign", 4);
		case 2:
			return new InventoryContainer("DangerZone:DangerZone:Spawn Cloud", 8);
		case 3:
			return new InventoryContainer("DangerZone:Wooden Sword", 1);
		case 4:
			return new InventoryContainer("DangerZone:Stone Sword", 1);
		case 5:
			return new InventoryContainer("DangerZone:Copper Sword", 1);
		case 6:
			return new InventoryContainer("DangerZone:Tin Sword", 1);
		case 7:
			return new InventoryContainer("DangerZone:Silver Sword", 1);
		case 8:
			return new InventoryContainer("DangerZone:Platinum Sword", 1);
		case 9:
			return new InventoryContainer("DangerZone:Darkness", 32);
		case 10:
			return new InventoryContainer("DangerZone:Lightness", 32);
		case 11:
			return new InventoryContainer("DangerZone:Diamond", 1);
		case 12:
			return new InventoryContainer("DangerZone:Emerald", 4);
		case 13:
			return new InventoryContainer("DangerZone:Bloodstone", 4);
		case 14:
			return new InventoryContainer("DangerZone:Sunstone", 2);
		case 21:
			return new InventoryContainer("DangerZone:Silver Helmet", 1);
		case 22:
			return new InventoryContainer("DangerZone:Silver Chestplate", 1);
		case 23:
			return new InventoryContainer("DangerZone:Silver Leggings", 1);
		case 24:
			return new InventoryContainer("DangerZone:Silver Boots", 1);
		case 25:
			return new InventoryContainer("DangerZone:Furball", 64);
		case 26:
			return new InventoryContainer("DangerZone:Vamire Teeth", 2);
		case 27:
			return new InventoryContainer("DangerZone:Instability", 32);
		case 28:
			return new InventoryContainer("DangerZone:Auto-Fence Key", 1);
		case 29:
			return new InventoryContainer("DangerZone:String", 8);
		case 30:
			return new InventoryContainer("DangerZone:Bucket", 1);
		case 31:
			return new InventoryContainer("DangerZone:Charcoal Stick", 4);
		case 32:
			return new InventoryContainer("DangerZone:Paper", 32);
		case 33:
			return new InventoryContainer("DangerZone:Arrow", 32);
		default:
			break;
		}
		return null;
	}
	private InventoryContainer getSomethingIF(){
		int i = world.rand.nextInt(43);
		switch(i){
		case 15:
			return new InventoryContainer("DangerZone:Raw Goose", 16);
		case 16:
			return new InventoryContainer("DangerZone:Raw Ostrich", 16);
		case 17:
			return new InventoryContainer("DangerZone:Raw Fish", 16);
		case 18:
			return new InventoryContainer("DangerZone:Raw Meat", 16);
		case 19:
			return new InventoryContainer("DangerZone:Awful Gluten Free Bread", 32);
		case 20:
			return new InventoryContainer("DangerZone:Dead Bug", 4);
		default:
			break;
		}
		return null;
	}
	private InventoryContainer getSomethingIS(){
		int i = world.rand.nextInt(43);
		switch(i){
		case 34:
			return new InventoryContainer("DangerZone:Speed 1", 4);
		case 35:
			return new InventoryContainer("DangerZone:Regeneration 1", 4);
		case 36:
			return new InventoryContainer("DangerZone:Accuracy Scroll I", 4);
		case 37:
			return new InventoryContainer("DangerZone:Damage Scroll I", 4);
		case 38:
			return new InventoryContainer("DangerZone:Durability Scroll I", 4);
		case 39:
			return new InventoryContainer("DangerZone:Reach Scroll I", 4);
		case 40:
			return new InventoryContainer("DangerZone:Spam Scroll I", 4);
		case 41:
			return new InventoryContainer("DangerZone:Harm Scroll", 4);
		case 42:
			return new InventoryContainer("DangerZone:Heal Scroll", 4);
		default:
			break;
		}
		return null;
	}
	private InventoryContainer getSomethingB(){
		int i = world.rand.nextInt(38);
		switch(i){
		case 0:
			return new InventoryContainer("DangerZone:Stone", 64);
		case 1:
			return new InventoryContainer("DangerZone:Grey Stone", 64);
		case 2:
			return new InventoryContainer("DangerZone:White Stone", 64);
		case 3:
			return new InventoryContainer("DangerZone:Hard Rock", 64);
		case 4:
			return new InventoryContainer("DangerZone:Fire Stone", 64);
		case 5:
			return new InventoryContainer("DangerZone:Copper Ore", 64);
		case 6:
			return new InventoryContainer("DangerZone:Tin Ore", 64);
		case 7:
			return new InventoryContainer("DangerZone:Silver Ore", 64);
		case 8:
			return new InventoryContainer("DangerZone:Chunk Marker", 64);
		case 9:
			return new InventoryContainer("DangerZone:Dark Ore", 64);
		case 10:
			return new InventoryContainer("DangerZone:Light Ore", 64);
		case 11:
			return new InventoryContainer("DangerZone:Cockroach Nest", 64);
		case 12:
			return new InventoryContainer("DangerZone:Glass", 64);
		case 22:
			return new InventoryContainer("DangerZone:Auto-Fence", 64);
		case 23:
			return new InventoryContainer("DangerZone:Fence Post", 64);
		case 24:
			return new InventoryContainer("DangerZone:Ladder", 64);
		case 25:
			return new InventoryContainer("DangerZone:Shredder", 2);
		default:
			break;
		}
		return null;
	}
	private InventoryContainer getSomethingBW(){
		int i = world.rand.nextInt(38);
		switch(i){
		case 13:
			return new InventoryContainer("DangerZone:Water Pump", 4);
		case 14:
			return new InventoryContainer("DangerZone:Water Spout", 16);
		case 15:
			return new InventoryContainer("DangerZone:Water Light", 16);
		case 16:
			return new InventoryContainer("DangerZone:Water Dark", 16);
		case 17:
			return new InventoryContainer("DangerZone:Music Box", 2);
		case 18:
			return new InventoryContainer("DangerZone:Puddle Maker", 8);
		case 19:
			return new InventoryContainer("DangerZone:Water Cannon", 8);
		case 20:
			return new InventoryContainer("DangerZone:Squishy Stone", 32);
		case 21:
			return new InventoryContainer("DangerZone:Water Switch", 8);
		case 22:
			return new InventoryContainer("DangerZone:Auto-Fence", 64);
		case 23:
			return new InventoryContainer("DangerZone:Fence Post", 64);
		case 24:
			return new InventoryContainer("DangerZone:Ladder", 64);
		case 25:
			return new InventoryContainer("DangerZone:Shredder", 2);
		default:
			break;
		}
		return null;
	}
	private InventoryContainer getSomethingBR(){
		int i = world.rand.nextInt(38);
		switch(i){
		case 26:
			return new InventoryContainer("DangerZone:MagLev Rail", 64);
		case 27:
			return new InventoryContainer("DangerZone:Acceleration Rail", 8);
		case 28:
			return new InventoryContainer("DangerZone:Deceleration Rail", 8);
		case 29:
			return new InventoryContainer("DangerZone:Stop Rail", 4);
		case 30:
			return new InventoryContainer("DangerZone:Levitate Up Rail", 4);
		case 31:
			return new InventoryContainer("DangerZone:Levitate Down Rail", 4);
		case 32:
			return new InventoryContainer("DangerZone:Slow Speed Rail", 8);
		case 33:
			return new InventoryContainer("DangerZone:Medium Speed Rail", 8);
		case 34:
			return new InventoryContainer("DangerZone:Load Rail", 4);
		case 35:
			return new InventoryContainer("DangerZone:Unload Rail", 4);
		case 36:
			return new InventoryContainer("DangerZone:Dimension+ Rail", 2);
		case 37:
			return new InventoryContainer("DangerZone:Dimension- Rail", 2);
		default:
			break;
		}
		return null;
	}
	
	public void doTargetPrep(Entity ent){
		//only hostile if holding a weapon!
		InventoryContainer ic = getHotbar(0);
		if(ic == null || ic.iid != Items.bow.itemID){
			temperament = Temperament.PASSIVE;
			setAttacking(false);
			enable_hostile = false;
			moveSpeed = 0.25f;
			return;
		}
		
		enable_hostile = true;
		temperament = Temperament.HOSTILE;
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
		if(world.rand.nextBoolean())return null;
		int which = world.rand.nextInt(4);
		if(which == 0)return "DangerZone:martian_living1";
		if(which == 1)return "DangerZone:martian_living2";
		if(which == 2)return "DangerZone:martian_living3";
		return "DangerZone:martian_living4";
	}
	
	public float getLivingSoundVolume(){
		return 0.25f;
	}
	
	public String getHurtSound(){
		return "DangerZone:martian_hit";
	}
	
	public String getDeathSound(){
		return "DangerZone:martian_death";
	}
	
	public String getAttackSound(){
		return null;
	}
	
	public void doDeathDrops(){	
		//clear inventory! Otherwise it's too lucrative to kill martians!
		for(int i=0;i<60;i++){
			setVarInventory(i, null);
		}
		super.doDeathDrops();
		if(world.rand.nextInt(10)==1)Utils.doDropRand(world, 0, Items.trophymartian.itemID, 1f, dimension, posx, posy, posz);
	}
	
	public boolean isSuitableTarget(Entity e){
		if(isIgnorable(e))return false;
		if(e instanceof Martian)return false;
		if(e.temperament == Temperament.HOSTILE && CanProbablySeeEntity(e) )return true;
		if(e instanceof Player && CanProbablySeeEntity(e) )return true;
		return false;
	}
	
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/Martiantexture.png");	//this is not fast, so we keep our own pointer!
		}
		return texture;
	}

}
