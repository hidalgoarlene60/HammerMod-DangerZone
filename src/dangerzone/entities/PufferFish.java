package dangerzone.entities;


import org.newdawn.slick.opengl.Texture;

import dangerzone.DangerZone;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.items.Items;

public class PufferFish extends BetterFish {
	
	Texture texture2 = null;
	Texture texture3 = null;

	public PufferFish(World w) {
		super(w);
		uniquename = "DangerZone:Puffer Fish";
		setBID(-1); //force a change for 0
		setBID(DangerZone.rand.nextInt(3));
	}
	
	public void doDeathDrops(){		
		if(world.rand.nextInt(10)==1)Utils.doDropRand(world, 0, Items.trophypufferfish.itemID, 1f, dimension, posx, posy, posz);	
		if(world.rand.nextInt(5)==1)Utils.doDropRand(world, 0, Items.pufferfishspikes.itemID, 1f, dimension, posx, posy, posz);	
		Utils.doDropRand(world, 0, Items.fishmeat.itemID, 1.5f, dimension, posx, posy, posz);		
	}
	
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/PufferFishtexture.png");	//this is not fast, so we keep our own pointer!
			texture2 = TextureMapper.getTexture("res/skins/PufferFishtexture2.png");
			texture3 = TextureMapper.getTexture("res/skins/PufferFishtexture3.png");
		}
		if(getBID() == 1)return texture2;
		if(getBID() == 2)return texture3;
		return texture;
	}

}
