package bme.creditcalc.model

import java.util.*

data class Subject(var name: String, var credit: Double) {
    var grade = 0
        set(value) {
            if (value > 5) {
                field = 5
                return
            } else if (value < 0) {
                field = 0
                return
            }
            field = value
        }
    var minta = true

    constructor(name: String, credit: Double, grade: Int) : this(name, credit) {
        this.grade = grade
    }

    constructor(name: String, credit: Double, grade: Int, minta: Boolean) : this(name, credit, grade) {
        this.minta = minta
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val subject = o as Subject
        return subject.credit.compareTo(credit) == 0 && grade == subject.grade && name == subject.name
    }

    override fun hashCode(): Int {
        return Objects.hash(name, credit, grade)
    }

    val isFinalized: Boolean get() = grade > 1

}