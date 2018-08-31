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


import dangerzone.entities.EntityLiving;


public class Swarm {
	
	//check to see if it is time for a swarm of something...
	public static void doSpawnSwarm(){
		
		//now? now? now?
		if(DangerZone.rand.nextInt(20000) != 1)return;
		
		//find someone to mess with...
		Player p = DangerZone.server.getRandomPlayer(DangerZone.rand);
		if(p == null)return;
		
		if(p.dimension == Dimensions.overworlddimension.dimensionID || p.dimension == Dimensions.bigroundtreedimension.dimensionID){
			//Bullet Bats only spawn in daytime... because why not...
			if(!DangerZone.server_world.isDaytime())return;
			
			//have player, in daytime, let's spawn some bats!
			double aroundx = p.posx - 160;
			double aroundz = p.posz - 160;
			int tryx, tryz;
			int swarmsize = 120 + DangerZone.rand.nextInt(100);
			int y;
			//System.out.printf("Spawn %d bats\n", swarmsize);
			for(int i=0;i<swarmsize;i++){

				tryx = (int)aroundx + DangerZone.rand.nextInt(10) - DangerZone.rand.nextInt(10);
				tryz = (int)aroundz + DangerZone.rand.nextInt(10) - DangerZone.rand.nextInt(10);

				for(y=255; y>10; y--){
					if(DangerZone.server_world.getblock(p.dimension, tryx, y, tryz) != 0)break;
				}

				EntityLiving b = (EntityLiving)DangerZone.server_world.createEntityByName("DangerZone:Bullet Bat", p.dimension, tryx, y+5, tryz);
				if(b != null){
					b.init();
					b.setSwarming(true); //set swarm mode
					DangerZone.server_world.spawnEntityInWorld(b);
				}

			}
			p.server_thread.sendChatToPlayer("What is that sound.....?");
		}
		
		if(p.dimension == Dimensions.pathwaydimension.dimensionID){

			if(!DangerZone.server_world.isDaytime())return;
			
			//have player, in daytime, let's spawn some Geese!
			double aroundx = p.posx - 160;
			double aroundz = p.posz - 160;
			int tryx, tryz;
			int swarmsize = 100 + DangerZone.rand.nextInt(80);
			int y;
	
			for(int i=0;i<swarmsize;i++){

				tryx = (int)aroundx + DangerZone.rand.nextInt(10) - DangerZone.rand.nextInt(10);
				tryz = (int)aroundz + DangerZone.rand.nextInt(10) - DangerZone.rand.nextInt(10);

				for(y=255; y>10; y--){
					if(DangerZone.server_world.getblock(p.dimension, tryx, y, tryz) != 0)break;
				}

				EntityLiving b = (EntityLiving)DangerZone.server_world.createEntityByName("DangerZone:Goose", p.dimension, tryx, y+10, tryz);
				if(b != null){
					b.init();
					b.setSwarming(true); //set swarm mode
					DangerZone.server_world.spawnEntityInWorld(b);
				}

			}
			p.server_thread.sendChatToPlayer("Do you hear something.....?");
		}
		
		if(p.dimension == Dimensions.ruggedhillsdimension.dimensionID){

			if(DangerZone.server_world.isDaytime())return;
			
			//have player, at night... be brutal...
			double aroundx = p.posx - 80; //closer when ground-based. Kick up speed a little in the entity too.
			double aroundz = p.posz - 80;
			int tryx, tryz;
			int swarmsize = 100 + DangerZone.rand.nextInt(80);
			int y;
	
			for(int i=0;i<swarmsize;i++){

				tryx = (int)aroundx + DangerZone.rand.nextInt(10) - DangerZone.rand.nextInt(10);
				tryz = (int)aroundz + DangerZone.rand.nextInt(10) - DangerZone.rand.nextInt(10);

				for(y=255; y>10; y--){
					if(DangerZone.server_world.getblock(p.dimension, tryx, y, tryz) != 0)break;
				}

				EntityLiving b = (EntityLiving)DangerZone.server_world.createEntityByName("DangerZone:Snarler", p.dimension, tryx, y+1, tryz);
				if(b != null){
					b.init();
					b.setSwarming(true); //set swarm mode
					DangerZone.server_world.spawnEntityInWorld(b);
				}

			}
			p.server_thread.sendChatToPlayer("Oh no. Oh no... no... no... no...");
		}
		
	}

}
