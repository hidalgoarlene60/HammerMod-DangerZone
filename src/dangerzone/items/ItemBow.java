package dangerzone.items;

import org.lwjgl.opengl.GL11;


import dangerzone.GameModes;
import dangerzone.InventoryContainer;
import dangerzone.ItemAttribute;
import dangerzone.Player;
import dangerzone.WorldRenderer;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityArrow;
import dangerzone.gui.InventoryMenus;

public class ItemBow extends Item {

	public ItemBow(String n, String txt, int a) {
		super(n, txt);
		maxuses = a;
		maxstack = 1;
		attackstrength = 5;
		burntime = 15;
		hold_straight = true;
		flopped = true;
		menu = InventoryMenus.HARDWARE;
		this.showInInventory = false;
	}
	
	public boolean rightclickup(Entity holder, InventoryContainer ic, int holdcount){
		//holdcount is roughly 100ths of a second
				
		if(holder.world.isServer){
			EntityArrow e = (EntityArrow)holder.world.createEntityByName("DangerZone:EntityArrow", 
					holder.dimension, 
					holder.posx+(float)Math.sin(Math.toRadians(holder.rotation_yaw_head))*(holder.getWidth()+1.5f)*(float)Math.cos(Math.toRadians(holder.rotation_pitch_head)),
					holder.posy+(holder.getHeight()*9/10) - (float)Math.sin(Math.toRadians(holder.rotation_pitch_head))*(holder.getWidth()+1),
					holder.posz+(float)Math.cos(Math.toRadians(holder.rotation_yaw_head))*(holder.getWidth()+1.5f)*(float)Math.cos(Math.toRadians(holder.rotation_pitch_head)));
			if(e != null){
				int spam = ic.getAttribute(ItemAttribute.SPAM);
				int acc = ic.getAttribute(ItemAttribute.ACCURACY);
				int dmg = ic.getAttribute(ItemAttribute.DAMAGE);
				int rch = ic.getAttribute(ItemAttribute.REACH);
				float held = holdcount+10*spam;
				float acu = 0.01f*acc;
				float dam = 8f + 2*dmg;
				float spd = 0.5f*rch;
				if(held > getfullholdcount())held = getfullholdcount();
				
				e.init();
				e.setBID(0);
				e.setIID(Items.arrow.itemID);
				e.thrower = holder;
				e.setDirectionAndVelocity(
						(float)Math.sin(Math.toRadians(holder.rotation_yaw_head))*(float)Math.cos(Math.toRadians(holder.rotation_pitch_head)), 
						-(float)Math.sin(Math.toRadians(holder.rotation_pitch_head)),
						(float)Math.cos(Math.toRadians(holder.rotation_yaw_head))*(float)Math.cos(Math.toRadians(holder.rotation_pitch_head)),
						1f + (10f*held/getfullholdcount()) + spd, 0.11f - acu);
				e.setAttackDamage(1f + (dam*held/getfullholdcount()));
				holder.world.spawnEntityInWorld(e);
			}
			holder.world.playSound("DangerZone:bow", holder.dimension, holder.posx, holder.posy+holder.getHeight(), holder.posz, 0.55f, 1.0f+((holder.world.rand.nextFloat()-holder.world.rand.nextFloat())*0.3f));
			if(holder instanceof Player){
				if(holder.getGameMode() != GameModes.SURVIVAL){
					return false; //Don't decrement arrows (below), and don't bother with uses either...
				}
			}
			holder.decrementInInventory(Items.arrow.itemID);
			return true; //inc currentuses...
		}
		return false;
		
	}
	

	
	public float getfullholdcount(){
		//for display so we can scale, stretch, fill something, whatever, to show progress.
		return 200;
	}
	
	public void renderMeHeld(WorldRenderer wr, Entity e, int iid, boolean isdisplay){
		if(e == null)return;
		if(!isdisplay){
			GL11.glTranslatef(-6, 2, 0); //don't ask me why, but x, y, and z are all confused...
			GL11.glRotatef(-10f, 0, 0, 1);
		}

		float count = e.getRightButtonDownCount();
		if(count > getfullholdcount())count = getfullholdcount();
		float pct = count/getfullholdcount();

		GL11.glScalef(1.0f+pct, 1.0f+pct, 1.0f); //yeah, I know... it just gets bigger, not stretching.... close enough...
		renderMe( wr,  e.world,  e.dimension, (int)e.posx, (int)e.posy, (int)e.posz,  iid);

	}
	
	public void inventoryTick(Entity holder, InventoryContainer ic, int invindex){
		if(holder.world.isServer){
			if(holder instanceof Player){
				if(!holder.hasInInventory(Items.arrow.itemID)){
					//shit. no easy way to do this...
					InventoryContainer ic2;
					for(int i=0;i<10;i++){
						ic2 = holder.getHotbar(i);
						if(ic2 == ic){
							ic.iid = Items.bow_empty.itemID;
							holder.setHotbarChanged(i);
							return;
						}
					}
					for(int i=0;i<50;i++){
						ic2 = holder.getInventory(i);
						if(ic2 == ic){
							ic.iid = Items.bow_empty.itemID;
							holder.setInventoryChanged(i);
							return;
						}
					}
				}
			}
		}
	}

}
