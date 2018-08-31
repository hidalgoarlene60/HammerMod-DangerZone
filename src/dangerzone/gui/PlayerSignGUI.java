package dangerzone.gui;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.opengl.Texture;

import dangerzone.DangerZone;
import dangerzone.TextureMapper;
import dangerzone.entities.Entity;




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
public class PlayerSignGUI extends GuiInterface {

	public Entity pet = null;
	String currstring1 = null;
	String currstring2 = null;
	String currstring3 = null;
	int curline = 1;
	int rotate = 0;

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
				if(pet.getBID() == 0){
					DangerZone.server_connection.sendVarIntMessage(pet.entityID, 0, 1);
				}else{
					DangerZone.server_connection.sendVarIntMessage(pet.entityID, 0, 0);
				}
			}

		}
		public void rightclickhandler(){
			leftclickhandler();
		}
	}
	public PlayerSignGUI(){
		super();
	}
	public void init(Entity inpet){

		pet = inpet;
		currstring1 = pet.getVarString(3);
		currstring2 = pet.getVarString(4);
		currstring3 = pet.getVarString(5);
		if(currstring1 == null)currstring1 = new String();
		if(currstring2 == null)currstring2 = new String();
		if(currstring3 == null)currstring3 = new String();
		rotate = pet.getVarInt(0); //also getBID();
		
	}
	
	/*
	 * Make a sign!
	 * 
	 */
	public void process(){
		Texture checktexture = null;
		Texture unchecktexture = null;
		Texture backtexture = null;
		int clickx, clicky;
		MyButtonHandler mb = null;
		MyButtonHandler fb = null;
		int i;

		checktexture = TextureMapper.getTexture("res/menus/"+"check.png");
		unchecktexture = TextureMapper.getTexture("res/menus/"+"uncheck.png");
		backtexture = TextureMapper.getTexture("res/menus/"+"back.png");
		
		textAt(50, 250, "Rotate Sign: ");
		
		if(curline == 1)drawRectangleWithTexture(DangerZone.textinputtexture, 180, 160, 450, 30);
		textAt(50, 160, "Text Line 1:   " + currstring1);
		
		if(curline == 2)drawRectangleWithTexture(DangerZone.textinputtexture, 180, 110, 450, 30);
		textAt(50, 110, "Text Line 2:   " + currstring2);
		
		if(curline == 3)drawRectangleWithTexture(DangerZone.textinputtexture, 180, 60, 450, 30);
		textAt(50, 60, "Text Line 3:   " + currstring3);
		
		buttons = new ArrayList<MyButtonHandler>();
		buttons.add(new MyButtonHandler(DangerZone.screen_width/2 - 150, DangerZone.screen_height - 200, 100, 100, backtexture, null, 0));
		buttons.add(new MyButtonHandler(250, 250, 40, 40, pet.getBID()!=0?unchecktexture:checktexture, null, 1));
		
		//Draw buttons!
		Iterator<MyButtonHandler> bb = buttons.iterator();
		while(bb.hasNext()){
			bb.next().draw();
		}
				
				
		//Check for exit via keypad
		String s = getTextChar();
		if(escaped){
			ImAllDone();
			return;
		}
		if(entered){
			//Send it!
			if(curline == 1)DangerZone.server_connection.sendVarStringMessage(pet.entityID, 3, currstring1);
			if(curline == 2)DangerZone.server_connection.sendVarStringMessage(pet.entityID, 4, currstring2);
			if(curline == 3)DangerZone.server_connection.sendVarStringMessage(pet.entityID, 5, currstring3);
			//we don't record it here, because we will get it back when it is broadcast out from the server	
			curline++;
			if(curline > 3)ImAllDone();
			return;
		}
		if(curline == 1 && s != null){
			if(!s.equals("delete")){
				//add a new char
				if(currstring1.length() < 35)currstring1 += s;
			}else{
				//delete the last char
				if(currstring1.length() > 0){
					String newstring = new String();
					for(i=0;i<currstring1.length()-1;i++){
						newstring += currstring1.charAt(i);
					}
					currstring1 = newstring;
				}
			}
		}
		if(curline == 2 && s != null){
			if(!s.equals("delete")){
				//add a new char
				if(currstring2.length() < 35)currstring2 += s;
			}else{
				//delete the last char
				if(currstring2.length() > 0){
					String newstring = new String();
					for(i=0;i<currstring2.length()-1;i++){
						newstring += currstring2.charAt(i);
					}
					currstring2 = newstring;
				}
			}
		}
		if(curline == 3 && s != null){
			if(!s.equals("delete")){
				//add a new char
				if(currstring3.length() < 35)currstring3 += s;
			}else{
				//delete the last char
				if(currstring3.length() > 0){
					String newstring = new String();
					for(i=0;i<currstring3.length()-1;i++){
						newstring += currstring3.charAt(i);
					}
					currstring3 = newstring;
				}
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
