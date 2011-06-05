/**
 * 
 */
package de.beimax.spacealert.mission;

/**
 * @author mkalus
 *
 */
public class IncomingData implements Event {
	/* (non-Javadoc)
	 * @see de.beimax.spacealert.Event#getLengthInSeconds()
	 */
	public int getLengthInSeconds() {
		return 8;
	}

	/**
	 * very simple
	 */
	@Override
	public String toString() {
		return "Incoming data.";
	}

	/* (non-Javadoc)
	 * @see de.beimax.spacealert.Event#getDescription(int)
	 */
	public String getDescription(int time) {
		return toString();
	}

	/**
	 * get MP3 file names
	 */
	public String getMP3s(int time) {
		return "incoming_data.mp3";
	}
	
	/**
	 * get XML attributes
	 */
	public String getXMLAttributes(int time) {
		return "type=\"IncomingData\"";
	}
}
