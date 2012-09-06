/**
 * 
 */
package de.beimax.spacealert.mp3;

import java.io.FileNotFoundException;
import java.io.IOException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 * @author mkalus
 *
 */
public class BackgroundMP3PlayerFixed extends BackgroundMP3Player {
	/**
	 * file cacher
	 */
	static private MP3Cache cache = MP3Cache.getSingleton();

	/* (non-Javadoc)
	 * @see de.beimax.spacealert.mp3.BackgroundMP3Player#startMusicPlaying()
	 */
	@Override
	protected void startMusicPlaying() {
		try {
			player = new Player(cache.getMP3InputStream("red_alert_2.mp3"));
			final Player myPlayer = player;
			// anonymous runnable
			Runnable r = new Runnable() {
				public void run() {
					try {
						myPlayer.play();
					} catch (JavaLayerException e) {}
				}
			};
			Thread t = new Thread(r);
			t.start();
		} catch (FileNotFoundException e) {
			logger.warning("Background sound not found: " + e.getMessage());
		} catch(IOException e) {
			logger.warning("I/O Exception reading background sounds:" + e.getMessage());
		} catch (JavaLayerException e) {
			logger.warning("MP3-Player exception: " + e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see de.beimax.spacealert.mp3.BackgroundMP3Player#stopMusicPlaying()
	 */
	@Override
	protected void stopMusicPlaying() {
		if (player != null) player.close();
		player = null;
	}

}
