package dangerzone.entities;
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
import java.util.Comparator;


public class GenericTargetSorter implements Comparator<Object>
{
    private Entity baseEntity;

    public GenericTargetSorter(Entity par2Entity)
    {
        this.baseEntity = par2Entity;
    }

    public int compareDistanceSq(Entity par1Entity, Entity par2Entity)
    {
    	double weight = 0;
        double var3 = this.baseEntity.getDistanceFromEntity(par1Entity);
        
        
        weight = par1Entity.getHeight() * par1Entity.getWidth();
        if(weight > 1.0)var3 = var3/weight; //bigger = closer!
        	
        double var5 = this.baseEntity.getDistanceFromEntity(par2Entity);
       
        
        weight = par2Entity.getHeight() * par2Entity.getWidth();
        if(weight > 1.0)var5 = var5/weight; //bigger = closer!
        
        return var3 < var5 ? -1 : (var3 > var5 ? 1 : 0);
    }

    public int compare(Object par1Obj, Object par2Obj)
    {
        return this.compareDistanceSq((Entity)par1Obj, (Entity)par2Obj);
    }
}