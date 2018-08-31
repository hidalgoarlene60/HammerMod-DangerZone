package dangerzone.entities;


import org.lwjgl.opengl.GL11;

import dangerzone.ModelBase;
import dangerzone.ModelRenderer;
import dangerzone.WorldRenderer;
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

public class ModelSnarler extends ModelBase
{

  //fields
    ModelRenderer head1;
    ModelRenderer head2;
    ModelRenderer ljaw1;
    ModelRenderer rjaw1;
    ModelRenderer ljaw2;
    ModelRenderer rjaw2;
    ModelRenderer ribs1;
    ModelRenderer ribs2;
    ModelRenderer ribs3;
    ModelRenderer ribs4;
    ModelRenderer ribs5;
    ModelRenderer ribs6;
    ModelRenderer ribs7;
    ModelRenderer eyes;
    ModelRenderer body;
    ModelRenderer lleg1;
    ModelRenderer lleg2;
    ModelRenderer lleg3;
    ModelRenderer rleg1;
    ModelRenderer rleg2;
    ModelRenderer rleg3;

  
    public ModelSnarler()
    {

        
          head1 = new ModelRenderer(this, 0, 28);
          head1.addBox(-1F, -2F, -4F, 4, 1, 8);
          head1.setRotationPoint(0F, 21F, -3F);
          head1.setTextureSize(64, 128);
          head1.mirror = true;
          setRotation(head1, 0F, 0F, 0.5235988F);
          head2 = new ModelRenderer(this, 26, 28);
          head2.addBox(-3F, -2F, -4F, 4, 1, 8);
          head2.setRotationPoint(0F, 21F, -3F);
          head2.setTextureSize(64, 128);
          head2.mirror = true;
          setRotation(head2, 0F, 0F, -0.5235988F);
          ljaw1 = new ModelRenderer(this, 0, 21);
          ljaw1.addBox(0F, 0F, -5F, 1, 1, 5);
          ljaw1.setRotationPoint(0F, 21F, -3F);
          ljaw1.setTextureSize(64, 128);
          ljaw1.mirror = true;
          setRotation(ljaw1, 0F, -0.7853982F, 0F);
          rjaw1 = new ModelRenderer(this, 13, 21);
          rjaw1.addBox(-1F, 0F, -5F, 1, 1, 5);
          rjaw1.setRotationPoint(0F, 21F, -3F);
          rjaw1.setTextureSize(64, 128);
          rjaw1.mirror = true;
          setRotation(rjaw1, 0F, 0.7853982F, 0F);
          ljaw2 = new ModelRenderer(this, 20, 11);
          ljaw2.addBox(4F, 0F, -10F, 1, 1, 8);
          ljaw2.setRotationPoint(0F, 21F, -3F);
          ljaw2.setTextureSize(64, 128);
          ljaw2.mirror = true;
          setRotation(ljaw2, 0F, 0.2792527F, 0F);
          rjaw2 = new ModelRenderer(this, 0, 11);
          rjaw2.addBox(-5F, 0F, -10F, 1, 1, 8);
          rjaw2.setRotationPoint(0F, 21F, -3F);
          rjaw2.setTextureSize(64, 128);
          rjaw2.mirror = true;
          setRotation(rjaw2, 0F, -0.2792527F, 0F);
          ribs1 = new ModelRenderer(this, 0, 39);
          ribs1.addBox(-4F, 0F, 0F, 8, 1, 8);
          ribs1.setRotationPoint(0F, 19F, 0F);
          ribs1.setTextureSize(64, 128);
          ribs1.mirror = true;
          setRotation(ribs1, -0.5235988F, 0F, 0F);
          ribs2 = new ModelRenderer(this, 0, 49);
          ribs2.addBox(-4F, 0F, 0F, 8, 1, 8);
          ribs2.setRotationPoint(0F, 19F, 3F);
          ribs2.setTextureSize(64, 128);
          ribs2.mirror = true;
          setRotation(ribs2, -0.5235988F, 0F, 0F);
          ribs3 = new ModelRenderer(this, 0, 59);
          ribs3.addBox(-4F, 0F, 0F, 8, 1, 8);
          ribs3.setRotationPoint(0F, 19F, 6F);
          ribs3.setTextureSize(64, 128);
          ribs3.mirror = true;
          setRotation(ribs3, -0.5235988F, 0F, 0F);
          ribs4 = new ModelRenderer(this, 34, 40);
          ribs4.addBox(-3F, 0F, 0F, 6, 1, 6);
          ribs4.setRotationPoint(0F, 20F, 11F);
          ribs4.setTextureSize(64, 128);
          ribs4.mirror = true;
          setRotation(ribs4, -0.5235988F, 0F, 0F);
          ribs5 = new ModelRenderer(this, 34, 50);
          ribs5.addBox(-3F, 0F, 0F, 6, 1, 6);
          ribs5.setRotationPoint(0F, 20F, 14F);
          ribs5.setTextureSize(64, 128);
          ribs5.mirror = true;
          setRotation(ribs5, -0.5235988F, 0F, 0F);
          ribs6 = new ModelRenderer(this, 34, 60);
          ribs6.addBox(-3F, 0F, 0F, 6, 1, 6);
          ribs6.setRotationPoint(0F, 20F, 17F);
          ribs6.setTextureSize(64, 128);
          ribs6.mirror = true;
          setRotation(ribs6, -0.5235988F, 0F, 0F);
          ribs7 = new ModelRenderer(this, 20, 71);
          ribs7.addBox(-2F, 0F, 0F, 4, 1, 4);
          ribs7.setRotationPoint(0F, 21F, 22F);
          ribs7.setTextureSize(64, 128);
          ribs7.mirror = true;
          setRotation(ribs7, -0.5235988F, 0F, 0F);
          eyes = new ModelRenderer(this, 27, 22);
          eyes.addBox(-3F, -1F, -2F, 6, 1, 2);
          eyes.setRotationPoint(0F, 21F, -3F);
          eyes.setTextureSize(64, 128);
          eyes.mirror = true;
          setRotation(eyes, 0F, 0F, 0F);
          body = new ModelRenderer(this, 0, 101);
          body.addBox(-1F, 0F, 0F, 2, 1, 24);
          body.setRotationPoint(0F, 22F, 5F);
          body.setTextureSize(64, 128);
          body.mirror = true;
          setRotation(body, 0F, 0F, 0F);
          lleg1 = new ModelRenderer(this, 5, 0);
          lleg1.addBox(0F, 0F, 0F, 1, 5, 1);
          lleg1.setRotationPoint(3F, 20F, 0F);
          lleg1.setTextureSize(64, 128);
          lleg1.mirror = true;
          setRotation(lleg1, 0F, 0F, -0.3490659F);
          lleg2 = new ModelRenderer(this, 10, 0);
          lleg2.addBox(0F, 0F, 0F, 1, 5, 1);
          lleg2.setRotationPoint(3F, 20F, 3F);
          lleg2.setTextureSize(64, 128);
          lleg2.mirror = true;
          setRotation(lleg2, 0F, 0F, -0.3490659F);
          lleg3 = new ModelRenderer(this, 15, 0);
          lleg3.addBox(0F, 0F, 0F, 1, 5, 1);
          lleg3.setRotationPoint(3F, 20F, 6F);
          lleg3.setTextureSize(64, 128);
          lleg3.mirror = true;
          setRotation(lleg3, 0F, 0F, -0.3490659F);
          rleg1 = new ModelRenderer(this, 20, 0);
          rleg1.addBox(-1F, 0F, 0F, 1, 5, 1);
          rleg1.setRotationPoint(-3F, 20F, 0F);
          rleg1.setTextureSize(64, 128);
          rleg1.mirror = true;
          setRotation(rleg1, 0F, 0F, 0.3490659F);
          rleg2 = new ModelRenderer(this, 25, 0);
          rleg2.addBox(-1F, 0F, 0F, 1, 5, 1);
          rleg2.setRotationPoint(-3F, 20F, 3F);
          rleg2.setTextureSize(64, 128);
          rleg2.mirror = true;
          setRotation(rleg2, 0F, 0F, 0.3490659F);
          rleg3 = new ModelRenderer(this, 0, 0);
          rleg3.addBox(-1F, 0F, 0F, 1, 5, 1);
          rleg3.setRotationPoint(-3F, 20F, 6F);
          rleg3.setTextureSize(64, 128);
          rleg3.mirror = true;
          setRotation(rleg3, 0F, 0F, 0.3490659F);
    }
  
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
    {  
    	if(!(entity instanceof Snarler))return;
    	Snarler sn = (Snarler)entity;
    	
	    float newangle = 0;
	    float amp = 0.02f;
	    //System.out.printf("floats: %f,  %f, %f, %f, %f, %f\n", f, f1, f2, f3, f4, f5);
	    if(f1 > 0.01){
	    	newangle = (float) (Math.cos(Math.toRadians(f*17.6f)) * 2.5f * f1);
	    	if(newangle > 1.3f)newangle = 1.3f;
	    	if(newangle < -1.3f)newangle = -1.3f;
	    }else{
	    	newangle = 0.0F;
	    }
	    
	    lleg1.rotateAngleX = newangle;
	    lleg2.rotateAngleX = newangle;
	    lleg3.rotateAngleX = newangle;
	    
	    rleg1.rotateAngleX = -newangle;
	    rleg2.rotateAngleX = -newangle;
	    rleg3.rotateAngleX = -newangle;
	    
	    ribs1.rotationPointZ = (float) (1.0f + Math.cos(Math.toRadians(f*4.1f)));
	    ribs2.rotationPointZ = (float) (4.0f + Math.cos(Math.toRadians(f*4.1f)+(Math.PI/4)));
	    ribs3.rotationPointZ = (float) (7.0f + Math.cos(Math.toRadians(f*4.1f)+(Math.PI/2)));	    
	    ribs4.rotationPointZ = (float) (12.0f + Math.cos(Math.toRadians(f*4.1f)+(Math.PI*3/4)));
	    ribs5.rotationPointZ = (float) (15.0f + Math.cos(Math.toRadians(f*4.1f)+(Math.PI)));
	    ribs6.rotationPointZ = (float) (18.0f + Math.cos(Math.toRadians(f*4.1f)+(Math.PI + (Math.PI/4))));	    
	    ribs7.rotationPointZ = (float) (23.0f + Math.cos(Math.toRadians(f*4.1f)+(Math.PI + (Math.PI/2))));
	    
	    if(sn.getAttacking()){
	    	newangle = (float) Math.cos(Math.toRadians(f*16.1f));
	    	amp = 0.25f;
	    }else{
	    	newangle = (float) Math.cos(Math.toRadians(f*3.1f));
	    }
	    ljaw1.rotateAngleY = -0.785f + newangle*amp;
	    ljaw2.rotateAngleY = 0.28f + newangle*amp;
	    
	    rjaw1.rotateAngleY = 0.785f - newangle*amp;
	    rjaw2.rotateAngleY = -0.28f - newangle*amp;


	    GL11.glEnable(GL11.GL_NORMALIZE);
	    GL11.glEnable(GL11.GL_BLEND);
	    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);  
	 	GL11.glColor4f(WorldRenderer.brightness_red, WorldRenderer.brightness_green, WorldRenderer.brightness_blue, sn.getVisibility());  
	 	
        head1.render(deathfactor);
        head2.render(deathfactor);
        ljaw1.render(deathfactor);
        rjaw1.render(deathfactor);
        ljaw2.render(deathfactor);
        rjaw2.render(deathfactor);       
        ribs1.render(deathfactor);
        ribs2.render(deathfactor);
        ribs3.render(deathfactor);
        ribs4.render(deathfactor);
        ribs5.render(deathfactor);
        ribs6.render(deathfactor);
        ribs7.render(deathfactor);
        eyes.render(deathfactor);
        body.render(deathfactor);
        lleg1.render(deathfactor);
        lleg2.render(deathfactor);
        lleg3.render(deathfactor);
        rleg1.render(deathfactor);
        rleg2.render(deathfactor);
        rleg3.render(deathfactor);

	    GL11.glDisable(GL11.GL_BLEND);
    }
  


}