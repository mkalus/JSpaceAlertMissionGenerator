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
	
	/**
	 * get flash player code
	 */
	public String getFlashPlayerCode(int time) {
		return "CS" + length;
	}
}
