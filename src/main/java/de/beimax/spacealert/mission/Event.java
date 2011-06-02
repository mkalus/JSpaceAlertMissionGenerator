/**
 * 
 */
package de.beimax.spacealert.mission;

/**
 * Event that can occur on missions...
 * @author mkalus
 *
 */
public interface Event {
	/**
	 * Get length of event in seconds
	 * 
	 * @return length of the event in seconds
	 */
	public int getLengthInSeconds();
	
	/**
	 * Get description
	 * 
	 * @param time at which the event occurs (some events need to know)
	 * @return English description like the one on the cards
	 */
	public String getDescription(int time);
}
