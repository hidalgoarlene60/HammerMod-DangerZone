package dangerzone.biomes;

public class RuggedPlainsBiome extends RuggedBiome {

	public RuggedPlainsBiome(String n) {
		super(n);
		hilliness = 0.5f; //0.5 = plains, 3 = hilly, 10+ = mountainous	
		roughness = 0.125f; //0.125 = smooth, 0.25 = a little lumpy, 1.0 = really bumpy
	}

}
