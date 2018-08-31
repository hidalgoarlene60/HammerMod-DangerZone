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

import java.util.Random;

import dangerzone.biomes.Biome;
import dangerzone.biomes.BiomeManager;
import dangerzone.entities.Entities;
import dangerzone.entities.Entity;
import dangerzone.particles.Particle;
import dangerzone.particles.Particles;
import dangerzone.threads.NotifyBlockTicker;



public class World {

	public  ChunkCache chunkcache = null;
	public  ServerChunkCache serverchunkcache = null;
	public Random rand;
	public boolean isServer = false;
	public int timetimer; //time of "day"
	public int lengthOfDay = 360; //BlockTicker thread keeps time for us
	public long worldseed = 0; //ONLY VALID ON SERVER

	
	public World(ChunkCache c, ServerChunkCache s){
		chunkcache = c;
		serverchunkcache = s;
		rand = new Random(System.currentTimeMillis());
		isServer = false;
		timetimer = 0;
		
	}

	public Biome getBiome(int d, int x, int y, int z){
		if(!isServer)return null;
		if(Dimensions.getBiomeManager(d) == null)return null;
		Chunk t = DangerZone.server_chunk_cache.getChunk(this, d, x, y, z);
		if(t != null){
			if(t.mybiome != null)return t.mybiome;
		}
		return Dimensions.getBiomeManager(d).getBiomeForChunk(t, d, x, y, z);
	}
	
	public BiomeManager getBiomeManager(int d, int x, int y, int z){
		if(!isServer)return null;
		return Dimensions.getBiomeManager(d);
	}
	
	public int getblock(int d, int x, int y, int z){
		if(isServer){
			return serverchunkcache.getBlock(this, d, x, y, z);
		}else{
			return chunkcache.getBlock(this, d, x, y, z);
		}
	}
	
	public int getblockmeta(int d, int x, int y, int z){
		if(isServer){
			return serverchunkcache.getBlockmeta(this, d, x, y, z);
		}else{
			return chunkcache.getBlockmeta(this, d, x, y, z);
		}
	}
	
	//Maybe use the *WithPerm routines below?
	/*
	 * ----------------- SERIOUSLY, to be SERVER-FRIENDLY (compatible), these routines are performed without permission checks,
	 * and should only be used in routines involved in chunk decoration (IE:world decorators) that CREATE the landscape.
	 * Server-side (where almost everything is done anyway, is not permission-checked. Client-side is ALWAYS permission-checked.
	 * Use the *WithPerm setblock routines whenever possible...
	 */
	public void setblock(int d, int x, int y, int z, int id){
		if(isServer){			
			serverchunkcache.setBlock(this, d, x, y, z, id, 0);
			if(serverchunkcache.isDecorated(d, x, y, z)){				
				NotifyBlockTicker.addNotifyBlocksAround(d, x, y, z);
			}
		}else{
			chunkcache.setBlock(this, d, x, y, z, id, 0);
			if(DangerZone.start_server){ //Single player
				NotifyBlockTicker.addNotifyBlocksAround(d, x, y, z);
			}
		}
	}

	public void setblockandmeta(int d, int x, int y, int z, int id, int meta){	
		if(isServer){
			serverchunkcache.setBlock(this, d, x, y, z, id, meta);
			if(serverchunkcache.isDecorated(d, x, y, z)){
				NotifyBlockTicker.addNotifyBlocksAround(d, x, y, z);
			}
		}else{
			chunkcache.setBlock(this, d, x, y, z, id, meta);
			if(DangerZone.start_server){ //Single player
				NotifyBlockTicker.addNotifyBlocksAround(d, x, y, z);
			}
		}
	}
	
	public void setblocknonotify(int d, int x, int y, int z, int id){
		if(isServer){			
			serverchunkcache.setBlock(this, d, x, y, z, id, 0);
		}else{
			chunkcache.setBlock(this, d, x, y, z, id, 0);
		}
	}

	public void setblockandmetanonotify(int d, int x, int y, int z, int id, int meta){	
		if(isServer){
			serverchunkcache.setBlock(this, d, x, y, z, id, meta);
		}else{
			chunkcache.setBlock(this, d, x, y, z, id, meta);
		}
	}
	
	//Use these in your destructive entity routines so that server permissions are automagically checked!
	//Use these in your block routines so that server permissions are automagically checked!
	public boolean setblockWithPerm(Entity e, int d, int x, int y, int z, int id){
		if(isServer){	
			if(!BreakChecks.canChangeBlock(e, d, x, y, z, id, 0))return false;		
			serverchunkcache.setBlock(this, d, x, y, z, id, 0);
			if(serverchunkcache.isDecorated(d, x, y, z)){				
				NotifyBlockTicker.addNotifyBlocksAround(d, x, y, z);
			}
			if(e != null && e instanceof Player){
				Player pl = (Player)e;
				ToDoList.onPlaced(pl, d, x, y, z, id);
			}
		}else{
			chunkcache.setBlock(this, d, x, y, z, id, 0);
			if(DangerZone.start_server){ //Single player
				NotifyBlockTicker.addNotifyBlocksAround(d, x, y, z);
			}
		}
		return true;
	}

	public boolean setblockandmetaWithPerm(Entity e, int d, int x, int y, int z, int id, int meta){	
		if(isServer){
			if(!BreakChecks.canChangeBlock(e, d, x, y, z, id, meta))return false;	
			serverchunkcache.setBlock(this, d, x, y, z, id, meta);
			if(serverchunkcache.isDecorated(d, x, y, z)){
				NotifyBlockTicker.addNotifyBlocksAround(d, x, y, z);
			}
			if(e != null && e instanceof Player){
				Player pl = (Player)e;
				ToDoList.onPlaced(pl, d, x, y, z, id);
			}
		}else{
			chunkcache.setBlock(this, d, x, y, z, id, meta);
			if(DangerZone.start_server){ //Single player
				NotifyBlockTicker.addNotifyBlocksAround(d, x, y, z);
			}
		}
		return true;
	}
	
	public boolean setblocknonotifyWithPerm(Entity e, int d, int x, int y, int z, int id){
		if(isServer){
			if(!BreakChecks.canChangeBlock(e, d, x, y, z, id, 0))return false;	
			serverchunkcache.setBlock(this, d, x, y, z, id, 0);
		}else{
			chunkcache.setBlock(this, d, x, y, z, id, 0);
		}
		return true;
	}

	public boolean setblockandmetanonotifyWithPerm(Entity e, int d, int x, int y, int z, int id, int meta){	
		if(isServer){
			if(!BreakChecks.canChangeBlock(e, d, x, y, z, id, meta))return false;	
			serverchunkcache.setBlock(this, d, x, y, z, id, meta);
		}else{
			chunkcache.setBlock(this, d, x, y, z, id, meta);
		}
		return true;
	}
	
	//only called on Server
	public void decorate(World world, int dimension, Biome b, int chunkx, int chunkz){
		if(!this.isServer)return;
		//Call the world decorators!
		for(int i=0;i<WorldDecorators.WorldDecoratorMAX;i++){
			if(WorldDecorators.WorldDecoratorArray[i] == null)break; //done
			WorldDecorators.WorldDecoratorArray[i].decorate(world, dimension, b, chunkx, chunkz);
		}
	}
	
	/*
	 * start with createEntityByName(),
	 * then set parameters as you want in the entity,
	 * then call e.init(), 		-- not really necessary on client side!
	 * then spawnEntityInWorld().
	 */
	public Entity createEntityByName(String name, int dimension, double x, double y, double z){	
		Entity e = ServerHooker.spawnEntityByName(name, this, dimension, x, y, z);
		if(e == null)e = Entities.spawnEntityByName(name, this);
		if(e == null)return null;
		e.dimension = dimension;
		e.posx = x;
		e.posy = y;
		e.posz = z;
		e.rotation_yaw = e.rotation_yaw_head = this.rand.nextInt(360);
		//caller should call the entitiy's init() function!!!
		return e;
	}
	
	/*
	 * spawning an entity is a two-step process, 
	 * to give the caller a chance to set/change specific values after creating the entity.
	 */
	public void spawnEntityInWorld(Entity e){
		if(this.isServer){ //Server sends to all players
			if(DangerZone.server.entityManager.addEntity(e) > 0){	
				DangerZone.server.sendSpawnEntityToAll(e);
			}
		}	
	}
	
	//ONLY ON CLIENT!!!!
	public Particle createParticleByName(String name, int dimension, double x, double y, double z){	
		if(this.isServer)return null;
		Particle e = Particles.spawnParticleByName(name);
		if(e == null)return null;
		e.dimension = dimension;
		e.posx = x;
		e.posy = y;
		e.posz = z;
		return e;
	}

	//ONLY ON CLIENT!!!!
	public void spawnParticleInWorld(Particle e){
		if(this.isServer)return;		
		DangerZone.particleManager.addParticle(e);
	}
	
	public void playSound(String name, int dimension, double posx, double posy, double posz, float volume, float frequency){
		if(name == null)return;
		if(name.equals(""))return;
		if(this.isServer){ //Server sends to all players
			DangerZone.server.sendSoundToAll(name, dimension, posx, posy, posz, volume, frequency);
		}else{
			//Client sends to server and plays its own.
			DangerZone.server_connection.sendSound(name, dimension, posx, posy, posz, volume, frequency);
			DangerZone.soundmangler.playSound(name, volume, frequency, dimension, posx, posy, posz);
		}
	}
	
	//For things like raindrops, that should only be played on the client, and when the player is very close
	public void playSoundCloseClient(String name, int dimension, double posx, double posy, double posz, float volume, float frequency){
		if(name == null)return;
		if(name.equals(""))return;
		if(!this.isServer){
			DangerZone.soundmangler.playSoundClose(name, volume, frequency, dimension, posx, posy, posz);
		}
	}
	
	public boolean isDaytime(){
		return this.timetimer < (lengthOfDay/2);
	}
	
	public int getTimeOfDay(){
		return this.timetimer;
	}
	
	public int getLengthOfDay(){
		return lengthOfDay;
	}
	

}
