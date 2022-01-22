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

data class Subject(var name: String, var credit: Double) {
    var grade = 0
        set(value) {
            if (value > 5) {
                field = 5
                return
            } else if (value < 0) {
                field = 0
                return
            }
            field = value
        }
    var minta = true

    constructor(name: String, credit: Double, grade: Int) : this(name, credit) {
        this.grade = grade
    }

    constructor(name: String, credit: Double, grade: Int, minta: Boolean) : this(name, credit, grade) {
        this.minta = minta
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val subject = o as Subject
        return subject.credit.compareTo(credit) == 0 && grade == subject.grade && name == subject.name
    }

    override fun hashCode(): Int {
        return Objects.hash(name, credit, grade)
    }

    val isFinalized: Boolean get() = grade > 1

}