package dangerzone;

import java.util.List;

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

public class BreakChecks {
	
	public static BreakCheck BreakCheckArray[] = null;
	public static final int BreakCheckMAX = 64;
		
	BreakChecks(){

	}
	
	public static void registerBreakCheck(BreakCheck b){
		int i;
		if(BreakCheckArray == null){
			BreakCheckArray = new BreakCheck[BreakCheckMAX];
			for(i=0;i<BreakCheckMAX;i++){
				BreakCheckArray[i] = null;
			}
		}
		for(i=0;i<BreakCheckMAX;i++){
			if(BreakCheckArray[i] == null){
				BreakCheckArray[i] = b;
				return;
			}
		}
		return;
	}
	
	public static boolean canChangeBlock(Entity e, int d, int x, int y, int z, int tobid, int tometa){		
		int i;
		
		if(e instanceof Player){
			Player pl = (Player)e;
			//Operator in Ghost mode can do whatever they want...
			if((pl.player_privs & PlayerPrivs.OPERATOR) != 0 && pl.getGameMode() == GameModes.GHOST)return true;
		}
		
		if(DangerZone.freeze_world)return false;

		if(DangerZone.start_server && DangerZone.start_client){
			return true;
		}
		
		if(e instanceof Player){
			Player pl = (Player)e;
			if(pl.getGameMode() == GameModes.LIMBO)return false;
			if(pl.world.isServer){ //check to make sure!
				List<String> cowners = DangerZone.server_chunk_cache.getChunkOwners(d, x, y, z);
				if(cowners != null){
					if(!isIn(pl.myname, cowners))return false; //not owner!
				}				
			}
		}else{
			if(DangerZone.start_server){
				List<String> cowners = DangerZone.server_chunk_cache.getChunkOwners(d, x, y, z);
				if(cowners != null){
					return false; //unknown assailant... not owner!
				}	
			}
		}
		
		if(BreakCheckArray == null)return true;
		
		//now custom server checks
		for(i=0;i<BreakCheckMAX;i++){
			if(BreakCheckArray[i] != null){
				if(!BreakCheckArray[i].canChangeBlock(e, d, x, y, z, tobid, tometa))return false;
			}
		}
		return true;
	}	
	
	public static boolean fireDamage(Entity e, int d, int x, int y, int z){		
		int i;
		
		if(!DangerZone.firedamage)return false;
		if(DangerZone.freeze_world)return false;
		
		if(BreakCheckArray == null)return true;
		
		//now custom server checks
		for(i=0;i<BreakCheckMAX;i++){
			if(BreakCheckArray[i] != null){
				if(!BreakCheckArray[i].fireDamage(e, d, x, y, z))return false;
			}
		}
		return true;
	}	
	
	public static boolean canChangeBlock(World w, int od, int ox, int oy, int oz, int d, int x, int y, int z, int tobid, int tometa){		
		int i;
		
		if(DangerZone.freeze_world)return false;

		if(DangerZone.start_server && DangerZone.start_client){
			return true;
		}

		if(DangerZone.start_server){
			List<String> toowners = DangerZone.server_chunk_cache.getChunkOwners(d, x, y, z);
			List<String> fowners = DangerZone.server_chunk_cache.getChunkOwners(od, ox, oy, oz);
			if(toowners != null){
				if(fowners == null)return false; //unknown assailant... not owner!
				//if anything matches, then is OK!
				boolean isOK = false;
				for(i=0; i<fowners.size();i++){
					if(isIn(fowners.get(i), toowners)){
						isOK = true;
						break;
					}
				}
				if(!isOK)return false;
			}
		}
		
		if(BreakCheckArray == null)return true;
		
		//now custom server checks
		for(i=0;i<BreakCheckMAX;i++){
			if(BreakCheckArray[i] != null){
				if(!BreakCheckArray[i].canChangeBlock(w, od, ox, oy, oz, d, x, y, z, tobid, tometa))return false;
			}
		}
		return true;
	}	
	
	public static boolean isIn(String is, List<String>inlist){
		if(is == null && inlist == null)return true;
		if(inlist == null)return false;
		if(is == null)return false;
		int i = 0;
		int ilen = inlist.size();
		while(ilen > 0){
			if(is.toLowerCase().equals(inlist.get(i).toLowerCase()))return true;
			ilen--;
			i++;
		}	
		return false;
	}
	
	//default to close by and can break block in chunk
	public static boolean canTakeStuff(Player p, Entity e){
		if(p.getDistanceFromEntity(e) > 10)return false;
		return canChangeBlock(p, e.dimension, (int)e.posx,(int)e.posy, (int)e.posz, 0, 0);
	}

}
