package dangerzone.entities;

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

import dangerzone.DangerZone;
import dangerzone.ModelBase;


public class ModelLightning extends ModelBase {


	public ModelLightning()
	{

	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
	{
		EntityLightning li = (EntityLightning)entity;
		
		//make it flash
		if(li.world != null && li.world.rand.nextBoolean())return;
		if(li.world == null && DangerZone.rand.nextBoolean())return;
		
		//reset our saved random numbers
		li.resetrand();
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		double dist = DangerZone.player.getHorizontalDistanceFromEntity(li);
		int howmany = 1;
		int wid = 10;
		if(li.getnextrand(4) == 2)howmany = 2;
		if(dist > 16)wid = 9;
		if(dist > 32)wid = 8;
		if(dist > 64)wid = 7;
		if(dist > 128)wid = 6;
		for(int i=0;i<howmany;i++){
			doline(li, 0, 0, 0, 200, wid);
		}
		GL11.glDisable(GL11.GL_BLEND);		
		GL11.glEnable(GL11.GL_TEXTURE_2D);	

	}
	
	//draw and split generally upwards
	private void doline(EntityLightning li, float x, float y, float z, float height, float width){
		if(width <= 1 || height <= 5)return;
		int nx = li.getnextrand(50) - li.getnextrand(50);
		int nz = li.getnextrand(50) - li.getnextrand(50);
		//draw self
		GL11.glLineWidth(width);		
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glColor3f(1f, 1f, 1f);
		GL11.glVertex3f(x, y, z);
		GL11.glVertex3f(x+nx, y + height, z+nz);
		GL11.glEnd();
		int howmany = 1;
		if(li.getnextrand(4) == 2)howmany = 2;
		for(int i=0;i<howmany;i++){
			doline(li, x+nx, y+height, z+nz, height-li.getnextrand(5), width-(0.5f*(howmany+1)));
		}				
	}

	//meh. 
	public void doScale(Entity ent){
		GL11.glScalef(1.125f, 1.125f, 1.125f); 
	}

}
