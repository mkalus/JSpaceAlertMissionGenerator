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

import java.io.FileWriter;
import java.io.IOException;

import de.beimax.spacealert.mission.Mission;
import de.beimax.spacealert.util.ConsoleColorer;
import de.beimax.spacealert.util.Options;

/**
 * Text output renderer
 * @author mkalus
 */
public class TextRenderer implements Renderer {
	/* (non-Javadoc)
	 * @see de.beimax.spacealert.render.Renderer#print()
	 */
	public boolean print(Mission mission) {
		System.out.println(ConsoleColorer.colorLines(getMissionText(mission)));
		return true;
	}

	/* (non-Javadoc)
	 * @see de.beimax.spacealert.render.Renderer#output()
	 */
	public boolean output(Mission mission) {
		// get options
		Options options = Options.getOptions();
		
		// check file name/size
		if (options.outPutfilePrefix == null || options.outPutfilePrefix.isEmpty()) {
			System.out.println("Error writing text file: file prefix is empty.");
			return true;
		}
		
		// write file
		try {
			FileWriter outFile = new FileWriter(options.outPutfilePrefix + ".txt");
			outFile.write(getMissionText(mission));
			outFile.close();
		} catch (IOException e) {
			System.out.println("Error writing text file: " + e.getMessage());
		}

		return true;
	}
	
	/**
	 * creates text representation of mission
	 * @param mission
	 * @return
	 */
	private String getMissionText(Mission mission) {
		return mission.toString();
	}
	
	@Override
	public String toString() {
		return "text";
	}
}
