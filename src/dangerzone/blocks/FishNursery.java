package dangerzone.blocks;

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




import dangerzone.DangerZone;
import dangerzone.World;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityBlockItem;


public class FishNursery extends SeaPlant {

		
	public FishNursery(String n, String txt){
		super(n, txt);
	}
	
	public void notifyNeighborChanged(World w, int d, int x, int y, int z){
		int bid = w.getblock(d, x, y-1, z);
		if(bid != Blocks.stone.blockID){
			//Don't know what I'm on, but it's not for growing!
			w.setblock(d, x, y, z, Blocks.water.blockID); //Can't grow on anything except stone!
			//System.out.printf("Grass died 4\n");
			//sometimes drop a grass block
			if(w.rand.nextInt(4) == 1){
				EntityBlockItem e = (EntityBlockItem)w.createEntityByName(DangerZone.blockitemname, d, (double)x+0.5f, (double)y+0.5f, (double)z+0.5f);
				if(e != null){
					e.fill(this, 1); //I am a block!	
					w.spawnEntityInWorld(e);
				}
			}
		}
		bid = w.getblock(d, x, y+1, z);
		if(bid != Blocks.waterstatic.blockID && bid != Blocks.water.blockID){
			//Don't know what I'm on, but it's not for growing!
			w.setblock(d, x, y, z, Blocks.water.blockID); //Can't grow under anything except waterstatic!
			//System.out.printf("Grass died 5\n");
			//sometimes drop a grass block
			if(w.rand.nextInt(4) == 1){
				EntityBlockItem e = (EntityBlockItem)w.createEntityByName(DangerZone.blockitemname, d, (double)x+0.5f, (double)y+0.5f, (double)z+0.5f);
				if(e != null){
					e.fill(this, 1); //I am a block!	
					w.spawnEntityInWorld(e);
				}
			}
		}

	}
	
	public void tickMe(World w, int d, int x, int y, int z){
		if(!w.isServer)return;
		int i, j, k, m;
		if(w.getblock(d, x, y+1, z) != Blocks.waterstatic.blockID){
			w.setblock(d, x, y, z, Blocks.water.blockID); //Can't grow with anything on top of me!
			//System.out.printf("Grass died 1\n");
			return;
		}
		if(w.getblock(d, x, y-1, z) != Blocks.stone.blockID){
			w.setblock(d, x, y, z, Blocks.water.blockID); //Can't grow on anything except grassblock!
			//System.out.printf("Grass died 2\n");
			return;
		}

		if(w.isDaytime() && w.rand.nextInt(15) == 1){
			int howmany = 1 + w.rand.nextInt(3);

			for(i=0;i<howmany;i++){
				String whichfish = "";
				int which = w.rand.nextInt(6);
				if(which == 0)whichfish = "DangerZone:Butterfly Fish";
				if(which == 1)whichfish = "DangerZone:Fish";
				if(which == 2)whichfish = "DangerZone:Minnow";
				if(which == 3)whichfish = "DangerZone:Puffer Fish";
				if(which == 4)whichfish = "DangerZone:Stick Fish";
				if(which == 5)whichfish = "DangerZone:Another Fish";

				j = w.rand.nextInt(2);
				k = w.rand.nextInt(2);
				if(w.rand.nextBoolean())k=-k;
				m = w.rand.nextInt(2);
				if(w.rand.nextBoolean())m = -m;

				if(w.getblock(d,  (int)x+k, (int)y-j, (int)z+m) == Blocks.waterstatic.blockID){				
					Entity eb = w.createEntityByName(whichfish, d, ((double)x+k)+0.5f, ((double)y-j)+0.5f, ((double)z+m)+0.5f);
					if(eb != null){
						eb.init();
						eb.setBaby(true);
						w.spawnEntityInWorld(eb);
					}
				}
			}
		}
	}
	
	
	


}
