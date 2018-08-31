package dangerzone.entities;

import java.util.List;
import java.util.ListIterator;

import dangerzone.DamageTypes;
import dangerzone.DangerZone;
import dangerzone.Explosion;
import dangerzone.World;

public class EntityLightning extends Entity {
	
	public int countdown = 0;
	public int myranddata[];
	public int nextrand = 0;
	public Entity thrower = null;

	public EntityLightning(World w) {
		super(w);
		uniquename = "DangerZone:Lightning";
		ignoreCollisions = true;
		width = 0.25f;
		height = 0.25f;
		setAttackDamage(0); //you can set this to whatever after you e.init() and before spawning into world!
	}
	
	public void init(){
		nextrand = 0;
		myranddata = new int[100];
		for(int i=0;i<100;i++){
			myranddata[i] = DangerZone.rand.nextInt(1000);
		}
	}
	
	public void resetrand(){
		nextrand = 0;
	}
	
	public int getnextrand(int inrange){
		nextrand++;
		return (myranddata[nextrand%100]%inrange);
	}
	
	
	public void update(float deltaT){
		motionx = motiony = motionz = 0;
		if(world.isServer){
			countdown++;
			if(countdown == 1){
				String thunder = String.format("DangerZone:thunder%d", world.rand.nextInt(4)+1);
				world.playSound(thunder, dimension, posx, posy, posz, 7.3f, 1.0f);
				List<Entity> nearby_list = null;
				ListIterator<Entity> li;
				Entity enthit = null;
				float edist;
				//Get a list of entities within reach of largest mob expected because we may hit their hitbox!
				nearby_list = DangerZone.server.entityManager.findEntitiesInRange(16.0f, dimension, posx, posy, posz);
				li = nearby_list.listIterator();
				while(li.hasNext()){
					enthit = (Entity)li.next();
					if(enthit != this && !enthit.canthitme && enthit instanceof EntityLiving){ //don't hit self or things that shouldn't be hit!
						if(enthit.posy+enthit.getHeight() > posy-3 && enthit.posy < posy+100){
							edist = (float) enthit.getHorizontalDistanceFromEntity(posx, posz); //Center of tip to center of entity
							edist -= getWidth()/2; //width of me
							edist -= (enthit.getWidth()/2); //general hitbox of entity
							if(edist < 3){ //zap anything within 3 blocks
								//Hit something!
								enthit.doAttackFrom(this, DamageTypes.LIGHTNING, 75f);
							}
						}
					}
				}				
			}
			if(countdown >= 10){
				if((int)getAttackDamage() > 0){
					if(thrower == null)thrower = this;
					Explosion.boom(thrower, world, dimension, posx, posy, posz, (int)getAttackDamage(), true);
				}
				this.deadflag = true;
			}
		}
		super.update(deltaT);
	}

}
