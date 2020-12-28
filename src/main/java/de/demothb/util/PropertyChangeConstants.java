package de.demothb.util;

public enum PropertyChangeConstants {

    EVENT_SELECTED("eventSelected"),
    QUANTITY_CHANGED("ticketQuantityChanged"),
    PURCHASE_TICKET("purchaseTicket"),
    EVENT_AVAILABILITY("availabilityChanged"),
    EVENT_LIST("newEventAdded"),
    NEW_EVENT_ADDED("newEventAdded");

    private String propertyName;

    PropertyChangeConstants(String propertyName) {
        this.propertyName = propertyName;
    }

    public String geName() {
        return propertyName;
    }

}
