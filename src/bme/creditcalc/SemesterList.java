package bme.creditcalc;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SemesterList implements MutableComboBoxModel<Semester> {
    ArrayList<Semester> semesters;
    ArrayList<ListDataListener> listeners;
    ArrayList<ActionListener> selectedChangeListeners;
    Semester selected;

    public SemesterList() {
        this.semesters = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.selectedChangeListeners = new ArrayList<>();
    }

    @Override
    public void setSelectedItem(Object anItem) {
        if(anItem instanceof Semester && semesters.contains(anItem)){
            selected = (Semester) anItem;
        }
    }

    @Override
    public Object getSelectedItem() {
        return selected;
    }

    @Override
    public int getSize() {
        return semesters.size();
    }

    @Override
    public Semester getElementAt(int index) {
        return semesters.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

    public void addSelectedChangedListener(ActionListener l) {
        selectedChangeListeners.add(l);
    }

    public void removeSelectedChangedListener(ActionListener l) {
        selectedChangeListeners.remove(l);
    }

    private void notifySelectedChangeListeners(){
        selectedChangeListeners.forEach(e->e.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "selectedChange")));
    }

    @Override
    public void addElement(Semester item) {
        semesters.add(item);
        if(semesters.contains(item)){
            listeners.forEach(e-> e.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, semesters.indexOf(item), semesters.indexOf(item))));
            selected = item;
            notifySelectedChangeListeners();
        }
    }

    @Override
    public void removeElement(Object obj) {
        if(obj instanceof Semester){
            semesters.remove(obj);
            listeners.forEach(e-> e.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, semesters.indexOf(obj), semesters.indexOf(obj))));
        }
        if(!semesters.contains(selected)){
            selected = null;
            notifySelectedChangeListeners();
        }
    }

    @Override
    public void insertElementAt(Semester item, int index) {
        semesters.add(index, item);
        if(semesters.contains(item)) {
            listeners.forEach(e -> e.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index, index)));
            selected = item;
            notifySelectedChangeListeners();
        }
    }

    @Override
    public void removeElementAt(int index) {
        semesters.remove(index);
        listeners.forEach(e-> e.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index, index)));
        if(!semesters.contains(selected)){
            selected = null;
            notifySelectedChangeListeners();
        }
    }
    public Semester[] toArray(){
        return semesters.toArray(new Semester[0]);
    }
}
