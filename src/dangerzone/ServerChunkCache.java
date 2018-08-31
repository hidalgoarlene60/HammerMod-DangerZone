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

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


//This cache has to handle multiple players and creatures running amok in multiple dimensions.
//A bit more complex than the client chunk cache, but the server has no rendering to do... so no problem...

public class ServerChunkCache {
	
	public Lock lock = new ReentrantLock();	
	public Lock slowlock = new ReentrantLock();	
	
	private int hashwidth = 512;
	private int hashmask = 0x1ff;
	private int sizes[];
	private int startsize = 128;
	public Chunk chunkhash[][];
	public Chunk lastchunk = null;
	//private int lastd, lastchunkx, lastchunkz; //because nine times out of ten it's the same!
	public int nextclean = 0;
	
	
	public ServerChunkCache(){
		int i,j;
		sizes = new int[hashwidth];
		chunkhash = new Chunk[hashwidth][];
		for(i=0;i<hashwidth;i++){
			sizes[i] = startsize;
			chunkhash[i] = new Chunk[startsize];
			for(j=0;j<startsize;j++){
				chunkhash[i][j] = null;
			}		
		}
	}
	
	private Chunk findChunk(int dimension, int x, int y, int z){
		int i, j, h;
		int k;
		i = (x>>4);
		j = (z>>4);
		if(lastchunk != null && dimension == lastchunk.dimension && i == lastchunk.chunkX && j == lastchunk.chunkZ){ //Cheap and dirty trick. Almost 100% hit rate
			return lastchunk;
		}
		
		h = ((i*16)+j)&hashmask;
		for(k=0;k<sizes[h];k++){
			if(chunkhash[h][k] == null)return null; //list always terminated by null! not found
			if(chunkhash[h][k].dimension == dimension && chunkhash[h][k].chunkX == i && chunkhash[h][k].chunkZ == j){
				lastchunk = chunkhash[h][k];
				if(k > 0){ //move active ones up the list, inactive ones fall slowly down the list
					chunkhash[h][k] = chunkhash[h][k-1];
					chunkhash[h][k-1] = lastchunk;
				}				
				return lastchunk;
			}
		}		
		return null; //not found
	}
	
	private Chunk findChunkUnlocked(int dimension, int x, int y, int z){
		int i, j, h;
		int k;
		i = (x>>4);
		j = (z>>4);
		Chunk t = lastchunk;
		if(t != null && dimension == t.dimension && i == t.chunkX && j == t.chunkZ){ //Cheap and dirty trick. Almost 100% hit rate
			return t;
		}
		
		h = ((i*16)+j)&hashmask;
		for(k=0;k<sizes[h];k++){
			if(chunkhash[h][k] == null)return null; //list always terminated by null! not found
			if(chunkhash[h][k].dimension == dimension && chunkhash[h][k].chunkX == i && chunkhash[h][k].chunkZ == j){
				return chunkhash[h][k];
			}
		}		
		return null; //not found
	}
	
	public boolean chunkExists(int dimension, int x, int y, int z){
		int i, j, h;
		int k;
		i = (x>>4);
		j = (z>>4);
		lock.lock();
		if(lastchunk != null && dimension == lastchunk.dimension && i == lastchunk.chunkX && j == lastchunk.chunkZ){ //Cheap and dirty trick. Almost 100% hit rate
			lock.unlock();
			return true;
		}
		
		h = ((i*16)+j)&hashmask;
		for(k=0;k<sizes[h];k++){
			if(chunkhash[h][k] == null){
				lock.unlock();
				return false; //list always terminated by null! not found
			}
			if(chunkhash[h][k].dimension == dimension && chunkhash[h][k].chunkX == i && chunkhash[h][k].chunkZ == j){	
				lock.unlock();
				return true;
			}
		}
		lock.unlock();
		return false; //not found
	}
	
	//ONLY called from Worldrenderer in single-player mode.
	public void releaseAllVBOs(){
		int i,j;
		Chunk c = null;
		lock.lock();
		for(i=0;i<hashwidth;i++){
			for(j=0;j<sizes[i];j++){
				c = chunkhash[i][j];
				if(c == null)break; //end of list
				DangerZone.wr.deleteVBOlist(c.VBOids);
				c.VBOids = new long[20];
			}		
		}
		lock.unlock();
	}
	
	public Chunk getChunk(World w, int dimension, int x, int y, int z){
		if(x < 0 || y < 0 || z < 0)return null;
		lock.lock();		
		Chunk t = findChunk(dimension, x, y, z);
		lock.unlock();
		return t;
	}
	

	public Chunk getDecoratedChunkForRenderer(int d, int x, int y, int z){
		if(x < 0 || y < 0 || z < 0)return null;
		lock.lock(); 
		Chunk t = findChunk(d, x, y, z);			
		lock.unlock();
		return t;
	}
	
	public List<String> getChunkOwners(int d, int x, int y, int z){
		if(x < 0 || y < 0 || z < 0)return null;
		lock.lock(); 
		Chunk t = findChunk(d, x, y, z);
		lock.unlock();
		if(t != null){
			return t.ownernames;
		}		
		return null;	
	}
	
	public void setChunkOwners(int d, int x, int y, int z, List<String> newowners){
		if(x < 0 || y < 0 || z < 0)return;
		lock.lock(); 
		Chunk t = findChunk(d, x, y, z);
		lock.unlock();
		if(t != null){
			t.setowners(newowners);
			if(DangerZone.server != null)DangerZone.server.sendChunkMetaUpdateToAll(t);
		}		
	}
	
	public Chunk getDecoratedChunk(int d, int x, int y, int z){
		if(x < 0 || y < 0 || z < 0)return null;
		lock.lock(); 
		Chunk t = findChunk(d, x, y, z);
		
		if(t != null){
			if(t.isDecorated != 0){
				lock.unlock(); 
				return t;	//Found and decorated!
			}
			//it's not decorated, or maybe not even valid.
			lock.unlock();
			
			//re-lock with the SLOW lock!
			slowlock.lock();
			if(t.isValid == 0){
				if(!t.readFromDisk()){
					//Not on disk...	generate a new one...	
					t.generate(DangerZone.server_world, d, x, y, z);
				}
				//one way or the other, it is valid now!
			}
			//now just make sure it is decorated
			if(t.isDecorated == 0){
				t.decorate(DangerZone.server_world, d, x, y, z); //This DOESN'T deadlock because it is thread-based and I can have it as many times as I want.
			}	
			slowlock.unlock();
			return t;
		}
		
		//chunk was null. make a new one.
		t = DangerZone.chunkwriter.getChunk(d, x>>4, z>>4);
		if(t == null)t = new Chunk(d, x, y, z);
		
		//add this in so they all know 
		this.addCacheChunk(t);
		lock.unlock();
		
		//re-lock with the SLOW lock!
		slowlock.lock();
		if(t.isValid == 0){
			if(!t.readFromDisk()){
				//Not on disk...	generate a new one...	
				t.generate(DangerZone.server_world, d, x, y, z);
			}
			//one way or the other, it is valid now!
		}
		//now just make sure it is decorated
		if(t.isDecorated == 0){
			t.decorate(DangerZone.server_world, d, x, y, z); //This DOESN'T deadlock because it is thread-based and I can have it as many times as I want.
		}	
		slowlock.unlock();
		return t;	
	}
	
	public short[] getDecoratedChunkLevelData(int d, int x, int y, int z){
		if(x < 0 || y < 0 || z < 0)return null;
		lock.lock(); 
		Chunk t = findChunk(d, x, y, z);
		if(t != null){
			if(t.isDecorated != 0){
				lock.unlock(); 
				return t.blockdata[y];	//Found and decorated!
			}
			//it's not decorated, or maybe not even valid.
			lock.unlock();
			
			//re-lock with the SLOW lock!
			slowlock.lock();
			if(t.isValid == 0){
				if(!t.readFromDisk()){
					//Not on disk...	generate a new one...	
					t.generate(DangerZone.server_world, d, x, y, z);
				}
				//one way or the other, it is valid now!
			}
			//now just make sure it is decorated
			if(t.isDecorated == 0){
				t.decorate(DangerZone.server_world, d, x, y, z); //This DOESN'T deadlock because it is thread-based and I can have it as many times as I want.
			}	
			slowlock.unlock();
			return t.blockdata[y];
		}
		
		//chunk was null. make a new one.	
		t = DangerZone.chunkwriter.getChunk(d, x>>4, z>>4);
		if(t == null)t = new Chunk(d, x, y, z);
		
		//add this in so they all know 
		this.addCacheChunk(t);
		lock.unlock();
		
		//re-lock with the SLOW lock!
		slowlock.lock();
		if(t.isValid == 0){
			if(!t.readFromDisk()){
				//Not on disk...	generate a new one...	
				t.generate(DangerZone.server_world, d, x, y, z);
			}
			//one way or the other, it is valid now!
		}
		//now just make sure it is decorated
		if(t.isDecorated == 0){
			t.decorate(DangerZone.server_world, d, x, y, z); //This DOESN'T deadlock because it is thread-based and I can have it as many times as I want.
		}	
		slowlock.unlock();
		return t.blockdata[y];
	}
	
	//NO WAITING!!!
	public long[] getDecoratedChunkVBOids(int d, int x, int y, int z){
		if(x < 0 || y < 0 || z < 0)return null;
		lock.lock(); 
		Chunk t = findChunk(d, x, y, z);
		lock.unlock();
		if(t == null){			
			return null;
		}		
		if(t.isDecorated != 0){
			return t.VBOids;	//Found and decorated!
		}			
		return null;
	}
	
	public short[] getDecoratedChunkMetaData(int d, int x, int y, int z){
		if(x < 0 || y < 0 || z < 0)return null;
		lock.lock(); 
		Chunk t = findChunk(d, x, y, z);
		if(t != null){
			if(t.isDecorated != 0){
				lock.unlock(); 
				return t.metadata[y];	//Found and decorated!
			}
			//it's not decorated, or maybe not even valid.
			lock.unlock();
			
			//re-lock with the SLOW lock!
			slowlock.lock();
			if(t.isValid == 0){
				if(!t.readFromDisk()){
					//Not on disk...	generate a new one...	
					t.generate(DangerZone.server_world, d, x, y, z);
				}
				//one way or the other, it is valid now!
			}
			//now just make sure it is decorated
			if(t.isDecorated == 0){
				t.decorate(DangerZone.server_world, d, x, y, z); //This DOESN'T deadlock because it is thread-based and I can have it as many times as I want.
			}	
			slowlock.unlock();
			return t.metadata[y];
		}
		
		//chunk was null. make a new one.	
		t = DangerZone.chunkwriter.getChunk(d, x>>4, z>>4);
		if(t == null)t = new Chunk(d, x, y, z);
		
		//add this in so they all know 
		this.addCacheChunk(t);
		lock.unlock();
		
		//re-lock with the SLOW lock!
		slowlock.lock();
		if(t.isValid == 0){
			if(!t.readFromDisk()){
				//Not on disk...	generate a new one...	
				t.generate(DangerZone.server_world, d, x, y, z);
			}
			//one way or the other, it is valid now!
		}
		//now just make sure it is decorated
		if(t.isDecorated == 0){
			t.decorate(DangerZone.server_world, d, x, y, z); //This DOESN'T deadlock because it is thread-based and I can have it as many times as I want.
		}	
		slowlock.unlock();
		return t.metadata[y];
	}
	
	public short[] getDecoratedChunkDrawn(int d, int x, int y, int z){
		if(x < 0 || y < 0 || z < 0)return null;
		lock.lock(); 
		Chunk t = findChunk(d, x, y, z);
		lock.unlock();
		if(t == null){			
			return null;
		}
		
		if(t.isDecorated != 0){
			return t.drawn;	//Found and decorated!
		}
		
		return null;
	}
	
	public short[] getDecoratedChunkLightmap(int d, int x, int y, int z){
		if(x < 0 || y < 0 || z < 0)return null;
		//lock.lock(); 
		Chunk t = findChunkUnlocked(d, x, y, z);
		//lock.unlock(); 
		if(t == null){
			return null;
		}
		
		if(t.isDecorated != 0){			
			if(t.lightmap == null)return null;
			return t.lightmap[y];	//Found and decorated!
		}
		
		return null;
	}
	
	public void clearDecoratedChunkLightmap(int d, int x, int y, int z){
		if(x < 0 || y < 0 || z < 0)return;
		lock.lock(); 
		Chunk t = findChunk(d, x, y, z);
		lock.unlock(); 
		if(t == null){
			return;
		}
		
		if(t.isDecorated != 0){			
			if(t.lightmap == null)return;
			t.lightmap[y] = null;	//Found and decorated!
			return;
		}
		
	}
	
	public void setDecoratedChunkLightmap(int d, int x, int y, int z, float v){
		if(x < 0 || y < 0 || z < 0)return;
		lock.lock(); 
		Chunk t = findChunk(d, x, y, z);
		lock.unlock(); 
		if(t == null){
			return;
		}
		
		if(t.isDecorated != 0){
			if(t.lightmap == null){
				t.lightmap = new short[256][];
			}
			if(t.lightmap[y] == null){
				t.lightmap[y] = new short[256];
			}
			int m = x&0x0f;
			int n = z&0x0f;
			float tmp = v*1000;
			t.lightmap[y][m*16+n] = (short)tmp;
			return;
		}
		
		return;
	}
		
	
	public void addCacheChunk(Chunk c){
		int h;
		int i;
		Chunk t;
		
		t = findChunk(c.dimension, c.chunkX<<4, 0, c.chunkZ<<4);
		if(t != null){
			return; //should not happen!
		}
		
		h = ((c.chunkX*16)+c.chunkZ)&hashmask;
		for(i=0;i<sizes[h];i++){
			if(i == sizes[h]-1){ //gotta grow the list! Always keep a null terminator!
				int oldsize = sizes[h];
				int newsize = oldsize+100; //add some
				Chunk newlist[] = new Chunk[newsize];
				int k;
				for(k=0;k<newsize;k++)newlist[k] = null; //make sure all null
				for(k=0;k<oldsize;k++)newlist[k] = chunkhash[h][k]; //copy old list
				for(k=0;k<oldsize;k++)chunkhash[h][k] = null; //help the java realize these are gone
				chunkhash[h] = newlist;
				sizes[h] = newsize;				
			}
			if(chunkhash[h][i] == null){
				chunkhash[h][i] = c;
				return;
			}
		}
	}
	
	public void cleanCacheRow(boolean should_flush){
		int h;
		int i;

		lock.lock();
		lastchunk = null; //Clear, just because....
		int disttoclean = DangerZone.entityupdatedist+32;
		
		//let's do some cleanup!
		for(i=0;i<sizes[nextclean] && !DangerZone.dimension_change_in_progress;i++){
			if(chunkhash[nextclean][i] == null)break; //end of list
			if(DangerZone.server.findNearestPlayerDistToHere(chunkhash[nextclean][i].dimension, chunkhash[nextclean][i].chunkX<<4, chunkhash[nextclean][i].chunkZ<<4) > disttoclean){
				//clean this thing! No one is near it!
				h = i;
				//System.out.printf("Cleaning %d: %d, %d\n", chunkhash[nextclean][i].dimension, chunkhash[nextclean][i].chunkX, chunkhash[nextclean][i].chunkZ  );

				DangerZone.chunkwriter.addChunk(chunkhash[nextclean][h]);
				
				if(DangerZone.wr != null){ //must be in passthru mode...
					DangerZone.wr.deleteVBOlist(chunkhash[nextclean][h].VBOids);
				}
				
				while(h+1 < sizes[nextclean]){				
					chunkhash[nextclean][h] = chunkhash[nextclean][h+1];
					if(chunkhash[nextclean][h] == null)break;
					h++;
				}
				//System.out.printf("cleaned one!\n");
			}else{
				//Do some auto-saving WITHOUT removing the chunk
				if(chunkhash[nextclean][i].must_be_written != 0 && should_flush){
					DangerZone.chunkwriter.addChunkKeep(chunkhash[nextclean][i]); 
				}				
			}
		}
		//set up for next cleaning run...
		nextclean++;
		if(nextclean > hashmask)nextclean = 0;
		lock.unlock();
		
	}
	
	public int getBlock(World w, int d, int x, int y, int z){
		if(x < 0 || y < 0 || z < 0)return 0;
		if(y > 255)return 0;
		
		//quick check without locking
		Chunk t = lastchunk;
		if(t != null){
			if(t.isValid != 0 && t.dimension == d){
				if((x>>4) == t.chunkX && (z>>4) == t.chunkZ){
					return t.getblock(x, y, z);
				}
			}
		}
				
		lock.lock();		
		t = findChunk(d, x, y, z);
		if(t != null){
			if(t.isValid != 0){			
				lock.unlock();
				return t.getblock(x, y, z);
			}
			//it's not decorated, or maybe not even valid.
			lock.unlock();
			
			//re-lock with the SLOW lock!
			slowlock.lock();
			if(t.isValid == 0){
				if(!t.readFromDisk()){
					//Not on disk...	generate a new one...	
					t.generate(DangerZone.server_world, d, x, y, z);
				}
				//one way or the other, it is valid now!
			}
			slowlock.unlock();
			return t.getblock(x, y, z);
		}
		
		//chunk was null. make a new one.	
		t = DangerZone.chunkwriter.getChunk(d, x>>4, z>>4);
		if(t == null)t = new Chunk(d, x, y, z);
		
		//add this in so they all know 
		this.addCacheChunk(t);
		lock.unlock();
		
		//re-lock with the SLOW lock!
		slowlock.lock();
		if(t.isValid == 0){
			if(!t.readFromDisk()){
				//Not on disk...	generate a new one...	
				t.generate(DangerZone.server_world, d, x, y, z);
			}
			//one way or the other, it is valid now!
		}
		slowlock.unlock();		
		return t.getblock(x, y, z);
	}
	
	public int getBlockTry(int d, int x, int y, int z){
		if(x < 0 || y < 0 || z < 0)return 0;
		if(y > 255)return 0;
		//quick check without locking
		Chunk t = lastchunk;
		if(t != null){
			if(t.isValid != 0 && t.dimension == d){
				if((x>>4) == t.chunkX && (z>>4) == t.chunkZ){
					return t.getblock(x, y, z);
				}
			}
		}
		
		lock.lock();		
		t = findChunk(d, x, y, z);
		if(t != null){
			if(t.isValid != 0 && t.isDecorated != 0){			
				lock.unlock();
				return t.getblock(x, y, z);
			}
		}
		lock.unlock();
		return 0;
	}
	
	public int getBlockmeta(World w, int d, int x, int y, int z){
		int b;
		if(x < 0 || y < 0 || z < 0)return 0;
		if(y > 255)return 0;
		//quick check without locking
		Chunk t = lastchunk;
		if(t != null){
			if(t.isValid != 0 && t.dimension == d){
				if((x>>4) == t.chunkX && (z>>4) == t.chunkZ){
					return t.getblockmeta(x, y, z);
				}
			}
		}
		
		lock.lock();		
		t = findChunk(d, x, y, z);
		if(t != null){
			if(t.isValid != 0){			
				b = t.getblockmeta(x, y, z);
				lock.unlock();
				return b;
			}
			//it's not decorated, or maybe not even valid.
			lock.unlock();
			
			//re-lock with the SLOW lock!
			slowlock.lock();
			if(t.isValid == 0){
				if(!t.readFromDisk()){
					//Not on disk...	generate a new one...	
					t.generate(DangerZone.server_world, d, x, y, z);
				}
				//one way or the other, it is valid now!
			}
			slowlock.unlock();
			b = t.getblockmeta(x, y, z);
			return b;
		}
		
		//chunk was null. make a new one.	
		t = DangerZone.chunkwriter.getChunk(d, x>>4, z>>4);
		if(t == null)t = new Chunk(d, x, y, z);
		
		//add this in so they all know 
		this.addCacheChunk(t);
		lock.unlock();
		
		//re-lock with the SLOW lock!
		slowlock.lock();
		if(t.isValid == 0){
			if(!t.readFromDisk()){
				//Not on disk...	generate a new one...	
				t.generate(DangerZone.server_world, d, x, y, z);
			}
			//one way or the other, it is valid now!
		}
		slowlock.unlock();	
		
		b = t.getblockmeta(x, y, z);
		return b;
	}
	
	public void setBlock(World w, int d, int x, int y, int z, int id, int meta){
		int b, m;
		if(x < 0 || y < 0 || z < 0)return;
		if(y > 255)return;
		Chunk t = lastchunk;
		if(t != null){
			if(t.isValid != 0 && t.dimension == d){
				if((x>>4) == t.chunkX && (z>>4) == t.chunkZ){
					b = t.getblock(x, y, z);
					m = t.getblockmeta(x, y, z);
					if(id == b && meta == m){
						return; //found and nothing changed
					}
					if(id != b)t.setblock(x, y, z, id);
					if(meta != m)t.setblockmeta(x, y, z, meta);
					//t.isChanged = 1; 
					//t.must_be_written = 1; 
					//TELL PLAYERS WE CHANGED A BLOCK!
					if(!DangerZone.start_client && !t.amDecorating && !t.amGenerating)DangerZone.server.sendBlockToAll(d, x, y, z, id, meta);
					return;	
				}
			}
		}
		
		lock.lock();		
		t = findChunk(d, x, y, z);
		if(t != null){
			if(t.isValid != 0){	
				lock.unlock();
				b = t.getblock(x, y, z);
				m = t.getblockmeta(x, y, z);
				if(id == b && meta == m){
					return; //found and nothing changed
				}
				if(id != b)t.setblock(x, y, z, id);
				if(meta != m)t.setblockmeta(x, y, z, meta);
				//t.isChanged = 1; 
				//t.must_be_written = 1; 
				//TELL PLAYERS WE CHANGED A BLOCK!
				if(!DangerZone.start_client && !t.amDecorating && !t.amGenerating)DangerZone.server.sendBlockToAll(d, x, y, z, id, meta);
				return;	
			}
			//it's not valid.
			lock.unlock();
			
			//re-lock with the SLOW lock!
			slowlock.lock();
			if(t.isValid == 0){
				if(!t.readFromDisk()){
					//Not on disk...	generate a new one...	
					t.generate(DangerZone.server_world, d, x, y, z);
				}
				//one way or the other, it is valid now!
			}
			slowlock.unlock();
			
			b = t.getblock(x, y, z);
			m = t.getblockmeta(x, y, z);
			if(id == b && meta == m){
				return; //found and nothing changed
			}
			if(id != b)t.setblock(x, y, z, id);
			if(meta != m)t.setblockmeta(x, y, z, meta);

			//t.isChanged = 1; 
			//t.must_be_written = 1; 
			//TELL PLAYERS WE CHANGED A BLOCK!
			if(!DangerZone.start_client)DangerZone.server.sendBlockToAll(d, x, y, z, id, meta);
			return;	
		}
		
		//chunk was null. make a new one.	
		t = DangerZone.chunkwriter.getChunk(d, x>>4, z>>4);
		if(t == null)t = new Chunk(d, x, y, z);
		
		//add this in so they all know 
		this.addCacheChunk(t);
		lock.unlock();
		
		//re-lock with the SLOW lock!
		slowlock.lock();
		if(t.isValid == 0){
			if(!t.readFromDisk()){
				//Not on disk...	generate a new one...	
				t.generate(DangerZone.server_world, d, x, y, z);
			}
			//one way or the other, it is valid now!
		}
		slowlock.unlock();	
		
		b = t.getblock(x, y, z);
		m = t.getblockmeta(x, y, z);
		if(id == b && meta == m){
			return; //found and nothing changed
		}
		if(id != b)t.setblock(x, y, z, id);
		if(meta != m)t.setblockmeta(x, y, z, meta);
		//t.isChanged = 1; 
		//t.must_be_written = 1; 
		//TELL PLAYERS WE CHANGED A BLOCK!
		if(!DangerZone.start_client)DangerZone.server.sendBlockToAll(d, x, y, z, id, meta);
		
	}
	
	//not terribly critical. Don't check disk or anything...
	public boolean isDecorated(int d, int x, int y, int z){
		boolean retflag = false;
		if(x < 0 || y < 0 || z < 0)return retflag;
		lock.lock();		
		Chunk t = findChunk(d, x, y, z);
		if(t != null){
			if(t.isValid != 0){			
				if(t.isDecorated != 0)retflag = true;
			}
		}
		lock.unlock();
		return retflag;		
	}
	
	
	public void shutdown(){
		int i, j;		
		Chunk t;
		lock.lock();
		for(i=0;i<hashwidth;i++){
			for(j=0;j<sizes[i];j++){
				t = chunkhash[i][j];
				if(t != null){
					if(t.isValid != 0){					
						t.saveToDisk(false);
					}
				}
			}		
		}
		//lock.unlock(); // LEAVE LOCKED!!!!
	}
	
	
	public void rebuildChunk(int d, int x, int y, int z){
		if(x < 0 || y < 0 || z < 0)return;
		lock.lock(); 

		int i, j, h;
		int k;
		boolean removed = false;
		i = (x>>4);
		j = (z>>4);

		h = ((i*16)+j)&hashmask;
		for(k=0;k<sizes[h];k++){
			if(chunkhash[h][k] == null)break; //list always terminated by null! not found
			if(chunkhash[h][k].dimension == d && chunkhash[h][k].chunkX == i && chunkhash[h][k].chunkZ == j){
				Chunk t = chunkhash[h][k];
				if(t.ownernames != null){
					break;
				}
				t.delete();
				removed = true;
	
				if(DangerZone.wr != null){ //must be in passthru mode...
					DangerZone.wr.deleteVBOlist(t.VBOids);
				}
				
				//move remaining list down
				j = k;
				while(j+1 < sizes[h]){				
					chunkhash[h][j] = chunkhash[h][j+1];
					if(chunkhash[h][j] == null)break;
					j++;
				}
				
				//clear last chunk
				lastchunk = null;
				break;
			}
		}


		lock.unlock();
		
		if(removed){
			Chunk t = getDecoratedChunk(d, x, y, z);
			if(t != null){
				DangerZone.server.sendChunkToAll(t);
			}
		}

	}
	

}

