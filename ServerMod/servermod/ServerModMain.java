package servermod;


import dangerzone.BaseMod;
import dangerzone.ServerHooker;

public class ServerModMain extends BaseMod {
	
	public static MyServerHooks svhook = null;
	
	public ServerModMain (){
		super();
	}
	
	public String getModName()
	{
		return "Server Mod Version 1.1";
	}
	
	public String versionBuiltWith()
	{
		return "1.7";
	}
	
	public void registerThings()
	{	
		svhook = new MyServerHooks();
		ServerHooker.registerServerHooks(svhook);
	}
	

	public void postLoadProcessing(){

	}

}
