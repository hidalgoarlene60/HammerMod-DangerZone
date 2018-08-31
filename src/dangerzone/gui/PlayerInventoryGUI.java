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

import dangerzone.DangerZone;
import dangerzone.GameModes;
import dangerzone.InventoryContainer;
import dangerzone.TextureMapper;
import dangerzone.blocks.Blocks;
import dangerzone.items.Items;



public class PlayerInventoryGUI extends GuiInterface {
	
	List<ButtonHandler> buttons; //more like inventory squares, really, but they do nicely as buttons

	
	public InventoryContainer crafter[]; 
	public InventoryContainer crafted;
	public boolean shifted = false;
	public int start_offset = 0;
	public boolean middle_down = false;
	public String isearch = "";
	public boolean isearchinput = false;

	private int mode = 0;
	private int filter = InventoryMenus.GENERIC;
	
	
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
	
		
		
		public void middleclickhandler(){
			//just for quick delete for now...
			if(ih == 1){				
				ClickedInventory(index, 2, false);
			}
			if(ih == 9 || ih == 10 || ih == 11 || ih == 12){
				ClickedArmor(index, 2, false);
			}
			if(ih == 0){
				ClickedHotBar(index, 2, false);
			}

		}
		
		public void leftclickhandler(){
			/*
			System.out.printf("Left in %d,  %d\n", ih, index);
			*/
			
			if(ih != 17){
				isearchinput = false;
			}else{
				isearchinput = true;
			}

			if(ih == 9 || ih == 10 || ih == 11 || ih == 12){
				ClickedArmor(index, 0, shifted);
				return;
			}
									
			if(ih == 7){
				start_offset += 100;
				return;
			}
			if(ih == 8){
				start_offset -= 100;
				if(start_offset < 0)start_offset = 0;
				return;
			}
			if(ih == 4){
				mode = 0; //blocks
				start_offset = 0;
				filter = InventoryMenus.GENERIC;
				return;
			}
			if(ih == 5){
				mode = 1; //items
				start_offset = 0;
				filter = InventoryMenus.GENERIC;
				return;
			}
			if(ih == 13){
				mode = 1; //items
				start_offset = 0;
				filter = InventoryMenus.SPAWNEGG;
				return;
			}
			if(ih == 14){
				mode = 0; //blocks
				start_offset = 0;
				filter = InventoryMenus.SPAWNER;
				return;
			}
			if(ih == 15){
				mode = 1; //items
				start_offset = 0;
				filter = InventoryMenus.TROPHY;
				return;
			}
			if(ih == 16){
				mode = 1; //items
				start_offset = 0;
				filter = InventoryMenus.HARDWARE;
				return;
			}
			if(ih == 6){ //delete!
				DeleteMouseBite();
				return;
			}
			if(ih == 2){
				//blocks
				ClickedCreativeInventory(0, index, shifted);
				return;
			}
			if(ih == 3){
				//items
				ClickedCreativeInventory(index, 0, shifted);
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
			}else{
				ClickedHotBar(index, 1, shifted);
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
			if(ih == 9){					
				ClickedCrafted(0, shifted);
			}else{
				ClickedCrafting(index, 0, shifted);
			}
		}
		
		public void rightclickhandler(){
			if(ih == 9){					
				ClickedCrafted(1, shifted);
			}else{
				ClickedCrafting(index, 1, shifted);
			}
		}
	}
	
	public PlayerInventoryGUI(){
		super();
		
		start_offset = 0;
		buttons = null;
		mousebite = null;
		crafted = null;
		crafter = new InventoryContainer[4];
		isearch = "";
	}
	
	public void process(){
		Texture invtx = null;
		Texture hottx = null;
		Texture buttontexture = null;
		Texture deletetexture = null;
		Texture tosstexture = null;
		Texture craft1texture = null;
		Texture craft4texture = null;
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
		String s = null;
		int mod = 10;
		Texture tx = null;
		InventoryContainer ic = null;
		float scalex = DangerZone.screen_width/1350f;
		float scaley = DangerZone.screen_height/700f;
		
		invtx = TextureMapper.getTexture("res/menus/playerinventory.png");
		hottx = TextureMapper.getTexture("res/menus/playerhotbar.png");
		buttontexture = TextureMapper.getTexture("res/menus/"+"button.png");
		deletetexture = TextureMapper.getTexture("res/menus/"+"deletebutton.png");
		tosstexture = TextureMapper.getTexture("res/menus/"+"tossbutton.png");
		craft1texture = TextureMapper.getTexture("res/menus/"+"craft1.png");
		craft4texture = TextureMapper.getTexture("res/menus/"+"craft4.png");
		helmettexture = TextureMapper.getTexture("res/menus/"+"invhelmet.png");
		chestplatetexture = TextureMapper.getTexture("res/menus/"+"invchestplate.png");
		leggingstexture = TextureMapper.getTexture("res/menus/"+"invleggings.png");
		bootstexture = TextureMapper.getTexture("res/menus/"+"invboots.png");
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
		
		
		//Now add Gamemode 1 and 2 inventory!
		if(DangerZone.player.getGameMode() != GameModes.SURVIVAL){
			
			
			textAt((int)((startx+300)*scalex), (int)((starty+10)*scaley), "Search:");	
			GL11.glColor3f(1,1,1); //because text messes this up!

			buttons.add(new MyButtonHandler((int)((startx+300)*scalex), (int)((starty-10)*scaley), 150, 25, textinputtexture, isearch, 17, isearchinput?1.0f:0.5f));

			
			starty = 600;
			startx = 775;
			drawRectangleWithTexture(invtx, (startx-6)*scalex, (starty-205)*scaley, 500*scalex, 250*scaley);
			starty = 350;
			drawRectangleWithTexture(invtx, (startx-6)*scalex, (starty-205)*scaley, 500*scalex, 250*scaley);
			
			//Now go back and fill in buttons
			starty = 600;
			int found = 0;
			int start_found = 0;
			
			if(mode == 0){
				while(found == 0){
					start_found = 0;
					for(i=1;i<Blocks.blocksMAX && found < 100;i++){
						if(Blocks.BlockArray[i] != null){
							if(Blocks.shouldShow(i) && filter == Blocks.getMenu(i) && matches(Blocks.getUniqueName(i), isearch)){ 
								start_found++;
								if(start_found < start_offset+1)continue; //not yet, next page perhaps
								row = found/10;
								col = found%10;
								ic = new InventoryContainer();
								ic.bid = i;
								ic.count = 1;
								buttons.add(new MyButtonHandler((int)((startx+cellsize*col)*scalex), (int)((starty-row*cellsize)*scaley), (int)(40*scalex), (int)(40*scaley), ic, 2, i)); //2 = blocks
								found++;
							}
						}
					}
					if(found == 0){
						if(start_offset >= 100){
							start_offset -= 100;
						}else{
							break;
						}
					}
				}
			}else{
				while(found == 0){
					start_found = 0;
					for(i=1;i<Items.itemsMAX && found < 100;i++){
						if(Items.ItemArray[i] != null){
							if(Items.shouldShow(i) && filter == Items.getMenu(i) && matches(Items.getUniqueName(i), isearch)){ 
								start_found++;
								if(start_found < start_offset+1)continue; //not yet, next page perhaps
								row = found/10;
								col = found%10;
								ic = new InventoryContainer();
								ic.iid = i;
								ic.count = 1;
								buttons.add(new MyButtonHandler((int)((startx+cellsize*col)*scalex), (int)((starty-row*cellsize)*scaley), (int)(40*scalex), (int)(40*scaley), ic, 3, i)); //3 = items
								found++;
							}
						}
					}
					if(found == 0){
						if(start_offset >= 100){
							start_offset -= 100;
						}else{
							break;
						}
					}
				}
			}
			
			//Blocks/Items switch
			buttons.add(new MyButtonHandler((int)(650*scalex), (int)(450*scaley), (int)(100*scalex), (int)(25*scaley), buttontexture, "Blocks", 4, 0)); //4 = blocks mode
			buttons.add(new MyButtonHandler((int)(650*scalex), (int)(425*scaley), (int)(100*scalex), (int)(25*scaley), buttontexture, "Items", 5, 0)); //5 - items mode
			buttons.add(new MyButtonHandler((int)(650*scalex), (int)(400*scaley), (int)(100*scalex), (int)(25*scaley), buttontexture, "Hardware", 16, 0)); //
			buttons.add(new MyButtonHandler((int)(650*scalex), (int)(375*scaley), (int)(100*scalex), (int)(25*scaley), buttontexture, "Spawners", 14, 0)); //
			buttons.add(new MyButtonHandler((int)(650*scalex), (int)(350*scaley), (int)(100*scalex), (int)(25*scaley), buttontexture, "Trophies", 15, 0)); //
			buttons.add(new MyButtonHandler((int)(650*scalex), (int)(325*scaley), (int)(100*scalex), (int)(25*scaley), buttontexture, "Eggs", 13, 0)); //
			
			//prev-next buttons
			buttons.add(new MyButtonHandler((int)(675*scalex), (int)(175*scaley), (int)(75*scalex), (int)(25*scaley), buttontexture, "Next", 7, 0));
			buttons.add(new MyButtonHandler((int)(675*scalex), (int)(150*scaley), (int)(75*scalex), (int)(25*scaley), buttontexture, "Prev", 8, 0)); 
			
		}else{
			//Normal 4-square crafting!
			if(crafter == null)crafter = new InventoryContainer[4];
			crafted = DangerZone.player.getCrafted();
			for(i=0;i<4;i++){
				crafter[i] = DangerZone.player.getCrafting(i);
			}
			starty = 600;
			startx = 775;
			drawRectangleWithTexture(craft4texture, (startx)*scalex, (starty-115)*scaley, 100*scalex, 100*scaley);
			drawRectangleWithTexture(craft1texture, (startx+145)*scalex, (starty-65)*scaley, 50*scalex, 50*scaley);
			
			//Now go back and fill in buttons
			starty = 600;
			
			for(i=0;i<crafter.length;i++){
				row = i/2;
				col = i%2;
				buttons.add(new MyCraftingHandler((int)((startx+cellsize*col+5)*scalex), (int)((starty-row*cellsize-59)*scaley), (int)(40*scalex), (int)(40*scaley), crafter[i], 8, i)); //8 = crafter
			}
			
			/*
			crafted = null;
			Recipe r = Crafting.find(crafter[0]==null?null:crafter[0].getUniqueName(), crafter[1]==null?null:crafter[1].getUniqueName(), null,
									crafter[2]==null?null:crafter[2].getUniqueName(), crafter[3]==null?null:crafter[3].getUniqueName(), null,
									null, null, null);
			if(r != null){
				crafted = new InventoryContainer(r.outname, r.out_count);
			}
			*/
			buttons.add(new MyCraftingHandler((int)((startx+150)*scalex), (int)((starty-59)*scaley), (int)(40*scalex), (int)(40*scaley), crafted, 9, 0)); //9 = crafted
			
		}

		buttons.add(new MyButtonHandler((int)(650*scalex), (int)(530*scaley), (int)(50*scalex), (int)(50*scaley), deletetexture, null, 6, 0)); //delete button
		buttons.add(new MyButtonHandler((int)(650*scalex), (int)(590*scaley), (int)(50*scalex), (int)(50*scaley), tosstexture, null, -1, -1)); //toss button
		
		
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
						middle_down = false;
					}
					if(M_getEventButton() == 1){
						if(fb != null)fb.rightclickhandler();
						middle_down = false;
					}
					if(M_getEventButton() == 2){
						if(fb != null)fb.middleclickhandler();
						middle_down = true;
					}				
				}
			}else{
				if(M_getEventButton() >= 0){ //released!		
					middle_down = false;
				}
			}
		}
		
		//enable dragging the middle button! (MASS DELETE!)
		if(middle_down){
			clickx = M_getX();
			clicky = M_getY();
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
			if(fb != null)fb.middleclickhandler();
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
							int attackstrength = Items.getAttackStrength(ic.iid);
							if(attackstrength > 1){
								hotstring += String.format(" +%d", attackstrength);
							}		
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
		crafter = null;
		
		super.ImAllDone();
	}
	


}
