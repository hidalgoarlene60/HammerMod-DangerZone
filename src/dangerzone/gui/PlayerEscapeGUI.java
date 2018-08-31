package dangerzone.gui;
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
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;

import dangerzone.DangerZone;
import dangerzone.TextureMapper;




public class PlayerEscapeGUI extends GuiInterface {
	List<MyButtonHandler> buttons;

	
	private class MyButtonHandler extends ButtonHandler {
		
		public int buttonid; //which button they hit
		
		MyButtonHandler(int xpos, int ypos, int bxsize, int bysize, Texture tx, String ot, int i){
			super(xpos, ypos, bxsize, bysize, tx, ot);
			buttonid = i;
		}
		
		public void leftclickhandler(){
			if(buttonid == 0){
				ImAllDone();
			}
			if(buttonid == 1){
				DangerZone.gameover = 1;
				ImAllDone();
			}
			if(buttonid == 2){
				DangerZone.renderdistance++;
				if(DangerZone.renderdistance > 24)DangerZone.renderdistance = 24;
				if(!DangerZone.bits64mode){
					if(DangerZone.renderdistance > 16)DangerZone.renderdistance = 16;
				}
				//lower max for when connected to real server
				if(!DangerZone.start_server){
					if(DangerZone.renderdistance > 16)DangerZone.renderdistance = 16;
				}
				DangerZone.initGL();
			}
			if(buttonid == 3){
				DangerZone.renderdistance--;
				if(DangerZone.renderdistance < 8)DangerZone.renderdistance = 8;
				DangerZone.initGL();
			}
			if(buttonid == 4){
				if(DangerZone.soundmangler.master_volume < 100)DangerZone.soundmangler.master_volume++;
			}
			if(buttonid == 5){
				if(DangerZone.soundmangler.master_volume > 0)DangerZone.soundmangler.master_volume--;
			}
			if(buttonid == 6){
				if(DangerZone.fieldOfView < 80)DangerZone.fieldOfView += 5;
				DangerZone.initGL();
			}
			if(buttonid == 7){
				if(DangerZone.fieldOfView > 30)DangerZone.fieldOfView -= 5;
				DangerZone.initGL();
			}
			if(buttonid == 8){
				if(DangerZone.mouseSensitivity < 9)DangerZone.mouseSensitivity++;
				DangerZone.initGL();
			}
			if(buttonid == 9){
				if(DangerZone.mouseSensitivity > -9)DangerZone.mouseSensitivity--;
				DangerZone.initGL();
			}
			if(buttonid == 10){
				if(DangerZone.mindrawlevel < 40)DangerZone.mindrawlevel++;
			}
			if(buttonid == 11){
				if(DangerZone.mindrawlevel > 0)DangerZone.mindrawlevel--;
			}
			if(buttonid == 12){
				DangerZone.all_sides = true;
				//if(!DangerZone.bits64mode){
				//	DangerZone.all_sides = false;
				//}
			}
			if(buttonid == 13){
				DangerZone.all_sides = false;
			}
			if(buttonid == 14){
				DangerZone.light_speed = true;
			}
			if(buttonid == 15){
				DangerZone.light_speed = false;
			}
			if(buttonid == 16){
				DangerZone.show_clouds = true;
			}
			if(buttonid == 17){
				DangerZone.show_clouds = false;
			}
			if(buttonid == 18){
				DangerZone.show_rain = true;
			}
			if(buttonid == 19){
				DangerZone.show_rain = false;
			}
			if(buttonid == 20){
				if(DangerZone.soundmangler.music_master_volume < 100)DangerZone.soundmangler.music_master_volume++;
				DangerZone.soundmangler.setMusicVolume();
			}
			if(buttonid == 21){
				if(DangerZone.soundmangler.music_master_volume > 0)DangerZone.soundmangler.music_master_volume--;
				DangerZone.soundmangler.setMusicVolume();
			}
			if(buttonid == 22){
				DangerZone.fullscreen = true;
			}
			if(buttonid == 23){
				DangerZone.fullscreen = false;
			}
			if(buttonid == 24){
				DangerZone.fog_enable = true;
				DangerZone.initGL();
			}
			if(buttonid == 25){
				DangerZone.fog_enable = false;
				DangerZone.initGL();
			}
			if(buttonid == 26){
				DangerZone.crafting_animation = true;
			}
			if(buttonid == 27){
				DangerZone.crafting_animation = false;
			}
			
		}
		public void rightclickhandler(){
			leftclickhandler();
		}
	}
	
	public PlayerEscapeGUI(){
		super();
	}
	
	/*
	 * Nice reasonably simple button event processing....
	 */
	public void process(){
		Texture backtexture = null;
		Texture exittexture = null;
		Texture buttontexture = null;
		int clickx, clicky;
		MyButtonHandler mb = null;
		MyButtonHandler fb = null;
		
		int starty = DangerZone.screen_height - 200;
		
		backtexture = TextureMapper.getTexture("res/menus/"+"back.png");
		exittexture = TextureMapper.getTexture("res/menus/"+"exit.png");
		buttontexture = TextureMapper.getTexture("res/menus/"+"button.png");

		//Build the button list... 
		buttons = new ArrayList<MyButtonHandler>();
		buttons.add(new MyButtonHandler(DangerZone.screen_width/2 - 150, starty, 100, 100, backtexture, null, 0));
		buttons.add(new MyButtonHandler(DangerZone.screen_width/2 + 50, starty, 100, 100, exittexture, null, 1));
		
		buttons.add(new MyButtonHandler(250, DangerZone.screen_height - 300, 75, 25, buttontexture, "More", 2));
		buttons.add(new MyButtonHandler(350, DangerZone.screen_height - 300, 75, 25, buttontexture, "Less", 3));
		
		buttons.add(new MyButtonHandler(250, DangerZone.screen_height - 350, 75, 25, buttontexture, "More", 6));
		buttons.add(new MyButtonHandler(350, DangerZone.screen_height - 350, 75, 25, buttontexture, "Less", 7));
		
		buttons.add(new MyButtonHandler(250, DangerZone.screen_height - 400, 75, 25, buttontexture, "More", 8));
		buttons.add(new MyButtonHandler(350, DangerZone.screen_height - 400, 75, 25, buttontexture, "Less", 9));
		
		buttons.add(new MyButtonHandler(250, DangerZone.screen_height - 450, 75, 25, buttontexture, "Higher", 10));
		buttons.add(new MyButtonHandler(350, DangerZone.screen_height - 450, 75, 25, buttontexture, "Lower", 11));
		
		buttons.add(new MyButtonHandler(250, DangerZone.screen_height - 500, 75, 25, buttontexture, "High", 12));
		buttons.add(new MyButtonHandler(350, DangerZone.screen_height - 500, 75, 25, buttontexture, "Low", 13));
		
		buttons.add(new MyButtonHandler(250, DangerZone.screen_height - 550, 75, 25, buttontexture, "High", 14));
		buttons.add(new MyButtonHandler(350, DangerZone.screen_height - 550, 75, 25, buttontexture, "Low", 15));
		
		buttons.add(new MyButtonHandler(250, DangerZone.screen_height - 600, 75, 25, buttontexture, "True", 16));
		buttons.add(new MyButtonHandler(350, DangerZone.screen_height - 600, 75, 25, buttontexture, "False", 17));
		
		buttons.add(new MyButtonHandler(250, DangerZone.screen_height - 650, 75, 25, buttontexture, "True", 18));
		buttons.add(new MyButtonHandler(350, DangerZone.screen_height - 650, 75, 25, buttontexture, "False", 19));
		
		
		buttons.add(new MyButtonHandler(DangerZone.screen_width/2 + 250, DangerZone.screen_height - 300, 75, 25, buttontexture, "More", 4));
		buttons.add(new MyButtonHandler(DangerZone.screen_width/2 + 350, DangerZone.screen_height - 300, 75, 25, buttontexture, "Less", 5));
		
		buttons.add(new MyButtonHandler(DangerZone.screen_width/2 + 250, DangerZone.screen_height - 350, 75, 25, buttontexture, "More", 20));
		buttons.add(new MyButtonHandler(DangerZone.screen_width/2 + 350, DangerZone.screen_height - 350, 75, 25, buttontexture, "Less", 21));
		
		buttons.add(new MyButtonHandler(DangerZone.screen_width/2 + 250, DangerZone.screen_height - 400, 75, 25, buttontexture, "True", 22));
		buttons.add(new MyButtonHandler(DangerZone.screen_width/2 + 350, DangerZone.screen_height - 400, 75, 25, buttontexture, "False", 23));
		
		buttons.add(new MyButtonHandler(DangerZone.screen_width/2 + 250, DangerZone.screen_height - 450, 75, 25, buttontexture, "True", 24));
		buttons.add(new MyButtonHandler(DangerZone.screen_width/2 + 350, DangerZone.screen_height - 450, 75, 25, buttontexture, "False", 25));
		
		buttons.add(new MyButtonHandler(DangerZone.screen_width/2 + 250, DangerZone.screen_height - 500, 75, 25, buttontexture, "True", 26));
		buttons.add(new MyButtonHandler(DangerZone.screen_width/2 + 350, DangerZone.screen_height - 500, 75, 25, buttontexture, "False", 27));
			
		//Draw buttons!
		Iterator<MyButtonHandler> bb = buttons.iterator();
		while(bb.hasNext()){
			bb.next().draw();
		}
		
		textAt( 50, DangerZone.screen_height - 300, "Render Distance:");
		textAt( 460, DangerZone.screen_height - 300, String.format("FPS: %d @ %d", DangerZone.wr.fps, DangerZone.renderdistance));
				
		textAt( 50, DangerZone.screen_height - 350, "Field of View:");
		textAt( 460, DangerZone.screen_height - 350, String.format("FOV: %d", DangerZone.fieldOfView));
		
		textAt( 50, DangerZone.screen_height - 400, "Mouse Sensitivity:");
		textAt( 460, DangerZone.screen_height - 400, String.format("MSV: %d", DangerZone.mouseSensitivity));
		
		textAt( 50, DangerZone.screen_height - 450, "Min Draw Level:");
		textAt( 460, DangerZone.screen_height - 450, String.format("MDL: %d", DangerZone.mindrawlevel));
		
		textAt( 50, DangerZone.screen_height - 500, "Graphics:");
		textAt( 460, DangerZone.screen_height - 500, String.format("GFX: %s", DangerZone.all_sides?"High":"Low"));
		
		textAt( 50, DangerZone.screen_height - 550, "Lighting:");
		textAt( 460, DangerZone.screen_height - 550, String.format("LGT: %s", DangerZone.light_speed?"High":"Low"));
		
		textAt( 50, DangerZone.screen_height - 600, "Show Clouds:");
		textAt( 460, DangerZone.screen_height - 600, String.format("SHC: %s", DangerZone.show_clouds?"True":"False"));
		
		textAt( 50, DangerZone.screen_height - 650, "Show Rain:");
		textAt( 460, DangerZone.screen_height - 650, String.format("SHR: %s", DangerZone.show_rain?"True":"False"));
		
		
		textAt( DangerZone.screen_width/2 + 50, DangerZone.screen_height - 300, "Sounds Volume:");
		textAt( DangerZone.screen_width/2 + 460, DangerZone.screen_height - 300, String.format("VOL: %d", DangerZone.soundmangler.master_volume));
		
		textAt( DangerZone.screen_width/2 + 50, DangerZone.screen_height - 350, "Music Volume:");
		textAt( DangerZone.screen_width/2 + 460, DangerZone.screen_height - 350, String.format("MVL: %d", DangerZone.soundmangler.music_master_volume));
		
		textAt( DangerZone.screen_width/2 + 50, DangerZone.screen_height - 400, "FullScreen:");
		textAt( DangerZone.screen_width/2 + 460, DangerZone.screen_height - 400, String.format("FSC: %s", DangerZone.fullscreen?"True":"False"));
		
		textAt( DangerZone.screen_width/2 + 50, DangerZone.screen_height - 450, "FogEnable:");
		textAt( DangerZone.screen_width/2 + 460, DangerZone.screen_height - 450, String.format("FOG: %s", DangerZone.fog_enable?"True":"False"));
		
		textAt( DangerZone.screen_width/2 + 50, DangerZone.screen_height - 500, "Craft Animation:");
		textAt( DangerZone.screen_width/2 + 460, DangerZone.screen_height - 500, String.format("CAN: %s", DangerZone.crafting_animation?"True":"False"));
		
		//Check for exit via keypad
		while(K_next()){
			if (K_getEventKey() == Keyboard.KEY_ESCAPE && K_isKeyDown(Keyboard.KEY_ESCAPE)){
				ImAllDone();
				return;
			}
		}
		
		//Check for mouse events!
		while(M_next()){
			clickx = M_getEventX();
			clicky = M_getEventY();
			if(M_getEventButtonState()){
				if(M_getEventButton() >= 0){ //clicked!
					
					//Find which "button" they clicked on
					bb = buttons.iterator();
					fb = null;
					while(bb.hasNext()){
						mb = bb.next();
						if(clickx >= mb.x && clickx <= mb.x+mb.xsize){
							if(clicky >= mb.y && clicky <= mb.y+mb.ysize){
								fb = mb;
								break;
							}
						}
					}
					// 0 = left, 1 = right, 2 = middle
					if(M_getEventButton() == 0){
						if(fb != null)fb.leftclickhandler();
					}
					if(M_getEventButton() == 1){
						if(fb != null)fb.rightclickhandler();
					}
				}
			}
		}

	}
	

	

}
