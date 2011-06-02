/**
 * 
 */
package de.beimax.spacealert;

/**
 * @author mkalus
 *
 */
public class WhiteNoise implements Event {
	/**
	 * length in seconds
	 */
	private int length;
	
	/**
	 * Constructor
	 * @param length of the white noise in seconds
	 */
	public WhiteNoise(int length) {
		this.length = length;
	}

	/**
	 * white noise time is the the time it takes...
	 */
	public int getLengthInSeconds() {
		return length;
	}

	/**
	 * from-to white noise
	 */
	public String toString() {
		return "White Noise - length " + length + " seconds.";
	}

	/**
	 * Get Description
	 */
	public String getDescription(int time) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Communication system down...\n");
		// length of communication restored is about 2 secs
		sb.append(EventList.formatTime(time + length - 2)).append(" - ... Communication system restored.");
		
		return sb.toString();
	}
}
