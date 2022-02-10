package bme.creditcalc.ui

import bme.creditcalc.model.Leckekonyv
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JPanel

class LeckekonyvComboBox(leckekonyv: Leckekonyv) {
    val view = JPanel()
    val model = LeckekonyvComboBoxModel(leckekonyv)
    val jComboBox = JComboBox(model)

    var newSemesterListener = mutableListOf<ActionListener>()
    var deleteSemesterListener = mutableListOf<ActionListener>()

    init {
        val dim = jComboBox.preferredSize
        dim.width = 400
        jComboBox.preferredSize = dim
        view.add(jComboBox)
        val newButton = JButton("New")
        view.add(newButton)
        newButton.addActionListener {
            newSemesterListener.forEach { e->e.actionPerformed(ActionEvent(this, ActionEvent.ACTION_PERFORMED, "")) }
        }
        val deleteButton = JButton("Delete")
        view.add(deleteButton)
        deleteButton.addActionListener {
            deleteSemesterListener.forEach { e->e.actionPerformed(ActionEvent(this, ActionEvent.ACTION_PERFORMED, "")) }
        }
    }
}