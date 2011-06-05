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
import de.beimax.spacealert.util.Options;

/**
 * @author mkalus
 *
 */
public class Mp3Renderer implements Renderer {

	/* (non-Javadoc)
	 * @see de.beimax.spacealert.render.Renderer#output(de.beimax.spacealert.mission.Mission)
	 */
	public boolean print(Mission mission) {
		// convert output MP3 to play
		Options options = Options.getOptions();
		if (!options.silent) {
			System.out.println("Converting --print MP3 to --play.");
		}
		options.play = true;
		return true;
	}

	/* (non-Javadoc)
	 * @see de.beimax.spacealert.render.Renderer#print(de.beimax.spacealert.mission.Mission)
	 */
	public boolean output(Mission mission) {
		/*// get options
		Options options = Options.getOptions();
		
		// check file name/size
		if (options.outPutfilePrefix == null || options.outPutfilePrefix.isEmpty()) {
			System.out.println("Error writing MP3 file: file prefix is empty.");
		}
		
		// TODO not yet supported
		return false;*/
		
		System.out.println("Sorry, MP3 export to files is not supported.");
		return true;
	}

	@Override
	public String toString() {
		return "MP3";
	}
}
