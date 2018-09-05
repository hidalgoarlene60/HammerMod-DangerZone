package com.jtrent238.hammermod.items.hammers;

import dangerzone.items.Item;

public class ItemBaseHammer extends Item{

	@SuppressWarnings("unused")
	private boolean showInInventory;

	public ItemBaseHammer(String n, String txt, int maxstack, int attackstrength, int burntime, boolean hold_straight, boolean flopped, int menu) {
		super(n, txt);
		//maxstack = 1;
		//attackstrength = 5;
		//stonestrength = strength;
		//burntime = 15;
		//hold_straight = true;
		//flopped = false;
		//menu = InventoryMenus.HARDWARE;
		//this.showInInventory = true;
	}

}
