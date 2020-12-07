package de.thb;

import de.thb.view.DashboardUI;
import de.thb.view.TableEventUI;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import java.awt.GridLayout;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;


public class ApplicationMain extends JPanel {

    public ApplicationMain() {
        super(new GridLayout(1, 1));

        //Define the tab component to hold two views
        JTabbedPane tabbedPaneViewChooser = new JTabbedPane();

        tabbedPaneViewChooser.addTab("Dashboard", null, new DashboardUI(),
                "Dashboard");

        tabbedPaneViewChooser.setMnemonicAt(0, KeyEvent.VK_1);

        tabbedPaneViewChooser.addTab("Table records", null, new TableEventUI(),
                "Table records");
        //        tabbedPaneViewChooser.setMnemonicAt(1, KeyEvent.VK_2);
        //
        //        //Add the tabbed pane to this panel.
        add(tabbedPaneViewChooser);

        //The following line enables to use scrolling tabs.
        tabbedPaneViewChooser.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }



    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from
     * the event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("ApplicationMain");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ApplicationMain applicationMain = new ApplicationMain();
        applicationMain.setPreferredSize((new Dimension(800,400)));
        frame.add(applicationMain, BorderLayout.CENTER);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }

}
