/**
 * 
 */
package de.beimax.spacealert.render;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import de.beimax.spacealert.mission.Announcement;
import de.beimax.spacealert.mission.Event;
import de.beimax.spacealert.mission.Mission;
import de.beimax.spacealert.util.Options;

/**
 * @author mkalus
 *
 */
public class FlashplayercodeRenderer implements Renderer {

	/* (non-Javadoc)
	 * @see de.beimax.spacealert.render.Renderer#print(de.beimax.spacealert.mission.Mission)
	 */
	public boolean print(Mission mission) {
		System.out.println(getMissionCode(mission));
		return true;
	}

	/* (non-Javadoc)
	 * @see de.beimax.spacealert.render.Renderer#output(de.beimax.spacealert.mission.Mission)
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
			FileWriter outFile = new FileWriter(options.outPutfilePrefix + "_flashplayercode.txt");
			outFile.write(getMissionCode(mission));
			outFile.close();
		} catch (IOException e) {
			System.out.println("Error writing text file: " + e.getMessage());
		}

		return true;
	}
	
	/**
	 * returns the mission codes
	 * @param mission
	 * @return
	 */
	private String getMissionCode(Mission mission) {
		StringBuilder sb = new StringBuilder();

		int phaseLength = 0;
		boolean first = true;
		for(Map.Entry<Integer,Event> entry : mission.getMissionEvents().getEntrySet()) {
			int time = entry.getKey();
			String code = entry.getValue().getFlashPlayerCode(time);
			if (code != null) {
				if (entry.getValue() instanceof Announcement) { // Announcements return only phase ends
					int phase = Integer.parseInt(code);
					phaseLength += mission.getMissionPhaseLength(phase);
					time = phaseLength;
					code = "PE" + code;
				}
				if (first) first = false; else sb.append(','); // add commas to later entries
				sb.append(formatTime(time)).append(code); // add code
			}
		}
		
		return sb.toString();
	}

	
	/**
	 * Time formatter for flash texts
	 * @param time
	 * @return
	 */
	private String formatTime(int time) {
		int minute = time/60;
		int seconds = time%60;
		return String.format(minute > 9?"%02d":"%01d", minute) + String.format("%02d", seconds);
	}

	@Override
	public String toString() {
		return "FlashPlayerCode";
	}
}
