package dangerzone.entities;

import org.lwjgl.opengl.GL11;

public class ModelMinnow extends ModelFish {

	
	  public void doScale(Entity ent){
		  GL11.glScalef(0.25f, 0.25f, 0.25f);
		  if(ent.isBaby()){
			  GL11.glScalef(0.25f, 0.25f, 0.25f);
		  }
	  }
}
