package bme.creditcalc.ui

import bme.creditcalc.model.Semester
import javax.swing.table.AbstractTableModel

class SemesterTable : AbstractTableModel() {
    var semester: Semester? = null
    fun setModel(s: Semester?) {
        semester = s
        fireTableDataChanged()
    }

    override fun getRowCount(): Int {
        return semester?.subjects?.size ?: 0
    }

    override fun getColumnCount(): Int {
        return 4
    }

    override fun getColumnName(column: Int): String {
        when (column) {
            0 -> return "Name"
            1 -> return "Credit"
            2 -> return "Grade"
            3 -> return "Minta"
        }
        return super.getColumnName(column)
    }

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        if(semester == null){
            return ""
        }
        return when (columnIndex) {
            0 -> semester!!.subjects[rowIndex].name
            1 -> semester!!.subjects[rowIndex].credit
            2 -> semester!!.subjects[rowIndex].grade
            3 -> semester!!.subjects[rowIndex].minta
            else -> semester!!.subjects[rowIndex].name
        }
    }

    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
        return true
    }

    override fun getColumnClass(columnIndex: Int): Class<*> {
        return when (columnIndex) {
            0 -> String::class.javaObjectType
            1 -> Double::class.javaObjectType
            2 -> Int::class.javaObjectType
            3 -> Boolean::class.javaObjectType
            else -> Any::class.javaObjectType
        }
    }

    override fun setValueAt(aValue: Any, rowIndex: Int, columnIndex: Int) {
        if(semester == null){
            return
        }
        when (columnIndex) {
            0 -> semester!!.subjects[rowIndex].name = aValue as String
            1 -> semester!!.subjects[rowIndex].credit = aValue as Double
            2 -> {
                semester!!.subjects[rowIndex].grade = aValue as Int
                fireTableRowsUpdated(rowIndex, rowIndex)
            }
            3 -> {
                semester!!.subjects[rowIndex].minta = aValue as Boolean
                fireTableRowsUpdated(rowIndex, rowIndex)
            }
            else -> {}
        }
    }
}