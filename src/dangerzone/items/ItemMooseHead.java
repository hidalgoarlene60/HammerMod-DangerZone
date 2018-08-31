package dangerzone.items;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import dangerzone.DangerZone;
import dangerzone.ModelRenderer;
import dangerzone.TextureMapper;
import dangerzone.entities.Entity;

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
public class ItemMooseHead extends ItemArmor {
	
	public static MooseHeadModel mhm = null;
	
	public ItemMooseHead(String n, String txt, float protvalue, int durability, int type) {
		super(n, txt, null, null, protvalue, durability, type);
		armortexturepath = "res/skins/"+ "Moosetexture.png"; //same moose texture!
		burntime = 50;
		if(mhm == null)mhm = new MooseHeadModel();
	}
	
	public Texture getArmorTexture(int id){
		if(armortexture == null){
			armortexture = TextureMapper.getTexture(armortexturepath);
		}
		return armortexture;
	}
	
	public Texture getArmorTexture2(int id){
		return null;
	}
	
	//uses a slightly modified moose head model.
	public void drawHelmet(Entity ent, ModelRenderer head, float deathfactor){
		GL11.glPushMatrix(); 
		DangerZone.wr.loadtexture(getArmorTexture(0));
		GL11.glScalef(1.20f, 1.20f, 1.20f);
		GL11.glTranslatef(0f, -11.8f, -1f); //translate down a little because we scaled differently...

		mhm.head.rotateAngleX = head.rotateAngleX + 0.2268f;
		mhm.lant1.rotateAngleX = head.rotateAngleX + 0.3316f;
		mhm.lant2.rotateAngleX = head.rotateAngleX + 0.4014f;
		mhm.lant3.rotateAngleX = head.rotateAngleX - 0.1570f;
		mhm.rant1.rotateAngleX = head.rotateAngleX + 0.3316f;
		mhm.rant2.rotateAngleX = head.rotateAngleX + 0.4014f;
		mhm.rant3.rotateAngleX = head.rotateAngleX - 0.157f;
		
		mhm.head.rotateAngleY = head.rotateAngleY;
		mhm.lant1.rotateAngleY = head.rotateAngleY;
		mhm.lant2.rotateAngleY = head.rotateAngleY;
		mhm.lant3.rotateAngleY = head.rotateAngleY;
		mhm.rant1.rotateAngleY = head.rotateAngleY;
		mhm.rant2.rotateAngleY = head.rotateAngleY;
		mhm.rant3.rotateAngleY = head.rotateAngleY;

		mhm.head.render(deathfactor);
		mhm.lant1.render(deathfactor);
		mhm.lant2.render(deathfactor);
		mhm.lant3.render(deathfactor);
		mhm.rant1.render(deathfactor);
		mhm.rant2.render(deathfactor);
		mhm.rant3.render(deathfactor);
		GL11.glPopMatrix();
	}
	



}