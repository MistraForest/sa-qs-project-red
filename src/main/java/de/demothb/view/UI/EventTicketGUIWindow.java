package de.demothb.view.UI;

import de.demothb.model.Event;
import de.demothb.util.PropertyChangeConstants;
import de.demothb.view.EventTicketViewInterface;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventTicketGUIWindow implements EventTicketViewInterface {

    private JFormattedTextField eventNameField, dateField, availabilityField, eventIdField;
    private JButton purchaseButton;
    private JList<Event> eventListField;
    private List<Event> events;

    private Event currentEvent;

    @Override
    public void setEventList(List<Event> events) {
        this.events = events;
    }

    @Override
    public void enableTicketPurchase(boolean enable) {
        purchaseButton.setEnabled(enable);
    }

    @Override
    public void clearEventSelection() {
        currentEvent = null;
    }

    @Override
    public void highlightAvailability(String color) {
        availabilityField.setForeground(Color.getColor(color));
    }

    @Override
    public void clearAvailabilityHighlighting() {
        availabilityField.setForeground(null);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propName = evt.getPropertyName();
        if (PropertyChangeConstants.EVENT_AVAILABILITY.geName() == propName) {
            currentEvent = (Event) evt.getSource();
            updateAvailabilityField();
        }
    }

    private void updateAvailabilityField() {
        availabilityField.setValue(currentEvent.getAvailability());
        availabilityField.setForeground(getAvailabilityColor());
    }

    private void updateEventNameField() {
        eventNameField.setValue(currentEvent.getName());
    }

    private void updateEventIdField() {
        eventIdField.setValue(currentEvent.getId());
    }

    private void updateDateField() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        eventIdField.setValue(formatter.format(currentEvent.getDate()));
    }

    private Color getAvailabilityColor() {
        switch (currentEvent.getAvailabilityThreshold()) {
            case EXTREMELY_LOW:
                return Color.ORANGE;
            case LOW:
                return Color.YELLOW;
            case NORMAL:
                return Color.GREEN;
            case SOLD_OUT:
                return Color.RED;
            default:
                throw new IllegalStateException("Unexpected value: " + currentEvent.getAvailabilityThreshold()
                                                                                   .toString());
        }
    }

}
