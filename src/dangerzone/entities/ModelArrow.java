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

public class ModelArrow extends ModelBase
{

    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;

  
    public ModelArrow()
    {


    	Shape1 = new ModelRenderer(this, 0, 0);
    	Shape1.addBox(0F, 0F, 0F, 1, 1, 25);
    	Shape1.setRotationPoint(0F, 23F, 0F);
    	Shape1.setTextureSize(64, 64);
    	Shape1.mirror = true;
    	setRotation(Shape1, 0F, 0F, 0F);
    	Shape2 = new ModelRenderer(this, 0, 0);
    	Shape2.addBox(0F, -1F, 21F, 1, 1, 6);
    	Shape2.setRotationPoint(0F, 23F, 0F);
    	Shape2.setTextureSize(64, 64);
    	Shape2.mirror = true;
    	setRotation(Shape2, 0F, 0F, 0F);
    	Shape3 = new ModelRenderer(this, 0, 0);
    	Shape3.addBox(0F, 1F, 21F, 1, 1, 6);
    	Shape3.setRotationPoint(0F, 23F, 0F);
    	Shape3.setTextureSize(64, 64);
    	Shape3.mirror = true;
    	setRotation(Shape3, 0F, 0F, 0F);
    	Shape4 = new ModelRenderer(this, 0, 0);
    	Shape4.addBox(1F, 0F, 22F, 2, 1, 4);
    	Shape4.setRotationPoint(0F, 23F, 0F);
    	Shape4.setTextureSize(64, 64);
    	Shape4.mirror = true;
    	setRotation(Shape4, 0F, 0F, 0F);
    	Shape5 = new ModelRenderer(this, 0, 0);
    	Shape5.addBox(-2F, 0F, 22F, 2, 1, 4);
    	Shape5.setRotationPoint(0F, 23F, 0F);
    	Shape5.setTextureSize(64, 64);
    	Shape5.mirror = true;
    	setRotation(Shape5, 0F, 0F, 0F);
    }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
  {
	   Shape1.render(deathfactor);
	   Shape2.render(deathfactor);
	   Shape3.render(deathfactor);
	   Shape4.render(deathfactor);
	   Shape5.render(deathfactor);   
  }
  
}