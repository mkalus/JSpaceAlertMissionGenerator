/**
 * 
 */
package de.beimax.spacealert.mission;

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
		return length + 2; // +2 to avoid collisions with other anouncements/ leave a little space between
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

	/**
	 * get MP3 file names
	 */
	public String getMP3s(int time) {
		StringBuilder sb = new StringBuilder();
		
		// calculate time of white noise
		sb.append("communications_down.mp3,white_noise.mp3:").append(length - 2).append(",communications_restored.mp3");

		return sb.toString();
	}
	
	/**
	 * get XML attributes
	 */
	public String getXMLAttributes(int time) {
		return "type=\"CommFailure\" duration=\"" + length + "000\"";
	}
}
