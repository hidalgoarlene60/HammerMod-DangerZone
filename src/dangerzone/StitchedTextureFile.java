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
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.newdawn.slick.opengl.Texture;



public class StitchedTextureFile {
	public int textureID = -1;
	public int nextx = 0;
	public int nexty = 0;
	public Texture blankfile = null;
	public ByteBuffer texturedata = null;
	public int total_size;
	public int total_sizemip1;
	public ByteBuffer texturedatamip1 = null;
	public int total_sizemip2;
	public ByteBuffer texturedatamip2 = null;
	public int total_sizemip3;
	public ByteBuffer texturedatamip3 = null;
	public int total_sizemip4;
	public ByteBuffer texturedatamip4 = null;
	
	
	
	public StitchedTextureFile(){
		total_size = 32*32*16*16*4; //1024 full-sized 16x16 images
		//mipmap level will be 4
		total_sizemip1 = 32*32*8*8*4; //8x8 images
		total_sizemip2 = 32*32*4*4*4; //4x4 images
		total_sizemip3 = 32*32*2*2*4; //2x2 images
		total_sizemip4 = 32*32*1*1*4; //1x1 images
				
		texturedata = BufferUtils.createByteBuffer(total_size);//4 bytes per pixel, by 32*32*1024 = one big image that holds up to 1024 little ones!
		texturedatamip1 = BufferUtils.createByteBuffer(total_sizemip1);
		texturedatamip2 = BufferUtils.createByteBuffer(total_sizemip2);
		texturedatamip3 = BufferUtils.createByteBuffer(total_sizemip3);
		texturedatamip4 = BufferUtils.createByteBuffer(total_sizemip4);

	}
	
	public StitchedTexture doAddTexture(String texturename){		
		StitchedTexture st = doAddTextureToBuffer(texturename);
		if(st != null)doMakeMipMaps();			
		return st;
	}

	
	public StitchedTexture doAddTextureToBuffer(String texturename){
		int i, j, k;
		Texture temp = null;
		if(nexty > 31)return null; // no more room! 32 * 32 images!
		if(texturename == null)return null;
		if(texturename.equals(""))return null;
		//System.out.printf("Stitching %s\n", texturename);
		temp = Utils.initTexture(texturename);
		if(temp == null){
			System.out.printf("Stitching texture not found: %s\n", texturename);
			return null;
		}
		
		//get data from the texture 
		byte b[] = temp.getTextureData();
		if(b.length < 256*3)return null;
		
		//copy little texture into the big one
		texturedata.position(0);
		if(b.length > 256*3){
			//transfer 16 rows and 16 columns of 4-byte=per=pixel data
			k = 0;			
			for(j=0;j<16;j++){
				//transfer a row
				for(i=0;i<16*4;i++){
					texturedata.put((nexty*32768 + nextx*64 + j*2048) + i, b[k]);
					k++;
				}
			}
		}else{
			//transfer 16 rows and 16 cols of 3-byte per pixel data
			k = 0;
			for(j=0;j<16;j++){
				//transfer a row
				for(i=0;i<16;i++){
					texturedata.put((nexty*32768 + nextx*64 + j*2048 + i*4), b[k]);
					k++;
					texturedata.put((nexty*32768 + nextx*64 + j*2048 + i*4) + 1, b[k]);
					k++;
					texturedata.put((nexty*32768 + nextx*64 + j*2048 + i*4) + 2, b[k]);
					k++;
					texturedata.put((nexty*32768 + nextx*64 + j*2048 + i*4) + 3, (byte)255);	//Alpha				
				}
			}
		}
		temp.release(); //bye bye!
		        
		//now calculate and return useful info to caller.
		StitchedTexture st = new StitchedTexture();
		st.yoffsetmin = (float)nexty/(32.0f);
		st.yoffsetmax = (float)((nexty+1f)/32.0f);
		st.xoffsetmin = (float)nextx/(32.0f);
		st.xoffsetmax = (float)((nextx+1f)/32.0f);
		
		nextx++;
		if(nextx >= 32){
			nexty++;
			nextx = 0;
		}
		return st;
	}
	
	public void doMakeMipMaps(){
		int i, j;
		
		//And now, to make some mipmaps...
		//map1 is 256*256
		texturedata.position(0);
		texturedatamip1.position(0);
		for(i=0;i<256;i++){
			for(j=0;j<256;j++){
					texturedatamip1.put(texturedata.get(i*2*2048 + j*8));
					texturedatamip1.put(texturedata.get(i*2*2048 + j*8 + 1));
					texturedatamip1.put(texturedata.get(i*2*2048 + j*8 + 2));
					texturedatamip1.put(texturedata.get(i*2*2048 + j*8 + 3));
			}
		}
		//map 2 is 128*128
		texturedatamip1.position(0);
		texturedatamip2.position(0);
		for(i=0;i<128;i++){
			for(j=0;j<128;j++){
					texturedatamip2.put(texturedatamip1.get(i*2*1024 + j*8));
					texturedatamip2.put(texturedatamip1.get(i*2*1024 + j*8 + 1));
					texturedatamip2.put(texturedatamip1.get(i*2*1024 + j*8 + 2));
					texturedatamip2.put(texturedatamip1.get(i*2*1024 + j*8 + 3));
			}
		}
		//map 3 is 64*64
		texturedatamip2.position(0);
		texturedatamip3.position(0);
		for(i=0;i<64;i++){
			for(j=0;j<64;j++){
					texturedatamip3.put(texturedatamip2.get(i*2*512 + j*8));
					texturedatamip3.put(texturedatamip2.get(i*2*512 + j*8 + 1));
					texturedatamip3.put(texturedatamip2.get(i*2*512 + j*8 + 2));
					texturedatamip3.put(texturedatamip2.get(i*2*512 + j*8 + 3));
			}
		}
		//map 4 is 32*32 - each original image is now 1 pixel
		texturedatamip3.position(0);
		texturedatamip4.position(0);
		for(i=0;i<32;i++){
			for(j=0;j<32;j++){
					texturedatamip4.put(texturedatamip3.get(i*2*256 + j*8));
					texturedatamip4.put(texturedatamip3.get(i*2*256 + j*8 + 1));
					texturedatamip4.put(texturedatamip3.get(i*2*256 + j*8 + 2));
					texturedatamip4.put(texturedatamip3.get(i*2*256 + j*8 + 3));
			}
		}
		
		//reset head and tail pointers
		texturedata.position(0);
		texturedata.limit(total_size);
		texturedatamip1.position(0);
		texturedatamip1.limit(total_sizemip1);
		texturedatamip2.position(0);
		texturedatamip2.limit(total_sizemip2);
		texturedatamip3.position(0);
		texturedatamip3.limit(total_sizemip3);
		texturedatamip4.position(0);
		texturedatamip4.limit(total_sizemip4);
		
		
		if(textureID >= 0){
			//delete the old texture from the graphics card
			IntBuffer texBuf = BufferUtils.createIntBuffer(1); 
			texBuf.put(textureID);
			texBuf.flip(); //rewind and set limit
			GL11.glDeleteTextures(texBuf);
		}
    	
    	//now get a new one and reload the updated version
        textureID = createTextureID(); 
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);       
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_NEAREST); 
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST); 
        
        // load texture to graphics card
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D,  0, 
        		GL11.GL_RGBA, //dest format in card
        		getPow2(512),    //size width
        		getPow2(512),  //size height
        		0, 
        		GL11.GL_RGBA,   //source format
        		GL11.GL_UNSIGNED_BYTE, 
        		texturedata); 
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D,  1, 
                GL11.GL_RGBA, //dest format in card
                getPow2(256),    //size width
                getPow2(256),  //size height
                0, 
                GL11.GL_RGBA,   //source format
                GL11.GL_UNSIGNED_BYTE, 
                texturedatamip1); 
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D,  2, 
                GL11.GL_RGBA, //dest format in card
                getPow2(128),    //size width
                getPow2(128),  //size height
                0, 
                GL11.GL_RGBA,   //source format
                GL11.GL_UNSIGNED_BYTE, 
                texturedatamip2); 
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D,  3, 
                GL11.GL_RGBA, //dest format in card
                getPow2(64),    //size width
                getPow2(64),  //size height
                0, 
                GL11.GL_RGBA,   //source format
                GL11.GL_UNSIGNED_BYTE, 
                texturedatamip3); 
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D,  4, 
                GL11.GL_RGBA, //dest format in card
                getPow2(32),    //size width
                getPow2(32),  //size height
                0, 
                GL11.GL_RGBA,   //source format
                GL11.GL_UNSIGNED_BYTE, 
                texturedatamip4); 
        
        //tell it we only have 4 mipmaps
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, 4);
        
	}
	
	public void bind(){
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	    GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_NEAREST); 
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST); 
	}

	public int createTextureID() { 
		IntBuffer tmp = BufferUtils.createIntBuffer(1);
		GL11.glGenTextures(tmp); 
		return tmp.get(0);
	} 
	
    public static int getPow2(int i) {
        int ret = 2;
        while (ret < i) {
            ret *= 2;
        }
        return ret;
    } 

}
