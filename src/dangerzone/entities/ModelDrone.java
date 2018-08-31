package dangerzone.entities;

import org.lwjgl.opengl.GL11;

import dangerzone.ModelBase;
import dangerzone.ModelRenderer;

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

public class ModelDrone extends ModelBase
{

    ModelRenderer cam1;
    ModelRenderer cam2;
    ModelRenderer mount1;
    ModelRenderer mount2;
    ModelRenderer mount3;
    ModelRenderer body;
    ModelRenderer ext2;
    ModelRenderer ext1;
    ModelRenderer motor4;
    ModelRenderer motor1;
    ModelRenderer motor2;
    ModelRenderer motor3;
    ModelRenderer shaft4;
    ModelRenderer shaft1;
    ModelRenderer shaft2;
    ModelRenderer shaft3;
    ModelRenderer prop4;
    ModelRenderer prop1;
    ModelRenderer prop2;
    ModelRenderer prop3;

  
  public ModelDrone()
  {
    
	    
	     cam1 = new ModelRenderer(this, 0, 18);
	      cam1.addBox(-3F, 0F, 0F, 6, 3, 1);
	      cam1.setRotationPoint(0F, 21F, 1F);
	      cam1.setTextureSize(128, 64);
	      cam1.mirror = true;
	      setRotation(cam1, 0.4014257F, 0F, 0F);
	      cam2 = new ModelRenderer(this, 15, 18);
	      cam2.addBox(-1.5F, 0.5F, -1F, 3, 2, 1);
	      cam2.setRotationPoint(0F, 21F, 1F);
	      cam2.setTextureSize(128, 64);
	      cam2.mirror = true;
	      setRotation(cam2, 0.4014257F, 0F, 0F);
	      mount1 = new ModelRenderer(this, 25, 18);
	      mount1.addBox(0F, 0F, 0F, 1, 5, 1);
	      mount1.setRotationPoint(-4F, 19F, 2F);
	      mount1.setTextureSize(128, 64);
	      mount1.mirror = true;
	      setRotation(mount1, 0F, 0F, 0F);
	      mount2 = new ModelRenderer(this, 30, 19);
	      mount2.addBox(0F, 0F, 0F, 3, 1, 1);
	      mount2.setRotationPoint(-3F, 19F, 2F);
	      mount2.setTextureSize(128, 64);
	      mount2.mirror = true;
	      setRotation(mount2, 0F, 0F, 0F);
	      mount3 = new ModelRenderer(this, 40, 18);
	      mount3.addBox(0F, 0F, 0F, 1, 3, 1);
	      mount3.setRotationPoint(0F, 17F, 2F);
	      mount3.setTextureSize(128, 64);
	      mount3.mirror = true;
	      setRotation(mount3, 0F, 0F, 0F);
	      body = new ModelRenderer(this, 0, 0);
	      body.addBox(-6F, 0F, 0F, 12, 3, 12);
	      body.setRotationPoint(0F, 14F, -5F);
	      body.setTextureSize(128, 64);
	      body.mirror = true;
	      setRotation(body, 0F, 0F, 0F);
	      ext2 = new ModelRenderer(this, 0, 30);
	      ext2.addBox(-16F, 0F, 0F, 31, 1, 1);
	      ext2.setRotationPoint(0F, 13F, 0F);
	      ext2.setTextureSize(128, 64);
	      ext2.mirror = true;
	      setRotation(ext2, 0F, 0.7853982F, 0F);
	      ext1 = new ModelRenderer(this, 0, 26);
	      ext1.addBox(-15F, 0F, 0F, 31, 1, 1);
	      ext1.setRotationPoint(0F, 13F, 0F);
	      ext1.setTextureSize(128, 64);
	      ext1.mirror = true;
	      setRotation(ext1, 0F, -0.7853982F, 0F);
	      motor4 = new ModelRenderer(this, 30, 34);
	      motor4.addBox(-1F, 0F, -1F, 2, 3, 2);
	      motor4.setRotationPoint(11F, 11F, -10F);
	      motor4.setTextureSize(128, 64);
	      motor4.mirror = true;
	      setRotation(motor4, 0F, 0F, 0F);
	      motor1 = new ModelRenderer(this, 0, 34);
	      motor1.addBox(-1F, 0F, -1F, 2, 3, 2);
	      motor1.setRotationPoint(-11F, 11F, -10F);
	      motor1.setTextureSize(128, 64);
	      motor1.mirror = true;
	      setRotation(motor1, 0F, 0F, 0F);
	      motor2 = new ModelRenderer(this, 10, 34);
	      motor2.addBox(-1F, 0F, -1F, 2, 3, 2);
	      motor2.setRotationPoint(-11F, 11F, 12F);
	      motor2.setTextureSize(128, 64);
	      motor2.mirror = true;
	      setRotation(motor2, 0F, 0F, 0F);
	      motor3 = new ModelRenderer(this, 20, 34);
	      motor3.addBox(-1F, 0F, -1F, 2, 3, 2);
	      motor3.setRotationPoint(11F, 11F, 12F);
	      motor3.setTextureSize(128, 64);
	      motor3.mirror = true;
	      setRotation(motor3, 0F, 0F, 0F);
	      shaft4 = new ModelRenderer(this, 0, 40);
	      shaft4.addBox(-0.5F, 0F, -0.5F, 1, 1, 1);
	      shaft4.setRotationPoint(11F, 10F, -10F);
	      shaft4.setTextureSize(128, 64);
	      shaft4.mirror = true;
	      setRotation(shaft4, 0F, 0F, 0F);
	      shaft1 = new ModelRenderer(this, 6, 40);
	      shaft1.addBox(-0.5F, 0F, -0.5F, 1, 1, 1);
	      shaft1.setRotationPoint(-11F, 10F, -10F);
	      shaft1.setTextureSize(128, 64);
	      shaft1.mirror = true;
	      setRotation(shaft1, 0F, 0F, 0F);
	      shaft2 = new ModelRenderer(this, 12, 40);
	      shaft2.addBox(-0.5F, 0F, -0.5F, 1, 1, 1);
	      shaft2.setRotationPoint(-11F, 10F, 12F);
	      shaft2.setTextureSize(128, 64);
	      shaft2.mirror = true;
	      setRotation(shaft2, 0F, 0F, 0F);
	      shaft3 = new ModelRenderer(this, 18, 40);
	      shaft3.addBox(-0.5F, 0F, -0.5F, 1, 1, 1);
	      shaft3.setRotationPoint(11F, 10F, 12F);
	      shaft3.setTextureSize(128, 64);
	      shaft3.mirror = true;
	      setRotation(shaft3, 0F, 0F, 0F);
	      prop4 = new ModelRenderer(this, 0, 44);
	      prop4.addBox(-6F, 0F, -1F, 12, 1, 2);
	      prop4.setRotationPoint(11F, 9F, -10F);
	      prop4.setTextureSize(128, 64);
	      prop4.mirror = true;
	      setRotation(prop4, 0F, 0F, 0F);
	      prop1 = new ModelRenderer(this, 0, 49);
	      prop1.addBox(-6F, 0F, -1F, 12, 1, 2);
	      prop1.setRotationPoint(-11F, 9F, -10F);
	      prop1.setTextureSize(128, 64);
	      prop1.mirror = true;
	      setRotation(prop1, 0F, 0F, 0F);
	      prop2 = new ModelRenderer(this, 30, 44);
	      prop2.addBox(-6F, 0F, -1F, 12, 1, 2);
	      prop2.setRotationPoint(-11F, 9F, 12F);
	      prop2.setTextureSize(128, 64);
	      prop2.mirror = true;
	      setRotation(prop2, 0F, 0F, 0F);
	      prop3 = new ModelRenderer(this, 29, 50);
	      prop3.addBox(-6F, 0F, -1F, 12, 1, 2);
	      prop3.setRotationPoint(11F, 9F, 12F);
	      prop3.setTextureSize(128, 64);
	      prop3.mirror = true;
	      setRotation(prop3, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
  {
	  float newangle = 0;
	  if(!(entity instanceof Drone))return;
	  Drone dr = (Drone)entity;

	  newangle =  (float) (Math.toRadians((f*19.3f)%260f));
	  prop1.rotateAngleY = newangle;
	  prop2.rotateAngleY = newangle;
	  prop3.rotateAngleY = newangle;
	  prop4.rotateAngleY = newangle;

	  float spinz = dr.getTiltLR();
	  if(spinz != 0){
		  GL11.glPushMatrix();
		  GL11.glRotatef(spinz*25, 0, 0, 1); //we push the matrix and let opengl do z rotation for us! otherwise... massive matrix math...
	  }

	  cam1.render(deathfactor);
	  cam2.render(deathfactor);
	  mount1.render(deathfactor);
	  mount2.render(deathfactor);
	  mount3.render(deathfactor);
	  body.render(deathfactor);
	  ext2.render(deathfactor);
	  ext1.render(deathfactor);
	  motor4.render(deathfactor);
	  motor1.render(deathfactor);
	  motor2.render(deathfactor);
	  motor3.render(deathfactor);
	  shaft4.render(deathfactor);
	  shaft1.render(deathfactor);
	  shaft2.render(deathfactor);
	  shaft3.render(deathfactor);
	  prop4.render(deathfactor);
	  prop1.render(deathfactor);
	  prop2.render(deathfactor);
	  prop3.render(deathfactor);

	  if(spinz != 0){
		  GL11.glPopMatrix();
	  }
	  

  }
  


}
