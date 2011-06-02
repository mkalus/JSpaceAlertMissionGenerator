/**
 * 
 */
package de.beimax.spacealert.exec;

import java.io.FileInputStream;
import java.io.InputStream;

import javazoom.jl.player.Player;
import de.beimax.spacealert.mission.Mission;

/**
 * @author mkalus
 *
 */
public class MissionGeneratorCLI {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Mission mission = new Mission();

		mission.generateMission();
		
		System.out.println(mission);
	}
}