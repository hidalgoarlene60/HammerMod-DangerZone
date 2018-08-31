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
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import dangerzone.DangerZone;
import dangerzone.TextureMapper;




public class PlayerDeathGUI extends GuiInterface {
	List<MyButtonHandler> buttons;

	int deadcounter = 0;
	
	private class MyButtonHandler extends ButtonHandler {
		
		public int buttonid; //which button they hit
		
		MyButtonHandler(int xpos, int ypos, int bxsize, int bysize, Texture tx, String ot, int i){
			super(xpos, ypos, bxsize, bysize, tx, ot);
			buttonid = i;
		}
		
		public void leftclickhandler(){
			if(buttonid == 0){
				DangerZone.player.server_connection.sendRespawn();				
				int which = DangerZone.rand.nextInt(3);
				if(which == 0)DangerZone.player.world.playSoundCloseClient("DangerZone:teleport1", DangerZone.player.dimension, DangerZone.player.posx, DangerZone.player.posy, DangerZone.player.posz, 1, 1);
				if(which == 1)DangerZone.player.world.playSoundCloseClient("DangerZone:teleport2", DangerZone.player.dimension, DangerZone.player.posx, DangerZone.player.posy, DangerZone.player.posz, 1, 1);
				if(which == 2)DangerZone.player.world.playSoundCloseClient("DangerZone:teleport3", DangerZone.player.dimension, DangerZone.player.posx, DangerZone.player.posy, DangerZone.player.posz, 1, 1);
				ImAllDone();

			}
			if(buttonid == 1){
				DangerZone.gameover = 1;
				DangerZone.player.setHealth(-1);
				ImAllDone();
			}			
		}
		public void rightclickhandler(){
			leftclickhandler();
		}
	}
	
	public PlayerDeathGUI(){
		super();



	}
	
	/*
	 * Nice reasonably simple button event processing....
	 */
	public void process(){

		int clickx, clicky;
		MyButtonHandler mb = null;
		MyButtonHandler fb = null;
		String respawn = "ReSpawn";
		Texture respawntexture = null;
		Texture exittexture = null;
		
		int starty = DangerZone.screen_height - 400;
		respawntexture = TextureMapper.getTexture("res/menus/"+"respawnbutton.png");
		exittexture = TextureMapper.getTexture("res/menus/"+"exit.png");
		
		//Player died??????
		if(deadcounter < DangerZone.wr.fps*4){
			String oops = "Oops. You died...";
			deadcounter++;

			GL11.glColor3f(1.0f, 0.2f, 0.2f); //red!
			drawRectangleWithTexture(DangerZone.logotexture, (DangerZone.screen_width/2)-300, (DangerZone.screen_height/2)-300, 600, 600);
			GL11.glColor3f(1.0f, 1.0f, 1.0f); //brighten things up a bit!			
			textAt((DangerZone.screen_width/2)-(oops.length()*5), DangerZone.screen_height/2, oops); 
			
			while(K_next()){};
			while(M_next()){};
			return;
	
		}	

		//Build the button list... 
		buttons = new ArrayList<MyButtonHandler>();
		buttons.add(new MyButtonHandler(DangerZone.screen_width/2 - 150, starty, 300, 100, respawntexture, null, 0));
		buttons.add(new MyButtonHandler(DangerZone.screen_width/2 - 50, starty - 150, 100, 100, exittexture, null, 1));

			
		//Draw buttons!
		Iterator<MyButtonHandler> bb = buttons.iterator();
		while(bb.hasNext()){
			bb.next().draw();
		}
		
		textAt(DangerZone.screen_width/2-(respawn.length()*7), starty + 30, respawn); 

		
		
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
	
	public void ImAllDone(){
		DangerZone.doDeathGUI = false;
		deadcounter = 0;
		super.ImAllDone();
	}
	

	

}
