package dangerzone.threads;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import dangerzone.Chunk;
import dangerzone.DangerZone;
import dangerzone.entities.Entity;

/*
 * So that server cache doesn't have to wait for chunks being written to disk.
 */
public class ChunkWriterThread  implements Runnable   {

	public  List<Chunk> chunk_list; 
	public  List<Chunk> chunk_keep_list; 
	public  List<Entity> ent_list; 
	public  Lock chunk_list_lock = new ReentrantLock();

	public void run()  {

		chunk_list = new ArrayList<Chunk>();
		chunk_keep_list = new ArrayList<Chunk>();
		ent_list = new ArrayList<Entity>();
		Chunk ck = null;
		Entity ent = null;
		//int counter = 0;

		while(true){
			
				try {
					Thread.sleep(20);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				
				chunk_list_lock.lock();
				//counter++;
				//if((counter%50)==0)System.out.printf("keep %d,  remove %d, ents %d\n", chunk_keep_list.size(), chunk_list.size(),  ent_list.size());
				
				//flush out the chunk but don't remove entities
				while(chunk_keep_list.size() > 0){
					ck = chunk_keep_list.get(0);
					chunk_keep_list.remove(0);
					chunk_list_lock.unlock();
					//System.out.printf("Writing keep %d, %d\n", ck.chunkX, ck.chunkZ);
					ck.saveToDisk(false);
					ck = null;
					Thread.yield();
					chunk_list_lock.lock();
				}
				
				//flush out the chunk and remove entities
				while(chunk_list.size() > 0){
					ck = chunk_list.get(0);
					chunk_list.remove(0);
					chunk_list_lock.unlock();
					//System.out.printf("Writing chunk %d, %d\n", ck.chunkX, ck.chunkZ);
					ck.saveToDisk(true);
					ck = null;
					Thread.yield();
					chunk_list_lock.lock();
				}
				
				//fetch a chunk for stray entities that wander off the playing field and don't have an existing chunk underneath to get saved into!!!
				while(ent_list.size() > 0){
					ent = ent_list.get(0);
					ent_list.remove(0);
					chunk_list_lock.unlock();
					//System.out.printf("Writing stray entity\n");
					//make or get the chunk
					DangerZone.server_chunk_cache.getDecoratedChunk(ent.dimension, (int)ent.posx, 100, (int)ent.posz);
					//now it has a chunk and will get flushed out with the chunk properly next go-round...
					//System.out.printf("saved: %s @ %f, %f\n", ent.uniquename, ent.posx, ent.posz);
					ent = null;
					Thread.yield();
					chunk_list_lock.lock();
				}

				chunk_list_lock.unlock();			
		}

	}
	
	
	public void addChunk(Chunk c){
		chunk_list_lock.lock(); 
		int i = chunk_list.size();
		for(int j=0;j<i;j++){
			if(c == chunk_list.get(j)){
				//it's already on the list
				chunk_list_lock.unlock();
				return;
			}
		}
		chunk_list.add(c);			
		chunk_list_lock.unlock();
		return;
	}
	
	public void addEntity(Entity e){
		chunk_list_lock.lock(); 
		int i = ent_list.size();
		for(int j=0;j<i;j++){
			if(e == ent_list.get(j)){
				//it's already on the list
				chunk_list_lock.unlock();
				return;
			}
		}
		ent_list.add(e);			
		chunk_list_lock.unlock();
		return;
	}
	
	public void addChunkKeep(Chunk c){
		chunk_list_lock.lock(); 
		int i;		
		i = chunk_list.size();
		for(int j=0;j<i;j++){
			if(c == chunk_list.get(j)){
				//it's already on the list
				chunk_list_lock.unlock();
				return;
			}
		}
		i = chunk_keep_list.size();
		for(int j=0;j<i;j++){
			if(c == chunk_keep_list.get(j)){
				//it's already on the list
				chunk_list_lock.unlock();
				return;
			}
		}
		chunk_keep_list.add(c);			
		chunk_list_lock.unlock();
		return;
	}
	
	public Chunk getChunk(int d, int cx, int cz){
		chunk_list_lock.lock(); 
		Chunk c;
		int i = chunk_list.size();
		for(int j=0;j<i;j++){
			c = chunk_list.get(j);
			if(d == c.dimension && c.chunkX == cx && c.chunkZ == cz){
				//it's already on the list
				chunk_list.remove(j);
				chunk_list_lock.unlock();
				return c;
			}
		}
		
		i = chunk_keep_list.size();
		for(int j=0;j<i;j++){
			c = chunk_keep_list.get(j);
			if(d == c.dimension && c.chunkX == cx && c.chunkZ == cz){
				//it's already on the list
				chunk_keep_list.remove(j);
				chunk_list_lock.unlock();
				return c;
			}
		}		
		chunk_list_lock.unlock();
		return null;
	}
	

	public int getSize(){
		chunk_list_lock.lock(); 
		int i = chunk_list.size();			//one fish
		i += chunk_keep_list.size();		//two fish
		chunk_list_lock.unlock();
		return i;
	}
	
	


}
