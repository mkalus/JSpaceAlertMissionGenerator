/**
 * This file is part of the JSpaceAlertMissionGenerator software.
 * Copyright (C) 2011 Maximilian Kalus
 * See http://www.beimax.de/ and https://github.com/mkalus/JSpaceAlertMissionGenerator
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **/
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
		case ANNOUNCEMENT_PH1_START: return 10; // if you make this higher, also check "- arg" stuff in missionImpl
		case ANNOUNCEMENT_PH1_ONEMINUTE: return 5;
		case ANNOUNCEMENT_PH1_TWENTYSECS: return 5;
		case ANNOUNCEMENT_PH1_ENDS: return 11;
		case ANNOUNCEMENT_PH2_ONEMINUTE: return 5;
		case ANNOUNCEMENT_PH2_TWENTYSECS: return 5;
		case ANNOUNCEMENT_PH2_ENDS: return 10; // if you make this higher, also check "- arg" stuff in missionImpl
		case ANNOUNCEMENT_PH3_ONEMINUTE: return 5;
		case ANNOUNCEMENT_PH3_TWENTYSECS: return 5;
		case ANNOUNCEMENT_PH3_ENDS: return 12;
		default: break;
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
		default: break;
		}
		return "ERROR"; //error
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
		switch (type) {
		case ANNOUNCEMENT_PH1_START: return "begin_first_phase.mp3";
		case ANNOUNCEMENT_PH1_ONEMINUTE: return "first_phase_ends_in_1_minute.mp3";
		case ANNOUNCEMENT_PH1_TWENTYSECS: return "first_phase_ends_in_20_seconds.mp3";
		case ANNOUNCEMENT_PH1_ENDS: return "first_phase_ends.mp3,second_phase_begins.mp3";
		case ANNOUNCEMENT_PH2_ONEMINUTE: return "second_phase_ends_in_1_minute.mp3";
		case ANNOUNCEMENT_PH2_TWENTYSECS: return "second_phase_ends_in_20_seconds.mp3";
		case ANNOUNCEMENT_PH2_ENDS: return "second_phase_ends.mp3,third_phase_begins.mp3";
		case ANNOUNCEMENT_PH3_ONEMINUTE: return "operation_ends_in_1_minute.mp3";
		case ANNOUNCEMENT_PH3_TWENTYSECS: return "operation_ends_in_20_seconds.mp3";
		case ANNOUNCEMENT_PH3_ENDS: return "operation_ends.mp3";
		default: break;
		}
		return null;
	}
	
	/**
	 * get XML attributes
	 */
	public String getXMLAttributes(int time) {
		StringBuilder sb = new StringBuilder();
		sb.append("type=\"Announcement\" message=\"");
		switch (type) {
		case ANNOUNCEMENT_PH1_START: sb.append("AnnounceBeginFirstPhase"); break;
		case ANNOUNCEMENT_PH1_ONEMINUTE: sb.append("AnnounceFirstPhaseEndsInOneMinute"); break;
		case ANNOUNCEMENT_PH1_TWENTYSECS: sb.append("AnnounceFirstPhaseEndsInTwentySeconds"); break;
		case ANNOUNCEMENT_PH1_ENDS: sb.append("AnnounceFirstPhaseEnds"); break; // begin of third phase printed in renderer itself
		case ANNOUNCEMENT_PH2_ONEMINUTE: sb.append("AnnounceSecondPhaseEndsInOneMinute"); break;
		case ANNOUNCEMENT_PH2_TWENTYSECS: sb.append("AnnounceSecondPhaseEndsInTwentySeconds"); break;
		case ANNOUNCEMENT_PH2_ENDS: sb.append("AnnounceSecondPhaseEnds"); break; // begin of third phase printed in renderer itself
		case ANNOUNCEMENT_PH3_ONEMINUTE: sb.append("AnnounceThirdPhaseEndsInOneMinute"); break;
		case ANNOUNCEMENT_PH3_TWENTYSECS: sb.append("AnnounceThirdPhaseEndsInTwentySeconds"); break;
		case ANNOUNCEMENT_PH3_ENDS: sb.append("AnnounceThirdPhaseEnds"); break;
		default: break;
		}
		sb.append('"');
		return sb.toString();
	}
	
	/**
	 * get flash player code
	 */
	public String getFlashPlayerCode(int time) {
		// special here - generally only phase lengths have to be defined
		switch (type) {
		case ANNOUNCEMENT_PH1_ENDS: return "1";
		case ANNOUNCEMENT_PH2_ENDS: return "2";
		case ANNOUNCEMENT_PH3_ENDS: return "3";
		default: break;
		}
		return null;
	}
}
