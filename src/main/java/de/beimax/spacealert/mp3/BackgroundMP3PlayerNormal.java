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

import java.io.FileNotFoundException;
import java.io.IOException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 * @author mkalus
 *
 */
public class BackgroundMP3PlayerNormal extends BackgroundMP3Player {
	/**
	 * file cacher
	 */
	static private MP3Cache cache = MP3Cache.getSingleton();
	
	/**
	 * noise number reference - to differenciate a little bit
	 */
	private char noiseNumber = '0';

	/**
	 * start playing background alerts
	 */
	protected void startMusicPlaying() {
		try {
			player = new Player(cache.getMP3InputStream("red_alert_" + (noiseNumber++) + ".mp3"));
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
		} catch(IOException e) {
			logger.warning("I/O Exception reading background sounds:" + e.getMessage());
		} catch (JavaLayerException e) {
			logger.warning("MP3-Player exception: " + e.getMessage());
		}
	}
	
	/**
	 * stop playing background alerts
	 */
	protected void stopMusicPlaying() {
		if (player != null) player.close();
		player = null;
	}
}
