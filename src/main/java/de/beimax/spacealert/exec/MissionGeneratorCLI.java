/**
 * 
 */
package de.beimax.spacealert.exec;

import de.beimax.spacealert.mission.Mission;
import de.beimax.spacealert.mission.MissionImpl;
import de.beimax.spacealert.mp3.MP3MissionPlayer;

/**
 * @author mkalus
 *
 */
public class MissionGeneratorCLI {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// generate default mission
		Mission mission = new MissionImpl();
		mission.generateMission();
		
		// print out mission information
		System.out.println(mission);
		
		// create mp3 player
		MP3MissionPlayer player = new MP3MissionPlayer(mission);

		// start it in a new thread
		Thread playerThread = new Thread(player);
		playerThread.start();
	}
}
