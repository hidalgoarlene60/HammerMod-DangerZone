package dangerzone.entities;
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

import dangerzone.DangerZone;
import dangerzone.ModelBase;


public class ModelFire extends ModelBase {
	
	private boolean compiled = false;

	private int side1[] = new int[8];
	private int side2[] = new int[8];
	private int side3[] = new int[8];
	private int side4[] = new int[8];
	private int side5[] = new int[8];
	private int side6[] = new int[8];
	
	public ModelFire()
	{
		if(!compiled && DangerZone.wr != null){
			float offinc = 0;
			float colorinc = 0;
			
			for(int i=0;i<8;i++){
				side1[i] = DangerZone.wr.getNextRenderID();
				GL11.glNewList(side1[i], GL11.GL_COMPILE);				
				GL11.glTranslatef(9f, 0, 0);
				GL11.glRotatef(90f, 0, 1f, 0);
				GL11.glColor4f(1f-colorinc, 1f-colorinc, 1f-colorinc, 1f-(colorinc*3));  
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(1,offinc);
				GL11.glVertex3f(9, 9, 0);
				GL11.glTexCoord2f(0,offinc);
				GL11.glVertex3f(-9, 9, 0);
				GL11.glTexCoord2f(0,offinc + 0.125f);
				GL11.glVertex3f(-9, -9, 0); 
				GL11.glTexCoord2f(1,offinc + 0.125f);
				GL11.glVertex3f(9, -9, 0); 
				GL11.glEnd(); // Done Drawing The Quad
				GL11.glRotatef(-90f, 0, 1f, 0);
				GL11.glTranslatef(-9f, 0, 0);
				GL11.glEndList();
				
				offinc += 0.125f;
				colorinc += 0.02f;
			}
			
			offinc = 0;
			colorinc = 0;
			for(int i=0;i<8;i++){
				side2[i] = DangerZone.wr.getNextRenderID();
				GL11.glNewList(side2[i], GL11.GL_COMPILE);				
				GL11.glTranslatef(-9f, 0, 0);
				GL11.glRotatef(90f, 0, 1f, 0);
				GL11.glColor4f(1f-colorinc, 1f-colorinc, 1f-colorinc, 1f-(colorinc*3));  
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(1,offinc);
				GL11.glVertex3f(9, 9, 0);
				GL11.glTexCoord2f(0,offinc);
				GL11.glVertex3f(-9, 9, 0);
				GL11.glTexCoord2f(0,offinc + 0.125f);
				GL11.glVertex3f(-9, -9, 0); 
				GL11.glTexCoord2f(1,offinc + 0.125f);
				GL11.glVertex3f(9, -9, 0); 
				GL11.glEnd(); // Done Drawing The Quad
				GL11.glRotatef(-90f, 0, 1f, 0);
				GL11.glTranslatef(9f, 0, 0);
				GL11.glEndList();
				
				offinc += 0.125f;
				colorinc += 0.02f;
			}
			
			offinc = 0;
			colorinc = 0;
			for(int i=0;i<8;i++){
				side3[i] = DangerZone.wr.getNextRenderID();
				GL11.glNewList(side3[i], GL11.GL_COMPILE);				
				GL11.glTranslatef(0, 0, 9f);		
				GL11.glColor4f(1f-colorinc, 1f-colorinc, 1f-colorinc, 1f-(colorinc*3));  
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(1,offinc);
				GL11.glVertex3f(9, 9, 0);
				GL11.glTexCoord2f(0,offinc);
				GL11.glVertex3f(-9, 9, 0);
				GL11.glTexCoord2f(0,offinc + 0.125f);
				GL11.glVertex3f(-9, -9, 0); 
				GL11.glTexCoord2f(1,offinc + 0.125f);
				GL11.glVertex3f(9, -9, 0); 
				GL11.glEnd(); // Done Drawing The Quad
				GL11.glTranslatef(0, 0, -9f);
				GL11.glEndList();
				
				offinc += 0.125f;
				colorinc += 0.02f;
			}
			
			offinc = 0;
			colorinc = 0;
			for(int i=0;i<8;i++){
				side4[i] = DangerZone.wr.getNextRenderID();
				GL11.glNewList(side4[i], GL11.GL_COMPILE);				
				GL11.glTranslatef(0, 0, -9f);	
				GL11.glColor4f(1f-colorinc, 1f-colorinc, 1f-colorinc, 1f-(colorinc*3));  
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(1,offinc);
				GL11.glVertex3f(9, 9, 0);
				GL11.glTexCoord2f(0,offinc);
				GL11.glVertex3f(-9, 9, 0);
				GL11.glTexCoord2f(0,offinc + 0.125f);
				GL11.glVertex3f(-9, -9, 0); 
				GL11.glTexCoord2f(1,offinc + 0.125f);
				GL11.glVertex3f(9, -9, 0); 
				GL11.glEnd(); // Done Drawing The Quad
				GL11.glTranslatef(0, 0, 9f);
				GL11.glEndList();
				
				offinc += 0.125f;
				colorinc += 0.02f;
			}
			
			offinc = 0;
			colorinc = 0;
			for(int i=0;i<8;i++){
				side5[i] = DangerZone.wr.getNextRenderID();
				GL11.glNewList(side5[i], GL11.GL_COMPILE);					
				GL11.glColor4f(1f-colorinc, 1f-colorinc, 1f-colorinc, 1f-(colorinc*3));  
				GL11.glTranslatef(0, 12f, 0);
				GL11.glScalef(0.5f,  0.5f, 0.5f);
				GL11.glRotatef(45f, 0, 1f, 0);
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(1,offinc);
				GL11.glVertex3f(9, 9, 0);
				GL11.glTexCoord2f(0,offinc);
				GL11.glVertex3f(-9, 9, 0);
				GL11.glTexCoord2f(0,offinc + 0.125f);
				GL11.glVertex3f(-9, -9, 0); 
				GL11.glTexCoord2f(1,offinc + 0.125f);
				GL11.glVertex3f(9, -9, 0); 
				GL11.glEnd(); // Done Drawing The Quad
				GL11.glRotatef(90f, 0, 1f, 0);
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(1,offinc);
				GL11.glVertex3f(9, 9, 0);
				GL11.glTexCoord2f(0,offinc);
				GL11.glVertex3f(-9, 9, 0);
				GL11.glTexCoord2f(0,offinc + 0.125f);
				GL11.glVertex3f(-9, -9, 0); 
				GL11.glTexCoord2f(1,offinc + 0.125f);
				GL11.glVertex3f(9, -9, 0); 
				GL11.glEnd(); // Done Drawing The Quad
				GL11.glRotatef(-135f, 0, 1f, 0);
				GL11.glScalef(2f,  2f, 2f);
				GL11.glTranslatef(0, -12f, 0);
				GL11.glEndList();

				offinc += 0.125f;
				colorinc += 0.02f;
			}
			
			offinc = 0;
			colorinc = 0;
			for(int i=0;i<8;i++){
				side6[i] = DangerZone.wr.getNextRenderID();
				GL11.glNewList(side6[i], GL11.GL_COMPILE);				
				GL11.glTranslatef(0, -9f, 0);	
				GL11.glRotatef(90f, 1f, 0, 0);
				GL11.glColor4f(1f-colorinc, 1f-colorinc, 1f-colorinc, 1f-(colorinc*3));  
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(1,offinc);
				GL11.glVertex3f(9, 9, 0);
				GL11.glTexCoord2f(0,offinc);
				GL11.glVertex3f(-9, 9, 0);
				GL11.glTexCoord2f(0,offinc + 0.125f);
				GL11.glVertex3f(-9, -9, 0); 
				GL11.glTexCoord2f(1,offinc + 0.125f);
				GL11.glVertex3f(9, -9, 0); 
				GL11.glEnd(); // Done Drawing The Quad
				GL11.glRotatef(-90f, 1f, 0, 0);
				GL11.glTranslatef(0, 9f, 0);
				GL11.glEndList();
				
				offinc += 0.125f;
				colorinc += 0.02f;
			}
			
			
			compiled = true;
		}		  
	}
	  
	  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
	  {
		  EntityFire fire = (EntityFire)entity;
		  int sides = fire.getSides();
		  if(sides == 0)return;
		  
		  GL11.glEnable(GL11.GL_BLEND);
		  GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		  GL11.glTranslatef(0, 8f, 0); //we are actually centered UP a little
		  
		  int which = fire.gettextureSeq();
		  if((sides & 0x01) != 0){
			  GL11.glCallList(side1[which]); //draw

		  }
		  if((sides & 0x02) != 0){
			  GL11.glCallList(side2[which]); //draw

		  }
		  if((sides & 0x04) != 0){
			  GL11.glCallList(side3[which]); //draw

		  }
		  if((sides & 0x08) != 0){
			  GL11.glCallList(side4[which]); //draw

		  }
		  
		  //top
		  if((sides & 0x10) != 0){	
			  GL11.glCallList(side5[which]); //draw

		  }
		  
		  if((sides & 0x20) != 0){
			  DangerZone.wr.loadtexture(fire.getTextureBottom()); 
			  GL11.glCallList(side6[which]); //draw

		  }
		  
		  
		  GL11.glDisable(GL11.GL_BLEND);

	  }


	  
	  public void doScale(Entity ent){
		  GL11.glScalef(1.125f, 1.125f, 1.125f); 
	  }

}
