/**
 * 
 */
package de.beimax.spacealert.mission;

/**
 * @author mkalus
 *
 */
public class Announcement implements Event {
	public static final int ANNOUNCEMENT_PH1_START = 1;
	public static final int ANNOUNCEMENT_PH1_ONEMINUTE = 2;
	public static final int ANNOUNCEMENT_PH1_TWENTYSECS = 3;
	public static final int ANNOUNCEMENT_PH1_ENDS = 4;
	public static final int ANNOUNCEMENT_PH2_ONEMINUTE = 11;
	public static final int ANNOUNCEMENT_PH2_TWENTYSECS = 12;
	public static final int ANNOUNCEMENT_PH2_ENDS = 13;
	public static final int ANNOUNCEMENT_PH3_ONEMINUTE = 21;
	public static final int ANNOUNCEMENT_PH3_TWENTYSECS = 22;
	public static final int ANNOUNCEMENT_PH3_ENDS = 23;
	
	/**
	 * announcement type
	 */
	private int type;
	
	public Announcement(int type) {
		this.type = type;
	}

	/**
	 * length in seconds depends on type
	 */
	public int getLengthInSeconds() {
		switch (type) {
		case ANNOUNCEMENT_PH1_START: return 10;
		case ANNOUNCEMENT_PH1_ONEMINUTE: return 3;
		case ANNOUNCEMENT_PH1_TWENTYSECS: return 3;
		case ANNOUNCEMENT_PH1_ENDS: return 10;
		case ANNOUNCEMENT_PH2_ONEMINUTE: return 3;
		case ANNOUNCEMENT_PH2_TWENTYSECS: return 3;
		case ANNOUNCEMENT_PH2_ENDS: return 10;
		case ANNOUNCEMENT_PH3_ONEMINUTE: return 3;
		case ANNOUNCEMENT_PH3_TWENTYSECS: return 3;
		case ANNOUNCEMENT_PH3_ENDS: return 12;
		}
		return -1; //error
	}

	/**
	 * length in seconds depends on type
	 */
	public String toString() {
		switch (type) {
		case ANNOUNCEMENT_PH1_START: return "Enemy activity detected. Please begin 1st phase.";
		case ANNOUNCEMENT_PH1_ONEMINUTE: return "1st phase ends in one minute.";
		case ANNOUNCEMENT_PH1_TWENTYSECS: return "1st phase ends in twenty seconds.";
		case ANNOUNCEMENT_PH1_ENDS: return "1st phase ends in 5, 4, 3, 2, 1... 2nd phase begins.";
		case ANNOUNCEMENT_PH2_ONEMINUTE: return "2nd phase ends in one minute.";
		case ANNOUNCEMENT_PH2_TWENTYSECS: return "2nd phase ends in twenty seconds.";
		case ANNOUNCEMENT_PH2_ENDS: return "2nd phase ends in 5, 4, 3, 2, 1... 3rd phase begins.";
		case ANNOUNCEMENT_PH3_ONEMINUTE: return "Operation ends in one minute.";
		case ANNOUNCEMENT_PH3_TWENTYSECS: return "Operation ends in twenty seconds.";
		case ANNOUNCEMENT_PH3_ENDS: return "Operation ends in 5, 4, 3, 2, 1. Mission complete.";
		}
		return "ERROR"; //error
	}

	/**
	 * Get Description
	 */
	public String getDescription(int time) {
		return toString();
	}
}
