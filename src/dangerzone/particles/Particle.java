package dangerzone.particles;
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
import org.newdawn.slick.opengl.Texture;

import dangerzone.DangerZone;
import dangerzone.ModelBase;
import dangerzone.TextureMapper;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entity;

/*
 * Small little short-lived things that are handled in their own thread solely on the client.
 * There is no collision or other interactions.
 * They are automatically removed and freed when their lifetimeticker >= maxlifetime.
 * If you don't like the defaults, override them!
 * Each "particle" is an 8x8 square. Four to a standard 16x16 texture.
 */

public class Particle {
	public double posx;
	public double posy;
	public double posz;
	public int dimension;
	public float rotation_yaw;
	public float rotation_pitch;
	public float rotation_roll;
	public float motionx;
	public float motiony;
	public float motionz;
	public int maxrenderdist = 32; //In blocks
	public String uniquename;
	public long lifetimeticker;
	public long maxlifetime = 30;	//default ticks
	public float rotation_pitch_motion;
	public float rotation_yaw_motion;
	public float rotation_roll_motion;
	public String texturepath = null;
	public int tex = 0; //which sub-texture this particle is.
	public float brightness = 0;
	public float scale = 0.25f;
	public int bid = 0; //for break block...
	
	//Draw me!
	public ModelBase model;
	Texture texture = null; //holds 4 little particles, all related.
	
	public Particle(){
		uniquename = null;  //set this in your constructor
		texturepath = null;  //set this in your constructor too! 
		posx = 0;
		posy = 0;
		posz = 0;
		rotation_yaw = 0;
		rotation_pitch = 0;
		rotation_roll = 0;
		dimension = 0;
		lifetimeticker = 0;
		brightness = 0;
				
		//default motion
		motionx = (DangerZone.rand.nextFloat() - DangerZone.rand.nextFloat())*0.05f;
		motiony = (DangerZone.rand.nextFloat() - DangerZone.rand.nextFloat())*0.05f;
		motionz = (DangerZone.rand.nextFloat() - DangerZone.rand.nextFloat())*0.05f;
		rotation_yaw = DangerZone.rand.nextInt(360);
		rotation_pitch = DangerZone.rand.nextInt(360);
		rotation_roll = DangerZone.rand.nextInt(360);
		rotation_yaw_motion = (DangerZone.rand.nextFloat() - DangerZone.rand.nextFloat())*10f;
		rotation_pitch_motion = (DangerZone.rand.nextFloat() - DangerZone.rand.nextFloat())*10f;
		rotation_roll_motion = (DangerZone.rand.nextFloat() - DangerZone.rand.nextFloat())*10f;
		
		//4 textures per image
		tex = DangerZone.rand.nextInt(4);
		maxlifetime = 25 + DangerZone.rand.nextInt(10);
	};
	
		
	/*
	 * Adds motion
	 */
	public void update(float deltaT){

		lifetimeticker++; //countdown to disappearance!

		posy += motiony*deltaT;
		posx += motionx*deltaT;
		posz += motionz*deltaT;
		rotation_pitch += rotation_pitch_motion*deltaT;
		rotation_yaw += rotation_yaw_motion*deltaT;
		rotation_roll += rotation_roll_motion*deltaT;

		while(rotation_yaw < 0)rotation_yaw += 360f;
		rotation_yaw %= 360f;
		while(rotation_pitch < 0)rotation_pitch += 360f;
		rotation_pitch %= 360f;
		while(rotation_roll < 0)rotation_roll += 360f;
		rotation_roll %= 360f;	

	}
	
	public Texture getTexture(){
		if(texture == null){
			if(bid == 0){
				//PARTICLES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
				texture = TextureMapper.getTexture(texturepath);	//this is not fast, so we keep our own pointer!
			}else{
				texture = Blocks.getTexture(bid);
			}
		}
		return texture;
	}
	
	public int getSubTexture(){
		return tex;
	}
	
	public float getBrightness(){
		return brightness;
	}
	
	public double getDistanceFromEntity(Entity p){
		double d1, d2, d3;
		d1 = p.posx - this.posx;
		d2 = p.posy - this.posy;
		d3 = p.posz - this.posz;
		return (float)Math.sqrt((d1*d1)+(d2*d2)+(d3*d3));
	}
	

}
