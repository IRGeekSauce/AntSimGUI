/**
 * Created by christopher on 4/15/16.
 */

import java.util.LinkedList;
import java.util.Random;

/**
 * DETERMINES ATTRIBUTES FOR AN INDIVIDUAL COLONY NODE
 */
public class GridNode {

    int foodUnit;
    int pheromoneUnit;
    int xCoord;
    int yCoord;
    int counter;
    boolean isQueen;
    boolean isColonyEntrance;
    boolean isMoving;
    boolean isNodeVisible;
    LinkedList<AntObjectTemplate> masterAntList;
    LinkedList<AntObjectTemplate> garbageAntList; //buffer between lists
    LinkedList<AntObjectTemplate> addAntList;
    ColonyNodeView colonyNodeView;
    AntColony antColony;

    /**
     * CONSTRUCTOR FOR NEW GRID NODE
     */
    GridNode(ColonyNodeView colonyNodeView, int xCoord, int yCoord) {
        Random randomFoodGen = new Random();
        if(randomFoodGen.nextInt(4) ==0) {
            foodUnit = RandomInstance.randomNumberGen(500,1000);
        }
        else {
            foodUnit = 0;
        }
        this.colonyNodeView = colonyNodeView;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        pheromoneUnit = 0;
        isNodeVisible = false;
        counter = 0;
        isQueen = false;
        isColonyEntrance = false;
        masterAntList = new LinkedList<>();
        garbageAntList = new LinkedList<>();
        addAntList = new LinkedList<>();

    }

    /**************************************
     *ACCESSORS AND MUTATORS FOR VARIABLES *
     **************************************
     */
    public void setFoodUnit(int food) {

        foodUnit = food;
    }
    public int getFoodUnit() {

        return foodUnit;
    }
    public void setPheromoneUnit(int pheromoneUnit) {

        this.pheromoneUnit = pheromoneUnit;
    }
    public int getPheromoneUnit() {

        return pheromoneUnit;
    }
    public void setXCoord(int xCoord) {

        this.xCoord = xCoord;
    }
    public int getXCoord() {

        return xCoord;
    }
    public void setYCoord(int yCoord) {

        this.yCoord = yCoord;
    }
    public int getYCoord() {

        return yCoord;
    }
    public int countSpecificTypes(AntObjectTemplate antObject) {
        int antCounter = 0;
        for(int ant = 0; ant < masterAntList.size(); ant++) {
            if(masterAntList.get(ant).getClass() == antObject.getClass()) {
                antCounter++;
            }
        }
        return antCounter;
    }
    public LinkedList<AntObjectTemplate> getSpecificTypes(AntObjectTemplate antObject) {
        LinkedList<AntObjectTemplate> type = new LinkedList<>();
        for(int ant = 0; ant < masterAntList.size(); ant++) {
            if(masterAntList.get(ant).getClass() == antObject.getClass()) {
                type.add(masterAntList.get(ant));
            }
        }
        return type;
    }
    public void setAntColony(AntColony antColony) {

        this.antColony = antColony;
    }
    public boolean containsQueen() {

        return isQueen;
    }
    public boolean isVisible() {

        return isNodeVisible;
    }
    public void setNodeVisible(boolean visible) {
        isNodeVisible = visible;
        if(visible) {
            colonyNodeView.showNode();
        }
        if(!visible) {
            colonyNodeView.hideNode();
        }
    }
    public LinkedList<GridNode> getNeighboringNodes() {

        return antColony.getNeighboringNodes(this);
    }
    public LinkedList<AntObjectTemplate> getFriendlyAnts() {
        LinkedList<AntObjectTemplate> friendlyAnts = new LinkedList<>();
        for(int ant = 0; ant < masterAntList.size(); ant++) {
            if(masterAntList.get(ant).getClass() != new BalaObject().getClass()) {
                friendlyAnts.add(masterAntList.get(ant));
            }
        }
        return friendlyAnts;
    }
    //ADDS A NEW ANT OBJECT TO RESPECTIVE LISTS
    public void addNewAntObject(AntObjectTemplate newAntObject) {
        if(isMoving) {
            addAntList.add(newAntObject);
        }
        else {
            masterAntList.add(newAntObject);
        }
        reloadColonyView();
    }
    //REMOVES ANT OBJECT FROM RESPECTIVE LISTS
    public void deleteAntObject(AntObjectTemplate antObject) {
        if(isMoving) {
            garbageAntList.add(antObject);
        }
        else {
            masterAntList.remove(antObject);
        }
        reloadColonyView();
    }
    //DETERMINES WHAT HAPPENS UPON EACH UPDATE OF COLONY VIEW ACCORDING TO ANT TYPE. ANT ICONS WILL SHOW IF COUNT IS GREATER THAN ZERO
    public void reloadColonyView() {
        int queenAntCounter = countSpecificTypes(new QueenObject());
        if(queenAntCounter == 1) {
            isQueen = true;
            colonyNodeView.setQueen(isQueen);
            colonyNodeView.showQueenIcon();
        }
        colonyNodeView.setBalaCount(countSpecificTypes(new BalaObject()));
        if(countSpecificTypes(new BalaObject()) > 0) {
            colonyNodeView.showBalaIcon();
        }
        else {
            colonyNodeView.hideBalaIcon();
        }
        colonyNodeView.setForagerCount(countSpecificTypes(new ForagerObject()));
        if(countSpecificTypes(new ForagerObject()) > 0) {
            colonyNodeView.showForagerIcon();
        }
        else {
            colonyNodeView.hideForagerIcon();
        }
        colonyNodeView.setScoutCount(countSpecificTypes(new ScoutObject()));
        if(countSpecificTypes(new ScoutObject()) > 0) {
            colonyNodeView.showScoutIcon();
        }
        else {
            colonyNodeView.hideScoutIcon();
        }
        colonyNodeView.setSoldierCount(countSpecificTypes(new SoldierObject()));
        if(countSpecificTypes(new SoldierObject()) > 0) {
            colonyNodeView.showSoldierIcon();
        }
        else {
            colonyNodeView.hideSoldierIcon();
        }
        colonyNodeView.setFoodAmount(foodUnit);
        colonyNodeView.setPheromoneLevel(pheromoneUnit);
    }
    public void reloadAntList() {
        for(AntObjectTemplate ant : garbageAntList) {
            masterAntList.remove(ant);
        }
        garbageAntList.clear();
        for(AntObjectTemplate ant : addAntList) {
            masterAntList.add(ant);
        }
        addAntList.clear();
    }
    public void newTurn(int thisTurn) { //DEFINES NEW TURN FOR SINGLE GRID NODE
        this.setXCoord(xCoord);
        this.setYCoord(yCoord);
        if((thisTurn != 0) && (thisTurn % 10 == 0)) {
            this.setPheromoneUnit(getPheromoneUnit() / 2);
        }
        isMoving = true;
        for(AntObjectTemplate antObject : masterAntList) {
            antObject.newTurn(thisTurn);
        }
        isMoving = false;
        reloadAntList();
        reloadColonyView();

    }


}
