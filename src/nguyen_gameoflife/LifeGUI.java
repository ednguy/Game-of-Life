/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nguyen_gameoflife;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import static java.lang.Integer.parseInt;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.Border;

/**
 *
 * @author nguye8697
 */
public class LifeGUI implements ActionListener {

    JFrame frame;
    JPanel masterPanel, gridPanel, commandPanel, counterPanel, menuPanel, speedPanel;
    JLabel prompt, result, stepCounter;
    JLabel title;
    JLabel empty = new JLabel();
    Life board = new Life();
    JButton[][] button = new JButton[board.grid.length][board.grid[0].length];
    JButton start, stop, clear, next;
    JComboBox premades, menu;
    JButton[] speed = new JButton[100];
    Timer timer;
    BufferedImage game;
    String[] patterns = {"Target", "Glider", "Line", "Buzzer", "Trapdoor"};
    String[] menuOptions = {"Save", "Load"};
    int[][] savedGrid = new int[board.grid.length][board.grid[0].length];
    int counter = 0;
    Border border = null;

    public LifeGUI() {
        frame = new JFrame("GameOfLife");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panels
        masterPanel = new JPanel(new BorderLayout());
        masterPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(0, 5, 5, 5));

        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(board.grid.length, board.grid[0].length, 0, 0));

        commandPanel = new JPanel();
        commandPanel.setLayout(new GridLayout(0, 7, 5, 5));

        speedPanel = new JPanel();
        speedPanel.setLayout(new GridLayout(0, 100, 0, 0));

        // Menu
        menu = new JComboBox(menuOptions);
        menu.addActionListener(new MenuListener());
        menuPanel.add(menu);

        // Title
        menuPanel.add(empty);
        title = new JLabel("Game of Life");
        title.setFont(new Font("", Font.BOLD, 30));
        menuPanel.add(title, BorderLayout.CENTER);

        // Timer
        timer = new Timer(500, new StartListener());

        // Grid
        for (int i = 0; i < board.grid.length; i++) {
            for (int j = 0; j < board.grid[0].length; j++) {
                savedGrid[i][j] = 0;
                button[i][j] = new JButton();
                button[i][j].setBackground(Color.yellow);
                button[i][j].setActionCommand("0");
                button[i][j].addActionListener(new ClickGridListener());
                gridPanel.add(button[i][j]);
            }
        }

        // Start
        start = new JButton("Start");
        start.setActionCommand("Start");
        start.setBorder(border);
        start.addActionListener(new TimerListener());
        commandPanel.add(start);

        // Next
        next = new JButton("Next");
        next.setActionCommand("Next");
        next.setBorder(border);
        next.addActionListener(new NextListener());
        commandPanel.add(next);

        // Stop
        stop = new JButton("Stop");
        stop.setActionCommand("Stop");
        stop.setBorder(border);
        stop.addActionListener(new TimerListener());
        commandPanel.add(stop);

        // Clear
        clear = new JButton("Clear");
        clear.setActionCommand("Clear");
        clear.setBorder(border);
        clear.addActionListener(new ClearListener());
        commandPanel.add(clear);

        // Speed
        for (int i = speed.length - 1; i > 0; i--) {
            speed[i] = new JButton();
            speed[i].setActionCommand(String.valueOf(i));
            speed[i].setBackground(Color.ORANGE);
            speed[i].setBorder(border);
            speed[i].addActionListener(new SpeedListener());
            speedPanel.add(speed[i]);
        }
        commandPanel.add(speedPanel);

        // Premades
        premades = new JComboBox(patterns);
        premades.setBorder(border);
        premades.addActionListener(new PremadeListener());
        commandPanel.add(premades);

        // Step Counter
        stepCounter = new JLabel(String.valueOf(counter), JLabel.RIGHT);
        commandPanel.add(stepCounter);

        // MasterPanel
        masterPanel.add(menuPanel, BorderLayout.NORTH);
        masterPanel.add(gridPanel, BorderLayout.CENTER);
        masterPanel.add(commandPanel, BorderLayout.SOUTH);

        // Frame
        frame.setContentPane(masterPanel);
        frame.setSize(1000, 1000);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

    }

    class ClearListener implements ActionListener {

        /**
         * Pre: none. Post: Sets all values in grid to 0.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            String eventName = event.getActionCommand();
            if ("Clear".equals(eventName)) {
                board.killAllCells();
                for (int i = 0; i < board.grid.length; i++) {
                    for (int j = 0; j < board.grid[0].length; j++) {
                        button[i][j].setBackground(Color.yellow);
                        button[i][j].setActionCommand("0");
                    }
                }
                counter = 0;
                stepCounter.setText(String.valueOf(counter));
            }
        }
    }

    class ClickGridListener implements ActionListener {

        /**
         * Pre: none. Post: Sets value of button to 0 if previously 1, 1 if
         * previously 0.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            String eventName = event.getActionCommand();
            JButton myButton = (JButton) event.getSource();
            if ("0".equals(eventName)) {
                myButton.setActionCommand("1");
                myButton.setBackground(Color.cyan);
            } else if ("1".equals(eventName)) {
                myButton.setActionCommand("0");
                myButton.setBackground(Color.yellow);
            }

            for (int i = 0; i < button.length; i++) {
                for (int j = 0; j < button[0].length; j++) {
                    if ("0".equals(button[i][j].getActionCommand())) {
                        board.grid[i][j] = 0;
                    } else if ("1".equals(button[i][j].getActionCommand())) {
                        board.grid[i][j] = 1;
                    }
                }
            }
        }
    }

    class MenuListener implements ActionListener {

        /**
         * Pre: none. Post: Saves current grid and allows the restoration of the
         * saved grid.
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            if ("Save".equals(menu.getSelectedItem())) {
                for (int i = 0; i < button.length; i++) {
                    for (int j = 0; j < button[0].length; j++) {
                        if ("0".equals(button[i][j].getActionCommand())) {
                            savedGrid[i][j] = 0;
                        } else if ("1".equals(button[i][j].getActionCommand())) {
                            savedGrid[i][j] = 1;
                        }
                    }
                }
            } else if ("Load".equals(menu.getSelectedItem())) {
                for (int i = 0; i < savedGrid.length; i++) {
                    for (int j = 0; j < savedGrid[0].length; j++) {
                        if (savedGrid[i][j] == 1) {
                            button[i][j].setActionCommand("1");
                            button[i][j].setBackground(Color.cyan);
                            board.grid[i][j] = 1;
                        } else if (savedGrid[i][j] == 0) {
                            button[i][j].setActionCommand("0");
                            button[i][j].setBackground(Color.yellow);
                            board.grid[i][j] = 0;
                        }
                    }
                }
            }
        }
    }

    class NextListener implements ActionListener {

        /**
         * Pre: none. Post: Applies rules to grid.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            String eventName = event.getActionCommand();

            int[][] tempGrid = board.grid;
            if ("Next".equals(eventName)) {
                for (int i = 0; i < button.length; i++) {
                    for (int j = 0; j < button[0].length; j++) {
                        if ("0".equals(button[i][j].getActionCommand())) {
                            board.grid[i][j] = 0;
                        } else if ("1".equals(button[i][j].getActionCommand())) {
                            board.grid[i][j] = 1;
                        }
                    }
                }
                for (int i = 0; i < button.length; i++) {
                    for (int j = 0; j < button[0].length; j++) {
                        if (board.applyRules(i, j) == 1) {
                            button[i][j].setActionCommand("1");
                            button[i][j].setBackground(Color.cyan);
                        } else if (board.applyRules(i, j) == 0) {
                            button[i][j].setActionCommand("0");
                            button[i][j].setBackground(Color.yellow);
                        }
                    }
                }
                counter++;
                stepCounter.setText(String.valueOf(counter));
                for (int i = 0; i < button.length; i++) {
                    for (int j = 0; j < button[0].length; j++) {
                        if ("0".equals(button[i][j].getActionCommand())) {
                            board.grid[i][j] = 0;
                        } else if ("1".equals(button[i][j].getActionCommand())) {
                            board.grid[i][j] = 1;
                        }
                    }
                }
            }
        }
    }

    class PremadeListener implements ActionListener {

        /**
         * Pre: none. Post: Sets grid to one of available pre-made patterns.
         */
        @Override
        public void actionPerformed(ActionEvent event) {

            for (int i = 0; i < button.length; i++) {
                for (int j = 0; j < button[0].length; j++) {
                    button[i][j].setActionCommand("0");
                    button[i][j].setBackground(Color.yellow);
                }
            }
            board.killAllCells();

            if (premades.getSelectedItem() == "Target") {
                button[button.length / 2 + 2][button[0].length / 2 + 1].setBackground(Color.cyan);
                button[button.length / 2 + 2][button[0].length / 2 + 1].setActionCommand("1");
                button[button.length / 2 - 2][button[0].length / 2 + 1].setBackground(Color.cyan);
                button[button.length / 2 - 2][button[0].length / 2 + 1].setActionCommand("1");
                button[button.length / 2 - 1][button[0].length / 2 + 2].setBackground(Color.cyan);
                button[button.length / 2 - 1][button[0].length / 2 + 2].setActionCommand("1");
                button[button.length / 2 - 1][button[0].length / 2 - 2].setBackground(Color.cyan);
                button[button.length / 2 - 1][button[0].length / 2 - 2].setActionCommand("1");
                button[button.length / 2 - 2][button[0].length / 2 - 1].setBackground(Color.cyan);
                button[button.length / 2 - 2][button[0].length / 2 - 1].setActionCommand("1");
                button[button.length / 2 + 2][button[0].length / 2 - 1].setBackground(Color.cyan);
                button[button.length / 2 + 2][button[0].length / 2 - 1].setActionCommand("1");
                button[button.length / 2 + 1][button[0].length / 2 - 2].setBackground(Color.cyan);
                button[button.length / 2 + 1][button[0].length / 2 - 2].setActionCommand("1");
                button[button.length / 2 + 1][button[0].length / 2 + 2].setBackground(Color.cyan);
                button[button.length / 2 + 1][button[0].length / 2 + 2].setActionCommand("1");
                for (int i = -3; i <= 3; i++) {
                    button[button.length / 2 + i][button[0].length / 2].setBackground(Color.cyan);
                    button[button.length / 2 + i][button[0].length / 2].setActionCommand("1");
                    button[button.length / 2][button[0].length / 2 + i].setBackground(Color.cyan);
                    button[button.length / 2][button[0].length / 2 + i].setActionCommand("1");
                }
            } else if (premades.getSelectedItem() == "Glider") {
                button[button.length / 2][button[0].length / 2].setBackground(Color.cyan);
                button[button.length / 2][button[0].length / 2].setActionCommand("1");
                button[button.length / 2 + 1][button[0].length / 2 + 1].setBackground(Color.cyan);
                button[button.length / 2 + 1][button[0].length / 2 + 1].setActionCommand("1");
                button[button.length / 2 + 2][button[0].length / 2 - 1].setBackground(Color.cyan);
                button[button.length / 2 + 2][button[0].length / 2 - 1].setActionCommand("1");
                button[button.length / 2 + 2][button[0].length / 2].setBackground(Color.cyan);
                button[button.length / 2 + 2][button[0].length / 2].setActionCommand("1");
                button[button.length / 2 + 2][button[0].length / 2 + 1].setBackground(Color.cyan);
                button[button.length / 2 + 2][button[0].length / 2 + 1].setActionCommand("1");
            } else if (premades.getSelectedItem() == "Line") {
                for (int i = 0; i < 10; i++) {
                    button[button.length / 2 - 5 + i][button[0].length / 2].setBackground(Color.cyan);
                    button[button.length / 2 - 5 + i][button[0].length / 2].setActionCommand("1");
                }
            } else if (premades.getSelectedItem() == "Buzzer") {
                for (int i = 0; i < 5; i++) {
                    button[button.length / 2 - 3 + i][button[0].length / 2 - 2].setBackground(Color.cyan);
                    button[button.length / 2 - 3 + i][button[0].length / 2 - 2].setActionCommand("1");
                    button[button.length / 2 - 3 + i][button[0].length / 2 + 2].setBackground(Color.cyan);
                    button[button.length / 2 - 3 + i][button[0].length / 2 + 2].setActionCommand("1");
                }
                button[button.length / 2 - 3][button[0].length / 2].setBackground(Color.cyan);
                button[button.length / 2 - 3][button[0].length / 2].setActionCommand("1");
                button[button.length / 2 + 1][button[0].length / 2].setBackground(Color.cyan);
                button[button.length / 2 + 1][button[0].length / 2].setActionCommand("1");
            } else if (premades.getSelectedItem() == "Trapdoor") {
                for (int i = 0; i < 3; i++) {
                    button[button.length / 2 - 1][button[0].length / 2 + i].setBackground(Color.cyan);
                    button[button.length / 2 - 1][button[0].length / 2 + i].setActionCommand("1");
                    button[button.length / 2][button[0].length / 2 + i - 1].setBackground(Color.cyan);
                    button[button.length / 2][button[0].length / 2 + i - 1].setActionCommand("1");
                }
            }
            for (int i = 0; i < button.length; i++) {
                for (int j = 0; j < button[0].length; j++) {
                    if ("0".equals(button[i][j].getActionCommand())) {
                        board.grid[i][j] = 0;
                    } else if ("1".equals(button[i][j].getActionCommand())) {
                        board.grid[i][j] = 1;
                    }
                }
            }
            counter = 0;
        }
    }

    class SpeedListener implements ActionListener {

        /**
         * Pre: none. Post: Sets speed to value of button.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            String eventName = event.getActionCommand();
            int s = parseInt(eventName);
            timer.setDelay(s * 10);
        }
    }

    class StartListener implements ActionListener {

        /**
         * Pre: none. Post: Starts timer; repeatedly applies rules until
         * stopped.
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            timer.start();
            for (int i = 0; i < button.length; i++) {
                for (int j = 0; j < button[0].length; j++) {
                    if ("0".equals(button[i][j].getActionCommand())) {
                        board.grid[i][j] = 0;
                    } else if ("1".equals(button[i][j].getActionCommand())) {
                        board.grid[i][j] = 1;
                    }
                }
            }
            for (int i = 0; i < button.length; i++) {
                for (int j = 0; j < button[0].length; j++) {
                    if (board.applyRules(i, j) == 1) {
                        button[i][j].setActionCommand("1");
                        button[i][j].setBackground(Color.cyan);
                    } else if (board.applyRules(i, j) == 0) {
                        button[i][j].setActionCommand("0");
                        button[i][j].setBackground(Color.yellow);
                    }
                }
            }
            counter++;
            stepCounter.setText(String.valueOf(counter));
            for (int i = 0; i < button.length; i++) {
                for (int j = 0; j < button[0].length; j++) {
                    if ("0".equals(button[i][j].getActionCommand())) {
                        board.grid[i][j] = 0;
                    } else if ("1".equals(button[i][j].getActionCommand())) {
                        board.grid[i][j] = 1;
                    }
                }
            }
        }
    }

    class TimerListener implements ActionListener {

        /**
         * Pre: none. Post: Starts or stops the timer.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            String eventName = event.getActionCommand();
            if ("Start".equals(eventName)) {
                timer.start();
            } else if ("Stop".equals(eventName)) {
                timer.stop();
            }
        }
    }

    /**
     * Pre: none. Post: Sets up GUI.
     */
    private static void runGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        LifeGUI gui = new LifeGUI();
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                runGUI();
            }
        });
    }
}
