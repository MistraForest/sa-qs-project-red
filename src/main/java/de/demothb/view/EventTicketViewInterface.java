package de.demothb.view;

import de.demothb.model.Event;

import java.beans.PropertyChangeListener;
import java.util.List;

public interface EventTicketViewInterface extends PropertyChangeListener {

    void setEventList(List<Event> events);

    void enableTicketPurchase(boolean enable);

    void clearEventSelection();

    void highlightAvailability(String color);

    void clearAvailabilityHighlighting();

}
