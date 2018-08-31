package dangerzone.particles;
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


public class ModelParticle extends ModelBase {
	
	private boolean compiled = false;
	private int list1, list2, list3, list4;
	
	public ModelParticle()
	{
		if(!compiled && DangerZone.wr != null){

			list1 = DangerZone.wr.getNextRenderID();
			GL11.glNewList(list1, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0.5f,0);
			GL11.glVertex3f(4, 4, 0);
			GL11.glTexCoord2f(0,0);
			GL11.glVertex3f(-4, 4, 0);
			GL11.glTexCoord2f(0,0.5f);
			GL11.glVertex3f(-4, -4, 0); 
			GL11.glTexCoord2f(0.5f,0.5f);
			GL11.glVertex3f(4, -4, 0); 
			GL11.glEnd(); // Done Drawing The Quad
			GL11.glEndList();
			
			list2 = DangerZone.wr.getNextRenderID();
			GL11.glNewList(list2, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(1,0);
			GL11.glVertex3f(4, 4, 0);
			GL11.glTexCoord2f(0.5f,0);
			GL11.glVertex3f(-4, 4, 0);
			GL11.glTexCoord2f(0.5f,0.5f);
			GL11.glVertex3f(-4, -4, 0); 
			GL11.glTexCoord2f(1,0.5f);
			GL11.glVertex3f(4, -4, 0); 
			GL11.glEnd(); // Done Drawing The Quad
			GL11.glEndList();
				
			list3 = DangerZone.wr.getNextRenderID();
			GL11.glNewList(list3, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0.5f,0.5f);
			GL11.glVertex3f(4, 4, 0);
			GL11.glTexCoord2f(0,0.5f);
			GL11.glVertex3f(-4, 4, 0);
			GL11.glTexCoord2f(0,1);
			GL11.glVertex3f(-4, -4, 0); 
			GL11.glTexCoord2f(0.5f,1);
			GL11.glVertex3f(4, -4, 0); 
			GL11.glEnd(); // Done Drawing The Quad
			GL11.glEndList();
			
			list4 = DangerZone.wr.getNextRenderID();
			GL11.glNewList(list4, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(1,0.5f);
			GL11.glVertex3f(4, 4, 0);
			GL11.glTexCoord2f(0.5f,0.5f);
			GL11.glVertex3f(-4, 4, 0);
			GL11.glTexCoord2f(0.5f,1);
			GL11.glVertex3f(-4, -4, 0); 
			GL11.glTexCoord2f(1,1);
			GL11.glVertex3f(4, -4, 0); 
			GL11.glEnd(); // Done Drawing The Quad
			GL11.glEndList();

			compiled = true;
		}		  
	}
	  
	  public void renderParticle(Particle p)
	  {
		  //All the rotation and such has been done for us... just render!
		  //GL11.glEnable(GL11.GL_BLEND);
		  //GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		  int which = p.getSubTexture();
		  switch(which){
		  case 0:
			  GL11.glCallList(list1); //draw
			  break;
		  case 1:
			  GL11.glCallList(list2); //draw
			  break;
		  case 2:
			  GL11.glCallList(list3); //draw
			  break;
		  case 3:
			  GL11.glCallList(list4); //draw
			  break;
		  default:
		  }
		  //GL11.glDisable(GL11.GL_BLEND);

	  }

}
