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

public class ModelBulletBat extends ModelBase
{

  //fields
    ModelRenderer body;
    ModelRenderer head;
    ModelRenderer lfang;
    ModelRenderer rfang;
    ModelRenderer lear;
    ModelRenderer rear;
    ModelRenderer lwing1;
    ModelRenderer rwing1;
    ModelRenderer lwing2;
    ModelRenderer rwing2;
    ModelRenderer lwing3;
    ModelRenderer rwing3;

  
  public ModelBulletBat()
  {
    
	    
	      body = new ModelRenderer(this, 0, 14);
	      body.addBox(-1.5F, -1F, -4F, 3, 3, 9);
	      body.setRotationPoint(0F, 20F, 0F);
	      body.setTextureSize(64, 64);
	      body.mirror = true;
	      setRotation(body, 0F, 0F, 0F);
	      head = new ModelRenderer(this, 33, 14);
	      head.addBox(-1F, -1F, -1F, 2, 2, 2);
	      head.setRotationPoint(0F, 21F, -5F);
	      head.setTextureSize(64, 64);
	      head.mirror = true;
	      setRotation(head, 0F, 0F, 0F);
	      lfang = new ModelRenderer(this, 30, 20);
	      lfang.addBox(0F, 0F, -3F, 1, 1, 3);
	      lfang.setRotationPoint(0F, 21F, -6F);
	      lfang.setTextureSize(64, 64);
	      lfang.mirror = true;
	      setRotation(lfang, 0.5934119F, -0.296706F, 0F);
	      rfang = new ModelRenderer(this, 40, 20);
	      rfang.addBox(-1F, 0F, -3F, 1, 1, 3);
	      rfang.setRotationPoint(0F, 21F, -6F);
	      rfang.setTextureSize(64, 64);
	      rfang.mirror = true;
	      setRotation(rfang, 0.5934119F, 0.296706F, 0F);
	      lear = new ModelRenderer(this, 28, 3);
	      lear.addBox(0F, -6F, 0F, 1, 6, 2);
	      lear.setRotationPoint(0F, 20F, -5F);
	      lear.setTextureSize(64, 64);
	      lear.mirror = true;
	      setRotation(lear, -0.6981317F, 0.4537856F, 0F);
	      rear = new ModelRenderer(this, 37, 3);
	      rear.addBox(-1F, -6F, 0F, 1, 6, 2);
	      rear.setRotationPoint(0F, 20F, -5F);
	      rear.setTextureSize(64, 64);
	      rear.mirror = true;
	      setRotation(rear, -0.6981317F, -0.4537856F, 0F);
	      lwing1 = new ModelRenderer(this, 0, 27);
	      lwing1.addBox(0F, 0F, 0F, 6, 1, 8);
	      lwing1.setRotationPoint(1F, 20F, -2F);
	      lwing1.setTextureSize(64, 64);
	      lwing1.mirror = true;
	      setRotation(lwing1, 0F, 0F, 0F);
	      rwing1 = new ModelRenderer(this, 29, 27);
	      rwing1.addBox(-6F, 0F, 0F, 6, 1, 8);
	      rwing1.setRotationPoint(-1F, 20F, -2F);
	      rwing1.setTextureSize(64, 64);
	      rwing1.mirror = true;
	      setRotation(rwing1, 0F, 0F, 0F);
	      lwing2 = new ModelRenderer(this, 0, 37);
	      lwing2.addBox(6F, 0F, 2F, 3, 1, 10);
	      lwing2.setRotationPoint(1F, 20F, -2F);
	      lwing2.setTextureSize(64, 64);
	      lwing2.mirror = true;
	      setRotation(lwing2, 0F, 0F, 0F);
	      rwing2 = new ModelRenderer(this, 27, 37);
	      rwing2.addBox(-9F, 0F, 2F, 3, 1, 10);
	      rwing2.setRotationPoint(-1F, 20F, -2F);
	      rwing2.setTextureSize(64, 64);
	      rwing2.mirror = true;
	      setRotation(rwing2, 0F, 0F, 0F);
	      lwing3 = new ModelRenderer(this, 0, 49);
	      lwing3.addBox(9F, 0F, 8F, 1, 1, 12);
	      lwing3.setRotationPoint(1F, 20F, -2F);
	      lwing3.setTextureSize(64, 64);
	      lwing3.mirror = true;
	      setRotation(lwing3, 0F, 0F, 0F);
	      rwing3 = new ModelRenderer(this, 27, 49);
	      rwing3.addBox(-10F, 0F, 8F, 1, 1, 12);
	      rwing3.setRotationPoint(-1F, 20F, -2F);
	      rwing3.setTextureSize(64, 64);
	      rwing3.mirror = true;
	      setRotation(rwing3, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
  {
	  float newangle = 0;
	  	  
	  newangle =  (float) (Math.cos(Math.toRadians(f*25.3f)) * (float)Math.PI * 0.25F);
	  //System.out.printf("newangle = %f\n", newangle);
    
    head.render(deathfactor);
    body.render(deathfactor);
    
    //this.rightwing.rotateAngleZ = (float) (Math.cos(f2 * 1.3F * this.wingspeed) * (float)Math.PI * 0.25F);
    this.lwing1.rotateAngleZ = newangle;
    this.lwing2.rotateAngleZ = this.lwing1.rotateAngleZ;
    this.lwing3.rotateAngleZ = this.lwing1.rotateAngleZ;
    
    this.rwing1.rotateAngleZ = -newangle;
    this.rwing2.rotateAngleZ = this.rwing1.rotateAngleZ;
    this.rwing3.rotateAngleZ = this.rwing1.rotateAngleZ;

    
    body.render(deathfactor);
    head.render(deathfactor);
    lfang.render(deathfactor);
    rfang.render(deathfactor);
    lear.render(deathfactor);
    rear.render(deathfactor);
    lwing1.render(deathfactor);
    rwing1.render(deathfactor);
    lwing2.render(deathfactor);
    rwing2.render(deathfactor);
    lwing3.render(deathfactor);
    rwing3.render(deathfactor);
    
  }
  


}
