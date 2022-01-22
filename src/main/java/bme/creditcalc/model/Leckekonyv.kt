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
import java.util.ArrayList

class Leckekonyv : MutableComboBoxModel<Semester> {
    var semesters = mutableListOf<Semester>()
    private var listeners = mutableListOf<ListDataListener>()
    private var selectedChangeListeners = mutableListOf<ActionListener>()
    private var selected: Semester? = null

    override fun setSelectedItem(anItem: Any) {
        if (anItem is Semester && semesters.contains(anItem)) {
            selected = anItem
        }
        notifySelectedChangeListeners()
    }

    override fun getSelectedItem(): Any? {
        return selected
    }

    override fun getSize(): Int {
        return semesters.size
    }

    override fun getElementAt(index: Int): Semester? {
        return semesters[index]
    }

    override fun addListDataListener(l: ListDataListener) {
        listeners.add(l)
    }

    override fun removeListDataListener(l: ListDataListener) {
        listeners.remove(l)
    }

    fun addSelectedChangedListener(l: ActionListener) {
        selectedChangeListeners.add(l)
    }

    fun removeSelectedChangedListener(l: ActionListener) {
        selectedChangeListeners.remove(l)
    }

    private fun notifySelectedChangeListeners() {
        selectedChangeListeners.forEach(Consumer { e: ActionListener -> e.actionPerformed(ActionEvent(this, ActionEvent.ACTION_PERFORMED, "selectedChange")) })
    }

    override fun addElement(item: Semester) {
        semesters.add(item)
        if (semesters.contains(item)) {
            selected = item
            listeners.forEach(Consumer { e: ListDataListener -> e.intervalAdded(ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, semesters.indexOf(item), semesters.indexOf(item))) })
        }
    }

    override fun removeElement(obj: Any) {
        if (obj is Semester) {
            val indx = semesters.indexOf(obj)
            semesters.remove(obj)
            listeners.forEach(Consumer { e: ListDataListener -> e.intervalAdded(ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, indx, indx)) })
        }
        if (!semesters.contains(selected)) {
            selected = null
            notifySelectedChangeListeners()
        }
    }

    override fun insertElementAt(item: Semester, index: Int) {
        semesters.add(index, item)
        if (semesters.contains(item)) {
            listeners.forEach(Consumer { e: ListDataListener -> e.intervalAdded(ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index, index)) })
            selected = item
            notifySelectedChangeListeners()
        }
    }

    override fun removeElementAt(index: Int) {
        semesters.removeAt(index)
        listeners.forEach(Consumer { e: ListDataListener -> e.intervalAdded(ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index, index)) })
        if (!semesters.contains(selected)) {
            selected = null
            notifySelectedChangeListeners()
        }
    }

    fun toArray(): Array<Semester?> {
        return semesters.toTypedArray()
    }

    fun findMostRecent(n: Int): Array<Semester?> {
        val sorted = semesters.subList(0, semesters.size)
        sorted.sortWith { e1: Semester, e2: Semester -> SemesterDate.compare(e2.date, e1.date) }
        return sorted.subList(0, n).toTypedArray()
    }

    fun collagePoints(tk: Double, plusPoints: Double): Double {
        val recent2 = findMostRecent(2)
        val a: Double = Semester.creditIndexAverages(recent2)
        return (a - 2.0) / ((tk - 2.0) / 100) + plusPoints
    }

    companion object {
        fun sumCreditGrade(semesters: Array<Semester?>, mintaOnly: Boolean, finalizedOnly: Boolean): Double {
            var sumCreditGrade = 0.0
            for (s in semesters) {
                sumCreditGrade += s!!.sumGradeCredit(mintaOnly, finalizedOnly)
            }
            return sumCreditGrade
        }

        fun sumCredit(semesters: Array<Semester?>, mintaOnly: Boolean, finalizedOnly: Boolean): Double {
            var sumCredit = 0.0
            for (s in semesters) {
                sumCredit += s!!.sumCredit(mintaOnly, finalizedOnly)
            }
            return sumCredit
        }
    }
}