/**
 * Created by christopher on 4/17/16.
 */
public class Driver {

    public static void main(String[] args)  {

        AntSimGUI antSimGUI = new AntSimGUI();
        antSimGUI.addSimulationEventListener(new SimManager(antSimGUI));
    }
}
