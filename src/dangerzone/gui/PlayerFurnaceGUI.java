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
import dangerzone.InventoryContainer;
import dangerzone.TextureMapper;
import dangerzone.blocks.Blocks;
import dangerzone.entities.EntityFurnace;
import dangerzone.items.Items;


public class PlayerFurnaceGUI extends GuiInterface {
	
	List<ButtonHandler> buttons; //more like inventory squares, really, but they do nicely as buttons

	public EntityFurnace ec = null;
	public boolean shifted = false;

	
	private class MyButtonHandler extends ButtonHandler {
		
		public int ih; //inventory or hotbar
		public int index; //index into above
		
		MyButtonHandler(int xpos, int ypos, int bxsize, int bysize, Texture tx, String ot, int i, int j){
			super(xpos, ypos, bxsize, bysize, tx, ot);
			ih = i;
			index = j;
		}
		
		MyButtonHandler(int xpos, int ypos, int bxsize, int bysize, InventoryContainer ic, int i, int j){
			super(xpos, ypos, bxsize, bysize, ic);
			ih = i;
			index = j;
		}
		
		public void leftclickhandler(){
			if(ih == 6){ //delete!
				DeleteMouseBite();
				return;
			}			
			if(ih == 3){
				ClickedEntityInventory(ec.entityID, index, 0, shifted);
				return;
			}			
			if(ih < 0 || index < 0){
				SpitMouseBite();
				return;
			}			
			if(ih == 1){
				ClickedInventoryWithEntity(index, 0, false, ec.entityID); //no shift-clicking into furnace!
			}else{
				ClickedHotBar(index, 0, shifted);
			}
			
		}
		public void rightclickhandler(){
			//System.out.printf("Right in %d,  %d\n", ih, index);
			//try picking up half
			if(ih == 3){					
				ClickedEntityInventory(ec.entityID, index, 1, shifted);
				return;
			}
			if(ih < 0 || ih > 1){
				return;
			}			
			if(ih == 1){
				ClickedInventory(index, 1, shifted);
			}else{
				ClickedHotBar(index, 1, shifted);
			}
		}
	}
	

	
	public PlayerFurnaceGUI(){
		super();
		buttons = null;
		mousebite = null;
	}
	
	public void process(){
		Texture craft1texture = null;
		Texture timertexture = null;
		Texture invtx = null;
		Texture hottx = null;
		Texture deletetexture = null;
		Texture tosstexture = null;
		int i, row, col;
		int startx, starty;
		int cellsize = 50;
		int clickx, clicky;
		ButtonHandler mb = null;
		ButtonHandler fb = null;
		String s = null;
		int mod = 10;
		Texture tx;
		InventoryContainer ic = null;
		float scalex = DangerZone.screen_width/1350f;
		float scaley = DangerZone.screen_height/700f;
		
		invtx = TextureMapper.getTexture("res/menus/playerinventory.png");
		hottx = TextureMapper.getTexture("res/menus/playerhotbar.png");
		deletetexture = TextureMapper.getTexture("res/menus/"+"deletebutton.png");
		tosstexture = TextureMapper.getTexture("res/menus/"+"tossbutton.png");
		craft1texture = TextureMapper.getTexture("res/menus/"+"craft1.png");
		timertexture = TextureMapper.getTexture("res/menus/"+"timer.png");
		
		startx = 100;
		starty = 600;
		if(scalex < scaley)scaley = scalex;
		if(scaley < scalex)scalex = scaley;
		
		if(ec == null){
			ImAllDone();
			return;
		}
		
		if(K_isKeyDown(Keyboard.KEY_LSHIFT) || K_isKeyDown(Keyboard.KEY_RSHIFT)){
			shifted = true;
		}else{
			shifted = false;
		}
		//OpenGL has some quirks...
		drawRectangleWithTexture(invtx, (startx-5)*scalex, (starty-205)*scaley, 500*scalex, 250*scaley);

		//Build the button list... yeah... i know... would be nice to not recreate it every time...
		//but they do have a tendency to change dynamically, so we don't...
		buttons = new ArrayList<ButtonHandler>();
		for(i=0;i<50;i++){
			row = i/mod;
			col = i%mod;
			buttons.add(new MyButtonHandler((int)((startx+cellsize*col)*scalex), (int)((starty-row*cellsize)*scaley), (int)(40*scalex), (int)(40*scaley), 
					DangerZone.player.getInventory(i), 1, i)); //1 = inventory
		}

		//add hotbar buttons...
		starty -= 300;
		drawRectangleWithTexture(hottx, (startx-5)*scalex, (starty-5)*scaley, 500*scalex, 50*scaley);
		for(i=0;i<10;i++){
			row = 0;
			col = i;
			buttons.add(new MyButtonHandler((int)((startx+cellsize*col)*scalex), (int)((starty-row*cellsize)*scaley), (int)(40*scalex), (int)(40*scaley), 
					DangerZone.player.getHotbar(i), 0, i)); //0 = hotbar
		}


		starty = 650;
		startx = 725;
		drawRectangleWithTexture(craft1texture, (startx+180)*scalex, (starty-70)*scaley, 50*scalex, 50*scaley);
		buttons.add(new MyButtonHandler((int)((startx+185)*scalex), (int)((starty-65)*scaley), (int)(40*scalex), (int)(40*scaley), ec.getInventory(0), 3, 0));
		starty -= 200;
		drawRectangleWithTexture(craft1texture, (startx+180)*scalex, (starty-70)*scaley, 50*scalex, 50*scaley);
		buttons.add(new MyButtonHandler((int)((startx+185)*scalex), (int)((starty-65)*scaley), (int)(40*scalex), (int)(40*scaley), ec.getInventory(1), 3, 1));
		starty -= 75;
		drawRectangleWithTexture(craft1texture, (startx+180)*scalex, (starty-70)*scaley, 50*scalex, 50*scaley);
		buttons.add(new MyButtonHandler((int)((startx+185)*scalex), (int)((starty-65)*scaley), (int)(40*scalex), (int)(40*scaley), ec.getInventory(2), 3, 2));
		
		starty = 650;
		startx = 825;
		drawRectangleWithTexture(craft1texture, (startx+180)*scalex, (starty-70)*scaley, 50*scalex, 50*scaley);
		buttons.add(new MyButtonHandler((int)((startx+185)*scalex), (int)((starty-65)*scaley), (int)(40*scalex), (int)(40*scaley), ec.getInventory(3), 3, 3));
		starty -= 200;
		drawRectangleWithTexture(craft1texture, (startx+180)*scalex, (starty-70)*scaley, 50*scalex, 50*scaley);
		buttons.add(new MyButtonHandler((int)((startx+185)*scalex), (int)((starty-65)*scaley), (int)(40*scalex), (int)(40*scaley), ec.getInventory(4), 3, 4));
		starty -= 75;
		drawRectangleWithTexture(craft1texture, (startx+180)*scalex, (starty-70)*scaley, 50*scalex, 50*scaley);
		buttons.add(new MyButtonHandler((int)((startx+185)*scalex), (int)((starty-65)*scaley), (int)(40*scalex), (int)(40*scaley), ec.getInventory(5), 3, 5));
		
		


		buttons.add(new MyButtonHandler((int)(650*scalex), (int)(500*scaley), (int)(50*scalex), (int)(50*scaley), deletetexture, null, 6, 0)); //delete button
		buttons.add(new MyButtonHandler((int)(650*scalex), (int)(560*scaley), (int)(50*scalex), (int)(50*scaley), tosstexture, null, -1, -1)); //toss button


		//catch-all!
		//buttons.add(new MyButtonHandler(0, 0, DangerZone.screen_width, DangerZone.screen_height, null, null, -1, -1));

		starty = 650 - 70;
		startx = 725 + 180;
		
		startx -= 33;
		starty -= 275;
		
		float pu = ec.getVarFloat(6); //fuel remaining	over fuel started with	
		if(pu != 0)pu = ec.getVarFloat(4)/pu;
		if(pu > 1)pu = 1;
		if(pu < 0)pu = 0;
		float xs = 64 * pu;
		if(xs > 0){
			GL11.glPushMatrix(); //save position
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glTranslatef(startx*scalex, starty*scaley, 0f);
			GL11.glScalef(1f, 1f, 1f);
			GL11.glBegin(GL11.GL_QUADS);	
			GL11.glColor3f(0.65f, 0, 0);
			GL11.glVertex3f(20, xs, 0); // Top Right
			GL11.glColor3f(0.65f, 0, 0);
			GL11.glVertex3f(0, xs, 0); // Top Left
			GL11.glColor3f(0.65f, 0, 0);
			GL11.glVertex3f(0, 0, 0); // Bottom left
			GL11.glColor3f(0.65f, 0, 0);
			GL11.glVertex3f(20, 0, 0); // Bottom right		
			GL11.glEnd(); // Done Drawing The Quad
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glPopMatrix();
		}
		
		startx += 200;
		pu = ec.getVarFloat(7); //fuel remaining	over fuel started with	
		if(pu != 0)pu = ec.getVarFloat(5)/pu;
		
		if(pu > 1)pu = 1;
		if(pu < 0)pu = 0;
		xs = 64 * pu;
		if(xs > 0){
			GL11.glPushMatrix(); //save position
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glTranslatef(startx*scalex, starty*scaley, 0f);
			GL11.glScalef(1f, 1f, 1f);
			GL11.glBegin(GL11.GL_QUADS);	
			GL11.glColor3f(0.65f, 0, 0);
			GL11.glVertex3f(20, xs, 0); // Top Right
			GL11.glColor3f(0.65f, 0, 0);
			GL11.glVertex3f(0, xs, 0); // Top Left
			GL11.glColor3f(0.65f, 0, 0);
			GL11.glVertex3f(0, 0, 0); // Bottom left
			GL11.glColor3f(0.65f, 0, 0);
			GL11.glVertex3f(20, 0, 0); // Bottom right		
			GL11.glEnd(); // Done Drawing The Quad
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glPopMatrix();
		}
		
		GL11.glColor3f(1,1,1); //because above messed this up!
		
		starty = 650 - 70;
		startx = 725 + 180;
		starty -= 75;
		startx += 20;
		pu = ec.getVarFloat(8);
		xs = ec.getVarFloat(10);
		if(xs != 0)pu = pu/xs;
		drawRectangleWithTexture(timertexture, (startx)*scalex, (starty)*scaley, 45*scalex, 45*scaley, pu*360);
		startx += 110;
		pu = ec.getVarFloat(9);
		xs = ec.getVarFloat(11);
		if(xs != 0)pu = pu/xs;
		drawRectangleWithTexture(timertexture, (startx)*scalex, (starty)*scaley, 45*scalex, 45*scaley, pu*360);
		
		
		//Now draw buttons!
		Iterator<ButtonHandler> bb = buttons.iterator();
		while(bb.hasNext()){
			bb.next().draw();
		}

		//Draw the things under the mouse
		mousebite = DangerZone.player.getMouseBite();
		if(mousebite != null){			
			tx = mousebite.getTexture();
			s = null;
			if(mousebite.count > 1){
				s = String.format("%d", mousebite.count);
			}
			if(tx != null){
				GL11.glColor3f(1,1,1);
				drawRectangleWithTexture(tx, M_getX()-16, M_getY()-16, 32*scalex, 32*scaley);
			}
			if( s != null && !s.equals("")){
				textAt( M_getX()-10, M_getY()-16, s);
				GL11.glColor3f(1,1,1); //because text messes this up!
			}
		}


		//Check for mouse events on our button list!
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
					ic = mb.ic;
					if(ic != null){
						String hotstring = null;
						String[] hss = null;
						if(ic.bid != 0){
							hotstring = Blocks.getUniqueName(ic.bid);
						}
						if(ic.iid != 0){
							hotstring = Items.getUniqueName(ic.iid);
						}
						if(hotstring != null){
							hss = hotstring.split(":");
							if(hss.length >= 2){
								DangerZone.hotmessagestring = hss[1];
								DangerZone.hotmessagetimer = 10;
								//int xoff = 0;
								//if(cx > DangerZone.screen_width/2)xoff = -(hss[1].length());
								//WorldRendererUtils.textAt(font, cx + xoff, cy - 5, hss[1]);
							}
						}
					}
				}
			}
		}



		//Check for exit
		while(K_next()){
			if (K_getEventKey() == Keyboard.KEY_E && K_isKeyDown(Keyboard.KEY_E)){
				ImAllDone();
				return;
			}
			if (K_getEventKey() == Keyboard.KEY_ESCAPE && K_isKeyDown(Keyboard.KEY_ESCAPE)){
				ImAllDone();
				return;
			}
		}
	}
	
	public void ImAllDone(){
		
		ClearTable();
		
		ec = null;
				
		super.ImAllDone();
	}
	

	


}
