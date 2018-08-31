package dangerzone.items;
import dangerzone.Player;
import dangerzone.blocks.BlockRotation;
import dangerzone.blocks.Blocks;

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
public class ItemDoor extends Item {
	
	
	public ItemDoor(String name, String tx){
		super(name, tx);
		maxstack = 4;
		burntime = 65;
	}
		
	//Player right-clicked on this block with this item
	//called from client side.
	public boolean rightClickOnBlock(Player p, int dimension, int x, int y, int z, int side){
		if(p != null && p.world.isServer && side == 0){
				int meta = 0;
				double dx, dz;
				dx = ((double)x+0.5f) - p.posx;
				dz = ((double)z+0.5f) - p.posz;
				if(Math.abs(dx)>Math.abs(dz)){
					if(dx > 0){
						meta = BlockRotation.Y_ROT_270;
					}else{
						meta = BlockRotation.Y_ROT_90;
					}			
				}else{
					if(dz > 0){
						meta = BlockRotation.Y_ROT_180;
					}else{
						meta = BlockRotation.Y_ROT_0;
					}				
				}

			int bid = p.world.getblock(dimension, x, y, z);
			if(Blocks.isSolid(bid)){
				if(p.world.getblock(dimension, x, y+1, z) == 0 && p.world.getblock(dimension, x, y+2, z) == 0){
					p.world.setblockandmetaWithPerm(p, dimension, x, y+1, z, Blocks.doorbottom.blockID, meta);
					p.world.setblockandmetaWithPerm(p, dimension, x, y+2, z, Blocks.doortop.blockID, meta);
					return true;
				}
			}
		}
		return false;
	}

}
