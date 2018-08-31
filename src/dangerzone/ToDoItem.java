package dangerzone;

import java.util.Properties;

import dangerzone.entities.Entity;
import dangerzone.entities.EntityLiving;


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
public class ToDoItem {

	public String title; //Short Displayed name - null = HIDDEN!
	public String helptext; //helpful info on how to get this acheivement
	public String uniquename; //general uniquename so it can be looked up and saved/restored!
	public int type; //when/where/how this gets called!
	public boolean truefalse = false;

	
	
	public ToDoItem(){
		title = null;
		helptext = null;
		uniquename = null;
		type = 0;
		init();
	}
	
	public ToDoItem(String inuniquename, String intitle, String inhelptext, int intype){
		this();
		uniquename = inuniquename;
		title = intitle;
		helptext = inhelptext;
		type = intype;
		init();
	}
	
	public void init(){
		truefalse = false;
	}
	
	public String gettitle(){
		//you can make things hidden until certain other todo items become true!
		return title;
	}
	
	public String gethelptext(){
		//dynamic help text, like "3 of 5 complete"...
		return helptext;
	}
	
	//fill this out if type == CRAFTED
	//this is your event callback!
	public void onCrafted(Player pl, InventoryContainer ic){
		//if thingy in inventory == my expected thingy, set truefalse to true!
	}
	
	//fill this out if type == KILLED
	//this is your event callback!
	public void onKilled(Player pl, Entity ent){
		//if ent type instanceof my expected ent, set truefalse to true!
	}
	
	//fill this out if type == SPAWNED
	//this is your event callback!
	public void onSpawned(Player pl, Entity ent){
		//if ent type instanceof my expected ent, set truefalse to true!
	}
	
	public void onTamed(Player pl, EntityLiving ent){
	}
	
	public void onRidden(Player pl, Entity ent){
	}
	
	public void onPickedUp(Player pl, InventoryContainer ic){
	}
	
	public void onDimension(Player pl, int oldd, int newd){
	}
	
	public void onPlaced(Player pl, int dimension, int x, int y, int z, int bid){
	}
	
	public void onBroken(Player pl, int dimension, int x, int y, int z, int bid){
	}
	
	public void onSpellCast(Player pl, int spellid, float spellstrength){
	}
	
	public void onLeveled(Player pl, int xp){
	}
	
	public void onEaten(Player pl, InventoryContainer ic){
	}
	
	public void onPetFed(Player pl, Entity ent, InventoryContainer ic){
	}
	
	public void onArmorPlaced(Player pl, InventoryContainer ic, int slot){
	}
	
	public void onAffected(Player pl, int type, int duration, float amp){
	}
	
	public void itemRightClickedBlock(Player pl, InventoryContainer ic, int dimension, int focusx, int focusy, int focusz){
	}
	
	public boolean isTrue(){
		//You can look up other things here to determine whether or not to return true!
		return truefalse;
	}
	
	public void writeSelf(Properties prop, String tag){	
		prop.setProperty(String.format("%s%s", tag, "TrueFalse"), String.format("%s", truefalse?"true":"false"));
	}
	
	public void readSelf(Properties prop, String tag){	
		truefalse = Utils.getPropertyBoolean(prop, String.format("%s%s", tag, "TrueFalse"), false); //default is false!
	}
	
	public void notify(Player pl){
		pl.server_thread.sendChatToPlayer("Achievement: " + title);
		pl.world.playSound("DangerZone:ding", pl.dimension, pl.posx, pl.posy, pl.posz, 0.55f, 1.0f);
		Utils.spawnExperience(111, pl.world, pl.dimension, pl.posx, pl.posy, pl.posz, false);
	}
	

}
