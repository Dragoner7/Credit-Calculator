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

class Semester(year: Int, semester: Int) {
    private val subjects = ArrayList<Subject>()
    var view: AbstractTableModel? = null
        private set
    var date: SemesterDate

    init {
        date = SemesterDate(year, semester)
    }

    fun addSubject(s: Subject) {
        subjects.add(s)
        if (view != null) {
            view!!.fireTableRowsInserted(subjects.indexOf(s), subjects.indexOf(s))
        }
    }

    fun removeSubject(s: Subject) {
        subjects.remove(s)
        if (view != null) {
            view!!.fireTableRowsDeleted(subjects.indexOf(s), subjects.indexOf(s))
        }
    }

    fun removeSubjectAt(row: Int) {
        subjects.removeAt(row)
        if (view != null) {
            view!!.fireTableRowsDeleted(row, row)
        }
    }

    fun addSubjectAt(row: Int, s: Subject) {
        if (row > subjects.size || row < 0) {
            addSubject(s)
            return
        }
        subjects.add(row, s)
        if (view != null) {
            view!!.fireTableRowsInserted(row, row)
        }
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val semester = o as Semester
        return date == semester.date
    }

    override fun hashCode(): Int {
        return Objects.hash(date)
    }

    fun getSubject(indx: Int): Subject {
        return subjects[indx]
    }

    fun getSubjects(): List<Subject?> {
        return subjects.clone() as List<Subject?>
    }

    fun attachView(view: AbstractTableModel?) {
        this.view = view
    }

    fun detachView() {
        view = null
    }

    fun calculateAverage(mintaOnly: Boolean, finalizedOnly: Boolean): Double {
        return sumGradeCredit(mintaOnly, finalizedOnly) / sumCredit(mintaOnly, finalizedOnly)
    }

    fun calculateCreditIndex(): Double {
        var sumGradeTimesCredit = 0.0
        for (s in subjects) {
            sumGradeTimesCredit += s.credit * s.grade
        }
        return sumGradeTimesCredit / 30
    }

    fun sumGradeCredit(mintaOnly: Boolean, finalizedOnly: Boolean): Double {
        var sumGrade = 0.0
        for (s in subjects) {
            if ((!mintaOnly || s.minta) && (!finalizedOnly || s.isFinalized)) {
                sumGrade += s.grade * s.credit
            }
        }
        return sumGrade
    }

    fun sumCredit(mintaOnly: Boolean, finalizedOnly: Boolean): Double {
        var sumCredit = 0.0
        for (s in subjects) {
            if ((!mintaOnly || s.minta) && (!finalizedOnly || s.isFinalized)) {
                sumCredit += s.credit
            }
        }
        return sumCredit
    }

    override fun toString(): String {
        return date.toString()
    }

    companion object {
        fun creditIndexAverages(semesters: Array<Semester?>): Double {
            var result = 0.0
            for (i in semesters.indices) {
                if (semesters[i] == null) {
                    return Double.NaN
                }
                result += semesters[i]!!.calculateCreditIndex()
            }
            return result / semesters.size
        } /*public static Semester[] find2MostRecentSemesters(Semester[] semesters) {
        Semester[] result = new Semester[2];
        for (int i = 0; i < semesters.length; ++i) {
            if (result[0] == null || SemesterDate.compare(semesters[i].getDate(), result[0].getDate()) > 0) {
                result[1] = result[0];
                result[0] = semesters[i];
            } else if (result[1] == null || SemesterDate.compare(semesters[i].getDate(), result[1].getDate()) > 0) {
                result[1] = semesters[i];
            }
        }
        return result;
    }*/
    }
}