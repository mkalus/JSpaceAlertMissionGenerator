/**
 * 
 */
package de.beimax.spacealert.mp3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 * @author mkalus
 *
 */
public class BackgroundMP3Player extends Thread {
	static private Logger logger = Logger.getLogger("BackgroundMP3Player");

	/**
	 * should the player stop completely?
	 */
	private boolean stopped;
	
	/**
	 * should the music stop?
	 */
	private boolean playing;
	
	/**
	 * player reference
	 */
	private Player player;
	
	/**
	 * noise number reference - to differenciate a little bit
	 */
	private char noiseNumber = '0';
	
	/**
	 * stop the thread completely
	 */
	public void stopThread() {
		stopped = true;
	}
	
	/**
	 * command to sent to thread to stop music
	 */
	public void startMusic() {
		playing = true;
	}
	
	/**
	 * command to sent to thread to stop music
	 */
	public void stopMusic() {
		playing = false;
	}
	
	/**
	 * start playing the background noises
	 */
	@Override
	public void run() {
		System.out.println("Thread started.");
		stopped = false;
		stopMusic(); // do not start background music right away
		boolean lastState = playing;
		while (!stopped) {
			// state changed
			if (lastState != playing) {
				// playing activated -> start backgrund noises
				if (playing) {
					startMusicPlaying();
				// playing deactivated -> stop music
				} else {
					stopMusicPlaying();
				}
				lastState = playing;
			}
			// restart if music has finished
			if (playing && player != null && player.isComplete()) {
				startMusicPlaying();
			}
			try {
				sleep(100);
			} catch (InterruptedException e) {}
		};
		stopMusic();
		System.out.println("Thread stopped.");
	}
	
	/**
	 * start playing background alerts
	 */
	private void startMusicPlaying() {
		try {
			player = new Player(new FileInputStream("clips" + File.separator + "red_alert_" + (noiseNumber++) + ".mp3"));
			final Player myPlayer = player;
			// reset noise if too high
			if (noiseNumber >= '4') noiseNumber = '0';
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
		} catch (JavaLayerException e) {
			logger.warning("MP3-Player exception: " + e.getMessage());
		}
	}
	
	/**
	 * stop playing background alerts
	 */
	private void stopMusicPlaying() {
		if (player != null) player.close();
		player = null;
	}
}
