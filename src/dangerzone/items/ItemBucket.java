package dangerzone.items;
import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entity;

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
public class ItemBucket extends Item {

	public ItemBucket(String n, String txt) {
		super(n, txt);
		maxstack = 1;
		burntime = 0;
	}
		
	public boolean rightClickOnBlock(Player p, int dimension, int x, int y, int z, int side){
		if(p != null && p.world.isServer){
			int bid = p.world.getblock(dimension, x, y, z);
			if(bid == Blocks.water.blockID || bid == Blocks.waterstatic.blockID){			
				p.world.setblockandmetaWithPerm(p, dimension, x, y, z, 0, 0);							
				p.setHotbar(p.gethotbarindex(), new InventoryContainer(0, Items.bucketwater.itemID, 1, 0));
				return false;
			}
			if(bid == Blocks.milk.blockID || bid == Blocks.milkstatic.blockID){			
				p.world.setblockandmetaWithPerm(p, dimension, x, y, z, 0, 0);							
				p.setHotbar(p.gethotbarindex(), new InventoryContainer(0, Items.bucketmilk.itemID, 1, 0));
				return false;
			}
			if(bid == Blocks.lava.blockID || bid == Blocks.lavastatic.blockID){			
				p.world.setblockandmetaWithPerm(p, dimension, x, y, z, 0, 0);							
				p.setHotbar(p.gethotbarindex(), new InventoryContainer(0, Items.bucketlava.itemID, 1, 0));
				return false;
			}
		}
		return false;
	}
	
	public boolean onLeftClick(Entity holder, Entity clickedon, InventoryContainer ic){
		if(holder != null && holder.world.isServer){			
			if(clickedon != null && clickedon.isMilkable){													
				holder.setHotbar(holder.gethotbarindex(), new InventoryContainer(0, Items.bucketmilk.itemID, 1, 0));
			}
		}
		return true; //continue with normal left click logic, else it is handled special here
	}
	
	public boolean onRightClick(Entity holder, Entity clickedon, InventoryContainer ic){
		if(holder != null && holder.world.isServer){			
			if(clickedon != null && clickedon.isMilkable){					
				holder.setHotbar(holder.gethotbarindex(), new InventoryContainer(0, Items.bucketmilk.itemID, 1, 0));
				return false;
			}
		}
		return false;
	}

}