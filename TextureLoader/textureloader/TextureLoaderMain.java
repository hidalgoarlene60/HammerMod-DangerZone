package textureloader;

//No copyrights here. Use freely.

import dangerzone.BaseMod;
import dangerzone.DangerZone;


public class TextureLoaderMain extends BaseMod {
	
	
	public TextureLoaderMain (){
		
	}
	
	public String getModName()
	{
		return "Custom Texture Pack 1.1";
	}
	
	public String versionBuiltWith()
	{
		return "1.7";
	}
	
	public void registerThings(){		
	
	}
	
	public void postLoadProcessing(){
		DangerZone.alt_texture_paths.add("customtextures/");
	}

}
