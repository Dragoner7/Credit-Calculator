package bme.creditcalc.model;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Leckekonyv implements MutableComboBoxModel<Semester> {
    ArrayList<Semester> semesters;
    ArrayList<ListDataListener> listeners;
    ArrayList<ActionListener> selectedChangeListeners;
    Semester selected;

    public Leckekonyv() {
        this.semesters = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.selectedChangeListeners = new ArrayList<>();
    }

    @Override
    public void setSelectedItem(Object anItem) {
        if(anItem instanceof Semester && semesters.contains(anItem)){
            selected = (Semester) anItem;
        }
        notifySelectedChangeListeners();
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
            setSelectedItem(item);
            listeners.forEach(e-> e.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, semesters.indexOf(item), semesters.indexOf(item))));
        }
    }

    @Override
    public void removeElement(Object obj) {
        if(obj instanceof Semester){
            int indx = semesters.indexOf(obj);
            semesters.remove(obj);
            listeners.forEach(e-> e.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, indx, indx)));
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

    public Semester[] findMostRecent(int n){
        List<Semester> sorted = (List<Semester>) semesters.subList(0, semesters.size());
        sorted.sort((e1, e2)-> SemesterDate.compare(e2.getDate(), e1.getDate()));
        List<Semester> cropped = (List<Semester>) sorted.subList(0, n);
        return (Semester[]) cropped.toArray(new Semester[cropped.size()]);
    }

    public double collagePoints(double tk, double plusPoints){
        Semester[] recent2 = findMostRecent(2);
        double a = Semester.creditIndexAverages(recent2);
        return (a - 2.0)/ ( (tk - 2.0) / 100 ) + plusPoints;
    }

    public static double sumCreditGrade(Semester[] semesters, boolean mintaOnly, boolean finalizedOnly){
        double sumCreditGrade = 0;
        for(Semester s : semesters){
            sumCreditGrade += s.sumGradeCredit(mintaOnly, finalizedOnly);
        }
        return sumCreditGrade;
    }

    public static double sumCredit(Semester[] semesters, boolean mintaOnly, boolean finalizedOnly){
        double sumCredit = 0;
        for(Semester s : semesters){
            sumCredit += s.sumCredit(mintaOnly, finalizedOnly);
        }
        return sumCredit;
    }
}
