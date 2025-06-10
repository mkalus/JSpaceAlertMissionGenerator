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
package de.beimax.spacealert;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import de.beimax.spacealert.exec.CommandLine;
import de.beimax.spacealert.exec.Gui;
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
			System.out.println("Java Space Alert Mission Generator - v" + mavenProperties.getVersionNumber() + "\nBuild: " + mavenProperties.getVersionTimestamp() + "\nThis program is free software published under the GPL v3.0");
		}
		
		// start GUI?
		if (options.gui && !options.help) {
			new Gui();
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
		if (!optionsOk || options.help || (!options.play && options.output.isEmpty() && options.print.isEmpty())) {
			Options.printHelp();
			printRenderers();
			printExamples();
			if (!optionsOk) System.out.println("Incorrect options.");
			return;
		}
		if (options.debug) {
			  //get the top Logger:
		    Logger topLogger = java.util.logging.Logger.getLogger("");

		    // Handler for console (reuse it if it already exists)
		    Handler consoleHandler = null;
		    //see if there is already a console handler
		    for (Handler handler : topLogger.getHandlers()) {
		        if (handler instanceof ConsoleHandler) {
		            //found the console handler
		            consoleHandler = handler;
		            break;
		        }
		    }


		    if (consoleHandler == null) {
		        //there was no console handler found, create a new one
		        consoleHandler = new ConsoleHandler();
		        topLogger.addHandler(consoleHandler);
		    }
		    //set the console handler to fine:
		    consoleHandler.setLevel(java.util.logging.Level.FINEST);


		}
		// command line execution
		new CommandLine().start();
	}
	
	/**
	 * print a list of renderers
	 */
	public static void printRenderers() {
		System.out.println("Available renderers:\n - text\n - TextPlayer\n - XML\n - MP3\n - FlashPlayerCode\n");
		//TODO: list of renderers should be dynamic in some way
		// I cannot access JAR content, but maybe I can get maven to add a list of Renderers to manifest or so
	}
	
	/**
	 * print some example uses
	 */
	public static void printExamples() {
		System.out.println("Some Examples:\n\njava -jar JSpaceAlertMissionGenerator.jar -p text --play\n - Print mission as English text file and start playing MP3s.\n\n -jar JSpaceAlertMissionGenerator.jar -o XML -p text -p MP3\n - Print mission as text on screen, save it as XML and\n   start playing MP3 (\"print\" MP3 translates to --play).");
	}
}
