Java Space Alert Mission Generator
==================================

Requires Java 18+

Download executables in https://github.com/mkalus/JSpaceAlertMissionGenerator/releases


Start with: java -jar JSpaceAlertMissionGenerator.jar

  Options:
    --alarm
      Set background alarm (normal,none,fixed). Fixed means that background
      alarm will not become louder.
      Default: normal
    --allow-double-threats
      Enable double threats ("The New Frontier": one external and internal
      threat will occur on one time slot)
      Default: false
    --chance-for-ambush-1
      Chance in percent for ambush in phase 4
      Default: 40
    --chance-for-ambush-2
      Chance in percent for ambush in phase 8
      Default: 40
    --clips-folder, -c
      Folder in which MP3 clips are stored
      Default: clips
    --debug, -D
      Debug mode
      Default: false
    --filename, -f
      Output file name prefix
      Default: mission
    --gui, -g
      Start GUI
      Default: false
    --help, -h
      Print help and exit
      Default: false
    --list-renderers, --renderers, -L
      List available renderers and exit
      Default: false
    --max-data-operations-1
      Maximum data operations in phase 1
      Default: 3
    --max-data-operations-2
      Maximum data operations in phase 2
      Default: 3
    --max-data-operations-3
      Maximum data operations in phase 3
      Default: 1
    --max-data-transfers-1
      Maximum data transfers (data operation and incoming data) in phase 1
      Default: 1
    --max-data-transfers-2
      Maximum data transfers (data operation and incoming data) in phase 2
      Default: 2
    --max-data-transfers-3
      Maximum data transfers (data operation and incoming data) in phase 3
      Default: 1
    --max-incoming-data-1
      Maximum incoming data in phase 1
      Default: 3
    --max-incoming-data-2
      Maximum incoming data in phase 2
      Default: 2
    --max-incoming-data-3
      Maximum incoming data in phase 3
      Default: 2
    --max-internal-level, -I
      Maximum levels of internal threats
      Default: 3
    --max-internal-threats
      Maximum number of internal threats
      Default: 2
    --max-phase-time-1
      Maximum phase time for phase 1
      Default: 240
    --max-phase-time-2
      Maximum phase time for phase 2
      Default: 225
    --max-phase-time-3
      Maximum phase time for phase 3
      Default: 155
    --max-time-for-threat-1
      Maximum time for first threat to appear in phase 1
      Default: 20
    --max-time-for-threat-2
      Maximum time for first threat to appear in phase 2
      Default: 40
    --max-time-normal-external
      Maximum time in which normal external threats will occur
      Default: 8
    --max-time-normal-internal
      Maximum time in which normal internal threats will occur
      Default: 7
    --max-time-serious-external
      Maximum time in which serious external threats will occur
      Default: 7
    --max-time-serious-internal
      Maximum time in which serious internal threats will occur
      Default: 6
    --max-whitenoise-single
      Maximum time of whitenoise of a single communication break
      Default: 20
    --max-whitenoise-total
      Maximum time of whitenoise in total
      Default: 60
    --min-data-operations-1
      Minimum data operations in phase 1
      Default: 2
    --min-data-operations-2
      Minimum data operations in phase 2
      Default: 2
    --min-data-operations-3
      Minimum data operations in phase 3
      Default: 0
    --min-data-transfers
      Minimum data transfers (data operation and incoming data) in all phases
      Default: 3
    --min-data-transfers-1
      Minimum data transfers (data operation and incoming data) in phase 1
      Default: 0
    --min-data-transfers-2
      Minimum data transfers (data operation and incoming data) in phase 2
      Default: 1
    --min-data-transfers-3
      Minimum data transfers (data operation and incoming data) in phase 3
      Default: 1
    --min-incoming-data
      Minimum incoming data in all phases
      Default: 2
    --min-incoming-data-1
      Minimum incoming data in phase 1
      Default: 1
    --min-incoming-data-2
      Minimum incoming data in phase 2
      Default: 0
    --min-incoming-data-3
      Minimum incoming data in phase 3
      Default: 0
    --min-internal-level, -i
      Minimum levels of internal threats
      Default: 1
    --min-phase-time-1
      Minimum phase time for phase 1
      Default: 205
    --min-phase-time-2
      Minimum phase time for phase 2
      Default: 180
    --min-phase-time-3
      Minimum phase time for phase 3
      Default: 140
    --min-time-for-threat-1
      Minimum time for first threat to appear in phase 1
      Default: 10
    --min-time-for-threat-2
      Minimum time for first threat to appear in phase 2
      Default: 10
    --min-time-normal-external
      Minimum time in which normal external threats will occur
      Default: 1
    --min-time-normal-internal
      Minimum time in which normal internal threats will occur
      Default: 2
    --min-time-serious-external
      Minimum time in which serious external threats will occur
      Default: 2
    --min-time-serious-internal
      Minimum time in which serious internal threats will occur
      Default: 3
    --min-whitenoise-single
      Minimum time of whitenoise of a single communication break
      Default: 9
    --min-whitenoise-total
      Minimum time of whitenoise in total
      Default: 45
    --output, -o
      Write mission to file using renderer, can be repeated for more renderes
      Default: []
    --play, -x
      Play MP3 after printing and outputting
      Default: false
    --print, -p
      Print mission to screen using renderer, can be repeated for more
      renderes
      Default: []
    --seed, -S
      Set a random number generator seed
    --silent, -s
      Silent mode
      Default: false
    --threat-level, -l
      Threat level of mission (should be <= 14)
      Default: 8
    --threats-within-percent
      Threats appear within this percentage of a phase
      Default: 70
    --unconfirmed-threat-level, -u
      Threat level unconfirmed threats
      Default: 1

Available renderers:
 - text
 - XML
 - MP3
 - FlashPlayerCode


Some Examples:

java -jar JSpaceAlertMissionGenerator.jar -p text --play
 - Print mission as English text file and start playing MP3s.

java -jar JSpaceAlertMissionGenerator.jar -o XML -p text -p MP3
 - Print mission as text on screen, save it as XML and start playing MP3 ("print" MP3 translates to --play).

java -jar JSpaceAlertMissionGenerator.jar -p text -l 10 -u 2 -i 2 -I 4 --allow-double-threats --max-time-serious-external 8 --max-time-normal-internal 8
 - Create "Standard Mission" for "The New Frontier" expansion using double actions.

Right now, the generator is quite basic: Start it from the command line, and it will output a mission profile.
It will also play the mission if you provide MP3 files in a directory called "clips" in the same directory where you execute the jar.


In order to play the MP3 clips, you need to download a set of MP3 files and save them in "clips".
Look at https://github.com/nibuen/SpaceAlertMissionGenerator/tree/master/app/src/main/res/raw for the English files.

Other languages:
* Czech: https://boardgamegeek.com/filepage/41495/space-alert-mp3-random-mission-generator-czech-voi
* French: https://boardgamegeek.com/filepage/146086/voice-text-french-pack-space-alert-mission-generat
* German: https://github.com/mkalus/AndroidSpaceAlertApplicationDE/tree/master/res/raw
* Greek: https://boardgamegeek.com/filepage/70048/greek-male-voice-pack
* Japanese: https://boardgamegeek.com/filepage/52302/japanese-voice-files-spacealertmissiongenerator
* Japanese "Yukkuri" pack: https://boardgamegeek.com/filepage/56305/japanese-yukkuri-voice-pack
* Russian: https://boardgamegeek.com/filepage/120033/russian-voice-pack
* Spanish: https://boardgamegeek.com/filepage/41392/voces-en-espanol-para-el-generador-de-misiones-en

Special voice files:
* Female 1: https://boardgamegeek.com/filepage/46941/female-voice-file-pack-random-mission-generator
* Female 2: https://boardgamegeek.com/filepage/54664/female-voice-pack-random-mission-generator
* GLaDOS: https://boardgamegeek.com/filepage/114996/glados-soundpack-android-mission-generator

Have fun!

Known bugs:
- currently none

Planed additions and wishlist:

- configuration via command line options
- import of XML, Flash File Code and text
- MP3 file export (generating an MP3 file of the whole mission)
- graphical GUI



Compiling Java Space Alert Mission Generator:

You can compile the generator yourself if you do not want to download the JAR above, you can compile the source yourself:

Download using git:
git clone git://github.com/mkalus/JSpaceAlertMissionGenerator.git

Change to directory JSpaceAlertMissionGenerator:
cd JSpaceAlertMissionGenerator

Compile using Maven2-Build System and Java:
mvn install

The executable will be compiled in:
target/JSpaceAlertMissionGenerator-1.4.1-jar-with-dependencies.jar


If you do not use Maven2, you will need following libraries:
- JLayer is an MP3 player (LGPL-Licence): http://www.javazoom.net/javalayer/javalayer.html
- Options Parser (Apache Licence): http://jcommander.org/