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
public class ParticleLeaves  extends Particle {
	
	public ParticleLeaves(){
		super();
		uniquename = "DangerZone:ParticleLeaves";
		texturepath = "res/misc/particles_break.png";
		brightness = 0.0f;
		maxlifetime = 1200;
		motiony = -0.02f - (DangerZone.rand.nextFloat()*-0.003f);
		motionx /= 2;
		motionx = Math.abs(motionx);
		motionz /= 20;
		scale = 0.20f;
		maxrenderdist = 64;
	}
	
	public void update(float deltaT){
		super.update(deltaT);

		if(DangerZone.player.world.getblock(dimension, (int)posx, (int)posy, (int)posz) != 0){
			maxlifetime = 0; //say bye bye!
		}

	}

}