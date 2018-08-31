package dangerzone.blocks;


import org.newdawn.slick.opengl.Texture;

import dangerzone.DangerZone;
import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.Utils;
import dangerzone.WorldRenderer;

public class ColoringBlock extends BlockStone {

	public ColoringBlock(String n, String txt) {
		super(n, txt, 5);
		mindamage = 1;
	}
	
	//Player right-clicked on this block
	public boolean rightClickOnBlock(Player p, int dimension, int x, int y, int z){
		if(p == null || p.world.isServer)return false;
		
		InventoryContainer ic = p.getHotbar(p.gethotbarindex());
		if(ic != null && ic.bid == this.blockID)return true; //Placing another one of these!
		

		DangerZone.coloringgui.init(this.blockID, dimension, x, y, z);
		DangerZone.setActiveGui(DangerZone.coloringgui);
		return false; //DO NOT PLACE NEW BLOCK BLOCK we are in a gui now...
	}
	
	public Texture getTexture(int side){
		WorldRenderer.last_texture = -1; //force a reload!
		if(texture == null){
			texture = Utils.initTexture(texturepath); //DYNAMIC! Don't use texturemapper!
		}
		return texture;
	}

}
