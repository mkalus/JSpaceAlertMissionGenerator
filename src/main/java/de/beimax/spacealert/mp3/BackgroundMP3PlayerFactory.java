/**
 * This file is part of the JSpaceAlertMissionGenerator software.
 * Copyright (C) 2012 Maximilian Kalus
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
package de.beimax.spacealert.mp3;

import java.util.logging.Logger;

/**
 * @author mkalus
 *
 */
public class BackgroundMP3PlayerFactory {
	static protected Logger logger = Logger.getLogger("BackgroundMP3PlayerFactory");

	/**
	 * Factory method to get player from setting
	 * @param type
	 * @return BackgroundMP3Player
	 */
	public static BackgroundMP3Player getBackgroundMP3Player(String type) {
		// to lower case
		type = type.toLowerCase();
		
		if (type.equals("fixed")) {
			return new BackgroundMP3PlayerFixed();
		}
		if (type.equals("none")) {
			return new BackgroundMP3PlayerNone();
		}
		// print warning if not having set this correctly
		if (!type.equals("normal")) {
			logger.warning("Incorrect alarm setting " + type + ". Fallback to \"normal.\"");
		}
		// return normal player
		return new BackgroundMP3PlayerNormal();
	}
}
