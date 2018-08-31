package dangerzone.items;
import dangerzone.DangerZone;
import dangerzone.InventoryContainer;
import dangerzone.Player;
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
public class ItemSqueakToy extends Item {
	

	
	public ItemSqueakToy(String name, String tx){
		super(name, tx);
		maxstack = 1;
		maxuses = 5000;
	}
	
	public boolean onLeftClick(Entity holder, Entity clickedon, InventoryContainer ic){
		make_noise(holder);	
		return true;
	}
	
	public boolean onRightClick(Entity holder, Entity clickedon, InventoryContainer ic){
		make_noise(holder);	
		return true;
	}
			
	public boolean rightClickOnBlock(Player p, int dimension, int x, int y, int z, int side){
		make_noise(p);	
		return true; 
	}
	
	public void leftClickOnBlock(Player p, int dimension, int x, int y, int z, int side){
		make_noise(p);	
	}
	
	public void make_noise(Entity e){
		if(e != null){
			if(e.world.isServer){
				String str = null;
				switch(e.world.rand.nextInt(10)){
				case 0: 
					str = "DangerZone:rainfrog1";
					break;
				case 1: 
					str = "DangerZone:rainfrog2";
					break;
				case 2: 
					str = "DangerZone:rainfrog3";
					break;
				case 3: 
					str = "DangerZone:rainfrog4";
					break;
				case 4: 
					str = "DangerZone:rainfrog5";
					break;
				case 5: 
					str = "DangerZone:rainfrog6";
					break;
				case 6: 
					str = "DangerZone:rainfrog7";
					break;
				case 7: 
					str = "DangerZone:rainfrog8";
					break;
				case 8: 
					str = "DangerZone:rainfrog9";
					break;
				case 9: 
					str = "DangerZone:rainfrog10";
					break;
				default:
					break;
				}
				if(str != null){
					e.world.playSound(str, e.dimension, e.posx, e.posy, e.posz, 1.0f, 1.0f+(DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat())*0.2f);
				}
			}
		}
	}

}
