package bme.creditcalc.ui;

import bme.creditcalc.model.Semester;

import javax.swing.table.AbstractTableModel;

public class SemesterTable extends AbstractTableModel {
    Semester semester;

    public Semester getSemester() {
        return semester;
    }

    public void setModel(Semester s){
        semester = s;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return semester.getSubjects().size();
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
                return semester.getSubjects().get(rowIndex).getName();
            case 1:
                return semester.getSubjects().get(rowIndex).getCredit();
            case 2:
                return semester.getSubjects().get(rowIndex).getGrade();
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
                semester.getSubjects().get(rowIndex).setName((String) aValue);
                break;
            case 1:
                semester.getSubjects().get(rowIndex).setCredit(Double.parseDouble((String) aValue));
                break;
            case 2:
                semester.getSubjects().get(rowIndex).setGrade(Integer.parseInt((String)aValue));
                this.fireTableRowsUpdated(rowIndex, rowIndex);
                break;
            default:
                break;
        }
    }
}
