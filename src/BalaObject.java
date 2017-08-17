import javax.swing.*;
import static javax.swing.JOptionPane.*;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by christopher on 4/15/16.
 */
public class BalaObject extends AntObjectTemplate {

    //String fileName = "src/Images/deadants.png";
    String fileName = "deadants.png";

    BalaObject(GridNode gridNode) {
        gridLocation = gridNode;
        previousTurn = -1;
    }
    BalaObject() {

    }
    public void newTurn(int thisTurn) {
        if(previousTurn == thisTurn) { // MAKE SURE TURN ISN'T ALREADY TAKEN
            return;
        }
        int maxBalaLifeSpan = 3650;
        if((thisTurn - hatchNum) > maxBalaLifeSpan) { // SELF EXPLANATORY
            antDies();
            return;
        }
        previousTurn = thisTurn;
        LinkedList<AntObjectTemplate> friendlyAnts = gridLocation.getFriendlyAnts();
        if(friendlyAnts.size() > 0) { //IF FRIENDLIES ARE PRESENT, LET'S BATTLE!
            antBattle();
        }
        else {
            //OTHERWISE, MOVE ALONG RANDOMLY
            Random random = new Random();
            LinkedList<GridNode> neighboringNodeList = gridLocation.getNeighboringNodes();
            GridNode targetNode;
            if(neighboringNodeList.size() == 0) {
                targetNode = neighboringNodeList.get(random.nextInt(neighboringNodeList.size()));
            }
            else {
                targetNode = neighboringNodeList.get(RandomInstance.randomNumberGen(1, neighboringNodeList.size() - 1));// + 1);
            }
            traverseTo(targetNode);
        }
    }
    private void antBattle() { //RANDOMLY DETERMINES OUTCOME OF ANT ENCOUNTER

        LinkedList<AntObjectTemplate> friendlyAnt = gridLocation.getFriendlyAnts();
        Random chanceOfDeath = new Random();
        if (chanceOfDeath.nextBoolean() == true) { //GIVES A 50/50 PROBABILITY OF DEATH BY BALA
            friendlyAnt.get(0).antDies(); //THE FRIENDLY ANT DIES
        }
        if (chanceOfDeath.nextBoolean() == true && friendlyAnt.get(0) instanceof QueenObject) { //IF BALA ATTACKS, AND IT'S THE QUEEN

            //simManager.playGameOverSong();
            SimManager.thisThread.interrupt(); //STOP MAIN THREAD TO AVOID COLLISION AND INTERRUPTED EXCEPTION.
            //System.out.println(chanceOfDeath.nextBoolean()); //THIS WAS A DEBUGGING STATEMENT. LEFT BEHIND FOR POSSIBLE FURTHER PRODUCTION IF BUG NOT FULLY ELIMINATED.
            Thread t = new Thread(new Runnable() {
                public void run() {

                    ImageIcon icon = new ImageIcon(fileName);
                    //DISPLAY CUSTOM ICON IN DIALOG BOX
                    showMessageDialog(null, "Your Queen has died from a Bala attack!\nShe lived " + SimManager.day + " Days\nSimulation Over.\nCLICK OK TO END", "SIMULATION ENDED", INFORMATION_MESSAGE, icon);
                    System.exit(0);
                }
            });
            t.start(); //START THE NEXT THREAD AFTER QUEEN DIES AND ICON IS ACCESSED.
        }
    }
    private void traverseTo(GridNode gridNode) {
        gridLocation.deleteAntObject(this); //SWAPPING NODES
        gridLocation = gridNode;
        gridLocation.addNewAntObject(this);
    }
}
