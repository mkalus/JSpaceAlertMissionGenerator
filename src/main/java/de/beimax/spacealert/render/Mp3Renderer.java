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
package de.beimax.spacealert.render;

import de.beimax.spacealert.mission.Event;
import de.beimax.spacealert.mission.EventList;
import de.beimax.spacealert.mission.Mission;
import de.beimax.spacealert.mp3.BackgroundMP3PlayerFactory;
import de.beimax.spacealert.mp3.MP3Cache;
import de.beimax.spacealert.mp3.MP3MissionPlayer;
import de.beimax.spacealert.util.Options;
import javazoom.jl.converter.WaveFileObuffer;
import javazoom.jl.decoder.*;
import javazoom.jl.player.Player;
import javazoom.spi.mpeg.sampled.convert.DecodedMpegAudioInputStream;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;

import javax.sound.sampled.*;
import java.io.*;
import java.util.Map;

/**
 * @author mkalus
 *
 */
public class Mp3Renderer implements Renderer {

	/* (non-Javadoc)
	 * @see de.beimax.spacealert.render.Renderer#output(de.beimax.spacealert.mission.Mission)
	 */
	public boolean print(Mission mission) {
		// convert output MP3 to play
		Options options = Options.getOptions();
		if (!options.silent) {
			System.out.println("Converting --print MP3 to --play.");
		}
		options.play = true;
		return true;
	}

	/* (non-Javadoc)
	 * @see de.beimax.spacealert.render.Renderer#print(de.beimax.spacealert.mission.Mission)
	 */
	public boolean output(Mission mission) {
		// get options
		Options options = Options.getOptions();

		// check file name/size
		if (options.outPutfilePrefix == null || options.outPutfilePrefix.isEmpty()) {
			System.out.println("Error writing MP3 file: file prefix is empty.");
		}

		// check for clips directory
		if (!options.checkForClipDirectory()) {
			System.out.println("In order to play the MP3 clips, you need to download a set of MP3 files and save them in a directory named clips (or specified by --clips-folder option) in the same directory as the jar.\nLook at http://sites.google.com/site/boardgametools/SpaceAlertMissionGenerator.\nGerman and English Sound sets are included in the the Space Alert Mission Generator. You can also look into the forums on http://www.boardgamegeek.com/ which provide some language files for Japanese and so on.");
		}

		MP3Cache cache = MP3Cache.getSingleton();

		AudioFormat outputFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

		try {
			InputStream stream = cache.getMP3InputStream("red_alert_2.mp3");

			MpegAudioFileReader reader = new MpegAudioFileReader();
			AudioInputStream audioInputStream = reader.getAudioInputStream(stream);

			DecodedMpegAudioInputStream is = new DecodedMpegAudioInputStream(outputFormat, audioInputStream);
			is.execute();
			// TODO: continue to test this...
//			FileOutputStream outputStream = new FileOutputStream("out");
//			outputStream.write(is.readAllBytes());
		} catch (Exception e) {
			// TODO
			System.out.println(e.getMessage());
		}


//		EventList events = mission.getMissionEvents();
//
//		Map.Entry<Integer, Event> nextEvent = events.getNextEvent(0);
//		do {
//			// get current event
//			Event event = nextEvent.getValue();
//			int eventTime = nextEvent.getKey();
//
//			System.out.println(event.getMP3s());
//
//			// get next event
//			nextEvent = events.getNextEvent(eventTime+1);
//		} while (nextEvent != null);

		return true;
	}

	@Override
	public String toString() {
		return "MP3";
	}
}
