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

public class ModelBetterFence extends ModelBase
{

    ModelRenderer post;
    ModelRenderer side1;
    ModelRenderer side2;
    ModelRenderer side3;
    ModelRenderer postA;
    ModelRenderer side1A;

  
    public ModelBetterFence()
    {


        post = new ModelRenderer(this, 0, 0);
        post.addBox(-2F, 0F, -2F, 4, 12, 4);
        post.setRotationPoint(0F, 8F, 0F);
        post.setTextureSize(16, 16);
        post.mirror = true;
        setRotation(post, 0F, 0F, 0F);
        side1 = new ModelRenderer(this, 0, 0);
        side1.addBox(2F, 0F, -1.5F, 4, 4, 3);
        side1.setRotationPoint(0F, 8F, 0F);
        side1.setTextureSize(16, 16);
        side1.mirror = true;
        setRotation(side1, 0F, 0F, 0F);
        side2 = new ModelRenderer(this, 0, 11);
        side2.addBox(2F, 0F, -1F, 6, 2, 2);
        side2.setRotationPoint(0F, 15F, 0F);
        side2.setTextureSize(16, 16);
        side2.mirror = true;
        setRotation(side2, 0F, 0F, 0F);
        side3 = new ModelRenderer(this, 0, 9);
        side3.addBox(2F, 0F, -0.5F, 6, 1, 1);
        side3.setRotationPoint(0F, 20F, 0F);
        side3.setTextureSize(16, 16);
        side3.mirror = true;
        setRotation(side3, 0F, 0F, 0F);
        postA = new ModelRenderer(this, 0, 0);
        postA.addBox(-2F, 0F, -2F, 4, 4, 4);
        postA.setRotationPoint(0F, 20F, 0F);
        postA.setTextureSize(16, 16);
        postA.mirror = true;
        setRotation(postA, 0F, 0F, 0F);
        side1A = new ModelRenderer(this, 0, 0);
        side1A.addBox(6F, 0F, -1.5F, 2, 4, 3);
        side1A.setRotationPoint(0F, 8F, 0F);
        side1A.setTextureSize(16, 16);
        side1A.mirror = true;
        setRotation(side1A, 0F, 0F, 0F);
    }
  
 
  
}