package demomodcode;

import dangerzone.ModelBase;
import dangerzone.ModelRenderer;
import dangerzone.entities.Entity;

public class ModelDemoBeast extends ModelBase
{

  //fields
    ModelRenderer body;
    ModelRenderer leftleg;
    ModelRenderer leftwing;
    ModelRenderer rightleg;
    ModelRenderer leftkneeknot;
    ModelRenderer head;
    ModelRenderer rightwing;

  
  public ModelDemoBeast()
  {
     
	    
	      body = new ModelRenderer(this, 0, 15);
	      body.addBox(-3F, 0F, 0F, 6, 11, 5);
	      body.setRotationPoint(0F, 7F, 0F);
	      body.setTextureSize(128, 128);
	      body.mirror = true;
	      setRotation(body, 0F, 0F, 0F);
	      leftleg = new ModelRenderer(this, 0, 37);
	      leftleg.addBox(-1F, 0F, -1F, 2, 6, 2);
	      leftleg.setRotationPoint(2F, 18F, 3F);
	      leftleg.setTextureSize(128, 128);
	      leftleg.mirror = true;
	      setRotation(leftleg, 0.0349066F, 0F, 0F);
	      leftwing = new ModelRenderer(this, 21, 33);
	      leftwing.addBox(0F, 0F, -3F, 9, 0, 10);
	      leftwing.setRotationPoint(3F, 9F, 0F);
	      leftwing.setTextureSize(128, 128);
	      leftwing.mirror = true;
	      setRotation(leftwing, -0.2792527F, 0F, -0.1396263F);
	      rightleg = new ModelRenderer(this, 9, 37);
	      rightleg.addBox(-1F, 0F, -1F, 2, 6, 2);
	      rightleg.setRotationPoint(-2F, 18F, 3F);
	      rightleg.setTextureSize(128, 128);
	      rightleg.mirror = true;
	      setRotation(rightleg, 0.0349066F, 0F, 0F);
	      leftkneeknot = new ModelRenderer(this, 0, 0);
	      leftkneeknot.addBox(0F, 2F, -2F, 1, 1, 1);
	      leftkneeknot.setRotationPoint(2F, 18F, 3F);
	      leftkneeknot.setTextureSize(128, 128);
	      leftkneeknot.mirror = true;
	      setRotation(leftkneeknot, 0F, 0F, 0F);
	      head = new ModelRenderer(this, 23, 16);
	      head.addBox(-3.5F, -6F, -4F, 7, 6, 6);
	      head.setRotationPoint(0F, 7F, 3F);
	      head.setTextureSize(128, 128);
	      head.mirror = true;
	      setRotation(head, 0F, 0F, 0F);
	      rightwing = new ModelRenderer(this, 19, 45);
	      rightwing.addBox(-9F, 0F, -3F, 9, 0, 10);
	      rightwing.setRotationPoint(-3F, 9F, 0F);
	      rightwing.setTextureSize(128, 128);
	      rightwing.mirror = true;
	      setRotation(rightwing, -0.2792527F, 0F, 0.1396263F);
  }
  
  // f = lifetime ticker. Doing Math.toRadians() on it provides a nice smooth wave cycle.
  // f1 = entity velocity
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
  {
	  float newangle = 0;
	  //System.out.printf("floats: %f,  %f, %f, %f, %f, %f\n", f, f1, f2, f3, f4, f5);
	  if(f1 > 0.01){
		  newangle = (float) (Math.cos(Math.toRadians(f*8.6f)) * (float)Math.PI * 0.30F * f1);
	  }else{
		  newangle = 0.0F;
	  }

	  this.leftleg.rotateAngleX = newangle;
	  this.leftkneeknot.rotateAngleX = newangle;
	  this.rightleg.rotateAngleX = -newangle;

	  if(f2 > 25)f2 = 25;
	  if(f2 < -25)f2 = -25;
	  if(f3 > 35)f3 = 35;
	  if(f3 < -35)f3 = -35;
	  if(f4 > 20)f4 = 20;
	  if(f4 < -20)f4 = -20;

	  head.rotateAngleX = (float) Math.toRadians(f2);
	  head.rotateAngleY = (float) -Math.toRadians(f3);
	  head.rotateAngleZ = (float) Math.toRadians(f4);

	  newangle =  (float) (Math.cos(Math.toRadians(f*15.3f)) * (float)Math.PI * 0.15F * f1);
	  leftwing.rotateAngleZ = newangle;
	  rightwing.rotateAngleZ = -newangle;

	  body.render(deathfactor);
	  leftleg.render(deathfactor);
	  leftwing.render(deathfactor);
	  rightleg.render(deathfactor);
	  leftkneeknot.render(deathfactor);
	  head.render(deathfactor);
	  rightwing.render(deathfactor);

  }
  

}
