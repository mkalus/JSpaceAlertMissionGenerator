/**
 * This file is part of the JSpaceAlertMissionGenerator software.
 * Copyright (C) 2011 Maximilian Kalus
 * See http://www.beimax.de/ and https://github.com/mkalus/JSpaceAlertMissionGenerator
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **/
package de.beimax.spacealert.mp3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.beimax.spacealert.util.Options;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 * Simple MP3 Player
 * @author mkalus
 */
public class MP3Player extends Thread {
	static private Logger logger = Logger.getLogger("MP3Player");
	static {
		// debugging option set?
		if (Options.getOptions().debug) logger.setLevel(Level.FINEST);
		else logger.setLevel(Level.WARNING);
	}
	
	/**
	 * file cacher
	 */
	static private MP3Cache cache = MP3Cache.getSingleton();
	
	/**
	 * list of files to play
	 */
	private String[] files;
	
	/**
	 * reference to player
	 */
	private Player[] players;
	
	/**
	 * reference to backgroundplayer
	 */
	BackgroundMP3Player backgroundPlayer;
	
	/**
	 * Constructor
	 * @param files comma separated list of file names
	 */
	public MP3Player(String files, BackgroundMP3Player backgroundPlayer) {
		// split files
		if (files != null)
			this.files = files.split(",");
		this.backgroundPlayer = backgroundPlayer;
	}
	
	/**
	 * Constructor
	 * @param files array of file names
	 */
	public MP3Player(String[] files, BackgroundMP3Player backgroundPlayer) {
		// split files
		this.files = files;
		this.backgroundPlayer = backgroundPlayer;
	}
	
	/**
	 * start playing the music
	 */
	@Override
	public void run() {
		// sanity check
		if (files == null) {
			logger.warning("Empty file list in MP3 player.");
			return;
		}
		// stop background player
		backgroundPlayer.stopMusic();
		// preload files
		players = new Player[files.length];
		for (int i = 0; i < files.length; i++) {
			try {
				// special separator?
				String file = files[i];
				if (files[i].indexOf(':') != -1) {
					file = files[i].split(":")[0];
				}
				InputStream is = cache.getMP3InputStream(file);
				players[i] = new Player(is);
			} catch(FileNotFoundException e) {
				logger.warning("Could not find file " + "clips" + File.pathSeparator + files[i]);
			} catch(IOException e) {
				logger.warning("I/O Exception reading " + "clips" + File.pathSeparator + files[i] + ":" + e.getMessage());
			} catch (JavaLayerException e) {
				logger.warning("JavaLayerException: " + e.getMessage());
			}
		}
		// play them
		for (int i = 0; i < files.length; i++) {
			try {
				if (files[i].indexOf(':') != -1) {
					playSpecial(players[i], Integer.parseInt(files[i].split(":")[1]), i);
				} else players[i].play(); // normal play
			} catch (JavaLayerException e) {
				logger.warning("JavaLayerException: " + e.getMessage());
			} catch (RuntimeException e) {
				logger.warning("Runtime Exception: " + e.getMessage());
			}
		}
		// after playing - start background player again
		backgroundPlayer.startMusic();
	}
	
	private void playSpecial(Player player, int seconds, int index) throws JavaLayerException, RuntimeException {
		long stoptime = System.currentTimeMillis() + ((long)(seconds) * 1000);
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
		
		// wait for time to finish
		while (System.currentTimeMillis() < stoptime && !myPlayer.isComplete()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
		
		// check, if the player has been playing to short
		long now = System.currentTimeMillis();
		if (now < stoptime) {
			String file = files[index].split(":")[0];
			int remainingTime = (int)((stoptime - now) / 1000);
			logger.info("Playing to short by " + remainingTime + " secs!");
			if (remainingTime <= 1) return; // no need to start miniclips
			// start special player again
			try {
				Player newPlayer = new Player(cache.getMP3InputStream(file));
				playSpecial(newPlayer, remainingTime, index);
			} catch (IOException e) {}
		} else
			player.close();
	}
	
	/**
	 * Stop the music
	 */
	public void stopPlayer() {
		for (int i = 0; i < files.length; i++) {
			if (players[i] != null && !players[i].isComplete())
				players[i].close();
		}
	}
}
