/**
 * 
 */
package de.beimax.spacealert.mp3;

import java.util.logging.Logger;

import de.beimax.spacealert.mission.EventList;
import de.beimax.spacealert.mission.Mission;

/**
 * MP3 Mission player (thread)
 * @author mkalus
 */
public class MP3MissionPlayer implements Runnable {
	static private Logger logger = Logger.getLogger("MP3MissionPlayer");

	/**
	 * event list of mission
	 */
	private EventList events;
	
	/**
	 * Constructor
	 * @param mission
	 */
	public MP3MissionPlayer(Mission mission) {
		events = mission.getMissionEvents(); // just get events
	}

	/**
	 * starts mission player
	 */
	public void run() {
		logger.info("MP3MissionPlayer started");
		// TODO Auto-generated method stub
		
	}
}
