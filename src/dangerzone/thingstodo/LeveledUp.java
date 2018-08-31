package dangerzone.thingstodo;



import dangerzone.Player;
import dangerzone.ToDoItem;


public class LeveledUp extends ToDoItem {
	int level = 0;
	
	//passthru...
	public LeveledUp(String uniname, String mytitle, String somehelp, int type, int lvl) {
		super(uniname, mytitle, somehelp, type);
		level = lvl;
	}

	//we just add our check that makes this true, and everything else is done for us!
	public void onLeveled(Player pl, int lvl){
		if(pl == null || truefalse == true)return;
		if(lvl > level){
			truefalse = true;
			notify(pl);
		}
	}


}
