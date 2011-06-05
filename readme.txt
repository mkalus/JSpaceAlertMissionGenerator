Java Space Alert Mission Generator
==================================

Download executable: http://dl.dropbox.com/u/9102471/JSpaceAlertMissionGenerator.jar


Start with: java -jar JSpaceAlertMissionGenerator.jar

  Options:
    --debug, -D                         Debug mode
                                        Default: false
    --filename, -f                      Output file name prefix
                                        Default: mission
    --help, -h                          Print help and exit
                                        Default: false
    --list-renderers, --renderers, -l   List available renderers and exit
                                        Default: false
    --output, -o                        Write mission to file using renderer,
                                        can be repeated for more renderes
                                        Default: []
    --play, -x                          Play MP3 after printing and outputting
                                        Default: false
    --print, -p                         Print mission to screen using renderer,
                                        can be repeated for more renderes
                                        Default: []
        --print-seed                    Print the seed number at the beginning
                                        of the mission
                                        Default: false
        --seed                          Set a random number generator seed
    --silent, -s                        Silent mode
                                        Default: false

Available renderers:
 - text
 - XML
 - MP3


Some Examples:

java -jar JSpaceAlertMissionGenerator.jar -p text --play
 - Print mission as English text file and start playing MP3s.

 -jar JSpaceAlertMissionGenerator.jar -o XML -p text -p MP3
 - Print mission as text on screen, save it as XML and start playing MP3 ("print" MP3 translates to --play).


Right, now the generator is quite basic: Start it from the command line and it will output a mission profile.
It will also play the mission if you provide MP3 files in a directory called "clips" in the same directory where you execute the jar.


In order to play the MP3 clips, you need to download a set of MP3 files and save them in "clips".
Look at http://sites.google.com/site/boardgametools/SpaceAlertMissionGenerator.
German and English Sound sets are included in the the Space Alert Mission Generator.
You can also look into the forums on http://www.boardgamegeek.com/ which provide some language files for Japanese and so on.

Have fun!

Known bugs:
- currently none

Planed additions:

- output options: code for flash-player, mp3-file
- GUI...



Compiling Java Space Alert Mission Generator:

You can compile the generator youself if you do not want to download the JAR above, you can compile the source yourself:

Download using git:
git clone git://github.com/mkalus/JSpaceAlertMissionGenerator.git

Change to directory JSpaceAlertMissionGenerator:
cd JSpaceAlertMissionGenerator

Compile using Maven2-Build System and Java:
mvn install

The executable will be compiled in:
target/JSpaceAlertMissionGenerator-0.0.1-SNAPSHOT-jar-with-dependencies.jar