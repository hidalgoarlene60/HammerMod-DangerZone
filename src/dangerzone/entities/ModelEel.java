package dangerzone.entities;



import dangerzone.ModelBase;
import dangerzone.ModelRenderer;
import dangerzone.entities.Entity;

public class ModelEel extends ModelBase
{

	   ModelRenderer tail1;
	    ModelRenderer tail2;
	    ModelRenderer tail3;
	    ModelRenderer tail4;
	    ModelRenderer tail5;
	    ModelRenderer tail6;
	    ModelRenderer tail7;
	    ModelRenderer tail8;
	    ModelRenderer body;
	    ModelRenderer neck;
	    ModelRenderer head;
	    ModelRenderer jaw;
	    ModelRenderer leye;
	    ModelRenderer reye;

  
  public ModelEel()
  {
	    
	    
      tail1 = new ModelRenderer(this, 0, 32);
      tail1.addBox(-2F, 0F, 0F, 4, 2, 10);
      tail1.setRotationPoint(0F, 16F, 5F);
      tail1.setTextureSize(64, 128);
      tail1.mirror = true;
      setRotation(tail1, 0F, 0F, 0F);
      tail2 = new ModelRenderer(this, 0, 45);
      tail2.addBox(-2F, 0F, 0F, 4, 2, 10);
      tail2.setRotationPoint(0F, 16F, 14F);
      tail2.setTextureSize(64, 128);
      tail2.mirror = true;
      setRotation(tail2, 0F, 0F, 0F);
      tail3 = new ModelRenderer(this, 0, 58);
      tail3.addBox(-1.5F, 0F, 0F, 3, 2, 10);
      tail3.setRotationPoint(0F, 16F, 23F);
      tail3.setTextureSize(64, 128);
      tail3.mirror = true;
      setRotation(tail3, 0F, 0F, 0F);
      tail4 = new ModelRenderer(this, 0, 71);
      tail4.addBox(-1.5F, 0F, 0F, 3, 2, 10);
      tail4.setRotationPoint(0F, 16F, 32F);
      tail4.setTextureSize(64, 128);
      tail4.mirror = true;
      setRotation(tail4, 0F, 0F, 0F);
      tail5 = new ModelRenderer(this, 30, 36);
      tail5.addBox(-1F, 0F, 0F, 2, 2, 10);
      tail5.setRotationPoint(0F, 16F, 41F);
      tail5.setTextureSize(64, 128);
      tail5.mirror = true;
      setRotation(tail5, 0F, 0F, 0F);
      tail6 = new ModelRenderer(this, 30, 49);
      tail6.addBox(-1F, 0F, 0F, 2, 2, 10);
      tail6.setRotationPoint(0F, 16F, 50F);
      tail6.setTextureSize(64, 128);
      tail6.mirror = true;
      setRotation(tail6, 0F, 0F, 0F);
      tail7 = new ModelRenderer(this, 30, 62);
      tail7.addBox(-0.5F, 0F, 0F, 1, 2, 10);
      tail7.setRotationPoint(0F, 16F, 59F);
      tail7.setTextureSize(64, 128);
      tail7.mirror = true;
      setRotation(tail7, 0F, 0F, 0F);
      tail8 = new ModelRenderer(this, 0, 0);
      tail8.addBox(0F, -4F, 0F, 0, 8, 10);
      tail8.setRotationPoint(0F, 16F, 68F);
      tail8.setTextureSize(64, 128);
      tail8.mirror = true;
      setRotation(tail8, 0F, 0F, 0F);
      body = new ModelRenderer(this, 0, 22);
      body.addBox(-2.466667F, 0F, 0F, 5, 2, 6);
      body.setRotationPoint(0F, 16F, 0F);
      body.setTextureSize(64, 128);
      body.mirror = true;
      setRotation(body, 0F, 0F, 0F);
      neck = new ModelRenderer(this, 46, 27);
      neck.addBox(-1F, -1F, -4F, 2, 2, 5);
      neck.setRotationPoint(0F, 17F, 0F);
      neck.setTextureSize(64, 128);
      neck.mirror = true;
      setRotation(neck, -0.2443461F, 0F, 0F);
      head = new ModelRenderer(this, 40, 8);
      head.addBox(-1.5F, -2F, -7F, 3, 2, 7);
      head.setRotationPoint(0F, 17F, -3F);
      head.setTextureSize(64, 128);
      head.mirror = true;
      setRotation(head, 0F, 0F, 0F);
      jaw = new ModelRenderer(this, 44, 19);
      jaw.addBox(-1F, 0F, -6F, 2, 1, 6);
      jaw.setRotationPoint(0F, 17F, -3F);
      jaw.setTextureSize(64, 128);
      jaw.mirror = true;
      setRotation(jaw, 0.2094395F, 0F, 0F);
      leye = new ModelRenderer(this, 35, 5);
      leye.addBox(1F, -2F, -2F, 1, 1, 1);
      leye.setRotationPoint(0F, 17F, -3F);
      leye.setTextureSize(64, 128);
      leye.mirror = true;
      setRotation(leye, 0F, 0F, 0F);
      reye = new ModelRenderer(this, 30, 5);
      reye.addBox(-2F, -1F, -2F, 1, 1, 1);
      reye.setRotationPoint(0F, 16F, -3F);
      reye.setTextureSize(64, 128);
      reye.mirror = true;
      setRotation(reye, 0F, 0F, 0F);
 
  }
  
  // f = lifetime ticker. Doing Math.toRadians() on it provides a nice smooth wave cycle.
  // f1 = entity velocity
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
  {

	    float newangle = 0;
	    
	    if(entity.getAttacking()){
	    	newangle = (float)Math.cos(Math.toRadians(f*3.2f)) * (float)Math.PI * 0.15f;
	    }
	    jaw.rotateAngleX = Math.abs(newangle);
	    
	    float pi4 = 3.14159F/4.0F;
	    float tlen = 9;
	    float my = entity.motiony;
	    my = -my;
	    my /= 6;
	    //Makes a nice fairly impressive slither!
	    if(f1 > 1)f1 = 1;
	    this.tail1.rotateAngleY = (float) (Math.cos(Math.toRadians(f*12.6f)) * (float)Math.PI * 0.4f * f1);
	    tail1.rotateAngleX = my;
	    tlen = (float) (Math.cos(tail1.rotateAngleX) * 9);
	    this.tail2.rotationPointZ = this.tail1.rotationPointZ + (float)Math.cos(this.tail1.rotateAngleY)*tlen;
	    this.tail2.rotationPointX = this.tail1.rotationPointX + (float)Math.sin(this.tail1.rotateAngleY)*tlen;
	    this.tail2.rotateAngleY = (float) (Math.cos(Math.toRadians(f*12.6f) - pi4) * (float)Math.PI * 0.4f * f1);
	    tail2.rotateAngleX = tail1.rotateAngleX + my;
	    tail2.rotationPointY = (float) (tail1.rotationPointY - Math.sin(tail1.rotateAngleX)*9);
	    tlen = (float) (Math.cos(tail2.rotateAngleX) * 9);
	    this.tail3.rotationPointZ = this.tail2.rotationPointZ + (float)Math.cos(this.tail2.rotateAngleY)*tlen;
	    this.tail3.rotationPointX = this.tail2.rotationPointX + (float)Math.sin(this.tail2.rotateAngleY)*tlen;
	    this.tail3.rotateAngleY = (float) (Math.cos(Math.toRadians(f*12.6f) - 2.0f*pi4) * (float)Math.PI * 0.4f * f1);
	    tail3.rotateAngleX = tail2.rotateAngleX + my;
	    tail3.rotationPointY = (float) (tail2.rotationPointY - Math.sin(tail2.rotateAngleX)*9);
	    tlen = (float) (Math.cos(tail3.rotateAngleX) * 9);
	    this.tail4.rotationPointZ = this.tail3.rotationPointZ + (float)Math.cos(this.tail3.rotateAngleY)*tlen;
	    this.tail4.rotationPointX = this.tail3.rotationPointX + (float)Math.sin(this.tail3.rotateAngleY)*tlen;
	    this.tail4.rotateAngleY = (float) (Math.cos(Math.toRadians(f*12.6f) - 3.0f*pi4) * (float)Math.PI * 0.4f * f1);
	    tail4.rotateAngleX = tail3.rotateAngleX + my;
	    tail4.rotationPointY = (float) (tail3.rotationPointY - Math.sin(tail3.rotateAngleX)*9);
	    tlen = (float) (Math.cos(tail4.rotateAngleX) * 9);
	    this.tail5.rotationPointZ = this.tail4.rotationPointZ + (float)Math.cos(this.tail4.rotateAngleY)*tlen;
	    this.tail5.rotationPointX = this.tail4.rotationPointX + (float)Math.sin(this.tail4.rotateAngleY)*tlen;
	    this.tail5.rotateAngleY = (float) (Math.cos(Math.toRadians(f*12.6f) - 4.0f*pi4) * (float)Math.PI * 0.4f * f1);
	    tail5.rotateAngleX = tail4.rotateAngleX + my;
	    tail5.rotationPointY = (float) (tail4.rotationPointY - Math.sin(tail4.rotateAngleX)*9);
	    tlen = (float) (Math.cos(tail5.rotateAngleX) * 9);
	    this.tail6.rotationPointZ = this.tail5.rotationPointZ + (float)Math.cos(this.tail5.rotateAngleY)*tlen;
	    this.tail6.rotationPointX = this.tail5.rotationPointX + (float)Math.sin(this.tail5.rotateAngleY)*tlen;
	    this.tail6.rotateAngleY = (float) (Math.cos(Math.toRadians(f*12.6f) - 5.0f*pi4) * (float)Math.PI * 0.4f * f1);  
	    tail6.rotateAngleX = tail5.rotateAngleX + my;
	    tail6.rotationPointY = (float) (tail5.rotationPointY - Math.sin(tail5.rotateAngleX)*9);
	    tlen = (float) (Math.cos(tail6.rotateAngleX) * 9);
	    
		//Now we switch to accumulative angles from wave, so we get a whip action in the tail! :)
		newangle = (float) (Math.cos(Math.toRadians(f * 12.6f)  - 5.0F*pi4) * (float)Math.PI * 0.45f * f1);
		newangle /= 2;
		  	    	    
	    this.tail7.rotationPointZ = this.tail6.rotationPointZ + (float)Math.cos(this.tail6.rotateAngleY)*tlen;
	    this.tail7.rotationPointX = this.tail6.rotationPointX + (float)Math.sin(this.tail6.rotateAngleY)*tlen;
	    this.tail7.rotateAngleY = this.tail6.rotateAngleY + newangle;  
	    tail7.rotateAngleX = tail6.rotateAngleX + my;
	    tail7.rotationPointY = (float) (tail6.rotationPointY - Math.sin(tail6.rotateAngleX)*9);
	    tlen = (float) (Math.cos(tail7.rotateAngleX) * 9);
	    this.tail8.rotationPointZ = this.tail7.rotationPointZ + (float)Math.cos(this.tail7.rotateAngleY)*tlen;
	    this.tail8.rotationPointX = this.tail7.rotationPointX + (float)Math.sin(this.tail7.rotateAngleY)*tlen;
	    this.tail8.rotateAngleY = this.tail7.rotateAngleY + newangle; 
	    tail8.rotateAngleX = tail7.rotateAngleX + my;
	    tail8.rotationPointY = (float) (tail7.rotationPointY - Math.sin(tail7.rotateAngleX)*9);
	    
	    
	    
	    
	    tail1.render(deathfactor);
	    tail2.render(deathfactor);
	    tail3.render(deathfactor);
	    tail4.render(deathfactor);
	    tail5.render(deathfactor);
	    tail6.render(deathfactor);
	    tail7.render(deathfactor);
	    tail8.render(deathfactor);
	    body.render(deathfactor);
	    neck.render(deathfactor);
	    head.render(deathfactor);
	    jaw.render(deathfactor);
	    leye.render(deathfactor);
	    reye.render(deathfactor);
    
  }
  

}
