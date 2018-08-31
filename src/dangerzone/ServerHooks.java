package dangerzone;

import dangerzone.entities.Entity;
import dangerzone.entities.EntityLiving;


public class ServerHooks {
	
	//extend and override this and register it with ServerHooker!
	public Player getNewPlayerObject(World w){
		//By extending Player with your own class, you can intercept and override almost everything.
		return null;
	}
	public boolean player_always_connect(Player p){
		return false; //connect even when too many on server
	}
	public boolean player_logged_in(Player p){
		return true; //false if login should be denied
	}
	public boolean player_login_respawn(Player p, boolean respawn){
		return respawn;
	}
	public void player_load(Player p, boolean isnew){
		//normal player props have already been loaded
		//read the props file and extract whatever else you need
		//isnew means prop file was not found and player is new
	}
	public void player_save(Player p){
		//READ the player props file, ADD your props, then write it back out.
	}
	public void player_logged_out(Player p){
	}
	public boolean player_died(Player p){
		return true; //continue normally with death
	}
	public boolean critter_died(EntityLiving e){
		return true; //continue normally with death
	}
	public boolean player_respawned(Player p){
		return true; //continue normal respawn
	}
	public boolean player_teleport(Player p, int d, double x, double y, double z){
		return true; //continue normal teleport
	}
	public void entity_loop_start(float deltaT){
		//entity update loop on server is about to cycle, generic
	}
	public void player_entity_loop_start(Player p){
		//entity update loop on server is about to cycle, called for each player
	}
	public void entity_action(Entity e, float deltaT){
		//might be a player, might not be. called for every entity.
	}
	public void onBlockBroken(Player p, int d, int x, int y, int z){
	}
	public void leftClickOnBlock(Player p, int d, int x, int y, int z){
	}
	public void rightClickOnBlock(Player p, int d, int x, int y, int z){
	}
	public boolean clickedHotBar(Player p, int which, int leftrightmid, boolean shifted){	
		return true;
	}
	public boolean clickedArmor(Player p, int which, int leftrightmid, boolean shifted){	
		return true;
	}
	public boolean clickedInventory(Player p, int which, int leftrightmid, boolean shifted){	
		return true;
	}
	public boolean clickedEntityInventory(Player p, int eid, int which, int leftrightmid, boolean shifted){	
		return true;
	}
	public boolean doAttackFrom(EntityLiving e, Entity attckr, int dt, float pain){	
		return true;
	}
	public boolean canClaimHere(Player p, int d, int x, int y, int z){
		return true;
	}	
	public boolean canSpawnHere(World w, int d, int x, int z, SpawnlistEntry st){
		return true;
	}
	//check the name and location. if you want something else spawn it by name and return it.
	//It ***MUST*** inherit from the original or things may break!
	//Example: SuperMoose which inherits from Moose, but has super-powers.
	//Or AppleMoose, which is exactly like a regular Moose, but overrides drops to drop apples instead.
	//OK, most of the time it's ok to spawn something else instead, but there *ARE* cases where it is not!!!
	//you are warned to be careful and test test test...
	public Entity spawnEntityByName(String name, World w, int d, double x, double y, double z){
		return null;
	}
	public void tickChunk(Player p, World w, Chunk c){
	}
	
}
