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
		
		// print help in certain cases:
		// - parsing error
		// - help was requested
		// - no output/print/play option was given
		if (!optionsOk || options.help || (!options.play && options.output.size() == 0 && options.print.size() == 0)) {
			Options.printHelp();
			if (!optionsOk) System.out.println("Incorrect options.");
			return;
		}
		
		// command line execution
		new CommandLine().start();
	}
}
