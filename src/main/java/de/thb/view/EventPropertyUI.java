package de.thb.view;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.BorderFactory;
import javax.swing.JSeparator;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.thb.model.ConstantPercentage;
import de.thb.model.Event;

import java.awt.Font;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.security.AccessControlException;

public class EventPropertyUI extends JPanel implements ActionListener, FocusListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	JTextField eventNameField, ticketQuantityField, ticketQuantityToBuyField, availibilityField, eventDateField;
	boolean addressSet = false;
	Font regularFont, italicFont;
	JLabel actualAvailabilityDisplay;
	JPanel leftHalf;
	private final int GAP_BETWEEN = 7;
	final static int TEXTFIELD_COLUMN = 5;
	private Event e;

	public EventPropertyUI() {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		add(createLeftHalf());
		add(createActualAvailabilityDisplay());
	}

	JPanel createLeftHalf() {
		if (null == leftHalf) {
			leftHalf = new JPanel() {

				private static final long serialVersionUID = 1L;

				public Dimension getMaximumSize() {
					Dimension pref = getPreferredSize();
					return new Dimension(Integer.MAX_VALUE, pref.height);
				}
			};
			leftHalf.setLayout(new BoxLayout(leftHalf, BoxLayout.PAGE_AXIS));
			leftHalf.add(createEntryFields());
			leftHalf.add(createButton());
			//eventUtilitiesUI.createButton(label)
		}
		return leftHalf;
	}
	
	private JButton createButton() {
		EventUtilitiesUI eventUtilitiesUI = new EventUtilitiesUI();
		final JButton button = eventUtilitiesUI.createButtons("Buy Tickets", GAP_BETWEEN);
		button.addActionListener(e -> {
			getHighLight();
		});
		return button;
	}

	/**
	 * Called when the user clicks the button or presses Enter in a text field.
	 */
	public void actionPerformed(ActionEvent e) {
		if ("clear".equals(e.getActionCommand())) {
			addressSet = false;
			eventNameField.setText("");
			eventDateField.setText("");
			availibilityField.setText("");
			ticketQuantityField.setText("");
		} else {
			addressSet = true;
		}
		updateDisplays();
	}

	protected void updateDisplays() {
		actualAvailabilityDisplay.setText(getActualAvailability());
		if (addressSet) {
			actualAvailabilityDisplay.setFont(regularFont);
		} else {
			actualAvailabilityDisplay.setFont(italicFont);
		}
	}

	protected JComponent createActualAvailabilityDisplay() {
		JPanel panel = new JPanel(new BorderLayout());
		actualAvailabilityDisplay = new JLabel();
		actualAvailabilityDisplay.setHorizontalAlignment(JLabel.CENTER);
		regularFont = actualAvailabilityDisplay.getFont().deriveFont(Font.BOLD, 16.0f);
		italicFont = regularFont.deriveFont(Font.ITALIC);
		updateDisplays();

		// Lay out the panel.
		panel.setBorder(BorderFactory.createEmptyBorder(GAP_BETWEEN - 5, 0, GAP_BETWEEN - 5, 0));
		panel.add(new JSeparator(JSeparator.VERTICAL), BorderLayout.LINE_START);
		panel.add(actualAvailabilityDisplay, BorderLayout.CENTER);
		panel.setPreferredSize(new Dimension(200, 150));

		return panel;
	}

	protected String getActualAvailability() {
		if (!addressSet)
			return "Actual Availability.";

		String eventName = eventNameField.getText();

		return "<html><p align=center>" + " Actual Availability " + eventName + "<br>" + "</p></html>";
	}

	/**
	 * Called when one of the fields gets the focus so that the focused field is
	 * selectable.
	 */
	public void focusGained(FocusEvent e) {
		Component c = e.getComponent();
		if (c instanceof JTextField) {
			selectItLater(c);
		} else if (c instanceof JTextField) {
			((JTextField) c).selectAll();
		}
	}

	// Workaround for formatted text field focus side effects.
	protected void selectItLater(Component c) {
		if (c instanceof JTextField) {
			final JTextField ftf = (JTextField) c;
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					ftf.selectAll();
				}
			});
		}
	}

	private void getHighLight() {
		final String valueOfFieldTicketToBuy = ticketQuantityToBuyField.getText();

		if (!valueOfFieldTicketToBuy.isEmpty()) {
			final int anzTicketToBuy = Integer.parseInt(valueOfFieldTicketToBuy);
			final float restOfEvents = (e.getNumberOfTicket() - anzTicketToBuy);
			final float percentage = restOfEvents/e.getNumberOfTicket();

			availibilityField.setText(String.valueOf(restOfEvents));
			if (percentage <= ConstantPercentage.PERCENTAGE_10.getPercentage() 
					&& percentage > ConstantPercentage.PERCENTAGE_5.getPercentage()) {
				availibilityField.setBackground(Color.green);
			} else if (percentage <= ConstantPercentage.PERCENTAGE_5.getPercentage()
					&& percentage > ConstantPercentage.PERCENTAGE_0.getPercentage()) {
				availibilityField.setBackground(Color.YELLOW);
			} else if (ConstantPercentage.PERCENTAGE_0.getPercentage() >= percentage) {
				availibilityField.setBackground(Color.RED);
				availibilityField.setText("Sold out!");
			}
		}
	}

	// Needed for FocusListener interface.
	public void focusLost(FocusEvent e) {
	} // ignore

	public void setData(final Event e) {
		this.e = e;
		eventNameField.setText(e.getName());
		eventDateField.setText(String.valueOf(e.getDate()).substring(0, 10));
		ticketQuantityField.setText(String.valueOf(e.getNumberOfTicket()));
		availibilityField.setText("");
		ticketQuantityToBuyField.setText("");
	}

	protected JComponent createEntryFields() {
		JPanel panel = new JPanel(new SpringLayout());

		String[] labelStrings = { "Event: ", "Date: ", "Quantity: ", "Actual Availibility: ", "Booked Tickets: " };

		JLabel[] labels = new JLabel[labelStrings.length];
		JComponent[] fields = new JComponent[labelStrings.length];
		int fieldNum = 0;

		// Create the text field and set it up.
		if (null == eventNameField) {
			eventNameField = new JTextField();
			eventNameField.setColumns(TEXTFIELD_COLUMN);
			fields[fieldNum++] = eventNameField;
		}
		if (null == eventDateField) {
			eventDateField = new JTextField();
			eventDateField.setColumns(TEXTFIELD_COLUMN);
			fields[fieldNum++] = eventDateField;
		}

		if (null == ticketQuantityField) {
			ticketQuantityField = new JTextField();
			ticketQuantityField.setColumns(TEXTFIELD_COLUMN);
			fields[fieldNum++] = ticketQuantityField;
		}
		if (null == availibilityField) {
			availibilityField = new JTextField();
			availibilityField.setColumns(TEXTFIELD_COLUMN);
			availibilityField.setEditable(false);
			fields[fieldNum++] = availibilityField;
		}

		if (null == ticketQuantityToBuyField) {
			ticketQuantityToBuyField = new JTextField();
			ticketQuantityToBuyField.setColumns(TEXTFIELD_COLUMN);
			fields[fieldNum++] = ticketQuantityToBuyField;
		}

		// Associate label/field pairs, add everything,
		// and lay it out.
		for (int i = 0; i < labelStrings.length; i++) {
			labels[i] = new JLabel(labelStrings[i], JLabel.TRAILING);
			labels[i].setLabelFor(fields[i]);
			panel.add(labels[i]);
			panel.add(fields[i]);

			// Add listeners to each field.
			JTextField actualField = (JTextField) fields[i];

			actualField.addActionListener(this);
			actualField.addFocusListener(this);
		}
		EventUtilitiesUI.buildCompactGrid(panel, labelStrings.length, 2, 0, 0, 0, GAP_BETWEEN);
		return panel;
	}
	
	private void highlight(final String setValue) {

	    Runnable doHighlight = new Runnable() {
	        @Override
	        public void run() {
	        	ticketQuantityToBuyField.setText(setValue);
				getHighLight();
	        }
	    };       
	    SwingUtilities.invokeLater(doHighlight);
	}

}