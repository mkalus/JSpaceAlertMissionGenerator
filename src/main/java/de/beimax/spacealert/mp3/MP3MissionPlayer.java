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

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.beimax.spacealert.mission.Event;
import de.beimax.spacealert.mission.EventList;
import de.beimax.spacealert.mission.Mission;
import de.beimax.spacealert.mission.WhiteNoise;
import de.beimax.spacealert.util.ConsoleColorer;
import de.beimax.spacealert.util.Options;

/**
 * MP3 Mission player (thread)
 * @author mkalus
 */
public class MP3MissionPlayer implements Runnable {
	static private Logger logger = Logger.getLogger("MP3MissionPlayer");
	static {
		// debugging option set?
		if (Options.getOptions().debug) logger.setLevel(Level.FINEST);
		else logger.setLevel(Level.WARNING);
	}
	
	/**
	 * event list of mission
	 */
	private EventList events;
	
	/**
	 * background player
	 */
	BackgroundMP3Player backgroundPlayer;
	
	/**
	 * current player
	 */
	MP3Player player;
	
	/**
	 * player interruption?
	 */
	private boolean stop;
	
	/**
	 * player has finished?
	 */
	private boolean finished;

	/**
	 * Constructor
	 * @param mission
	 * @param alarm
	 */
	public MP3MissionPlayer(Mission mission, BackgroundMP3Player alarm) {
		events = mission.getMissionEvents(); // just get events
		backgroundPlayer = alarm; // do not start player right away, it will be started by the first player anyway
	}

	/**
	 * starts mission player
	 */
	public void run() {
		logger.info("MP3MissionPlayer started");

		// Add some space to the console output
		System.out.println("");

		// start time
		long start = System.currentTimeMillis();
		int nextEventAt = 0;
		
		// start BackgroundMP3Player
		backgroundPlayer.start();
		
		// get next event
		Map.Entry<Integer, Event> nextEvent = events.getNextEvent(nextEventAt);
		String lastPrintedString = "";
		Boolean colorConsoleText = Options.getOptions().colorConsoleText;
		do {
			// wait for event time
			while(System.currentTimeMillis() - start < ((long) nextEventAt) * 1000) {
				try {
					if (stop) {
						stopMyself();
						return;
					}
					Thread.sleep(100);
				} catch (InterruptedException e) {}
			};
			Event event = nextEvent.getValue();
			int eventTime = nextEvent.getKey();
			if (!(event instanceof WhiteNoise)) {
				// Print the event to Standard Output so you can 'check' the mission
				// in case someone yelled something and you didn't hear it.
				//
				// We skip white noise events, as you will hear those continually.
				String lineToPrint = EventList.formatTime(eventTime) + " - " + event.getDescription(eventTime);
				if (colorConsoleText) {
					if (lastPrintedString.contains("ends in") && lineToPrint.contains("ends in")) {
						// If the last line we printed was a 'phase/operation ends in...' line,
						// then to make the console easier to read, we clear the last printed line
						// before writing out this new line.
						// We assume here that if the console supports color ANSI codes, it also supports
						// the ANSI code to clear the last line.
						// Note that if the console wraps text, then this will only clear the "last
						// wrapped line" and not the entire line. The 'one minute' and '20 seconds' lines
						// are short though so probably won't wrap.
						System.out.print("\033[A\033[J");
					}
					lastPrintedString = lineToPrint;
					System.out.println(ConsoleColorer.colorLine(lineToPrint));
				} else {
					// No color, just print the line.
					// No need to keep track of the last printed line as it is never checked.
					System.out.println(lineToPrint);
				}
			}
			
			player = new MP3Player(event.getMP3s(eventTime), backgroundPlayer);
			player.start();
			
			// get next event
			nextEvent = events.getNextEvent(eventTime+1);
			if (nextEvent != null)
				nextEventAt = nextEvent.getKey();
		} while (nextEvent != null);
		
		// stop the thread completely
		backgroundPlayer.stopThread();
		
		// finished
		finished = true;
		
		logger.info("MP3MissionPlayer finished");
	}

	/**
	 * returns true, if finished
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * set the stop signal for the player
	 */
	public void stopPlayer() {
		this.stop = true;
	}

	/**
	 * stops the MP3 player - called by myself
	 */
	private void stopMyself() {
		logger.info("MP3Player has received stop signal - waiting for MP3s to finish.");
		backgroundPlayer.stopThread();
		// wait for current thread to finish
		while (player.isAlive()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
		finished = true;
		logger.info("MP3Player has been stopped.");
	}
}
