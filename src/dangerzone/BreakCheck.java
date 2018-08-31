package dangerzone;

import dangerzone.entities.Entity;

public class BreakCheck {
	
	//extend and override this and register it with BreakChecks!
	//Your version can make whatever checks it wants.
	//Return false, and the block will not be changed.
	public boolean canChangeBlock(Entity e, int d, int x, int y, int z, int tobid, int tometa){
		return true;
	}
	
	public boolean fireDamage(Entity e, int d, int x, int y, int z){
		return true;
	}
	
	//for blocks that spread, like lava
	public boolean canChangeBlock(World w, int od, int ox, int oy, int oz, int d, int x, int y, int z, int tobid, int tometa){
		return true;
	}
	
	//for players accessing chests and things
	public boolean canTakeStuff(Player p, Entity e){
		return true;
	}

}
