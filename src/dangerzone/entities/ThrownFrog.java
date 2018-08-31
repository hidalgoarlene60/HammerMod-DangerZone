package dangerzone.entities;

import dangerzone.DangerZone;
import dangerzone.Effects;
import dangerzone.Player;
import dangerzone.ToDoList;
import dangerzone.World;

public class ThrownFrog extends ThrownBlockItem {

	public ThrownFrog(World w) {
		super(w);
		uniquename = "DangerZone:ThrownFrog";
	}
	
	public void doHitSomething(boolean hb, double x, double y, double z, boolean he, Entity ent){
		boolean doit = false;
		if(he){
			if(ent != null){
				if(ent instanceof EntityLiving){
					doit = true; //don't ask...
				}
			}
		}
		if(doit){
			Effects ef = null;
			for(int i=0;i<effect_list.size();i++){
				ef = effect_list.get(i);
				ent.addEffect(ef);
				if(ent instanceof Player){
					Player p = (Player)ent;
					p.server_thread.sendNewEffect(ef);
					ToDoList.onAffected(p, ef.effect, ef.duration, ef.amplitude);
				}
			}
		}else{
			//change back into an item/block
			EntityBlockItem ee = (EntityBlockItem)world.createEntityByName(DangerZone.blockitemname, dimension, posx+motionx, posy+motiony, posz+motionz);
			if(ee != null){
				ee.fill(this.getBID(), this.getIID(), 1);					
				world.spawnEntityInWorld(ee);
			}
		}
		world.playSound("DangerZone:little_splat", dimension, posx+motionx, posy+motiony, posz+motionz, 0.5f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
	}

}
