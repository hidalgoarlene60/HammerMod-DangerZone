package dangerzone.entities;


import dangerzone.Explosion;
import dangerzone.World;

public class EntityExplosion extends Entity {
		
	public Entity thrower = null;
	
	public EntityExplosion(World w){
		super(w);
		maxrenderdist = 1; //in blocks
		this.height = 0.0f;
		this.width = 0.0f;
		uniquename = "DangerZone:Explosion";
		canthitme = true; //Ignore me!
		setBID(0); //default true, do entity damage and fire
	}	

	public void update( float deltaT){
				
		if(this.world.isServer){
			if(thrower == null)thrower = this;
			Explosion.boom(thrower, world, dimension, (int)posx, (int)posy, (int)posz, (int)getAttackDamage(), getBID()==0?true:false);
			this.deadflag = true;
		}
		super.update( deltaT );
	}
	
}
