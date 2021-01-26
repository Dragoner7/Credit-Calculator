package bme.creditcalc.ui;

import bme.creditcalc.Semester;
import bme.creditcalc.SemesterDate;
import bme.creditcalc.Subject;
import bme.creditcalc.neptunreader.NeptunReader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

public class Window extends JFrame {
    private JPanel mainPanel;
    private DefaultComboBoxModel<Semester> semesters = new DefaultComboBoxModel<>();
    private JTable table;
    private JLabel averageLabel;
    private JLabel creditIndexLabel;

    private double tk = 4;
    private double plusPoints = 0;

    private Point lastPopupLocation;

    public Window(){
        super("Credit Calculator");
        mainPanel = new JPanel(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        initializeWindow();
        pack();
        setVisible(true);
    }
    private void initializeWindow(){
        initializeMenu();
        initializeTable();
        initializeTableMenu();
        initializeComboBox();

        JPanel infoPanel = new JPanel();
        averageLabel = new JLabel();
        creditIndexLabel = new JLabel();
        infoPanel.add(averageLabel);
        infoPanel.add(creditIndexLabel);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);
    }
    private void initializeTable(){
        table = new JTable(null);
        table.setDefaultRenderer(Object.class, new SemesterTableCellRenderer());
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        table.addPropertyChangeListener(e -> updateAverages());
    }
    private void initializeTableMenu(){
        JPopupMenu tableMenu = new JPopupMenu();
        JMenuItem addMenuItem = new JMenuItem("Add Subject");
        JMenuItem deleteMenuItem = new JMenuItem("Delete Subject");
        tableMenu.add(addMenuItem);
        tableMenu.add(deleteMenuItem);
        table.addMouseListener(new PopupListener(tableMenu, this));

        addMenuItem.addActionListener(e->addSubject(table.rowAtPoint(lastPopupLocation)));
        deleteMenuItem.addActionListener(e->deleteSubject(table.rowAtPoint(lastPopupLocation)));
    }
    private void initializeComboBox(){
        JPanel topPanel = new JPanel();
        JComboBox<Semester> comboBox = new JComboBox<>(semesters);
        Dimension dim = comboBox.getPreferredSize();
        dim.width = 400;
        comboBox.setPreferredSize(dim);
        topPanel.add(comboBox);

        updateTableModel();
        comboBox.addActionListener(e -> updateTableModel());

        JButton newButton = new JButton("New");
        topPanel.add(newButton);
        newButton.addActionListener(e->addSemester());
        JButton deleteButton = new JButton("Delete");
        topPanel.add(deleteButton);
        deleteButton.addActionListener(e->deleteSemester());

        mainPanel.add(topPanel, BorderLayout.NORTH);
    }
    private void initializeMenu(){
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu subjectsMenu = new JMenu("Subjects");
        menuBar.add(subjectsMenu);
        JMenuItem addSubjectMenuItem = new JMenuItem("Add");
        subjectsMenu.add(addSubjectMenuItem);
        JMenuItem removeSubjectMenuItem = new JMenuItem("Remove");
        subjectsMenu.add(removeSubjectMenuItem);

        addSubjectMenuItem.addActionListener(e-> addSubject(-1));
        removeSubjectMenuItem.addActionListener(e-> deleteSubject(
                Integer.parseInt(
                        (String) JOptionPane.showInputDialog(
                            this,
                            "Which subject to delete (row number):",
                            "Which subject to delete",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            0
                        )
                )
        ));

        JMenu calculateMenu = new JMenu("Calculate");
        menuBar.add(calculateMenu);
        JMenuItem calculateAveragesMenuItem = new JMenuItem("Avarages of last 2 Semeters");
        calculateMenu.add(calculateAveragesMenuItem);
        JMenuItem calculateCollagePointsMenuItem = new JMenuItem("Collage points");
        calculateMenu.add(calculateCollagePointsMenuItem);

        calculateAveragesMenuItem.addActionListener(e -> calculateLast2());
        calculateCollagePointsMenuItem.addActionListener(e -> calculateCollage());
    }

    private void updateTableModel(){
        Semester selected = (Semester) semesters.getSelectedItem();
        if(selected != null){
            table.setModel(selected);
            updateAverages();
        }
    }
    private void updateAverages(){
        Semester selected = (Semester) semesters.getSelectedItem();
        if(selected != null) {
            averageLabel.setText("Average: " + selected.calculateAverage());
            creditIndexLabel.setText("CreditIndex: " + selected.calculateCreditIndex());
        }
    }
    private void loadSemester() {
        JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
        //fc.changeToParentDirectory();
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            (new NeptunReader(file, semesters)).execute();
        }
    }

    private void newEmptySemester(){
        ArrayList<Integer> years = new ArrayList<>();
        LocalDate now = LocalDate.now();
        for(int i = now.getYear() - 10; i < now.getYear() + 10;++i){
            years.add(i);
        }
        int selectedYear = (Integer) JOptionPane.showInputDialog(
                this,
                "Choose year",
                "Choose year",
                JOptionPane.PLAIN_MESSAGE,
                null,
                years.toArray(),
                now.getYear()
        );
        String[] options = {"Winter",
                "Spring"};
        int selectedSemester = JOptionPane.showOptionDialog(
                this,
                "Select season",
                "Select season",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        ) + 1;
        semesters.addElement(new Semester(selectedYear, selectedSemester));
    }

    private void deleteSemester(){
        semesters.removeElement(semesters.getSelectedItem());
        table.setModel(new DefaultTableModel());
    }

    private void addSubject(int row){
        ((Semester) semesters.getSelectedItem()).addSubjectAt(row, new Subject("New Subject", 0, 1));
    }

    private void deleteSubject(int row){
        ((Semester) semesters.getSelectedItem()).removeSubjectAt(row);
    }

    private double creditIndexAverages(Semester[] semesters){
        double result = 0;
        for(int i = 0; i < semesters.length; ++i){
            result += semesters[i].calculateCreditIndex();
        }
        return result / semesters.length;
    }

    private void calculateLast2(){
        Semester[] recent2 = find2MostRecentSemesters();
        if (recent2[0] != null && recent2[1] != null) {
            JOptionPane.showMessageDialog(this, "" +
                    "Used semesters:\n" +
                    recent2[0].toString() + "\n"+
                    recent2[1].toString() + "\n"+
                    "Average: " + (recent2[0].calculateAverage() + recent2[1].calculateAverage()) /2 + "\n" +
                    "Credit Index Average:" + creditIndexAverages(recent2));
        }
    }
    private void calculateCollage(){
        Semester[] recent2 = find2MostRecentSemesters();
        double a = creditIndexAverages(recent2);
        tk = Double.parseDouble(
                (String) JOptionPane.showInputDialog(
                        this,
                        "Enter 'tanulmányilag kiemelt' value:",
                        "Enter 'tanulmányilag kiemelt' value:",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        tk
                        )
        );
        plusPoints = Double.parseDouble(
                (String) JOptionPane.showInputDialog(
                        this,
                        "Enter plus points:",
                        "Enter plus points (if you have any):",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        plusPoints
                )
        );
        double points = (a - 2.0)/ ( (tk - 2.0) / 100 ) + plusPoints;
        JOptionPane.showMessageDialog(this, "Your collage points: " + points);
    }

    private Semester[] find2MostRecentSemesters(){
        Semester[] result = new Semester[2];
        for(int i = 0; i < semesters.getSize(); ++i){
            if(result[0] == null || SemesterDate.compare(semesters.getElementAt(i).getDate(), result[0].getDate()) > 0){
                result[1] = result[0];
                result[0] = semesters.getElementAt(i);
            } else if (result[1] == null || SemesterDate.compare(semesters.getElementAt(i).getDate(), result[1].getDate()) > 0){
                result[1] = semesters.getElementAt(i);
            }
        }
        return result;
    }
    private void addSemester(){
        String[] options = {"New", "Import Neptun XLSX", "Cancel"};
        int result = -1;
        result = JOptionPane.showOptionDialog(this, "Please select from the options below", "Creating new Semester", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
        switch (result){
            case 0:
                newEmptySemester();
                break;
            case 1:
                loadSemester();
                break;
        }
    }

    void updatePopupLocation(Point point){
        lastPopupLocation = point;
    }
}
