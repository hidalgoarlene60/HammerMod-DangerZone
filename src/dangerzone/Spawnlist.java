package dangerzone;
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



import java.util.ArrayList;
import java.util.List;

import dangerzone.biomes.Biome;
import dangerzone.entities.Entity;


public class Spawnlist {
		
	public static  List<SpawnlistEntry> spawnlist; // a list
		
	Spawnlist(){
		spawnlist = new ArrayList<SpawnlistEntry>();
	}
	
/*
 * e = entity to be spawned
 * dimension (null = any)
 * biome (null = any)
 * mny = minumum y level
 * mxy - maximum y level
 * ch = chance 1-100% of spawning in a chunk
 * tr = roughly how many to spawn in the chunk
 * ty = type: 0 = ground, 1 = air
 * prm = 0 = ambient (will despan on their own), 1 = permanent
 */
	public static void registerSpawn(Entity e, Dimension d, Biome bio, int mny, int mxy, int chance, int howmany, int ty, int prm){
		SpawnlistEntry no = new SpawnlistEntry();
		no.whatToSpawn = e;
		no.dimensionname = null;
		if(d != null){
			no.dimensionname = d.uniquename;
		}
		no.biomename = null;
		if(bio != null){
			no.biomename = bio.uniquename;
		}
		no.miny = mny;
		no.maxy = mxy;
		no.chance = chance;
		no.tries = howmany;
		no.type = ty;
		no.permanence = prm; //will only be spawned ONCE per chunk, at creation time.
		
		if(mny < 0 || mxy < 0 || chance < 1 || howmany < 1)return;
		if(mny > mxy)return;
		
		spawnlist.add(no);

	}
	


}