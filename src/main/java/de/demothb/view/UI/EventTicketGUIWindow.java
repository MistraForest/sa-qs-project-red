package de.demothb.view.UI;

import de.demothb.model.Event;
import de.demothb.util.PropertyChangeConstants;
import de.demothb.view.EventTicketViewInterface;
import de.demothb.view.UI.EventListUI.EventListUIModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventTicketGUIWindow extends JPanel implements EventTicketViewInterface {

    public static final String PROP_NAME_VALUE = "value";

    private final PropertyChangeSupport support;

    private JFormattedTextField eventNameField, dateField, availabilityField, quantityField, eventIdField;
    private DefaultFormatterFactory integerFormatterFactory;
    private JLabel eventNameLabel, dateLabel, availabilityLabel, quantityLabel, eventIdLabel;
    private JButton purchaseButton;
    private EventListUIModel listUIModel;
    private JList<Event> eventListBox;

    private Event currentEvent;

    public EventTicketGUIWindow() {
        support = new PropertyChangeSupport(this);
        initializeTextFields();
        initializeLabels();
        initializePurchaseButton();
        initializeEventListBox();
        layoutAndWireUp();
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
        eventNameField.setColumns(15);
        eventNameField.setEditable(false);
    }

    private void initializeDateTextField() {
        dateField = new JFormattedTextField();
        dateField.setFormatterFactory(integerFormatterFactory);
        dateField.setColumns(15);
        dateField.setEditable(false);
    }

    private void initializeAvailabilityTextField() {
        availabilityField = new JFormattedTextField();
        availabilityField.setFormatterFactory(integerFormatterFactory);
        availabilityField.setColumns(5);
        availabilityField.setEditable(false);
    }

    private void initializeQuantityTextField() {
        quantityField = new JFormattedTextField();
        quantityField.setFormatterFactory(integerFormatterFactory);
        quantityField.setColumns(5);
        quantityField.setValue(Integer.valueOf(0));
        quantityField.addPropertyChangeListener(PROP_NAME_VALUE, this);
    }

    private void initializeEventIdTextField() {
        eventIdField = new JFormattedTextField();
        eventIdField.setColumns(5);
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

        eventIdLabel = new JLabel("Event ID:");
        eventIdLabel.setVisible(false);
        eventIdLabel.setLabelFor(eventIdField);
    }

    private void initializePurchaseButton() {
        purchaseButton = new JButton("Purchase ticket");
        purchaseButton.setEnabled(false);
        purchaseButton.setName("Purchase ticket");
        purchaseButton.setActionCommand("Purchase ticket");
        purchaseButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Fire Purchase Event
                        Integer quantity = ((Integer) quantityField.getValue());
                        if (currentEvent != null && quantity.intValue() > 0) {
                            support.firePropertyChange(
                                    new PropertyChangeEvent(
                                            currentEvent,
                                            PropertyChangeConstants.PURCHASE_TICKET.geName(),
                                            null,
                                            quantity
                                    )
                            );
                        }
                    }
                }
        );
    }

    private void initializeEventListBox() {
        eventListBox = new EventListUI();
        eventListBox.setName("Event list");
        eventListBox.addListSelectionListener(
                new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        if (!e.getValueIsAdjusting()) {
                            currentEvent = eventListBox.getSelectedValue();
                            handleAvailabilityChanged();
                        }
                    }
                }
        );
    }

    private void layoutAndWireUp(){
        //Lay out the labels in a panel.
        JPanel labelPane = new JPanel(new GridLayout(0,1, 5, 11));
        labelPane.add(eventNameLabel);
        labelPane.add(dateLabel);
        labelPane.add(availabilityLabel);
        labelPane.add(quantityLabel);
        labelPane.add(eventIdLabel);
        labelPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        //Layout the text fields in a panel.
        JPanel fieldPane = new JPanel(new GridLayout(0,1, 0, 2));
        fieldPane.add(eventNameField);
        fieldPane.add(dateField);
        fieldPane.add(availabilityField);
        fieldPane.add(quantityField);
        fieldPane.add(eventIdField);
        fieldPane.add(purchaseButton);
        fieldPane.setBorder(BorderFactory.createEmptyBorder(25, 20, 20, 20));
        
        //Layout the list box in a panel.
        JPanel listPane = new JPanel(new GridLayout(0,1, 0, 10));
        JScrollPane listScroller = new JScrollPane(eventListBox);
        listScroller.setPreferredSize(new Dimension(180, 125));
        listPane.add(listScroller);
        listPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        //Put the panels in this panel, labels on left,
        //text fields on right.
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(listPane, BorderLayout.LINE_START);
        add(labelPane, BorderLayout.CENTER);
        add(fieldPane, BorderLayout.LINE_END);
    }

    @Override
    public void setEventList(List<Event> events) {
        listUIModel = new EventListUIModel(events);
        eventListBox.setModel(listUIModel);
    }

    @Override
    public void enableTicketPurchase(boolean enable) {
        purchaseButton.setEnabled(enable);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propName = evt.getPropertyName();
        Object source = evt.getSource();
        if (source instanceof Event) {
            if (propName.equals(PropertyChangeConstants.EVENT_AVAILABILITY.geName())) {
                currentEvent = (Event) evt.getSource();
                handleAvailabilityChanged();
                return;
            }
            if (propName.equals(PropertyChangeConstants.NEW_EVENT_ADDED.geName())) {
                listUIModel.addElement((Event) evt.getSource());
                return;
            }
        }
        if (propName.equals(PropertyChangeConstants.EVENT_LIST.geName())) {
            setEventList((List<Event>) evt.getNewValue());
            return;
        }
        if (source == quantityField) {
            if (((Integer) quantityField.getValue()).intValue() > 0) {
                enableTicketPurchase(true);
            }
        }
    }

    private void handleAvailabilityChanged() {
        updateAvailabilityField();
        resetQuantityField();
        updateEventNameField();
        updateEventIdField();
        updateDateField();
        enableTicketPurchase(false);
    }

    private void updateAvailabilityField() {
        Color color = null;
        Integer value = null;
        if (currentEvent != null) {
            color = getAvailabilityColor();
            value = Integer.valueOf(currentEvent.getAvailability());
        }
        availabilityField.setValue(value);
        availabilityField.setForeground(color);
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

    private void resetQuantityField() {
        quantityField.setText("0");
        quantityField.setValue(Integer.valueOf(0));
    }

    private void updateEventNameField() {
        eventNameField.setValue(currentEvent != null ? currentEvent.getName() : null);
    }

    private void updateEventIdField() {
        eventIdField.setValue(currentEvent != null ? currentEvent.getId() : null);
    }

    private void updateDateField() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' HH:mm");
        dateField.setValue(currentEvent != null ? formatter.format(currentEvent.getDate()) : null);
    }

    @Override
    public void subscribe(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    @Override
    public void unsubscribe(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }
}
