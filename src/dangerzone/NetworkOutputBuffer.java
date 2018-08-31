package dangerzone;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;


public class NetworkOutputBuffer {
	int currentlen = 0;
	ByteBuffer b = null;
	int maxlen = 0;
	OutputStream out = null;
	boolean error_happened = false;
	
	public NetworkOutputBuffer(OutputStream inout, int howbig){
		out = inout;
		b = ByteBuffer.allocate(howbig+200);	//a little extra...			
		maxlen = howbig;
	}
	
	public void close(){
		
	}
	
	public boolean errorOccurred(){
		return error_happened;
	}
	
	public void flush(){
		if(error_happened)return;
		if(currentlen > 0){
			try {				
				out.write(b.array(), 0, currentlen); //probably writes one byte at a time... sigh...
				out.flush();
				b.rewind();
				b.position(0);
				currentlen = 0;
			} catch (IOException e) {
				if(DangerZone.start_client){
					DangerZone.gameover = 1;
				}
				error_happened = true;
			}			
		}
	}
	
	public void writeInt(int in){
		if(error_happened)return;
		if(maxlen-currentlen < 4){
			flush();
		}
		b.putInt(in);
		currentlen += 4;
	}
	
	public void writeShort(short in){
		if(error_happened)return;
		if(maxlen-currentlen < 2){
			flush();
		}
		b.putShort(in);
		currentlen += 2;
	}
	
	public void writeByte(byte in){
		if(error_happened)return;
		if(maxlen-currentlen < 1){
			flush();
		}
		b.put(in);
		currentlen += 1;
	}
	
	public void writeFloat(float in){
		if(error_happened)return;
		if(maxlen-currentlen < 4){
			flush();
		}
		b.putFloat(in);
		currentlen += 4;
	}
	
	public void writeDouble(double in){
		if(error_happened)return;
		if(maxlen-currentlen < 8){
			flush();
		}
		b.putDouble(in);
		currentlen += 8;
	}
	
	public void writeLong(long in){
		if(error_happened)return;
		if(maxlen-currentlen < 8){
			flush();
		}
		b.putLong(in);
		currentlen += 8;
	}
	
	public void writeString(String in){
		if(error_happened)return;
		int len = 0;
		byte [] stringbytes = null;
		
		if(in != null){
			try {
				stringbytes = in.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				stringbytes = in.getBytes();
			}
			len = stringbytes.length;
		}
		writeInt(len);
		
		if(maxlen-currentlen < len){
			flush();
		}
		if(len > 0){
			b.put(stringbytes, 0, len);
		}
		
		currentlen += len;
	}
	
	public void writeShortArray(short sh[], int len){
		if(error_happened)return;
		writeInt(len);
		if(error_happened)return;
		if(len > 0){
			if(maxlen-currentlen < len*2){
				flush();
				if(error_happened)return;
			}
			for(int i=0;i<len;i++){
				b.putShort(sh[i]);
			}
			currentlen += len*2;
		}
	}
	
	public void writeByteArray(byte sh[], int len){
		if(error_happened)return;
		writeInt(len);
		if(error_happened)return;
		if(len > 0){
			if(maxlen-currentlen < len){
				flush();
				if(error_happened)return;
			}
			b.put(sh, 0, len);
			currentlen += len;
		}
	}

}
