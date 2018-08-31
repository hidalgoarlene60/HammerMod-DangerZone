package dangerzone.items;


import dangerzone.gui.InventoryMenus;

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
public class ItemSword extends Item {

	public ItemSword(String n, String txt, int a, int b) {
		super(n, txt);
		maxuses = a;
		maxstack = 1;
		attackstrength = b;
		burntime = 15;
		menu = InventoryMenus.HARDWARE;
	}

	/*
	 * add some buffs the first time something right-clicks this thing...
	 * 
	public boolean onRightClick(Entity holder, Entity clickedon, InventoryContainer ic){
		if(holder == null)return false;
		if(ic.attributes == null){ //if not already added...
			if(holder.world.isServer){
				//add some just for fun...
				ic.addAttribute(ItemAttribute.ACCURACY, 3);
				ic.addAttribute(ItemAttribute.DAMAGE, 3);
				ic.addAttribute(ItemAttribute.DURABILITY, 2);
				ic.addAttribute(ItemAttribute.REACH, 2);
				ic.addAttribute(ItemAttribute.SPAM, 5);
				holder.setVarInventoryChanged(ic); //finds this container and sets bit to update to clients!
			}
		}
		return false;
	}
	*/
	
	/*
	 * example for adding buffs when crafted (or from creative inventory) instead of on a right-click
	 * Much easier. No updating necessary as it will all get done later automagically...
	 * 
	public void onCrafted(Player p, InventoryContainer ic){
		//add some just for fun...
		ic.addAttribute(ItemAttribute.ACCURACY, 3);
		ic.addAttribute(ItemAttribute.DAMAGE, 3);
		ic.addAttribute(ItemAttribute.DURABILITY, 2);
		ic.addAttribute(ItemAttribute.REACH, 2);
		ic.addAttribute(ItemAttribute.SPAM, 5);
	}	  
	*/
	

}
