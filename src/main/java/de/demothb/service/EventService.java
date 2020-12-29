package de.demothb.service;

import de.demothb.model.Event;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class EventService implements EventServiceInterface {

    HashMap<String, Event> events;

    public EventService() {
        initialize();
    }

    @Override
    public Event generateEvent() {
        String eventName = RandomStringUtils.randomAlphabetic(10);
        int ticketCount = NumberUtils.toInt(RandomStringUtils.randomNumeric(3));
        LocalDateTime localDate = generateRandomLocalDate();
        try {
            return new Event(eventName, ticketCount, localDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Event getById(String id) {
        if (events != null && StringUtils.isNotEmpty(id)) {
            return events.get(id);
        }
        return null;
    }

    @Override
    public void saveEvent(Event event) {
        events.put(event.getId(), event);
    }

    @Override
    public List<Event> getList() {
        return new ArrayList<Event>(events.values());
    }

    private void initialize() {
        events = new HashMap<String, Event>();
        String[][] data = {
                {"Kim Larson Concert", "2000", "2020/12/31 10:30"},
                {"Symposium on Math", "50", "2021/01/06 15:45"},
                {"Cph Museum open day", "400", "2020/02/25 09:30"}
        };
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        for (String[] eventData : data) {
            try {
                Event event = new Event(
                        eventData[0],
                        Integer.parseInt(eventData[1]),
                        LocalDateTime.parse(eventData[2], formatter)
                );
                events.put(event.getId(), event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private LocalDateTime generateRandomLocalDate() {
        LocalDateTime date = LocalDateTime.now();
        Random r = new Random();
        date.plusYears(r.nextInt(3));
        date.plusMonths(r.nextInt(12));
        date.plusDays(r.nextInt(31));
        return date;
    }

}
