package dangerzone.items;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import dangerzone.DangerZone;
import dangerzone.InventoryContainer;
import dangerzone.ModelRenderer;
import dangerzone.TextureMapper;
import dangerzone.entities.Entity;
import dangerzone.gui.InventoryMenus;

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
public class ItemArmor extends Item {
	
	 // 0 = helmet, 1 = chestplate, 2 = leggings, 3 = boots
	
	public float protection = 0.0f; //added to players defense when attacked
	public Texture armortexture = null;
	public String armortexturepath = null;
	public Texture armortexture2 = null; //leggings
	public String armortexturepath2 = null; //leggings
	public int armortype = 0;

	public ItemArmor(String n, String txt, String armortxt, String leggingstxt, float protvalue, int durability, int type) {
		super(n, txt);
		armortype = type; // 0 = helmet, 1 = chestplate, 2 = leggings, 3 = boots
		armortexturepath = armortxt;
		armortexturepath2 = leggingstxt;
		protection = protvalue;
		maxuses = durability;
		maxstack = 1;
		burntime = 0;
		menu = InventoryMenus.HARDWARE;
	}
	
	public void armorTick(Entity e, InventoryContainer ic, int armorindex){		
		//called when being worn, 10x a sec...
		//this is DIFFERENT from Item.inUseTick().
	}
	
	public Texture getArmorTexture(int id){
		if(armortexture == null){
			armortexture = TextureMapper.getTexture(armortexturepath);
		}
		return armortexture;
	}
	
	public Texture getArmorTexture2(int id){
		if(armortexture2 == null){
			armortexture2 = TextureMapper.getTexture(armortexturepath2);
		}
		return armortexture2;
	}
	
	//defaults just scale the original humanoid parts up a little and draw over them.
	public void drawHelmet(Entity ent, ModelRenderer head, float deathfactor){
		  DangerZone.wr.loadtexture(getArmorTexture(0));
		  GL11.glTranslatef(0f, -3.0f, 0f); //translate down a little because we will scale differently...
		  GL11.glScalef(1.125f, 1.125f, 1.125f); //scale up
		  head.rotationPointY = 0.5f; //rotate off center a little
		  head.render(deathfactor); //texture is in same place in the file!
		  head.rotationPointY = 0.0f; //restore this!!!!!
		  GL11.glScalef(0.88889f, 0.88889f, 0.88889f); //unscale
		  GL11.glTranslatef(0f, 3.0f, 0f); //translate back!!!
	}
	
	public void drawLeggings(Entity ent, ModelRenderer body, ModelRenderer leftleg, ModelRenderer rightleg, float deathfactor){
		  GL11.glPushMatrix(); 
		  DangerZone.wr.loadtexture(getArmorTexture2(0)); //2!
		  GL11.glScalef(1.10f, 1.10f, 1.10f);
		  GL11.glTranslatef(0f, -1.1f, 0f);
		  GL11.glTranslatef(-0.18f, 0f, 0f);
		  leftleg.render(deathfactor);
		  GL11.glTranslatef(0.36f, 0f, 0f);			  
		  rightleg.render(deathfactor);
		  GL11.glPopMatrix();
		  //around the waist!
		  GL11.glPushMatrix(); 
		  GL11.glScalef(1.10f, 1.10f, 1.10f);
		  GL11.glTranslatef(0f, -1.8f, 0f); //translate down a little because we scaled differently...
		  body.render(deathfactor);
		  GL11.glPopMatrix();
	}
	
	public void drawChestplate(Entity ent, ModelRenderer body, ModelRenderer leftarm, ModelRenderer rightarm, float deathfactor){
		  GL11.glPushMatrix(); 
		  DangerZone.wr.loadtexture(getArmorTexture(0));
		  GL11.glScalef(1.15f, 1.15f, 1.15f);
		  GL11.glTranslatef(0f, -2.9f, 0f); //translate down a little because we scaled differently...
		  body.render(deathfactor);
		  GL11.glTranslatef(-0.6f, 0f, 0f);
		  leftarm.render(deathfactor);
		  GL11.glTranslatef(1.2f, 0f, 0f);
		  rightarm.render(deathfactor);
		  GL11.glPopMatrix();
	}
	
	public void drawBoots(Entity ent, ModelRenderer leftleg, ModelRenderer rightleg, float deathfactor){
		  GL11.glPushMatrix(); 
		  DangerZone.wr.loadtexture(getArmorTexture(0));
		  GL11.glScalef(1.15f, 1.15f, 1.15f);
		  GL11.glTranslatef(0f, -1.3f, 0f); //translate down a little because we scaled differently...
		  GL11.glTranslatef(-0.18f, 0f, 0f);
		  leftleg.render(deathfactor);
		  GL11.glTranslatef(0.36f, 0f, 0f);
		  rightleg.render(deathfactor);
		  GL11.glPopMatrix();
	}

}
