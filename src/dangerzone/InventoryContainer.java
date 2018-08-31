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
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

import org.newdawn.slick.opengl.Texture;

import dangerzone.blocks.Block;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entity;
import dangerzone.items.Item;
import dangerzone.items.ItemArmor;
import dangerzone.items.Items;



public class InventoryContainer {
	public int count;
	public int bid;
	public int iid;
	public int currentuses = 0;
	public List<ItemAttribute> attributes;
	public String icmeta = null; //for for whatever you like. Saved and Restored. Rarely used by anyone else.
	public InventoryContainer moreInventory[]; //yes, it's now recursive, max depth 5, max size 200... still waaaaaaaay more than can/should ever be used...
	
	public InventoryContainer(){
		count = bid = iid = 0;
		attributes = null;
		moreInventory = null;
	}
	
	public InventoryContainer(String uniquename, int incount){
		bid = Blocks.findByName(uniquename);
		if(bid == 0){
			iid = Items.findByName(uniquename);
		}
		count = 0;
		if(bid != 0 || iid != 0){
			count = incount;
		}
		attributes = null;
		moreInventory = null;
	}
	
	public InventoryContainer(int inbid, int iniid, int incount, int incurrentuses){
		bid = inbid;
		iid = iniid;
		count = incount;
		currentuses = incurrentuses;
		attributes = null;
		moreInventory = null;
	}
	
	public InventoryContainer(Object inobj, int incount, int incurrentuses){
		iid = bid = 0;
		if(inobj instanceof Item)iid = ((Item) inobj).itemID;
		if(inobj instanceof Block)bid = ((Block) inobj).blockID;
		count = incount;
		currentuses = incurrentuses;
		attributes = null;
		moreInventory = null;
	}
	
	public InventoryContainer(int inbid, int iniid, int incount, int incurrentuses, List<ItemAttribute>inlist){
		bid = inbid;
		iid = iniid;
		count = incount;
		currentuses = incurrentuses;

		if(inlist != null){
			//copy!
			attributes = new ArrayList<ItemAttribute>();
			ListIterator<ItemAttribute> li = inlist.listIterator();
			ItemAttribute ia;
			ItemAttribute ib;
			while(li.hasNext()){
				ia = li.next();
				ib = new ItemAttribute(ia.type, ia.value);
				attributes.add(ib);
			}		
		}else{
			attributes = null;
		}
		moreInventory = null;
	}
	
	public InventoryContainer(int inbid, int iniid, int incount, int incurrentuses, List<ItemAttribute>inlist, String inicmeta){
		bid = inbid;
		iid = iniid;
		count = incount;
		currentuses = incurrentuses;

		if(inlist != null){
			//copy!
			attributes = new ArrayList<ItemAttribute>();
			ListIterator<ItemAttribute> li = inlist.listIterator();
			ItemAttribute ia;
			ItemAttribute ib;
			while(li.hasNext()){
				ia = li.next();
				ib = new ItemAttribute(ia.type, ia.value);
				attributes.add(ib);
			}
		
		}else{
			attributes = null;
		}
		icmeta = inicmeta;
		moreInventory = null;
	}
	
	public InventoryContainer(int inbid, int iniid, int incount, int incurrentuses, List<ItemAttribute>inlist, String inicmeta, InventoryContainer inic[]){
		bid = inbid;
		iid = iniid;
		count = incount;
		currentuses = incurrentuses;

		if(inlist != null){
			//copy!
			attributes = new ArrayList<ItemAttribute>();
			ListIterator<ItemAttribute> li = inlist.listIterator();
			ItemAttribute ia;
			ItemAttribute ib;
			while(li.hasNext()){
				ia = li.next();
				ib = new ItemAttribute(ia.type, ia.value);
				attributes.add(ib);
			}
		
		}else{
			attributes = null;
		}
		icmeta = inicmeta;
		moreInventory = inic;
	}
	
	public InventoryContainer(Object inobj, int incount, int incurrentuses, List<ItemAttribute>inlist){
		iid = bid = 0;
		if(inobj instanceof Item)iid = ((Item) inobj).itemID;
		if(inobj instanceof Block)bid = ((Block) inobj).blockID;
		count = incount;
		currentuses = incurrentuses;
		if(inlist != null){
			//copy!
			attributes = new ArrayList<ItemAttribute>();
			ListIterator<ItemAttribute> li = inlist.listIterator();
			ItemAttribute ia;
			ItemAttribute ib;
			while(li.hasNext()){
				ia = li.next();
				ib = new ItemAttribute(ia.type, ia.value);
				attributes.add(ib);
			}
		
		}else{
			attributes = null;
		}
		moreInventory = null;
	}
	
	public InventoryContainer(Object inobj, int incount, int incurrentuses, List<ItemAttribute>inlist, String inicmeta){
		iid = bid = 0;
		if(inobj instanceof Item)iid = ((Item) inobj).itemID;
		if(inobj instanceof Block)bid = ((Block) inobj).blockID;
		count = incount;
		currentuses = incurrentuses;
		if(inlist != null){
			//copy!
			attributes = new ArrayList<ItemAttribute>();
			ListIterator<ItemAttribute> li = inlist.listIterator();
			ItemAttribute ia;
			ItemAttribute ib;
			while(li.hasNext()){
				ia = li.next();
				ib = new ItemAttribute(ia.type, ia.value);
				attributes.add(ib);
			}
		
		}else{
			attributes = null;
		}
		icmeta = inicmeta;
		moreInventory = null;
	}
	
	public InventoryContainer(Object inobj, int incount, int incurrentuses, List<ItemAttribute>inlist, String inicmeta, InventoryContainer yetmore[]){
		iid = bid = 0;
		if(inobj instanceof Item)iid = ((Item) inobj).itemID;
		if(inobj instanceof Block)bid = ((Block) inobj).blockID;
		count = incount;
		currentuses = incurrentuses;
		if(inlist != null){
			//copy!
			attributes = new ArrayList<ItemAttribute>();
			ListIterator<ItemAttribute> li = inlist.listIterator();
			ItemAttribute ia;
			ItemAttribute ib;
			while(li.hasNext()){
				ia = li.next();
				ib = new ItemAttribute(ia.type, ia.value);
				attributes.add(ib);
			}
		
		}else{
			attributes = null;
		}
		icmeta = inicmeta;
		moreInventory = yetmore;
	}
	
	public void writeSelf(Properties prop, String tag){
		writeSelfRecursive(this, prop, tag, 0);
	}	
	public void writeSelfRecursive(InventoryContainer ic, Properties prop, String tag, int reclevel){
		if(reclevel > 5)return; //MAX!!!
		
		prop.setProperty(String.format("%s%s", tag, "BID"), String.format("%d", ic.bid));
		prop.setProperty(String.format("%s%s", tag, "IID"), String.format("%d", ic.iid));
		prop.setProperty(String.format("%s%s", tag, "count"), String.format("%d", ic.count));
		prop.setProperty(String.format("%s%s", tag, "currentuses"), String.format("%d", ic.currentuses));
		prop.setProperty(String.format("%s%s", tag, "icmeta"), ic.icmeta==null?"null":ic.icmeta);
		int listsize = 0;
		if(ic.attributes != null)listsize = ic.attributes.size();
		prop.setProperty(String.format("%s%s", tag, "attrlistlen"), String.format("%d", listsize));
		if(listsize > 0){
			ItemAttribute ia;
			for(int i=0;i<listsize;i++){
				ia = ic.attributes.get(i);
				prop.setProperty(String.format("%s%s_%d", tag, "attrtype", i), String.format("%d", ia.type));
				prop.setProperty(String.format("%s%s_%d", tag, "attrval", i), String.format("%d", ia.value));
			}
		}
		int moresize = 0;
		if(ic.moreInventory != null)moresize = ic.moreInventory.length;
		if(moresize < 0 || moresize > 200)moresize = 0; //max 200!
		prop.setProperty(String.format("%s%s", tag, "moresize"), String.format("%d", moresize));
		if(moresize > 0){
			for(int i=0;i<moresize;i++){
				String tt = String.format("%s%d_%d:", tag, reclevel+1, i);
				if(ic.moreInventory[i] != null){					
					prop.setProperty(String.format("%s%s_%d", tt, "exists", i), String.format("%d", i));
					writeSelfRecursive(ic.moreInventory[i], prop, tt, reclevel+1);
				}else{
					prop.setProperty(String.format("%s%s_%d", tt, "exists", i), String.format("%d", -1));
				}
			}
		}
		
	}
	public void readSelf(Properties prop, String tag){
		readSelfRecursive(this, prop, tag, 0);
	}
	public void readSelfRecursive(InventoryContainer ic, Properties prop, String tag, int reclevel){
		if(reclevel > 5)return; //MAX!!!
		
		ic.bid = Utils.getPropertyInt(prop, String.format("%s%s", tag, "BID"), 0, Blocks.blocksMAX-1, 0);
		ic.iid = Utils.getPropertyInt(prop, String.format("%s%s", tag, "IID"), 0, Items.itemsMAX-1, 0);
		ic.count = Utils.getPropertyInt(prop, String.format("%s%s", tag, "count"), 0, 128, 1);		
		ic.currentuses = Utils.getPropertyInt(prop, String.format("%s%s", tag, "currentuses"), 0, Integer.MAX_VALUE, 0);
		ic.icmeta = Utils.getPropertyString(prop, String.format("%s%s", tag, "icmeta"), null);
		if(ic.icmeta!=null){
			if(ic.icmeta.equals("null"))ic.icmeta = null;
		}
		
		int listsize = Utils.getPropertyInt(prop, String.format("%s%s", tag, "attrlistlen"), 0, 100, 0);
		if(listsize > 0){
			ic.attributes = new ArrayList<ItemAttribute>();
			ItemAttribute ia;
			for(int i=0;i<listsize;i++){
				ia = new ItemAttribute();
				ia.type = Utils.getPropertyInt(prop, String.format("%s%s_%d", tag, "attrtype", i), 0, 100, 0);
				ia.value = Utils.getPropertyInt(prop, String.format("%s%s_%d", tag, "attrval", i), Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
				ic.attributes.add(ia);
			}
		}
		
		int moresize = Utils.getPropertyInt(prop, String.format("%s%s", tag, "moresize"), 0, 200, 0);
		if(moresize > 0){
			ic.moreInventory = new InventoryContainer[moresize];
			int exists = -1;
			for(int i=0;i<moresize;i++){
				String tt = String.format("%s%d_%d:", tag, reclevel+1, i);
				exists = Utils.getPropertyInt(prop, String.format("%s%s_%d", tt, "exists", i), -1, 200, -1);
				if(exists >= 0){
					ic.moreInventory[i] = new InventoryContainer();
					readSelfRecursive(ic.moreInventory[i], prop, tt, reclevel+1);
				}
			}			
		}
	}
	
	public InventoryContainer validate(){
		return validateRecursive(this);
	}
	public InventoryContainer validateRecursive(InventoryContainer ic){
		if(ic == null)return null;
		if(ic.iid == 0 && ic.bid == 0)return null;
		if(ic.iid != 0 && ic.bid != 0)return null;
		if(ic.iid < 0 || ic.bid < 0)return null;
		//valid, but does it still exist?
		if(ic.iid != 0){
			if(Items.ItemArray[ic.iid] == null)return null;
		}
		if(ic.bid != 0){
			if(Blocks.BlockArray[ic.bid] == null)return null;
		}
		if(ic.count == 0)return null;
		
		if(ic.moreInventory != null){
			//System.out.printf("Validate going down\n");
			for(int i=0; i<ic.moreInventory.length;i++){
				//System.out.printf("Validate checking %d\n", i);
				ic.moreInventory[i] = validateRecursive(ic.moreInventory[i]);
			}
			//System.out.printf("Validate came back\n");
		}
		
		return ic;
	}
	
	public List<ItemAttribute> getAttributes(){
		if(bid!=0)return null;
		return attributes;
	}
	
	public int getAttribute(int type){
		if(count <= 0)return 0;
		if(attributes == null)return 0;
		if(iid == 0)return 0;
		if(getMaxStack() != 1)return 0;
		
		ListIterator<ItemAttribute> li = attributes.listIterator();
		ItemAttribute ia;
		while(li.hasNext()){
			ia = li.next();
			if(ia.type == type){
				return ia.value;
			}
		}
		return 0;
	}
	
	public void addAttribute(int type, int value){
		if(bid!=0)return;
		if(count != 1)return;
		if(getMaxStack()!=1)return;
		int newval = value;
		if(newval > 10)newval = 10;
		
		if(attributes == null){
			attributes = new ArrayList<ItemAttribute>();
		}
		
		//add to existing
		ListIterator<ItemAttribute> li = attributes.listIterator();
		ItemAttribute ia;
		while(li.hasNext()){
			ia = li.next();
			if(ia.type == type){
				ia.value += newval;
				if(ia.value > 10)ia.value = 10;
				return;
			}
		}
		//not found, make new
		ia = new ItemAttribute(type, newval);
		attributes.add(ia);
	}	

	public Texture getTexture(){
		if(count <= 0)return null;
		if(bid!=0){
			if(Blocks.showTop(bid))return Blocks.getTextureForSide(bid, 0);
			return Blocks.getTexture(bid); //defaults to front
		}
		if(iid!=0)return Items.getTexture(iid);
		return null;
	}
	
	public String getUniqueName(){
		if(count <= 0)return null;
		if(bid!=0)return Blocks.getUniqueName(bid);
		if(iid!=0)return Items.getUniqueName(iid);
		return null;
	}
	
	public int getMaxStack(){
		if(bid!=0)return Blocks.getMaxStack(bid);
		if(iid!=0)return Items.getMaxStack(iid);
		return 1;
	}
	
	public Item getItem(){
		if(bid != 0)return null;
		return Items.getItem(iid);		
	}
	
	public Block getBlock(){
		if(iid != 0)return null;
		return Blocks.getBlock(bid);		
	}
	
	//anything in inventory, anywhere
	public void inventoryTick(Entity e, int invindex){
		if(iid != 0){
			Item it = getItem();
			if(it != null){
				it.inventoryTick(e, this, invindex);				
			}
		}
		if(bid != 0){
			Block bl = getBlock();
			if(bl != null){
				bl.inventoryTick(e, this, invindex);				
			}
		}		
	}
	
	//currently active item in hand
	public void inUseTick(Entity e, int invindex){
		if(iid != 0){
			Item it = getItem();
			if(it != null){
				it.inUseTick(e, this, invindex);				
			}
		}
		if(bid != 0){
			Block bl = getBlock();
			if(bl != null){
				bl.inUseTick(e, this, invindex);				
			}
		}	
	}
	
	//armor being worn
	public void armorTick(Entity e, int armorindex){
		if(iid != 0){
			Item it = getItem();
			if(it != null && it instanceof ItemArmor){
				ItemArmor ia = (ItemArmor)it;
				ia.armorTick(e, this, armorindex);				
			}
		}
	}
	
	//Crafted or creative inventory
	public void onCrafted(Player pl){
		if(iid != 0){
			Item it = getItem();
			if(it != null){
				it.onCrafted(pl, this);				
			}
		}
		if(bid != 0){
			Block bl = getBlock();
			if(bl != null){
				bl.onCrafted(pl, this);				
			}
		}		
	}

}
