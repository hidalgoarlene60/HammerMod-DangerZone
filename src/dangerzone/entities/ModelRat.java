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
public class ModelRat extends ModelBase
{

  //fields
	   ModelRenderer body;
	    ModelRenderer tail1;
	    ModelRenderer tail2;
	    ModelRenderer lfleg;
	    ModelRenderer rfleg;
	    ModelRenderer lrleg;
	    ModelRenderer rrleg;
	    ModelRenderer body2;
	    ModelRenderer head;
	    ModelRenderer nose;
	    ModelRenderer lear;
	    ModelRenderer rear;

  
  public ModelRat()
  {
    
      body = new ModelRenderer(27, 0);
      body.addCube(-2F, -1F, 0F, 5, 3, 10);
      body.setRotationPoint(0F, 20F, -3F);
      body.setTextureSize(64, 64);
      body.setRotation(0F, 0F, 0F);
      tail1 = new ModelRenderer( 0, 30);
      tail1.addCube(-0.5F, -1F, 0F, 2, 2, 9);
      tail1.setRotationPoint(0F, 21F, 7F);
      tail1.setTextureSize(64, 64);
      tail1.setRotation(0F, 0F, 0F);
      tail2 = new ModelRenderer( 0, 43);
      tail2.addCube(0F, 0F, 0F, 1, 1, 12);
      tail2.setRotationPoint(0F, 21F, 16F);
      tail2.setTextureSize(64, 64);
      tail2.setRotation(0F, 0F, 0F);
      lfleg = new ModelRenderer( 0, 14);
      lfleg.addCube(0F, 0F, 0F, 1, 2, 1);
      lfleg.setRotationPoint(2F, 22F, -2F);
      lfleg.setTextureSize(64, 64);
      lfleg.setRotation( 0F, 0F, 0F);
      rfleg = new ModelRenderer( 10, 14);
      rfleg.addCube(0F, 0F, 0F, 1, 2, 1);
      rfleg.setRotationPoint(-2F, 22F, -2F);
      rfleg.setTextureSize(64, 64);
      rfleg.setRotation( 0F, 0F, 0F);
      lrleg = new ModelRenderer( 0, 18);
      lrleg.addCube(0F, 0F, 0F, 2, 4, 2);
      lrleg.setRotationPoint(2F, 20F, 4F);
      lrleg.setTextureSize(64, 64);
      lrleg.setRotation( 0F, 0F, 0F);
      rrleg = new ModelRenderer( 9, 18);
      rrleg.addCube(0F, 0F, 0F, 2, 4, 2);
      rrleg.setRotationPoint(-3F, 20F, 4F);
      rrleg.setTextureSize(64, 64);
      rrleg.setRotation( 0F, 0F, 0F);
      body2 = new ModelRenderer( 0, 0);
      body2.addCube(0F, 0F, 0F, 1, 1, 6);
      body2.setRotationPoint(0F, 18F, 0F);
      body2.setTextureSize(64, 64);
      body2.setRotation( 0F, 0F, 0F);
      head = new ModelRenderer( 27, 17);
      head.addCube(-1F, -2F, -3F, 3, 2, 4);
      head.setRotationPoint(0F, 22F, -4F);
      head.setTextureSize(64, 64);
      head.setRotation( 0F, 0F, 0F);
      nose = new ModelRenderer( 27, 25);
      nose.addCube(0F, -1F, -5F, 1, 1, 2);
      nose.setRotationPoint(0F, 22F, -4F);
      nose.setTextureSize(64, 64);
      nose.setRotation( 0F, 0F, 0F);
      lear = new ModelRenderer( 0, 9);
      lear.addCube(0F, 0F, 0F, 1, 1, 1);
      lear.setRotationPoint(1.5F, 19.5F, -4F);
      lear.setTextureSize(64, 64);
      lear.setRotation( 0F, 0F, 0F);
      rear = new ModelRenderer( 5, 9);
      rear.addCube(0F, 0F, 0F, 1, 1, 1);
      rear.setRotationPoint(-1.5F, 19.5F, -4F);
      rear.setTextureSize(64, 64);
      rear.setRotation( 0F, 0F, 0F);
  }
  
  // f = lifetime ticker. Doing Math.toRadians() on it provides a nice smooth wave cycle.
  // f1 = entity velocity
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
  {
	  Rat r = (Rat)entity;
	    float newangle = 0;
	    //System.out.printf("floats: %f,  %f, %f, %f, %f, %f\n", f, f1, f2, f3, f4, f5);
	    if(f1 > 0.1){
	    	newangle = (float) (Math.cos(Math.toRadians(f*27.6f)) * (float)Math.PI * 0.35F * f1);
	    }else{
	    	newangle = 0.0F;
	    }
	    
	    this.rfleg.rotateAngleX = newangle;
	    this.lfleg.rotateAngleX = -newangle;
	    this.rrleg.rotateAngleX = -newangle;
	    this.lrleg.rotateAngleX = newangle;

	    if(r.getAttacking()){
	    	newangle = (float) (Math.cos(Math.toRadians(f*17.6f)) * (float)Math.PI * 0.25F);
	    }else{
	    	newangle = (float) (Math.cos(Math.toRadians(f*4.6f)) * (float)Math.PI * 0.05F);
	    }
	    this.tail1.rotateAngleY = newangle * 0.5F;
	    this.tail2.rotateAngleY = newangle * 1.25F;
	    this.tail2.rotationPointZ = this.tail1.rotationPointZ + (float)Math.cos(this.tail1.rotateAngleY)*9;
	    this.tail2.rotationPointX = this.tail1.rotationPointX + (float)Math.sin(this.tail1.rotateAngleY)*9;

	    body.render(deathfactor);
	    tail1.render(deathfactor);
	    tail2.render(deathfactor);
	    lfleg.render(deathfactor);
	    rfleg.render(deathfactor);
	    lrleg.render(deathfactor);
	    rrleg.render(deathfactor);
	    body2.render(deathfactor);
	    head.render(deathfactor);
	    nose.render(deathfactor);
	    lear.render(deathfactor);
	    rear.render(deathfactor);
    
  }
  

}
