package dangerzone.items;
import org.lwjgl.opengl.GL11;

import dangerzone.InventoryContainer;
import dangerzone.World;
import dangerzone.WorldRenderer;
import dangerzone.entities.Entity;
import dangerzone.entities.ThrownInstability;

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
public class ItemInstability extends Item {

	public float mypower = 1f;
	
	public ItemInstability(String n, String txt, float power) {
		super(n, txt);
		burntime = 120;
		mypower = power;
	}
	
	public boolean onRightClick(Entity holder, Entity clickedon, InventoryContainer ic){
		if(clickedon != null)return false;
		if(holder == null)return false; //error!
		
		//Make a throwable instability entity!
		if(holder.world.isServer){
			ThrownInstability e = (ThrownInstability)holder.world.createEntityByName("DangerZone:ThrownInstability", 
					holder.dimension, 
					holder.posx+(float)Math.sin(Math.toRadians(holder.rotation_yaw_head))*(holder.getWidth()+1)*(float)Math.cos(Math.toRadians(holder.rotation_pitch_head)),
					holder.posy+(holder.getHeight()*9/10) - (float)Math.sin(Math.toRadians(holder.rotation_pitch_head))*(holder.getWidth()+1),
					holder.posz+(float)Math.cos(Math.toRadians(holder.rotation_yaw_head))*(holder.getWidth()+1)*(float)Math.cos(Math.toRadians(holder.rotation_pitch_head)));
			if(e != null){
				e.init();
				e.maxrenderdist = 64;
				e.setBID(ic.bid);
				e.setIID(ic.iid);
				e.thrower = holder;
				e.explosive_power = mypower;
				e.setDirectionAndVelocity(
						(float)Math.sin(Math.toRadians(holder.rotation_yaw_head))*(float)Math.cos(Math.toRadians(holder.rotation_pitch_head)), 
						-(float)Math.sin(Math.toRadians(holder.rotation_pitch_head)),
						(float)Math.cos(Math.toRadians(holder.rotation_yaw_head))*(float)Math.cos(Math.toRadians(holder.rotation_pitch_head)),
						6f, 0.05f);
				
				holder.world.spawnEntityInWorld(e);
			}
			holder.world.playSound("DangerZone:bow", holder.dimension, holder.posx, holder.posy+holder.getHeight(), holder.posz, 0.5f, 1.0f+((holder.world.rand.nextFloat()-holder.world.rand.nextFloat())*0.3f));
			return true; //delete me please!
		}
		
		return false; 
	}
	
	public void renderMe(WorldRenderer wr, World w, int d, int x, int y, int z, int bid){
		GL11.glPushMatrix();
		float rot = (float) (w.rand.nextFloat()*360);
		GL11.glRotatef(rot, 0, 0, 1);
		super.renderMe(wr, w, d, x, y, z, bid);
		GL11.glPopMatrix();
	}

}
