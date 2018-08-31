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

public class ModelLadder extends ModelBase
{

    ModelRenderer side2;
    ModelRenderer side1;
    ModelRenderer side4;
    ModelRenderer side3;
    ModelRenderer rung1b;
    ModelRenderer rung1a;
    ModelRenderer rung2b;
    ModelRenderer rung3b;
    ModelRenderer rung4b;
    ModelRenderer rung2a;
    ModelRenderer rung3a;
    ModelRenderer rung4a;

  
    public ModelLadder()
    {


        side2 = new ModelRenderer(this, 0, 0);
        side2.addBox(0F, 0F, 0F, 2, 8, 1);
        side2.setRotationPoint(-8F, 16F, -6F);
        side2.setTextureSize(16, 16);
        side2.mirror = true;
        setRotation(side2, 0F, 0F, 0F);
        side1 = new ModelRenderer(this, 0, 0);
        side1.addBox(0F, 0F, 0F, 2, 8, 1);
        side1.setRotationPoint(-8F, 8F, -6F);
        side1.setTextureSize(16, 16);
        side1.mirror = true;
        setRotation(side1, 0F, 0F, 0F);
        side4 = new ModelRenderer(this, 7, 0);
        side4.addBox(0F, 0F, 0F, 2, 8, 1);
        side4.setRotationPoint(-8F, 16F, 5F);
        side4.setTextureSize(16, 16);
        side4.mirror = true;
        setRotation(side4, 0F, 0F, 0F);
        side3 = new ModelRenderer(this, 7, 0);
        side3.addBox(0F, 0F, 0F, 2, 8, 1);
        side3.setRotationPoint(-8F, 8F, 5F);
        side3.setTextureSize(16, 16);
        side3.mirror = true;
        setRotation(side3, 0F, 0F, 0F);
        rung1b = new ModelRenderer(this, 4, 10);
        rung1b.addBox(0F, 0F, 0F, 1, 1, 5);
        rung1b.setRotationPoint(-7.5F, 10F, 0F);
        rung1b.setTextureSize(16, 16);
        rung1b.mirror = true;
        setRotation(rung1b, 0F, 0F, 0F);
        rung1a = new ModelRenderer(this, 4, 10);
        rung1a.addBox(0F, 0F, 0F, 1, 1, 5);
        rung1a.setRotationPoint(-7.5F, 10F, -5F);
        rung1a.setTextureSize(16, 16);
        rung1a.mirror = true;
        setRotation(rung1a, 0F, 0F, 0F);
        rung2b = new ModelRenderer(this, 4, 10);
        rung2b.addBox(0F, 0F, 0F, 1, 1, 5);
        rung2b.setRotationPoint(-7.5F, 14F, 0F);
        rung2b.setTextureSize(16, 16);
        rung2b.mirror = true;
        setRotation(rung2b, 0F, 0F, 0F);
        rung3b = new ModelRenderer(this, 4, 10);
        rung3b.addBox(0F, 0F, 0F, 1, 1, 5);
        rung3b.setRotationPoint(-7.5F, 18F, 0F);
        rung3b.setTextureSize(16, 16);
        rung3b.mirror = true;
        setRotation(rung3b, 0F, 0F, 0F);
        rung4b = new ModelRenderer(this, 4, 10);
        rung4b.addBox(0F, 0F, 0F, 1, 1, 5);
        rung4b.setRotationPoint(-7.5F, 22F, 0F);
        rung4b.setTextureSize(16, 16);
        rung4b.mirror = true;
        setRotation(rung4b, 0F, 0F, 0F);
        rung2a = new ModelRenderer(this, 4, 10);
        rung2a.addBox(0F, 0F, 0F, 1, 1, 5);
        rung2a.setRotationPoint(-7.5F, 14F, -5F);
        rung2a.setTextureSize(16, 16);
        rung2a.mirror = true;
        setRotation(rung2a, 0F, 0F, 0F);
        rung3a = new ModelRenderer(this, 4, 10);
        rung3a.addBox(0F, 0F, 0F, 1, 1, 5);
        rung3a.setRotationPoint(-7.5F, 18F, -5F);
        rung3a.setTextureSize(16, 16);
        rung3a.mirror = true;
        setRotation(rung3a, 0F, 0F, 0F);
        rung4a = new ModelRenderer(this, 4, 10);
        rung4a.addBox(0F, 0F, 0F, 1, 1, 5);
        rung4a.setRotationPoint(-7.5F, 22F, -5F);
        rung4a.setTextureSize(16, 16);
        rung4a.mirror = true;
        setRotation(rung4a, 0F, 0F, 0F);
    }
  
 
  
}