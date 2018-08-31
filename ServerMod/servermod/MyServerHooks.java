package servermod;

import dangerzone.Player;
import dangerzone.ServerHooks;

public class MyServerHooks extends ServerHooks {
		
	public boolean player_always_connect(Player p){
		if(p.myname.toLowerCase().equals("danger"))return true;//connect even when too many on server
		if(p.myname.toLowerCase().equals("theycallmedanger"))return true;//connect even when too many on server
		return false; 
	}

}
