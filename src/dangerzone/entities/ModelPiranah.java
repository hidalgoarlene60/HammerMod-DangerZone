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

import dangerzone.ModelBase;
import dangerzone.ModelRenderer;

public class ModelPiranah extends ModelBase
{

  //fields
    ModelRenderer body;
    ModelRenderer topfin;
    ModelRenderer tailfin;
    ModelRenderer head;
    ModelRenderer lfin;
    ModelRenderer rfin;
    ModelRenderer head2;
    ModelRenderer topfin2;
  
  public ModelPiranah()
  {
	  
	    
      body = new ModelRenderer(this, 0, 0);
      body.addBox(0F, 0F, 0F, 2, 11, 13);
      body.setRotationPoint(-1F, 10F, -7F);
      body.setTextureSize(64, 32);
      body.mirror = true;
      setRotation(body, 0F, 0F, 0F);
      topfin = new ModelRenderer(this, 31, 0);
      topfin.addBox(-0.5F, -2F, 0F, 1, 3, 6);
      topfin.setRotationPoint(0F, 11F, -3F);
      topfin.setTextureSize(64, 32);
      topfin.mirror = true;
      setRotation(topfin, 0.2617994F, 0F, 0F);
      tailfin = new ModelRenderer(this, 31, 11);
      tailfin.addBox(-0.5F, -0.5F, 0F, 1, 7, 4);
      tailfin.setRotationPoint(0F, 13F, 6F);
      tailfin.setTextureSize(64, 32);
      tailfin.mirror = true;
      setRotation(tailfin, 0F, 0F, 0F);
      head = new ModelRenderer(this, 0, 25);
      head.addBox(-0.5F, -0.5F, 0F, 1, 3, 1);
      head.setRotationPoint(0F, 14F, -8F);
      head.setTextureSize(64, 32);
      head.mirror = true;
      setRotation(head, 0F, 0F, 0F);
      lfin = new ModelRenderer(this, 48, 20);
      lfin.addBox(0F, 0F, 0F, 3, 1, 3);
      lfin.setRotationPoint(1F, 20F, -5F);
      lfin.setTextureSize(64, 32);
      lfin.mirror = true;
      setRotation(lfin, 0F, 0F, 0.3490659F);
      rfin = new ModelRenderer(this, 48, 25);
      rfin.addBox(-3F, 0F, 0F, 3, 1, 3);
      rfin.setRotationPoint(-1F, 20F, -5F);
      rfin.setTextureSize(64, 32);
      rfin.mirror = true;
      setRotation(rfin, 0F, 0F, -0.3490659F);
      head2 = new ModelRenderer(this, 5, 25);
      head2.addBox(0F, 0F, 0F, 1, 3, 1);
      head2.setRotationPoint(-0.5F, 17F, -8F);
      head2.setTextureSize(64, 32);
      head2.mirror = true;
      setRotation(head2, 0F, 0F, 0F);
      topfin2 = new ModelRenderer(this, 31, 25);
      topfin2.addBox(-0.5F, 0F, 0F, 1, 2, 4);
      topfin2.setRotationPoint(0F, 20F, -1F);
      topfin2.setTextureSize(64, 32);
      topfin2.mirror = true;
      setRotation(topfin2, -0.1396263F, 0F, 0F);
    
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
  {
	  
	   // this.LArm.rotateAngleZ = (float) (-0.33f + Math.cos(Math.toRadians(f * 5.3F)) * (float)Math.PI * 0.05F);
	  float newangle, newangle2;
	  
	    if(f1 > 0.01){
	    	newangle = 0.349f + (float) (Math.cos(Math.toRadians(f*17.0f)) * (float)Math.PI * 0.45F * f1);
	    	newangle2 = -0.349f + (float) (Math.cos(Math.toRadians(f*15.0f)) * (float)Math.PI * 0.45F * f1);
	    }else{
	    	newangle = 0.349f;
	    	newangle2 = -0.349f;
	    }
	    lfin.rotateAngleZ = newangle;
	    rfin.rotateAngleZ = newangle2;
	    
	    if(f1 > 0.01){
	    	newangle = (float) (Math.cos(Math.toRadians(f*19.0f)) * (float)Math.PI * 0.55F * f1);
	    }else{
	    	newangle = (float) (Math.cos(Math.toRadians(f*2.0f)) * (float)Math.PI * 0.05F);
	    }   
	    tailfin.rotateAngleY = newangle;
	    
	    body.render(deathfactor);
	    topfin.render(deathfactor);
	    tailfin.render(deathfactor);
	    head.render(deathfactor);
	    lfin.render(deathfactor);
	    rfin.render(deathfactor);
	    head2.render(deathfactor);
	    topfin2.render(deathfactor);
    
  }
  


}
