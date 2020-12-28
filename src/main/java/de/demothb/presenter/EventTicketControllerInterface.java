package de.demothb.presenter;

import java.beans.PropertyChangeListener;

public interface EventTicketControllerInterface extends PropertyChangeListener {

    void eventSelectionChanged(String eventId);

    void ticketQuantityChanged(int quantity);

    void purchaseTicket(String eventId, int quantity);

    //void highlightAvailability();

}
