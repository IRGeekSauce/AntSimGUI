import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import static javax.swing.JOptionPane.*;

/**
 * Created by christopher on 4/16/16.
 */
public class SimManager implements SimulationEventListener, LineListener  {

    AntColony colonySim;
    AntSimGUI antSimGUI;
    int thisTurn = 0;
    public static boolean isQueenAlive;
    public static boolean isMoving;
    public static Thread thisThread;
    int year = 1;
    public static int day = 1;
    int turn = 1;
    String time = "Year 0   Day 0   Turn 0";
    String queenAgeTestTime = "Year 19   Day 364   Turn 0"; //CUSTOM LABEL FOR QUEEN LIFESPAN TEST
    //String questionAnt = "src/Images/AntQuestionMark.jpg";
    //String wavingAnt = "src/Images/AntWaving.jpg";
    String questionAnt = "AntQuestionMark.jpg";
    String wavingAnt = "AntWaving.jpg";
    //File mainMusicThemeSong = new File("src/Sounds/CircusSong.wav"); //SONG THAT PLAYS DURING RUNTIME
    File mainMusicThemeSong = new File("CircusSong.wav"); //SONG THAT PLAYS DURING RUNTIME
    Clip mainClip;



    SimManager(AntSimGUI antSimGUI) { //CONSTRUCTOR

        isQueenAlive = true;
        isMoving = true;
        this.antSimGUI = antSimGUI;
        colonySim = new AntColony(new ColonyView(27, 27), this);
        antSimGUI.initGUI(colonySim.getColonyView());
        thisThread = null;
    }
    public void update(LineEvent le) { // REQUIRED METHOD FOR IMPLEMENTING LINE LISTENER.

        LineEvent.Type type = le.getType();
        if(type == LineEvent.Type.OPEN) {
            System.out.println("OPENED SONG FILE");
        }
        else if(type == LineEvent.Type.CLOSE) {
            System.out.println("CLOSED SONG FILE");
        }
        else if(type == LineEvent.Type.START) {
            System.out.println("START SOUND FILE");
        }
        else if(type == LineEvent.Type.STOP) {
            System.out.println("STOPPED SOUND FILE...CLOSING");
            mainClip.close();
        }
    }
    //DETERMINES WHICH TURN THE COLONY IS CURRENTLY ON
    public void newTurn() {

        do {
            year = thisTurn / 3650;
            day = (thisTurn / 10) % 365;
            turn = thisTurn % 10;
            time = "Year " + year + "   Day " + day + "   Turn " + turn;
            antSimGUI.setTime(time);
            colonySim.newTurn(thisTurn);
            thisTurn++;
            try {
                Thread.sleep(300); //THIS CAUSES THE THREAD TO SUSPEND EXECUTION SO IT PACES THE SIMULATION AT A DECENT SPEED.
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } while (!isMoving && isQueenAlive);
    }
    public void stopAntSimulation() { //STOPS THE THREAD AND SOUND CLIP

        isQueenAlive = false;
        mainClip.stop();
        if (thisThread != null) {
            thisThread.interrupt();
        }
    }
    public void playMusic() { // PLAYS SOUND FILE

        try {
            Line.Info linfo = new Line.Info(Clip.class);
            Line line = AudioSystem.getLine(linfo);
            mainClip = (Clip) line;
            mainClip.addLineListener(this);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(mainMusicThemeSong);
            mainClip.open(audioIn);
            mainClip.start();
        } catch (Exception e) {
            System.out.println("ERROR PLAYING MUSIC CLIP");
            e.printStackTrace();
        }
    }
    /**
     * THIS WILL DETERMINE WHICH TESTS ARE PERFORMED. IF USER SELECTS "QUEEN TEST", THEY WILL BE GIVEN THE OPTION TO TEST FOR FOOD LEVELS OR AGE LIMIT
     *
     * @param simulationEvent
     */

    public void simulationEventOccurred(SimulationEvent simulationEvent)  {

        if (simulationEvent.getEventType() == SimulationEvent.NORMAL_SETUP_EVENT) {

            colonySim.initializeAntColony(); // INITIALIZE NORMALLY
            antSimGUI.setTime(time);
        }
        if (simulationEvent.getEventType() == SimulationEvent.RUN_EVENT) {

            isMoving = false;
            thisThread = new Thread() {
                public void run() {

                    playMusic();
                    mainClip.loop(-1); // CONTINUOUSLY LOOP .WAV FILE UNTIL SIMULATION ENDS, OR USER PRESSES STOP
                    newTurn(); // START INCREMENTING THROUGH TURNS
                }
            };
            thisThread.start();
        }
        if (simulationEvent.getEventType() == SimulationEvent.STEP_EVENT) {

            isMoving = true;
            newTurn(); // JUST GO THROUGH ONE TURN EACH TIME STEP IS PRESSED
        }
        if(simulationEvent.getEventType() == SimulationEvent.STOP_EVENT) { //THIS WILL KILL THE SIM DURING ANY PARTICULAR MODE

            stopAntSimulation();
            mainClip.stop();
            ImageIcon questionMark = new ImageIcon(questionAnt);
            int option = showOptionDialog(null, "Are you sure you wish to exit?", "Simulation Paused", YES_NO_OPTION, QUESTION_MESSAGE, questionMark, new String[]{"EXIT", "RESUME"}, "");
            if (option == NO_OPTION) {
                isQueenAlive = true;
                antSimGUI.fireSimulationEvent(5);
                mainClip.start();
            }
            if(option == YES_OPTION) {
                stopAntSimulation();

                Thread exitThread = new Thread() {
                    public void run() {
                        ImageIcon wavingAntIcon = new ImageIcon(wavingAnt);
                        showMessageDialog(null, "Thank you for playing!\n            Goodbye!", "Simulation Over!", INFORMATION_MESSAGE, wavingAntIcon);
                        System.exit(0);
                    }
                }; exitThread.start();
            }
        }
        if (simulationEvent.getEventType() == SimulationEvent.QUEEN_TEST_EVENT) {

            //playSelectionSound("src/Sounds/CameraClickWAV.wav");
            /**
             * NOW GIVE THE USER THE OPTION TO PICK WHICH VERSION OF QUEEN TEST TO PERFORM. CAN CHOOSE LIFESPAN OR FOOD LEVEL.
             * A SINGLE QUEEN TEST PROVED TO BE LENGTHY IF CLOCK WAS SET TO ZERO, SO TWO SEPARATE TESTS WERE IMPLEMENTED.
             */
            //CREATE CUSTOM JBUTTONS INSTEAD OF "OK"/"CANCEL"

            ImageIcon questionMark = new ImageIcon(questionAnt);
            int option = showOptionDialog(null, "Do you wish to test the Queen's food level\nor the Queen's lifespan?", "SELECT TEST MODE", YES_NO_OPTION, QUESTION_MESSAGE, questionMark, new String[]{"FOOD", "LIFESPAN"}, "default");

            if (option == YES_OPTION) { // FOOD OPTION

                colonySim.initializeQueenFoodTest(); // INITIALIZE COLONY WITH ONLY FOOD IN MIND
                antSimGUI.setTime(time);
            }
            if (option == NO_OPTION) { // AGE OPTION

                colonySim.initializeQueenAgeTest(); // INITIALIZE COLONY WITH ONLY LIFESPAN IN MIND
                thisTurn = 72980; // SET THE QUEEN TO JUST 20 TURNS BEFORE DEATH
                antSimGUI.setTime(queenAgeTestTime); // SET THE NEW CUSTOM LABEL TO NEAR DEATH
            }
        }
        if (simulationEvent.getEventType() == SimulationEvent.FORAGER_TEST_EVENT) {

            colonySim.initializeForagerTest(); //SET UP WITH ONLY FORAGERS AND A QUEEN. SCOUTS INCLUDED SO FORAGERS HAVE ROOM TO EXPLORE
            antSimGUI.setTime(time);
        }
        if(simulationEvent.getEventType() == SimulationEvent.SCOUT_TEST_EVENT) {

            colonySim.initializeScoutTest(); //SET UP WITH ONLY SCOUTS AND A QUEEN
            antSimGUI.setTime(time);
        }
        if(simulationEvent.getEventType() == SimulationEvent.SOLDIER_TEST_EVENT) {

            colonySim.initializeSoldierTest(); //SET UP WITH ONLY SOLDIERS, QUEEN, AND SCOUTS.
            antSimGUI.setTime(time);
        }
    }
}



