package bme.creditcalc;

import java.util.Objects;

public class SemesterDate{
    private int year;
    private int semester;
    public SemesterDate(int year, int semester){
        this.year = year;
        this.semester = semester;
    }
    static public int compare(SemesterDate date1, SemesterDate date2){
        if(date1.year > date2.year){
            return 1;
        } else if(date1.year == date2.year){
            if(date1.semester > date2.semester){
                return 1;
            } else if(date1.semester == date2.semester){
                return 0;
            }
            return -1;
        }
        return -1;
    }

    @Override
    public String toString() {
        return year + "/" + (year +1) +  "/" + semester;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SemesterDate that = (SemesterDate) o;
        return year == that.year && semester == that.semester;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, semester);
    }
}