package dangerzone.entities;


import dangerzone.DangerZone;
import dangerzone.ModelBase;
import dangerzone.blocks.Blocks;

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

public class ModelBlock extends ModelBase
{

	public ModelBlock()
	{

	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
	{
		//draws an ENTITY block
		if(Blocks.hasOwnRenderer(entity.getBID())){
			Blocks.renderMe(DangerZone.wr, entity.world, entity.dimension, 
					(int)entity.posx, (int)entity.posy, (int)entity.posz, entity.getBID(), entity.getIID(), 0xff, false);							
		}else{
			DangerZone.wr.drawTexturedCube(0xff, Blocks.isSolidForRender(entity.getBID()), entity.getBID(), entity.getIID(), false);
		}
	}
	
}
