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
import java.io.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.io.IOException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.util.ResourceLocation;

// TODO FIXME !!! - This is broken, and adds everything in the directory.
// Have to make a new temporary class loader, and only add to the system class loader if isCompatible.
// !*&^@!&^%$ Java -- there is no removeURL()
//
public class ModLoader {
	
	@SuppressWarnings("rawtypes")
	private static final Class[] parameters = new Class[] {URL.class};
	
	//Our own resource loader that gets called from the slick loader. 
	//We loop through the loaded mods to try to find the resource...
	public class ResLoader implements ResourceLocation {

		@Override
		public URL getResource(String arg0) {
			return null; //not supported
		}

		@Override
		public InputStream getResourceAsStream(String arg0) {
			
			if(DangerZone.all_the_mods == null || DangerZone.all_the_mods.isEmpty())return null;
			
			//Custom resource loader to look through the mods to find something!
			//Luckily, pretty much everything is MAPPED or saved, so this only actually happens ONCE for each texture, sound, etc.
			InputStream is = null;
			//System.out.printf("Customresourceloader called on: %s\n", arg0);
			Iterator<ModListEntry> ii = DangerZone.all_the_mods.iterator();
			ModListEntry me;
			String str = arg0;
			if(str.charAt(0) == '/')str = arg0.substring(1); //remove extra '/' that breaks everything...
			while(ii.hasNext()){
				me = ii.next();
				try {
					//System.out.printf("Trying mod %s\n", me.modname);
					is = me.jarfile.getInputStream(me.jarfile.getEntry(str));
				} catch (Exception e){
					is = null;
				}
				if(is != null)return is;	//Found!			
			}			
			return null;
		}
		
	}
	
	public ModLoader(){
		ResourceLoader.addResourceLocation(new ResLoader()); //Tell the slick utils about our custom loader!
	}

	//load all compatible mods.
	@SuppressWarnings("resource")
	public String loadmods(){

		String curdir = System.getProperty("user.dir");
		File[] files = new File(curdir + "/mods").listFiles();
		JarFile jf = null;
		
		if(files == null)return null;
		
		for (File file : files) {
			if (file.isFile()) {
				BaseMod instance = null;
				//System.out.printf("Found file %s:%s\n", file.getAbsolutePath(), file.getName());
				if(file.getName().endsWith(".jar")){
					try {
												
						jf = new JarFile(file.getAbsolutePath());
						Enumeration<JarEntry> ev = jf.entries();
						while (ev.hasMoreElements()) {
							JarEntry je = (JarEntry) ev.nextElement();
							//System.out.printf("Things in jarfile: %s\n", je.getName());
							if(je.getName().endsWith("Main.class")){	
								String fixedname = je.getName().replace('/', '.');
								fixedname = fixedname.replace('\\', '.');
								fixedname = fixedname.replace(".class", "");
								addFile(file);
								Constructor<?> cs = ClassLoader.getSystemClassLoader().loadClass(fixedname).getConstructor();
								if(cs != null){
									Object o = cs.newInstance();
									if(o instanceof BaseMod){
										//System.out.printf("Found! %s\n", je.getName());
										instance = (BaseMod)o;
										break;
									}
								}
							}
						}
					    
						
					} catch (IOException e) {
						System.out.printf("Cannot load file: %s\n", file.getAbsolutePath());
						e.printStackTrace();
					} catch (SecurityException e) {
						System.out.printf("Cannot load file: %s\n", file.getAbsolutePath());
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						System.out.printf("Cannot load file: %s\n", file.getAbsolutePath());
						e.printStackTrace();
					} catch (InstantiationException e) {
						System.out.printf("Cannot load file: %s\n", file.getAbsolutePath());
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						System.out.printf("Cannot load file: %s\n", file.getAbsolutePath());
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						System.out.printf("Cannot load file: %s\n", file.getAbsolutePath());
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						System.out.printf("Cannot load file: %s\n", file.getAbsolutePath());
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						System.out.printf("Cannot load file: %s\n", file.getAbsolutePath());
						e.printStackTrace();
					}

					if(instance != null){
						if(isCompatible(instance.versionBuiltWith())){
							ModListEntry thismod = new ModListEntry();
							thismod.versionbuiltwith = instance.versionBuiltWith();
							thismod.modname = instance.getModName();
							thismod.modinstance = instance;
							thismod.filepath = file.getAbsolutePath();
							thismod.jarfile = jf;
							DangerZone.all_the_mods.add(thismod);
							instance.registerThings();
						}else{
							System.out.printf("File: %s is incompatible. Please remove it.\n", file.getAbsolutePath());
							return file.getAbsolutePath(); //incompatible mod or file!
						}
					}else{
						System.out.printf("File: %s is incompatible. Please remove it.\n", file.getAbsolutePath());
						return file.getAbsolutePath(); //incompatible mod or file!
					}		        	
				}
			}
		}
		
		//now have list of registered mods!
		//go back and call the mods so they can have a full list of mods available
		//and do post-registration things, if any...
		Iterator<ModListEntry> ii = DangerZone.all_the_mods.iterator();
		ModListEntry me;
		while(ii.hasNext()){
			me = ii.next();
			me.modinstance.postLoadProcessing();
			System.out.printf("Loaded mod: %s\n", me.modname);
		}
		
		return null;
	}
	
	//load all specified mods.
	@SuppressWarnings("resource")
	public boolean loadmods(List<String> namelist){

		String curdir = System.getProperty("user.dir");
		File[] files = new File(curdir + "/mods").listFiles();
		JarFile jf = null;
		
		if(files == null)return false;
		
		for (File file : files) {
			if (file.isFile()) {
				BaseMod instance = null;
				//System.out.printf("Found file %s:%s\n", file.getAbsolutePath(), file.getName());
				if(file.getName().endsWith(".jar")){
					try {
						
						jf = new JarFile(file.getAbsolutePath());
						Enumeration<JarEntry> ev = jf.entries();
						while (ev.hasMoreElements()) {
							JarEntry je = (JarEntry) ev.nextElement();
							//System.out.printf("Things in jarfile: %s\n", je.getName());
							if(je.getName().endsWith("Main.class")){	
								String fixedname = je.getName().replace('/', '.');
								fixedname = fixedname.replace('\\', '.');
								fixedname = fixedname.replace(".class", "");
								addFile(file);
								Constructor<?> cs = ClassLoader.getSystemClassLoader().loadClass(fixedname).getConstructor();
								if(cs != null){
									Object o = cs.newInstance();
									if(o instanceof BaseMod){
										//System.out.printf("Found! %s\n", je.getName());
										instance = (BaseMod)o;
										break;
									}
								}
							}
						}


					} catch (IOException e) {
						System.out.printf("Cannot load file: %s\n", file.getAbsolutePath());
						e.printStackTrace();
					} catch (SecurityException e) {
						System.out.printf("Cannot load file: %s\n", file.getAbsolutePath());
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						System.out.printf("Cannot load file: %s\n", file.getAbsolutePath());
						e.printStackTrace();
					} catch (InstantiationException e) {
						System.out.printf("Cannot load file: %s\n", file.getAbsolutePath());
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						System.out.printf("Cannot load file: %s\n", file.getAbsolutePath());
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						System.out.printf("Cannot load file: %s\n", file.getAbsolutePath());
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						System.out.printf("Cannot load file: %s\n", file.getAbsolutePath());
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						System.out.printf("Cannot load file: %s\n", file.getAbsolutePath());
						e.printStackTrace();
					}

					if(instance != null){
						if(isCompatible(instance.versionBuiltWith()) && isonList(namelist, instance.getModName())){
							ModListEntry thismod = new ModListEntry();
							thismod.versionbuiltwith = instance.versionBuiltWith();
							thismod.modname = instance.getModName();
							thismod.modinstance = instance;
							thismod.filepath = file.getAbsolutePath();
							thismod.jarfile = jf;
							DangerZone.all_the_mods.add(thismod);
							instance.registerThings();
						}
					}		        	
				}
			}
		}
		
		if(namelist.size() != DangerZone.all_the_mods.size())return false;
		
		//now have list of registered mods!
		//go back and call the mods so they can have a full list of mods available
		//and do post-registration things, if any...
		Iterator<ModListEntry> ii = DangerZone.all_the_mods.iterator();
		ModListEntry me;
		while(ii.hasNext()){
			me = ii.next();
			me.modinstance.postLoadProcessing();
			System.out.printf("Loaded mod: %s\n", me.modname);
		}
		return true;
	}
	
	private boolean isonList(List<String> mlist, String s){
		int i = mlist.size();
		for(int k=0;k<i;k++){
			if(s.equals(mlist.get(k)))return true;
		}
		return false;
	}
	
	//this should grow and be a little smarter, check backwards compatibility and such...
	private boolean isCompatible(String modver){
		if(DangerZone.versionstring.equals(modver))return true;
		return false;
	}
	
    public static void addFile(File f) throws IOException {
        addURL(f.toURI().toURL());
    }

    /**
     * Adds the content pointed by the URL to the classpath.
     * @param u the URL pointing to the content to be added
     * @throws IOException
     */
    public static void addURL(URL u) throws IOException {
        URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
        Class<?> sysclass = URLClassLoader.class;
        try {
            Method method = sysclass.getDeclaredMethod("addURL", parameters);
            method.setAccessible(true);
            method.invoke(sysloader,new Object[]{ u }); 
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to system classloader");
        }        
    }

	//load all server-only compatible mods.
	public void loadservermods(){

		String curdir = System.getProperty("user.dir");
		File[] files = new File(curdir + "/server_mods").listFiles();
		JarFile jf = null;
		
		if(files == null)return;
		
		for (File file : files) {
			if (file.isFile()) {
				BaseMod instance = null;
				//System.out.printf("Found file %s:%s\n", file.getAbsolutePath(), file.getName());
				if(file.getName().endsWith(".jar")){
					try {
												
						jf = new JarFile(file.getAbsolutePath());
						Enumeration<JarEntry> ev = jf.entries();
						while (ev.hasMoreElements()) {
							JarEntry je = (JarEntry) ev.nextElement();
							//System.out.printf("Things in jarfile: %s\n", je.getName());
							if(je.getName().endsWith("Main.class")){	
								String fixedname = je.getName().replace('/', '.');
								fixedname = fixedname.replace('\\', '.');
								fixedname = fixedname.replace(".class", "");
								addFile(file);
								Constructor<?> cs = ClassLoader.getSystemClassLoader().loadClass(fixedname).getConstructor();
								if(cs != null){
									Object o = cs.newInstance();
									if(o instanceof BaseMod){
										//System.out.printf("Found! %s\n", je.getName());
										instance = (BaseMod)o;
										break;
									}
								}
							}
						}
					    
						
					} catch (IOException e) {
						System.out.printf("Cannot load file: %s\n", file.getAbsolutePath());
						e.printStackTrace();
					} catch (SecurityException e) {
						System.out.printf("Cannot load file: %s\n", file.getAbsolutePath());
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						System.out.printf("Cannot load file: %s\n", file.getAbsolutePath());
						e.printStackTrace();
					} catch (InstantiationException e) {
						System.out.printf("Cannot load file: %s\n", file.getAbsolutePath());
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						System.out.printf("Cannot load file: %s\n", file.getAbsolutePath());
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						System.out.printf("Cannot load file: %s\n", file.getAbsolutePath());
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						System.out.printf("Cannot load file: %s\n", file.getAbsolutePath());
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						System.out.printf("Cannot load file: %s\n", file.getAbsolutePath());
						e.printStackTrace();
					}

					if(instance != null){
						if(isCompatible(instance.versionBuiltWith())){
							ModListEntry thismod = new ModListEntry();
							thismod.versionbuiltwith = instance.versionBuiltWith();
							thismod.modname = instance.getModName();
							thismod.modinstance = instance;
							thismod.filepath = file.getAbsolutePath();
							thismod.jarfile = jf;
							DangerZone.all_the_server_mods.add(thismod);
							instance.registerThings();
						}
					}		        	
				}
			}
		}
		
		//now have list of registered mods!
		//go back and call the mods so they can have a full list of mods available
		//and do post-registration things, if any...
		Iterator<ModListEntry> ii = DangerZone.all_the_server_mods.iterator();
		ModListEntry me;
		while(ii.hasNext()){
			me = ii.next();
			me.modinstance.postLoadProcessing();
			System.out.printf("Loaded Server mod: %s\n", me.modname);
		}
	}

}
