package de.thb.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TableEventUI extends JPanel {

    private static final int GAP_BETWEEN = 10;
    private static final int INITIAL_ROWS_NUMBER = 2;
    private static final String[] columnHeader = {
            "#",
            "Name",
            "Available tickets",
            "Booked tickets",
            "Actual availability"};
    private final DefaultTableModel tableModel = new DefaultTableModel(null, columnHeader) {

        @Override
        public Class<?> getColumnClass(int col) {
            return getValueAt(0, col).getClass();
        }
    };

    private boolean isAutoScroll;
    TableColumn tColumn;

    public TableEventUI(){

        //Table row dynamically added when an event is generated
        this.setLayout(new BorderLayout());
        JTable table = new JTable(tableModel) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component returnComp = super.prepareRenderer(renderer, row, column);
                Color alternateColor = Color.decode("#FFFAF0");
                Color whiteColor = Color.WHITE;
                if (!returnComp.getBackground().equals(getSelectionBackground())) {
                    Color backgroundColor = (row % 2 == 0 ? alternateColor : whiteColor);
                    returnComp.setBackground(backgroundColor);
                }
                this.getColumnModel().getColumn(4).setCellRenderer(EventUtilitiesUI.setColumnColor(Color.RED));
                return returnComp;
            }
        };
        Dimension d = new Dimension(320, INITIAL_ROWS_NUMBER * table.getRowHeight());
        table.setPreferredScrollableViewportSize(d);
        tColumn = table.getColumnModel().getColumn(4);
        tColumn.setCellRenderer(EventUtilitiesUI.setColumnColor(Color.RED));
        for (int i = 0; i < INITIAL_ROWS_NUMBER; i++) {
            addRow();
        }
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JScrollBar vScroll = scrollPane.getVerticalScrollBar();
        vScroll.addAdjustmentListener(e -> isAutoScroll = !e.getValueIsAdjusting());
        this.add(scrollPane, BorderLayout.CENTER);
        JPanel panel = new JPanel(new GridLayout(1,2));
        panel.add(new JButton(new AbstractAction("Add Row") {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isAutoScroll) addRow();
            }
        }));
        this.add(panel, BorderLayout.PAGE_END);
        //this.add(new Panel().add(EventUtilitiesUI.createButtons("Buy Tickets", GAP_BETWEEN-5)), BorderLayout.EAST);
    }

    private void addRow() {
        tableModel.addRow(new Object[] {
                " EventID",
                "event name",
                "Number of tickets",
                "book some tickets",
                "Actual availability"
        });
    }

}
