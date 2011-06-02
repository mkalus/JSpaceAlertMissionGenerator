/**
 * 
 */
package de.beimax.spacealert;

/**
 * @author mkalus
 *
 */
public class IncomingData implements Event {
	/* (non-Javadoc)
	 * @see de.beimax.spacealert.Event#getLengthInSeconds()
	 */
	public int getLengthInSeconds() {
		return 6;
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

}
