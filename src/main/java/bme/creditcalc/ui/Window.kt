package bme.creditcalc.ui

import bme.creditcalc.model.Leckekonyv
import bme.creditcalc.model.Semester
import bme.creditcalc.model.Subject
import bme.creditcalc.neptunreader.NeptunReader
import bme.creditcalc.neptunreader.XLSXFileFilter
import com.github.weisj.darklaf.LafManager
import com.github.weisj.darklaf.theme.DarculaTheme
import java.awt.BorderLayout
import java.awt.Color
import javax.swing.*
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener

object Window : JFrame("Credit Calculator") {
    val DONE_COLOR = Color(0x368058)

    val leckekonyv = Leckekonyv()

    private val mainPanel: JPanel = JPanel(BorderLayout())

    private var leckekonyvComboBox = LeckekonyvComboBox(leckekonyv)
    private var leckekonyvTable : LeckekonyvTable = LeckekonyvTable(leckekonyv)
    private var semesterAverageView = SemesterAverageView(leckekonyvComboBox.selectedItem)

    init {
        initializeSwing()
    }

    private fun initializeSwing() {
        defaultCloseOperation = EXIT_ON_CLOSE
        contentPane.add(mainPanel, BorderLayout.CENTER)
        initializeContent()
        isResizable = false
        pack()
        LafManager.install(DarculaTheme())
    }

    private fun initializeContent() {
        createBottomPanel()
        createMenuBar()
        initializeTable()
        initializeComboBox()
    }

    private fun initializeTable() {
        mainPanel.add(leckekonyvTable.view, BorderLayout.CENTER)
        leckekonyvTable.addSubjectListeners.add{ e->addSubject(e.actionCommand.toInt())}
        leckekonyvTable.removeSubjectListeners.add{ e-> removeSubject(e.actionCommand.toInt()) }
    }

    private fun initializeComboBox() {
        createTopPanel()
        leckekonyvComboBox.addListDataListener(WindowListDataListener())
    }

    private fun createBottomPanel() {
        mainPanel.add(semesterAverageView.view, BorderLayout.SOUTH)
    }

    private fun createTopPanel() {
        val topPanel = JPanel()
        val comboBox = JComboBox(leckekonyvComboBox)
        val dim = comboBox.preferredSize
        dim.width = 400
        comboBox.preferredSize = dim
        topPanel.add(comboBox)
        val newButton = JButton("New")
        topPanel.add(newButton)
        newButton.addActionListener {
            chooseSemesterSource()
        }
        val deleteButton = JButton("Delete")
        topPanel.add(deleteButton)
        deleteButton.addActionListener { removeLastSemester() }
        mainPanel.add(topPanel, BorderLayout.NORTH)
    }

    private fun chooseSemesterSource() {
        when (semesterSourceDialog(this)) {
            0 -> userCreateEmptySemester()
            1 -> userImportSemester()
        }
    }

    private fun createMenuBar() {
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
            removeSubject(removeSubjectDialog(this))
        }
        val calculateMenu = JMenu("Calculate")
        menuBar.add(calculateMenu)
        val calculateCollagePointsMenuItem = JMenuItem("Collage points")
        calculateMenu.add(calculateCollagePointsMenuItem)
        calculateCollagePointsMenuItem.addActionListener { promptCollagePointsDialogs() }
        val advancedCalculatorMenuItem = JMenuItem("Advanced")
        calculateMenu.add(advancedCalculatorMenuItem)
        advancedCalculatorMenuItem.addActionListener {
            val dialog = AdvancedCalculator(this)
            dialog.isVisible = true
            dialog.setLocationRelativeTo(this)
        }
    }

    private fun onSemesterChange() {
        leckekonyvTable.setSemester(leckekonyvComboBox.selectedItem)
        semesterAverageView.setSemester(leckekonyvComboBox.selectedItem)
    }

    fun addSemester(semester: Semester) {
        leckekonyvComboBox.addElement(semester)
    }

    private fun removeLastSemester() {
        leckekonyvComboBox.selectedItem?.let { leckekonyvComboBox.removeElement(it) }
    }

    private fun userCreateEmptySemester() {
        try {
            val selectedYear = yearDialog(this)
            val selectedSemester = semesterDialog(this)
            if (selectedSemester < 1 || selectedSemester > 2) {
                throw NullPointerException()
            }
            addSemester(Semester(selectedYear, selectedSemester))
        } catch (ignored: NullPointerException) {
        }
    }

    private fun userImportSemester() {
        val fc = JFileChooser(System.getProperty("user.dir"))
        fc.fileFilter = XLSXFileFilter()
        val returnVal = fc.showOpenDialog(this)
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            val file = fc.selectedFile
            NeptunReader(file, leckekonyv).execute()
        }
    }

    private fun addNewSubject() {
        val currentSemester = leckekonyvComboBox.selectedItem
        currentSemester?.addSubject(Subject("New Subject", 0.0, 1))
    }

    private fun addSubject(row: Int) {
        val currentSemester = leckekonyvComboBox.selectedItem
        currentSemester?.addSubjectAt(row, Subject("New Subject", 0.0, 1))
    }

    private fun removeSubject(row: Int) {
        val currentSemester = leckekonyvComboBox.selectedItem
        currentSemester?.removeSubjectAt(row)
    }

    private fun promptCollagePointsDialogs() {
        try {
            val tk = tkDialog(this)
            val plusPoints = plusPointsDialog(this)
            val points = leckekonyv.collagePoints(tk, plusPoints)
            collagePointsResultDialog(this,points)
        } catch (ignored: Exception) {
        }
    }

    class WindowListDataListener : ListDataListener {
        override fun intervalAdded(e: ListDataEvent) {
            onSemesterChange()
        }

        override fun intervalRemoved(e: ListDataEvent) {
            onSemesterChange()
        }

        override fun contentsChanged(e: ListDataEvent) {
            onSemesterChange()
        }
    }
}