package dangerzone.items;
import org.newdawn.slick.opengl.Texture;

import dangerzone.DangerZone;
import dangerzone.InventoryContainer;
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
public class ItemMask extends ItemArmor {
	
	public static MaskModel mhm = null;
	
	public ItemMask(String n, String txt, float protvalue, int durability, int type) {
		super(n, txt, null, null, protvalue, durability, type);
		armortexturepath = "res/skins/"+ "MaskAndTanktexture.png";
		burntime = 5;
		maxstack = 1;
		if(mhm == null)mhm = new MaskModel();
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
	
	public void armorTick(Entity e, InventoryContainer ic, int armorindex){	
		if(e == null)return;
		InventoryContainer ia = e.getArmor(1);
		if(ia != null){
			if(ia.iid == Items.scubatanks.itemID){
				e.setAir(e.getMaxAir());
				if(e.world.rand.nextInt(150) == 5 && e.getInLiquid()){
					e.world.playSound(bubbleSound(), e.dimension, e.posx, e.posy, e.posz, 0.5f, 1);
				}
			}
		}
	}
	
	public String bubbleSound(){
		int which = DangerZone.rand.nextInt(4);
		if(which == 0)return "DangerZone:martian_living1";
		if(which == 1)return "DangerZone:martian_living2";
		if(which == 2)return "DangerZone:martian_living3";
		return "DangerZone:martian_living4";
	}
	
	public void drawHelmet(Entity ent, ModelRenderer head, float deathfactor){
		//GL11.glPushMatrix(); 
		DangerZone.wr.loadtexture(getArmorTexture(0));
		//GL11.glScalef(1.20f, 1.20f, 1.20f);
		//GL11.glTranslatef(0f, -11.8f, -1f); //translate down a little because we scaled differently...

		mhm.masktop.rotateAngleX = head.rotateAngleX;
		
		mhm.masktop.rotateAngleY = head.rotateAngleY;

		mhm.masktop.render(deathfactor);

		//GL11.glPopMatrix();
	}
	



}