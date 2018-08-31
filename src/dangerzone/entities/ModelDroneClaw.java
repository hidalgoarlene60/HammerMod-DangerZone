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

public class ModelDroneClaw extends ModelBase
{

    ModelRenderer swivel;
    ModelRenderer b1;
    ModelRenderer c1;
    ModelRenderer a1;
    ModelRenderer d1;
    ModelRenderer e1;
    ModelRenderer b2;
    ModelRenderer a2;
    ModelRenderer c2;
    ModelRenderer e2;
    ModelRenderer d2;
    ModelRenderer b3;
    ModelRenderer a3;
    ModelRenderer c3;
    ModelRenderer e3;
    ModelRenderer d3;

  
  public ModelDroneClaw()
  {
    
	   
	    
	      swivel = new ModelRenderer(this, 0, 0);
	      swivel.addBox(-7F, -1F, -1F, 14, 2, 2);
	      swivel.setRotationPoint(0F, 8F, 0F);
	      swivel.setTextureSize(64, 64);
	      swivel.mirror = true;
	      setRotation(swivel, 0F, 0F, 0F);
	      b1 = new ModelRenderer(this, 0, 22);
	      b1.addBox(-1F, 0F, -1F, 2, 6, 2);
	      b1.setRotationPoint(0F, 8F, 0F);
	      b1.setTextureSize(64, 64);
	      b1.mirror = true;
	      setRotation(b1, -0.8901179F, 0F, 0F);
	      c1 = new ModelRenderer(this, 0, 31);
	      c1.addBox(-7F, 0F, -1F, 2, 6, 2);
	      c1.setRotationPoint(0F, 8F, 0F);
	      c1.setTextureSize(64, 64);
	      c1.mirror = true;
	      setRotation(c1, -0.8901179F, 0F, 0F);
	      a1 = new ModelRenderer(this, 0, 13);
	      a1.addBox(5F, 0F, -1F, 2, 6, 2);
	      a1.setRotationPoint(0F, 8F, 0F);
	      a1.setTextureSize(64, 64);
	      a1.mirror = true;
	      setRotation(a1, -0.8901179F, 0F, 0F);
	      d1 = new ModelRenderer(this, 0, 40);
	      d1.addBox(2F, 0F, -1F, 2, 6, 2);
	      d1.setRotationPoint(0F, 8F, 0F);
	      d1.setTextureSize(64, 64);
	      d1.mirror = true;
	      setRotation(d1, 0.8901179F, 0F, 0F);
	      e1 = new ModelRenderer(this, 0, 49);
	      e1.addBox(-4F, 0F, -1F, 2, 6, 2);
	      e1.setRotationPoint(0F, 8F, 0F);
	      e1.setTextureSize(64, 64);
	      e1.mirror = true;
	      setRotation(e1, 0.8901179F, 0F, 0F);
	      b2 = new ModelRenderer(this, 9, 22);
	      b2.addBox(-1F, 5F, -3F, 2, 6, 2);
	      b2.setRotationPoint(0F, 8F, 0F);
	      b2.setTextureSize(64, 64);
	      b2.mirror = true;
	      setRotation(b2, -0.5235988F, 0F, 0F);
	      a2 = new ModelRenderer(this, 9, 13);
	      a2.addBox(5F, 5F, -3F, 2, 6, 2);
	      a2.setRotationPoint(0F, 8F, 0F);
	      a2.setTextureSize(64, 64);
	      a2.mirror = true;
	      setRotation(a2, -0.5235988F, 0F, 0F);
	      c2 = new ModelRenderer(this, 9, 31);
	      c2.addBox(-7F, 5F, -3F, 2, 6, 2);
	      c2.setRotationPoint(0F, 8F, 0F);
	      c2.setTextureSize(64, 64);
	      c2.mirror = true;
	      setRotation(c2, -0.5235988F, 0F, 0F);
	      e2 = new ModelRenderer(this, 9, 49);
	      e2.addBox(-4F, 5F, 1F, 2, 6, 2);
	      e2.setRotationPoint(0F, 8F, 0F);
	      e2.setTextureSize(64, 64);
	      e2.mirror = true;
	      setRotation(e2, 0.5235988F, 0F, 0F);
	      d2 = new ModelRenderer(this, 9, 40);
	      d2.addBox(2F, 5F, 1F, 2, 6, 2);
	      d2.setRotationPoint(0F, 8F, 0F);
	      d2.setTextureSize(64, 64);
	      d2.mirror = true;
	      setRotation(d2, 0.5235988F, 0F, 0F);
	      b3 = new ModelRenderer(this, 26, 22);
	      b3.addBox(-0.5F, 9F, -7F, 1, 9, 2);
	      b3.setRotationPoint(0F, 8F, 0F);
	      b3.setTextureSize(64, 64);
	      b3.mirror = true;
	      setRotation(b3, -0.122173F, 0F, 0F);
	      a3 = new ModelRenderer(this, 19, 13);
	      a3.addBox(5.5F, 9F, -7F, 1, 9, 2);
	      a3.setRotationPoint(0F, 8F, 0F);
	      a3.setTextureSize(64, 64);
	      a3.mirror = true;
	      setRotation(a3, -0.122173F, 0F, 0F);
	      c3 = new ModelRenderer(this, 19, 31);
	      c3.addBox(-6.5F, 9F, -7F, 1, 9, 2);
	      c3.setRotationPoint(0F, 8F, 0F);
	      c3.setTextureSize(64, 64);
	      c3.mirror = true;
	      setRotation(c3, -0.122173F, 0F, 0F);
	      e3 = new ModelRenderer(this, 19, 49);
	      e3.addBox(-3.5F, 9F, 5F, 1, 9, 2);
	      e3.setRotationPoint(0F, 8F, 0F);
	      e3.setTextureSize(64, 64);
	      e3.mirror = true;
	      setRotation(e3, 0.122173F, 0F, 0F);
	      d3 = new ModelRenderer(this, 26, 40);
	      d3.addBox(2.5F, 9F, 5F, 1, 9, 2);
	      d3.setRotationPoint(0F, 8F, 0F);
	      d3.setTextureSize(64, 64);
	      d3.mirror = true;
	      setRotation(d3, 0.122173F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
  {
	  if(!(entity instanceof DroneClaw))return;
	  DroneClaw dr = (DroneClaw)entity;
	  float open = dr.getOpen();
	  
	  a1.rotateAngleX = (float) (Math.toRadians(open) - 0.89f);
	  a2.rotateAngleX = (float) (Math.toRadians(open) - 0.524f);
	  a3.rotateAngleX = (float) (Math.toRadians(open) - 0.122f);
	  
	  b1.rotateAngleX = a1.rotateAngleX;
	  b2.rotateAngleX = a2.rotateAngleX;
	  b3.rotateAngleX = a3.rotateAngleX;
	  
	  c1.rotateAngleX = a1.rotateAngleX;
	  c2.rotateAngleX = a2.rotateAngleX;
	  c3.rotateAngleX = a3.rotateAngleX;
	  
	  d1.rotateAngleX = (float) (0.89f - Math.toRadians(open));
	  d2.rotateAngleX = (float) (0.524f - Math.toRadians(open));
	  d3.rotateAngleX = (float) (0.122f - Math.toRadians(open));
	  
	  e1.rotateAngleX = d1.rotateAngleX;
	  e2.rotateAngleX = d2.rotateAngleX;
	  e3.rotateAngleX = d3.rotateAngleX;

	    swivel.render(deathfactor);
	    b1.render(deathfactor);
	    c1.render(deathfactor);
	    a1.render(deathfactor);
	    d1.render(deathfactor);
	    e1.render(deathfactor);
	    b2.render(deathfactor);
	    a2.render(deathfactor);
	    c2.render(deathfactor);
	    e2.render(deathfactor);
	    d2.render(deathfactor);
	    b3.render(deathfactor);
	    a3.render(deathfactor);
	    c3.render(deathfactor);
	    e3.render(deathfactor);
	    d3.render(deathfactor);


  }
  


}
