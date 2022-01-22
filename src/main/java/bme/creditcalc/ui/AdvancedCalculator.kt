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

class AdvancedCalculator(parent: JFrame?) : JDialog(parent) {
    private var average: JLabel
    private var creditsCounted: JLabel
    private var mintaOnly: JCheckBox
    private var acquiredOnly: JCheckBox
    private var customDiv: JRadioButton
    var div: JTextField
    private var recentOnly: JRadioButton
    private var recentN: JTextField

    init {
        minimumSize = Dimension(500, 250)
        isResizable = false
        layout = BoxLayout(this.contentPane, BoxLayout.Y_AXIS)
        average = JLabel()
        creditsCounted = JLabel()
        val mintaPanel = JPanel()
        mintaOnly = JCheckBox("Minta only")
        mintaPanel.add(mintaOnly)
        add(mintaPanel)
        val acquiredPanel = JPanel()
        acquiredOnly = JCheckBox("Acquired only")
        acquiredPanel.add(acquiredOnly)
        add(acquiredPanel)
        val semestersPanel = JPanel()
        val allRB = JRadioButton("All Semesters")
        allRB.isSelected = true
        semestersPanel.add(allRB)
        recentOnly = JRadioButton("Recents")
        recentOnly.addActionListener { allRB.isSelected = false }
        allRB.addActionListener { recentOnly.isSelected = false }
        semestersPanel.add(recentOnly)
        recentN = JTextField()
        recentN.preferredSize = Dimension(25, 25)
        semestersPanel.add(recentN)
        semestersPanel.add(JLabel("only"))
        add(semestersPanel)
        val customDivPanel = JPanel()
        val allCreditDiv = JRadioButton("Auto divider")
        allCreditDiv.isSelected = true
        customDivPanel.add(allCreditDiv)
        customDiv = JRadioButton("Custom divider:")
        customDiv.addActionListener { allCreditDiv.isSelected = false }
        allCreditDiv.addActionListener { customDiv.isSelected = false }
        customDivPanel.add(customDiv)
        div = JTextField()
        div.preferredSize = Dimension(25, 25)
        customDivPanel.add(div)
        customDivPanel.add(JLabel("credits"))
        add(customDivPanel)
        val calculateButton = JButton("Calculate")
        calculateButton.addActionListener { updateAverage() }
        add(calculateButton)
        val averagePanel = JPanel()
        averagePanel.add(calculateButton)
        averagePanel.add(JLabel("Average:"))
        averagePanel.add(average)
        averagePanel.add(JLabel("Credits counted:"))
        averagePanel.add(creditsCounted)
        add(averagePanel)
        title = "Advanced Calculator"
        pack()
    }

    fun updateAverage() {
        val semesters: Array<Semester?>
        var n: Int = Window.leckekonyv.size
        if (recentOnly.isSelected) {
            n = recentN.text.toInt()
        }
        semesters = Window.leckekonyv.findMostRecent(n)
        val sumCreditGrade: Double = Leckekonyv.sumCreditGrade(semesters, mintaOnly.isSelected, acquiredOnly.isSelected)
        val credits: Double = if (customDiv.isSelected) {
            div.text.toDouble()
        } else {
            Leckekonyv.sumCredit(semesters, mintaOnly.isSelected, acquiredOnly.isSelected)
        }
        average.text = (sumCreditGrade / credits).toString()
        creditsCounted.text = credits.toString()
    }
}