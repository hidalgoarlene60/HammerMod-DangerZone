package dangerzone.items;

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

public class MooseHeadModel extends ModelBase
{

    ModelRenderer head;
    ModelRenderer lant1;
    ModelRenderer lant2;
    ModelRenderer lant3;
    ModelRenderer rant1;
    ModelRenderer rant2;
    ModelRenderer rant3;

  
    public MooseHeadModel()
    {

        
          head = new ModelRenderer(this, 49, 161);
          head.addBox(-4F, -9F, -14F, 8, 9, 19);
          head.setRotationPoint(0F, -9F, 0F);
          head.setTextureSize(128, 256);
          head.mirror = true;
          setRotation(head, 0.2268928F, 0F, 0F);
          lant1 = new ModelRenderer(this, 0, 32);
          lant1.addBox(4.5F, -8F, 0F, 5, 1, 4);
          lant1.setRotationPoint(0F, -9F, 0F);
          lant1.setTextureSize(128, 256);
          lant1.mirror = true;
          setRotation(lant1, 0.3316126F, 0F, -0.1396263F);
          lant2 = new ModelRenderer(this, 29, 0);
          lant2.addBox(9F, -7F, 2F, 11, 1, 27);
          lant2.setRotationPoint(0F, -9F, 0F);
          lant2.setTextureSize(128, 256);
          lant2.mirror = true;
          setRotation(lant2, 0.4014257F, 0.2094395F, -0.1745329F);
          lant3 = new ModelRenderer(this, 0, 47);
          lant3.addBox(5F, -8F, -12F, 1, 1, 8);
          lant3.setRotationPoint(0F, -9F, 0F);
          lant3.setTextureSize(128, 256);
          lant3.mirror = true;
          setRotation(lant3, -0.1570796F, -0.2268928F, 0F);
          rant1 = new ModelRenderer(this, 0, 39);
          rant1.addBox(-9.5F, -8F, 0F, 5, 1, 4);
          rant1.setRotationPoint(0F, -9F, 0F);
          rant1.setTextureSize(128, 256);
          rant1.mirror = true;
          setRotation(rant1, 0.3316126F, 0F, 0.1396263F);
          rant2 = new ModelRenderer(this, 29, 30);
          rant2.addBox(-20F, -7F, 2F, 11, 1, 27);
          rant2.setRotationPoint(0F, -9F, 0F);
          rant2.setTextureSize(128, 256);
          rant2.mirror = true;
          setRotation(rant2, 0.4014257F, -0.2094395F, 0.1745329F);
          rant3 = new ModelRenderer(this, 0, 59);
          rant3.addBox(-6F, -8F, -12F, 1, 1, 8);
          rant3.setRotationPoint(0F, -9F, 0F);
          rant3.setTextureSize(128, 256);
          rant3.mirror = true;
          setRotation(rant3, -0.1570796F, 0.2268928F, 0F);
    }
  
 
  
}