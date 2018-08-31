package dangerzone.entities;


import org.lwjgl.opengl.GL11;

import dangerzone.ModelBase;
import dangerzone.ModelRenderer;
import dangerzone.entities.Entity;

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

public class ModelFrog extends ModelBase
{

  //fields
	   ModelRenderer body;
	    ModelRenderer jaw;
	    ModelRenderer lfleg;
	    ModelRenderer rfleg;
	    ModelRenderer lleg1;
	    ModelRenderer rleg1;
	    ModelRenderer lleg2;
	    ModelRenderer rleg2;
	    ModelRenderer leye;
	    ModelRenderer reye;

  
    public ModelFrog()
    {


        body = new ModelRenderer(this, 41, 0);
        body.addBox(-4F, -10F, 0F, 8, 11, 2);
        body.setRotationPoint(0F, 24F, 2F);
        body.setTextureSize(64, 64);
        body.mirror = true;
        setRotation(body, 0.7330383F, 0F, 0F);
        jaw = new ModelRenderer(this, 42, 15);
        jaw.addBox(-4F, -8F, 0F, 8, 8, 1);
        jaw.setRotationPoint(0F, 24F, 2F);
        jaw.setTextureSize(64, 64);
        jaw.mirror = true;
        setRotation(jaw, 1.22173F, 0F, 0F);
        lfleg = new ModelRenderer(this, 14, 0);
        lfleg.addBox(0F, 0F, 0F, 1, 5, 1);
        lfleg.setRotationPoint(3F, 20F, 0F);
        lfleg.setTextureSize(64, 64);
        lfleg.mirror = true;
        setRotation(lfleg, -0.5235988F, 0F, -0.4712389F);
        rfleg = new ModelRenderer(this, 20, 0);
        rfleg.addBox(-1F, 0F, 0F, 1, 5, 1);
        rfleg.setRotationPoint(-3F, 20F, 0F);
        rfleg.setTextureSize(64, 64);
        rfleg.mirror = true;
        setRotation(rfleg, -0.5235988F, 0F, 0.4712389F);
        lleg1 = new ModelRenderer(this, 10, 8);
        lleg1.addBox(0F, -9F, -1F, 1, 9, 2);
        lleg1.setRotationPoint(3F, 24F, 3F);
        lleg1.setTextureSize(64, 64);
        lleg1.mirror = true;
        setRotation(lleg1, 0F, 0F, 0.2268928F);
        rleg1 = new ModelRenderer(this, 18, 8);
        rleg1.addBox(-1F, -9F, -1F, 1, 9, 2);
        rleg1.setRotationPoint(-3F, 24F, 3F);
        rleg1.setTextureSize(64, 64);
        rleg1.mirror = true;
        setRotation(rleg1, 0F, 0F, -0.2268928F);
        lleg2 = new ModelRenderer(this, 11, 20);
        lleg2.addBox(0F, 0F, 0F, 1, 10, 1);
        lleg2.setRotationPoint(5F, 15F, 3F);
        lleg2.setTextureSize(64, 64);
        lleg2.mirror = true;
        setRotation(lleg2, 0F, 0F, -0.3839724F);
        rleg2 = new ModelRenderer(this, 19, 20);
        rleg2.addBox(-1F, 0F, 0F, 1, 10, 1);
        rleg2.setRotationPoint(-5F, 15F, 3F);
        rleg2.setTextureSize(64, 64);
        rleg2.mirror = true;
        setRotation(rleg2, 0F, 0F, 0.3839724F);
        leye = new ModelRenderer(this, 0, 8);
        leye.addBox(0F, 0F, 0F, 1, 2, 1);
        leye.setRotationPoint(2F, 17F, -2F);
        leye.setTextureSize(64, 64);
        leye.mirror = true;
        setRotation(leye, 0.7330383F, 0F, 0F);
        reye = new ModelRenderer(this, 0, 4);
        reye.addBox(0F, 0F, 0F, 1, 2, 1);
        reye.setRotationPoint(-3F, 17F, -2F);
        reye.setTextureSize(64, 64);
        reye.mirror = true;
        setRotation(reye, 0.7330383F, 0F, 0F);
    }
  
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
    {
    	float newangle = 0;
    	if(f1 > 0.01){
    		newangle = (float) (Math.cos(Math.toRadians(f*17.6f)) * (float)Math.PI * 0.55F * f1);
    	}else{
    		newangle = 0.0F;
    	}	    

    	lfleg.rotateAngleY = newangle;
    	rfleg.rotateAngleY = -newangle;
    	lleg2.rotateAngleY = -newangle/2;
    	rleg2.rotateAngleY = newangle/2;
    	if(entity.getSinging()){
    		newangle = (float) (Math.cos(Math.toRadians(f*5.6f)) * (float)Math.PI * 0.15F);
    	}else{
    		newangle = 0;
    	}
    	jaw.rotateAngleX = newangle + 1.22f;

    	if(entity.motiony > 0.1f || entity.motiony < -0.1f){
    		lleg1.rotateAngleZ = 2.44f;
    		rleg1.rotateAngleZ = -2.44f;
    	}else{
    		lleg1.rotateAngleZ = 0.227f;
    		rleg1.rotateAngleZ = -0.227f;
    	}
    	lleg2.rotationPointY = lleg1.rotationPointY - (float)Math.cos(lleg1.rotateAngleZ)*9;
    	lleg2.rotationPointX = lleg1.rotationPointX + (float)Math.sin(lleg1.rotateAngleZ)*9;  

    	rleg2.rotationPointY = rleg1.rotationPointY - (float)Math.cos(rleg1.rotateAngleZ)*9;
    	rleg2.rotationPointX = rleg1.rotationPointX + (float)Math.sin(rleg1.rotateAngleZ)*9;  

    	body.render(deathfactor);
    	jaw.render(deathfactor);
    	lfleg.render(deathfactor);
    	rfleg.render(deathfactor);
    	lleg1.render(deathfactor);
    	rleg1.render(deathfactor);
    	lleg2.render(deathfactor);
    	rleg2.render(deathfactor);
    	leye.render(deathfactor);
    	reye.render(deathfactor);

    }
  
	  public void doScale(Entity ent){
		  GL11.glScalef(0.35f, 0.35f, 0.35f);
		  if(ent.isBaby()){
			  GL11.glScalef(0.25f, 0.25f, 0.25f);
		  }
	  }


}
