package bme.creditcalc.ui;

import bme.creditcalc.Main;
import com.github.weisj.darklaf.LafManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class SemesterTableCellRenderer implements TableCellRenderer {
    TableCellRenderer defaultRenderer;
    public SemesterTableCellRenderer(TableCellRenderer defaultRenderer){
        this.defaultRenderer = defaultRenderer;
    }
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(((SemesterTable)table.getModel()).getSemester().getSubject(row).isFinalized()){
            c.setBackground(Window.DONE_COLOR);
        } else {
            c.setBackground(table.getBackground());
        }
        return c;
    }
}
