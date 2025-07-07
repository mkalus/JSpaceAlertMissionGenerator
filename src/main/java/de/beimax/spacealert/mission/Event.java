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
 * Event that can occur on missions...
 * @author mkalus
 *
 */
public interface Event {
	/**
	 * Get length of event in seconds
	 * 
	 * @return length of the event in seconds
	 */
	public int getLengthInSeconds();
	
	/**
	 * Get description
	 * 
	 * @param time at which the event occurs (some events need to know)
	 * @return English description like the one on the cards
	 */
	public String getDescription(int time);
	
	/**
	 * Get a list of MP3 files to play
	 * @return comma separated list of MP3s
	 */
	public String getMP3s();
	
	/**
	 * return XML attributes for XML output
	 * @return
	 */
	public String getXMLAttributes(int time);
	
	/**
	 * return Flash player code
	 * @return
	 */
	public String getFlashPlayerCode(int time);
}
