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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;


public class SoundManager {
	
	private class Soundinfo {
		public String filepath;
		public Audio sound;
		Soundinfo(String f){
			filepath = f;
			sound = null;
		}
	}
	
	Map <String, Soundinfo> soundmap;
	public int master_volume = 20;
	public int music_master_volume = 20;
	private Audio currentsong = null;
	
	private static long lasttime = 0;
	
	public SoundManager(){
		soundmap = new HashMap<String, Soundinfo>();			
		lasttime = System.currentTimeMillis();
		lasttime -= 10*60*1000; //ten minutes in milliseconds, so we start playing first song in about 5 minutes
	}
	
	public void playMusic(){
		if(DangerZone.f12_on)return; //PAUSED!!!
		
		long curtime = System.currentTimeMillis();
		curtime -= lasttime;
		if(curtime > 15*60*1000){ //15 minutes!
			if(music_master_volume > 0){
				if(currentsong == null || (currentsong != null && !currentsong.isPlaying())){
					lasttime = System.currentTimeMillis();
					try {
						String newfile = getRandomMusicFile();
						if(newfile == null)return;
						currentsong = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/music/" + newfile));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						return;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						return;
					}
					float mx;
					mx = music_master_volume;
					mx /= 400;
					currentsong.playAsMusic(1.0f, 0.125f, false); //set the baseline volume here (1/4)
					DangerZone.setMusicVolume(mx); //then make the adjustments to the baseline
				}
			}
		}
	}
	
	public void playThisMusic(String songname){
		if(DangerZone.f12_on)return; //PAUSED!!!
		
		if(songname == null)return;
		if(music_master_volume > 0){
			if(currentsong == null || (currentsong != null && !currentsong.isPlaying())){
				lasttime = System.currentTimeMillis();
				try {						
					currentsong = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/music/" + songname));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					return;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					return;
				}
				float mx;
				mx = music_master_volume;
				mx /= 400;
				currentsong.playAsMusic(1.0f, 0.125f, false); //set the baseline volume here (1/4)
				DangerZone.setMusicVolume(mx); //then make the adjustments to the baseline
			}
		}
	}
	
	//Music volume adjustment must be done through the STORE, and is handled in DZ main.
	public void setMusicVolume(){
		if(currentsong != null){
			if(currentsong.isPlaying()){
				float mx;
				mx = music_master_volume;
				mx /= 400;
				DangerZone.setMusicVolume(mx);
			}
		}
	}
	
	public void registerSound(String soundname, String filepath){
		soundmap.put(soundname, new Soundinfo(filepath));
	}
	
	public void playSound(String soundname, float vol, float freq, int d, double px, double py, double pz){
		//if(DangerZone.f12_on)return; //PAUSED!!!
		
		Soundinfo s = soundmap.get(soundname);
		if(s != null && !s.equals("")){
			if(s.sound == null){
				try {
					//System.out.printf("load sound file %s\n", s.filepath);
					s.sound = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream(s.filepath));
				} catch (Exception e) {
					System.out.printf("Failed to load sound file: %s\n", s.filepath);
					e.printStackTrace();
					s.sound = null;
					soundmap.remove(soundname);
				}
			}
			if(s.sound != null && master_volume > 0){				
				if(DangerZone.start_client && d == DangerZone.player.dimension){
					double mx = DangerZone.player.getDistanceFromEntityCenter(px, py, pz);
					if(mx < 128){
						mx -= 4;					
						mx = mx/8; //scale it! (so it doesn't drop off so fast!)
						if(mx < 1)mx = 1;
						mx = mx*mx; //Drop off with distance squared...
						mx = vol / mx;
						mx *= (float)(master_volume/100f);
						if(mx > 0.00125){ //if not too low to hear!
							if(mx>2)mx=2; //throttle it!
							try{
								//s.sound.playAsSoundEffect(freq,  (float)mx, false);
								s.sound.playAsSoundEffect(freq,  (float)mx, false, (float)px, (float)py, (float)pz);
							}catch(Exception e){
								System.out.printf("Sound broke again...\n");
							}
						}
					}
				}
			}
		}		
	}
	
	public void playSoundClose(String soundname, float vol, float freq, int d, double px, double py, double pz){
		//if(DangerZone.f12_on)return; //PAUSED!!!
		
		Soundinfo s = soundmap.get(soundname);
		if(s != null && !s.equals("")){
			if(s.sound == null){
				try {
					//System.out.printf("load sound file %s\n", s.filepath);
					s.sound = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream(s.filepath));
				} catch (Exception e) {
					System.out.printf("Failed to load sound file: %s\n", s.filepath);
					e.printStackTrace();
					s.sound = null;
					soundmap.remove(soundname);
				}
			}
			if(s.sound != null && master_volume > 0){				
				if(DangerZone.start_client && d == DangerZone.player.dimension){
					double mx = DangerZone.player.getDistanceFromEntityCenter(px, py, pz);
					if(mx < 8){
						mx -= 4;					
						mx = mx/8; //scale it! (so it doesn't drop off so fast!)
						if(mx < 1)mx = 1;
						mx = mx*mx; //Drop off with distance squared...
						mx = vol / mx;
						mx *= (float)(master_volume/100f);
						if(mx > 0.0125){ //if not too low to hear!
							if(mx>2)mx=2; //throttle it!
							try{
								//s.sound.playAsSoundEffect(freq,  (float)mx, false);
								s.sound.playAsSoundEffect(freq,  (float)mx, false, (float)px, (float)py, (float)pz);
							}catch(Exception e){
								System.out.printf("Sound broke again...\n");
							}
						}
					}
				}
			}
		}		
	}
	
	public String getRandomMusicFile(){
		
		long curtime = System.currentTimeMillis();
		Random rand = new Random(curtime);		
		String newsong = String.format("song%d.wav", rand.nextInt(13)+1);
		return newsong;
	}

}
