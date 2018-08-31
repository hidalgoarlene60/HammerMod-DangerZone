package dangerzone.blocks;
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
public class BlockAppleLeaves extends Block {
		
	public BlockAppleLeaves(String n, String txt){
		super(n, txt);
		isSolidForRendering = false;
		//isSolid = false;
		breaksound = "DangerZone:leavesbreak";
		placesound = "DangerZone:leavesplace";
		hitsound =   "DangerZone:leaves_hit";
		isLeaves = true;
		randomtick = true;
		maxdamage = 5;
		burntime = 10;
	}

	public String getStepSound(){
		int i = DangerZone.rand.nextInt(4);
		if(i == 0)return "DangerZone:leaves1";
		if(i == 1)return "DangerZone:leaves2";
		if(i == 2)return "DangerZone:leaves3";
		return "DangerZone:leaves4";
	}
	
	public void tickMe(World w, int d, int x, int y, int z){
		int i, j, k, t;
		
		if(w.getblock(d,  x,  y-1,  z) == 0){
			if(w.rand.nextInt(30) == 0)Utils.spawnParticlesFromServer(w, "DangerZone:ParticleLeaves", 1, d, x, (double)y-0.10f, z, blockID);
		}

		for(i=-2;i<=2;i++){
			for(j=-2;j<=0;j++){ //Only check Down!
				for(k=-2;k<=2;k++){
					t = Math.abs(i) + Math.abs(j) + Math.abs(k); //Make roundish corners, not square!
					if(t <= 3){ 		//Furthest a leaf is allowed to be from a block of wood!
						if(Blocks.canLeavesGrow(w.getblock(d, x+i, y+j, z+k))){
							//occaisionally drop a fruit
							if(w.rand.nextInt(400)== 0 && w.getblock(d, x, y-1, z) == 0){
								EntityBlockItem e = (EntityBlockItem)w.createEntityByName(DangerZone.blockitemname, d, (double)x+0.5f, (double)y-0.5f, (double)z+0.5f);
								if(e != null){
									e.fill(Items.apple, 1);		
									w.spawnEntityInWorld(e);
								}
							}
							return;
						}
					}
				}
			}
		}
		
		//destroy self
		w.setblock(d, x, y, z, 0);
		if(w.getblock(d,  x,  y-1,  z) == 0){
			Utils.spawnParticlesFromServer(w, "DangerZone:ParticleLeaves", 5, d, (double)x+0.5f, (double)y+0.5f, (double)z+0.5f, blockID);
		}
		
		//sometimes drop a leaf block
		if(w.rand.nextInt(400) == 1){
			EntityBlockItem e = (EntityBlockItem)w.createEntityByName(DangerZone.blockitemname, d, (double)x+0.5f, (double)y+0.5f, (double)z+0.5f);
			if(e != null){
				e.fill(this, 1); //I am a block!	
				w.spawnEntityInWorld(e);
			}
		}
	}
	
	//server-side only
	public void onBlockBroken(Player p, int dimension, int x, int y, int z){
		super.onBlockBroken(p, dimension, x, y, z);
		if(p == null)return;
		if(DangerZone.rand.nextInt(10) != 1)return;

		EntityBlockItem e = (EntityBlockItem)p.world.createEntityByName(DangerZone.blockitemname, dimension, (double)x+0.5f, (double)y+0.5f, (double)z+0.5f);
		if(e != null){
			e.fill(Blocks.sapling_apple.blockID, 0, 1); //I am a block!	
			p.world.spawnEntityInWorld(e);
		}
		
		e = (EntityBlockItem)p.world.createEntityByName(DangerZone.blockitemname, dimension, (double)x+0.5f, (double)y+0.5f, (double)z+0.5f);
		if(e != null){
			e.fill(0, Items.apple.itemID, 1); //I am an item!	
			p.world.spawnEntityInWorld(e);
		}
	}


}
