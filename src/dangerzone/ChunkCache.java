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


//This works very well for one player accessing one dimension at a time.

public class ChunkCache {
	
	private Lock lock = new ReentrantLock();
	public Chunk chunks[];
	public static final int cachewidth = 64;
	public static final int cachemask = 0x03f;
	public static int nextrow = 0;
	public boolean passthru = false;
	
	
	public ChunkCache(){
		int i, j;
		chunks = new Chunk [cachewidth*cachewidth];
		for(i=0;i<cachewidth;i++){
			for(j=0;j<cachewidth;j++){
				chunks[i*cachewidth+j] = null;
			}
		}
		passthru = false;
	}
	
	public boolean chunkExists(int dimension, int x, int y, int z){
		int i, j;
		if(x < 0 || y < 0 || z < 0)return false;
		if(y > 255)return false;
		if(passthru)return DangerZone.server_chunk_cache.chunkExists(dimension, x, y, z);
		
		lock.lock();		
		i = (x>>4)&cachemask;
		j = (z>>4)&cachemask;
		if(chunks[i*cachewidth+j] != null){
			if(chunks[i*cachewidth+j].isValid != 0){
				if(chunks[i*cachewidth+j].dimension == dimension && chunks[i*cachewidth+j].chunkX == (x>>4) && chunks[i*cachewidth+j].chunkZ == (z>>4)){
					lock.unlock();
					return true;
				}
			}
		}
		lock.unlock();
		
		//Not found.		
		return false;
	}
	
	public Chunk getChunk(World w, int dimension, int x, int y, int z){
		int i, j;
		if(x < 0 || y < 0 || z < 0)return null;
		if(y > 255)return null;
		if(passthru)return DangerZone.server_chunk_cache.getChunk(DangerZone.server_world, dimension, x, y, z);
		
		lock.lock();		
		i = (x>>4)&cachemask;
		j = (z>>4)&cachemask;
		if(chunks[i*cachewidth+j] != null){
			if(chunks[i*cachewidth+j].isValid != 0){
				if(chunks[i*cachewidth+j].dimension == dimension && chunks[i*cachewidth+j].chunkX == (x>>4) && chunks[i*cachewidth+j].chunkZ == (z>>4)){
					lock.unlock();
					return chunks[i*cachewidth+j];
				}
			}
		}
		lock.unlock();
		
		//Not found. Go ask for it.
		DangerZone.server_connection.getDecoratedChunk(dimension, x, y, z);
		
		return null;
	}
	
	/*
	 * Clean up and clear out anything not used! Memory is scarce!
	 */
	public void cleanCacheRow( int dimension, int x, int y, int z){
		int i;
		int j, k;		
		if(passthru)return; //should not happen!
		
		int dist = DangerZone.entityupdatedist/16; //blocks to chunks
		if(DangerZone.renderdistance > dist)dist = DangerZone.renderdistance;
		
		lock.lock();		
		for(i=0;i<cachewidth;i++){
			if(chunks[nextrow*cachewidth+i] != null){
				if(chunks[nextrow*cachewidth+i].dimension != dimension){
					
						if(DangerZone.wr != null){
							DangerZone.wr.deleteVBOlist(chunks[nextrow*cachewidth+i].VBOids);
						}
					
					chunks[nextrow*cachewidth+i] = null;
					//System.out.printf("Cleaned one\n");
					continue;
				}
				j = chunks[nextrow*cachewidth+i].chunkX;
				k = chunks[nextrow*cachewidth+i].chunkZ;
				j <<= 4;
				k <<= 4;
				if(Math.sqrt((j-x)*(j-x) + (k-z)*(k-z))/16 > dist+4){ //too far away, clean it!
					//System.out.printf("Cleaned one\n");				
						if(DangerZone.wr != null){
							DangerZone.wr.deleteVBOlist(chunks[nextrow*cachewidth+i].VBOids);
						}
					
					chunks[nextrow*cachewidth+i] = null;
				}
			}
		}
		nextrow++;
		if(nextrow >= cachewidth)nextrow = 0;
		lock.unlock();
	}
	
	//ONLY called by worldrenderer on dimension change to free all the video memory!
	public void releaseAllVBOs(){
		if(passthru){
			DangerZone.server_chunk_cache.releaseAllVBOs();
			return;
		}
		lock.lock();
		int i, j;
		Chunk c = null;
		for(i=0;i<cachewidth;i++){
			for(j=0;j<cachewidth;j++){
				c = chunks[i*cachewidth+j];
				if(c != null){
					DangerZone.wr.deleteVBOlist(c.VBOids);
					c.VBOids = new long[20];
				}
			}
		}
		lock.unlock();
	}
	
	/*
	 * ONLY called by the world renderer.
	 */
	public Chunk getDecoratedChunkForRenderer(int dimension, int x, int y, int z){
		int i, j;
		if(x < 0 || y < 0 || z < 0)return null;
		if(y > 255)return null;
		if(passthru)return DangerZone.server_chunk_cache.getDecoratedChunkForRenderer(dimension, x, y, z);
		
		lock.lock();		
		i = (x>>4)&cachemask;
		j = (z>>4)&cachemask;

		if(chunks[i*cachewidth+j] != null){
			if(chunks[i*cachewidth+j].isValid != 0 && chunks[i*cachewidth+j].isDecorated != 0){
				if(chunks[i*cachewidth+j].dimension == dimension && chunks[i*cachewidth+j].chunkX == (x>>4) && chunks[i*cachewidth+j].chunkZ == (z>>4)){
						lock.unlock();
						return chunks[i*cachewidth+j];
				}
			}
		}
		lock.unlock();
		
		//Not found. Go ask the server for it...
		DangerZone.server_connection.getDecoratedChunk(dimension, x, y, z);
	
		return null;
	}
	
	public Chunk getDecoratedChunk(int dimension, int x, int y, int z){
		int i, j;
		if(x < 0 || y < 0 || z < 0)return null;
		if(y > 255)return null;
		if(passthru)return DangerZone.server_chunk_cache.getDecoratedChunk(dimension, x, y, z);
		
		lock.lock();		
		i = (x>>4)&cachemask;
		j = (z>>4)&cachemask;

		if(chunks[i*cachewidth+j] != null){
			if(chunks[i*cachewidth+j].isValid != 0 && chunks[i*cachewidth+j].isDecorated != 0){
				if(chunks[i*cachewidth+j].dimension == dimension && chunks[i*cachewidth+j].chunkX == (x>>4) && chunks[i*cachewidth+j].chunkZ == (z>>4)){
						lock.unlock();
						return chunks[i*cachewidth+j];
				}
			}
		}
		lock.unlock();
		
		//Not found. Go ask the server for it...
		DangerZone.server_connection.getDecoratedChunk(dimension, x, y, z);
	
		return null;
	}
	
	public short[] getDecoratedChunkLevelData(int dimension, int x, int y, int z){
		int i, j;
		if(x < 0 || y < 0 || z < 0)return null;
		if(y > 255)return null;
		if(passthru)return DangerZone.server_chunk_cache.getDecoratedChunkLevelData(dimension, x, y, z);
		
		//lock.lock();		
		i = (x>>4)&cachemask;
		j = (z>>4)&cachemask;

		if(chunks[i*cachewidth+j] != null){
			if(chunks[i*cachewidth+j].isValid != 0 && chunks[i*cachewidth+j].isDecorated != 0){
				if(chunks[i*cachewidth+j].dimension == dimension && chunks[i*cachewidth+j].chunkX == (x>>4) && chunks[i*cachewidth+j].chunkZ == (z>>4)){
						//lock.unlock();
						return chunks[i*cachewidth+j].blockdata[y];
				}
			}
		}
		//lock.unlock();
		
		//Not found. Go ask the server for it...
		DangerZone.server_connection.getDecoratedChunk(dimension, x, y, z);
	
		return null;
	}
	
	//ONLY THE WORLD RENDERER MAKES THIS CALL. DO NOT HANG HERE!!!
	public long[] getDecoratedChunkVBOids(int dimension, int x, int y, int z){
		int i, j;
		if(x < 0 || y < 0 || z < 0)return null;
		if(y > 255)return null;
		if(passthru){
			long VBOdata[] = DangerZone.server_chunk_cache.getDecoratedChunkVBOids(dimension, x, y, z);
			if(VBOdata == null){
				//Kick the server into fetching this chunk
				DangerZone.server_connection.getDecoratedChunk(dimension, x, y, z);
				return null;
			}
			return VBOdata;
		}
		
		//lock.lock();		
		i = (x>>4)&cachemask;
		j = (z>>4)&cachemask;

		if(chunks[i*cachewidth+j] != null){
			if(chunks[i*cachewidth+j].isValid != 0 && chunks[i*cachewidth+j].isDecorated != 0){
				if(chunks[i*cachewidth+j].dimension == dimension && chunks[i*cachewidth+j].chunkX == (x>>4) && chunks[i*cachewidth+j].chunkZ == (z>>4)){
						//lock.unlock();
						return chunks[i*cachewidth+j].VBOids;
				}
			}
		}
		//lock.unlock();
		
		//Not found. Go ask the server for it...
		DangerZone.server_connection.getDecoratedChunk(dimension, x, y, z);
	
		return null;
	}
	
	public short[] getDecoratedChunkLevelMetaData(int dimension, int x, int y, int z){
		int i, j;
		if(x < 0 || y < 0 || z < 0)return null;
		if(y > 255)return null;
		if(passthru)return DangerZone.server_chunk_cache.getDecoratedChunkMetaData(dimension, x, y, z);
		
		//lock.lock();		
		i = (x>>4)&cachemask;
		j = (z>>4)&cachemask;

		if(chunks[i*cachewidth+j] != null){
			if(chunks[i*cachewidth+j].isValid != 0 && chunks[i*cachewidth+j].isDecorated != 0){
				if(chunks[i*cachewidth+j].dimension == dimension && chunks[i*cachewidth+j].chunkX == (x>>4) && chunks[i*cachewidth+j].chunkZ == (z>>4)){
						//lock.unlock();
						return chunks[i*cachewidth+j].metadata[y];
				}
			}
		}
		//lock.unlock();
		
		//Not found. Go ask the server for it...
		DangerZone.server_connection.getDecoratedChunk(dimension, x, y, z);
	
		return null;
	}
	
	public short[] getDecoratedChunkDrawn(int dimension, int x, int y, int z){
		int i, j;
		if(x < 0 || y < 0 || z < 0)return null;
		if(y > 255)return null;
		if(passthru)return DangerZone.server_chunk_cache.getDecoratedChunkDrawn(dimension, x, y, z);
		
		//lock.lock();		
		i = (x>>4)&cachemask;
		j = (z>>4)&cachemask;

		if(chunks[i*cachewidth+j] != null){
			if(chunks[i*cachewidth+j].isValid != 0 && chunks[i*cachewidth+j].isDecorated != 0){
				if(chunks[i*cachewidth+j].dimension == dimension && chunks[i*cachewidth+j].chunkX == (x>>4) && chunks[i*cachewidth+j].chunkZ == (z>>4)){
						//lock.unlock();
						return chunks[i*cachewidth+j].drawn;
				}
			}
		}
		//lock.unlock();
		
		//Not found. Go ask the server for it...
		DangerZone.server_connection.getDecoratedChunk(dimension, x, y, z);
	
		return null;
	}
	
	public short[] getDecoratedChunkLightmap(int dimension, int x, int y, int z){
		int i, j;
		if(x < 0 || y < 0 || z < 0)return null;
		if(y > 255)return null;
		if(passthru)return DangerZone.server_chunk_cache.getDecoratedChunkLightmap(dimension, x, y, z);
	
		i = (x>>4)&cachemask;
		j = (z>>4)&cachemask;

		if(chunks[i*cachewidth+j] != null){
			if(chunks[i*cachewidth+j].isValid != 0 && chunks[i*cachewidth+j].isDecorated != 0){
				if(chunks[i*cachewidth+j].dimension == dimension && chunks[i*cachewidth+j].chunkX == (x>>4) && chunks[i*cachewidth+j].chunkZ == (z>>4)){
						if(chunks[i*cachewidth+j].lightmap == null)return null;
						return chunks[i*cachewidth+j].lightmap[y];
				}
			}
		}
	
		return null;
	}
	
	public void clearDecoratedChunkLightmap(int dimension, int x, int y, int z){
		int i, j;
		if(x < 0 || y < 0 || z < 0)return;
		if(y > 255)return;
		if(passthru){
			DangerZone.server_chunk_cache.clearDecoratedChunkLightmap(dimension, x, y, z);
			return;
		}
	
		i = (x>>4)&cachemask;
		j = (z>>4)&cachemask;

		if(chunks[i*cachewidth+j] != null){
			if(chunks[i*cachewidth+j].isValid != 0 && chunks[i*cachewidth+j].isDecorated != 0){
				if(chunks[i*cachewidth+j].dimension == dimension && chunks[i*cachewidth+j].chunkX == (x>>4) && chunks[i*cachewidth+j].chunkZ == (z>>4)){
						if(chunks[i*cachewidth+j].lightmap == null)return;
						chunks[i*cachewidth+j].lightmap[y] = null;
						return;
				}
			}
		}
	
	}
	
	public void setDecoratedChunkLightValue(int dimension, int x, int y, int z, float v){
		int i, j;
		int m, n;
		if(x < 0 || y < 0 || z < 0)return;
		if(y > 255)return;
		if(passthru){
			DangerZone.server_chunk_cache.setDecoratedChunkLightmap(dimension, x, y, z, v);
			return;
		}
	
		i = (x>>4)&cachemask;
		j = (z>>4)&cachemask;

		if(chunks[i*cachewidth+j] != null){
			if(chunks[i*cachewidth+j].isValid != 0 && chunks[i*cachewidth+j].isDecorated != 0){
				if(chunks[i*cachewidth+j].dimension == dimension && chunks[i*cachewidth+j].chunkX == (x>>4) && chunks[i*cachewidth+j].chunkZ == (z>>4)){
						if(chunks[i*cachewidth+j].lightmap == null){
							chunks[i*cachewidth+j].lightmap = new short[256][];
						}
						if(chunks[i*cachewidth+j].lightmap[y] == null){
							chunks[i*cachewidth+j].lightmap[y] = new short[256];
						}
						m = x&0x0f;
						n = z&0x0f;
						float tmp = v*1000;
						chunks[i*cachewidth+j].lightmap[y][m*16+n] = (short)(tmp);
						return;
				}
			}
		}
		return;
	}
		
	
	public void addCacheChunk(Chunk c){
		int i, j;
		if(passthru)return; //It is in the server cache now. That's all we need.
		
		lock.lock();
		i = (c.chunkX)&cachemask;
		j = (c.chunkZ)&cachemask;
		if(chunks[i*cachewidth+j] != null){
			if(DangerZone.wr != null){
				Chunk old = chunks[i*cachewidth+j];
				if(old.dimension == c.dimension && old.chunkX == c.chunkX && old.chunkZ == c.chunkZ){
					////Same chunk, re-use old VBOs...
					//for(i=0;i<20;i++){
					//	c.VBOids[i] = old.VBOids[i];
					//}
					DangerZone.wr.deleteVBOlist(old.VBOids);
				}else{
					//it is a new chunk from server. Do we even want it?
					int dx = (int) DangerZone.player.posx;
					int dz = (int) DangerZone.player.posz;
					int x = c.chunkX<<4;
					int z = c.chunkZ<<4;
					float newdist = (float) Math.sqrt((x-dx)*(x-dx)+(z-dz)*(z-dz));
					x = old.chunkX<<4;
					z = old.chunkZ<<4;
					float olddist = (float) Math.sqrt((x-dx)*(x-dx)+(z-dz)*(z-dz));
					if(olddist < newdist){ //no!
						lock.unlock();
						return;
					}
					DangerZone.wr.deleteVBOlist(chunks[i*cachewidth+j].VBOids);
				}
			}
		}
		chunks[i*cachewidth+j] = c;
		lock.unlock();
	}
	
	public int getBlock(World w, int d, int x, int y, int z){
		int i, j, b;
		if(x < 0 || y < 0 || z < 0)return 0;
		if(y > 255)return 0;
		if(passthru)return DangerZone.server_chunk_cache.getBlock(DangerZone.server_world, d, x, y, z);
		
		lock.lock();		
		i = (x>>4)&cachemask;
		j = (z>>4)&cachemask;
		if(chunks[i*cachewidth+j] != null){
			if(chunks[i*cachewidth+j].isValid != 0){
				if(chunks[i*cachewidth+j].dimension == d && chunks[i*cachewidth+j].chunkX == (x>>4) && chunks[i*cachewidth+j].chunkZ == (z>>4)){
					b = chunks[i*cachewidth+j].getblock(x, y, z);
					lock.unlock();
					return b;
				}
			}
		}
		lock.unlock();
		
		//Not found. Go ask for it.
		DangerZone.server_connection.getDecoratedChunk(d, x, y, z);
		
		return 0;
	}
	
	public int getBlockmeta(World w, int d, int x, int y, int z){
		int i, j, b;
		if(x < 0 || y < 0 || z < 0)return 0;
		if(y > 255)return 0;
		if(passthru)return DangerZone.server_chunk_cache.getBlockmeta(DangerZone.server_world, d, x, y, z);
		
		lock.lock();		
		i = (x>>4)&cachemask;
		j = (z>>4)&cachemask;
		if(chunks[i*cachewidth+j] != null){
			if(chunks[i*cachewidth+j].isValid != 0){
				if(chunks[i*cachewidth+j].dimension == d && chunks[i*cachewidth+j].chunkX == (x>>4) && chunks[i*cachewidth+j].chunkZ == (z>>4)){
					b = chunks[i*cachewidth+j].getblockmeta(x, y, z);
					lock.unlock();
					return b;
				}
			}
		}
		lock.unlock();
		
		//Not found. Go ask for it.
		DangerZone.server_connection.getDecoratedChunk(d, x, y, z);

		return 0;
	}
	
	public void setBlock(World w, int d, int x, int y, int z, int id, int meta){
		int i, j, b, m;
		if(x < 0 || y < 0 || z < 0)return;
		if(y > 255)return;
		if(passthru){
			DangerZone.server_chunk_cache.setBlock(DangerZone.server_world, d, x, y, z, id, meta);
			return;
		}
		
		lock.lock();		
		i = (x>>4)&cachemask;
		j = (z>>4)&cachemask;
		if(chunks[i*cachewidth+j] != null){
			if(chunks[i*cachewidth+j].isValid != 0){
				if(chunks[i*cachewidth+j].dimension == d && chunks[i*cachewidth+j].chunkX == (x>>4) && chunks[i*cachewidth+j].chunkZ == (z>>4)){
					
					b = chunks[i*cachewidth+j].getblock(x, y, z);
					m = chunks[i*cachewidth+j].getblockmeta(x, y, z);
					if(id == b && meta == m){
						lock.unlock();
						return; //found and nothing changed
					}
					if(id != b)chunks[i*cachewidth+j].setblock(x, y, z, id);
					if(meta != m)chunks[i*cachewidth+j].setblockmeta(x, y, z, meta);
					
					lock.unlock();
					
					//TELL SERVER WE CHANGED A BLOCK!
					DangerZone.server_connection.blockChanged(d, x, y, z, id, meta);
										
					return;
				}
			}
		}
		lock.unlock();
		
		return;
	}
	
	public void setBlockSilent(int d, int x, int y, int z, int id, int meta){
		int i, j, b, m;
		if(x < 0 || y < 0 || z < 0)return;
		if(y > 255)return;
		if(passthru){
			DangerZone.server_chunk_cache.setBlock(DangerZone.server_world, d, x, y, z, id, meta);
			return;
		}
		
		lock.lock();		
		i = (x>>4)&cachemask;
		j = (z>>4)&cachemask;
		if(chunks[i*cachewidth+j] != null){
			if(chunks[i*cachewidth+j].isValid != 0){
				if(chunks[i*cachewidth+j].dimension == d && chunks[i*cachewidth+j].chunkX == (x>>4) && chunks[i*cachewidth+j].chunkZ == (z>>4)){
					b = chunks[i*cachewidth+j].getblock(x, y, z);
					m = chunks[i*cachewidth+j].getblockmeta(x, y, z);
					if(id == b && meta == m){
						lock.unlock();
						return; //found and nothing changed
					}
					if(id != b)chunks[i*cachewidth+j].setblock(x, y, z, id);
					if(meta != m)chunks[i*cachewidth+j].setblockmeta(x, y, z, meta);
					lock.unlock();
					return;
				}
			}
		}
		lock.unlock();
		return;
	}
	
	public void setChunkMeta(int d, int x, int z, List<String> newowners){
		int i, j;
		if(x < 0 || z < 0)return;

		if(passthru){
			return; //nothing to do! Using server cache, which is already correct.
		}
		
		lock.lock();		
		i = (x>>4)&cachemask;
		j = (z>>4)&cachemask;
		if(chunks[i*cachewidth+j] != null){
			if(chunks[i*cachewidth+j].isValid != 0){
				if(chunks[i*cachewidth+j].dimension == d && chunks[i*cachewidth+j].chunkX == (x>>4) && chunks[i*cachewidth+j].chunkZ == (z>>4)){
					chunks[i*cachewidth+j].ownernames = newowners;
					lock.unlock();
					return;
				}
			}
		}
		lock.unlock();
		return;
	}
	
	

}
