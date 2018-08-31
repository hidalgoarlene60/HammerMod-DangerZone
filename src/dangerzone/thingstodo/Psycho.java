package dangerzone.thingstodo;


import dangerzone.Player;
import dangerzone.ToDoItem;
import dangerzone.entities.Entity;


public class Psycho extends ToDoItem {

	//passthru...
	public Psycho(String uniname, String mytitle, String somehelp, int type) {
		super(uniname, mytitle, somehelp, type);
	}
	
	//we just add our check that makes this true, and everything else is done for us!
	public void onKilled(Player pl, Entity ent){
		if(ent == null || pl == null || truefalse == true)return;

		if(ent.getOwnerName() != null && !ent.getOwnerName().equals(pl.myname)){
			truefalse = true;
			notify(pl);
		}
	}

}