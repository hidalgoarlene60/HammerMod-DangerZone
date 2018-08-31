package dangerzone.blocks;

import java.util.List;
import java.util.ListIterator;

import dangerzone.BreakChecks;
import dangerzone.DangerZone;
import dangerzone.World;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityExplosiveBlock;

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
public class BlockInstability extends Block {

	public BlockInstability(String n, String txt, int hardness) {
		super(n, txt);
		maxdamage = hardness;
		mindamage = 5; 
		isSolidForRendering = false;
		breaksound = "DangerZone:crystalblockbreak"; 
		placesound = "DangerZone:crystalblockplace"; 
		hitsound =   "DangerZone:crystalblockhit";
	}
	
	//One of your neighbors has changed. (YOU are at d, x, y, z)
	public void notifyNeighborChanged(World w, int d, int x, int y, int z){
		
		if(!w.isServer)return;
		if(w.getblock(d, x, y, z) != blockID)return;
		
		if(!Blocks.isSolid(w.getblock(d, x, y-1, z)) || isExplosionAround(w, d, x, y, z)){
			int meta = w.getblockmeta(d, x, y, z);
			if(!BreakChecks.canChangeBlock(w, d, x, y, z, d, x, y, z, 0, 0))return;
			w.setblock(d, x, y, z, 0);
			EntityExplosiveBlock eb = (EntityExplosiveBlock) w.createEntityByName("DangerZone:EntityExplosiveBlock", d, 
					(double)x+0.5f+((w.rand.nextFloat()-w.rand.nextFloat())*0.01f), 
					y,
					(double)z+0.5f+((w.rand.nextFloat()-w.rand.nextFloat())*0.01f));
			if(eb != null){
				eb.init();
				eb.rotation_pitch = eb.rotation_yaw = eb.rotation_roll = 0;
				eb.rotation_pitch_motion = eb.rotation_yaw_motion = eb.rotation_roll_motion = 0;
				eb.setBID(blockID);
				eb.setIID(meta);
				eb.setAttackDamage(maxdamage);
				w.spawnEntityInWorld(eb);
			}
		}		
	}
	
	public boolean isExplosionAround(World w, int d, int x, int y, int z){	
		List<Entity> nearby_list = null;
		//Get a list of entities within reach of largest mob expected because we may hit their hitbox!
		nearby_list = DangerZone.server.entityManager.findEntitiesInRange(3.5f, d, (double)x+0.5f, (double)y+0.5f, (double)z+0.5f);
		if(nearby_list != null){
			if(!nearby_list.isEmpty()){
				Entity e = null;
				ListIterator<Entity> li;
				li = nearby_list.listIterator();
				while(li.hasNext()){
					e = (Entity)li.next();
					if(e instanceof EntityExplosiveBlock)return true;
				}								
			}			
		}
		return false;
	}

}
