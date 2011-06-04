/**
 * 
 */
package de.beimax.spacealert.render;

import java.io.FileWriter;
import java.io.IOException;

import de.beimax.spacealert.mission.Mission;
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
		System.out.println(getMissionText(mission));
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
