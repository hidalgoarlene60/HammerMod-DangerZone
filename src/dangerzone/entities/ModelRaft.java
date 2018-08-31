package dangerzone.entities;


import org.lwjgl.opengl.GL11;

import dangerzone.DangerZone;
import dangerzone.InventoryContainer;
import dangerzone.ModelBase;
import dangerzone.ModelRenderer;
import dangerzone.blocks.Blocks;
import dangerzone.items.Items;

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

public class ModelRaft extends ModelBase
{

    ModelRenderer log1;
    ModelRenderer log1end1;
    ModelRenderer log1end2;
    ModelRenderer log1end3;
    ModelRenderer log1end4;
    ModelRenderer log2end4;
    ModelRenderer log2end3;
    ModelRenderer log2;
    ModelRenderer log2end1;
    ModelRenderer log2end2;
    ModelRenderer log3;
    ModelRenderer log3end1;
    ModelRenderer log3end2;
    ModelRenderer log3end3;
    ModelRenderer log3end4;
    ModelRenderer log4;
    ModelRenderer log5;
    ModelRenderer log4end1;
    ModelRenderer log5end1;
    ModelRenderer log4end2;
    ModelRenderer log5end2;
    ModelRenderer log4end3;
    ModelRenderer log4end4;
    ModelRenderer log5end3;
    ModelRenderer log5end4;

  
  public ModelRaft()
  {
    
	    
      log1 = new ModelRenderer(this, 0, 0);
      log1.addBox(-2F, -2F, -24F, 4, 4, 48);
      log1.setRotationPoint(0F, 18f, 0F);
      log1.setTextureSize(128, 128);
      log1.mirror = true;
      setRotation(log1, 0F, 0F, 0F);
      log1end1 = new ModelRenderer(this, 0, 55);
      log1end1.addBox(-1.5F, -1.5F, -25F, 3, 3, 1);
      log1end1.setRotationPoint(0F, 18f, 0F);
      log1end1.setTextureSize(128, 128);
      log1end1.mirror = true;
      setRotation(log1end1, 0F, 0F, 0F);
      log1end2 = new ModelRenderer(this, 0, 61);
      log1end2.addBox(-1F, -1F, -26F, 2, 2, 1);
      log1end2.setRotationPoint(0F, 18f, 0F);
      log1end2.setTextureSize(128, 128);
      log1end2.mirror = true;
      setRotation(log1end2, 0F, 0F, 0F);
      log1end3 = new ModelRenderer(this, 0, 67);
      log1end3.addBox(-1.5F, -1.5F, 24F, 3, 3, 1);
      log1end3.setRotationPoint(0F, 18f, 0F);
      log1end3.setTextureSize(128, 128);
      log1end3.mirror = true;
      setRotation(log1end3, 0F, 0F, 0F);
      log1end4 = new ModelRenderer(this, 0, 74);
      log1end4.addBox(-1F, -1F, 25F, 2, 2, 1);
      log1end4.setRotationPoint(0F, 18f, 0F);
      log1end4.setTextureSize(128, 128);
      log1end4.mirror = true;
      setRotation(log1end4, 0F, 0F, 0F);
      log2end4 = new ModelRenderer(this, 0, 0);
      log2end4.addBox(-5F, -1F, 26F, 2, 2, 1);
      log2end4.setRotationPoint(0F, 18f, 0F);
      log2end4.setTextureSize(128, 128);
      log2end4.mirror = true;
      setRotation(log2end4, 0F, 0F, 0F);
      log2end3 = new ModelRenderer(this, 0, 0);
      log2end3.addBox(-5.5F, -1.5F, 24F, 3, 3, 1);
      log2end3.setRotationPoint(0F, 18f, 1F);
      log2end3.setTextureSize(128, 128);
      log2end3.mirror = true;
      setRotation(log2end3, 0F, 0F, 0F);
      log2 = new ModelRenderer(this, 0, 0);
      log2.addBox(-6F, -2.05F, 1F, 4, 4, 48);
      log2.setRotationPoint(0F, 18f, -24F);
      log2.setTextureSize(128, 128);
      log2.mirror = true;
      setRotation(log2, 0F, 0F, 0F);
      log2end1 = new ModelRenderer(this, 0, 0);
      log2end1.addBox(-5.5F, -1.5F, -24F, 3, 3, 1);
      log2end1.setRotationPoint(0F, 18f, 0F);
      log2end1.setTextureSize(128, 128);
      log2end1.mirror = true;
      setRotation(log2end1, 0F, 0F, 0F);
      log2end2 = new ModelRenderer(this, 0, 0);
      log2end2.addBox(-5F, -1F, -25F, 2, 2, 1);
      log2end2.setRotationPoint(0F, 18f, 0F);
      log2end2.setTextureSize(128, 128);
      log2end2.mirror = true;
      setRotation(log2end2, 0F, 0F, 0F);
      log3 = new ModelRenderer(this, 0, 0);
      log3.addBox(2F, -2.1F, -23F, 4, 4, 48);
      log3.setRotationPoint(0F, 18f, 0F);
      log3.setTextureSize(128, 128);
      log3.mirror = true;
      setRotation(log3, 0F, 0F, 0F);
      log3end1 = new ModelRenderer(this, 0, 0);
      log3end1.addBox(2.5F, -1.5F, -24F, 3, 3, 1);
      log3end1.setRotationPoint(0F, 18f, 0F);
      log3end1.setTextureSize(128, 128);
      log3end1.mirror = true;
      setRotation(log3end1, 0F, 0F, 0F);
      log3end2 = new ModelRenderer(this, 0, 0);
      log3end2.addBox(3F, -1F, -25F, 2, 2, 1);
      log3end2.setRotationPoint(0F, 18f, 0F);
      log3end2.setTextureSize(128, 128);
      log3end2.mirror = true;
      setRotation(log3end2, 0F, 0F, 0F);
      log3end3 = new ModelRenderer(this, 0, 0);
      log3end3.addBox(2.5F, -1.5F, 25F, 3, 3, 1);
      log3end3.setRotationPoint(0F, 18f, 0F);
      log3end3.setTextureSize(128, 128);
      log3end3.mirror = true;
      setRotation(log3end3, 0F, 0F, 0F);
      log3end4 = new ModelRenderer(this, 0, 0);
      log3end4.addBox(3F, -1F, 26F, 2, 2, 1);
      log3end4.setRotationPoint(0F, 18f, 0F);
      log3end4.setTextureSize(128, 128);
      log3end4.mirror = true;
      setRotation(log3end4, 0F, 0F, 0F);
      log4 = new ModelRenderer(this, 0, 0);
      log4.addBox(-10F, -2F, -24F, 4, 4, 48);
      log4.setRotationPoint(0F, 18f, 0F);
      log4.setTextureSize(128, 128);
      log4.mirror = true;
      setRotation(log4, 0F, 0F, 0F);
      log5 = new ModelRenderer(this, 0, 0);
      log5.addBox(6F, -2F, -24F, 4, 4, 48);
      log5.setRotationPoint(0F, 18f, 0F);
      log5.setTextureSize(128, 128);
      log5.mirror = true;
      setRotation(log5, 0F, 0F, 0F);
      log4end1 = new ModelRenderer(this, 0, 0);
      log4end1.addBox(-9.5F, -1.5F, -25F, 3, 3, 1);
      log4end1.setRotationPoint(0F, 18f, 0F);
      log4end1.setTextureSize(128, 128);
      log4end1.mirror = true;
      setRotation(log4end1, 0F, 0F, 0F);
      log5end1 = new ModelRenderer(this, 0, 0);
      log5end1.addBox(6.5F, -1.5F, -25F, 3, 3, 1);
      log5end1.setRotationPoint(0F, 18f, 0F);
      log5end1.setTextureSize(128, 128);
      log5end1.mirror = true;
      setRotation(log5end1, 0F, 0F, 0F);
      log4end2 = new ModelRenderer(this, 0, 0);
      log4end2.addBox(-9F, -1F, -26F, 2, 2, 1);
      log4end2.setRotationPoint(0F, 18f, 0F);
      log4end2.setTextureSize(128, 128);
      log4end2.mirror = true;
      setRotation(log4end2, 0F, 0F, 0F);
      log5end2 = new ModelRenderer(this, 0, 0);
      log5end2.addBox(7F, -1F, -26F, 2, 2, 1);
      log5end2.setRotationPoint(0F, 18f, 0F);
      log5end2.setTextureSize(128, 128);
      log5end2.mirror = true;
      setRotation(log5end2, 0F, 0F, 0F);
      log4end3 = new ModelRenderer(this, 0, 0);
      log4end3.addBox(-9.5F, -1.5F, 24F, 3, 3, 1);
      log4end3.setRotationPoint(0F, 18f, 0F);
      log4end3.setTextureSize(128, 128);
      log4end3.mirror = true;
      setRotation(log4end3, 0F, 0F, 0F);
      log4end4 = new ModelRenderer(this, 0, 0);
      log4end4.addBox(-9F, -1F, 25F, 2, 2, 1);
      log4end4.setRotationPoint(0F, 18f, 0F);
      log4end4.setTextureSize(128, 128);
      log4end4.mirror = true;
      setRotation(log4end4, 0F, 0F, 0F);
      log5end3 = new ModelRenderer(this, 0, 0);
      log5end3.addBox(6.5F, -1.5F, 24F, 3, 3, 1);
      log5end3.setRotationPoint(0F, 18f, 0F);
      log5end3.setTextureSize(128, 128);
      log5end3.mirror = true;
      setRotation(log5end3, 0F, 0F, 0F);
      log5end4 = new ModelRenderer(this, 0, 0);
      log5end4.addBox(7F, -1F, 25F, 2, 2, 1);
      log5end4.setRotationPoint(0F, 18f, 0F);
      log5end4.setTextureSize(128, 128);
      log5end4.mirror = true;
      setRotation(log5end4, 0F, 0F, 0F);
	      
	      //draw as one piece for better performance
	      log1.connectPart(log1end1);
	      log1.connectPart(log1end2);
	      log1.connectPart(log1end3);
	      log1.connectPart(log1end4);
	      log1.finishConnect(); //finalize it.
	      log2.connectPart(log2end1);
	      log2.connectPart(log2end2);
	      log2.connectPart(log2end3);
	      log2.connectPart(log2end4);
	      log2.finishConnect(); //finalize it.
	      log3.connectPart(log3end1);
	      log3.connectPart(log3end2);
	      log3.connectPart(log3end3);
	      log3.connectPart(log3end4);
	      log3.finishConnect(); //finalize it.
	      log4.connectPart(log4end1);
	      log4.connectPart(log4end2);
	      log4.connectPart(log4end3);
	      log4.connectPart(log4end4);
	      log4.finishConnect(); //finalize it.
	      log5.connectPart(log5end1);
	      log5.connectPart(log5end2);
	      log5.connectPart(log5end3);
	      log5.connectPart(log5end4);
	      log5.finishConnect(); //finalize it.
	      
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
  {
	  if(!(entity instanceof EntityRaft))return;
	  EntityRaft eml = (EntityRaft)entity;
	  
	  float displayoff = eml.getDisplayOffset();

	  if(displayoff != 0){
		  GL11.glPushMatrix();
		  GL11.glTranslatef(0, displayoff, 0);
	  }
	  log1.render(deathfactor);
	  log2.render(deathfactor);
	  log3.render(deathfactor);
	  log4.render(deathfactor);
	  log5.render(deathfactor);


	  InventoryContainer ic = entity.getInventory(0);
	  if(ic != null){

		  int bid, iid;

		  bid = ic.bid;
		  iid = ic.iid;

		  float spinz = eml.getSpinz();

		  GL11.glPushMatrix();
		  GL11.glTranslatef(0, 12, 0);

		  if(bid != 0){
			  GL11.glRotatef(spinz, 0, 1, 0); //rotate on Y
			  GL11.glScalef(0.333333f, 0.333333f, 0.333333f);
			  if(Blocks.hasOwnRenderer(bid)){
				  Blocks.renderMe(DangerZone.wr, entity.world, entity.dimension, (int)entity.posx, (int)entity.posy, (int)entity.posz, bid, 0, 0xff, false);
			  }else{
				  DangerZone.wr.drawTexturedCube(0xff, Blocks.isSolidForRender(bid), bid, 0, false);
			  }
			  GL11.glScalef(3, 3, 3);
		  }else{
			  if(iid != 0){
				  //Render Item!
				  GL11.glRotatef(180, 1, 0, 0);
				  GL11.glRotatef(spinz, 0, 1, 0); //rotate on Y
				  GL11.glScalef(0.5f, 0.5f, 0.5f);
				  DangerZone.wr.loadtexture(Items.getTexture(iid));				
				  Items.renderMe(DangerZone.wr, entity.world, entity.dimension, (int)entity.posx, (int)entity.posy, (int)entity.posz, iid);	
				  GL11.glScalef(2, 2, 2);
			  }
		  }

		  GL11.glTranslatef(0, -12, 0);
		  GL11.glPopMatrix();

	  }
	  
	  if(displayoff != 0){
		  GL11.glTranslatef(0, -displayoff, 0);
		  GL11.glPopMatrix();
	  }

  }
}
  


