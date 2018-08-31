package dangerzone.entities;


import org.newdawn.slick.opengl.Texture;

import dangerzone.DangerZone;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.items.Items;

public class StickFish extends BetterFish {
	
	Texture texture2 = null;

	public StickFish(World w) {
		super(w);
		uniquename = "DangerZone:Stick Fish";
		setBID(-1); //force a change for 0
		setBID(DangerZone.rand.nextInt(2));
	}
	
	public void doDeathDrops(){		
		if(world.rand.nextInt(10)==1)Utils.doDropRand(world, 0, Items.trophystickfish.itemID, 1f, dimension, posx, posy, posz);	
		if(world.rand.nextInt(5)==1)Utils.doDropRand(world, 0, Items.deadstickfish.itemID, 1f, dimension, posx, posy, posz);	
		Utils.doDropRand(world, 0, Items.fishmeat.itemID, 1.5f, dimension, posx, posy, posz);		
	}
	
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/StickFishtexture2.png");	//this is not fast, so we keep our own pointer!
			texture2 = TextureMapper.getTexture("res/skins/StickFishtexture.png");
		}
		if(getBID() == 1)return texture2;
		return texture;
	}

}
