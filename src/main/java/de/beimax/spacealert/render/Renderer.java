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
package de.beimax.spacealert.render;

import de.beimax.spacealert.mission.Mission;

/**
 * Renderers print or save missions into stuff
 * @author mkalus
 */
public interface Renderer {
	/**
	 * print mission to standard output
	 * @param mission to be printed
	 * @return true if print is supported
	 */
	public boolean print(Mission mission);

	/**
	 * save mission to file (using filename prefix used in options)
	 * @param mission to be saved to file
	 * @return true if output is supported
	 */
	public boolean output(Mission mission);
}
