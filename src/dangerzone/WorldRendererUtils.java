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


import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;

import dangerzone.blocks.Blocks;
import dangerzone.entities.Entities;
import dangerzone.entities.Entity;
import dangerzone.items.Item;
import dangerzone.items.Items;



public class WorldRendererUtils {
	
	private static boolean square_compiled = false;
	private static int squareid;
	private static final int blockrenderwidth = 16;
	public static Texture hotbarbackground = null;
	public static Texture firetexture = null;
	private static boolean fire_compiled = false;
	private static int fire_list1, fire_list2, fire_list3, fire_list4;
	private static int fire_list5, fire_list6, fire_list7, fire_list8;
	private static float sunspin = 0;
	private static int blink = 0;
	private static Entity curr_entity = null;
	private static int curr_entity_count = 0; //60 frames per second, so 60 == 1 second!
	private static float pitchdir = 0.3f;
	private static float ticker = 0;
	private static float curr_yaw = 0;
	private static float curr_pitch = 0;
	
	public static void drawShowcaseMonster(){
		while(curr_entity == null){
			curr_entity = Entities.random_entity();
			if(curr_entity != null){
				curr_entity.init();
				curr_yaw = DangerZone.rand.nextInt(360);
				curr_pitch = DangerZone.rand.nextInt(60)-30;
				if(DangerZone.rand.nextInt(50)==1)curr_entity = DangerZone.player; //show the player too!
				//must have model and texture!!!
				if(curr_entity.model == null)curr_entity = null;
				if(curr_entity != null){
					if(curr_entity.getTexture() == null)curr_entity = null;
				}
				if(curr_entity != null && DangerZone.world != null && curr_entity != DangerZone.player){
					curr_entity.world = DangerZone.world; //Don't mess with player world, just because...
					DangerZone.world.playSound(curr_entity.getLivingSound(), DangerZone.player.dimension, DangerZone.player.posx, DangerZone.player.posy, DangerZone.player.posz, 0.5f, 1);
				}
				
			}
			curr_entity_count = 600;
		}
		ticker++;
		if(ticker > 1000000)ticker = 0;
		
		curr_yaw += 0.75f;
		curr_pitch += pitchdir;
		if(curr_pitch > 30)pitchdir = -0.25f;
		if(curr_pitch < -30)pitchdir = 0.25f;
		
				
		GL11.glPushMatrix();  //SAVE!
		//put the entitiy where it goes.

		GL11.glTranslated(0, ((DangerZone.player.posy-curr_entity.getHeight()/2)*blockrenderwidth), 
							-((2.5f + curr_entity.getWidth()*2.5f + curr_entity.getHeight()*1.75f)*blockrenderwidth));
		//Rotate it around						
		if(curr_yaw != 0)GL11.glRotatef(curr_yaw, 0.0f, 1.0f, 0.0f); // Rotate The Entity On X, Y & Z
		if(curr_pitch != 0)GL11.glRotatef(curr_pitch, 1.0f, 0.0f, 0.0f); // Rotate The Entity On X, Y & Z

		GL11.glColor3f(0.95f, 0.95f, 0.95f);
		//Now all it has to do is draw itself.

		
		ModelBase model = curr_entity.model;
		loadtexture(curr_entity.getTexture()); 		//Get a texture for it	
		model.doScale(curr_entity);	
		model.render(curr_entity, ticker, 0.11f, 0, 0, 0, 1f);	//Draw!	
	
		
		GL11.glPopMatrix();	//RESTORE!!
		
		
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
		
		//Put up the entity name!
		textAt(DangerZone.font, 50, 150, curr_entity.uniquename);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		curr_entity_count--;
		if(curr_entity_count <= 0)curr_entity = null;
	}
	
	public static void loadtexture(Texture lt){
		if(lt == null)return;
		TextureImpl.unbind(); //force reset
		lt.bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
	}
	
	public static void textAt(TrueTypeFont ff, float xpos, float ypos, String text){
		GL11.glPushMatrix();
		GL11.glTranslatef(xpos, ypos, 0f); 
		GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f); // Don't ask me why, but the text is upside down! Flip it!		
		ff.drawString(0, 0, text, Color.lightGray);		
		GL11.glPopMatrix();
	}
	
	//Mostly for items, which are almost never solid
	public static void drawSquare(){
		
		if(!square_compiled){
			squareid = DangerZone.wr.getNextRenderID();
			GL11.glNewList(squareid, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);	
			GL11.glTexCoord2f(1,0);
			GL11.glVertex3f(blockrenderwidth/2, blockrenderwidth/2, 0); // Flat square
			GL11.glTexCoord2f(0,0);
			GL11.glVertex3f(-blockrenderwidth/2, blockrenderwidth/2, 0); // 
			GL11.glTexCoord2f(0,1);
			GL11.glVertex3f(-blockrenderwidth/2, -blockrenderwidth/2, 0); // 
			GL11.glTexCoord2f(1,1);
			GL11.glVertex3f(blockrenderwidth/2, -blockrenderwidth/2, 0); // 
			GL11.glEnd(); // Done Drawing The Quad
			GL11.glEndList();
			square_compiled = true;
		}
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glCallList(squareid);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public static float getLightMapValue(World w, int d, int x, int y, int z){
		short lightmap[] = null;
		lightmap = w.chunkcache.getDecoratedChunkLightmap(d, x, y, z);
		if(lightmap == null)return 0.0f;
		float tmp = (float)lightmap[((x&0x0f)*16) + (z&0x0f)];
		return tmp/1000f;
	}
	
	public static float getTotalLightAt(World w, int d, int x, int y, int z){
		short lightmap[] = null;
		float lt = getBrightnessForLevel(d, y);
		lightmap = w.chunkcache.getDecoratedChunkLightmap(d, x, y, z);
		if(lightmap == null)return lt;
		float tmp = (float)lightmap[((x&0x0f)*16) + (z&0x0f)];
		return (tmp/1000f) + lt;
	}
	
	public static float getBrightnessForLevel(int d, int yp){
		float tod = DangerZone.world.timetimer % DangerZone.world.lengthOfDay;
		float fsin = (float) Math.sin(Math.toRadians((tod/(float)DangerZone.world.lengthOfDay)*360f));
		fsin *= 1.75f;
		if(fsin > 1)fsin = 1;
		if(fsin < -1)fsin = -1;
		float f = 0.50f + 0.45f*fsin;
		if(yp < 50 && Dimensions.DimensionArray[d].fade_light_level){
			if(yp < 0)yp = 0;
			float f2 = (float)yp/(50.0f + (50f-yp));
			f *= f2;
		}
		return f;
	}
	
	//convenience box...
	public static void drawRectangleWithTexture(Texture t, float xpos, float ypos, float xsize, float ysize){
		DangerZone.wr.forceloadtexture(t);
		float w = t.getWidth();
		float h = t.getHeight();
		GL11.glPushMatrix(); //save position
		GL11.glTranslatef(xpos, ypos, 0f); 
		GL11.glScalef(xsize/t.getWidth(), ysize/t.getHeight(), 1f);
		GL11.glBegin(GL11.GL_QUADS);	
		GL11.glTexCoord2f(w,0);
		GL11.glVertex3f(1, 1, 0); // Top Right
		GL11.glTexCoord2f(0,0);
		GL11.glVertex3f(0, 1, 0); // Top Left
		GL11.glTexCoord2f(0,h);
		GL11.glVertex3f(0, 0, 0); // Bottom left
		GL11.glTexCoord2f(w,h);
		GL11.glVertex3f(1, 0, 0); // Bottom right		
		GL11.glEnd(); // Done Drawing The Quad
		GL11.glPopMatrix();
	}
	

	private static void drawhotbarbackground(){
		if(hotbarbackground == null){
			hotbarbackground = TextureMapper.getTexture("res/menus/bkg1.png");
		}
		//GL11.glTranslatef(0, 0, 0f);
		GL11.glScalef(3.25f, 3.25f, 3.25f);					
		DangerZone.wr.loadtexture(hotbarbackground);
		drawSquare();
		GL11.glScalef(1.0f/3.25f, 1.0f/3.25f, 1.0f/3.25f);
		//GL11.glTranslatef(-8, 8, 0f);
	}
	
	public static void drawHotbar(World world){
		
		if(DangerZone.current_gui != null)return;
		InventoryContainer ic = null;
		DangerZone.wr.setBrightnessNonFocus();		
		GL11.glPushMatrix();
		GL11.glTranslatef((DangerZone.screen_width/2) - (30*10), 30, 0f);
		for(int i=0;i<10;i++){
			WorldRenderer.last_texture = -1; //force reload after text!
			if(i == DangerZone.player.gethotbarindex())DangerZone.wr.setBrightnessFocus();
			drawhotbarbackground();
			
			ic = DangerZone.player.getHotbar(i);
			if(ic != null){			
				if(ic.bid != 0){
					GL11.glScalef(2.0f, 2.0f, 2.0f);
					if(Blocks.showTop(ic.bid)){
						DangerZone.wr.drawTexturedSquare(0, Blocks.isSolidForRender(ic.bid), ic.bid);
					}else{
						DangerZone.wr.drawTexturedCube(0x08, Blocks.isSolidForRender(ic.bid), ic.bid, 0, false);
					}
					GL11.glScalef(0.5f, 0.5f, 0.5f);
				}
				if(ic.iid != 0){
					GL11.glScalef(2.0f, 2.0f, 2.0f);					
					DangerZone.wr.loadtexture(Items.getTexture(ic.iid));
					WorldRendererUtils.drawSquare();
					GL11.glScalef(0.5f, 0.5f, 0.5f);
					//Now draw the damage bar!
					if(ic.count == 1){
						if(ic.currentuses > 0){
							if(ic.getMaxStack() == 1){
								Item it = ic.getItem();
								if(it != null){
									int md = it.maxuses;
									if(md > 0){
										float pu = (float)ic.currentuses/(float)md;										
										if(pu > 1)pu = 1;
										if(pu < 0)pu = 0;
										float xs = 32 * (1f - pu);
										if(xs < 1)xs = 1;
										GL11.glPushMatrix(); //save position
										GL11.glDisable(GL11.GL_TEXTURE_2D);
										GL11.glTranslatef(-16, -18, 0f); //just a little lower...
										GL11.glScalef(1f, 1f, 1f);
										GL11.glBegin(GL11.GL_QUADS);	
										GL11.glColor3f(pu, 1f-pu, 0);
										GL11.glVertex3f(xs, 2, 0); // Top Right
										GL11.glColor3f(pu, 1f-pu, 0);
										GL11.glVertex3f(0, 2, 0); // Top Left
										GL11.glColor3f(pu, 1f-pu, 0);
										GL11.glVertex3f(0, 0, 0); // Bottom left
										GL11.glColor3f(pu, 1f-pu, 0);
										GL11.glVertex3f(xs, 0, 0); // Bottom right		
										GL11.glEnd(); // Done Drawing The Quad
										GL11.glEnable(GL11.GL_TEXTURE_2D);
										GL11.glPopMatrix();
									}
								}
							}
						}
					}
				}
				//Display the count
				if(ic.count > 1){
					if(ic.count > 9){ //two chars
						WorldRendererUtils.textAt(DangerZone.wr.font, -12, 10, String.format("%d", ic.count));
					}else{ //one char
						WorldRendererUtils.textAt(DangerZone.wr.font, -6, 10, String.format("%d", ic.count));
					}
				}
				//Always reset brightness after text!
				DangerZone.wr.setBrightnessNonFocus();
			}
			//Always reset brightness after text!
			DangerZone.wr.setBrightnessNonFocus();
			
			GL11.glTranslatef(60, 0, 0f);
		}
		GL11.glPopMatrix();
	}
	
	public static void drawHealth(World world){		
		if(DangerZone.current_gui != null)return;
		if(WorldRenderer.heart_texture == null || WorldRenderer.unheart_texture == null)return;
		DangerZone.wr.setBrightnessNonFocus();		
		GL11.glPushMatrix();
		GL11.glTranslatef(((DangerZone.screen_width/2) - (30*10)) - 14, 65, 0f);
		float h = DangerZone.player.getHealth()/DangerZone.player.getMaxHealth();
		h *= 20;
		for(int i=0;i<20;i++){
			if(h > (float)i+0.5f){
				DangerZone.wr.loadtexture(WorldRenderer.heart_texture);
			}else{
				DangerZone.wr.loadtexture(WorldRenderer.unheart_texture);
			}
			WorldRendererUtils.drawSquare();
			GL11.glTranslatef(30, 0, 0f);
		}
		GL11.glPopMatrix();

	}
	
	public static void drawHunger(World world){		
		if(DangerZone.current_gui != null)return;
		if(WorldRenderer.hungerfull_texture == null || WorldRenderer.hungerempty_texture == null)return;
		DangerZone.wr.setBrightnessNonFocus();		
		GL11.glPushMatrix();
		GL11.glTranslatef(((DangerZone.screen_width/2) - (30*9.5f)) - 14, 75, 0f);
		float h = DangerZone.player.getHunger()/DangerZone.player.getMaxHunger();
		h *= 19;
		for(int i=0;i<19;i++){
			if(h > (float)i+0.5f){
				DangerZone.wr.loadtexture(WorldRenderer.hungerfull_texture);
			}else{
				DangerZone.wr.loadtexture(WorldRenderer.hungerempty_texture);
			}
			WorldRendererUtils.drawSquare();
			GL11.glTranslatef(30, 0, 0f);
		}
		GL11.glPopMatrix();

	}
	
	public static void drawMagic(World world){		
		if(DangerZone.current_gui != null)return;
		if(DangerZone.player.getMaxMagic() < 1f)return;
		blink++;
		if(WorldRenderer.magic_texture == null || WorldRenderer.magicempty_texture == null)return;
		DangerZone.wr.setBrightnessNonFocus();		
		GL11.glPushMatrix();
		GL11.glTranslatef(((DangerZone.screen_width/2) - (30*9f)) - 14, 85, 0f);
		float h = DangerZone.player.getMagic()/DangerZone.player.getMaxMagic();
		h *= 18;
		for(int i=0;i<18;i++){
			if(h > (float)i+0.5f){
				DangerZone.wr.loadtexture(WorldRenderer.magic_texture);							
			}else{
				DangerZone.wr.loadtexture(WorldRenderer.magicempty_texture);		
			}			
			if(DangerZone.magic_power != 0){
				if((blink & 0x07) > 3 || (float)i+0.5f > DangerZone.magic_power*18){
					WorldRendererUtils.drawSquare();
				}
			}else{
				WorldRendererUtils.drawSquare();
			}
			GL11.glTranslatef(30, 0, 0f);
		}
		//String mt = null;
		//if(DangerZone.magic_type == 1)mt = "Heal";
		//if(DangerZone.magic_type == 2)mt = "Harm";
		//if(DangerZone.magic_type == 3)mt = "Destroy";
		//if(mt != null)WorldRendererUtils.textAt(DangerZone.wr.font, 10, 15, mt);
		GL11.glPopMatrix();

	}
	
	public static void drawAir(World world){		
		if(DangerZone.current_gui != null)return;
		if(WorldRenderer.bubble_texture == null)return;
		if(!Blocks.isLiquid(world.getblock(DangerZone.player.dimension, (int)DangerZone.player.posx, (int)(DangerZone.player.posy+DangerZone.player.eyeheight),(int)DangerZone.player.posz)))return;	
		DangerZone.wr.setBrightnessNonFocus();		
		GL11.glPushMatrix();
		GL11.glTranslatef(((DangerZone.screen_width/2) - (30*8.5f)) - 14, 95, 0f);
		float h = DangerZone.player.getAir()/DangerZone.player.getMaxAir();
		h *= 17;
		for(int i=0;i<17;i++){
			if(h > (float)i+0.5f){
				DangerZone.wr.loadtexture(WorldRenderer.bubble_texture);			
				WorldRendererUtils.drawSquare();
			}
			GL11.glTranslatef(30, 0, 0f);
		}
		GL11.glPopMatrix();

	}
	
	
	
	public static void drawSunAndMoon(World world, boolean spinit){

		double sposx, sposy, sposz;
		double dist = (254.0f*DangerZone.renderdistance);
		float scale = (float)DangerZone.renderdistance/24f;
		int tod = world.getTimeOfDay();
		int lod = world.getLengthOfDay();
		boolean moon = false;
		if(tod > lod/2){
			tod -= lod/2;
			moon = true;
		}
		sposx = (DangerZone.player.posx%16f)*16f;
		sposy =  (dist * Math.sin(Math.toRadians(((float)tod/(float)lod)*360f)));
		sposz =  (dist * Math.cos(Math.toRadians(((float)tod/(float)lod)*360f))) + (DangerZone.player.posz%16f)*16f;
		
		
		GL11.glPushMatrix();
		GL11.glTranslatef((float)sposx, (float)(sposy+DangerZone.player.posy*16), (float)sposz);
		GL11.glRotatef(360f - ((float)tod/(float)lod)*360f, 1, 0, 0); 
		
		DangerZone.wr.setBrightness(1.0f);
		
		
		
		if(!moon){
			DangerZone.wr.loadtexture(WorldRenderer.sun_texture);
		}else{
			DangerZone.wr.loadtexture(WorldRenderer.moon_texture);
		}

		if(DangerZone.fog_enable)GL11.glDisable(GL11.GL_FOG);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glScalef(20f*scale, 20f*scale, 20f*scale);
		if(!moon)GL11.glScalef(2, 2, 2);
		
		if(spinit && !moon){
			sunspin += 0.15f;
			GL11.glRotatef(sunspin%360, 0, 0, 1);			
		}
		WorldRendererUtils.drawSquare();
		
		GL11.glScalef(.05f/scale, .05f/scale, .05f/scale);
		if(!moon)GL11.glScalef(0.5f, 0.5f, 0.5f);
		
		GL11.glDisable(GL11.GL_BLEND);
		if(DangerZone.fog_enable)GL11.glEnable(GL11.GL_FOG);
		DangerZone.wr.setBrightness();
		GL11.glPopMatrix();
	}
	
	//Renders the fire of an entity that is on fire...
	public static void drawEntityOnFire(Entity ent){
		if(firetexture == null){
			firetexture = TextureMapper.getTexture("res/misc/"+ "fire_side.png");
		}
		if(!fire_compiled){
			float offinc = 0;
			fire_list1 = DangerZone.wr.getNextRenderID();
			GL11.glNewList(fire_list1, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(1,offinc);
			GL11.glVertex3f(9, 9, 0);
			GL11.glTexCoord2f(0,offinc);
			GL11.glVertex3f(-9, 9, 0);
			GL11.glTexCoord2f(0,offinc + 0.125f);
			GL11.glVertex3f(-9, -9, 0); 
			GL11.glTexCoord2f(1,offinc + 0.125f);
			GL11.glVertex3f(9, -9, 0); 
			GL11.glEnd(); // Done Drawing The Quad
			GL11.glEndList();
			
			offinc += 0.125f;
			fire_list2 = DangerZone.wr.getNextRenderID();
			GL11.glNewList(fire_list2, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(1,offinc);
			GL11.glVertex3f(9, 9, 0);
			GL11.glTexCoord2f(0,offinc);
			GL11.glVertex3f(-9, 9, 0);
			GL11.glTexCoord2f(0,offinc + 0.125f);
			GL11.glVertex3f(-9, -9, 0); 
			GL11.glTexCoord2f(1,offinc + 0.125f);
			GL11.glVertex3f(9, -9, 0); 
			GL11.glEnd(); // Done Drawing The Quad
			GL11.glEndList();
			
			offinc += 0.125f;
			fire_list3 = DangerZone.wr.getNextRenderID();
			GL11.glNewList(fire_list3, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(1,offinc);
			GL11.glVertex3f(9, 9, 0);
			GL11.glTexCoord2f(0,offinc);
			GL11.glVertex3f(-9, 9, 0);
			GL11.glTexCoord2f(0,offinc + 0.125f);
			GL11.glVertex3f(-9, -9, 0); 
			GL11.glTexCoord2f(1,offinc + 0.125f);
			GL11.glVertex3f(9, -9, 0); 
			GL11.glEnd(); // Done Drawing The Quad
			GL11.glEndList();
			
			offinc += 0.125f;
			fire_list4 = DangerZone.wr.getNextRenderID();
			GL11.glNewList(fire_list4, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(1,offinc);
			GL11.glVertex3f(9, 9, 0);
			GL11.glTexCoord2f(0,offinc);
			GL11.glVertex3f(-9, 9, 0);
			GL11.glTexCoord2f(0,offinc + 0.125f);
			GL11.glVertex3f(-9, -9, 0); 
			GL11.glTexCoord2f(1,offinc + 0.125f);
			GL11.glVertex3f(9, -9, 0); 
			GL11.glEnd(); // Done Drawing The Quad
			GL11.glEndList();
			
			offinc += 0.125f;
			fire_list5 = DangerZone.wr.getNextRenderID();
			GL11.glNewList(fire_list5, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(1,offinc);
			GL11.glVertex3f(9, 9, 0);
			GL11.glTexCoord2f(0,offinc);
			GL11.glVertex3f(-9, 9, 0);
			GL11.glTexCoord2f(0,offinc + 0.125f);
			GL11.glVertex3f(-9, -9, 0); 
			GL11.glTexCoord2f(1,offinc + 0.125f);
			GL11.glVertex3f(9, -9, 0); 
			GL11.glEnd(); // Done Drawing The Quad
			GL11.glEndList();
			
			offinc += 0.125f;
			fire_list6 = DangerZone.wr.getNextRenderID();
			GL11.glNewList(fire_list6, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(1,offinc);
			GL11.glVertex3f(9, 9, 0);
			GL11.glTexCoord2f(0,offinc);
			GL11.glVertex3f(-9, 9, 0);
			GL11.glTexCoord2f(0,offinc + 0.125f);
			GL11.glVertex3f(-9, -9, 0); 
			GL11.glTexCoord2f(1,offinc + 0.125f);
			GL11.glVertex3f(9, -9, 0); 
			GL11.glEnd(); // Done Drawing The Quad
			GL11.glEndList();
			
			offinc += 0.125f;
			fire_list7 = DangerZone.wr.getNextRenderID();
			GL11.glNewList(fire_list7, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(1,offinc);
			GL11.glVertex3f(9, 9, 0);
			GL11.glTexCoord2f(0,offinc);
			GL11.glVertex3f(-9, 9, 0);
			GL11.glTexCoord2f(0,offinc + 0.125f);
			GL11.glVertex3f(-9, -9, 0); 
			GL11.glTexCoord2f(1,offinc + 0.125f);
			GL11.glVertex3f(9, -9, 0); 
			GL11.glEnd(); // Done Drawing The Quad
			GL11.glEndList();
			
			offinc += 0.125f;
			fire_list8 = DangerZone.wr.getNextRenderID();
			GL11.glNewList(fire_list8, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(1,offinc);
			GL11.glVertex3f(9, 9, 0);
			GL11.glTexCoord2f(0,offinc);
			GL11.glVertex3f(-9, 9, 0);
			GL11.glTexCoord2f(0,offinc + 0.125f);
			GL11.glVertex3f(-9, -9, 0); 
			GL11.glTexCoord2f(1,offinc + 0.125f);
			GL11.glVertex3f(9, -9, 0); 
			GL11.glEnd(); // Done Drawing The Quad
			GL11.glEndList();

			fire_compiled = true;
		}
		
		DangerZone.wr.loadtexture(firetexture);
		
		int which = DangerZone.rand.nextInt(8);
		GL11.glColor4f(1f, 1f, 1f, 0.80f);		
		//use width and height because entity was already scaled elsewhere!
		GL11.glScalef(ent.getWidth(), ent.getHeight()*1.25f, 1);
		GL11.glTranslatef(0, 8, 0); //go up a little...
		
		switch(which){
		case 0:
			GL11.glCallList(fire_list1); //draw
			break;
		case 1:
			GL11.glCallList(fire_list2); //draw
			break;
		case 2:
			GL11.glCallList(fire_list3); //draw
			break;
		case 3:
			GL11.glCallList(fire_list4); //draw
			break;
		case 4:
			GL11.glCallList(fire_list5); //draw
			break;
		case 5:
			GL11.glCallList(fire_list6); //draw
			break;
		case 6:
			GL11.glCallList(fire_list7); //draw
			break;
		case 7:
			GL11.glCallList(fire_list8); //draw
			break;
		default:
		}
		GL11.glColor4f(1f, 1f, 1f, 1.0f);	
		
	}

}
