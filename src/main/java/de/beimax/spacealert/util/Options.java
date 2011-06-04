/**
 * 
 */
package de.beimax.spacealert.util;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.*;

/**
 * holds the command line and other options
 * @author mkalus
 */
public class Options {
	/**
	 * singleton reference
	 */
	private static Options optionsSingleton = null;
	
	/**
	 * singleton getter
	 * @return Options
	 */
	public static Options getOptions() {
		if (optionsSingleton == null) {
			optionsSingleton =  new Options();
		}
		return optionsSingleton;
	}
	
	/**
	 * parse the options
	 * @param args command line options
	 * @return true if parsing was ok
	 */
	public static boolean parseOptions(String[] args) {
		Options options = getOptions();
		try {
			new JCommander(options, args);
		} catch (ParameterException e) {
			return false;
		}
		
		return true;
	}
	
	public static void printHelp() {
		Options options = getOptions();
		JCommander jCommander = new JCommander(options);
		jCommander.setProgramName("JSpaceAlertMissionGenerator.jar");
		jCommander.usage();
	}
	
	/**
	 * hide default constructor
	 */
	private Options() {}

	// Options come here
	/**
	 * silent mode - do not print banner and the like
	 */
	@Parameter(names = { "--help", "-h" }, description = "Print help and exit")
	public boolean help = false;

	/**
	 * silent mode - do not print banner and the like
	 */
	@Parameter(names = { "--silent", "-s" }, description = "Silent mode")
	public boolean silent = false;
	
	/**
	 * turn on debug mode (set to fine)
	 */
	@Parameter(names = { "--debug", "-D" }, description = "Debug mode")
	public boolean debug = false;
	
	/**
	 * list of output commands
	 */
	@Parameter(names = { "--output", "-o" }, description = "Output commands e.g. text, XML, MP3")
	public List<String> output = new ArrayList<String>();
	
	/**
	 * output filename prefix
	 */
	@Parameter(names = { "--filename", "-f" }, description = "Output file name prefix")
	public String outPutfilePrefix = "mission";
	
	/**
	 * list of print commands
	 */
	@Parameter(names = { "--print", "-p" }, description = "Print commands e.g. text, XML, MP3")
	public List<String> print = new ArrayList<String>();

	/**
	 * do we want to play MP3?
	 */
	@Parameter(names = { "--play" }, description = "Play MP3 after printing and outputting")
	public boolean play = false;
	
	/**
	 * start GUI?
	 */
	@Parameter(names = { "--gui", "-g" }, description = "Start GUI", hidden = true) //hidden at the moment
	public boolean gui = false;
}
