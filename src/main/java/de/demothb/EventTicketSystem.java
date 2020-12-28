package de.demothb;

import de.demothb.model.EventTicketModel;
import de.demothb.model.EventTicketModelInterface;
import de.demothb.presenter.EventTicketController;
import de.demothb.presenter.EventTicketControllerInterface;
import de.demothb.service.EventService;
import de.demothb.service.EventServiceInterface;
import de.demothb.view.UI.EventTicketGUIWindow;

import javax.swing.*;
import java.awt.*;

public class EventTicketSystem {

    private static EventTicketGUIWindow bootstrap(){
        EventServiceInterface eventService = new EventService();
        EventTicketModelInterface eventModel = new EventTicketModel(eventService);
        EventTicketGUIWindow view = new EventTicketGUIWindow();
        EventTicketControllerInterface presenter = new EventTicketController(eventModel);

        // Wire up dependency --> Observer Synchronization.
        view.subscribe(presenter);
        eventModel.subscribe(view);
        eventModel.loadEvents();
        return view;
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from
     * the event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("EventTicketSystem");
        EventTicketGUIWindow view = bootstrap();
        frame.add(view, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }
}
