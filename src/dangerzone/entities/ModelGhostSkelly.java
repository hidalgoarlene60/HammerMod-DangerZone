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
import org.lwjgl.opengl.GL11;

import dangerzone.ModelBase;
import dangerzone.ModelRenderer;


public class ModelGhostSkelly extends ModelBase
{

  //fields
    ModelRenderer body;
    ModelRenderer shirt;
    ModelRenderer head;
    ModelRenderer stem;
    ModelRenderer rarm;
    ModelRenderer larm;
    ModelRenderer rsleeve;
    ModelRenderer lsleeve;
    ModelRenderer lchains;
    ModelRenderer rchains;

  
  public ModelGhostSkelly()
  {
	  
      
      body = new ModelRenderer(0, 0);
      body.addCube(0F, 0F, 0F, 1, 21, 1);
      body.setRotationPoint(0F, -1F, 0F);
      body.setTextureSize(128, 64);
      body.setRotation(0F, 0F, 0F);
      shirt = new ModelRenderer( 42, 43);
      shirt.addCube(-2F, 0F, -2F, 5, 12, 5);
      shirt.setRotationPoint(0F, 0F, 0F);
      shirt.setTextureSize(128, 64);
      shirt.setRotation( 0F, 0F, 0F);
      head = new ModelRenderer(40, 29);
      head.addCube(-3F, 0F, -3F, 7, 5, 7);
      head.setRotationPoint(0F, -6F, 0F);
      head.setTextureSize(128, 64);
      head.setRotation(0F, 0F, 0F);
      stem = new ModelRenderer(49, 23);
      stem.addCube(0F, 0F, 0F, 1, 2, 1);
      stem.setRotationPoint(0F, -8F, 0F);
      stem.setTextureSize(128, 64);
      stem.setRotation( 0.1745329F, 0F, 0.1745329F);
      rarm = new ModelRenderer(26, 0);
      rarm.addCube(-14F, 0F, 0F, 15, 1, 1);
      rarm.setRotationPoint(0F, 0F, 0F);
      rarm.setTextureSize(128, 64);
      rarm.setRotation( 0F, 0F, 0F);
      larm = new ModelRenderer(63, 0);
      larm.addCube(0F, 0F, 0F, 15, 1, 1);
      larm.setRotationPoint(0F, 0F, 0F);
      larm.setTextureSize(128, 64);
      larm.setRotation(0F, 0F, 0F);
      rsleeve = new ModelRenderer(31, 7);
      rsleeve.addCube(-11F, 0F, -1F, 9, 8, 3);
      rsleeve.setRotationPoint(0F, 0F, 0F);
      rsleeve.setTextureSize(128, 64);
      rsleeve.setRotation(0F, 0F, 0F);
      lsleeve = new ModelRenderer(71, 7);
      lsleeve.addCube(3F, 0F, -1F, 9, 8, 3);
      lsleeve.setRotationPoint(0F, 0F, 0F);
      lsleeve.setTextureSize(128, 64);
      lsleeve.setRotation( 0F, 0F, 0F);
      lchains = new ModelRenderer( 98, 0);
      lchains.addCube(11F, -1F, 0F, 3, 16, 1);
      lchains.setRotationPoint(0F, 0F, 0F);
      lchains.setTextureSize(128, 64);
      lchains.setRotation(0F, 0F, 0F);
      rchains = new ModelRenderer(12, 0);
      rchains.addCube(-13F, -1F, 0F, 3, 10, 1);
      rchains.setRotationPoint(0F, 0F, 0F);
      rchains.setTextureSize(128, 64);
      rchains.setRotation(0F, 0F, 0F);
    
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
  {
	    
		  this.larm.rotateAngleZ = this.lsleeve.rotateAngleZ = this.lchains.rotateAngleZ = (float) (Math.cos(Math.toRadians(f * 4.3F)) * (float)Math.PI * 0.05F);
		  this.rarm.rotateAngleZ = this.rsleeve.rotateAngleZ = this.rchains.rotateAngleZ = (float) (Math.cos(Math.toRadians(f * 5.3F)) * (float)Math.PI * 0.05F);
		  this.larm.rotateAngleY = this.lsleeve.rotateAngleY = this.lchains.rotateAngleY = (float) (Math.cos(Math.toRadians(f * 6.3F)) * (float)Math.PI * 0.05F);
		  this.rarm.rotateAngleY = this.rsleeve.rotateAngleY = this.rchains.rotateAngleY = (float) (Math.cos(Math.toRadians(f * 3.3F)) * (float)Math.PI * 0.05F);
		  this.head.rotateAngleY = (float) Math.cos(Math.toRadians(f * 8.3F));
	    //
	    //Make it mostly transparent, and keep its color!
	    //

	    GL11.glEnable(GL11.GL_NORMALIZE);
	    GL11.glEnable(GL11.GL_BLEND);
	    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);  
	 	GL11.glColor4f(0.75F, 0.75F, 0.75F, 0.25F);  
	 	
		  body.render(deathfactor);
		  shirt.render(deathfactor);
		  head.render(deathfactor);
		  stem.render(deathfactor);
		  rarm.render(deathfactor);
		  larm.render(deathfactor);
		  rsleeve.render(deathfactor);
		  lsleeve.render(deathfactor);
		  lchains.render(deathfactor);
		  rchains.render(deathfactor);
	    
	    GL11.glDisable(GL11.GL_BLEND);
    
  }
  


}
