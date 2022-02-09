package bme.creditcalc.ui

import bme.creditcalc.model.Semester
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.event.TableModelListener

class SemesterAverageView(private var semester : Semester?) {
    private var creditLabel: JLabel = JLabel()
    private var averageLabel: JLabel = JLabel()
    private var creditIndexLabel: JLabel = JLabel()

    val view = JPanel()

    private val tableModelListener = TableModelListener { updateAverages() }

    init {
        view.add(creditLabel)
        view.add(averageLabel)
        view.add(creditIndexLabel)
    }

    private fun updateAverages() {
        creditLabel.text = "Credits: " + (semester?.sumCredit() ?: "0.0")
        averageLabel.text = "Average: " + (semester?.calculateAverage() ?: "0.0")
        creditIndexLabel.text = "CreditIndex: " + (semester?.calculateCreditIndex() ?: "0.0")
    }

    fun setSemester(semester: Semester?){
        if(semester != null){
            if(this.semester != null){
                semester.view.removeTableModelListener(tableModelListener)
            }
            semester.view.addTableModelListener(tableModelListener)
        }
        this.semester = semester
        updateAverages()
    }

}