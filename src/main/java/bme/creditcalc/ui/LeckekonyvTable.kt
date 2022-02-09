package bme.creditcalc.ui

import bme.creditcalc.model.Leckekonyv
import bme.creditcalc.model.Semester
import java.awt.Point
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.table.DefaultTableModel

class LeckekonyvTable(val leckekonyv: Leckekonyv) {
    private var table = JTable(null)
    val view = JScrollPane(table)
    private val tableMenu = JPopupMenu()
    private val popupListener = PopupListener(tableMenu)

    val addSubjectListeners : MutableList<ActionListener> = mutableListOf()
    val removeSubjectListeners : MutableList<ActionListener> = mutableListOf()

    init {
        initializeRenderers()
        initializeTableMenu()
        initializePopups()
    }

    private fun initializeRenderers(){
        table.setDefaultRenderer(String::class.javaObjectType, SemesterTableCellRenderer(table.getDefaultRenderer(String::class.java)))
        table.setDefaultRenderer(Int::class.javaObjectType, SemesterTableCellRenderer(table.getDefaultRenderer(Int::class.java)))
        table.setDefaultRenderer(Boolean::class.javaObjectType, SemesterTableCellRenderer(table.getDefaultRenderer(Boolean::class.java)))
        table.setDefaultRenderer(Double::class.javaObjectType, SemesterTableCellRenderer(table.getDefaultRenderer(Double::class.java)))
    }

    private fun initializeTableMenu() {
        val addMenuItem = JMenuItem("Add Subject")
        val deleteMenuItem = JMenuItem("Delete Subject")
        tableMenu.add(addMenuItem)
        tableMenu.add(deleteMenuItem)
        addMenuItem.addActionListener { popUpAddSubject() }
        deleteMenuItem.addActionListener { popUpRemoveSubject() }
    }

    private fun initializePopups(){
        table.addMouseListener(popupListener)
    }

    private fun rowAtPoint(): Int {
        return table.rowAtPoint(popupListener.lastPopupLocation ?: Point(0,0))
    }

    private fun popUpAddSubject(){
        addSubjectListeners.forEach { e->e.actionPerformed(ActionEvent(this, ActionEvent.ACTION_PERFORMED, rowAtPoint().toString())) }
    }

    private fun popUpRemoveSubject(){
        removeSubjectListeners.forEach { e->e.actionPerformed(ActionEvent(this, ActionEvent.ACTION_PERFORMED, rowAtPoint().toString())) }
    }

    fun setSemester(semester: Semester?){
        if(semester != null && leckekonyv.semesters.contains(semester)){
            table.model = semester.view
        } else {
            table.model = DefaultTableModel()
        }
    }
}