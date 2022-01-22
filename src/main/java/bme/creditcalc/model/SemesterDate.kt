package bme.creditcalc.model

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
import java.util.*

data class SemesterDate(val year: Int, val semester: Int) {
    override fun toString(): String {
        return year.toString() + "/" + (year + 1) + "/" + semester
    }

    companion object {
        fun compare(date1: SemesterDate?, date2: SemesterDate?): Int {
            if (date1!!.year > date2!!.year) {
                return 1
            } else if (date1.year == date2.year) {
                if (date1.semester > date2.semester) {
                    return 1
                } else if (date1.semester == date2.semester) {
                    return 0
                }
                return -1
            }
            return -1
        }
    }
}