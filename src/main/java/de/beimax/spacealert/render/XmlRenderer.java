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

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import de.beimax.spacealert.mission.Event;
import de.beimax.spacealert.mission.Mission;
import de.beimax.spacealert.util.Options;

/**
 * @author mkalus
 *
 */
public class XmlRenderer implements Renderer {

	/* (non-Javadoc)
	 * @see de.beimax.spacealert.render.Renderer#print(de.beimax.spacealert.mission.Mission)
	 */
	public boolean print(Mission mission) {
		System.out.println(getMissionXML(mission));
		return true;
	}

	/* (non-Javadoc)
	 * @see de.beimax.spacealert.render.Renderer#output(de.beimax.spacealert.mission.Mission)
	 */
	public boolean output(Mission mission) {
		// get options
		Options options = Options.getOptions();
		
		// check file name/size
		if (options.outPutfilePrefix == null || options.outPutfilePrefix.isEmpty()) {
			System.out.println("Error writing XML file: file prefix is empty.");
		}
		
		// write file
		try {
			FileWriter outFile = new FileWriter(options.outPutfilePrefix + ".xml");
			outFile.write(getMissionXML(mission));
			outFile.close();
		} catch (IOException e) {
			System.out.println("Error writing XML file: " + e.getMessage());
		}

		return true;
	}
	
	private String getMissionXML(Mission mission) {
		StringBuilder sb = new StringBuilder();
		
		// some render statistics stuff
		int phaseLength = 0;
		int lastPhaseLength = 0;
		int phase = 1;

		// create XML beginning
		sb.append("<Mission>\n");
		
		// get values
		for(Map.Entry<Integer,Event> entry : mission.getMissionEvents().getEntrySet()) {
			int time = entry.getKey();
			// new phase?
			if (time >= phaseLength) {
				// calculate new phase length
				lastPhaseLength = phaseLength;
				phaseLength += mission.getMissionPhaseLength(phase);
				
				// output info
				sb.append("\t<Phase duration=\"" + mission.getMissionPhaseLength(phase) + "000\">\n");
				// phase anouncements for secons and third phases
				if (phase >= 2) {
					sb.append("\t\t<Element start=\"0\" type=\"Announcement\" message=\"Announce").append(phase==2?"Second":"Third").append("PhaseBegins\" />\n");
				}
				phase++;
			}
			sb.append("\t\t<Element start=\"").append((long)(time - lastPhaseLength) * 1000).append("\" ").append(entry.getValue().getXMLAttributes(time)).append(" />\n");
		}

		// create XML end
		sb.append("\t</Phase>\n</Mission>");
		
		return sb.toString();
	}

	/*
	<Mission>
		<Phase duration="203000">
			<Element start="0" type="Announcement" message="AnnounceBeginFirstPhase" />
			<Element start="17848" type="Threat" confidence="Confirmed"
				threatLevel="NormalThreat" threatType="ExternalThreat" time="0" zone="Blue" />
			<Element start="40943" type="DataTransfer" />
			<Element start="68085" type="Threat" confidence="Unconfirmed"
				threatLevel="NormalThreat" threatType="InternalThreat" time="1" />
			<Element start="140452" type="Announcement"
				message="AnnounceFirstPhaseEndsInOneMinute" />
			<Element start="145638" type="CommFailure" duration="17000" />
			<Element start="165712" type="Threat" confidence="Confirmed"
				threatLevel="SeriousThreat" threatType="InternalThreat" time="3" />
			<Element start="177436" type="Announcement"
				message="AnnounceFirstPhaseEndsInTwentySeconds" />
			<Element start="193198" type="Announcement" message="AnnounceFirstPhaseEnds" />
		</Phase>
		<Phase duration="173000">
			<Element start="0" type="Announcement" message="AnnounceSecondPhaseBegins" />
			<Element start="10621" type="DataTransfer" />
			<Element start="34353" type="IncomingData" />
			<Element start="58479" type="Threat" confidence="Confirmed"
				threatLevel="NormalThreat" threatType="ExternalThreat" time="5" zone="White" />
			<Element start="77687" type="CommFailure" duration="17000" />
			<Element start="110634" type="Announcement"
				message="AnnounceSecondPhaseEndsInOneMinute" />
			<Element start="119504" type="IncomingData" />
			<Element start="127399" type="DataTransfer" />
			<Element start="142821" type="IncomingData" />
			<Element start="150114" type="Announcement"
				message="AnnounceSecondPhaseEndsInTwentySeconds" />
			<Element start="163276" type="Announcement" message="AnnounceSecondPhaseEnds" />
		</Phase>
		<Phase duration="125000">
			<Element start="0" type="Announcement" message="AnnounceThirdPhaseBegins" />
			<Element start="10190" type="DataTransfer" />
			<Element start="31021" type="CommFailure" duration="14000" />
			<Element start="62530" type="Announcement"
				message="AnnounceThirdPhaseEndsInOneMinute" />
			<Element start="102218" type="Announcement"
				message="AnnounceThirdPhaseEndsInTwentySeconds" />
			<Element start="118000" type="Announcement" message="AnnounceThirdPhaseEnds" />
		</Phase>
	</Mission>
	 */
	
	@Override
	public String toString() {
		return "XML";
	}
}
