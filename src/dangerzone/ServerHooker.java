package dangerzone;

import dangerzone.entities.Entity;
import dangerzone.entities.EntityLiving;


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

public class ServerHooker {
	
	public static ServerHooks ServerHooksArray[] = null;
	public static final int ServerHooksMAX = 64;
		
	ServerHooker(){

	}
	
	public static Player getNewPlayerObject(World w){
		if(ServerHooksArray == null)return new Player(w);	
		
		Player p = null;
		//now custom server checks
		for(int i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] != null){
				p = ServerHooksArray[i].getNewPlayerObject(w);
				if(p != null)return p; //ONLY ONE SERVER MOD can override the player... Override is local only to server. Clients all have generic Player.
			}
		}
		return new Player(w);
	}
	
	public static void registerServerHooks(ServerHooks b){
		int i;
		if(ServerHooksArray == null){
			ServerHooksArray = new ServerHooks[ServerHooksMAX];
			for(i=0;i<ServerHooksMAX;i++){
				ServerHooksArray[i] = null;
			}
		}
		for(i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] == null){
				ServerHooksArray[i] = b;
				return;
			}
		}
		return;
	}
	
	public static boolean player_logged_in(Player p){				
		if(ServerHooksArray == null)return true;		
		//now custom server checks
		for(int i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] != null){
				if(!ServerHooksArray[i].player_logged_in(p))return false;
			}
		}
		return true;
	}
	
	public static boolean player_login_respawn(Player p, boolean respawn){				
		if(ServerHooksArray == null)return respawn;		
		//now custom server checks
		for(int i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] != null){
				if(ServerHooksArray[i].player_login_respawn(p, respawn) != respawn)return !respawn;
			}
		}
		return respawn;
	}
	
	public static boolean player_load(Player p){				
		if(ServerHooksArray == null)return DangerZone.server.loadPlayer(p);	
		
		boolean isnew = DangerZone.server.loadPlayer(p);
		//now custom server checks
		for(int i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] != null){
				ServerHooksArray[i].player_load(p, isnew);
			}
		}
		return isnew;
	}
	
	public static boolean player_always_connect(Player p){				
		if(ServerHooksArray == null)return false;	
		
		//now custom server checks
		for(int i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] != null){
				if(ServerHooksArray[i].player_always_connect(p))return true;
			}
		}
		return false;
	}
	
	public static void player_save(Player p){				
		if(ServerHooksArray == null){
			DangerZone.server.saveThisPlayer(p);
			return;
		}
		
		DangerZone.server.saveThisPlayer(p);
		//now custom server checks
		for(int i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] != null){
				ServerHooksArray[i].player_save(p);
			}
		}
		return;
	}	
	
	public static void player_logged_out(Player p){				
		if(ServerHooksArray == null)return;		
		//now custom server checks
		for(int i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] != null){
				ServerHooksArray[i].player_logged_out(p);
			}
		}
	}	
	public static boolean player_died(Player p){				
		if(ServerHooksArray == null)return true;		
		//now custom server checks
		boolean retval = true;
		for(int i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] != null){
				if(!ServerHooksArray[i].player_died(p))retval = false;
			}
		}
		return retval;
	}
	public static boolean critter_died(EntityLiving e){				
		if(ServerHooksArray == null)return true;		
		//now custom server checks
		boolean retval = true;
		for(int i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] != null){
				if(!ServerHooksArray[i].critter_died(e))retval = false;
			}
		}
		return retval;
	}	
	public static boolean player_respawned(Player p){				
		if(ServerHooksArray == null)return true;		
		//now custom server checks
		boolean retval = true;
		for(int i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] != null){
				if(!ServerHooksArray[i].player_respawned(p))retval = false;
			}
		}
		return retval;
	}
	public static boolean player_teleport(Player p, int d, double x, double y, double z){				
		if(ServerHooksArray == null)return true;		
		//now custom server checks
		boolean retval = true;
		for(int i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] != null){
				if(!ServerHooksArray[i].player_teleport(p, d, x, y, z))retval = false;
			}
		}
		return retval;
	}
	
	public static void entity_loop_start(float deltaT){				
		if(ServerHooksArray == null)return;		
		//now custom server checks
		for(int i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] != null){
				ServerHooksArray[i].entity_loop_start(deltaT);
			}
		}
	}
	
	public static void player_entity_loop_start(Player p){				
		if(ServerHooksArray == null)return;		
		//now custom server checks
		for(int i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] != null){
				ServerHooksArray[i].player_entity_loop_start(p);
			}
		}
	}
	
	public static void entity_action(Entity e, float deltaT){				
		if(ServerHooksArray == null)return;		
		//now custom server checks
		for(int i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] != null){
				ServerHooksArray[i].entity_action(e, deltaT);
			}
		}
	}
	
	public static void onBlockBroken(Player p, int d, int x, int y, int z){
		if(ServerHooksArray == null)return;		
		//now custom server checks
		for(int i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] != null){
				ServerHooksArray[i].onBlockBroken(p, d, x, y, z);
			}
		}
	}
	public static void leftClickOnBlock(Player p, int d, int x, int y, int z){
		if(ServerHooksArray == null)return;		
		//now custom server checks
		for(int i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] != null){
				ServerHooksArray[i].leftClickOnBlock(p, d, x, y, z);
			}
		}
	}
	public static void rightClickOnBlock(Player p, int d, int x, int y, int z){
		if(ServerHooksArray == null)return;		
		//now custom server checks
		for(int i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] != null){
				ServerHooksArray[i].rightClickOnBlock(p, d, x, y, z);
			}
		}
	}
	public static boolean clickedHotBar(Player p, int which, int leftrightmid, boolean shifted){	
		if(ServerHooksArray == null)return true;		
		//now custom server checks
		boolean retval = true;
		for(int i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] != null){
				if(!ServerHooksArray[i].clickedHotBar(p, which, leftrightmid, shifted))retval = false;
			}
		}
		return retval;
	}
	public static boolean clickedArmor(Player p, int which, int leftrightmid, boolean shifted){	
		if(ServerHooksArray == null)return true;		
		//now custom server checks
		boolean retval = true;
		for(int i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] != null){
				if(!ServerHooksArray[i].clickedArmor(p, which, leftrightmid, shifted))retval = false;
			}
		}
		return retval;
	}
	public static boolean clickedInventory(Player p, int which, int leftrightmid, boolean shifted){	
		if(ServerHooksArray == null)return true;		
		//now custom server checks
		boolean retval = true;
		for(int i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] != null){
				if(!ServerHooksArray[i].clickedInventory(p, which, leftrightmid, shifted))retval = false;
			}
		}
		return retval;
	}
	public static boolean clickedEntityInventory(Player p, int eid, int which, int leftrightmid, boolean shifted){	
		if(ServerHooksArray == null)return true;		
		//now custom server checks
		boolean retval = true;
		for(int i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] != null){
				if(!ServerHooksArray[i].clickedEntityInventory(p, eid, which, leftrightmid, shifted))retval = false;
			}
		}
		return retval;
	}
	public static boolean doAttackFrom(EntityLiving e, Entity attckr, int dt, float pain){	
		if(ServerHooksArray == null)return true;		
		//now custom server checks
		boolean retval = true;
		for(int i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] != null){
				if(!ServerHooksArray[i].doAttackFrom(e, attckr, dt, pain))retval = false;
			}
		}
		return retval;
	}
	public static boolean canClaimHere(Player p, int d, int x, int y, int z){
		if(ServerHooksArray == null)return true;		
		//now custom server checks
		for(int i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] != null){
				if(!ServerHooksArray[i].canClaimHere(p, d, x, y, z))return false;
			}
		}
		return true;
	}
	
	public static boolean canSpawnHere(World w, int d, int x, int z, SpawnlistEntry st){
		if(ServerHooksArray == null)return true;		
		//now custom server checks
		for(int i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] != null){
				if(!ServerHooksArray[i].canSpawnHere(w, d, x, z, st))return false;
			}
		}
		return true;
	}
	
	public static Entity spawnEntityByName(String name, World w, int d, double x, double y, double z){
		Entity e = null;
		if(ServerHooksArray == null)return e;		
		//now custom server checks
		for(int i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] != null){
				e = ServerHooksArray[i].spawnEntityByName(name, w, d, x, y, z);
				if(e != null)break;
			}
		}
		return e;
	}
	
	public static void tickChunk(Player p, World w, Chunk c){
		if(ServerHooksArray == null)return;		
		//now custom server checks
		for(int i=0;i<ServerHooksMAX;i++){
			if(ServerHooksArray[i] != null){
				ServerHooksArray[i].tickChunk(p, w, c);
			}
		}
	}

}