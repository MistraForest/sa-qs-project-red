package de.demothb.model;

import de.demothb.util.Observable;

public interface EventTicketModelInterface extends Observable {

    boolean purchaseTicket(String eventId, int quantity);

}
