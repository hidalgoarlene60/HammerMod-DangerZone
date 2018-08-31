package dangerzone.thingstodo;



import dangerzone.Player;
import dangerzone.ToDoItem;
import dangerzone.entities.Entity;


public class MobTamer extends ToDoItem {
	
	Class<? extends Entity> objclass;
	
	//passthru...
	public MobTamer(String uniname, String mytitle, String somehelp, int type, Class<? extends Entity> inobjclass) {
		super(uniname, mytitle, somehelp, type);
		objclass = inobjclass;
	}

	//we just add our check that makes this true, and everything else is done for us!
	public void onTamed(Player pl, Entity ent){
		if(ent == null || pl == null || truefalse == true)return;

		Class<? extends Entity> entclass = ent.getClass();		
		if(entclass.getName().equals(objclass.getName())){
			truefalse = true;
			notify(pl);
		}
	}
	

}
