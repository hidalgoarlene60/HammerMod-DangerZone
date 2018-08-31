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
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;


public class ModelRenderer {
	

    public float textureWidth;
    public float textureHeight;
    public float textureOffsetX;
    public float textureOffsetY;
    
    public float rotationPointX;
    public float rotationPointY;
    public float rotationPointZ;
    
    public float rotateAngleX;
    public float rotateAngleY;
    public float rotateAngleZ;
    
    public float offsetX;
    public float offsetY;
    public float offsetZ;
    
    public float sizeX;
    public float sizeY;
    public float sizeZ;
    
    public boolean mirror = true;    
    public boolean compiled;
    public int displayList;
    
    public List<ModelRenderer> connected_parts = null;
    public boolean connected = false;    
    public float drawoffx, drawoffy, drawoffz;
	public float cosrx, sinrx;
	public float cosry, sinry;	
	public float cosrz, sinrz;
    
    
    public ModelRenderer(int par2, int par3)
    {
        this.textureOffsetX = par2;
        this.textureOffsetY = par3;
    }
    
    //techne compatibiliy
    public ModelRenderer(ModelBase mb, int par2, int par3)
    {
        this.textureOffsetX = par2;
        this.textureOffsetY = par3;
    }

    public void setRotationPoint(float par1, float par2, float par3)
    {
        this.rotationPointX = par1;
        this.rotationPointY = par2;
        this.rotationPointZ = par3;
    }
    
    public void setRotation(float x, float y, float z)
    {
    	this.rotateAngleX = x;
    	this.rotateAngleY = y;
    	this.rotateAngleZ = z;
    }
    
    public void addCube(float par1, float par2, float par3, int par4, int par5, int par6)
    {
        this.offsetX = par1;
        this.offsetY = par2;
        this.offsetZ = par3;
        this.sizeX = par4;
        this.sizeY = par5;
        this.sizeZ = par6;
    }
    
    //techne compatibiliy
    public void addBox(float par1, float par2, float par3, int par4, int par5, int par6)
    {
        this.offsetX = par1;
        this.offsetY = par2;
        this.offsetZ = par3;
        this.sizeX = par4;
        this.sizeY = par5;
        this.sizeZ = par6;
    }
    
    public void setTextureSize(int par1, int par2)
    {
        this.textureWidth = (float)par1;
        this.textureHeight = (float)par2;
    }
    
    // Joins parts such that you only have to manipulate and render the first part.
    // Helps with efficiency of the renderer and less coding.
    // Restrictions: once joined, you cannot change the offset or relative rotation of the connected parts.
    //  Example: A leg with a thigh, a shin, and a foot.
    //	parts are at different angles and offsets and rotation points
    //	shin and foot get connected to thigh
    //	to move the whole leg, just rotate and render the thigh
    //
    public void connectPart(ModelRenderer inpart){
    	if(inpart.connected_parts != null)return;
    	if(inpart.connected)return;
    	if(connected)return;
    	
    	if(connected_parts == null){
    		connected_parts = new ArrayList<ModelRenderer>();
    	}
    	connected_parts.add(inpart);
    	inpart.connected = true;
    }
    
    public void render(float deathfactorscale)
    {
    	if(connected)return; //ignore parts which have been connected to others!
    	
    	if (!this.compiled)
    	{
    		this.compileDisplayList();
    	}

    	float my = -(this.rotationPointY*deathfactorscale-24);
    	if(deathfactorscale >= 1.1f && my < 0)my = -my; //So small things don't just explode into the ground on dying!
    	GL11.glTranslatef(this.rotationPointX*deathfactorscale, my, -this.rotationPointZ*deathfactorscale);
    	
       	GL11.glPushMatrix();
    	
       	if (this.rotateAngleZ != 0.0f)
    	{
    		GL11.glRotatef(-this.rotateAngleZ * (180f / (float)Math.PI), 0.0f, 0.0f, 1.0f);
    	}
    	if (this.rotateAngleY != 0.0f)
    	{
    		GL11.glRotatef(-this.rotateAngleY * (180f / (float)Math.PI), 0.0f, 1.0f, 0.0f);
    	}
    	if (this.rotateAngleX != 0.0f)
    	{
    		GL11.glRotatef(this.rotateAngleX * (180f / (float)Math.PI), 1.0f, 0.0f, 0.0f);
    	}
    	
       	my = -this.offsetY*deathfactorscale;
    	if(deathfactorscale > 1.25f && my < 0)my = -my;
    	GL11.glTranslatef(this.offsetX*deathfactorscale, my, -this.offsetZ*deathfactorscale);
 
    	GL11.glCallList(this.displayList);

    	GL11.glPopMatrix();
    	
    	my = -(this.rotationPointY*deathfactorscale-24);
    	if(deathfactorscale >= 1.1f && my < 0)my = -my;
    	GL11.glTranslatef(-this.rotationPointX*deathfactorscale, -my, this.rotationPointZ*deathfactorscale);  
    	
    	GL14.glSecondaryColor3f(0.0f, 0.0f, 0.0f);
    }
    
    public void renderRaw(){
    	if(connected)return; //ignore parts which have been connected to others!
    	if (!this.compiled)
    	{
    		this.compileDisplayList();
    	}
    	GL11.glCallList(this.displayList);
    }
    
    public void finishConnect(){
    	compileDisplayList(); //DO THIS NOW BEFORE WE MOVE THINGS IN THE REAL RENDER ROUTINE!!!
    }
    
    private void compileDisplayList()
    {
    	if(compiled)return;
    	if(DangerZone.wr == null)return; //server!
    	
        this.displayList = DangerZone.wr.getNextRenderID();
        GL11.glNewList(this.displayList, GL11.GL_COMPILE);
        
		GL11.glBegin(GL11.GL_QUADS);
		
		compile_part();
		
		if(connected_parts != null){
			for(int i=0;i<connected_parts.size();i++){
				compile_connected_part(connected_parts.get(i));
			}
			connected_parts = null;
		}
		
		GL11.glEnd(); // Done Drawing Quads
				
        GL11.glEndList();
        this.compiled = true;
    }
    
    private void compile_connected_part(ModelRenderer inpart){
    	//
    	//parent part is compiled to be drawn at RELATIVE pos 0,0,0 off 0,0,0 rot 0,0,0.
    	//child parts may have different position and offset and rotation, so adjust for that,
    	//so much fun...
    	//lots of calculations, so good thing this is only done ONCE!!!
    	//
 	
    	// calculate parents 0,0,0 drawpoint from their offsets and rotation
		float pdrawx, pdrawy, pdrawz;
		float newx, newy, newz;	
		cosrx = (float) Math.cos(rotateAngleX);
		sinrx = (float) Math.sin(rotateAngleX);
		cosry = (float) Math.cos(rotateAngleY);
		sinry = (float) Math.sin(rotateAngleY);	
		cosrz = (float) Math.cos(rotateAngleZ);
		sinrz = (float) Math.sin(rotateAngleZ);
		
		pdrawx = offsetX;
		pdrawy = offsetY;
		pdrawz = offsetZ;
		
		// rot z
		newx = pdrawx*cosrz - pdrawy*sinrz;
		newy = pdrawy*cosrz + pdrawx*sinrz;
		pdrawx = newx;
		pdrawy = newy;
		// rot y
		newx = pdrawx*cosry - pdrawz*sinry;
		newz = pdrawz*cosry + pdrawx*sinry;
		pdrawx = newx;
		pdrawz = newz;
		// rot x
		newy = pdrawy*cosrx - pdrawz*sinrx;
		newz = pdrawz*cosrx + pdrawy*sinrx;
		pdrawy = newy;
		pdrawz = newz;
		
		pdrawx += rotationPointX;
		pdrawy += rotationPointY;
		pdrawz += rotationPointZ;
		//System.out.printf("----parent final x,  y, z = %f, %f, %f\n", pdrawx, pdrawy, pdrawz);
		
    	
    	// calculate inpart 0,0,0 drawpoint from my offset and rotation
		float drawx, drawy, drawz;
		cosrx = (float) Math.cos(inpart.rotateAngleX);
		sinrx = (float) Math.sin(inpart.rotateAngleX);
		cosry = (float) Math.cos(inpart.rotateAngleY);
		sinry = (float) Math.sin(inpart.rotateAngleY);	
		cosrz = (float) Math.cos(inpart.rotateAngleZ);
		sinrz = (float) Math.sin(inpart.rotateAngleZ);
		
		//System.out.printf("----inpart rotz = %f, %f\n", cosrz, sinrz);
		
		drawx = inpart.offsetX;
		drawy = inpart.offsetY;
		drawz = inpart.offsetZ;

		// rot z
		newx = drawx*cosrz - drawy*sinrz;
		newy = drawy*cosrz + drawx*sinrz;
		drawx = newx;
		drawy = newy;
		// rot y
		newx = drawx*cosry - drawz*sinry;
		newz = drawz*cosry + drawx*sinry;
		drawx = newx;
		drawz = newz;
		// rot x
		newy = drawy*cosrx - drawz*sinrx;
		newz = drawz*cosrx + drawy*sinrx;
		drawy = newy;
		drawz = newz;
		
		drawx += inpart.rotationPointX;
		drawy += inpart.rotationPointY;
		drawz += inpart.rotationPointZ;

		
		//System.out.printf("    inpart final x,  y, z = %f, %f, %f\n", drawx, drawy, drawz);
		
		//how to get from parent 0,0,0 to our 0,0,0 in real space
    	drawoffx = drawx - pdrawx; //this appears to be backwards
    	drawoffy = pdrawy - drawy;
    	drawoffz = pdrawz - drawz;
    	
    	//System.out.printf("    drawoff x,  y, z = %f, %f, %f\n", drawoffx, drawoffy, drawoffz);
    	
		
		//convert to parent-relative draw offsets...
		cosrx = (float) Math.cos(rotateAngleX);
		sinrx = (float) Math.sin(rotateAngleX);
		cosry = (float) Math.cos(rotateAngleY);
		sinry = (float) Math.sin(rotateAngleY);	
		cosrz = (float) Math.cos(rotateAngleZ);
		sinrz = (float) Math.sin(rotateAngleZ);
		
		// rot z
		newx = drawoffx*cosrz - drawoffy*sinrz;
		newy = drawoffy*cosrz + drawoffx*sinrz;
		drawoffx = newx;
		drawoffy = newy;
		// rot y
		newx = drawoffx*cosry - drawoffz*sinry;
		newz = drawoffz*cosry + drawoffx*sinry;
		drawoffx = newx;
		drawoffz = newz;
		// rot x
		newy = drawoffy*cosrx + drawoffz*sinrx;
		newz = drawoffz*cosrx + drawoffy*sinrx;
		drawoffy = newy;
		drawoffz = newz;
    	
		//System.out.printf("    rotated drawoff x,  y, z = %f, %f, %f\n", drawoffx, drawoffy, drawoffz);		

    	//and now set our relative rotation
		float relrotX, relrotY, relrotZ;
		relrotX = inpart.rotateAngleX - rotateAngleX;
		relrotY = rotateAngleY - inpart.rotateAngleY;
		relrotZ = rotateAngleZ - inpart.rotateAngleZ;
		cosrx = (float) Math.cos(relrotX);
		sinrx = (float) Math.sin(relrotX);
		cosry = (float) Math.cos(relrotY);
		sinry = (float) Math.sin(relrotY);	
		cosrz = (float) Math.cos(relrotZ);
		sinrz = (float) Math.sin(relrotZ);
		
    	// draw at difference in drawpoints and positions using relative rotation		
    	compile_this_thing(inpart);
    	
    }
    	
    private void relative_point(float x, float y, float z){
    	float relx, rely, relz;
    	float newx, newy, newz;
    	
    	relx = x; rely = y; relz = z;
    	
		newx = relx*cosrz - rely*sinrz;
		newy = rely*cosrz + relx*sinrz;
		relx = newx;
		rely = newy;
		// rot y
		newx = relx*cosry - relz*sinry;
		newz = relz*cosry + relx*sinry;
		relx = newx;
		relz = newz;
		// rot x
		newy = rely*cosrx - relz*sinrx;
		newz = relz*cosrx + rely*sinrx;
		rely = newy;
		relz = newz;
    	
    	GL11.glVertex3f(relx+drawoffx, rely+drawoffy, relz+drawoffz);
    }
    
    private void compile_this_thing(ModelRenderer inpart){
    	if(mirror){ //normal (flag is backwards)
    		if(inpart.sizeX != 0 && inpart.sizeZ != 0){
    			GL14.glSecondaryColor3f(0f, 0f, 0f);
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX)/textureWidth,(inpart.textureOffsetY)/textureHeight);
    			relative_point(inpart.sizeX, 0, -inpart.sizeZ); // Top Right Of The Quad (Top)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ)/textureWidth,(inpart.textureOffsetY)/textureHeight);
    			relative_point(0, 0, -inpart.sizeZ); // Top Left Of The Quad (Top)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ)/textureHeight);
    			relative_point(0, 0, 0); // Bottom Left Of The Quad (Top)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ)/textureHeight);
    			relative_point(inpart.sizeX, 0, 0); // Bottom Right Of The Quad (Top)

    			GL14.glSecondaryColor3f(0f, 0f, 0f);
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX+inpart.sizeX)/textureWidth,(inpart.textureOffsetY)/textureHeight);
    			relative_point(inpart.sizeX, -inpart.sizeY,0); // Top Right Of The Quad (Bottom)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX)/textureWidth,(inpart.textureOffsetY)/textureHeight);
    			relative_point(0, -inpart.sizeY, 0); // Top Left Of The Quad (Bottom)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ)/textureHeight);
    			relative_point(0, -inpart.sizeY, -inpart.sizeZ); // Bottom Left Of The Quad (Bottom)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX+inpart.sizeX)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ)/textureHeight);
    			relative_point(inpart.sizeX, -inpart.sizeY, -inpart.sizeZ); // Bottom Right Of The Quad (Bottom)
    		}

    		if(inpart.sizeX != 0 && inpart.sizeY != 0){
    			GL14.glSecondaryColor3f(0.02f, 0.02f, 0.02f);
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ)/textureHeight);
    			relative_point(inpart.sizeX,0 , 0); // Top Right Of The Quad (Front)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ)/textureHeight);
    			relative_point(0,0 , 0); // Top Left Of The Quad (Front)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ+inpart.sizeY)/textureHeight);
    			relative_point(0, -inpart.sizeY, 0); // Bottom Left Of The Quad (Front)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ+inpart.sizeY)/textureHeight);
    			relative_point(inpart.sizeX, -inpart.sizeY, 0); // Bottom Right Of The Quad (Front)

    			GL14.glSecondaryColor3f(0.02f, 0.02f, 0.02f);
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX+inpart.sizeZ+inpart.sizeX)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ)/textureHeight);				
    			relative_point(0, 0, -inpart.sizeZ); // Top Left Of The Quad (Back)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeZ+inpart.sizeX)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ)/textureHeight);				
    			relative_point(inpart.sizeX, 0, -inpart.sizeZ); // Top Right Of The Quad (Back)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeZ+inpart.sizeX)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ+inpart.sizeY)/textureHeight);			
    			relative_point(inpart.sizeX, -inpart.sizeY, -inpart.sizeZ); // Bottom Right Of The Quad (Back)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX+inpart.sizeZ+inpart.sizeX)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ+inpart.sizeY)/textureHeight);				
    			relative_point(0, -inpart.sizeY, -inpart.sizeZ); // Bottom Left Of The Quad (Back)
    		}

    		if(inpart.sizeZ != 0 && inpart.sizeY != 0){
    			GL14.glSecondaryColor3f(0.04f, 0.04f, 0.04f);
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ)/textureHeight);
    			relative_point(0, 0, 0); // Top Right Of The Quad (Left)
    			GL11.glTexCoord2f((inpart.textureOffsetX)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ)/textureHeight);
    			relative_point(0, 0, -inpart.sizeZ); // Top Left Of The Quad (Left)
    			GL11.glTexCoord2f((inpart.textureOffsetX)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ+inpart.sizeY)/textureHeight);
    			relative_point(0, -inpart.sizeY, -inpart.sizeZ); // Bottom Left Of The Quad (Left)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ+inpart.sizeY)/textureHeight);
    			relative_point(0, -inpart.sizeY, 0); // Bottom Right Of The Quad (Left)

    			GL14.glSecondaryColor3f(0.04f, 0.04f, 0.04f);
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX+inpart.sizeZ)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ)/textureHeight);
    			relative_point(inpart.sizeX, 0, -inpart.sizeZ); // Top Right Of The Quad (Right)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ)/textureHeight);
    			relative_point(inpart.sizeX, 0, 0); // Top Left Of The Quad (Right)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ+inpart.sizeY)/textureHeight);
    			relative_point(inpart.sizeX, -inpart.sizeY, 0); // Bottom Left Of The Quad (Right)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX+inpart.sizeZ)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ+inpart.sizeY)/textureHeight);
    			relative_point(inpart.sizeX, -inpart.sizeY, -inpart.sizeZ); // Bottom Right Of The Quad (Right)
    		}

    	}else{

    		if(inpart.sizeX != 0 && inpart.sizeZ != 0){
    			GL14.glSecondaryColor3f(0f, 0f, 0f);
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ)/textureWidth,(inpart.textureOffsetY)/textureHeight);
    			relative_point(inpart.sizeX, 0, -inpart.sizeZ); // Top Right Of The Quad (Top)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX)/textureWidth,(inpart.textureOffsetY)/textureHeight);
    			relative_point(0, 0, -inpart.sizeZ); // Top Left Of The Quad (Top)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ)/textureHeight);
    			relative_point(0, 0, 0); // Bottom Left Of The Quad (Top)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ)/textureHeight);
    			relative_point(inpart.sizeX, 0, 0); // Bottom Right Of The Quad (Top)

    			GL14.glSecondaryColor3f(0f, 0f, 0f);
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX)/textureWidth,(inpart.textureOffsetY)/textureHeight);
    			relative_point(inpart.sizeX, -inpart.sizeY,0); // Top Right Of The Quad (Bottom)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX+inpart.sizeX)/textureWidth,(inpart.textureOffsetY)/textureHeight);
    			relative_point(0, -inpart.sizeY, 0); // Top Left Of The Quad (Bottom)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX+inpart.sizeX)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ)/textureHeight);
    			relative_point(0, -inpart.sizeY, -inpart.sizeZ); // Bottom Left Of The Quad (Bottom)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ)/textureHeight);
    			relative_point(inpart.sizeX, -inpart.sizeY, -inpart.sizeZ); // Bottom Right Of The Quad (Bottom)
    		}

    		if(inpart.sizeX != 0 && inpart.sizeY != 0){
    			GL14.glSecondaryColor3f(0.02f, 0.02f, 0.02f);
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ)/textureHeight);
    			relative_point(inpart.sizeX,0 , 0); // Top Right Of The Quad (Front)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ)/textureHeight);
    			relative_point(0,0 , 0); // Top Left Of The Quad (Front)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ+inpart.sizeY)/textureHeight);
    			relative_point(0, -inpart.sizeY, 0); // Bottom Left Of The Quad (Front)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ+inpart.sizeY)/textureHeight);
    			relative_point(inpart.sizeX, -inpart.sizeY, 0); // Bottom Right Of The Quad (Front)

    			GL14.glSecondaryColor3f(0.02f, 0.02f, 0.02f);
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeZ+inpart.sizeX)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ)/textureHeight);				
    			relative_point(0, 0, -inpart.sizeZ); // Top Left Of The Quad (Back)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX+inpart.sizeZ+inpart.sizeX)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ)/textureHeight);				
    			relative_point(inpart.sizeX, 0, -inpart.sizeZ); // Top Right Of The Quad (Back)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX+inpart.sizeZ+inpart.sizeX)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ+inpart.sizeY)/textureHeight);			
    			relative_point(inpart.sizeX, -inpart.sizeY, -inpart.sizeZ); // Bottom Right Of The Quad (Back)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeZ+inpart.sizeX)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ+inpart.sizeY)/textureHeight);				
    			relative_point(0, -inpart.sizeY, -inpart.sizeZ); // Bottom Left Of The Quad (Back)
    		}

    		if(inpart.sizeZ != 0 && inpart.sizeY != 0){
    			GL14.glSecondaryColor3f(0.04f, 0.04f, 0.04f);
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ)/textureHeight);
    			relative_point(0, 0, 0); // Top Right Of The Quad (Left)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX+inpart.sizeZ)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ)/textureHeight);
    			relative_point(0, 0, -inpart.sizeZ); // Top Left Of The Quad (Left)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX+inpart.sizeZ)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ+inpart.sizeY)/textureHeight);
    			relative_point(0, -inpart.sizeY, -inpart.sizeZ); // Bottom Left Of The Quad (Left)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ+inpart.sizeX)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ+inpart.sizeY)/textureHeight);
    			relative_point(0, -inpart.sizeY, 0); // Bottom Right Of The Quad (Left)

    			GL14.glSecondaryColor3f(0.04f, 0.04f, 0.04f);
    			GL11.glTexCoord2f((inpart.textureOffsetX)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ)/textureHeight);
    			relative_point(inpart.sizeX, 0, -inpart.sizeZ); // Top Right Of The Quad (Right)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ)/textureHeight);
    			relative_point(inpart.sizeX, 0, 0); // Top Left Of The Quad (Right)
    			GL11.glTexCoord2f((inpart.textureOffsetX+inpart.sizeZ)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ+inpart.sizeY)/textureHeight);
    			relative_point(inpart.sizeX, -inpart.sizeY, 0); // Bottom Left Of The Quad (Right)
    			GL11.glTexCoord2f((inpart.textureOffsetX)/textureWidth,(inpart.textureOffsetY+inpart.sizeZ+inpart.sizeY)/textureHeight);
    			relative_point(inpart.sizeX, -inpart.sizeY, -inpart.sizeZ); // Bottom Right Of The Quad (Right)
    		}

    	}

    }
    
    private void compile_part(){

    	if(mirror){ //normal (flag is backwards)
    		if(sizeX != 0 && sizeZ != 0){
    			GL14.glSecondaryColor3f(0f, 0f, 0f);
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX)/textureWidth,(textureOffsetY)/textureHeight);
    			GL11.glVertex3f(sizeX, 0, -sizeZ); // Top Right Of The Quad (Top)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ)/textureWidth,(textureOffsetY)/textureHeight);
    			GL11.glVertex3f(0, 0, -sizeZ); // Top Left Of The Quad (Top)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ)/textureWidth,(textureOffsetY+sizeZ)/textureHeight);
    			GL11.glVertex3f(0, 0, 0); // Bottom Left Of The Quad (Top)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX)/textureWidth,(textureOffsetY+sizeZ)/textureHeight);
    			GL11.glVertex3f(sizeX, 0, 0); // Bottom Right Of The Quad (Top)

    			GL14.glSecondaryColor3f(0f, 0f, 0f);
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX+sizeX)/textureWidth,(textureOffsetY)/textureHeight);
    			GL11.glVertex3f(sizeX, -sizeY,0); // Top Right Of The Quad (Bottom)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX)/textureWidth,(textureOffsetY)/textureHeight);
    			GL11.glVertex3f(0, -sizeY, 0); // Top Left Of The Quad (Bottom)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX)/textureWidth,(textureOffsetY+sizeZ)/textureHeight);
    			GL11.glVertex3f(0, -sizeY, -sizeZ); // Bottom Left Of The Quad (Bottom)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX+sizeX)/textureWidth,(textureOffsetY+sizeZ)/textureHeight);
    			GL11.glVertex3f(sizeX, -sizeY, -sizeZ); // Bottom Right Of The Quad (Bottom)
    		}

    		if(sizeX != 0 && sizeY != 0){
    			GL14.glSecondaryColor3f(0.02f, 0.02f, 0.02f);
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX)/textureWidth,(textureOffsetY+sizeZ)/textureHeight);
    			GL11.glVertex3f(sizeX,0 , 0); // Top Right Of The Quad (Front)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ)/textureWidth,(textureOffsetY+sizeZ)/textureHeight);
    			GL11.glVertex3f(0,0 , 0); // Top Left Of The Quad (Front)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ)/textureWidth,(textureOffsetY+sizeZ+sizeY)/textureHeight);
    			GL11.glVertex3f(0, -sizeY, 0); // Bottom Left Of The Quad (Front)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX)/textureWidth,(textureOffsetY+sizeZ+sizeY)/textureHeight);
    			GL11.glVertex3f(sizeX, -sizeY, 0); // Bottom Right Of The Quad (Front)

    			GL14.glSecondaryColor3f(0.02f, 0.02f, 0.02f);
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX+sizeZ+sizeX)/textureWidth,(textureOffsetY+sizeZ)/textureHeight);				
    			GL11.glVertex3f(0, 0, -sizeZ); // Top Left Of The Quad (Back)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeZ+sizeX)/textureWidth,(textureOffsetY+sizeZ)/textureHeight);				
    			GL11.glVertex3f(sizeX, 0, -sizeZ); // Top Right Of The Quad (Back)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeZ+sizeX)/textureWidth,(textureOffsetY+sizeZ+sizeY)/textureHeight);			
    			GL11.glVertex3f(sizeX, -sizeY, -sizeZ); // Bottom Right Of The Quad (Back)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX+sizeZ+sizeX)/textureWidth,(textureOffsetY+sizeZ+sizeY)/textureHeight);				
    			GL11.glVertex3f(0, -sizeY, -sizeZ); // Bottom Left Of The Quad (Back)
    		}

    		if(sizeZ != 0 && sizeY != 0){
    			GL14.glSecondaryColor3f(0.04f, 0.04f, 0.04f);
    			GL11.glTexCoord2f((textureOffsetX+sizeZ)/textureWidth,(textureOffsetY+sizeZ)/textureHeight);
    			GL11.glVertex3f(0, 0, 0); // Top Right Of The Quad (Left)
    			GL11.glTexCoord2f((textureOffsetX)/textureWidth,(textureOffsetY+sizeZ)/textureHeight);
    			GL11.glVertex3f(0, 0, -sizeZ); // Top Left Of The Quad (Left)
    			GL11.glTexCoord2f((textureOffsetX)/textureWidth,(textureOffsetY+sizeZ+sizeY)/textureHeight);
    			GL11.glVertex3f(0, -sizeY, -sizeZ); // Bottom Left Of The Quad (Left)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ)/textureWidth,(textureOffsetY+sizeZ+sizeY)/textureHeight);
    			GL11.glVertex3f(0, -sizeY, 0); // Bottom Right Of The Quad (Left)

    			GL14.glSecondaryColor3f(0.04f, 0.04f, 0.04f);
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX+sizeZ)/textureWidth,(textureOffsetY+sizeZ)/textureHeight);
    			GL11.glVertex3f(sizeX, 0, -sizeZ); // Top Right Of The Quad (Right)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX)/textureWidth,(textureOffsetY+sizeZ)/textureHeight);
    			GL11.glVertex3f(sizeX, 0, 0); // Top Left Of The Quad (Right)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX)/textureWidth,(textureOffsetY+sizeZ+sizeY)/textureHeight);
    			GL11.glVertex3f(sizeX, -sizeY, 0); // Bottom Left Of The Quad (Right)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX+sizeZ)/textureWidth,(textureOffsetY+sizeZ+sizeY)/textureHeight);
    			GL11.glVertex3f(sizeX, -sizeY, -sizeZ); // Bottom Right Of The Quad (Right)
    		}

    	}else{

    		if(sizeX != 0 && sizeZ != 0){
    			GL14.glSecondaryColor3f(0f, 0f, 0f);
    			GL11.glTexCoord2f((textureOffsetX+sizeZ)/textureWidth,(textureOffsetY)/textureHeight);
    			GL11.glVertex3f(sizeX, 0, -sizeZ); // Top Right Of The Quad (Top)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX)/textureWidth,(textureOffsetY)/textureHeight);
    			GL11.glVertex3f(0, 0, -sizeZ); // Top Left Of The Quad (Top)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX)/textureWidth,(textureOffsetY+sizeZ)/textureHeight);
    			GL11.glVertex3f(0, 0, 0); // Bottom Left Of The Quad (Top)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ)/textureWidth,(textureOffsetY+sizeZ)/textureHeight);
    			GL11.glVertex3f(sizeX, 0, 0); // Bottom Right Of The Quad (Top)

    			GL14.glSecondaryColor3f(0f, 0f, 0f);
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX)/textureWidth,(textureOffsetY)/textureHeight);
    			GL11.glVertex3f(sizeX, -sizeY,0); // Top Right Of The Quad (Bottom)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX+sizeX)/textureWidth,(textureOffsetY)/textureHeight);
    			GL11.glVertex3f(0, -sizeY, 0); // Top Left Of The Quad (Bottom)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX+sizeX)/textureWidth,(textureOffsetY+sizeZ)/textureHeight);
    			GL11.glVertex3f(0, -sizeY, -sizeZ); // Bottom Left Of The Quad (Bottom)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX)/textureWidth,(textureOffsetY+sizeZ)/textureHeight);
    			GL11.glVertex3f(sizeX, -sizeY, -sizeZ); // Bottom Right Of The Quad (Bottom)
    		}

    		if(sizeX != 0 && sizeY != 0){
    			GL14.glSecondaryColor3f(0.02f, 0.02f, 0.02f);
    			GL11.glTexCoord2f((textureOffsetX+sizeZ)/textureWidth,(textureOffsetY+sizeZ)/textureHeight);
    			GL11.glVertex3f(sizeX,0 , 0); // Top Right Of The Quad (Front)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX)/textureWidth,(textureOffsetY+sizeZ)/textureHeight);
    			GL11.glVertex3f(0,0 , 0); // Top Left Of The Quad (Front)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX)/textureWidth,(textureOffsetY+sizeZ+sizeY)/textureHeight);
    			GL11.glVertex3f(0, -sizeY, 0); // Bottom Left Of The Quad (Front)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ)/textureWidth,(textureOffsetY+sizeZ+sizeY)/textureHeight);
    			GL11.glVertex3f(sizeX, -sizeY, 0); // Bottom Right Of The Quad (Front)

    			GL14.glSecondaryColor3f(0.02f, 0.02f, 0.02f);
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeZ+sizeX)/textureWidth,(textureOffsetY+sizeZ)/textureHeight);				
    			GL11.glVertex3f(0, 0, -sizeZ); // Top Left Of The Quad (Back)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX+sizeZ+sizeX)/textureWidth,(textureOffsetY+sizeZ)/textureHeight);				
    			GL11.glVertex3f(sizeX, 0, -sizeZ); // Top Right Of The Quad (Back)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX+sizeZ+sizeX)/textureWidth,(textureOffsetY+sizeZ+sizeY)/textureHeight);			
    			GL11.glVertex3f(sizeX, -sizeY, -sizeZ); // Bottom Right Of The Quad (Back)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeZ+sizeX)/textureWidth,(textureOffsetY+sizeZ+sizeY)/textureHeight);				
    			GL11.glVertex3f(0, -sizeY, -sizeZ); // Bottom Left Of The Quad (Back)
    		}

    		if(sizeZ != 0 && sizeY != 0){
    			GL14.glSecondaryColor3f(0.04f, 0.04f, 0.04f);
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX)/textureWidth,(textureOffsetY+sizeZ)/textureHeight);
    			GL11.glVertex3f(0, 0, 0); // Top Right Of The Quad (Left)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX+sizeZ)/textureWidth,(textureOffsetY+sizeZ)/textureHeight);
    			GL11.glVertex3f(0, 0, -sizeZ); // Top Left Of The Quad (Left)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX+sizeZ)/textureWidth,(textureOffsetY+sizeZ+sizeY)/textureHeight);
    			GL11.glVertex3f(0, -sizeY, -sizeZ); // Bottom Left Of The Quad (Left)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ+sizeX)/textureWidth,(textureOffsetY+sizeZ+sizeY)/textureHeight);
    			GL11.glVertex3f(0, -sizeY, 0); // Bottom Right Of The Quad (Left)

    			GL14.glSecondaryColor3f(0.04f, 0.04f, 0.04f);
    			GL11.glTexCoord2f((textureOffsetX)/textureWidth,(textureOffsetY+sizeZ)/textureHeight);
    			GL11.glVertex3f(sizeX, 0, -sizeZ); // Top Right Of The Quad (Right)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ)/textureWidth,(textureOffsetY+sizeZ)/textureHeight);
    			GL11.glVertex3f(sizeX, 0, 0); // Top Left Of The Quad (Right)
    			GL11.glTexCoord2f((textureOffsetX+sizeZ)/textureWidth,(textureOffsetY+sizeZ+sizeY)/textureHeight);
    			GL11.glVertex3f(sizeX, -sizeY, 0); // Bottom Left Of The Quad (Right)
    			GL11.glTexCoord2f((textureOffsetX)/textureWidth,(textureOffsetY+sizeZ+sizeY)/textureHeight);
    			GL11.glVertex3f(sizeX, -sizeY, -sizeZ); // Bottom Right Of The Quad (Right)
    		}

    	}
    }
    
    //
    //The below are NOT used for entities... only special BLOCKS!
    //
    // 
    // This one only does hardwired rotation around Y on center of block, and only does %90 properly.
    // Probably not what you are looking for.
    //
    //Graphics interface designers created VBOs because Display Lists were too easy and lots of people could create cool things.
    //Couldn't have that, so they made it harder...
    //Add in texture mapping from Techne, and voila...
    //
    public void renderToVBO(VBOBuffer v, StitchedTexture st, float red, float green, float blue, int xo, int yo, int zo, int roty)
    {
    	float xs = rotationPointX + offsetX;
    	float xe = xs + sizeX;   	
    	float ye = 16 - (rotationPointY + offsetY);
    	float ys = ye - sizeY;   	
    	float zs = rotationPointZ + offsetZ;
    	float ze = zs + sizeZ;   	
    	float mxx, mnx, mxy, mny;
		float cosr = (float) Math.cos(Math.toRadians(roty));
		float sinr = (float) Math.sin(Math.toRadians(roty));   		
    	
	   	ys += yo;
    	ye += yo;
    	
		//draw all 6 quads while properly mapping into techne textures
    	//checks to see if sides should even be drawn...
    	//maybe someone made one side width 0, in which case only ONE quad is drawn
    	
    	//top/bottom
    	if(Math.abs(xs - xe) > 0.01f && Math.abs(zs - ze) > 0.01f){   	//neither?		
    		if(Math.abs(ys - ye) > 0.01f){								//just one?
    			mxx = st.xoffsetmin + ((textureOffsetX+sizeZ+sizeX)/textureWidth)*(st.xoffsetmax - st.xoffsetmin);
    			mnx = st.xoffsetmin + ((textureOffsetX+sizeZ)/textureWidth)*(st.xoffsetmax - st.xoffsetmin);
    			mny = st.yoffsetmin + ((textureOffsetY+sizeZ)/textureHeight)*(st.yoffsetmax - st.yoffsetmin);
    			mxy = st.yoffsetmin + ((textureOffsetY)/textureHeight)*(st.yoffsetmax - st.yoffsetmin);
    			rotateIntoVBO(v, cosr, sinr, xe, ye, ze, mxx, mny, red, green, blue, xo, zo);		
    			rotateIntoVBO(v, cosr, sinr, xs, ye, ze, mnx, mny, red, green, blue, xo, zo);		
    			rotateIntoVBO(v, cosr, sinr, xs, ye, zs, mnx, mxy, red, green, blue, xo, zo);
    			rotateIntoVBO(v, cosr, sinr, xe, ye, zs, mxx, mxy, red, green, blue, xo, zo);
    		}
    		mxx = st.xoffsetmin + ((textureOffsetX+sizeZ+sizeX+sizeX)/textureWidth)*(st.xoffsetmax - st.xoffsetmin);
    		mnx = st.xoffsetmin + ((textureOffsetX+sizeZ+sizeX)/textureWidth)*(st.xoffsetmax - st.xoffsetmin);
    		mny = st.yoffsetmin + ((textureOffsetY+sizeZ)/textureHeight)*(st.yoffsetmax - st.yoffsetmin);
    		mxy = st.yoffsetmin + ((textureOffsetY)/textureHeight)*(st.yoffsetmax - st.yoffsetmin);
    		rotateIntoVBO(v, cosr, sinr, xe, ys, ze, mxx, mny, red, green, blue, xo, zo);	 			
    		rotateIntoVBO(v, cosr, sinr, xs, ys, ze, mnx, mny, red, green, blue, xo, zo);	
    		rotateIntoVBO(v, cosr, sinr, xs, ys, zs, mnx, mxy, red, green, blue, xo, zo);	
    		rotateIntoVBO(v, cosr, sinr, xe, ys, zs, mxx, mxy, red, green, blue, xo, zo);	
    	}
    	
    	
		//front/back
    	if(Math.abs(xs - xe) > 0.01f && Math.abs(ys - ye) > 0.01f){   		
    		if(Math.abs(zs - ze) > 0.01f){
    			mxx = st.xoffsetmin + ((textureOffsetX+sizeZ+sizeX)/textureWidth)*(st.xoffsetmax - st.xoffsetmin);
    			mnx = st.xoffsetmin + ((textureOffsetX+sizeZ)/textureWidth)*(st.xoffsetmax - st.xoffsetmin);
    			mny = st.yoffsetmin + ((textureOffsetY+sizeZ)/textureHeight)*(st.yoffsetmax - st.yoffsetmin);
    			mxy = st.yoffsetmin + ((textureOffsetY+sizeZ+sizeY)/textureHeight)*(st.yoffsetmax - st.yoffsetmin);		
    			rotateIntoVBO(v, cosr, sinr, xe, ye, ze, mxx, mny, red, green, blue, xo, zo);
    			rotateIntoVBO(v, cosr, sinr, xs, ye, ze, mnx, mny, red, green, blue, xo, zo);	
    			rotateIntoVBO(v, cosr, sinr, xs, ys, ze, mnx, mxy, red, green, blue, xo, zo);		
    			rotateIntoVBO(v, cosr, sinr, xe, ys, ze, mxx, mxy, red, green, blue, xo, zo);
    		}		
    		mnx = st.xoffsetmin + ((textureOffsetX+sizeZ+sizeX+sizeZ+sizeX)/textureWidth)*(st.xoffsetmax - st.xoffsetmin);
    		mxx = st.xoffsetmin + ((textureOffsetX+sizeZ+sizeZ+sizeX)/textureWidth)*(st.xoffsetmax - st.xoffsetmin);
    		mny = st.yoffsetmin + ((textureOffsetY+sizeZ)/textureHeight)*(st.yoffsetmax - st.yoffsetmin);
    		mxy = st.yoffsetmin + ((textureOffsetY+sizeZ+sizeY)/textureHeight)*(st.yoffsetmax - st.yoffsetmin);		
    		rotateIntoVBO(v, cosr, sinr, xe, ye, zs, mxx, mny, red, green, blue, xo, zo);
    		rotateIntoVBO(v, cosr, sinr, xs, ye, zs, mnx, mny, red, green, blue, xo, zo);	
    		rotateIntoVBO(v, cosr, sinr, xs, ys, zs, mnx, mxy, red, green, blue, xo, zo);	
    		rotateIntoVBO(v, cosr, sinr, xe, ys, zs, mxx, mxy, red, green, blue, xo, zo);
    	}
    	
    	//left/right
    	if(Math.abs(zs - ze) > 0.01f && Math.abs(ys - ye) > 0.01f){   		
    		if(Math.abs(xs - xe) > 0.01f){
    			mxx = st.xoffsetmin + ((textureOffsetX+sizeZ)/textureWidth)*(st.xoffsetmax - st.xoffsetmin);
    			mnx = st.xoffsetmin + ((textureOffsetX)/textureWidth)*(st.xoffsetmax - st.xoffsetmin);
    			mny = st.yoffsetmin + ((textureOffsetY+sizeZ)/textureHeight)*(st.yoffsetmax - st.yoffsetmin);
    			mxy = st.yoffsetmin + ((textureOffsetY+sizeZ+sizeY)/textureHeight)*(st.yoffsetmax - st.yoffsetmin);		
    			rotateIntoVBO(v, cosr, sinr, xs, ye, ze, mxx, mny, red, green, blue, xo, zo);
    			rotateIntoVBO(v, cosr, sinr, xs, ye, zs, mnx, mny, red, green, blue, xo, zo); 
    			rotateIntoVBO(v, cosr, sinr, xs, ys, zs, mnx, mxy, red, green, blue, xo, zo); 
    			rotateIntoVBO(v, cosr, sinr, xs, ys, ze, mxx, mxy, red, green, blue, xo, zo);
    		}		
    		mnx = st.xoffsetmin + ((textureOffsetX+sizeZ+sizeX+sizeZ)/textureWidth)*(st.xoffsetmax - st.xoffsetmin);
    		mxx = st.xoffsetmin + ((textureOffsetX+sizeZ+sizeX)/textureWidth)*(st.xoffsetmax - st.xoffsetmin);
    		mny = st.yoffsetmin + ((textureOffsetY+sizeZ)/textureHeight)*(st.yoffsetmax - st.yoffsetmin);
    		mxy = st.yoffsetmin + ((textureOffsetY+sizeZ+sizeY)/textureHeight)*(st.yoffsetmax - st.yoffsetmin);		
    		rotateIntoVBO(v, cosr, sinr, xe, ye, ze, mxx, mny, red, green, blue, xo, zo);
    		rotateIntoVBO(v, cosr, sinr, xe, ye, zs, mnx, mny, red, green, blue, xo, zo); 
    		rotateIntoVBO(v, cosr, sinr, xe, ys, zs, mnx, mxy, red, green, blue, xo, zo);
    		rotateIntoVBO(v, cosr, sinr, xe, ys, ze, mxx, mxy, red, green, blue, xo, zo);
    	}
    }
    
    //rotate the point around 0 along with its texture position
    public void rotateIntoVBO(VBOBuffer v, float cosr, float sinr, float x, float y, float z, float txx, float txy, float r, float g, float b, int xo, int zo){
    	v.addVertexInfoToVBO(x*cosr + z*sinr + xo, y, z*cosr + x*sinr + zo, txx, txy, r, g, b);
    }
    
    /*
     * Generic XYZ rotation of cube into VBO.
     * Probably won't rotate the way you want it to. Rotation never works as expected.
     * Set your Techne rotation point to center of cube (0, 16, 0) for least unexpected results!
     *      * Set your Techne rotation point to center of cube (0, 16, 0) for least unexpected results!
     *           * Set your Techne rotation point to center of cube (0, 16, 0) for least unexpected results!
     *                * Set your Techne rotation point to center of cube (0, 16, 0) for least unexpected results!
     *                     * Set your Techne rotation point to center of cube (0, 16, 0) for least unexpected results!
     *                          * Set your Techne rotation point to center of cube (0, 16, 0) for least unexpected results!
     */
    public void renderRotatedToVBO(VBOBuffer v, StitchedTexture st, float red, float green, float blue, int xo, int yo, int zo, int rotx, int roty, int rotz)
    {
    	float xs = offsetX;
    	float xe = xs + sizeX;   	
    	float ye = offsetY;
    	float ys = ye + sizeY;   	
    	float zs = offsetZ;
    	float ze = zs + sizeZ;   	
    	float mxx, mnx, mxy, mny;
		float cosrx = (float) Math.cos(Math.toRadians(rotx)+rotateAngleX);
		float sinrx = (float) Math.sin(Math.toRadians(rotx)+rotateAngleX);
		float cosry = (float) Math.cos(Math.toRadians(roty)+rotateAngleY);
		float sinry = (float) Math.sin(Math.toRadians(roty)+rotateAngleY);	
		float cosrz = (float) Math.cos(Math.toRadians(rotz)+rotateAngleZ);
		float sinrz = (float) Math.sin(Math.toRadians(rotz)+rotateAngleZ);	
    	
		//draw all 6 quads while properly mapping into techne textures
    	//checks to see if sides should even be drawn...
    	//maybe someone made one side width 0, in which case only ONE quad is drawn
    	
    	//top/bottom
    	if(Math.abs(xs - xe) > 0.01f && Math.abs(zs - ze) > 0.01f){   	//neither?		
    		if(Math.abs(ys - ye) > 0.01f){								//just one?
    			mxx = st.xoffsetmin + ((textureOffsetX+sizeZ+sizeX)/textureWidth)*(st.xoffsetmax - st.xoffsetmin);
    			mnx = st.xoffsetmin + ((textureOffsetX+sizeZ)/textureWidth)*(st.xoffsetmax - st.xoffsetmin);
    			mny = st.yoffsetmin + ((textureOffsetY+sizeZ)/textureHeight)*(st.yoffsetmax - st.yoffsetmin);
    			mxy = st.yoffsetmin + ((textureOffsetY)/textureHeight)*(st.yoffsetmax - st.yoffsetmin);
    			rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, xe, ye, ze, mxx, mny, red, green, blue, xo, yo, zo);		
    			rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, xs, ye, ze, mnx, mny, red, green, blue, xo, yo, zo);		
    			rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, xs, ye, zs, mnx, mxy, red, green, blue, xo, yo, zo);	
    			rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, xe, ye, zs, mxx, mxy, red, green, blue, xo, yo, zo);	
    		}
    		mxx = st.xoffsetmin + ((textureOffsetX+sizeZ+sizeX+sizeX)/textureWidth)*(st.xoffsetmax - st.xoffsetmin);
    		mnx = st.xoffsetmin + ((textureOffsetX+sizeZ+sizeX)/textureWidth)*(st.xoffsetmax - st.xoffsetmin);
    		mny = st.yoffsetmin + ((textureOffsetY+sizeZ)/textureHeight)*(st.yoffsetmax - st.yoffsetmin);
    		mxy = st.yoffsetmin + ((textureOffsetY)/textureHeight)*(st.yoffsetmax - st.yoffsetmin);
    		rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, xe, ys, ze, mxx, mny, red, green, blue, xo, yo, zo);		 			
    		rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, xs, ys, ze, mnx, mny, red, green, blue, xo, yo, zo);	
    		rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, xs, ys, zs, mnx, mxy, red, green, blue, xo, yo, zo);		
    		rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, xe, ys, zs, mxx, mxy, red, green, blue, xo, yo, zo);	
    	}
    	
    	
		//front/back
    	if(Math.abs(xs - xe) > 0.01f && Math.abs(ys - ye) > 0.01f){   		
    		if(Math.abs(zs - ze) > 0.01f){
    			mxx = st.xoffsetmin + ((textureOffsetX+sizeZ+sizeX)/textureWidth)*(st.xoffsetmax - st.xoffsetmin);
    			mnx = st.xoffsetmin + ((textureOffsetX+sizeZ)/textureWidth)*(st.xoffsetmax - st.xoffsetmin);
    			mny = st.yoffsetmin + ((textureOffsetY+sizeZ)/textureHeight)*(st.yoffsetmax - st.yoffsetmin);
    			mxy = st.yoffsetmin + ((textureOffsetY+sizeZ+sizeY)/textureHeight)*(st.yoffsetmax - st.yoffsetmin);		
    			rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, xe, ye, ze, mxx, mny, red, green, blue, xo, yo, zo);	
    			rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, xs, ye, ze, mnx, mny, red, green, blue, xo, yo, zo);	
    			rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, xs, ys, ze, mnx, mxy, red, green, blue, xo, yo, zo);		
    			rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, xe, ys, ze, mxx, mxy, red, green, blue, xo, yo, zo);	
    		}		
    		mnx = st.xoffsetmin + ((textureOffsetX+sizeZ+sizeX+sizeZ+sizeX)/textureWidth)*(st.xoffsetmax - st.xoffsetmin);
    		mxx = st.xoffsetmin + ((textureOffsetX+sizeZ+sizeZ+sizeX)/textureWidth)*(st.xoffsetmax - st.xoffsetmin);
    		mny = st.yoffsetmin + ((textureOffsetY+sizeZ)/textureHeight)*(st.yoffsetmax - st.yoffsetmin);
    		mxy = st.yoffsetmin + ((textureOffsetY+sizeZ+sizeY)/textureHeight)*(st.yoffsetmax - st.yoffsetmin);		
    		rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, xe, ye, zs, mxx, mny, red, green, blue, xo, yo, zo);	
    		rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, xs, ye, zs, mnx, mny, red, green, blue, xo, yo, zo);	
    		rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, xs, ys, zs, mnx, mxy, red, green, blue, xo, yo, zo);		
    		rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, xe, ys, zs, mxx, mxy, red, green, blue, xo, yo, zo);	
    	}
    	
    	//left/right
    	if(Math.abs(zs - ze) > 0.01f && Math.abs(ys - ye) > 0.01f){   		
    		if(Math.abs(xs - xe) > 0.01f){
    			mxx = st.xoffsetmin + ((textureOffsetX+sizeZ)/textureWidth)*(st.xoffsetmax - st.xoffsetmin);
    			mnx = st.xoffsetmin + ((textureOffsetX)/textureWidth)*(st.xoffsetmax - st.xoffsetmin);
    			mny = st.yoffsetmin + ((textureOffsetY+sizeZ)/textureHeight)*(st.yoffsetmax - st.yoffsetmin);
    			mxy = st.yoffsetmin + ((textureOffsetY+sizeZ+sizeY)/textureHeight)*(st.yoffsetmax - st.yoffsetmin);		
    			rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, xs, ye, ze, mxx, mny, red, green, blue, xo, yo, zo);	
    			rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, xs, ye, zs, mnx, mny, red, green, blue, xo, yo, zo);	
    			rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, xs, ys, zs, mnx, mxy, red, green, blue, xo, yo, zo);	
    			rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, xs, ys, ze, mxx, mxy, red, green, blue, xo, yo, zo);	
    		}		
    		mnx = st.xoffsetmin + ((textureOffsetX+sizeZ+sizeX+sizeZ)/textureWidth)*(st.xoffsetmax - st.xoffsetmin);
    		mxx = st.xoffsetmin + ((textureOffsetX+sizeZ+sizeX)/textureWidth)*(st.xoffsetmax - st.xoffsetmin);
    		mny = st.yoffsetmin + ((textureOffsetY+sizeZ)/textureHeight)*(st.yoffsetmax - st.yoffsetmin);
    		mxy = st.yoffsetmin + ((textureOffsetY+sizeZ+sizeY)/textureHeight)*(st.yoffsetmax - st.yoffsetmin);		
    		rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, xe, ye, ze, mxx, mny, red, green, blue, xo, yo, zo);	
    		rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, xe, ye, zs, mnx, mny, red, green, blue, xo, yo, zo);	
    		rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, xe, ys, zs, mnx, mxy, red, green, blue, xo, yo, zo);	
    		rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, xe, ys, ze, mxx, mxy, red, green, blue, xo, yo, zo);	
    	}
    }
    
    //rotate the point around its 0 along with its texture position
    //Z first
    private void rotateIntoVBOXYZ(VBOBuffer v, float cosrx, float sinrx, float cosry, float sinry, float cosrz, float sinrz, 
    		float x, float y, float z, float txx, float txy, float r, float g, float b, int xo, int yo, int zo){
    		rotateIntoVBOXY(v, cosrx, sinrx, cosry, sinry, x*cosrz - y*sinrz, y*cosrz + x*sinrz, z, txx, txy, r, g, b, xo, yo, zo);	
    }
    
    //now Y
    private void rotateIntoVBOXY(VBOBuffer v, float cosrx, float sinrx, float cosry, float sinry, 
    		float x, float y, float z, float txx, float txy, float r, float g, float b, int xo, int yo, int zo){
    		rotateIntoVBOX(v, cosrx, sinrx, x*cosry - z*sinry, y, z*cosry + x*sinry, txx, txy, r, g, b, xo, yo, zo);	
    }
    
    //now X
    private void rotateIntoVBOX(VBOBuffer v, float cosrx, float sinrx,
    		float x, float y, float z, float txx, float txy, float r, float g, float b, int xo, int yo, int zo){
    		stuffIntoVBO(v, x, y*cosrx - z*sinrx, z*cosrx + y*sinrx , txx, txy, r, g, b, xo, yo, zo);	
    }
    
    //lastly, offsets...
    private void stuffIntoVBO(VBOBuffer v, float x, float y, float z, float txx, float txy, float r, float g, float b, int xo, int yo, int zo){
    	v.addVertexInfoToVBO(x + rotationPointX + xo , (16 - (y + rotationPointY)) + yo, z + rotationPointZ + zo, txx, txy, r, g, b);
    }

}
