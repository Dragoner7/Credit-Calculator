package bme.creditcalc.model;

import javax.swing.table.AbstractTableModel;
import java.util.*;

public class Semester {
    private ArrayList<Subject> subjects = new ArrayList<>();
    private AbstractTableModel view;
    SemesterDate date;
    public Semester(int year, int semester){
        date = new SemesterDate(year, semester);
    }
    public void addSubject(Subject s){
        subjects.add(s);
        if(view != null){
            view.fireTableRowsInserted(subjects.indexOf(s), subjects.indexOf(s));
        }
    }

    public void removeSubject(Subject s){
        subjects.remove(s);
        if(view != null){
            view.fireTableRowsDeleted(subjects.indexOf(s), subjects.indexOf(s));
        }
    }

    public void removeSubjectAt(int row){
        subjects.remove(row);
        if(view != null) {
            view.fireTableRowsDeleted(row, row);
        }
    }

    public void addSubjectAt(int row, Subject s){
        if(row > subjects.size() || row < 0){
            addSubject(s);
            return;
        }
        subjects.add(row, s);
        if(view != null) {
            view.fireTableRowsInserted(row, row);
        }
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

    public List<Subject> getSubjects() {
        return (List<Subject>) subjects.clone();
    }

    public void attachView(AbstractTableModel view){
        this.view = view;
    }

    public void detachView(){
        this.view = null;
    }

    public AbstractTableModel getView(){
        return view;
    }

    public double calculateAverage(){
        return sumGradeCredit() / sumCredit();
    }

    public double calculateCreditIndex(){
        double sumGradeTimesCredit = 0;
        for(Subject s : subjects){
            sumGradeTimesCredit += s.getCredit() * s.getGrade();
        }
        return sumGradeTimesCredit / 30;
    }

    public double sumGradeCredit(){
        double sumGrade = 0;
        for(Subject s : subjects){
            sumGrade += s.getGrade() * s.getCredit();
        }
        return sumGrade;
    }

    public double sumCredit(){
        double sumCredit = 0;
        for(Subject s : subjects){
            sumCredit += s.getCredit();
        }
        return sumCredit;
    }

    @Override
    public String toString() {
        return date.toString();
    }

    public static double creditIndexAverages(Semester[] semesters){
        double result = 0;
        for(int i = 0; i < semesters.length; ++i){
            if(semesters[i] == null){
                return Double.NaN;
            }
            result += semesters[i].calculateCreditIndex();
        }
        return result / semesters.length;
    }

    /*public static Semester[] find2MostRecentSemesters(Semester[] semesters) {
        Semester[] result = new Semester[2];
        for (int i = 0; i < semesters.length; ++i) {
            if (result[0] == null || SemesterDate.compare(semesters[i].getDate(), result[0].getDate()) > 0) {
                result[1] = result[0];
                result[0] = semesters[i];
            } else if (result[1] == null || SemesterDate.compare(semesters[i].getDate(), result[1].getDate()) > 0) {
                result[1] = semesters[i];
            }
        }
        return result;
    }*/
}
