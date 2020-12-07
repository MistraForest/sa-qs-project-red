package de.thb.view;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.thb.model.ConstantPercentage;
import de.thb.model.Event;
import de.thb.model.EventList;
import de.thb.presenter.AppSystem;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class DashboardUI extends JPanel {

	private AppSystem appSystem;
	private DefaultListModel eventListModel;
	private JList eventListField;
	private EventPropertyUI eventPropertyUI;
	private List<Event> events = new ArrayList<>();
	private EventList model = new EventList();

	public DashboardUI() {

		setLayout(new GridLayout(0, 1, 5, 10));
		setBounds(20, 20, 60, 300);
		JScrollPane scrollPaneForEvents = new JScrollPane();

		appSystem = new AppSystem();
		eventPropertyUI = new EventPropertyUI();
		events = appSystem.getEvents();

		// DefaultListModell wird erzeugt
		eventListModel = new DefaultListModel();

		// JList mit Einträgen wird erstellt
		eventListField = new JList(eventListModel);
		eventListField.setCellRenderer(EventUtilitiesUI.getCellRenderer());

		for (Event event : events) {
			eventListModel.addElement(event.getName());
		}

		// JList wird Panel hinzugefügt
		add(eventListField);
		scrollPaneForEvents.setViewportView(eventListField);
		eventListField.setLayoutOrientation(JList.VERTICAL);
		add(scrollPaneForEvents);
		add(eventPropertyUI);

		eventListField.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent evt) {
				jList1ValueChanged(evt);
			}
		});

	}

	private void jList1ValueChanged(ListSelectionEvent evt) {
		final String selectedValue = (String) eventListField.getSelectedValue();
		final Event e = appSystem.findByEventName(selectedValue);
		eventPropertyUI.setData(e);
	}
}
