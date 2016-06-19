/**
 * 
 */
package de.beimax.spacealert.exec;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.beimax.spacealert.mission.Mission;
import de.beimax.spacealert.mission.MissionImpl;
import de.beimax.spacealert.mp3.BackgroundMP3PlayerFactory;
import de.beimax.spacealert.mp3.MP3MissionPlayer;
import de.beimax.spacealert.util.Options;

/**
 * @author mkalus
 * 
 */
public class Gui extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1311362317523759091L;

	static private Logger logger = Logger.getLogger("CommandLine");
	static {
		// debugging option set?
		if (Options.getOptions().debug) logger.setLevel(Level.FINEST);
		else logger.setLevel(Level.WARNING);
	}

	/**
	 * button to start mission
	 */
	private JButton startMissionBtn;

	/**
	 *  mission output
	 */
	private JTextArea missionOutputTArea;
	
	/**
	 *  mission
	 */
	private Mission mission;
	
	/**
	 * mission MP3 player
	 */
	private MP3MissionPlayer player;

	/**
	 * Constructor that builds JFrame and starts it
	 */
	public Gui() {
		// set title
		this.setTitle("Java Space Alert Mission Generator");

		// get content pane
		Container pane = this.getContentPane();

		// button
		startMissionBtn = new JButton("Start Mission!");
		startMissionBtn.setActionCommand("actionBtnPressed");
		startMissionBtn.addActionListener(this);
		startMissionBtn.setPreferredSize(new Dimension(200, 50));
		pane.add(startMissionBtn, BorderLayout.SOUTH);

		// text area
		missionOutputTArea = new JTextArea();
		missionOutputTArea.setPreferredSize(new Dimension(600, 500));
		missionOutputTArea.setEditable(false);
		// line wrap on
		missionOutputTArea.setLineWrap(true);
		missionOutputTArea.setWrapStyleWord(true);
		// scrollable
		JScrollPane scrollPane = new JScrollPane(missionOutputTArea); // add scroller
		pane.add(scrollPane, BorderLayout.CENTER);

		// close on end
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// pack window and show
		this.pack();
		centerScreen();
	}

	/**
	 * centers frame on screen and also shows it
	 */
	private void centerScreen() {
		Dimension dim = getToolkit().getScreenSize();
		Rectangle abounds = getBounds();
		setLocation((dim.width - abounds.width) / 2,
				(dim.height - abounds.height) / 2);
		setVisible(true);
		requestFocus();
	}

	/**
	 * play MP3 if option is set to do so
	 *  @param mission to play
	 */
	private void playMP3(Mission mission) {
		logger.info("Starting MP3 playback thread.");
		
		// create mp3 player
		player = new MP3MissionPlayer(mission, BackgroundMP3PlayerFactory.getBackgroundMP3Player(Options.getOptions().backgroundAlarm));

		// start it in a new thread
		Thread playerThread = new Thread(player);
		playerThread.start();
	}
	
	/**
	 * start a mission
	 */
	private void startMission() {
		// generate default mission
		mission = new MissionImpl();
		mission.generateMission();

		// get options
		Options options = Options.getOptions();
		
		// check for clips directory
		if (!checkForClipDirectory(options.clipsFolder)) {
			missionOutputTArea.setText(mission.toString());
			missionOutputTArea.append("\nIn order to play the MP3 clips, you need to download a set of MP3 files and save them in a directory named clips (or specified by --clips-folder option) in the same directory as the jar.\nLook at http://sites.google.com/site/boardgametools/SpaceAlertMissionGenerator.\nGerman and English Sound sets are included in the the Space Alert Mission Generator. You can also look into the forums on http://www.boardgamegeek.com/ which provide some language files for Japanese and so on.");
			// delete mission
			mission = null;
			return;
		}

		// change button
		startMissionBtn.setText("Stop Mission!");
		
		// output text
		missionOutputTArea.setText(mission.toString());
		
		// start playing MP3
		playMP3(mission);
	}

	/**
	 * stop mission
	 */
	private void stopMission() {
		// change button
		startMissionBtn.setText("Waiting for MP3 to finish...");
		startMissionBtn.setEnabled(false);
		update(getGraphics()); // update to actually show button changed
		
		// waiter thread - does not work so well right now...
		new Thread() {
			@Override
			public void run() {
				// stop player
				if (player != null) player.stopPlayer();

				// wait for the player to stop
				while (!player.isFinished()) {
					try {
						sleep(100);
					} catch (InterruptedException e) {}
				}

				// free mission
				mission = null;

				// re-enable button again
				startMissionBtn.setText("Start Mission!");
				startMissionBtn.setEnabled(true);
			}
		}.run();
	}

	/**
	 * check for the existence of the clip directory
	 * @return true if directory exists
	 */
	private static boolean checkForClipDirectory(String folder) {
		File file = new File(folder);
		if (!file.exists() || !file.isDirectory()) return false;
		return true;
	}

	/**
	 * called when action was performed in frame
	 */
	public void actionPerformed(ActionEvent event) {
		// start mission button was pressed
		if (event.getActionCommand().equals("actionBtnPressed")) {
			if (mission == null) {
				startMission();
			} else {
				stopMission();
			}
		}
	}
}