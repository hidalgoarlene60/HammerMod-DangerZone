package dangerzone.thingstodo;



import dangerzone.Player;
import dangerzone.ToDoItem;


public class SpellCast extends ToDoItem {
	
	//passthru...
	public SpellCast(String uniname, String mytitle, String somehelp, int type) {
		super(uniname, mytitle, somehelp, type);
	}

	//we just add our check that makes this true, and everything else is done for us!
	public void onSpellCast(Player pl, int type, float power){
		if(pl == null || truefalse == true)return;
		if(type <= 0 || power <= 0)return;
		truefalse = true;
		notify(pl);
	}


}
