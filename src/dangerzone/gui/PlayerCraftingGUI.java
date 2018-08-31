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
import java.util.StringTokenizer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;


import dangerzone.Crafting;
import dangerzone.DangerZone;
import dangerzone.InventoryContainer;
import dangerzone.Recipe;
import dangerzone.TextureMapper;
import dangerzone.blocks.Blocks;
import dangerzone.items.Items;



public class PlayerCraftingGUI extends GuiInterface {
	
	List<ButtonHandler> buttons; //more like inventory squares, really, but they do nicely as buttons
	List<MyCraftingHandler> crafting_buttons;
	
	public InventoryContainer crafted;
	public InventoryContainer last_crafted;
	public boolean shifted = false;
	public String isearch = "";
	public boolean isearchinput = false;
	public int curidx = 0;

	public int crafting;

	
	
	private class MyButtonHandler extends ButtonHandler {
		
		public int ih; //inventory or hotbar
		public int index; //index into above
		public float transp;
		
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
		
		MyButtonHandler(int xpos, int ypos, int bxsize, int bysize, Texture tx, String ot, int i, float tr){
			super(xpos, ypos, bxsize, bysize, tx, ot);
			ih = i;
			transp = tr;
		}
		
		public void leftclickhandler(){
			
			if(ih != 17){
				isearchinput = false;
			}else{
				isearchinput = true;
			}
			
			if(ih >= 20 && ih <= 29){
				curidx = ih-20;
				return;
			}
			
			//System.out.printf("Left in %d,  %d\n", ih, index);
			if(ih == 9 || ih == 10 || ih == 11 || ih == 12){
				ClickedArmor(index, 0, shifted);
				return;
			}
						
			if(ih == 6){ //delete!
				DeleteMouseBite();
				return;
			}
			
			if(ih < 0 || index < 0){
				SpitMouseBite();
				return;
			}
			
			if(ih == 1){
				ClickedInventory(index, 0, shifted);
			}
			if(ih == 0){
				ClickedHotBar(index, 0, shifted);
			}
		}
	
		public void rightclickhandler(){
			if(ih == 1){
				ClickedInventory(index, 1, shifted);
			}
			if(ih == 0){
				ClickedHotBar(index, 1, shifted);
			}
		}
		
		public void draw(){
			
			if(ic != null){
				super.draw();
			}else{
				if(t != null){
					if(transp != 0)GL11.glColor4f(1,1,1, transp);
					else GL11.glColor4f(1,1,1,1);
					drawRectangleWithTexture(t, x, y, xsize, ysize);

				}
				if(s != null && !s.equals("")){
					textAt(x+6, y, s);
					GL11.glColor4f(1,1,1,1); //because text messes this up!
				}
			}
		}
		
	}
	
	private class MyCraftingHandler extends ButtonHandler {
		
		public int ih; //crafter or crafted
		public int index; //index into above
		
		MyCraftingHandler(int xpos, int ypos, int bxsize, int bysize, Texture tx, String ot, int i, int j){
			super(xpos, ypos, bxsize, bysize, tx, ot);
			ih = i;
			index = j;
		}
		
		MyCraftingHandler(int xpos, int ypos, int bxsize, int bysize, InventoryContainer ic, int i, int j){
			super(xpos, ypos, bxsize, bysize, ic);
			ih = i;
			index = j;
		}
		
		public void leftclickhandler(){
			//System.out.printf("Left in %d,  %d\n", ih, index);
			if(ih == 1){
				if(crafting < 60)return;
				ClickedCrafted(0, shifted);	
			}else{
				ClickedCrafting(index, 0, shifted);
			}	
		}
		public void rightclickhandler(){
			//System.out.printf("Right in %d,  %d\n", ih, index);
			//try picking up half
			if(ih == 1){	
				if(crafting < 60)return;
				ClickedCrafted(1, shifted);
			}else{
				ClickedCrafting(index, 1, shifted);
			}
		}
	}
	
	public PlayerCraftingGUI(){
		super();
		buttons = null;
		mousebite = null;
		crafted = null;
		last_crafted = null;
		crafting = 0;
		isearch = "";

	}

	/*
	 * This is the callback from main to process the GUI.
	 * 
	 */
	public void process(){
		Texture woodtexture = null;
		Texture axetexture = null;
		Texture invtx = null;
		Texture hottx = null;
		Texture deletetexture = null;
		Texture tosstexture = null;
		Texture craft1texture = null;
		Texture craft9texture = null;
		Texture helmettexture = null;
		Texture leggingstexture = null;
		Texture chestplatetexture = null;
		Texture bootstexture = null;
		Texture textinputtexture = null;
		int i, row, col;
		int startx, starty;
		int cellsize = 50;
		int clickx, clicky;
		ButtonHandler mb = null;
		ButtonHandler fb = null;
		Texture tx;
		String s = null;
		int mod = 10;
		float scalex = DangerZone.screen_width/1350f;
		float scaley = DangerZone.screen_height/700f;
		
		invtx = TextureMapper.getTexture("res/menus/playerinventory.png");
		hottx = TextureMapper.getTexture("res/menus/playerhotbar.png");
		//buttontexture = TextureMapper.getTexture("res/menus/"+"button.png");
		deletetexture = TextureMapper.getTexture("res/menus/"+"deletebutton.png");
		tosstexture = TextureMapper.getTexture("res/menus/"+"tossbutton.png");
		craft1texture = TextureMapper.getTexture("res/menus/"+"craft1.png");
		craft9texture = TextureMapper.getTexture("res/menus/"+"craft9.png");
		helmettexture = TextureMapper.getTexture("res/menus/"+"invhelmet.png");
		chestplatetexture = TextureMapper.getTexture("res/menus/"+"invchestplate.png");
		leggingstexture = TextureMapper.getTexture("res/menus/"+"invleggings.png");
		bootstexture = TextureMapper.getTexture("res/menus/"+"invboots.png");
		woodtexture = TextureMapper.getTexture("res/blocks/plywood.png");
		axetexture = TextureMapper.getTexture("res/items/copperaxe.png");
		textinputtexture = TextureMapper.getTexture("res/menus/"+"textinput.png");
		
		startx = 100;
		starty = 600;
		if(scalex < scaley)scaley = scalex;
		if(scaley < scalex)scalex = scaley;
		
		
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
		crafting_buttons = new ArrayList<MyCraftingHandler>();
		for(i=0;i<50;i++){
			row = i/mod;
			col = i%mod;		
			buttons.add(new MyButtonHandler((int)((startx+cellsize*col)*scalex), (int)((starty-row*cellsize)*scaley), (int)(40*scalex), (int)(40*scaley), DangerZone.player.getInventory(i), 1, i)); //1 = inventory
		}
		
		//add hotbar buttons...
		starty -= 300;
		drawRectangleWithTexture(hottx, (startx-5)*scalex, (starty-5)*scaley, 500*scalex, 50*scaley);
		for(i=0;i<10;i++){
			row = 0;
			col = i;
			buttons.add(new MyButtonHandler((int)((startx+cellsize*col)*scalex), (int)((starty-row*cellsize)*scaley), (int)(40*scalex), (int)(40*scaley), DangerZone.player.getHotbar(i), 0, i)); //0 = hotbar
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
		
		//Normal 9-square crafting!
		starty = 600;
		startx = 775;
		drawRectangleWithTexture(craft9texture, (startx-2)*scalex, (starty-165)*scaley, 150*scalex, 150*scaley);
		drawRectangleWithTexture(craft1texture, (startx+180)*scalex, (starty-70)*scaley, 50*scalex, 50*scaley);

		//Now go back and fill in buttons
		for(i=0;i<9;i++){
			row = i/3;
			col = i%3;
			crafting_buttons.add(new MyCraftingHandler((int)((startx+cellsize*col+5)*scalex), (int)((starty-row*cellsize-59)*scaley), (int)(40*scalex), (int)(40*scaley), DangerZone.player.getCrafting(i), 0, i)); //0 = crafter
		}
		
		crafted = DangerZone.player.getCrafted();
		if(last_crafted != crafted){
			crafting = 1;
			last_crafted = crafted;
			if(last_crafted == null)crafting = 0;
		}
		
		if(crafting > 0){			
			crafting++;
		}
		
		if(!DangerZone.crafting_animation)crafting = 100;
		
		if(crafting >= 60){
			if(crafted != null)buttons.add(new MyCraftingHandler((int)((startx+185)*scalex), (int)((starty-65)*scaley), (int)(40*scalex), (int)(40*scaley), crafted, 1, 0)); //1 = crafted
			crafting = 61;
		}else{
			if(crafting > 0){
				drawRectangleWithTexture(woodtexture, (startx+185)*scalex, (starty-65)*scaley, 40*scalex, 40*scaley);
				drawRectangleWithTextureTwo(axetexture, (startx+190+(crafting%40))*scalex, (starty-35)*scaley, 40*scalex, 40*scaley, (crafting%20)-10, 0.5f);
			}
		}
	
		buttons.add(new MyButtonHandler((int)(650*scalex), (int)(500*scaley), (int)(50*scalex), (int)(50*scaley), deletetexture, null, 6, 0)); //delete button
		buttons.add(new MyButtonHandler((int)(650*scalex), (int)(560*scaley), (int)(50*scalex), (int)(50*scaley), tosstexture, null, -1, -1)); //toss button

		
		
		textAt((int)((startx+10)*scalex), (int)((starty-270)*scaley), "Search:");	
		GL11.glColor3f(1,1,1); //because text messes this up!

		buttons.add(new MyButtonHandler((int)((startx+10)*scalex), (int)((starty-290)*scaley), 150, 25, textinputtexture, isearch, 17, isearchinput?1.0f:0.5f));
		
		//add search items list...
		drawRectangleWithTexture(hottx, (startx-5)*scalex, (starty-355)*scaley, 500*scalex, 50*scaley);
		
		InventoryContainer icr;		
		Iterator<Recipe> ii = Crafting.recipies.iterator();
		Recipe r = null;
		Recipe rsave = null;
		String prevname = "";
		i = 0;
		while(i < 10){
			
			if(!ii.hasNext())break;
			r = ii.next();
			if(!matches(r.outname, isearch))continue;
			if(prevname.equals(r.outname))continue;
			
			icr = new InventoryContainer(r.outname, 1);			
			row = 0;
			col = i;
			buttons.add(new MyButtonHandler((int)((startx+cellsize*col)*scalex), (int)((starty-row*cellsize-350)*scaley), (int)(40*scalex), (int)(40*scaley), icr, 20+i, 0)); 
			if(i == curidx){
				rsave = r;
				drawRectangleWithTexture(textinputtexture, (int)(((startx-10)+cellsize*col)*scalex), (int)(((starty-10)-row*cellsize-350)*scaley), (int)(60*scalex), (int)(60*scaley));
			}
			i++;
			prevname = r.outname;
		}
		
		starty = 600;
		startx = 1075;
		drawRectangleWithTexture(craft9texture, (startx-2)*scalex, (starty-165)*scaley, 150*scalex, 150*scaley);

		if(rsave != null){
			//Now go back and fill in buttons
			for(i=0;i<9;i++){
				if(rsave.ingredients[i] == null)continue;
				icr = new InventoryContainer(rsave.ingredients[i], 1);
				row = i/3;
				col = i%3;
				buttons.add(new MyButtonHandler((int)((startx+cellsize*col+5)*scalex), (int)((starty-row*cellsize-59)*scaley), (int)(40*scalex), (int)(40*scaley), icr, -99, 0)); 
			}
		}

		
		
		
		
		
		//catch-all!
		//buttons.add(new MyButtonHandler(0, 0, DangerZone.screen_width, DangerZone.screen_height, null, null, -1, -1));
				
		//Now draw them!
		Iterator<ButtonHandler> bb = buttons.iterator();
		while(bb.hasNext()){
			bb.next().draw();
		}
		Iterator<MyCraftingHandler> bc = crafting_buttons.iterator();
		while(bc.hasNext()){
			bc.next().draw();
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
				drawRectangleWithTexture(tx, M_getX()-16, M_getY()-16, 32, 32);
			}
			if(s != null && !s.equals("")){
				textAt( M_getX()-16, M_getY()-16, s);
				GL11.glColor3f(1,1,1); //because text messes this up!
			}
		}
		

		
		
		//Check for mouse EVENTS on our button list!
		while(M_next()){
			clickx = M_getEventX();
			clicky = M_getEventY();
			if(!M_getEventButtonState()){ //Mouse button UP!				
				// 0 = left, 1 = right, 2 = middle
				if(M_getEventButton() == 0){	
					SpreadListClear();
				}
			}else{ //mouse button down
				if(M_getEventButton() >= 0){ //clicked!				
					//Find which "button" they clicked on
					//System.out.printf("down\n");
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
						if(fb != null){
							fb.leftclickhandler();
							SpreadListClear();
						}
					}
					if(M_getEventButton() == 1){
						if(fb != null){
							fb.rightclickhandler();
							SpreadListClear();
						}						
					}
					
/* crafting buttons */					
										
					bc = crafting_buttons.iterator();
					fb = null;
					while(bc.hasNext()){
						mb = bc.next();
						if(clickx >= mb.x && clickx <= mb.x+mb.xsize){
							if(clicky >= mb.y && clicky <= mb.y+mb.ysize){
								fb = mb;
								break;
							}
						}
					}
					if(fb != null){	
						// 0 = left, 1 = right, 2 = middle
						if(M_getEventButton() == 0){						
							MyCraftingHandler mch = (MyCraftingHandler)fb;
							SpreadListAdd(mch.index, shifted?1:0, 1); //down
						}
						if(M_getEventButton() == 1){
							if(fb != null){
								fb.rightclickhandler();
							}
							SpreadListClear();
						}
					}else{
						SpreadListClear();
					}

				}
				
			}
		}
		
		//handle mouse CURRENT position.
		
		//show some descriptive text and handle spreading!
		int cx, cy;
		cx = M_getX();
		cy = M_getY();
		bb = buttons.iterator();
		fb = null;
		while(bb.hasNext()){
			mb = bb.next();
			if(cx >= mb.x && cx <= mb.x+mb.xsize){
				if(cy >= mb.y && cy <= mb.y+mb.ysize){
					InventoryContainer ic = mb.ic;
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
		
		cx = M_getX();
		cy = M_getY();
		bc = crafting_buttons.iterator();
		MyCraftingHandler mch = null;
		while(bc.hasNext()){
			mch = bc.next();
			if(cx >= mch.x && cx <= mch.x+mch.xsize){
				if(cy >= mch.y && cy <= mch.y+mch.ysize){
					InventoryContainer ic = mch.ic;
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
					}else{
						SpreadListAdd(mch.index, shifted?1:0, 2); //drag
					}
				}
			}
		}


		if(isearchinput){
			String si = getTextChar();
			if(escaped){
				ImAllDone();
				return;
			}
			if(si != null){
				if(!si.equals("delete")){
					//add a new char
					if(isearch.length() < 16){
						isearch += si;
					}
				}else{
					//delete the last char
					if(isearch.length() > 0){
						String newstring = new String();
						for(i=0;i<isearch.length()-1;i++){
							newstring += isearch.charAt(i);
						}
						isearch = newstring;
					}
				}
			}

		}else{
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
		
	}
	
	public boolean matches(String checkthis, String containsthis){
		if(containsthis == null || containsthis.length() == 0)return true;
		if(checkthis == null || checkthis.length() == 0)return true;
		
		StringTokenizer st = new StringTokenizer(containsthis);
		while(st.hasMoreTokens()){
			String thisone = st.nextToken();
			if(!checkthis.toLowerCase().contains(thisone.toLowerCase()))return false;
		}		
		return true;
	}
	
	
	public void ImAllDone(){
		
		ClearTable();
		
		mousebite = null;
		crafted = null;
		buttons = null;
		last_crafted = null;
		
		super.ImAllDone();
	}

}
