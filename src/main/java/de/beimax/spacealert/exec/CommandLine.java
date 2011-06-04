/**
 * 
 */
package de.beimax.spacealert.exec;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.beimax.spacealert.mission.Mission;
import de.beimax.spacealert.mission.MissionImpl;
import de.beimax.spacealert.mp3.MP3MissionPlayer;
import de.beimax.spacealert.render.Renderer;
import de.beimax.spacealert.util.Options;

/**
 * command line thread
 * @author mkalus
 */
public class CommandLine extends Thread {
	static private Logger logger = Logger.getLogger("CommandLine");
	static {
		// debugging option set?
		if (Options.getOptions().debug) logger.setLevel(Level.FINEST);
		else logger.setLevel(Level.WARNING);
	}

	/**
	 * start CLI thread
	 */
	@Override
	public void run() {
		logger.fine("Started command line thread.");
		
		// get options
		Options options = Options.getOptions();
		
		// get renderers to check correct input
		List<Renderer> printRenderers;
		List<Renderer> outputRenderers;
		try {
			printRenderers = getRendererClasses(options.print);
			outputRenderers = getRendererClasses(options.output);
		} catch (Exception e) {
			System.out.println("Output/print options are incorrect: " + e.getMessage());
			return;
		}
		
		// generate default mission
		Mission mission = new MissionImpl();
		mission.generateMission();

		// call print and out for each renderer
		render(mission, printRenderers, false); // print
		printRenderers = null; // free memory
		render(mission, outputRenderers, true); // output
		outputRenderers = null; // free memory
		
		// play MP3 if needed
		playMP3(mission);
		
		logger.fine("Stopped command line thread.");
	}
	
	/**
	 * get renderers from Strings
	 * @param rendererNames
	 * @return
	 */
	private List<Renderer> getRendererClasses(List<String> rendererNames) throws Exception {
		if (rendererNames == null) return null; //sanity check
		
		List<Renderer> rendererList = new ArrayList<Renderer>(rendererNames.size());
		for (String renderer : rendererNames) {
			// bring into firstUpper, lower case format
			renderer = upFirstLowerRest(renderer);
			try {
				rendererList.add((Renderer) Class.forName("de.beimax.spacealert.render." + renderer + "Renderer").newInstance());
			} catch (ClassNotFoundException e) {
				throw new Exception("Could not find renderer \"" + renderer + "\".");
			}
		}

		return rendererList;
	}
	
	/**
	 * helper to create Upper case first character and lower case rest of a given string
	 * @param s
	 * @return
	 */
	public String upFirstLowerRest(String s) {
		return (s.length() > 0) ? Character.toUpperCase(s.charAt(0))
				+ s.substring(1).toLowerCase() : s;
	}
	
	/**
	 * output/print mission using renderes
	 * @param mission to render
	 * @param renderers list of renderers
	 * @param output true for output, false for print
	 */
	private void render(Mission mission, List<Renderer> renderers, boolean output) {
		if (renderers == null) return; // sanity check

		// get options
		Options options = Options.getOptions();

		// traverse renderers and print or output missions
		boolean succeeded;
		for (Renderer renderer : renderers) {
			if (output) {
				logger.info("Renderer " + renderer.toString() + ": output mission.");
				succeeded = renderer.output(mission);
			} else {
				logger.info("Renderer " + renderer.toString() + ": print mission.");
				succeeded = renderer.print(mission);
			}
			if (!succeeded) {
				if (!options.silent)
					System.out.println("Renderer " + renderer.toString() + " does not support " + (output?"output":"print") + ".");
				logger.warning("Renderer " + renderer.toString() + " does not support " + (output?"output":"print") + ".");
			}
		}
	}
	
	/**
	 * play MP3 if option is set to do so
	 *  @param mission to play
	 */
	private void playMP3(Mission mission) {
		// get options
		Options options = Options.getOptions();

		// do we want to play mp3 at all?
		if (!options.play) return;
		
		// check for clips directory
		if (!checkForClipDirectory()) {
			System.out.println("In order to play the MP3 clips, you need to download a set of MP3 files and save them in a directory named clips in the same directory as the jar.\nLook at http://sites.google.com/site/boardgametools/SpaceAlertMissionGenerator.\nGerman and English Sound sets are included in the the Space Alert Mission Generator. You can also look into the forums on http://www.boardgamegeek.com/ which provide some language files for Japanese and so on.");
			return;
		}
		
		logger.info("Starting MP3 playback thread.");
		if (!options.silent) System.out.println("Starting MP3 playback.");
		
		// create mp3 player
		MP3MissionPlayer player = new MP3MissionPlayer(mission);

		// start it in a new thread
		Thread playerThread = new Thread(player);
		playerThread.start();
	}

	/**
	 * check for the existence of the clip directory
	 * @return
	 */
	private boolean checkForClipDirectory() {
		File file = new File("clips");
		if (!file.exists() || !file.isDirectory()) return false;
		return true;
	}
}