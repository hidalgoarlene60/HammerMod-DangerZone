package dangerzone;
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

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;


public class VBOBuffer {
	
	public volatile ByteBuffer vbodata = null;
	public volatile int vcount = 0;
	public volatile int drawvcount = 0;
	public volatile int vmax = 0;
	public volatile int drawvmax = 0;
	public volatile boolean isfar = false;
	private int stride = 0;
	private int textureoff = 0;
	private int coloroff = 0;
	private volatile int glvboID = 0;
	public int textureindex = 0;
	public volatile long VBOid = 0;
	public boolean isTranslucent = false;
	public int xoff;
	public int zoff;
	public long lastusedframe;
	public volatile int vbostate = 0; //0 = sizing, 1 = draw, 2 = newdata, 3 = freed

	
	
	public VBOBuffer(){		

		stride = 3*4; //xyz coord size (floats!)
		textureoff = stride;
		stride += 2*4; //texture coord size
		coloroff = stride;
		stride += 3; //rgb (brightness bytes!)	
		
		vmax = 0;
		drawvmax = 0;
		vbostate = 0;
		glvboID = 0;
		vbodata = null;
		isTranslucent = false; //Translucent VBOs get drawn AFTER ones with only solids
		xoff = zoff = 0;
		lastusedframe = 0;
	}
	
	public void addVertexInfoToVBO(float vx, float vy, float vz,
						float tx, float ty,
						float br, float bg, float bb){
		
		if(vbostate == 0){
			vcount++;
			return;
		}
		if(vbostate != 1){
			return; //do nothing.
		}
		
		//just in case we need to grow even more!
		if(vcount >= vmax){
			//System.out.printf("resize at vcount %d pos %d exp %d\n", vcount, vbodata.position(), stride*vcount);
			vmax += 512; //make it bigger!
			DangerZone.wr.VBOmemorysize += 512*stride; //bytes
			ByteBuffer tmpvbodata = BufferUtils.createByteBuffer(vmax*stride); 
			vbodata.position(0);
			vbodata.limit(vbodata.capacity());
			tmpvbodata.position(0);
			tmpvbodata.limit(tmpvbodata.capacity());
			tmpvbodata.put(vbodata); //memcpy-ish
			vbodata = tmpvbodata; //because java doesn't have a realloc that can usually just extend the current buffer....
			//System.out.printf("vmax extended to %d\n", vmax);
			vbodata.limit(vbodata.capacity()); //set actual size of data
			vbodata.position(vcount*stride); 
		}
		

		//if(vx < -8 || vx > 248)System.out.printf("bad vx = %f\n", vx);
		//if(vy < -8 || vy > 4088)System.out.printf("bad vy = %f\n", vy);
		//if(vz < -8 || vz > 248)System.out.printf("bad vz = %f\n", vz);
		vbodata.putFloat( vx);
		vbodata.putFloat( vy);
		vbodata.putFloat( vz);
		//if(tx < 0 || tx > 1)System.out.printf("bad tx = \n", tx);
		//if(ty < 0 || ty > 1)System.out.printf("bad ty = \n", ty);
		vbodata.putFloat(tx);
		vbodata.putFloat(ty);
		//if(br < 0 || br > 1)System.out.printf("bad br = \n", br);
		//if(bg < 0 || bg > 1)System.out.printf("bad bg = \n", bg);
		//if(bb < 0 || bb > 1)System.out.printf("bad bb = \n", bb);
		vbodata.put((byte) (br*255));
		vbodata.put((byte) (bg*255));
		vbodata.put((byte) (bb*255));
		vcount++;
	}
	
	/*
	 * Draw the specified buffer.
	 * Tells GPU to draw the VBO data we sent previously.
	 */
	public void draw()
	{
		if(vbostate == 0)return;
				
		if(vbostate == 2){		//new data!!!			
			if(vcount == 0){
				drawvcount = 0; //it is empty, for now at least...
				vbostate = 1;
				return;			
			}
			if(glvboID == 0){
				glvboID = GL15.glGenBuffers();
			}
			
			if(drawvmax >= vcount){
				//new data that should fit into the existing buffer
				drawvcount = vcount;
				drawvmax = vmax;
				
				if(isTranslucent){
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);	
				}
				GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);	
				GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
				GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);

			    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, glvboID);
				
				GL11.glVertexPointer(3, GL11.GL_FLOAT, stride, 0);
				GL11.glColorPointer(3, GL11.GL_UNSIGNED_BYTE, stride, coloroff);
				GL11.glTexCoordPointer(2, GL11.GL_FLOAT, stride, textureoff);
				vbodata.position(0);
				vbodata.limit(vbodata.capacity());
				//rewrite over top of existing buffer
				GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, vbodata );
				GL11.glDrawArrays(GL11.GL_QUADS, 0, drawvcount);
				
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); //un-attach it
				
				GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
				GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
				GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);	
				if(isTranslucent)GL11.glDisable(GL11.GL_BLEND);
				vbodata.limit(vbodata.capacity());
				vbostate = 1;
				return;
				
			}else{
				//if(drawvcount != 0 && drawvcount != vcount)System.out.printf("buffer resized from %d to %d\n", drawvcount, vcount);
				drawvcount = vcount;
				drawvmax = vmax;
				if(isTranslucent){
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);	
				}
				
				GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);	
				GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
				GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);

			    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, glvboID);
				
				GL11.glVertexPointer(3, GL11.GL_FLOAT, stride, 0);
				GL11.glColorPointer(3, GL11.GL_UNSIGNED_BYTE, stride, coloroff);
				GL11.glTexCoordPointer(2, GL11.GL_FLOAT, stride, textureoff);
				vbodata.position(0);
				vbodata.limit(vbodata.capacity());
				if(isfar){
					GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vbodata, GL15.GL_STATIC_DRAW);
					//GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vbodata, GL15.GL_STREAM_DRAW);
				}else{
					GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vbodata, GL15.GL_DYNAMIC_DRAW);
				}				
				GL11.glDrawArrays(GL11.GL_QUADS, 0, drawvcount);
				
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); //un-attach it
				
				GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
				GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
				GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);	
				if(isTranslucent)GL11.glDisable(GL11.GL_BLEND);
				vbostate = 1;
				
				return;
	
			}
		}
		
		if(vbostate != 1)return;
		if(drawvcount == 0)return;
		
		if(isTranslucent){
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);	
		}
		
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);	
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);

	    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, glvboID);
		
		GL11.glVertexPointer(3, GL11.GL_FLOAT, stride, 0);
		GL11.glColorPointer(3, GL11.GL_UNSIGNED_BYTE, stride, coloroff);
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, stride, textureoff);
	    
		GL11.glDrawArrays(GL11.GL_QUADS, 0, drawvcount);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); //un-attach it
		
		GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);	
		if(isTranslucent)GL11.glDisable(GL11.GL_BLEND);

	}
	
	public boolean finishDraw(){
		if(vbostate == 0){  //sizing pass, change to draw and do it again!
			//first time through a non-sizing pass. Time to get real!
			//System.out.printf("buffer setup %d\n", vcount);
			
			vmax = vcount/128;
			vmax++;
			vmax *= 128; //just a little extra space...
			vbodata = BufferUtils.createByteBuffer(vmax*stride); 
			vbodata.rewind();
			vbodata.limit(vbodata.capacity());
			vbodata.position(0);
			DangerZone.wr.VBOmemorysize += vmax*stride; //bytes
					
			vcount = 0;
			drawvcount = 0;
			drawvmax = 0;
			vbostate = 1;
			return true;
		}
		
		if(vbostate == 1){
			if(vmax > vcount+128){
				//buffers need to be resized down!
				int downsize = vmax-(vcount+128);
				downsize *= stride;
				//System.out.printf("Downsizing %d bytes\n", downsize);
				DangerZone.wr.VBOmemorysize -= downsize; //bytes
				vmax = vcount+128;				
				ByteBuffer tmpvbodata = BufferUtils.createByteBuffer(vmax*stride); 
				vbodata.position(0);
				vbodata.limit(vcount*stride);
				tmpvbodata.position(0);
				tmpvbodata.limit(tmpvbodata.capacity());
				tmpvbodata.put(vbodata); //memcpy-ish
				vbodata = tmpvbodata;
				vbodata.limit(vbodata.capacity()); 
				vbodata.position(vcount*stride); 
				drawvmax = 0;				
			}
			vbostate = 2;
		}

		return false;
	}
	
	public boolean reset(){
		//this vbo is being re-used.
		if(vbostate != 1){
			return false;
		}		
		vbodata.position(0);          //rewind internal pointer to beginning
		vbodata.limit(vbodata.capacity()); //set size of data to max
		vcount = 0;
		
		return true; 
	}
	
	
	public void free(){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		if(glvboID != 0)GL15.glDeleteBuffers(glvboID);
		glvboID = 0;
		if(vbodata != null)DangerZone.wr.VBOmemorysize -= vbodata.capacity(); //bytes
		drawvcount = 0;
		vbostate = 3; 
	}
	

}
