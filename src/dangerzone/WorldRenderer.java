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


import java.awt.Font;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;

import dangerzone.biomes.Biome;
import dangerzone.biomes.BiomeManager;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entity;
import dangerzone.items.Item;
import dangerzone.items.Items;
import dangerzone.particles.Particle;
import dangerzone.threads.CleanerThread;
import dangerzone.threads.VBODataBuilderThread;



public class WorldRenderer {
	
	private volatile List<long[]> VBO_delete_list; // a list of VBO lists that need to be deleted from chunks!
	public static Lock VBOlistlock = new ReentrantLock();
		
	public static Texture heart_texture = null;
	public static Texture unheart_texture = null;
	public static Texture hungerfull_texture = null;
	public static Texture hungerempty_texture = null;
	public static Texture bubble_texture = null;
	public static Texture sun_texture = null;
	public static Texture moon_texture = null;
	public static Texture magic_texture = null;
	public static Texture magicempty_texture = null;
	public static int last_texture = -1;
	public static StitchedTextureFile[] stitches = null;
	public static int next_stitch = 0;
	public volatile int oneblock = -1;
	
	public int blockrenderwidth = 16;
	public static volatile int focus_x, focus_y, focus_z, focus_side;
	public static volatile boolean do_draw_focus = false;
	public static volatile int focus_bid, focus_meta;
	public static volatile float focus_damage = 0;
	public static volatile float focus_maxdamage = 0;
	public static volatile float focus_dist = 0;
	public static volatile Entity focus_entity = null;
	public float linewidth = 3f;
	private int topid, bottomid, leftid, rightid, frontid, backid;
	private Font awtfont = null;
	public TrueTypeFont font = null;
	public static  float brightness_red;
	public static float brightness_green;
	public static float brightness_blue;
	private float sky_red, sky_green, sky_blue;
	private Entity ent;
	private static Lock lock = new ReentrantLock();
	private long lasttime = 0, nowtime = 0;
	public int fps = 0;
	private int fpscounter = 0;
	private int pps = 0;
	//private int cps = 0;
	private float bounce = 0;
	private float lastbounce = 0;
	private float eatbounce = 0;
	private int hit_cycles = 7;
	private int hit_cycle_count = 0;
	private int hit_cycle_dir = 1;	
	private float hitx = -20;
	private float hity = 10;
	private float hitz = -25;//-35
	private float hitp = 62;
	private float hitw = 34;
	private float hitr = -137;//-147
	private float hitxn = -24.5f;
	private float hityn = 7.4f;
	private float hitzn = -33.8f;//-35
	private float hitpn = 29.8f;
	private float hitwn = 7.7f;
	private float hitrn = 83.6f;//-147
	private int eat_cycles = 7;
	private int eat_cycle_count = 0;
	private int eat_cycle_dir = 1;
	private int eat_delay_count = 0;
	private float eatx = -10.5f;
	private float eaty = -2.1f;
	private float eatz = 18.5f;
	private float eatp = -58.1f;
	private float eatw = -80.8f;
	private float eatr = 59.0f;
	private float eatxn = -12.7f;
	private float eatyn = -0.6f;
	private float eatzn = 20.5f;
	private float eatpn = 63.2f;
	private float eatwn = 55.9f;
	private float eatrn = 58.3f;
	boolean washurt = false;
	float ouch = 1.0f;
	private float f5x, f5y, f5z, f5yaw, f5pitch;
	private float cdir, tdir, pdiff, ydiff, rdiff;

	private volatile static long nextVBOid;
	public volatile static Map<Long , VBOBuffer> VBOmap = null;
	private volatile List<VBOBuffer> translucentVBOs; // a list of VBOs that need to be drawn LAST. Blocks with translucent parts or wholes.
	public volatile int VBOmemorysize;
	public int lastplayerdimension = 0;
	private boolean eat_sound = true;
	private double traveled = 0;
	public long framecounter = 0;
	public volatile static int vbocount = 0;
	public VBODataBuilderThread builder = null;
	private int usex, usey, usez, uses;
	private Entity usee = null;
	private FloatBuffer wrfogColor = null;
	public Entity ViewFromEntity = null;
	public float Viewer_yaw = 0;
	public boolean normal_sun = true;
	


	
	public WorldRenderer(){

		heart_texture = TextureMapper.getTexture("res/menus/heart.png");
		unheart_texture = TextureMapper.getTexture("res/menus/unheart.png");
		hungerfull_texture = TextureMapper.getTexture("res/menus/hungerfull.png");
		hungerempty_texture = TextureMapper.getTexture("res/menus/hungerempty.png");
		bubble_texture = TextureMapper.getTexture("res/menus/bubble.png");
		magic_texture = TextureMapper.getTexture("res/menus/star.png");
		magicempty_texture = TextureMapper.getTexture("res/menus/star_empty.png");
		
		GregorianCalendar gcalendar = new GregorianCalendar();
		int nowmonth, nowday;
		nowmonth = gcalendar.get(Calendar.MONTH);
		nowday = gcalendar.get(Calendar.DATE);
		//System.out.printf("month,  day = %d, %d\n", nowmonth, nowday);
		//month is 0-based. Day is actual.
		if(nowmonth == 9 && nowday == 31){ //If it is Halloween, ghosts are everywhere! :)
			sun_texture = TextureMapper.getTexture("res/misc/sunpumpkin.png");
			normal_sun = false;
		}else{
			sun_texture = TextureMapper.getTexture("res/misc/sun.png");
		}
		

		moon_texture = TextureMapper.getTexture("res/misc/moon.png");

		stitches = new StitchedTextureFile[20]; //Should be enough, ya think? :)
		nextVBOid = 1;
		VBOmap = new HashMap<Long, VBOBuffer>();
		VBO_delete_list = new ArrayList<long[]>();
		translucentVBOs = new ArrayList<VBOBuffer>();
		framecounter = 0;
		vbocount = 0;
		VBOmemorysize = 0;

		if(font == null){
			awtfont = new Font("Times New Roman", Font.PLAIN, 24);
			font = new TrueTypeFont(awtfont, false);
		}
		oneblock = -1;

		topid = getNextRenderID();
		GL11.glNewList(topid, GL11.GL_COMPILE);
		GL11.glBegin(GL11.GL_QUADS);	
		GL11.glTexCoord2f(1,0);
		GL11.glVertex3f(blockrenderwidth/2, blockrenderwidth/2, -blockrenderwidth/2); // Top Right Of The Quad (Top)
		GL11.glTexCoord2f(0,0);
		GL11.glVertex3f(-blockrenderwidth/2, blockrenderwidth/2, -blockrenderwidth/2); // Top Left Of The Quad (Top)
		GL11.glTexCoord2f(0,1);
		GL11.glVertex3f(-blockrenderwidth/2, blockrenderwidth/2, blockrenderwidth/2); // Bottom Left Of The Quad (Top)
		GL11.glTexCoord2f(1,1);
		GL11.glVertex3f(blockrenderwidth/2, blockrenderwidth/2, blockrenderwidth/2); // Bottom Right Of The Quad (Top)
		GL11.glEnd(); // Done Drawing The Quad
		GL11.glEndList();

		bottomid = getNextRenderID();
		GL11.glNewList(bottomid, GL11.GL_COMPILE);
		GL11.glBegin(GL11.GL_QUADS);	
		GL11.glTexCoord2f(1,0);
		GL11.glVertex3f(blockrenderwidth/2, -blockrenderwidth/2, blockrenderwidth/2); // Top Right Of The Quad (Bottom)
		GL11.glTexCoord2f(0,0);
		GL11.glVertex3f(-blockrenderwidth/2, -blockrenderwidth/2, blockrenderwidth/2); // Top Left Of The Quad (Bottom)
		GL11.glTexCoord2f(0,1);
		GL11.glVertex3f(-blockrenderwidth/2, -blockrenderwidth/2, -blockrenderwidth/2); // Bottom Left Of The Quad (Bottom)
		GL11.glTexCoord2f(1,1);
		GL11.glVertex3f(blockrenderwidth/2, -blockrenderwidth/2, -blockrenderwidth/2); // Bottom Right Of The Quad (Bottom)
		GL11.glEnd(); // Done Drawing The Quad
		GL11.glEndList();

		frontid = getNextRenderID();
		GL11.glNewList(frontid, GL11.GL_COMPILE);
		GL11.glBegin(GL11.GL_QUADS);	
		GL11.glTexCoord2f(1,0);
		GL11.glVertex3f(blockrenderwidth/2, blockrenderwidth/2, blockrenderwidth/2); // Top Right Of The Quad (Front)
		GL11.glTexCoord2f(0,0);
		GL11.glVertex3f(-blockrenderwidth/2, blockrenderwidth/2, blockrenderwidth/2); // Top Left Of The Quad (Front)
		GL11.glTexCoord2f(0,1);
		GL11.glVertex3f(-blockrenderwidth/2, -blockrenderwidth/2, blockrenderwidth/2); // Bottom Left Of The Quad (Front)
		GL11.glTexCoord2f(1,1);
		GL11.glVertex3f(blockrenderwidth/2, -blockrenderwidth/2, blockrenderwidth/2); // Bottom Right Of The Quad (Front)
		GL11.glEnd(); // Done Drawing The Quad
		GL11.glEndList();

		backid = getNextRenderID();
		GL11.glNewList(backid, GL11.GL_COMPILE);
		GL11.glBegin(GL11.GL_QUADS);	
		GL11.glTexCoord2f(1,0);
		GL11.glVertex3f(-blockrenderwidth/2, blockrenderwidth/2, -blockrenderwidth/2); // Top Left Of The Quad (Back)
		GL11.glTexCoord2f(0,0);
		GL11.glVertex3f(blockrenderwidth/2, blockrenderwidth/2, -blockrenderwidth/2); // Top Right Of The Quad (Back)
		GL11.glTexCoord2f(0,1);
		GL11.glVertex3f(blockrenderwidth/2, -blockrenderwidth/2, -blockrenderwidth/2); // Bottom Right Of The Quad (Back)
		GL11.glTexCoord2f(1,1);
		GL11.glVertex3f(-blockrenderwidth/2, -blockrenderwidth/2, -blockrenderwidth/2); // Bottom Left Of The Quad (Back)
		GL11.glEnd(); // Done Drawing The Quad
		GL11.glEndList();

		leftid = getNextRenderID();
		GL11.glNewList(leftid, GL11.GL_COMPILE);
		GL11.glBegin(GL11.GL_QUADS);	
		GL11.glTexCoord2f(1,0);
		GL11.glVertex3f(-blockrenderwidth/2, blockrenderwidth/2, blockrenderwidth/2); // Top Right Of The Quad (Left)
		GL11.glTexCoord2f(0,0);
		GL11.glVertex3f(-blockrenderwidth/2, blockrenderwidth/2, -blockrenderwidth/2); // Top Left Of The Quad (Left)
		GL11.glTexCoord2f(0,1);
		GL11.glVertex3f(-blockrenderwidth/2, -blockrenderwidth/2, -blockrenderwidth/2); // Bottom Left Of The Quad (Left)
		GL11.glTexCoord2f(1,1);
		GL11.glVertex3f(-blockrenderwidth/2, -blockrenderwidth/2, blockrenderwidth/2); // Bottom Right Of The Quad (Left)
		GL11.glEnd(); // Done Drawing The Quad
		GL11.glEndList();

		rightid = getNextRenderID();
		GL11.glNewList(rightid, GL11.GL_COMPILE);
		GL11.glBegin(GL11.GL_QUADS);	
		GL11.glTexCoord2f(1,0);
		GL11.glVertex3f(blockrenderwidth/2, blockrenderwidth/2, -blockrenderwidth/2); // Top Right Of The Quad (Right)
		GL11.glTexCoord2f(0,0);
		GL11.glVertex3f(blockrenderwidth/2, blockrenderwidth/2, blockrenderwidth/2); // Top Left Of The Quad (Right)
		GL11.glTexCoord2f(0,1);
		GL11.glVertex3f(blockrenderwidth/2, -blockrenderwidth/2, blockrenderwidth/2); // Bottom Left Of The Quad (Right)
		GL11.glTexCoord2f(1,1);
		GL11.glVertex3f(blockrenderwidth/2, -blockrenderwidth/2, -blockrenderwidth/2); // Bottom Right Of The Quad (Right)
		GL11.glEnd(); // Done Drawing The Quad
		GL11.glEndList();

		wrfogColor = BufferUtils.createFloatBuffer(4);
		wrfogColor.put(0.75f);
		wrfogColor.put(0.75f);
		wrfogColor.put(0.75f);
		wrfogColor.put(1.0f);
		wrfogColor.position(0);	
		
		ViewFromEntity = DangerZone.player;
		Viewer_yaw = DangerZone.player.rotation_yaw_head;

	}
	
	/*
	 * Preloads ALL the block textures from ALL the mods.
	 */
	public void loadBlockTextures(){
		int side, bid;
		StitchedTexture st = null;
		for(bid=0;bid<Blocks.blocksMAX;bid++){
			if(Blocks.BlockArray[bid] != null){
				for(side=0;side<6;side++){
					st = Blocks.BlockArray[bid].getStitchedTexture(side);
					if(st.texturesindex < 0){
						//Add this thing!
						StitchedTextureFile stf = stitches[next_stitch];
						if(stf == null){
							stitches[next_stitch] = new StitchedTextureFile();
						}
						stf = stitches[next_stitch];
						if(stf.nexty >= 32){ //filled up!
							stitches[next_stitch].doMakeMipMaps();
							next_stitch++;
							stitches[next_stitch] = new StitchedTextureFile();
						}
						st.texturesindex = next_stitch;
						StitchedTexture stnew = stitches[next_stitch].doAddTextureToBuffer(Blocks.BlockArray[bid].getStitchedTextureName(side));
						if(stnew != null){
							st.yoffsetmax = stnew.yoffsetmax;
							st.yoffsetmin = stnew.yoffsetmin;
							st.xoffsetmax = stnew.xoffsetmax;
							st.xoffsetmin = stnew.xoffsetmin;
						}
					}
				}
			}
		}
		stitches[next_stitch].doMakeMipMaps();
	}
	
	public void loadOneBlockTexture(int bid){
		int side;
		StitchedTexture st = null;		
		if(Blocks.BlockArray[bid] != null){
			for(side=0;side<6;side++){
				st = Blocks.BlockArray[bid].getStitchedTexture(side);
				if(st.texturesindex < 0){
					//Add this thing!
					StitchedTextureFile stf = stitches[next_stitch];
					if(stf == null){
						stitches[next_stitch] = new StitchedTextureFile();
					}
					stf = stitches[next_stitch];
					if(stf.nexty >= 32){ //filled up!
						next_stitch++;
						stitches[next_stitch] = new StitchedTextureFile();
					}
					st.texturesindex = next_stitch;
					StitchedTexture stnew = stitches[next_stitch].doAddTexture(Blocks.BlockArray[bid].getStitchedTextureName(side));
					if(stnew != null){
						st.yoffsetmax = stnew.yoffsetmax;
						st.yoffsetmin = stnew.yoffsetmin;
						st.xoffsetmax = stnew.xoffsetmax;
						st.xoffsetmin = stnew.xoffsetmin;
					}
				}
			}
		}
	}
	
	private void doHouseKeeping(World world){
		
		framecounter++;
		if(builder == null){
			//Let's get to making some data!
			builder = new VBODataBuilderThread(world);
			Thread cwt = new Thread(builder);
			cwt.setPriority(Thread.NORM_PRIORITY);
			cwt.start();
		}
		
		
		fpscounter++;
		nowtime = System.currentTimeMillis();
		if(nowtime - lasttime > 1000){
			fps = ((fps*2) + fpscounter)/3; //smoothed just a little....
			//fps = 9;
			if(fps < 1)fps = 1; //divide by zero protection for things...
			fpscounter = 0;
			lasttime = nowtime;
			pps = DangerZone.packets_per_second;
			DangerZone.packets_per_second = 0;
			//cps = DangerZone.chunks_per_second;
			DangerZone.chunks_per_second = 0;
		}
		
		//
		// DIMENSION CHANGE! CLEAN EVERYTHING!
		//
		if(lastplayerdimension != DangerZone.player.dimension){
			//EMERGENCY VBO CLEANUP! Can't have two dimensions at once on some machines...
			//can cause out-of-memory problems. Don't need the old ones anyway!
			world.chunkcache.releaseAllVBOs();
			lastplayerdimension = DangerZone.player.dimension;
			CleanerThread.cleanmenowplease = true;
			while(CleanerThread.cleanmenowplease){
				Thread.yield();
			}
		}
		
		//reload a texture... coloring block?
		if(oneblock != -1){
			loadOneBlockTexture(oneblock);
			oneblock = -1;
		}
		
		/*
		 * VBO housekeeping.
		 * We keep a maximum number in the map, and delete the oldest as we run over...
		 * We can do this, because when a chunk looks up a vboid in the map and it is invalid,
		 * it will automatically create a new one.
		 * But first, see if there are any on the delete list!
		 */
		
		VBOlistlock.lock();
		if(!VBO_delete_list.isEmpty()){
			Iterator<long[]> ii = VBO_delete_list.iterator();
			long[] st;
			int vdx;
			while(ii.hasNext()){
				st = ii.next();
				if(st != null){
					for(vdx=0;vdx<20;vdx++){
						if(st[vdx] == 0)continue;
						VBOBuffer v = VBOmap.get(st[vdx]);
						if(v != null){
							VBOmap.remove(v.VBOid);
							vbocount--;
							v.free();
							//System.out.printf("VBOid %d removed\n", v.VBOid);
							//System.out.printf("VBOFAIL: normal delete\n");
						}
						st[vdx] = 0;
					}
				}
				ii.remove();
			}
		}	
		
		int maxvbo = 3200;
		int maxdirect = 600;
		if(!DangerZone.bits64mode){
			maxvbo = 2400;
			maxdirect = 400;
		}
		
		if(DangerZone.renderdistance <= 16){ //Player hurting for speed, hack down on memory usage!
			int calc =  DangerZone.renderdistance * 2;
			calc = calc * calc;
			calc *= 3; //allow up to 3 vbos for each chunk in viewable area
			if(calc < maxvbo)maxvbo = calc;
			//cut down on memory too
			calc = (600 * DangerZone.renderdistance)/24;
			if(calc < maxdirect)maxdirect = calc;
		}
				
		//if STILL too many...
		if(vbocount > maxvbo || VBOmemorysize/(1024*1024) > maxdirect){ //Too many hanging around?
			//blast them down below threshold again.
			//System.out.printf("count, size at start == %d, %d\n", vbocount, VBOmemorysize/(1024*1024));
			Iterator<Entry<Long, VBOBuffer>> it = VBOmap.entrySet().iterator();
			while(it.hasNext()){
				@SuppressWarnings("rawtypes")
				Map.Entry pair = (Map.Entry)it.next();
				VBOBuffer v = (VBOBuffer) pair.getValue();
				if(v != null){ //should never be null
					if(framecounter-v.lastusedframe > 10){ //if hasn't been drawn in the last 10 frames, wipe it.
						vbocount--;
						v.free();
						it.remove();
					}
				}
			}
			//System.out.printf("----count, size at end == %d, %d\n", vbocount, VBOmemorysize/(1024*1024));
		}
		
		VBOlistlock.unlock();
		
		//viewing entity got killed?
		if(ViewFromEntity != DangerZone.player){
			if(ViewFromEntity == null){
				ViewFromEntity = DangerZone.player;
				Viewer_yaw = DangerZone.player.rotation_yaw_head;
			}
			if(ViewFromEntity.deadflag){
				ViewFromEntity = DangerZone.player;
				Viewer_yaw = DangerZone.player.rotation_yaw_head;
			}
		}
		
		if(ViewFromEntity.dimension != DangerZone.player.dimension){
			//System.out.printf("OOoops! %d,  %d\n", ViewFromEntity.dimension, DangerZone.player.dimension);
			ViewFromEntity.dimension = DangerZone.player.dimension;
		}
		
	}
	
	private void calcBounceAndFocus(World world){
		double rdd;
		
		if(ViewFromEntity != DangerZone.player){
			bounce = lastbounce = 0;
			ViewFromEntity.rotation_pitch_head = ViewFromEntity.rotation_pitch_head % 360;
			ViewFromEntity.rotation_yaw_head = ViewFromEntity.rotation_yaw_head % 360;
			ViewFromEntity.rotation_roll_head = ViewFromEntity.rotation_roll_head % 360;
			Viewer_yaw = (360f-((ViewFromEntity.rotation_yaw_head+180)%360)); //it's bass-ackward, yes...
			focus_x = focus_y = focus_z = focus_bid = focus_meta = 0;
			focus_damage = focus_dist = 0;
			focus_entity = null;
			return;
		}
		
		Viewer_yaw = DangerZone.player.rotation_yaw_head;
		
		/*
		 * Add some bounce to our step!
		 */
		if(DangerZone.player.getGameMode() != GameModes.GHOST && DangerZone.player.getGameMode() != GameModes.LIMBO && DangerZone.player.getRiddenEntity() == null){
			rdd = Math.sqrt(DangerZone.player.motionx*DangerZone.player.motionx + DangerZone.player.motionz*DangerZone.player.motionz);
			if(DangerZone.player.isLadder())rdd = Math.abs(DangerZone.player.motiony*2);
			traveled += rdd;
			if(DangerZone.player.isBaby()){
				traveled += rdd;
				rdd /= 2;
			}
			rdd /= 18; //this is velocity!!!! NOT amplitude....
			
			//System.out.printf("rdd = %f, trvled = %f\n", (float)rdd, (float)traveled);			
			//get SMOOTHER with speed!
			rdd *= (0.35f-rdd)*2.0f;
			if(rdd < 0)rdd = 0;
			
			if(DangerZone.gofast != 0){
				bounce = (float) (rdd * Math.cos(Math.toRadians(traveled*11.3d)));				
			}else{
				bounce = (float) (rdd * Math.cos(Math.toRadians(traveled*13.3d)));
				bounce *= 2.5;
			}
			
			if(!DangerZone.player.getOnGround() && !DangerZone.player.isLadder()){
				bounce = 0;
			}

		}else{
			bounce = 0;
		}
		
		if(bounce != 0){
			if((lastbounce > 0 && bounce < 0) || (lastbounce < 0 && bounce > 0)){
				DangerZone.world.playSound(Blocks.getStepSound(DangerZone.world.getblock(DangerZone.player.dimension, (int)DangerZone.player.posx, (int)(DangerZone.player.posy-0.1f), (int)DangerZone.player.posz)), 
						DangerZone.player.dimension, DangerZone.player.posx, DangerZone.player.posy, DangerZone.player.posz, 
						0.25f, 1.0f + ((DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat())*0.2f));
			}
			
		}
		
		lastbounce = bounce;
		
		//just a barely noticeable little swagger...
		DangerZone.player.posx += Math.cos(Math.toRadians(DangerZone.player.rotation_yaw))*bounce/12f;
		DangerZone.player.posz += Math.sin(Math.toRadians(DangerZone.player.rotation_yaw))*bounce/12f;
		
		if(bounce < 0)bounce = -bounce;
		
		DangerZone.player.rotation_pitch_head = DangerZone.player.rotation_pitch_head % 360;
		DangerZone.player.rotation_yaw_head = DangerZone.player.rotation_yaw_head % 360;
		DangerZone.player.rotation_roll_head = DangerZone.player.rotation_roll_head % 360;
		
		//what are we looking at?
		find_focus(world);
	}
	
		
	private void lookAtSelf(World world){
		
		f5x = f5y = f5z = f5yaw = f5pitch = 0;
		
		if(DangerZone.f5_front){
			float t;
			float dst = ViewFromEntity.getHeight()*3.25f;
			if(dst < 6)dst = 6;
			Entity e = ViewFromEntity.getRiddenEntity();
			if(e != null){
				float ddst = e.getHeight()*3.25f;
				if(dst < ddst)dst = ddst;
			}
			t = (float) Math.cos(Math.toRadians(ViewFromEntity.rotation_pitch_head));
			f5y = (float) Math.sin(Math.toRadians(ViewFromEntity.rotation_pitch_head))*dst*blockrenderwidth;
			f5x = (float) Math.cos(Math.toRadians(Viewer_yaw+90f))*dst*blockrenderwidth*Math.abs(t);
			f5z = (float) Math.sin(Math.toRadians(Viewer_yaw+90f))*dst*blockrenderwidth*Math.abs(t);
			f5yaw = 180f;
			f5pitch = -ViewFromEntity.rotation_pitch_head*2;
		}
		if(DangerZone.f5_back){
			float t;
			float dst = ViewFromEntity.getHeight()*4.25f;
			if(dst < 8)dst = 8;
			Entity e = ViewFromEntity.getRiddenEntity();
			if(e != null){
				float ddst = e.getHeight()*4.25f;
				if(dst < ddst)dst = ddst;
			}
			t = (float) Math.cos(Math.toRadians(ViewFromEntity.rotation_pitch_head));
			f5y = -(float) Math.sin(Math.toRadians(ViewFromEntity.rotation_pitch_head))*dst*blockrenderwidth;
			f5x = -(float) Math.cos(Math.toRadians(Viewer_yaw+90f))*dst*blockrenderwidth*Math.abs(t);
			f5z = -(float) Math.sin(Math.toRadians(Viewer_yaw+90f))*dst*blockrenderwidth*Math.abs(t);
			f5yaw = 0f;
			f5pitch = 0f;
		}
		
		if(DangerZone.f5_front || DangerZone.f5_back){
			Scalef5toSolid();
		}
	}
	
	private void doClickyStuff(World world){
		int i;
		
		if(ViewFromEntity != DangerZone.player)return;
		
		if(hit_cycle_count != 0){
			//scale to fps
			i = fps/10;
			if(i < 1)i = 1;
			if(i > 6)i = 6;
			hit_cycles = i+1;			
			hit_cycle_count += hit_cycle_dir;
			if(hit_cycle_count >= hit_cycles){
				if(DangerZone.doleftclick){
					DangerZone.player.leftclick(world, usex, usey, usez, uses, usee==null?0:usee.entityID, DangerZone.magic_power, DangerZone.magic_type);
					DangerZone.doleftclick = false;
					DangerZone.magic_power = 0;
				}
				if(DangerZone.dorightclick){
					DangerZone.player.rightclick(world, usex, usey, usez, uses, usee==null?0:usee.entityID);
					DangerZone.dorightclick = false;
					DangerZone.magic_power = 0;
				}
				hit_cycle_dir = -1;
				//hit_cycle_count = hit_cycles;
			}
		}else{
			if(DangerZone.do_hit_cycle != 0){
				//DangerZone.do_hit_cycle = 0;
				hit_cycle_count = 1;
				hit_cycle_dir = 1;
				usex = focus_x;
				usey = focus_y;
				usez = focus_z;
				uses = focus_side;
				usee = focus_entity;
				if(DangerZone.doleftclick){
					int which = DangerZone.player.world.rand.nextInt(6);
					if(which == 0)DangerZone.player.world.playSound("DangerZone:swish3", DangerZone.player.dimension, DangerZone.player.posx, DangerZone.player.posy+1.25d, DangerZone.player.posz, 0.25f, 1);
					if(which == 1)DangerZone.player.world.playSound("DangerZone:swish4", DangerZone.player.dimension, DangerZone.player.posx, DangerZone.player.posy+1.25d, DangerZone.player.posz, 0.25f, 1);
					if(which == 2)DangerZone.player.world.playSound("DangerZone:swish5", DangerZone.player.dimension, DangerZone.player.posx, DangerZone.player.posy+1.25d, DangerZone.player.posz, 0.25f, 1);
					if(which == 3)DangerZone.player.world.playSound("DangerZone:swish6", DangerZone.player.dimension, DangerZone.player.posx, DangerZone.player.posy+1.25d, DangerZone.player.posz, 0.25f, 1);
					if(which == 4)DangerZone.player.world.playSound("DangerZone:swish7", DangerZone.player.dimension, DangerZone.player.posx, DangerZone.player.posy+1.25d, DangerZone.player.posz, 0.25f, 1);
					if(which == 5)DangerZone.player.world.playSound("DangerZone:swish8", DangerZone.player.dimension, DangerZone.player.posx, DangerZone.player.posy+1.25d, DangerZone.player.posz, 0.25f, 1);
				}
			}
		}
		
		if(eat_cycle_count != 0){			
			//scale to fps
			i = fps/2;
			if(i < 1)i = 1;
			if(i > 30)i = 30;
			eat_cycles = i+1;			
			eat_cycle_count += eat_cycle_dir;
			if(eat_cycle_count >= eat_cycles){
				eat_cycle_count = eat_cycles;
				if(eat_delay_count > 0){
					if(eat_sound){
						world.playSound("DangerZone:eating", DangerZone.player.dimension, DangerZone.player.posx, DangerZone.player.posy, DangerZone.player.posz, 0.25f, 1.0f);
					}
					eat_sound = false;
					eat_delay_count--;
				}else{
					InventoryContainer ic = DangerZone.player.getHotbar(DangerZone.player.gethotbarindex());
					if(ic != null && Items.isFood(ic.iid)){
						if(DangerZone.player.getHunger() < DangerZone.player.getMaxHunger()  || Items.eatAnyTime(ic.iid)){
							Item itm = ic.getItem();							
							if(itm != null){
								itm.onFoodEaten(DangerZone.player); //client side callback!
								DangerZone.server_connection.playerActionToServer(0, 2, itm.itemID, 0, 0, 0, 0, 0, 0);
							}
						}
					}
					eat_cycle_dir = -1;
				}
			}
		}else{
			if(DangerZone.do_food_cycle != 0){
				InventoryContainer ic = DangerZone.player.getHotbar(DangerZone.player.gethotbarindex());
				if(ic != null && Items.isFood(ic.iid)){
					if(DangerZone.player.getHunger() < DangerZone.player.getMaxHunger() || Items.eatAnyTime(ic.iid)){
						eat_cycle_count = 1;
						eat_cycle_dir = 1;
						eat_delay_count = fps+10;
						eat_sound = true;
					}
				}
			}
		}
		
		if(DangerZone.dorightclickup){			
			//do something here!
			DangerZone.player.rightclickup(world, usex, usey, usez, uses, usee==null?0:usee.entityID, (DangerZone.rightbuttondowncounter*100)/fps);			
			DangerZone.dorightclickup = false;
			DangerZone.rightbuttondowncounter = 0;
		}
		DangerZone.player.setRightButtonDownCount(((float)DangerZone.rightbuttondowncounter*100f)/fps); //so other people can see too! Goes to server...
		

	}
	
	private void drawPlayerHeldItem(World world){
		
		if(ViewFromEntity != DangerZone.player)return;
		
		//Draw the currently held item!
		//If not looking at self! Otherwise, humanoid renderer will draw it...
		if(DangerZone.player.getHotbar(DangerZone.player.gethotbarindex()) != null && !DangerZone.f5_front && !DangerZone.f5_back){
			InventoryContainer ic = DangerZone.player.getHotbar(DangerZone.player.gethotbarindex());
			
			GL11.glPushMatrix();
			GL11.glTranslatef(6f, -(4+(bounce*2)), -12f*45f/DangerZone.fieldOfView);
			GL11.glRotatef(-15f, 1.0f, 3.0f, 0.0f); // Rotate The Entity On X, Y & Z

			recalcBrightness(DangerZone.player.dimension, (int)(DangerZone.player.posy+(DangerZone.player.getHeight()/2)));
			setBrightness(WorldRendererUtils.getLightMapValue(world, DangerZone.player.dimension, (int)DangerZone.player.posx, (int)(DangerZone.player.posy+(DangerZone.player.getHeight()/2)), (int)DangerZone.player.posz));
			
			if(ic.bid != 0){
				if(hit_cycle_count != 0){
					GL11.glTranslatef(hitx*((float)hit_cycle_count/hit_cycles)/2, hity*((float)hit_cycle_count/hit_cycles)/2, hitz*((float)hit_cycle_count/hit_cycles)/2);
					GL11.glRotatef(-hitp*((float)hit_cycle_count/hit_cycles), 1.0f, 0.0f, 0.0f); // Rotate The Entity On X, Y & Z
					GL11.glRotatef(hitw*((float)hit_cycle_count/hit_cycles), 0.0f, 1.0f, 0.0f);
					GL11.glRotatef(-hitr*((float)hit_cycle_count/hit_cycles), 0.0f, 0.0f, 1.0f);
				}
				GL11.glScalef(0.20f, 0.20f, 0.20f);
				if(Blocks.hasOwnRenderer(ic.bid)){
					//Blocks.renderMe(this, world, DangerZone.player.dimension, (int)DangerZone.player.posx, (int)DangerZone.player.posy, (int)DangerZone.player.posz, ic.bid, 0, 0xff, false);
					Blocks.renderMeHeld(DangerZone.wr, DangerZone.player, ic.bid, false);
				}else{
					drawTexturedCube(0xff, Blocks.isSolidForRender(ic.bid), ic.bid, 0, false);
				}
			}else{
				if(ic.iid != 0){
					GL11.glScalef(0.35f, 0.35f, 0.35f);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					
					//OK, this has gotten ugly and most of it should probably be in the item routine.... ugh....
					if(Items.isFlipped(ic.iid)){
						if(Items.getfullholdcount(ic.iid)!=0 && (DangerZone.rightbuttondowncounter != 0 || DangerZone.rapidfire_delay != 0)){								
							GL11.glTranslatef(-10.8f+DangerZone.testx, 8.7f+DangerZone.testy, 7.9f+DangerZone.testz);
							GL11.glRotatef(-174.2f+DangerZone.testp, 1.0f, 0.0f, 0.0f); // Rotate The Entity On X, Y & Z
							GL11.glRotatef(65.3f+(bounce*4)+DangerZone.testw, 0.0f, 1.0f, 0.0f);
							GL11.glRotatef(-39.2f+(bounce*4)+DangerZone.testr, 0.0f, 0.0f, 1.0f);
						}else if(hit_cycle_count != 0){														
								GL11.glTranslatef(1+DangerZone.testx+hitx*((float)hit_cycle_count/hit_cycles), 3+DangerZone.testy+hity*((float)hit_cycle_count/hit_cycles), 3+DangerZone.testz+hitz*((float)hit_cycle_count/hit_cycles));
								GL11.glRotatef(-91+DangerZone.testp+hitp*((float)hit_cycle_count/hit_cycles), 1.0f, 0.0f, 0.0f); // Rotate The Entity On X, Y & Z
								GL11.glRotatef(102+(bounce*4)+DangerZone.testw+hitw*((float)hit_cycle_count/hit_cycles), 0.0f, 1.0f, 0.0f);
								GL11.glRotatef(-124+(bounce*4)+DangerZone.testr+hitr*((float)hit_cycle_count/hit_cycles), 0.0f, 0.0f, 1.0f);
						}else if(eat_cycle_count != 0){
							eatbounce = bounce;
							if(eat_cycle_count >= eat_cycles){						
								if(eat_delay_count > 0){
									eatbounce = (float) (Math.cos(Math.toRadians(DangerZone.player.lifetimeticker*25.3f)));
								}
							}
							GL11.glTranslatef(1+DangerZone.testx+eatx*((float)eat_cycle_count/eat_cycles), 3+DangerZone.testy+eaty*((float)eat_cycle_count/eat_cycles), 3+DangerZone.testz+eatz*((float)eat_cycle_count/eat_cycles));
							GL11.glRotatef(-91+DangerZone.testp+eatp*((float)eat_cycle_count/eat_cycles), 1.0f, 0.0f, 0.0f); // Rotate The Entity On X, Y & Z
							GL11.glRotatef(102+(eatbounce*4)+DangerZone.testw+eatw*((float)eat_cycle_count/eat_cycles), 0.0f, 1.0f, 0.0f);
							GL11.glRotatef(-124+(eatbounce*4)+DangerZone.testr+eatr*((float)eat_cycle_count/eat_cycles), 0.0f, 0.0f, 1.0f);
						}else{
							GL11.glTranslatef(1+DangerZone.testx, 3+DangerZone.testy, 3+DangerZone.testz);
							GL11.glRotatef(-91+DangerZone.testp, 1.0f, 0.0f, 0.0f); // Rotate The Entity On X, Y & Z
							GL11.glRotatef(102+(bounce*4)+DangerZone.testw, 0.0f, 1.0f, 0.0f);
							GL11.glRotatef(-124+(bounce*4)+DangerZone.testr, 0.0f, 0.0f, 1.0f);
						}
					}else{
						//other way
						if(Items.getfullholdcount(ic.iid)!=0 && (DangerZone.rightbuttondowncounter != 0 || DangerZone.rapidfire_delay != 0)){							
							GL11.glTranslatef(-7.7f+DangerZone.testx, 8.3f + DangerZone.testy, 5.4f+DangerZone.testz);
							GL11.glRotatef(-6.3f+DangerZone.testp, 1.0f, 0.0f, 0.0f); // Rotate The Entity On X, Y & Z
							GL11.glRotatef(-60.1f+(bounce*4)+DangerZone.testw, 0.0f, 1.0f, 0.0f);
							GL11.glRotatef(120.5f+(bounce*4)+DangerZone.testr, 0.0f, 0.0f, 1.0f);
						}else if(hit_cycle_count != 0){
							GL11.glTranslatef(1.2f+DangerZone.testx+hitxn*((float)hit_cycle_count/hit_cycles), 4.0f +DangerZone.testy+hityn*((float)hit_cycle_count/hit_cycles), 0.1f+DangerZone.testz+hitzn*((float)hit_cycle_count/hit_cycles));
							GL11.glRotatef(-30f+DangerZone.testp+hitpn*((float)hit_cycle_count/hit_cycles), 1.0f, 0.0f, 0.0f); // Rotate The Entity On X, Y & Z
							GL11.glRotatef(-67.7f+(bounce*4)+DangerZone.testw+hitwn*((float)hit_cycle_count/hit_cycles), 0.0f, 1.0f, 0.0f);
							GL11.glRotatef(120.5f+(bounce*4)+DangerZone.testr+hitrn*((float)hit_cycle_count/hit_cycles), 0.0f, 0.0f, 1.0f);
						}else if(eat_cycle_count != 0){
							eatbounce = bounce;
							if(eat_cycle_count >= eat_cycles){						
								if(eat_delay_count > 0){
									eatbounce = (float) (Math.cos(Math.toRadians(DangerZone.player.lifetimeticker*25.3f)));
								}
							}
							GL11.glTranslatef(1.2f+DangerZone.testx+eatxn*((float)eat_cycle_count/eat_cycles), 4.0f +DangerZone.testy+eatyn*((float)eat_cycle_count/eat_cycles), 0.1f+DangerZone.testz+eatzn*((float)eat_cycle_count/eat_cycles));
							GL11.glRotatef(-30f+DangerZone.testp+eatpn*((float)eat_cycle_count/eat_cycles), 1.0f, 0.0f, 0.0f); // Rotate The Entity On X, Y & Z
							GL11.glRotatef(-67.7f+(eatbounce*4)+DangerZone.testw+eatwn*((float)eat_cycle_count/eat_cycles), 0.0f, 1.0f, 0.0f);
							GL11.glRotatef(120.5f+(eatbounce*4)+DangerZone.testr+eatrn*((float)eat_cycle_count/eat_cycles), 0.0f, 0.0f, 1.0f);
						}else{
							GL11.glTranslatef(1.2f+DangerZone.testx, 4.0f + DangerZone.testy, 0.1f+DangerZone.testz);
							if(Items.isFlopped(ic.iid)){
								GL11.glRotatef(-6f+DangerZone.testp, 1.0f, 0.0f, 0.0f); // Rotate The Entity On X, Y & Z
							}else{
								GL11.glRotatef(-30f+DangerZone.testp, 1.0f, 0.0f, 0.0f); // Rotate The Entity On X, Y & Z
							}
								
							GL11.glRotatef(-67.7f+(bounce*4)+DangerZone.testw, 0.0f, 1.0f, 0.0f);
							GL11.glRotatef(120.5f+(bounce*4)+DangerZone.testr, 0.0f, 0.0f, 1.0f);
							
						}

					}
					forceloadtexture(Items.getTexture(ic.iid));
					Items.renderMeHeld(this, DangerZone.player, ic.iid, true);
					//Items.renderMe(this, world, DangerZone.player.dimension, (int)DangerZone.player.posx, (int)DangerZone.player.posy, (int)DangerZone.player.posz, ic.iid);
					GL11.glDisable(GL11.GL_BLEND);
				}
			}
			GL11.glPopMatrix();
		}
	}
	
	/*
	 * ****************************************************************************************************************************************
	 */
	public void renderWorld(World world) {
		int i,j;
		double pi = 3.1415926545D;
		double rdd, rr, rhdir;
		int torender = 6;
		float velocity;
		double dist = 0;

		//clean up some memory and buffers
		doHouseKeeping(world);

		//player movement and focus
		//also set correct display yaw, since player and server actually don't agree!
		calcBounceAndFocus(world);
		
		//looking at self?
		lookAtSelf(world);
		
		recalcSkyBrightness();
		if(DangerZone.f12_on && DangerZone.current_gui == null){
			sky_red = sky_green = sky_blue = 0.5f;
		}

		if(DangerZone.fog_enable){
			wrfogColor.position(0);
			wrfogColor.put(sky_red);
			wrfogColor.put(sky_green);
			wrfogColor.put(sky_blue);
			wrfogColor.put(1.0f);
			wrfogColor.position(0);
			GL11.glFog (GL11.GL_FOG_COLOR, wrfogColor);
		}
		GL11.glClearColor(sky_red, sky_green, sky_blue, 0.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer
		GL11.glLoadIdentity(); // Reset The View
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL14.glSecondaryColor3f(0.0f, 0.0f, 0.0f);
		GL11.glEnable(GL14.GL_COLOR_SUM);

		recalcBrightness(ViewFromEntity.dimension, 60);
		setBrightness();
		last_texture = -1;
		
				
		if(DangerZone.f12_on && DangerZone.current_gui == null){
			
			//DangerZone.player.rotation_pitch = 0;
			//DangerZone.player.rotation_pitch_head = 0;
			//DangerZone.player.rotation_yaw = 0;
			//DangerZone.player.rotation_yaw_head = 0;
			//DangerZone.player.rotation_pitch_motion = 0;
			//DangerZone.player.rotation_yaw_motion = 0;
			//DangerZone.player.rotation_roll_motion = 0;
			
			
			GL11.glPushMatrix();  //PUSH ---------------------------------------------------------------------------------------------PUSH
			//GL11.glRotatef(DangerZone.player.rotation_pitch, 1.0f, 0.0f, 0.0f); // Rotate The View On X, Y & Z
			//GL11.glRotatef(DangerZone.player.rotation_yaw, 0.0f, 1.0f, 0.0f); // Rotate The View On X, Y & Z

			GL11.glTranslated(0, -((DangerZone.player.posy)*blockrenderwidth), 0);
			
			sky_red = sky_green = sky_blue = 0.15f;
			
			WorldRendererUtils.drawShowcaseMonster();
			
			GL11.glPopMatrix();   //POP -----------------------------------------------------------------------------------------------------POP
			GL11.glFlush();
			return;
		}
		
		
		GL11.glPushMatrix();  //PUSH ---------------------------------------------------------------------------------------------PUSH
		
		GL11.glRotatef(ViewFromEntity.rotation_pitch_head+f5pitch, 1.0f, 0.0f, 0.0f); // Rotate The View On X, Y & Z
		GL11.glRotatef(Viewer_yaw+f5yaw, 0.0f, 1.0f, 0.0f); // Rotate The View On X, Y & Z
		//GL11.glRotatef(ViewFromEntity.rotation_roll, 0.0f, 0.0f, 1.0f); // Rotate The View On X, Y & Z - works! But is weird! :)
		//Ow. The next line hurts...
		GL11.glTranslated(-((((ViewFromEntity.posx)*blockrenderwidth))%(16*blockrenderwidth))+blockrenderwidth/2 + f5x, 
				-(((ViewFromEntity.posy+ViewFromEntity.eyeheight+bounce)-0.5f)*blockrenderwidth) + f5y, 
				-((((ViewFromEntity.posz)*blockrenderwidth))%(16*blockrenderwidth))+blockrenderwidth/2 + f5z);
		
		
		if(ViewFromEntity.rotation_pitch_head < 45 || ViewFromEntity.rotation_pitch_head > 315){ //looking down or up?
			torender = DangerZone.renderdistance;
		}else{
			torender = DangerZone.renderdistance - 2;
		}
		if(torender < 3)torender = 3;
		do_draw_focus = false;
		

		

		
		WorldRendererUtils.drawSunAndMoon(world, normal_sun);
		
		//int skipped = 0;
		for(i=-torender;i<=torender;i++){
			for(j=-torender;j<=torender;j++){ 
				if(Math.sqrt((i*i)+(j*j))<=torender){
					dist = (float) Math.sqrt(i*i + j*j);

					rr = (float) Math.atan2((j*16), (i*16));					
					rhdir = Math.toRadians(((Viewer_yaw+f5yaw)-90)%360f); 
					rdd = Math.abs(rr - rhdir)%(pi*2.0D);
					if(rdd > pi)rdd = rdd-(pi*2.0D);
					rdd = Math.abs(rdd); //Total differential, minus sign.
				
					if(ViewFromEntity.rotation_pitch_head < 45 || ViewFromEntity.rotation_pitch_head > 315){ //NOT looking down or up?
						if(dist > 8 && rdd > pi/3*DangerZone.fieldOfView/45.0f){
							//skipped++;
							continue; //Don't render what we can't see!
						}
						if(dist > 4 && rdd > pi/2*DangerZone.fieldOfView/45.0f){
							//skipped++;
							continue; //Don't render what we can't see!
						}
						if(dist > 1 && rdd > pi*3/4*DangerZone.fieldOfView/45.0f){
							//skipped++;
							if(!DangerZone.f5_front && !DangerZone.f5_back)continue; //Don't render what we can't see!
						}
					}	
					
					renderChunk(world, i, j, ViewFromEntity.posx, ViewFromEntity.posy, ViewFromEntity.posz);					
				}
			}
		}

		
		GL11.glPopMatrix(); //POP -----------------------------------------------------------------------------------------------------POP
		GL11.glFlush();
		//And finally, the FOCUS block
		if(focus_x != 0 && focus_z != 0){
			//it HAS focus
			//but should we draw it?
			int pbid = world.getblock(ViewFromEntity.dimension, (int)ViewFromEntity.posx, (int)(ViewFromEntity.posy+ViewFromEntity.eyeheight), (int)ViewFromEntity.posz);
			if(pbid == 0 || !Blocks.isSolid(pbid) || Blocks.isLiquid(pbid)){
				GL11.glPushMatrix();
				GL11.glRotatef(ViewFromEntity.rotation_pitch_head+f5pitch, 1.0f, 0.0f, 0.0f); // Rotate The View On X, Y & Z
				GL11.glRotatef(Viewer_yaw+f5yaw, 0.0f, 1.0f, 0.0f); // Rotate The View On X, Y & Z			
				GL11.glTranslated(-((((ViewFromEntity.posx)-((double)focus_x+0.5f))*blockrenderwidth))+f5x, 
						-((((ViewFromEntity.posy)+ViewFromEntity.eyeheight+bounce)-((double)focus_y+0.5f))*blockrenderwidth)+f5y, 
						-(((ViewFromEntity.posz)-((double)focus_z+0.5f))*blockrenderwidth)+f5z);
				//Damaged block animation
				if(focus_damage != 0 && focus_maxdamage > 0){								
					GL11.glScalef(1.0f - (0.65f*focus_damage/focus_maxdamage), 1.0f - (0.65f*focus_damage/focus_maxdamage), 1.0f - (0.65f*focus_damage/focus_maxdamage));
					GL11.glRotatef((float) (15f*focus_damage/focus_maxdamage*Math.cos(Math.toRadians(ViewFromEntity.lifetimeticker*3))), 1.0f, 0, 0 );
					GL11.glRotatef((float) (15f*focus_damage/focus_maxdamage*Math.cos(Math.toRadians(ViewFromEntity.lifetimeticker*7))), 0, 1.0f, 0 );
					GL11.glRotatef((float) (15f*focus_damage/focus_maxdamage*Math.cos(Math.toRadians(ViewFromEntity.lifetimeticker*5))), 0, 0, 1.0f );
				}else{
					GL11.glScalef(1.001f, 1.001f, 1.001f);
				}
				setBrightnessFocusBlock();
				if(Blocks.hasOwnRenderer(focus_bid)){
					//GL11.glPushMatrix();
					Blocks.renderMe(this, world, ViewFromEntity.dimension, focus_x, focus_y, focus_z, focus_bid, focus_meta, 0xff, true);
					//GL11.glPopMatrix();								
				}else{
					drawTexturedCube(0xff, Blocks.isSolidForRender(focus_bid), focus_bid, focus_meta, true);
				}
				setBrightness();
				GL11.glPopMatrix();
			}
		}
		

		//Entity ex = ViewFromEntity.getRiddenEntity();	
		//if(ex != null){
		//	System.out.printf("ydiff = %f\n", ViewFromEntity.posy - ex.display_posy);
		//}
		
		//Render all the entities!
		GL11.glPushMatrix(); //PUSH ---------------------------------------------------------------------------------------------PUSH
		GL11.glRotatef(ViewFromEntity.rotation_pitch_head+f5pitch, 1.0f, 0.0f, 0.0f); // Rotate The View On X, Y & Z
		GL11.glRotatef(Viewer_yaw+f5yaw, 0.0f, 1.0f, 0.0f); // Rotate The View On X, Y & Z
		//GL11.glRotatef(ViewFromEntity.rotation_roll, 0.0f, 0.0f, 1.0f); // Rotate The View On X, Y & Z - works! But is weird! :)
		
		int inext = 0;
		boolean doforce = false;
		ModelBase modl = null;	
		Entity morphent = null;
		String petname =  null;

		
		for(inext = 0; inext < DangerZone.max_entities; inext++){
			
			ent = DangerZone.entityManager.entities[inext];

			if(ent != null){
				
				if(ent.dimension == ViewFromEntity.dimension && (ent.entityID != ViewFromEntity.entityID || ViewFromEntity.always_draw)){
					dist = ViewFromEntity.getDistanceFromEntity(ent);
					if(dist <= ent.maxrenderdist && dist <= torender*16 ){
						//filter out some entities that we can't see...
						rr = (float) Math.atan2((ent.display_posz-ViewFromEntity.posz), (ent.display_posx-ViewFromEntity.posx));					
						rhdir = Math.toRadians(((Viewer_yaw+f5yaw)-90)%360f); 
						rdd = Math.abs(rr - rhdir)%(pi*2.0D);
						if(rdd > pi)rdd = rdd-(pi*2.0D);
						rdd = Math.abs(rdd); //Total differential, minus sign.	
						if(!ent.always_draw){
							if(ViewFromEntity.rotation_pitch_head < 45 || ViewFromEntity.rotation_pitch_head > 315){ //NOT looking down or up?						
								if(dist > 16 && rdd > pi/2*DangerZone.fieldOfView/45.0f){
									if(!DangerZone.f5_front && !DangerZone.f5_back){
										continue; //Don't render what we can't see!
									}
								}
							}
						}
						if(ent.isInvisible())continue;

						if(ent.model != null){	
							modl = ent.model;
							morphent = null;
							if(ent instanceof Player){
								Player pp = (Player)ent;
								modl = pp.model;
								morphent = pp.morph;
							}
							petname = ent.getPetName();
							
							GL11.glPushMatrix();  //SAVE!
							//put the entitiy where it goes.
							GL11.glTranslated(-((((ViewFromEntity.posx)-ent.display_posx)*blockrenderwidth))+f5x, 
									-((((ViewFromEntity.posy)+ViewFromEntity.eyeheight+bounce)-ent.display_posy)*blockrenderwidth)+f5y, 
									-(((ViewFromEntity.posz)-ent.display_posz)*blockrenderwidth)+f5z);
							//Rotate it around						
							if(ent.display_rotation_roll != 0)GL11.glRotatef(ent.display_rotation_roll, 0.0f, 0.0f, 1.0f); // Rotate The Entity On X, Y & Z
							if(ent.display_rotation_yaw != 0)GL11.glRotatef(ent.display_rotation_yaw, 0.0f, 1.0f, 0.0f); // Rotate The Entity On X, Y & Z
							if(ent.display_rotation_pitch != 0)GL11.glRotatef(ent.display_rotation_pitch, 1.0f, 0.0f, 0.0f); // Rotate The Entity On X, Y & Z

							recalcBrightness(ViewFromEntity.dimension, (int)(ent.display_posy+(ent.getHeight()/2)));
							setBrightness(ent.getBrightness()+WorldRendererUtils.getLightMapValue(ent.world, ent.dimension, (int)ent.display_posx, (int)(ent.display_posy+(ent.getHeight()/2)), (int)ent.display_posz));
							//Now all it has to do is draw itself.

							ouch = 1.0f;//Let it scale itself
							washurt = ent.isHurt();
							if(washurt){
								ouch = 1.1f;
								GL11.glColor3f(1.0f, 0.15f, 0.15f);
							}
							if(ent.getDeathFactor() > 0){
								ouch = ent.getDeathFactor();
								//System.out.printf("Entity Death %f\n", ouch);
							}
							velocity = (float) Math.sqrt((ent.motionx*ent.motionx)+(ent.motiony*ent.motiony)+(ent.motionz*ent.motionz));

							cdir = (float) Math.toRadians(ent.display_rotation_pitch);
							tdir = (float) Math.toRadians(ent.rotation_pitch_head);
							pdiff = tdir - cdir;
							while(pdiff>Math.PI)pdiff -= Math.PI*2;
							while(pdiff<-Math.PI)pdiff += Math.PI*2;
							pdiff = (float) Math.toDegrees(pdiff);

							cdir = (float) Math.toRadians(ent.display_rotation_yaw);
							tdir = (float) Math.toRadians(ent.rotation_yaw_head);
							ydiff = tdir - cdir;
							while(ydiff>Math.PI)ydiff -= Math.PI*2;
							while(ydiff<-Math.PI)ydiff += Math.PI*2;
							ydiff = (float) Math.toDegrees(ydiff);

							cdir = (float) Math.toRadians(ent.display_rotation_roll);
							tdir = (float) Math.toRadians(ent.rotation_roll_head);
							rdiff = tdir - cdir;
							while(rdiff>Math.PI)rdiff -= Math.PI*2;
							while(rdiff<-Math.PI)rdiff += Math.PI*2;
							rdiff = (float) Math.toDegrees(rdiff);						

							GL11.glPushMatrix();  //SAVE! - because model can scale and throw off name!
							if(morphent != null){
								if(doforce){									
									forceloadtexture(modl.getTexture(morphent)); 		//Get a texture for it	
									doforce = false;
								}else{
									loadtexture(modl.getTexture(morphent)); 		//Get a texture for it	
								}							
								modl.doScale(morphent);	
								modl.render(morphent, (float)ent.lifetimeticker, velocity, pdiff, ydiff, rdiff, ouch);	//Draw!	
							}else{
								if(doforce){
									forceloadtexture(modl.getTexture(ent)); 		//Get a texture for it	
									doforce = false;
								}else{
									loadtexture(modl.getTexture(ent)); 		//Get a texture for it	
								}
								modl.doScale(ent);	
								modl.render(ent, (float)ent.lifetimeticker, velocity, pdiff, ydiff, rdiff, ouch);	//Draw!	
							}
							GL11.glPopMatrix();	//RESTORE!!
							

							if(ent.display_rotation_pitch != 0)GL11.glRotatef(-ent.display_rotation_pitch, 1.0f, 0.0f, 0.0f); // Rotate The Entity On X, Y & Z
							if(ent.display_rotation_yaw != 0)GL11.glRotatef(-ent.display_rotation_yaw, 0.0f, 1.0f, 0.0f); // Rotate The Entity On X, Y & Z
							if(ent.display_rotation_roll != 0)GL11.glRotatef(-ent.display_rotation_roll, 0.0f, 0.0f, 1.0f); // Rotate The Entity On X, Y & Z
							GL11.glRotatef(-(Viewer_yaw+f5yaw), 0.0f, 1.0f, 0.0f); // Rotate FLAT to player
							if(petname != null){
								doforce = true;
								if(ent.isBaby())GL11.glScalef(0.25f, 0.25f, 0.25f);
								GL11.glScalef(0.15f, 0.15f, 0.15f);
								//why the centering scale is so different from the Y scale... I don't even want to know...
								//WorldRendererUtils.textAt(font, - (5 * ent.getPetName().length()), (ent.getNameHeight() * 130)+20, ent.getPetName());
								WorldRendererUtils.textAt(font, - (font.getWidth(petname)/2), (ent.getNameHeight() * 125)+30, petname);
								GL11.glScalef(1.0f/0.15f, 1.0f/0.15f, 1.0f/0.15f);
								if(ent.isBaby())GL11.glScalef(4, 4, 4);
							}							
							if(ent.getOnFire() > 0){
								WorldRendererUtils.drawEntityOnFire(ent);
							}		
							if(washurt)setBrightness();
							GL11.glPopMatrix();	//RESTORE!!
						}
					//}else{
					//	System.out.printf("dist problem! %f\n", dist);
					}
				}
			}
			
		}
		petname =  null;
		
		//Now draw some particles!		
		Particle pst;
		int npart;
		inext = 0;
		DangerZone.particleManager.particle_list_lock.lock();
		npart = DangerZone.particleManager.particle_list.size();
		
		while(inext < npart){
			pst = DangerZone.particleManager.particle_list.get(inext);		
			dist = pst.getDistanceFromEntity(ViewFromEntity);
			if(dist <= pst.maxrenderdist && dist <= torender*16 ){					
				//filter out some particles that we can't see... saves a lot of CPU for rain!
				rr = (float) Math.atan2((pst.posz-ViewFromEntity.posz), (pst.posx-ViewFromEntity.posx));					
				rhdir = Math.toRadians(((Viewer_yaw+f5yaw)-90)%360f); 
				rdd = Math.abs(rr - rhdir)%(pi*2.0D);
				if(rdd > pi)rdd = rdd-(pi*2.0D);
				rdd = Math.abs(rdd); //Total differential, minus sign.				
				if(ViewFromEntity.rotation_pitch_head < 45 || ViewFromEntity.rotation_pitch_head > 315){ //NOT looking down or up?						
					if(dist > 4 && rdd > pi/2*DangerZone.fieldOfView/45.0f){
						if(!DangerZone.f5_front && !DangerZone.f5_back){
							inext++;
							continue; //Don't render what we can't see!
						}
					}
				}	
				if(pst.model != null){												
					GL11.glPushMatrix();  //SAVE!
					//put the entitiy where it goes.
					GL11.glTranslated(-((((ViewFromEntity.posx)-pst.posx)*blockrenderwidth))+f5x, 
							-((((ViewFromEntity.posy)+ViewFromEntity.eyeheight+bounce)-pst.posy)*blockrenderwidth)+f5y, 
							-(((ViewFromEntity.posz)-pst.posz)*blockrenderwidth)+f5z);
					//Rotate it around						
					GL11.glRotatef(pst.rotation_roll, 0.0f, 0.0f, 1.0f); // Rotate The Entity On X, Y & Z
					GL11.glRotatef(pst.rotation_yaw, 0.0f, 1.0f, 0.0f); // Rotate The Entity On X, Y & Z
					GL11.glRotatef(pst.rotation_pitch, 1.0f, 0.0f, 0.0f); // Rotate The Entity On X, Y & Z

					recalcBrightness(ViewFromEntity.dimension, (int)pst.posy);
					setBrightness(pst.brightness + WorldRendererUtils.getLightMapValue(world, pst.dimension, (int)pst.posx, (int)(pst.posy), (int)pst.posz));
					GL11.glScalef(pst.scale, pst.scale, pst.scale);
					//Now all it has to do is draw itself.
					
					if(doforce){
						forceloadtexture(pst.model.getTexture(pst)); 		//Get a texture for it	
						doforce = false;
					}else{
						loadtexture(pst.model.getTexture(pst)); 		//Get a texture for it	
					}
										
					pst.model.renderParticle(pst);	//Draw!	

					GL11.glPopMatrix();	//RESTORE!!
				}
			}
			inext++;
		}
		DangerZone.particleManager.particle_list_lock.unlock();

		
		//Draw self!!!! LOOK AT MEEEEEEEEEEEE! :)
		if((DangerZone.f5_front || DangerZone.f5_back) && ViewFromEntity == DangerZone.player){
			//Draw self!
			modl = ViewFromEntity.model;
			morphent = null;
			if(ViewFromEntity == DangerZone.player)morphent = DangerZone.player.morph;
			ent = ViewFromEntity;
			if(!ent.isInvisible()){
				GL11.glPushMatrix();  //SAVE!
				//put the entitiy where it goes.
				GL11.glTranslated(-((((ViewFromEntity.posx)-ent.posx)*blockrenderwidth))+f5x, 
						-((((ViewFromEntity.posy)+ViewFromEntity.eyeheight+bounce)-ent.posy)*blockrenderwidth)+f5y, 
						-(((ViewFromEntity.posz)-ent.posz)*blockrenderwidth)+f5z);			//Rotate it around						
				//GL11.glRotatef(ent.rotation_roll, 0.0f, 0.0f, 1.0f); // Rotate The Entity On X, Y & Z
				float fyw = 360f-((ent.rotation_yaw+180)%360); //WTF?
				GL11.glRotatef(fyw, 0.0f, 1.0f, 0.0f); // Rotate The Entity On X, Y & Z
				GL11.glRotatef(ent.rotation_pitch, 1.0f, 0.0f, 0.0f); // Rotate The Entity On X, Y & Z

				recalcBrightness(ViewFromEntity.dimension, (int)(ViewFromEntity.posy+(ViewFromEntity.getHeight()/2)));
				setBrightness(WorldRendererUtils.getLightMapValue(world, ViewFromEntity.dimension, (int)ViewFromEntity.posx, (int)(ViewFromEntity.posy+(ent.getHeight()/2)), (int)ViewFromEntity.posz));

				//Now all it has to do is draw itself.
				GL11.glPushMatrix();  //SAVE!
				if(morphent != null){
					loadtexture(modl.getTexture(morphent)); 		//Get a texture for it	
					modl.doScale(morphent);	
				}else{
					loadtexture(modl.getTexture(ent)); 		//Get a texture for it	
					modl.doScale(ent);	
				}

				ouch = 1.0f;//Let it scale itself
				washurt = ent.isHurt();
				if(washurt){
					ouch = 1.1f;
					GL11.glColor3f(1.0f, 0.15f, 0.15f);
				}
				if(ent.getDeathFactor() > 0){
					ouch = ent.getDeathFactor();
					//System.out.printf("Entity Death %f\n", ouch);
				}
				velocity = (float) Math.sqrt((ent.motionx*ent.motionx)+(ent.motiony*ent.motiony)+(ent.motionz*ent.motionz));
				//ent.model.render(ent, (float)ent.lifetimeticker, velocity, 0, 0, 0, ouch);
				//System.out.printf("ph,  p, yh, y == %f, %f, %f, %f\n", ent.rotation_pitch_head, ent.rotation_pitch, ent.rotation_yaw_head, ent.rotation_yaw );
				cdir = (float) Math.toRadians(ent.rotation_pitch);
				tdir = (float) Math.toRadians(ent.rotation_pitch_head);
				pdiff = tdir - cdir;
				while(pdiff>Math.PI)pdiff -= Math.PI*2;
				while(pdiff<-Math.PI)pdiff += Math.PI*2;
				pdiff = (float) Math.toDegrees(pdiff);

				cdir = (float) Math.toRadians(ent.rotation_yaw);
				tdir = (float) Math.toRadians(ent.rotation_yaw_head);
				ydiff = tdir - cdir;
				while(ydiff>Math.PI)ydiff -= Math.PI*2;
				while(ydiff<-Math.PI)ydiff += Math.PI*2;
				ydiff = (float) Math.toDegrees(ydiff);

				cdir = (float) Math.toRadians(ent.rotation_roll);
				tdir = (float) Math.toRadians(ent.rotation_roll_head);
				rdiff = tdir - cdir;
				while(rdiff>Math.PI)rdiff -= Math.PI*2;
				while(rdiff<-Math.PI)rdiff += Math.PI*2;
				rdiff = (float) Math.toDegrees(rdiff);						

				if(morphent != null){
					modl.render(morphent, (float)ent.lifetimeticker, velocity, pdiff, -ydiff, rdiff, ouch);	//Draw!	
				}else{
					modl.render(ent, (float)ent.lifetimeticker, velocity, pdiff, -ydiff, rdiff, ouch);	//Draw!	
				}

				GL11.glPopMatrix();	//RESTORE!!


				GL11.glRotatef(-ent.display_rotation_roll, 0.0f, 0.0f, 1.0f); // Rotate The Entity On X, Y & Z
				GL11.glRotatef(-ent.display_rotation_pitch, 1.0f, 0.0f, 0.0f); // Rotate The Entity On X, Y & Z	
				if(ent.getPetName() != null){
					if(ent.isBaby())GL11.glScalef(0.25f, 0.25f, 0.25f);
					GL11.glScalef(0.15f, 0.15f, 0.15f);
					WorldRendererUtils.textAt(font, - (5 * ent.getPetName().length()), (ent.getNameHeight() * 125)+30, ent.getPetName());
					GL11.glScalef(1.0f/0.15f, 1.0f/0.15f, 1.0f/0.15f);
					if(ent.isBaby())GL11.glScalef(4, 4, 4);
				}
				if(ent.getOnFire() > 0){
					WorldRendererUtils.drawEntityOnFire(ent);
				}	   
				if(washurt)setBrightness();

				GL11.glPopMatrix();	//RESTORE!!
			}
		}	
		GL11.glPopMatrix(); //POP -----------------------------------------------------------------------------------------------------POP
		

		doClickyStuff(world);
		
		drawPlayerHeldItem(world);
				
		
		GL11.glPushMatrix();  //PUSH ---------------------------------------------------------------------------------------------PUSH
		GL11.glRotatef(ViewFromEntity.rotation_pitch_head+f5pitch, 1.0f, 0.0f, 0.0f); // Rotate The View On X, Y & Z
		GL11.glRotatef(Viewer_yaw+f5yaw, 0.0f, 1.0f, 0.0f); // Rotate The View On X, Y & Z
		//Ow. The next line hurts...
		GL11.glTranslated(-((((ViewFromEntity.posx)*blockrenderwidth))%(16*blockrenderwidth))+blockrenderwidth/2 + f5x, 
				-(((ViewFromEntity.posy+ViewFromEntity.eyeheight+bounce)-0.5f)*blockrenderwidth) + f5y, 
				-((((ViewFromEntity.posz)*blockrenderwidth))%(16*blockrenderwidth))+blockrenderwidth/2 + f5z);
				
		//Now go back and draw the VBOs with translucent blocks!!!
		//Water!!!
		if(!translucentVBOs.isEmpty()){
			Iterator<VBOBuffer> iitr = translucentVBOs.iterator();
			VBOBuffer sstr = null;
			
			while(iitr.hasNext()){
				sstr = iitr.next();
				GL11.glPushMatrix();
				//draw the VBOs for this chunk!
				GL11.glTranslatef(sstr.xoff, 0, sstr.zoff); //position to draw from
				loadStitchedtexture(sstr.textureindex);
				sstr.lastusedframe = framecounter;
				sstr.draw();
				GL11.glPopMatrix();
			}
						
			translucentVBOs.clear();
		}
		
		GL11.glPopMatrix();   //POP -----------------------------------------------------------------------------------------------------POP
		
		
		recalcBrightness(DangerZone.player.dimension, 60);
		setBrightness();
		
		//Draw a little X in the middle of the screen!
		if(DangerZone.overlays_on && !DangerZone.f5_front && !DangerZone.f5_back){
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glLineWidth(1.0f);		
			GL11.glBegin(GL11.GL_LINE_STRIP);
			GL11.glColor3f(0.65f, 0.65f, 0.65f);
			GL11.glVertex3f(-.04f, 0f, -2f);
			GL11.glVertex3f(.04f, 0f, -2f);
			GL11.glEnd();
			GL11.glBegin(GL11.GL_LINE_STRIP);
			GL11.glColor3f(0.65f, 0.65f, 0.65f);
			GL11.glVertex3f(0f, .04f, -2f);
			GL11.glVertex3f(0f, -.04f, -2f);
			GL11.glEnd();
			GL11.glDisable(GL11.GL_BLEND);		
			GL11.glEnable(GL11.GL_TEXTURE_2D);	
		}
		
		
		if(DangerZone.overlays_on)doMenuThings(world);
		
		/*
		 * DONE! Push it all out to the screen!		
		 */		
		GL11.glFlush();
		
	}
	
				
	private void doMenuThings(World world){
		
		int i;
		
		if(ViewFromEntity != DangerZone.player)return;
		
		/*
		 * Get into menu mode...
		 */
		//What a freaking pain in the ass to get working!
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		//puts 0,0 (x,y) at lower left of screen!
		GL11.glOrtho(0,DangerZone.screen_width,0,DangerZone.screen_height,-320,320);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		

		/*
		 * Draw health/hotbar/etc
		 */
		WorldRendererUtils.drawHotbar(world);
		WorldRendererUtils.drawHealth(world);
		WorldRendererUtils.drawHunger(world);
		WorldRendererUtils.drawMagic(world);
		WorldRendererUtils.drawAir(world);
		
		//Let's do some text overlays!
		if(DangerZone.messagetimer > 0 && DangerZone.messagestring != null){
			DangerZone.messagetimer--;
			WorldRendererUtils.drawRectangleWithTexture(DangerZone.textinputtexture, 30, 120, 600, 30);
			WorldRendererUtils.textAt(font, 50, 150, DangerZone.messagestring);
		}
		if(DangerZone.hotmessagetimer > 0 && DangerZone.hotmessagestring != null){
			DangerZone.hotmessagetimer--;
			WorldRendererUtils.drawRectangleWithTexture(DangerZone.textinputtexture, DangerZone.screen_width/2 - (5 * DangerZone.hotmessagestring.length()) - 50, 85, 
					(13 * DangerZone.hotmessagestring.length())+20, 30);
			WorldRendererUtils.textAt(font, DangerZone.screen_width/2 - (5 * DangerZone.hotmessagestring.length()) - 30, 115, DangerZone.hotmessagestring);
			InventoryContainer ic = DangerZone.player.getHotbar(DangerZone.player.gethotbarindex());
			if(DangerZone.current_gui == null && ic != null && ic.iid != 0 && ic.count == 1 && ic.getMaxStack() == 1 && ic.attributes != null){
				int ayoff = 250;
				for(i=1;i<10;i++){
					int val = ic.getAttribute(i);
					if(val > 0){
						String outtext = "";
						if(i == ItemAttribute.ACCURACY)outtext = 	"Accuracy:   ";
						if(i == ItemAttribute.DAMAGE)outtext = 		"Damage:     ";
						if(i == ItemAttribute.DURABILITY)outtext = 	"Durability: ";
						if(i == ItemAttribute.REACH)outtext = 		"Reach:      ";
						if(i == ItemAttribute.SPAM)outtext = 		"Spam:       ";
						outtext += String.format("%d", val);
						WorldRendererUtils.textAt(font, 20, ayoff, outtext);
						ayoff -= 25;
					}					
				}
			}
		}else{
			if(DangerZone.current_gui == null){
				String s = String.format("%d", DangerZone.player.getExperience());
				WorldRendererUtils.textAt(font, DangerZone.screen_width/2 - (5 * s.length()) - 30, 140, s);
			}
		}
		
		if(DangerZone.f3_on && DangerZone.current_gui == null){
			int modepos = 10;
			String s = String.format("Version: %s", DangerZone.versionstring);
			WorldRendererUtils.textAt(font, 10, DangerZone.screen_height-10, s);
			s = String.format("FPS: %d at Render Distance: %d", fps, DangerZone.renderdistance);
			WorldRendererUtils.textAt(font, 10, DangerZone.screen_height-50, s);
			
			if(DangerZone.server != null && DangerZone.server_chunk_cache != null){
				Dimension dd = Dimensions.DimensionArray[DangerZone.player.dimension];
				if(dd != null){
					s = String.format("Dimension: %s", dd.uniquename);				
					WorldRendererUtils.textAt(font, DangerZone.screen_width/2, DangerZone.screen_height-10, s);
					BiomeManager bm = dd.getBiomeManager();
					if(bm != null){
						Biome bb = bm.getBiomeForChunk(null, DangerZone.player.dimension, (int)DangerZone.player.posx, (int)DangerZone.player.posy, (int)DangerZone.player.posz);
						if(bb != null){
							s = String.format("Biome: %s", bb.uniquename);
							WorldRendererUtils.textAt(font, DangerZone.screen_width/2, DangerZone.screen_height-50, s);
						}
					}
				}
				modepos = 90;
			}
			s = "Survival";
			if(DangerZone.player.getGameMode() == GameModes.CREATIVE)s = "Creative";
			if(DangerZone.player.getGameMode() == GameModes.GHOST)s = "Ghost";
			if(DangerZone.player.getGameMode() == GameModes.LIMBO)s = "Limbo";		
			s = String.format("GameMode: %s", s);				
			WorldRendererUtils.textAt(font, DangerZone.screen_width/2, DangerZone.screen_height-modepos, s);
			
			s = "Normal";
			if(DangerZone.player.getGameDifficulty() == -1)s = "Easy";
			if(DangerZone.player.getGameDifficulty() == -2)s = "Girly";
			if(DangerZone.player.getGameDifficulty() == 1)s = "Hard";	
			if(DangerZone.player.getGameDifficulty() == 2)s = "Brutal";	
			s = String.format("Difficulty: %s", s);				
			WorldRendererUtils.textAt(font, DangerZone.screen_width/2, DangerZone.screen_height-(modepos+40), s);
			
			Chunk c = world.chunkcache.getChunk(world, DangerZone.player.dimension, (int)DangerZone.player.posx, (int)DangerZone.player.posy, (int)DangerZone.player.posz);
			if(c != null){	
				List<String>owners = c.ownernames;
				if(owners != null){
					int olen = owners.size();
					i = 0;
					s = "Chunk Owners: ";
					while(olen > 0){
						s += owners.get(i);
						s += ", ";
						olen--;
						i++;
					}
					WorldRendererUtils.textAt(font, DangerZone.screen_width/2, DangerZone.screen_height-(modepos+80), s);
				}
				c = null;
			}
			
			

			s = String.format("XPOS: %d", (int)DangerZone.player.posx);
			WorldRendererUtils.textAt(font, 10, DangerZone.screen_height-90, s);
			s = String.format("YPOS: %d", (int)DangerZone.player.posy);
			WorldRendererUtils.textAt(font, 10, DangerZone.screen_height-130, s);
			s = String.format("ZPOS: %d", (int)DangerZone.player.posz);
			WorldRendererUtils.textAt(font, 10, DangerZone.screen_height-170, s);
			s = String.format("Packets: %d", (int)pps);
			WorldRendererUtils.textAt(font, 10, DangerZone.screen_height-210, s);
			s = String.format("VBO memory: %d MB", VBOmemorysize/(1024*1024));
			WorldRendererUtils.textAt(font, 10, DangerZone.screen_height-250, s);
			s = String.format("Light LVL: %f", WorldRendererUtils.getTotalLightAt(DangerZone.player.world, DangerZone.player.dimension, (int)DangerZone.player.posx, (int)DangerZone.player.posy, (int)DangerZone.player.posz));
			WorldRendererUtils.textAt(font, 10, DangerZone.screen_height-290, s);
			if(DangerZone.server != null){
				s = String.format("Entity Freelist: %d", DangerZone.server.entityManager.entity_free_list.size());
				WorldRendererUtils.textAt(font, 10, DangerZone.screen_height-330, s);
				s = String.format("Entities: %d", DangerZone.server.entityManager.entity_list.size());
				WorldRendererUtils.textAt(font, 10, DangerZone.screen_height-370, s);
				if(DangerZone.particleManager != null){
					s = String.format("Particles: %d", DangerZone.particleManager.particle_list.size());
					WorldRendererUtils.textAt(font, 10, DangerZone.screen_height-410, s);
				}
			}else{
				s = String.format("Server Latency(ms): %d", DangerZone.server_connection.lastaverageloop);				
				WorldRendererUtils.textAt(font, 10, DangerZone.screen_height-330, s);
				//int cx = (int)DangerZone.player.posx;
				//int cz = (int)DangerZone.player.posz;
				//cx >>= 4;
				//cz >>= 4;
				//s = String.format("ChunkFile: %s/Dimension-%d/%2x/%d_%d.dat", DangerZone.worldname,DangerZone.player.dimension,(cx+cz)&0xff,cx,cz);			
				//WorldRendererUtils.textAt(font, 10, DangerZone.screen_height-370, s);
				
			}
		}

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		

	}
	


    
	/*
	 * Find the block we are pointing at, and the side too...
	 */
	private void find_focus(World w){
		double dir, dx, dz, dy;
		double sx, sz, sy;
		double delta = 0.0d;
		double xzscale;
		double delta2;
		double dist, lastdist;
		int x,y,z;
		double fx,fy,fz;
		int lx, ly, lz;
		int bid;
		int eyebid;
		int lfx, lfy, lfz;
		Entity tempe = null;
		List<Entity> nearby_list = null;
		List<Entity> check_list = new ArrayList<Entity>();
		ListIterator<Entity> li;
		int riddenid = 0;
		Entity ridden = DangerZone.player.getRiddenEntity();
		if(ridden != null )riddenid = ridden.entityID;
		
		
		fx =  DangerZone.player.posx;
		fy = DangerZone.player.posy+DangerZone.player.eyeheight;
		fz =  DangerZone.player.posz;
		x = (int) fx;
		y = (int) fy;
		z = (int) fz;
		eyebid = w.getblock(DangerZone.player.dimension, x, y, z); //where our eyes are!
		
		dir = Math.toRadians((DangerZone.player.rotation_yaw_head-90)%360f); 
		dx = Math.cos(dir);
		dz = Math.sin(dir);
		dy = Math.sin(Math.toRadians((DangerZone.player.rotation_pitch_head)%360f));
		xzscale = Math.abs(Math.cos(Math.toRadians((DangerZone.player.rotation_pitch_head)%360f)));
		dx *= xzscale;
		dz *= xzscale;
		dy = -dy;
		
		float reach = 2 + (DangerZone.player.getHeight() * 2.5f);
		if(reach < 2)reach = 2;
		float itemreach = reach - 1;
		float blockreach = reach;
		float itemwidth = 0.125f;
		InventoryContainer ic = DangerZone.player.getHotbar(DangerZone.player.gethotbarindex());
		if(ic != null){
			itemreach += 0.5f * (float)ic.getAttribute(ItemAttribute.REACH);
			blockreach += 0.25f * (float)ic.getAttribute(ItemAttribute.REACH);
			itemwidth += 0.25f * (float)ic.getAttribute(ItemAttribute.ACCURACY);
		}
		if(itemreach > reach)reach = itemreach;
		if(blockreach > reach)reach = blockreach;
		
		//pre-filter to take out things we cannot possibly or shouldn't hit!
		nearby_list = DangerZone.entityManager.findEntitiesInRange(20.0f+reach, DangerZone.player.dimension, DangerZone.player.posx, DangerZone.player.posy, DangerZone.player.posz);
		if(nearby_list != null){
			if(!nearby_list.isEmpty()){
				li = nearby_list.listIterator();
				while(li.hasNext()){
					tempe = (Entity)li.next();
					dist = Math.sqrt((fx-tempe.posx)*(fx-tempe.posx)+(fy-tempe.posy)*(fy-tempe.posy)+(fz-tempe.posz)*(fz-tempe.posz));
					dist -= 0.125f; //width of sword or whatever
					dist -= (tempe.getWidth()/2); //general hitbox of entity
					if(tempe != DangerZone.player && !tempe.canthitme && dist < 14){					
						//don't hit mount unless looking almost straight down!
						if(tempe.entityID != riddenid){
							//System.out.printf("added %d\n", tempe.entityID);
							check_list.add(tempe);
						}
					}
				}
			}
		}
		
		if(ridden != null && dy < -0.75f)check_list.add(ridden); //add whatever we are riding on! (if looking down!)

		//focus_x = focus_y = focus_z = focus_side = 0;
		lx = ly = lz = 0;
		lfx = focus_x; lfy = focus_y; lfz = focus_z;


		//increment along the axis looking for a block
		while(delta < reach){
			fx =  (DangerZone.player.posx + dx*delta);
			fy =  ((DangerZone.player.posy+DangerZone.player.eyeheight) + dy*delta);
			fz =  (DangerZone.player.posz + dz*delta);
			x = (int) fx;
			y = (int) fy;
			z = (int) fz;
			//Hit an entity or a block... whichever...
			if(delta < itemreach && check_list != null){
				if(!check_list.isEmpty()){
					li = check_list.listIterator();
					while(li.hasNext()){
						tempe = (Entity)li.next();
						if(fy > tempe.posy-itemwidth && fy < tempe.posy+tempe.getHeight()+itemwidth){
							dist = Math.sqrt((fx-tempe.posx)*(fx-tempe.posx)+(fz-tempe.posz)*(fz-tempe.posz));
							dist -= itemwidth; //width of sword or whatever
							dist -= (tempe.getWidth()/2); //general hitbox of entity
							//System.out.printf("entity dist %d, d = %f\n", tempe.entityID, dist);
							if(dist < 0){
								//System.out.printf("entity hit %d, dy = %f\n", tempe.entityID, dy);
								focus_x = focus_y = focus_z = focus_bid = focus_meta = 0;
								focus_dist = (float) dist;
								focus_entity = tempe;
								return;
							}
						}
					}
				}
			}
			
			//wait until block actually changes
			if(delta < blockreach && x != lx || y != ly || z != lz){
				lx = x; ly = y; lz = z;
				bid = w.getblock(DangerZone.player.dimension, x, y, z);
				if(eyebid != 0 && Blocks.isLiquid(eyebid) && Blocks.isLiquid(bid))bid = 0; //we are in water. ignore water.
				if(bid != 0){ //Hit a block!!!	
					
					//but wait! If it is NOT a solid block, then we need to check for a hit-able entity inside!
					//hit the entity if possible, else continue with hitting the block...
					if(!Blocks.isSolid(bid) && check_list != null){
						int tbid;
						int tx, ty, tz;
						int tlx = lx;
						int tlz = lz;
						int tly = ly;
						double tdelta = delta;
						while(tdelta < itemreach){
							fx =  (DangerZone.player.posx + dx*tdelta);
							fy =  ((DangerZone.player.posy+DangerZone.player.eyeheight) + dy*tdelta);
							fz =  (DangerZone.player.posz + dz*tdelta);
							tx = (int) fx;
							ty = (int) fy;
							tz = (int) fz;
							if(tx != tlx || ty != tly || tz != tlz){
								tbid = w.getblock(DangerZone.player.dimension, tx, ty, tz);
								if(Blocks.isSolid(tbid)){
									break; //hit something solid. break out and process original non-solid block.
								}
								tlx = tx; tly = ty; tlz = tz;
							}
							if(!check_list.isEmpty()){
								li = check_list.listIterator();
								while(li.hasNext()){
									tempe = (Entity)li.next();
									if(fy > tempe.posy-itemwidth && fy < tempe.posy+tempe.getHeight()+itemwidth){
										dist = Math.sqrt((fx-tempe.posx)*(fx-tempe.posx)+(fz-tempe.posz)*(fz-tempe.posz));
										dist -= itemwidth; //width of sword or whatever
										dist -= (tempe.getWidth()/2); //general hitbox of entity
										//System.out.printf("entity dist %d, d = %f\n", tempe.entityID, dist);
										if(dist < 0){
											//System.out.printf("entity hit %d, dy = %f\n", tempe.entityID, dy);
											focus_x = focus_y = focus_z = focus_bid = focus_meta = 0;
											focus_dist = (float) dist;
											focus_entity = tempe;
											return;
										}
									}
								}
							}
							tdelta += 0.1d;
						}
					}
					
					//hit something! Now back it up one step and loop for 0.01;
					delta2 = delta;
					delta = delta - 0.1f;
					x = (int) (DangerZone.player.posx + dx*delta);
					y = (int) ((DangerZone.player.posy+DangerZone.player.eyeheight) + dy*delta );
					z = (int) (DangerZone.player.posz + dz*delta);
					lx = x; ly = y; lz = z;

					//loop to get really really close to intersection
					while(delta < delta2+0.01f){
						fx =  (DangerZone.player.posx + dx*delta);
						fy =  ((DangerZone.player.posy+DangerZone.player.eyeheight) + dy*delta);
						fz =  (DangerZone.player.posz + dz*delta);
						x = (int) fx;
						y = (int) fy;
						z = (int) fz;
						if(x != lx || y != ly || z != lz){
							lx = x; ly = y; lz = z;
							bid = w.getblock(DangerZone.player.dimension, x, y, z);
							if(eyebid != 0 && Blocks.isLiquid(eyebid) && Blocks.isLiquid(bid))bid = 0; //we are in water. ignore water.
							if(bid != 0){ //Hit a block!!! (again)
								
								//Now we are within 1/100th of the side. 
								//See which side center is closest to this ray-plane intersection point.

								dx =  (DangerZone.player.posx + dx*delta);
								dy =  ((DangerZone.player.posy+DangerZone.player.eyeheight) + dy*delta );
								dz =  (DangerZone.player.posz + dz*delta);
								dist = 2.0d;
								lastdist = 2.0d;
								focus_side = 0;

								//top
								sx = ((double)x + 0.5f);
								sy = ((double)y + 1.0f);
								sz = ((double)z + 0.5f);
								dist = Math.sqrt((sx-dx)*(sx-dx)+(sy-dy)*(sy-dy)+(sz-dz)*(sz-dz));
								if(dist < lastdist){
									lastdist = dist;
									focus_side = 0;
								}						

								//back
								sx = ((double)x + 0.5f);
								sy = ((double)y + 0.5f);
								sz = ((double)z + 0.0f);
								dist = Math.sqrt((sx-dx)*(sx-dx)+(sy-dy)*(sy-dy)+(sz-dz)*(sz-dz));
								if(dist < lastdist){
									lastdist = dist;
									focus_side = 2;
								}

								//front
								sx = ((double)x + 0.5f);
								sy = ((double)y + 0.5f);
								sz = ((double)z + 1.0f);
								dist = Math.sqrt((sx-dx)*(sx-dx)+(sy-dy)*(sy-dy)+(sz-dz)*(sz-dz));
								if(dist < lastdist){
									lastdist = dist;
									focus_side = 1;
								}

								//left
								sx = ((double)x + 0.0f);
								sy = ((double)y + 0.5f);
								sz = ((double)z + 0.5f);
								dist = Math.sqrt((sx-dx)*(sx-dx)+(sy-dy)*(sy-dy)+(sz-dz)*(sz-dz));
								if(dist < lastdist){
									lastdist = dist;
									focus_side = 3;
								}

								//right
								sx = ((double)x + 1.0f);
								sy = ((double)y + 0.5f);
								sz = ((double)z + 0.5f);
								dist = Math.sqrt((sx-dx)*(sx-dx)+(sy-dy)*(sy-dy)+(sz-dz)*(sz-dz));
								if(dist < lastdist){
									lastdist = dist;
									focus_side = 4;
								}

								//bottom
								sx = ((double)x + 0.5f);
								sy = ((double)y + 0.0f);
								sz = ((double)z + 0.5f);
								dist = Math.sqrt((sx-dx)*(sx-dx)+(sy-dy)*(sy-dy)+(sz-dz)*(sz-dz));
								if(dist < lastdist){
									lastdist = dist;
									focus_side = 5;
								}

								//Done! Fast and efficient, with no complex vector math involved! :)
								focus_x = lx;
								focus_y = ly;
								focus_z = lz;
								focus_bid = bid;
								focus_meta = w.getblockmeta(DangerZone.player.dimension, x, y, z);
								focus_dist = (float) delta;
								focus_entity = null;
								if(lfx != focus_x || lfy != focus_y || lfz != focus_z){
									focus_damage = 0;
									focus_maxdamage = Blocks.getMaxDamage(bid);
									if(focus_maxdamage < 1)focus_maxdamage = 1;
								}
								//System.out.printf("Side = %d\n", focus_side);
								return;
							}
						}
						delta += 0.01f;
					}
				}
			}
			delta += 0.1d;
		}
		focus_x = focus_y = focus_z = focus_bid = focus_meta = 0;
		focus_dist = 0;
		focus_entity = null;
	}
	
/*
This routine just does the drawing. The actual VBO vertex data is generated in the VBODataBuilderThread.
In theory, this greatly reduces the possibility of "lag".
In practice, well, it's java. We'll see...
ALL graphics calls are done here, in this thread.
*/
	private void renderChunk(World world, int xrel, int zrel, double px, double py, double pz){
		int xpos, zpos;	
		VBOBuffer v = null;
		int itemp;

		xpos = (xrel*16)+(int)px;
		zpos = (zrel*16)+(int)pz;

		//This call is special. If the chunk is not in cache, it will just be ignored, but a request is sent to fetch it.
		Chunk c = world.chunkcache.getDecoratedChunkForRenderer(DangerZone.player.dimension, xpos, 0, zpos);
		if(c == null){
			return;
		}
		
		
		VBOlistlock.lock();
		//first make sure VBOs are all valid! Else we have to redraw anyway...
		for(itemp=0;itemp<=20;itemp++){
			if(c.VBOids[itemp] <= 0)break; //end of list. done.
			v = VBOmap.get(c.VBOids[itemp]);
			if(v == null){
				//System.out.printf("VBOFAIL: invalid vbo\n");
				//oops. at least one is no longer valid.
				for(int ivi=0;ivi<20;ivi++){
					//invalidate and free all of them!
					if(c.VBOids[ivi] <= 0)continue;
					v = VBOmap.get(c.VBOids[ivi]);
					if(v != null){					
						VBOmap.remove(v.VBOid);
						vbocount--;
						v.free();
					}					
					c.VBOids[ivi] = 0;
				}
				VBOlistlock.unlock();
				return;
			}
		}
		VBOlistlock.unlock();

		GL11.glPushMatrix();
		//draw the VBOs for this chunk!
		GL11.glTranslatef((xrel*16*blockrenderwidth), 0, (zrel*16*blockrenderwidth)); //position to draw from
		for(itemp=0;itemp<=20;itemp++){
			if(c.VBOids[itemp] <= 0)break;
			VBOlistlock.lock();
			v = VBOmap.get(c.VBOids[itemp]);
			VBOlistlock.unlock();
			if(v != null){
				if(!v.isTranslucent){
					loadStitchedtexture(v.textureindex);
					v.lastusedframe = framecounter;
					v.draw(); //this will check for and load new data, or simply redraw old data.
				}else{
					v.xoff = (xrel*16*blockrenderwidth);
					v.zoff = (zrel*16*blockrenderwidth);
					translucentVBOs.add(v);
				}
			}
		}
		GL11.glPopMatrix();

	}
	 

	/*
	 * Standard block drawing.
	 */
	public void drawTexturedCube(int sides, boolean isSolid, int bid, int meta, boolean focus) {
		if(bid <= 0 || bid >= Blocks.blocksMAX)return;
		if(Blocks.BlockArray[bid] == null)return;
		
		//Allow non-solid cubes!
		if(!isSolid){
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}	
		
		if((meta&0xfc00)!=0){ //There is rotation!
			GL11.glPushMatrix();
			//High two bits
			if((meta&0xc000) == 0x4000){//twist
				GL11.glRotatef(90f, 1, 0, 0);
			}
			if((meta&0xc000) == 0x8000){//twist
				GL11.glRotatef(180f, 1, 0, 0);
			}
			if((meta&0xc000) == 0xc000){//twist
				GL11.glRotatef(270f, 1, 0, 0);
			}
			//next bits
			if((meta&0x3000) == 0x1000){//twist
				GL11.glRotatef(90f, 0, 1, 0);
			}
			if((meta&0x3000) == 0x2000){//twist
				GL11.glRotatef(180f, 0, 1, 0);
			}
			if((meta&0x3000) == 0x3000){//twist
				GL11.glRotatef(270f, 0, 1, 0);
			}
			//next bits
			if((meta&0x0c00) == 0x0400){//twist
				GL11.glRotatef(90f, 0, 0, 1);
			}
			if((meta&0x0c00) == 0x0800){//twist
				GL11.glRotatef(180f, 0, 0, 1);
			}
			if((meta&0x0c00) == 0x0c00){//twist
				GL11.glRotatef(270f, 0, 0, 1);
			}
			
			sides = 0xff; //Draw all sides, because we don't know which is where any more... 
		}
		


		if((sides & 0x20) != 0){
			if(loadtextureforblockside(0, bid, isSolid)){ //TOP
				GL11.glCallList(topid);
			}
		}
		
		if((sides & 0x10) != 0){
			if(loadtextureforblockside(5, bid, isSolid)){ //BOTTOM
				GL11.glCallList(bottomid);	
			}
		}

		if((sides & 0x08) != 0){
			if(loadtextureforblockside(1, bid, isSolid)){ //FRONT
				GL11.glCallList(frontid);
			}						
		}

		if((sides & 0x04) != 0){
			if(loadtextureforblockside(2, bid, isSolid)){ //BACK
				GL11.glCallList(backid);
			}
		}

		if((sides & 0x01) != 0){
			if(loadtextureforblockside(3, bid, isSolid)){ //LEFT
				GL11.glCallList(leftid);
			}
		}

		if((sides & 0x02) != 0){
			if(loadtextureforblockside(4, bid, isSolid)){ //RIGHT
				GL11.glCallList(rightid);
			}
		}
		
		if((meta&0xfc00)!=0){ //There was rotation!
			GL11.glPopMatrix();
		}
		
		if(!isSolid){
			GL11.glDisable(GL11.GL_BLEND);
		}
		
		
		if(focus){			
			if(focus_side == 0){		
				GL11.glLineWidth(linewidth);		
				GL11.glBegin(GL11.GL_LINE_STRIP);
				GL11.glColor3f(0.0f, 0.0f, 0.0f);
				GL11.glVertex3f(blockrenderwidth/2, blockrenderwidth/2, -blockrenderwidth/2); // Top Right Of The Quad (Top)
				GL11.glVertex3f(-blockrenderwidth/2, blockrenderwidth/2, -blockrenderwidth/2); // Top Left Of The Quad (Top)
				GL11.glVertex3f(-blockrenderwidth/2, blockrenderwidth/2, blockrenderwidth/2); // Bottom Left Of The Quad (Top)
				GL11.glVertex3f(blockrenderwidth/2, blockrenderwidth/2, blockrenderwidth/2); // Bottom Right Of The Quad (Top)
				GL11.glVertex3f(blockrenderwidth/2, blockrenderwidth/2, -blockrenderwidth/2); // Top Right Of The Quad (Top)
				GL11.glEnd();
			}
			if(focus_side == 5){
				GL11.glLineWidth(linewidth);		
				GL11.glBegin(GL11.GL_LINE_STRIP);
				GL11.glColor3f(0.0f, 0.0f, 0.0f);
				GL11.glVertex3f(blockrenderwidth/2, -blockrenderwidth/2, blockrenderwidth/2); // Top Right Of The Quad (Bottom)
				GL11.glVertex3f(-blockrenderwidth/2, -blockrenderwidth/2, blockrenderwidth/2); // Top Left Of The Quad (Bottom)
				GL11.glVertex3f(-blockrenderwidth/2, -blockrenderwidth/2, -blockrenderwidth/2); // Bottom Left Of The Quad (Bottom)	
				GL11.glVertex3f(blockrenderwidth/2, -blockrenderwidth/2, -blockrenderwidth/2); // Bottom Right Of The Quad (Bottom)
				GL11.glVertex3f(blockrenderwidth/2, -blockrenderwidth/2, blockrenderwidth/2); // Top Right Of The Quad (Bottom)
				GL11.glEnd();
			}
			if(focus_side == 1){
				GL11.glLineWidth(linewidth);		
				GL11.glBegin(GL11.GL_LINE_STRIP);
				GL11.glColor3f(0.0f, 0.0f, 0.0f);								
				GL11.glVertex3f(blockrenderwidth/2, blockrenderwidth/2, blockrenderwidth/2); // Top Right Of The Quad (Front)			
				GL11.glVertex3f(-blockrenderwidth/2, blockrenderwidth/2, blockrenderwidth/2); // Top Left Of The Quad (Front)				
				GL11.glVertex3f(-blockrenderwidth/2, -blockrenderwidth/2, blockrenderwidth/2); // Bottom Left Of The Quad (Front)				
				GL11.glVertex3f(blockrenderwidth/2, -blockrenderwidth/2, blockrenderwidth/2); // Bottom Right Of The Quad (Front)
				GL11.glVertex3f(blockrenderwidth/2, blockrenderwidth/2, blockrenderwidth/2); // Top Right Of The Quad (Front)	
				GL11.glEnd();
			}
			if(focus_side == 2){
				GL11.glLineWidth(linewidth);		
				GL11.glBegin(GL11.GL_LINE_STRIP);
				GL11.glColor3f(0.0f, 0.0f, 0.0f);
				GL11.glVertex3f(-blockrenderwidth/2, blockrenderwidth/2, -blockrenderwidth/2); // Top Left Of The Quad (Back)
				GL11.glVertex3f(blockrenderwidth/2, blockrenderwidth/2, -blockrenderwidth/2); // Top Right Of The Quad (Back)
				GL11.glVertex3f(blockrenderwidth/2, -blockrenderwidth/2, -blockrenderwidth/2); // Bottom Right Of The Quad (Back)
				GL11.glVertex3f(-blockrenderwidth/2, -blockrenderwidth/2, -blockrenderwidth/2); // Bottom Left Of The Quad (Back)
				GL11.glVertex3f(-blockrenderwidth/2, blockrenderwidth/2, -blockrenderwidth/2); // Top Left Of The Quad (Back)
				GL11.glEnd();
			}
			if(focus_side == 3){
				GL11.glLineWidth(linewidth);		
				GL11.glBegin(GL11.GL_LINE_STRIP);
				GL11.glColor3f(0.0f, 0.0f, 0.0f);
				GL11.glVertex3f(-blockrenderwidth/2, blockrenderwidth/2, blockrenderwidth/2); // Top Right Of The Quad (Left)
				GL11.glVertex3f(-blockrenderwidth/2, blockrenderwidth/2, -blockrenderwidth/2); // Top Left Of The Quad (Left)
				GL11.glVertex3f(-blockrenderwidth/2, -blockrenderwidth/2, -blockrenderwidth/2); // Bottom Left Of The Quad (Left)
				GL11.glVertex3f(-blockrenderwidth/2, -blockrenderwidth/2, blockrenderwidth/2); // Bottom Right Of The Quad (Left)
				GL11.glVertex3f(-blockrenderwidth/2, blockrenderwidth/2, blockrenderwidth/2); // Top Right Of The Quad (Left)
				GL11.glEnd();
			}
			if(focus_side == 4){
				GL11.glLineWidth(linewidth);		
				GL11.glBegin(GL11.GL_LINE_STRIP);
				GL11.glColor3f(0.0f, 0.0f, 0.0f);
				GL11.glVertex3f(blockrenderwidth/2, blockrenderwidth/2, -blockrenderwidth/2); // Top Right Of The Quad (Right)
				GL11.glVertex3f(blockrenderwidth/2, blockrenderwidth/2, blockrenderwidth/2); // Top Left Of The Quad (Right)
				GL11.glVertex3f(blockrenderwidth/2, -blockrenderwidth/2, blockrenderwidth/2); // Bottom Left Of The Quad (Right)
				GL11.glVertex3f(blockrenderwidth/2, -blockrenderwidth/2, -blockrenderwidth/2); // Bottom Right Of The Quad (Right)
				GL11.glVertex3f(blockrenderwidth/2, blockrenderwidth/2, -blockrenderwidth/2); // Top Right Of The Quad (Right)
				GL11.glEnd();
			}
		}
		
				

	}
	
	public void drawTexturedSquare(int side, boolean isSolid, int bid) {
		if(bid <= 0 || bid >= Blocks.blocksMAX)return;
		if(Blocks.BlockArray[bid] == null)return;
		
		//Allow non-solid cubes!
		if(!isSolid){
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}		

		if(loadtextureforblockside(side, bid, isSolid)){ //TOP
			GL11.glCallList(frontid);
		}
		
		if(!isSolid){
			GL11.glDisable(GL11.GL_BLEND);
		}			

	}


	public void loadStitchedtexture(int which){	
		if(which < 0 || which > 19)return; //error! Something has a bad texture
		if(stitches[which] != null){
			if(last_texture != stitches[which].textureID){
				stitches[which].bind();	
				last_texture = stitches[which].textureID;
			}
		}
	}

	public boolean loadtextureforblockside(int side, int bid, boolean solid){
		
		Texture lt = Blocks.BlockArray[bid].getTexture(side);
		if(lt == null)return false;
		if(last_texture != lt.getTextureID()){
			TextureImpl.unbind(); //force reset
			lt.bind();
			last_texture = lt.getTextureID();
			//if(!solid){
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			//}else{
			//	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_NEAREST); 
			//	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			//}

		}
		
		return true;
	}
	
	public boolean forceloadtexture(Texture lt){
		if(lt == null)return false;
		last_texture = -1;
		TextureImpl.unbind(); //force reset
		lt.bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		return true;
	}
	
	public boolean loadtexture(Texture lt){
		if(lt == null)return false;
		if(last_texture != lt.getTextureID()){
			last_texture = lt.getTextureID();
			TextureImpl.unbind(); //force reset
			lt.bind();
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		}
		return true;
	}
		
	public static void recalcBrightness(int d, int yp){			
			float f = WorldRendererUtils.getBrightnessForLevel(d, yp);
			if(f < 0)f = 0;
			if(f > 1)f = 1;
			brightness_red = brightness_green = brightness_blue = f;			
	}
		
	public void recalcSkyBrightness(){
		float tod = DangerZone.world.timetimer % DangerZone.world.lengthOfDay;
		float fsin = (float) Math.sin(Math.toRadians((tod/(float)DangerZone.world.lengthOfDay)*360f));
		fsin *= 1.75f;
		if(fsin > 1)fsin = 1;
		if(fsin < -1)fsin = -1;
		float fsky = 0.50f + 0.45f*fsin;
		float maxfsky = 1.0f - 0.50f*((float)DangerZone.thundercount/(float)300);
		if(maxfsky<0.5f)maxfsky = 0.5f;
		if(fsky > maxfsky)fsky = maxfsky;		
		
		//sky color is variable per dimension!
		int pld = DangerZone.player.dimension;
		sky_red = Dimensions.DimensionArray[pld].sky_red * fsky;
		sky_green = Dimensions.DimensionArray[pld].sky_green * fsky;
		sky_blue = Dimensions.DimensionArray[pld].sky_blue * fsky;		
	
	}
	
	public void setBrightness(){
		GL11.glColor3f(brightness_red, brightness_green, brightness_blue);
	}
	
	public void setBrightness(float m){
		float flr, flg, flb;
		flr = brightness_red + m;
		if(flr < 0)flr = 0;
		if(flr > 1)flr = 1;
		flg = brightness_green + m;
		if(flg < 0)flg = 0;
		if(flg > 1)flg = 1;
		flb = brightness_blue + m;
		if(flb < 0)flb = 0;
		if(flb > 1)flb = 1;
		GL11.glColor3f(flr, flg, flb);
	}
	

	
	public void setBrightnessFocus(){
		float bval = 1.0f;
		if(!DangerZone.world.isDaytime())bval = 0.75f;
		GL11.glColor3f(bval, bval, bval);
	}
	
	public void setBrightnessFocusBlock(){
		float bval = WorldRendererUtils.getTotalLightAt(DangerZone.world, DangerZone.player.dimension, focus_x, focus_y, focus_z);
		//if(!DangerZone.world.isDaytime())bval = 0.75f;
		if(bval > 1)bval = 1;
		if(bval < 0)bval = 0;
		if(bval > 0.65f){
			bval -= 0.35f;
		}else{
			bval += 0.35f;
		}
		GL11.glColor3f(bval, bval, bval);
	}
	
	public void setBrightnessNonFocus(){
		float bval = 0.75f;
		if(!DangerZone.world.isDaytime())bval = 0.50f;
		GL11.glColor3f(bval, bval, bval);
	}
	
	public int getNextRenderID(){
		int i;
		lock.lock();
		i = GL11.glGenLists(1); //fetch a new display list
		lock.unlock();
		return i;
	}
	
	public static long getNextVBOid(){
		long i;
		lock.lock();
		i = nextVBOid;
		nextVBOid++;
		lock.unlock();
		return i;
	}
	

	public void deleteVBOlist(long[] vboids){
		VBOlistlock.lock();
		VBO_delete_list.add(vboids); 
		VBOlistlock.unlock();
	}

	/*
	 * Don't allow seeing through things.
	 * Start at eye level and scan outwards until we find max f5 distance.
	 */
	private void Scalef5toSolid(){
		float dx, dy, dz;
		float dst = 0; 
		dx = dy = dz = 0;
		int bid;
		int ix, iy, iz;
		int lx, ly, lz;
		lx = ly = lz = 0;		
		
		while(dst <= 8f){
			dx = f5x*dst/8f;
			dy = f5y*dst/8f;
			dz = f5z*dst/8f;
			ix = (int) (ViewFromEntity.posx - dx/blockrenderwidth);
			iy = (int) (ViewFromEntity.posy + ViewFromEntity.eyeheight - dy/blockrenderwidth);
			iz = (int) (ViewFromEntity.posz - dz/blockrenderwidth);
			if(ix != lx || iy != ly || iz != lz){ //wait until block change to actually go get one...
				lx = ix;
				ly = iy;
				lz = iz;
				bid = DangerZone.world.getblock(ViewFromEntity.dimension, ix, iy, iz);
				if(bid != 0 && Blocks.isSolidForRender(bid)){
					dst -= 0.25f; //back up a bit!
					if(dst < 0)dst = 0;
					dx = f5x*dst/8f;
					dy = f5y*dst/8f;
					dz = f5z*dst/8f;
					break;
				}
			}		
			dst += 0.1f;			
		}
		
		f5x = dx;
		f5y = dy;
		f5z = dz;
	}
	
	
}