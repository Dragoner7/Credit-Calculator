package bme.creditcalc.model

import java.util.*
import javax.swing.table.AbstractTableModel

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val semester = other as Semester
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

    fun sumGradeCredit(mintaOnly: Boolean = false, finalizedOnly: Boolean = false): Double {
        var sumGrade = 0.0
        for (s in subjects) {
            if ((!mintaOnly || s.minta) && (!finalizedOnly || s.isFinalized)) {
                sumGrade += s.grade * s.credit
            }
        }
        return sumGrade
    }

    fun sumCredit(mintaOnly: Boolean = false, finalizedOnly: Boolean = false): Double {
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
}