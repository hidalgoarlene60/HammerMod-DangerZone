package com.jtrent238.hammermod.items.hammers;

import dangerzone.gui.InventoryMenus;
import dangerzone.items.Item;

public class ItemToasterHammer extends Item {

	public ItemToasterHammer(String n, String txt) {
		//TODO: Fill in INFO!!!
		super(n, txt);
		maxstack = 1;
		attackstrength = (int) 0.5;
		stonestrength = 1000;
		maxuses = 30000;
		burntime = 15;
		hold_straight = true;
		flopped = false;
		menu = InventoryMenus.HARDWARE;
		this.showInInventory = true;
	}

}
