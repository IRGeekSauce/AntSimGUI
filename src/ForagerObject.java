import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

/**
 * Created by christopher on 4/16/16.
 */
public class ForagerObject extends AntObjectTemplate {

    private boolean returning;
    private Stack<GridNode> foragerMoveHistory;
    private int traceSteps;

    public ForagerObject(GridNode gridNode) {
        gridLocation = gridNode;
        previousTurn = -1;
        returning = false;
        foragerMoveHistory = new Stack<>();
        foragerMoveHistory.push(gridLocation);
    }

    ForagerObject() {

    }

    private void foodIsPresent() { //DETERMINES IF FOOD IS DISCOVERED, AND BACKTRACKS TO THE NEXT
        if (gridLocation.getFoodUnit() > 0 && !(gridLocation.containsQueen())) {
            gridLocation.setFoodUnit(gridLocation.getFoodUnit() - 1);
            returning = true;
            traceSteps = foragerMoveHistory.size() - 1;
            return;
        } else {
            return;
        }
    }

    //DEFINES A SINGLE TURN FOR A FORAGER.
    public void newTurn(int thisTurn) {
        GridNode targetNode;
        if (previousTurn == thisTurn) {
            return;
        }
        previousTurn = thisTurn;
        if (returning) {
            traceSteps--; //IF FORAGER IS BACKTRACKING, DECREMENT STEPS TAKEN.
            targetNode = foragerMoveHistory.get(traceSteps);
            dropOffPherms();
        } else {
            targetNode = locateHighestPherms();
        }
        traverseTo(targetNode);
        if (!returning) {
            foodIsPresent();
        }
        int maxForagerLifeSpan = 3650;
        if ((thisTurn - hatchNum) > maxForagerLifeSpan) {
            antDies();
        }
    }

    public void antDies() {
        if (returning) {
            gridLocation.setFoodUnit(gridLocation.getFoodUnit() + 1);
        }
        gridLocation.deleteAntObject(this);
    }

    private void dropOffPherms() {
        if (!gridLocation.containsQueen()) {
            if (gridLocation.getPheromoneUnit() < 1000) { //IF PHEROMONE LEVEL IS BELOW MAX AND IT'S NOT THE QUEEN NODE, ADD 10 TO THE NODE
                gridLocation.setPheromoneUnit(gridLocation.getPheromoneUnit() + 10);
            }
        }
    }
    private void traverseTo(GridNode gridNode) { //SWAPS NODES TO MOVE FORAGER. PUSHES NODE INTO MOVE HISTORY STACK
        gridLocation.deleteAntObject(this); //SWAPPING NODES
        gridLocation = gridNode;
        gridLocation.addNewAntObject(this);

        if (gridLocation.containsQueen() && returning) {
            gridLocation.setFoodUnit(gridLocation.getFoodUnit() + 1);
            returning = false;
            foragerMoveHistory.clear();
        }
        foragerMoveHistory.push(gridLocation);
    }

    private GridNode locateHighestPherms() {
        Random randomNode = new Random();
        LinkedList<GridNode> neighborNodeList = gridLocation.getNeighboringNodes(); //a List of Node Objects that keeps track of adjacent nodes
        LinkedList<GridNode> randomGridNode = new LinkedList<>(); //random destination Node

        Iterator<GridNode> gridNodeIterator = neighborNodeList.iterator(); // Iterate through nodes

        while(gridNodeIterator.hasNext()) {

            GridNode alreadyVisited = gridNodeIterator.next();
            if (foragerMoveHistory.contains(alreadyVisited) || !alreadyVisited.isVisible()) {
                gridNodeIterator.remove();
            }
        }
        if (neighborNodeList.size() == 0) {
            neighborNodeList = gridLocation.getNeighboringNodes();
        }
        GridNode nodeWithMostPherms = neighborNodeList.get(0);

        for (int checkNode = 1; checkNode < neighborNodeList.size(); checkNode++) {
            if (nodeWithMostPherms.isVisible() && nodeWithMostPherms.getPheromoneUnit() < neighborNodeList.get(checkNode).getPheromoneUnit()) {
                nodeWithMostPherms = neighborNodeList.get(checkNode);
            }
        }
        for (GridNode neighborNode : neighborNodeList) {
            if ((neighborNode.getPheromoneUnit() == nodeWithMostPherms.getPheromoneUnit()) && neighborNode.isVisible()) {
                randomGridNode.add(neighborNode);
            }
        }
        //DEBUGGING
        //System.out.println("X: " + nodeWithMostPherms.getXCoord() + ", " + "Y: " + nodeWithMostPherms.getYCoord() + " - Number of Ants: " + randomGridNode.size());

        try {
            if (randomGridNode.size() == 0) {
                nodeWithMostPherms = randomGridNode.get(RandomInstance.randomNumberGen(1, randomGridNode.size()) + 1);
                //DEBUGGING
                //System.out.println(nodeWithMostPherms + " " + randomGridNode.size());
            } else {
                nodeWithMostPherms = randomGridNode.get(randomNode.nextInt(randomGridNode.size()));
            }
        }catch(IllegalArgumentException e) {
            System.out.println(nodeWithMostPherms);
            e.printStackTrace();
        }
        return nodeWithMostPherms;
    }
}
