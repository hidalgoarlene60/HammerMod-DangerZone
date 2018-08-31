package dangerzone.blocks;

import org.lwjgl.opengl.GL11;

import dangerzone.DangerZone;
import dangerzone.InventoryContainer;
import dangerzone.ModelBase;
import dangerzone.ModelRenderer;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityStand;
import dangerzone.items.Items;


public class ModelStand extends ModelBase
{

    ModelRenderer stem;
    ModelRenderer xbase1;
    ModelRenderer xbase2;
    ModelRenderer top;
    ModelRenderer zbase1;
    ModelRenderer zbase2;
    ModelRenderer xbase1a;
    ModelRenderer xbase2a;
    ModelRenderer zbase1a;
    ModelRenderer zbase2a;
    ModelRenderer frame1;
    ModelRenderer frame2;
    ModelRenderer frame4;
    ModelRenderer frame5;
    ModelRenderer frame7;
    ModelRenderer frame8;
    ModelRenderer frame10;
    ModelRenderer frame11;
  
    public ModelStand()
    {

        stem = new ModelRenderer(this, 0, 0);
        stem.addBox(-0.5F, -6F, -0.5F, 1, 10, 1);
        stem.setRotationPoint(0F, 16F, 0F);
        stem.setTextureSize(16, 16);
        stem.mirror = true;
        setRotation(stem, 0F, 0F, 0F);
        xbase1 = new ModelRenderer(this, 4, 6);
        xbase1.addBox(0F, 3F, -0.5F, 4, 1, 1);
        xbase1.setRotationPoint(0F, 16F, 0F);
        xbase1.setTextureSize(16, 16);
        xbase1.mirror = true;
        setRotation(xbase1, 0F, 0F, 0F);
        xbase2 = new ModelRenderer(this, 0, 0);
        xbase2.addBox(3F, 4F, -0.5F, 1, 4, 1);
        xbase2.setRotationPoint(0F, 16F, 0F);
        xbase2.setTextureSize(16, 16);
        xbase2.mirror = true;
        setRotation(xbase2, 0F, 0F, 0F);
        top = new ModelRenderer(this, 0, 11);
        top.addBox(-4F, -7F, -4F, 4, 1, 4);
        top.setRotationPoint(0F, 16F, 0F);
        top.setTextureSize(16, 16);
        top.mirror = true;
        setRotation(top, 0F, 0F, 0F);
        zbase1 = new ModelRenderer(this, 0, 0);
        zbase1.addBox(-0.5F, 3F, 0F, 1, 1, 4);
        zbase1.setRotationPoint(0F, 16F, 0F);
        zbase1.setTextureSize(16, 16);
        zbase1.mirror = true;
        setRotation(zbase1, 0F, 0F, 0F);
        zbase2 = new ModelRenderer(this, 0, 0);
        zbase2.addBox(-0.5F, 4F, 3F, 1, 4, 1);
        zbase2.setRotationPoint(0F, 16F, 0F);
        zbase2.setTextureSize(16, 16);
        zbase2.mirror = true;
        setRotation(zbase2, 0F, 0F, 0F);
        xbase1a = new ModelRenderer(this, 4, 6);
        xbase1a.addBox(-4F, 3F, -0.5F, 4, 1, 1);
        xbase1a.setRotationPoint(0F, 16F, 0F);
        xbase1a.setTextureSize(16, 16);
        xbase1a.mirror = true;
        setRotation(xbase1a, 0F, 0F, 0F);
        xbase2a = new ModelRenderer(this, 0, 0);
        xbase2a.addBox(-4F, 4F, -0.5F, 1, 4, 1);
        xbase2a.setRotationPoint(0F, 16F, 0F);
        xbase2a.setTextureSize(16, 16);
        xbase2a.mirror = true;
        setRotation(xbase2a, 0F, 0F, 0F);
        zbase1a = new ModelRenderer(this, 0, 0);
        zbase1a.addBox(-0.5F, 3F, -4F, 1, 1, 4);
        zbase1a.setRotationPoint(0F, 16F, 0F);
        zbase1a.setTextureSize(16, 16);
        zbase1a.mirror = true;
        setRotation(zbase1a, 0F, 0F, 0F);
        zbase2a = new ModelRenderer(this, 0, 0);
        zbase2a.addBox(-0.5F, 4F, -4F, 1, 4, 1);
        zbase2a.setRotationPoint(0F, 16F, 0F);
        zbase2a.setTextureSize(16, 16);
        zbase2a.mirror = true;
        setRotation(zbase2a, 0F, 0F, 0F);
        frame1 = new ModelRenderer(this, 4, 6);
        frame1.addBox(-4F, -8F, -4F, 4, 1, 1);
        frame1.setRotationPoint(0F, 16F, 0F);
        frame1.setTextureSize(16, 16);
        frame1.mirror = true;
        setRotation(frame1, 0F, 0F, 0F);
        frame2 = new ModelRenderer(this, 4, 6);
        frame2.addBox(0F, -8F, -4F, 3, 1, 1);
        frame2.setRotationPoint(0F, 16F, 0F);
        frame2.setTextureSize(16, 16);
        frame2.mirror = true;
        setRotation(frame2, 0F, 0F, 0F);
        frame4 = new ModelRenderer(this, 0, 0);
        frame4.addBox(3F, -8F, -4F, 1, 1, 4);
        frame4.setRotationPoint(0F, 16F, 0F);
        frame4.setTextureSize(16, 16);
        frame4.mirror = true;
        setRotation(frame4, 0F, 0F, 0F);
        frame5 = new ModelRenderer(this, 0, 0);
        frame5.addBox(3F, -8F, 0F, 1, 1, 3);
        frame5.setRotationPoint(0F, 16F, 0F);
        frame5.setTextureSize(16, 16);
        frame5.mirror = true;
        setRotation(frame5, 0F, 0F, 0F);
        frame7 = new ModelRenderer(this, 4, 6);
        frame7.addBox(0F, -8F, 3F, 4, 1, 1);
        frame7.setRotationPoint(0F, 16F, 0F);
        frame7.setTextureSize(16, 16);
        frame7.mirror = true;
        setRotation(frame7, 0F, 0F, 0F);
        frame8 = new ModelRenderer(this, 4, 6);
        frame8.addBox(-3F, -8F, 3F, 3, 1, 1);
        frame8.setRotationPoint(0F, 16F, 0F);
        frame8.setTextureSize(16, 16);
        frame8.mirror = true;
        setRotation(frame8, 0F, 0F, 0F);
        frame10 = new ModelRenderer(this, 0, 0);
        frame10.addBox(-4F, -8F, 0F, 1, 1, 4);
        frame10.setRotationPoint(0F, 16F, 0F);
        frame10.setTextureSize(16, 16);
        frame10.mirror = true;
        setRotation(frame10, 0F, 0F, 0F);
        frame11 = new ModelRenderer(this, 0, 0);
        frame11.addBox(-4F, -8F, -3F, 1, 1, 3);
        frame11.setRotationPoint(0F, 16F, 0F);
        frame11.setTextureSize(16, 16);
        frame11.mirror = true;
        setRotation(frame11, 0F, 0F, 0F);
        
    }
    
    
    //This just draws the item or block being held. The actual stand is drawn as a block in the VBO thread!
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
    {
  	  if(!(entity instanceof EntityStand))return;
  	    
  	    InventoryContainer ic = entity.getInventory(0);
  	    if(ic != null){
  	    	EntityStand eml = (EntityStand)entity;
  	    	int bid, iid;

  	    	bid = ic.bid;
  	    	iid = ic.iid;

  	    	float spinz = eml.getSpinz();


  	    	GL11.glPushMatrix();
  	    	GL11.glTranslatef(0, 20, 0);


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
  	    	
  	    	GL11.glTranslatef(0, -20, 0);
  	    	GL11.glPopMatrix();

  	    }

    }
    
  
 
  
}