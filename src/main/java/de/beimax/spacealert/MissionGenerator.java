/**
 * 
 */
package de.beimax.spacealert;

import de.beimax.spacealert.exec.CommandLine;
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
		boolean optionsOk = Options.parseOptions(args);
		Options options = Options.getOptions();
		
		// output banner
		if (!options.silent || !optionsOk) {
			// get version number from META-INF file
			MavenProperties mavenProperties = new MavenProperties("de.beimax.spacealert", "JSpaceAlertMissionGenerator");
			System.out.println("Java Space Alert Mission Generator - v" + mavenProperties.getVersionNumber() + "\nBuild: " + mavenProperties.getVersionTimestamp());
		}
		
		// start GUI?
		if (options.gui && !options.help) {
			System.out.println("GUI is not implemented yet.");
			// this will redirect to the GUI once implemented
			return;
		}
		
		// list renderers
		if (options.listRenderers) {
			printRenderers();
			return;
		}

		// print help in certain cases:
		// - parsing error
		// - help was requested
		// - no output/print/play option was given
		if (!optionsOk || options.help || (!options.play && options.output.size() == 0 && options.print.size() == 0)) {
			Options.printHelp();
			printRenderers();
			printExamples();
			if (!optionsOk) System.out.println("Incorrect options.");
			return;
		}
		
		// command line execution
		new CommandLine().start();
	}
	
	/**
	 * print a list of renderers
	 */
	public static void printRenderers() {
		System.out.println("Available renderers:\n - text\n - XML\n - MP3\n");
		//TODO: list of renderers should be dynamic in some way
		// I cannot access JAR content, but maybe I can get maven to add a list of Renderers to manifest or so
	}
	
	/**
	 * print some example uses
	 */
	public static void printExamples() {
		System.out.println("Some Examples:\n\njava -jar JSpaceAlertMissionGenerator.jar -p text -play\n - Print mission as English text file and start playing MP3s.\n\n -jar JSpaceAlertMissionGenerator.jar -o XML -p text -p MP3\n - Print mission as text on screen, save it as XML and\n   start playing MP3 (\"print\" MP3 translates to --play).");
	}
}
