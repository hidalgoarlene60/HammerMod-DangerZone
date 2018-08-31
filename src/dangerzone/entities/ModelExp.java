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


public class ModelExp extends ModelBase {
	
	private boolean compiled = false;
	private int list1, list2, list3, list4;
	private int list5, list6, list7, list8;
	
	public ModelExp()
	{
		if(!compiled && DangerZone.wr != null){
			float offinc = 0;
			list1 = DangerZone.wr.getNextRenderID();
			GL11.glNewList(list1, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(1,offinc);
			GL11.glVertex3f(4, 4, 0);
			GL11.glTexCoord2f(0,offinc);
			GL11.glVertex3f(-4, 4, 0);
			GL11.glTexCoord2f(0,offinc + 0.125f);
			GL11.glVertex3f(-4, -4, 0); 
			GL11.glTexCoord2f(1,offinc + 0.125f);
			GL11.glVertex3f(4, -4, 0); 
			GL11.glEnd(); // Done Drawing The Quad
			GL11.glEndList();
			
			offinc += 0.125f;
			list2 = DangerZone.wr.getNextRenderID();
			GL11.glNewList(list2, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(1,offinc);
			GL11.glVertex3f(4, 4, 0);
			GL11.glTexCoord2f(0,offinc);
			GL11.glVertex3f(-4, 4, 0);
			GL11.glTexCoord2f(0,offinc + 0.125f);
			GL11.glVertex3f(-4, -4, 0); 
			GL11.glTexCoord2f(1,offinc + 0.125f);
			GL11.glVertex3f(4, -4, 0); 
			GL11.glEnd(); // Done Drawing The Quad
			GL11.glEndList();
			
			offinc += 0.125f;
			list3 = DangerZone.wr.getNextRenderID();
			GL11.glNewList(list3, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(1,offinc);
			GL11.glVertex3f(4, 4, 0);
			GL11.glTexCoord2f(0,offinc);
			GL11.glVertex3f(-4, 4, 0);
			GL11.glTexCoord2f(0,offinc + 0.125f);
			GL11.glVertex3f(-4, -4, 0); 
			GL11.glTexCoord2f(1,offinc + 0.125f);
			GL11.glVertex3f(4, -4, 0); 
			GL11.glEnd(); // Done Drawing The Quad
			GL11.glEndList();
			
			offinc += 0.125f;
			list4 = DangerZone.wr.getNextRenderID();
			GL11.glNewList(list4, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(1,offinc);
			GL11.glVertex3f(4, 4, 0);
			GL11.glTexCoord2f(0,offinc);
			GL11.glVertex3f(-4, 4, 0);
			GL11.glTexCoord2f(0,offinc + 0.125f);
			GL11.glVertex3f(-4, -4, 0); 
			GL11.glTexCoord2f(1,offinc + 0.125f);
			GL11.glVertex3f(4, -4, 0); 
			GL11.glEnd(); // Done Drawing The Quad
			GL11.glEndList();
			
			offinc += 0.125f;
			list5 = DangerZone.wr.getNextRenderID();
			GL11.glNewList(list5, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(1,offinc);
			GL11.glVertex3f(4, 4, 0);
			GL11.glTexCoord2f(0,offinc);
			GL11.glVertex3f(-4, 4, 0);
			GL11.glTexCoord2f(0,offinc + 0.125f);
			GL11.glVertex3f(-4, -4, 0); 
			GL11.glTexCoord2f(1,offinc + 0.125f);
			GL11.glVertex3f(4, -4, 0); 
			GL11.glEnd(); // Done Drawing The Quad
			GL11.glEndList();
			
			offinc += 0.125f;
			list6 = DangerZone.wr.getNextRenderID();
			GL11.glNewList(list6, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(1,offinc);
			GL11.glVertex3f(4, 4, 0);
			GL11.glTexCoord2f(0,offinc);
			GL11.glVertex3f(-4, 4, 0);
			GL11.glTexCoord2f(0,offinc + 0.125f);
			GL11.glVertex3f(-4, -4, 0); 
			GL11.glTexCoord2f(1,offinc + 0.125f);
			GL11.glVertex3f(4, -4, 0); 
			GL11.glEnd(); // Done Drawing The Quad
			GL11.glEndList();
			
			offinc += 0.125f;
			list7 = DangerZone.wr.getNextRenderID();
			GL11.glNewList(list7, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(1,offinc);
			GL11.glVertex3f(4, 4, 0);
			GL11.glTexCoord2f(0,offinc);
			GL11.glVertex3f(-4, 4, 0);
			GL11.glTexCoord2f(0,offinc + 0.125f);
			GL11.glVertex3f(-4, -4, 0); 
			GL11.glTexCoord2f(1,offinc + 0.125f);
			GL11.glVertex3f(4, -4, 0); 
			GL11.glEnd(); // Done Drawing The Quad
			GL11.glEndList();
			
			offinc += 0.125f;
			list8 = DangerZone.wr.getNextRenderID();
			GL11.glNewList(list8, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(1,offinc);
			GL11.glVertex3f(4, 4, 0);
			GL11.glTexCoord2f(0,offinc);
			GL11.glVertex3f(-4, 4, 0);
			GL11.glTexCoord2f(0,offinc + 0.125f);
			GL11.glVertex3f(-4, -4, 0); 
			GL11.glTexCoord2f(1,offinc + 0.125f);
			GL11.glVertex3f(4, -4, 0); 
			GL11.glEnd(); // Done Drawing The Quad
			GL11.glEndList();


			compiled = true;
		}		  
	}
	  
	  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
	  {
		  EntityExp exp = (EntityExp)entity;
		  
	      //always point straight at player so we look like a nice round sphere!	
		  float tdir = (float) Math.atan2(DangerZone.player.posx - exp.posx, DangerZone.player.posz - exp.posz);
		  double dx, dz;
		  float bri = exp.getBrightness();
		  dx = DangerZone.player.posx - exp.posx;
		  dz = DangerZone.player.posz - exp.posz;
		  dx = (float) Math.sqrt(dx*dx + dz*dz);
		  dz = (DangerZone.player.posy + DangerZone.player.eyeheight) - exp.posy;
		  dz = (float) Math.atan2(dx, dz);
		  GL11.glRotatef((float) Math.toDegrees(tdir), 0, 1, 0); 
		  GL11.glRotatef((float) Math.toDegrees(dz+Math.PI/2), 1, 0, 0); 
		  
		  GL11.glEnable(GL11.GL_BLEND);
		  GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		  int which = exp.getSubTexture();
		  switch(which){
		  case 0:
			  GL11.glColor4f(bri+0.86f, bri+0.86f, bri+0.86f, 0.70f);
			  GL11.glCallList(list1); //draw
			  break;
		  case 1:
			  GL11.glColor4f(bri+0.88f, bri+0.88f,bri+0.88f, 0.75f);
			  GL11.glCallList(list2); //draw
			  break;
		  case 2:
			  GL11.glColor4f(bri+0.90f, bri+0.90f, bri+0.90f, 0.80f);
			  GL11.glCallList(list3); //draw
			  break;
		  case 3:
			  GL11.glColor4f(bri+0.92f, bri+0.92f, bri+0.92f, 0.85f);
			  GL11.glCallList(list4); //draw
			  break;
		  case 4:
			  GL11.glColor4f(bri+0.94f, bri+0.94f, bri+0.94f, 0.90f);
			  GL11.glCallList(list5); //draw
			  break;
		  case 5:
			  GL11.glColor4f(bri+0.96f, bri+0.96f, bri+0.96f, 0.95f);
			  GL11.glCallList(list6); //draw
			  break;
		  case 6:
			  GL11.glColor4f(bri+0.98f, bri+0.98f, bri+0.98f, 1.0f);
			  GL11.glCallList(list7); //draw
			  break;
		  case 7:
			  GL11.glColor4f(bri+1f, bri+1f, bri+1f, 1f);
			  GL11.glCallList(list8); //draw
			  break;
		  default:
		  }
		  GL11.glDisable(GL11.GL_BLEND);

	  }
	  
	  public void doScale(Entity ent){
		  GL11.glScalef(0.25f, 0.25f, 0.25f); 
	  }

}
