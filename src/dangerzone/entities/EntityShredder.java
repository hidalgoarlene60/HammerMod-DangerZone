package dangerzone.entities;
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




import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.Shred;
import dangerzone.Shredding;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.blocks.Blocks;
import dangerzone.items.Items;


public class EntityShredder extends Entity {
	
	public int blocktries = 0;
	public int shredding = 0;
	public Shred spitout = null;
	public int shredded = 60;

	public EntityShredder(World w) {
		super(w);
		uniquename = "DangerZone:EntityShredder";

		ignoreCollisions = true;
		width = 0.01f;
		height = 0.01f;
		has_inventory = true;
		maxrenderdist = 64; //it is invisible, so just update when the player gets near
	}
	
	public void update(float deltaT){
		int bid = world.getblock(dimension,  (int)posx, (int)posy, (int)posz);
		if(bid != Blocks.shredder.blockID ){ //check to see if our block type is there
			if(world.isServer){
				blocktries++;
				if(blocktries > 20){					
					InventoryContainer ic = getInventory(0);
					if(ic != null){
						if(ic.count > 0){
							String icstring = null;
							icstring = ic.getUniqueName();
							int iid, i;
							iid = Items.findByName(icstring);
							bid = Blocks.findByName(icstring);
							for(i=0;i<ic.count;i++){
								Utils.doDropRand(world, bid, iid, 2f, dimension, posx, posy, posz);
							}
							world.playSound("DangerZone:pop", dimension, posx, posy, posz, 1, 1);							
						}
					}
					this.deadflag = true;
				}
			}
		}

		
		if(!this.deadflag && world.isServer){
			
			if(shredding > 0){
				shredding++;
				if(shredding > shredded){ //six seconds - ish!
					shredding = 0;
					if(spitout != null){
						int iid, i;
						iid = Items.findByName(spitout.out);
						bid = Blocks.findByName(spitout.out);
						for(i=0;i<spitout.out_count;i++){
							Utils.doDropRand(world, bid, iid, 2f, dimension, posx, posy, posz);
						}
						world.playSound("DangerZone:pop", dimension, posx, posy, posz, 1, 1);
					}
					spitout = null;
				}
				
			}else{
				InventoryContainer ic = getInventory(0);
				if(ic != null){
					if(ic.count > 0){
						String icstring = null;
						icstring = ic.getUniqueName();
						if(icstring != null){
							spitout = Shredding.find(icstring);
							if(spitout != null){
								ic.count--;
								shredding = 1;
								shredded = 60 + world.rand.nextInt(20); //just because it'll drive everyone crazy... :)
								if(ic.count < 1){
									ic = null;
								}
								setInventory(0, ic);
								int which = world.rand.nextInt(3);
								if(which == 0)world.playSound("DangerZone:shredder1", dimension, posx, posy, posz, 1, 1);
								if(which == 1)world.playSound("DangerZone:shredder2", dimension, posx, posy, posz, 1, 1);
								if(which == 2)world.playSound("DangerZone:shredder3", dimension, posx, posy, posz, 1, 1);
							}
						}
					}

				}
			}

			
		}
		

		motionx = motiony = motionz = 0;
		super.update(deltaT);
	}
	
	public boolean rightClickedByPlayer(Player p, InventoryContainer ic){
		int bid = world.getblock(dimension,  (int)posx, (int)posy, (int)posz);
		if(bid != Blocks.shredder.blockID){
			this.deadflag = true;
			return false;
		}
		Blocks.rightClickOnBlock(Blocks.shredder.blockID, p, dimension, (int)posx, (int)posy, (int)posz);
		return false;
	}
	


}
