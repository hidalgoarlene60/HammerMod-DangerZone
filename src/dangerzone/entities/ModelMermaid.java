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


import dangerzone.DangerZone;
import dangerzone.InventoryContainer;
import dangerzone.ModelBase;
import dangerzone.ModelRenderer;
import dangerzone.Player;
import dangerzone.blocks.Blocks;
import dangerzone.items.Item;
import dangerzone.items.ItemArmor;
import dangerzone.items.Items;




public class ModelMermaid extends ModelBase
{
  //fields
    ModelRenderer head;
    ModelRenderer body;
    ModelRenderer leftarm;
    ModelRenderer rightarm;
    ModelRenderer tail1;
    ModelRenderer tail2;
    ModelRenderer tail3;
    ModelRenderer tail4;
    ModelRenderer tail5;
    ModelRenderer flip1;
    ModelRenderer flip2;
    ModelRenderer flip3;
    ModelRenderer flip4;
    ModelRenderer flip5;
    ModelRenderer flip6;
 

    
  public ModelMermaid()
  {
	    
	      head = new ModelRenderer(this, 0, 0);
	      head.addBox(-4F, -8F, -4F, 8, 8, 8);
	      head.setRotationPoint(0F, 0F, 0F);
	      head.setTextureSize(64, 128);
	      setRotation(head, 0F, 0F, 0F);
	      body = new ModelRenderer(this, 0, 16);
	      body.addBox(-4F, 0F, -2F, 8, 12, 4);
	      body.setRotationPoint(0F, 0F, 0F);
	      body.setTextureSize(64, 128);
	      setRotation(body, 0F, 0F, 0F);
	      leftarm = new ModelRenderer(this, 24, 16);
	      leftarm.addBox(0F, -1F, -2F, 4, 12, 4);
	      leftarm.setRotationPoint(4F, 1F, 0F);
	      leftarm.setTextureSize(64, 128);
	      setRotation(leftarm, 0F, 0F, 0F);
	      leftarm.mirror = false;
	      rightarm = new ModelRenderer(this, 24, 16);
	      rightarm.addBox(-4F, -1F, -2F, 4, 12, 4);
	      rightarm.setRotationPoint(-4F, 1F, 0F);
	      rightarm.setTextureSize(64, 128);
	      setRotation(rightarm, 0F, 0F, 0F);
	      tail1 = new ModelRenderer(this, 0, 33);
	      tail1.addBox(-4F, 0F, -2F, 8, 8, 4);
	      tail1.setRotationPoint(0F, 10F, 0F);
	      tail1.setTextureSize(64, 128);
	      setRotation(tail1, 0.5410521F, 0F, 0F);
	      tail2 = new ModelRenderer(this, 0, 46);
	      tail2.addBox(-4F, 0F, -2F, 8, 7, 4);
	      tail2.setRotationPoint(0F, 16F, 3F);
	      tail2.setTextureSize(64, 128);
	      setRotation(tail2, 0.9948377F, 0F, 0F);
	      tail3 = new ModelRenderer(this, 0, 58);
	      tail3.addBox(-3.5F, 0F, -2F, 7, 6, 4);
	      tail3.setRotationPoint(0F, 19F, 8F);
	      tail3.setTextureSize(64, 128);
	      setRotation(tail3, 1.343904F, 0F, 0F);
	      tail4 = new ModelRenderer(this, 0, 69);
	      tail4.addBox(-3F, 0F, -2F, 6, 6, 4);
	      tail4.setRotationPoint(0F, 20F, 13F);
	      tail4.setTextureSize(64, 128);
	      setRotation(tail4, 1.500983F, 0F, 0F);
	      tail5 = new ModelRenderer(this, 0, 80);
	      tail5.addBox(-2.5F, 0F, -1.5F, 5, 6, 3);
	      tail5.setRotationPoint(0F, 20.5F, 18F);
	      tail5.setTextureSize(64, 128);
	      setRotation(tail5, 1.570796F, 0F, 0F);
	      flip1 = new ModelRenderer(this, 0, 90);
	      flip1.addBox(-2F, 0F, -1F, 4, 7, 2);
	      flip1.setRotationPoint(0F, 20.5F, 23F);
	      flip1.setTextureSize(64, 128);
	      setRotation(flip1, 1.570796F, 0F, 0F);
	      flip2 = new ModelRenderer(this, 0, 100);
	      flip2.addBox(-2F, 6F, -1F, 4, 7, 2);
	      flip2.setRotationPoint(0F, 20.5F, 23F);
	      flip2.setTextureSize(64, 128);
	      setRotation(flip2, 1.570796F, 0F, 0F);
	      flip3 = new ModelRenderer(this, 0, 112);
	      flip3.addBox(-4F, -0.5F, 10.5F, 10, 1, 12);
	      flip3.setRotationPoint(0F, 20.5F, 23F);
	      flip3.setTextureSize(64, 128);
	      setRotation(flip3, 0F, 0.3316126F, 0F);
	      flip4 = new ModelRenderer(this, 18, 97);
	      flip4.addBox(-6F, -0.5F, 10.5F, 10, 1, 12);
	      flip4.setRotationPoint(0F, 20.5F, 23F);
	      flip4.setTextureSize(64, 128);
	      setRotation(flip4, 0F, -0.3316126F, 0F);
	      flip5 = new ModelRenderer(this, 29, 84);
	      flip5.addBox(-7.5F, -0.5F, 3F, 6, 1, 9);
	      flip5.setRotationPoint(0F, 20.5F, 23F);
	      flip5.setTextureSize(64, 128);
	      setRotation(flip5, 0F, 0.9773844F, 0F);
	      flip6 = new ModelRenderer(this, 29, 72);
	      flip6.addBox(1.5F, -0.5F, 3F, 6, 1, 9);
	      flip6.setRotationPoint(0F, 20.5F, 23F);
	      flip6.setTextureSize(64, 128);
	      setRotation(flip6, 0F, -0.9773844F, 0F);
	      
	      //For some reason, connecting the tail parts doesn't work.
	      //I don't feel like debugging the connector!!!
      
  }
  
  // f = lifetime ticker. Doing Math.toRadians() on it provides a nice smooth wave cycle.
  // f1 = entity velocity
  // f2 = pitch head
  // f3 = yaw head
  // f4 = roll head
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
  {
	  float newangle = 0;
	  Entity ridden = null;
	  
	  if(entity != null){
		  ridden = entity.getRiddenEntity();
	  }

	  if(ridden != null)f1 = 0;
	  
	  if(f1 > 0.02){
		  if(f1 > 1)f1 = 1; //MAX! Otherwise arms and legs go flying in circles...
		  newangle = (float) (Math.cos(Math.toRadians(f*10.6f)) * (float)Math.PI * 0.45F * f1);
		  if(entity.isBaby())newangle = (float) (Math.cos(Math.toRadians(f*20.6f)) * (float)Math.PI * 0.45F * f1);
	  }else{
		  newangle = 0.0F;
	  }



	  leftarm.rotateAngleX = -newangle;
	  rightarm.rotateAngleX = newangle;

	  newangle = (float) (Math.cos(Math.toRadians(f*1.6f)) * (float)Math.PI * 0.01f);
	  leftarm.rotateAngleZ = -0.017f + newangle;
	  newangle = (float) (Math.cos(Math.toRadians(f*1.3f)) * (float)Math.PI * 0.01f);
	  rightarm.rotateAngleZ = 0.017f + newangle;

	  newangle = (float) (Math.cos(Math.toRadians(f*1.1f)) * (float)Math.PI * 0.015f);
	  leftarm.rotateAngleX += newangle;
	  newangle = (float) (Math.cos(Math.toRadians(f*1.2f)) * (float)Math.PI * 0.015f);
	  rightarm.rotateAngleX += newangle;
	  if(entity != null){		  
		  if(entity.getArmsUp()){
			  rightarm.rotateAngleX -= Math.PI/2;
			  leftarm.rotateAngleX -= Math.PI/2;
		  }else{
			  rightarm.rotateAngleX += Math.toRadians(entity.getRightArmAngle());
			  rightarm.rotateAngleZ += Math.toRadians(entity.getRightArmAngle())/6f;
		  }
	  }
	  
	  //Don't go breaking our necks!
	  if(f2 > 40)f2 = 40;
	  if(f2 < -40)f2 = -40;
	  if(f3 > 55)f3 = 55;
	  if(f3 < -55)f3 = -55;
	  if(f4 > 30)f4 = 30;
	  if(f4 < -30)f4 = -30;
	  
	  head.rotateAngleX = (float) Math.toRadians(f2);
	  if(entity instanceof Player){
		  head.rotateAngleY = (float) Math.toRadians(f3);
	  }else{
		  head.rotateAngleY = (float) -Math.toRadians(f3);
	  }
	  head.rotateAngleZ = (float) Math.toRadians(f4);

	  //Need to know if we are holding something and will move the arm later.
	  //have to move it now so we don't have to load the texture twice!
	  if(entity != null && (entity != DangerZone.player || DangerZone.f5_front || DangerZone.f5_back)){
		  InventoryContainer ic = entity.getHotbar(entity.gethotbarindex());
		  if(ic != null){
			  //Arm UP a bit...
			  rightarm.rotateAngleX += -0.624f;
			  if(entity.getRightButtonDownCount() != 0){
				  Item it = ic.getItem();
				  if(it != null){
					  if(it.hold_straight){
						  rightarm.rotateAngleX = head.rotateAngleX - (3.24159f/2f);
						  rightarm.rotateAngleZ = 0;
					  }
				  }
			  }
		  }
	  }
	  
	  //Tail movement... up and down!
	  
	    //Makes a nice fairly impressive slither!
	    if(f1 > 1)f1 = 1;
	    if(f1 < 0.01f)f1 = 0.01f;
		  // TAIL
		  float tailspeed = 3.25f;
		  float tailamp = 0.10f;
		  float pi4 = 3.1415926535f/4.0f;
		  
		  if(entity.getAttacking()){
			  tailspeed = 5.25f;
			  tailamp = 0.25f;
		  }
		  
		  tailamp *= f1*3;
		  
		  this.tail1.rotateAngleX = (float) (Math.sin(Math.toRadians(f * tailspeed) ) * (float)Math.PI * tailamp/2.0F);
		  this.tail1.rotateAngleX += 0.541f;

		  this.tail2.rotationPointZ = this.tail1.rotationPointZ + (float)Math.sin(this.tail1.rotateAngleX)*7;
		  this.tail2.rotationPointY = this.tail1.rotationPointY + (float)Math.cos(this.tail1.rotateAngleX)*7;   
		  this.tail2.rotateAngleX = (float) (Math.sin(Math.toRadians(f * tailspeed)  - pi4) * (float)Math.PI * tailamp);
		  this.tail2.rotateAngleX += 0.995f;

		  this.tail3.rotationPointZ = this.tail2.rotationPointZ + (float)Math.sin(this.tail2.rotateAngleX)*6;
		  this.tail3.rotationPointY = this.tail2.rotationPointY + (float)Math.cos(this.tail2.rotateAngleX)*6;    
		  this.tail3.rotateAngleX = (float) (Math.sin(Math.toRadians(f * tailspeed)  - 2.0F*pi4) * (float)Math.PI * tailamp);
		  this.tail3.rotateAngleX += 1.344f;

		  this.tail4.rotationPointZ = this.tail3.rotationPointZ + (float)Math.sin(this.tail3.rotateAngleX)*5;
		  this.tail4.rotationPointY = this.tail3.rotationPointY + (float)Math.cos(this.tail3.rotateAngleX)*5;    
		  this.tail4.rotateAngleX = (float) (Math.sin(Math.toRadians(f * tailspeed)  - 3.0F*pi4) * (float)Math.PI * tailamp);
		  this.tail4.rotateAngleX += 1.501f;

		  this.tail5.rotationPointZ = this.tail4.rotationPointZ + (float)Math.sin(this.tail4.rotateAngleX)*5;
		  this.tail5.rotationPointY = this.tail4.rotationPointY + (float)Math.cos(this.tail4.rotateAngleX)*5;
		  this.tail5.rotateAngleX = (float) (Math.sin(Math.toRadians(f * tailspeed)  - 4.0F*pi4) * (float)Math.PI * tailamp);
		  this.tail5.rotateAngleX += 1.571f;
		  
		  this.flip1.rotationPointZ = this.tail5.rotationPointZ + (float)Math.sin(this.tail5.rotateAngleX)*5;
		  this.flip1.rotationPointY = this.tail5.rotationPointY + (float)Math.cos(this.tail5.rotateAngleX)*5;
		  this.flip1.rotateAngleX = (float) (Math.sin(Math.toRadians(f * tailspeed)  - 4.0F*pi4) * (float)Math.PI * tailamp);
		  this.flip1.rotateAngleX += 1.571f;
		  
		  flip2.rotationPointX = flip3.rotationPointX = flip4.rotationPointX = flip5.rotationPointX = flip6.rotationPointX = flip1.rotationPointX;
		  flip2.rotationPointY = flip3.rotationPointY = flip4.rotationPointY = flip5.rotationPointY = flip6.rotationPointY = flip1.rotationPointY;
		  flip2.rotateAngleX = flip1.rotateAngleX;
		  flip3.rotateAngleX = flip4.rotateAngleX = flip5.rotateAngleX = flip6.rotateAngleX = flip1.rotateAngleX - 1.571f;
	  
	  

	  head.render(deathfactor);
	  body.render(deathfactor);
	  leftarm.render(deathfactor);
	  rightarm.render(deathfactor);
	  tail1.render(deathfactor);
	  tail2.render(deathfactor);
	  tail3.render(deathfactor);
	  tail4.render(deathfactor);
	  tail5.render(deathfactor);
	  flip1.render(deathfactor);
	  flip2.render(deathfactor);
	  flip3.render(deathfactor);
	  flip4.render(deathfactor);
	  flip5.render(deathfactor);
	  flip6.render(deathfactor);
	  

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
			  GL11.glTranslatef(rightarm.rotationPointX, rightarm.rotationPointY+24, rightarm.rotationPointZ);	    			

			  if(bid != 0){
				  
				  GL11.glRotatef(-(float) Math.toDegrees(rightarm.rotateAngleZ), 0.0f, 0.0f, 1.0f); // Rotate
				  GL11.glRotatef((float) Math.toDegrees(rightarm.rotateAngleY), 0.0f, 1.0f, 0.0f);
				  GL11.glRotatef((float) Math.toDegrees(rightarm.rotateAngleX)*8/10, 1.0f, 0.0f, 0.0f); // Rotate

				  GL11.glTranslatef((float) (rightarm.offsetX), 
						  (float) (rightarm.offsetY-8f), 
						  (float) (rightarm.offsetZ+10));

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

					  GL11.glRotatef(-(float) Math.toDegrees(rightarm.rotateAngleZ), 0.0f, 0.0f, 1.0f); // Rotate
					  GL11.glRotatef((float) Math.toDegrees(rightarm.rotateAngleY), 0.0f, 1.0f, 0.0f);
					  GL11.glRotatef((float) Math.toDegrees(rightarm.rotateAngleX)*8/10, 1.0f, 0.0f, 0.0f); // Rotate - why 0.8???

					  GL11.glTranslatef((float) (rightarm.offsetX), 
							  (float) (rightarm.offsetY-8f), 
							  (float) (rightarm.offsetZ+10));

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
	  
	  /*
	   * Now let's do armor!
	   * It is essentially the same, just scaled up a smidge to overlay.
	   */
	  if(entity != null && entity instanceof EntityLiving){
		  EntityLiving pl = (EntityLiving)entity;

		  int armorid = pl.getHelmetID();
		  if(armorid != 0){
			  Item it = Items.getItem(armorid);
			  if(it != null && it instanceof ItemArmor){
				  ItemArmor ita = (ItemArmor)it;				  
				  ita.drawHelmet(entity, head, deathfactor);				  
			  }
		  }

		  //chestplate and boots have to come after leggings, because they overlap 
		  armorid = pl.getChestplateID();
		  if(armorid != 0){
			  Item it = Items.getItem(armorid);
			  if(it != null && it instanceof ItemArmor){
				  ItemArmor ita = (ItemArmor)it;
				  ita.drawChestplate(entity, body, leftarm, rightarm, deathfactor);
			  }
		  }

	  }
	  
  }
  
  
  public void doScale(Entity ent){
	  super.doScale(ent);
	  GL11.glScalef(0.80f, 0.95f, 0.80f); 
  }
  
  

}
