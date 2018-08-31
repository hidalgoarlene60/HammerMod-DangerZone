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


public class ModelCockroach extends ModelBase
{

  //fields
    ModelRenderer head;
    ModelRenderer body1;
    ModelRenderer body2;
    ModelRenderer body3;
    ModelRenderer ab1;
    ModelRenderer ab2;
    ModelRenderer ab3;
    ModelRenderer ab4;
    ModelRenderer ab5;
    ModelRenderer ab6;
    ModelRenderer ab7;
    ModelRenderer ab8;
    ModelRenderer belly;
    ModelRenderer ll1;
    ModelRenderer rl1;
    ModelRenderer ll2;
    ModelRenderer rl2;
    ModelRenderer ll3;
    ModelRenderer rl3;
    ModelRenderer la;
    ModelRenderer ra;

  
  public ModelCockroach()
  {
    
      head = new ModelRenderer( 44, 0);
      head.addCube(-4F, 0F, 0F, 8, 1, 3);
      head.setRotationPoint(0F, 22F, -10F);
      head.setTextureSize(128, 128);
      head.mirror = true;
      head.setRotation( 0.296706F, 0F, 0F);
      body1 = new ModelRenderer( 42, 6);
      body1.addCube(-4.5F, 0F, 0F, 9, 1, 5);
      body1.setRotationPoint(0F, 21F, -7F);
      body1.setTextureSize(128, 128);
      body1.mirror = true;
      body1.setRotation( 0.0698132F, 0F, 0F);
      body2 = new ModelRenderer( 42, 14);
      body2.addCube(-5F, 0F, 0F, 10, 1, 4);
      body2.setRotationPoint(0F, 21F, -2F);
      body2.setTextureSize(128, 128);
      body2.mirror = true;
      body2.setRotation( 0.0698132F, 0F, 0F);
      body3 = new ModelRenderer( 42, 21);
      body3.addCube(-5F, 0F, 0F, 10, 1, 4);
      body3.setRotationPoint(0F, 21F, 2F);
      body3.setTextureSize(128, 128);
      body3.mirror = true;
      body3.setRotation( 0.0698132F, 0F, 0F);
      ab1 = new ModelRenderer( 0, 6);
      ab1.addCube(-4F, 0F, 0F, 8, 1, 2);
      ab1.setRotationPoint(0F, 21F, 6F);
      ab1.setTextureSize(128, 128);
      ab1.mirror = true;
      ab1.setRotation( 0.0698132F, 0F, 0F);
      ab2 = new ModelRenderer( 0, 10);
      ab2.addCube(-4F, 0F, 0F, 8, 1, 2);
      ab2.setRotationPoint(0F, 21F, 8F);
      ab2.setTextureSize(128, 128);
      ab2.mirror = true;
      ab2.setRotation( 0.0698132F, 0F, 0F);
      ab3 = new ModelRenderer( 0, 14);
      ab3.addCube(-4F, 0F, 0F, 8, 1, 2);
      ab3.setRotationPoint(0F, 21F, 10F);
      ab3.setTextureSize(128, 128);
      ab3.mirror = true;
      ab3.setRotation( 0.0698132F, 0F, 0F);
      ab4 = new ModelRenderer( 0, 18);
      ab4.addCube(-4F, 0F, 0F, 8, 1, 2);
      ab4.setRotationPoint(0F, 21F, 12F);
      ab4.setTextureSize(128, 128);
      ab4.mirror = true;
      ab4.setRotation( 0.0698132F, 0F, 0F);
      ab5 = new ModelRenderer( 0, 22);
      ab5.addCube(-4F, 0F, 0F, 8, 1, 2);
      ab5.setRotationPoint(0F, 21F, 14F);
      ab5.setTextureSize(128, 128);
      ab5.mirror = true;
      ab5.setRotation( 0.0698132F, 0F, 0F);
      ab6 = new ModelRenderer( 0, 26);
      ab6.addCube(-4F, 0F, 0F, 8, 1, 2);
      ab6.setRotationPoint(0F, 21F, 16F);
      ab6.setTextureSize(128, 128);
      ab6.mirror = true;
      ab6.setRotation( 0.0698132F, 0F, 0F);
      ab7 = new ModelRenderer( 0, 30);
      ab7.addCube(-4F, 0F, 0F, 8, 1, 2);
      ab7.setRotationPoint(0F, 21F, 18F);
      ab7.setTextureSize(128, 128);
      ab7.mirror = true;
      ab7.setRotation( 0.0698132F, 0F, 0F);
      ab8 = new ModelRenderer( 0, 34);
      ab8.addCube(-3F, 0F, 0F, 6, 1, 2);
      ab8.setRotationPoint(0F, 21F, 20F);
      ab8.setTextureSize(128, 128);
      ab8.mirror = true;
      ab8.setRotation( 0.0698132F, 0F, 0F);
      belly = new ModelRenderer( 0, 95);
      belly.addCube(-3F, 0F, -9F, 6, 1, 28);
      belly.setRotationPoint(0F, 22F, 0F);
      belly.setTextureSize(128, 128);
      belly.mirror = true;
      belly.setRotation( 0F, 0F, 0F);
      ll1 = new ModelRenderer( 0, 42);
      ll1.addCube(0F, 0F, 0F, 5, 1, 1);
      ll1.setRotationPoint(2F, 22F, -4F);
      ll1.setTextureSize(128, 128);
      ll1.mirror = true;
      ll1.setRotation( 0F, 0F, 0.2443461F);
      rl1 = new ModelRenderer( 14, 42);
      rl1.addCube(-5F, 0F, 0F, 5, 1, 1);
      rl1.setRotationPoint(-2F, 22F, -4F);
      rl1.setTextureSize(128, 128);
      rl1.mirror = true;
      rl1.setRotation( 0F, 0F, -0.2443461F);
      ll2 = new ModelRenderer( 0, 46);
      ll2.addCube(0F, 0F, 0F, 7, 1, 1);
      ll2.setRotationPoint(2F, 22F, 0F);
      ll2.setTextureSize(128, 128);
      ll2.mirror = true;
      ll2.setRotation( 0F, 0F, 0.1745329F);
      rl2 = new ModelRenderer( 18, 46);
      rl2.addCube(-7F, 0F, 0F, 7, 1, 1);
      rl2.setRotationPoint(-2F, 22F, 0F);
      rl2.setTextureSize(128, 128);
      rl2.mirror = true;
      rl2.setRotation( 0F, 0F, -0.1745329F);
      ll3 = new ModelRenderer( 0, 50);
      ll3.addCube(0F, 0F, 0F, 7, 1, 1);
      ll3.setRotationPoint(2F, 22F, 4F);
      ll3.setTextureSize(128, 128);
      ll3.mirror = true;
      ll3.setRotation( 0F, 0F, 0.1745329F);
      rl3 = new ModelRenderer( 18, 50);
      rl3.addCube(-7F, 0F, 0F, 7, 1, 1);
      rl3.setRotationPoint(-2F, 22F, 4F);
      rl3.setTextureSize(128, 128);
      rl3.mirror = true;
      rl3.setRotation( 0F, 0F, -0.1745329F);
      la = new ModelRenderer( 0, 75);
      la.addCube(0F, 0F, -16F, 1, 1, 16);
      la.setRotationPoint(1F, 22F, -9F);
      la.setTextureSize(128, 128);
      la.mirror = true;
      la.setRotation( -0.0523599F, -0.2094395F, 0F);
      ra = new ModelRenderer( 36, 75);
      ra.addCube(0F, 0F, -16F, 1, 1, 16);
      ra.setRotationPoint(-2F, 22F, -9F);
      ra.setTextureSize(128, 128);
      ra.mirror = true;
      ra.setRotation( -0.0523599F, 0.2094395F, 0F);
      
      //draw as one piece for efficient graphics
      body1.connectPart(body2);
      body1.connectPart(body3);
      body1.connectPart(ab1);
      body1.connectPart(ab2);
      body1.connectPart(ab3);
      body1.connectPart(ab4);
      body1.connectPart(ab5);
      body1.connectPart(ab6);
      body1.connectPart(ab7);
      body1.connectPart(ab8);
      body1.connectPart(belly);
      body1.finishConnect();
      
  }
  
  // f = lifetime ticker. Doing Math.toRadians() on it provides a nice smooth wave cycle.
  // f1 = entity velocity
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
  {
	  Cockroach r = (Cockroach)entity;
	    float newangle = 0;
	    float newangle2 = 0;
	    //System.out.printf("floats: %f,  %f, %f, %f, %f, %f\n", f, f1, f2, f3, f4, f5);
	    if(f1 > 0.01){
	    	newangle = (float) (Math.cos(Math.toRadians(f*27.6f)) * (float)Math.PI * 0.35F * f1);
	    }else{
	    	newangle = 0.0F;
	    }
	    
	    this.ll1.rotateAngleY = newangle;
	    this.rl1.rotateAngleY = -newangle;
	    this.ll2.rotateAngleY = -newangle;
	    this.rl2.rotateAngleY = newangle;
	    this.ll3.rotateAngleY = newangle;
	    this.rl3.rotateAngleY = -newangle;

	    if(r.getAttacking()){
	    	newangle = (float) (Math.cos(Math.toRadians(f*27.6f)) * (float)Math.PI * 0.15F);
	    	newangle2 = (float) (Math.cos(Math.toRadians(f*20.6f)) * (float)Math.PI * 0.15F);
	    }else{
	    	newangle = (float) (Math.cos(Math.toRadians(f*4.6f)) * (float)Math.PI * 0.05F);
	    	newangle2 = (float) (Math.cos(Math.toRadians(f*3.6f)) * (float)Math.PI * 0.05F);
	    }
	    
	    la.rotateAngleY = -0.209f + newangle;
	    ra.rotateAngleY = 0.209f + newangle2;
	    
	    head.render(deathfactor);
	    body1.render(deathfactor);
	    ll1.render(deathfactor);
	    rl1.render(deathfactor);
	    ll2.render(deathfactor);
	    rl2.render(deathfactor);
	    ll3.render(deathfactor);
	    rl3.render(deathfactor);
	    la.render(deathfactor);
	    ra.render(deathfactor);

    
  }
  
  public void doScale(Entity ent){
	  super.doScale(ent);
	  GL11.glScalef(0.25f, 0.35f, 0.25f); //-example to do nothing. Scale your critter here if you need to!
  }
  

}
