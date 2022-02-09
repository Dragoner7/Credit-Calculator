package bme.creditcalc.ui

import java.awt.Component
import java.time.LocalDate
import javax.swing.JOptionPane


fun collagePointsResultDialog(parent : Component, points: Double) {
    JOptionPane.showMessageDialog(parent, "Your collage points: $points")
}

fun plusPointsDialog(parent : Component, plusPoints: Double = 0.0) = JOptionPane.showInputDialog(
    parent,
    "Enter plus points:",
    "Enter plus points (if you have any):",
    JOptionPane.PLAIN_MESSAGE,
    null,
    null,
    plusPoints
).toString().toDouble()

fun tkDialog(parent : Component, initialValue: Double = 4.0) = JOptionPane.showInputDialog(
    parent,
    "Enter 'tanulmányilag kiemelt' value:",
    "Enter 'tanulmányilag kiemelt' value:",
    JOptionPane.PLAIN_MESSAGE,
    null,
    null,
    initialValue
).toString().toDouble()

fun semesterSourceDialog(parent : Component) : Int {
    val options = arrayOf("New", "Import Neptun XLSX", "Cancel")
    return JOptionPane.showOptionDialog(
        parent,
        "Please select from the options below",
        "Creating new Semester",
        JOptionPane.YES_NO_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        options,
        options[2]
    )
}

fun semesterDialog(parent : Component) : Int {
    val options = arrayOf("Winter",
        "Spring")
    return JOptionPane.showOptionDialog(
        parent,
        "Select season",
        "Select season",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        options,
        options[0]
    ) + 1
}

fun yearDialog(parent : Component) : Int {
    val years = ArrayList<Int>()
    val now = LocalDate.now()
    for (i in now.year - 10 until now.year + 10) {
        years.add(i)
    }
    return JOptionPane.showInputDialog(
        parent,
        "Choose year",
        "Choose year",
        JOptionPane.PLAIN_MESSAGE,
        null,
        years.toTypedArray(),
        now.year
    ) as Int
}

fun removeSubjectDialog(parent : Component) = JOptionPane.showInputDialog(
    parent,
    "Which subject to delete (row number):",
    "Which subject to delete",
    JOptionPane.PLAIN_MESSAGE,
    null,
    null,
    0
).toString().toInt()
