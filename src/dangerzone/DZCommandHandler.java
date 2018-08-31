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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import dangerzone.blocks.Blocks;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityCloud;
import dangerzone.entities.EntityFire;
import dangerzone.entities.EntityLiving;
import dangerzone.entities.EntityVolcano;
import dangerzone.entities.Temperament;
import dangerzone.entities.EntityBlockItem;
import dangerzone.items.Items;



public class DZCommandHandler extends CommandHandler {
	
	public boolean doCommand(Player pl, String cmd){
		String s;
		StringTokenizer st = new StringTokenizer(cmd);

		if(!pl.world.isServer)return false; //DZ does everything on the server.

		//TODO FIXME - some day we should record commands to a log file...
		
		//strip off playername
		s = st.nextToken();
		if(st.hasMoreTokens()){
			s = st.nextToken();
		}else{
			return true; //error in command. pretend we handled it!
		}
		
		if(s.equalsIgnoreCase("time")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0 && (pl.player_privs & PlayerPrivs.TIME) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){
				s = st.nextToken();
				if(s.equalsIgnoreCase("set")){
					if(st.hasMoreTokens()){
						s = st.nextToken();
						if(s.equalsIgnoreCase("day")){
							DangerZone.server_world.timetimer = DangerZone.server_world.lengthOfDay/4; //noon!
							DangerZone.server.sendTimeToAll(DangerZone.server_world.timetimer, DangerZone.server_world.lengthOfDay);
							return true;

						}else if(s.equalsIgnoreCase("night")){
							DangerZone.server_world.timetimer = DangerZone.server_world.lengthOfDay*3/4; //midnight!
							DangerZone.server.sendTimeToAll(DangerZone.server_world.timetimer, DangerZone.server_world.lengthOfDay);
							return true;

						}else if(s.equalsIgnoreCase("dawn")){
							DangerZone.server_world.timetimer = 0; 									//morning
							DangerZone.server.sendTimeToAll(DangerZone.server_world.timetimer, DangerZone.server_world.lengthOfDay);
							return true;

						}else if(s.equalsIgnoreCase("dusk")){
							DangerZone.server_world.timetimer = DangerZone.server_world.lengthOfDay/2; //evening!
							DangerZone.server.sendTimeToAll(DangerZone.server_world.timetimer, DangerZone.server_world.lengthOfDay);
							return true;

						}else{
							int newtime;
							try{
								newtime = Integer.parseInt(s);
							}catch(NumberFormatException e){
								newtime = -1;
							}
							if(newtime >= 0 && newtime <= DangerZone.server_world.lengthOfDay){
								DangerZone.server_world.timetimer = newtime;
								DangerZone.server.sendTimeToAll(DangerZone.server_world.timetimer, DangerZone.server_world.lengthOfDay);
								return true;
							}else{
								return errOther(pl, " Error: invalid time");
							}
						}
					}
				}
			}
			return errInvalidCmd(pl);
		}else if(s.equalsIgnoreCase("sethome")){
			pl.home_dimension = pl.dimension;
			pl.home_x = pl.posx;
			pl.home_y = pl.posy;
			pl.home_z = pl.posz;
			DangerZone.server.saveThisPlayer(pl);
			return true;
		}else if(s.equalsIgnoreCase("home")){
			Utils.doTeleport(pl, pl.home_dimension, pl.home_x, pl.home_y, pl.home_z);
			return true;
		}else if(s.equalsIgnoreCase("spawn")){
			Utils.doTeleport(pl, 1, 10000+(DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat())*64f, 100, 10000+(DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat())*64f);
			return true;
		}else if(s.equalsIgnoreCase("tp") || s.equalsIgnoreCase("teleport") ){
			int newx, newy, newz;
			newx=newy=newz=0;
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0 && (pl.player_privs & PlayerPrivs.TELEPORT) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){				
				s = st.nextToken();
				Entity e = DangerZone.server.entityManager.findPlayerByName(s);
				if(e != null){						
					if(!st.hasMoreTokens()){
						Utils.doTeleport(pl, e.dimension, e.posx, e.posy, e.posz);		
						return true;
					}else{
						s = st.nextToken();
						Entity ee = DangerZone.server.entityManager.findPlayerByName(s);
						if(ee != null){
							if((pl.player_privs & PlayerPrivs.OPERATOR) == 0)return errPermissionDenied(pl); //requires op to tp OTHER players
							Utils.doTeleport((Player)e, ee.dimension, ee.posx, ee.posy, ee.posz);		
							return true;				
						}else{
							if((pl.player_privs & PlayerPrivs.OPERATOR) == 0)return errPermissionDenied(pl); //requires op to tp OTHER players
							try{
								newx = Integer.parseInt(s);
							}catch(NumberFormatException ex){
								newx = 0;
							}
							if(st.hasMoreTokens()){
								s = st.nextToken();
								try{
									newy = Integer.parseInt(s);
								}catch(NumberFormatException ex){
									newy = 0;
								}
							}
							if(st.hasMoreTokens()){
								s = st.nextToken();
								try{
									newz = Integer.parseInt(s);
								}catch(NumberFormatException ex){
									newz = 0;
								}
							}
							if(newx > 0 && newy > 0 && newy < 256 && newz > 0){
								Utils.doTeleport((Player)e, e.dimension, (double)newx, (double)newy, (double)newz);		
								return true;
							}else{
								return errOther(pl, " Error: invalid coodinates");
							}
						}
					}
				}
				
				//try for coordinates
				try{
					newx = Integer.parseInt(s);
				}catch(NumberFormatException ex){
					newx = 0;
				}
				if(st.hasMoreTokens()){
					s = st.nextToken();
					try{
						newy = Integer.parseInt(s);
					}catch(NumberFormatException ex){
						newy = 0;
					}
				}
				if(st.hasMoreTokens()){
					s = st.nextToken();
					try{
						newz = Integer.parseInt(s);
					}catch(NumberFormatException ex){
						newz = 0;
					}
				}
				if(newx > 0 && newy > 0 && newy < 256 && newz > 0){
					Utils.doTeleport(pl, pl.dimension, (double)newx, (double)newy, (double)newz);		
					return true;
				}else{
					return errOther(pl, " Error: invalid coodinates");
				}
			}
			return errInvalidCmd(pl);
		}else if(s.equalsIgnoreCase("stop") || s.equalsIgnoreCase("shutdown") ){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0 && (pl.player_privs & PlayerPrivs.SHUTDOWN) == 0)return errPermissionDenied(pl);
			DangerZone.gameover = 1;
			return true;
			
		}else if(s.equalsIgnoreCase("rebuild")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){
				s = st.nextToken();
				if(s.equalsIgnoreCase("chunk")){
					DangerZone.server_chunk_cache.rebuildChunk(pl.dimension, (int)pl.posx, 100, (int)pl.posz);
					return true;
				}else if(s.equalsIgnoreCase("area")){
					//9*9 chunk area!
					for(int i=-4;i<=4;i++){
						for(int j=-4;j<=4;j++){
							DangerZone.server_chunk_cache.rebuildChunk(pl.dimension, (int)pl.posx+(i*16), 100, (int)pl.posz+(j*16));
						}
					}
					return true;					
				}				
			}
			return errInvalidCmd(pl);

		}else if(s.equalsIgnoreCase("show")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0 && (pl.player_privs & PlayerPrivs.MISC) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){
				s = st.nextToken();
				if(s.equalsIgnoreCase("eid")){
					int i;
					for(i=1;i<DangerZone.max_entities;i++){
						Entity e = DangerZone.server.entityManager.findEntityByID(i);
						if(e != null && e.dimension == pl.dimension){
							e.setPetName(String.format("%d", i));
							return true;
						}
					}
				}else{
					int i;
					for(i=1;i<DangerZone.max_entities;i++){
						Entity e = DangerZone.server.entityManager.findEntityByID(i);
						if(e != null && e.dimension == pl.dimension){
							e.setPetName(null);
							return true;
						}
					}
				}
			}
			return errInvalidCmd(pl);
		}else if(s.equalsIgnoreCase("weather")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0 && (pl.player_privs & PlayerPrivs.WEATHER) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){
				s = st.nextToken();
				if(s.equalsIgnoreCase("clear")){
					int i;
					for(i=1;i<DangerZone.max_entities;i++){
						Entity e = DangerZone.server.entityManager.findEntityByID(i);
						if(e != null && e.dimension == pl.dimension){
							if(e instanceof EntityCloud){
								if(e.getBID() == Blocks.cloud_thunder.blockID || e.getBID() == Blocks.cloud_rain.blockID){
									e.deadflag = true;
								}
							}
						}
					}
					return true;
				}else{
					return errOther(pl, " Error: unknown weather");
				}
			}	
			return errInvalidCmd(pl);
		}else if(s.equalsIgnoreCase("kill")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0 && (pl.player_privs & PlayerPrivs.KILL) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){
				s = st.nextToken();
				if(s.equalsIgnoreCase("all")){
					int i;
					for(i=1;i<DangerZone.max_entities;i++){
						Entity e = DangerZone.server.entityManager.findEntityByID(i);
						if(e != null && pl.getDistanceFromEntity(e) < 256){
							if(!(e instanceof Player) && e instanceof EntityLiving){
								if(e.getOwnerName() == null){
									e.deadflag = true;
								}
							}
						}
					}
					return true;
				}else if(s.equalsIgnoreCase("hostile")){
					int i;
					for(i=1;i<DangerZone.max_entities;i++){
						Entity e = DangerZone.server.entityManager.findEntityByID(i);
						if(e != null && pl.getDistanceFromEntity(e) < 256){
							if(e instanceof EntityLiving && e.temperament == Temperament.HOSTILE && !(e instanceof Player)){
								if(e.getOwnerName() == null){
									e.deadflag = true;
								}
							}								
						}
					}
					return true;
				}else if(s.equalsIgnoreCase("items")){
					int i;
					for(i=1;i<DangerZone.max_entities;i++){
						Entity e = DangerZone.server.entityManager.findEntityByID(i);
						if(e != null && pl.getDistanceFromEntity(e) < 256){
							if(e instanceof EntityBlockItem){
								e.deadflag = true;
							}								
						}
					}
					return true;
				}else if(s.equalsIgnoreCase("pets")){
					int i;
					for(i=1;i<DangerZone.max_entities;i++){
						Entity e = DangerZone.server.entityManager.findEntityByID(i);
						if(e != null && pl.getDistanceFromEntity(e) < 256){
							if(!(e instanceof Player) && e instanceof EntityLiving){
								if(e.getOwnerName() != null){
									e.deadflag = true;
								}
							}
						}
					}
					return true;
				}else if(s.equalsIgnoreCase("volcanoes")){
					int i;
					for(i=1;i<DangerZone.max_entities;i++){
						Entity e = DangerZone.server.entityManager.findEntityByID(i);
						if(e != null && pl.getDistanceFromEntity(e) < 256){
							if(e instanceof EntityVolcano){
								e.deadflag = true;
							}								
						}
					}
					return true;
				}else{
					Entity e = DangerZone.server.entityManager.findPlayerByName(s);
					if(e != null){
						Player tlp = (Player)e;
						tlp.setGameMode(GameModes.LIMBO);
						tlp.server_thread.fatal_error = 1;
						return true;
					}else{
						return errOther(pl, " Error: don't know how to kill that!");
					}
				}
			}
			return errInvalidCmd(pl);
		}else if(s.equalsIgnoreCase("nofire")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0 && (pl.player_privs & PlayerPrivs.NOFIRE) == 0)return errPermissionDenied(pl);		
			int i;
			for(i=1;i<DangerZone.max_entities;i++){
				Entity e = DangerZone.server.entityManager.findEntityByID(i);
				if(e != null && pl.getDistanceFromEntity(e) < 256){
					if(!(e instanceof Player) && e instanceof EntityFire){
						e.deadflag = true;
					}
				}
			}
			return true;
		}else if(s.equalsIgnoreCase("op")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){
				s = st.nextToken();
				Entity e = DangerZone.server.entityManager.findPlayerByName(s);
				if(e != null){
					Player plyr = (Player)e;
					plyr.player_privs = 0xffffffff;
					DangerZone.server.save_privs(plyr.myname, plyr.player_privs);
					return true;
				}else{
					return errOther(pl, " Error: player not found");
				}
			}
			return errInvalidCmd(pl);
		}else if(s.equalsIgnoreCase("deop")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){
				s = st.nextToken();
				Entity e = DangerZone.server.entityManager.findPlayerByName(s);
				if(e != null){
					Player plyr = (Player)e;
					plyr.player_privs = 0x0;
					DangerZone.server.save_privs(plyr.myname, plyr.player_privs);
					return true;
				}else{
					return errOther(pl, " Error: player not found");
				}
			}
			return errInvalidCmd(pl);
		}else if(s.equalsIgnoreCase("givepriv")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){
				s = st.nextToken();
				Entity e = DangerZone.server.entityManager.findPlayerByName(s);
				if(e != null){					
					int newpriv = 0;
					if(st.hasMoreTokens()){
						s = st.nextToken();
						if(s.equalsIgnoreCase("gamemode"))newpriv = PlayerPrivs.GAMEMODE;
						if(s.equalsIgnoreCase("op"))newpriv = PlayerPrivs.OPERATOR;
						if(s.equalsIgnoreCase("teleport"))newpriv = PlayerPrivs.TELEPORT;
						if(s.equalsIgnoreCase("kill"))newpriv = PlayerPrivs.KILL;
						if(s.equalsIgnoreCase("weather"))newpriv = PlayerPrivs.WEATHER;
						if(s.equalsIgnoreCase("misc"))newpriv = PlayerPrivs.MISC;
						if(s.equalsIgnoreCase("time"))newpriv = PlayerPrivs.TIME;
						if(s.equalsIgnoreCase("shutdown"))newpriv = PlayerPrivs.SHUTDOWN;
						if(s.equalsIgnoreCase("nofire"))newpriv = PlayerPrivs.NOFIRE;
						if(s.equalsIgnoreCase("chat"))newpriv = PlayerPrivs.CHAT;
					}
										
					Player plyr = (Player)e;
					plyr.player_privs |= newpriv;
					DangerZone.server.save_privs(plyr.myname, plyr.player_privs);
					return true;
				}else{
					return errOther(pl, " Error: player not found");
				}
			}
			return errInvalidCmd(pl);
		}else if(s.equalsIgnoreCase("takepriv")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){
				s = st.nextToken();
				Entity e = DangerZone.server.entityManager.findPlayerByName(s);
				if(e != null){					
					int newpriv = 0;
					if(st.hasMoreTokens()){
						s = st.nextToken();
						if(s.equalsIgnoreCase("gamemode"))newpriv = PlayerPrivs.GAMEMODE;
						if(s.equalsIgnoreCase("op"))newpriv = PlayerPrivs.OPERATOR;
						if(s.equalsIgnoreCase("teleport"))newpriv = PlayerPrivs.TELEPORT;
						if(s.equalsIgnoreCase("kill"))newpriv = PlayerPrivs.KILL;
						if(s.equalsIgnoreCase("weather"))newpriv = PlayerPrivs.WEATHER;
						if(s.equalsIgnoreCase("misc"))newpriv = PlayerPrivs.MISC;
						if(s.equalsIgnoreCase("time"))newpriv = PlayerPrivs.TIME;
						if(s.equalsIgnoreCase("shutdown"))newpriv = PlayerPrivs.SHUTDOWN;
						if(s.equalsIgnoreCase("nofire"))newpriv = PlayerPrivs.NOFIRE;
						if(s.equalsIgnoreCase("chat"))newpriv = PlayerPrivs.CHAT;
					}
										
					Player plyr = (Player)e;
					plyr.player_privs &= ~newpriv;
					DangerZone.server.save_privs(plyr.myname, plyr.player_privs);
					return true;
				}else{
					return errOther(pl, " Error: player not found");
				}
			}
			return errInvalidCmd(pl);
		}else if(s.equalsIgnoreCase("cavegeneration")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){
				s = st.nextToken();
				if(s.equalsIgnoreCase("true")){
					DangerZone.generatecaves = true;
				}else{
					DangerZone.generatecaves = false;
				}
			}else{
				String outstring = String.format("CaveGeneration = %s", DangerZone.generatecaves?"true":"false");
				pl.server_thread.sendCommandToPlayer(outstring);
			}
			return true;
		}else if(s.equalsIgnoreCase("playnicely")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){
				s = st.nextToken();
				if(s.equalsIgnoreCase("true")){
					DangerZone.playnicely = true;
				}else{
					DangerZone.playnicely = false;
				}
			}else{
				String outstring = String.format("PlayNicely = %s", DangerZone.playnicely?"true":"false");
				pl.server_thread.sendCommandToPlayer(outstring);
			}
			return true;
		}else if(s.equalsIgnoreCase("firedamage")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){
				s = st.nextToken();
				if(s.equalsIgnoreCase("true")){
					DangerZone.firedamage = true;
				}else{
					DangerZone.firedamage = false;
				}
			}else{
				String outstring = String.format("FireDamage = %s", DangerZone.firedamage?"true":"false");
				pl.server_thread.sendCommandToPlayer(outstring);
			}
			return true;
		}else if(s.equalsIgnoreCase("petprotection")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){
				s = st.nextToken();
				if(s.equalsIgnoreCase("true")){
					DangerZone.petprotection = true;
				}else{
					DangerZone.petprotection = false;
				}
			}else{
				String outstring = String.format("PetProtection = %s", DangerZone.petprotection?"true":"false");
				pl.server_thread.sendCommandToPlayer(outstring);
			}
			return true;
		}else if(s.equalsIgnoreCase("freezeworld")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){
				s = st.nextToken();
				if(s.equalsIgnoreCase("true")){
					DangerZone.freeze_world = true;
				}else{
					DangerZone.freeze_world = false;
				}
			}else{
				String outstring = String.format("FreezeWorld = %s", DangerZone.freeze_world?"true":"false");
				pl.server_thread.sendCommandToPlayer(outstring);
			}
			return true;
		}else if(s.equalsIgnoreCase("private_server")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){
				s = st.nextToken();
				if(s.equalsIgnoreCase("true")){
					DangerZone.private_server = true;
				}else{
					DangerZone.private_server = false;
				}
			}else{
				String outstring = String.format("PrivateServer = %s", DangerZone.private_server?"true":"false");
				pl.server_thread.sendCommandToPlayer(outstring);
			}
			return true;
		}else if(s.equalsIgnoreCase("validateplayers")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){
				s = st.nextToken();
				if(s.equalsIgnoreCase("true")){
					DangerZone.require_valid_passwords = true;
				}else{
					DangerZone.require_valid_passwords = false;
				}
			}else{
				String outstring = String.format("ValidatePlayers = %s", DangerZone.require_valid_passwords?"true":"false");
				pl.server_thread.sendCommandToPlayer(outstring);
			}
			return true;
		}else if(s.equalsIgnoreCase("allowanonymous")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){
				s = st.nextToken();
				if(s.equalsIgnoreCase("true")){
					DangerZone.allow_anonymous = true;
				}else{
					DangerZone.allow_anonymous = false;
				}
			}else{
				String outstring = String.format("Allow Anonymous Players = %s", DangerZone.allow_anonymous?"true":"false");
				pl.server_thread.sendCommandToPlayer(outstring);
			}
			return true;
		}else if(s.equalsIgnoreCase("defaultprivs")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){	
				int newpriv = 0;
				while(st.hasMoreTokens()){
					s = st.nextToken();
					if(s.equalsIgnoreCase("gamemode"))newpriv |= PlayerPrivs.GAMEMODE;
					if(s.equalsIgnoreCase("op"))newpriv |= PlayerPrivs.OPERATOR;
					if(s.equalsIgnoreCase("teleport"))newpriv |= PlayerPrivs.TELEPORT;
					if(s.equalsIgnoreCase("kill"))newpriv |= PlayerPrivs.KILL;
					if(s.equalsIgnoreCase("weather"))newpriv |= PlayerPrivs.WEATHER;
					if(s.equalsIgnoreCase("misc"))newpriv |= PlayerPrivs.MISC;
					if(s.equalsIgnoreCase("time"))newpriv |= PlayerPrivs.TIME;
					if(s.equalsIgnoreCase("shutdown"))newpriv |= PlayerPrivs.SHUTDOWN;
					if(s.equalsIgnoreCase("nofire"))newpriv |= PlayerPrivs.NOFIRE;
					if(s.equalsIgnoreCase("chat"))newpriv |= PlayerPrivs.CHAT;
				}
				DangerZone.default_privs = newpriv;
			}else{
				String outstring = "  DefaultPrivs = ";
				if((DangerZone.default_privs & PlayerPrivs.GAMEMODE) != 0)outstring += "GameMode ";
				if((DangerZone.default_privs & PlayerPrivs.OPERATOR) != 0)outstring += "OP ";
				if((DangerZone.default_privs & PlayerPrivs.TELEPORT) != 0)outstring += "Teleport ";
				if((DangerZone.default_privs & PlayerPrivs.KILL) != 0)outstring += "Kill ";
				if((DangerZone.default_privs & PlayerPrivs.WEATHER) != 0)outstring += "Weather ";
				if((DangerZone.default_privs & PlayerPrivs.MISC) != 0)outstring += "Misc ";
				if((DangerZone.default_privs & PlayerPrivs.TIME) != 0)outstring += "Time ";
				if((DangerZone.default_privs & PlayerPrivs.SHUTDOWN) != 0)outstring += "Shutdown ";
				if((DangerZone.default_privs & PlayerPrivs.NOFIRE) != 0)outstring += "NoFire ";
				if((DangerZone.default_privs & PlayerPrivs.CHAT) != 0)outstring += "Chat ";
				pl.server_thread.sendCommandToPlayer(outstring);			
			}
			return true;
		}else if(s.equalsIgnoreCase("maxplayers")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){
				s = st.nextToken();
				int newmaxplayers;
				try{
					newmaxplayers = Integer.parseInt(s);
				}catch(NumberFormatException e){
					newmaxplayers = 10;
				}
				if(newmaxplayers < 2 || newmaxplayers > 255)newmaxplayers = 10;
				DangerZone.max_players_on_server = newmaxplayers;
			}else{
				String outstring = String.format("MaxPlayers = %d", DangerZone.max_players_on_server);
				pl.server_thread.sendCommandToPlayer(outstring);
			}
			return true;
		}else if(s.equalsIgnoreCase("chunkowner")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){
				List<String> newowners = null;
				s = st.nextToken();
				if(!s.equals("null")){
					newowners = new ArrayList<String>();
					newowners.add(s);
					while(st.hasMoreTokens()){
						s = st.nextToken();
						newowners.add(s);
					}
				}
				DangerZone.server_chunk_cache.setChunkOwners(pl.dimension, (int)pl.posx, (int)pl.posy, (int)pl.posz, newowners);			
			}else{
				List<String> owners = DangerZone.server_chunk_cache.getChunkOwners(pl.dimension, (int)pl.posx, (int)pl.posy, (int)pl.posz);
				if(owners != null){
					String outstring = "Chunk Owners: ";
					int i = 0;
					int ilen = owners.size();
					while(ilen > 0){
						outstring += owners.get(i);
						outstring += ", ";
						ilen--;
						i++;
					}
					pl.server_thread.sendCommandToPlayer(outstring);
				}else{
					pl.server_thread.sendCommandToPlayer("no owners");
				}
			}
			return true;
		}else if(s.equalsIgnoreCase("ban")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){
				s = st.nextToken();
				DangerZone.server.setBanned(s, true);
				Entity e = DangerZone.server.entityManager.findPlayerByName(s);
				if(e != null){				
					Player tlp = (Player)e;
					tlp.setGameMode(GameModes.LIMBO);
					tlp.server_thread.fatal_error = 1;
				}
				return true;
			}
			return errInvalidCmd(pl);
		}else if(s.equalsIgnoreCase("unban")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){
				s = st.nextToken();
				DangerZone.server.setBanned(s, false);
				return true;
			}
			return errInvalidCmd(pl);
		}else if(s.equalsIgnoreCase("whitelist")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){
				s = st.nextToken();
				DangerZone.server.setBanned(s, true);
				return true;
			}
			return errInvalidCmd(pl);
		}else if(s.equalsIgnoreCase("unwhitelist")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){
				s = st.nextToken();
				DangerZone.server.setBanned(s, false);
				return true;
			}
			return errInvalidCmd(pl);
		}else if(s.equalsIgnoreCase("showprivs")){
			Player tpl = pl;
			Entity e = null;
			if(st.hasMoreTokens()){
				if((pl.player_privs & PlayerPrivs.OPERATOR) == 0)return errPermissionDenied(pl);
				s = st.nextToken();
				e = DangerZone.server.entityManager.findPlayerByName(s);
				if(e != null){
					tpl = (Player)e;
				}else{
					return errOther(pl, " Error: player not found");
				}
			}
			String outstring = "  Privs for ";
			outstring += tpl.myname;
			outstring += ": ";
			if((tpl.player_privs & PlayerPrivs.GAMEMODE) != 0)outstring += "GameMode ";
			if((tpl.player_privs & PlayerPrivs.OPERATOR) != 0)outstring += "OP ";
			if((tpl.player_privs & PlayerPrivs.TELEPORT) != 0)outstring += "Teleport ";
			if((tpl.player_privs & PlayerPrivs.KILL) != 0)outstring += "Kill ";
			if((tpl.player_privs & PlayerPrivs.WEATHER) != 0)outstring += "Weather ";
			if((tpl.player_privs & PlayerPrivs.MISC) != 0)outstring += "Misc ";
			if((tpl.player_privs & PlayerPrivs.TIME) != 0)outstring += "Time ";
			if((tpl.player_privs & PlayerPrivs.SHUTDOWN) != 0)outstring += "Shutdown ";
			if((tpl.player_privs & PlayerPrivs.NOFIRE) != 0)outstring += "NoFire ";
			if((tpl.player_privs & PlayerPrivs.CHAT) != 0)outstring += "Chat ";
			pl.server_thread.sendCommandToPlayer(outstring);			
			return true;
		}else if(s.equalsIgnoreCase("who") || s.equalsIgnoreCase("players") ){
			String outstring = "  Players: ";
			DangerZone.server.player_list_lock.lock();
			int i;
			int nonline = 0;
			for(i=0;i<DangerZone.server.max_players;i++){
				if(DangerZone.server.players[i] != null){
					Player ppl = DangerZone.server.players[i].p;
					if(ppl != null){
						outstring += ppl.myname;
						if((ppl.player_privs & PlayerPrivs.OPERATOR) != 0)outstring += "(OP)";
						outstring += ", ";
						nonline++;
						if(nonline > 5){
							nonline = 0;
							outstring += "\n   ";
						}
					}
				}
			}
			DangerZone.server.player_list_lock.unlock();
			pl.server_thread.sendCommandToPlayer(outstring);
			return true;
			
		}else if(s.equalsIgnoreCase("items") ){
			if(!st.hasMoreTokens()){
				return errOther(pl, " Error: need a search string");
			}
			s = "";
			while(st.hasMoreTokens()){
				s += st.nextToken();
				if(st.hasMoreTokens()){
					s += " ";
				}
			}			
			String outstring = "";			
			int i;
			for(i=0;i<Items.itemsMAX;i++){
				if(Items.ItemArray[i] != null){
					if(Items.ItemArray[i].uniquename.toLowerCase().contains(s.toLowerCase())){
						outstring += String.format("Item: %d : %s\n", i, Items.ItemArray[i].uniquename);
					}
				}
			}			
			pl.server_thread.sendCommandToPlayer(outstring);
			return true;
		}else if(s.equalsIgnoreCase("blocks") ){
			if(!st.hasMoreTokens()){
				return errOther(pl, " Error: need a search string");
			}
			s = "";
			while(st.hasMoreTokens()){
				s += st.nextToken();
				if(st.hasMoreTokens()){
					s += " ";
				}
			}			
			String outstring = "";			
			int i;
			for(i=0;i<Blocks.blocksMAX;i++){
				if(Blocks.BlockArray[i] != null){
					if(Blocks.BlockArray[i].uniquename.toLowerCase().contains(s.toLowerCase())){
						outstring += String.format("Block: %d : %s\n", i, Blocks.BlockArray[i].uniquename);
					}
				}
			}			
			pl.server_thread.sendCommandToPlayer(outstring);
			return true;
		}else if(s.equalsIgnoreCase("give")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){
				s = st.nextToken();
				Entity e = DangerZone.server.entityManager.findPlayerByName(s);
				if(e != null){					
					Player plyr = (Player)e;
					if(st.hasMoreTokens()){
						s = st.nextToken();
						if(s.toLowerCase().equals("item")){
							if(st.hasMoreTokens()){
								s = st.nextToken();
								int iid;
								try{
									iid = Integer.parseInt(s);
								}catch(NumberFormatException ex){
									iid = 0;
								}
								if(iid <= 0 || iid >= Items.itemsMAX || Items.ItemArray[iid] == null){
									return errOther(pl, " Error: invalid itemID");
								}
								int count = 1;
								if(st.hasMoreTokens()){
									s = st.nextToken();									
									try{
										count = Integer.parseInt(s);
									}catch(NumberFormatException ex){
										count = 1;
									}
								}
								int maxcount = Items.getMaxStack(iid);
								if(count > maxcount)count = maxcount;
								InventoryContainer ic = new InventoryContainer();
								ic.iid = iid;
								ic.count = count;
								plyr.putMeInASlot(ic);
								return true;
							}else{
								return errOther(pl, " Error: expected itemID");
							}
						}
						if(s.toLowerCase().equals("block")){
							if(st.hasMoreTokens()){
								s = st.nextToken();
								int bid;
								try{
									bid = Integer.parseInt(s);
								}catch(NumberFormatException ex){
									bid = 0;
								}
								if(bid <= 0 || bid >= Blocks.blocksMAX || Blocks.BlockArray[bid] == null){
									return errOther(pl, " Error: invalid blockID");
								}
								int count = 1;
								if(st.hasMoreTokens()){
									s = st.nextToken();									
									try{
										count = Integer.parseInt(s);
									}catch(NumberFormatException ex){
										count = 1;
									}
								}
								int maxcount = Blocks.getMaxStack(bid);
								if(count > maxcount)count = maxcount;
								InventoryContainer ic = new InventoryContainer();
								ic.bid = bid;
								ic.count = count;
								plyr.putMeInASlot(ic);
								return true;
							}else{
								return errOther(pl, " Error: expected itemID");
							}					
						}
					}									
				}else{
					return errOther(pl, " Error: player not found");
				}
			}
			return errInvalidCmd(pl);
		}else if(s.equalsIgnoreCase("clearinventory")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){
				s = st.nextToken();
				Entity e = DangerZone.server.entityManager.findPlayerByName(s);
				if(e != null){					
					Player plyr = (Player)e;
					for(int i=0;i<64;i++){ //general inventory, hotbar, and armor!
						plyr.setVarInventory(i, null);
					}
					return true;
				}
			}
			return errInvalidCmd(pl);
		}else if(s.equalsIgnoreCase("kick")){
			if((pl.player_privs & PlayerPrivs.OPERATOR) == 0)return errPermissionDenied(pl);
			if(st.hasMoreTokens()){
				s = st.nextToken();
				Entity e = DangerZone.server.entityManager.findPlayerByName(s);
				if(e != null){					
					Player plyr = (Player)e;
					plyr.server_thread.fatal_error = 1; //say bye bye!
					return true;
				}
			}
			return errInvalidCmd(pl);
		}else if(s.equalsIgnoreCase("whereis") ){			
			if(st.hasMoreTokens()){
				s = st.nextToken();
				Entity e = DangerZone.server.entityManager.findPlayerByName(s);
				if(e != null){	
					String outstring = String.format(" %d: %d, %d, %d", e.dimension, (int)e.posx, (int)e.posy, (int)e.posz);
					pl.server_thread.sendCommandToPlayer(outstring);
					return true;
				}else{
					return errOther(pl, " Error: player not found");
				}
			}
			return errInvalidCmd(pl);
		}

		return false;
	}
	
	public boolean errInvalidCmd(Player pl){
		pl.server_thread.sendCommandToPlayer(" Error: Command Syntax");
		return false;
	}
	
	public boolean errPermissionDenied(Player pl){
		pl.server_thread.sendCommandToPlayer(" Error: Permission Denied");
		return false;
	}
	
	public boolean errOther(Player pl, String str){
		pl.server_thread.sendCommandToPlayer(str);
		return false;
	}

}
