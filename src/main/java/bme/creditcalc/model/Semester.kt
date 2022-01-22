package bme.creditcalc.model

import javax.swing.table.AbstractTableModel
import java.util.*

data class Semester(val year: Int, val semester: Int) {
    private var subjectsBacking = mutableListOf<Subject>()
    val subjects : List<Subject> = subjectsBacking
    var view: AbstractTableModel? = null
        private set
    var date: SemesterDate = SemesterDate(year, semester)

    fun addSubject(s: Subject) {
        subjectsBacking.add(s)
        if (view != null) {
            view!!.fireTableRowsInserted(subjects.indexOf(s), subjects.indexOf(s))
        }
    }

    fun removeSubject(s: Subject) {
        subjectsBacking.remove(s)
        if (view != null) {
            view!!.fireTableRowsDeleted(subjects.indexOf(s), subjects.indexOf(s))
        }
    }

    fun removeSubjectAt(row: Int) {
        subjectsBacking.removeAt(row)
        if (view != null) {
            view!!.fireTableRowsDeleted(row, row)
        }
    }

    fun addSubjectAt(row: Int, s: Subject) {
        if (row > subjects.size || row < 0) {
            addSubject(s)
            return
        }
        subjectsBacking.add(row, s)
        if (view != null) {
            view!!.fireTableRowsInserted(row, row)
        }
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val semester = o as Semester
        return date == semester.date
    }

    override fun hashCode(): Int {
        return Objects.hash(date)
    }

    fun attachView(view: AbstractTableModel?) {
        this.view = view
    }

    fun detachView() {
        view = null
    }

    fun calculateAverage(mintaOnly: Boolean, finalizedOnly: Boolean): Double {
        return sumGradeCredit(mintaOnly, finalizedOnly) / sumCredit(mintaOnly, finalizedOnly)
    }

    fun calculateCreditIndex(): Double {
        var sumGradeTimesCredit = 0.0
        for (s in subjects) {
            sumGradeTimesCredit += s.credit * s.grade
        }
        return sumGradeTimesCredit / 30
    }

    fun sumGradeCredit(mintaOnly: Boolean, finalizedOnly: Boolean): Double {
        var sumGrade = 0.0
        for (s in subjects) {
            if ((!mintaOnly || s.minta) && (!finalizedOnly || s.isFinalized)) {
                sumGrade += s.grade * s.credit
            }
        }
        return sumGrade
    }

    fun sumCredit(mintaOnly: Boolean, finalizedOnly: Boolean): Double {
        var sumCredit = 0.0
        for (s in subjects) {
            if ((!mintaOnly || s.minta) && (!finalizedOnly || s.isFinalized)) {
                sumCredit += s.credit
            }
        }
        return sumCredit
    }

    override fun toString(): String {
        return date.toString()
    }

    companion object {
        fun creditIndexAverages(semesters: Array<Semester?>): Double {
            var result = 0.0
            for (i in semesters.indices) {
                if (semesters[i] == null) {
                    return Double.NaN
                }
                result += semesters[i]!!.calculateCreditIndex()
            }
            return result / semesters.size
        }
    }
}