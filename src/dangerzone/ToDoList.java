package dangerzone;
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
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import dangerzone.entities.Entity;
import dangerzone.entities.EntityLiving;

public class ToDoList {
	
	//ALL CALLBACKS ARE SERVER-SIDE ONLY!
	//Player is passed in for convenient access to additional info if needed
	//
	public static final int IGNORED = 0;
	public static final int CRAFTED = 0x1; //called when something is crafted
	public static final int KILLED = 0x2; //called when something is killed
	public static final int SPAWNED = 0x3; //called when something is spawned
	public static final int TAMED = 0x4; //called when something is tamed
	public static final int RIDDEN = 0x5; //called when something is ridden
	public static final int PICKEDUP = 0x6; //when placed in inventory
	public static final int DIMENSION = 0x7; //dimension changed
	public static final int PLACED = 0x8; //block placed
	public static final int SPELLCAST = 0x9; //cast a spell
	public static final int LEVELED = 0x0a; //every 1000 xp
	public static final int EATEN = 0x0b; //ate something
	public static final int ARMORPLACED = 0x0c; //put on a piece of armor
	public static final int BROKEN = 0x0d; //block broken
	public static final int PETFED = 0x0e; //feed a tamed pet
	public static final int AFFECTED = 0x0f; //affect added
	public static final int ITEMRIGHTCLICKEDBLOCK = 0x10; //item right-clicked block

	
	public static  List<ToDoItem> todo;
	
	public ToDoList(){
		todo = new ArrayList<ToDoItem>();
	}
	
	public static void registerToDoItem(ToDoItem tobedone){
		//add work to list!
		
		if(find(tobedone.uniquename) != null){
			System.out.printf("todo name (%s) not unique - NOT ADDED\n", tobedone.uniquename); //for developers!
			return;
		}
		todo.add(tobedone);				
	}
	
	public static ToDoItem find(String i1){
		if(i1 == null)return null;
		if(todo == null)return null;
		if(todo.isEmpty())return null;
		Iterator<ToDoItem> ii = todo.iterator();
		ToDoItem r;
		while(ii.hasNext()){
			r = ii.next();
			//System.out.printf("compare (%s) to (%s)\n", i1, r.uniquename);
			if(i1.equals(r.uniquename))return r;			
		}		
		return null;
	}
	
	public static void onCrafted(Player pl, InventoryContainer ic){
		if(todo == null)return;
		if(todo.isEmpty())return;
		Iterator<ToDoItem> ii = todo.iterator();
		ToDoItem r;
		while(ii.hasNext()){
			r = ii.next();
			if(r.type == CRAFTED)r.onCrafted(pl, ic);			
		}		
	}
	
	public static void onKilled(Player pl, Entity ent){
		if(todo == null)return;
		if(todo.isEmpty())return;
		Iterator<ToDoItem> ii = todo.iterator();
		ToDoItem r;
		while(ii.hasNext()){
			r = ii.next();
			if(r.type == KILLED)r.onKilled(pl, ent);			
		}		
	}
	
	public static void onSpawned(Player pl, Entity ent){
		if(todo == null)return;
		if(todo.isEmpty())return;
		Iterator<ToDoItem> ii = todo.iterator();
		ToDoItem r;
		while(ii.hasNext()){
			r = ii.next();
			if(r.type == SPAWNED)r.onSpawned(pl, ent);			
		}		
	}
	
	public static void onTamed(Player pl, EntityLiving ent){
		if(todo == null)return;
		if(todo.isEmpty())return;
		Iterator<ToDoItem> ii = todo.iterator();
		ToDoItem r;
		while(ii.hasNext()){
			r = ii.next();
			if(r.type == TAMED)r.onTamed(pl, ent);			
		}		
	}
	
	public static void onRidden(Player pl, Entity ent){
		if(todo == null)return;
		if(todo.isEmpty())return;
		Iterator<ToDoItem> ii = todo.iterator();
		ToDoItem r;
		while(ii.hasNext()){
			r = ii.next();
			if(r.type == SPAWNED)r.onRidden(pl, ent);			
		}		
	}
	
	public static void onPickedUp(Player pl, InventoryContainer ic){
		if(todo == null)return;
		if(todo.isEmpty())return;
		Iterator<ToDoItem> ii = todo.iterator();
		ToDoItem r;
		while(ii.hasNext()){
			r = ii.next();
			if(r.type == PICKEDUP)r.onPickedUp(pl, ic);			
		}	
	}
	
	public static void onDimension(Player pl, int oldd, int newd){
		if(todo == null)return;
		if(todo.isEmpty())return;
		Iterator<ToDoItem> ii = todo.iterator();
		ToDoItem r;
		while(ii.hasNext()){
			r = ii.next();
			if(r.type == DIMENSION)r.onDimension(pl, oldd, newd);			
		}	
	}
	
	public static void onPlaced(Player pl, int dimension, int x, int y, int z, int bid){
		if(todo == null)return;
		if(todo.isEmpty())return;
		Iterator<ToDoItem> ii = todo.iterator();
		ToDoItem r;
		while(ii.hasNext()){
			r = ii.next();
			if(r.type == PLACED)r.onPlaced(pl, dimension, x, y, z, bid);			
		}	
	}
	
	public static void onBroken(Player pl, int dimension, int x, int y, int z, int bid){
		if(todo == null)return;
		if(todo.isEmpty())return;
		Iterator<ToDoItem> ii = todo.iterator();
		ToDoItem r;
		while(ii.hasNext()){
			r = ii.next();
			if(r.type == PLACED)r.onBroken(pl, dimension, x, y, z, bid);			
		}	
	}
	
	public static void onSpellCast(Player pl, int spellid, float spellstrength){
		if(todo == null)return;
		if(todo.isEmpty())return;
		Iterator<ToDoItem> ii = todo.iterator();
		ToDoItem r;
		while(ii.hasNext()){
			r = ii.next();
			if(r.type == SPELLCAST)r.onSpellCast(pl, spellid, spellstrength);			
		}	
	}
	
	public static void onLeveled(Player pl, int xp){
		if(todo == null)return;
		if(todo.isEmpty())return;
		Iterator<ToDoItem> ii = todo.iterator();
		ToDoItem r;
		while(ii.hasNext()){
			r = ii.next();
			if(r.type == LEVELED)r.onLeveled(pl, xp);			
		}	
	}
	
	public static void onEaten(Player pl, InventoryContainer ic){
		if(todo == null)return;
		if(todo.isEmpty())return;
		Iterator<ToDoItem> ii = todo.iterator();
		ToDoItem r;
		while(ii.hasNext()){
			r = ii.next();
			if(r.type == EATEN)r.onEaten(pl, ic);			
		}	
	}
	
	public static void onArmorPlaced(Player pl, InventoryContainer ic, int slot){
		if(todo == null)return;
		if(todo.isEmpty())return;
		Iterator<ToDoItem> ii = todo.iterator();
		ToDoItem r;
		while(ii.hasNext()){
			r = ii.next();
			if(r.type == ARMORPLACED)r.onArmorPlaced(pl, ic, slot);			
		}	
	}
	
	public static void onPetFed(Player pl, Entity ent, InventoryContainer ic){
		if(todo == null)return;
		if(todo.isEmpty())return;
		Iterator<ToDoItem> ii = todo.iterator();
		ToDoItem r;
		while(ii.hasNext()){
			r = ii.next();
			if(r.type == PETFED)r.onPetFed(pl, ent, ic);			
		}	
	}
	
	public static void onAffected(Player pl, int type, int duration, float amp){
		if(todo == null)return;
		if(todo.isEmpty())return;
		Iterator<ToDoItem> ii = todo.iterator();
		ToDoItem r;
		while(ii.hasNext()){
			r = ii.next();
			if(r.type == AFFECTED)r.onAffected(pl, type, duration, amp);			
		}	
	}
	
	public static void itemRightClickedBlock(Player pl, InventoryContainer ic, int dimension, int focusx, int focusy, int focusz){
		if(todo == null)return;
		if(todo.isEmpty())return;
		Iterator<ToDoItem> ii = todo.iterator();
		ToDoItem r;
		while(ii.hasNext()){
			r = ii.next();
			if(r.type == ITEMRIGHTCLICKEDBLOCK)r.itemRightClickedBlock( pl,  ic,  dimension,  focusx,  focusy,  focusz);	
		}	
	}
	
	public static void writeSelf(Properties prop, String tag){	
		if(todo == null)return;
		if(todo.isEmpty())return;
		Iterator<ToDoItem> ii = todo.iterator();
		ToDoItem r;
		while(ii.hasNext()){
			r = ii.next();
			r.writeSelf(prop, tag+r.uniquename);			
		}	
	}
	
	public static void readSelf(Properties prop, String tag){	
		if(todo == null)return;
		if(todo.isEmpty())return;
		Iterator<ToDoItem> ii = todo.iterator();
		ToDoItem r;
		while(ii.hasNext()){
			r = ii.next();
			r.readSelf(prop, tag+r.uniquename);	
		}	
	}

}
