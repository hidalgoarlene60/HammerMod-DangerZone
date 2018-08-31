package dangerzone;
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

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import dangerzone.entities.Entity;
import dangerzone.particles.Particle;



public class ModelBase {
    
	  public ModelBase()
	  {		  
	  }	 
	  
	  //entity, lifetimeticker, velocity, pitch, yaw, roll, deathfactor
	  public void render(Entity entity, float lifetimeticker, float velocity, float headupdown, float headleftright, float headtilt, float deathfactor)
	  {		  
	  }
	  
	  public void renderParticle(Particle p)
	  {		  
	  }
	  
	  public Texture getTexture(Entity ent){
		  if(ent != null)return ent.getTexture();
		  return null;
	  }
	  
	  public Texture getTexture(Particle p){
		  if(p != null)return p.getTexture();
		  return null;
	  }
	  
	  public void doScale(Entity ent){
		  if(ent.getScale() != 1f){
			  GL11.glScalef(ent.getScale(), ent.getScale(), ent.getScale());
		  }
	  }
	  
	  //techne compatibiliy
	  public void setRotation(ModelRenderer mr, float pitch, float yaw, float roll){
		  mr.setRotation(pitch, yaw, roll);
	  }
	  
}
