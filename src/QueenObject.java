import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;

import static javax.swing.JOptionPane.*;

/**
 * Created by christopher on 4/16/16.
 */
/*
QUEEN IS STATIONARY
DEFINES QUEEN OBJECT, AS WELL AS A QUEEN OBJECT TURN
QUEEN CREATES NEW ANTS: 25% SOLDIERS, 25% SCOUTS, 50% FORAGER
 */
public class QueenObject extends AntObjectTemplate implements LineListener {

    int thisTurn;
    int previousAntID;
    int antID;
    int maxQueenLifeSpan = 73000;
    //String sadQueen = "src/Images/SadAnt.jpg";
    //String happyQueen = "src/Images/HappyAnt.jpg";
    String sadQueen = "/Images/SadAnt.jpg";
    String happyQueen = "/Images/HappyAnt.jpg";
    File gameOverSong = new File("src/Sounds/SadTrombone.wav");
    SimManager simManager;
    public static Clip mainClip;
    public static Clip gameOverClip;
    //public final Thread t = null;




    public QueenObject(GridNode gridNode) { //CONSTRUCTOR FOR NEW QUEEN OBJECT
        gridLocation = gridNode;
        previousAntID = 0;
        antID = 0;
    }
    QueenObject() {

    }
    public void update(LineEvent le) {

        LineEvent.Type type = le.getType();
        if(type == LineEvent.Type.OPEN) {
            System.out.println("OPEN");
        }
        else if(type == LineEvent.Type.CLOSE) {
            System.out.println("CLOSE");
        }
        else if(type == LineEvent.Type.START) {
            System.out.println("START");
        }
        else if(type == LineEvent.Type.STOP) {
            System.out.println("STOP");
            //mainClip.close();
        }
    }
    public void createNewAntObject(AntObjectTemplate antObject) { // RANDOM HATCHES OF PARTICULAR ANTS WITH EACH DAY

        int randomHatch = RandomInstance.randomNumberGen(1, 4);
        AntObjectTemplate newAntObject;

        if (randomHatch < 2) {
            newAntObject = new ScoutObject(gridLocation);
            //antObject = new ScoutObject(gridLocation);
        }
        else if (randomHatch == 2) {
            newAntObject = new SoldierObject(gridLocation);
            //antObject = new SoldierObject(gridLocation);
        }
        else {
            newAntObject = new ForagerObject(gridLocation);
            //antObject = new ForagerObject(gridLocation);
        }
        if (antObject != null) {
            newAntObject = antObject;

            newAntObject.setHatchNum(thisTurn);
            previousAntID++;
            newAntObject.setUniqueID(previousAntID);
            gridLocation.addNewAntObject(newAntObject);
        }
    }
    /**
     * NOTE: QUEEN DOES NOT CREATE BALA ANTS, BUT EACH QUEEN TURN CAN COINCIDE WITH THE APPEARANCE OF BALA ANTS
     */
    public void spawnBalaAnt() {
        int chanceOfBala = RandomInstance.randomNumberGen(0, 100);
        GridNode balaObjectLocation = gridLocation.antColony.grid[0][0]; //THIS WILL MAKE BALA APPEAR IN UPPER LEFT CORNER EACH TURN
        if(chanceOfBala < 30) { //30 == 3 PERCENT
            BalaObject balaObject = new BalaObject(balaObjectLocation);
            balaObjectLocation.addNewAntObject(balaObject);
            balaObject.setHatchNum(thisTurn);
            previousAntID++;
            balaObject.setUniqueID(previousAntID);
        }
    }
    public void consumeFood() {
        int countFoodUnit = gridLocation.getFoodUnit();
        if(countFoodUnit < 1) {
            //KEYWORD THIS SINCE QUEEN DEATH IS MORE SIGNIFICANT THAN NORMAL ANT DEATH
            this.antDies();
        }
        countFoodUnit = countFoodUnit - 1; // DECREMENT FOOD UNITS EACH TURN
        gridLocation.setFoodUnit(countFoodUnit);
    }
    public void antDies() { // SIM ENDS IF QUEEN DIES
        gridLocation.colonyNodeView.hideQueenIcon();
        //simManager.mainClip.stop();
        gridLocation.antColony.simulation.stopAntSimulation();
    }
    public void newTurn(int thisTurn) {

        this.thisTurn = thisTurn;
        // QUEEN WILL DIE FROM OLD AGE UPON REACHING MAX LIFE SPAN OF 20 YEARS, IF THE SIM MAKES IT THAT FAR (Probably not)

        if((thisTurn == maxQueenLifeSpan)) {
            antDies(); // IF THE THREAD IS NOT INFORMED THAT THE QUEEN HAS DIED, THE MESSAGE DIALOG WILL ENTER AN INFINITE LOOP
            SimManager.thisThread.interrupt();
            Thread t = new Thread() {
                public void run() {
                    ImageIcon icon = new ImageIcon(happyQueen);
                    showMessageDialog(null, "You finished! Your Queen died from old age.", "Simulation Ended", JOptionPane.INFORMATION_MESSAGE, icon);
                    System.exit(0);
                }
            };
            t.start();
        }

        //QUEEN WILL DIE DUE TO LACK OF FOOD
        if(gridLocation.getFoodUnit() < 1) {
            this.antDies();
            SimManager.thisThread.interrupt();
            Thread t = new Thread() {
                public void run() {
                    ImageIcon icon = new ImageIcon(sadQueen);
                    showMessageDialog(null, "Your Queen died of starvation! Bad Foragers!", "Simulation Ended", JOptionPane.INFORMATION_MESSAGE, icon);
                    System.exit(0);
                }
                    };
            t.start();
        }

        if((thisTurn % 10) == 0) { //QUEEN WILL HATCH ANTS EVERY TURN
            this.createNewAntObject(null);
        }
        spawnBalaAnt(); //3% CHANCE OF BALA APPEARANCE WILL BE DEFINED IN THIS METHOD
        this.consumeFood(); //QUEEN WILL CONSUME FOOD EACH TURN
    }
}
