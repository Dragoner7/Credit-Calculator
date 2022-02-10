package bme.creditcalc.ui

import bme.creditcalc.io.LoadWorker
import bme.creditcalc.model.Leckekonyv
import bme.creditcalc.model.Semester
import bme.creditcalc.model.Subject
import bme.creditcalc.io.NeptunReader
import bme.creditcalc.io.SaveWorker
import bme.creditcalc.io.ExtensionBasedFileFilter
import java.awt.BorderLayout
import java.awt.Color
import java.io.File
import javax.swing.*
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener


object Window : JFrame("Credit Calculator") {
    val DONE_COLOR = Color(0x368058)

    var leckekonyv = Leckekonyv()

    private val mainPanel: JPanel = JPanel(BorderLayout())

    private var leckekonyvComboBox = LeckekonyvComboBox(leckekonyv)
    private var leckekonyvTable : LeckekonyvTable = LeckekonyvTable(leckekonyv)
    private var semesterAverageView = SemesterAverageView(leckekonyvComboBox.model.selectedItem)

    private var windowListDataListener = WindowListDataListener()

    init {
        initializeSwing()
    }

    fun switchLeckekonyv(leckekonyv: Leckekonyv){
        this.leckekonyv = leckekonyv
        this.leckekonyvTable.leckekonyv = leckekonyv
        this.leckekonyvComboBox.model.leckekonyv = leckekonyv
        leckekonyvComboBox.model.setSelectedItem(leckekonyv.semesters[0])
        onSemesterChange()
    }

    private fun initializeSwing() {
        defaultCloseOperation = EXIT_ON_CLOSE
        contentPane.add(mainPanel, BorderLayout.CENTER)
        initializeContent()
        isResizable = false
        pack()
    }

    private fun initializeContent() {
        initializeTopPanel()
        initializeComboBox()
        initializeTable()
        initializeBottomPanel()
        createMenuBar()
    }

    private fun initializeTable() {
        mainPanel.add(leckekonyvTable.view, BorderLayout.CENTER)
        leckekonyvTable.addSubjectListeners.add{ e->addSubject(e.actionCommand.toInt())}
        leckekonyvTable.removeSubjectListeners.add{ e-> removeSubject(e.actionCommand.toInt()) }
    }

    private fun initializeComboBox() {
        leckekonyvComboBox.model.addListDataListener(windowListDataListener)
    }

    private fun initializeBottomPanel() {
        mainPanel.add(semesterAverageView.view, BorderLayout.SOUTH)
    }

    private fun initializeTopPanel() {
        leckekonyvComboBox.newSemesterListener.add{
            chooseSemesterSource()
        }
        leckekonyvComboBox.deleteSemesterListener.add{
            removeLastSemester()
        }
        mainPanel.add(leckekonyvComboBox.view, BorderLayout.NORTH)
    }

    private fun chooseSemesterSource() {
        when (semesterSourceDialog(this)) {
            0 -> promptEmptySemesterCreationDialogs()
            1 -> promptAndImportNeptunXLSX()
        }
    }

    private fun createMenuBar() {
        val menuBar = JMenuBar()
        jMenuBar = menuBar
        val fileMenu = JMenu("File")
        menuBar.add(fileMenu)
        val saveMenuItem = JMenuItem("Save")
        fileMenu.add(saveMenuItem)
        val loadMenuItem = JMenuItem("Load")
        fileMenu.add(loadMenuItem)

        saveMenuItem.addActionListener {
            promptAndSaveJSON()
        }

        loadMenuItem.addActionListener {
            promptAndLoadJSON()
        }

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
        leckekonyvTable.setSemester(leckekonyvComboBox.model.selectedItem)
        semesterAverageView.setSemester(leckekonyvComboBox.model.selectedItem)
    }

    fun addSemester(semester: Semester) {
        leckekonyvComboBox.model.addElement(semester)
    }

    private fun removeLastSemester() {
        leckekonyvComboBox.model.selectedItem?.let { leckekonyvComboBox.model.removeElement(it) }
    }

    private fun promptEmptySemesterCreationDialogs() {
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

    private fun promptAndImportNeptunXLSX() {
        val fc = JFileChooser(System.getProperty("user.dir"))
        fc.fileFilter = ExtensionBasedFileFilter(".xlsx")
        val returnVal = fc.showOpenDialog(this)
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            val file = fc.selectedFile
            NeptunReader(file, leckekonyv).execute()
        }
    }

    private fun promptAndLoadJSON() {
        val fc = JFileChooser(System.getProperty("user.dir"))
        fc.fileFilter = ExtensionBasedFileFilter(".json")
        val returnVal = fc.showOpenDialog(this)
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            val file = fc.selectedFile
            LoadWorker(file).execute()
        }
    }

    private fun promptAndSaveJSON() {
        val fc = JFileChooser(System.getProperty("user.dir"))
        val filter = ExtensionBasedFileFilter(".json")
        fc.fileFilter = filter
        val returnVal = fc.showSaveDialog(this)
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            var file = fc.selectedFile
            if(!filter.accept(file)){
                file = File(file.path + ".json")
            }
            SaveWorker(file, leckekonyv).execute()
        }
    }

    private fun addNewSubject() {
        val currentSemester = leckekonyvComboBox.model.selectedItem
        currentSemester?.addSubject(Subject("New Subject", 0.0, 1))
    }

    private fun addSubject(row: Int) {
        val currentSemester = leckekonyvComboBox.model.selectedItem
        currentSemester?.addSubjectAt(row, Subject("New Subject", 0.0, 1))
    }

    private fun removeSubject(row: Int) {
        val currentSemester = leckekonyvComboBox.model.selectedItem
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