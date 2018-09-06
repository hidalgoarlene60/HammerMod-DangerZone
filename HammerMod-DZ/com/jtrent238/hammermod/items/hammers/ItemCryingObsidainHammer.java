package com.jtrent238.hammermod.items.hammers;

import dangerzone.gui.InventoryMenus;
import dangerzone.items.Item;

public class ItemCryingObsidainHammer extends Item {

	public ItemCryingObsidainHammer(String n, String txt) {
		//TODO: Fill in INFO!!!
		super(n, txt);
		maxstack = 1;
		attackstrength = 10;
		stonestrength = Math.round(attackstrength / 2);
		maxuses = 8000;
		burntime = 15;
		hold_straight = true;
		flopped = false;
		menu = InventoryMenus.HARDWARE;
		this.showInInventory = true;
	}

	


}
