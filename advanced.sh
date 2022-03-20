#!/bin/bash

# Usual invocation:

# To save the mission to a text file for check, and start the
# sound:
# ./advanced.sh -o text --play

# To print the mission to the screen for check, and start the
# sound:
# ./advanced.sh -p text --play

java -jar target/JSpaceAlertMissionGenerator-1.3.0-jar-with-dependencies.jar \
	--unconfirmed-threat-level 0 \
	--threat-level 10 \
	--min-phase-time-1 285 --max-phase-time-1 305 \
	--min-phase-time-2 260 --max-phase-time-2 280 \
	--min-phase-time-3 230 --max-phase-time-3 250 \
	--chance-for-ambush-1 40 --chance-for-ambush-2 40 \
	--max-incoming-data-1 2 \
	--max-incoming-data-2 2 \
	--max-incoming-data-3 1 \
	--min-incoming-data 1 \
	--max-data-transfers-1 3 \
	--max-data-transfers-2 3 \
	--max-data-transfers-3 3 \
	--min-data-transfers 4 \
	--max-data-operations-1 4 \
	--max-data-operations-2 3 \
	--max-data-operations-3 3 \
	--min-data-operations-1 2 \
	--min-data-operations-2 2 \
	--min-data-operations-3 1 \
	--max-internal-level 4 \
	--max-internal-threats 3 \
	--min-time-serious-internal 2 \
	--min-time-serious-external 1 \
	$*
#
#	--max-incoming-data 3 \
#	--max-data-transfers 7 \
#	--max-data-operations 9 \
#	--min-data-operations 6 \
