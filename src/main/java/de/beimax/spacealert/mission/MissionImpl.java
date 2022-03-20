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
package de.beimax.spacealert.mission;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.beimax.spacealert.util.Options;
import de.beimax.spacealert.mission.ThreatGroup;

/**
 * Default Mission Generator
 * @author mkalus
 */
public class MissionImpl implements Mission {
	static private Logger logger = Logger.getLogger("MissionLogger");
	static {
		// debugging option set?
		if (Options.getOptions().debug) logger.setLevel(Level.FINEST);
		else logger.setLevel(Level.WARNING);
	}

	/**
	 * allow double threats (internal and external at the same time slot - as introduced in "The New Frontier")
	 */
	private boolean enableDoubleThreats = false;
	
	/**
	 * configuration: threat level (8 for std game)
	 */
	private int threatLevel = 8;
	
	/**
	 * ...of which 1 level is unconfirmed (for 5 players)
	 */
	private int threatUnconfirmed = 1;
	
	/**
	 * ...of which x levels are internal
	 */
	private int minInternalThreats = 1;
	private int maxInternalThreats = 3;
	private int maxInternalThreatsNumber = 2; // number of internal threats max
	
	/**
	 * minimum and maximum time in which normal threats can occur
	 */
	private int minTNormalExternalThreat = 1;
	private int maxTNormalExternalThreat = 8;
	
	/**
	 * minimum and maximum time in which serious threats can occur
	 */
	private int minTSeriousExternalThreat = 2;
	private int maxTSeriousExternalThreat = 7;
	
	/**
	 * minimum and maximum time in which normal threats can occur
	 */
	private int minTNormalInternalThreat = 2;
	private int maxTNormalInternalThreat = 7;
	
	/**
	 * minimum and maximum time in which serious threats can occur
	 */
	private int minTSeriousInternalThreat = 3;
	private int maxTSeriousInternalThreat = 6;
	
	/**
	 * minimum data operations (either data transfer or incoming data)
	 */
	private int[] minDataOperations = {2, 2, 0};
	private int[] maxDataOperations = {3, 3, 1};
	
	/**
	 * minimum and maximum incoming data by phases
	 */
	private int[] minIncomingData = {1, 0, 0};
	private int[] maxIncomingData = {3, 2, 0};
	private int minIncomingDataTotal = 2;
	
	/**
	 * minimum and maximum data transfers by phases
	 */
	private int[] minDataTransfer = {0, 1, 1};
	private int[] maxDataTransfer = {1, 2, 1};
	private int minDataTransferTotal = 3;
	
	/**
	 * minimum and maximum time for white noise
	 */
	private int minWhiteNoise = 45;
	private int maxWhiteNoise = 60;
	private int minWhiteNoiseTime = 9;
	private int maxWhiteNoiseTime = 20;
	
	/**
	 * minimum and maximum time for phases
	 */
	private int[] minPhaseTime = {205, 180, 140};
	private int[] maxPhaseTime = {240, 225, 155};
	
	/**
	 * times for first threats to appear
	 */
	private int[] minTimeForFirst = { 10, 10 };
	private int[] maxTimeForFirst = { 20, 40 };
	
	/**
	 * chance for ambush in phases 4/8 in %
	 */
	private int[] chanceForAmbush = { 40, 40 };
	
	/**
	 * "middle" threats (2+3/5+6) should appear with % of phase length
	 */
	private int threatsWithInPercent = 70;
	
	/**
	 * keeps threats
	 */
	private ThreatGroup[] threats;
	
	/**
	 * keeps incoming and data transfers
	 */
	private int[] incomingData;
	private int[] dataTransfers;
	
	/**
	 * white noise chunks in seconds (to distribute)
	 */
	private WhiteNoise[] whiteNoise;

	/**
	 * phase times in seconds
	 */
	private int[] phaseTimes;
	
	/**
	 * event list
	 */
	EventList eventList;
	
	/**
	 * random number generator
	 */
	Random generator;
	
	/**
	 * Constructor
	 */
	public MissionImpl() {
		// get Options
		Options options = Options.getOptions();

		if (options.seed == null) generator = new Random(); 
	        else generator = new Random((long)options.seed);
		
		// copy variables from options
		threatLevel = options.threatLevel;
		threatUnconfirmed = options.threatUnconfirmed;
		minInternalThreats = options.minInternalThreats;
		maxInternalThreats = options.maxInternalThreats;
		maxInternalThreatsNumber = options.maxInternalThreatsNumber;
		enableDoubleThreats = options.enableDoubleThreats;
		minTNormalExternalThreat = options.minTNormalExternalThreat;
		maxTNormalExternalThreat = options.maxTNormalExternalThreat;
		minTSeriousExternalThreat = options.minTSeriousExternalThreat;
		maxTSeriousExternalThreat = options.maxTSeriousExternalThreat;
		minTNormalInternalThreat = options.minTNormalInternalThreat;
		maxTNormalInternalThreat = options.maxTNormalInternalThreat;
		minTSeriousInternalThreat = options.minTSeriousInternalThreat;
		maxTSeriousInternalThreat = options.maxTSeriousInternalThreat;
		minDataOperations = new int[]{ options.minDataOperations1, options.minDataOperations2, options.minDataOperations3 };
		maxDataOperations = new int[]{ options.maxDataOperations1, options.maxDataOperations2, options.maxDataOperations3 };
		minIncomingData = new int[]{ options.minIncomingData1, options.minIncomingData2, options.minIncomingData3 };
		maxIncomingData = new int[]{ options.maxIncomingData1, options.maxIncomingData2, options.maxIncomingData3 };
		minIncomingDataTotal = options.minIncomingDataTotal;
		minDataTransfer = new int[]{ options.minDataTransfer1, options.minDataTransfer1, options.minDataTransfer3 };
		maxDataTransfer = new int[]{ options.maxDataTransfer1, options.maxDataTransfer2, options.maxDataTransfer3 };
		minDataTransferTotal = options.minDataTransferTotal;
		minWhiteNoise = options.minWhiteNoise;
		maxWhiteNoise = options.maxWhiteNoise;
		minWhiteNoiseTime = options.minWhiteNoiseTime;
		maxWhiteNoiseTime = options.maxWhiteNoiseTime;
		minPhaseTime = new int[]{ options.minPhaseTime1, options.minPhaseTime2, options.minPhaseTime3 };
		maxPhaseTime = new int[]{ options.maxPhaseTime1, options.maxPhaseTime2, options.maxPhaseTime3 };
		minTimeForFirst = new int[]{ options.minTimeForFirst1, options.minTimeForFirst2 };
		maxTimeForFirst = new int[]{ options.maxTimeForFirst1, options.maxTimeForFirst2 };
		chanceForAmbush = new int[] { options.chanceForAmbush1, options.chanceForAmbush2 };
		threatsWithInPercent = options.threatsWithInPercent;
	}
	
	/**
	 * Return event list of mission
	 * @return ordered event list of mission
	 */
	public EventList getMissionEvents() {
		return eventList;
	}

	/**
	 * Return length of a phase in seconds
	 * @param phase 1-3
	 * @return phase length of mission or -1
	 */
	public int getMissionPhaseLength(int phase) {
		if (phase < 1 || phase > phaseTimes.length) return -1;
		return phaseTimes[phase-1];
	}

	/**
	 * Generate new mission
	 * 
	 * @return true if mission creation succeeded
	 */
	public boolean generateMission() {
		// generate threats
		boolean generated;
		int tries = 100; //maximum number of tries to generate mission
		do {generated = generateThreats();} while(!generated && tries-- > 0);
		if (!generated) {
			logger.warning("Giving up creating threats.");
			return false; //fail
		}

		// generate data transfer and incoming data
		generated = false; tries = 100;
		do {generated = generateDataOperations();} while(!generated && tries-- > 0);
		if (!generated) {
			logger.warning("Giving up creating data operations.");
			return false; //fail
		}
		
		//generate times
		generateTimes();

		//generate phases
		generated = false; tries = 100;
		do {generated = generatePhases();} while(!generated && tries-- > 0);
		if (!generated) {
			logger.warning("Giving up creating phase details.");
			return false; //fail
		}

		return false;
	}

	/**
	 * Inner class to facilitate basic threat generation
	 */
	class BasicThreatGenerator {
		// counters for threats by level, class, type, etc.
		int internalThreats, externalThreats,
				seriousThreats, normalThreats,
				seriousUnconfirmed, normalUnconfirmed,
				threatsSum;

		/**
		 * Initialize threat numbers
		 * @return false if something goes wrong
		 */
		boolean initialize() {
			internalThreats = generator.nextInt(maxInternalThreats - minInternalThreats + 1) + minInternalThreats;
			externalThreats = threatLevel - internalThreats;

			logger.fine("Threat Level: " + threatLevel + "; interal = " + internalThreats + ", external = " + externalThreats);

			// generate number of serious threats
			seriousThreats = generator.nextInt(threatLevel / 2 + 1);
			// if we only have serious threats and normal unconfirmed reports: reduce number of threats by 1
			if (threatUnconfirmed % 2 == 1 && seriousThreats * 2 == threatLevel)
				seriousThreats--;
			normalThreats =  threatLevel - seriousThreats * 2;

			logger.fine("Normal Threats: " + normalThreats + "; Serious Threats: " + seriousThreats);

			// if there are 8 normal threats - check again, if we really want this
			if (normalThreats >= 8 && generator.nextInt(3) != 0) {
				logger.info("8 or more normal threats unlikely. Redoing.");
				return false;
			}

			if ((seriousThreats == (threatLevel / 2) || seriousThreats >= 5) && generator.nextInt(3) != 0) {
				logger.info("all (or 5 or more) serious threats unlikely. Redoing.");
				return false;
			}

			// get sums
			threatsSum = normalThreats + seriousThreats;

			// if threat level is higher than 8, create serious threats until we have a threat level of 8 or lower
			// thanks to Leif Norcott from BoardGameGeek
			while (threatsSum > 8) {
				normalThreats -= 2;
				seriousThreats++;
				threatsSum = normalThreats + seriousThreats;
			}

			// special case: if we have enableDoubleThreats and only have serious threats -> convert one of them to 2 normal threats
			if (enableDoubleThreats && normalThreats == 0) {
				seriousThreats -= 1;
				normalThreats += 2;
				threatsSum = normalThreats + seriousThreats;
			}

			// distribute unconfirmed
			seriousUnconfirmed = generator.nextInt(threatUnconfirmed / 2 + 1);
			normalUnconfirmed = threatUnconfirmed - seriousUnconfirmed * 2;
			if (normalUnconfirmed > normalThreats) { // adjust, if there are not enough threats
				normalUnconfirmed -= 2;
				seriousUnconfirmed++;
			}
			else if (seriousUnconfirmed > seriousThreats) { // adjust, if there are not enough serious threats
				normalUnconfirmed += 2;
				seriousUnconfirmed--;
			}
			logger.fine("Normal unconfirmed Threats: " + normalUnconfirmed + "; Serious unconfirmed Threats: " + seriousUnconfirmed);

			return true;
		}

		/**
		 * helper to add normal threat
		 * @param t Threat
		 */
		void normalThreatAdded(Threat t) {
			normalThreats--;
			t.setThreatLevel(Threat.THREAT_LEVEL_NORMAL);
			t.setConfirmed(true);
		}

		/**
		 * helper to add normal unconfirmed threat
		 * @param t Threat
		 */
		void normalUnconfirmedThreatAdded(Threat t) {
			normalUnconfirmed--;
			normalThreats--;
			t.setThreatLevel(Threat.THREAT_LEVEL_NORMAL);
		}

		/**
		 * helper to add serious threat
		 * @param t Threat
		 */
		void seriousThreatAdded(Threat t) {
			seriousThreats--;
			t.setThreatLevel(Threat.THREAT_LEVEL_SERIOUS);
			t.setConfirmed(true);
		}

		/**
		 * helper to add serious unconfirmed threat
		 * @param t Threat
		 */
		void seriousUnconfirmedThreatAdded(Threat t) {
			seriousUnconfirmed--;
			seriousThreats--;
			t.setThreatLevel(Threat.THREAT_LEVEL_SERIOUS);
		}

		/**
		 * helper to add internal threat
		 * @param t Threat
		 */
		void internalThreatAdded(Threat t) {
			internalThreats -= t.getThreatLevel() == Threat.THREAT_LEVEL_SERIOUS ? 2 : 1;
			t.setThreatPosition(Threat.THREAT_POSITION_INTERNAL);
		}

		/**
		 * helper to add external threat
		 * @param t Threat
		 */
		void externalThreatAdded(Threat t) {
			externalThreats -= t.getThreatLevel() == Threat.THREAT_LEVEL_SERIOUS ? 2 : 1;
			t.setThreatPosition(Threat.THREAT_POSITION_EXTERNAL);
		}

		/**
		 * Actually generate threats
		 * @return generated threats
		 */
		ThreatGroup[] generateThreats() {
			ThreatGroup[] threats = new ThreatGroup[enableDoubleThreats ? threatsSum - 1 : threatsSum];
			int threatIdx = 0; // current id in above array

			// if we have a double threat, create this first
			if (enableDoubleThreats) {
				Threat newThreat = new Threat(); // new threat created
				// confirmed or unconfirmed?
				if (generator.nextInt(threatsSum) + 1 > threatUnconfirmed) {
					if (generator.nextInt(normalUnconfirmed + seriousUnconfirmed) + 1 <= normalUnconfirmed) {
						normalUnconfirmedThreatAdded(newThreat);
					} else {
						seriousUnconfirmedThreatAdded(newThreat);
					}
				} else { // normal threats aka confirmed
					// serious or not?
					if (generator.nextInt(normalThreats + seriousThreats - normalUnconfirmed - seriousUnconfirmed) + 1 <= normalThreats - normalUnconfirmed) {
						normalThreatAdded(newThreat);
					} else {
						seriousThreatAdded(newThreat);
					}
				}

				// internal or external?
				if (internalThreats > 1 && newThreat.getThreatLevel() == Threat.THREAT_LEVEL_SERIOUS) { // number must be greater to work
					// internal/external?
					if (generator.nextInt(externalThreats + internalThreats) + 1 <= externalThreats) {
						externalThreatAdded(newThreat);
					} else {
						internalThreatAdded(newThreat);
					}
				} else {
					// create external
					newThreat.setThreatLevel(Threat.THREAT_LEVEL_NORMAL);
					externalThreatAdded(newThreat);
				}

				// create second threat
				Threat newThreat2 = new Threat(); // new threat created
				newThreat2.setConfirmed(true); // second threat is always confirmed

				// add new threat group with two elements
				ThreatGroup g = new ThreatGroup(newThreat);
				newThreat2.setThreatPosition(newThreat.getThreatPosition() == Threat.THREAT_POSITION_INTERNAL ?
						Threat.THREAT_POSITION_EXTERNAL : Threat.THREAT_POSITION_INTERNAL);
				g.set(newThreat2);

				// now check second threat level
				if (newThreat.getThreatLevel() != Threat.THREAT_LEVEL_SERIOUS && seriousThreats > 0) {
					// not serious and serious threats left -> second might be serious
					if (generator.nextInt(normalThreats + seriousThreats - normalUnconfirmed - seriousUnconfirmed) + 1 <= normalThreats - normalUnconfirmed) {
						newThreat2.setThreatLevel((Threat.THREAT_LEVEL_NORMAL));
					} else {
						newThreat2.setThreatLevel((Threat.THREAT_LEVEL_SERIOUS));
					}
				} else {
					// second is always normal
					newThreat2.setThreatLevel((Threat.THREAT_LEVEL_NORMAL));
				}

				// adjust levels
				if (newThreat2.getThreatLevel() == Threat.THREAT_LEVEL_SERIOUS) {
					seriousThreatAdded(newThreat2);
				} else {
					normalThreatAdded(newThreat2);
				}
				if (newThreat2.getThreatPosition() == Threat.THREAT_POSITION_INTERNAL) {
					internalThreatAdded(newThreat2);
				} else {
					externalThreatAdded(newThreat2);
				}

				threats[threatIdx++] = g;
			}

			// create serious threats
			for (int i = 0; i < seriousThreats; i++) {
				Threat newThreat = new Threat(); // new threat created
				// unconfirmed or confirmed?
				newThreat.setConfirmed(i >= seriousUnconfirmed);
				newThreat.setThreatLevel(Threat.THREAT_LEVEL_SERIOUS);

				// internal or external threat?
				if (internalThreats > 1 && generator.nextInt(externalThreats + internalThreats) + 1 > externalThreats) {
					internalThreatAdded(newThreat);
				} else {
					externalThreatAdded(newThreat);
				}

				threats[threatIdx++] = new ThreatGroup(newThreat);
			}

			// create normal threats
			for (int i = 0; i < normalThreats; i++) {
				Threat newThreat = new Threat(); // new threat created
				// unconfirmed or confirmed?
				newThreat.setConfirmed(i >= normalUnconfirmed);
				newThreat.setThreatLevel(Threat.THREAT_LEVEL_NORMAL);

				// internal/external?
				if (generator.nextInt(externalThreats + internalThreats) + 1 > externalThreats) {
					internalThreatAdded(newThreat);
				} else {
					externalThreatAdded(newThreat);
				}

				threats[threatIdx++] = new ThreatGroup(newThreat);
			}

			return threats;
		}
	}
	
	/**
	 * "sane" generator method for threats
	 * @return true if generation was successful
	 */
	protected boolean generateThreats() {
		BasicThreatGenerator tg = new BasicThreatGenerator();

		// initialize numbers - might fail, then we return false to try again
		if (!tg.initialize()) {
			logger.info("Threat initialization failed. Retrying.");
			return false;
		}

		// generate the basic threats
		threats = tg.generateThreats();

		// keeps number of threats each phase - used to check sanity further down
		int threatsFirstPhase = 0;
		int threatsSecondPhase = 0;

		// generate phases and distribute threats
		ThreatGroup[] sortedThreats = new ThreatGroup[8];

		for (ThreatGroup threatGroup : threats) {
			if (threatGroup != null) {
				// for each threat group, set min and max phases
				int minPhase = 1;
				int maxPhase = 8;

				Threat externalThreat = threatGroup.getExternal();
				if (externalThreat != null) {
					if (externalThreat.getThreatLevel() == Threat.THREAT_LEVEL_SERIOUS) {
						if (minPhase < minTSeriousExternalThreat) minPhase = minTSeriousExternalThreat;
						if (maxPhase > maxTSeriousExternalThreat) maxPhase = maxTSeriousExternalThreat;
					} else {
						if (minPhase < minTNormalExternalThreat) minPhase = minTNormalExternalThreat;
						if (maxPhase > maxTNormalExternalThreat) maxPhase = maxTNormalExternalThreat;
					}
				}

				Threat internalThreat = threatGroup.getInternal();
				if (internalThreat != null) {
					if (internalThreat.getThreatLevel() == Threat.THREAT_LEVEL_SERIOUS) {
						if (minPhase < minTSeriousInternalThreat) minPhase = minTSeriousInternalThreat;
						if (maxPhase > maxTSeriousInternalThreat) maxPhase = maxTSeriousInternalThreat;
					} else {
						if (minPhase < minTNormalInternalThreat) minPhase = minTNormalInternalThreat;
						if (maxPhase > maxTNormalInternalThreat) maxPhase = maxTNormalInternalThreat;
					}
				}

				// create list of possible phases - find remaining possible phases and pick one
				LinkedList<Integer> possiblePhases = new LinkedList<Integer>();
				for (int i = minPhase; i <= maxPhase; i++) {
					if (sortedThreats[i-1] == null) possiblePhases.add(i);
				}

				// no possible phases left - giving up to continue again
				if (possiblePhases.size() == 0) {
					logger.info("Threat distribution failed - no possible phases left to put created threat into. Retrying.");
					return false;
				}

				// pick random phase
				int phase = possiblePhases.get(generator.nextInt(possiblePhases.size()));

				// set stuff
				if (externalThreat != null) externalThreat.setTime(phase);
				if (internalThreat != null) internalThreat.setTime(phase);
				sortedThreats[phase-1] = threatGroup;

				// add threat score
				if (externalThreat != null && internalThreat != null) {
					if (phase <= 4) threatsFirstPhase += 2;
					else threatsSecondPhase += 2;
				} else {
					if (phase <= 4) threatsFirstPhase++;
					else threatsSecondPhase++;
				}
			}
		}

		// check sanity of distributions of threats among phase 1 and 2
		if (Math.abs(threatsFirstPhase - threatsSecondPhase) > 1) {
			logger.info("Threat distribution failed - not balanced enough. Retrying.");
			return false; // the distribution should be equal
		}

		// set sorted threats
		threats = sortedThreats;

		// generate attack sectors
		int lastSector = -1; // to not generate same sectors twice
		boolean lastThreatWasInternal = false; // sanity check if there are two internal threats in a row - if there are, retry mission
		for (int i = 0; i < 8; i++) {
			if (threats[i] != null) {
				Threat t = threats[i].getExternal();
				if (t != null) {
					switch (generator.nextInt(3)) {
						case 0:
							if (lastSector != Threat.THREAT_SECTOR_BLUE) t.setSector(Threat.THREAT_SECTOR_BLUE);
							else t.setSector(Threat.THREAT_SECTOR_WHITE);
							break;
						case 1:
							if (lastSector != Threat.THREAT_SECTOR_WHITE) t.setSector(Threat.THREAT_SECTOR_WHITE);
							else t.setSector(Threat.THREAT_SECTOR_RED);
							break;
						case 2:
							if (lastSector != Threat.THREAT_SECTOR_RED) t.setSector(Threat.THREAT_SECTOR_RED);
							else t.setSector(Threat.THREAT_SECTOR_BLUE);
							break;
						// default: System.out.println("No Way!");
					}
					lastSector = t.getSector();
				}
				t = threats[i].getInternal();
				if (t != null) {
					if (lastThreatWasInternal) {
						logger.info("Two internal threats in a row. Retrying.");
						return false;
					}
					lastThreatWasInternal = true;
				}
			} else {
				// add empty group to not have NPEs later on - this is not so elegant and might be subject to refactoring at some time...
				threats[i] = new ThreatGroup();
			}
		}

		return true;
	}
	
	/**
	 * Generate data operations (either data transfer or incoming data)
	 * @return true, if data creation could be generated
	 */
	protected boolean generateDataOperations() {
		// clear data
		incomingData = new int[3];
		dataTransfers = new int[3];
		
		int incomingSum = 0;
		int transferSum = 0;

		// generate stuff by phase
		for (int i = 0; i < 3; i++) {
			incomingData[i] = generator.nextInt(maxIncomingData[i] - minIncomingData[i] + 1) + minIncomingData[i];
			dataTransfers[i] = generator.nextInt(maxDataTransfer[i] - minDataTransfer[i] + 1) + minDataTransfer[i];
			
			// check minimums
			if (incomingData[i] + dataTransfers[i] < minDataOperations[i] ||
					incomingData[i] + dataTransfers[i] > maxDataOperations[i]) return false;
			
			incomingSum += incomingData[i];
			transferSum += dataTransfers[i];
		}
		
		// check minimums
		if (incomingSum < minIncomingDataTotal || transferSum < minDataTransferTotal) return false;
		
		// debuggin information
		if (logger.getLevel() == Level.FINE) {
			for (int i = 0; i < 3; i++) {
				logger.fine("Phase " + (i+1) + ": Incoming Data = " + incomingData[i] + "; Data Transfers = " + dataTransfers[i]);
			}
		}
		
		return true;
	}
	
	/**
	 * simple generation of times for phases, white noise etc.
	 */
	protected void generateTimes() {
		// generate white noise
		int whiteNoiseTime = generator.nextInt(maxWhiteNoise - minWhiteNoise + 1) + minWhiteNoise;
		logger.fine("White noise time: " + whiteNoiseTime);
		
		// create chunks
		ArrayList<Integer> whiteNoiseChunks = new ArrayList<Integer>();
		while (whiteNoiseTime > 0) {
			// create random chunk
			int chunk = generator.nextInt(maxWhiteNoiseTime - minWhiteNoiseTime + 1) + minWhiteNoiseTime;
			// check if there is enough time left
			if (chunk > whiteNoiseTime) {
				// hard case: smaller than minimum time
				if (chunk < minWhiteNoiseTime) {
					// add to last chunk that fits
					for (int i = whiteNoiseChunks.size()-1; i >= 0; i--) {
						int sumChunk = whiteNoiseChunks.get(i) + chunk;
						// if smaller than maximum time: add to this chunk
						if (sumChunk <= maxWhiteNoiseTime) {
							whiteNoiseChunks.set(i, sumChunk);
							whiteNoiseTime = 0;
							break;
						}
					}
					// still not zeroed
					if (whiteNoiseTime > 0) { // add to last element, regardless - quite unlikely though
						int lastIdx = whiteNoiseChunks.size()-1;
						whiteNoiseChunks.set(lastIdx, whiteNoiseChunks.get(lastIdx) + chunk);
						whiteNoiseTime = 0;
					}
				} else { // easy case: create smaller rest chunk
					whiteNoiseChunks.add(whiteNoiseTime);
					whiteNoiseTime = 0;
				}
			} else { // add new chunk
				whiteNoiseChunks.add(chunk);
				whiteNoiseTime -= chunk;
			}
		}
		
		// ok, add chunks to mission
		whiteNoise = new WhiteNoise[whiteNoiseChunks.size()];
		for (int i = 0; i < whiteNoiseChunks.size(); i++) whiteNoise[i] = new WhiteNoise(whiteNoiseChunks.get(i));
		
		// add mission lengths
		phaseTimes = new int[3];
		for (int i = 0; i < 3; i++) {
			phaseTimes[i] = generator.nextInt(maxPhaseTime[i] - minPhaseTime[i] + 1) + minPhaseTime[i];
		}
	}
	
	/**
	 * generate phase stuff from data above
	 * @return true if phase generation succeeded
	 */
	protected boolean generatePhases() {
		logger.info("Data gathered: Generating phases.");

		// create events
		eventList = new EventList();

		// add fixed events: announcements
		eventList.addPhaseEvents(phaseTimes[0], phaseTimes[1], phaseTimes[2]);
		
		boolean ambushOccured = false;
		// add threats in first phase
		// ambush handling - is there a phase 4, and it is a normal external threat? ... and chance is taken?
		Threat maybeAmbush = threats[3].getExternal();
		if (maybeAmbush != null && maybeAmbush.getThreatLevel() == Threat.THREAT_LEVEL_NORMAL && generator.nextInt(100) + 1 < chanceForAmbush[0]) {
			//...then add an "ambush" threat between 1 minute and 20 secs warnings
			boolean done = false; // try until it fits
			do {
				// TODO: remove hardcoded length here:
				int ambushTime = generator.nextInt(35) + phaseTimes[0] - 59;
				logger.info("Ambush in phase 1 at time: " + ambushTime);
				done = eventList.addEvent(ambushTime, maybeAmbush);
			} while (!done);
			
			threats[3].removeExternal();
			ambushOccured = true; // to disallow two ambushes in one game
		}

		// to be used further down
		int[] lastThreatTime = { 0, 0 };

		// add the rest of the threats
		int currentTime = generator.nextInt(maxTimeForFirst[0] - minTimeForFirst[0] + 1) + minTimeForFirst[0];
		// threats should appear within this time
		int lastTime = (int) (phaseTimes[0] * (((float)threatsWithInPercent) / 100));
		boolean first = true;
		// look for first threat
		for (int i = 0; i <= 3; i++) {
			ThreatGroup now = threats[i];
			Threat activeThreat;
			if (now.hasExternal()) {
				activeThreat = now.removeExternal();
				i--; //check again
			} else if (now.hasInternal()) {
				activeThreat = now.removeInternal();
				i--; //check again
			} else {
				continue;
			}
			// first event?
			if (first) {
				if (!eventList.addEvent(currentTime, activeThreat)) logger.warning("Could not add first event to list (time " + currentTime + ") - arg!");
				first = false;
			} else {
				boolean done = false; // try until it fits
				int nextTime = 0;
				int tries = 0; // number of tries
				do {
					// next threat appears
					// next element occurs
					int divisor = 2;
					if (++tries > 10) divisor = 3;
					else if (tries > 20) divisor = 4;
					if (lastTime <= currentTime) return false;
					nextTime = generator.nextInt((lastTime - currentTime) / divisor) + 5;
					if (tries > 30) return false;
					done = eventList.addEvent(currentTime + nextTime, activeThreat);
				} while (!done);
				currentTime += nextTime;
				// save lastThreatTime for data transfers further down
				if (i < 3) lastThreatTime[0] = currentTime;
			}
			// add to time
			currentTime += activeThreat.getLengthInSeconds();
		}

		// add threats in second phase
		// ambush handling - is there a phase 8, and it is a normal external threat? ... and chance is taken?
		maybeAmbush = threats[7].getExternal();
		if (!ambushOccured && maybeAmbush != null && maybeAmbush.getThreatLevel() == Threat.THREAT_LEVEL_NORMAL && generator.nextInt(100) + 1 < chanceForAmbush[1]) {
			//...then add an "ambush" threat between 1 minute and 20 secs warnings
			boolean done = false; // try until it fits
			do {
				// TODO: remove hardcoded length here:
				int ambushTime = generator.nextInt(35) + phaseTimes[0] + phaseTimes[1] - 59;
				logger.info("Ambush in phase 2 at time: " + ambushTime);
				done = eventList.addEvent(ambushTime, maybeAmbush);
			} while (!done);
			
			threats[7].removeExternal();
		}

		// add the rest of the threats
		currentTime = phaseTimes[0] + generator.nextInt(maxTimeForFirst[1] - minTimeForFirst[1] + 1) + minTimeForFirst[1];
		// threats should appear within this time
		lastTime = phaseTimes[0] + (int) (phaseTimes[1] * (((float)threatsWithInPercent) / 100));
		first = true;
		// look for first threat
		for (int i = 4; i <= 7; i++) {
			ThreatGroup now = threats[i];
			Threat activeThreat;
			if (now.hasExternal()) {
				activeThreat = now.removeExternal();
				i--; //check again
			} else if (now.hasInternal()) {
				activeThreat = now.removeInternal();
				i--; //check again
			} else {
				continue;
			}
			// first event?
			if (first) {
				if (!eventList.addEvent(currentTime, activeThreat)) logger.warning("Could not add first event to list in second phase (time " + currentTime + ") - arg!");
				first = false;
			} else {
				boolean done = false; // try until it fits
				int nextTime = 0;
				int tries = 0; // number of tries
				do {
					// next element occurs
					int divisor = 2;
					if (++tries > 10) divisor = 3;
					else if (tries > 20) divisor = 4;
					if (lastTime <= currentTime) return false;
					nextTime = generator.nextInt((lastTime - currentTime) / divisor) + 5;
					if (tries > 30) return false;
					done = eventList.addEvent(currentTime + nextTime, activeThreat);
				} while (!done);
				currentTime += nextTime;
				// save lastThreatTime for data transfers further down
				if (i < 7) lastThreatTime[1] = currentTime;
			}
			// add to time
			currentTime += activeThreat.getLengthInSeconds();
		}

		//add data transfers
		// get start and end times
		int startTime = 0;
		int endTime = 0;
		// special balance: first data transfers in phase 1 and 2 should occur shortly after first threat wave
		for (int i = 0; i < 2; i++) {
			startTime = endTime;
			endTime += phaseTimes[i];
			if (dataTransfers[i] > 0) { // if there is a data transfer
				startTime = lastThreatTime[i];
				boolean done = false; // try until it fits
				do { // try to add incoming data within 30 seconds of event
					startTime = generator.nextInt(31) + startTime + 1;
					done = eventList.addEvent(startTime, new DataTransfer());
				} while (!done && startTime < endTime);
				if (done) {
					// reduce data transfers below
					dataTransfers[i]--;
				}
			}
		}
		
		startTime = 0;
		endTime = 0;
		// distribute rest of data transfers and incoming data randomly within the phases
		for (int i = 0; i < 3; i++) {
			// recalculate phase times
			startTime = endTime;
			endTime += phaseTimes[i];
			// data transfer first, since these are fairly long
			for (int j = 0; j < dataTransfers[i]; j++) {
				boolean done = false; // try until it fits
				do {
					// white noise can pretty much occur everywhere
					int time = generator.nextInt(endTime - startTime) + startTime - 5; // to fend off events after mission ends
					done = eventList.addEvent(time, new DataTransfer());
				} while (!done);
			}
			// incoming data second
			for (int j = 0; j < incomingData[j]; j++) {
				boolean done = false; // try until it fits
				do {
					// white noise can pretty much occur everywhere
					int time = generator.nextInt(endTime - startTime) + startTime - 5; // to fend off events after mission ends
					done = eventList.addEvent(time, new IncomingData());
				} while (!done);
			}
		}

		//add white noise at random times
		for (int i = 0; i < whiteNoise.length; i++) {
			boolean done = false; // try until it fits
			do {
				// white noise can pretty much occur everywhere
				int time = generator.nextInt(phaseTimes[0] + phaseTimes[1] + phaseTimes[2] - 30) + 10;
				done = eventList.addEvent(time, whiteNoise[i]);
			} while (!done);
		}
		
		return true;
	}
	
	/**
	 * Prints list of missions
	 */
	@Override
	public String toString() {
		return eventList.toString();
	}
}
