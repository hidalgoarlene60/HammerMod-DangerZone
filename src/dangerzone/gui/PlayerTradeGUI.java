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
import dangerzone.entities.EntityLiving;
import dangerzone.items.Items;


public class PlayerTradeGUI extends GuiInterface {
	
	List<ButtonHandler> buttons; //more like inventory squares, really, but they do nicely as buttons

	public EntityLiving ec = null;
	public boolean shifted = false;
	public boolean buy_mode = true;
	public int buy_index = 0;

	
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
				
			if(ih == 7){
				buy_index++;
				if(buy_index > 7)buy_index = 0;
				return;
			}
			if(ih == 8){
				buy_index--;
				for(int i=0;i<8;i++){
					ic = ec.getInventory(buy_index);
					if(ic != null)break;
					buy_index--;
					if(buy_index < 0)buy_index = 7;
				}
				return;
			}
			if(ih == 9){
				buy_mode = !buy_mode;
				return;
			}
			if(ih == 10){
				//buy it!
				if(!buy_mode)return;
				BuyFromEntity(buy_index, ec.entityID);
				return;
			}
			if(ih == 11){
				//sell it!
				if(buy_mode)return;
				SellToEntity(ec.entityID);
				return;
			}
			if(ih == 12){
				//pick up or put down
				if(buy_mode)return;
				MouseBiteToEntity(ec.entityID);
				return;
			}
			
			if(ih < 0 || ih > 1){
				return;
			}	
			if(ih == 1){
				ClickedInventory(index, 0, false);
			}else{
				ClickedHotBar(index, 0, shifted);
			}
			
		}
		public void rightclickhandler(){
			//System.out.printf("Right in %d,  %d\n", ih, index);
			if(ih < 0 || ih > 1){
				return;
			}			
			if(ih == 1){
				ClickedInventory(index, 1, false);
			}else{
				ClickedHotBar(index, 1, shifted);
			}
		}
	}
	

	
	public PlayerTradeGUI(){
		super();
		buttons = null;
		mousebite = null;
	}
	
	public void process(){
		Texture invtx = null;
		Texture hottx = null;
		Texture buttontexture = null;
		Texture selltexture = null;
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
		int totalcoins = 0;
		
		invtx = TextureMapper.getTexture("res/menus/playerinventory.png");
		hottx = TextureMapper.getTexture("res/menus/playerhotbar.png");
		buttontexture = TextureMapper.getTexture("res/menus/"+"button.png");
		selltexture = TextureMapper.getTexture("res/menus/"+"shredder.png");
		
		startx = 100;
		starty = 600;
		if(scalex < scaley)scaley = scalex;
		if(scaley < scalex)scalex = scaley;
		
		if(ec == null){
			ImAllDone();
			return;
		}
		if(ec.deadflag){
			ImAllDone();
			return;
		}
		
		for(i=0;i<8;i++){
			ic = ec.getInventory(buy_index);
			if(ic != null)break;
			buy_index++;
			if(buy_index > 7)buy_index = 0;
		}

		
		if(K_isKeyDown(Keyboard.KEY_LSHIFT) || K_isKeyDown(Keyboard.KEY_RSHIFT)){
			shifted = true;
		}else{
			shifted = false;
		}
		
		//Draw full player inventory
		
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
		
		for(i=0;i<50;i++){
				ic = DangerZone.player.getInventory(i);
				if(ic != null){
					if(ic.iid == Items.coinplatinum.itemID)totalcoins += 1000 * ic.count;
					if(ic.iid == Items.coingold.itemID)totalcoins += 100 * ic.count;
					if(ic.iid == Items.coinsilver.itemID)totalcoins += 10 * ic.count;
				}
		}

		for(i=0;i<10;i++){
				ic = DangerZone.player.getHotbar(i);
				if(ic != null){
					if(ic.iid == Items.coinplatinum.itemID)totalcoins += 1000 * ic.count;
					if(ic.iid == Items.coingold.itemID)totalcoins += 100 * ic.count;
					if(ic.iid == Items.coinsilver.itemID)totalcoins += 10 * ic.count;
				}
		}

		//----------------------------------------------------------------- Trading


		// buy-sell toggle
		buttons.add(new MyButtonHandler((int)(725*scalex), (int)(625*scaley), (int)(85*scalex), (int)(25*scaley), buttontexture, "Buy/Sell", 9, 0)); 
		
		textAt((200)*scalex, (200)*scaley, String.format("You have: $%d", totalcoins));
		
		if(buy_mode){
			//prev-next buttons
			buttons.add(new MyButtonHandler((int)(725*scalex), (int)(575*scaley), (int)(75*scalex), (int)(25*scaley), buttontexture, "Next", 7, 0));
			buttons.add(new MyButtonHandler((int)(725*scalex), (int)(550*scaley), (int)(75*scalex), (int)(25*scaley), buttontexture, "Prev", 8, 0)); 
			
			buttons.add(new MyButtonHandler((int)(890*scalex), (int)(525*scaley), (int)(75*scalex), (int)(25*scaley), buttontexture, "Buy It!", 10, 0)); 
			ic = ec.getInventory(buy_index);
			if(ic != null){
				int cost = ec.getVarInt(buy_index+16);
				int subcost = cost/1000;
				int off = 125;
				
				textAt((900+off)*scalex, (650)*scaley, String.format("Price: $%d", cost));
				
				if(subcost > 0){
					drawRectangleWithTexture(Items.coinplatinum.getTexture(), (900+off)*scalex, (600)*scaley, 50*scalex, 50*scaley);
					textAt((900+off)*scalex, (590)*scaley, String.format("%d", subcost));
					cost -= subcost*1000;
					off += 55;
				}
				subcost = cost/100;
				if(subcost > 0){
					drawRectangleWithTexture(Items.coingold.getTexture(), (900+off)*scalex, (600)*scaley, 50*scalex, 50*scaley);
					textAt((900+off)*scalex, (590)*scaley, String.format("%d", subcost));
					cost -= subcost*100;
					off += 55;
				}
				subcost = cost/10;
				if(subcost > 0){
					drawRectangleWithTexture(Items.coinsilver.getTexture(), (900+off)*scalex, (600)*scaley, 50*scalex, 50*scaley);
					textAt((900+off)*scalex, (590)*scaley, String.format("%d", subcost));
					cost -= subcost*10;
				}
				drawRectangleWithTexture(selltexture, (888)*scalex, (588)*scaley, 75*scalex, 75*scaley);
				//dumy button just so we get text description
				buttons.add(new MyButtonHandler((int)(900*scalex), (int)(600*scaley), (int)(50*scalex), (int)(50*scaley), ic, 999, 999)); 

			}

		}else{
			buttons.add(new MyButtonHandler((int)(890*scalex), (int)(525*scaley), (int)(75*scalex), (int)(25*scaley), buttontexture, "Sell It!", 11, 0)); 			
			buttons.add(new MyButtonHandler((int)(888*scalex), (int)(588*scaley), (int)(75*scalex), (int)(75*scaley), selltexture, null, 12, 0)); 
			
			ic = ec.getHotbar(0);
			if(ic != null){
				int cost = ec.getVarInt(30);
				int subcost = cost/1000;
				int off = 125;
				
				
				textAt((900+off)*scalex, (650)*scaley, String.format("Offer: $%d", cost));
				
				if(subcost > 0){
					drawRectangleWithTexture(Items.coinplatinum.getTexture(), (900+off)*scalex, (600)*scaley, 50*scalex, 50*scaley);
					textAt((900+off)*scalex, (590)*scaley, String.format("%d", subcost));
					cost -= subcost*1000;
					off += 55;
				}
				subcost = cost/100;
				if(subcost > 0){
					drawRectangleWithTexture(Items.coingold.getTexture(), (900+off)*scalex, (600)*scaley, 50*scalex, 50*scaley);
					textAt((900+off)*scalex, (590)*scaley, String.format("%d", subcost));
					cost -= subcost*100;
					off += 55;
				}
				subcost = cost/10;
				if(subcost > 0){
					drawRectangleWithTexture(Items.coinsilver.getTexture(), (900+off)*scalex, (600)*scaley, 50*scalex, 50*scaley);
					textAt((900+off)*scalex, (590)*scaley, String.format("%d", subcost));
					cost -= subcost*10;
				}
				
				drawRectangleWithTexture(selltexture, (888)*scalex, (588)*scaley, 75*scalex, 75*scaley);
				buttons.add(new MyButtonHandler((int)(900*scalex), (int)(600*scaley), (int)(50*scalex), (int)(50*scaley), ic, 999, 999)); 

			}
		}
		
		//----------------------------------------------------------------- normal stuff
		
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
		mousebite = null;
		shifted = false;
		buy_mode = true;
		buy_index = 0;
		UnStayEntity(ec.entityID); 
		ec = null;
		super.ImAllDone();
	}
	

	


}