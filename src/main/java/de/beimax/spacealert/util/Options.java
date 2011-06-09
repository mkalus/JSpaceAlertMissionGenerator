/**
 * This file is part of the JSpaceAlertMissionGenerator software.
 * Copyright (C) 2011 Maximilian Kalus
 * See http://www.beimax.de/ and https://github.com/mkalus/JSpaceAlertMissionGenerator
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **/
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
	 * start GUI?
	 */
	@Parameter(names = {"--list-renderers", "--renderers", "-l" }, description = "List available renderers and exit")
	public boolean listRenderers = false;

	/**
	 * list of output commands
	 */
	@Parameter(names = { "--output", "-o" }, description = "Write mission to file using renderer, can be repeated for more renderes")
	public List<String> output = new ArrayList<String>();
	
	/**
	 * output filename prefix
	 */
	@Parameter(names = { "--filename", "-f" }, description = "Output file name prefix")
	public String outPutfilePrefix = "mission";
	
	/**
	 * list of print commands
	 */
	@Parameter(names = { "--print", "-p" }, description = "Print mission to screen using renderer, can be repeated for more renderes")
	public List<String> print = new ArrayList<String>();

	/**
	 * do we want to play MP3?
	 */
	@Parameter(names = { "--play", "-x" }, description = "Play MP3 after printing and outputting")
	public boolean play = false;
	
	/**
	 * print the seed of the number generator
	 */
	@Parameter(names = { "--clips-folder", "-c" }, description = "Folder in which MP3 clips are stored")
	public String clipsFolder = "clips";

	/**
	 * seed for random number generator
	 */
	@Parameter(names = { "--seed", "-S" }, description = "Set a random number generator seed")
	public Long seed = null;
	
	/**
	 * print the seed of the number generator
	 */
	@Parameter(names = "--print-seed", description = "Print the seed number at the beginning of the mission")
	public boolean printSeed = false;
	
	/**
	 * start GUI?
	 */
	@Parameter(names = { "--gui", "-g" }, description = "Start GUI")
	public boolean gui = false;
}
