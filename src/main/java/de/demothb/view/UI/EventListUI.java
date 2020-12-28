package de.demothb.view.UI;

import de.demothb.model.Event;
import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventListUI extends JList<Event> {
    
    public EventListUI(){
        super();
        initializeEventListBox();
    }

    private void initializeEventListBox() {
        setName("Event list");
        setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        setLayoutOrientation(JList.HORIZONTAL_WRAP);
        setVisibleRowCount(-1);
        setCellRenderer(new EventListUICellRenderer());
    }

    public class EventListUICellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList<?> list,
                                                      Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Event) {
                Event evt = (Event)value;
                setText(evt.getName());
                setToolTipText(evt.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' H:m")));
            }
            return this;
        }
    }

    public static class EventListUIModel extends AbstractListModel<Event>{

        private List<Event> events;

        public EventListUIModel(List<Event> events){
            this.events = events;
        }

        @Override
        public int getSize() {
            return events.size();
        }

        @Override
        public Event getElementAt(int index) {
            return events.get(index);
        }

        public boolean addElement(Event event){
            if (event != null) {
                return events.add(event);
            }
            return false;
        }
    }
    
}
