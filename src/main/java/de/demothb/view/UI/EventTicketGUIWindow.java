package de.demothb.view.UI;

import de.demothb.model.Event;
import de.demothb.util.PropertyChangeConstants;
import de.demothb.view.EventTicketViewInterface;
import de.demothb.view.UI.EventListUI.EventListUIModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventTicketGUIWindow extends JFrame implements EventTicketViewInterface {

    public static final String PROP_NAME_VALUE = "value";

    private final PropertyChangeSupport support;
    private JFormattedTextField eventNameField, dateField, availabilityField, quantityField, eventIdField;
    private NumberFormat quantityFormat;
    private DateFormat dateFormat;
    private JLabel eventNameLabel, dateLabel, availabilityLabel, quantityLabel;
    private JButton purchaseButton;
    private JList<Event> eventListBox;
    private EventListUIModel listUIModel;
    private Event currentEvent;

    public EventTicketGUIWindow() {
        initializeTextFields();
        initializeLabels();
        initializePurchaseButton();
        initializeEventListBox();
        layoutAndWireUp();
        support = new PropertyChangeSupport(this);
    }

    private void initializeTextFields() {
        quantityFormat = NumberFormat.getIntegerInstance();
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
        dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm");
        dateField = new JFormattedTextField(dateFormat);
        dateField.setColumns(10);
        dateField.setEditable(false);
    }

    private void initializeAvailabilityTextField() {
        availabilityField = new JFormattedTextField(quantityFormat);
        availabilityField.setColumns(10);
        availabilityField.setEditable(false);
    }

    private void initializeQuantityTextField() {
        quantityField = new JFormattedTextField(quantityFormat);
        quantityField.setColumns(10);
        quantityField.setValue(Integer.valueOf(0));
        quantityField.addPropertyChangeListener(PROP_NAME_VALUE, this);
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

    private void initializePurchaseButton() {
        purchaseButton = new JButton("Purchase ticket(s)");
        purchaseButton.setEnabled(false);
        purchaseButton.setName("Purchase ticket(s)");
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
                                            Integer.valueOf(0),
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
        JPanel labelPane = new JPanel(new GridLayout(0,1));
        labelPane.add(eventNameLabel);
        labelPane.add(dateLabel);
        labelPane.add(availabilityLabel);
        labelPane.add(quantityLabel);

        //Layout the text fields in a panel.
        JPanel fieldPane = new JPanel(new GridLayout(0,1));
        fieldPane.add(eventNameField);
        fieldPane.add(dateField);
        fieldPane.add(availabilityField);
        fieldPane.add(quantityField);
        
        //Layout the list box in a panel.
        JPanel listPane = new JPanel(new GridLayout(0,1));
        JScrollPane listScroller = new JScrollPane(eventListBox);
        listScroller.setPreferredSize(new Dimension(250, 80));
        listPane.add(listScroller);

        //Put the panels in this panel, labels on left,
        //text fields on right.
        JPanel mainPane =  new JPanel();
        mainPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPane.add(listPane, BorderLayout.LINE_START);
        mainPane.add(labelPane, BorderLayout.CENTER);
        mainPane.add(fieldPane, BorderLayout.LINE_END);
        setContentPane(mainPane);
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
    public void clearEventSelection() {
        currentEvent = null;
        eventListBox.clearSelection();
        handleAvailabilityChanged();
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
        if (propName.equals(PropertyChangeConstants.EVENT_AVAILABILITY.geName())) {
            currentEvent = (Event) evt.getSource();
            handleAvailabilityChanged();
            return;
        }
        if (propName.equals(PropertyChangeConstants.NEW_EVENT_ADDED.geName())) {
            listUIModel.addElement((Event) evt.getSource());
            //Make the new item it visible within the viewport of the window.
            eventListBox.ensureIndexIsVisible(listUIModel.getSize());
            return;
        }
        Object source = evt.getSource();
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        eventIdField.setValue(currentEvent != null ? formatter.format(currentEvent.getDate()) : null);
    }

}
