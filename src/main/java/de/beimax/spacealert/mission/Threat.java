/**
 * 
 */
package de.beimax.spacealert.mission;

import java.io.File;

/**
 * Threat class
 * 
 * @author mkalus
 */
public class Threat implements Event {
	public static final int THREAT_LEVEL_NORMAL = 1;
	public static final int THREAT_LEVEL_SERIOUS = 2;

	public static final int THREAT_POSITION_INTERNAL = 1;
	public static final int THREAT_POSITION_EXTERNAL = 2;
	
	public static final int THREAT_SECTOR_BLUE = 1;
	public static final int THREAT_SECTOR_WHITE = 2;
	public static final int THREAT_SECTOR_RED = 3;
	
	/**
	 * assumed time in seconds: 10
	 */
	public int getLengthInSeconds() {
		return 10;
	}
	
	/**
	 * Threat level
	 */
	private int threatLevel = 0;
	
	/**
	 * internal/external threat
	 */
	private int threatPosition = 0;
	
	/**
	 * sector of threat
	 */
	private int sector = 0;
	
	/**
	 * time in which threat appears
	 */
	private int time = 0;
	
	/**
	 * confirmed threat?
	 */
	private boolean confirmed = false;

	/**
	 * @return the threatLevel
	 */
	public int getThreatLevel() {
		return threatLevel;
	}

	/**
	 * @param threatLevel the threatLevel to set
	 */
	public void setThreatLevel(int threatLevel) {
		this.threatLevel = threatLevel;
	}

	/**
	 * @return the threatPosition
	 */
	public int getThreatPosition() {
		return threatPosition;
	}

	/**
	 * @param threatPosition the threatPosition to set
	 */
	public void setThreatPosition(int threatPosition) {
		this.threatPosition = threatPosition;
	}

	/**
	 * @return the sector
	 */
	public int getSector() {
		return sector;
	}

	/**
	 * @param sector the sector to set
	 */
	public void setSector(int sector) {
		this.sector = sector;
	}

	/**
	 * @return the time
	 */
	public int getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * @return the confirmed
	 */
	public boolean isConfirmed() {
		return confirmed;
	}

	/**
	 * @param confirmed the confirmed to set
	 */
	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}
	
	/**
	 * string representation
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		// unconfirmed wrapper?
		if (!confirmed) sb.append("[Unconfirmed: ");
		// add time
		sb.append("Time T+").append(time).append(". ");
		// internal/external?
		if (threatPosition == THREAT_POSITION_INTERNAL) {
			if (threatLevel == THREAT_LEVEL_SERIOUS) {
				sb.append("Serious internal threat");
			} else sb.append("Internal threat");
		} else if (threatLevel == THREAT_LEVEL_SERIOUS) {
			sb.append("Serious threat");
		} else sb.append("Threat");
		sb.append('.');
		if (threatPosition != THREAT_POSITION_INTERNAL) {
			sb.append(" Zone ");
			switch (sector) {
			case THREAT_SECTOR_BLUE: sb.append("blue"); break;
			case THREAT_SECTOR_WHITE: sb.append("white"); break;
			case THREAT_SECTOR_RED: sb.append("red"); break;
			default: sb.append("unknown");
			}
			sb.append('.');
		}
		if (!confirmed) sb.append(']');

		/*s.append("Threat [");
		// unconfirmed?
		if (!confirmed) s.append("unconfirmed ");
		// position?
		switch (threatPosition) {
		case THREAT_POSITION_EXTERNAL: break;
		case THREAT_POSITION_INTERNAL: s.append("internal "); break;
		default: s.append("non-positioned ");
		}
		// add level
		switch (threatLevel) {
		case THREAT_LEVEL_NORMAL: s.append("threat"); break;
		case THREAT_LEVEL_SERIOUS: s.append("serious threat"); break;
		default: s.append("unknown threat");
		}
		s.append("; T+");
		s.append(time);
		if (threatPosition != THREAT_POSITION_INTERNAL) {
			s.append(" in sector ");
			switch (sector) {
			case THREAT_SECTOR_BLUE: s.append("blue"); break;
			case THREAT_SECTOR_WHITE: s.append("white"); break;
			case THREAT_SECTOR_RED: s.append("red"); break;
			default: s.append("unknown");
			}
		}
		s.append(']');*/
		
		return sb.toString();
	}

	/**
	 * Get Description
	 */
	public String getDescription(int time) {
		return toString();
	}

	/**
	 * get MP3 file names
	 */
	public String getMP3s(int time) {
		StringBuilder sb = new StringBuilder();
		
		// check clip directory for english or german type files
		// ...why did these guys even split this up?
		boolean german = false;
		if (new File("clips" + File.separator + "threat_zone_blue.mp3").isFile()) german = true;
		
		// start
		if (!confirmed) sb.append("unconfirmed_report.mp3,");
		sb.append("time_t_plus_").append(this.time).append(".mp3,");

		// internal/external?
		if (threatPosition == THREAT_POSITION_INTERNAL) {
			if (threatLevel == THREAT_LEVEL_SERIOUS) {
				sb.append("serious_internal_threat");
			} else sb.append("internal_threat");
		} else if (threatLevel == THREAT_LEVEL_SERIOUS) {
			sb.append("serious_threat");
		} else sb.append("threat");
		if (threatPosition != THREAT_POSITION_INTERNAL) {
			if (german) sb.append("_zone_");
			else sb.append(".mp3,zone_"); // language dependent threats
			switch (sector) {
			case THREAT_SECTOR_BLUE: sb.append("blue"); break;
			case THREAT_SECTOR_WHITE: sb.append("white"); break;
			case THREAT_SECTOR_RED: sb.append("red"); break;
			}
		}		
		sb.append(".mp3");
		
		// main message
		String message = sb.toString();
		
		// clean and but together
		sb = new StringBuilder();
		
		sb.append("alert.mp3,").append(message).append(",repeat.mp3,").append(message);

		return sb.toString();
	}
}
