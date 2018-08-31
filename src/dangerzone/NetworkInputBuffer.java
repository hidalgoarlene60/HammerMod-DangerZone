package dangerzone;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;


public class NetworkInputBuffer {
	volatile int currentlen = 0;
	ByteBuffer b = null;
	int maxlen = 0;
	InputStream datain = null;
	boolean error_happened = false;
	byte bb[] = new byte[32000];
	BufferedInputStream bis = null;
	
	//howbig just needs to be a a few k. its all byte-oriented so bigger is not better...
	public NetworkInputBuffer(InputStream inout, int howbig){
		datain = inout;
		b = ByteBuffer.allocate(howbig+200);	//plus a little extra			
		maxlen = howbig;
		bis = new BufferedInputStream(datain, bb.length);
	}
	
	public void close(){
		
	}
	
	public boolean errorOccurred(){
		return error_happened;
	}
	
	//read as much as possible...
	private void getAtLeast(int min){
		if(currentlen >= min)return;
		if(error_happened){
			b.rewind();
			b.position(0);
			return;
		}
		int newval, i;
		int maxread = 0;
		boolean readshort = false;
				
		//copy current buffer down, then reset position and read more
		if(currentlen > 0){
			byte b1[] = b.array();			
			for(i=0;i<currentlen;i++){
				b1[i] = b.get();
			}
		}
		
		//if(currentlen < 0 || currentlen > maxlen){
		//	System.out.printf("WFT? %d and %d\n", currentlen, min);
		//}
		b.position(currentlen);

		//read as many as we can, maybe... .available is an "estimate"...
		try {
				maxread = bis.available();
				//System.out.printf("available = %d\n", maxread);
		} catch (IOException e1) {
			error_happened = true;
			if(DangerZone.start_client)DangerZone.gameover = 1;
			b.rewind();
			b.position(0);
			return;
		}
		if(maxread < min-currentlen)maxread = min-currentlen;
		if(maxread > maxlen-currentlen)maxread = maxlen-currentlen;
		if(maxread > bb.length)maxread = bb.length;
		
		//System.out.printf("trying for = %d\n", maxread);

		newval = maxread;
		try {
			if(currentlen != 0){
				newval = bis.read(bb, 0, maxread);
				//if(newval != maxread){
				//	System.out.printf("read bb fail: %d,  vs %d\n", newval, maxread);
				//	System.out.printf("min,  cl == %d, %d\n", min, currentlen);
				//}
			}else{
				newval = bis.read(b.array(), 0, maxread);
				//if(newval != maxread){
				//	System.out.printf("read b.array fail: %d,  vs %d\n", newval, maxread);
				//	System.out.printf("min,  cl == %d, %d\n", min, currentlen);
				//}
			}
		} catch (IOException e) {
			error_happened = true;
			if(DangerZone.start_client)DangerZone.gameover = 1;
		} //read a chunk of data at a time
		
		if(newval != maxread){
			if(newval <= 0){
				System.out.printf("maxread mismatch %d vs %d player quit?\n", newval, maxread);
				error_happened = true;
				if(DangerZone.start_client)DangerZone.gameover = 1;
			}else{
				readshort = true;
			}
		}
		
		if(currentlen != 0)b.put(bb, 0, newval);
		currentlen += newval;
		//System.out.printf("err = %s\n", error_happened?"true":"false");
		b.rewind();
		b.position(0);
		
		if(readshort){ //figure out if we have enough already!
			if(currentlen < min){ //nope, not enough yet!
				//System.out.printf("reading %d more, cl was %d\n", min-currentlen, currentlen-newval);
				//System.out.printf("min,  cl == %d, %d\n", min, currentlen);
				getAtLeast(min); //Keep trying!!! (happens, but doesn't happen too often, so OK)
			}
		}

	}
	
	public int readInt(){
		if(error_happened)return 0;
		if(currentlen < 4){
			getAtLeast(4);
			if(error_happened)return 0;
		}
		currentlen -= 4;
		return b.getInt();
	}
	
	public short readShort(){
		if(error_happened)return 0;
		if(currentlen < 2){
			getAtLeast(2);
			if(error_happened)return 0;
		}
		currentlen -= 2;
		return b.getShort();
	}
	
	public byte readByte(){
		if(error_happened)return 0;
		if(currentlen < 1){
			getAtLeast(1);
			if(error_happened)return 0;
		}
		currentlen -= 1;
		return b.get();
	}
	
	public float readFloat(){
		if(error_happened)return 0;
		if(currentlen < 4){
			getAtLeast(4);
			if(error_happened)return 0;
		}
		currentlen -= 4;
		return b.getFloat();
	}	
	
	public double readDouble(){
		if(error_happened)return 0;
		if(currentlen < 8){
			getAtLeast(8);
			if(error_happened)return 0;
		}
		currentlen -= 8;
		return b.getDouble();
	}	
	
	public long readLong(){
		if(error_happened)return 0;
		if(currentlen < 8){
			getAtLeast(8);
			if(error_happened)return 0;
		}
		currentlen -= 8;
		return b.getLong();
	}	

	
	public String readString(){
		if(error_happened)return null;
		byte [] stringbytes = readByteArray();
		if(stringbytes == null)return null;
		String s = null;
		
		try {
			s = new String(stringbytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			s = new String();
			for(int i=0;i<stringbytes.length;i++){
				s += (char)stringbytes[i];
			}
		}

		return s;
	}
	
	public short [] readShortArray(){
		int len = readInt();
		if(len <= 0)return null;
		
		short [] sh = new short[len];
		
		getAtLeast(len*2);
		if(error_happened)return null;
		
		for(int i=0;i<len;i++){
			sh[i] = b.getShort(); //No! Cannot use b.asShortBuffer() here!!!
		}
		currentlen -= len*2;
		
		return sh;
	}
	
	public byte [] readByteArray(){
		int len = readInt();
		if(len <= 0)return null;
		
		byte [] sh = new byte[len];
		
		getAtLeast(len);
		if(error_happened)return null;

		b.get(sh, 0, len);
		currentlen -= len;
		return sh;
	}

}
