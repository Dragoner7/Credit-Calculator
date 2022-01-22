package bme.creditcalc.neptunreader

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
import javax.swing.JScrollPane
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeEvent
import javax.swing.JPopupMenu
import javax.swing.JMenuItem
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import javax.swing.JComboBox
import bme.creditcalc.model.Semester
import java.awt.Dimension
import javax.swing.JButton
import javax.swing.JMenuBar
import javax.swing.JMenu
import javax.swing.JOptionPane
import javax.swing.table.DefaultTableModel
import java.time.LocalDate
import java.lang.NullPointerException
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
import bme.creditcalc.model.Subject
import bme.creditcalc.ui.*
import org.apache.poi.ss.usermodel.Row
import javax.swing.MutableComboBoxModel
import java.util.function.Consumer
import kotlin.jvm.JvmStatic
import javax.swing.SwingWorker
import kotlin.Throws
import java.io.IOException
import java.io.FileInputStream
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFSheet
import java.io.File

class NeptunReader(var file: File, var model: Leckekonyv) : SwingWorker<Semester?, Any?>() {
    @Throws(IOException::class)
    private fun readXLSX(): Semester? {
        if (!XLSXFileFilter().accept(file)) {
            return null
        }
        val fis = FileInputStream(file)
        val workbook = XSSFWorkbook(fis)
        val sheet = workbook.getSheetAt(0)
        val date = workbook.getSheetName(0)
        val rowIt: Iterator<Row> = sheet.iterator()
        if (!basicFormatCheck(rowIt.next())) {
            return null
        }
        val result = Semester(date.substring(date.length - 7, date.length - 3).toInt(), date.substring(date.length - 1).toInt())
        for (i in 0 until model.size) {
            if (result == model.getElementAt(i)) {
                workbook.close()
                fis.close()
                return null
            }
        }
        while (rowIt.hasNext()) {
            val row = rowIt.next()
            val subject = Subject(row.getCell(1).toString(), row.getCell(2).toString().toInt().toDouble(), findGrade(row.getCell(7).toString()))
            result.addSubject(subject)
        }
        workbook.close()
        fis.close()
        return result
    }

    @Throws(Exception::class)
    override fun doInBackground(): Semester? {
        return readXLSX()
    }

    override fun done() {
        try {
            if (get() == null) {
                return
            }
            Window.addSemester(get())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun basicFormatCheck(header: Row): Boolean {
        return header.getCell(1).toString().contains("Tárgy") &&
                header.getCell(2).toString().contains("Kr.") &&
                header.getCell(7).toString().contains("Jegyek")
    }

    companion object {
        private fun findGrade(string: String): Int {
            val grades = IntArray(6)
            grades[0] = -1
            grades[1] = string.indexOf("Elégtelen")
            grades[2] = string.indexOf("Elégséges")
            grades[3] = string.indexOf("Közepes")
            grades[4] = string.indexOf("Jó")
            grades[5] = string.indexOf("Jeles")
            var highest = 0
            for (i in 0..5) {
                if (grades[i] > grades[highest]) {
                    highest = i
                }
            }
            return highest
        }
    }
}