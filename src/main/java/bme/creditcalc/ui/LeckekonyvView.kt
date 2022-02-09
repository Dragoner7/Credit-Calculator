package bme.creditcalc.ui

import bme.creditcalc.model.Leckekonyv
import bme.creditcalc.model.Semester
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.MutableComboBoxModel
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener

class LeckekonyvView(var leckekonyv: Leckekonyv) : MutableComboBoxModel<Semester> {
    private var listeners = mutableListOf<ListDataListener>()
    private var selectedChangeListeners = mutableListOf<ActionListener>()
    private var selected: Semester? = null

    override fun setSelectedItem(anItem: Any) {
        if (anItem is Semester && leckekonyv.semesters.contains(anItem)) {
            selected = anItem
        }
        notifySelectedChangeListeners()
    }

    override fun getSelectedItem(): Semester? {
        return selected
    }

    override fun getSize(): Int {
        return leckekonyv.semesters.size
    }

    override fun getElementAt(index: Int): Semester {
        return leckekonyv.semesters[index]
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
        selectedChangeListeners.forEach{ e -> e.actionPerformed(ActionEvent(this, ActionEvent.ACTION_PERFORMED, "selectedChange")) }
    }

    override fun addElement(item: Semester) {
        leckekonyv.semesters.add(item)
        if (leckekonyv.semesters.contains(item)) {
            selected = item
            listeners.forEach{e -> e.intervalAdded(ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, leckekonyv.semesters.indexOf(item), leckekonyv.semesters.indexOf(item))) }
        }
    }

    override fun removeElement(obj: Any) {
        if (obj is Semester) {
            val index = leckekonyv.semesters.indexOf(obj)
            leckekonyv.semesters.remove(obj)
            listeners.forEach{e -> e.intervalAdded(ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index, index))}
        }
        if (!leckekonyv.semesters.contains(selected)) {
            selected = null
            notifySelectedChangeListeners()
        }
    }

    override fun insertElementAt(item: Semester, index: Int) {
        leckekonyv.semesters.add(index, item)
        if (leckekonyv.semesters.contains(item)) {
            listeners.forEach{ e -> e.intervalAdded(ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index, index))}
            selected = item
            notifySelectedChangeListeners()
        }
    }

    override fun removeElementAt(index: Int) {
        leckekonyv.semesters.removeAt(index)
        listeners.forEach{e -> e.intervalAdded(ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index, index)) }
        if (!leckekonyv.semesters.contains(selected)) {
            selected = null
            notifySelectedChangeListeners()
        }
    }
}