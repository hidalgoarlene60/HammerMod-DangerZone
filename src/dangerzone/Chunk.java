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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import dangerzone.biomes.Biome;
import dangerzone.biomes.BiomeManager;
import dangerzone.entities.Entities;
import dangerzone.entities.Entity;
import dangerzone.threads.SpawnerThread;


public class Chunk {
	
	public int chunkX;
	public int chunkZ;
	public int dimension;
	public int isDecorated;
	public short blockdata[][] = null;
	public short metadata[][] = null;
	public int isChanged; //field is not really used any more....
	public int isValid = 0;
	public int must_be_written = 0;
	private int chunkDiskVersion = 105;
	public short drawn[] = null;
	public short lightmap[][] = null;
	public volatile long VBOids[] = null;
	public boolean amGenerating = false; //just to be safe
	public boolean amDecorating = false; //just to be safe
	public List<ListCoords> tickblocks = null;
	public int redraw = 0;
	public Biome mybiome = null;
	public float b_red, b_green, b_blue;
	public List<String> ownernames = null; //only player that can modify this chunk (besides operators!)
	private Lock rwlock = new ReentrantLock(); //Annoying, but needed. Sometimes the same chunk can be reading and writing at the same time.
	
	public Chunk(int d, int x, int y, int z){
		int i;
		blockdata = new short [256][];
		metadata = new short [256][];
		drawn = new short[256];
		chunkX = (x>>4);
		chunkZ = (z>>4);
		isDecorated = 0;
		isChanged = 0;
		dimension = d;
		for(i=0;i<256;i++){
			blockdata[i] = null;
			metadata[i] = null;
			//if(i > 50){
				drawn[i] = 1; //set this for the world renderer to try everything once
			//}else{
			//	drawn[i] = 0; //Don't, because it kicks off lighting in underground ores...
			//}
		}
		isValid = 0;
		VBOids = new long[20];
		amGenerating = false;
		amDecorating = false;
		tickblocks = null;
		ownernames = null;
		redraw = 1;
		b_red = b_green = b_blue = 1;
	}
	
	
	public int getblock(int x, int y, int z){
		int i, j, k;
		i = x&0x0f;
		j = y&0x0ff;
		k = z&0x0f;
		if(blockdata[j] == null)return 0;
		return blockdata[j][i*16+k] & 0xffff;
	}
	
	
	public int getblockmeta(int x, int y, int z){
		int i, j, k;
		i = x&0x0f;
		j = y&0x0ff;
		k = z&0x0f;
		if(metadata[j] == null)return 0;
		return metadata[j][i*16+k] & 0xffff;
	}
	
	public void setowners(List<String> newowners){
		ownernames = newowners;
		isChanged = 1; //needs to be sent to clients, maybe, someday, not... is actually clients problem to ask...
		must_be_written = 1; //needs to be saved to disk!
	}
	
	public void setblock(int x, int y, int z, int type){
		if(DangerZone.freeze_world)return;
		int i, j, k, d;
		i = x&0x0f;
		j = y&0x0ff;
		k = z&0x0f;
		tickblocks = null;
		if(blockdata[j] == null){
			if(type == 0)return;
			blockdata[j] = new short [256];
		}
		drawn[j]=1; //recheck this now! Doesn't quite work on chunk boundaries... oh well...
		drawn[(j+1)&0xff]=1;
		drawn[(j-1)&0xff]=1;
		d = blockdata[j][i*16+k];
		blockdata[j][i*16+k] = (short)(type&0xffff);
		if(d != type){
			isChanged = 1; //needs to be sent to clients
			must_be_written = 1; //needs to be saved to disk!
		}
	}
	public void setblockmeta(int x, int y, int z, int meta){
		if(DangerZone.freeze_world)return;
		int i, j, k, d;
		i = x&0x0f;
		j = y&0x0ff;
		k = z&0x0f;
		//tickblocks = null;
		if(metadata[j] == null){
			if(meta == 0)return;
			metadata[j] = new short [256];
		}
		drawn[j]=1; //recheck this now!
		drawn[(j+1)&0xff]=1;
		drawn[(j-1)&0xff]=1;
		d = metadata[j][i*16+k];
		metadata[j][i*16+k] = (short)(meta&0xffff);
		if(d != meta){
			isChanged = 1;
			must_be_written = 1;
		}
	}
	
	//terrain generation is done by the biomeMANAGER because it may have to merge biomes gracefully...
	public void generate(World w, int d, int x, int y, int z){	
		if(DangerZone.freeze_world){
			isValid = 1;
			return;
		}
		if(isValid != 0 || isDecorated != 0 || amGenerating)return;	
		BiomeManager bm = null;
		amGenerating = true;
		chunkX = (x>>4);
		chunkZ = (z>>4);
		dimension = d;		
		isChanged = 0;	
		bm = w.getBiomeManager(d, x, y, z);
		if(bm != null){
			mybiome = bm.getBiomeForChunk(null, d, x, y, z);
			if(mybiome != null){
				bm.generate(w, d, mybiome, this, chunkX, chunkZ);
				b_red = mybiome.mul_red;
				b_green = mybiome.mul_green;
				b_blue = mybiome.mul_blue;
			}
		}
		isChanged = 0;		//Don't count as changed until decorated!
		isDecorated = 0;
		isValid = 1;
		must_be_written = 1;
		amGenerating = false;
	}
	
	//decoration is done by the individual biomes, no manager needed!
	public void decorate(World w, int d, int x, int y, int z){
		if(DangerZone.freeze_world){
			isDecorated = 1;
			return;
		}
		if(isValid == 0 || isDecorated != 0 || amDecorating)return;
		amDecorating = true;
			
		if(mybiome == null)mybiome = w.getBiome(d, x, y, z);
		if(mybiome != null){
			//world decorator first, because large underground and surface structures... caves and things!
			w.decorate(w, d, mybiome, chunkX, chunkZ);
			mybiome.decorate(w, d, this, chunkX, chunkZ);
		}

		SpawnerThread.doSpawnChunk(this, w, d, x, z, CreatureTypes.PERMANENT); //spawn some real critters!
		isDecorated = 1;
		must_be_written = 1;		
		DangerZone.server.flushAll(); //Decorating can cause bunches of queued block changes. Flush them all out.
		amDecorating = false;
	}
	
	
	//TODO Use the GZIP input/output streams to save a shitload of space?
	public void saveToDisk(boolean remove_entities){
			
		String filepath = new String();	
		File f = null;
		FileOutputStream os = null;
		BufferedOutputStream bos = null;
		ObjectOutputStream oos = null;
		int i;		
				
		rwlock.lock();
		
		if(must_be_written != 0){
			int separator = -1;

			filepath = String.format("worlds/%s/Dimension-%d/%2x/%d_%d.dat", DangerZone.worldname,this.dimension,(this.chunkX+this.chunkZ)&0xff,this.chunkX,this.chunkZ);
			f = new File(filepath);		
			f.getParentFile().mkdirs();	

			try {
				os = new FileOutputStream(filepath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			bos = new BufferedOutputStream(os, 16000);
			try {
				oos = new ObjectOutputStream(bos);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {

				oos.writeInt(chunkDiskVersion);
				//version 104 adds ownername
				//version 105 adds color scales
				if(ownernames == null){
					oos.writeInt(0);
				}else{
					int olen = ownernames.size();	
					int j;
					String owner = null;
					int ilen;
					oos.writeInt(olen);					
					for(j=0;j<olen;j++){
						owner = ownernames.get(j);
						ilen = owner.length();
						oos.writeInt(ilen);
						for(i=0;i<ilen;i++){
							oos.writeChar(owner.charAt(i));
						}
					}
				}
				oos.writeInt(chunkX);
				oos.writeInt(chunkZ);
				oos.writeInt(dimension);
				oos.writeInt(isDecorated);
				isChanged = 0;
				oos.writeInt(isChanged);
				oos.writeInt(isValid);
				must_be_written = 0;
				oos.writeInt(must_be_written);
				oos.writeFloat(b_red);
				oos.writeFloat(b_green);
				oos.writeFloat(b_blue);
				oos.writeInt(separator);

				//version 103, some crude but effective compression to shorts
				short curval, curcount, indx;
				for(i=0;i<256;i++){
					if(blockdata[i] != null){
						oos.writeInt(i); //current index
						curval = blockdata[i][0];
						curcount = 0;
						for(indx=0;indx<256;indx++){
							if(blockdata[i][indx] == curval){
								curcount++;
							}else{
								oos.writeShort(curcount);
								oos.writeShort(curval);
								curcount = 1;
								curval = blockdata[i][indx];
							}						
						}	
						oos.writeShort(curcount);
						oos.writeShort(curval);
					}
				}

				oos.writeInt(separator);

				for(i=0;i<256;i++){
					if(metadata[i] != null){
						oos.writeInt(i);
						oos.writeObject(metadata[i]);
					}
				}
				oos.writeInt(separator); //end of meta

			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				oos.flush();
				oos.close();
				bos.flush();
				bos.close();
				os.flush();
				//os.getFD().sync(); //its java... it doesn't actually work...
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//And now for entities...

		boolean eic = DangerZone.server.entityManager.areEntitiesInChunk(dimension, chunkX, chunkZ);
		filepath = String.format("worlds/%s/Dimension-%d/%2x/%d_%d.properties", DangerZone.worldname,this.dimension,(this.chunkX+this.chunkZ)&0xff,this.chunkX,this.chunkZ);
		f = new File(filepath);
		f.getParentFile().mkdirs();	
		f.delete();
		if(!eic){
			rwlock.unlock();
			return;
		}
		
		Properties prop = new Properties();
		FileOutputStream output = null;
		
		try {	 			
			Entity[] entlist = DangerZone.server.entityManager.getEntitiesInChunk(dimension, chunkX, chunkZ);
			int elen = entlist.length;
			if(elen > 0){
				output = new FileOutputStream(filepath);
				bos = new BufferedOutputStream(output, 16000);
				String s;
				int entcount = 0;
				for(i=0;i<elen;i++){
					if(entlist[i] != null){
						if(!(entlist[i] instanceof Player)){				
							s = String.format("Entity_%d:", entcount);
							prop.setProperty(String.format("%s%s", s, "uniquename"), entlist[i].uniquename);
							if(remove_entities)entlist[i].de_init();
							entlist[i].writeSelf(prop, s);
							entcount++;
						}	

						if(remove_entities){
							DangerZone.server.entityManager.removeEntityByID(entlist[i].entityID);
							DangerZone.server.sendEntityRemoveToAll(entlist[i]);
						}
					}
				}

				prop.store(bos, null);
			}
	 
		} catch (IOException io) {
			io.printStackTrace();
		}
		if (output != null) {
			try {
				bos.flush();
				bos.close();
				output.flush();
				//output.getFD().sync(); //actually fails... sigh...
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		rwlock.unlock();
	}
	
	public boolean readFromDisk(){
		String filepath = new String();		
		filepath = String.format("worlds/%s/Dimension-%d/%2x/%d_%d.dat", DangerZone.worldname,this.dimension,(this.chunkX+this.chunkZ)&0xff,this.chunkX,this.chunkZ);	
		FileInputStream os = null;
		ObjectInputStream oos = null;
		BufferedInputStream bos = null;
		
		rwlock.lock();
		
		try {
			os = new FileInputStream(filepath);
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			rwlock.unlock();
			return false;
		}
		
		bos = new BufferedInputStream(os, 32000);
		try {
			oos = new ObjectInputStream(bos);
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			try {
				bos.close();
				os.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			rwlock.unlock();
			return false;
		} catch (IOException e) {
			//e.printStackTrace();
			//System.out.printf("IO Excpetion on file %s\n", filepath);
			//really a late file not found exception.
			//it would appear the java finally tries to read the file, and fails.
			try {
				bos.close();
				os.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			rwlock.unlock();
			return false;
		}
		
		try {
			
			int separator, ondiskversion;
			ondiskversion = oos.readInt();
			if(ondiskversion < 101 || ondiskversion > 105){
				System.out.printf("Unknown chunk file format version!\n");				
				oos.close();
				bos.close();
				os.close();
				rwlock.unlock();
				return false;
			}
			
			if(ondiskversion >= 104){
				ownernames = null;
				int olen = oos.readInt();
				String owner = null;
				if(olen > 0){
					ownernames = new ArrayList<String>();
					while(olen > 0){
						int ilen = oos.readInt();
						if(ilen > 0){	
							owner = "";
							while(ilen > 0){
								owner += oos.readChar();
								ilen--;
							}
							ownernames.add(owner);
						}
						olen--;
					}
				}
			}
			chunkX = oos.readInt();
			chunkZ = oos.readInt();
			dimension = oos.readInt();
			isDecorated = oos.readInt();
			isChanged = oos.readInt();
			isValid = oos.readInt();
			must_be_written = oos.readInt();
			if(ondiskversion >= 105){
				b_red = oos.readFloat();
				b_green = oos.readFloat();
				b_blue = oos.readFloat();
			}
			
			separator = oos.readInt();
			if(separator != -1){
				System.out.printf("Big oops!\n");
				oos.close();
				bos.close();
				os.close();
				rwlock.unlock();
				return false;
			}
			
			if(ondiskversion == 101){
				//old way, no compression
				while(true){
					separator = oos.readInt();
					if(separator < 0)break;				
					blockdata[separator] = (short[]) oos.readObject();
				}
			}else if(ondiskversion == 102){
				//version 102, cheap and dirty compression
				int curcount, curval, indx;
				while(true){
					separator = oos.readInt();
					if(separator < 0)break;	
					blockdata[separator] = new short [256];
					curcount = oos.readInt();
					curval = oos.readInt();
					for(indx=0;indx<256;indx++){
						blockdata[separator][indx] = (short) (curval & 0xffff);
						curcount--;
						if(indx < 255 && curcount == 0){
							curcount = oos.readInt();
							curval = oos.readInt();
						}					
					}
				}
			}else{
				//compressed and shorts
				short curcount, curval, indx;
				while(true){
					separator = oos.readInt();
					if(separator < 0)break;	
					blockdata[separator] = new short [256];
					curcount = oos.readShort();
					curval = oos.readShort();
					for(indx=0;indx<256;indx++){
						blockdata[separator][indx] = (short) (curval & 0xffff);
						curcount--;
						if(indx < 255 && curcount == 0){
							curcount = oos.readShort();
							curval = oos.readShort();
						}					
					}
				}
			}
			
			while(true){
				separator = oos.readInt();
				if(separator < 0)break;				
				metadata[separator] = (short[]) oos.readObject();
			}
			
			
						
			
		} catch (IOException e) {
			e.printStackTrace();
			try {
				oos.close();
				bos.close();
				os.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			rwlock.unlock();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			try {
				oos.close();
				bos.close();
				os.close();
			} catch (IOException ez) {
				ez.printStackTrace();
			}
			rwlock.unlock();
			return false;
		}
		
		
		try {
			oos.close();
			bos.close();
			os.close();
			bos = null;
			oos = null;
			os = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		Properties prop = new Properties();
		InputStream input = null;
		filepath = String.format("worlds/%s/Dimension-%d/%2x/%d_%d.properties", DangerZone.worldname,this.dimension,(this.chunkX+this.chunkZ)&0xff,this.chunkX,this.chunkZ);
	 
		try {	 
			input = new FileInputStream(filepath);
			bos = new BufferedInputStream(input, 16000);	 
			// load a properties file
			prop.load(bos);
			
		} catch (IOException ex) {
			//ex.printStackTrace();
			rwlock.unlock();
			return true; //no entities!
		}
		
 			
			Entity ent;
			String s;
			int entcount = 0;
			while(true){
				s = String.format("Entity_%d:", entcount);
				s = prop.getProperty(String.format("%s%s", s, "uniquename"));
				if(s == null)break;				
				must_be_written = 1; //Because we don't know if this entity will stay in this chunk!				
				ent = Entities.spawnEntityByName(s, DangerZone.server_world);
				if(ent != null){
					s = String.format("Entity_%d:", entcount);
					ent.readSelf(prop, s);
					ent.init();
					if(DangerZone.server.entityManager.addEntity(ent) > 0){	
						DangerZone.server.sendSpawnEntityToAll((Entity)ent);
					}
				}			
				entcount++;

			}
			
			
		if (input != null) {
			try {
				bos.close();
				input.close();
				bos = null;
				input = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		rwlock.unlock();
		return true;
	}
	
	public void delete(){		
		String filepath = new String();	
		File f = null;				
		rwlock.lock();
		filepath = String.format("worlds/%s/Dimension-%d/%2x/%d_%d.dat", DangerZone.worldname,this.dimension,(this.chunkX+this.chunkZ)&0xff,this.chunkX,this.chunkZ);
		f = new File(filepath);		
		f.delete();
		rwlock.unlock();
	}
	
}
