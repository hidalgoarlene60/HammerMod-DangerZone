package demomodcode;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import dangerzone.DangerZone;
import dangerzone.ModelRenderer;
import dangerzone.TextureMapper;
import dangerzone.entities.Entity;
import dangerzone.items.ItemArmor;


public class Beanie extends ItemArmor {
	
	public static ModelBeanie mhm = null;
	
	public Beanie(String n, String txt, float protvalue, int durability, int type) {
		super(n, txt, null, null, protvalue, durability, type);
		armortexturepath = "demores/items/"+ "BeanieTexture.png"; //same moose texture!
		burntime = 5;
		if(mhm == null)mhm = new ModelBeanie();
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

		mhm.beaniebase.rotateAngleX = mhm.beanieaxel.rotateAngleX = mhm.beanieprop.rotateAngleX = head.rotateAngleX;
		mhm.beaniebase.rotateAngleY = mhm.beanieaxel.rotateAngleY = mhm.beanieprop.rotateAngleY = head.rotateAngleY;
		mhm.beaniebase.rotateAngleZ = mhm.beanieaxel.rotateAngleZ = mhm.beanieprop.rotateAngleZ = head.rotateAngleZ;
		
		mhm.beanieprop.rotateAngleY = (float) Math.toRadians((ent.lifetimeticker*15)%360);
		
		mhm.beaniebase.render(1);
		mhm.beanieaxel.render(1);
		
		GL11.glTranslatef( 0, 24f, 0);	//undo Techne offset			
		GL11.glRotatef((float)Math.toDegrees(mhm.beanieprop.rotateAngleZ), 0, 0, 1);
		GL11.glRotatef((float)Math.toDegrees(mhm.beanieprop.rotateAngleX), 1, 0, 0);		
		GL11.glPushMatrix();
		
		GL11.glRotatef((float)Math.toDegrees(mhm.beanieprop.rotateAngleY), 0, 1, 0);
		GL11.glTranslatef( mhm.beanieprop.offsetX, -mhm.beanieprop.offsetY, mhm.beanieprop.offsetZ+1f); //negative Y because Techne is really upside down
		mhm.beanieprop.renderRaw();	
		
		GL11.glPopMatrix();		
		GL11.glPopMatrix();
	}
}
	



