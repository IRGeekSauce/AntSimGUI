/**
 * Created by christopher on 4/14/16.
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import javax.sound.sampled.*;
import javax.swing.*;

/**
 * class AntSimGUI
 * <p>
 * main window for ant simulation
 * contains:
 * 1.	a control panel for setting up and running the simulation
 * 2.	a graphical view of the ant colony
 */
public class AntSimGUI extends JFrame {

    /*************
     * attributes
     ************/

    // view for colony
    private ColonyView colonyView;

    // scroll pane for colonyView
    private JScrollPane colonyPane;

    // panel containing buttons for controlling simulation
    private ControlPanel controlPanel;

    // layout for positioning child components
    private SpringLayout layout;

    // user's screen width
    private int screenWidth;

    // user's screen height
    private int screenHeight;

    // list of event listeners for this view
    private LinkedList simulationEventListenerList;


    /***************
     *	constructors
     **************/

    /**
     * create a new AntSimGUI
     */
    public AntSimGUI() {
        // call superclass constructor and set title of window
        super("Ant Simulation GUI");

        //set custom Icon for JFrame
        //setIconImage(new ImageIcon("src/Images/ant-icon.png").getImage());
          setIconImage(new ImageIcon("ant-icon.png").getImage());

        // create anonymous listener to allow user to close window and end sim
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });


        // get user's screen width and height
        screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        // set layout
        getContentPane().setLayout(new BorderLayout());

        // set the size of the window
        resizeGUI();

        // create event listener list
        simulationEventListenerList = new LinkedList();

        // show window
        setVisible(true);

        // validate all components
        validate();
    }


    /**********
     *	methods
     *********/

    /**
     * initialize this GUI
     * <p>
     * a control panel and scrollable pane for displaying the specified
     * ColonyView will be created and added to this GUI
     *
     * @param    colonyView        the ColonyView to be displayed
     */
    public void initGUI(ColonyView colonyView) {
        // create button control panel
        controlPanel = new ControlPanel();

        // set up colony view with default dimensions
        colonyPane = new JScrollPane(colonyView);
        colonyPane.setPreferredSize(new Dimension(800, 600));
        colonyPane.getViewport().setViewPosition(new java.awt.Point(475, 700)); //THIS WILL SET THE SCROLL PANE TO THE COORDINATES OF THE COLONY ENTRANCE SO USER DOESN'T HAVE TO SCROLL TO FIND IT.

        // add control panel and colony view
        getContentPane().add(controlPanel, BorderLayout.NORTH);
        getContentPane().add(colonyPane, BorderLayout.CENTER);

        // validate all components
        validate();
    }


    /**
     * set window size based on user's screen settings
     * <p>
     * initial size will be smaller than the dimensions of the user's screen
     * once sized, the window is maximized to fill the screen
     */
    private void resizeGUI() {
        // set window size
        if (screenWidth >= 1280)
            setSize(1024, 768);
        else if (screenWidth >= 1024)
            setSize(800, 600);
        else if (screenWidth >= 800)
            setSize(640, 480);

        // maximize window
        setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH);
    }


    /**
     * set the current simulation time
     *
     * @param    time        String indicating simulation time in terms of days and turns
     */
    public void setTime(String time) {
        String title = "    Passage of Time in Ant Colony: ";
        controlPanel.setTitleLabel(title);
        controlPanel.setTime(time);
    }


    /**
     * add an event listener to this view
     *
     * @param    listener        listener interested in this view's events
     */
    public void addSimulationEventListener(SimulationEventListener listener) {
        simulationEventListenerList.add(listener);
    }


    /**
     * remove an event listener from this view
     *
     * @param    listener        listener to be removed
     */
    public void removeSimulationEventListener(SimulationEventListener listener) {
        simulationEventListenerList.remove(listener);
    }


    /**
     * fire a simulation event
     *
     * @param    eventType        the type of event that occurred (see the
     * SimulationEvent class for allowable types)
     */
    public void fireSimulationEvent(int eventType) {
        // create event
        SimulationEvent simEvent = new SimulationEvent(this, eventType);

        // inform all listeners
        for (Iterator itr = simulationEventListenerList.iterator(); itr.hasNext(); ) {
            ((SimulationEventListener) itr.next()).simulationEventOccurred(simEvent);
        }

    }


    /**
     * inner class ControlPanel
     * <p>
     * contains buttons for controlling the simulation, and displays the
     * simulation time
     */
    private class ControlPanel extends JPanel {

        /*************
         * attributes
         ************/

        // button for setting up a normal simulation
        private JButton normalSetupButton;

        // button for setting up to test the queen ant
        private JButton queenTestButton;

        // button for setting up to test the scout ant
        private JButton scoutTestButton;

        // button for setting up to test the forager ant
        private JButton foragerTestButton;

        // button for setting up to test the soldier ant
        private JButton soldierTestButton;

        // button for running the simulation continuously
        private JButton runButton;

        // button for running the simulation one turn at a time
        private JButton stepButton;

        // button to STOP simulation
        private JButton stopButton;

        // label for displaying the time in the simulation
        private JLabel timeLabel;

        // label for displaying what the time means in the simulation
        private JLabel titleLabel;

        // event handler for button press events
        private ButtonHandler buttonHandler;


        /***************
         *	constructors
         **************/

        /**
         * create a new control panel
         */
        public ControlPanel() {
            // call superclass constructor
            super();

            // create handler for button press events
            buttonHandler = new ButtonHandler();

            // initialize child components
            initComponents();

            // position child components
            layoutComponents();
        }


        /**
         * create child components
         */
        private void initComponents() {

            //ImageIcon normalButtonImage = new ImageIcon("src/Images/normalIcon.png");
            ImageIcon normalButtonImage = new ImageIcon("normalIcon.png");
            normalSetupButton = new JButton();
            normalSetupButton.setIcon(normalButtonImage);
            normalSetupButton.setName("Normal Setup");
            normalSetupButton.addActionListener(buttonHandler);
            normalSetupButton.setToolTipText("Set up simulation for normal execution");

            // queen test button
            //ImageIcon queenButtonImage = new ImageIcon("src/Images/yellowIcon.jpg");
            ImageIcon queenButtonImage = new ImageIcon("yellowIcon.jpg");
            queenTestButton = new JButton();
            queenTestButton.setIcon(queenButtonImage);
            queenTestButton.setName("Queen Test");
            queenTestButton.addActionListener(buttonHandler);
            queenTestButton.setToolTipText("Set up to test Queen Lifespan or Food Levels");

            // scout test button
            //ImageIcon scoutButtonImage = new ImageIcon("src/Images/blueIcon.png");
            ImageIcon scoutButtonImage = new ImageIcon("blueIcon.png");
            scoutTestButton = new JButton();
            scoutTestButton.setIcon(scoutButtonImage);
            scoutTestButton.setName("Scout Test");
            scoutTestButton.addActionListener(buttonHandler);
            scoutTestButton.setToolTipText("Set up simulation for testing the Scout ant");

            // forager test button
            //ImageIcon foragerButtonImage = new ImageIcon("src/Images/greenIcon.jpg");
            ImageIcon foragerButtonImage = new ImageIcon("greenIcon.jpg");
            foragerTestButton = new JButton();
            foragerTestButton.setIcon(foragerButtonImage);
            foragerTestButton.setName("Forager Test");
            foragerTestButton.addActionListener(buttonHandler);
            foragerTestButton.setToolTipText("Set up simulation for testing the Forager ant (Scouts are included)");

            // soldier test button
            //ImageIcon soldierButtonImage = new ImageIcon("src/Images/blackIcon.png");
            ImageIcon soldierButtonImage = new ImageIcon("blackIcon.png");
            soldierTestButton = new JButton();
            soldierTestButton.setIcon(soldierButtonImage);
            soldierTestButton.setName("Soldier Test");
            soldierTestButton.addActionListener(buttonHandler);
            soldierTestButton.setToolTipText("Set up simulation for testing the Soldier ant (Scouts are included)");

            // button for running simulation continuously
            //ImageIcon playImage = new ImageIcon("src/Images/play.png");
            ImageIcon playImage = new ImageIcon("play.png");
            runButton = new JButton();
            runButton.setIcon(playImage);
            runButton.setName("Run");
            runButton.addActionListener(buttonHandler);
            runButton.setToolTipText("Run the simulation continuously");

            // button for running simulation one turn at a time
            //ImageIcon stepButtonImage = new ImageIcon("src/Images/stepIcon.png");
            ImageIcon stepButtonImage = new ImageIcon("stepIcon.png");
            stepButton = new JButton();
            stepButton.setIcon(stepButtonImage);
            stepButton.setName("Step");
            stepButton.addActionListener(buttonHandler);
            stepButton.setToolTipText("Step through the simulation one turn at a time");

            // button for stopping/pausing simulation
            //ImageIcon stopImage = new ImageIcon("src/Images/Stop_Circle_Red.png");
            ImageIcon stopImage = new ImageIcon("Stop_Circle_Red.png");
            stopButton = new JButton();
            stopButton.setIcon(stopImage);
            stopButton.setName("Stop");
            stopButton.addActionListener(buttonHandler);
            stopButton.setToolTipText("Stop or Pause the simulation");


            // label for displaying simulation time
            timeLabel = new JLabel();
            timeLabel.setFont(new Font("Arial", Font.BOLD, 14));

            // label for displaying simulation time title
            titleLabel = new JLabel();
            titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        }

        /**
         * position child components and add them to this view
         */
        private void layoutComponents() {
            this.add(normalSetupButton);
            this.add(queenTestButton);
            this.add(scoutTestButton);
            this.add(foragerTestButton);
            this.add(soldierTestButton);
            this.add(runButton);
            this.add(stepButton);
            this.add(stopButton);
            this.add(titleLabel);
            this.add(timeLabel);
        }

        public void setTitleLabel(String title) {

            titleLabel.setText(title);
        }


        /**
         * set the current simulation time
         *
         * @param    time        String indicating simulation time in terms of days and turns
         */
        public void setTime(String time) {
            timeLabel.setText("    " + time);
        }


        /**
         * inner class ButtonHandler
         * <p>
         * responsible for handling button press events from the control panel
         */
        private class ButtonHandler implements ActionListener {

            /**********
             *	methods
             *********/

            /**
             * respond to a button action
             * <p>
             * fires a simulation event appropriate for the button that is pressed
             *
             * DISABLE CERTAIN BUTTONS FOR RESPECTIVE EVENT TO PREVENT RUNTIME EXCEPTIONS
             */
            public void actionPerformed(ActionEvent e) {
                // get the button that was pressed
                JButton b = (JButton) e.getSource();

                // fire appropriate event
                if(b.getName().equals("Normal Setup")) {
                    queenTestButton.setEnabled(false);
                    foragerTestButton.setEnabled(false);
                    scoutTestButton.setEnabled(false);
                    soldierTestButton.setEnabled(false);
                    fireSimulationEvent(SimulationEvent.NORMAL_SETUP_EVENT);
                }
                else if(b.getName().equals("Queen Test")) {
                    fireSimulationEvent(SimulationEvent.QUEEN_TEST_EVENT);
                    normalSetupButton.setEnabled(false);
                    foragerTestButton.setEnabled(false);
                    soldierTestButton.setEnabled(false);
                    scoutTestButton.setEnabled(false);
                    queenTestButton.setEnabled(false);
                    stepButton.setEnabled(false);

                }
                else if (b.getName().equals("Forager Test")) {
                    // set for testing the forager ant
                    fireSimulationEvent(SimulationEvent.FORAGER_TEST_EVENT);
                    queenTestButton.setEnabled(false);
                    soldierTestButton.setEnabled(false);
                    scoutTestButton.setEnabled(false);
                    normalSetupButton.setEnabled(false);
                }
                else if(b.getName().equals("Scout Test")) {
                    // set for testing the scout ant
                    fireSimulationEvent(SimulationEvent.SCOUT_TEST_EVENT);
                    queenTestButton.setEnabled(false);
                    foragerTestButton.setEnabled(false);
                    soldierTestButton.setEnabled(false);
                    normalSetupButton.setEnabled(false);
                }
                else if (b.getName().equals("Soldier Test")) {

                    // set for testing the soldier ant
                    fireSimulationEvent(SimulationEvent.SOLDIER_TEST_EVENT);
                    queenTestButton.setEnabled(false);
                    foragerTestButton.setEnabled(false);
                    scoutTestButton.setEnabled(false);
                    normalSetupButton.setEnabled(false);
                }
                else if (b.getName().equals("Run")) {
                    // run the simulation continuously
                    fireSimulationEvent(SimulationEvent.RUN_EVENT);
                    stepButton.setEnabled(false);
                    queenTestButton.setEnabled(false);
                    foragerTestButton.setEnabled(false);
                    scoutTestButton.setEnabled(false);
                    soldierTestButton.setEnabled(false);
                    normalSetupButton.setEnabled(false);
                    runButton.setEnabled(false);
                }
                else if (b.getName().equals("Step")) {
                    // run the simulation one turn at a time
                    fireSimulationEvent(SimulationEvent.STEP_EVENT);
                    stopButton.setEnabled(false);
                    runButton.setEnabled(false);
                    normalSetupButton.setEnabled(false);
                    queenTestButton.setEnabled(false);
                    foragerTestButton.setEnabled(false);
                    scoutTestButton.setEnabled(false);
                    soldierTestButton.setEnabled(false);
                }
                else if (b.getName().equals("Stop")) {
                    //stop everything
                    fireSimulationEvent(SimulationEvent.STOP_EVENT);
                }
            }
        }
    }
}
