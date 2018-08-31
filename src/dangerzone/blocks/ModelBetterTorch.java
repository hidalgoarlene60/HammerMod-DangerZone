package dangerzone.blocks;

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

public class ModelBetterTorch extends ModelBase
{

	ModelRenderer t1base;
	ModelRenderer t1flame;
	ModelRenderer t2base1;
	ModelRenderer t2base2;
	ModelRenderer t2base3;
	ModelRenderer t2flame;
	ModelRenderer t3base;
	ModelRenderer t3base2;
	ModelRenderer t3flame;

  
    public ModelBetterTorch()
    {


    	t1base = new ModelRenderer(this, 0, 0);
    	t1base.addBox(-0.5F, 0F, -0.5F, 1, 8, 1);
    	t1base.setRotationPoint(0F, 16F, 0F);
    	t1base.setTextureSize(16, 16);
    	t1base.mirror = true;
    	setRotation(t1base, 0F, 0F, 0F);
    	t1flame = new ModelRenderer(this, 4, 0);
    	t1flame.addBox(-1F, 0F, -1F, 2, 4, 2);
    	t1flame.setRotationPoint(0F, 12F, 0F);
    	t1flame.setTextureSize(16, 16);
    	t1flame.mirror = true;
    	setRotation(t1flame, 0F, 0F, 0F);
    	t2base1 = new ModelRenderer(this, 0, 10);
    	t2base1.addBox(0F, 0F, -1.5F, 1, 3, 3);
    	t2base1.setRotationPoint(-8F, 19F, 0F);
    	t2base1.setTextureSize(16, 16);
    	t2base1.mirror = true;
    	setRotation(t2base1, 0F, 0F, 0F);
    	t2base2 = new ModelRenderer(this, 4, 6);
    	t2base2.addBox(0F, 0F, -0.5F, 2, 1, 1);
    	t2base2.setRotationPoint(-7F, 20F, 0F);
    	t2base2.setTextureSize(16, 16);
    	t2base2.mirror = true;
    	setRotation(t2base2, 0F, 0F, 0F);
    	t2base3 = new ModelRenderer(this, 0, 0);
    	t2base3.addBox(0F, 0F, -0.5F, 1, 3, 1);
    	t2base3.setRotationPoint(-5F, 18F, 0F);
    	t2base3.setTextureSize(16, 16);
    	t2base3.mirror = true;
    	setRotation(t2base3, 0F, 0F, 0F);
    	t2flame = new ModelRenderer(this, 4, 0);
    	t2flame.addBox(-1.5F, 0F, -1F, 2, 4, 2);
    	t2flame.setRotationPoint(-4F, 14F, 0F);
    	t2flame.setTextureSize(16, 16);
    	t2flame.mirror = true;
    	setRotation(t2flame, 0F, 0F, 0F);
    	t3base = new ModelRenderer(this, 0, 12);
    	t3base.addBox(-1.5F, 0F, -1.5F, 3, 1, 3);
    	t3base.setRotationPoint(0F, 8F, 0F);
    	t3base.setTextureSize(16, 16);
    	t3base.mirror = true;
    	setRotation(t3base, 0F, 0F, 0F);
    	t3base2 = new ModelRenderer(this, 0, 0);
    	t3base2.addBox(-0.5F, 0F, -0.5F, 1, 2, 1);
    	t3base2.setRotationPoint(0F, 9F, 0F);
    	t3base2.setTextureSize(16, 16);
    	t3base2.mirror = true;
    	setRotation(t3base2, 0F, 0F, 0F);
    	t3flame = new ModelRenderer(this, 4, 0);
    	t3flame.addBox(-1F, 0F, -1F, 2, 2, 2);
    	t3flame.setRotationPoint(0F, 11F, 0F);
    	t3flame.setTextureSize(16, 16);
    	t3flame.mirror = true;
    	setRotation(t3flame, 0F, 0F, 0F);
    }
  
 
  
}