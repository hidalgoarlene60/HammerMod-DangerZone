package dangerzone.items;


import dangerzone.Effects;
import dangerzone.InventoryContainer;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityLiving;
import dangerzone.items.ItemSword;

public class ItemPufferFishSpikes extends ItemSword {
	
	
	public ItemPufferFishSpikes(String name, String tx, int uses, int damage){
		super(name, tx, uses, damage);
	}
	
	public boolean onLeftClick(Entity holder, Entity clickedon, InventoryContainer ic){
		if(holder != null && clickedon != null && holder.world.isServer){
			if(clickedon instanceof EntityLiving){
				Effects ef = new Effects(Effects.POISON, 0.2f, 200);
				clickedon.addEffectFromServer(ef);
			}
		}
		return true;
	}

}