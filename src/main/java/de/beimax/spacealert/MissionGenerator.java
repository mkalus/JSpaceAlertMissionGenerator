/**
 * 
 */
package de.beimax.spacealert;

import java.io.File;

import de.beimax.spacealert.util.MavenProperties;
import de.beimax.spacealert.util.Options;

/**
 * @author mkalus
 *
 */
public class MissionGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// parse options
		Options.parseOptions(args);
		Options options = Options.getOptions();
		
		// output banner
		if (!options.silent) {
			// get version number from META-INF file
			MavenProperties mavenProperties = new MavenProperties("de.beimax.spacealert", "JSpaceAlertMissionGenerator");
			System.out.println("Java Space Alert Mission Generator - v" + mavenProperties.getVersionNumber() + "\nBuild: " + mavenProperties.getVersionTimestamp());
		}
		
		// what do we want to outpout?
		//TODO
		
/*		// generate default mission
		Mission mission = new MissionImpl();
		mission.generateMission();
		
		// print out mission information
		System.out.println(mission);
		
		if (!checkForClipDirectory()) {
			System.out.println("In order to play the MP3 clips, you need to download a set of MP3 files and save them in a directory named clips in the same directory as the jar.\nLook at http://sites.google.com/site/boardgametools/SpaceAlertMissionGenerator.\nGerman and English Sound sets are included in the the Space Alert Mission Generator. You can also look into the forums on http://www.boardgamegeek.com/ which provide some language files for Japanese and so on.");
			System.exit(-1);
		}
		
		// create mp3 player
		MP3MissionPlayer player = new MP3MissionPlayer(mission);

		// start it in a new thread
		Thread playerThread = new Thread(player);
		playerThread.start();*/
	}
	
	/**
	 * check for the existence of the clip directory
	 * @return
	 */
	public static boolean checkForClipDirectory() {
		File file = new File("clips");
		if (!file.exists() || !file.isDirectory()) return false;
		return true;
	}
}
