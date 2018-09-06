package com.jtrent238.hammermod.items.hammers;

import com.jtrent238.hammermod.ItemLoader;

import dangerzone.gui.InventoryMenus;
import dangerzone.items.Item;

public class ItemincapablegamerHammer extends Item {

	public ItemincapablegamerHammer(String n, String txt) {
		//TODO: Fill in INFO!!!
		super(n, txt);
		maxstack = 1;
		attackstrength = 2 * ItemLoader.TWITCH;
		stonestrength = Math.round(attackstrength / 2 ) * ItemLoader.TWITCH;
		maxuses = Math.round((attackstrength * stonestrength) * 2) * ItemLoader.TWITCH;
		burntime = 15 * ItemLoader.TWITCH;
		hold_straight = true;
		flopped = false;
		menu = InventoryMenus.HARDWARE;
		this.showInInventory = true;
	}

	


}
