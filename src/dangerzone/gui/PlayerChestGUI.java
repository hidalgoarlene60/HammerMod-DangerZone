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
import dangerzone.entities.EntityChest;
import dangerzone.items.Items;




public class PlayerChestGUI extends GuiInterface {
	
	List<ButtonHandler> buttons; //more like inventory squares, really, but they do nicely as buttons

	public EntityChest ec = null;
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
			//System.out.printf("Left in %d,  %d\n", ih, index);

			if(ih == 9 || ih == 10 || ih == 11 || ih == 12){
				ClickedArmor(index, 0, shifted);
				return;
			}

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
				ClickedInventoryWithEntity(index, 0, shifted, ec.entityID);
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
	

	
	public PlayerChestGUI(){
		super();
		buttons = null;
		mousebite = null;


	}
	
	public void process(){
		Texture helmettexture = null;
		Texture leggingstexture = null;
		Texture chestplatetexture = null;
		Texture bootstexture = null;
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
		helmettexture = TextureMapper.getTexture("res/menus/"+"invhelmet.png");
		chestplatetexture = TextureMapper.getTexture("res/menus/"+"invchestplate.png");
		leggingstexture = TextureMapper.getTexture("res/menus/"+"invleggings.png");
		bootstexture = TextureMapper.getTexture("res/menus/"+"invboots.png");
		
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
		
		//add armor buttons!
		starty = 200;
		drawRectangleWithTexture(helmettexture, (startx-5)*scalex, (starty-5)*scaley, 50*scalex, 50*scaley);
		buttons.add(new MyButtonHandler((int)((startx)*scalex), (int)((starty)*scaley), (int)(40*scalex), (int)(40*scaley), DangerZone.player.getArmor(0), 9, 0));
		showArmorValue(0, startx+5, scalex, starty-25, scaley);
		
		drawRectangleWithTexture(chestplatetexture, (startx+45)*scalex, (starty-5)*scaley, 50*scalex, 50*scaley);
		buttons.add(new MyButtonHandler((int)((startx+50)*scalex), (int)((starty)*scaley), (int)(40*scalex), (int)(40*scaley), DangerZone.player.getArmor(1), 10, 1));
		showArmorValue(1, startx+55, scalex, starty-25, scaley);
		
		drawRectangleWithTexture(leggingstexture, (startx+95)*scalex, (starty-5)*scaley, 50*scalex, 50*scaley);
		buttons.add(new MyButtonHandler((int)((startx+100)*scalex), (int)((starty)*scaley), (int)(40*scalex), (int)(40*scaley), DangerZone.player.getArmor(2), 11, 2));
		showArmorValue(2, startx+105, scalex, starty-25, scaley);
		
		drawRectangleWithTexture(bootstexture, (startx+145)*scalex, (starty-5)*scaley, 50*scalex, 50*scaley);
		buttons.add(new MyButtonHandler((int)((startx+150)*scalex), (int)((starty)*scaley), (int)(40*scalex), (int)(40*scaley), DangerZone.player.getArmor(3), 12, 3));
		showArmorValue(3, startx+155, scalex, starty-25, scaley);

		starty = 600;
		startx = 775;
		drawRectangleWithTexture(invtx, (startx-6)*scalex, (starty-205)*scaley, 500*scalex, 250*scaley);

		//Now go back and fill in buttons
		for(i=0;i<50;i++){
				row = i/10;
				col = i%10;
				buttons.add(new MyButtonHandler((int)((startx+cellsize*col)*scalex), (int)((starty-row*cellsize)*scaley), (int)(40*scalex), (int)(40*scaley), 
						ec.getInventory(i), 3, i)); //3 = chest
		}


		buttons.add(new MyButtonHandler((int)(650*scalex), (int)(500*scaley), (int)(50*scalex), (int)(50*scaley), deletetexture, null, 6, 0)); //delete button
		buttons.add(new MyButtonHandler((int)(650*scalex), (int)(560*scaley), (int)(50*scalex), (int)(50*scaley), tosstexture, null, -1, -1)); //toss button

		//catch-all!
		//buttons.add(new MyButtonHandler(0, 0, DangerZone.screen_width, DangerZone.screen_height, null, null, -1, -1));

		//Now draw them!
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
			if(s != null && !s.equals("")){
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
		DangerZone.world.playSound("DangerZone:chest_close", DangerZone.player.dimension, DangerZone.player.posx, DangerZone.player.posy, DangerZone.player.posz, 0.75f, 1.0f);		
		ClearTable();		
		ec = null;				
		super.ImAllDone();
	}
	

}
