package dangerzone.items;
import dangerzone.BreakChecks;
import dangerzone.DamageTypes;
import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityBlock;
import dangerzone.entities.EntityLiving;

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
public class ItemDropStick extends Item {
	

	
	public ItemDropStick(String name, String tx){
		super(name, tx);
		maxstack = 1;
		maxuses = 50;
		brightness = 0.55f;
	}
	
	public boolean onLeftClick(Entity holder, Entity clickedon, InventoryContainer ic){
		if(holder != null && clickedon != null && holder.world.isServer){
			if(clickedon instanceof EntityLiving){
				clickedon.doSetOnFire(10);
				clickedon.doAttackFrom(holder, DamageTypes.FIRE,  1.0f);
			}
		}
		return true;
	}
	
	public boolean onRightClick(Entity holder, Entity clickedon, InventoryContainer ic){
		if(holder != null && clickedon != null && holder.world.isServer){
			if(clickedon instanceof EntityLiving){
				clickedon.setOnFire(10);
				clickedon.doAttackFrom(holder, DamageTypes.FIRE,  1.0f);
				return true;
			}
		}
		return false;
	}
		
	//Player right-clicked on this block with this item
	//called from client side.
	public boolean rightClickOnBlock(Player p, int dimension, int x, int y, int z, int side){
		if(p != null && p.world.isServer){
			int bid = p.world.getblock(dimension, x, y, z);
			if( bid != 0){ //loop protection
				if(p.world.getblock(dimension, x, y-1, z) == 0
						&& BreakChecks.canChangeBlock(p, dimension, x, y, z, 0, 0)){
					int meta = p.world.getblockmeta(dimension, x, y, z);
					EntityBlock eb = (EntityBlock) p.world.createEntityByName("DangerZone:EntityBlock", dimension, 
							(double)x+0.5f+((p.world.rand.nextFloat()-p.world.rand.nextFloat())*0.01f), 
							(double)y,
							(double)z+0.5f+((p.world.rand.nextFloat()-p.world.rand.nextFloat())*0.01f));
					if(eb != null){
						eb.init();
						p.world.setblock(dimension, x, y, z, 0);
						eb.rotation_pitch = eb.rotation_yaw = eb.rotation_roll = 0;
						eb.rotation_pitch_motion = eb.rotation_yaw_motion = eb.rotation_roll_motion = 0;
						eb.setBID(bid);
						eb.setIID(meta);
						p.world.spawnEntityInWorld(eb);
					}
					return true; 
				}
			}
		}
		return false; 
	}

}
