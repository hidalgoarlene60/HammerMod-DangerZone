package dangerzone.biomes;


public class RuggedHillsBiome extends RuggedBiome {

	public RuggedHillsBiome(String n) {
		super(n);
		hilliness = 16.5f; //0.5 = plains, 3 = hilly, 10+ = mountainous	
		roughness = 1.25f; //0.125 = smooth, 0.25 = a little lumpy, 1.0 = really bumpy
	}
	


}
