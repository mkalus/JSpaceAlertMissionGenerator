/**
 * 
 */
package de.beimax.spacealert.exec;

import de.beimax.spacealert.Mission;

/**
 * @author mkalus
 *
 */
public class MissionGeneratorCLI {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Mission mission = new Mission();

		mission.generateMission();
	}

}
