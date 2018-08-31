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
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.opengl.Texture;


/*
 * Used so entities don't all hog up the GPU with their own copies of their own textures!
 * This allows multiple entities to share, and doesn't run the graphics system out of memory...
 * Otherwise, every entity will get it's own and I'm not sure anything really cleans up graphics memory...
 * So even after death, the textures would clog up the graphics!
 * Now we can have multiple entities with multiple textures, like girlfriends!
 */
public class TextureMapper {
	
	public static Map<String , Texture> texturemap = null;
	
	public TextureMapper(){
		texturemap = new HashMap<String, Texture>();
	}
	
	public static Texture getTexture(String s){
		Texture t = texturemap.get(s);
		if(t == null){
			t = Utils.initTexture(s);
			if(t != null){
				texturemap.put(s, t);
			}			 
		}
		return t;
	}
	
	//If called early, BEFORE the old texture is used, then releasOld should be TRUE.
	//If in doubt, use releaseOld = FALSE
	//Almost everything is perfectly safe to release if done in your post_load() routine of your mod.
	//Use releaseOld = true if you never want to use that nasty ugly old texture again...
	//THIS IS A ONE-SHOT GLOBAL REPLACE. Go see how the Butterflies work if you want multiple
	//dynamic textures...
	public static void replaceTexture(String s, String newtex, boolean releaseOld){
		if(s.equals(newtex))return;
		Texture oldt = texturemap.get(s);
		if(oldt != null){
			Texture newt = Utils.initTexture(newtex);
			if(newt != null){
				if(releaseOld)oldt.release();
				texturemap.replace(s, newt);
			}			 
		}
	}

}
