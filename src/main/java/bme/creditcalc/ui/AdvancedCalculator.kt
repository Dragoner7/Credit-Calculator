package bme.creditcalc.ui

import javax.swing.JFrame
import javax.swing.JPanel
import bme.creditcalc.model.Leckekonyv
import javax.swing.JLabel
import java.awt.Dimension
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JCheckBox
import javax.swing.JRadioButton
import javax.swing.JTextField
import javax.swing.BoxLayout

class AdvancedCalculator(parent: JFrame?) : JDialog(parent) {
    private var average: JLabel
    private var creditsCounted: JLabel
    private var mintaOnly: JCheckBox
    private var acquiredOnly: JCheckBox
    private var customDiv: JRadioButton
    var div: JTextField
    private var recentOnly: JRadioButton
    private var recentN: JTextField

    init {
        minimumSize = Dimension(500, 250)
        isResizable = false
        layout = BoxLayout(this.contentPane, BoxLayout.Y_AXIS)
        average = JLabel()
        creditsCounted = JLabel()
        val mintaPanel = JPanel()
        mintaOnly = JCheckBox("Minta only")
        mintaPanel.add(mintaOnly)
        add(mintaPanel)
        val acquiredPanel = JPanel()
        acquiredOnly = JCheckBox("Acquired only")
        acquiredPanel.add(acquiredOnly)
        add(acquiredPanel)
        val semestersPanel = JPanel()
        val allRB = JRadioButton("All Semesters")
        allRB.isSelected = true
        semestersPanel.add(allRB)
        recentOnly = JRadioButton("Recents")
        recentOnly.addActionListener { allRB.isSelected = false }
        allRB.addActionListener { recentOnly.isSelected = false }
        semestersPanel.add(recentOnly)
        recentN = JTextField()
        recentN.preferredSize = Dimension(25, 25)
        semestersPanel.add(recentN)
        semestersPanel.add(JLabel("only"))
        add(semestersPanel)
        val customDivPanel = JPanel()
        val allCreditDiv = JRadioButton("Auto divider")
        allCreditDiv.isSelected = true
        customDivPanel.add(allCreditDiv)
        customDiv = JRadioButton("Custom divider:")
        customDiv.addActionListener { allCreditDiv.isSelected = false }
        allCreditDiv.addActionListener { customDiv.isSelected = false }
        customDivPanel.add(customDiv)
        div = JTextField()
        div.preferredSize = Dimension(25, 25)
        customDivPanel.add(div)
        customDivPanel.add(JLabel("credits"))
        add(customDivPanel)
        val calculateButton = JButton("Calculate")
        calculateButton.addActionListener { updateAverage() }
        add(calculateButton)
        val averagePanel = JPanel()
        averagePanel.add(calculateButton)
        averagePanel.add(JLabel("Average:"))
        averagePanel.add(average)
        averagePanel.add(JLabel("Credits counted:"))
        averagePanel.add(creditsCounted)
        add(averagePanel)
        title = "Advanced Calculator"
        pack()
    }

    fun updateAverage() {
        val leckekonyvOfRecentSemesters: Leckekonyv
        var n: Int = Window.leckekonyv.semesters.size
        if (recentOnly.isSelected) {
            n = recentN.text.toInt()
        }
        leckekonyvOfRecentSemesters = Window.leckekonyv.findMostRecent(n)
        val sumCreditGrade: Double = leckekonyvOfRecentSemesters.sumCreditGrade(mintaOnly.isSelected, acquiredOnly.isSelected)
        val credits: Double = if (customDiv.isSelected) {
            div.text.toDouble()
        } else {
            leckekonyvOfRecentSemesters.sumCredit(mintaOnly.isSelected, acquiredOnly.isSelected)
        }
        average.text = (sumCreditGrade / credits).toString()
        creditsCounted.text = credits.toString()
    }
}