package com.jtrent238.hammermod.items.hammers;

import com.jtrent238.hammermod.ItemLoader;

import dangerzone.gui.InventoryMenus;
import dangerzone.items.Item;

public class ItemRainbowHammer extends Item {

	public ItemRainbowHammer(String n, String txt) {
		//TODO: Fill in INFO!!!
		super(n, txt);
		maxstack = 1;
		attackstrength = 2 * ItemLoader.RAINBOW_MULTIPLIER;
		stonestrength = Math.round(attackstrength / 2 ) * ItemLoader.RAINBOW_MULTIPLIER;
		maxuses = Math.round((attackstrength * stonestrength) * 2) * ItemLoader.RAINBOW_MULTIPLIER;
		burntime = 15 * ItemLoader.RAINBOW_MULTIPLIER;
		hold_straight = true;
		flopped = false;
		menu = InventoryMenus.HARDWARE;
		this.showInInventory = true;
	}

	


}
