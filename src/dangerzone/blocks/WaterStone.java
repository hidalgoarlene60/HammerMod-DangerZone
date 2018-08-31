package dangerzone.blocks;

import dangerzone.BreakChecks;
import dangerzone.World;
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
public class WaterStone extends BlockStone {

	public WaterStone(String n, String txt) {
		super(n, txt, 10);
	}
	
	//client for player, server for entities
	public void doSteppedOn(Entity e, World w, int d, int x, int y, int z){
		if(e != null){
			fillit(w, d, x, y-1, z, Blocks.water.blockID);
			fillit(w, d, x+1, y, z, Blocks.water.blockID);
			fillit(w, d, x-1, y, z, Blocks.water.blockID);
			fillit(w, d, x, y, z+1, Blocks.water.blockID);
			fillit(w, d, x, y, z-1, Blocks.water.blockID);			
			w.playSound("DangerZone:little_splash", d, x, y, z, 0.5f, 1.0f);
		}
	}
	
	public boolean fillit(World w, int d, int x, int y, int z, int lib){
		int bid = w.getblock(d, x, y, z);
		if(bid == 0 || Blocks.isLiquid(bid) || Blocks.isLeaves(bid)){
			int bidtoset = Blocks.getActiveBlockid(lib);
			if(bidtoset == 0)bidtoset = lib; //this must be the active one
			if(!BreakChecks.canChangeBlock(w, d, x, y, z, d, x, y, z, bidtoset, 0x01))return false;
			w.setblockandmeta(d, x, y, z, bidtoset, 0x01); //as high as it can get without being permanent so it stops when this stops
			return true;
		}
		return false;
	}

}