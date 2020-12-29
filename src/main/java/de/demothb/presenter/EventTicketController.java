package de.demothb.presenter;

import de.demothb.model.Event;
import de.demothb.model.EventTicketModelInterface;
import de.demothb.util.PropertyChangeConstants;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyChangeEvent;

public class EventTicketController implements EventTicketControllerInterface {

    private EventTicketModelInterface eventTicketModel;

    public EventTicketController(EventTicketModelInterface eventTicketModel) {
        this.eventTicketModel = eventTicketModel;
    }

    @Override
    public void purchaseTicket(String eventId, int quantity) {
        if (StringUtils.isNotEmpty(eventId) && quantity > 0) {
            eventTicketModel.purchaseTicket(eventId, quantity);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propName = evt.getPropertyName();
        if (propName.equals(PropertyChangeConstants.PURCHASE_TICKET.geName())) {
            Event event = (Event) evt.getSource();
            purchaseTicket(event.getId(), ((Integer) evt.getNewValue()).intValue());
        }
    }

}
