package de.demothb.view.UI;

import de.demothb.model.Event;
import de.demothb.util.PropertyChangeConstants;
import de.demothb.view.EventTicketViewInterface;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventTicketGUIWindow extends JFrame implements EventTicketViewInterface {

    private JFormattedTextField eventNameField, dateField, availabilityField, quantityField, eventIdField;
    private JLabel eventNameLabel, dateLabel, availabilityLabel, quantityLabel;
    private JButton purchaseButton;
    private JList<Event> eventListBox;
    private List<Event> events;

    private Event currentEvent;

    public EventTicketGUIWindow() {
        initializeTextFields();
        initializeLabels();
        initializeEventListBox();
        initializePurchaseButton();
    }

    private void initializeTextFields() {
        initializeEventNameTextField();
        initializeDateTextField();
        initializeAvailabilityTextField();
        initializeQuantityTextField();
        initializeEventIdTextField();
    }

    private void initializeEventNameTextField() {
        eventNameField = new JFormattedTextField();
        eventNameField.setColumns(10);
        eventNameField.setEditable(false);
    }

    private void initializeDateTextField() {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm");
        dateField = new JFormattedTextField(format);
        dateField.setColumns(10);
        dateField.setEditable(false);
    }

    private void initializeAvailabilityTextField() {
        availabilityField = new JFormattedTextField();
        availabilityField.setColumns(10);
        availabilityField.setEditable(false);
    }

    private void initializeQuantityTextField() {
        quantityField = new JFormattedTextField();
        quantityField.setColumns(10);
        quantityField.setEditable(true);
        quantityField.setValue(Integer.valueOf(0));
        quantityField.addPropertyChangeListener("value", this);
    }

    private void initializeEventIdTextField() {
        eventIdField = new JFormattedTextField();
        eventIdField.setColumns(10);
        eventIdField.setEditable(false);
        eventIdField.setVisible(false);
    }

    private void initializeLabels() {
        eventNameLabel = new JLabel("Event:");
        eventNameLabel.setLabelFor(eventNameField);

        dateLabel = new JLabel("Date:");
        dateLabel.setLabelFor(dateField);

        availabilityLabel = new JLabel("Available ticket:");
        availabilityLabel.setLabelFor(availabilityField);

        quantityLabel = new JLabel("Quantity:");
        quantityLabel.setLabelFor(quantityField);
    }

    private void initializeEventListBox() {
        eventListBox = new JList<Event>();
        eventListBox.setName("Event list");
        eventListBox.addListSelectionListener(
                new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        if (!e.getValueIsAdjusting()) {
                            currentEvent = eventListBox.getSelectedValue();
                            eventIdField.setValue(currentEvent.getId());
                            eventNameField.setValue(currentEvent.getName());
                            availabilityField.setValue(Integer.valueOf(currentEvent.getAvailability()));
                            quantityField.setValue(Integer.valueOf(0));
                            enableTicketPurchase(false);
                        }
                    }
                }
        );
    }

    private void initializePurchaseButton() {
        purchaseButton = new JButton("Purchase ticket(s)");
        purchaseButton.setEnabled(false);
        purchaseButton.setName("Purchase ticket(s)");
        purchaseButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Fire Purchase Event
                    }
                }
        );
    }

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
