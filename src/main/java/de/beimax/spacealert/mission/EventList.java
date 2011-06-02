/**
 * 
 */
package de.beimax.spacealert.mission;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Keeps mission events in an ordered state and also checks for collisions and the like
 * @author mkalus
 *
 */
public class EventList {
	public static String formatTime(int time) {
		int minute = time/60;
		int seconds = time%60;
		return String.format("%02d", minute) + ":" + String.format("%02d", seconds);
	}
	
	/**
	 * actual time table for this mission
	 */
	TreeMap<Integer, Event> events;
	
	/**
	 * Constructor
	 */
	public EventList() {
		events = new TreeMap<Integer, Event>();
	}
	
	/**
	 * Add announcements
	 * @param phase1 - length of phase 1
	 * @param phase2 - length of phase 2
	 * @param phase3 - length of phase 3
	 */
	public void addPhaseEvents(int phase1, int phase2, int phase3) {
		addEvent(0, new Announcement(Announcement.ANNOUNCEMENT_PH1_START));
		Announcement a = new Announcement(Announcement.ANNOUNCEMENT_PH1_ONEMINUTE);
		addEvent(phase1 - 60 - a.getLengthInSeconds(), a);
		a = new Announcement(Announcement.ANNOUNCEMENT_PH1_TWENTYSECS);
		addEvent(phase1 - 20 - a.getLengthInSeconds(), a);
		a = new Announcement(Announcement.ANNOUNCEMENT_PH1_ENDS);
		addEvent(phase1 - a.getLengthInSeconds(), a);
		a = new Announcement(Announcement.ANNOUNCEMENT_PH2_ONEMINUTE);
		addEvent(phase1 + phase2 - 60 - a.getLengthInSeconds(), a);
		a = new Announcement(Announcement.ANNOUNCEMENT_PH2_TWENTYSECS);
		addEvent(phase1 + phase2 - 20 - a.getLengthInSeconds(), a);
		a = new Announcement(Announcement.ANNOUNCEMENT_PH2_ENDS);
		addEvent(phase1 + phase2 - a.getLengthInSeconds(), a);
		a = new Announcement(Announcement.ANNOUNCEMENT_PH3_ONEMINUTE);
		addEvent(phase1 + phase2 + phase3 - 60 - a.getLengthInSeconds(), a);
		a = new Announcement(Announcement.ANNOUNCEMENT_PH3_TWENTYSECS);
		addEvent(phase1 + phase2 + phase3 - 20 - a.getLengthInSeconds(), a);
		a = new Announcement(Announcement.ANNOUNCEMENT_PH3_ENDS);
		addEvent(phase1 + phase2 + phase3 - a.getLengthInSeconds(), a);
	}
	
	/**
	 * attempts to add event at time
	 * @param time
	 * @param event
	 * @return false, if a collision was detected
	 */
	public boolean addEvent(int time, Event event) {
		// check first
		if (!checkTime(time, event.getLengthInSeconds())) return false;
		
		// otherwise add event
		events.put(new Integer(time), event);
		return true;
	}
	
	/**
	 * Checks whether a certain time slot is free
	 * @param time
	 * @param length in seconds
	 * @return false, if time slot is not free
	 */
	public boolean checkTime(int time, int length) {
		// if empty set, you can add stuff
		if (events.isEmpty()) return true;
		
		// lowest or highest keys?
		int lowest = events.firstKey();
		if (lowest > time) { // there is no key before?
			if (time + length > lowest) return false; // too long
			else return true; // ok
		}
		int highest = events.lastKey();
		if (highest < time + length) return true;

		// ok, we are in between somewhere - check event before
		Entry<Integer, Event> before = events.floorEntry(time);
		int endTime = before.getKey() + before.getValue().getLengthInSeconds();
		if (endTime > time) return false;

		// check event after
		int after = events.ceilingKey(before.getKey() + 1); // next event after before key
		if (time + length > after) return false;

		return true;
	}
	
	/**
	 * return the next event for a given time
	 * @param time
	 * @return map.entry
	 */
	public Map.Entry<Integer,Event> getNextEvent(int time) {
		// last key?
		if (time > events.lastKey()) return null;
		
		return events.ceilingEntry(time);
	}

	/**
	 * Prints list of missions
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		// get values
		for(Map.Entry<Integer,Event> entry : events.entrySet()) {
			int time = entry.getKey();
			builder.append(EventList.formatTime(time));
			builder.append(" - ");
			builder.append(entry.getValue().getDescription(time));
			builder.append('\n');
		}
		return builder.toString();
	}
}
