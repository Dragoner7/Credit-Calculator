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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val subject = other as Subject
        return subject.credit.compareTo(credit) == 0 && grade == subject.grade && name == subject.name
    }

    override fun hashCode(): Int {
        return Objects.hash(name, credit, grade)
    }

    val isFinalized: Boolean get() = grade > 1

}