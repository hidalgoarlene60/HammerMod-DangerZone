package dangerzone.blocks;

import dangerzone.Player;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.items.Items;

public class BlockOreBloodstone extends BlockOre {

	public BlockOreBloodstone(String n, String txt, int hardness, int mindam) {
		super(n, txt, hardness, mindam);
	}
	
	//client-side only
	public int getBlockDrop(Player p, World w, int dimension, int x, int y, int z){
		//Player can be NULL! Make sure you check!
		return 0;
	}
	
	//client-side only
	public int getItemDrop(Player p, World w, int d, int x, int y, int z){
		//Player can be NULL! Make sure you check!
		Utils.spawnExperience(10, w, d, (double)x+0.5f, (double)y+0.5f, (double)z+0.5f);
		return Items.bloodstone.itemID; //override this if you want to drop an item instead of a block.
	}

}
