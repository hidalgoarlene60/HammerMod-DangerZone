package dangerzone.blocks;
import dangerzone.Player;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.items.Items;

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
public class BlockDark extends Block {

	public BlockDark(String n, String txt, int hardness) {
		super(n, txt);
		isStone = true;
		maxdamage = hardness;
		brightness = -0.25f;
		mindamage = 15; //at least
		breaksound = "DangerZone:stonebreak"; 
		placesound = "DangerZone:stoneplace"; 
		hitsound =   "DangerZone:stonehit";
	}
	
	//client-side only
	public int getBlockDrop(Player p, World w, int dimension, int x, int y, int z){
		//Player can be NULL! Make sure you check!
		return 0;
	}
	
	//client-side only
	public int getItemDrop(Player p, World w, int d, int x, int y, int z){
		//Player can be NULL! Make sure you check!
		Utils.spawnExperience(15, w, d, (double)x+0.5f, (double)y+0.5f, (double)z+0.5f);
		return Items.dark.itemID; //override this if you want to drop an item instead of a block.
	}

}
