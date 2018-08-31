package dangerzone.entities;

import org.newdawn.slick.opengl.Texture;

import dangerzone.DangerZone;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.items.Items;

public class AnotherFish extends BetterFish {
	
	Texture texture2 = null;
	Texture texture3 = null;
	Texture texture4 = null;

	public AnotherFish(World w) {
		super(w);
		uniquename = "DangerZone:Another Fish";
		setBID(-1); //force a change for 0
		setBID(DangerZone.rand.nextInt(4));
	}
	
	public void doDeathDrops(){		
		if(world.rand.nextInt(10)==1)Utils.doDropRand(world, 0, Items.trophyanotherfish.itemID, 1f, dimension, posx, posy, posz);				
		Utils.doDropRand(world, 0, Items.fishmeat.itemID, 1.5f, dimension, posx, posy, posz);		
	}
	
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/Fishtexture2.png");	//this is not fast, so we keep our own pointer!
			texture2 = TextureMapper.getTexture("res/skins/Fishtexture3.png");
			texture3 = TextureMapper.getTexture("res/skins/Fishtexture4.png");
			texture4 = TextureMapper.getTexture("res/skins/Fishtexture5.png");
		}
		if(getBID() == 1)return texture2;
		if(getBID() == 2)return texture3;
		if(getBID() == 3)return texture4;
		return texture;
	}

}
