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
import de.beimax.spacealert.util.MeanWeightedValueGenerator;

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
	 * limiting the number of normal and serious threats
	 */
	public int maxNormalThreatsNumber = threatLevel - 1;
	public int maxSeriousThreatsNumber = (int) Math.floor((float)(threatLevel - 1)/2);

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
     * random number generator favoring middle values between a minimum and
     * maximum values
     */
    MeanWeightedValueGenerator meanWeightedValueGenerator;

	/**
	 * Constructor
	 */
	public MissionImpl() {
		// get Options
		Options options = Options.getOptions();

		if (options.seed == null) generator = new Random(); 
	        else generator = new Random((long)options.seed);
		
        meanWeightedValueGenerator = new MeanWeightedValueGenerator(((double) options.gaussianWeight) / 100, ((double) options.standardDeviation) / 100, options.seed);

		// copy variables from options
		threatLevel = options.threatLevel;
		threatUnconfirmed = options.threatUnconfirmed;
		minInternalThreats = options.minInternalThreats;
		maxInternalThreats = options.maxInternalThreats;
		maxInternalThreatsNumber = options.maxInternalThreatsNumber;
		maxNormalThreatsNumber = options.maxNormalThreatsNumber;
		maxSeriousThreatsNumber = options.maxSeriousThreatsNumber;
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
            internalThreats = meanWeightedValueGenerator.nextInt(minInternalThreats, maxInternalThreats);
			externalThreats = threatLevel - internalThreats;

			logger.fine("Threat Level: " + threatLevel + "; interal = " + internalThreats + ", external = " + externalThreats);

			// generate number of serious threats
			// e.g. threatLevel=8, maxNormalThreatsNumber=7. Diff is 1. At least 1 threat level must come from
			// a serious threat. Each give 2 threat value, so we only need half that number of serious threats.
			// But we can't have half-serious threats, so round up. 
			int maxSerious = (int) Math.min(Math.floor((float)threatLevel/2), maxSeriousThreatsNumber);
			int minSerious = Math.max(0, (int) Math.ceil((float)(threatLevel - maxNormalThreatsNumber)/2));
            seriousThreats = meanWeightedValueGenerator.nextInt(minSerious, maxSerious);
			// if we only have serious threats and normal unconfirmed reports: reduce number of threats by 1
			if (threatUnconfirmed % 2 == 1 && seriousThreats * 2 == threatLevel)
				seriousThreats--;
			normalThreats =  threatLevel - seriousThreats * 2;

			logger.fine("Normal Threats: " + normalThreats + "; Serious Threats: " + seriousThreats);

			// get sums
			threatsSum = normalThreats + seriousThreats;

			// if threat level is higher than 8, create serious threats until we have a threat level of 8 or lower
			// thanks to Leif Norcott from BoardGameGeek
			while (threatsSum > (enableDoubleThreats ? 9 : 8)) {
				logger.log(Level.FINE, "Converting two normal threats to a serious threat to fit our {0} time slots.", enableDoubleThreats ? 9 : 8);
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
				int desiredUnconfirmedThreats = threatUnconfirmed + seriousUnconfirmed;
				if (desiredUnconfirmedThreats > 0 && generator.nextInt(threatsSum) + 1 > threatUnconfirmed) {
					if (generator.nextInt(desiredUnconfirmedThreats) + 1 <= normalUnconfirmed) {
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

		// Check that we do not have too many internal threats.
		{
			int internalThreatsCounter = 0;
			for (ThreatGroup threatGroup : threats) {
				if (threatGroup.hasInternal()) {
					internalThreatsCounter++;
				}
			}
			if (internalThreatsCounter > maxInternalThreatsNumber) {
				logger.info("Too many internal threats. Retrying.");
				return false;
			}
		}

		// set sorted threats
		threats = assignTimeslotsToThreats(threats, new ThreatGroup[8]);

		if (threats == null) {
			logger.info("Unable to assign timeslots to theats. Retrying.");
			return false;
		}

		// generate attack sectors
		int lastSector = -1; // to not generate same sectors twice
		int redThreats = 0;
		int blueThreats = 0;
		int whiteThreats = 0;
		for (int i = 0; i < 8; i++) {
			if (threats[i] != null) {
				Threat t = threats[i].getExternal();
				if (t != null) {
					t.setTime(i+1);
					Set<Integer> possibleSectors = new HashSet<>();
					if (redThreats < 3) possibleSectors.add(Threat.THREAT_SECTOR_RED);
					if (blueThreats < 3) possibleSectors.add(Threat.THREAT_SECTOR_BLUE);
					if (whiteThreats < 3) possibleSectors.add(Threat.THREAT_SECTOR_WHITE);
					possibleSectors.remove(lastSector);
					int index = generator.nextInt(possibleSectors.size());
					lastSector = (int) possibleSectors.toArray()[index];
					t.setSector(lastSector);
					if (lastSector == Threat.THREAT_SECTOR_RED) redThreats++;
					if (lastSector == Threat.THREAT_SECTOR_BLUE) blueThreats++;
					if (lastSector == Threat.THREAT_SECTOR_WHITE) whiteThreats++;
				}
				t = threats[i].getInternal();
				if (t != null) {
					t.setTime(i+1);
				}
			} else {
				// add empty group to not have NPEs later on - this is not so elegant and might be subject to refactoring at some time...
				threats[i] = new ThreatGroup();
			}
		}

		return true;
	}

	/**
	 * Recursive helper function to assign timeslots to a list of threats. This method will NOT
	 * set the internal T of the threats, i.e. it will not call .setTime on the threats.
	 * Doing so would make it impossible for the method to call itself recursively.
	 *
	 * @param untimedThreats These are threats that we have so far not sorted into their respective timeslots
	 * @param timedThreats These are the threats that we have so far sorted. This is a sparse array of length 8.
	 * @return a new timedThreats array of length 8 with all the threats asorted, or null if this is impossible to do.
	 */
	private ThreatGroup[] assignTimeslotsToThreats(ThreatGroup[] untimedThreats, ThreatGroup[] timedThreats) {
		logger.log(Level.FINE, () -> {
			StringBuilder sb = new StringBuilder();
			sb.append("| ");
			for (int i = 0; i < timedThreats.length; i++) {
				if (timedThreats[i] == null) sb.append("  ");
				else {
					if (timedThreats[i].hasExternal()) sb.append((timedThreats[i].getExternal().getThreatLevel() == 1 ? 'e' : 'E'));
					else sb.append(" ");
					if (timedThreats[i].hasInternal()) sb.append((timedThreats[i].getInternal().getThreatLevel() == 1 ? 'i' : 'I'));
					else sb.append(" ");
				}
				if (i == 3) sb.append(" |   | ");
				else if (i != 7) sb.append(" , ");
			}
			sb.append("|");
			return sb.toString();
		});
		// Base case.
		if (untimedThreats.length == 0) {
			// We need to check if the threat distribtion across phases is balanced.
			{
				int threatCountPhase1 = 0;
				int threatCountPhase2 = 0;
				int threatLevelPhase1 = 0;
				int threatLevelPhase2 = 0;
				

				for (int i = 0; i < timedThreats.length; i++) {
					if (timedThreats[i] != null) {
						int count = 0;
						int level = 0;
						if (timedThreats[i].hasExternal()) {
							count++;
							level += timedThreats[i].getExternal().getThreatLevel();
						}
						if (timedThreats[i].hasInternal()) {
							count++;
							level += timedThreats[i].getInternal().getThreatLevel();
						}
						if (i < 4) {
							threatCountPhase1 += count;
							threatLevelPhase1 += level;
						} else {
							threatCountPhase2 += count;
							threatLevelPhase2 += level;
						}
					}
				}
				// check sanity of distributions of threats among phase 1 and 2
				if (Math.abs(threatCountPhase1 - threatCountPhase2) > 1) {
					logger.info("Threats are not evenly distributed across the two phases. Back up.");
					return null;
				} else if (Math.abs(threatLevelPhase1 - threatLevelPhase2) > 2) {
					logger.info("Threat levels are not evenly distributed across the two phases. Back up.");
					return null;
				}
			}

			// We need to check that there are no two internal threats with no external threat
			// in between them.
			{
				boolean lastSeenWasInternal = false;
				for (ThreatGroup timedThreat : timedThreats) {
					if (timedThreat != null) {
						if (timedThreat.hasInternal()) {
							if (lastSeenWasInternal) {
								logger.info("Threat distribution failed - found two adjacent internal threats. Back up.");
								return null;
							} else lastSeenWasInternal = true;
						} else if (timedThreat.hasExternal()) {
							lastSeenWasInternal = false;
						}
					}
				}
			}
			
			// Success!
			logger.info("Threat time assignment complete!");
			return timedThreats;
		}

		// Recursive Case. Take one untimed threat and make it timed, then recursively call this function again
		// to do the rest. Magic!
		ArrayList<ThreatGroup> untimedThreatsToTry = new ArrayList(Arrays.asList(untimedThreats));
		while (!untimedThreatsToTry.isEmpty()) {
			int index = this.generator.nextInt(untimedThreatsToTry.size());
			ThreatGroup threatGroup = untimedThreatsToTry.remove(index);

			// Attempt to add it
			// for each threat group, set min and max phases
			int minTime = 1;
			int maxTime = 8;

			Threat externalThreat = threatGroup.getExternal();
			if (externalThreat != null) {
				if (externalThreat.getThreatLevel() == Threat.THREAT_LEVEL_SERIOUS) {
					if (minTime < minTSeriousExternalThreat) {
						minTime = minTSeriousExternalThreat;
					}
					if (maxTime > maxTSeriousExternalThreat) {
						maxTime = maxTSeriousExternalThreat;
					}
				} else {
					if (minTime < minTNormalExternalThreat) {
						minTime = minTNormalExternalThreat;
					}
					if (maxTime > maxTNormalExternalThreat) {
						maxTime = maxTNormalExternalThreat;
					}
				}
			}

			Threat internalThreat = threatGroup.getInternal();
			if (internalThreat != null) {
				if (internalThreat.getThreatLevel() == Threat.THREAT_LEVEL_SERIOUS) {
					if (minTime < minTSeriousInternalThreat) {
						minTime = minTSeriousInternalThreat;
					}
					if (maxTime > maxTSeriousInternalThreat) {
						maxTime = maxTSeriousInternalThreat;
					}
				} else {
					if (minTime < minTNormalInternalThreat) {
						minTime = minTNormalInternalThreat;
					}
					if (maxTime > maxTNormalInternalThreat) {
						maxTime = maxTNormalInternalThreat;
					}
				}
			}

			// create list of possible times - find remaining possible times and pick one
			LinkedList<Integer> possibleTimedThreatsIndices = new LinkedList<>();
			for (int t = minTime; t <= maxTime; t++) {
				// t is the time slot (1-indexes) and i is the index index (0-based)
				int i = t - 1;
				// If this timeslot is alread taken, skip it.
				if (timedThreats[i] != null) continue;

				// If this threat group contains an internal threat, skip this index
				// if the previous or next index also has an internal threat
				// as we do not allow consecutive internal threats.
				if (internalThreat != null) {
					if (t > 1 && timedThreats[i - 1] != null && timedThreats[i - 1].hasInternal()) {
						continue;
					}
					if (t < 8 && timedThreats[i + 1] != null && timedThreats[i + 1].hasInternal()) {
						continue;
					}
				}

				// This is a possible index to put this threat into
				possibleTimedThreatsIndices.add(i);
			}

			// no possible time slots left - giving up
			if (possibleTimedThreatsIndices.isEmpty()) {
				continue;
			}

			// We have one or more valid timeslots. Pick one at random,
			// create the two new arguments for the recursive call and call it.
			int possibleTimedThreatsIndex = possibleTimedThreatsIndices.get(generator.nextInt(possibleTimedThreatsIndices.size()));
			ThreatGroup[] newTimedThreats = timedThreats.clone();
			newTimedThreats[possibleTimedThreatsIndex] = threatGroup;
			ArrayList<ThreatGroup> newUntimedThreats = new ArrayList(Arrays.asList(untimedThreats));
			newUntimedThreats.remove(threatGroup);
			ThreatGroup[] result = assignTimeslotsToThreats(newUntimedThreats.toArray(new ThreatGroup[0]), newTimedThreats);
			if (result != null) return result;

			// If the result of the recursive call is null, it means that the picked candidate index didn't work.
			// Loop back and try another.
		}

		// We have run out of untimed threats to try. This is a dead end. Return null
		logger.info("Cannot find a time slot for an untimed threat. Back up.");
		return null;
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
            incomingData[i] = meanWeightedValueGenerator.nextInt(minIncomingData[i], maxIncomingData[i]);
            dataTransfers[i] = meanWeightedValueGenerator.nextInt(minDataTransfer[i], maxDataTransfer[i]);
			
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
		int whiteNoiseTime = meanWeightedValueGenerator.nextInt(minWhiteNoise, maxWhiteNoise);
		logger.fine("White noise time: " + whiteNoiseTime);
		
		// create chunks
		ArrayList<Integer> whiteNoiseChunks = new ArrayList<Integer>();
		while (whiteNoiseTime > 0) {
			// create random chunk
			int chunk = meanWeightedValueGenerator.nextInt(minWhiteNoiseTime, maxWhiteNoiseTime);
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
			phaseTimes[i] = meanWeightedValueGenerator.nextInt(minPhaseTime[i], maxPhaseTime[i]);
		}
	}
	
	/**
	 * generate phase stuff from data above
	 * @return true if phase generation succeeded
	 */
	protected boolean generatePhases() {
		logger.info("Data gathered: Generating phases.");

		// Deep copy as we modify the groups when attempting to fit - thanks to nibuen
		ThreatGroup[] threats = new ThreatGroup[this.threats.length];
		for (int i = 0; i < threats.length; i++) {
			ThreatGroup original = this.threats[i];
			threats[i] = new ThreatGroup(original.getInternal(), original.getExternal());
		}

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
				int ambushTime = meanWeightedValueGenerator.nextInt(0, 34) + phaseTimes[0] - 59;
				logger.info("Ambush in phase 1 at time: " + ambushTime);
				done = eventList.addEvent(ambushTime, maybeAmbush);
			} while (!done);
			
			threats[3].removeExternal();
			ambushOccured = true; // to disallow two ambushes in one game
		}

		// to be used further down
		int[] lastThreatTime = { 0, 0 };

		// add the rest of the threats
		int currentTime = meanWeightedValueGenerator.nextInt(minTimeForFirst[0], maxTimeForFirst[0]);
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
				int ambushTime = meanWeightedValueGenerator.nextInt(0, 34) + phaseTimes[0] + phaseTimes[1] - 59;
				logger.info("Ambush in phase 2 at time: " + ambushTime);
				done = eventList.addEvent(ambushTime, maybeAmbush);
			} while (!done);
			
			threats[7].removeExternal();
		}

		// add the rest of the threats
		currentTime = phaseTimes[0] + meanWeightedValueGenerator.nextInt(minTimeForFirst[1], maxTimeForFirst[1]);
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
