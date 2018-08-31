package dangerzone.blocks;

public class BlockRotation {
	//DO NOT CHANGE!!!! Convenience only... not everything uses these... some hardcoded spots still exist..
	public static final int X_MASK = 0xc000;
	public static final int Y_MASK = 0x3000;
	public static final int Z_MASK = 0x0c00;
	
	public static final int NOT_X_MASK = 0x3fff;
	public static final int NOT_Y_MASK = 0xcfff;
	public static final int NOT_Z_MASK = 0xf3ff;
	
	public static final int X_ROT_0 = 0x0000;
	public static final int X_ROT_90 = 0x4000;
	public static final int X_ROT_180 = 0x8000;
	public static final int X_ROT_270 = 0xc000;
	
	public static final int Y_ROT_0 = 0x0000;
	public static final int Y_ROT_90 = 0x1000;
	public static final int Y_ROT_180 = 0x2000;
	public static final int Y_ROT_270 = 0x3000;
	
	public static final int Z_ROT_0 = 0x0000;
	public static final int Z_ROT_90 = 0x0400;
	public static final int Z_ROT_180 = 0x0800;
	public static final int Z_ROT_270 = 0x0c00;

}
