package dangerzone.items;
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

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.newdawn.slick.opengl.Texture;

import dangerzone.DangerZone;
import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.TextureMapper;
import dangerzone.World;
import dangerzone.WorldRenderer;
import dangerzone.entities.Entity;
import dangerzone.gui.InventoryMenus;



public class Item {
	public int itemID;
	public String texturepath;
	public String uniquename;
	private Texture texture = null;
	public int maxstack = 64;
	private int compiled_quads_id;
	public int maxuses;
	public int attackstrength;
	public int stonestrength;
	public int woodstrength;
	public int dirtstrength;
	public int energy = 0;
	public boolean showInInventory = true;
	public float brightness = 0.0f; //range -2.0 to about 2.0 - adder! less than 0 = darker, greater than 0 = brighter
	public boolean isfood = false;
	public boolean eatanytime = false;
	public float foodvalue = 0;
	public int burntime = 0;
	public int menu = InventoryMenus.GENERIC;
	public boolean flipped = false; //rendering flag
	public boolean flopped = false; //another rendering flag
	public boolean hold_straight = false;

	
	public Item(String n, String txt){
		texturepath = null;
		itemID = 0;
		texturepath = txt;
		uniquename = n;
		maxstack = 64;
		compiled_quads_id = 0;
		attackstrength = stonestrength = woodstrength = dirtstrength = 1;
		maxuses = 0;
		isfood = false;
		eatanytime = false;
		foodvalue = 0;
	}
	
	public void inUseTick(Entity e, InventoryContainer ic, int invindex){
		//Ticks the currently Held Item! (client AND server)
	}
	
	public void inventoryTick(Entity e, InventoryContainer ic, int invindex){
		//Ticks the item anywhere in inventory (server only)
	}
		
	/*
	 * Default 3D item renderer. Override it if you have your own special item rendering.
	 * Used for EntityBlockItems (drops) and while being held by Player.
	 * 
	 * Inventory drawing uses a standard square and your default texture, not this routine.
	 */
	public void renderMe(WorldRenderer wr, World w, int d, int x, int y, int z, int iid){
		//texture is already auto-loaded for us
		if(compiled_quads_id == 0){
			compile_item();
		}
		if(compiled_quads_id != 0){
			GL11.glCallList(compiled_quads_id);	
			GL14.glSecondaryColor3f(0.0f, 0.0f, 0.0f);
		}
	}
	
	public void renderMeHeld(WorldRenderer wr, Entity e, int iid, boolean isdisplay){
		if(e == null)return;
		renderMe( wr,  e.world,  e.dimension, (int)e.posx, (int)e.posy, (int)e.posz,  iid);
	}
	
	public boolean onLeftClick(Entity holder, Entity clickedon, InventoryContainer ic){
		return true; //continue with normal left click logic, else it is handled special here
	}
	
	//called only if NOT clicked on a block. (air or entity)
	public boolean onRightClick(Entity holder, Entity clickedon, InventoryContainer ic){
		return false; //return TRUE if your item did something and should be deleted/decremented
	}
	
	public boolean rightclickup(Entity ent, InventoryContainer ic, int holdcount){
		//holdcount is roughly tenths of a second
		return false; //inc currentuses
	}
	
	public float getfullholdcount(){
		//for display so we can scale, stretch, fill something, whatever, to show progress.
		return 0;
	}
	
	public void onCrafted(Player p, InventoryContainer ic){
	}
	
	/*
	 * ONLY works with 16*16 8-bit RGBA .png files.
	 * Draws only the exterior of the item by checking each side of each pixel.
	 */
	private void compile_item(){

		int curval;
		int i, j;
		byte[] data;
		Texture t;

		t = getTexture();
		if(t == null)return;
		data = t.getTextureData();
		compiled_quads_id = DangerZone.wr.getNextRenderID();

		GL11.glNewList(compiled_quads_id, GL11.GL_COMPILE);
		GL11.glBegin(GL11.GL_QUADS);
		
		if(!flopped){

		for(i=0;i<16;i++){
			for(j=0;j<16;j++){
				curval = gimmedataat(data, i, j); //Get a pixel
				if(curval >= 0){					
					/*
					 * The -8, -8 moves the center to the center of the image.
					 */
					
					GL14.glSecondaryColor3f(0.02f, 0.02f, 0.02f);
					GL11.glTexCoord2f((float)j/16f+0.0625f, (float)i/16f);
					GL11.glVertex3f(-8+j+1, -8+i+1, 0); // Top Right
					GL11.glTexCoord2f((float)j/16f, (float)i/16f);
					GL11.glVertex3f(-8+j+0, -8+i+1, 0); // Top Left
					GL11.glTexCoord2f((float)j/16f, (float)i/16f+0.0625f);
					GL11.glVertex3f(-8+j+0, -8+i+0, 0); // Bottom Left
					GL11.glTexCoord2f((float)j/16f+0.0625f, (float)i/16f+0.0625f);
					GL11.glVertex3f(-8+j+1, -8+i+0, 0); // Bottom Right

					
					GL14.glSecondaryColor3f(0.02f, 0.02f, 0.02f);
					GL11.glTexCoord2f((float)j/16f+0.0625f, (float)i/16f);
					GL11.glVertex3f(-8+j+1, -8+i+1, 1); // Top Right
					GL11.glTexCoord2f((float)j/16f, (float)i/16f);
					GL11.glVertex3f(-8+j+0, -8+i+1, 1); // Top Left
					GL11.glTexCoord2f((float)j/16f, (float)i/16f+0.0625f);
					GL11.glVertex3f(-8+j+0, -8+i+0, 1); // Bottom Left
					GL11.glTexCoord2f((float)j/16f+0.0625f, (float)i/16f+0.0625f);
					GL11.glVertex3f(-8+j+1, -8+i+0, 1); // Bottom Right
					
					
					if(gimmedataat(data, i-1, j) < 0){
						GL14.glSecondaryColor3f(0.04f, 0.04f, 0.04f);
						GL11.glTexCoord2f((float)j/16f+0.0625f, (float)i/16f);
						GL11.glVertex3f(-8+j+1, -8+i+0, 1); // Top Right
						GL11.glTexCoord2f((float)j/16f, (float)i/16f);
						GL11.glVertex3f(-8+j+0, -8+i+0, 1); // Top Left
						GL11.glTexCoord2f((float)j/16f, (float)i/16f+0.0625f);
						GL11.glVertex3f(-8+j+0, -8+i+0, 0); // Bottom Left
						GL11.glTexCoord2f((float)j/16f+0.0625f, (float)i/16f+0.0625f);
						GL11.glVertex3f(-8+j+1, -8+i+0, 0); // Bottom Right
					}

					
					if(gimmedataat(data, i+1, j) < 0){
						GL14.glSecondaryColor3f(0.04f, 0.04f, 0.04f);
						GL11.glTexCoord2f((float)j/16f+0.0625f, (float)i/16f);
						GL11.glVertex3f(-8+j+1, -8+i+1, 1); // Top Right
						GL11.glTexCoord2f((float)j/16f, (float)i/16f);
						GL11.glVertex3f(-8+j+0, -8+i+1, 1); // Top Left
						GL11.glTexCoord2f((float)j/16f, (float)i/16f+0.0625f);
						GL11.glVertex3f(-8+j+0, -8+i+1, 0); // Bottom Left
						GL11.glTexCoord2f((float)j/16f+0.0625f, (float)i/16f+0.0625f);
						GL11.glVertex3f(-8+j+1, -8+i+1, 0); // Bottom Right
					}

					
					if(gimmedataat(data, i, j-1) < 0){
						GL14.glSecondaryColor3f(0f, 0f, 0f);
						GL11.glTexCoord2f((float)j/16f+0.0625f, (float)i/16f);
						GL11.glVertex3f(-8+j+0, -8+i+1, 0); // Top Right
						GL11.glTexCoord2f((float)j/16f, (float)i/16f);
						GL11.glVertex3f(-8+j+0, -8+i+1, 1); // Top Left
						GL11.glTexCoord2f((float)j/16f, (float)i/16f+0.0625f);
						GL11.glVertex3f(-8+j+0, -8+i+0, 1); // Bottom Left
						GL11.glTexCoord2f((float)j/16f+0.0625f, (float)i/16f+0.0625f);
						GL11.glVertex3f(-8+j+0, -8+i+0, 0); // Bottom Right
					}

					
					if(gimmedataat(data, i, j+1) < 0){
						GL14.glSecondaryColor3f(0f, 0f, 0f);
						GL11.glTexCoord2f((float)j/16f+0.0625f, (float)i/16f);
						GL11.glVertex3f(-8+j+1, -8+i+1, 1); // Top Right
						GL11.glTexCoord2f((float)j/16f, (float)i/16f);
						GL11.glVertex3f(-8+j+1, -8+i+1, 0); // Top Left
						GL11.glTexCoord2f((float)j/16f, (float)i/16f+0.0625f);
						GL11.glVertex3f(-8+j+1, -8+i+0, 0); // Bottom Left
						GL11.glTexCoord2f((float)j/16f+0.0625f, (float)i/16f+0.0625f);
						GL11.glVertex3f(-8+j+1, -8+i+0, 1); // Bottom Right
					}
				}
			}
		}
		
		}else{
			
			for(i=0;i<16;i++){
				for(j=0;j<16;j++){
					curval = gimmedataat(data, i, j); //Get a pixel
					if(curval >= 0){					
						/*
						 * The -8, -8 moves the center to the center of the image.
						 */
						
						GL14.glSecondaryColor3f(0.02f, 0.02f, 0.02f);
						GL11.glTexCoord2f((float)j/16f+0.0625f, (float)i/16f);
						GL11.glVertex3f(8-j+1, 8-i+1, 0); // Top Right
						GL11.glTexCoord2f((float)j/16f, (float)i/16f);
						GL11.glVertex3f(8-j+0, 8-i+1, 0); // Top Left
						GL11.glTexCoord2f((float)j/16f, (float)i/16f+0.0625f);
						GL11.glVertex3f(8-j+0, 8-i+0, 0); // Bottom Left
						GL11.glTexCoord2f((float)j/16f+0.0625f, (float)i/16f+0.0625f);
						GL11.glVertex3f(8-j+1, 8-i+0, 0); // Bottom Right

						
						GL14.glSecondaryColor3f(0.02f, 0.02f, 0.02f);
						GL11.glTexCoord2f((float)j/16f+0.0625f, (float)i/16f);
						GL11.glVertex3f(8-j+1, 8-i+1, 1); // Top Right
						GL11.glTexCoord2f((float)j/16f, (float)i/16f);
						GL11.glVertex3f(8-j+0, 8-i+1, 1); // Top Left
						GL11.glTexCoord2f((float)j/16f, (float)i/16f+0.0625f);
						GL11.glVertex3f(8-j+0, 8-i+0, 1); // Bottom Left
						GL11.glTexCoord2f((float)j/16f+0.0625f, (float)i/16f+0.0625f);
						GL11.glVertex3f(8-j+1, 8-i+0, 1); // Bottom Right
						
						
						if(gimmedataat(data, i+1, j) < 0){
							GL14.glSecondaryColor3f(0.04f, 0.04f, 0.04f);
							GL11.glTexCoord2f((float)j/16f+0.0625f, (float)i/16f);
							GL11.glVertex3f(8-j+1, 8-i+0, 1); // Top Right
							GL11.glTexCoord2f((float)j/16f, (float)i/16f);
							GL11.glVertex3f(8-j+0, 8-i+0, 1); // Top Left
							GL11.glTexCoord2f((float)j/16f, (float)i/16f+0.0625f);
							GL11.glVertex3f(8-j+0, 8-i+0, 0); // Bottom Left
							GL11.glTexCoord2f((float)j/16f+0.0625f, (float)i/16f+0.0625f);
							GL11.glVertex3f(8-j+1, 8-i+0, 0); // Bottom Right
						}

						
						if(gimmedataat(data, i-1, j) < 0){
							GL14.glSecondaryColor3f(0.04f, 0.04f, 0.04f);
							GL11.glTexCoord2f((float)j/16f+0.0625f, (float)i/16f);
							GL11.glVertex3f(8-j+1, 8-i+1, 1); // Top Right
							GL11.glTexCoord2f((float)j/16f, (float)i/16f);
							GL11.glVertex3f(8-j+0, 8-i+1, 1); // Top Left
							GL11.glTexCoord2f((float)j/16f, (float)i/16f+0.0625f);
							GL11.glVertex3f(8-j+0, 8-i+1, 0); // Bottom Left
							GL11.glTexCoord2f((float)j/16f+0.0625f, (float)i/16f+0.0625f);
							GL11.glVertex3f(8-j+1, 8-i+1, 0); // Bottom Right
						}

						
						if(gimmedataat(data, i, j+1) < 0){
							GL14.glSecondaryColor3f(0f, 0f, 0f);
							GL11.glTexCoord2f((float)j/16f+0.0625f, (float)i/16f);
							GL11.glVertex3f(8-j+0, 8-i+1, 0); // Top Right
							GL11.glTexCoord2f((float)j/16f, (float)i/16f);
							GL11.glVertex3f(8-j+0, 8-i+1, 1); // Top Left
							GL11.glTexCoord2f((float)j/16f, (float)i/16f+0.0625f);
							GL11.glVertex3f(8-j+0, 8-i+0, 1); // Bottom Left
							GL11.glTexCoord2f((float)j/16f+0.0625f, (float)i/16f+0.0625f);
							GL11.glVertex3f(8-j+0, 8-i+0, 0); // Bottom Right
						}

						
						if(gimmedataat(data, i, j-1) < 0){
							GL14.glSecondaryColor3f(0f, 0f, 0f);
							GL11.glTexCoord2f((float)j/16f+0.0625f, (float)i/16f);
							GL11.glVertex3f(8-j+1, 8-i+1, 1); // Top Right
							GL11.glTexCoord2f((float)j/16f, (float)i/16f);
							GL11.glVertex3f(8-j+1, 8-i+1, 0); // Top Left
							GL11.glTexCoord2f((float)j/16f, (float)i/16f+0.0625f);
							GL11.glVertex3f(8-j+1, 8-i+0, 0); // Bottom Left
							GL11.glTexCoord2f((float)j/16f+0.0625f, (float)i/16f+0.0625f);
							GL11.glVertex3f(8-j+1, 8-i+0, 1); // Bottom Right
						}
					}
				}
			}
			
		}
		
		
		GL11.glEnd(); // Done Drawing The Quads
		GL11.glEndList();
	}
	
	/*
	 * ONLY works with 8-bit RGBA png files.
	 * ONLY works with 16x16.
	 */
	private int gimmedataat(byte[] data, int x, int y){
		if(x < 0 || x >= 16)return -1;
		if(y < 0 || y >= 16)return -1;
		//System.out.printf("data %d at %d, %d is: %d, %d, %d, %d\n", data.length, x, y, data[((x*16)+y)*4], data[((x*16)+y)*4 + 1], data[((x*16)+y)*4 + 2], data[((x*16)+y)*4 + 3] );		
		if(data[((x*16)+y)*4 + 3] >= 0 && data[((x*16)+y)*4 + 3] < 100)return -1; //check alpha first! Arbitrary alpha min. Should really always be 0 or -1 (255) for solid colors.		
		return (data[((x*16)+y)*4] + data[((x*16)+y)*4 + 1] + data[((x*16)+y)*4 + 2])&0xffffff;
	}
	
	public Texture getTexture(){
		if(texture == null){
			texture = TextureMapper.getTexture(texturepath);
		}
		return texture;
	}

	//Player right-clicked on this block with this item
	public boolean rightClickOnBlock(Player p, int dimension, int x, int y, int z, int side){
		return false; //return TRUE if your item did something and should be deleted/decremented
	}
	
	//Player left-clicked on this block with this item
	public void leftClickOnBlock(Player p, int dimension, int x, int y, int z, int side){
		
	}
	
	public void onFoodEaten(Entity e){
	}
	
	public void onBlockBroken(Entity ent, int dimension, int x, int y, int z, int wasbid){
		//ent is player. server side!
	}
	
	
}
