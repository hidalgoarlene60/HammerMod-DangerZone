package dangerzone.blocks;
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


import dangerzone.BreakChecks;
import dangerzone.DangerZone;
import dangerzone.Player;
import dangerzone.StitchedTexture;
import dangerzone.Utils;
import dangerzone.VBOBuffer;
import dangerzone.World;
import dangerzone.WorldRenderer;
import dangerzone.entities.EntityBlockItem;
import dangerzone.items.Items;
import dangerzone.threads.VBODataBuilderThread;


public class BlockCorn extends Block {
	
	private boolean compiled = false; //graphics outputs compiled. Or not.
	private int myrenderid = 0;
	private float blockrenderwidth = 16;
		
	public BlockCorn(String n, String txt){
		super(n, txt);
		isSolidForRendering = false;
		isSolid = false;
		breaksound = "DangerZone:leavesbreak";
		placesound = "DangerZone:leavesplace";
		hitsound =   "DangerZone:leaves_hit";
		isLeaves = true;
		randomtick = true;
		maxdamage = 5;
		showInInventory = false;
		hasOwnRenderer = true;
		burntime = 15;
	}
	
	public void notifyNeighborChanged(World w, int d, int x, int y, int z){
		int bid = w.getblock(d, x, y-1, z);
		if(bid != Blocks.dirt.blockID 
				&& bid != Blocks.corn_plant.blockID && bid != Blocks.corn_plant1.blockID
				&& bid != Blocks.corn_plant2.blockID && bid != Blocks.corn_plant3.blockID){
			//Don't know what I'm on, but it's not for growing!
			w.setblock(d, x, y, z, 0);
			if(this.blockID == Blocks.corn_plant3.blockID){
				int i = w.rand.nextInt(3) + 1;				
				for(int j=0;j<i;j++){
					EntityBlockItem e = (EntityBlockItem)w.createEntityByName(DangerZone.blockitemname, d, (double)x+0.5f, (double)y+0.5f, (double)z+0.5f);
					if(e != null){
						e.fill(Items.corn, 1);		
						w.spawnEntityInWorld(e);
					}
					Utils.spawnExperience(1, w, d, (double)x+0.5f, (double)y+0.5f, (double)z+0.5f);
				}
			}
		}
	}
	
	//client-side only
	public int getBlockDrop(Player p, World w, int dimension, int x, int y, int z){
		//Player can be NULL! Make sure you check!
		return 0;
	}
	
	//client-side only
	public int getItemDrop(Player p, World w, int d, int x, int y, int z){
		//Player can be NULL! Make sure you check!
		if(this.blockID == Blocks.corn_plant3.blockID){
			Utils.spawnExperience(1, w, d, (double)x+0.5f, (double)y+0.5f, (double)z+0.5f);
			return Items.corn.itemID; //override this if you want to drop an item instead of a block.
		}
		return 0;
	}
	
	
	public void tickMe(World w, int d, int x, int y, int z){

		if(this.blockID != Blocks.corn_plant.blockID && this.blockID != Blocks.corn_plant1.blockID) return; 

		int i;    	
		int bid;
		int var6;
		int Height = 1;
		int dontGrow = 0;
		int myMaxHeight;
		
		if(w.rand.nextInt(14) != 1)return; //SLOW DOWN!
		if(!BreakChecks.canChangeBlock(w, d, x, y, z, d, x, y, z, 0, 0))return;

		myMaxHeight = w.getblockmeta(d, x, y, z);
		myMaxHeight &= 0xf;
		//System.out.printf("Corn counter = %d\n", var7); 
		if(myMaxHeight == 0)myMaxHeight = 4 + w.rand.nextInt(4); //Add some variation...
		
		bid = w.getblock(d, x, y + 1, z);
		if(bid == 0){
			for (var6 = 1; var6 < 10; var6++){
				bid = w.getblock(d, x, y - var6, z);
				if(bid != Blocks.corn_plant.blockID && bid != Blocks.corn_plant1.blockID && bid != Blocks.corn_plant2.blockID && bid != Blocks.corn_plant3.blockID ){
					break;
				}
				Height++;
				if(bid == Blocks.corn_plant2.blockID || bid == Blocks.corn_plant3.blockID){
					dontGrow = 1;
				}
			}

			if(dontGrow != 0){
				myMaxHeight = Height; //Something is hosed. Try to fix it.          
			}

			if(Height < myMaxHeight){
				//System.out.printf("Growing\n");
				w.setblockandmeta(d, x, y+1, z, Blocks.corn_plant.blockID, myMaxHeight); //Move top of plant up one block.          		
				w.setblockandmeta(d, x, y, z, Blocks.corn_plant1.blockID, myMaxHeight); //Now have a longer stem          		
			}else{
				//We grew up as much as we wanted to. Now mature the corn.            		
				for(i=1;i<(myMaxHeight-1);i++){
					bid = w.getblock(d, x, y - i, z);
					if(bid == Blocks.corn_plant1.blockID && w.rand.nextInt(2) == 0){            			
						w.setblockandmeta(d, x, y - i, z, Blocks.corn_plant2.blockID, myMaxHeight);            				

					}else if(bid == Blocks.corn_plant2.blockID && w.rand.nextInt(2) == 0){          			
						w.setblockandmeta(d, x, y - i, z, Blocks.corn_plant3.blockID, myMaxHeight);                     			
					}
				}
			}
		}
	}
	
	public void renderMeToVBO(long chunkvbos[], WorldRenderer wr, World w, int d, int x, int y, int z, int bid, int meta, int sides, boolean focus, int xo, int yo, int zo){

		VBOBuffer v = null;
		StitchedTexture st = null;
		
		st = VBODataBuilderThread.findVBOtextureforblockside(0, bid); //loads us into the stitching if we are not already!
		v = VBODataBuilderThread.findOrMakeVBOForTexture(chunkvbos, st, isTranslucent);
		
		if(v != null){
			v.addVertexInfoToVBO(blockrenderwidth/2 + xo, blockrenderwidth/2 + yo, blockrenderwidth/2 + zo, st.xoffsetmax, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 	
			v.addVertexInfoToVBO(-blockrenderwidth/2 + xo, blockrenderwidth/2 + yo, -blockrenderwidth/2 + zo, st.xoffsetmin, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
			v.addVertexInfoToVBO(-blockrenderwidth/2 + xo, -blockrenderwidth/2 + yo, -blockrenderwidth/2 + zo, st.xoffsetmin, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
			v.addVertexInfoToVBO(blockrenderwidth/2 + xo, -blockrenderwidth/2 + yo, blockrenderwidth/2 + zo, st.xoffsetmax, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb);
			
			v.addVertexInfoToVBO(blockrenderwidth/2 + xo, blockrenderwidth/2 + yo, -blockrenderwidth/2 + zo, st.xoffsetmax, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 	
			v.addVertexInfoToVBO(-blockrenderwidth/2 + xo, blockrenderwidth/2 + yo, blockrenderwidth/2 + zo, st.xoffsetmin, st.yoffsetmin, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
			v.addVertexInfoToVBO(-blockrenderwidth/2 + xo, -blockrenderwidth/2 + yo, blockrenderwidth/2 + zo, st.xoffsetmin, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 		
			v.addVertexInfoToVBO(blockrenderwidth/2 + xo, -blockrenderwidth/2 + yo, -blockrenderwidth/2 + zo, st.xoffsetmax, st.yoffsetmax, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb);
		}

	}
	
	public void renderMe(WorldRenderer wr, World w, int d, int x, int y, int z, int bid, int meta, int sides, boolean focus){

		if(!compiled){
			myrenderid = DangerZone.wr.getNextRenderID();
			GL11.glNewList(myrenderid, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			
			GL11.glTexCoord2f(1,0);
			GL11.glVertex3f(blockrenderwidth/2, blockrenderwidth/2, blockrenderwidth/2);
			GL11.glTexCoord2f(0,0);
			GL11.glVertex3f(-blockrenderwidth/2, blockrenderwidth/2, -blockrenderwidth/2);
			GL11.glTexCoord2f(0,1);
			GL11.glVertex3f(-blockrenderwidth/2, -blockrenderwidth/2, -blockrenderwidth/2); 
			GL11.glTexCoord2f(1,1);
			GL11.glVertex3f(blockrenderwidth/2, -blockrenderwidth/2, blockrenderwidth/2); 
			
			GL11.glTexCoord2f(1,0);
			GL11.glVertex3f(blockrenderwidth/2, blockrenderwidth/2, -blockrenderwidth/2); 
			GL11.glTexCoord2f(0,0);
			GL11.glVertex3f(-blockrenderwidth/2, blockrenderwidth/2, blockrenderwidth/2); 
			GL11.glTexCoord2f(0,1);
			GL11.glVertex3f(-blockrenderwidth/2, -blockrenderwidth/2, blockrenderwidth/2);
			GL11.glTexCoord2f(1,1);
			GL11.glVertex3f(blockrenderwidth/2, -blockrenderwidth/2, -blockrenderwidth/2); 
			
			GL11.glEnd(); // Done Drawing The Quad
			GL11.glEndList();			
			compiled = true;
		}

		wr.loadtextureforblockside(0, bid, false); //We only have one side. Tell world renderer to load our texture.
		
		//We are NOT solid
		//GL11.glEnable(GL11.GL_BLEND);
		//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glCallList(myrenderid); //draw
		
		//GL11.glDisable(GL11.GL_BLEND);

	}


}

