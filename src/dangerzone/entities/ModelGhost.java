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

import dangerzone.ModelBase;
import dangerzone.ModelRenderer;

public class ModelGhost extends ModelBase
{

  //fields
    ModelRenderer HeadAndBody;
    ModelRenderer LArm;
    ModelRenderer RArm;

  
  public ModelGhost()
  {
	  
      HeadAndBody = new ModelRenderer( 0, 0);
      HeadAndBody.addCube(-3F, 0F, -3F, 6, 21, 6);
      HeadAndBody.setRotationPoint(0F, 0F, 0F);
      HeadAndBody.setTextureSize(64, 64);
      HeadAndBody.setRotation( 0F, 0F, 0F);
      LArm = new ModelRenderer(34, 0);
      LArm.addCube(-1F, -1F, -1F, 2, 11, 2);
      LArm.setRotationPoint(3F, 6F, 0F);
      LArm.setTextureSize(64, 64);
      LArm.setRotation( 0F, 0F, -0.3316126F);
      RArm = new ModelRenderer( 25, 0);
      RArm.addCube(-1F, -1F, -1F, 2, 11, 2);
      RArm.setRotationPoint(-3F, 6F, 0F);
      RArm.setTextureSize(64, 64);
      RArm.setRotation(0F, 0F, 0.3316126F);
    
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
  {
	  
	    this.LArm.rotateAngleZ = (float) (-0.33f + Math.cos(Math.toRadians(f * 5.3F)) * (float)Math.PI * 0.05F);
	    this.RArm.rotateAngleZ = (float) (0.33f + Math.cos(Math.toRadians(f * 6.32F)) * (float)Math.PI * 0.05F);
	    this.LArm.rotateAngleX = (float) (-0.33f + Math.cos(Math.toRadians(f * 3.34F)) * (float)Math.PI * 0.05F);
	    this.RArm.rotateAngleX = (float) (0.33f + Math.cos(Math.toRadians(f * 4.36F)) * (float)Math.PI * 0.05F);
	    
	    //
	    //Make it mostly transparent, and keep its color!
	    //

	    GL11.glEnable(GL11.GL_NORMALIZE);
	    GL11.glEnable(GL11.GL_BLEND);
	    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);  
	 	GL11.glColor4f(0.75F, 0.75F, 0.75F, 0.25F);  
	 	
	    HeadAndBody.render(deathfactor);
	    LArm.render(deathfactor);
	    RArm.render(deathfactor);
	    
	    GL11.glDisable(GL11.GL_BLEND);
    
  }
  


}
