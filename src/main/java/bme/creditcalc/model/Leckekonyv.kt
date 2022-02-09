package bme.creditcalc.model

class Leckekonyv() {
    var semesters = mutableListOf<Semester>()

    constructor(semesters: List<Semester>) : this() {
        this.semesters.addAll(semesters)
    }

    fun findMostRecent(n: Int): Leckekonyv {
        val sorted = semesters.subList(0, semesters.size)
        sorted.sortWith { e1: Semester, e2: Semester -> SemesterDate.compare(e2.date, e1.date) }
        return Leckekonyv(sorted.subList(0, n))
    }

    fun collagePoints(tk: Double, plusPoints: Double): Double {
        val recent2 = findMostRecent(2)
        val a: Double = recent2.creditIndexAverages()
        return (a - 2.0) / ((tk - 2.0) / 100) + plusPoints
    }

    fun sumCreditGrade(mintaOnly: Boolean = false, finalizedOnly: Boolean = false): Double {
        var sumCreditGrade = 0.0
        for (s in semesters) {
            sumCreditGrade += s.sumGradeCredit(mintaOnly, finalizedOnly)
        }
        return sumCreditGrade
    }

    fun sumCredit(mintaOnly: Boolean = false, finalizedOnly: Boolean = false): Double {
        var sumCredit = 0.0
        for (s in semesters) {
            sumCredit += s.sumCredit(mintaOnly, finalizedOnly)
        }
        return sumCredit
    }

    fun creditIndexAverages(): Double {
        var result = 0.0
        for (i in semesters.indices) {
            result += semesters[i].calculateCreditIndex()
        }
        return result / semesters.size
    }

}