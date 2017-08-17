import java.util.LinkedList;
import java.util.Random;

/**
 * Created by christopher on 4/16/16.
 */
public class ScoutObject extends AntObjectTemplate {

    ScoutObject(GridNode gridNode) { //CONSTRUCTOR
        gridLocation = gridNode;
        previousTurn = -1;
    }
    ScoutObject() {

    }
    public void newTurn(int thisTurn) { //DEFINES NEW TURN FOR SCOUT
        if(previousTurn == thisTurn) {
            return;
        }
        int maxScoutLifeSpan = 3650;
        if((thisTurn - hatchNum) > maxScoutLifeSpan) {
            antDies();
            return;
        }
        previousTurn = thisTurn;
        Random random = new Random();
        LinkedList<GridNode> neighboringNodeList = gridLocation.getNeighboringNodes();
        GridNode targetNode;

        if(neighboringNodeList.size() == 0) {
            targetNode = neighboringNodeList.get(RandomInstance.randomNumberGen(1, neighboringNodeList.size() - 1) + 1);
        }
        else {
            targetNode = neighboringNodeList.get(random.nextInt(neighboringNodeList.size()));
        }
        traverseTo(targetNode);
    }
    private void traverseTo(GridNode gridNode) {
        gridLocation.deleteAntObject(this); //SWAPPING NODES
        gridLocation = gridNode;
        gridLocation.addNewAntObject(this);

        if(!gridLocation.isVisible()) {
            gridLocation.setNodeVisible(true);
        }
    }
}
