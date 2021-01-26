package bme.creditcalc;

import bme.creditcalc.Subject;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.*;

public class Semester extends AbstractTableModel {
    private List<Subject> subjects = new ArrayList<>();
    SemesterDate date;
    public Semester(int year, int semester){
        date = new SemesterDate(year, semester);
    }
    public void addSubject(Subject s){
        subjects.add(s);
        fireTableRowsInserted(subjects.indexOf(s), subjects.indexOf(s));
    }

    public void removeSubject(Subject s){
        subjects.remove(s);
        fireTableRowsDeleted(subjects.indexOf(s), subjects.indexOf(s));
    }

    public void removeSubjectAt(int row){
        subjects.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public void addSubjectAt(int row, Subject s){
        if(row > subjects.size() || row < 0){
            addSubject(s);
            return;
        }
        subjects.add(row, s);
        fireTableRowsInserted(row, row);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Semester semester = (Semester) o;
        return date.equals(semester.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }

    public SemesterDate getDate() {
        return date;
    }

    public Subject getSubject(int indx){
        return subjects.get(indx);
    }

    public double calculateAverage(){
        double sumGradeTimesCredit = 0;
        double sumCredit = 0;
        for(Subject s : subjects){
            sumGradeTimesCredit += s.getCredit() * s.getGrade();
            sumCredit += s.getCredit();
        }
        return sumGradeTimesCredit / sumCredit;
    }

    public double calculateCreditIndex(){
        double sumGradeTimesCredit = 0;
        for(Subject s : subjects){
            sumGradeTimesCredit += s.getCredit() * s.getGrade();
        }
        return sumGradeTimesCredit / 30;
    }

    @Override
    public int getRowCount() {
        return subjects.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int column) {
        switch (column){
            case 0:
                return "Name";
            case 1:
                return "Credit";
            case 2:
                return "Grade";
        }
        return super.getColumnName(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0:
                return subjects.get(rowIndex).getName();
            case 1:
                return subjects.get(rowIndex).getCredit();
            case 2:
                return subjects.get(rowIndex).getGrade();
            default:
                return null;
        }
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
        /*if(columnIndex > 1){
            return true;
            //return !subjects.get(rowIndex).isFinalized();
        }
        return false;*/
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0:
                subjects.get(rowIndex).setName((String) aValue);
                break;
            case 1:
                subjects.get(rowIndex).setCredit(Double.parseDouble((String) aValue));
                break;
            case 2:
                subjects.get(rowIndex).setGrade(Integer.parseInt((String)aValue));
                this.fireTableRowsUpdated(rowIndex, rowIndex);
                break;
            default:
                break;
        }
    }

    @Override
    public String toString() {
        return date.toString();
    }
}
