package dangerzone.entities;



import dangerzone.ModelBase;
import dangerzone.ModelRenderer;
import dangerzone.entities.Entity;

public class ModelHammerhead extends ModelBase
{

    ModelRenderer body1;
    ModelRenderer neck;
    ModelRenderer head1;
    ModelRenderer body2;
    ModelRenderer jaw;
    ModelRenderer tail1;
    ModelRenderer tail2;
    ModelRenderer tail3;
    ModelRenderer tail4;
    ModelRenderer tail5;
    ModelRenderer tail6;
    ModelRenderer tail7;
    ModelRenderer lffin;
    ModelRenderer rffin;
    ModelRenderer topfin1;
    ModelRenderer topfin2;

  
  public ModelHammerhead()
  {
	    
	    
      body1 = new ModelRenderer(this, 0, 64);
      body1.addBox(-8F, -6F, 0F, 16, 14, 16);
      body1.setRotationPoint(0F, 14F, 0F);
      body1.setTextureSize(128,256);
      body1.mirror = true;
      setRotation(body1, 0F, 0F, 0F);
      neck = new ModelRenderer(this, 0, 95);
      neck.addBox(-6F, -4F, -6F, 12, 8, 8);
      neck.setRotationPoint(0F, 17F, 0F);
      neck.setTextureSize(128,256);
      neck.mirror = true;
      setRotation(neck, 0F, 0F, 0F);
      head1 = new ModelRenderer(this, 0, 112);
      head1.addBox(-24F, -1F, -16F, 48, 3, 12);
      head1.setRotationPoint(0F, 17F, 0F);
      head1.setTextureSize(128,256);
      head1.mirror = true;
      setRotation(head1, 0F, 0F, 0F);
      body2 = new ModelRenderer(this, 0, 129);
      body2.addBox(-9F, -8F, 0F, 18, 16, 21);
      body2.setRotationPoint(0F, 15F, 16F);
      body2.setTextureSize(128,256);
      body2.mirror = true;
      setRotation(body2, 0F, 0F, 0F);
      jaw = new ModelRenderer(this, 41, 95);
      jaw.addBox(-7F, -1F, -13F, 14, 2, 13);
      jaw.setRotationPoint(0F, 22F, 16F);
      jaw.setTextureSize(128,256);
      jaw.mirror = true;
      setRotation(jaw, 0.1047198F, 0F, 0F);
      tail1 = new ModelRenderer(this, 0, 167);
      tail1.addBox(-8F, -7F, 0F, 16, 14, 21);
      tail1.setRotationPoint(0F, 15F, 37F);
      tail1.setTextureSize(128,256);
      tail1.mirror = true;
      setRotation(tail1, 0F, 0F, 0F);
      tail2 = new ModelRenderer(this, 0, 205);
      tail2.addBox(-7F, -6F, 0F, 14, 12, 21);
      tail2.setRotationPoint(0F, 15F, 55F);
      tail2.setTextureSize(128,256);
      tail2.mirror = true;
      setRotation(tail2, 0F, 0F, 0F);
      tail3 = new ModelRenderer(this, 0, 0);
      tail3.addBox(-6F, -5F, 0F, 12, 10, 21);
      tail3.setRotationPoint(0F, 15F, 74F);
      tail3.setTextureSize(128,256);
      tail3.mirror = true;
      setRotation(tail3, 0F, 0F, 0F);
      tail4 = new ModelRenderer(this, 0, 34);
      tail4.addBox(-5F, -4F, 0F, 10, 8, 21);
      tail4.setRotationPoint(0F, 15F, 93F);
      tail4.setTextureSize(128,256);
      tail4.mirror = true;
      setRotation(tail4, 0F, 0F, 0F);
      tail5 = new ModelRenderer(this, 73, 49);
      tail5.addBox(-3F, -3F, 0F, 6, 6, 21);
      tail5.setRotationPoint(0F, 15F, 112F);
      tail5.setTextureSize(128,256);
      tail5.mirror = true;
      setRotation(tail5, 0F, 0F, 0F);
      tail6 = new ModelRenderer(this, 103, 0);
      tail6.addBox(-2F, -41F, 9F, 4, 32, 8);
      tail6.setRotationPoint(0F, 15F, 112F);
      tail6.setTextureSize(128,256);
      tail6.mirror = true;
      setRotation(tail6, -0.6457718F, 0F, -0.0174533F);
      tail7 = new ModelRenderer(this, 84, 32);
      tail7.addBox(-1.5F, 6F, 15F, 3, 9, 5);
      tail7.setRotationPoint(0F, 15F, 112F);
      tail7.setTextureSize(128,256);
      tail7.mirror = true;
      setRotation(tail7, 0.3665191F, 0F, 0F);
      lffin = new ModelRenderer(this, 66, 85);
      lffin.addBox(0F, -1F, 0F, 23, 2, 5);
      lffin.setRotationPoint(6F, 22F, 20F);
      lffin.setTextureSize(128,256);
      lffin.mirror = true;
      setRotation(lffin, 0F, -0.3839724F, 0.4363323F);
      rffin = new ModelRenderer(this, 66, 77);
      rffin.addBox(-23F, -1F, 0F, 23, 2, 5);
      rffin.setRotationPoint(-6F, 22F, 20F);
      rffin.setTextureSize(128,256);
      rffin.mirror = true;
      setRotation(rffin, 0F, 0.3839724F, -0.4363323F);
      topfin1 = new ModelRenderer(this, 97, 93);
      topfin1.addBox(-1F, 0F, 0F, 2, 9, 9);
      topfin1.setRotationPoint(0F, 7F, 19F);
      topfin1.setTextureSize(128,256);
      topfin1.mirror = true;
      setRotation(topfin1, 0.6806784F, 0F, 0F);
      topfin2 = new ModelRenderer(this, 80, 0);
      topfin2.addBox(-1F, 0F, 7F, 2, 9, 9);
      topfin2.setRotationPoint(0F, 15F, 74F);
      topfin2.setTextureSize(128,256);
      topfin2.mirror = true;
      setRotation(topfin2, 0.6806784F, 0F, 0F);
 
  }
  
  // f = lifetime ticker. Doing Math.toRadians() on it provides a nice smooth wave cycle.
  // f1 = entity velocity
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
  {
	    
	  float newangle, newangle2;
	  
	    if(f1 > 0.01){
	    	newangle = 0.436f + (float) (Math.cos(Math.toRadians(f*7.0f)) * (float)Math.PI * 0.15F * f1);
	    	newangle2 = -0.436f + (float) (Math.cos(Math.toRadians(f*5.0f)) * (float)Math.PI * 0.15F * f1);
	    }else{
	    	newangle = 0.436f;
	    	newangle2 = -0.436f;
	    }
	    lffin.rotateAngleZ = newangle;
	    rffin.rotateAngleZ = newangle2;
	    
	    if(entity.getAttacking()){
	    	newangle = (float)Math.cos(Math.toRadians(f*8.2f)) * (float)Math.PI * 0.25f;
	    }else{
	    	newangle = 0;
	    }
	    jaw.rotateAngleX = Math.abs(newangle);
	    
	    float pi4 = 3.14159F/8.0F;
	    float tlen = 9;

	    //Makes a nice fairly impressive slither!
	    if(f1 > 1)f1 = 1;
	    this.tail1.rotateAngleY = (float) (Math.cos(Math.toRadians(f*6.6f)) * (float)Math.PI * 0.1f * f1);

	    tlen = 19;
	    this.tail2.rotationPointZ = this.tail1.rotationPointZ + (float)Math.cos(this.tail1.rotateAngleY)*tlen;
	    this.tail2.rotationPointX = this.tail1.rotationPointX + (float)Math.sin(this.tail1.rotateAngleY)*tlen;
	    this.tail2.rotateAngleY = (float) (Math.cos(Math.toRadians(f*6.6f) - pi4) * (float)Math.PI * 0.2f * f1);

	    tlen = 19;
	    this.tail3.rotationPointZ = this.tail2.rotationPointZ + (float)Math.cos(this.tail2.rotateAngleY)*tlen;
	    this.tail3.rotationPointX = this.tail2.rotationPointX + (float)Math.sin(this.tail2.rotateAngleY)*tlen;
	    this.tail3.rotateAngleY = (float) (Math.cos(Math.toRadians(f*6.6f) - 2.0f*pi4) * (float)Math.PI * 0.4f * f1);
	    topfin2.rotationPointZ = tail3.rotationPointZ;
	    topfin2.rotationPointX = tail3.rotationPointX;
	    topfin2.rotateAngleY = tail3.rotateAngleY;

	    tlen = 19;
	    this.tail4.rotationPointZ = this.tail3.rotationPointZ + (float)Math.cos(this.tail3.rotateAngleY)*tlen;
	    this.tail4.rotationPointX = this.tail3.rotationPointX + (float)Math.sin(this.tail3.rotateAngleY)*tlen;
	    this.tail4.rotateAngleY = (float) (Math.cos(Math.toRadians(f*6.6f) - 3.0f*pi4) * (float)Math.PI * 0.4f * f1);

	    tlen = 19;
	    this.tail5.rotationPointZ = this.tail4.rotationPointZ + (float)Math.cos(this.tail4.rotateAngleY)*tlen;
	    this.tail5.rotationPointX = this.tail4.rotationPointX + (float)Math.sin(this.tail4.rotateAngleY)*tlen;
	    this.tail5.rotateAngleY = (float) (Math.cos(Math.toRadians(f*6.6f) - 4.0f*pi4) * (float)Math.PI * 0.4f * f1);

	    tail6.rotationPointZ = tail7.rotationPointZ = tail5.rotationPointZ;
	    tail6.rotationPointX = tail7.rotationPointX = tail5.rotationPointX;
	    tail6.rotateAngleY = tail7.rotateAngleY = tail5.rotateAngleY;


		  if(f2 > 25)f2 = 25;
		  if(f2 < -25)f2 = -25;
		  if(f3 > 15)f3 = 15;
		  if(f3 < -15)f3 = -15;
		  
		  head1.rotateAngleX = neck.rotateAngleX = (float) Math.toRadians(f2);
		  head1.rotateAngleY = neck.rotateAngleY = (float) -Math.toRadians(f3);
	    
	    
	    body1.render(deathfactor);
	    neck.render(deathfactor);
	    head1.render(deathfactor);
	    body2.render(deathfactor);
	    jaw.render(deathfactor);
	    tail1.render(deathfactor);
	    tail2.render(deathfactor);
	    tail3.render(deathfactor);
	    tail4.render(deathfactor);
	    tail5.render(deathfactor);
	    tail6.render(deathfactor);
	    tail7.render(deathfactor);
	    lffin.render(deathfactor);
	    rffin.render(deathfactor);
	    topfin1.render(deathfactor);
	    topfin2.render(deathfactor);
	    

    
  }
  

}
