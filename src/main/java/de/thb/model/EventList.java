package de.thb.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class EventList implements Observer{

	
	public List<Event> events = new ArrayList<>();
	public Event e = null;

	public List<Event> loadEvents(){
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("EventsData.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
		    String line = br.readLine();
		    
		    while (line != null) {
		    	//Getting Event Attribut
		    	final String[] eventAttributs= line.split(",");
		    	
		    	//creating Events
		    	Event event = new Event();
		    	event.setName(eventAttributs[0]);
		    	event.setDate(LocalDate.parse(eventAttributs[1], DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		    	event.setNumberOfTicket(Integer.valueOf(eventAttributs[2]));
		    	
		    	events.add(event);
		        
		        line = br.readLine();
		    }
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}   
		return events;
	}
	
	public Event findByEventName(final String eventName) {
		List<Event> events = loadEvents();
		for(int i = 0; i < events.size(); i++) {
			if(eventName.equals(events.get(i).getName())) {
				e = events.get(i);
			}
		}
    	return e;
    }
	
	@Override
	public void update(Observable arg0, Object arg1) {
		loadEvents();    	
	}
}
