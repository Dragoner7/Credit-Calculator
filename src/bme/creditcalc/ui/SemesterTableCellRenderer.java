package bme.creditcalc.ui;

import bme.creditcalc.Semester;
import org.apache.poi.sl.usermodel.TableCell;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class SemesterTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(((Semester)table.getModel()).getSubject(row).isFinalized()){
            c.setBackground(Color.green);
        } else {
            c.setBackground(table.getBackground());
        }
        return c;
    }

}
