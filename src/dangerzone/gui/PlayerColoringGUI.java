package dangerzone.gui;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;

import dangerzone.DangerZone;
import dangerzone.TextureMapper;
import dangerzone.blocks.Blocks;


public class PlayerColoringGUI extends GuiInterface {
	
	List<MyButtonHandler> buttons;
	float colordata[][][] = null;
	boolean leftdown = false;
	int thisbid = 0;
	int cred = 138;
	int cgreen = 128;
	int cblue = 118;
	int dimension, px, py, pz;
	
	private class MyButtonHandler extends ButtonHandler {
		
		public int buttonid; //which button they hit
		
		MyButtonHandler(int xpos, int ypos, int bxsize, int bysize, Texture tx, String ot, int i){
			super(xpos, ypos, bxsize, bysize, tx, ot);
			buttonid = i;
		}
		
		public void leftclickhandler(){
			if(buttonid == 0){
				ImAllDone();
				return;
			}
			if(buttonid >= 1 && buttonid <= 256){
				int i = buttonid - 1;
				colordata[i/16][i%16][0] = cred;
				colordata[i/16][i%16][1] = cgreen;
				colordata[i/16][i%16][2] = cblue;
				return;
			}
			if(buttonid == 257){
				doSaveBlock();
				return;
			}
			if(buttonid == 258){
				cred++;
				if(cred > 255)cred = 255;
				return;
			}
			if(buttonid == 259){
				cred--;
				if(cred <0)cred = 0;
				return;
			}
			if(buttonid == 260){
				cgreen++;
				if(cgreen > 255)cgreen = 255;
				return;
			}
			if(buttonid == 261){
				cgreen--;
				if(cgreen <0)cgreen = 0;
				return;
			}
			if(buttonid == 262){
				cblue++;
				if(cblue > 255)cblue = 255;
				return;
			}
			if(buttonid == 263){
				cblue--;
				if(cblue <0)cblue = 0;
				return;
			}
		}
		public void rightclickhandler(){
			leftclickhandler();
		}
	}
	
	public PlayerColoringGUI(){
		super();
	}
	
	public void init(int tb, int d, int x, int y, int z){

		colordata = new float [16][16][4];
		thisbid = tb;
		dimension = d;
		px = x;
		py = y;
		pz = z;
				
		//Do it this way, because the resource is in the jar file... doh!
		byte b[] = Blocks.getTexture(thisbid).getTextureData();
		int i, j, k;
		int cl;
		k = 0;
		for(j=0;j<16;j++){
			for(i=0;i<16;i++){	
				cl = b[k]; k++;
				if(cl < 0)cl += 256;
				colordata[i][15-j][0] = cl;	
				cl = b[k]; k++;
				if(cl < 0)cl += 256;
				colordata[i][15-j][1] = cl;
				cl = b[k]; k++;
				if(cl < 0)cl += 256;
				colordata[i][15-j][2] = cl; 
				colordata[i][15-j][3] = 255f;		
			}
		}				
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
		int i, j;
		
		int starty = DangerZone.screen_height - 200;
		int startx = (DangerZone.screen_width/2) + (300);
		
		backtexture = TextureMapper.getTexture("res/menus/"+"back.png");
		buttontexture = TextureMapper.getTexture("res/menus/"+"button.png");

		//Build the button list... 
		buttons = new ArrayList<MyButtonHandler>();
		buttons.add(new MyButtonHandler(DangerZone.screen_width/2 - 150, starty, 100, 100, backtexture, null, 0));
		
		//nice little outline
		drawcoloredsquare((DangerZone.screen_width/2) - 2, (DangerZone.screen_height/2) - 102, 260, 260, 255, 255, 255, 255);
		drawcoloredsquare((DangerZone.screen_width/2) - 1, (DangerZone.screen_height/2) - 101, 258, 258, 0, 0, 0, 255);

		for(i=0;i<16;i++){
			for(j=0;j<16;j++){
				drawcoloredsquare((DangerZone.screen_width/2) + (i*16), (DangerZone.screen_height/2) + (j*16) - 100, 16, 16, 
						colordata[i][j][0]/256f, colordata[i][j][1]/256f, colordata[i][j][2]/256f, colordata[i][j][3]/256f);
				buttons.add(new MyButtonHandler((DangerZone.screen_width/2) + (i*16), (DangerZone.screen_height/2) + (j*16) - 100, 16, 16, null, null, (i*16)+j+1));
			}
		}
		
		starty = (DangerZone.screen_height/2);
		drawcoloredsquare(startx, starty - 100, 32, 256, 1f, 1f, 1f, 1f);
		drawcoloredsquare(startx+8, starty - 100, 16, cred, 1f, 0f, 0f, 1f);
		buttons.add(new MyButtonHandler(startx, starty - 150, 32, 32, buttontexture, "+", 258));
		buttons.add(new MyButtonHandler(startx, starty - 190, 32, 32, buttontexture, "-", 259));
		drawcoloredsquare(startx+50, starty - 100, 32, 256, 1f, 1f, 1f, 1f);
		drawcoloredsquare(startx+58, starty - 100, 16, cgreen, 0f, 1f, 0f, 1f);
		buttons.add(new MyButtonHandler(startx+50, starty - 150, 32, 32, buttontexture, "+", 260));
		buttons.add(new MyButtonHandler(startx+50, starty - 190, 32, 32, buttontexture, "-", 261));
		drawcoloredsquare(startx+100, starty - 100, 32, 256, 1f, 1f, 1f, 1f);
		drawcoloredsquare(startx+108, starty - 100, 16, cblue, 0f, 0f, 1f, 1f);
		buttons.add(new MyButtonHandler(startx+100, starty - 150, 32, 32, buttontexture, "+", 262));
		buttons.add(new MyButtonHandler(startx+100, starty - 190, 32, 32, buttontexture, "-", 263));
		
		drawcoloredsquare(startx, starty+200, 132, 32, (float)cred/256f, (float)cgreen/256f, (float)cblue/256f, 1f);
		//buttons.add(new MyButtonHandler((DangerZone.screen_width/2) + (i*16), (DangerZone.screen_height/2) + (j*16), 16, 16, null, null, (i*16)+j+1));
		
		buttons.add(new MyButtonHandler(DangerZone.screen_width/2 + 75, starty + 200, 100, 40, buttontexture, "Save", 257));
		 
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
		}
		
		//Check for mouse events!
		while(M_next()){
			clickx = M_getEventX();
			clicky = M_getEventY();
			if(M_getEventButtonState()){ //clicked!
				if(M_getEventButton() >= 0){ 
					
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
						leftdown = true;
					}
					if(M_getEventButton() == 1){
						if(fb != null)fb.rightclickhandler();
					}
				}
			}else{
				if(M_getEventButton() == 0){
					leftdown = false;
				}
			}
		}
		
		//process buttons being held down too...
		int cx, cy;
		cx = M_getX();
		cy = M_getY();
		bb = buttons.iterator();
		fb = null;
		while(bb.hasNext()){
			mb = bb.next();
			if(cx >= mb.x && cx <= mb.x+mb.xsize){
				if(cy >= mb.y && cy <= mb.y+mb.ysize){
					if(leftdown && mb.buttonid >= 1 && mb.buttonid <= 256){ //paint
						mb.leftclickhandler();
					}
					if(leftdown && mb.buttonid >= 258 && mb.buttonid <= 263){ //+- color
						mb.leftclickhandler();
					}
				}
			}
		}

	}
	
	/*
	 * Save the new colored texture and force a reload for the renderer
	 */
	public void doSaveBlock(){
		String blkname = Blocks.getUniqueName(thisbid);
		if(blkname == null)return;
		/*
		 * Send this thing out to the server so it can deal with it!
		*/
		DangerZone.player.server_connection.sendColoringBlock(thisbid, dimension, px, py, pz, colordata);
	}

}
