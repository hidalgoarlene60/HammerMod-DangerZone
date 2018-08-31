package dangerzone.entities;



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

public class ModelFlag extends ModelBase
{

	  //fields
    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape4;
    ModelRenderer Shape5;
    ModelRenderer Shape6;
  
    public ModelFlag()
    {


    	Shape1 = new ModelRenderer(this, 18, 0);
    	Shape1.addBox(-2F, -20F, -2F, 4, 20, 4);
    	Shape1.setRotationPoint(0F, 24F, 0F);
    	Shape1.setTextureSize(128, 128);
    	Shape1.mirror = true;
    	setRotation(Shape1, 0F, 0F, 0F);
    	Shape2 = new ModelRenderer(this, 7, 0);
    	Shape2.addBox(-1F, -80F, -1F, 2, 60, 2);
    	Shape2.setRotationPoint(0F, 24F, 0F);
    	Shape2.setTextureSize(128, 128);
    	Shape2.mirror = true;
    	setRotation(Shape2, 0F, 0F, 0F);
    	Shape4 = new ModelRenderer(this, 0, 0);
    	Shape4.addBox(-0.5F, -160F, -0.5F, 1, 100, 1);
    	Shape4.setRotationPoint(0F, 24F, 0F);
    	Shape4.setTextureSize(128, 128);
    	Shape4.mirror = true;
    	setRotation(Shape4, 0F, 0F, 0F);
    	Shape5 = new ModelRenderer(this, 12, 67);
    	Shape5.addBox(-20.5F, -160F, 0F, 41, 20, 0);
    	Shape5.setRotationPoint(0F, 24F, 0F);
    	Shape5.setTextureSize(128, 128);
    	Shape5.mirror = true;
    	setRotation(Shape5, 0F, 0F, 0F);
    	Shape6 = new ModelRenderer(this, 12, 26);
    	Shape6.addBox(0F, -160F, -20.5F, 0, 20, 41);
    	Shape6.setRotationPoint(0F, 24F, 0F);
    	Shape6.setTextureSize(128, 128);
    	Shape6.mirror = true;
    	setRotation(Shape6, 0F, 0F, 0F);

    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
    {
    	float ypos = entity.getHealth()/entity.getMaxHealth();
    	ypos *= -140;
    	ypos -= 20;

    	Shape5.offsetY = ypos;
    	Shape6.offsetY = ypos;
    	
    	Shape1.render(deathfactor);
    	Shape2.render(deathfactor);
    	Shape4.render(deathfactor);
    	Shape5.render(deathfactor);
    	Shape6.render(deathfactor);

    }



}
