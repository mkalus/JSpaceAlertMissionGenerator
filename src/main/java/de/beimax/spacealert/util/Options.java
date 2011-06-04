/**
 * 
 */
package de.beimax.spacealert.util;

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
	 */
	public static void parseOptions(String[] args) {
		Options options = getOptions();
		new JCommander(options, args);
	}
	
	// Options come here
	@Parameter(names = { "--silent", "-s" }, description = "Silent mode (do not output more stuff than needed)")
	public boolean silent = false;
	
	// Options come here
	@Parameter(names = { "--debug", "-D" }, description = "Debug mode")
	public boolean debug = false;
	
	/**
	 * hide default constructor
	 */
	private Options() {}
}
