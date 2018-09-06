package com.jtrent238.hammermod.items.hammers;

import com.jtrent238.hammermod.ItemLoader;

import dangerzone.gui.InventoryMenus;
import dangerzone.items.Item;

public class Item_MrGregor_Hammer extends Item {

	public Item_MrGregor_Hammer(String n, String txt) {
		//TODO: Fill in INFO!!!
		super(n, txt);
		maxstack = 1;
		attackstrength = 2 * ItemLoader.YT;
		stonestrength = Math.round(attackstrength / 2 ) * ItemLoader.YT;
		maxuses = Math.round((attackstrength * stonestrength) * 2) * ItemLoader.YT;
		burntime = 15 * ItemLoader.YT;
		hold_straight = true;
		flopped = false;
		menu = InventoryMenus.HARDWARE;
		this.showInInventory = true;
	}

	


}
