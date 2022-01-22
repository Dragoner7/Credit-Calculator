package bme.creditcalc.ui;

import bme.creditcalc.model.Leckekonyv;
import bme.creditcalc.model.Semester;

import javax.swing.*;
import java.awt.*;

public class AdvancedCalculator extends JDialog {
    JLabel average;
    JLabel creditsCounted;
    JCheckBox mintaOnly;
    JCheckBox acquiredOnly;
    JRadioButton customDiv;
    JTextField div;
    JRadioButton recentOnly;
    JTextField recentN;
    public AdvancedCalculator(JFrame parent){
        super(parent);
        setMinimumSize(new Dimension(500, 250));
        setResizable(false);
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        average = new JLabel();
        creditsCounted = new JLabel();

        JPanel mintaPanel = new JPanel();
        mintaOnly = new JCheckBox("Minta only");
        mintaPanel.add(mintaOnly);
        add(mintaPanel);

        JPanel acquiredPanel = new JPanel();
        acquiredOnly = new JCheckBox("Acquired only");
        acquiredPanel.add(acquiredOnly);
        add(acquiredPanel);

        JPanel semestersPanel = new JPanel();
        JRadioButton allRB = new JRadioButton("All Semesters");
        allRB.addActionListener(e->recentOnly.setSelected(false));
        allRB.setSelected(true);
        semestersPanel.add(allRB);
        recentOnly = new JRadioButton("Recents");
        recentOnly.addActionListener(e-> allRB.setSelected(false));
        semestersPanel.add(recentOnly);
        recentN = new JTextField();
        recentN.setPreferredSize(new Dimension(25, 25));
        semestersPanel.add(recentN);
        semestersPanel.add(new JLabel("only"));
        add(semestersPanel);

        JPanel customDivPanel = new JPanel();
        JRadioButton allCreditDiv = new JRadioButton("Auto divider");
        allCreditDiv.addActionListener(e->customDiv.setSelected(false));
        allCreditDiv.setSelected(true);
        customDivPanel.add(allCreditDiv);
        customDiv = new JRadioButton("Custom divider:");
        customDiv.addActionListener(e-> allCreditDiv.setSelected(false));
        customDivPanel.add(customDiv);
        div = new JTextField();
        div.setPreferredSize(new Dimension(25, 25));
        customDivPanel.add(div);
        customDivPanel.add(new JLabel("credits"));
        add(customDivPanel);

        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(e->updateAverage());
        add(calculateButton);

        JPanel averagePanel = new JPanel();
        averagePanel.add(calculateButton);
        averagePanel.add(new JLabel("Average:"));
        averagePanel.add(average);
        averagePanel.add(new JLabel("Credits counted:"));
        averagePanel.add(creditsCounted);
        add(averagePanel);
        setTitle("Advanced Calculator");
        pack();
    }
    public void updateAverage(){
        Semester[] semesters;
        int n = Window.getInstance().getLeckekonyv().getSize();
        if(recentOnly.isSelected()){
            n = Integer.parseInt(recentN.getText());
        }
        semesters = Window.getInstance().getLeckekonyv().findMostRecent(n);
        double sumCreditGrade = Leckekonyv.sumCreditGrade(semesters, mintaOnly.isSelected(), acquiredOnly.isSelected());
        double credits;
        if(customDiv.isSelected()){
            credits = Double.parseDouble(div.getText());
        } else {
            credits = Leckekonyv.sumCredit(semesters, mintaOnly.isSelected(), acquiredOnly.isSelected());
        }
        average.setText(String.valueOf(sumCreditGrade / credits));
        creditsCounted.setText(String.valueOf(credits));
    }
}
