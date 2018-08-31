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
import dangerzone.ToDoItem;
import dangerzone.ToDoList;



public class PlayerToDoGUI extends GuiInterface {
	List<MyButtonHandler> buttons;
	int start_offset = 0;
	
	private class MyButtonHandler extends ButtonHandler {
		
		public int buttonid; //which button they hit
		public String helper = null;
		
		MyButtonHandler(int xpos, int ypos, int bxsize, int bysize, Texture tx, String ot, int i){
			super(xpos, ypos, bxsize, bysize, tx, ot);
			buttonid = i;
		}
		
		MyButtonHandler(int xpos, int ypos, int bxsize, int bysize, Texture tx, String ot, String help, int i){
			super(xpos, ypos, bxsize, bysize, tx, ot);
			buttonid = i;
			helper = help;
		}
		
		public void leftclickhandler(){
			if(buttonid == 7){
				start_offset += 30;
				return;
			}
			if(buttonid == 8){
				start_offset -= 30;
				if(start_offset < 0)start_offset = 0;
				return;
			}
			if(buttonid == 0){
				ImAllDone();
			}
		}
		public void rightclickhandler(){
			leftclickhandler();
		}
	}
	
	public PlayerToDoGUI(){
		super();
		start_offset = 0;
	}
	
	/*
	 * Nice reasonably simple button event processing....
	 */
	public void process(){
		Texture checktexture = null;
		Texture unchecktexture = null;
		Texture buttontexture = null;
		Texture backtexture = null;
		buttontexture = TextureMapper.getTexture("res/menus/"+"button.png");
		int clickx, clicky;
		MyButtonHandler mb = null;
		MyButtonHandler fb = null;
		int totalfound = 0;
		int hidden = 0;
		int row, col;
		ToDoItem todo = null;
		
		int starty = DangerZone.screen_height - 150;
		
		checktexture = TextureMapper.getTexture("res/menus/"+"check.png");
		unchecktexture = TextureMapper.getTexture("res/menus/"+"uncheck.png");
		backtexture = TextureMapper.getTexture("res/menus/"+"back.png");

		//Build the button list... 
		buttons = new ArrayList<MyButtonHandler>();
		buttons.add(new MyButtonHandler(DangerZone.screen_width/2 - 250, starty, 100, 100, backtexture, null, 0));
		//prev-next buttons
		buttons.add(new MyButtonHandler(DangerZone.screen_width/2 - 125, starty + 30, (int)(75), (int)(25), buttontexture, "Next", 7));
		buttons.add(new MyButtonHandler(DangerZone.screen_width/2 - 125, starty, (int)(75), (int)(25), buttontexture, "Prev", 8)); 
		
		row = col = 0;
		int listlen = ToDoList.todo.size();
		while(totalfound < 30){
			if(start_offset + totalfound + hidden >= listlen)break;

			todo = ToDoList.todo.get(start_offset + totalfound + hidden);

			if(todo.gettitle() == null){ //hidden!
				hidden++;
			}else{
				smallTextAt(48 + (col*450), 603 - (row*40), String.format("%d", start_offset + totalfound + 1));
				buttons.add(new MyButtonHandler(120 + (col*450), 600 - (row*40), 225, 25, null, todo.gettitle(), todo.gethelptext(), -2));			
				buttons.add(new MyButtonHandler(85 + (col*450), 600 - (row*40), 25, 25, todo.isTrue()?checktexture:unchecktexture, null, -1));			
				totalfound++;
				col++;
				if(col >= 3){
					col = 0;
					row++;
				}
				if(row >= 10)break;
			}
		}
		
		if(totalfound == 0){
			start_offset -= 30;
			if(start_offset < 0)start_offset = 0;
		}


			
		//Draw buttons!
		Iterator<MyButtonHandler> bb = buttons.iterator();
		while(bb.hasNext()){
			bb.next().draw();
		}
		
		
		//Check for exit via keypad
		while(K_next()){
			if (K_getEventKey() == Keyboard.KEY_ESCAPE && K_isKeyDown(Keyboard.KEY_ESCAPE)){
				ImAllDone();
				return;
			}
			if (K_getEventKey() == Keyboard.KEY_F11 && K_isKeyDown(Keyboard.KEY_F11)){
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
		
		//show some descriptive text!
		int cx, cy;
		cx = M_getX();
		cy = M_getY();
		bb = buttons.iterator();
		fb = null;
		while(bb.hasNext()){
			mb = bb.next();
			if(cx >= mb.x && cx <= mb.x+mb.xsize){
				if(cy >= mb.y && cy <= mb.y+mb.ysize){
					if(mb.helper != null){	
						String[] temps = mb.helper.split("\n");
						for(int j=0 ; j<temps.length;j++){
							if(temps[j] != null)textAt(DangerZone.screen_width/2 + 50, DangerZone.screen_height - (75 +(j*30)), temps[j]);
						}
					}
				}
			}
		}
	}
	

	

}





















