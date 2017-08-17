import java.util.LinkedList;
import java.util.Random;

/**
 * Created by christopher on 4/16/16.
 */
public class SoldierObject extends AntObjectTemplate {


    SoldierObject(GridNode gridNode) { // CONSTRUCTOR
        gridLocation = gridNode;
        previousTurn = -1;
    }
    SoldierObject() {

    }
    //DEFINES WHAT HAPPENS DURING A BATTLE BETWEEN SOLDIER AND BALA. THIS IS IDENTICAL TO THE antsBattle() METHOD IN THE BALA CLASS, ONLY REVERSED
    private void antsBattle() {
        LinkedList<AntObjectTemplate> nonFriendlyList = gridLocation.getSpecificTypes(new BalaObject());
        /*
        DECIDED NOT TO ACCESS THE CUSTOM RANDOM CLASS. 50/50 IS MORE SIMPLY ACQUIRED WITH .nextBoolean()
         */
        Random chanceOfDeath = new Random();
        if(chanceOfDeath.nextBoolean()) { //GIVES A 50/50 PROBABILITY OF DEATH BY SOLDIER
            nonFriendlyList.get(0).antDies();
        }
    }
    public void newTurn(int thisTurn) {
        if(previousTurn == thisTurn) {
            return;
        }
        int maxSoldierLifeSpan = 3650;
        if((thisTurn - hatchNum) > maxSoldierLifeSpan) {
            antDies();
            return;
        }
        previousTurn = thisTurn;

        if(gridLocation.getSpecificTypes(new BalaObject()).size() > 0) {
            antsBattle();
        }
        else {
            Random random = new Random();
            LinkedList<GridNode> neighborNodeList = gridLocation.getNeighboringNodes();
            LinkedList<GridNode> discoveredNodeList = new LinkedList<>();
            GridNode targetNode;
            for (GridNode aNeighborNodeList : neighborNodeList) {
                if (aNeighborNodeList.isVisible()) {
                    discoveredNodeList.add(aNeighborNodeList);
                }
            }
            if(discoveredNodeList.size() == 0) {
                targetNode = discoveredNodeList.get(RandomInstance.randomNumberGen(1, discoveredNodeList.size() - 1));// + 1);
            }
            else {
                targetNode = discoveredNodeList.get(random.nextInt(discoveredNodeList.size()));
            }
            for(GridNode node : discoveredNodeList) {
                if(node.getSpecificTypes(new BalaObject()).size() > 0) {
                    targetNode = node;
                }
            }
            traverseTo(targetNode);
        }
    }
    private void traverseTo(GridNode gridNode) {
        gridLocation.deleteAntObject(this); // SWAPPING NODES
        gridLocation = gridNode;
        gridLocation.addNewAntObject(this);
    }
}
