package de.thb.view;


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import java.awt.*;


public class EventUtilitiesUI extends DefaultListCellRenderer{
	private JButton button;

    private static SpringLayout.Constraints getConstraintsForCell(int row, int col, Container parent, int cols) {
        SpringLayout layout = (SpringLayout) parent.getLayout();
        Component c = parent.getComponent(row * cols + col);
        return layout.getConstraints(c);
    }

    /**
     * Aligns the first <code>rows</code> * <code>cols</code> components of
     * <code>parent</code> in a grid. Each component in a column is as wide as the
     * maximum preferred width of the components in that column; height is similarly
     * determined for each row. The parent is made just big enough to fit them all.
     *
     * @param rows     number of rows
     * @param cols     number of columns
     * @param initialX x location to start the grid at
     * @param initialY y location to start the grid at
     * @param xPad     x padding between cells
     * @param yPad     y padding between cells
     */
    public static void buildCompactGrid(
            Container parent,
            int rows,
            int cols,
            int initialX,
            int initialY,
            int xPad,
            int yPad) {

        SpringLayout layout;
        try {
            layout = (SpringLayout) parent.getLayout();
        } catch (ClassCastException exc) {
            System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
            return;
        }

        // Align all cells in each column and make them the same width.
        Spring xPosition = Spring.constant(initialX);
        for (int c = 0; c < cols; c++) {
            Spring width = Spring.constant(0);
            for (int r = 0; r < rows; r++) {
                width = Spring.max(width, getConstraintsForCell(r, c, parent, cols).getWidth());
            }
            for (int r = 0; r < rows; r++) {
                SpringLayout.Constraints constraints = getConstraintsForCell(r, c, parent, cols);
                constraints.setX(xPosition);
                constraints.setWidth(width);
            }
            xPosition = Spring.sum(xPosition, Spring.sum(width, Spring.constant(xPad)));
        }

        // Align all cells in each row and make them the same height.
        Spring yPosition = Spring.constant(initialY);
        for (int r = 0; r < rows; r++) {
            Spring height = Spring.constant(0);
            for (int c = 0; c < cols; c++) {
                height = Spring.max(height, getConstraintsForCell(r, c, parent, cols).getHeight());
            }
            for (int c = 0; c < cols; c++) {
                SpringLayout.Constraints constraints = getConstraintsForCell(r, c, parent, cols);
                constraints.setY(yPosition);
                constraints.setHeight(height);
            }
            yPosition = Spring.sum(yPosition, Spring.sum(height, Spring.constant(yPad)));
        }

        // Set the parent's size.
        SpringLayout.Constraints pCons = layout.getConstraints(parent);
        pCons.setConstraint(SpringLayout.SOUTH, yPosition);
        pCons.setConstraint(SpringLayout.EAST, xPosition);
    }

    /**
     * The EventListCellRenderer class generate a Jlist with alternating colors
     * It will automatically do what's correct for the current look and feel
     * @see javax.swing.DefaultListCellRenderer
     *
     */
    private static class EventListCellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (index % 2 == 0) setBackground(Color.decode("#E0FFFF"));
            else setBackground(Color.WHITE);
            return this;
        }
    }

    private static class ColumnColorRenderer extends DefaultTableCellRenderer {
        Color backgroundColor;
        public ColumnColorRenderer(Color backgroundColor) {
            super();
            this.backgroundColor = backgroundColor;
        }
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,   boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            cell.setBackground(backgroundColor);
            return cell;
        }
    }

    public static TableCellRenderer setColumnColor(Color color){
        return new ColumnColorRenderer(color);
    }
    public static ListCellRenderer getCellRenderer(){
        return new EventListCellRenderer();
    }

    public JButton createButtons(String label, int padding) {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        button = new JButton(label);
        panel.add(button);

        // Match the SpringLayout's gap, subtracting 5 to make
        // up for the default gap FlowLayout provides.
        panel.setBorder(BorderFactory.createEmptyBorder(padding*2, 0, 0, padding - 5));
        return button;
    }
}