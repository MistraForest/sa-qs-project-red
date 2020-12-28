package de.demothb.model;

import de.demothb.service.EventServiceInterface;
import de.demothb.util.PropertyChangeConstants;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class EventTicketModel implements EventTicketModelInterface {

    private final PropertyChangeSupport support;
    private final EventServiceInterface eventService;

    public EventTicketModel(EventServiceInterface eventService) {
        this.eventService = eventService;
        support = new PropertyChangeSupport(this);
    }

    public boolean purchaseTicket(String eventId, int quantity) {
        try {
            Event event = eventService.getById(eventId);
            if (event != null && isTicketPurchasable(event, quantity)) {
                int oldAvailability = event.getAvailability();
                event.purchaseTicket(quantity);
                eventService.saveEvent(event);
                availabilityChange(event, oldAvailability);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void loadEvents() {
        support.firePropertyChange(
                new PropertyChangeEvent(
                        this,
                        PropertyChangeConstants.EVENT_LIST.geName(),
                        null,
                        eventService.getList()
                )
        );
    }

    private Boolean isTicketPurchasable(Event event, int quantity) {
        return event.getAvailability() >= quantity;
    }

    private void availabilityChange(Event event, int oldAvailability) {
        support.firePropertyChange(
                new PropertyChangeEvent(
                        event,
                        PropertyChangeConstants.EVENT_AVAILABILITY.geName(),
                        oldAvailability,
                        event.getAvailability()
                )
        );
    }

    @Override
    public void subscribe(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    @Override
    public void unsubscribe(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

}
