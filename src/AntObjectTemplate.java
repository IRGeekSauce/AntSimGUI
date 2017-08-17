/**
 * Created by christopher on 4/15/16.
 */

/**
 * DEFAULT ATTRIBUTES FOR A NEW ANT.
 * CHARACTERISTICS ARE THE SAME FOR EACH TYPE, EXCEPT QUEEN
 */
public class AntObjectTemplate {

    int uniqueID;
    int previousTurn;
    int hatchNum;
    GridNode gridLocation;

    public AntObjectTemplate(GridNode gridNode) { // CONSTRUCTOR
        uniqueID = 0;
        gridLocation = gridNode;
        previousTurn = 0;
    }

    public AntObjectTemplate() {

    }
    //OVERRIDE newTurn METHOD
    public void newTurn(int thisTurn) {

    }
    public void antDies() { // WHAT HAPPENS WHEN AN ANT DIES

        gridLocation.deleteAntObject(this);
    }
    public void setHatchNum(int thisTurn) {

        hatchNum = thisTurn;
    }
    public void setUniqueID(int uniqueID) {

        this.uniqueID = uniqueID;
    }


}
