/**
 * 
 */
package de.beimax.spacealert.mission;

/**
 * @author mkalus
 *
 */
public class DataTransfer implements Event {

	/* (non-Javadoc)
	 * @see de.beimax.spacealert.Event#getLengthInSeconds()
	 */
	public int getLengthInSeconds() {
		return 16; //fairly long
	}

	/**
	 * very simple
	 */
	@Override
	public String toString() {
		return "Data transfer. Data transfer in 5, 4, 3, 2, 1.";
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
		return "data_transfer.mp3";
	}
	
	/**
	 * get XML attributes
	 */
	public String getXMLAttributes(int time) {
		return "type=\"DataTransfer\"";
	}
}
