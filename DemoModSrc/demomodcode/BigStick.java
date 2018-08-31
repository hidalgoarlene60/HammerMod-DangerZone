package demomodcode;


import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import dangerzone.DamageTypes;
import dangerzone.DangerZone;
import dangerzone.InventoryContainer;
import dangerzone.TextureMapper;
import dangerzone.World;
import dangerzone.WorldRenderer;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityExtendedRangeDamage;
import dangerzone.items.ItemSword;

public class BigStick extends ItemSword {
	
	Texture texturebig = null;
	ModelBigStick ma = null;

	public BigStick(String n, String txt, int a, int b) {
		super(n, txt, a, b);
		burntime = 70;
		flipped = true;
	}
	
	public void renderMe(WorldRenderer wr, World w, int d, int x, int y, int z, int bid){
		//texture is already auto-loaded for us
		if(texturebig == null){
			texturebig = TextureMapper.getTexture("demores/items/BigStickTexture.png");
		}
		DangerZone.wr.loadtexture(texturebig);
		if(ma == null){
			ma = new ModelBigStick();
		}
		
		GL11.glRotatef(180, 1, 0, 0);
		GL11.glTranslatef(-5, -25, -1);
		if(ma != null)ma.render();
		
	}
	
	public boolean onLeftClick(Entity holder, Entity clickedon, InventoryContainer ic){
		if(holder == null)return false;
		if(!holder.world.isServer)return true;
		
		EntityExtendedRangeDamage e = (EntityExtendedRangeDamage)holder.world.createEntityByName("DangerZone:ExtendedRangeDamage", 
				holder.dimension, 
				holder.posx+(float)Math.sin(Math.toRadians(holder.rotation_yaw_head))*(holder.getWidth()+1)*(float)Math.cos(Math.toRadians(holder.rotation_pitch_head)),
				holder.posy+(holder.getHeight()*9/10) - (float)Math.sin(Math.toRadians(holder.rotation_pitch_head))*(holder.getWidth()+1),
				holder.posz+(float)Math.cos(Math.toRadians(holder.rotation_yaw_head))*(holder.getWidth()+1)*(float)Math.cos(Math.toRadians(holder.rotation_pitch_head)));
		if(e != null){
			e.init();
			e.setAttackDamage(attackstrength); //damage amount
			e.setDamageType(DamageTypes.SWORD); //damage type
			e.setExplosivePower(0); //explosive power
			e.setRange(8); //range
			e.thrower = holder;
			e.setDirectionAndVelocity(
					(float)Math.sin(Math.toRadians(holder.rotation_yaw_head))*(float)Math.cos(Math.toRadians(holder.rotation_pitch_head)), 
					-(float)Math.sin(Math.toRadians(holder.rotation_pitch_head)),
					(float)Math.cos(Math.toRadians(holder.rotation_yaw_head))*(float)Math.cos(Math.toRadians(holder.rotation_pitch_head)),
					25f, 0.01f);
			
			holder.world.spawnEntityInWorld(e);
		}
		
		return true; //continue with normal left click logic, else it is handled special here
	}
	
	

}
