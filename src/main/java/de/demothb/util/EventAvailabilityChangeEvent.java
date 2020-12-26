package de.demothb.util;

import de.demothb.model.Event;

import java.beans.PropertyChangeEvent;

public class EventAvailabilityChangeEvent extends PropertyChangeEvent {

    private String id;

    /**
     * Constructs a new {@code PropertyChangeEvent}.
     *
     * @param source       the bean that fired the event
     * @param propertyName the programmatic name of the property that was changed
     * @param oldValue     the old value of the property
     * @param newValue     the new value of the property
     * @throws IllegalArgumentException if {@code source} is {@code null}
     */
    public EventAvailabilityChangeEvent(Object source, String propertyName, Object oldValue, Object newValue, Event event) {
        super(source, propertyName, oldValue, newValue);
        this.id = event.getId();
    }

    /**
     * Gets the id of the event that was changed.
     *
     * @return The id specifying the event id element that was
     *         changed.
     */
    public String getId() {
        return id;
    }

    void appendTo(StringBuilder sb) {
        sb.append("; id=").append(getId());
    }

}
