package dangerzone.entities;


import org.lwjgl.opengl.GL11;

import dangerzone.DangerZone;
import dangerzone.InventoryContainer;
import dangerzone.ModelBase;
import dangerzone.ModelRenderer;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entity;
import dangerzone.items.Item;
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

public class ModelMartian extends ModelBase
{

    ModelRenderer rl1;
    ModelRenderer rl2;
    ModelRenderer rl3;
    ModelRenderer rl4;
    ModelRenderer ll1;
    ModelRenderer ll2;
    ModelRenderer ll3;
    ModelRenderer ll4;
    ModelRenderer body;
    ModelRenderer neck;
    ModelRenderer brain;
    ModelRenderer helmet;
    ModelRenderer la1;
    ModelRenderer la2;
    ModelRenderer la4;
    ModelRenderer la3;
    ModelRenderer la5;
    ModelRenderer ra1;
    ModelRenderer ra2;
    ModelRenderer ra3;
    ModelRenderer ra4;
    ModelRenderer ra5;
    ModelRenderer bp1;
    ModelRenderer bp3;
    ModelRenderer bp2;;

  
    public ModelMartian()
    {

        rl1 = new ModelRenderer(this, 0, 23);
        rl1.addBox(-6F, 14F, -5F, 5, 2, 8);
        rl1.setRotationPoint(0F, 8F, 0F);
        rl1.setTextureSize(64, 128);
        rl1.mirror = true;
        setRotation(rl1, 0F, 0F, 0F);
        rl2 = new ModelRenderer(this, 0, 34);
        rl2.addBox(-5F, 12F, -3F, 4, 2, 5);
        rl2.setRotationPoint(0F, 8F, 0F);
        rl2.setTextureSize(64, 128);
        rl2.mirror = true;
        setRotation(rl2, 0F, 0F, 0F);
        rl3 = new ModelRenderer(this, 0, 42);
        rl3.addBox(-4F, 8F, 0F, 2, 4, 2);
        rl3.setRotationPoint(0F, 8F, 0F);
        rl3.setTextureSize(64, 128);
        rl3.mirror = true;
        setRotation(rl3, 0F, 0F, 0F);
        rl4 = new ModelRenderer(this, 0, 49);
        rl4.addBox(-1F, 0F, 0F, 1, 9, 1);
        rl4.setRotationPoint(0F, 8F, 0F);
        rl4.setTextureSize(64, 128);
        rl4.mirror = true;
        setRotation(rl4, 0F, 0F, 0.2792527F);
        ll1 = new ModelRenderer(this, 28, 23);
        ll1.addBox(1F, 14F, -5F, 5, 2, 8);
        ll1.setRotationPoint(0F, 8F, 0F);
        ll1.setTextureSize(64, 128);
        ll1.mirror = true;
        setRotation(ll1, 0F, 0F, 0F);
        ll2 = new ModelRenderer(this, 20, 34);
        ll2.addBox(1F, 12F, -3F, 4, 2, 5);
        ll2.setRotationPoint(0F, 8F, 0F);
        ll2.setTextureSize(64, 128);
        ll2.mirror = true;
        setRotation(ll2, 0F, 0F, 0F);
        ll3 = new ModelRenderer(this, 10, 42);
        ll3.addBox(2F, 8F, 0F, 2, 4, 2);
        ll3.setRotationPoint(0F, 8F, 0F);
        ll3.setTextureSize(64, 128);
        ll3.mirror = true;
        setRotation(ll3, 0F, 0F, 0F);
        ll4 = new ModelRenderer(this, 5, 49);
        ll4.addBox(0F, 0F, 0F, 1, 9, 1);
        ll4.setRotationPoint(0F, 8F, 0F);
        ll4.setTextureSize(64, 128);
        ll4.mirror = true;
        setRotation(ll4, 0F, 0F, -0.2792527F);
        body = new ModelRenderer(this, 0, 62);
        body.addBox(0F, 0F, 0F, 4, 5, 4);
        body.setRotationPoint(-2F, 3F, -2F);
        body.setTextureSize(64, 128);
        body.mirror = true;
        setRotation(body, 0F, 0F, 0F);
        neck = new ModelRenderer(this, 0, 72);
        neck.addBox(-0.5F, 0F, 0F, 1, 4, 1);
        neck.setRotationPoint(0F, 0F, 0F);
        neck.setTextureSize(64, 128);
        neck.mirror = true;
        setRotation(neck, 0.1570796F, 0F, 0F);
        brain = new ModelRenderer(this, 0, 84);
        brain.addBox(-5F, -2F, 0F, 10, 3, 13);
        brain.setRotationPoint(0F, 0F, 0F);
        brain.setTextureSize(64, 128);
        brain.mirror = true;
        setRotation(brain, 0.6981317F, 0F, 0F);
        helmet = new ModelRenderer(this, 0, 0);
        helmet.addBox(-6F, -3F, -1F, 12, 5, 15);
        helmet.setRotationPoint(0F, 0F, 0F);
        helmet.setTextureSize(64, 128);
        helmet.mirror = true;
        setRotation(helmet, 0.6981317F, 0F, 0F);
        la1 = new ModelRenderer(this, 36, 44);
        la1.addBox(-1F, 0F, 0F, 1, 7, 1);
        la1.setRotationPoint(2F, 3F, 0F);
        la1.setTextureSize(64, 128);
        la1.mirror = true;
        setRotation(la1, 0F, 0F, -0.4886922F);
        la2 = new ModelRenderer(this, 35, 53);
        la2.addBox(-1F, 7F, -1F, 1, 2, 3);
        la2.setRotationPoint(2F, 3F, 0F);
        la2.setTextureSize(64, 128);
        la2.mirror = true;
        setRotation(la2, 0F, 0F, -0.4886922F);
        la4 = new ModelRenderer(this, 39, 59);
        la4.addBox(-1F, 9F, 0F, 1, 3, 1);
        la4.setRotationPoint(2F, 3F, 0F);
        la4.setTextureSize(64, 128);
        la4.mirror = true;
        setRotation(la4, 0F, 0F, -0.4886922F);
        la3 = new ModelRenderer(this, 44, 59);
        la3.addBox(-1F, 9F, 0F, 1, 2, 1);
        la3.setRotationPoint(2F, 3F, 0F);
        la3.setTextureSize(64, 128);
        la3.mirror = true;
        setRotation(la3, -0.1396263F, 0F, -0.4886922F);
        la5 = new ModelRenderer(this, 34, 59);
        la5.addBox(-1F, 9F, 0F, 1, 3, 1);
        la5.setRotationPoint(2F, 3F, 0F);
        la5.setTextureSize(64, 128);
        la5.mirror = true;
        setRotation(la5, 0.1396263F, 0F, -0.4886922F);
        ra1 = new ModelRenderer(this, 30, 44);
        ra1.addBox(0F, 0F, 0F, 1, 7, 1);
        ra1.setRotationPoint(-2F, 3F, 0F);
        ra1.setTextureSize(64, 128);
        ra1.mirror = true;
        setRotation(ra1, 0F, 0F, 0.4886922F);
        ra2 = new ModelRenderer(this, 25, 53);
        ra2.addBox(0F, 7F, -1F, 1, 2, 3);
        ra2.setRotationPoint(-2F, 3F, 0F);
        ra2.setTextureSize(64, 128);
        ra2.mirror = true;
        setRotation(ra2, 0F, 0F, 0.4886922F);
        ra3 = new ModelRenderer(this, 19, 59);
        ra3.addBox(0F, 9F, 0F, 1, 2, 1);
        ra3.setRotationPoint(-2F, 3F, 0F);
        ra3.setTextureSize(64, 128);
        ra3.mirror = true;
        setRotation(ra3, -0.1396263F, 0F, 0.4886922F);
        ra4 = new ModelRenderer(this, 24, 59);
        ra4.addBox(0F, 9F, 0F, 1, 3, 1);
        ra4.setRotationPoint(-2F, 3F, 0F);
        ra4.setTextureSize(64, 128);
        ra4.mirror = true;
        setRotation(ra4, 0F, 0F, 0.4886922F);
        ra5 = new ModelRenderer(this, 29, 59);
        ra5.addBox(0F, 9F, 0F, 1, 3, 1);
        ra5.setRotationPoint(-2F, 3F, 0F);
        ra5.setTextureSize(64, 128);
        ra5.mirror = true;
        setRotation(ra5, 0.1396263F, 0F, 0.4886922F);
        bp1 = new ModelRenderer(this, 31, 67);
        bp1.addBox(0F, 0F, 0F, 2, 3, 1);
        bp1.setRotationPoint(-1F, 4F, 2F);
        bp1.setTextureSize(64, 128);
        bp1.mirror = true;
        setRotation(bp1, 0F, 0F, 0F);
        bp3 = new ModelRenderer(this, 23, 72);
        bp3.addBox(-3.5F, 0F, 0F, 3, 8, 2);
        bp3.setRotationPoint(0F, 3F, 3F);
        bp3.setTextureSize(64, 128);
        bp3.mirror = true;
        setRotation(bp3, 0F, 0F, 0F);
        bp2 = new ModelRenderer(this, 35, 72);
        bp2.addBox(0.5F, 0F, 0F, 3, 8, 2);
        bp2.setRotationPoint(0F, 3F, 3F);
        bp2.setTextureSize(64, 128);
        bp2.mirror = true;
        setRotation(bp2, 0F, 0F, 0F);
        
        //these parts get connected together exactly as in the model, permanently.
        //rotate/position the first, and they all move together
        //less typing, and faster graphics! :)
        //easy for you, but I had to figure out the code to do it!!!
        //do this here in initialization BEFORE render() messes with the parts.
        //
        la1.connectPart(la2);
        la1.connectPart(la3);
        la1.connectPart(la4);
        la1.connectPart(la5);
        la1.finishConnect(); //finalize the connections!
        
        ra1.connectPart(ra2);
        ra1.connectPart(ra3);
        ra1.connectPart(ra4);
        ra1.connectPart(ra5);
        ra1.finishConnect();
        
        ll1.connectPart(ll2);
        ll1.connectPart(ll3);
        ll1.connectPart(ll4);
        ll1.finishConnect();
        
        rl1.connectPart(rl2);
        rl1.connectPart(rl3);
        rl1.connectPart(rl4);
        rl1.finishConnect();
    }
  
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
    {
    	float newangle = 0;
    	float roff = 0;

    	if(f1 > 0.02){
    		if(f1 > 1)f1 = 1; //MAX! Otherwise arms and legs go flying in circles...
    		newangle = (float) (Math.cos(Math.toRadians(f*12.5f)) * (float)Math.PI * 0.45F * f1);
    		if(entity.isBaby())newangle = (float) (Math.cos(Math.toRadians(f*22.5f)) * (float)Math.PI * 0.45F * f1);
    	}else{
    		newangle = 0.0F;
    	}

    	ll1.rotateAngleX = newangle;
    	rl1.rotateAngleX = -newangle;

    	ra1.rotateAngleZ = 0.488f;
    	//Need to know if we are holding something and will move the arm later.
    	//have to move it now so we don't have to load the texture twice!
    	if(entity != null && (entity != DangerZone.player || DangerZone.f5_front || DangerZone.f5_back)){
    		InventoryContainer ic = entity.getHotbar(0);
    		if(ic != null){
    			//Arm UP a bit...
    			roff += -0.824f;
    			if(entity.getRightButtonDownCount() != 0){
    				Item it = ic.getItem();
    				if(it != null){
    					if(it.hold_straight){
    						roff = (brain.rotateAngleX - (3.24159f/2f)) - 0.9f;
    						ra1.rotateAngleZ = 0;
    					}
    				}
    			}
    		}
    	}

    	ra1.rotateAngleX = newangle/2 + roff;
 
    	la1.rotateAngleX = -newangle/2;

    	//Don't go breaking our necks!
    	if(f2 > 40)f2 = 40;
    	if(f2 < -40)f2 = -40;
    	if(f3 > 55)f3 = 55;
    	if(f3 < -55)f3 = -55;
    	if(f4 > 30)f4 = 30;
    	if(f4 < -30)f4 = -30;

    	helmet.rotateAngleX = brain.rotateAngleX = 0.7f + (float) Math.toRadians(f2);
    	helmet.rotateAngleY = brain.rotateAngleY = (float) -Math.toRadians(f3);
    	helmet.rotateAngleZ = brain.rotateAngleZ = (float) Math.toRadians(f4);

    	if(f1 > 0.01){
    		if(f1 > 1)f1 = 1; //MAX! Otherwise arms and legs go flying in circles...
    		newangle = (float) (Math.cos(Math.toRadians(f*6.25f)) * (float)Math.PI * 0.25F * f1);
    		if(entity.isBaby())newangle = (float) (Math.cos(Math.toRadians(f*11.25f)) * (float)Math.PI * 0.25F * f1);
    	}else{
    		newangle = 0.0F;
    	}

    	helmet.rotateAngleZ += newangle;
    	brain.rotateAngleZ = helmet.rotateAngleZ;
	  
  	
    	rl1.render(deathfactor);
    	ll1.render(deathfactor);
    	body.render(deathfactor);
    	neck.render(deathfactor);
    	brain.render(deathfactor);
    	la1.render(deathfactor);
    	ra1.render(deathfactor);
    	bp1.render(deathfactor);
    	bp3.render(deathfactor);
    	bp2.render(deathfactor);

    	GL11.glEnable(GL11.GL_NORMALIZE);
    	GL11.glEnable(GL11.GL_BLEND);
    	GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);  

    	helmet.render(deathfactor);
    	
    	GL11.glDisable(GL11.GL_BLEND);
    	
  	  //Do all this AFTER so we don't have to reload the entity texture!
  	  //Draw whatever is being held...
  	  if(entity != null && (entity != DangerZone.player || DangerZone.f5_front || DangerZone.f5_back)){
  		  int bid = 0;
  		  int iid = 0;
  		  InventoryContainer ic = entity.getHotbar(entity.gethotbarindex());
  		  if(ic != null){
  			  bid = ic.bid;
  			  iid = ic.iid;
  		  }
  		  if(bid != 0 || iid != 0){
  			  GL11.glPushMatrix();
  			  GL11.glTranslatef(ra1.rotationPointX, ra1.rotationPointY+24, ra1.rotationPointZ);	    			

  			  if(bid != 0){
  				  
  				  GL11.glRotatef(-(float) Math.toDegrees(ra1.rotateAngleZ), 0.0f, 0.0f, 1.0f); // Rotate
  				  GL11.glRotatef((float) Math.toDegrees(ra1.rotateAngleY), 0.0f, 1.0f, 0.0f);
  				  GL11.glRotatef((float) Math.toDegrees(ra1.rotateAngleX)*8/10, 1.0f, 0.0f, 0.0f); // Rotate

  				  GL11.glTranslatef((float) (ra1.offsetX), 
  						  (float) (ra1.offsetY-8f), 
  						  (float) (ra1.offsetZ+10));

  				  GL11.glPushMatrix(); 
  				  
  				  GL11.glTranslatef(2.4f, -2.2f, -4.4f);
  				  GL11.glRotatef(46.1f, 0.0f, 0.0f, 1.0f); // Rotate
  				  GL11.glRotatef(22.0f, 0.0f, 1.0f, 0.0f);
  				  GL11.glRotatef(85.8f, 1.0f, 0.0f, 0.0f); // Rotate
  				  
  				  
  				  GL11.glScalef(0.30f, 0.30f, 0.30f);
  				  if(Blocks.hasOwnRenderer(bid)){
  					  Blocks.renderMeHeld(DangerZone.wr, entity, bid, false);
  				  }else{
  					  DangerZone.wr.drawTexturedCube(0xff, Blocks.isSolidForRender(bid), bid, 0, false);
  				  }
  				  GL11.glPopMatrix();
  				  
  			  }else{
  				  if(iid != 0){

  					  GL11.glRotatef(-(float) Math.toDegrees(ra1.rotateAngleZ), 0.0f, 0.0f, 1.0f); // Rotate
  					  GL11.glRotatef((float) Math.toDegrees(ra1.rotateAngleY), 0.0f, 1.0f, 0.0f);
  					  GL11.glRotatef((float) Math.toDegrees(ra1.rotateAngleX)*8/10, 1.0f, 0.0f, 0.0f); // Rotate - why 0.8???

  					  GL11.glTranslatef((float) (ra1.offsetX+2), 
  							  (float) (ra1.offsetY-16f), 
  							  (float) (ra1.offsetZ+2));

  					  GL11.glPushMatrix();  
  					  if(Items.isFlipped(iid)){
  						  GL11.glRotatef(268, 0.0f, 0.0f, 1.0f); // Rotate
  						  GL11.glRotatef(200, 0.0f, 1.0f, 0.0f);
  						  GL11.glRotatef(68, 1.0f, 0.0f, 0.0f); // Rotate
  					  }else{
  						GL11.glTranslatef(0, -0.7f, 0.6f);
  						GL11.glRotatef(-95.7f, 0.0f, 0.0f, 1.0f); // Rotate
  						GL11.glRotatef(-53.8f, 0.0f, 1.0f, 0.0f);
  						GL11.glRotatef(-73.2f, 1.0f, 0.0f, 0.0f); // Rotate
  					  }
  					  

  					  DangerZone.wr.loadtexture(Items.getTexture(iid));
  					  Items.renderMeHeld(DangerZone.wr, entity, iid, false);

  					  GL11.glPopMatrix();
  				  }
  			  }
  			  GL11.glPopMatrix();
  		  }	    	
  	  }

    }
  
	  public void doScale(Entity ent){
		  GL11.glScalef(0.75f, 0.75f, 0.75f);
		  if(ent.isBaby()){
			  GL11.glScalef(0.25f, 0.25f, 0.25f);
		  }
	  }


}
