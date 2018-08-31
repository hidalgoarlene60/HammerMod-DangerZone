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

public class ModelMagLev extends ModelBase
{

    ModelRenderer base;
    ModelRenderer lwing;
    ModelRenderer rwing;
    ModelRenderer lrail;
    ModelRenderer rrail;
    ModelRenderer side1;
    ModelRenderer side2;
    ModelRenderer side3;
    ModelRenderer side4;

  
  public ModelMagLev()
  {
    
	    
	     base = new ModelRenderer(this, 0, 0);
	      base.addBox(-4F, -2F, -8F, 8, 1, 16);
	      base.setRotationPoint(0F, 21F, 0F);
	      base.setTextureSize(128, 64);
	      base.mirror = true;
	      setRotation(base, 0F, 0F, 0F);
	      lwing = new ModelRenderer(this, 0, 18);
	      lwing.addBox(2F, -4F, -7F, 5, 1, 14);
	      lwing.setRotationPoint(0F, 21F, 0F);
	      lwing.setTextureSize(128, 64);
	      lwing.mirror = true;
	      setRotation(lwing, 0F, 0F, 0.6632251F);
	      rwing = new ModelRenderer(this, 0, 18);
	      rwing.addBox(-7F, -4F, -7F, 5, 1, 14);
	      rwing.setRotationPoint(0F, 21F, 0F);
	      rwing.setTextureSize(128, 64);
	      rwing.mirror = true;
	      setRotation(rwing, 0F, 0F, -0.6632251F);
	      lrail = new ModelRenderer(this, 0, 35);
	      lrail.addBox(2F, -1F, -7.5F, 1, 2, 15);
	      lrail.setRotationPoint(0F, 21F, 0F);
	      lrail.setTextureSize(128, 64);
	      lrail.mirror = true;
	      setRotation(lrail, 0F, 0F, 0F);
	      rrail = new ModelRenderer(this, 0, 35);
	      rrail.addBox(-3F, -1F, -7.5F, 1, 2, 15);
	      rrail.setRotationPoint(0F, 21F, 0F);
	      rrail.setTextureSize(128, 64);
	      rrail.mirror = true;
	      setRotation(rrail, 0F, 0F, 0F);
	      side1 = new ModelRenderer(this, 35, 45);
	      side1.addBox(4F, -3F, -8F, 1, 1, 16);
	      side1.setRotationPoint(0F, 21F, 0F);
	      side1.setTextureSize(128, 64);
	      side1.mirror = true;
	      setRotation(side1, 0F, 0F, 0F);
	      side2 = new ModelRenderer(this, 35, 45);
	      side2.addBox(-5F, -3F, -8F, 1, 1, 16);
	      side2.setRotationPoint(0F, 21F, 0F);
	      side2.setTextureSize(128, 64);
	      side2.mirror = true;
	      setRotation(side2, 0F, 0F, 0F);
	      side3 = new ModelRenderer(this, 0, 60);
	      side3.addBox(-5F, -3F, -9F, 10, 1, 1);
	      side3.setRotationPoint(0F, 21F, 0F);
	      side3.setTextureSize(128, 64);
	      side3.mirror = true;
	      setRotation(side3, 0F, 0F, 0F);
	      side4 = new ModelRenderer(this, 0, 60);
	      side4.addBox(-5F, -3F, 8F, 10, 1, 1);
	      side4.setRotationPoint(0F, 21F, 0F);
	      side4.setTextureSize(128, 64);
	      side4.mirror = true;
	      setRotation(side4, 0F, 0F, 0F);
	      
	      //draw as one piece for better performance
	      base.connectPart(lwing);
	      base.connectPart(rwing);
	      base.connectPart(lrail);
	      base.connectPart(rrail);
	      base.connectPart(side1);
	      base.connectPart(side2);
	      base.connectPart(side3);
	      base.connectPart(side4);
	      base.finishConnect(); //finalize it.
	      
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
  {
	  if(!(entity instanceof EntityMagLev))return;

	    base.render(deathfactor);

	    
	    InventoryContainer ic = entity.getInventory(0);
	    if(ic != null){
	    	EntityMagLev eml = (EntityMagLev)entity;
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

  }
  


}