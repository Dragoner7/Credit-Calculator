package bme.creditcalc.ui

import java.awt.Component
import javax.swing.JTable
import javax.swing.table.TableCellRenderer

class SemesterTableCellRenderer(private var defaultRenderer: TableCellRenderer) : TableCellRenderer {
    override fun getTableCellRendererComponent(table: JTable, value: Any, isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int): Component {
        val c = defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
        if ((table.model as SemesterTable).semester!!.subjects[row].isFinalized) {
            c.background = Window.DONE_COLOR
        } else {
            c.background = table.background
        }
        return c
    }
}