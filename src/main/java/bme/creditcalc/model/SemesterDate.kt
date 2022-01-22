package bme.creditcalc.model

data class SemesterDate(val year: Int, val semester: Int) {
    override fun toString(): String {
        return year.toString() + "/" + (year + 1) + "/" + semester
    }

    companion object {
        fun compare(date1: SemesterDate?, date2: SemesterDate?): Int {
            if (date1!!.year > date2!!.year) {
                return 1
            } else if (date1.year == date2.year) {
                if (date1.semester > date2.semester) {
                    return 1
                } else if (date1.semester == date2.semester) {
                    return 0
                }
                return -1
            }
            return -1
        }
    }
}