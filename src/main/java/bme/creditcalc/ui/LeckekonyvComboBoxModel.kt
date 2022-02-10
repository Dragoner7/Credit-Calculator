package bme.creditcalc.ui

import bme.creditcalc.model.Leckekonyv
import bme.creditcalc.model.Semester
import javax.swing.MutableComboBoxModel
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener

class LeckekonyvComboBoxModel(leckekonyv: Leckekonyv) : MutableComboBoxModel<Semester> {
    private var listeners = mutableListOf<ListDataListener>()
    private var selected: Semester? = null

    var leckekonyv = leckekonyv
    set(value) {
        field= value
        listeners.forEach { e->e.contentsChanged(ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED,0, value.semesters.size)) }
    }

    override fun setSelectedItem(anItem: Any) {
        if (anItem is Semester && leckekonyv.semesters.contains(anItem)) {
            selected = anItem
            listeners.forEach { e->e.contentsChanged(ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED,leckekonyv.semesters.indexOf(selected), leckekonyv.semesters.indexOf(selected))) }
        }
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

    override fun addElement(item: Semester) {
        leckekonyv.semesters.add(item)
        if (leckekonyv.semesters.contains(item)) {
            setSelectedItem(item)
            listeners.forEach{e -> e.intervalAdded(ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, leckekonyv.semesters.indexOf(item), leckekonyv.semesters.indexOf(item))) }
        }
    }

    override fun removeElement(obj: Any) {
        if (obj is Semester) {
            val index = leckekonyv.semesters.indexOf(obj)
            leckekonyv.semesters.remove(obj)
            if (!leckekonyv.semesters.contains(selected)) {
                selected = null
                listeners.forEach{e -> e.intervalAdded(ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index, index))}
            }
        }
    }

    override fun insertElementAt(item: Semester, index: Int) {
        leckekonyv.semesters.add(index, item)
        if (leckekonyv.semesters.contains(item)) {
            selected = item
            listeners.forEach{ e -> e.intervalAdded(ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index, index))}
        }
    }

    override fun removeElementAt(index: Int) {
        leckekonyv.semesters.removeAt(index)
        if (!leckekonyv.semesters.contains(selected)) {
            selected = null
            listeners.forEach{e -> e.intervalAdded(ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index, index)) }
        }
    }
}