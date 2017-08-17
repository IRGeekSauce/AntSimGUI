import java.util.LinkedList;

/**
 * Created by christopher on 4/15/16.
 */
public class AntColony {

    GridNode[][] grid;
    ColonyView colonyView;
    ColonyNodeView colonyNodeView;
    GridNode gridNode;
    SimManager simulation;
    String xLabel = "X: ";
    String yLabel = "Y: ";

    AntColony(ColonyView colonyView, SimManager simulation) { //CONSTRUCTOR FOR ANT COLONY
        this.simulation = simulation;
        grid = new GridNode[27][27];
        this.colonyView = colonyView;
    }

    public ColonyView getColonyView() {

        return colonyView;
    }

    public LinkedList<GridNode> getNeighboringNodes(GridNode gridNode) {
        int xCoord = gridNode.getXCoord();
        int yCoord = gridNode.getYCoord();
        LinkedList<GridNode> neighboringNodeList = new LinkedList<>();

        for (int neighborX = -1; neighborX <= 1; neighborX++) {
            for (int neighborY = -1; neighborY <= 1; neighborY++) {
                if (neighborX != 0 || neighborY != 0) {
                    try {
                        neighboringNodeList.add(grid[xCoord + neighborX][yCoord + neighborY]);
                    } catch (ArrayIndexOutOfBoundsException ex) {
                    } //HAVE TO USE THIS TO AVOID OUT OF BOUNDS EXCEPTION
                }
            }
        }
        return neighboringNodeList;
    }

    public void addNewGridNode(GridNode gridNodeItem, int xLoc, int yLoc) {

        grid[xLoc][yLoc] = gridNodeItem;
    }

    /**
     * SERIES OF METHODS TO DETERMINE NUMBER OF RESPECTIVE ANTS AND INITIAL QUEEN LIFESPAN/FOOD LEVEL
     */

    public void initializeAntColony() { /** NORMAL INITIALIZATION */

        for (int x = 0; x < 27; x++) {
            for (int y = 0; y < 27; y++) {

                colonyNodeView = new ColonyNodeView();
                gridNode = new GridNode(colonyNodeView, x, y);
                gridNode.setAntColony(this);
                colonyView.addColonyNodeView(colonyNodeView, x, y);
                addNewGridNode(gridNode, x, y);
                colonyNodeView.setID(xLabel + x + "," + yLabel + y); //EVERY NODE WILL HAVE THE ID AS: "X: 13,Y: 13" NO EXTRA LABEL CREATED. JUST IMPLEMENTED HERE

                if (x == 13 && y == 13) {
                    QueenObject queen = new QueenObject(gridNode);
                    gridNode.setFoodUnit(1000);
                    gridNode.addNewAntObject(queen);

                    for (int forager = 0; forager < 50; forager++) {
                        queen.createNewAntObject(new ForagerObject(gridNode));
                    }
                    for (int scout = 0; scout < 4; scout++) {
                        queen.createNewAntObject(new ScoutObject(gridNode));
                    }
                    for (int soldier = 0; soldier < 20; soldier++) {
                        queen.createNewAntObject(new SoldierObject(gridNode));
                    }
                }
                if ((x >= 12 && x <= 14) && (y >= 12 && y <= 14)) { //ALL NODES EXCEPT THE 9 CENTER SQUARES ARE VISIBLE UPON INITIALIZATION
                    gridNode.setNodeVisible(true);
                }
            }
        }
    }
    public void initializeQueenAgeTest() { /** TEST QUEEN LIFESPAN ONLY */


        for(int x = 0; x < 27; x++) {

            for(int y = 0; y < 27; y++) {

                colonyNodeView = new ColonyNodeView();
                gridNode = new GridNode(colonyNodeView, x, y);
                gridNode.setAntColony(this);
                colonyView.addColonyNodeView(colonyNodeView, x, y);
                addNewGridNode(gridNode, x, y);
                colonyNodeView.setID(xLabel + x + "," + yLabel + y);

                if(x == 13 && y == 13) {
                    QueenObject queen = new QueenObject(gridNode);
                    gridNode.setFoodUnit(100);
                    gridNode.addNewAntObject(queen);
                }
                if ((x >= 12 && x <= 14) && (y >= 12 && y <= 14)) { //ALL NODES EXCEPT THE 9 CENTER SQUARES ARE VISIBLE UPON INITIALIZATION
                    gridNode.setNodeVisible(true);
                }
            }
        }
    }
    public void initializeQueenFoodTest() { /** TEXT QUEEN FOOD LEVELS ONLY */

        for(int x = 0; x < 27; x++) {

            for(int y = 0; y < 27; y++) {

                colonyNodeView = new ColonyNodeView();
                gridNode = new GridNode(colonyNodeView, x, y);
                gridNode.setAntColony(this);
                colonyView.addColonyNodeView(colonyNodeView, x, y);
                addNewGridNode(gridNode, x, y);
                colonyNodeView.setID(xLabel + x + "," + yLabel + y);

                if(x == 13 && y == 13) {
                    QueenObject queen = new QueenObject(gridNode);
                    gridNode.setFoodUnit(20);
                    gridNode.addNewAntObject(queen);
                }
                if ((x >= 12 && x <= 14) && (y >= 12 && y <= 14)) { //ALL NODES EXCEPT THE 9 CENTER SQUARES ARE VISIBLE UPON INITIALIZATION
                    gridNode.setNodeVisible(true);
                }
            }
        }
    }
    public void initializeForagerTest() { /** TEST FORAGERS ONLY. SCOUTS ARE INCLUDED SO FORAGERS CAN MOVE AROUND */

        for(int x = 0; x < 27; x++) {

            for(int y = 0; y < 27; y++) {

                colonyNodeView = new ColonyNodeView();
                gridNode = new GridNode(colonyNodeView, x, y);
                gridNode.setAntColony(this);
                colonyView.addColonyNodeView(colonyNodeView, x, y);
                addNewGridNode(gridNode, x, y);
                colonyNodeView.setID(xLabel + x + "," + yLabel + y);

                if(x == 13 && y == 13) {
                    QueenObject queen = new QueenObject(gridNode);

                    /*
                    GIVE THE COLONY SOME SCOUTS TO REVEAL NODES FOR THE FORAGERS
                     */
                    for(int scout = 0; scout < 5; scout++) {
                        queen.createNewAntObject(new ScoutObject(gridNode));
                    }
                    for (int forager = 0; forager < 50; forager++) {
                        queen.createNewAntObject(new ForagerObject(gridNode));
                    }
                    gridNode.setFoodUnit(1000);
                    gridNode.addNewAntObject(queen);
                }
                if ((x >= 12 && x <= 14) && (y >= 12 && y <= 14)) { //ALL NODES EXCEPT THE 9 CENTER SQUARES ARE VISIBLE UPON INITIALIZATION
                    gridNode.setNodeVisible(true);
                }
            }
        }
    }
    public void initializeScoutTest() { /** TEST SCOUTS ONLY. */

        for(int x = 0; x < 27; x++) {

            for(int y = 0; y < 27; y++) {

                colonyNodeView = new ColonyNodeView();
                gridNode = new GridNode(colonyNodeView, x, y);
                gridNode.setAntColony(this);
                colonyView.addColonyNodeView(colonyNodeView, x, y);
                addNewGridNode(gridNode, x, y);
                colonyNodeView.setID(xLabel + x + "," + yLabel + y);

                if(x == 13 && y == 13) {
                    QueenObject queen = new QueenObject(gridNode);
                    for(int scout = 0; scout < 10; scout++) {
                        queen.createNewAntObject(new ScoutObject(gridNode));
                    }
                    gridNode.setFoodUnit(1000);
                    gridNode.addNewAntObject(queen);
                }
                if ((x >= 12 && x <= 14) && (y >= 12 && y <= 14)) { //ALL NODES EXCEPT THE 9 CENTER SQUARES ARE VISIBLE UPON INITIALIZATION
                    gridNode.setNodeVisible(true);
                }
            }
        }
    }
    public void initializeSoldierTest() { /** TEST SOLDIERS ONLY. SCOUTS ARE INCLUDED SO SOLDIERS CAN MOVE AROUND. */

        for(int x = 0; x < 27; x++) {

            for(int y = 0; y < 27; y++) {

                colonyNodeView = new ColonyNodeView();
                gridNode = new GridNode(colonyNodeView, x, y);
                gridNode.setAntColony(this);
                colonyView.addColonyNodeView(colonyNodeView, x, y);
                addNewGridNode(gridNode, x, y);
                colonyNodeView.setID(xLabel + x + "," + yLabel + y);

                if(x == 13 && y == 13) {
                    QueenObject queen = new QueenObject(gridNode);

                    /*
                    GIVE THE COLONY SOME ADDITIONAL SCOUTS SO THE SOLDIERS CAN PATROL. OTHERWISE THEY JUST HANG OUT IN THE CENTER SQUARE, WANDERING AIMLESSLY
                     */
                    for(int scout = 0; scout < 5; scout++) {
                        queen.createNewAntObject(new ScoutObject(gridNode));
                    }
                    for(int soldier = 0; soldier < 20; soldier++) {
                        queen.createNewAntObject(new SoldierObject(gridNode));
                    }
                    gridNode.setFoodUnit(1000);
                    gridNode.addNewAntObject(queen);
                }
                if ((x >= 12 && x <= 14) && (y >= 12 && y <= 14)) { //ALL NODES EXCEPT THE 9 CENTER SQUARES ARE VISIBLE UPON INITIALIZATION
                    gridNode.setNodeVisible(true);
                }
            }
        }
    }
    public void newTurn(int thisTurn) { //DEFINES NEW TURN FOR ENTIRE ANT COLONY

        for (int x = 0; x < 27; x++) {
            for (int y = 0; y < 27; y++) {
                grid[x][y].newTurn(thisTurn);
            }
        }
    }
}
