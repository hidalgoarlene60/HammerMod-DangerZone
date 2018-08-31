package dangerzone.items;

import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.entities.Entity;
import dangerzone.gui.InventoryMenus;

public class ItemBowEmpty extends Item {

	public ItemBowEmpty(String n, String txt, int a) {
		super(n, txt);
		maxuses = a;
		maxstack = 1;
		attackstrength = 3;
		burntime = 15;
		flopped = true;
		menu = InventoryMenus.HARDWARE;
	}
	
	public void inventoryTick(Entity holder, InventoryContainer ic, int invindex){
		if(holder.world.isServer){
			if(holder instanceof Player){
				if(holder.hasInInventory(Items.arrow.itemID)){
					//shit. no easy way to do this...
					InventoryContainer ic2;
					for(int i=0;i<10;i++){
						ic2 = holder.getHotbar(i);
						if(ic2 == ic){
							ic.iid = Items.bow.itemID;
							holder.setHotbarChanged(i);
							return;
						}
					}
					for(int i=0;i<50;i++){
						ic2 = holder.getInventory(i);
						if(ic2 == ic){
							ic.iid = Items.bow.itemID;
							holder.setInventoryChanged(i);
							return;
						}
					}
				}
			}
		}
	}

}