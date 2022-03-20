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
	private static Options optionsSingleton;
	
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
	 * print help and exit
	 */
	@Parameter(names = { "--help", "-h" }, description = "Print help and exit")
	public boolean help;

	/**
	 * silent mode - do not print banner and the like
	 */
	@Parameter(names = { "--silent", "-s" }, description = "Silent mode")
	public boolean silent;
	
	/**
	 * turn on debug mode (set to fine)
	 */
	@Parameter(names = { "--debug", "-D" }, description = "Debug mode")
	public boolean debug;
	
	/**
	 * start GUI?
	 */
	@Parameter(names = {"--list-renderers", "--renderers", "-L" }, description = "List available renderers and exit")
	public boolean listRenderers;

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
	public boolean play;

	/**
	 * do we want to play MP3?
	 */
	@Parameter(names = { "--alarm" }, description = "Set background alarm (normal,none,fixed). Fixed means that background alarm will not become louder.")
	public String backgroundAlarm = "normal";

	/**
	 * folder in which MP3 clips are stored
	 */
	@Parameter(names = { "--clips-folder", "-c" }, description = "Folder in which MP3 clips are stored")
	public String clipsFolder = "clips";

	/**
	 * seed for random number generator
	 */
	@Parameter(names = { "--seed", "-S" }, description = "Set a random number generator seed")
	public Long seed;

	/**
	 * start GUI?
	 */
	@Parameter(names = { "--gui", "-g" }, description = "Start GUI")
	public boolean gui;

	/**
	 *
	 */

	/**
	 * threat level
	 */
	@Parameter(names = { "--threat-level", "-l" }, description = "Threat level of mission (should be <= 14)")
	public int threatLevel = 8;
	
	/**
	 * unconfirmed threats
	 */
	@Parameter(names = { "--unconfirmed-threat-level", "-u" }, description = "Threat level unconfirmed threats")
	public int threatUnconfirmed = 1;

	/**
	 * ...of which x levels are internal
	 */
	@Parameter(names = { "--min-internal-level", "-i" }, description = "Minimum levels of internal threats")
	public int minInternalThreats = 1;
	@Parameter(names = { "--max-internal-level", "-I" }, description = "Maximum levels of internal threats")
	public int maxInternalThreats = 3;
	@Parameter(names = { "--max-internal-threats" }, description = "Maximum number of internal threats")
	public int maxInternalThreatsNumber = 2; // number of internal threats max

	/**
	 * enable double threats - see "The New Frontier"
	 */
	@Parameter(names = { "--allow-double-threats"}, description = "Enable double threats (\"The New Frontier\": one external and internal threat will occur on one time slot)")
	public boolean enableDoubleThreats = false;

	/**
	 * minimum and maximum time in which normal threats can occur
	 */
	@Parameter(names = { "--min-time-normal-external" }, description = "Minimum time in which normal external threats will occur")
	public int minTNormalExternalThreat = 1;
	@Parameter(names = { "--max-time-normal-external" }, description = "Maximum time in which normal external threats will occur")
	public int maxTNormalExternalThreat = 8;
	
	/**
	 * minimum and maximum time in which serious threats can occur
	 */
	@Parameter(names = { "--min-time-serious-external" }, description = "Minimum time in which serious external threats will occur")
	public int minTSeriousExternalThreat = 2;
	@Parameter(names = { "--max-time-serious-external" }, description = "Maximum time in which serious external threats will occur")
	public int maxTSeriousExternalThreat = 7;
	
	/**
	 * minimum and maximum time in which normal threats can occur
	 */
	@Parameter(names = { "--min-time-normal-internal" }, description = "Minimum time in which normal internal threats will occur")
	public int minTNormalInternalThreat = 2;
	@Parameter(names = { "--max-time-normal-internal" }, description = "Maximum time in which normal internal threats will occur")
	public int maxTNormalInternalThreat = 7;
	
	/**
	 * minimum and maximum time in which serious threats can occur
	 */
	@Parameter(names = { "--min-time-serious-internal" }, description = "Minimum time in which serious internal threats will occur")
	public int minTSeriousInternalThreat = 3;
	@Parameter(names = { "--max-time-serious-internal" }, description = "Maximum time in which serious internal threats will occur")
	public int maxTSeriousInternalThreat = 6;
	
	/**
	 * minimum data operations (either data transfer or incoming data)
	 */
	@Parameter(names = { "--min-data-operations-1" }, description = "Minimum data operations in phase 1")
	public int minDataOperations1 = 2;
	@Parameter(names = { "--max-data-operations-1" }, description = "Maximum data operations in phase 1")
	public int maxDataOperations1 = 3;
	@Parameter(names = { "--min-data-operations-2" }, description = "Minimum data operations in phase 2")
	public int minDataOperations2 = 2;
	@Parameter(names = { "--max-data-operations-2" }, description = "Maximum data operations in phase 2")
	public int maxDataOperations2 = 3;
	@Parameter(names = { "--min-data-operations-3" }, description = "Minimum data operations in phase 3")
	public int minDataOperations3;
	@Parameter(names = { "--max-data-operations-3" }, description = "Maximum data operations in phase 3")
	public int maxDataOperations3 = 1;
	
	/**
	 * minimum and maximum incoming data by phases
	 */
	@Parameter(names = { "--min-incoming-data-1" }, description = "Minimum incoming data in phase 1")
	public int minIncomingData1 = 1;
	@Parameter(names = { "--max-incoming-data-1" }, description = "Maximum incoming data in phase 1")
	public int maxIncomingData1 = 3;
	@Parameter(names = { "--min-incoming-data-2" }, description = "Minimum incoming data in phase 2")
	public int minIncomingData2;
	@Parameter(names = { "--max-incoming-data-2" }, description = "Maximum incoming data in phase 2")
	public int maxIncomingData2 = 2;
	@Parameter(names = { "--min-incoming-data-3" }, description = "Minimum incoming data in phase 3")
	public int minIncomingData3;
	@Parameter(names = { "--max-incoming-data-3" }, description = "Maximum incoming data in phase 3")
	public int maxIncomingData3 = 2;
	@Parameter(names = { "--min-incoming-data" }, description = "Minimum incoming data in all phases")
	public int minIncomingDataTotal = 2;
	
	/**
	 * minimum and maximum data transfers by phases
	 */
	@Parameter(names = { "--min-data-transfers-1" }, description = "Minimum data transfers (data operation and incoming data) in phase 1")
	public int minDataTransfer1;
	@Parameter(names = { "--max-data-transfers-1" }, description = "Maximum data transfers (data operation and incoming data) in phase 1")
	public int maxDataTransfer1 = 1;
	@Parameter(names = { "--min-data-transfers-2" }, description = "Minimum data transfers (data operation and incoming data) in phase 2")
	public int minDataTransfer2 = 1;
	@Parameter(names = { "--max-data-transfers-2" }, description = "Maximum data transfers (data operation and incoming data) in phase 2")
	public int maxDataTransfer2 = 2;
	@Parameter(names = { "--min-data-transfers-3" }, description = "Minimum data transfers (data operation and incoming data) in phase 3")
	public int minDataTransfer3 = 1;
	@Parameter(names = { "--max-data-transfers-3" }, description = "Maximum data transfers (data operation and incoming data) in phase 3")
	public int maxDataTransfer3 = 1;
	@Parameter(names = { "--min-data-transfers" }, description = "Minimum data transfers (data operation and incoming data) in all phases")
	public int minDataTransferTotal = 3;
	
	/**
	 * minimum and maximum time for white noise
	 */
	@Parameter(names = { "--min-whitenoise-total" }, description = "Minimum time of whitenoise in total")
	public int minWhiteNoise = 45;
	@Parameter(names = { "--max-whitenoise-total" }, description = "Maximum time of whitenoise in total")
	public int maxWhiteNoise = 60;
	@Parameter(names = { "--min-whitenoise-single" }, description = "Minimum time of whitenoise of a single communication break")
	public int minWhiteNoiseTime = 9;
	@Parameter(names = { "--max-whitenoise-single" }, description = "Maximum time of whitenoise of a single communication break")
	public int maxWhiteNoiseTime = 20;
	
	/**
	 * minimum and maximum time for phases
	 */
	@Parameter(names = { "--min-phase-time-1" }, description = "Minimum phase time for phase 1")
	public int minPhaseTime1 = 205;
	@Parameter(names = { "--max-phase-time-1" }, description = "Maximum phase time for phase 1")
	public int maxPhaseTime1 = 240;
	@Parameter(names = { "--min-phase-time-2" }, description = "Minimum phase time for phase 2")
	public int minPhaseTime2 = 180;
	@Parameter(names = { "--max-phase-time-2" }, description = "Maximum phase time for phase 2")
	public int maxPhaseTime2 = 225;
	@Parameter(names = { "--min-phase-time-3" }, description = "Minimum phase time for phase 3")
	public int minPhaseTime3 = 140;
	@Parameter(names = { "--max-phase-time-3" }, description = "Maximum phase time for phase 3")
	public int maxPhaseTime3 = 155;
	
	/**
	 * times for first threats to appear
	 */
	@Parameter(names = { "--min-time-for-threat-1" }, description = "Minimum time for first threat to appear in phase 1")
	public int minTimeForFirst1 = 10;
	@Parameter(names = { "--max-time-for-threat-1" }, description = "Maximum time for first threat to appear in phase 1")
	public int maxTimeForFirst1 = 20;
	@Parameter(names = { "--min-time-for-threat-2" }, description = "Minimum time for first threat to appear in phase 2")
	public int minTimeForFirst2 = 10;
	@Parameter(names = { "--max-time-for-threat-2" }, description = "Maximum time for first threat to appear in phase 2")
	public int maxTimeForFirst2 = 40;
	
	/**
	 * chance for ambush in phases 4/8 in %
	 */
	@Parameter(names = { "--chance-for-ambush-1" }, description = "Chance in percent for ambush in phase 4")
	public int chanceForAmbush1 = 40;
	@Parameter(names = { "--chance-for-ambush-2" }, description = "Chance in percent for ambush in phase 8")
	public int chanceForAmbush2 = 40;
	
	/**
	 * "middle" threats (2+3/5+6) should appear with % of phase length
	 */
	@Parameter(names = { "--threats-within-percent" }, description = "Threats appear within this percentage of a phase")
	public int threatsWithInPercent = 70;
}
