/**
 * 
 */
package de.beimax.spacealert.mission;

/**
 * Generic Mission Interface
 * @author mkalus
 */
public interface Mission {
	/**
	 * Generate new mission
	 * 
	 * @return true if mission creation succeeded
	 */
	public boolean generateMission();
	
	/**
	 * Return event list of mission
	 * @return ordered event list of mission
	 */
	public EventList getMissionEvents();
}
