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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.FloatBuffer;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import dangerzone.blocks.BlockRotation;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entities;
import dangerzone.entities.EntityBlockItem;
import dangerzone.entities.EntityExp;
import dangerzone.entities.ModelEntityBlockItem;
import dangerzone.entities.ModelExp;
import dangerzone.entities.ModelHumanoid;
import dangerzone.gui.GuiInterface;
import dangerzone.gui.KeyHandlers;
import dangerzone.gui.OverlayGUI;
import dangerzone.gui.PlayerChatGUI;
import dangerzone.gui.PlayerChestGUI;
import dangerzone.gui.PlayerColoringGUI;
import dangerzone.gui.PlayerCommandGUI;
import dangerzone.gui.PlayerCraftingGUI;
import dangerzone.gui.PlayerDeathGUI;
import dangerzone.gui.PlayerDeskGUI;
import dangerzone.gui.PlayerDroneGUI;
import dangerzone.gui.PlayerEntityGUI;
import dangerzone.gui.PlayerEscapeGUI;
import dangerzone.gui.PlayerFurnaceGUI;
import dangerzone.gui.PlayerHelpGUI;
import dangerzone.gui.PlayerInventoryGUI;
import dangerzone.gui.PlayerPetNameGUI;
import dangerzone.gui.PlayerShredderGUI;
import dangerzone.gui.PlayerSignGUI;
import dangerzone.gui.PlayerStatsGUI;
import dangerzone.gui.PlayerToDoGUI;
import dangerzone.gui.PlayerTradeGUI;
import dangerzone.items.Items;
import dangerzone.particles.Particles;
import dangerzone.threads.ChunkWriterThread;
import dangerzone.threads.CleanerThread;
import dangerzone.threads.EntityUpdateLoop;
import dangerzone.threads.LightingThread;
import dangerzone.threads.ParticleUpdateLoop;
import dangerzone.threads.Server;
import dangerzone.threads.ServerConnection;



public class DangerZone {
	

	public static String versionstring = "1.7";
	
	public static int gameover = 0;
	static String windowTitle = "Welcome to the Danger Zone...";
	public static float deltaT = 0;
	public static String server_address = null;
	public static int server_port = 0;
	public static String nsserver_address = null;
	public static int nsserver_port = 0;
	public static ChunkCache client_chunk_cache = null;
	public static ServerChunkCache server_chunk_cache = null;
	public static ChunkWriterThread chunkwriter = null;
	public static int screen_width = 1000;
	public static int screen_height = 600;
	public static Player player = null;
	public static World world = null;
	public static World server_world = null;
	public static String worldname = null; 
	public static String origworldname = null; 
	public static WorldRenderer wr = null;
	public static ServerConnection server_connection = null;
	public static Server server = null;
	public static WorldDecorators wds = null;
	public static BreakChecks breakchecker = null;
	public static Blocks all_the_blocks;
	public static Items all_the_items;
	public static Dimensions all_the_dimensions;
	public static Crafting all_the_recipies;
	public static DeskCrafting all_the_deskrecipies;
	public static Cooking all_the_cooking;
	public static Entities all_the_entities;
	public static Particles all_the_particles;
	public static Ores all_the_ores;
	public static Spawnlist all_the_spawns;
	public static CustomPackets all_the_custompackets;
	public static CommandHandlers all_the_handlers;
	public static Shredding all_the_shreds;
	public static Texture logotexture = null;
	public static Texture textinputtexture = null;
	public static int messagetimer = 0;
	public static String messagestring = null;
	public static int hotmessagetimer = 0;
	public static String hotmessagestring = null;
	public static String ghost_string = "Game Mode Ghost";
	public static String creative_string = "Game Mode Creative";
	public static String survival_string = "Game Mode Survival";
	public static String limbo_string = "Game Mode Limbo";
	public static int renderdistance = 24; //chunks
	public static int savedrenderdistance = 0;
	public static EntityUpdateLoop entityManager;
	public static ParticleUpdateLoop particleManager;
	public static int entityupdatedist = 16*24; //blocks!
	public static int max_entities = 8000;		//max entities allowed alive at any one time.
	public static DangerZoneBase base;
	public static int entityupdaterate = 16; //ms
	public static int serverentityupdaterate = 100; //ms
	public static boolean f3_on = false; //useful info overlay
	public static volatile boolean f12_on = false;//PAUSE and SHOWCASE!
	public static volatile boolean f5_front = false;
	public static volatile boolean f5_back = false;
	public static boolean view_ores = false;
	public static String blockitemname = "DangerZone:BlockItem"; //Builtin
	public static boolean escape_options_menu = false;
	public static boolean mouse_grabbed = false;
	public static volatile GuiInterface current_gui = null;
	
	//these can now all be overridden by the modder
	//completely replace any DZ gui
	//some take no input, some take an input being set by the caller, and some take a call to init()
	//find an example usage and do the same... I'm tired. I don't want to make them all the same... grrrr....
	//all external routines that start a DZ GUI should use these references and NOT declare a new instance.
	public static PlayerEscapeGUI escapegui = null;
	public static PlayerHelpGUI helpgui = null;
	public static PlayerChatGUI chatgui = null;
	public static PlayerCommandGUI commandgui = null;
	public static PlayerInventoryGUI inventorygui = null;
	public static PlayerDeathGUI deathgui = null;	
	public static PlayerChestGUI chestgui = null;
	public static PlayerColoringGUI coloringgui = null;
	public static PlayerCraftingGUI craftinggui = null;
	public static PlayerDeskGUI deskgui = null;
	public static PlayerDroneGUI dronegui = null;
	public static PlayerFurnaceGUI furnacegui = null;
	public static PlayerPetNameGUI petnamegui = null;
	public static PlayerShredderGUI shreddergui = null;
	public static PlayerSignGUI signgui = null;
	public static PlayerEntityGUI entitygui = null;
	public static PlayerTradeGUI tradegui = null;
	public static PlayerStatsGUI statsgui = null;
	public static PlayerToDoGUI todogui = null;
	
	public static DZCommandHandler commandhandler = null;
	public static EntityBlockItem blockitem = null;
	public static int packets_per_second = 0;
	public static int chunks_per_second = 0;
	public static int gofasttimer = 0;
	public static int gofast = 0;
	public static int flytimer = 0;
	public static int gofly = 0;
	public static volatile int do_hit_cycle = 0;
	public static volatile int do_food_cycle = 0;
	private static int volume_temp = 0;
	private static int music_volume_temp = 0;
	public static SoundManager soundmangler = null;
	public static boolean start_server = true;
	public static boolean start_client = true;
	private static long nowtime = 0;
	private static long thentime = 0;
	private static int pps = 0;
	public static Font awtfont = null;
	public static TrueTypeFont font = null;
	public static Font awtfont16 = null;
	public static TrueTypeFont font16 = null;
	public static LightingThread lto = null;
	public static CleanerThread tidyup = null;
	public static String playername = "Player";
	public static Random rand = null;
	public static TextureMapper texmap = new TextureMapper();
	public static int fieldOfView;
	public static int fieldOfViewLast;
	public static int mouseSensitivity;
	public static float accel = 0.0f;
	public static volatile boolean doleftclick = false;
	public static volatile boolean dorightclick = false;
	public static boolean keepitsqueakyclean = false;
	public static boolean generatecaves = true;
	public static int mindrawlevel = 0;
	public static ModLoader modloader = null;
	public static List<String> alt_texture_paths = new ArrayList<String>();
	public static List<ModListEntry> all_the_mods = new ArrayList<ModListEntry>();
	public static List<ModListEntry> all_the_server_mods = new ArrayList<ModListEntry>();
	public static boolean all_sides = true;
	public static boolean light_speed = true;
	public volatile static int new_dimension = 0;
	public volatile static double new_posx = 0;
	public volatile static double new_posy = 0;
	public volatile static double new_posz = 0;
	private static Chunk currchunk = null;
	public static boolean leftbuttondown = false;
	public static boolean rightbuttondown = false;
	public static boolean prevrightbuttondown = false;
	public static boolean dorightclickup = false;
	public static int rightbuttondowncounter = 0;
	public static int rapidfire_delay = 0;
	private static int repeatdelay = 0;
	private static int chunkwaitdelay = 0;
	private static boolean nogui = false;
	public static volatile boolean dimension_change_in_progress = false; //because the chunk cleaner can sometimes clean entities while teleporting!
	public static OverlayGUI overlayguis = new OverlayGUI();
	public static KeyHandlers keyhandlers = new KeyHandlers();
	public static boolean playnicely = false;
	public static boolean keep_inventory_on_death = false;
	public static boolean show_clouds = true;
	public static boolean show_rain = true;
	public volatile static int thundercount = 0;
	public static String crypted_password = null;
	public static boolean require_valid_passwords = false;
	public static boolean allow_anonymous = false;
	public static int default_privs = 0xffffffff;
	private static Properties configprop = new Properties();
	public static int max_players_on_server = 10;
	public static boolean private_server = false;
	public static volatile String connection_msg = null;
	public volatile static int connection_error = 0;
	public static float testx, testy, testz, testp, testw, testr;
	public static boolean show_server_stats = false;
	public static boolean bits64mode = true;
	public static boolean fullscreen = true;
	public static boolean fog_enable = true;
	public static boolean crafting_animation = true;
	private static FloatBuffer fogColor = null;
	public static boolean doDeathGUI = false;
	public static boolean freeze_world = false;
	public static boolean firedamage = true;
	public static boolean petprotection = false;
	public static ServerHooker server_hooks = null;
	public static volatile float magic_power = 0;
	public static volatile int magic_type = 0;
	//public static boolean printit = false;	
	public static Map<Integer, Integer> keymap_real_to_new = null;
	public static Map<Integer, Integer> keymap_new_to_real = null;
	public static ToDoList achievements = new ToDoList();
	public static boolean overlays_on = true;
	//public static float lastvol = 0;

	
	public static void initGL() 
	{
		/* OpenGL */
		screen_width = Display.getWidth();
		screen_height = Display.getHeight();
		GL11.glViewport(0, 0, screen_width, screen_height); // Reset The Current Viewport
		GL11.glMatrixMode(GL11.GL_PROJECTION); // Select The Projection Matrix
		GL11.glLoadIdentity(); // Reset The Projection Matrix
		GLU.gluPerspective((float)fieldOfView, ((float) screen_width / (float) screen_height), 1.0f, 256.0f*renderdistance); // Calculate The Aspect Ratio Of The Window
		GL11.glMatrixMode(GL11.GL_MODELVIEW); // Select The Modelview Matrix
		GL11.glLoadIdentity(); // Reset The Modelview Matrix
		//GL11.glShadeModel(GL11.GL_SMOOTH); // Enables Smooth Shading
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
		GL11.glClearDepth(1.0f); // Depth Buffer Setup
		GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
		GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Test To Do
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_FASTEST); // Fast Perspective Calculations
		GL11.glEnable(GL11.GL_TEXTURE_2D); 
		//transparent pixels do not write to the Z buffer!
		GL11.glAlphaFunc ( GL11.GL_GREATER, 0.1f ) ;
		GL11.glEnable ( GL11.GL_ALPHA_TEST ) ;	
		GL11.glEnable(GL14.GL_COLOR_SUM);
		
		if(fog_enable){
			if(fogColor == null)fogColor = BufferUtils.createFloatBuffer(4);
			fogColor.put(0.75f);
			fogColor.put(0.75f);
			fogColor.put(0.75f);
			fogColor.put(1.0f);
			fogColor.position(0);
			
			GL11.glEnable (GL11.GL_FOG); //enable the fog
			GL11.glFogi (GL11.GL_FOG_MODE, GL11.GL_LINEAR); //set the fog mode to GL_LINEAR
			GL11.glFogf (GL11.GL_FOG_START, 148.0f*renderdistance);
			GL11.glFogf (GL11.GL_FOG_END, 248.0f*renderdistance);			
			GL11.glFog (GL11.GL_FOG_COLOR, fogColor); //set the fog color to our color chosen above
			GL11.glFogf (GL11.GL_FOG_DENSITY, 0.0010f); //set the density
			GL11.glHint (GL11.GL_FOG_HINT, GL11.GL_FASTEST); // set the fog to look the fastest, may slow down on older cards
			
		}else{
			GL11.glDisable(GL11.GL_FOG);
		}
		
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {
		long lasttime = System.currentTimeMillis();
		long currtime, tlong;
		FloatBuffer listenerPos = BufferUtils.createFloatBuffer(3);
		FloatBuffer listenerOri = BufferUtils.createFloatBuffer(6);
		
		//because some places use commas in numbers, and of course java hoses them up!
		if(Locale.getDefault() != Locale.US) {
		    Locale.setDefault(Locale.US);
		}

		String curdir = System.getProperty("user.dir");	
		File libfile = new File(curdir + "/DangerZone_lib/native");
		File stopfile = new File(curdir + "/stop.txt");
		if(libfile.exists())System.setProperty("org.lwjgl.librarypath", curdir + "/DangerZone_lib/native");
		
		CodeSource codeSource = DangerZone.class.getProtectionDomain().getCodeSource();
		try {
			File myfile = new File(codeSource.getLocation().toURI().getPath());
			//System.out.printf("Adding self as: %s\n",  myfile.getAbsolutePath());
			ModLoader.addFile(myfile); //add self to path so it will search here for resources!
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		bits64mode = (System.getProperty("os.arch").indexOf("64") != -1);
		//bits64mode = false;
		
		if(args.length > 1){
			int arglen = args.length;
			int i;
			for(i=0;i<arglen;i++){
				//System.out.printf("arg %d = :%s\n", i, args[i]);
				if(args[i].contains("client")){
					start_server = false;
				}
				if(args[i].contains("server")){
					start_client = false;
				}
				if(args[i].contains("nogui")){
					nogui = true;
					start_client = false;
				}
				if(args[i].contains("stats")){
					show_server_stats = true;
				}
			}
			if(!start_client && !start_server){
				System.out.printf("Bad args?\n");
				System.exit(1);			
			}
		}else{
			start_client = true;
			start_server = true;
		}
		
		//for remapping keys.
		keymap_real_to_new = new HashMap<Integer, Integer>();
		keymap_new_to_real = new HashMap<Integer, Integer>();
		
		
		rand = new Random(lasttime);
		server_hooks = new ServerHooker(); //common to all
		
		Fastmath.inittable(); //prep the sin table.
		
		readConfig();
		
		if(!bits64mode){
			if(renderdistance > 16)renderdistance = 16;
			all_sides = false; //icky graphics
			entityupdatedist = 16*16; //smaller active area!!!
			light_speed = false; //slow lighting!
			System.out.printf("Aw man... 32-bit Java detected!\n");
		}
		
		if(!start_server){ //CLIENT ONLY
			if(renderdistance > 16)renderdistance = 16;
		}
		
		if(!start_client){ //SERVER ONLY
			renderdistance = 20;
			entityupdatedist = 16*20;
		}
		
		if(start_server && !start_client){ //SERVER ONLY
			screen_width = 800;
			screen_height = 400;
			fullscreen = false;
		}
		
		if(!nogui){
			//System.out.printf("I am here!\n");
			try {				
				if(!fullscreen){
					Display.setDisplayMode(new DisplayMode(screen_width, screen_height));
					Display.setResizable(true);
				}else{
					DisplayMode originalmode = Display.getDesktopDisplayMode();
					Display.setDisplayMode(originalmode);
					Display.setFullscreen(true);
					screen_width = Display.getWidth();
					screen_height = Display.getHeight();
				}
				//Display.setVSyncEnabled(true);// -- CPU MONSTER!!!
				Display.setVSyncEnabled(false);
				Display.setTitle(windowTitle);				
				Display.create();
				Keyboard.create(); //Keyboard only works in same context as Display, because it needs Display.update()!!!
				Mouse.create(); //Same with mouse!
				SoundStore.get().init();
				initGL();
				
				AL10.alDistanceModel(AL10.AL_NONE); //we do our own volume scaling
				
			} catch (LWJGLException e1) {
				System.out.printf("Something went horribly wrong with initialization!\n");
				e1.printStackTrace();
				System.exit(1);
			}

			showLogo(false);
			Display.update();


			if(font == null){
				awtfont = new Font("Times New Roman", Font.PLAIN, 24);
				font = new TrueTypeFont(awtfont, false);
				awtfont16 = new Font("Times New Roman", Font.PLAIN, 16);
				font16 = new TrueTypeFont(awtfont16, false);
			}
		}
		
		if(start_server){
			if(start_client){
				doStartSinglePlayer(); //fire it ALL up!
			}else{
				doStartServer(); //just the server stuff
			}
		}else{
			if(start_client){
				doStartClient(); //just the client stuff
			}else{
				System.out.printf("Hunh?\n");
				System.exit(1);		
			}
		}
		
		
		lasttime = System.currentTimeMillis();
		
		/*
		 * And finally, the main server/client loop...
		 */
		while(DangerZone.gameover == 0){
			
			if(stopfile.exists()){
				stopfile.delete();
				DangerZone.gameover = 1;
			}
			
			if(thundercount > 0){
				thundercount--;
			}
			
			if(start_client){				
								
				//TODO FIXME - find an interrupt/event vsync, not polled!
				//DO NOT use Display.sync()!!!
				currtime = System.currentTimeMillis();
				tlong = currtime - lasttime;
				tlong = 16 - tlong;
				
				if(wr.fps < 40){
					if(tlong < 4)tlong = 4; //give others at least a small chance to run!
				}
				if(wr.fps < 20){
					//Slower machines NEED this to process network packets!
					if(tlong < 8)tlong = 8; //give others at least a small chance to run!
				}

				if(tlong > 16)tlong = 16;

				if(tlong > 0)Thread.sleep(tlong);
				
				//update moved up here after the sleep, because it gives a much more stable frame rate,
				//since the sleep is what tries to adjust to make a constant speed!
				Display.update();
								
				//Display.sync(60); //CPU MONSTER!

				//Fixed time loop 16ms ~= 60 frames per second
				currtime = System.currentTimeMillis();
				tlong = currtime - lasttime;
				lasttime = currtime;
				deltaT = tlong;
				if(deltaT < 8)deltaT = 8;
				if(deltaT > 256)deltaT = 256;
				deltaT = deltaT / 16; //Scale! - 16ms loops (60 hz)
				
				//System.out.printf("Delta = %f\n", deltaT);				

				/*
				 * Keep safe from teleports by making sure we have the current chunk!
				 */
				if(new_dimension != 0){
					player.posx = new_posx;
					player.posy = new_posy;
					player.posz = new_posz;
					player.dimension = new_dimension;
					//new_dimension = 0;
					player.server_connection.sendPlayerEntityUpdate(player);
				
					chunkwaitdelay = 0;
					currchunk = world.chunkcache.getDecoratedChunk(player.dimension, (int)player.posx, 100, (int)player.posz); 			
					while(chunkwaitdelay < 200 && currchunk == null){
						DangerZone.server_connection.flushit();
						chunkwaitdelay++; //just in case it gets confused. timeout.
						Thread.sleep(100);
						player.server_connection.sendPlayerEntityUpdate(player);
						currchunk = world.chunkcache.getDecoratedChunk(player.dimension, (int)player.posx, 100, (int)player.posz); 
					}
					currchunk = null;
					new_dimension = 0;
				}
				
				//Have to do player updates here. Seems to cause screen glitches if not...
				if(!f12_on){
					player.update(deltaT);
				}else{
					player.server_connection.idle();
				}
				if(gofasttimer > 0)gofasttimer--;
				if(flytimer > 0)flytimer--;

				//Draw the world!!!
				wr.renderWorld(world); //f12 gui is drawn in here!!!
				
				if(doDeathGUI){
					f12_on = false;
					if(current_gui != null){
						if(current_gui != deathgui){
							current_gui.ImAllDone();
							current_gui = deathgui;
						}
					}else{
						current_gui = deathgui;
					}
				}

				if(current_gui != null && !f12_on){
					if(mouse_grabbed && !current_gui.grab_mouse){
						Mouse.setGrabbed(false); //We be un-cat-like
						mouse_grabbed = false;
					}
					
					messagestring = null;
					messagetimer = 0;

					/*
					 * Get into menu mode...
					 */
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
					GL11.glColor3f(1.0f, 1.0f, 1.0f); //brighten things up a bit!

					current_gui.process();

					//back out of menu mode
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glMatrixMode(GL11.GL_PROJECTION);
					GL11.glPopMatrix();
					GL11.glMatrixMode(GL11.GL_MODELVIEW);
					GL11.glPopMatrix();
					GL11.glEnable(GL11.GL_DEPTH_TEST);

				}else{
					if(OverlayGUI.guiarray[1] != null && overlays_on && !f12_on){
						/*
						 * Get into menu mode...
						 */
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
						GL11.glColor3f(1.0f, 1.0f, 1.0f); //brighten things up a bit!
						
						int ig;
						for(ig = 1;ig<OverlayGUI.guiMAX;ig++){
							if(OverlayGUI.guiarray[ig] != null){
								OverlayGUI.guiarray[ig].process();
							}
						}
						
						//back out of menu mode
						GL11.glDisable(GL11.GL_BLEND);
						GL11.glMatrixMode(GL11.GL_PROJECTION);
						GL11.glPopMatrix();
						GL11.glMatrixMode(GL11.GL_MODELVIEW);
						GL11.glPopMatrix();
						GL11.glEnable(GL11.GL_DEPTH_TEST);
					}
					if(!mouse_grabbed && !f12_on){
						Mouse.setGrabbed(true); //We be cat-like
						mouse_grabbed = true;
					}
					//Get some Keyboard and Mouse inputs
					doNormalInput();
				}

				// Force output to screen and fetch any new inputs....
				//Display.update();

				//Close clicked?
				if (Display.isCloseRequested()) {
					DangerZone.gameover = 1;
				}
				//Someone resized the window!
				if(Display.wasResized() || fieldOfView != fieldOfViewLast){
					initGL();
					fieldOfViewLast = fieldOfView;
				}

				//And lastly, kick the sound effects into gear...
				soundmangler.playMusic();
				
				//tell the sound player where we are and our orientation.
				//mostly all it needs is orientation so the sound hits the correct ear,
				//but it seems to overdo this and its mostly one ear or the other...
				//or maybe my speakers are just too far apart!
				
				listenerPos.rewind();
				listenerOri.rewind();
				
				listenerPos.put((float)player.posx);
				listenerPos.put((float)player.posy);
				listenerPos.put((float)player.posz);
				listenerPos.flip();
				
				listenerOri.put(-(float)Math.cos(Math.toRadians(player.rotation_yaw_head+90)));
				listenerOri.put(0);
				listenerOri.put(-(float)Math.sin(Math.toRadians(player.rotation_yaw_head+90)));
				listenerOri.put((float)0);
				listenerOri.put((float)1);
				listenerOri.put((float)0);
				listenerOri.flip();
				

				AL10.alListener(AL10.AL_POSITION, listenerPos);
				AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
				
				SoundStore.get().poll(0);
				
				if(player.deadflag){
					break; //will stop things slowly, later
				}
				
			}else{
				//is a server!
				Thread.sleep(100);
				nowtime = System.currentTimeMillis();
				if(nowtime - thentime > 1000){				
					thentime = nowtime;
					pps = DangerZone.packets_per_second;
					DangerZone.packets_per_second = 0;
				}
				
				if(!nogui){
					
					showLogo(false);

					/*
					 * Get into menu mode...
					 */
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
					GL11.glColor3f(1.0f, 1.0f, 1.0f); //brighten things up a bit!

					GL11.glPushMatrix();
					GL11.glTranslatef(10, screen_height-10, 0f); 
					GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f); // Don't ask me why, but the text is upside down! Flip it!		
					font.drawString(0, 0, String.format("Players: %d", server.server_thread_list.size()), Color.gray);		
					GL11.glPopMatrix();

					GL11.glPushMatrix();
					GL11.glTranslatef(10, screen_height-60, 0f); 
					GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f); // Don't ask me why, but the text is upside down! Flip it!		
					font.drawString(0, 0, String.format("Packets per Second: %d", pps), Color.gray);		
					GL11.glPopMatrix();

					GL11.glPushMatrix();
					GL11.glTranslatef(10, screen_height-110, 0f); 
					GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f); // Don't ask me why, but the text is upside down! Flip it!		
					font.drawString(0, 0, String.format("Entities: %d", DangerZone.server.entityManager.entity_list.size()), Color.gray);		
					GL11.glPopMatrix();
					
					GL11.glPushMatrix();
					GL11.glTranslatef(10, screen_height-160, 0f); 
					GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f); // Don't ask me why, but the text is upside down! Flip it!		
					font.drawString(0, 0, String.format("Active Entities: %d", DangerZone.server.entityManager.active_entities), Color.gray);		
					GL11.glPopMatrix();

					//back out of menu mode
					GL11.glMatrixMode(GL11.GL_PROJECTION);
					GL11.glPopMatrix();
					GL11.glMatrixMode(GL11.GL_MODELVIEW);
					GL11.glPopMatrix();
					GL11.glEnable(GL11.GL_DEPTH_TEST);


					Display.update();

					//Close clicked?
					if (Display.isCloseRequested()) {
						DangerZone.gameover = 1;
					}
					//Someone resized the window!
					if(Display.wasResized()){
						initGL();
					}	

					while (Keyboard.next()) {	
						//System.out.printf("Key!\n");
						if (K_getEventKey() == Keyboard.KEY_ESCAPE && K_isKeyDown(Keyboard.KEY_ESCAPE)){
							if(K_getEventKeyState()){
								DangerZone.gameover = 1;
							}
						}
					}
				}
			}
			
		}

		/*
		 * Game is over... shut down gracefully and save things properly...
		 */
		if(start_client){
			//Tell the server!
			player.server_connection.sendDisconnect();
			DangerZone.gameover = 1; //start shutting things down!		
			showLogo(false);
			Display.update();
			writeConfig(false);
			Thread.sleep(1000);

		}
		
		//Say bye bye!		
		Thread.sleep(500);	//Pretend we're busy doing real work.
		
		if(start_server){
			Thread.sleep(100);
			saveWorld(server_world);

			while(chunkwriter.getSize() > 0){
				Thread.sleep(100);
			}
			server_world.serverchunkcache.shutdown();
			Thread.sleep(200);
			
			//Close and save all the players!		
			server.savePlayers();
			Thread.sleep(200);
			
		}
		Thread.sleep(500);	//Pretend we're busy doing real work.

		if(!nogui){
			Mouse.destroy();
			Keyboard.destroy();
			Display.destroy();
			AL.destroy();
		}

		//System.out.printf("I am NOT here!\n");
		System.exit(0);
	}
	
	//Because these calls can ONLY be made from here.
	//No, you CANNOT access Keyboard or Mouse calls directly.
	//Access must go through DangerZone.java where the Keyboard is defined.
	//Use these routines to access Mouse and Keyboard from outside this file!
	//
	
	public static void keymap_clear(){
		keymap_real_to_new.clear();
		keymap_new_to_real.clear();
	}
	
	//remaps the physical realkey to the logical newkey
	public static void keymap_remap(int realkey, int newkey){
		keymap_real_to_new.put(realkey, newkey);
		keymap_new_to_real.put(newkey, realkey);
	}
	
	public static boolean K_next(){
		return Keyboard.next();
	}
	
	public static int K_getEventKey(){
		//return the logical key that was hit.
		int realkey = Keyboard.getEventKey();
		Integer mappedkey = keymap_real_to_new.get(realkey);
		if(mappedkey != null)return mappedkey.intValue();
		return realkey;
	}
	
	public static int K_getEventKeyUnMapped(){
		//return the key that was hit.
		return Keyboard.getEventKey();
	}
	
	public static boolean K_getEventKeyState(){
		return Keyboard.getEventKeyState();
	}
	
	public static boolean K_isKeyDown(int key){
		//map logical back to real and see if it is down
		Integer realkey = keymap_new_to_real.get(key);
		if(realkey != null)return Keyboard.isKeyDown(realkey.intValue());
		return Keyboard.isKeyDown(key);
	}
	
	public static boolean K_isKeyDownUnMapped(int key){
		return Keyboard.isKeyDown(key);
	}
	
	public static boolean M_next(){
		return Mouse.next();
	}
	
	public static int M_getEventButton(){
		return Mouse.getEventButton();
	}
	
	public static boolean M_getEventButtonState(){
		return Mouse.getEventButtonState();
	}
	
	public static int M_getEventX(){
		return Mouse.getEventX();
	}
	
	public static int M_getEventY(){
		return Mouse.getEventY();
	}
	
	public static int M_getX(){
		return Mouse.getX();
	}
	
	public static int M_getY(){
		return Mouse.getY();
	}
	
	public static int M_getDX(){
		return Mouse.getDX();
	}
	
	public static int M_getDY(){
		return Mouse.getDY();
	}
	
	
	public static void setActiveGui(GuiInterface g){
		if(current_gui == null){
			current_gui = g;
			do_hit_cycle = 0;
			do_food_cycle = 0;
			repeatdelay = 0;
			doleftclick = false;
			dorightclick = false;
			leftbuttondown = false;
			rightbuttondown = false;
		}
	}
	
	public static void clearActiveGui(){
			current_gui = null;
	}
	
	public static void doNormalInput(){
		float dx, dy;
		int i;
		accel -= 0.05f;
		if(accel < 0.1f)accel = 0.1f;
		
		//Keyboard input checks
		while (Keyboard.next()) {	
			
			if(f12_on && !(K_getEventKey() == Keyboard.KEY_F12)){
				f12_on = false;
				//setMusicVolume(lastvol);
				resumeMusic();
			}
			//System.out.printf("Key!\n");
			if (K_getEventKey() == Keyboard.KEY_ESCAPE && K_isKeyDown(Keyboard.KEY_ESCAPE)){
				if(K_getEventKeyState()){
					setActiveGui(escapegui);
				}
			}
			if (K_getEventKey() == Keyboard.KEY_H && K_isKeyDown(Keyboard.KEY_H)){
				if(K_getEventKeyState()){
					setActiveGui(helpgui);
				}
			}
			if (K_getEventKey() == Keyboard.KEY_E && K_isKeyDown(Keyboard.KEY_E)){
				if(K_getEventKeyState()){
					setActiveGui(inventorygui);
				}
			}
			if (K_getEventKey() == Keyboard.KEY_T && K_isKeyDown(Keyboard.KEY_T)){
				if(K_getEventKeyState()){
					setActiveGui(chatgui);
				}
			}
			if (K_getEventKey() == Keyboard.KEY_SLASH && K_isKeyDown(Keyboard.KEY_SLASH)){
				if(K_getEventKeyState()){
					setActiveGui(commandgui);
				}
			}
			if (K_getEventKey() == Keyboard.KEY_BACKSLASH && K_isKeyDown(Keyboard.KEY_BACKSLASH)){
				if(K_getEventKeyState()){
					setActiveGui(entitygui);
				}
			}
			if (K_getEventKey() == Keyboard.KEY_G && K_isKeyDown(Keyboard.KEY_G)){
				//change gamemode
				if(player.getGameMode() == GameModes.SURVIVAL){
					player.server_connection.changeGameMode(GameModes.GHOST);
				}else if(player.getGameMode() == GameModes.GHOST){
					player.server_connection.changeGameMode(GameModes.CREATIVE);
				}else if(player.getGameMode() == GameModes.CREATIVE){
					player.server_connection.changeGameMode(GameModes.SURVIVAL);
				}else{
					player.server_connection.changeGameMode(GameModes.SURVIVAL);
				}
			}
			
			if (K_getEventKey() == Keyboard.KEY_M && K_isKeyDown(Keyboard.KEY_M)){
				//change difficulty
				if(player.getGameDifficulty() == 0){
					player.server_connection.changeGameDifficulty(-2); //girly
				}else if(player.getGameDifficulty() == -2){
					player.server_connection.changeGameDifficulty(-1);
				}else if(player.getGameDifficulty() == -1){
					player.server_connection.changeGameDifficulty(1);
				}else if(player.getGameDifficulty() == 1){
					player.server_connection.changeGameDifficulty(2);
				}else{
					player.server_connection.changeGameDifficulty(0);
				}
			}

			if (K_getEventKey() == Keyboard.KEY_X && K_isKeyDown(Keyboard.KEY_X)){
				if(WorldRenderer.focus_x != 0 && WorldRenderer.focus_y != 0 && WorldRenderer.focus_z != 0){
					int bid = world.getblock(player.dimension, WorldRenderer.focus_x, WorldRenderer.focus_y, WorldRenderer.focus_z);
					int meta = world.getblockmeta(player.dimension, WorldRenderer.focus_x, WorldRenderer.focus_y, WorldRenderer.focus_z);
					if(!Blocks.isLiquid(bid)){
						if((meta&BlockRotation.X_MASK) == BlockRotation.X_ROT_0){//around
							meta = (meta & BlockRotation.NOT_X_MASK) | BlockRotation.X_ROT_90;
						}else if((meta&BlockRotation.X_MASK) == BlockRotation.X_ROT_90){//around
							meta = (meta & BlockRotation.NOT_X_MASK) | BlockRotation.X_ROT_180;
						}else if((meta&BlockRotation.X_MASK) == BlockRotation.X_ROT_180){//around
							meta = (meta & BlockRotation.NOT_X_MASK) | BlockRotation.X_ROT_270;
						}else if((meta&BlockRotation.X_MASK) == BlockRotation.X_ROT_270){//around
							meta = (meta & BlockRotation.NOT_X_MASK) | BlockRotation.X_ROT_0;
						}
						//System.out.printf("meta now %x\n", meta);
						world.setblockandmeta(player.dimension, WorldRenderer.focus_x, WorldRenderer.focus_y, WorldRenderer.focus_z, bid, meta);
					}
				}
			}
			if (K_getEventKey() == Keyboard.KEY_C && K_isKeyDown(Keyboard.KEY_C)){
				if(WorldRenderer.focus_x != 0 && WorldRenderer.focus_y != 0 && WorldRenderer.focus_z != 0){
					int bid = world.getblock(player.dimension, WorldRenderer.focus_x, WorldRenderer.focus_y, WorldRenderer.focus_z);
					int meta = world.getblockmeta(player.dimension, WorldRenderer.focus_x, WorldRenderer.focus_y, WorldRenderer.focus_z);
					if(!Blocks.isLiquid(bid)){
						if((meta&BlockRotation.Y_MASK) == BlockRotation.Y_ROT_0){//around
							meta = (meta & BlockRotation.NOT_Y_MASK) | BlockRotation.Y_ROT_90;
						}else if((meta&BlockRotation.Y_MASK) == BlockRotation.Y_ROT_90){//around
							meta = (meta & BlockRotation.NOT_Y_MASK) | BlockRotation.Y_ROT_180;
						}else if((meta&BlockRotation.Y_MASK) == BlockRotation.Y_ROT_180){//around
							meta = (meta & BlockRotation.NOT_Y_MASK) | BlockRotation.Y_ROT_270;
						}else if((meta&BlockRotation.Y_MASK) == BlockRotation.Y_ROT_270){//around
							meta = (meta & BlockRotation.NOT_Y_MASK) | BlockRotation.Y_ROT_0;
						}
						//System.out.printf("meta now %x\n", meta);
						world.setblockandmeta(player.dimension, WorldRenderer.focus_x, WorldRenderer.focus_y, WorldRenderer.focus_z, bid, meta);
					}
				}
			}
			if (K_getEventKey() == Keyboard.KEY_V && K_isKeyDown(Keyboard.KEY_V)){
				if(WorldRenderer.focus_x != 0 && WorldRenderer.focus_y != 0 && WorldRenderer.focus_z != 0){
					int bid = world.getblock(player.dimension, WorldRenderer.focus_x, WorldRenderer.focus_y, WorldRenderer.focus_z);
					int meta = world.getblockmeta(player.dimension, WorldRenderer.focus_x, WorldRenderer.focus_y, WorldRenderer.focus_z);
					if(!Blocks.isLiquid(bid)){
						if((meta&BlockRotation.Z_MASK) == BlockRotation.Z_ROT_0){//around
							meta = (meta & BlockRotation.NOT_Z_MASK) | BlockRotation.Z_ROT_90;
						}else if((meta&BlockRotation.Z_MASK) == BlockRotation.Z_ROT_90){//around
							meta = (meta & BlockRotation.NOT_Z_MASK) | BlockRotation.Z_ROT_180;
						}else if((meta&BlockRotation.Z_MASK) == BlockRotation.Z_ROT_180){//around
							meta = (meta & BlockRotation.NOT_Z_MASK) | BlockRotation.Z_ROT_270;
						}else if((meta&BlockRotation.Z_MASK) == BlockRotation.Z_ROT_270){//around
							meta = (meta & BlockRotation.NOT_Z_MASK) | BlockRotation.Z_ROT_0;
						}
						//System.out.printf("meta now %x\n", meta);
						world.setblockandmeta(player.dimension, WorldRenderer.focus_x, WorldRenderer.focus_y, WorldRenderer.focus_z, bid, meta);
					}
				}
			}
			if (K_getEventKey() == Keyboard.KEY_Y && K_isKeyDown(Keyboard.KEY_Y)){
				//Find the next existing dimension!
				if(player.getGameMode() != GameModes.SURVIVAL){					
					if(K_isKeyDown(Keyboard.KEY_LCONTROL)){
						Dimensions.DimensionArray[1].teleportToDimension(player, world, 1, (int)player.posx, (int)player.posy, (int)player.posz);
					}else{
						if(K_isKeyDown(Keyboard.KEY_LSHIFT)){
							for(i=0;i<Dimensions.dimensionsMAX;i++){
								int id = (player.dimension-i-1)%Dimensions.dimensionsMAX;
								while(id<0)id += Dimensions.dimensionsMAX;
								if(Dimensions.DimensionArray[id] != null){
									if(!Dimensions.DimensionArray[id].special_hidden){
										//System.out.printf("requesting switch to %d:%s\n", id, Dimensions.DimensionArray[id].uniquename);
										Dimensions.DimensionArray[id].teleportToDimension(player, world, id, (int)player.posx, (int)player.posy, (int)player.posz);
										break;
									}
								}
							}
						}else{
							for(i=0;i<Dimensions.dimensionsMAX;i++){
								int id = (player.dimension+i+1)%Dimensions.dimensionsMAX;
								if(Dimensions.DimensionArray[id] != null){
									if(!Dimensions.DimensionArray[id].special_hidden){
										//System.out.printf("requesting switch to %d:%s\n", id, Dimensions.DimensionArray[id].uniquename);
										Dimensions.DimensionArray[id].teleportToDimension(player, world, id, (int)player.posx, (int)player.posy, (int)player.posz);
										break;
									}
								}
							}
						}
					}
				}
			}
			if (K_getEventKey() == Keyboard.KEY_Q && K_isKeyDown(Keyboard.KEY_Q)){
				//Tell the server to spit out one item!
				DangerZone.server_connection.handleInventory(13, 0, 0, 0, 0);
			}
			if (K_getEventKey() == Keyboard.KEY_F2 && K_isKeyDown(Keyboard.KEY_F2)){
				Utils.screenshot(screen_width, screen_height);
				messagestring = "Screenshot!";
				messagetimer = 60;
			}
			if (K_getEventKey() == Keyboard.KEY_F3 && K_isKeyDown(Keyboard.KEY_F3)){
				//show some stats
				if(f3_on){
					f3_on = false;
				}else{
					f3_on = true;
					f12_on = false;
				}
			}
			if (K_getEventKey() == Keyboard.KEY_F12 && K_isKeyDown(Keyboard.KEY_F12) && start_client && start_server){
				//show some stats
				if(f12_on){
					f12_on = false;
					//setMusicVolume(lastvol);
					resumeMusic();
				}else{
					//lastvol = getMusicVolume();
					//setMusicVolume(0);
					pauseMusic();
					Mouse.setGrabbed(false); //We be cat-like
					mouse_grabbed = false;
					f12_on = true;
					f3_on = false;
				}
			}
			if (K_getEventKey() == Keyboard.KEY_F4 && K_isKeyDown(Keyboard.KEY_F4)){
				if(K_getEventKeyState()){
					setActiveGui(statsgui);
				}
			}
			if (K_getEventKey() == Keyboard.KEY_F11 && K_isKeyDown(Keyboard.KEY_F11)){
				if(K_getEventKeyState()){
					setActiveGui(todogui);
				}
			}
			if (K_getEventKey() == Keyboard.KEY_F6 && K_isKeyDown(Keyboard.KEY_F6)){
				if(player.getGameMode() != GameModes.SURVIVAL){
					if(view_ores){
						view_ores = false;
					}else{
						view_ores = true;
					}
				}
			}
			if (K_getEventKey() == Keyboard.KEY_F7 && K_isKeyDown(Keyboard.KEY_F7)){
					if(overlays_on){
						overlays_on = false;
					}else{
						overlays_on = true;
					}
			}
			if (K_getEventKey() == Keyboard.KEY_F5 && K_isKeyDown(Keyboard.KEY_F5)){
				//show me!!!
				if(f5_front){
					f5_front = false;
					f5_back = true;
				}else{
					if(f5_back){
						f5_back = false;
					}else{
						f5_front = true;
					}
				}
			}
			if (K_getEventKey() == Keyboard.KEY_1 && K_isKeyDown(Keyboard.KEY_1)){
				show_selection(0);
			}
			if (K_getEventKey() == Keyboard.KEY_2 && K_isKeyDown(Keyboard.KEY_2)){
				show_selection(1);
			}
			if (K_getEventKey() == Keyboard.KEY_3 && K_isKeyDown(Keyboard.KEY_3)){
				show_selection(2);
			}
			if (K_getEventKey() == Keyboard.KEY_4 && K_isKeyDown(Keyboard.KEY_4)){
				show_selection(3);
			}
			if (K_getEventKey() == Keyboard.KEY_5 && K_isKeyDown(Keyboard.KEY_5)){
				show_selection(4);
			}
			if (K_getEventKey() == Keyboard.KEY_6 && K_isKeyDown(Keyboard.KEY_6)){
				show_selection(5);
			}
			if (K_getEventKey() == Keyboard.KEY_7 && K_isKeyDown(Keyboard.KEY_7)){
				show_selection(6);
			}
			if (K_getEventKey() == Keyboard.KEY_8 && K_isKeyDown(Keyboard.KEY_8)){
				show_selection(7);
			}
			if (K_getEventKey() == Keyboard.KEY_9 && K_isKeyDown(Keyboard.KEY_9)){
				show_selection(8);
			}
			if (K_getEventKey() == Keyboard.KEY_0 && K_isKeyDown(Keyboard.KEY_0)){
				show_selection(9);
			}
			
			if (K_getEventKey() == Keyboard.KEY_W && K_isKeyDown(Keyboard.KEY_W)){
				if(gofasttimer != 0)gofast = 1;
				gofasttimer = 20;
			}
			
			if (K_getEventKey() == Keyboard.KEY_SPACE && K_isKeyDown(Keyboard.KEY_SPACE)){
				if(flytimer != 0)gofly = 1;
				flytimer = 20;
			}
			
			KeyHandlers.handleEvent();
		}

		if (K_isKeyDown(Keyboard.KEY_SPACE)){

				if(player.getGameMode() == GameModes.GHOST || player.getGameMode() == GameModes.LIMBO || player.isFlying() || player.isLadder()){
					player.motiony += 0.055f*deltaT*accel;
					accel += 0.1f;
					if(accel > 1)accel = 1;
				}else{
					if(player.getOnGround() || player.getInLiquid()){
						player.jump();
					}
					if(gofly != 0)player.tryfly();
				}
			
			player.setUp(true);
		}else{
			player.setUp(false);
			gofly = 0;
		}
		if (K_isKeyDown(Keyboard.KEY_LSHIFT)){

				if(player.getGameMode() == GameModes.GHOST || player.getGameMode() == GameModes.LIMBO || player.isFlying() || player.isLadder()){
					player.motiony -= 0.055f*deltaT*accel;
					accel += 0.1f;
					if(accel > 1)accel = 1;
				}
			
			player.setDown(true);
		}else{
			player.setDown(false);
		}
		if (K_isKeyDown(Keyboard.KEY_W)){
			
				float speed = 0.042f*accel;
				if(player.getGameMode() != GameModes.GHOST && player.getGameMode() != GameModes.LIMBO && 
						(world.getblock(player.dimension, (int)player.posx, (int)player.posy+2, (int)player.posz) != 0
						|| world.getblock(player.dimension, (int)player.posx, (int)player.posy+3, (int)player.posz) != 0 ))speed *= 0.70f;
				accel += 0.2f;
				if(accel > 1)accel = 1;
				if(player.getGameMode() == GameModes.GHOST || player.getGameMode() == GameModes.LIMBO || player.isFlying()){
					speed *= 4.0f;
				}else{
					if(gofast != 0)speed *= 2.5f;
				}
				float effectspeed;
				effectspeed = player.getTotalEffect(Effects.SPEED);
				if(effectspeed > 1){
					speed *= effectspeed;
				}
				effectspeed = player.getTotalEffect(Effects.SLOWNESS);
				if(effectspeed > 1){
					speed /= effectspeed;
				}
				player.motionx += speed*Math.cos(Math.toRadians(player.rotation_yaw_head-90))*deltaT;
				player.motionz += speed*Math.sin(Math.toRadians(player.rotation_yaw_head-90))*deltaT;
			
			player.setForward(true);
		}else{
			gofast = 0;
			player.setForward(false);
		}
		if (K_isKeyDown(Keyboard.KEY_S)){
			
				float speed = 0.030f*accel;
				accel += 0.1f;
				if(accel > 1)accel = 1;
				if(player.getGameMode() == GameModes.GHOST || player.getGameMode() == GameModes.LIMBO)speed *= 2.5f;
				float effectspeed;
				effectspeed = player.getTotalEffect(Effects.SPEED);
				if(effectspeed > 1){
					speed *= effectspeed;
				}
				effectspeed = player.getTotalEffect(Effects.SLOWNESS);
				if(effectspeed > 1){
					speed /= effectspeed;
				}
				player.motionx -= speed*Math.cos(Math.toRadians(player.rotation_yaw_head-90))*deltaT;
				player.motionz -= speed*Math.sin(Math.toRadians(player.rotation_yaw_head-90))*deltaT;
			
			player.setBackward(true);
		}else{
			player.setBackward(false);
		}
		
		if (K_isKeyDown(Keyboard.KEY_A)){
			
				float speed = 0.030f*accel;
				accel += 0.1f;
				if(accel > 1)accel = 1;
				if(player.getGameMode() == GameModes.GHOST || player.getGameMode() == GameModes.LIMBO)speed *= 2.5f;
				float effectspeed;
				effectspeed = player.getTotalEffect(Effects.SPEED);
				if(effectspeed > 1){
					speed *= effectspeed;
				}
				effectspeed = player.getTotalEffect(Effects.SLOWNESS);
				if(effectspeed > 1){
					speed /= effectspeed;
				}
				player.motionx += speed*Math.cos(Math.toRadians(player.rotation_yaw_head-180))*deltaT;
				player.motionz += speed*Math.sin(Math.toRadians(player.rotation_yaw_head-180))*deltaT;
			
			player.setLeft(true);
		}else{
			player.setLeft(false);
		}
		if (K_isKeyDown(Keyboard.KEY_D)){
			
				float speed = 0.030f*accel;
				accel += 0.1f;
				if(accel > 1)accel = 1;
				if(player.getGameMode() == GameModes.GHOST || player.getGameMode() == GameModes.LIMBO)speed *= 2.5f;
				float effectspeed;
				effectspeed = player.getTotalEffect(Effects.SPEED);
				if(effectspeed > 1){
					speed *= effectspeed;
				}
				effectspeed = player.getTotalEffect(Effects.SLOWNESS);
				if(effectspeed > 1){
					speed /= effectspeed;
				}
				player.motionx -= speed*Math.cos(Math.toRadians(player.rotation_yaw_head-180))*deltaT;
				player.motionz -= speed*Math.sin(Math.toRadians(player.rotation_yaw_head-180))*deltaT;
			
			player.setRight(true);
		}else{
			player.setRight(false);
		}
		
		if ((K_isKeyDown(Keyboard.KEY_LCONTROL) || K_isKeyDown(Keyboard.KEY_LMENU)) && player.getHotbar(player.gethotbarindex())==null){ //MENU == ALT
			int nump = 1;
			if(magic_power < 1f)magic_power += 0.005f;
			nump = (int)(magic_power*5f);
			if(nump < 1)nump = 1;
			
			if(K_isKeyDown(Keyboard.KEY_LCONTROL) && !K_isKeyDown(Keyboard.KEY_LMENU)){
				magic_type = 1; //heal
				if(rand.nextInt(3) == 1)Utils.spawnParticles(world, "DangerZone:ParticleHappy", nump, player.dimension, 
						player.posx+(float)Math.sin(Math.toRadians(180-player.rotation_yaw_head))*(player.getWidth()+1.5f)*(float)Math.cos(Math.toRadians(player.rotation_pitch_head)),
						player.posy+(player.getHeight()*8/10) - (float)Math.sin(Math.toRadians(player.rotation_pitch_head))*(player.getWidth()+1.5f),
						player.posz+(float)Math.cos(Math.toRadians(180-player.rotation_yaw_head))*(player.getWidth()+1.5f)*(float)Math.cos(Math.toRadians(player.rotation_pitch_head)),
						true);
			}
			if(!K_isKeyDown(Keyboard.KEY_LCONTROL) && K_isKeyDown(Keyboard.KEY_LMENU)){
				magic_type = 2; //harm
				if(rand.nextInt(3) == 1)Utils.spawnParticles(world, "DangerZone:ParticleDeath", nump, player.dimension, 
						player.posx+(float)Math.sin(Math.toRadians(180-player.rotation_yaw_head))*(player.getWidth()+1.5f)*(float)Math.cos(Math.toRadians(player.rotation_pitch_head)),
						player.posy+(player.getHeight()*8/10) - (float)Math.sin(Math.toRadians(player.rotation_pitch_head))*(player.getWidth()+1.5f),
						player.posz+(float)Math.cos(Math.toRadians(180-player.rotation_yaw_head))*(player.getWidth()+1.5f)*(float)Math.cos(Math.toRadians(player.rotation_pitch_head)),
						true);
			}
			if(K_isKeyDown(Keyboard.KEY_LCONTROL) && K_isKeyDown(Keyboard.KEY_LMENU)){
				magic_type = 3; //destruct!
				if(rand.nextInt(3) == 1)Utils.spawnParticles(world, "DangerZone:ParticleSmoke", nump, player.dimension, 
						player.posx+(float)Math.sin(Math.toRadians(180-player.rotation_yaw_head))*(player.getWidth()+1.5f)*(float)Math.cos(Math.toRadians(player.rotation_pitch_head)),
						player.posy+(player.getHeight()*8/10) - (float)Math.sin(Math.toRadians(player.rotation_pitch_head))*(player.getWidth()+1.5f),
						player.posz+(float)Math.cos(Math.toRadians(180-player.rotation_yaw_head))*(player.getWidth()+1.5f)*(float)Math.cos(Math.toRadians(player.rotation_pitch_head)),
						true);
			}
			
			int which = rand.nextInt(100);
			if(which == 0)player.world.playSound("DangerZone:swish3", player.dimension, player.posx, player.posy+1.25d, player.posz, 0.5f, 1);
			if(which == 1)player.world.playSound("DangerZone:swish4", player.dimension, player.posx, player.posy+1.25d, player.posz, 0.5f, 1);
			if(which == 2)player.world.playSound("DangerZone:swish5", player.dimension, player.posx, player.posy+1.25d, player.posz, 0.5f, 1);
			if(which == 3)player.world.playSound("DangerZone:swish6", player.dimension, player.posx, player.posy+1.25d, player.posz, 0.5f, 1);
			if(which == 4)player.world.playSound("DangerZone:swish7", player.dimension, player.posx, player.posy+1.25d, player.posz, 0.5f, 1);
			if(which == 5)player.world.playSound("DangerZone:swish8", player.dimension, player.posx, player.posy+1.25d, player.posz, 0.5f, 1);

		}else{
			magic_power = 0;
			magic_type = 0;
		}
		
		KeyHandlers.handleDown();
		
		//if (K_isKeyDown(Keyboard.KEY_P)){
		//	printit = true;
		//}		
		
		/*
		if (K_isKeyDown(Keyboard.KEY_I)){
			if(K_isKeyDown(Keyboard.KEY_LSHIFT)){
				testx -= 0.1f;
			}else{
				testx += 0.1f;
			}
			System.out.printf("xyz, pwr = %f, %f, %f and %f, %f, %f\n", testx, testy, testz, testp, testw, testr);
		}
		if (K_isKeyDown(Keyboard.KEY_O)){
			if(K_isKeyDown(Keyboard.KEY_LSHIFT)){
				testy -= 0.1f;
			}else{
				testy += 0.1f;
			}
			System.out.printf("xyz, pwr = %f, %f, %f and %f, %f, %f\n", testx, testy, testz, testp, testw, testr);
		}
		if (K_isKeyDown(Keyboard.KEY_P)){
			if(K_isKeyDown(Keyboard.KEY_LSHIFT)){
				testz -= 0.1f;
			}else{
				testz += 0.1f;
			}
			System.out.printf("xyz, pwr = %f, %f, %f and %f, %f, %f\n", testx, testy, testz, testp, testw, testr);
		}
		if (K_isKeyDown(Keyboard.KEY_J)){
			if(K_isKeyDown(Keyboard.KEY_LSHIFT)){
				testp -= 0.1f;
			}else{
				testp += 0.1f;
			}
			System.out.printf("xyz, pwr = %f, %f, %f and %f, %f, %f\n", testx, testy, testz, testp, testw, testr);
		}
		if (K_isKeyDown(Keyboard.KEY_K)){
			if(K_isKeyDown(Keyboard.KEY_LSHIFT)){
				testw -= 0.1f;
			}else{
				testw += 0.1f;
			}
			System.out.printf("xyz, pwr = %f, %f, %f and %f, %f, %f\n", testx, testy, testz, testp, testw, testr);
		}
		if (K_isKeyDown(Keyboard.KEY_L)){
			if(K_isKeyDown(Keyboard.KEY_LSHIFT)){
				testr -= 0.1f;
			}else{
				testr += 0.1f;
			}
			System.out.printf("xyz, pwr = %f, %f, %f and %f, %f, %f\n", testx, testy, testz, testp, testw, testr);
		}
		*/
		
		//if (K_isKeyDown(Keyboard.KEY_Z)){
		//	player.rotation_roll -= 1;
		//	player.rotation_roll = player.rotation_roll%360;
		//}
		//if (K_isKeyDown(Keyboard.KEY_C)){
		//	player.rotation_roll += 1;
		//	player.rotation_roll = player.rotation_roll%360;
		//}
		
				
		while(Mouse.next()){
			//System.out.printf("Mouse!\n");
			dx = Mouse.getDX();
			dx *= 0.1f + (0.01f * (float)mouseSensitivity);
			dy = Mouse.getDY();
			dy *= 0.1f + (0.01f * (float)mouseSensitivity);

			player.rotation_yaw_head += dx;
			if(player.rotation_yaw_head < 0)player.rotation_yaw_head += 360;
			player.rotation_yaw_head = player.rotation_yaw_head%360;		
			player.rotation_pitch_head -= dy;
			player.rotation_pitch_head = player.rotation_pitch_head%360;
			//System.out.printf("pitch = %f\n", player.rotation_pitch_head);
			if(player.rotation_pitch_head > 180){
				if(player.rotation_pitch_head < 270)player.rotation_pitch_head = 270;
			}else{
				if(player.rotation_pitch_head > 90)player.rotation_pitch_head = 90;
			}
			

			if(Mouse.getEventButtonState()){	
				//System.out.printf("DOWN: %d\n", Mouse.getEventButton());
				//clicking is hard work! :)
				if(player.getGameMode() == GameModes.SURVIVAL)player.setHunger(player.getHunger()-0.01f);
				
				if(Mouse.getEventButton() == 0){
					leftbuttondown = true;	
					rightbuttondown = false;
				}	
				if(Mouse.getEventButton() == 1){
					rightbuttondown = true;
					leftbuttondown = false;	
				}
				if(Mouse.getEventButton() == 2)player.middleclick(world, WorldRenderer.focus_x, WorldRenderer.focus_y, WorldRenderer.focus_z, 0);

			}else{
				//Mouse button released!
				if(Mouse.getEventButton() >= 0){
					//System.out.printf("UP: %d\n", Mouse.getEventButton());
					do_hit_cycle = 0;
					do_food_cycle = 0;
					leftbuttondown = false;
					rightbuttondown = false;
					repeatdelay = 0;
				}
			}
				
			i = Mouse.getDWheel();
			if(i != 0){
				i = -i/120; //why why why????
				//System.out.printf("Mouse wheel %d\n", i);
				i += player.gethotbarindex();
				while(i < 0)i += 10;
				i %= 10;			
				show_selection(i);
			}

		}
		
		if(leftbuttondown || rightbuttondown){			
			if(repeatdelay == 0 || repeatdelay > 30){
				if(leftbuttondown){
					do_hit_cycle = 1;
					do_food_cycle = 0;
					doleftclick = true;
					dorightclick = false;
				}	
				if(rightbuttondown){
					InventoryContainer ic = player.getHotbar(player.gethotbarindex());
					if(WorldRenderer.focus_entity != null){
						do_hit_cycle = 1;
						do_food_cycle = 0;
						dorightclick = true;
						doleftclick = false;
					}else{
						if(ic != null && Items.isFood(ic.iid) && WorldRenderer.focus_x == 0 && WorldRenderer.focus_z == 0){
							do_food_cycle = 1;
							do_hit_cycle = 0;
							doleftclick = false;
							dorightclick = false;
						}else{
							do_hit_cycle = 1;
							do_food_cycle = 0;
							dorightclick = true;
							doleftclick = false;
						}
					}
				}				
			}
			repeatdelay++;
		}else{
			repeatdelay = 0;
		}
		
		if(rapidfire_delay > 0)rapidfire_delay--;
		if(prevrightbuttondown){
			if(!rightbuttondown){
				//right button is now up!
				dorightclickup = true;
			}else{
				rightbuttondowncounter++;
				rapidfire_delay = 10;
			}			
		}
		
		prevrightbuttondown = rightbuttondown;

	}
	
	public static void showLogo(boolean redcolor){
		float x, y;
		
		if(logotexture == null){
			try {
				logotexture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/menus/logo.png"));
				textinputtexture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/menus/textinput.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(redcolor){
			GL11.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
		}else{
			GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		}
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer
		GL11.glLoadIdentity(); // Reset The View
		
		if(redcolor){
			GL11.glColor3f(1.0f, 0.2f, 0.2f);
		}else{			
			GL11.glColor3f(1.0f, 1.0f, 1.0f);
		}
		logotexture.bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		
		x = 200;
		y = 190;
		
		GL11.glTranslatef(0, 0, -600);
		
		GL11.glBegin(GL11.GL_QUADS);	
		GL11.glTexCoord2f(1,0);
		GL11.glVertex3f(x, y, 0.0f); // Top Right Of The Quad (Front)
		GL11.glTexCoord2f(0,0);
		GL11.glVertex3f(-x, y, 0.0f); // Top Left Of The Quad (Front)
		GL11.glTexCoord2f(0,1);
		GL11.glVertex3f(-x, -y, 0.0f); // Bottom Left Of The Quad (Front)
		GL11.glTexCoord2f(1,1);
		GL11.glVertex3f(x, -y, 0.0f); // Bottom Right Of The Quad (Front)
		GL11.glEnd(); // Done Drawing The Quad
		
		
		if(connection_msg != null){
			/*
			 * Get into menu mode...
			 */
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
			GL11.glColor3f(1.0f, 1.0f, 1.0f); //brighten things up a bit!
			
			GL11.glPushMatrix();
			GL11.glTranslatef((screen_width/2)-(connection_msg.length()*5), screen_height/2, 0f); 
			GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f); // Don't ask me why, but the text is upside down! Flip it!		
			font.drawString(0, 0, connection_msg, Color.white);		
			GL11.glPopMatrix();
			
			//back out of menu mode
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glPopMatrix();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glPopMatrix();
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		}
		
	}
	

	
	private static void readConfig(){
		InputStream input = null;
		
		//defaults for when the file doesn't exist!
		server_address = "127.0.0.1";
		server_port = 18668;
		nsserver_address = "73.135.82.220";
		nsserver_port = 18669;
		screen_width = 1024;
		screen_height = 600;
		renderdistance = 24;
		volume_temp = 20;
		music_volume_temp = 20;
		worldname = "my_world";
		playername ="Player";
		fieldOfView = 45;
		mouseSensitivity = 0;
		mindrawlevel = 0;
		all_sides = true;
		light_speed = true;
		keepitsqueakyclean = false;
		show_clouds = true;
		show_rain = true;
		crypted_password = "";
		fullscreen = true;
		fog_enable = true;
		crafting_animation = true;
		
		try {	 
			input = new FileInputStream("DangerZone.properties");
	 
			// load a properties file
			configprop.load(input);
	 
			screen_width = Utils.getPropertyInt(configprop, "ScreenWidth", 500, 4000, 1024); //min, max, default
			screen_height = Utils.getPropertyInt(configprop, "ScreenHeight", 300, 3000, 600); //min, max, default
			renderdistance = Utils.getPropertyInt(configprop, "RenderDistance", 4, 24, 16); //min, max, default
			volume_temp = Utils.getPropertyInt(configprop, "Volume", 0, 100, 20); //min, max, default
			music_volume_temp = Utils.getPropertyInt(configprop, "MusicVolume", 0, 100, 20); //min, max, default
			server_address = Utils.getPropertyString(configprop, "ServerAddress", "127.0.0.1"); //
			server_port = Utils.getPropertyInt(configprop, "ServerPort", 0, Integer.MAX_VALUE, 18668); //min, max, default
			nsserver_address = Utils.getPropertyString(configprop, "NameServerAddress", "73.135.82.220"); //
			nsserver_port = Utils.getPropertyInt(configprop, "NameServerPort", 0, Integer.MAX_VALUE, 18669); //min, max, default
			worldname = Utils.getPropertyString(configprop, "WorldName", "my_world"); 
			origworldname = worldname;
			playername = Utils.getPropertyString(configprop, "Playername", "Player");
			fieldOfView = Utils.getPropertyInt(configprop, "FieldOfView", 30, 80, 45); //min, max, default
			mouseSensitivity = Utils.getPropertyInt(configprop, "MouseSensitivity", -9, 9, 0); //min, max, default
			keepitsqueakyclean = Utils.getPropertyBoolean(configprop, "RunFinalization", false);
			mindrawlevel = Utils.getPropertyInt(configprop, "MinDrawLevel", 0, 40, 0); //min, max, default
			all_sides = Utils.getPropertyBoolean(configprop, "MaxGraphics", true);
			light_speed = Utils.getPropertyBoolean(configprop, "FastLighting", true);
			show_clouds = Utils.getPropertyBoolean(configprop, "ShowClouds", true);
			show_rain = Utils.getPropertyBoolean(configprop, "ShowRain", true);
			crypted_password = Utils.getPropertyString(configprop, "CryptedPassword", "");
			fullscreen = Utils.getPropertyBoolean(configprop, "FullScreen", true);
			fog_enable = Utils.getPropertyBoolean(configprop, "FogEnable", true);
			crafting_animation = Utils.getPropertyBoolean(configprop, "CraftingAnimation", true);


		} catch (IOException ex) {
			//ex.printStackTrace();
		}
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}	
		
		//Throttle for real server. Takes a huge load off the network.
		if(!start_server){
			if(renderdistance > 20){
				savedrenderdistance = renderdistance;
				renderdistance = 20;
			}
		}
		
	}
	
	private static void writeConfig(boolean pld){
		FileOutputStream output = null;
	 
		try {	 
			output = new FileOutputStream("DangerZone.properties");	 
			// set the properties value
			configprop.setProperty("ScreenWidth", String.format("%d", screen_width));
			configprop.setProperty("ScreenHeight", String.format("%d", screen_height));
			if(savedrenderdistance > renderdistance)renderdistance = savedrenderdistance; //restore
			configprop.setProperty("RenderDistance", String.format("%d", renderdistance));
			if(soundmangler != null){
				configprop.setProperty("Volume", String.format("%d", soundmangler.master_volume));
				configprop.setProperty("MusicVolume", String.format("%d", soundmangler.music_master_volume));
			}
			configprop.setProperty("ServerAddress", server_address);
			configprop.setProperty("ServerPort", String.format("%d", server_port));
			configprop.setProperty("NameServerAddress", nsserver_address);
			configprop.setProperty("NameServerPort", String.format("%d", nsserver_port));
			String strippedname = worldname;
			if(!start_server)strippedname = origworldname; //restore. don't save server world name!
			if(strippedname.contains("/")){
				int is = strippedname.lastIndexOf("/");
				strippedname = strippedname.substring(is+1);
			}
			configprop.setProperty("WorldName", strippedname);
			configprop.setProperty("Playername", playername);
			configprop.setProperty("FieldOfView", String.format("%d", fieldOfView));
			configprop.setProperty("MouseSensitivity", String.format("%d", mouseSensitivity));
			configprop.setProperty("RunFinalization", String.format("%s", keepitsqueakyclean?"true":"false"));
			configprop.setProperty("MinDrawLevel", String.format("%d", mindrawlevel));
			configprop.setProperty("MaxGraphics", String.format("%s", all_sides?"true":"false"));
			configprop.setProperty("FastLighting", String.format("%s", light_speed?"true":"false"));
			configprop.setProperty("ShowClouds", String.format("%s", show_clouds?"true":"false"));
			configprop.setProperty("ShowRain", String.format("%s", show_rain?"true":"false"));
			configprop.setProperty("PlayerDied", String.format("%s", pld?"true":"false"));
			configprop.setProperty("CryptedPassword", crypted_password);
			configprop.setProperty("FullScreen", String.format("%s", fullscreen?"true":"false"));
			configprop.setProperty("FogEnable", String.format("%s", fog_enable?"true":"false"));
			configprop.setProperty("CraftingAnimation", String.format("%s", crafting_animation?"true":"false"));
	 
			// save properties to project root folder
			configprop.store(output, null);
	 
		} catch (IOException io) {
			io.printStackTrace();
		}
		if (output != null) {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	

	
	public static void loadWorld(World w){
		InputStream input = null;
		Properties worldprop = new Properties();
		String filepath = new String();		
		filepath = String.format("worlds/%s/world.dat", DangerZone.worldname);	
		
		generatecaves = true;
		w.timetimer = 0;
		playnicely = false;
		keep_inventory_on_death = false;
		require_valid_passwords = false;
		allow_anonymous = false;
		default_privs = 0xffffffff;
		private_server = false;
		freeze_world = false;
		petprotection = false;
		firedamage = true;
		max_players_on_server = 10;
		w.worldseed = System.currentTimeMillis();
		 
		try {	 
			input = new FileInputStream(filepath);	 
			// load a properties file
			worldprop.load(input);
			
			w.timetimer = Utils.getPropertyInt(worldprop, "TimeOfDay", 0, Integer.MAX_VALUE, 0); //min, max, default
			generatecaves = Utils.getPropertyBoolean(worldprop, "CaveGeneration", true);
			playnicely = Utils.getPropertyBoolean(worldprop, "PlayNicely", false);
			keep_inventory_on_death = Utils.getPropertyBoolean(worldprop, "KeepInventory", false);
			private_server = Utils.getPropertyBoolean(worldprop, "PrivateServer", false);
			freeze_world = Utils.getPropertyBoolean(worldprop, "FreezeWorld", false);
			petprotection = Utils.getPropertyBoolean(worldprop, "PetProtection", false);
			firedamage = Utils.getPropertyBoolean(worldprop, "FireDamage", true);
			require_valid_passwords = Utils.getPropertyBoolean(worldprop, "RequireValidPassword", false);
			allow_anonymous = Utils.getPropertyBoolean(worldprop, "AllowAnonymous", false);
			default_privs = Utils.getPropertyInt(worldprop, "DefaultPrivs", Integer.MIN_VALUE, Integer.MAX_VALUE, 0xffffffff); //min, max, default
			max_players_on_server = Utils.getPropertyInt(worldprop, "MaxPlayers", 2, 255, 10); //min, max, default
			w.worldseed = Utils.getPropertyLong(worldprop, "WorldSeed", Long.MIN_VALUE, Long.MAX_VALUE, w.worldseed); //min, max, default

		} catch (IOException ex) {
			//ex.printStackTrace();
			input = null;
			//new world?

			FileOutputStream privoutput = null;
			filepath = String.format("worlds/%s/player_privs.dat", DangerZone.worldname);	
			File f = new File(filepath);		
			f.getParentFile().mkdirs();	
			Properties privprop = new Properties();
			privprop.setProperty(playername.toLowerCase(), String.format("%d", 0xffffffff)); //world creator gets all privs
			try {
				privoutput = new FileOutputStream(filepath);			 
				// save properties
				privprop.store(privoutput, null);	
				privoutput.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	

		}
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		
	}
	
	public static void saveWorld(World w){
		Properties worldprop = new Properties();
		FileOutputStream output = null;
		String filepath = new String();		
		filepath = String.format("worlds/%s/world.dat", DangerZone.worldname);	
		File f = new File(filepath);		
		f.getParentFile().mkdirs();	
		
		try {	
					
			worldprop.setProperty("TimeOfDay", String.format("%d", w.timetimer));
			worldprop.setProperty("CaveGeneration", String.format("%s", generatecaves?"true":"false"));
			worldprop.setProperty("PlayNicely", String.format("%s", playnicely?"true":"false"));
			worldprop.setProperty("KeepInventory", String.format("%s", keep_inventory_on_death?"true":"false"));
			worldprop.setProperty("PrivateServer", String.format("%s", private_server?"true":"false"));
			worldprop.setProperty("FreezeWorld", String.format("%s", freeze_world?"true":"false"));
			worldprop.setProperty("PetProtection", String.format("%s", petprotection?"true":"false"));
			worldprop.setProperty("FireDamage", String.format("%s", firedamage?"true":"false"));
			worldprop.setProperty("RequireValidPassword", String.format("%s", require_valid_passwords?"true":"false"));
			worldprop.setProperty("AllowAnonymous", String.format("%s", allow_anonymous?"true":"false"));
			worldprop.setProperty("DefaultPrivs", String.format("%d", default_privs));
			worldprop.setProperty("MaxPlayers", String.format("%d", max_players_on_server));
			worldprop.setProperty("WorldSeed", String.format("%d", w.worldseed));
			
			output = new FileOutputStream(filepath);	 
			// save properties
			worldprop.store(output, null);			
	 
		} catch (IOException io) {
			io.printStackTrace();
		}
		if (output != null) {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public static String getWorldDirectoryPath()
	{
		return String.format("worlds/%s/", DangerZone.worldname);
	}

	private static void doStartSinglePlayer(){
		
		connection_msg = "Starting a few things...";
		showLogo(false);
		Display.update();
		
		//SOUNDS!!! Woohoo!
		soundmangler = new SoundManager();
		soundmangler.master_volume = volume_temp;
		soundmangler.music_master_volume = music_volume_temp;

		//Make a place for chunks (client)		
		client_chunk_cache = new ChunkCache();  		//This caching mechanism is entirely different from...
		client_chunk_cache.passthru = true; //ONLY ONE ACTIVE CACHE!

		server_chunk_cache = new ServerChunkCache();	//this caching mechanism!
		chunkwriter = new ChunkWriterThread();
		Thread cwt = new Thread(chunkwriter);
		cwt.start();
		
		wds = new WorldDecorators();
		breakchecker = new BreakChecks();

		player = new Player(null);	

		player.model = new ModelHumanoid(); //Set renderer! (by hand)
		player.myname = playername;
		player.setPetName(playername);
		world = new World(client_chunk_cache, null);
		player.world = world; 


		//And a server world!
		server_world = new World(null, server_chunk_cache); //world for the server to use the server chunk cache!
		server_world.isServer = true; //yes we are!
		server_world.timetimer = 0;
		
		loadWorld(server_world);

		//Make a world renderer
		wr = new WorldRenderer();

		all_the_blocks = new Blocks();
		Blocks.load(); //Grab the known list from disk
		all_the_items = new Items();
		Items.load(); //Grab the known list from disk
		all_the_dimensions = new Dimensions();
		Dimensions.load(); //Grab the known list from disk

		//custom packets!
		all_the_custompackets = new CustomPackets();

		//make the crafting list
		all_the_recipies = new Crafting();
		all_the_deskrecipies = new DeskCrafting();

		//make the cooking list
		all_the_cooking = new Cooking();

		//make ore list holder
		all_the_ores = new Ores();

		//make spawn list holder
		all_the_spawns = new Spawnlist();
		
		//make command handler list
		all_the_handlers = new CommandHandlers();

		//particles!
		all_the_particles = new Particles();
		
		//make the shredding list
		all_the_shreds = new Shredding();

		//make a couple internal things we can't live without!
		all_the_entities = new Entities();
		blockitem = new EntityBlockItem(world);
		Entities.registerEntity(EntityBlockItem.class, blockitem.uniquename, new ModelEntityBlockItem());	//SPECIAL! Internal entity
		Entities.registerEntity(Player.class, "DangerZone:Player", new ModelHumanoid());					//SPECIAL! Internal entity
		Entities.registerEntity(EntityExp.class, "DangerZone:Experience", new ModelExp());					//SPECIAL! Internal entity

		//Load up the default dimensions, creatures, blocks, items, etc!
		base = new DangerZoneBase();
		base.registerThings();

		/*
		 * LOAD ALL OTHER MODS HERE!
		 */	
		connection_msg = "Loading mods...";
		showLogo(false);
		Display.update();
		
		modloader = new ModLoader();
		String badfile = modloader.loadmods();
		if(badfile != null){
			connection_msg = "Please remove incompatible mod:";
			showLogo(false);
			Display.update();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			} 
			connection_msg = badfile;
			showLogo(false);
			Display.update();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			} 
			System.exit(1);
		}

		base.postLoadProcessing();

		//FIXME TODO! If new world, copy mods to the created world directory.
		// If existing world, load mods from the world directory.
		//
		//FIXME TODO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// 


		//Save all the new blocks if any
		Blocks.save();
		//Save all the new items if any
		Items.save();
		//Save all the new Dimensions if any
		Dimensions.save();


		try {
			Thread.sleep(200);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		} //No reason, just make it seem like we're doing something...

		connection_msg = "Firing up some threads...";
		showLogo(false);
		Display.update();

		entityManager = new EntityUpdateLoop();
		Thread it = new Thread(entityManager);	//Use to do gc and run updates to the entities...
		//it.setPriority(Thread.MAX_PRIORITY);
		it.start();	

		particleManager = new ParticleUpdateLoop();
		Thread pit = new Thread(particleManager);	//Use to do gc and run updates to the entities...
		//it.setPriority(Thread.MAX_PRIORITY);
		pit.start();	

		commandhandler = new DZCommandHandler();

		server = new Server();
		Thread st = new Thread(server);	//Fire up the actual SERVER side
		st.start();	
		
		while(server.entityManager == null){
			Thread.yield();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} //Let it all catch up a little...
		}

		player.getTexture(); //pre-load this here! It doesn't work in the server_connection thread!
		server_connection = new ServerConnection(player, false);
		Thread ct = new Thread(server_connection);	//Fire up server comms for the player
		ct.setPriority(Thread.MAX_PRIORITY); //Don't bog down the network!
		ct.start();	

		escapegui = new PlayerEscapeGUI();
		inventorygui = new PlayerInventoryGUI();
		chatgui = new PlayerChatGUI();
		helpgui = new PlayerHelpGUI();
		commandgui = new PlayerCommandGUI();
		deathgui = new PlayerDeathGUI();		
		chestgui = new PlayerChestGUI();
		coloringgui = new PlayerColoringGUI();
		craftinggui = new PlayerCraftingGUI();
		deskgui = new PlayerDeskGUI();
		dronegui = new PlayerDroneGUI();
		furnacegui = new PlayerFurnaceGUI();
		petnamegui = new PlayerPetNameGUI();
		shreddergui = new PlayerShredderGUI();
		signgui = new PlayerSignGUI();
		entitygui = new PlayerEntityGUI();
		tradegui = new PlayerTradeGUI();
		statsgui = new PlayerStatsGUI();
		todogui = new PlayerToDoGUI();


		int i = 0;
		while(server_connection.connected == 0 && i < 10){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} //Wait for the two to connect!
			i++;
		}
		if(i >= 10){
			System.exit(1);
		}

		//Lighting!!!
		lto = new LightingThread();
		Thread lt = new Thread(lto);
		lt.start();

		//What a MESS!!!
		tidyup = new CleanerThread();
		Thread clt = new Thread(tidyup);
		clt.setPriority(Thread.MAX_PRIORITY); //YES! Needed!!! Especially for slower machines to keep from running out of memory...
		clt.start();
		
		connection_msg = "Loading textures...";
		showLogo(false);
		Display.update();
		
		//load ALL block textures
		wr.loadBlockTextures();
		
		connection_msg = "Waiting for first chunk...";
		showLogo(false);
		Display.update();
		
		//Wait for at least the chunk I am in before continuing!
		//tell it what dimension we are in, or it won't send us the chunk we are waiting for.		
		DangerZone.server_connection.sendPlayerEntityUpdate(player);
		Chunk c = null;
		while(c == null){
			c = player.world.chunkcache.getDecoratedChunk(player.dimension, (int)player.posx, 100, (int)player.posz);
			DangerZone.server_connection.flushit();
			if(c != null)break;
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		c = null;

		//set this so the wr knows what dimension we are in. it is a flag to clear all VBOs when changed!
		wr.lastplayerdimension = player.dimension;

		//And finally... grab the mouse pointer!
		Mouse.setGrabbed(true); //We be cat-like
		mouse_grabbed = true;

		//we have to do this to help smooth the mouse motion...
		Thread thisthread = Thread.currentThread();
		thisthread.setPriority(Thread.MAX_PRIORITY);

		connection_msg = null;
	}

	private static void doStartServer(){
		
		//SOUNDS!!! Woohoo! We don't play any in the server, just registration...
		soundmangler = new SoundManager();
		soundmangler.master_volume = volume_temp;
		soundmangler.music_master_volume = music_volume_temp;

		server_chunk_cache = new ServerChunkCache();
		chunkwriter = new ChunkWriterThread();
		Thread cwt = new Thread(chunkwriter);
		cwt.start();
		
		wds = new WorldDecorators();
		breakchecker = new BreakChecks();

		//player = new Player(null);	
		
		//And a server world!
		server_world = new World(null, server_chunk_cache); //world for the server to use the server chunk cache!
		server_world.isServer = true; //yes we are!
		server_world.timetimer = 0;
		//player.world = server_world; 
		
		loadWorld(server_world);

		all_the_blocks = new Blocks();
		Blocks.load(); //Grab the known list from disk
		all_the_items = new Items();
		Items.load(); //Grab the known list from disk
		all_the_dimensions = new Dimensions();
		Dimensions.load(); //Grab the known list from disk

		//custom packets!
		all_the_custompackets = new CustomPackets();

		//make the crafting list
		all_the_recipies = new Crafting();
		all_the_deskrecipies = new DeskCrafting();

		//make the cooking list
		all_the_cooking = new Cooking();

		//make ore list holder
		all_the_ores = new Ores();

		//make spawn list holder
		all_the_spawns = new Spawnlist();
		
		//make command handler list
		all_the_handlers = new CommandHandlers();

		//particles!
		all_the_particles = new Particles();
		
		//make the shredding list
		all_the_shreds = new Shredding();

		//make a couple internal things we can't live without!
		all_the_entities = new Entities();
		blockitem = new EntityBlockItem(world);
		Entities.registerEntity(EntityBlockItem.class, blockitem.uniquename, new ModelEntityBlockItem());	//SPECIAL! Internal entity
		Entities.registerEntity(Player.class, "DangerZone:Player", new ModelHumanoid());						//SPECIAL! Internal entity
		Entities.registerEntity(EntityExp.class, "DangerZone:Experience", new ModelExp());					//SPECIAL! Internal entity

		//Load up the default dimensions, creatures, blocks, items, etc!
		base = new DangerZoneBase();
		base.registerThings();

		/*
		 * LOAD ALL OTHER MODS HERE!
		 */	
		modloader = new ModLoader();
		String badfile = modloader.loadmods();
		if(badfile != null)System.exit(1);
		modloader.loadservermods();

		base.postLoadProcessing();
		

		//Save all the new blocks if any
		Blocks.save();
		//Save all the new items if any
		Items.save();
		//Save all the new Dimensions if any
		Dimensions.save();

		try {
			Thread.sleep(200);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} //No reason, just make it seem like we're doing something...

		commandhandler = new DZCommandHandler();

		server = new Server();
		Thread st = new Thread(server);	//Fire up the actual SERVER side
		st.start();	

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} //Let it all catch up a little...

		//What a MESS!!!
		tidyup = new CleanerThread();
		Thread clt = new Thread(tidyup);
		clt.setPriority(Thread.MAX_PRIORITY); //YES! Needed!!! Especially for slower machines to keep from running out of memory...
		clt.start();
		
	}

	private static void doStartClient(){
		
		connection_msg = "Starting a few things...";
		showLogo(false);
		Display.update();
			
		
		//SOUNDS!!! Woohoo!
		soundmangler = new SoundManager();
		soundmangler.master_volume = volume_temp;
		soundmangler.music_master_volume = music_volume_temp;

		//Make a place for chunks (client)		
		client_chunk_cache = new ChunkCache();  		//This caching mechanism is entirely different from...

		player = new Player(null);	
		player.model = new ModelHumanoid(); //Set renderer! (by hand)
		player.myname = playername;
		player.setPetName(playername);

		//Make a world!
		world = new World(client_chunk_cache, null);
		player.world = world; 

		//Make a world renderer
		wr = new WorldRenderer();
		//blocks!
		all_the_blocks = new Blocks();
		//items!
		all_the_items = new Items();
		//dimensions!
		all_the_dimensions = new Dimensions();
		//custom packets!
		all_the_custompackets = new CustomPackets();
		//make the crafting list
		all_the_recipies = new Crafting();
		all_the_deskrecipies = new DeskCrafting();
		//make the cooking list
		all_the_cooking = new Cooking();
		//make ore list holder
		all_the_ores = new Ores();
		//make spawn list holder
		all_the_spawns = new Spawnlist();
		//make command handler list
		all_the_handlers = new CommandHandlers();
		//particles!
		all_the_particles = new Particles();
		//make the shredding list
		all_the_shreds = new Shredding();

		//make a couple internal things we can't live without!
		all_the_entities = new Entities();
		blockitem = new EntityBlockItem(world);
		Entities.registerEntity(EntityBlockItem.class, blockitem.uniquename, new ModelEntityBlockItem());	//SPECIAL! Internal entity
		Entities.registerEntity(Player.class, "DangerZone:Player", new ModelHumanoid());						//SPECIAL! Internal entity
		Entities.registerEntity(EntityExp.class, "DangerZone:Experience", new ModelExp());					//SPECIAL! Internal entity

		escapegui = new PlayerEscapeGUI();
		inventorygui = new PlayerInventoryGUI();
		chatgui = new PlayerChatGUI();
		helpgui = new PlayerHelpGUI();
		commandgui = new PlayerCommandGUI();
		deathgui = new PlayerDeathGUI();
		chestgui = new PlayerChestGUI();
		coloringgui = new PlayerColoringGUI();
		craftinggui = new PlayerCraftingGUI();
		deskgui = new PlayerDeskGUI();
		dronegui = new PlayerDroneGUI();
		furnacegui = new PlayerFurnaceGUI();
		petnamegui = new PlayerPetNameGUI();
		shreddergui = new PlayerShredderGUI();
		signgui = new PlayerSignGUI();
		entitygui = new PlayerEntityGUI();
		tradegui = new PlayerTradeGUI();
		statsgui = new PlayerStatsGUI();
		todogui = new PlayerToDoGUI();

		entityManager = new EntityUpdateLoop();
		Thread it = new Thread(entityManager);	//Use to do gc and run updates to the entities...
		//it.setPriority(Thread.MAX_PRIORITY);
		it.start();	

		particleManager = new ParticleUpdateLoop();
		Thread pit = new Thread(particleManager);	//Use to do gc and run updates to the entities...
		//it.setPriority(Thread.MAX_PRIORITY);
		pit.start();	


		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //Let it all catch up a little...

		connection_msg = "Connecting to Server...";
		showLogo(false);
		Display.update();
		/*
		 * Connect to the server!
		 */
		player.getTexture(); //pre-load this here! It doesn't work in the server_connection thread!
		server_connection = new ServerConnection(player, true);
		Thread ct = new Thread(server_connection);	//Fire up server comms for the player
		ct.setPriority(Thread.MAX_PRIORITY); //Don't bog down the network!
		ct.start();	

		//we establish a partial connection with server to get world name and mods
		int i = 0;
		while(!server_connection.connectionInProgress && i < 30 && connection_error == 0){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} //Wait for the two to connect!
			i++;
		}
		if(i >= 30 || connection_error != 0){
			if(connection_error == 0)connection_msg = "Oops. Connection timed out.";
			showLogo(false);
			Display.update();
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
			System.exit(1);
		}
		
		DangerZone.worldname = server_connection.worldname;
		
		if(DangerZone.worldname == null){
			connection_msg = "Invalid Playername/Password, or Null world!";
			showLogo(false);
			Display.update();
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
			System.exit(1);
		}
						
		//Load up the default dimensions, creatures, blocks, items, etc!
		base = new DangerZoneBase();
		base.registerThings();
		
		connection_msg = "Loading Mods...";
		showLogo(false);
		Display.update();

		/*
		 * LOAD ALL OTHER MODS HERE!
		 */	
		modloader = new ModLoader();
		if(!modloader.loadmods(server_connection.modnames)){
			connection_msg = "Failed to load mods specified by server.";
			showLogo(false);
			Display.update();
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
			//System.out.printf("Failed to load mods specified by server.\n");
			System.exit(1);
		}

		base.postLoadProcessing();
		
		//re-sync with server HERE!!!
		//the connection is waiting for this flag to receive the re-synced IDs, which we couldn't do because we need to load mods first.
		server_connection.waitformodstoload = false;
		
		//Now we wait for the resyncs to happen. Then we're done!
		i = 0;
		while(server_connection.connected == 0 && i < 30  && connection_error == 0){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} //Wait for the two to connect!
			i++;
		}
		if(i >= 30 || connection_error != 0){
			if(connection_error == 0)connection_msg = "Oops. Connection timed out while loading mods.";
			showLogo(false);
			Display.update();
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
			System.exit(1);
		}

		connection_msg = "Loading textures...";
		showLogo(false);
		Display.update();
		
		//Now it's safe to load up ALL the block textures!	
		//We had to wait until server and client sync'ed up blockids!
		wr.loadBlockTextures();
		
		//Wait for at least the chunk I am in before continuing!
		//tell it what dimension we are in, or it won't send us the chunk we are waiting for.
		connection_msg = "Waiting for first chunk...";
		showLogo(false);
		Display.update();
		
		DangerZone.server_connection.sendPlayerEntityUpdate(player);
		
		Chunk c = null;
		i = 0;
		while(c == null){
			c = player.world.chunkcache.getDecoratedChunk(player.dimension, (int)player.posx, 100, (int)player.posz);
			DangerZone.server_connection.flushit(); //make sure any request gets sent!
			if(c != null)break;
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			i++;
			if(i > 150)break;
		}
		if(c == null){
			connection_msg = "Oops. Couldn't get first chunk.";
			showLogo(false);
			Display.update();
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
			System.exit(1);
		}
		c = null;
		
		connection_msg = "Almost ready...";
		showLogo(false);
		Display.update();
		
		//Lighting!!!
		lto = new LightingThread();
		Thread lt = new Thread(lto);
		lt.start();

		//What a MESS!!!
		tidyup = new CleanerThread();
		Thread clt = new Thread(tidyup);
		clt.setPriority(Thread.MAX_PRIORITY); //YES! Needed!!! Especially for slower machines to keep from running out of memory...
		clt.start();

		//set this so the wr knows what dimension we are in. it is a flag to clear all VBOs when changed!
		wr.lastplayerdimension = player.dimension;

		//And finally... grab the mouse pointer!
		Mouse.setGrabbed(true); //We be cat-like
		mouse_grabbed = true;

		//we have to do this to help smooth the mouse motion...
		Thread thisthread = Thread.currentThread();
		thisthread.setPriority(Thread.MAX_PRIORITY);
		
		connection_msg = null;

	}
	
	public static void show_selection(int i){
		
		player.sethotbarindex(i);
		InventoryContainer ic = player.getHotbar(i);
		if(ic != null && ic.count > 0){
			String hotstring = null;
			String[] hss = null;
			
			if(ic.bid != 0){
				hotstring = Blocks.getUniqueName(ic.bid);
			}
			if(ic.iid != 0){
				hotstring = Items.getUniqueName(ic.iid);
				int attackstrength = Items.getAttackStrength(ic.iid);
				if(attackstrength > 1){
					hotstring += String.format(" +%d", attackstrength);
				}					
			}
			if(hotstring != null){
				hss = hotstring.split(":");
				if(hss.length >= 2){
					hotmessagestring = hss[1];
					hotmessagetimer = 60;
				}
			}
		}else{
			hotmessagestring = null;
			hotmessagetimer = 0;
		}
	}
	
	public static void setMusicVolume(float newvol){
		SoundStore.get().setMusicVolume(newvol);
	}
	
	public static float getMusicVolume(){
		return SoundStore.get().getCurrentMusicVolume();
	}
	
	public static void pauseMusic(){
		SoundStore.get().pauseLoop();
	}
	public static void resumeMusic(){
		SoundStore.get().restartLoop();
	}


}
