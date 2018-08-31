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

public class ModelRail extends ModelBase
{

    ModelRenderer railside;
    ModelRenderer railcenter;
    ModelRenderer railpost;
    ModelRenderer railsideA;
    ModelRenderer railsideB;
    ModelRenderer railsideD;
    ModelRenderer railsideC;
    ModelRenderer railcenterA;
    ModelRenderer railpostA;
    ModelRenderer railpostB;
    ModelRenderer railpostC;

  
    public ModelRail()
    {


        railside = new ModelRenderer(this, 0, 0);
        railside.addBox(2F, 0F, -1F, 6, 2, 2);
        railside.setRotationPoint(0F, 20F, 0F);
        railside.setTextureSize(16, 16);
        railside.mirror = true;
        setRotation(railside, 0F, 0F, 0F);
        railcenter = new ModelRenderer(this, 0, 10);
        railcenter.addBox(-2F, 0F, -2F, 4, 2, 4);
        railcenter.setRotationPoint(0F, 20F, 0F);
        railcenter.setTextureSize(16, 16);
        railcenter.mirror = true;
        setRotation(railcenter, 0F, 0F, 0F);
        railpost = new ModelRenderer(this, 0, 5);
        railpost.addBox(-0.5F, 0F, -0.5F, 1, 2, 1);
        railpost.setRotationPoint(0F, 22F, 0F);
        railpost.setTextureSize(16, 16);
        railpost.mirror = true;
        setRotation(railpost, 0F, 0F, 0F);
        railsideA = new ModelRenderer(this, 0, 0);
        railsideA.addBox(-10F, -3F, -1F, 4, 2, 2);
        railsideA.setRotationPoint(0F, 16F, 0F);
        railsideA.setTextureSize(16, 16);
        railsideA.mirror = true;
        setRotation(railsideA, 0F, 0F, -0.7853982F);
        railsideB = new ModelRenderer(this, 0, 0);
        railsideB.addBox(-6F, -3F, -1F, 4, 2, 2);
        railsideB.setRotationPoint(0F, 16F, 0F);
        railsideB.setTextureSize(16, 16);
        railsideB.mirror = true;
        setRotation(railsideB, 0F, 0F, -0.7853982F);
        railsideD = new ModelRenderer(this, 0, 0);
        railsideD.addBox(8F, -3F, -1F, 6, 2, 2);
        railsideD.setRotationPoint(0F, 16F, 0F);
        railsideD.setTextureSize(16, 16);
        railsideD.mirror = true;
        setRotation(railsideD, 0F, 0F, -0.7853982F);
        railsideC = new ModelRenderer(this, 0, 0);
        railsideC.addBox(2F, -3F, -1F, 6, 2, 2);
        railsideC.setRotationPoint(0F, 16F, 0F);
        railsideC.setTextureSize(16, 16);
        railsideC.mirror = true;
        setRotation(railsideC, 0F, 0F, -0.7853982F);
        railcenterA = new ModelRenderer(this, 0, 10);
        railcenterA.addBox(-2F, -3F, -2F, 4, 2, 4);
        railcenterA.setRotationPoint(0F, 16F, 0F);
        railcenterA.setTextureSize(16, 16);
        railcenterA.mirror = true;
        setRotation(railcenterA, 0F, 0F, -0.7853982F);
        railpostA = new ModelRenderer(this, 0, 3);
        railpostA.addBox(-0.5F, -2F, -0.5F, 1, 10, 1);
        railpostA.setRotationPoint(0F, 16F, 0F);
        railpostA.setTextureSize(16, 16);
        railpostA.mirror = true;
        setRotation(railpostA, 0F, 0F, 0F);
        railpostB = new ModelRenderer(this, 0, 5);
        railpostB.addBox(-2F, -0.5F, -0.5F, 7, 1, 1);
        railpostB.setRotationPoint(0F, 16F, 0F);
        railpostB.setTextureSize(16, 16);
        railpostB.mirror = true;
        setRotation(railpostB, 0F, 0F, 0F);
        railpostC = new ModelRenderer(this, 0, 5);
        railpostC.addBox(5F, -0.5F, -0.5F, 3, 1, 1);
        railpostC.setRotationPoint(0F, 16F, 0F);
        railpostC.setTextureSize(16, 16);
        railpostC.mirror = true;
        setRotation(railpostC, 0F, 0F, 0F);
    }
}
  
 
  
