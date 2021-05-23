package bme.creditcalc;

import java.util.Objects;

public class Subject {
    private String name;
    private double credit;
    private int grade;
    private boolean minta;

    public Subject(String name, double credit){
        this.name = name;
        this.credit = credit;
        this.grade = 0;
        this.minta = true;
    }
    public Subject(String name, double credit, int grade){
        this(name, credit);
        this.grade = grade;
    }
    public Subject(String name, double credit, int grade, boolean minta){
        this(name, credit, grade);
        this.minta = minta;

    }
    public double getCredit() {
        return credit;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Double.compare(subject.credit, credit) == 0 && grade == subject.grade && name.equals(subject.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, credit, grade);
    }

    public void setGrade(int grade) {
        //if(!finalized){
            if(grade > 5){
                grade = 5;
            } else if(grade < 0){
                grade = 0;
            }
            this.grade = grade;
            //this.finalized = true;
        //}
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public boolean isFinalized(){
        return grade > 1;
    }

    public int getGrade() {
        return grade;
    }

    public boolean getMinta(){
        return minta;
    }
    public void setMinta(boolean minta){
        this.minta = minta;
    }
}
