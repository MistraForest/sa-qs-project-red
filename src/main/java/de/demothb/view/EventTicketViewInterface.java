package de.demothb.view;

import de.demothb.model.Event;
import de.demothb.util.Observable;

import java.beans.PropertyChangeListener;
import java.util.List;

public interface EventTicketViewInterface extends PropertyChangeListener, Observable {

    void setEventList(List<Event> events);

    void enableTicketPurchase(boolean enable);

}
