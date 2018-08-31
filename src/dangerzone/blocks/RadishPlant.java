package dangerzone.blocks;
import dangerzone.BreakChecks;
import dangerzone.DangerZone;
import dangerzone.Player;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.entities.EntityBlockItem;
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


public class RadishPlant extends ButterflyPlant { //re-use the rendering part!
		
	public RadishPlant(String n, String txt){
		super(n, txt);
		breaksound = "DangerZone:leavesbreak";
		placesound = "DangerZone:leavesplace";
		hitsound =   "DangerZone:leaves_hit";
		isSolidForRendering = false;
		isSolid = false;
		hasOwnRenderer = true;
		randomtick = true;	
		isLeaves = true;
		maxdamage = 1;
		burntime = 10;
		showInInventory = false;
	}
	
	public int getBlockDrop(Player p, World w, int dimension, int x, int y, int z){
		return 0;
	}
	
	public int getItemDrop(Player p, World w, int dimension, int x, int y, int z){
		//drop some rice if we are fully grown
		if(this.blockID == Blocks.radish_plant3.blockID){
			int howmany = 2 + w.rand.nextInt(5);
			for(int i=0;i<howmany;i++){
				Utils.doDropRand(w, 0, Items.radish.itemID, 1, dimension, x, y+1, z);
				Utils.spawnExperience(1, w, dimension, x, y+1, z);
			}
		}
		return 0;
	}
	
	public void notifyNeighborChanged(World w, int d, int x, int y, int z){
		int bid = w.getblock(d, x, y-1, z);
		if(bid != Blocks.dirt.blockID){
			//Don't know what I'm on, but it's not for growing!
			w.setblock(d, x, y, z, 0); 
			//sometimes drop a block
			if(w.rand.nextInt(4) == 1){
				EntityBlockItem e = (EntityBlockItem)w.createEntityByName(DangerZone.blockitemname, d, (double)x+0.5f, (double)y+0.5f, (double)z+0.5f);
				if(e != null){
					e.fill(Blocks.radish_plant, 1); //I am a block!	
					w.spawnEntityInWorld(e);
				}
			}
		}
	}
	
	public void tickMe(World w, int d, int x, int y, int z){
		if(!w.isServer)return;

		if(w.getblock(d, x, y+1, z) != 0){
			w.setblock(d, x, y, z, 0); //Can't grow with anything on top of me!
			//System.out.printf("Grass died\n");
			return;
		}
		int bid = w.getblock(d, x, y-1, z);
		if(bid != Blocks.dirt.blockID){
			w.setblock(d, x, y, z, 0); 
			//System.out.printf("Grass died\n");
			return;
		}
		if(!BreakChecks.canChangeBlock(w, d, x, y, z, d, x, y, z, 0, 0))return;
		if(w.isDaytime() && w.rand.nextInt(22) == 1){			
			int growto = 0;
			if(this.blockID == Blocks.radish_plant.blockID)growto = Blocks.radish_plant1.blockID;
			if(this.blockID == Blocks.radish_plant1.blockID)growto = Blocks.radish_plant2.blockID;
			if(this.blockID == Blocks.radish_plant2.blockID)growto = Blocks.radish_plant3.blockID;
			if(growto != 0){
				w.setblock(d, x, y, z, growto);
			}
		}
	}

}
