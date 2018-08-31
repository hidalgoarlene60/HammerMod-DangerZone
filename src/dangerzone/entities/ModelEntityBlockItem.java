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
import org.newdawn.slick.opengl.Texture;


import dangerzone.DangerZone;
import dangerzone.ModelBase;
import dangerzone.blocks.Blocks;
import dangerzone.items.Items;


public class ModelEntityBlockItem extends ModelBase {


	public ModelEntityBlockItem()
	{
		super();		  
	}	  

	/*
	 * rendering for Item/Block when floating around as an entity.
	 */
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
	{
		int bid, iid;
		if(entity instanceof EntityBlockItem){
			bid = ((EntityBlockItem)entity).getBlockID();
			iid = ((EntityBlockItem)entity).getItemID();
			if(bid == 0 && iid == 0){ //try the old way
				bid = entity.getBID();
				iid = entity.getIID();
			}
		}else{
			bid = entity.getBID();
			iid = entity.getIID();
		}

		float spinz = entity.getSpinz();
		
		if(spinz != 0){
			GL11.glPushMatrix();
			GL11.glRotatef(spinz, 0, 0, 1); //we push the matrix and let opengl do z rotation for us! otherwise... massive matrix math...
		}
		if(bid != 0){
			if(Blocks.hasOwnRenderer(bid)){
				Blocks.renderMe(DangerZone.wr, entity.world, entity.dimension, (int)entity.posx, (int)entity.posy, (int)entity.posz, bid, 0, 0xff, false);
			}else{
				DangerZone.wr.drawTexturedCube(0xff, Blocks.isSolidForRender(bid), bid, 0, false);
			}
		}else{
			if(iid != 0){
				//Render Item!
				DangerZone.wr.loadtexture(Items.getTexture(iid));				
				Items.renderMe(DangerZone.wr, entity.world, entity.dimension, (int)entity.posx, (int)entity.posy, (int)entity.posz, iid);						  
			}
		}
		if(spinz != 0){
			GL11.glPopMatrix();
		}
	}

	public Texture getTexture(Entity ent){
		return null;
	}

	public void doScale(Entity ent){
		if(!(ent instanceof EntityBlockItem))return;
		if(((EntityBlockItem)ent).getBlockID() != 0){
			GL11.glScalef(0.25f, 0.25f, 0.25f);
		}else{
			GL11.glScalef(0.45f, 0.45f, 0.45f);
		}
	}


}