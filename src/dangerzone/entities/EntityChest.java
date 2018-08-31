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


import dangerzone.DangerZone;
import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.World;
import dangerzone.blocks.Blocks;


public class EntityChest extends Entity {
	

	public int blocktries = 0;

	public EntityChest(World w) {
		super(w);
		uniquename = "DangerZone:EntityChest";
		ignoreCollisions = true;
		width = 0.01f;
		height = 0.01f;
		has_inventory = true; //we use this to pass from client to server! (not using packets!)
		maxrenderdist = 64; //it is invisible, so just update when the player gets near
	}
	
	public void update(float deltaT){
		if(world.isServer && world.getblock(dimension,  (int)posx, (int)posy, (int)posz) != Blocks.chest.blockID){
			//System.out.printf("update chest entity no chest!\n");	
			blocktries++;
			if(blocktries > 20){
				dumpInventory();
				this.deadflag = true;
			}
		}
		
		//kick entity out to other players!
		motionx = motiony = motionz = 0;
		super.update(deltaT);
	}
	
	public boolean rightClickedByPlayer(Player p, InventoryContainer ic){
		if(world.getblock(dimension,  (int)posx, (int)posy, (int)posz) != Blocks.chest.blockID){
			//System.out.printf("rightclickentity chest entity no chest!\n");
			if(world.isServer){
				dumpInventory();
			}
			this.deadflag = true;
			return false;
		}
		if(!p.world.isServer)Blocks.rightClickOnBlock(Blocks.chest.blockID, p, dimension, (int)posx, (int)posy, (int)posz);
		return false;
	}
	
	private void dumpInventory(){
		InventoryContainer ic = null;
		for(int i=0;i<50;i++){
			ic = getInventory(i);
			if(ic != null){				
				EntityBlockItem e = (EntityBlockItem)world.createEntityByName(DangerZone.blockitemname, dimension, posx, posy, posz);
				if(e != null){
					e.fill(ic);
					e.rotation_pitch = world.rand.nextInt(360);
					e.rotation_yaw = world.rand.nextInt(360);
					e.rotation_roll = world.rand.nextInt(360);
					e.motionx = (world.rand.nextFloat()-world.rand.nextFloat()/10f); 
					e.motiony = world.rand.nextFloat()/2;
					e.motionz = (world.rand.nextFloat()-world.rand.nextFloat()/10f); 
					world.spawnEntityInWorld(e);
				}				
			}
		}
	}
	

}
