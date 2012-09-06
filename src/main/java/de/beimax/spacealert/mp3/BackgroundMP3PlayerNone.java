/**
 * This file is part of the JSpaceAlertMissionGenerator software.
 * Copyright (C) 2012 Maximilian Kalus
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
package de.beimax.spacealert.mp3;

/**
 * Silent background player
 * @author mkalus
 */
public class BackgroundMP3PlayerNone extends BackgroundMP3Player {

	/* (non-Javadoc)
	 * @see de.beimax.spacealert.mp3.BackgroundMP3Player#startMusicPlaying()
	 */
	@Override
	protected void startMusicPlaying() {
		//NOOP
	}

	/* (non-Javadoc)
	 * @see de.beimax.spacealert.mp3.BackgroundMP3Player#stopMusicPlaying()
	 */
	@Override
	protected void stopMusicPlaying() {
		//NOOP
	}
}
