package de.demothb.presenter;

import de.demothb.model.EventTicketModelInterface;
import de.demothb.util.PropertyChangeConstants;

import java.beans.PropertyChangeEvent;

public class EventTicketController implements EventTicketControllerInterface {

    private  EventTicketModelInterface eventTicketModel;

    private String selectedEventId;

    private int ticketQuantity;

    public EventTicketController(EventTicketModelInterface eventTicketModel) {
        this.eventTicketModel = eventTicketModel;
    }

    @Override
    public void eventSelectionChanged(String eventId) {
        selectedEventId = eventId;
    }

    @Override
    public void ticketQuantityChanged(int quantity) {
        if (quantity > 0) {
            ticketQuantity = quantity;
        }
    }

    @Override
    public void purchaseTicket() {
        if (selectedEventId != null && ticketQuantity > 0){
            eventTicketModel.purchaseTicket(selectedEventId, ticketQuantity);
        }
    }

    /* public void highlightAvailability() {
        view.highlightAvailability(selectedEventId);
    }*/

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propName = evt.getPropertyName();
        if (propName == PropertyChangeConstants.EVENT_SELECTED.geName()){
            eventSelectionChanged((String) evt.getNewValue());
            return;
        }
        if (propName == PropertyChangeConstants.QUANTITY_CHANGED.geName()){
            ticketQuantityChanged((int) evt.getNewValue());
            return;
        }
        if (propName == PropertyChangeConstants.PURCHASE_TICKET.geName()){
            purchaseTicket();
            return;
        }
    }

}
