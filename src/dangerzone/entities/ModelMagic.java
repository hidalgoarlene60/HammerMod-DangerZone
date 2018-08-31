package dangerzone.entities;


import org.lwjgl.opengl.GL11;

import dangerzone.DangerZone;
import dangerzone.ModelBase;
import dangerzone.entities.Entity;

public class ModelMagic extends ModelBase {

	private boolean compiled = false;
	private int list1;
	
	public ModelMagic()
	{
		super();		  
	}	  

	/*
	 * rendering for Item/Block when floating around as an entity.
	 */
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
	{
		EntityMagic exp = (EntityMagic)entity;
		if(exp.getTexture() == null)return; //nevermind!
		
		
		float spinz = entity.getSpinz();
		if(spinz != 0){
			GL11.glPushMatrix();
			GL11.glRotatef(spinz, 0, 0, 1); //we push the matrix and let opengl do z rotation for us! otherwise... massive matrix math...
		}
		if(!compiled){
			doCompile();
		}

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1f, 1f, 1f, exp.getDensity());  

		
		GL11.glCallList(list1); //draw

		GL11.glDisable(GL11.GL_BLEND);

		if(spinz != 0){
			GL11.glPopMatrix();
		}
	}

	public void doScale(Entity ent){
		EntityMagic pf = (EntityMagic)ent;
		float scl = pf.getScale();
		if(scl > 20)scl = 20;
		GL11.glScalef(4f*scl, 4f*scl, 4f*scl);		
	}

	private void doCompile(){

		list1 = DangerZone.wr.getNextRenderID();
		GL11.glNewList(list1, GL11.GL_COMPILE);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(1,0);
		GL11.glVertex3f(4, 4, 0);
		GL11.glTexCoord2f(0,0);
		GL11.glVertex3f(-4, 4, 0);
		GL11.glTexCoord2f(0,1);
		GL11.glVertex3f(-4, -4, 0); 
		GL11.glTexCoord2f(1,1);
		GL11.glVertex3f(4, -4, 0); 
		GL11.glEnd(); // Done Drawing The Quad
		GL11.glEndList();

		compiled = true;
	}
}


