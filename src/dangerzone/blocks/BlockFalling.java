package dangerzone.blocks;

import dangerzone.BreakChecks;
import dangerzone.World;
import dangerzone.entities.EntityBlock;

public class BlockFalling extends Block {

	public BlockFalling(String n, String txt) {
		super(n, txt);
	}
	
	//One of your neighbors has changed. (YOU are at d, x, y, z)
	public void notifyNeighborChanged(World w, int d, int x, int y, int z){

		if(!w.isServer)return;
		if(w.getblock(d, x, y, z) != blockID)return;
		
		int bid = w.getblock(d, x, y-1, z);
		if(!Blocks.isSolid(bid)){
			int meta = w.getblockmeta(d, x, y, z);
			if(!BreakChecks.canChangeBlock(w, d, x, y, z, d, x, y, z, 0, 0))return;
			w.setblock(d, x, y, z, 0);
			EntityBlock eb = (EntityBlock) w.createEntityByName("DangerZone:EntityBlock", d, 
					(double)x+0.5f+((w.rand.nextFloat()-w.rand.nextFloat())*0.01f), 
					(double)y,
					(double)z+0.5f+((w.rand.nextFloat()-w.rand.nextFloat())*0.01f));
			if(eb != null){
				eb.init();
				eb.rotation_pitch = eb.rotation_yaw = eb.rotation_roll = 0;
				eb.rotation_pitch_motion = eb.rotation_yaw_motion = eb.rotation_roll_motion = 0;
				eb.setBID(blockID);
				eb.setIID(meta);
				w.spawnEntityInWorld(eb);
			}
		}

	}
}
