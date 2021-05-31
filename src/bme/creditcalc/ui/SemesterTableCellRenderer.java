package bme.creditcalc.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class SemesterTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(isSelected){
            c.setForeground(Color.black);
        } else {
            c.setForeground(table.getForeground());
        }
        if(((SemesterTable)table.getModel()).getSemester().getSubject(row).isFinalized()){
            c.setBackground(Color.green);
        } else {
            c.setBackground(table.getBackground());
        }
        return c;
    }

}
