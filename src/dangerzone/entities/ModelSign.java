package dangerzone.entities;
import org.lwjgl.opengl.GL11;

import dangerzone.DangerZone;
import dangerzone.ModelBase;
import dangerzone.ModelRenderer;
import dangerzone.WorldRenderer;
import dangerzone.WorldRendererUtils;

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
public class ModelSign extends ModelBase
{

  //fields
    ModelRenderer body;
    ModelRenderer lfleg;
    ModelRenderer rfleg;
    ModelRenderer lrleg;
    ModelRenderer rrleg;
    ModelRenderer neck;
    ModelRenderer head;
    ModelRenderer lant1;
    ModelRenderer lant2;
    ModelRenderer lant3;
    ModelRenderer rant1;
    ModelRenderer rant2;
    ModelRenderer rant3;
    ModelRenderer Sign;
  
  public ModelSign()
  {
      
      body = new ModelRenderer(0, 194);
      body.addCube(-7F, 0F, -6F, 14, 18, 40);
      body.setRotationPoint(0F, -15F, 0F);
      body.setTextureSize(128, 256);
      body.mirror = true;
      body.setRotation( 0F, 0F, 0F);
      lfleg = new ModelRenderer(0, 75);
      lfleg.addCube(-2F, -2F, -2F, 4, 26, 4);
      lfleg.setRotationPoint(6F, 0F, 0F);
      lfleg.setTextureSize(128, 256);
      lfleg.mirror = true;
      lfleg.setRotation( 0F, 0F, 0F);
      rfleg = new ModelRenderer(19, 75);
      rfleg.addCube(-2F, -2F, -2F, 4, 26, 4);
      rfleg.setRotationPoint(-6F, 0F, 0F);
      rfleg.setTextureSize(128, 256);
      rfleg.mirror = true;
      rfleg.setRotation( 0F, 0F, 0F);
      lrleg = new ModelRenderer(38, 75);
      lrleg.addCube(-2F, -2F, -2F, 4, 26, 4);
      lrleg.setRotationPoint(6F, 0F, 29F);
      lrleg.setTextureSize(128, 256);
      lrleg.mirror = true;
      lrleg.setRotation( 0F, 0F, 0F);
      rrleg = new ModelRenderer( 57, 75);
      rrleg.addCube(-2F, -2F, -2F, 4, 26, 4);
      rrleg.setRotationPoint(-6F, 0F, 29F);
      rrleg.setTextureSize(128, 256);
      rrleg.mirror = true;
      rrleg.setRotation( 0F, 0F, 0F);
      neck = new ModelRenderer(0, 161);
      neck.addCube(-3F, -5F, -15F, 6, 11, 17);
      neck.setRotationPoint(0F, -9F, 0F);
      neck.setTextureSize(128, 256);
      neck.mirror = true;
      neck.setRotation( -0.837758F, 0F, 0F);
      head = new ModelRenderer(49, 161);
      head.addCube(-4F, -19F, -21F, 8, 9, 19);
      head.setRotationPoint(0F, -9F, 0F);
      head.setTextureSize(128, 256);
      head.mirror = true;
      head.setRotation( 0.2268928F, 0F, 0F);
      lant1 = new ModelRenderer(0, 32);
      lant1.addCube(5F, -18F, -7F, 5, 1, 4);
      lant1.setRotationPoint(0F, -9F, 0F);
      lant1.setTextureSize(128, 256);
      lant1.mirror = true;
      lant1.setRotation( 0.3316126F, 0F, -0.1047198F);
      lant2 = new ModelRenderer(29, 0);
      lant2.addCube(12F, -17F, -4F, 11, 1, 27);
      lant2.setRotationPoint(0F, -9F, 0F);
      lant2.setTextureSize(128, 256);
      lant2.mirror = true;
      lant2.setRotation( 0.4014257F, 0.2094395F, -0.1745329F);
      lant3 = new ModelRenderer(0, 47);
      lant3.addCube(2F, -14F, -22F, 1, 1, 8);
      lant3.setRotationPoint(0F, -9F, 0F);
      lant3.setTextureSize(128, 256);
      lant3.mirror = true;
      lant3.setRotation( -0.1570796F, -0.2269F, 0F);
      rant1 = new ModelRenderer( 0, 39);
      rant1.addCube(-10F, -18F, -7F, 5, 1, 4);
      rant1.setRotationPoint(0F, -9F, 0F);
      rant1.setTextureSize(128, 256);
      rant1.mirror = true;
      rant1.setRotation( 0.3316126F, 0F, 0.1047198F);
      rant2 = new ModelRenderer(29, 30);
      rant2.addCube(-23F, -17F, -4F, 11, 1, 27);
      rant2.setRotationPoint(0F, -9F, 0F);
      rant2.setTextureSize(128, 256);
      rant2.mirror = true;
      rant2.setRotation( 0.4014257F, -0.2094395F, 0.1745329F);
      rant3 = new ModelRenderer(0, 59);
      rant3.addCube(-3F, -14F, -22F, 1, 1, 8);
      rant3.setRotationPoint(0F, -9F, 0F);
      rant3.setTextureSize(128, 256);
      rant3.mirror = true;
      rant3.setRotation( -0.1570796F, 0.2269F, 0F);
      Sign = new ModelRenderer(this, 0, 109);
      Sign.addBox(-30F, -30F, 0F, 60, 30, 1);
      Sign.setRotationPoint(0F, -33F, 0F);
      Sign.setTextureSize(128, 256);
      Sign.mirror = true;
      setRotation(Sign, 0F, 0F, 0F);
  }
  
  // f = lifetime ticker. Doing Math.toRadians() on it provides a nice smooth wave cycle.
  // f1 = entity velocity
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
  {
	  EntitySign es = (EntitySign)entity;

	    body.render(1);
	    lfleg.render(1);
	    rfleg.render(1);
	    lrleg.render(1);
	    rrleg.render(1);
	    neck.render(1);
	    head.render(1);
	    lant1.render(1);
	    lant2.render(1);
	    lant3.render(1);
	    rant1.render(1);
	    rant2.render(1);
	    rant3.render(1);
	    
	    //now put up the sign and some text!
	    Sign.render(1);
	    
	    String s1 = es.getVarString(3);
	    String s2 = es.getVarString(4);
	    String s3 = es.getVarString(5);
	    
	    if(s1 != null && s1.length() > 0){
	    	int w1 = DangerZone.wr.font.getWidth(s1);
	    	//System.out.printf("s1 width = %d\n", w1);
	    	GL11.glPushMatrix();
	    	GL11.glScalef(0.15f, 0.15f, 0.15f);			
	    	GL11.glTranslatef(0, 0, 1);
			WorldRendererUtils.textAt(DangerZone.wr.font, - (w1/2), 550, s1);
			GL11.glTranslatef(0, 0, -9);
			GL11.glRotatef(180, 0, 1, 0);			
			WorldRendererUtils.textAt(DangerZone.wr.font, - (w1/2), 550, s1);
			GL11.glPopMatrix();
	    }
	    if(s2 != null && s2.length() > 0){
	    	int w2 = DangerZone.wr.font.getWidth(s2);
	    	//System.out.printf("s2 width = %d\n", w2);
	    	GL11.glPushMatrix();
	    	GL11.glScalef(0.15f, 0.15f, 0.15f);			
	    	GL11.glTranslatef(0, 0, 1);
			WorldRendererUtils.textAt(DangerZone.wr.font, - (w2/2), 500, s2);
			GL11.glTranslatef(0, 0, -9);
			GL11.glRotatef(180, 0, 1, 0);			
			WorldRendererUtils.textAt(DangerZone.wr.font, - (w2/2), 500, s2);
			GL11.glPopMatrix();
	    }
	    if(s3 != null && s3.length() > 0){
	    	int w3 = DangerZone.wr.font.getWidth(s3);
	    	//System.out.printf("s3 width = %d\n", w3);
	    	GL11.glPushMatrix();
	    	GL11.glScalef(0.15f, 0.15f, 0.15f);			
	    	GL11.glTranslatef(0, 0, 1);
			WorldRendererUtils.textAt(DangerZone.wr.font, - (w3/2), 450, s3);
			GL11.glTranslatef(0, 0, -9);
			GL11.glRotatef(180, 0, 1, 0);			
			WorldRendererUtils.textAt(DangerZone.wr.font, - (w3/2), 450, s3);
			GL11.glPopMatrix();
	    }
	    
	    WorldRenderer.last_texture = -1; //force it to reload because TEXT messes everything up...
    
  }
  
  public void doScale(Entity ent){
	  GL11.glScalef(0.25f, 0.25f, 0.25f); 
  }
  

}
