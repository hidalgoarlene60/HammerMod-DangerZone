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
public class ModelOstrich extends ModelBase
{

  //fields
    ModelRenderer Body1;
    ModelRenderer body2;
    ModelRenderer LLeg1;
    ModelRenderer Rleg1;
    ModelRenderer LLeg2;
    ModelRenderer Lfoot1;
    ModelRenderer RLeg2;
    ModelRenderer Lfoot2;
    ModelRenderer Lfoot3;
    ModelRenderer LClaw1;
    ModelRenderer LClaw2;
    ModelRenderer LClaw3;
    ModelRenderer Lfoot4;
    ModelRenderer LClaw4;
    ModelRenderer Rfoot1;
    ModelRenderer Rfoot2;
    ModelRenderer Rclaw1;
    ModelRenderer Rfoot3;
    ModelRenderer Rclaw3;
    ModelRenderer Rfoot4;
    ModelRenderer Rclaw2;
    ModelRenderer Rclaw4;
    ModelRenderer Body3;
    ModelRenderer Tail1;
    ModelRenderer Tail2;
    ModelRenderer Tail3;
    ModelRenderer Body4;
    ModelRenderer Hat1;
    ModelRenderer Hat2;
    ModelRenderer head;
    ModelRenderer leftleg;
    ModelRenderer Neck1;
    ModelRenderer Head1;
    ModelRenderer mouth1;
    ModelRenderer neck2;
    ModelRenderer rightleg;
    ModelRenderer Lwing;
    ModelRenderer Rwing;

  
  public ModelOstrich()
  {
      
	     Body1 = new ModelRenderer(this, 0, 28);
	      Body1.addBox(-4F, 0F, 0F, 8, 9, 8);
	      Body1.setRotationPoint(0F, 0F, -6F);
	      Body1.setTextureSize(256, 128);
	      Body1.mirror = true;
	      setRotation(Body1, -0.2230717F, 0F, 0F);
	      body2 = new ModelRenderer(this, 25, 111);
	      body2.addBox(-4F, 0F, 0F, 8, 8, 8);
	      body2.setRotationPoint(0F, 2F, -1F);
	      body2.setTextureSize(256, 128);
	      body2.mirror = true;
	      setRotation(body2, 0F, 0F, 0F);
	      LLeg1 = new ModelRenderer(this, 25, 70);
	      LLeg1.addBox(-1F, 3F, -5F, 2, 7, 3);
	      LLeg1.setRotationPoint(3F, 8F, 1F);
	      LLeg1.setTextureSize(256, 128);
	      LLeg1.mirror = true;
	      setRotation(LLeg1, 0.4833219F, 0F, 0F);
	      Rleg1 = new ModelRenderer(this, 25, 70);
	      Rleg1.addBox(-2F, 3F, -5F, 2, 7, 3);
	      Rleg1.setRotationPoint(-2F, 8F, 1F);
	      Rleg1.setTextureSize(256, 128);
	      Rleg1.mirror = true;
	      setRotation(Rleg1, 0.4833219F, 0F, 0F);
	      LLeg2 = new ModelRenderer(this, 29, 59);
	      LLeg2.addBox(-1F, 7F, 4F, 2, 7, 3);
	      LLeg2.setRotationPoint(3F, 8F, 1F);
	      LLeg2.setTextureSize(256, 128);
	      LLeg2.mirror = true;
	      setRotation(LLeg2, -0.4370552F, 0F, 0F);
	      Lfoot1 = new ModelRenderer(this, 29, 50);
	      Lfoot1.addBox(-1F, 14F, -5F, 2, 2, 6);
	      Lfoot1.setRotationPoint(3F, 8F, 1F);
	      Lfoot1.setTextureSize(256, 128);
	      Lfoot1.mirror = true;
	      setRotation(Lfoot1, 0F, 0F, 0F);
	      RLeg2 = new ModelRenderer(this, 29, 59);
	      RLeg2.addBox(-2F, 7F, 4F, 2, 7, 3);
	      RLeg2.setRotationPoint(-2F, 8F, 1F);
	      RLeg2.setTextureSize(256, 128);
	      RLeg2.mirror = true;
	      setRotation(RLeg2, -0.4370552F, 0F, 0F);
	      Lfoot2 = new ModelRenderer(this, 0, 9);
	      Lfoot2.addBox(-1F, 15F, -4F, 2, 1, 5);
	      Lfoot2.setRotationPoint(3F, 8F, 1F);
	      Lfoot2.setTextureSize(256, 128);
	      Lfoot2.mirror = true;
	      setRotation(Lfoot2, 0F, 0.2602503F, 0F);
	      Lfoot3 = new ModelRenderer(this, 0, 9);
	      Lfoot3.addBox(-1F, 15F, -4F, 2, 1, 5);
	      Lfoot3.setRotationPoint(3F, 8F, 1F);
	      Lfoot3.setTextureSize(256, 128);
	      Lfoot3.mirror = true;
	      setRotation(Lfoot3, 0F, -0.260246F, 0F);
	      LClaw1 = new ModelRenderer(this, 16, 10);
	      LClaw1.addBox(0F, 14F, -7F, 0, 2, 3);
	      LClaw1.setRotationPoint(3F, 8F, 1F);
	      LClaw1.setTextureSize(256, 128);
	      LClaw1.mirror = true;
	      setRotation(LClaw1, 0F, 0F, 0F);
	      LClaw2 = new ModelRenderer(this, 19, 16);
	      LClaw2.addBox(-0.5F, 15F, -5F, 0, 1, 3);
	      LClaw2.setRotationPoint(3F, 8F, 1F);
	      LClaw2.setTextureSize(256, 128);
	      LClaw2.mirror = true;
	      setRotation(LClaw2, 0F, 0.260246F, 0F);
	      LClaw3 = new ModelRenderer(this, 19, 16);
	      LClaw3.addBox(0.5F, 15F, -5F, 0, 1, 3);
	      LClaw3.setRotationPoint(3F, 8F, 1F);
	      LClaw3.setTextureSize(256, 128);
	      LClaw3.mirror = true;
	      setRotation(LClaw3, 0F, -0.260246F, 0F);
	      Lfoot4 = new ModelRenderer(this, 0, 0);
	      Lfoot4.addBox(-1F, 14F, -1F, 2, 2, 4);
	      Lfoot4.setRotationPoint(3F, 8F, 1F);
	      Lfoot4.setTextureSize(256, 128);
	      Lfoot4.mirror = true;
	      setRotation(Lfoot4, 0F, 0F, 0F);
	      LClaw4 = new ModelRenderer(this, 16, 10);
	      LClaw4.addBox(0F, 14F, 2F, 0, 2, 3);
	      LClaw4.setRotationPoint(3F, 8F, 1F);
	      LClaw4.setTextureSize(256, 128);
	      LClaw4.mirror = true;
	      setRotation(LClaw4, 0F, 0F, 0F);
	      Rfoot1 = new ModelRenderer(this, 29, 50);
	      Rfoot1.addBox(-2F, 14F, -5F, 2, 2, 6);
	      Rfoot1.setRotationPoint(-2F, 8F, 1F);
	      Rfoot1.setTextureSize(256, 128);
	      Rfoot1.mirror = true;
	      setRotation(Rfoot1, 0F, 0F, 0F);
	      Rfoot2 = new ModelRenderer(this, 0, 0);
	      Rfoot2.addBox(-2F, 14F, -1F, 2, 2, 4);
	      Rfoot2.setRotationPoint(-2F, 8F, 1F);
	      Rfoot2.setTextureSize(256, 128);
	      Rfoot2.mirror = true;
	      setRotation(Rfoot2, 0F, 0F, 0F);
	      Rclaw1 = new ModelRenderer(this, 16, 10);
	      Rclaw1.addBox(-1F, 14F, -7F, 0, 2, 3);
	      Rclaw1.setRotationPoint(-2F, 8F, 1F);
	      Rclaw1.setTextureSize(256, 128);
	      Rclaw1.mirror = true;
	      setRotation(Rclaw1, 0F, 0F, 0F);
	      Rfoot3 = new ModelRenderer(this, 0, 9);
	      Rfoot3.addBox(-2F, 15F, -4F, 2, 1, 5);
	      Rfoot3.setRotationPoint(-2F, 8F, 1F);
	      Rfoot3.setTextureSize(256, 128);
	      Rfoot3.mirror = true;
	      setRotation(Rfoot3, 0F, -0.260246F, 0F);
	      Rclaw3 = new ModelRenderer(this, 19, 16);
	      Rclaw3.addBox(-0.5F, 15F, -5F, 0, 1, 3);
	      Rclaw3.setRotationPoint(-2F, 8F, 1F);
	      Rclaw3.setTextureSize(256, 128);
	      Rclaw3.mirror = true;
	      setRotation(Rclaw3, 0F, -0.260246F, 0F);
	      Rfoot4 = new ModelRenderer(this, 0, 9);
	      Rfoot4.addBox(-2F, 15F, -4F, 2, 1, 5);
	      Rfoot4.setRotationPoint(-2F, 8F, 1F);
	      Rfoot4.setTextureSize(256, 128);
	      Rfoot4.mirror = true;
	      setRotation(Rfoot4, 0F, 0.2602503F, 0F);
	      Rclaw2 = new ModelRenderer(this, 19, 16);
	      Rclaw2.addBox(-1.5F, 15F, -5F, 0, 1, 3);
	      Rclaw2.setRotationPoint(-2F, 8F, 1F);
	      Rclaw2.setTextureSize(256, 128);
	      Rclaw2.mirror = true;
	      setRotation(Rclaw2, 0F, 0.260246F, 0F);
	      Rclaw4 = new ModelRenderer(this, 16, 10);
	      Rclaw4.addBox(-1F, 14F, 2F, 0, 2, 3);
	      Rclaw4.setRotationPoint(-2F, 8F, 1F);
	      Rclaw4.setTextureSize(256, 128);
	      Rclaw4.mirror = true;
	      setRotation(Rclaw4, 0F, 0F, 0F);
	      Body3 = new ModelRenderer(this, 17, 96);
	      Body3.addBox(-3F, 0F, 0F, 6, 7, 3);
	      Body3.setRotationPoint(0F, 2F, 6F);
	      Body3.setTextureSize(256, 128);
	      Body3.mirror = true;
	      setRotation(Body3, 0F, 0F, 0F);
	      Tail1 = new ModelRenderer(this, 33, 81);
	      Tail1.addBox(-2F, 0F, 0F, 4, 0, 14);
	      Tail1.setRotationPoint(0F, 3F, 9F);
	      Tail1.setTextureSize(256, 128);
	      Tail1.mirror = true;
	      setRotation(Tail1, -0.5948578F, 0F, 0F);
	      Tail2 = new ModelRenderer(this, 36, 97);
	      Tail2.addBox(-1F, 0F, 0F, 3, 0, 13);
	      Tail2.setRotationPoint(0F, 3F, 8F);
	      Tail2.setTextureSize(256, 128);
	      Tail2.mirror = true;
	      setRotation(Tail2, -0.5948578F, 0.3346075F, 0F);
	      Tail3 = new ModelRenderer(this, 36, 97);
	      Tail3.addBox(-2F, 0F, 0F, 3, 0, 13);
	      Tail3.setRotationPoint(0F, 3F, 8F);
	      Tail3.setTextureSize(256, 128);
	      Tail3.mirror = true;
	      setRotation(Tail3, -0.5948578F, -0.3346145F, 0F);
	      Body4 = new ModelRenderer(this, 17, 89);
	      Body4.addBox(-2F, 0F, 0F, 4, 3, 3);
	      Body4.setRotationPoint(0F, 6F, 7F);
	      Body4.setTextureSize(256, 128);
	      Body4.mirror = true;
	      setRotation(Body4, 1.003822F, 0F, 0F);
	      Hat1 = new ModelRenderer(this, 40, 0);
	      Hat1.addBox(-2.5F, -26F, -4F, 5, 1, 5);
	      Hat1.setRotationPoint(0F, 5F, -7F);
	      Hat1.setTextureSize(256, 128);
	      Hat1.mirror = true;
	      setRotation(Hat1, 0F, 0F, 0F);
	      Hat2 = new ModelRenderer(this, 40, 0);
	      Hat2.addBox(-2F, -28F, -3F, 4, 2, 4);
	      Hat2.setRotationPoint(0F, 5F, -7F);
	      Hat2.setTextureSize(256, 128);
	      Hat2.mirror = true;
	      setRotation(Hat2, 0F, 0F, 0F);
	      head = new ModelRenderer(this, 74, 48);
	      head.addBox(-1F, -24F, -7F, 2, 2, 4);
	      head.setRotationPoint(0F, 5F, -7F);
	      head.setTextureSize(256, 128);
	      head.mirror = true;
	      setRotation(head, 0F, 0F, 0F);
	      leftleg = new ModelRenderer(this, 0, 16);
	      leftleg.addBox(-2F, 0F, -2F, 4, 6, 5);
	      leftleg.setRotationPoint(3F, 8F, 1F);
	      leftleg.setTextureSize(256, 128);
	      leftleg.mirror = true;
	      setRotation(leftleg, -0.2974289F, 0F, 0F);
	      Neck1 = new ModelRenderer(this, 79, 84);
	      Neck1.addBox(-1.5F, -21F, -2F, 3, 21, 3);
	      Neck1.setRotationPoint(0F, 5F, -7F);
	      Neck1.setTextureSize(256, 128);
	      Neck1.mirror = true;
	      setRotation(Neck1, 0F, -0.0349066F, 0F);
	      Head1 = new ModelRenderer(this, 0, 70);
	      Head1.addBox(-2F, -25F, -3F, 4, 4, 4);
	      Head1.setRotationPoint(0F, 5F, -7F);
	      Head1.setTextureSize(256, 128);
	      Head1.mirror = true;
	      setRotation(Head1, 0F, 0F, 0F);
	      mouth1 = new ModelRenderer(this, 74, 64);
	      mouth1.addBox(-1F, -22F, -6F, 2, 1, 3);
	      mouth1.setRotationPoint(0F, 5F, -7F);
	      mouth1.setTextureSize(256, 128);
	      mouth1.mirror = true;
	      setRotation(mouth1, 0F, 0F, 0F);
	      neck2 = new ModelRenderer(this, 0, 99);
	      neck2.addBox(-1F, -2F, -2F, 2, 4, 3);
	      neck2.setRotationPoint(0F, 5F, -6.9F);
	      neck2.setTextureSize(256, 128);
	      neck2.mirror = true;
	      setRotation(neck2, 0F, 0F, 0F);
	      rightleg = new ModelRenderer(this, 0, 16);
	      rightleg.addBox(-3F, 0F, -2F, 4, 6, 5);
	      rightleg.setRotationPoint(-2F, 8F, 1F);
	      rightleg.setTextureSize(256, 128);
	      rightleg.mirror = true;
	      setRotation(rightleg, -0.2974216F, 0F, 0F);
	      Lwing = new ModelRenderer(this, 0, 107);
	      Lwing.addBox(0F, 0F, 0F, 1, 7, 11);
	      Lwing.setRotationPoint(4F, 1F, -5F);
	      Lwing.setTextureSize(256, 128);
	      Lwing.mirror = true;
	      setRotation(Lwing, 0F, 0F, 0F);
	      Rwing = new ModelRenderer(this, 0, 107);
	      Rwing.addBox(0F, 0F, 0F, 1, 7, 11);
	      Rwing.setRotationPoint(-5F, 1F, -5F);
	      Rwing.setTextureSize(256, 128);
	      Rwing.mirror = true;
	      setRotation(Rwing, 0F, 0F, 0F);
  }
  
  // f = lifetime ticker. Doing Math.toRadians() on it provides a nice smooth wave cycle.
  // f1 = entity velocity
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
  {
	  Ostrich o = (Ostrich)entity;
	  RenderInfo r = o.renderdata;

	  float newangle = 0;
	  float nextangle = 0;
	  //System.out.printf("floats: %f,  %f, %f, %f, %f, %f\n", f, f1, f2, f3, f4, f5);
	  if(f1 > 0.01){
		  newangle = (float) (Math.cos(Math.toRadians(f*9.6f)) * (float)Math.PI * 0.30F * f1);
	  }else{
		  newangle = 0.0F;
	  }

	  if(newangle > 0.75)newangle = 0.75f;
	  if(newangle < -0.75)newangle = -0.75f;

	  leftleg.rotateAngleX = -0.297f + newangle;
	  LLeg1.rotateAngleX = 0.483f + newangle;
	  LLeg2.rotateAngleX = -0.437f + newangle;
	  Lfoot1.rotateAngleX = newangle;
	  Lfoot2.rotateAngleX = newangle;
	  Lfoot3.rotateAngleX = newangle;
	  Lfoot4.rotateAngleX = newangle;
	  LClaw1.rotateAngleX = newangle;
	  LClaw2.rotateAngleX = newangle;
	  LClaw3.rotateAngleX = newangle;
	  LClaw4.rotateAngleX = newangle;

	  rightleg.rotateAngleX = -0.297f - newangle;
	  Rleg1.rotateAngleX = 0.483f - newangle;
	  RLeg2.rotateAngleX = -0.437f - newangle;
	  Rfoot1.rotateAngleX = -newangle;
	  Rfoot2.rotateAngleX = -newangle;
	  Rfoot3.rotateAngleX = -newangle;
	  Rfoot4.rotateAngleX = -newangle;
	  Rclaw1.rotateAngleX = -newangle;
	  Rclaw2.rotateAngleX = -newangle;
	  Rclaw3.rotateAngleX = -newangle;
	  Rclaw4.rotateAngleX = -newangle;

	  Tail1.rotateAngleX = (float) (-0.594f + Math.cos(f * 0.005F) * (float)Math.PI * 0.06F);
	  Tail2.rotateAngleX = Tail1.rotateAngleX;
	  Tail3.rotateAngleX = Tail1.rotateAngleX;

	  Tail3.rotateAngleY = (float) (-0.334f + Math.cos(f * 0.0061F) * (float)Math.PI * 0.08F);
	  Tail2.rotateAngleY = (float) (0.334f - Math.cos(f * 0.0072F) * (float)Math.PI * 0.08F);

	  if(o.getStaying()){
		  f3 = 0;
		  Head1.rotateAngleX = 3.1415f;
		  head.rotateAngleX = Head1.rotateAngleX;
		  mouth1.rotateAngleX = Head1.rotateAngleX;
		  Neck1.rotateAngleX = Head1.rotateAngleX;
		  Hat1.rotateAngleX = Head1.rotateAngleX;
		  Hat2.rotateAngleX = Head1.rotateAngleX;
	  }else{
		  Head1.rotateAngleX = 0.0f;
		  head.rotateAngleX = Head1.rotateAngleX;
		  mouth1.rotateAngleX = Head1.rotateAngleX;
		  Neck1.rotateAngleX = Head1.rotateAngleX;
		  Hat1.rotateAngleX = Head1.rotateAngleX;
		  Hat2.rotateAngleX = Head1.rotateAngleX;

		  //Don't go breaking our necks!
		  if(f2 > 30)f2 = 30;
		  if(f2 < -30)f2 = -30;
		  if(f3 > 45)f3 = 45;
		  if(f3 < -45)f3 = -45;
		  if(f4 > 20)f4 = 20;
		  if(f4 < -20)f4 = -20;

		  Head1.rotateAngleY = (float) -Math.toRadians(f3);
		  head.rotateAngleY = Head1.rotateAngleY;
		  mouth1.rotateAngleY = Head1.rotateAngleY;

	  } 


	  newangle = (float) (Math.cos(f*0.3f) * (float)Math.PI * 0.15F);
	  nextangle = (float) (Math.cos((f+1)*0.3f) * (float)Math.PI * 0.15F);

	  if(nextangle > 0.0F && newangle < 0.0F){
		  //cycle change detected. maybe time to do something?
		  r.ri1 = 0; //reset wings
		  if(o.world.rand.nextInt(4) == 1){
			  r.ri1 = 1; //flap them
		  }
	  }
	  
	  if(o.madtimer != 0 || !o.getOnGround()){
		  r.ri1 = 1;
	  }

	  if(r.ri1 == 0){ 
		  newangle = 0;
	  }
	  newangle = Math.abs(newangle);
	  Lwing.rotateAngleZ = -newangle;
	  Lwing.rotateAngleY = newangle/2;
	  Rwing.rotateAngleZ = newangle;
	  Rwing.rotateAngleY = -newangle/2;


	  Body1.render(deathfactor);
	  body2.render(deathfactor);
	  LLeg1.render(deathfactor);
	  Rleg1.render(deathfactor);
	  LLeg2.render(deathfactor);
	  Lfoot1.render(deathfactor);
	  RLeg2.render(deathfactor);
	  Lfoot2.render(deathfactor);
	  Lfoot3.render(deathfactor);
	  LClaw1.render(deathfactor);
	  LClaw2.render(deathfactor);
	  LClaw3.render(deathfactor);
	  Lfoot4.render(deathfactor);
	  LClaw4.render(deathfactor);
	  Rfoot1.render(deathfactor);
	  Rfoot2.render(deathfactor);
	  Rclaw1.render(deathfactor);
	  Rfoot3.render(deathfactor);
	  Rclaw3.render(deathfactor);
	  Rfoot4.render(deathfactor);
	  Rclaw2.render(deathfactor);
	  Rclaw4.render(deathfactor);
	  Body3.render(deathfactor);
	  Tail1.render(deathfactor);
	  Tail2.render(deathfactor);
	  Tail3.render(deathfactor);
	  Body4.render(deathfactor);
	  //Hat1.render(deathfactor);
	  //Hat2.render(deathfactor);
	  head.render(deathfactor);
	  leftleg.render(deathfactor);
	  Neck1.render(deathfactor);
	  Head1.render(deathfactor);
	  mouth1.render(deathfactor);
	  neck2.render(deathfactor);
	  rightleg.render(deathfactor);
	  Lwing.render(deathfactor);
	  Rwing.render(deathfactor);

  }


}
