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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.newdawn.slick.opengl.Texture;

import dangerzone.DamageTypes;
import dangerzone.DangerZone;
import dangerzone.Dimensions;
import dangerzone.Effects;
import dangerzone.GameModes;
import dangerzone.InventoryContainer;
import dangerzone.ModelBase;
import dangerzone.Player;
import dangerzone.PlayerKeyEvent;
import dangerzone.ToDoList;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.WorldRendererUtils;
import dangerzone.blocks.Blocks;


public class Entity {
	public volatile int dimension;
	public volatile double posx;
	public volatile double posy;
	public volatile double posz;
	public volatile float motionx;
	public volatile float motiony;
	public volatile float motionz;
	public volatile float rotation_yaw;
	public volatile float rotation_pitch;
	public volatile float rotation_roll;
	public volatile float rotation_yaw_head;
	public volatile float rotation_pitch_head;
	public volatile float rotation_roll_head;
	public volatile float rotation_yaw_motion;
	public volatile float rotation_pitch_motion;
	public volatile float rotation_roll_motion;

	public int prevdimension;
	public double prevposx;
	public double prevposy;
	public double prevposz;
	public float prevrotation_yaw;
	public float prevrotation_pitch;
	public float prevrotation_roll;
	public float prevrotation_yaw_head;
	public float prevrotation_pitch_head;
	public float prevrotation_roll_head;
	
	public double display_posx; //For display purposes, to help keep rider and mount in sync.
	public double display_posy;
	public double display_posz;
	public float display_rotation_yaw;
	public float display_rotation_pitch;
	public float display_rotation_roll;
	
	//smoothing factors....
	//the movement "anticipation" logic isn't perfect, so slowly make up the difference.
	public int diff_ticker;
	public float diff_posx;
	public float diff_posy;
	public float diff_posz;
	public float diff_rotpitch;
	public float diff_rotyaw;
	public float diff_rotroll;
	public float diff_rotpitchh;
	public float diff_rotyawh;
	public float diff_rotrollh;


	public float width = 0.75f; //use getWidth() and getHeight() for EntityLiving things!
	public float height = 1.95f;
	public float eyeheight = 0;
	public boolean deadflag = false;
	//Globally unique ID for each individual entity across server and all clients.
	public int entityID = 0;
	public long lasttime = System.currentTimeMillis();
	public long currtime;
	public World world = null;
	public int maxrenderdist = 256; //In blocks
	public String uniquename;
	public long lifetimeticker;
	public int hurtanimationtimer;
	public int madtimer = 0;
	public int diffticker;
	
	//Draw me!
	//All entities of the same type share the same model and texture(s)
	//This is so that they don't eat up GPU memory, which they would exhaust rapidly...
	//The model can call back into the entity to get state when it needs to.
	public ModelBase model;
	public Texture texture;
	
	//These are extra variables that go from server to client (for non-player entities).
	//(they go client to server for players!)
	//Use the set/get functions for accessing them.
	//Your changes will get sent on the next update packet, usually in about 1/10th of a second.
	//No, there aren't any variables that go from client to server for non-player entities.
	//NOT access-locked. A bit risky, i know...
	// -- maxinv must be > maxvars !!!!
	//0-30 ARE RESERVED!!! USE ONLY 31-49
	public final int maxvars = 50; // ********* 0-30 ARE RESERVED!!! USE ONLY 31-49
	//0-75 ARE RESERVED!!! USE ONLY 76-99
	public final int maxinv = 100; // ********* 0-75 ARE RESERVED!!! USE ONLY 76-99
	
	public volatile short changed;
	public volatile short changes[];
	public int entity_ints[];
	public float entity_floats[];
	public String entity_strings[];
	
	public InventoryContainer entity_inventory[];
	public boolean has_inventory = false;
	
	public boolean takesFallDamage = false;
	public boolean ignoreCollisions = false;
	public boolean canthitme = false; //yes, you can hit me.
	public boolean movement_friction = true;
	public int stray_entity_ticker = 0;
	public int firecounter = 0;
	public boolean sit_when_riding = true;	
	public boolean always_draw = false;
	public int temperament = Temperament.NONE;
	public boolean canFly = false; 
	public boolean isMilkable = false;
	public boolean isImmuneToFire = false;
	public boolean canBreateUnderWater = false;
	public List<Effects> effect_list = null;
	public List<PlayerKeyEvent> keyevent_list = null;
	public List<PlayerKeyEvent> mouseevent_list = null;
	public boolean do_death_drops = true; 
	public int maxidleupdate = 3000;

	
	public Entity(World w){
		world = w; //CAREFULL!!!! WORLD CAN BE NULL if this is a spawn list entry!!!
		posx = 0;
		posy = 0;
		posz = 0;
		rotation_yaw = 0;
		rotation_pitch = 0;
		rotation_roll = 0;
		rotation_yaw_head = 0;
		rotation_pitch_head = 0;
		rotation_roll_head = 0;
		prevdimension = dimension = 1;
		prevposx = prevposy = prevposz = 0;
		prevrotation_yaw = prevrotation_pitch = prevrotation_roll = 0;
		prevrotation_yaw_head = prevrotation_pitch_head = prevrotation_roll_head = 0;
		deadflag = false;
		lifetimeticker = 0;
		uniquename = "DangerZone:BaseEntity";
		changed = 0;
		
		entity_ints = new int[maxvars];
		entity_floats = new float[maxvars];
		entity_strings = new String[maxvars];
		for(int i=0;i<maxvars;i++){
			entity_ints[i] = 0;
			entity_floats[i] = 0;
			entity_strings[i] = null;
		}
		
		changes = new short[maxinv];
		for(int i=0;i<maxinv;i++){
			changes[i] = 0;
		}
		entity_inventory = new InventoryContainer[maxinv];
		for(int i=0;i<maxinv;i++){
			entity_inventory[i] = null;
		}

		hurtanimationtimer = 0;
		texture = null;
		canthitme = false;
		setCanDespawn(true);
		diffticker = 0;

		effect_list = new ArrayList <Effects>();
		maxidleupdate = 3000 + DangerZone.rand.nextInt(300); //to spread out massive numbers of things that would have updated in sync, like chests.

	};
	
	public void init(){
		//Do any special stuff you can't do in your constructor, here.
		//This is AFTER it is read from disk (readself()) or spawned.		
		//clear mount/mounted!
		setVarInt(10, 0); //rider
		setVarInt(11, 0); //ridee
		setSitting(false);

	}
	
	public void de_init(){
		//Do any special stuff you can't do in your destructor, here.
		//This is just before it is written to disk (writeself()) on the SERVER side.
		int i = getVarInt(10);
		int j = getVarInt(11);
		setVarInt(10, 0);
		setVarInt(11, 0);
		if(i != 0 || j != 0){			
			setSitting(false); //dismount riders
		}
	}
	
	public void setSize(float x_size, float y_size){
		width = x_size;
		height = y_size;
	}
	
	public float getWidth(){
		if(isBaby())return width/4;
		return width;
	}
	
	public float getHeight(){
		float h = height;
		if(getSitting())h /= 2;
		if(isBaby())return h/4;
		return h;
	}
	
	public float getNameHeight(){
		return height;
	}
	
	public int getVarInt(int index){
		if(index < 0 || index >= maxvars)return 0;
		return entity_ints[index];
	}
	
	public float getVarFloat(int index){
		if(index < 0 || index >= maxvars)return 0;
		return entity_floats[index];
	}
	
	public String getVarString(int index){
		if(index < 0 || index >= maxvars)return null;
		return entity_strings[index];
	}
	
	public InventoryContainer getVarInventory(int index){
		if(!has_inventory)return null;
		if(index < 0 || index >= maxinv)return null;
		return entity_inventory[index];
	}
	
	public void setVarInt(int index, int val){
		if(index < 0 || index >= maxvars)return;
		if(entity_ints[index] != val){
			entity_ints[index] = val;
			changes[index] |= 0x01;
			changed = 1;
		}
	}
	
	public void setVarFloat(int index, float val){
		if(index < 0 || index >= maxvars)return;
		if(entity_floats[index] != val){
			entity_floats[index] = val;
			changes[index] |= 0x02;
			changed = 1;
		}
	}
	
	public void setVarString(int index, String val){
		if(index < 0 || index >= maxvars)return;
		if(entity_strings[index] != val){
			entity_strings[index] = val;
			changes[index] |= 0x04;
			changed = 1;
		}
	}
	
	public void setVarInventory(int index, InventoryContainer val){
		if(!has_inventory)return;
		if(index < 0 || index >= maxinv)return;
		if(val == null && entity_inventory[index] == null)return;
		entity_inventory[index] = val;
		changes[index] |= 0x08;
		changed = 1;
	}
	public void setVarInventoryChanged(int index){
		if(!has_inventory)return;
		if(index < 0 || index >= maxinv)return;
		changes[index] |= 0x08;
		changed = 1;
	}
	
	public void setVarInventoryChanged(InventoryContainer ic){
		if(!has_inventory)return;
		for(int index = 0;index < maxinv; index++){
			if(entity_inventory[index] == ic){
				changes[index] |= 0x08;
				changed = 1;
				break;
			}
		}
	}

	//recommended range 1-10000
	public void setMaxHealth(float f){
		setVarFloat(0, f);
	}
	
	public float getMaxHealth(){
		if(isBaby())return getVarFloat(0)/16f;
		return getVarFloat(0);
	}
	
	public void setHealth(float f){
		if(f > getMaxHealth())f = getMaxHealth();
		setVarFloat(1, f);
	}
	
	public void heal(float f){
		float mh = getMaxHealth();
		float newhealth = getHealth() + f;
		if(newhealth > mh)newhealth = mh;
		setHealth(newhealth);		
	}
	
	public float getHealth(){
		float f = getVarFloat(1);
		if(f > getMaxHealth()){
			f = getMaxHealth();
			setVarFloat(1, f);
		}
		return f;
	}
	
	//recommended range 1-100
	public void setDefense(float f){
		setVarFloat(2, f);
	}
	
	public float getDefense(){ //attack damage is DIVIDED by 
		return getVarFloat(2);
	}
	
	//recommended range 1-100
	public void setAttackDamage(float f){
		setVarFloat(3, f);
	}
	
	public float getAttackDamage(){ 
		return getVarFloat(3);
	}
	
	public void setMaxHunger(float f){
		setVarFloat(4, f);
	}
	
	public float getMaxHunger(){
		return getVarFloat(4);
	}
	
	public void setHunger(float f){
		setVarFloat(5, f);
	}
	
	public float getHunger(){
		return getVarFloat(5);
	}
	
	public void setMaxAir(float f){
		setVarFloat(6, f);
	}
	
	public float getMaxAir(){
		return getVarFloat(6);
	}
	
	public void setAir(float f){
		setVarFloat(7, f);
	}
	
	public float getAir(){
		return getVarFloat(7);
	}
	
	public void setBID(int f){
		setVarInt(0, f);
	}
	
	public int getBID(){
		return getVarInt(0);
	}
	
	public void setIID(int f){
		setVarInt(1, f);
	}
	
	public int getIID(){
		return getVarInt(1);
	}
	
	public int getGameMode(){
		return getVarInt(2);
	}
	
	public void setGameMode(int f){
		setVarInt(2, f);
	}
	
	public void sethotbarindex(int f){
		if(f >= 0 && f <= 9){
			setVarInt(3, f);
		}
	}
	
	public int gethotbarindex(){
		return getVarInt(3);
	}
	
	public void setItemDamage(int f){
		setVarInt(4, f);
	}
	
	public int getItemDamage(){
		return getVarInt(4);
	}
	
	public void setAttacking(boolean tf){
		int f = getVarInt(5);
		if(tf){
			f |= 0x01;
		}else{
			f &= 0xfffffffe;
		}
		setVarInt(5, f);
	}	
	public boolean getAttacking(){
		if((getVarInt(5)&0x01) == 0x01)return true;
		return false;
	}
	
	public void setInLiquid(boolean tf){
		int f = getVarInt(5);
		if(tf){
			f |= 0x02;
		}else{
			f &= 0xfffffffd;
		}
		setVarInt(5, f);
	}	
	public boolean getInLiquid(){
		if((getVarInt(5)&0x02) == 0x02)return true;
		return false;
	}
	
	public void setOnGround(boolean tf){
		int f = getVarInt(5);
		if(tf){
			f |= 0x04;
		}else{
			f &= 0xfffffffb;
		}
		setVarInt(5, f);
	}	
	public boolean getOnGround(){
		if((getVarInt(5)&0x04) == 0x04)return true;
		return false;
	}
	
	public void setStaying(boolean tf){
		int f = getVarInt(5);
		if(tf){
			f |= 0x08;
		}else{
			f &= 0xfffffff7;
		}
		setVarInt(5, f);
	}	
	public boolean getStaying(){
		if((getVarInt(5)&0x08) == 0x08)return true;
		return false;
	}
	
	public void setCanDespawn(boolean tf){
		int f = getVarInt(5);
		if(tf){
			f |= 0x10;
		}else{
			f &= 0xffffffef;
		}
		setVarInt(5, f);
	}	
	public boolean getCanDespawn(){
		if((getVarInt(5)&0x10) == 0x10)return true;
		return false;
	}
	
	public void setSitting(boolean tf){
		int f = getVarInt(5);
		if(tf){
			f |= 0x020;
		}else{
			f &= 0xffffffdf;
		}
		setVarInt(5, f);
	}	
	public boolean getSitting(){
		if((getVarInt(5)&0x020) == 0x020)return true;
		return false;
	}
	
	public void setBaby(boolean tf){
		int f = getVarInt(5);
		if(tf){
			f |= 0x040;
		}else{
			f &= 0xffffffbf;
		}
		setVarInt(5, f);
	}	
	public boolean isBaby(){
		if((getVarInt(5)&0x040) == 0x040)return true;
		return false;
	}
	
	public void setQuiet(boolean tf){
		int f = getVarInt(5);
		if(tf){
			f |= 0x080;
		}else{
			f &= 0xffffff7f;
		}
		setVarInt(5, f);
	}	
	public boolean getQuiet(){
		if((getVarInt(5)&0x080) == 0x080)return true;
		return false;
	}
	
	public void setSinging(boolean tf){
		int f = getVarInt(5);
		if(tf){
			f |= 0x100;
		}else{
			f &= 0xfffffeff;
		}
		setVarInt(5, f);
	}	
	public boolean getSinging(){
		if((getVarInt(5)&0x100) == 0x100)return true;
		return false;
	}
	
	public void setArmsUp(boolean tf){
		int f = getVarInt(5);
		if(tf){
			f |= 0x200;
		}else{
			f &= 0xfffffdff;
		}
		setVarInt(5, f);
	}	
	public boolean getArmsUp(){
		if((getVarInt(5)&0x200) == 0x200)return true;
		return false;
	}
	
	public void setFlying(boolean tf){
		int f = getVarInt(5);
		if(tf){
			f |= 0x0400;
		}else{
			f &= 0xfffffbff;
		}
		if(world != null && !world.isServer && this == DangerZone.player){
			if(f != getVarInt(5))DangerZone.player.server_connection.sendVarIntMessage(entityID, 5, f);
		}
		setVarInt(5, f);
	}	
	public boolean isFlying(){
		if((getVarInt(5)&0x0400) == 0x0400)return true;
		return false;
	}
	
	public void setLadder(boolean tf){
		int f = getVarInt(5);
		if(tf){
			f |= 0x0800;
		}else{
			f &= 0xfffff7ff;
		}
		setVarInt(5, f);
	}	
	public boolean isLadder(){
		if((getVarInt(5)&0x0800) == 0x0800)return true;
		return false;
	}
	
	public void setInvisible(boolean tf){
		int f = getVarInt(5);
		if(tf){
			f |= 0x1000;
		}else{
			f &= 0xffffefff;
		}
		setVarInt(5, f);
	}	
	public boolean isInvisible(){
		if((getVarInt(5)&0x1000) == 0x1000)return true;
		return false;
	}
	
	public void setSwarming(boolean tf){
		int f = getVarInt(5);
		if(tf){
			f |= 0x2000;
		}else{
			f &= 0xffffdfff;
		}
		setVarInt(5, f);
	}	
	public boolean isSwarming(){
		if((getVarInt(5)&0x2000) == 0x2000)return true;
		return false;
	}
	
	public void setExperience(int f){
		setVarInt(6, f);
	}
	
	public int getExperience(){
		return getVarInt(6);
	}
	
	public void setInitialized(int f){
		setVarInt(8, f);
	}
	
	public int getInitialized(){
		return getVarInt(8);
	}
	
	public void setOnFire(int f){
		setVarInt(9, f);
	}
	
	public void doSetOnFire(int f){
		setOnFire(f);
	}
	
	public int getOnFire(){
		return getVarInt(9);
	}
	
	public String getOwnerName(){
		return getVarString(0);
	}
	
	public void setOwnerName(String s){
		setVarString(0, s);
	}
	
	public String getPetName(){
		return getVarString(1);
	}
	
	public void setPetName(String s){
		setVarString(1, s);
	}
	
	public String getMorphName(){
		return getVarString(2);
	}
	
	public void setMorphName(String s){
		setVarString(2, s);
	}
	
	public void Mount(Entity rider){
		//if(getVarInt(10) != 0)return; //already mounted!
		if(rider == null)return;
		
		//if rider is riding anything, get him off it!
		int was_on = rider.getVarInt(11);
		if(was_on != 0){
			if(DangerZone.server != null){
				Entity getoffof = DangerZone.server.entityManager.findEntityByID(was_on);
				if(getoffof != null)getoffof.setVarInt(10, 0);
			}
		}
		
		setVarInt(10, rider.entityID); //link me to rider	
		rider.setVarInt(11, entityID); //and rider back to me
		if(world.isServer){
			rider.setSitting(sit_when_riding);
			if(rider instanceof Player){
				Player pl = (Player)rider;
				ToDoList.onRidden(pl, this);
			}
		}
		
	}
	
	public void unMount(Entity rider){
		setVarInt(10, 0);
		if(rider != null){
			rider.setVarInt(11, 0);
		}
		if(world.isServer && rider != null){
			rider.setSitting(false);
		}		
	}
	
	public boolean isMountedBy(Entity e){
		if(getVarInt(10) == e.entityID)return true;
		return false;
	}
	
	
	//returns the thing we are RIDING ON
	public Entity getRiddenEntity(){
		Entity mounted = null;
		int i = getVarInt(11);
		if(i == 0)return null;
		if(world.isServer){
			mounted = DangerZone.server.entityManager.findEntityByID(i);
		}else{
			mounted = DangerZone.entityManager.findEntityByID(i);
		}
		
		return mounted;
	}
	
	public float getRiderYoffset(){
		return getHeight(); //This is most assuredly wrong... override for your ride able critter!
	}
	
	public float getRiderXZoffset(){
		return 0;
	}
	
	//returns the thing that is RIDING US
	public Entity getRiderEntity(){
		Entity rider = null;
		int i = getVarInt(10);
		if(i == 0)return null;
		if(world.isServer){
			rider = DangerZone.server.entityManager.findEntityByID(i);
		}else{
			rider = DangerZone.entityManager.findEntityByID(i);
		}
		return rider;
	}
	
	public void setForward(boolean tf){
		int f = getVarInt(12);
		if(tf){
			f |= 0x01;
		}else{
			f &= 0xfffffffe;
		}
		if(!world.isServer && this == DangerZone.player){
			if(f != getVarInt(12))DangerZone.player.server_connection.sendVarIntMessage(entityID, 12, f);
		}
		setVarInt(12, f);

	}	
	public boolean getForward(){
		if((getVarInt(12)&0x01) == 0x01)return true;
		return false;
	}
	
	public void setBackward(boolean tf){
		int f = getVarInt(12);
		if(tf){
			f |= 0x02;
		}else{
			f &= 0xfffffffd;
		}
		if(!world.isServer && this == DangerZone.player){
			if(f != getVarInt(12))DangerZone.player.server_connection.sendVarIntMessage(entityID, 12, f);
		}
		setVarInt(12, f);
	}	
	public boolean getBackward(){
		if((getVarInt(12)&0x02) == 0x02)return true;
		return false;
	}
	
	public void setLeft(boolean tf){
		int f = getVarInt(12);
		if(tf){
			f |= 0x04;
		}else{
			f &= 0xfffffffb;
		}
		if(!world.isServer && this == DangerZone.player){
			if(f != getVarInt(12))DangerZone.player.server_connection.sendVarIntMessage(entityID, 12, f);
		}
		setVarInt(12, f);
	}	
	public boolean getLeft(){
		if((getVarInt(12)&0x04) == 0x04)return true;
		return false;
	}
	
	public void setRight(boolean tf){
		int f = getVarInt(12);
		if(tf){
			f |= 0x08;
		}else{
			f &= 0xfffffff7;
		}
		if(!world.isServer && this == DangerZone.player){
			if(f != getVarInt(12))DangerZone.player.server_connection.sendVarIntMessage(entityID, 12, f);
		}
		setVarInt(12, f);
	}	
	public boolean getRight(){
		if((getVarInt(12)&0x08) == 0x08)return true;
		return false;
	}
	
	public void setUp(boolean tf){
		int f = getVarInt(12);
		if(tf){
			f |= 0x10;
		}else{
			f &= 0xffffffef;
		}
		if(!world.isServer && this == DangerZone.player){
			if(f != getVarInt(12))DangerZone.player.server_connection.sendVarIntMessage(entityID, 12, f);
		}
		setVarInt(12, f);
	}	
	public boolean getUp(){
		if((getVarInt(12)&0x10) == 0x10)return true;
		return false;
	}
	
	public void setDown(boolean tf){
		int f = getVarInt(12);
		if(tf){
			f |= 0x20;
		}else{
			f &= 0xffffffdf;
		}
		if(!world.isServer && this == DangerZone.player){
			if(f != getVarInt(12))DangerZone.player.server_connection.sendVarIntMessage(entityID, 12, f);
		}
		setVarInt(12, f);
	}	
	public boolean getDown(){
		if((getVarInt(12)&0x20) == 0x20)return true;
		return false;
	}
	
	//setVarInt(13, x) is used for swarm counter...
	//setVarInt(13, x) is used for swarm counter...
	//setVarInt(13, x) is used for swarm counter...
	
	public int getGameDifficulty(){
		//NEGATIVE = easier, -1 = easy, -2 = baby
		//POSITIVE = harder, 1 = hard, 2 = brutal
		// 0 = normal
		return getVarInt(14);
	}
	
	public void setGameDifficulty(int f){
		setVarInt(14, f);
	}

	public InventoryContainer getInventory(int i){
		if(i >= 0 && i <= 49){
			return getVarInventory(i);
		}
		return null;
	}
	public InventoryContainer getHotbar(int i){
		if(i >= 0 && i <= 9){
			return getVarInventory(i+50);
		}
		return null;
	}
	
	// 0 = helmet, 1 = chestplate, 2 = leggings, 3 = boots
	public InventoryContainer getArmor(int i){
		if(i >= 0 && i <= 3){
			return getVarInventory(i+60);
		}
		return null;
	}	
	public InventoryContainer getMouseBite(){	
		return getVarInventory(64);
	}
	public InventoryContainer getCrafted(){	
		return getVarInventory(65);
	}
	public InventoryContainer getCrafting(int i){
		if(i >= 0 && i <= 8){
			return getVarInventory(i+66);
		}
		return null;
	}	
	
	public void setInventory(int i, InventoryContainer ic){
		if(i >= 0 && i <= 49){
			setVarInventory(i, ic);
		}
	}
	public void setInventoryChanged(int i){
		if(i >= 0 && i <= 49){
			setVarInventoryChanged(i);
		}
	}
	
	public void setHotbar(int i, InventoryContainer ic){
		if(i >= 0 && i <= 9){
			setVarInventory(i+50, ic);
		}
	}
	public void setHotbarChanged(int i){
		if(i >= 0 && i <= 9){
			setVarInventoryChanged(i+50);
		}
	}
	
	public void setArmor(int i, InventoryContainer ic){
		if(i >= 0 && i <= 3){
			setVarInventory(i+60, ic);
		}
	}
	public void setArmorChanged(int i){
		if(i >= 0 && i <= 3){
			setVarInventoryChanged(i+60);
		}
	}	
	public void setMouseBite(InventoryContainer ic){
		setVarInventory(64, ic);
	}
	public void setMouseBiteChanged(){
		setVarInventoryChanged(64);
	}
	public void setCrafted(InventoryContainer ic){
		setVarInventory(65, ic);
	}
	public void setCraftedChanged(){
		setVarInventoryChanged(65);
	}
	public void setCrafting(int i, InventoryContainer ic){
		if(i >= 0 && i <= 8){
			setVarInventory(i+66, ic);
		}
	}
	public void setCraftingChanged(int i){
		if(i >= 0 && i <= 8){
			setVarInventoryChanged(i+66);
		}
	}	
	
	public void setRightButtonDownCount(float count){ 
		setVarFloat(8, count);
	}
	
	public float getRightButtonDownCount(){ 
		return getVarFloat(8);
	}
	
	public void setMagic(float mgc){ 
		setVarFloat(9, mgc);
	}
	
	public float getMagic(){ 
		return getVarFloat(9);
	}
	
	public void setMaxMagic(float mgc){ 
		setVarFloat(10, mgc);
	}
	
	public float getMaxMagic(){ 
		return getVarFloat(10);
	}
	
	public void doAttackFrom(Entity e, /*DamageTypes*/int dt, float ouch){		
	}
	public void doAttackFromNoKnock(/*entity that hit me*/Entity e, /*DamageTypes*/int dt, float pain){	
	}
	public void doAttackFromCustom(/*entity that hit me*/Entity e, /*DamageTypes*/int dt, float pain, boolean addknockback){			
	}
	public void doTargetPrep(Entity e){
		
	}
	
	public boolean takesDamageFrom(/*DamageTypes*/int dt){
		if(dt == DamageTypes.FIRE && isImmuneToFire)return false;
		if(dt == DamageTypes.WATER && canBreateUnderWater)return false;
		return true;
	}
	
	public void doHurtAnimation(){
	}
	
	public void doDeathParticles(){
	}
	
	public float getScale(){
		if(isBaby()){
			return 0.25f;
		}
		return 1f;
	}
	
	public void onDeath(){
		Entity ridden = getRiddenEntity();
		Entity rider = getRiderEntity();
		if(rider != null){
			unMount(rider);
		}
		if(ridden != null){
			ridden.unMount(this);
		}
	}
	
	public void addKnockback(Entity hitter, float xz, float y){
	}
	
	public void addKnockback(float dir, float xz, float y){
	}
	
	public void doArmorDamage(Entity e, /*DamageTypes*/int dt, float pain){
	}
	
	public void jump(){
	}
	
	public float getRightArmAngle(){
		return 0.0f;
	}
	
	public boolean isHurt(){
		return false;
	}
	
	public boolean isMad(){
		return false;
	}
	
	public boolean isDying(){
		return false;
	}
	
	public String getLivingSound(){
		return null;
	}
	
	public float getLivingSoundPitch(){
		if(isBaby())return 2.0f+(DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat())*0.2f;
		return 1.0f+(DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat())*0.2f;
	}
	
	public float getLivingSoundVolume(){
		float voladjust = (float)Math.sqrt(getWidth()*getHeight());
		voladjust /= 1.15f; //adjust so humanoid is roughly 1
		//set some limits
		if(voladjust > 2)voladjust = 2;
		if(voladjust < 0.125f)voladjust = 0.125f;
		return voladjust;
	}
	
	public String getHurtSound(){
		return null;
	}
	
	public String getDeathSound(){
		return null;
	}
	
	public String getAttackSound(){
		return null;
	}
	
	public void doDeathAnimation(){
	}
	
	public float getDeathFactor(){
		return 0;
	}
	
	private boolean areCloseEnough(float f1, float f2){
		if(Math.signum(f1) != Math.signum(f2))return false;
		if(Math.abs(f1) < Math.abs(f2) - (Math.abs(f2)*0.1f))return false;
		if(Math.abs(f1) > Math.abs(f2) + (Math.abs(f2)*0.1f))return false;
		return true;
	}
	
	public void addEffect(Effects ef){

		Effects eft = null;
		for(int i=0;i<effect_list.size();i++){
			eft = effect_list.get(i);
			if(eft.effect == ef.effect){
				if(areCloseEnough(eft.amplitude, ef.amplitude)){
					//add less and less and less as they try to add more...
					float f1, f2;
					f1 = eft.duration; //no div0!
					if(f1 < 1)f1 = 1;
					f2 = ef.duration;
					f1 = (f2/f1)*f2;
					//can't add more than came in
					if(f1 > ef.duration)f1 = ef.duration;
					eft.duration += (int)f1;
					//but always at least the latest duration!
					if(eft.duration - eft.duration_counter < ef.duration)eft.duration = eft.duration_counter + ef.duration;
					//System.out.printf("UPDATE counters %d and %d on %s\n", eft.duration, eft.duration_counter, world.isServer?"server":"client");
					return;
				}
			}
		}			

		//System.out.printf("NEW effect! %d and %d on %s\n", ef.duration, ef.duration_counter, world.isServer?"server":"client");
		//System.out.printf("Ent = %s\n", uniquename);
		effect_list.add(ef);

	}
	
	public void addEffectFromServer(Effects ef){
		if(!world.isServer)return;
		//effect_list.add(ef);
		addEffect(ef);
		if(this instanceof Player){
			Player p = (Player)this;
			p.server_thread.sendNewEffect(ef);
			ToDoList.onAffected(p, ef.effect, ef.duration, ef.amplitude);
		}
	}
	
	public void removeAllEffects(){
		effect_list = new ArrayList <Effects>();
	}
	
	//removes all of one type of effect
	public void removeEffect(int efid){
		boolean found = true;
		while(found){
			found = false;
			for(int i=0;i<effect_list.size();i++){
				if(effect_list.get(i).effect == efid){
					effect_list.remove(i);
					found = true;
					break;
				}
			}			
		}
	}
	
	public float getTotalEffect(int efid){
		float amp = 0;
		for(int i=0;i<effect_list.size();i++){
			if(effect_list.get(i).effect == efid){
				amp += effect_list.get(i).amplitude;
			}
		}					
		return amp;
	}
	
	public void updateEffects(){
		
		//inc counters
		for(int i=0;i<effect_list.size();i++){
			effect_list.get(i).duration_counter++;
		}	
		//remove when timed out
		boolean found = true;
		while(found){
			found = false;
			for(int i=0;i<effect_list.size();i++){
				if(effect_list.get(i).duration_counter >= effect_list.get(i).duration){
					effect_list.remove(i);
					found = true;
					break;
				}
			}			
		}
	}
	
	public void writeSelf(Properties prop, String tag){
		InventoryContainer ic = null;
		
		while(rotation_yaw < 0)rotation_yaw += 360f;
		rotation_yaw %= 360f;
		while(rotation_pitch < 0)rotation_pitch += 360f;
		rotation_pitch %= 360f;
		while(rotation_roll < 0)rotation_roll += 360f;
		rotation_roll %= 360f;
		
		while(rotation_yaw_head < 0)rotation_yaw_head += 360f;
		rotation_yaw_head %= 360f;
		while(rotation_pitch_head < 0)rotation_pitch_head += 360f;
		rotation_pitch_head %= 360f;
		while(rotation_roll_head < 0)rotation_roll_head += 360f;
		rotation_roll_head %= 360f;
		
		prop.setProperty(String.format("%s%s", tag, "posx"), String.format("%f", posx));
		prop.setProperty(String.format("%s%s", tag, "posy"), String.format("%f", posy));
		prop.setProperty(String.format("%s%s", tag, "posz"), String.format("%f", posz));
		prop.setProperty(String.format("%s%s", tag, "dimension"), String.format("%d", dimension));
		prop.setProperty(String.format("%s%s", tag, "rotation_yaw"), String.format("%f", rotation_yaw));
		prop.setProperty(String.format("%s%s", tag, "rotation_pitch"), String.format("%f", rotation_pitch));
		prop.setProperty(String.format("%s%s", tag, "rotation_roll"), String.format("%f", rotation_roll));
		prop.setProperty(String.format("%s%s", tag, "rotation_yaw_motion"), String.format("%f", rotation_yaw_motion));
		prop.setProperty(String.format("%s%s", tag, "rotation_pitch_motion"), String.format("%f", rotation_pitch_motion));
		prop.setProperty(String.format("%s%s", tag, "rotation_roll_motion"), String.format("%f", rotation_roll_motion));
		prop.setProperty(String.format("%s%s", tag, "motionx"), String.format("%f", motionx));
		prop.setProperty(String.format("%s%s", tag, "motiony"), String.format("%f", motiony));
		prop.setProperty(String.format("%s%s", tag, "motionz"), String.format("%f", motionz));
		prop.setProperty(String.format("%s%s", tag, "width"), String.format("%f", width));
		prop.setProperty(String.format("%s%s", tag, "height"), String.format("%f", height));
		prop.setProperty(String.format("%s%s", tag, "maxrenderdist"), String.format("%d", maxrenderdist));
		prop.setProperty(String.format("%s%s", tag, "lifetimeticker"), String.format("%d", lifetimeticker));
		prop.setProperty(String.format("%s%s", tag, "rotation_yaw_head"), String.format("%f", rotation_yaw_head));
		prop.setProperty(String.format("%s%s", tag, "rotation_pitch_head"), String.format("%f", rotation_pitch_head));
		prop.setProperty(String.format("%s%s", tag, "rotation_roll_head"), String.format("%f", rotation_roll_head));
			
		
		for(int i=0;i<maxvars;i++){
			if(entity_ints[i] != 0)prop.setProperty(String.format("%s%s%d", tag, "varint", i), String.format("%d", entity_ints[i]));
			if(entity_floats[i] != 0)prop.setProperty(String.format("%s%s%d", tag, "varfloat", i), String.format("%f", entity_floats[i]));
			if(entity_strings[i] != null)prop.setProperty(String.format("%s%s%d", tag, "varstring", i), entity_strings[i]);
		}
		
		if(has_inventory){
			String s;
			for(int i=0;i<maxinv;i++){
				s = String.format("%sInventory_%d:", tag, i);
				ic = entity_inventory[i];
				if(ic != null && (ic.count <= 0 || (ic.iid == 0 && ic.bid == 0)))ic = null;
				if(ic != null){					
					prop.setProperty(String.format("%s%s", s, "data"), "valid");				
					ic.writeSelf(prop, s);
				}else{
					//Because it's Java, and there is no DELETE property...
					prop.setProperty(String.format("%s%s", s, "data"), "null");
				}
			}
		}
		int listlen = 0;
		if(effect_list != null)listlen = effect_list.size();
		prop.setProperty(String.format("%s%s", tag, "effectlistlen"), String.format("%d", listlen));
		
		if(effect_list != null && listlen > 0){
			Effects ef = null;
			for(int i=0;i<listlen;i++){
				ef = effect_list.get(i);
				prop.setProperty(String.format("%s%s%d", tag, "effect", i), String.format("%d", ef.effect));
				prop.setProperty(String.format("%s%s%d_duration", tag, "effect", i), String.format("%d", ef.duration));
				prop.setProperty(String.format("%s%s%d_counter", tag, "effect", i), String.format("%d", ef.duration_counter));
				prop.setProperty(String.format("%s%s%d_amp", tag, "effect", i), String.format("%f", ef.amplitude));
				//System.out.printf("wrote effect %d\n", ef.effect);
			}
		}
		
	}
	
	public void readSelf(Properties prop, String tag){

		posx = Utils.getPropertyDouble(prop, String.format("%s%s", tag, "posx"), 0, Integer.MAX_VALUE, 10000f); //yes, INTEGER.MAX_VALUE
		posy = Utils.getPropertyDouble(prop, String.format("%s%s", tag, "posy"), 0, 256, 75f);
		posz = Utils.getPropertyDouble(prop, String.format("%s%s", tag, "posz"), 0, Integer.MAX_VALUE, 10000f);
		dimension = Utils.getPropertyInt(prop, String.format("%s%s", tag, "dimension"), 1, Dimensions.dimensionsMAX, 1);
		rotation_yaw = Utils.getPropertyFloat(prop, String.format("%s%s", tag, "rotation_yaw"), 0, 360, 0f);
		rotation_pitch = Utils.getPropertyFloat(prop, String.format("%s%s", tag, "rotation_pitch"), 0, 360, 00f);
		rotation_roll = Utils.getPropertyFloat(prop, String.format("%s%s", tag, "rotation_roll"), 0, 360, 00f);
		rotation_yaw_motion = Utils.getPropertyFloat(prop, String.format("%s%s", tag, "rotation_yaw_motion"), -300, 300, 0f);
		rotation_pitch_motion = Utils.getPropertyFloat(prop, String.format("%s%s", tag, "rotation_pitch_motion"), -300, 300, 0f);
		rotation_roll_motion = Utils.getPropertyFloat(prop, String.format("%s%s", tag, "rotation_roll_motion"), -300, 300, 0f);
		motionx = Utils.getPropertyFloat(prop, String.format("%s%s", tag, "motionx"), -10, 10, 0f);
		motiony = Utils.getPropertyFloat(prop, String.format("%s%s", tag, "motiony"), -10, 10, 0f);
		motionz = Utils.getPropertyFloat(prop, String.format("%s%s", tag, "motionz"), -10, 10, 0f);
		width = Utils.getPropertyFloat(prop, String.format("%s%s", tag, "width"), 0.1f, 30f, width);
		height = Utils.getPropertyFloat(prop, String.format("%s%s", tag, "height"), 0.1f, 50f, height);
		maxrenderdist = Utils.getPropertyInt(prop, String.format("%s%s", tag, "maxrenderdist"), 2, 512, 16*DangerZone.renderdistance);
		lifetimeticker = Utils.getPropertyLong(prop, String.format("%s%s", tag, "lifetimeticker"), 0, Long.MAX_VALUE, 0);
		rotation_yaw_head = Utils.getPropertyFloat(prop, String.format("%s%s", tag, "rotation_yaw_head"), 0, 360, 0f);
		rotation_pitch_head = Utils.getPropertyFloat(prop, String.format("%s%s", tag, "rotation_pitch_head"), 0, 360, 00f);
		rotation_roll_head = Utils.getPropertyFloat(prop, String.format("%s%s", tag, "rotation_roll_head"), 0, 360, 00f);
		
		
		for(int i=0;i<maxvars;i++){
			entity_ints[i] = Utils.getPropertyInt(prop, String.format("%s%s%d", tag, "varint", i), Integer.MIN_VALUE, Integer.MAX_VALUE, entity_ints[i]);
			if(entity_ints[i] != 0){
				changed = 1;
				changes[i] |= 0x01;
			}
			entity_floats[i] = Utils.getPropertyFloat(prop, String.format("%s%s%d", tag, "varfloat", i), -Float.MAX_VALUE, Float.MAX_VALUE, entity_floats[i]);
			if(entity_floats[i] != 0){
				changed = 1;
				changes[i] |= 0x02;
			}
			entity_strings[i] = Utils.getPropertyString(prop, String.format("%s%s%d", tag, "varstring", i), entity_strings[i]);	
			if(entity_strings[i] != null){
				changed = 1;
				changes[i] |= 0x04;
			}
		}
				
		if(has_inventory){
			String s;
			String n;
			for(int i=0;i<maxinv;i++){
				entity_inventory[i] = null;
				s = String.format("%sInventory_%d:", tag, i);
				n = prop.getProperty(String.format("%s%s", s, "data"));
				if(n != null){
					if(!n.equals("null")){
						entity_inventory[i] = new InventoryContainer();
						entity_inventory[i].readSelf(prop, s);
						//if(entity_inventory[i].count == 0)entity_inventory[i] = null; //invalid!
						entity_inventory[i] = entity_inventory[i].validate();
					}
				}		
			}
		}
		
		int listlen = 0;
		listlen = Utils.getPropertyInt(prop, String.format("%s%s", tag, "effectlistlen"), 0, 100, 0);
		if(listlen > 0){
			Effects ef = null;
			int efid = 0;
			for(int i=0;i<listlen;i++){
				efid = Utils.getPropertyInt(prop, String.format("%s%s%d", tag, "effect", i), 1, Integer.MAX_VALUE, 0);
				ef = new Effects();
				ef.effect = efid;
				ef.duration = Utils.getPropertyInt(prop, String.format("%s%s%d_duration", tag, "effect", i), 1, Integer.MAX_VALUE, 1);
				ef.duration_counter = Utils.getPropertyInt(prop, String.format("%s%s%d_counter", tag, "effect", i), 1, Integer.MAX_VALUE, 1);
				ef.amplitude = Utils.getPropertyFloat(prop, String.format("%s%s%d_amp", tag, "effect", i), Float.MIN_VALUE, Float.MAX_VALUE, 1);
				effect_list.add(ef);
				//System.out.printf("read effect %d\n", ef.effect);
			}
		}

	}
	
	public void doEntityCollisions(float deltaT){		
	}
	
	/* only called on server*/
	public void doEntityAction(float deltaT){		
	}
	
	/*
	 * Here is where you should drop all your stuff
	 * Called server side.
	 */
	public void doDeathDrops(){
		if(has_inventory){
			InventoryContainer ic;
			float mdst = getHeight()*2;
			if(mdst > 10)mdst = 10;
			for(int i=0;i<maxinv;i++){
				ic = getVarInventory(i);			
				if(ic != null){									
					Utils.doDropRand(world, ic, mdst, dimension, posx, posy, posz); //multi-item drop
					setVarInventory(i, null);	
				}
			}
		}		
	}
	
	/*
	 * Adds motion, tidys up, sends update if necessary
	 */
	public void update(float deltaT){
		long too_long;
		int different = 0;
		//Server updates entities (motion, collisions, actions) 10 times a second.
		//Client updates entities (motion only) 60 times a second.
		//Scale the difference so we are at least close!
		float rate = DangerZone.entityupdaterate;
		rate /= DangerZone.serverentityupdaterate;
		//rate *= 0.75f;
		
		lifetimeticker++; //Mostly just for renderers, but can be used for other things too. Different on client and server!
		if(deltaT > 1.5f)lifetimeticker += (int)((deltaT+0.5f)-1);

		//because we got blown into the low 20,000's one time... oops!
		
		if(posy > 512){
			if((world.isServer && !(this instanceof Player)) || (!world.isServer && this == DangerZone.player)){
				motiony -= 0.25f;
			}
			//System.out.printf("High up! %f,  %f\n", posy, motiony);
		}
		
		if(posy > 1024){
			if((world.isServer && !(this instanceof Player)) || (!world.isServer && this == DangerZone.player)){
				if(motiony > 0)motiony = -motiony;
			}
			//System.out.printf("High up! %f,  %f\n", posy, motiony);
		}
		
		if(world.isServer){
			if(posy < -64)deadflag = true;
			if(posx < 0 || posz < 0)deadflag = true;
			if(posx > Integer.MAX_VALUE || posz > Integer.MAX_VALUE)deadflag = true;
		}
		
		if(!world.isServer && effect_list != null && world.rand.nextBoolean()){ //client!
			for(int i=0;i<effect_list.size();i++){
				int k = effect_list.get(i).effect;
				switch(k){
				case Effects.SPEED:
					Utils.spawnParticles(world, "DangerZone:ParticleSpeed", 1, dimension, 
							posx + (world.rand.nextFloat()-world.rand.nextFloat())*getWidth()/2, 
							posy+world.rand.nextFloat()*getHeight(), 
							posz + (world.rand.nextFloat()-world.rand.nextFloat())*getWidth()/2, 
							false);
					break;
				case Effects.STRENGTH:
					Utils.spawnParticles(world, "DangerZone:ParticleStrength", 1, dimension, 
							posx + (world.rand.nextFloat()-world.rand.nextFloat())*getWidth()/2, 
							posy+world.rand.nextFloat()*getHeight(), 
							posz + (world.rand.nextFloat()-world.rand.nextFloat())*getWidth()/2, 
							false);
					break;
				case Effects.WEAKNESS:
					Utils.spawnParticles(world, "DangerZone:ParticleWeakness", 1, dimension, 
							posx + (world.rand.nextFloat()-world.rand.nextFloat())*getWidth()/2, 
							posy+world.rand.nextFloat()*getHeight(), 
							posz + (world.rand.nextFloat()-world.rand.nextFloat())*getWidth()/2, 
							false);
					break;
				case Effects.REGENERATION:
					Utils.spawnParticles(world, "DangerZone:ParticleRegeneration", 1, dimension, 
							posx + (world.rand.nextFloat()-world.rand.nextFloat())*getWidth()/2, 
							posy+world.rand.nextFloat()*getHeight(), 
							posz + (world.rand.nextFloat()-world.rand.nextFloat())*getWidth()/2, 
							false);
					break;
				case Effects.POISON:
					Utils.spawnParticles(world, "DangerZone:ParticlePoison", 1, dimension, 
							posx + (world.rand.nextFloat()-world.rand.nextFloat())*getWidth()/2, 
							posy+world.rand.nextFloat()*getHeight(), 
							posz + (world.rand.nextFloat()-world.rand.nextFloat())*getWidth()/2, 
							false);
					break;
				case Effects.CONFUSION:
					Utils.spawnParticles(world, "DangerZone:ParticleConfusion", 1, dimension, 
							posx + (world.rand.nextFloat()-world.rand.nextFloat())*getWidth()/2, 
							posy+world.rand.nextFloat()*getHeight(), 
							posz + (world.rand.nextFloat()-world.rand.nextFloat())*getWidth()/2, 
							false);
					break;
				case Effects.MORPH:
					Utils.spawnParticles(world, "DangerZone:ParticleMorph", 1, dimension, 
							posx + (world.rand.nextFloat()-world.rand.nextFloat())*getWidth()/2, 
							posy+world.rand.nextFloat()*getHeight(), 
							posz + (world.rand.nextFloat()-world.rand.nextFloat())*getWidth()/2, 
							false);
					break;
				default:
					Utils.spawnParticles(world, "DangerZone:ParticleSlowness", 1, dimension, 
							posx + (world.rand.nextFloat()-world.rand.nextFloat())*getWidth()/2, 
							posy+world.rand.nextFloat()*getHeight(), 
							posz + (world.rand.nextFloat()-world.rand.nextFloat())*getWidth()/2, 
							false);
					break;
				}				
			}
		}
		
		if(!getOnGround() && !getInLiquid() && getRiddenEntity() == null && getRiderEntity() == null && !(this instanceof Player) && !isFlying()){
			movement_friction = false;
		}else{
			movement_friction = true;
		}
		
		
		//Standard movement processing
		if(!world.isServer){			
			if(this instanceof Player){
				Player p = (Player)this;
				if(p.getGameMode() == GameModes.GHOST || p.getGameMode() == GameModes.LIMBO){
					motiony *= (1.0f-(0.15f*deltaT*rate));
				}
			}else{
				//some creatures may use this
				prevdimension = dimension;
				prevposy = posy;
				prevposx = posx;
				prevposz = posz;
				prevrotation_yaw = rotation_yaw;
				prevrotation_pitch = rotation_pitch;
				prevrotation_roll = rotation_roll;
				prevrotation_yaw_head = rotation_yaw_head;
				prevrotation_pitch_head = rotation_pitch_head;
				prevrotation_roll_head = rotation_roll_head;
			}
			
			if(movement_friction){
				float mf = 0.35f*deltaT*rate;
				if(mf > 0.85f)mf = 0.85f;				
				motionx *= (1.0f-mf);
				motionz *= (1.0f-mf);
				mf = 0.05f*deltaT*rate;
				if(mf > 0.85f)mf = 0.85f;
				motiony *= (1.0f-mf);
				
				mf = 0.25f*deltaT*rate;
				if(mf > 0.85f)mf = 0.85f;
				rotation_pitch_motion *= (1.0f-mf);
				rotation_yaw_motion *= (1.0f-mf);
				rotation_roll_motion *= (1.0f-mf);
			}
			
			posy += motiony*deltaT*rate;
			posx += motionx*deltaT*rate;
			posz += motionz*deltaT*rate;
			rotation_pitch += rotation_pitch_motion*deltaT*rate;
			if(this instanceof Player){
				rotation_yaw_head += rotation_yaw_motion*deltaT*rate;
			}else{
				rotation_yaw += rotation_yaw_motion*deltaT*rate;
			}
			rotation_roll += rotation_roll_motion*deltaT*rate;
						
			display_posx = posx;
			display_posy = posy;
			display_posz = posz;
			display_rotation_pitch = rotation_pitch;
			display_rotation_yaw = rotation_yaw;
			display_rotation_roll = rotation_roll;
			
		}else{
			
			
			if(movement_friction){
				float mf = 0.35f*deltaT;
				if(mf > 0.85f)mf = 0.85f;				
				motionx *= (1.0f-mf);
				motionz *= (1.0f-mf);
				mf = 0.05f*deltaT;
				if(mf > 0.85f)mf = 0.85f;
				motiony *= (1.0f-mf);
				
				mf = 0.25f*deltaT;
				if(mf > 0.85f)mf = 0.85f;
				rotation_pitch_motion *= (1.0f-mf);
				rotation_yaw_motion *= (1.0f-mf);
				rotation_roll_motion *= (1.0f-mf);
			}
			
			posy += motiony*deltaT;
			posx += motionx*deltaT;
			posz += motionz*deltaT;
			rotation_pitch += rotation_pitch_motion*deltaT;
			rotation_yaw += rotation_yaw_motion*deltaT;
			rotation_roll += rotation_roll_motion*deltaT;
			
		}
		
		while(rotation_yaw < 0)rotation_yaw += 360f;
		rotation_yaw %= 360f;
		while(rotation_pitch < 0)rotation_pitch += 360f;
		rotation_pitch %= 360f;
		while(rotation_roll < 0)rotation_roll += 360f;
		rotation_roll %= 360f;	
		
		while(rotation_yaw_head < 0)rotation_yaw_head += 360f;
		rotation_yaw_head %= 360f;
		while(rotation_pitch_head < 0)rotation_pitch_head += 360f;
		rotation_pitch_head %= 360f;
		while(rotation_roll_head < 0)rotation_roll_head += 360f;
		rotation_roll_head %= 360f;	
				
		
		//Try to make sure rider and mount are in sync...
		Entity e = getRiddenEntity();
		if(e != null){
			motionx = e.motionx;
			motiony = e.motiony;
			motionz = e.motionz;
			posx =  (e.posx+(Math.sin(Math.toRadians(e.rotation_yaw))*e.getRiderXZoffset()));
			posy = e.posy+e.getRiderYoffset();
			posz =  (e.posz+(Math.cos(Math.toRadians(e.rotation_yaw))*e.getRiderXZoffset()));	
			display_posx = posx;
			display_posy = posy;
			display_posz = posz;
		}
		e = getRiderEntity();
		if(e != null){
			e.motionx = motionx;
			e.motiony = motiony;
			e.motionz = motionz;
			e.posx =  (posx+(Math.sin(Math.toRadians(rotation_yaw))*getRiderXZoffset()));
			e.posy = posy+getRiderYoffset();
			e.posz =  (posz+(Math.cos(Math.toRadians(rotation_yaw))*getRiderXZoffset()));	
			e.display_posx = e.posx;
			e.display_posy = e.posy;
			e.display_posz = e.posz;
		}
				
		if(!world.isServer && this != DangerZone.player)return; //Only player on Client sends packets.
		
		different = 0;
		//See if we should send a position/rotation update to someone...
		currtime = System.currentTimeMillis();
		too_long = currtime - lasttime;	
		if(too_long >= maxidleupdate){
			different++; //At least every few seconds, regardless.
		}else{
			if(!world.isServer){ //throttle player updates!
				if(too_long < 90)return; //keep it around 10x per second
			}
		}

		if(changed != 0)different++;			
		if(prevdimension != dimension || prevposx != posx || prevposy != posy || prevposz != posz)different++;
		if(prevrotation_yaw != rotation_yaw || prevrotation_pitch != rotation_pitch || prevrotation_roll != rotation_roll)different++;
		if(prevrotation_yaw_head != rotation_yaw_head || prevrotation_pitch_head != rotation_pitch_head || prevrotation_roll_head != rotation_roll_head)different++;
		if(different == 0){ //keep sending for a few more...
			diffticker++;
			if(diffticker < 3){
				different++;
			}
		}else{
			diffticker = 0;
		}

		if(different != 0){
			lasttime = currtime;
			prevdimension = dimension;
			prevposy = posy;
			prevposx = posx;
			prevposz = posz;
			prevrotation_yaw = rotation_yaw;
			prevrotation_pitch = rotation_pitch;
			prevrotation_roll = rotation_roll;
			prevrotation_yaw_head = rotation_yaw_head;
			prevrotation_pitch_head = rotation_pitch_head;
			prevrotation_roll_head = rotation_roll_head;	
			
			if(world.isServer){
				DangerZone.server.sendEntityUpdateToAll(this, false);
			}else{
				DangerZone.server_connection.sendPlayerEntityUpdate(this); //Send MY player update to server
			}
			
		}			
		
	}
	
	//The player is morphed as this. Callback from the player update routine. Most need do nothing.
	public void player_morph_update(float deltaT, Player pl){
	
	}
	
	public float getAdjustedFallDamage(float damage){
		if(!takesFallDamage)return 0;
		return damage;
	}
	
	public double getDistanceFromEntity(Entity p){
		double d1, d2, d3;
		d1 = p.posx - posx;
		d2 = p.posy - posy;
		d3 = p.posz - posz;
		if(p.dimension != dimension)return 9999.0f;
		return Math.sqrt((d1*d1)+(d2*d2)+(d3*d3));
	}
	
	public double getDistanceFromEntityCenter(Entity p){
		double d1, d2, d3;
		if(p.dimension != dimension)return 9999.0f;
		d1 = p.posx - posx;
		d2 = (p.posy+p.getHeight()/2) - (posy+getHeight()/2);
		d3 = p.posz - posz;
		return Math.sqrt((d1*d1)+(d2*d2)+(d3*d3));
	}
	
	public double getHorizontalDistanceFromEntity(Entity p){
		double d1, d3;
		if(p.dimension != dimension)return 9999.0f;
		d1 = p.posx - posx;
		d3 = p.posz - posz;
		return Math.sqrt((d1*d1)+(d3*d3));
	}
	
	public double getHorizontalDistanceFromEntity(double x, double z){
		double d1, d3;
		d1 = x - posx;
		d3 = z - posz;
		return Math.sqrt((d1*d1)+(d3*d3));
	}
	
	public double getDistanceFromEntityCenter(double x, double y, double z){
		double d1, d2, d3;
		d1 = x - posx;
		d2 = y - (posy+(getHeight()/2));
		d3 = z - posz;
		return Math.sqrt((d1*d1)+(d2*d2)+(d3*d3));
	}
	
	/* only called by server */
	public boolean rightClickedByPlayer(Player p, InventoryContainer ic){
		return false; //do not decrement inventory
	}
	
	/* only called by server */
	public boolean leftClickedByPlayer(Player p, InventoryContainer ic){
		return true; //continue with attack damage
	}
	
	public void onKill(Entity deadthing){
		//only called on server, when something kills something else!
	}
	
	
	public Texture getTexture(){
		//MAKE SURE YOU USE TextureMapper.getTexture(string) when getting the texture(s) for your entity!
		return texture;
	}
	
	public boolean getCanSpawnHereNow(World w, int dimension, int x, int y, int z){
		//USE "w" if you need world access. entity.world is null in this routine!
		//ONLY called through an inert/inactive entity on the server.
		return true;
	}
	
	public boolean isFoodForMe(int bid, int iid){
		if(isFoodBlock(bid) || isFoodItem(iid))return true;
		return false;
	}
	
	public boolean isBreedingFoodForMe(int bid, int iid){
		return isFoodForMe(bid, iid);
	}
	
	public boolean isFoodBlock(int bid){
		return false;
	}
	
	public boolean isFoodItem(int iid){
		return false;
	}
	
	public void doDistanceAttack(Entity victim){
	}
	
	 public void findBlockFood(int maxdistxz, int maxdisty , int healamount, int eatitdistance){
	 }
	 
	 public void playburp(){
	 }
	 
	 public float getSpinz(){
		 return 0;
	 }
	 
	 public void doFromSpawner(){
	 }


	 public float getLightAtLocation(World w, int d, int x, int y, int z){
		 if(!w.isServer){
			 return WorldRendererUtils.getTotalLightAt(w, d, x, y, z);
		 }else{
			 if(DangerZone.start_client){ //single player maybe?
				 return WorldRendererUtils.getTotalLightAt(DangerZone.world, d, x, y, z);
			 }
			 //TODO - FIXME - this is TOO SLOW for servers. Need another way!
			 ////have to go ask a real client!!! That's where the lightmaps are...
			 //Player p = DangerZone.server.findNearestPlayerToHere(d, x, y, z);
			 //if(p != null){
			 //	 return p.server_thread.doLightingRequest(d, x, y, z);
			 //}
			 //TODO FIXME! lightmap on server too???
			 if(w.isDaytime()){
				 return 0.5f + w.rand.nextFloat()*0.5f; //punt
			 }
			 return w.rand.nextFloat(); //punt			 
		 }
	 }
	 
	 /*
	  * Cheap and dirty canSee() 
	  * 
	  * Scans out one delta block at a time checking for solids that probably block vision of object.
	  */
	 public boolean CanProbablySee(int d, double x, double y, double z, int distinblocks)
	 {
		 double startx;
		 double starty;
		 double startz;
		 double xzoff = getWidth()/2;
		 double cx, cz;
		 double dx;
		 double dy;
		 double dz;
		 int i;
		 int nblks = distinblocks;
		 
		 if(distinblocks < 1)return true;
		 
		 //Calculate approximately where eyes should be
		 cx = posx+(xzoff*Math.cos(Math.toRadians(rotation_yaw)));
		 cz = posz+(xzoff*Math.sin(Math.toRadians(rotation_yaw)));
		 startx = (cx);
		 starty =  (posy+(getHeight()*7/8));
		 startz =  (cz);
		 dx =  ((x-startx)/distinblocks);
		 dy =  ((y-starty)/distinblocks);
		 dz =  ((z-startz)/distinblocks);

		 if(Math.abs(dx) > 1.0){
			 dy = dy/Math.abs(dx);
			 dz = dz/Math.abs(dx);
			 nblks *= Math.abs(dx);
			 if(dx > 1)dx = 1;
			 if(dx < -1)dx = -1;
		 }
		 if(Math.abs(dy) > 1.0){
			 dx = dx/Math.abs(dy);
			 dz = dz/Math.abs(dy);
			 nblks *= Math.abs(dy);
			 if(dy > 1)dy = 1;
			 if(dy < -1)dy = -1;
		 }
		 if(Math.abs(dz) > 1.0){
			 dy = dy/Math.abs(dz);
			 dx = dx/Math.abs(dz);
			 nblks *= Math.abs(dz);
			 if(dz > 1)dz = 1;
			 if(dz < -1)dz = -1;
		 }

		 for(i=0;i<nblks;i++){
			 startx += dx;
			 starty += dy;
			 startz += dz;
			 //TODO FIXME - get blockid and check for clear solids or opaque liquids!
			 if(Blocks.isSolid(world.getblock(d, (int)startx, (int)starty, (int)startz), world, d, (int)startx, (int)starty, (int)startz))return false;
		 } 


		 return true;
	 }
	 
	 public boolean amFacingTarget(Entity e){
		 float cdir = (float) Math.toRadians(rotation_yaw);
		 float tdir = (float) Math.atan2(e.posx - posx, e.posz - posz);	
		 float ddiff = tdir - cdir;
		 while(ddiff>Math.PI)ddiff -= Math.PI*2;
		 while(ddiff<-Math.PI)ddiff += Math.PI*2;
		 if(ddiff > Math.PI*3/4)return false; //a little more than 180 vision range
		 if(ddiff < -Math.PI*3/4)return false;
		 return true;
	 }
	 
	 /*
	  * Cheap and dirty canSee() 
	  * 
	  * Scans out one delta block at a time checking for solids that probably block vision of object.
	  */
	 public boolean CanProbablySeeEntity(Entity e)
	 {
		 double startx;
		 double starty;
		 double startz;
		 double estarty;
		 double xzoff = getWidth()/2;
		 double dx;
		 double dy;
		 double dz;
		 int i;
		 double nblks;

		 //System.out.printf("nblks1 = %f\n", nblks);
		 /*
		 //check direction! Are we even facing the other entity?
		 float cdir = (float) Math.toRadians(rotation_yaw);
		 float tdir = (float) Math.atan2(e.posx - posx, e.posz - posz);	
		 float ddiff = tdir - cdir;
		 while(ddiff>Math.PI)ddiff -= Math.PI*2;
		 while(ddiff<-Math.PI)ddiff += Math.PI*2;
		 if(ddiff > Math.PI*3/4)return false; //a little more than 180 vision range
		 if(ddiff < -Math.PI*3/4)return false;
		 */

		 //Calculate approximately where eyes should be
		 startx =  posx+(xzoff*Math.cos(Math.toRadians(rotation_yaw)));
		 starty =  (posy+(getHeight()*7/8));
		 startz =  posz+(xzoff*Math.sin(Math.toRadians(rotation_yaw)));
		 
		 estarty =  (e.posy+(e.getHeight()*7/8));
		 		 
		 dx =  e.posx-startx;
		 dy =  estarty-starty;
		 dz =  e.posz-startz;
		 
		 nblks = 1;
		 if(nblks < Math.abs(dx))nblks = Math.abs(dx);
		 if(nblks < Math.abs(dy))nblks = Math.abs(dy);
		 if(nblks < Math.abs(dz))nblks = Math.abs(dz);
		 if(nblks < 1)return true;
		 if(nblks < e.getWidth()/2)return true;
		 
		 dx /= nblks;
		 dy /= nblks;
		 dz /= nblks;
		 	
		 for(i=0;i<(int)(nblks-e.getWidth()/2);i++){
			 startx += dx;
			 starty += dy;
			 startz += dz;
			 //TODO FIXME - get blockid and check for clear solids or opaque liquids!
			 if(Blocks.isSolid(world.getblock(dimension, (int)startx, (int)starty, (int)startz), world, dimension, (int)startx, (int)starty, (int)startz)){
				 return false;
			 }
		 } 
		 return true;
	 }
	 
	 public void doInLiquid(float factor){
		 if(getInLiquid()){
			 int bid = world.getblock(dimension, (int)posx, (int)posy, (int)posz);
			 if(Blocks.isLiquid(bid)){
				 int meta = world.getblockmeta(dimension, (int)posx, (int)posy, (int)posz)&0x0f;
				 int meta2, bid2;
				 int i, j;
				 
				 //move towards low
				 for(i=-1;i<=1;i++){
					 for(j=-1;j<=1;j++){
						 bid2 = world.getblock(dimension, (int)posx+i, (int)posy, (int)posz+j);
						 if(Blocks.isLiquid(bid2)){
							 meta2 = world.getblockmeta(dimension, (int)posx+i, (int)posy, (int)posz+j)&0x0f;
							 if(meta2 > meta){ //backwards!
								 motionx += 0.025f*i*factor;
								 motionz += 0.025f*j*factor;
							 }
						 }
					 }
				 }
				 //move away from high
				 for(i=-1;i<=1;i++){
					 for(j=-1;j<=1;j++){
						 bid2 = world.getblock(dimension, (int)posx+i, (int)posy, (int)posz+j);
						 if(Blocks.isLiquid(bid2)){
							 meta2 = world.getblockmeta(dimension, (int)posx+i, (int)posy, (int)posz+j)&0x0f;
							 if(meta2 < meta){ //backwards!
								 motionx -= 0.025f*i*factor;
								 motionz -= 0.025f*j*factor;
							 }
						 }
					 }
				 }
			 }
		 }
	 }
	 
	 public void smoothMotion(){
		 
		 //we have anticipated were the entity will be, but still, we need to adjust it a bit more...
		 //make up the difference we were off, based on the frame rate as set by the update that came in...
		 int mydifft = diff_ticker; //capture this so it doesn't change in the middle!!!
		 if(mydifft > 0){
			 float dx = diff_posx/mydifft;
			 posx += dx;
			 diff_posx -= dx;

			 dx = diff_posy/mydifft;
			 posy += dx;
			 diff_posy -= dx;

			 dx = diff_posz/mydifft;
			 posz += dx;
			 diff_posz -= dx;

			 //body
			 dx = diff_rotpitch/mydifft;
			 rotation_pitch += dx;
			 diff_rotpitch -= dx;
			 while(rotation_pitch > 360)rotation_pitch -= 360;
			 while(rotation_pitch < 0)rotation_pitch += 360;

			 dx = diff_rotroll/mydifft;
			 rotation_roll += dx;
			 diff_rotroll -= dx;
			 while(rotation_roll > 360)rotation_roll -= 360;
			 while(rotation_roll < 0)rotation_roll += 360;

			 dx = diff_rotyaw/mydifft;
			 rotation_yaw += dx;
			 diff_rotyaw -= dx;
			 while(rotation_yaw > 360)rotation_yaw -= 360;
			 while(rotation_yaw < 0)rotation_yaw += 360;


			 //head
			 dx = diff_rotpitchh/mydifft;
			 rotation_pitch_head += dx;
			 diff_rotpitchh -= dx;
			 while(rotation_pitch_head > 360)rotation_pitch_head -= 360;
			 while(rotation_pitch_head < 0)rotation_pitch_head += 360;

			 dx = diff_rotyawh/mydifft;
			 rotation_yaw_head += dx;
			 diff_rotyawh -= dx;
			 while(rotation_yaw_head > 360)rotation_yaw_head -= 360;
			 while(rotation_yaw_head < 0)rotation_yaw_head += 360;

			 dx = diff_rotrollh/mydifft;
			 rotation_roll_head += dx;
			 diff_rotrollh -= dx;
			 while(rotation_roll_head > 360)rotation_roll_head -= 360;
			 while(rotation_roll_head < 0)rotation_roll_head += 360;			 

			 diff_ticker--;
		 }
		 	 

	 }
	 
	 public void setMotion(){
		 
		 //we have anticipated were the entity will be, but still, we need to adjust it a bit more...
		 //make up the difference we were off, based on the frame rate as set by the update that came in...

			 posx += diff_posx;
			 diff_posx = 0;
			 posy += diff_posy;
			 diff_posy = 0;
			 posz += diff_posz;
			 diff_posz = 0;

			 //body
			 rotation_pitch += diff_rotpitch;
			 diff_rotpitch = 0;
			 while(rotation_pitch > 360)rotation_pitch -= 360;
			 while(rotation_pitch < 0)rotation_pitch += 360;

			 rotation_roll += diff_rotroll;
			 diff_rotroll = 0;
			 while(rotation_roll > 360)rotation_roll -= 360;
			 while(rotation_roll < 0)rotation_roll += 360;

			 rotation_yaw += diff_rotyaw;
			 diff_rotyaw = 0;
			 while(rotation_yaw > 360)rotation_yaw -= 360;
			 while(rotation_yaw < 0)rotation_yaw += 360;


			 //head
			 rotation_pitch_head += diff_rotpitchh;
			 diff_rotpitchh = 0;
			 while(rotation_pitch_head > 360)rotation_pitch_head -= 360;
			 while(rotation_pitch_head < 0)rotation_pitch_head += 360;

			 rotation_yaw_head += diff_rotyawh;
			 diff_rotyawh = 0;
			 while(rotation_yaw_head > 360)rotation_yaw_head -= 360;
			 while(rotation_yaw_head < 0)rotation_yaw_head += 360;

			 rotation_roll_head += diff_rotrollh;
			 diff_rotrollh = 0;
			 while(rotation_roll_head > 360)rotation_roll_head -= 360;
			 while(rotation_roll_head < 0)rotation_roll_head += 360;			 
		 	 
	 }
	 
	 public float getBrightness(){
		 return 0;
	 }
	 
	 public boolean hasInInventory(int iid){
		 if(!has_inventory)return false;
		 
		 int i;
		 InventoryContainer ic;
		 for(i=0;i<10;i++){
			 ic = getHotbar(i);
			 if(ic != null){
				 if(ic.iid == iid && ic.count > 0)return true;
			 }			 
		 }
		 for(i=0;i<50;i++){
			 ic = getInventory(i);
			 if(ic != null){
				 if(ic.iid == iid && ic.count > 0)return true;
			 }			 
		 }		 
		 return false;
	 }
	 
	 public void decrementInInventory(int iid){
		 if(!has_inventory)return;
		 if(!world.isServer)return;	
		 int i;
		 InventoryContainer ic;
		 for(i=0;i<10;i++){
			 ic = getHotbar(i);
			 if(ic != null){
				 if(ic.iid == iid){
					 ic.count--;
					 if(ic.count <= 0)ic = null;					
					 setHotbar(i, ic);
					 return;
				 }
			 }			 
		 }
		 for(i=0;i<50;i++){
			 ic = getInventory(i);
			 if(ic != null){
				 if(ic.iid == iid){
					 ic.count--;
					 if(ic.count <= 0)ic = null;					 
					 setInventory(i, ic);
					 return;
				 }
			 }			 
		 }		 
	 }
	
	public void lookAtEntity(Entity ent){
			
	}
	
	public void addKeyEvent(int key, boolean updown){
		if(keyevent_list == null)keyevent_list = new ArrayList <PlayerKeyEvent>();
		keyevent_list.add(new PlayerKeyEvent(key, updown));
	}
	
	public PlayerKeyEvent getNextKeyEvent(){
		if(keyevent_list == null)return null;
		if(keyevent_list.size() <= 0)return null;
		return keyevent_list.remove(0);
	}
	
	public void addMouseEvent(int mouse, boolean updown){
		if(mouseevent_list == null)mouseevent_list = new ArrayList <PlayerKeyEvent>();
		mouseevent_list.add(new PlayerKeyEvent(mouse, updown));
	}
	
	public PlayerKeyEvent getNextMouseEvent(){
		if(mouseevent_list == null)return null;
		if(mouseevent_list.size() <= 0)return null;
		return mouseevent_list.remove(0);
	}
	
	public void justCollidedWith(Entity e){
		//this entity just got bumped by the one passed in.
	}

}
