/**
 * 
 */
package de.beimax.spacealert.mp3;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.beimax.spacealert.mission.Event;
import de.beimax.spacealert.mission.EventList;
import de.beimax.spacealert.mission.Mission;

/**
 * MP3 Mission player (thread)
 * @author mkalus
 */
public class MP3MissionPlayer implements Runnable {
	static private Logger logger = Logger.getLogger("MP3MissionPlayer");
	static {
		logger.setLevel(Level.WARNING);
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
		// start time
		long start = System.currentTimeMillis();
		int nextEventAt = 0;
		
		// create BackgroundMP3Player
		backgroundPlayer =  new BackgroundMP3Player(); // do not start player right away, it will be started by the first player anyway
		backgroundPlayer.start();
		
		// get next event
		Map.Entry<Integer, Event> nextEvent = events.getNextEvent(nextEventAt);
		do {
			// wait for event time
			while(System.currentTimeMillis() - start < ((long) nextEventAt) * 1000) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {}
			};
			Event event = nextEvent.getValue();
			int eventTime = nextEvent.getKey();
			logger.info("Event " + EventList.formatTime(eventTime) + " - " + event.getDescription(eventTime));
			
			MP3Player player = new MP3Player(event.getMP3s(eventTime), backgroundPlayer);
			player.start();
			
			// get next event
			nextEvent = events.getNextEvent(eventTime+1);
			if (nextEvent != null)
				nextEventAt = nextEvent.getKey();
		} while (nextEvent != null);
		
		// stop the thread completely
		backgroundPlayer.stopThread();
		
		logger.info("MP3MissionPlayer finished");
	}
}
