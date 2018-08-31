package dangerzone.entities;

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

public class ModelButterfly extends ModelBase
{

  //fields
    ModelRenderer body;
    ModelRenderer leftwing;
    ModelRenderer rightwing;
    ModelRenderer leftwing2;
    ModelRenderer rightwing2;
    ModelRenderer leftwing3;
    ModelRenderer rightwing3;
    ModelRenderer head;
    ModelRenderer leftwing4;
    ModelRenderer rightwing4;

  
  public ModelButterfly()
  {
    
    body = new ModelRenderer(21, 19);
    body.addCube(0F, 0F, -4F, 1, 1, 8);
    body.setRotationPoint(0F, 17F, 0F);
    body.setTextureSize(64, 32);
    body.setRotation(0F, 0F, 0F);   
    leftwing = new ModelRenderer(43, 24);
    leftwing.addCube(0F, 0F, -4F, 5, 1, 5);
    leftwing.setRotationPoint(1F, 17F, 0F);
    leftwing.setTextureSize(64, 32);
    leftwing.setRotation(0F, 0F, 0F);
    rightwing = new ModelRenderer(43, 17);
    rightwing.addCube(-5F, 0F, -4F, 5, 1, 5);
    rightwing.setRotationPoint(0F, 17F, 0F);
    rightwing.setTextureSize(64, 32);
    rightwing.setRotation(0F, 0F, 0F);
    leftwing2 = new ModelRenderer(0, 0);
    leftwing2.addCube(1F, 0F, -6F, 6, 1, 7);
    leftwing2.setRotationPoint(1F, 17F, 0F);
    leftwing2.setTextureSize(64, 32);
    leftwing2.setRotation(0F, 0F, 0F);
    rightwing2 = new ModelRenderer(29, 0);
    rightwing2.addCube(-7F, 0F, -6F, 6, 1, 7);
    rightwing2.setRotationPoint(0F, 17F, 0F);
    rightwing2.setTextureSize(64, 32);
    rightwing2.setRotation(0F, 0F, 0F);
    leftwing3 = new ModelRenderer(0, 9);
    leftwing3.addCube(0F, 0F, 1F, 5, 1, 5);
    leftwing3.setRotationPoint(1F, 17F, 0F);
    leftwing3.setTextureSize(64, 32);
    leftwing3.setRotation(0F, 0F, 0F);
    rightwing3 = new ModelRenderer(27, 9);
    rightwing3.addCube(-5F, 0F, 1F, 5, 1, 5);
    rightwing3.setRotationPoint(0F, 17F, 0F);
    rightwing3.setTextureSize(64, 32);
    rightwing3.setRotation(0F, 0F, 0F);
    head = new ModelRenderer(21, 11);
    head.addCube(0F, 0F, -6F, 1, 1, 1);
    head.setRotationPoint(0F, 17F, 1F);
    head.setTextureSize(64, 32);
    head.setRotation(0F, 0F, 0F);
    leftwing4 = new ModelRenderer(2, 24);
    leftwing4.addCube(0F, 0F, 6F, 1, 1, 7);
    leftwing4.setRotationPoint(1F, 17F, 0F);
    leftwing4.setTextureSize(64, 32);
    leftwing4.setRotation(0F, 0F, 0F);
    rightwing4 = new ModelRenderer(2, 16);
    rightwing4.addCube(-1F, 0F, 6F, 1, 1, 7);
    rightwing4.setRotationPoint(0F, 17F, 0F);
    rightwing4.setTextureSize(64, 32);
    rightwing4.setRotation(0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
  {
	  float newangle = 0;
	  	  
	  newangle =  (float) (Math.cos(Math.toRadians(f*19.3f)) * (float)Math.PI * 0.25F);
	  //System.out.printf("newangle = %f\n", newangle);
    
    head.render(deathfactor);
    body.render(deathfactor);
    
    //this.rightwing.rotateAngleZ = (float) (Math.cos(f2 * 1.3F * this.wingspeed) * (float)Math.PI * 0.25F);
    this.rightwing.rotateAngleZ = newangle;
    this.rightwing2.rotateAngleZ = this.rightwing.rotateAngleZ;
    this.rightwing3.rotateAngleZ = this.rightwing.rotateAngleZ;
    this.rightwing4.rotateAngleZ = this.rightwing.rotateAngleZ;
    
    this.leftwing.rotateAngleZ = -this.rightwing.rotateAngleZ;
    this.leftwing2.rotateAngleZ = -this.rightwing.rotateAngleZ;
    this.leftwing3.rotateAngleZ = -this.rightwing.rotateAngleZ;
    this.leftwing4.rotateAngleZ = -this.rightwing.rotateAngleZ;
    
    leftwing.render(deathfactor);
    rightwing.render(deathfactor);
    leftwing2.render(deathfactor);
    rightwing2.render(deathfactor);
    leftwing3.render(deathfactor);
    rightwing3.render(deathfactor);  
    leftwing4.render(deathfactor);
    rightwing4.render(deathfactor);
    
  }
  


}
