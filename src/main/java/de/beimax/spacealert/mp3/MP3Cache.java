/**
 * 
 */
package de.beimax.spacealert.mp3;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import de.beimax.spacealert.util.Options;

/**
 * Caches MP3 files in order to save disk accesses
 * @author mkalus
 */
public class MP3Cache {
	/** Block size that we want to read in one time. */
	private static final int READ_BLOCK = 8192;
	/** Singleton instance */
	private static MP3Cache cacheSingleton;
	
	/**
	 * @return singleton
	 */
	public static MP3Cache getSingleton() {
		if (cacheSingleton == null) cacheSingleton = new MP3Cache();
		return cacheSingleton;
	}
	
	/**
	 * cache hash
	 */
	HashMap<String, byte[]> cache;
	
	/**
	 * private constructor for singleton
	 */
	private MP3Cache() {
		cache = new HashMap<String, byte[]>();
	}
	
	/**
	 * returns MP3 stream from cache
	 * @param file in the "clips" folder
	 * @return input stream of MP3
	 * @throws IOException 
	 */
	public InputStream getMP3InputStream(String file) throws IOException {
		byte[] mp3;
		
		// check for cache hit
		if (cache.containsKey(file)) {
			//System.out.println("Cache hit: " + file);
			mp3 = cache.get(file);
		} else {
			//System.out.println("Read new: " + file);
			// not cached: read into byte array
			mp3 = readToEnd(new FileInputStream(Options.getOptions().clipsFolder + File.separator + file));
			// insert into cache
			cache.put(file, mp3);
		}
		
		return new ByteArrayInputStream(mp3);
		
		//return new FileInputStream(Options.getOptions().clipsFolder + File.separator + file);
	}

	/**
	 * Read all from stream, using nio.
	 * 
	 * @param is source stream.
	 * 
	 * @return result byte array that read from source
	 * 
	 * @throws IOException by {@code Channel.read()}
	 */
	public byte[] readToEnd(InputStream is) throws IOException {
		// create channel for input stream
		ReadableByteChannel bc = Channels.newChannel(is);
		ByteBuffer bb = ByteBuffer.allocate(READ_BLOCK);

		while (bc.read(bb) != -1) {
			bb = resizeBuffer(bb); // get new buffer for read
		}
		byte[] result = new byte[bb.position()];
		bb.position(0);
		bb.get(result);

		return result;
	}

	/**
	 * helper to resize a byte buffer
	 * @param in
	 * @return
	 */
	private static ByteBuffer resizeBuffer(ByteBuffer in) {
		ByteBuffer result = in;
		if (in.remaining() < READ_BLOCK) {
			// create new buffer
			result = ByteBuffer.allocate(in.capacity() * 2);
			// set limit to current position in buffer and set position to zero.
			in.flip();
			// put original buffer to new buffer
			result.put(in);
		}

		return result;
	}
}
