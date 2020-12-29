package de.demothb.presenter;

import java.beans.PropertyChangeListener;

public interface EventTicketControllerInterface extends PropertyChangeListener {

    void purchaseTicket(String eventId, int quantity);

}
