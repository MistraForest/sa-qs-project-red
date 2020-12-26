package de.demothb.service;

import de.demothb.model.Event;

import java.util.List;

public interface EventServiceInterface {

    Event addEvent();

    Event getById(String id);

    void saveEvent(Event event);

    List<Event> getList();

}
