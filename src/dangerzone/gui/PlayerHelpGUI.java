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




public class PlayerHelpGUI extends GuiInterface {
	List<MyButtonHandler> buttons;

	public int startat = 0;
	
	public String helpstrings[] = {
			"w, a, s, d - Move forward, left, back, right",
			"space - jump, or fly up",
			"shift (left) - fly down",
			"g - toggle game modes",
			"m - toggle survival difficulty",
			"y, Y, ctrl-y - change dimensions",
			"x, c, v - rotate block in X, Y, Z",
			"t - Talk/Chat for multiplayer",
			"h - Help",
			"\\ - show client/server entities nearby",
			"e - Inventory",
			"q - Drop held item/block",
			"F2 - Screen Capture",
			"F3 - Status overlay",
			"F4 - Player Statistics",
			"F5 - Narcissism toggle",
			"F6 - View underground ores",
			"F7 - Overlay screens on/off",
			"F11 - Achievements",
			"F12 - Pause/Showcase",
			"right-click on entity - mount/dismount",
			"jump on cockroach - change dimension",
			"esc - in-game graphics options",
			"mouse wheel - select hotbar item",
			"alt (left) - ready HARM spell",
			"control (left) - ready HEAL spell",
			"alt (left) + control (left) - ready DESTROY spell",
			"/ - COMMANDS -------",
			" time set (day|night|dawn|dusk|0-359)",
			" tp (playername | x y z | playername1 playername2 | playername x y z)",
			" stop",
			" kill (all|hostile|pets|volcanoes|playername|items)",
			" kick (playername)",
			" op (playername)",
			" deop (playername)",			
			" givepriv (playername) (gamemode|op|teleport|kill|weather|time|shutdown|nofire|chat)",
			" takepriv (playername) (gamemode|op|teleport|kill|weather|time|shutdown|nofire|chat)",
			" ban (playername)",
			" unban (playername)",
			" playnicely (true|false)",
			" firedamage (true|false)",
			" petprotection (true|false)",
			" defaultprivs (list of: gamemode|op|teleport|kill|weather|time|shutdown|nofire|chat)",
			" maxplayers (number)",
			" nofire",
			" items (search string)",
			" blocks (search string)",
			" give (playername) (item|block) (id) (count)",
			" clearinventory (playername)",
			" validateplayers (true|false)",
			" allowanonymous (true|false)",
			" cavegeneration (true|false)",
			" freezeworld (true|false)",
			" who",
			" showprivs (playername)",
			" private_server (true|false)",
			" whitelist (playername)",
			" unwhitelist (playername)",
			" weather clear",
			" whereis (playername)",
			" chunkowner (playername(s)|null)",
			" rebuild (chunk|area)",
			" spawn - go back to spawn",
			" home - go back to wherever you set home",
			" sethome - set home location",
			" ",
			" ",
	};
	
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
			if(buttonid == 7){
				startat += 10;
				if(startat >= helpstrings.length)startat = 0;
			}
			if(buttonid == 8){
				startat -= 10;
				if(startat < 0)startat = 0;
			}

		}
		public void rightclickhandler(){
			leftclickhandler();
		}
	}
	
	public PlayerHelpGUI(){
		super();
	}
	
	/*
	 * Nice reasonably simple button event processing....
	 */
	public void process(){
		Texture backtexture = null;
		Texture buttontexture = null;
		int clickx, clicky;
		MyButtonHandler mb = null;
		MyButtonHandler fb = null;
		
		int starty = DangerZone.screen_height - 200;
		
		backtexture = TextureMapper.getTexture("res/menus/"+"back.png");
		buttontexture = TextureMapper.getTexture("res/menus/"+"button.png");

		//Build the button list... 
		buttons = new ArrayList<MyButtonHandler>();
		buttons.add(new MyButtonHandler(DangerZone.screen_width/2 - 150, starty, 100, 100, backtexture, null, 0));

		buttons.add(new MyButtonHandler(DangerZone.screen_width/2 - 150, starty - 100, 75, 25, buttontexture, "Next", 7));
		buttons.add(new MyButtonHandler(DangerZone.screen_width/2 - 150, starty - 125, 75, 25, buttontexture, "Prev", 8)); 
			
		//Draw buttons!
		Iterator<MyButtonHandler> bb = buttons.iterator();
		while(bb.hasNext()){
			bb.next().draw();
		}
		
		textAt( 150, 60,  helpstrings[(startat+9)%helpstrings.length]);
		textAt( 150, 100, helpstrings[(startat+8)%helpstrings.length]);
		textAt( 150, 140, helpstrings[(startat+7)%helpstrings.length]);
		textAt( 150, 180, helpstrings[(startat+6)%helpstrings.length]);
		textAt( 150, 220, helpstrings[(startat+5)%helpstrings.length]);
		textAt( 150, 260, helpstrings[(startat+4)%helpstrings.length]);
		textAt( 150, 300, helpstrings[(startat+3)%helpstrings.length]);
		textAt( 150, 340, helpstrings[(startat+2)%helpstrings.length]);
		textAt( 150, 380, helpstrings[(startat+1)%helpstrings.length]);
		textAt( 150, 420, helpstrings[(startat  )%helpstrings.length]);

		
		//Check for exit via keypad
		while(K_next()){
			if (K_getEventKey() == Keyboard.KEY_ESCAPE && K_isKeyDown(Keyboard.KEY_ESCAPE)){
				ImAllDone();
				return;
			}
			if (K_getEventKey() == Keyboard.KEY_H && K_isKeyDown(Keyboard.KEY_H)){
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
