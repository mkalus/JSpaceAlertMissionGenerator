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
public class IncomingData implements Event {
	/* (non-Javadoc)
	 * @see de.beimax.spacealert.Event#getLengthInSeconds()
	 */
	public int getLengthInSeconds() {
		return 10;
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
	public String getMP3s() {
		return "incoming_data.mp3";
	}
	
	/**
	 * get XML attributes
	 */
	public String getXMLAttributes(int time) {
		return "type=\"IncomingData\"";
	}
	
	/**
	 * get flash player code
	 */
	public String getFlashPlayerCode(int time) {
		return "ID";
	}
}
