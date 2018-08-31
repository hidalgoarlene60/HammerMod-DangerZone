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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import dangerzone.blocks.Block;
import dangerzone.blocks.BlockRotation;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityBlockItem;
import dangerzone.entities.EntityChest;
import dangerzone.entities.EntityExp;
import dangerzone.entities.EntityLiving;
import dangerzone.items.Item;
import dangerzone.items.Items;
import dangerzone.particles.Particle;





public class Utils {
	
	public static void add_chest(World w, int dimension, int x, int y, int z, StuffList[] stuff){
		int nthings = w.rand.nextInt(25)+20; //lots of "tries"
		add_chest(w, dimension, x, y, z, stuff, nthings);
	}
	
	public static void add_chest(World w, int dimension, int x, int y, int z, StuffList[] stuff, int chances){	
		if(!w.isServer)return;
		//add a chest and put some things in it
		//try to face an open block
		int meta = 0;
		if(w.getblock(dimension, x, y, z+1)!=0){
			meta = BlockRotation.Y_ROT_180; //try this!
			if(w.getblock(dimension, x, y, z-1)!=0){
				meta = BlockRotation.Y_ROT_270; //try this!
				if(w.getblock(dimension, x-1, y, z)!=0){
					meta = BlockRotation.Y_ROT_90; //try this!
					if(w.getblock(dimension, x+1, y, z)!=0){
						meta = 0; //give up
					}
				}
			}			
		}
		w.setblockandmeta(dimension, x, y, z, Blocks.chest.blockID, meta); //hopefully it's pointing the right way!
		Entity eb = w.createEntityByName("DangerZone:EntityChest", dimension, (double)(x)+0.5f, (double)(y)+0.05f, (double)(z)+0.5f);
		if(eb != null){
			//put some things into our new chest entity **BEFORE** we spawn it.
			eb.init();
			EntityChest ec = (EntityChest)eb;
			if(stuff != null && stuff.length > 0 && chances > 0){

				int where, i;
				int val, which, howmany;
				int bid, iid;

				for(i=0;i<chances;i++){
					where = w.rand.nextInt(50);
					val = w.rand.nextInt(100);
					if(stuff.length < 2){
						which = 0;
					}else{
						which = w.rand.nextInt(stuff.length);
					}
					if(val <= stuff[which].chance){
						howmany = stuff[which].min;
						if(stuff[which].max - stuff[which].min > 0){
							howmany += w.rand.nextInt((stuff[which].max - stuff[which].min)+1);
						}
						bid = stuff[which].getbid();
						iid = stuff[which].getiid();
						if(bid != 0){
							if(howmany > Blocks.getMaxStack(bid))howmany = Blocks.getMaxStack(bid);
						}
						if(iid != 0){
							if(howmany > Items.getMaxStack(iid))howmany = Items.getMaxStack(iid);
						}
						if(bid != 0 && iid != 0)continue;
						if(bid == 0 && iid == 0)continue;
						if(bid < 0 || iid < 0)continue;
						if(howmany > 0){							
							ec.setInventory(where, new InventoryContainer(bid, iid, howmany, 0)); //just set it						
						}
					}
				}
			}
			//spawn the chest entity!	

			w.spawnEntityInWorld(eb);
		}
	}
	
	public static EntityChest add_empty_chest(World w, int dimension, int x, int y, int z){		
		//add a chest and put some things in it
		//try to face an open block
		int meta = 0;
		if(w.getblock(dimension, x, y, z+1)!=0){
			meta = BlockRotation.Y_ROT_180; //try this!
			if(w.getblock(dimension, x, y, z-1)!=0){
				meta = BlockRotation.Y_ROT_270; //try this!
				if(w.getblock(dimension, x-1, y, z)!=0){
					meta = BlockRotation.Y_ROT_90; //try this!
					if(w.getblock(dimension, x+1, y, z)!=0){
						meta = 0; //give up
					}
				}
			}			
		}
		w.setblockandmeta(dimension, x, y, z, Blocks.chest.blockID, meta); //hopefully it's pointing the right way!
		Entity eb = w.createEntityByName("DangerZone:EntityChest", dimension, (double)(x)+0.5f, (double)(y)+0.05f, (double)(z)+0.5f);
		if(eb != null){
			eb.init();
			w.spawnEntityInWorld(eb);
			return (EntityChest)eb;
		}
		return null;
	}
	
	public static void add_to_chest(EntityChest ec, int bid, int iid, int howmany, int slot){
		if(howmany == 0){
			ec.setInventory(slot,  null);
			return;
		}
		if(bid != 0){
			if(howmany > Blocks.getMaxStack(bid))howmany = Blocks.getMaxStack(bid);
		}
		if(iid != 0){
			if(howmany > Items.getMaxStack(iid))howmany = Items.getMaxStack(iid);
		}		
		if(howmany > 0){
			ec.setInventory(slot, new InventoryContainer(bid, iid, howmany, 0));
		}else{
			ec.setInventory(slot,  null);
		}
	}
	
	public static void screenshot(int width, int height){
		GL11.glReadBuffer(GL11.GL_FRONT);
		int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
		GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer );
		Date date = new Date(System.currentTimeMillis());
		DateFormat df = new SimpleDateFormat("yyyy_MM_dd_HHmmss");
		String filepath = new String();	
		filepath = String.format("screenshots/DZimage_%s.png", df.format(date));	
		File file = new File(filepath);	
		file.getParentFile().mkdirs();	
		try {
			file.createNewFile();
		} catch (IOException e1) {
			//e1.printStackTrace();
			return;
		}		
		String format = "PNG"; 
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		   
		for(int x = 0; x < width; x++){
		    for(int y = 0; y < height; y++){
		        int i = (x + (width * y)) * bpp;
		        int r = buffer.get(i) & 0xFF;
		        int g = buffer.get(i + 1) & 0xFF;
		        int b = buffer.get(i + 2) & 0xFF;
		        image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
		    }
		}
		   
		try {
		    ImageIO.write(image, format, file);
		} catch (IOException e) { e.printStackTrace(); }
		image.flush();
	}

	public static double getDistanceBetweenEntities(Entity e, Entity p){
		double d1, d2, d3;
		d1 = p.posx - e.posx;
		d2 = p.posy - e.posy;
		d3 = p.posz - e.posz;
		return Math.sqrt((d1*d1)+(d2*d2)+(d3*d3));
	}
	
	public static double getHorizontalDistanceBetweenEntities(Entity e, Entity p){
		double d1, d3;
		d1 = p.posx - e.posx;
		d3 = p.posz - e.posz;
		return Math.sqrt((d1*d1)+(d3*d3));
	}
	
	public static int getPropertyInt(Properties prop, String propname, int min, int max, int dflt){
		String s;
		int retval = dflt;
		// get the property value and print it out
		s = prop.getProperty(propname);
		if(s != null){
			try {
				retval = Integer.parseInt(s);
			} catch (NumberFormatException e) {
				retval = dflt;
			}
			if(retval < min)retval = min;
			if(retval > max)retval = max;
		}
		return retval;
	}
	
	public static long getPropertyLong(Properties prop, String propname, long min, long max, long dflt){
		String s;
		long retval = dflt;
		// get the property value and print it out
		s = prop.getProperty(propname);
		if(s != null){
			try {
				retval = Long.parseLong(s);
			} catch (NumberFormatException e) {
				retval = dflt;
			}
			if(retval < min)retval = min;
			if(retval > max)retval = max;
		}
		return retval;
	}
	
	public static float getPropertyFloat(Properties prop, String propname, float min, float max, float dflt){
		String s;
		float retval = dflt;
		// get the property value and print it out
		s = prop.getProperty(propname);
		if(s != null){
			try {
				retval = Float.parseFloat(s);
			} catch (NumberFormatException e) {
				retval = dflt;
			}
			if(retval < min)retval = min;
			if(retval > max)retval = max;
		}
		return retval;
	}
	
	public static double getPropertyDouble(Properties prop, String propname, double min, double max, double dflt){
		String s;
		double retval = dflt;
		// get the property value and print it out
		s = prop.getProperty(propname);
		if(s != null){
			try {
				retval = Double.parseDouble(s);
			} catch (NumberFormatException e) {
				retval = dflt;
			}
			if(retval < min)retval = min;
			if(retval > max)retval = max;
		}
		return retval;
	}
	
	public static boolean getPropertyBoolean(Properties prop, String propname, boolean dflt){
		String s;
		boolean retval = dflt;
		// get the property value and print it out
		s = prop.getProperty(propname);
		if(s != null){
			retval = Boolean.parseBoolean(s);
		}
		return retval;
	}
	
	public static String getPropertyString(Properties prop, String propname, String dflt){
		String s;
		String retval = dflt;
		// get the property value and print it out
		s = prop.getProperty(propname);
		if(s != null){
			retval = s;
		}
		return retval;
	}
	
	public static void spawnParticlesFromServer(World w, String name, int howmany, int d, double x, double y, double z){
		if(w.isServer){
			DangerZone.server.sendSpawnParticleToAllExcept(null, name, howmany, d, x, y, z);
		}
	}
	
	public static void spawnParticlesFromServer(World w, String name, int howmany, int d, double x, double y, double z, int bid){
		if(w.isServer){
			DangerZone.server.sendSpawnParticleToAllExcept(null, name, howmany, d, x, y, z, bid);
		}
	}
	
	public static void spawnParticles(World w, String name, int howmany, int d, double x, double y, double z, boolean forwardtoserver){
		if(w.isServer){
			if(forwardtoserver)DangerZone.server.sendSpawnParticleToAllExcept(null, name, howmany, d, x, y, z, 0);
		}else{
			spawnParticles( w,  name,  howmany,  d,  x,  y,  z, 0, forwardtoserver);
		}
	}
	
	public static void spawnParticles(World w, String name, int howmany, int d, double x, double y, double z, int bid, boolean forwardtoserver){
		if(!w.isServer){
			for(int i=0;i<howmany;i++){
				Particle eb = w.createParticleByName(name, d, x, y, z);
				if(eb != null){
					eb.bid = bid;
					w.spawnParticleInWorld(eb);
				}
			}
		}
		//iff we initiated these on the client, then send them out for everyone else.
		if(forwardtoserver && ! w.isServer){
			DangerZone.player.server_connection.sendSpawnParticles(name, howmany, d, x, y, z, bid);
		}
		if(w.isServer){
			DangerZone.server.sendSpawnParticleToAllExcept(null, name, howmany, d, x, y, z, bid);
		}
	}
	
	public static void spawnDeathParticles(World w, int d, double x, double y, double z, float width, float height){
		if(!w.isServer){
			int howmany = (int) (10 * (width+height));
			if(howmany < 1)howmany = 1;
			if(howmany > 500)howmany = 500;
			float mf = (width+height/2);
			if(mf < 0.5f)mf = 0.5f;
			if(mf > 10f)mf = 10f;
			for(int i=0;i<howmany;i++){
				Particle eb = w.createParticleByName("DangerZone:ParticleDeath", d, 
						x + (w.rand.nextFloat()-w.rand.nextFloat())*width/2, 
						y + w.rand.nextFloat()*height, 
						z + (w.rand.nextFloat()-w.rand.nextFloat())*width/2);
				if(eb != null){
					eb.motionx *= mf;
					eb.motiony *= mf;
					eb.motionz *= mf;
					w.spawnParticleInWorld(eb);
				}
			}
		}
	}
	
	public static void spitInventoryOut(InventoryContainer ic){
		if(ic != null){
			//Spit them out!
			//Not very far or fast, because we actually want to pick them back up.
			EntityBlockItem e = (EntityBlockItem)DangerZone.player.world.createEntityByName(DangerZone.blockitemname, 
					DangerZone.player.dimension, 
					DangerZone.player.posx+Math.cos(Math.toRadians(DangerZone.player.rotation_yaw_head-90))*Math.cos(Math.toRadians(DangerZone.player.rotation_pitch_head)),
					DangerZone.player.posy+(DangerZone.player.getHeight()*7/8) - Math.sin(Math.toRadians(DangerZone.player.rotation_pitch_head)),
					DangerZone.player.posz+Math.sin(Math.toRadians(DangerZone.player.rotation_yaw_head-90))*Math.cos(Math.toRadians(DangerZone.player.rotation_pitch_head)));
			if(e != null){
				e.init();
				e.fill(ic);
				e.rotation_pitch = DangerZone.world.rand.nextInt(360);
				e.rotation_yaw = DangerZone.world.rand.nextInt(360);
				e.rotation_roll = DangerZone.world.rand.nextInt(360);
				e.motionx = 0; 
				e.motiony = 0;
				e.motionz = 0;
				DangerZone.player.world.spawnEntityInWorld(e);
			}

		}
	}
	public static void spawnExperience(int exp, World world, int d, double x, double y, double z){	
		spawnExperience( exp,  world,  d,  x,  y,  z, true);
	}
	
	public static void spawnExperience(int exp, World world, int d, double x, double y, double z, boolean popsound){	
		
			//make some Experience particles!
			int iexp = exp;
			int popper = 0;
			if(!popsound)popper = 1;
			while(iexp >= 1000){
				EntityExp e = (EntityExp)world.createEntityByName("DangerZone:Experience", d, x, y, z);
				if(e != null){
					e.setBID(popper);
					e.setIID(0);
					e.setExperience(1000);
					e.rotation_pitch = 0;
					e.rotation_yaw = world.rand.nextInt(360);
					e.rotation_roll = 0;
					e.motionx = (world.rand.nextFloat()-world.rand.nextFloat()); 
					e.motiony = world.rand.nextFloat()/4;
					e.motionz = (world.rand.nextFloat()-world.rand.nextFloat());
					world.spawnEntityInWorld(e);
				}				
				iexp -= 1000;
			}
			while(iexp >= 100){
				EntityExp e = (EntityExp)world.createEntityByName("DangerZone:Experience", d, x, y, z);
				if(e != null){
					e.setBID(popper);
					e.setIID(0);
					e.setExperience(100);
					e.rotation_pitch = 0;
					e.rotation_yaw = world.rand.nextInt(360);
					e.rotation_roll = 0;
					e.motionx = (world.rand.nextFloat()-world.rand.nextFloat()); 
					e.motiony = world.rand.nextFloat()/4;
					e.motionz = (world.rand.nextFloat()-world.rand.nextFloat());
					world.spawnEntityInWorld(e);
				}				
				iexp -= 100;
			}
			while(iexp >= 10){
				EntityExp e = (EntityExp)world.createEntityByName("DangerZone:Experience", d, x, y, z);
				if(e != null){
					e.setBID(popper);
					e.setIID(0);
					e.setExperience(10);
					e.rotation_pitch = 0;
					e.rotation_yaw = world.rand.nextInt(360);
					e.rotation_roll = 0;
					e.motionx = (world.rand.nextFloat()-world.rand.nextFloat()); 
					e.motiony = world.rand.nextFloat()/4;
					e.motionz = (world.rand.nextFloat()-world.rand.nextFloat());
					world.spawnEntityInWorld(e);
				}				
				iexp -= 10;
			}
			while(iexp >= 1){
				EntityExp e = (EntityExp)world.createEntityByName("DangerZone:Experience", d, x, y, z);
				if(e != null){
					e.setBID(popper);
					e.setIID(0);
					e.setExperience(1);
					e.rotation_pitch = 0;
					e.rotation_yaw = world.rand.nextInt(360);
					e.rotation_roll = 0;
					e.motionx = (world.rand.nextFloat()-world.rand.nextFloat()); 
					e.motiony = world.rand.nextFloat()/4;
					e.motionz = (world.rand.nextFloat()-world.rand.nextFloat());
					world.spawnEntityInWorld(e);
				}				
				iexp -= 1;
			}
	}
	
	public static void doDropRand(World w, Object obj, float dist, int d, double x, double y, double z){
		if(obj == null)return;
		int bid, iid;
		bid = iid = 0;
		
		if(obj instanceof Item)iid = ((Item)obj).itemID;
		if(obj instanceof Block)bid = ((Block)obj).blockID;
		if(iid == 0 && bid == 0)return;
		
		EntityBlockItem e = (EntityBlockItem)w.createEntityByName(DangerZone.blockitemname, d, x, y, z);
		if(e != null){
			e.init();
			e.fill(bid, iid, 1);
			e.rotation_pitch = w.rand.nextInt(360);
			e.rotation_yaw = w.rand.nextInt(360);
			e.rotation_roll = w.rand.nextInt(360);
			e.motionx = dist * (w.rand.nextFloat()-w.rand.nextFloat()); 
			e.motiony = dist * w.rand.nextFloat()/4;
			e.motionz = dist * (w.rand.nextFloat()-w.rand.nextFloat());
			w.spawnEntityInWorld(e);
		}			
		
	}
	
	public static void doDropRand(World w, int blockid, int itemid, float dist, int d, double x, double y, double z){
		if(blockid == itemid)return;
		if(blockid < 0 || itemid < 0)return;
		if(blockid != 0 && itemid != 0)return;
		
		EntityBlockItem e = (EntityBlockItem)w.createEntityByName(DangerZone.blockitemname, d, x, y, z);
		if(e != null){
			e.init();
			e.fill(blockid, itemid, 1);
			e.rotation_pitch = w.rand.nextInt(360);
			e.rotation_yaw = w.rand.nextInt(360);
			e.rotation_roll = w.rand.nextInt(360);
			e.motionx = dist * (w.rand.nextFloat()-w.rand.nextFloat()); 
			e.motiony = dist * w.rand.nextFloat()/4;
			e.motionz = dist * (w.rand.nextFloat()-w.rand.nextFloat());
			w.spawnEntityInWorld(e);
		}			
		
	}
	
	public static void doDropRand(World w, InventoryContainer ic, float dist, int d, double x, double y, double z){
		if(ic == null)return;
		if(ic.iid == ic.bid)return;
		if(ic.bid < 0 || ic.iid < 0)return;
		if(ic.bid != 0 && ic.iid != 0)return;
		
		EntityBlockItem e = (EntityBlockItem)w.createEntityByName(DangerZone.blockitemname, d, x, y, z);
		if(e != null){
			e.init();
			e.fill(ic);
			e.rotation_pitch = w.rand.nextInt(360);
			e.rotation_yaw = w.rand.nextInt(360);
			e.rotation_roll = w.rand.nextInt(360);
			e.motionx = dist * (w.rand.nextFloat()-w.rand.nextFloat()); 
			e.motiony = dist * w.rand.nextFloat()/4;
			e.motionz = dist * (w.rand.nextFloat()-w.rand.nextFloat());
			w.spawnEntityInWorld(e);
		}			
		
	}
	
	public static Texture initTexture(String tp) {
		Texture ltexture = null;
		
		if(DangerZone.alt_texture_paths.size() > 0){
			for(int i=0;i<DangerZone.alt_texture_paths.size();i++){
				//try for a texture that is being overridden first...
				try {
					ltexture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(DangerZone.alt_texture_paths.get(i) + tp));
				} catch (Exception e) {
					//System.out.printf("Failed to load block texture %s\n", "res/blocks/" + tp);
				}
				if(ltexture != null)break;
			}
		}
		//not there? try where it says it is...
		if(ltexture == null){
			try {
				ltexture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(tp));
			} catch (Exception e) {
				System.out.printf("--- Failed to load texture %s\n", tp);
				e.printStackTrace();
			}
		}		
		return ltexture;
	}
	
	public static List<Integer> find_nearby_pets(Player p){
		List<Entity> all_list = DangerZone.server.entityManager.findALLEntitiesInRange( 128f, p.dimension, p.posx, p.posy, p.posz);
		List<Integer> pet_list = new ArrayList<Integer>();
		if(all_list != null){
			Iterator<Entity> ii = all_list.iterator();
			Entity e = null;
			while(ii.hasNext()){
				e = ii.next();
				if(e instanceof EntityLiving){
					if(e != p && e.getOwnerName() != null){
						if(e.getOwnerName().equals(p.myname)){
							if(!e.getStaying()){
								pet_list.add(e.entityID);
							}
						}
					}
				}
			}
		}		
		return pet_list;
	}
	
	public static void doTeleport(Player p, int d, double x, double y, double z){
		if(p == null)return;
		if(!p.world.isServer)return;
		
		int which = p.world.rand.nextInt(3);
		if(which == 0)p.world.playSound("DangerZone:teleport1", p.dimension, p.posx, p.posy, p.posz, 0.75f, 1);
		if(which == 1)p.world.playSound("DangerZone:teleport2", p.dimension, p.posx, p.posy, p.posz, 0.75f, 1);
		if(which == 2)p.world.playSound("DangerZone:teleport3", p.dimension, p.posx, p.posy, p.posz, 0.75f, 1);
		
		if(d > 0 && d < Dimensions.dimensionsMAX){
			if(Dimensions.DimensionArray[d] != null){
				//Stop the cleaner!
				DangerZone.dimension_change_in_progress = true;
				/*
				 * Have to make a list of pets nearby and teleport those too!
				 */
				List<Integer> entity_list = Utils.find_nearby_pets(p);
				//And whatever we are riding!
				Entity riding = p.getRiddenEntity();
				if(riding != null){
					entity_list.add(riding.entityID);
				}
				
				Dimensions.DimensionArray[d].teleportToDimension(p, DangerZone.server_world, d, (int)x, (int)y, (int)z);							
				p.server_thread.sendTeleportToPlayer(d, p.posx, p.posy, p.posz); //tell him his new position
				DangerZone.server.sendPlayerUpdateToAllExcept(p, p, true); //now tell everyone else too! FORCE
				
				/*
				 * Now teleport the pets!
				 */
				if(entity_list != null){
					Iterator<Integer> ii = entity_list.iterator();
					while(ii.hasNext()){
						int ient = ii.next();
						Entity eent = DangerZone.server.entityManager.findEntityByID(ient);
						if(eent != null){
							Dimensions.DimensionArray[d].teleportToDimension(eent, DangerZone.server_world, d, (int)x, (int)y, (int)z);
							DangerZone.server.sendEntityUpdateToAll(eent, true); //FORCE
						}
					}
				}
				
				if(riding != null){
					riding.Mount(p); //re-send to client to make sure!
				}
				//Restart the cleaner!
				DangerZone.dimension_change_in_progress = false;
			}
		}
	}
	
}
