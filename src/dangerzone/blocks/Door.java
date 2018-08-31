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
import dangerzone.VBOBuffer;
import dangerzone.World;
import dangerzone.WorldRenderer;
import dangerzone.items.Items;
import dangerzone.threads.VBODataBuilderThread;


public class Door extends Block {
	private boolean compiled = false; //graphics outputs compiled. Or not.
	private int myrenderid = 0;
	private float blockrenderwidth = 16;
		
	public Door(String n, String txt){
		super(n, txt);
		breaksound = "DangerZone:woodbreak"; 
		placesound = "DangerZone:woodplace"; 
		hitsound =   "DangerZone:woodhit";
		isSolidForRendering = false;
		isSolid = true;
		hasOwnRenderer = true;	
		maxdamage = 100;
		burntime = 65;
		hasFront = true;
		showInInventory = false;
	}
	
	public String getStepSound(){
		int i = DangerZone.rand.nextInt(4);
		if(i == 0)return "DangerZone:wood1";
		if(i == 1)return "DangerZone:wood2";
		if(i == 2)return "DangerZone:wood3";
		return "DangerZone:wood4";
	}
	
	public void notifyNeighborChanged(World w, int d, int x, int y, int z){
		int bid = 0;
		
		if(!w.isServer)return;
		bid = w.getblock(d, x, y, z);
		if(bid != Blocks.doortop.blockID && bid != Blocks.doorbottom.blockID)return;
		
		if(!BreakChecks.canChangeBlock(w, d, x, y, z, d, x, y, z, 0, 0))return;
		if(blockID == Blocks.doorbottom.blockID){
			bid = w.getblock(d, x, y+1, z);
			if(bid != Blocks.doortop.blockID){
				w.setblock(d, x, y, z, 0);
			}
		}else{
			bid = w.getblock(d, x, y-1, z);
			if(bid != Blocks.doorbottom.blockID){
				w.setblock(d, x, y, z, 0);
			}
		}
	}
	
	public boolean getIsSolid(World w, int d, int x, int y, int z){
		int meta = w.getblockmeta(d, x, y, z);
		if((meta&0x01)==0x01){
			return false;
		}
		return true;
	}
	
	//client-side only
	public int getBlockDrop(Player p, World w, int dimension, int x, int y, int z){		
		//Player can be NULL! Make sure you check!
		return 0; //standard default return
	}
	
	//client-side only
	public int getItemDrop(Player p, World w, int dimension, int x, int y, int z){
		return Items.door.itemID;
	}
	
	//Player right-clicked on this block
	//client-side only
	public boolean rightClickOnBlock(Player p, int dimension, int x, int y, int z){
		if(p != null){
			if(p.world.isServer){
				int meta = p.world.getblockmeta(p.dimension, x, y, z);
				if((meta&0x01)==0x01){
					meta &= 0xfffe; //mask it off! //close door
					if(p.world.rand.nextBoolean()){
						p.world.playSound("DangerZone:door_close1", dimension, x, y, z, 0.75f, 1.0f);
					}else{
						p.world.playSound("DangerZone:door_close2", dimension, x, y, z, 0.75f, 1.0f);
					}
				}else{
					meta |= 0x01; //open door
					if(p.world.rand.nextBoolean()){
						p.world.playSound("DangerZone:door_open1", dimension, x, y, z, 0.75f, 1.0f);
					}else{
						p.world.playSound("DangerZone:door_open2", dimension, x, y, z, 0.75f, 1.0f);
					}
				}
				if(!BreakChecks.canChangeBlock(p, dimension, x, y, z, blockID, meta))return false;
				p.world.setblockandmeta(p.dimension, x, y, z, blockID, meta);
				
				//adjust our other half
				if(blockID == Blocks.doortop.blockID){
					p.world.setblockandmeta(p.dimension, x, y-1, z, Blocks.doorbottom.blockID, meta);
				}else{
					p.world.setblockandmeta(p.dimension, x, y+1, z, Blocks.doortop.blockID, meta);
				}
			}
		}
		return false; //NO block placement here!
	}
	
	public void renderMeToVBO(long chunkvbos[], WorldRenderer wr, World w, int d, int x, int y, int z, int bid, int inmeta, int sides, boolean focus, int xo, int yo, int zo){

		VBOBuffer v = null;
		StitchedTexture st = null;
		float brw = blockrenderwidth/2;
		float xoff = 0;
		int meta = inmeta;
			
		st = VBODataBuilderThread.findVBOtextureforblockside(0, bid); //loads us into the stitching if we are not already!
		if(st == null)return;
		v = VBODataBuilderThread.findOrMakeVBOForTexture(chunkvbos, st, isTranslucent);
		if(v == null)return;
		
		if((inmeta&0x01)==0x01){
			xoff = 7; //door is open!
			//now fix open rotation to match door open
			if((inmeta&BlockRotation.Y_MASK) == BlockRotation.Y_ROT_0){
				meta = BlockRotation.Y_ROT_90;
			}else if((inmeta&BlockRotation.Y_MASK) == BlockRotation.Y_ROT_90){
				meta = BlockRotation.Y_ROT_180;
			}else if((inmeta&BlockRotation.Y_MASK) == BlockRotation.Y_ROT_180){
				meta = BlockRotation.Y_ROT_270;
			}else{
				meta = BlockRotation.Y_ROT_0;
			}			
		}
		
		//if((sides & 0x20) != 0){
			addVertextWithRotation(v, 1 + xoff, brw, -brw, xo, yo, zo, st.xoffsetmax, st.yoffsetmin + ((st.yoffsetmax-st.yoffsetmin)/8), meta);		
			addVertextWithRotation(v, -1 + xoff, brw, -brw, xo, yo, zo, st.xoffsetmax, st.yoffsetmin, meta);		
			addVertextWithRotation(v, -1 + xoff, brw, brw, xo, yo, zo, st.xoffsetmin, st.yoffsetmin, meta);
			addVertextWithRotation(v, 1 + xoff, brw, brw, xo, yo, zo, st.xoffsetmin, st.yoffsetmin + ((st.yoffsetmax-st.yoffsetmin)/8), meta);
		//}
		
		//if((sides & 0x10) != 0){
			addVertextWithRotation(v, 1 + xoff, -brw, brw, xo, yo, zo, st.xoffsetmax, st.yoffsetmin + ((st.yoffsetmax-st.yoffsetmin)/8), meta); 			
			addVertextWithRotation(v, -1 + xoff, -brw, brw, xo, yo, zo, st.xoffsetmax, st.yoffsetmin, meta); 			
			addVertextWithRotation(v, -1 + xoff, -brw, -brw, xo, yo, zo, st.xoffsetmin, st.yoffsetmin, meta); 			
			addVertextWithRotation(v, 1 + xoff, -brw, -brw, xo, yo, zo, st.xoffsetmin, st.yoffsetmin + ((st.yoffsetmax-st.yoffsetmin)/8), meta);
		//}
		
		//if((sides & 0x08) != 0){
			addVertextWithRotation(v, 1 + xoff, brw, brw, xo, yo, zo, st.xoffsetmin + ((st.xoffsetmax-st.xoffsetmin)/8), st.yoffsetmin, meta); 	
			addVertextWithRotation(v, -1 + xoff, brw, brw, xo, yo, zo, st.xoffsetmin, st.yoffsetmin, meta); 		
			addVertextWithRotation(v, -1 + xoff, -brw, brw, xo, yo, zo, st.xoffsetmin, st.yoffsetmax, meta); 		
			addVertextWithRotation(v, 1 + xoff, -brw, brw, xo, yo, zo, st.xoffsetmin + ((st.xoffsetmax-st.xoffsetmin)/8), st.yoffsetmax, meta);
		//}
			
		//if((sides & 0x04) != 0){
			addVertextWithRotation(v, -1 + xoff, brw, -brw, xo, yo, zo, st.xoffsetmin + ((st.xoffsetmax-st.xoffsetmin)/8), st.yoffsetmin, meta); 
			addVertextWithRotation(v, 1 + xoff, brw, -brw, xo, yo, zo, st.xoffsetmin, st.yoffsetmin, meta); 
			addVertextWithRotation(v, 1 + xoff, -brw, -brw, xo, yo, zo, st.xoffsetmin, st.yoffsetmax, meta); 
			addVertextWithRotation(v, -1 + xoff, -brw, -brw, xo, yo, zo, st.xoffsetmin + ((st.xoffsetmax-st.xoffsetmin)/8), st.yoffsetmax, meta); 
		//}
		
		//front
		//if((sides & 0x01) != 0){
			addVertextWithRotation(v, -1 + xoff, brw, brw, xo, yo, zo, st.xoffsetmax, st.yoffsetmin, meta);
			addVertextWithRotation(v, -1 + xoff, brw, -brw, xo, yo, zo, st.xoffsetmin, st.yoffsetmin, meta); 
			addVertextWithRotation(v, -1 + xoff, -brw, -brw, xo, yo, zo, st.xoffsetmin, st.yoffsetmax, meta); 
			addVertextWithRotation(v, -1 + xoff, -brw, brw, xo, yo, zo, st.xoffsetmax, st.yoffsetmax, meta);
		//}
		//back
		//if((sides & 0x02) != 0){
			addVertextWithRotation(v, 1 + xoff, brw, -brw, xo, yo, zo, st.xoffsetmin, st.yoffsetmin, meta); 
			addVertextWithRotation(v, 1 + xoff, brw, brw, xo, yo, zo, st.xoffsetmax, st.yoffsetmin, meta); 
			addVertextWithRotation(v, 1 + xoff, -brw, brw, xo, yo, zo, st.xoffsetmax, st.yoffsetmax, meta); 
			addVertextWithRotation(v, 1 + xoff, -brw, -brw, xo, yo, zo, st.xoffsetmin, st.yoffsetmax, meta); 
		//}

	}
	
	public void addVertextWithRotation(VBOBuffer v, float x, float y, float z, float xo, float yo, float zo, float xtx, float ytx, int meta){
		if((meta & BlockRotation.Y_MASK) == 0){			
			v.addVertexInfoToVBO(z + xo, y + yo, -x + zo, xtx, ytx, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 
		}else if((meta & BlockRotation.Y_MASK) == BlockRotation.Y_ROT_90){
			v.addVertexInfoToVBO(-x + xo, y + yo, -z + zo, xtx, ytx, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 
		}else if((meta & BlockRotation.Y_MASK) == BlockRotation.Y_ROT_180){
			v.addVertexInfoToVBO(-z + xo, y + yo, x + zo, xtx, ytx, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb); 
		}else{							
			v.addVertexInfoToVBO(x + xo, y + yo, z + zo, xtx, ytx, VBODataBuilderThread.cbr, VBODataBuilderThread.cbg, VBODataBuilderThread.cbb);
		}
	}
	
	public void renderMe(WorldRenderer wr, World w, int d, int x, int y, int z, int bid, int meta, int sides, boolean focus){

		if(!compiled){
			myrenderid = DangerZone.wr.getNextRenderID();
			GL11.glNewList(myrenderid, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			
			GL11.glTexCoord2f(1,0.125f);
			GL11.glVertex3f(1, blockrenderwidth/2, -blockrenderwidth/2); // Top Right Of The Quad (Top)
			GL11.glTexCoord2f(1,0);
			GL11.glVertex3f(-1, blockrenderwidth/2, -blockrenderwidth/2); // Top Left Of The Quad (Top)
			GL11.glTexCoord2f(0,0);
			GL11.glVertex3f(-1, blockrenderwidth/2, blockrenderwidth/2); // Bottom Left Of The Quad (Top)
			GL11.glTexCoord2f(0,0.125f);
			GL11.glVertex3f(1, blockrenderwidth/2, blockrenderwidth/2); // Bottom Right Of The Quad (Top)
			
			GL11.glTexCoord2f(1,0.125f);
			GL11.glVertex3f(1, -blockrenderwidth/2, blockrenderwidth/2); // Top Right Of The Quad (Bottom)
			GL11.glTexCoord2f(1,0);
			GL11.glVertex3f(-1, -blockrenderwidth/2, blockrenderwidth/2); // Top Left Of The Quad (Bottom)
			GL11.glTexCoord2f(0,0);
			GL11.glVertex3f(-1, -blockrenderwidth/2, -blockrenderwidth/2); // Bottom Left Of The Quad (Bottom)
			GL11.glTexCoord2f(0,0.125f);
			GL11.glVertex3f(1, -blockrenderwidth/2, -blockrenderwidth/2); // Bottom Right Of The Quad (Bottom)

			GL11.glTexCoord2f(0.125f,0);
			GL11.glVertex3f(1, blockrenderwidth/2, blockrenderwidth/2); // Top Right Of The Quad (Front)
			GL11.glTexCoord2f(0,0);
			GL11.glVertex3f(-1, blockrenderwidth/2, blockrenderwidth/2); // Top Left Of The Quad (Front)
			GL11.glTexCoord2f(0,1);
			GL11.glVertex3f(-1, -blockrenderwidth/2, blockrenderwidth/2); // Bottom Left Of The Quad (Front)
			GL11.glTexCoord2f(0.125f,1);
			GL11.glVertex3f(1, -blockrenderwidth/2, blockrenderwidth/2); // Bottom Right Of The Quad (Front)

			GL11.glTexCoord2f(0.125f,0);
			GL11.glVertex3f(-1, blockrenderwidth/2, -blockrenderwidth/2); // Top Left Of The Quad (Back)
			GL11.glTexCoord2f(0,0);
			GL11.glVertex3f(1, blockrenderwidth/2, -blockrenderwidth/2); // Top Right Of The Quad (Back)
			GL11.glTexCoord2f(0,1);
			GL11.glVertex3f(1, -blockrenderwidth/2, -blockrenderwidth/2); // Bottom Right Of The Quad (Back)
			GL11.glTexCoord2f(0.125f,1);
			GL11.glVertex3f(-1, -blockrenderwidth/2, -blockrenderwidth/2); // Bottom Left Of The Quad (Back)
			
			GL11.glTexCoord2f(1,0);
			GL11.glVertex3f(-1, blockrenderwidth/2, blockrenderwidth/2); // Top Right Of The Quad (Left)
			GL11.glTexCoord2f(0,0);
			GL11.glVertex3f(-1, blockrenderwidth/2, -blockrenderwidth/2); // Top Left Of The Quad (Left)
			GL11.glTexCoord2f(0,1);
			GL11.glVertex3f(-1, -blockrenderwidth/2, -blockrenderwidth/2); // Bottom Left Of The Quad (Left)
			GL11.glTexCoord2f(1,1);
			GL11.glVertex3f(-1, -blockrenderwidth/2, blockrenderwidth/2); // Bottom Right Of The Quad (Left)			

			GL11.glTexCoord2f(0,0);
			GL11.glVertex3f(1, blockrenderwidth/2, -blockrenderwidth/2); // Top Right Of The Quad (Right)
			GL11.glTexCoord2f(1,0);
			GL11.glVertex3f(1, blockrenderwidth/2, blockrenderwidth/2); // Top Left Of The Quad (Right)
			GL11.glTexCoord2f(1,1);
			GL11.glVertex3f(1, -blockrenderwidth/2, blockrenderwidth/2); // Bottom Left Of The Quad (Right)
			GL11.glTexCoord2f(0,1);
			GL11.glVertex3f(1, -blockrenderwidth/2, -blockrenderwidth/2); // Bottom Right Of The Quad (Right)
			
			GL11.glEnd(); // Done Drawing The Quad
			GL11.glEndList();			
			compiled = true;
		}

		wr.loadtextureforblockside(0, bid, false); //We only have one side. Tell world renderer to load our texture.
		
		float rot = 0;
		
		if((meta&0x01)==0x00){
			if((meta & BlockRotation.Y_MASK) == 0){			
				rot = 90f;
			}else if((meta & BlockRotation.Y_MASK) == BlockRotation.Y_ROT_90){
				rot = 180f;
			}else if((meta & BlockRotation.Y_MASK) == BlockRotation.Y_ROT_180){
				rot = 270f;
			}else{							
				rot = 0;
			}
		}else{
			if((meta & BlockRotation.Y_MASK) == 0){			
				rot = 180f;
			}else if((meta & BlockRotation.Y_MASK) == BlockRotation.Y_ROT_90){
				rot = 270f;
			}else if((meta & BlockRotation.Y_MASK) == BlockRotation.Y_ROT_180){
				rot = 0f;
			}else{							
				rot = 90f;
			}
		}
		
		//We are NOT solid
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		if(rot != 0)GL11.glRotatef(rot, 0, 1, 0);
		if((meta&0x01)==0x01)GL11.glTranslatef(6.93f,  0,  0);
		GL11.glCallList(myrenderid); //draw
		if((meta&0x01)==0x01)GL11.glTranslatef(-6.93f,  0,  0);
		if(rot != 0)GL11.glRotatef(-rot, 0, 1, 0);
		
		GL11.glDisable(GL11.GL_BLEND);

	}

}
