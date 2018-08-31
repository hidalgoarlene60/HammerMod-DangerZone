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
public class ModelAnteater extends ModelBase
{

  //fields
    ModelRenderer lfleg;
    ModelRenderer rfleg;
    ModelRenderer body;
    ModelRenderer tail;
    ModelRenderer lrleg;
    ModelRenderer rrleg;
    ModelRenderer head;
    ModelRenderer nose;
    ModelRenderer tongue;

  
  public ModelAnteater()
  {
      
      lfleg = new ModelRenderer(this, 27, 62);
      lfleg.addBox(-1F, -1F, -1F, 2, 9, 2);
      lfleg.setRotationPoint(4F, 16F, -8F);
      lfleg.setTextureSize(128,128);
      lfleg.mirror = true;
      setRotation(lfleg, 0f, 0f, 0f);
      rfleg = new ModelRenderer(this, 37, 62);
      rfleg.addBox(-1F, -1F, -1F, 2, 9, 2);
      rfleg.setRotationPoint(-4F, 16F, -8F);
      rfleg.setTextureSize(128,128);
      rfleg.mirror = true;
      setRotation(rfleg, 0F, 0F, 0F);
      body = new ModelRenderer(this, 53, 60);
      body.addBox(-3F, -6F, 0F, 6, 13, 25);
      body.setRotationPoint(0F, 12F, -11F);
      body.setTextureSize(128,128);
      body.mirror = true;
      setRotation(body, 0.1396263F, 0F, 0F);
      tail = new ModelRenderer(this, 40, 0);
      tail.addBox(-1F, -4F, 0F, 2, 17, 39);
      tail.setRotationPoint(0F, 6F, 12F);
      tail.setTextureSize(128,128);
      tail.mirror = true;
      setRotation(tail, 0F, 0F, 0F);
      lrleg = new ModelRenderer(this, 0, 18);
      lrleg.addBox(-1F, -1F, -1F, 3, 11, 4);
      lrleg.setRotationPoint(4F, 14F, 7F);
      lrleg.setTextureSize(128,128);
      lrleg.mirror = true;
      setRotation(lrleg, 0F, 0F, 0F);
      rrleg = new ModelRenderer(this, 0, 0);
      rrleg.addBox(-2F, -1F, -1F, 3, 11, 4);
      rrleg.setRotationPoint(-4F, 14F, 7F);
      rrleg.setTextureSize(128,128);
      rrleg.mirror = true;
      setRotation(rrleg, 0F, 0F, 0F);
      head = new ModelRenderer(this, 0, 81);
      head.addBox(-2F, -2F, -5F, 4, 6, 8);
      head.setRotationPoint(0F, 9F, -13F);
      head.setTextureSize(128,128);
      head.mirror = true;
      setRotation(head, 0.2617994F, 0F, 0F);
      nose = new ModelRenderer(this, 0, 62);
      nose.addBox(-1F, 0F, -14F, 2, 3, 10);
      nose.setRotationPoint(0F, 8F, -13F);
      nose.setTextureSize(128,128);
      nose.mirror = true;
      setRotation(nose, 0.2792527F, 0F, 0F);
      tongue = new ModelRenderer(this, 0, 40);
      tongue.addBox(-0.5F, 0F, -14F, 1, 1, 17);
      tongue.setRotationPoint(0F, 9F, -13F);
      tongue.setTextureSize(128,128);
      tongue.mirror = true;
      setRotation(tongue, 0.2792527F, 0F, 0F);
  }
  
  // f = lifetime ticker. Doing Math.toRadians() on it provides a nice smooth wave cycle.
  // f1 = entity velocity
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
  {
	  Anteater r = (Anteater)entity;
	    float newangle = 0;
	    //System.out.printf("floats: %f,  %f, %f, %f, %f, %f\n", f, f1, f2, f3, f4, f5);
	    if(f1 > 0.01){
	    	newangle = (float) (Math.cos(Math.toRadians(f*14.6f)) * (float)Math.PI * 0.30F * f1);
	    }else{
	    	newangle = 0.0F;
	    }
	    
	    this.rfleg.rotateAngleX = newangle;
	    this.lfleg.rotateAngleX = -newangle;
	    this.rrleg.rotateAngleX = -newangle;
	    this.lrleg.rotateAngleX = newangle;

	    if(r.getAttacking()){
	    	newangle = (float) (Math.sin(Math.toRadians(f*12.6f)) * 16);
		    newangle = Math.abs(newangle);
		    tail.rotateAngleY = (float) (Math.sin(Math.toRadians(f*13.6f)) * 0.35);
	    }else{
	    	newangle = 0;
	    	if(r.getStaying()){
	    		tail.rotateAngleY = 0;
	    	}else{
	    		tail.rotateAngleY = (float) (Math.sin(Math.toRadians(f*3.6f)) * 0.02);
	    	}
	    }
	    if(r.getOwnerName() != null){
	    	tail.rotateAngleX = -0.35f + (r.getHealth()/r.getMaxHealth()*0.70f);
	    }else{
	    	tail.rotateAngleX = 0;
	    }
	    tongue.offsetZ = -14f - newangle;
	    
	    
		  if(f2 > 25)f2 = 25;
		  if(f2 < -25)f2 = -25;
		  if(f3 > 35)f3 = 35;
		  if(f3 < -35)f3 = -35;
		  if(f4 > 20)f4 = 20;
		  if(f4 < -20)f4 = -20;
		  
		  head.rotateAngleX = nose.rotateAngleX = tongue.rotateAngleX = (float) Math.toRadians(f2) + 0.262f;
		  head.rotateAngleY = nose.rotateAngleY = tongue.rotateAngleY = (float) -Math.toRadians(f3);
		  head.rotateAngleZ = nose.rotateAngleZ = tongue.rotateAngleZ = (float) Math.toRadians(f4);
	    
	    lfleg.render(deathfactor);
	    rfleg.render(deathfactor);
	    body.render(deathfactor);
	    tail.render(deathfactor);
	    lrleg.render(deathfactor);
	    rrleg.render(deathfactor);
	    head.render(deathfactor);
	    nose.render(deathfactor);
	    tongue.render(deathfactor);
    
  }
  

}
