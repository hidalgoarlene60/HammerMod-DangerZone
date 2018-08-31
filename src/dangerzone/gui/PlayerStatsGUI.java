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




public class PlayerStatsGUI extends GuiInterface {
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
		}
		public void rightclickhandler(){
			leftclickhandler();
		}
	}
	
	public PlayerStatsGUI(){
		super();
	}
	
	/*
	 * Nice reasonably simple button event processing....
	 */
	public void process(){
		Texture backtexture = null;
		int clickx, clicky;
		MyButtonHandler mb = null;
		MyButtonHandler fb = null;
		
		int starty = DangerZone.screen_height - 150;
		
		backtexture = TextureMapper.getTexture("res/menus/"+"back.png");

		//Build the button list... 
		buttons = new ArrayList<MyButtonHandler>();
		buttons.add(new MyButtonHandler(DangerZone.screen_width/2 - 150, starty, 100, 100, backtexture, null, 0));

			
		//Draw buttons!
		Iterator<MyButtonHandler> bb = buttons.iterator();
		while(bb.hasNext()){
			bb.next().draw();
		}
		
		String playerstat = "some interesting statistics";

		playerstat = String.format("Kills:  %d", DangerZone.player.kills);
		textAt( 100, 580, playerstat);
		playerstat = String.format("Deaths: %d", DangerZone.player.deaths);
		textAt( 100, 540, playerstat);
		playerstat = String.format("Damage Taken: %f", DangerZone.player.damage_taken);
		textAt( 100, 500, playerstat);
		playerstat = String.format("Damage Dealt: %f", DangerZone.player.damage_dealt);
		textAt( 100, 460, playerstat);
		playerstat = String.format("Blocks Broken:   %d", DangerZone.player.blocks_broken);
		textAt( 100, 420,  playerstat);
		playerstat = String.format("Blocks Placed:   %d", DangerZone.player.blocks_placed);
		textAt( 100, 380, playerstat);
		playerstat = String.format("Blocks Colored:  %d", DangerZone.player.blocks_colored);
		textAt( 100, 340, playerstat);
		playerstat = String.format("Blocks Traveled: %d", DangerZone.player.traveled);
		textAt( 100, 300, playerstat);
		playerstat = String.format("Crafted: %d", DangerZone.player.crafted);
		textAt( 100, 260, playerstat);
		playerstat = String.format("Bought:  %d", DangerZone.player.bought);
		textAt( 100, 220, playerstat);
		playerstat = String.format("Sold:    %d", DangerZone.player.sold);
		textAt( 100, 180, playerstat);
		playerstat = String.format("Tools Broken: %d", DangerZone.player.broken);
		textAt( 100, 140, playerstat);
		playerstat = String.format("Morphs:    %d", DangerZone.player.morphs);
		textAt( 100, 100, playerstat);
		playerstat = String.format("Teleports: %d", DangerZone.player.teleports);
		textAt( 100, 60, playerstat);
		

		
		//column two!	
		playerstat = String.format("Eaten:     %d", DangerZone.player.eaten);
		textAt( 450, 580, playerstat);
		playerstat = String.format("Roach Stomps:  %d", DangerZone.player.roachstomps);
		textAt( 450, 540, playerstat);
		playerstat = String.format("Flights: %d", DangerZone.player.flights);
		textAt( 450, 500, playerstat);		
		playerstat = String.format("Hard Landings: %d", DangerZone.player.hard_landings);
		textAt( 450, 460, playerstat);
		playerstat = String.format("Spells Cast:   %d", DangerZone.player.spells);
		textAt( 450, 420, playerstat);
		
		//Check for exit via keypad
		while(K_next()){
			if (K_getEventKey() == Keyboard.KEY_ESCAPE && K_isKeyDown(Keyboard.KEY_ESCAPE)){
				ImAllDone();
				return;
			}
			if (K_getEventKey() == Keyboard.KEY_F4 && K_isKeyDown(Keyboard.KEY_F4)){
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
