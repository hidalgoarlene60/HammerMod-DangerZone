package dangerzone.items;

import dangerzone.DamageTypes;
import dangerzone.GameModes;
import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entity;

public class ItemBucketLava extends ItemFood {

	public ItemBucketLava(String n, String tx, int fv) {
		super(n, tx, fv);
		maxstack = 1;
		eatanytime = true;
		burntime = 1000;
	}
	
	public void onFoodEaten(Entity e){
		e.doSetOnFire(500);	
		e.doAttackFrom(null, DamageTypes.FIRE, 100);		
	}
	
	public boolean rightClickOnBlock(Player p, int dimension, int x, int y, int z, int side){
		if(p != null && p.world.isServer){						
				if(Blocks.doPlaceBlock(Blocks.lava.blockID, p.world.getblock(dimension, x, y, z), p, p.world, dimension, x, y, z, side)){							
					if(p.getGameMode() == GameModes.SURVIVAL)p.setHotbar(p.gethotbarindex(), new InventoryContainer(0, Items.bucket.itemID, 1, 0));
				}
		}
		return false;
	}
}
