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
import javax.swing.JScrollPane
import javax.swing.JPopupMenu
import javax.swing.JMenuItem
import java.awt.event.ActionEvent
import javax.swing.JComboBox
import bme.creditcalc.model.Semester
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
import bme.creditcalc.model.Subject
import java.awt.Color
import java.util.ArrayList

object Window : JFrame("Credit Calculator") {
    val DONE_COLOR = Color(0x368058)

    private val mainPanel: JPanel = JPanel(BorderLayout())
    val leckekonyv = Leckekonyv()
    private var table: JTable? = null
    private var creditLabel: JLabel? = null
    private var averageLabel: JLabel? = null
    private var creditIndexLabel: JLabel? = null

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        contentPane.add(mainPanel, BorderLayout.CENTER)
        initializeWindow()
        isResizable = false
        pack()
        LafManager.install(DarculaTheme())
    }

    private fun initializeWindow() {
        val infoPanel = JPanel()
        averageLabel = JLabel()
        creditIndexLabel = JLabel()
        creditLabel = JLabel()
        infoPanel.add(creditLabel)
        infoPanel.add(averageLabel)
        infoPanel.add(creditIndexLabel)
        mainPanel.add(infoPanel, BorderLayout.SOUTH)
        leckekonyv.addListDataListener(object : ListDataListener {
            override fun intervalAdded(e: ListDataEvent) {
                update()
            }

            override fun intervalRemoved(e: ListDataEvent) {
                update()
            }

            override fun contentsChanged(e: ListDataEvent) {
                update()
            }

            fun update() {
                updateTableModel()
                updateAverages()
            }
        })
        initializeMenu()
        initializeTable()
        initializeTableMenu()
        initializeComboBox()
    }

    private fun initializeTable() {
        table = JTable(null)
        table!!.setDefaultRenderer(String::class.javaObjectType, SemesterTableCellRenderer(table!!.getDefaultRenderer(String::class.java)))
        table!!.setDefaultRenderer(Int::class.javaObjectType, SemesterTableCellRenderer(table!!.getDefaultRenderer(Int::class.java)))
        table!!.setDefaultRenderer(Boolean::class.javaObjectType, SemesterTableCellRenderer(table!!.getDefaultRenderer(Boolean::class.java)))
        table!!.setDefaultRenderer(Double::class.javaObjectType, SemesterTableCellRenderer(table!!.getDefaultRenderer(Double::class.java)))
        val scrollPane = JScrollPane(table)
        mainPanel.add(scrollPane, BorderLayout.CENTER)
        table!!.addPropertyChangeListener { updateAverages() }
    }

    private fun initializeTableMenu() {
        val tableMenu = JPopupMenu()
        val addMenuItem = JMenuItem("Add Subject")
        val deleteMenuItem = JMenuItem("Delete Subject")
        tableMenu.add(addMenuItem)
        tableMenu.add(deleteMenuItem)
        val popupListener = PopupListener(tableMenu)
        table!!.addMouseListener(popupListener)
        addMenuItem.addActionListener { addSubject(table!!.rowAtPoint(popupListener.lastPopupLocation)) }
        deleteMenuItem.addActionListener { deleteSubject(table!!.rowAtPoint(popupListener.lastPopupLocation)) }
    }

    private fun initializeComboBox() {
        val topPanel = JPanel()
        val comboBox = JComboBox(leckekonyv)
        val dim = comboBox.preferredSize
        dim.width = 400
        comboBox.preferredSize = dim
        topPanel.add(comboBox)
        leckekonyv.addSelectedChangedListener { updateTableModel() }
        val newButton = JButton("New")
        topPanel.add(newButton)
        newButton.addActionListener { addSemesterDialog() }
        val deleteButton = JButton("Delete")
        topPanel.add(deleteButton)
        deleteButton.addActionListener { deleteLastSemester() }
        mainPanel.add(topPanel, BorderLayout.NORTH)
    }

    private fun initializeMenu() {
        val menuBar = JMenuBar()
        jMenuBar = menuBar
        val subjectsMenu = JMenu("Subjects")
        menuBar.add(subjectsMenu)
        val addSubjectMenuItem = JMenuItem("Add")
        subjectsMenu.add(addSubjectMenuItem)
        val removeSubjectMenuItem = JMenuItem("Remove")
        subjectsMenu.add(removeSubjectMenuItem)
        addSubjectMenuItem.addActionListener { addNewSubject() }
        removeSubjectMenuItem.addActionListener {
            deleteSubject(JOptionPane.showInputDialog(
                    this,
                    "Which subject to delete (row number):",
                    "Which subject to delete",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    0
            ).toString().toInt())
        }
        val calculateMenu = JMenu("Calculate")
        menuBar.add(calculateMenu)
        val calculateCollagePointsMenuItem = JMenuItem("Collage points")
        calculateMenu.add(calculateCollagePointsMenuItem)
        calculateCollagePointsMenuItem.addActionListener { e: ActionEvent? -> calculateCollageDialog() }
        val advancedCalculatorMenuItem = JMenuItem("Advanced")
        calculateMenu.add(advancedCalculatorMenuItem)
        advancedCalculatorMenuItem.addActionListener { e: ActionEvent? ->
            val dialog = AdvancedCalculator(this)
            dialog.isVisible = true
            dialog.setLocationRelativeTo(this)
        }
    }

    private fun updateTableModel() {
        val selected = leckekonyv.selectedItem as Semester
        table!!.model = selected.view
        updateAverages()
    }

    private fun updateAverages() {
        val selected = leckekonyv.selectedItem as Semester?
        creditLabel!!.text = "Credits: " + (selected?.sumCredit(false, false) ?: 0.0)
        averageLabel!!.text = "Average: " + (selected?.calculateAverage(false, false) ?: Double.NaN)
        creditIndexLabel!!.text = "CreditIndex: " + (selected?.calculateCreditIndex() ?: Double.NaN)
    }

    private fun newEmptySemester() {
        val years = ArrayList<Int>()
        val now = LocalDate.now()
        for (i in now.year - 10 until now.year + 10) {
            years.add(i)
        }
        try {
            val selectedYear = JOptionPane.showInputDialog(
                    this,
                    "Choose year",
                    "Choose year",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    years.toTypedArray(),
                    now.year
            ) as Int
            val options = arrayOf("Winter",
                    "Spring")
            val selectedSemester = JOptionPane.showOptionDialog(
                    this,
                    "Select season",
                    "Select season",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            ) + 1
            if (selectedSemester < 1 || selectedSemester > 2) {
                throw NullPointerException()
            }
            addSemester(Semester(selectedYear, selectedSemester))
        } catch (ignored: NullPointerException) {
        }
    }

    private fun deleteLastSemester() {
        leckekonyv.selectedItem?.let { leckekonyv.removeElement(it) }
    }

    fun addSemester(semester: Semester?) {
        val semesterTable = SemesterTable()
        semester!!.attachView(semesterTable)
        semesterTable.setModel(semester)
        leckekonyv.addElement(semester)
    }

    private fun loadSemester() {
        val fc = JFileChooser(System.getProperty("user.dir"))
        fc.fileFilter = XLSXFileFilter()
        //fc.changeToParentDirectory();
        val returnVal = fc.showOpenDialog(this)
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            val file = fc.selectedFile
            NeptunReader(file, leckekonyv).execute()
        }
        //updateTableModel();
    }

    private fun addNewSubject() {
        val currentSemester = leckekonyv.selectedItem as Semester
        currentSemester?.addSubject(Subject("New Subject", 0.0, 1))
    }

    private fun addSubject(row: Int) {
        val currentSemester = leckekonyv.selectedItem as Semester
        currentSemester?.addSubjectAt(row, Subject("New Subject", 0.0, 1))
    }

    private fun deleteSubject(row: Int) {
        val currentSemester = leckekonyv.selectedItem as Semester
        currentSemester?.removeSubjectAt(row)
    }

    private fun calculateCollageDialog() {
        var tk = 4.0
        var plusPoints = 0.0
        try {
            tk = JOptionPane.showInputDialog(
                    this,
                    "Enter 'tanulmányilag kiemelt' value:",
                    "Enter 'tanulmányilag kiemelt' value:",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    tk
            ).toString().toDouble()
            plusPoints = JOptionPane.showInputDialog(
                    this,
                    "Enter plus points:",
                    "Enter plus points (if you have any):",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    plusPoints
            ).toString().toDouble()
            val points = leckekonyv.collagePoints(tk, plusPoints)
            JOptionPane.showMessageDialog(this, "Your collage points: $points")
        } catch (ignored: Exception) {
        }
    }

    private fun addSemesterDialog() {
        val options = arrayOf("New", "Import Neptun XLSX", "Cancel")
        val result: Int = JOptionPane.showOptionDialog(
                this,
                "Please select from the options below",
                "Creating new Semester",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[2]
        )
        when (result) {
            0 -> newEmptySemester()
            1 -> loadSemester()
        }
    }
}