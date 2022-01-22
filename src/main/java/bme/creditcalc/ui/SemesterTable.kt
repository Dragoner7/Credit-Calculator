package bme.creditcalc.ui

import javax.swing.JFrame
import javax.swing.JPanel
import bme.creditcalc.model.Leckekonyv
import javax.swing.JTable
import javax.swing.JLabel
import java.awt.BorderLayout
import com.github.weisj.darklaf.LafManager
import com.github.weisj.darklaf.theme.DarculaTheme
import javax.swing.event.ListDataListener
import javax.swing.event.ListDataEvent
import bme.creditcalc.ui.SemesterTableCellRenderer
import javax.swing.JScrollPane
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeEvent
import javax.swing.JPopupMenu
import javax.swing.JMenuItem
import bme.creditcalc.ui.PopupListener
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import javax.swing.JComboBox
import bme.creditcalc.model.Semester
import java.awt.Dimension
import javax.swing.JButton
import javax.swing.JMenuBar
import javax.swing.JMenu
import javax.swing.JOptionPane
import bme.creditcalc.ui.AdvancedCalculator
import javax.swing.table.DefaultTableModel
import java.time.LocalDate
import java.lang.NullPointerException
import bme.creditcalc.ui.SemesterTable
import javax.swing.JFileChooser
import bme.creditcalc.neptunreader.XLSXFileFilter
import bme.creditcalc.neptunreader.NeptunReader
import java.lang.Exception
import java.awt.event.MouseAdapter
import javax.swing.table.AbstractTableModel
import javax.swing.JDialog
import javax.swing.JCheckBox
import javax.swing.JRadioButton
import javax.swing.JTextField
import javax.swing.BoxLayout
import javax.swing.table.TableCellRenderer
import bme.creditcalc.model.SemesterDate
import javax.swing.MutableComboBoxModel
import java.util.function.Consumer
import kotlin.jvm.JvmStatic
import javax.swing.SwingWorker
import kotlin.Throws
import java.io.IOException
import java.io.FileInputStream
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFSheet

class SemesterTable : AbstractTableModel() {
    var semester: Semester? = null
    fun setModel(s: Semester?) {
        semester = s
        fireTableDataChanged()
    }

    override fun getRowCount(): Int {
        return semester!!.subjects.size
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
        return when (columnIndex) {
            0 -> semester!!.subjects[rowIndex].name
            1 -> semester!!.subjects[rowIndex].credit
            2 -> semester!!.subjects[rowIndex]!!.grade
            3 -> semester!!.subjects[rowIndex].minta
            else -> null
        }
    }

    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
        return true
        /*if(columnIndex > 1){
            return true;
            //return !subjects.get(rowIndex).isFinalized();
        }
        return false;*/
    }

    override fun getColumnClass(columnIndex: Int): Class<*> {
        when (columnIndex) {
            0 -> return String::class.java
            1 -> return Double::class.java
            2 -> return Int::class.java
            3 -> return Boolean::class.java
        }
        return Any::class.java
    }

    override fun setValueAt(aValue: Any, rowIndex: Int, columnIndex: Int) {
        when (columnIndex) {
            0 -> semester!!.subjects[rowIndex].name = aValue as String
            1 -> semester!!.subjects[rowIndex].credit = aValue as Double
            2 -> {
                semester!!.subjects[rowIndex]!!.grade = aValue as Int
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