package dangerzone.particles;
import dangerzone.DangerZone;

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
public class ParticleRain  extends Particle {
	
	public ParticleRain(){
		super();
		uniquename = "DangerZone:ParticleRain";
		texturepath = "res/misc/particles_rain.png";
		brightness = 0.0f;
		maxlifetime = 6000;
		//motiony = -0.02f; snow speed!
		motiony = -0.22f;
		scale = 0.30f;
		motionx = 0;
		motionz = 0;
		maxrenderdist = 16;
		rotation_pitch_motion = 0;
		rotation_roll_motion = 0;
		rotation_yaw_motion *= 3;
		rotation_pitch = rotation_yaw = rotation_roll = 0;
	}
	
	public void update(float deltaT){
		
		if(motiony > -0.22f)motiony -= 0.01f;
		
		super.update(deltaT);

		if(DangerZone.player.world.getblock(dimension, (int)posx, (int)posy, (int)posz) != 0){
			int iy = (int)posy;
			float fiy = iy+1.01f;
			for(int i=0;i<2;i++){
				ParticleDroplet pr = (ParticleDroplet) DangerZone.player.world.createParticleByName("DangerZone:ParticleDroplet",
						dimension, posx, fiy, posz);
				if(pr != null){
					DangerZone.player.world.spawnParticleInWorld(pr);
				}
			}
			if(DangerZone.player.world.rand.nextBoolean()){
				String whs = String.format("DangerZone:rain%d", DangerZone.rand.nextInt(8)+1);
				DangerZone.player.world.playSoundCloseClient(whs, dimension, posx, posy, posz, 0.5f, 1+((DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat())*0.2f));
			}
			maxlifetime = 0; //say bye bye!
		}

	}

}