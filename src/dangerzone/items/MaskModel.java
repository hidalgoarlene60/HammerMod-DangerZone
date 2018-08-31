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

public class MaskModel extends ModelBase
{

    ModelRenderer masktop;
    ModelRenderer maskbot;
    ModelRenderer maskright;
    ModelRenderer maskleft;
    ModelRenderer mouthpiece;
    ModelRenderer lline1;
    ModelRenderer rline1;
    ModelRenderer lline2;
    ModelRenderer rline2;
    
    ModelRenderer ltank;
    ModelRenderer rtank;

  
    public MaskModel()
    {

        
        masktop = new ModelRenderer(this, 39, 19);
        masktop.addBox(-5F, -6F, -5F, 10, 1, 1);
        masktop.setRotationPoint(0F, 0F, 0F);
        masktop.setTextureSize(64, 32);
        masktop.mirror = true;
        setRotation(masktop, 0F, 0F, 0F);
        maskbot = new ModelRenderer(this, 39, 26);
        maskbot.addBox(-5F, -3F, -5F, 10, 1, 1);
        maskbot.setRotationPoint(0F, 0F, 0F);
        maskbot.setTextureSize(64, 32);
        maskbot.mirror = true;
        setRotation(maskbot, 0F, 0F, 0F);
        maskright = new ModelRenderer(this, 57, 22);
        maskright.addBox(-5F, -5F, -5F, 1, 2, 1);
        maskright.setRotationPoint(0F, 0F, 0F);
        maskright.setTextureSize(64, 32);
        maskright.mirror = true;
        setRotation(maskright, 0F, 0F, 0F);
        maskleft = new ModelRenderer(this, 39, 22);
        maskleft.addBox(4F, -5F, -5F, 1, 2, 1);
        maskleft.setRotationPoint(0F, 0F, 0F);
        maskleft.setTextureSize(64, 32);
        maskleft.mirror = true;
        setRotation(maskleft, 0F, 0F, 0F);
        mouthpiece = new ModelRenderer(this, 24, 19);
        mouthpiece.addBox(-1.5F, -2F, -7F, 3, 3, 3);
        mouthpiece.setRotationPoint(0F, 0F, 0F);
        mouthpiece.setTextureSize(64, 32);
        mouthpiece.mirror = true;
        setRotation(mouthpiece, 0F, 0F, 0F);
        lline1 = new ModelRenderer(this, 6, 26);
        lline1.addBox(1.5F, -1F, -6F, 3, 1, 1);
        lline1.setRotationPoint(0F, 0F, 0F);
        lline1.setTextureSize(64, 32);
        lline1.mirror = true;
        setRotation(lline1, 0F, 0F, 0F);
        rline1 = new ModelRenderer(this, 16, 26);
        rline1.addBox(-4.5F, -1F, -6F, 3, 1, 1);
        rline1.setRotationPoint(0F, 0F, 0F);
        rline1.setTextureSize(64, 32);
        rline1.mirror = true;
        setRotation(rline1, 0F, 0F, 0F);
        lline2 = new ModelRenderer(this, 0, 12);
        lline2.addBox(4.5F, -1F, -6F, 1, 1, 10);
        lline2.setRotationPoint(0F, 0F, 0F);
        lline2.setTextureSize(64, 32);
        lline2.mirror = true;
        setRotation(lline2, 0F, 0F, 0F);
        rline2 = new ModelRenderer(this, 0, 0);
        rline2.addBox(-5.5F, -1F, -6F, 1, 1, 10);
        rline2.setRotationPoint(0F, 0F, 0F);
        rline2.setTextureSize(64, 32);
        rline2.mirror = true;
        setRotation(rline2, 0F, 0F, 0F);
        
        masktop.connectPart(maskbot);
        masktop.connectPart(maskright);
        masktop.connectPart(maskleft);
        masktop.connectPart(lline1);
        masktop.connectPart(lline2);
        masktop.connectPart(rline1);
        masktop.connectPart(rline2);
        masktop.connectPart(mouthpiece);
        masktop.finishConnect();
        
        ltank = new ModelRenderer(this, 22, 0);
        ltank.addBox(0.5F, 0F, 2F, 5, 13, 5);
        ltank.setRotationPoint(0F, 0F, 0F);
        ltank.setTextureSize(64, 32);
        ltank.mirror = true;
        setRotation(ltank, 0F, 0F, 0F);
        rtank = new ModelRenderer(this, 43, 0);
        rtank.addBox(-5.5F, 0F, 2F, 5, 13, 5);
        rtank.setRotationPoint(0F, 0F, 0F);
        rtank.setTextureSize(64, 32);
        rtank.mirror = true;
        setRotation(rtank, 0F, 0F, 0F);
    }
}
 
  
