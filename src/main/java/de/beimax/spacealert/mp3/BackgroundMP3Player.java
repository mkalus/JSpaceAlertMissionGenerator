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

import java.util.logging.Level;
import java.util.logging.Logger;

import de.beimax.spacealert.util.Options;

import javazoom.jl.player.Player;

/**
 * @author mkalus
 *
 */
public abstract class BackgroundMP3Player extends Thread {
	static protected Logger logger = Logger.getLogger("BackgroundMP3Player");
	static {
		// debugging option set?
		if (Options.getOptions().debug) logger.setLevel(Level.FINEST);
		else logger.setLevel(Level.WARNING);
	}

	/**
	 * should the player stop completely?
	 */
	protected boolean stopped;
	
	/**
	 * should the music stop?
	 */
	protected boolean playing;
	
	/**
	 * player reference
	 */
	protected Player player;
	
	/**
	 * stop the thread completely
	 */
	public void stopThread() {
		stopMusic(); //stop music, too
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
		//System.out.println("Thread started.");
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
		//System.out.println("Thread stopped.");
	}
	
	/**
	 * start playing background alerts
	 */
	protected abstract void startMusicPlaying();
	
	/**
	 * stop playing background alerts
	 */
	protected abstract void stopMusicPlaying();
}
