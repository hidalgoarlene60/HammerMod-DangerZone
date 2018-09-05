package com.jtrent238.hammermod.items.hammers;

import dangerzone.gui.InventoryMenus;
import dangerzone.items.Item;

public class ItemWoodHammer extends Item {

	public ItemWoodHammer(String n, String txt) {
		super(n, txt);
		maxstack = 1;
		attackstrength = 1;
		stonestrength = Math.round(attackstrength / 2);
		maxuses = Math.round((attackstrength * stonestrength) * 2);
		burntime = 15;
		hold_straight = true;
		flopped = false;
		menu = InventoryMenus.HARDWARE;
		this.showInInventory = true;
	}

	


}
