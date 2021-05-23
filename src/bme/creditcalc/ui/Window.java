package bme.creditcalc.ui;

import bme.creditcalc.Semester;
import bme.creditcalc.Subject;
import bme.creditcalc.neptunreader.NeptunReader;
import bme.creditcalc.SemesterList;
import bme.creditcalc.neptunreader.XLSXFileFilter;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

public class Window extends JFrame{
    private final JPanel mainPanel;
    private SemesterList semesters = new SemesterList();
    private JTable table;
    private JLabel creditLabel;
    private JLabel averageLabel;
    private JLabel creditIndexLabel;

    private double tk = 4;
    private double plusPoints = 0;

    public Window(){
        super("Credit Calculator");
        mainPanel = new JPanel(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        initializeWindow();
    }
    private void initializeWindow(){
        JPanel infoPanel = new JPanel();
        averageLabel = new JLabel();
        creditIndexLabel = new JLabel();
        creditLabel = new JLabel();
        infoPanel.add(creditLabel);
        infoPanel.add(averageLabel);
        infoPanel.add(creditIndexLabel);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);

        semesters.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                update();
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                update();
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                update();
            }
            public void update(){
                updateTableModel();
                updateAverages();
            }
        });

        initializeMenu();
        initializeTable();
        initializeTableMenu();
        initializeComboBox();
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
        PopupListener popupListener = new PopupListener(tableMenu);
        table.addMouseListener(popupListener);

        addMenuItem.addActionListener(e->addSubject(table.rowAtPoint(popupListener.getLastPopupLocation())));
        deleteMenuItem.addActionListener(e->deleteSubject(table.rowAtPoint(popupListener.getLastPopupLocation())));
    }
    private void initializeComboBox(){
        JPanel topPanel = new JPanel();
        JComboBox<Semester> comboBox = new JComboBox<>(semesters);
        Dimension dim = comboBox.getPreferredSize();
        dim.width = 400;
        comboBox.setPreferredSize(dim);
        topPanel.add(comboBox);

        //updateTableModel();
        //comboBox.addActionListener(e -> updateTableModel());
        semesters.addSelectedChangedListener(e -> updateTableModel());

        JButton newButton = new JButton("New");
        topPanel.add(newButton);
        newButton.addActionListener(e->addSemesterDialog());
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

        addSubjectMenuItem.addActionListener(e-> addSubject());
        removeSubjectMenuItem.addActionListener(e-> deleteSubject(Integer.parseInt((String) JOptionPane.showInputDialog(
                this,
                "Which subject to delete (row number):",
                "Which subject to delete",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                0
        ))));

        JMenu calculateMenu = new JMenu("Calculate");
        menuBar.add(calculateMenu);
        JMenuItem calculateCollagePointsMenuItem = new JMenuItem("Collage points");
        calculateMenu.add(calculateCollagePointsMenuItem);

        calculateCollagePointsMenuItem.addActionListener(e -> calculateCollageDialog());

        JMenuItem specMenuItem = new JMenuItem("Optimal Spec");

        calculateMenu.add(specMenuItem);
        specMenuItem.addActionListener(e->calculateOptimalSpec());
    }

    private void calculateOptimalSpec(){
        double sumGradexCredit = 0;
        double sumCredit = 0;
        boolean is120 = true;
        for(Semester semester : semesters.toArray()){
            sumGradexCredit += semester.sumGradeCredit(true);
            sumCredit += semester.sumCredit(true);
        }
        if(sumCredit != 120){
            is120 = false;
        }
        JOptionPane.showMessageDialog(this, "Spec average: " + sumGradexCredit / sumCredit + "\n Credits count? " + sumCredit);
    }

    private void updateTableModel(){
        Semester selected = (Semester) semesters.getSelectedItem();
        if(selected != null){
            table.setModel(selected);
        } else {
            table.setModel(new DefaultTableModel());
        }
        updateAverages();
    }
    private void updateAverages(){
        Semester selected = (Semester) semesters.getSelectedItem();
        if(selected != null) {
            creditLabel.setText("Credits: " + selected.sumCredit(false));
            averageLabel.setText("Average: " + selected.calculateAverage());
            creditIndexLabel.setText("CreditIndex: " + selected.calculateCreditIndex());
        } else {
            creditLabel.setText("Credits:");
            averageLabel.setText("Average:");
            creditIndexLabel.setText("CreditIndex:");
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
        //updateTableModel();
    }

    private void deleteSemester(){
        semesters.removeElement(semesters.getSelectedItem());
        //updateTableModel();
    }

    private void loadSemester() {
        JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
        fc.setFileFilter(new XLSXFileFilter());
        //fc.changeToParentDirectory();
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            (new NeptunReader(file, semesters)).execute();
        }
        //updateTableModel();
    }

    private void addSubject(){
        Semester currentSemester = (Semester)semesters.getSelectedItem();
        if(currentSemester != null){
            currentSemester.addSubject(new Subject("New Subject", 0, 1));
        }
    }

    private void addSubject(int row){
        Semester currentSemester = (Semester)semesters.getSelectedItem();
        if(currentSemester != null){
            currentSemester.addSubjectAt(row, new Subject("New Subject", 0, 1));
        }
    }

    private void deleteSubject(int row){
        Semester currentSemester = (Semester)semesters.getSelectedItem();
        if(currentSemester != null){
            currentSemester.removeSubjectAt(row);
        }
    }

    private void calculateCollageDialog(){
        Semester[] recent2 = Semester.find2MostRecentSemesters(semesters.toArray());
        double a = Semester.creditIndexAverages(recent2);
        try{
            tk = Double.parseDouble((String) JOptionPane.showInputDialog(
                    this,
                    "Enter 'tanulmányilag kiemelt' value:",
                    "Enter 'tanulmányilag kiemelt' value:",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    tk
            ));
            plusPoints = Double.parseDouble((String) JOptionPane.showInputDialog(
                    this,
                    "Enter plus points:",
                    "Enter plus points (if you have any):",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    plusPoints
            ));
            double points = (a - 2.0)/ ( (tk - 2.0) / 100 ) + plusPoints;
            JOptionPane.showMessageDialog(this, "Your collage points: " + points);
        }catch (Exception ignored){ }
    }
    private void addSemesterDialog(){
        String[] options = {"New", "Import Neptun XLSX", "Cancel"};
        int result;
        result = JOptionPane.showOptionDialog(
                this,
                "Please select from the options below",
                "Creating new Semester",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[2]
        );
        switch (result){
            case 0:
                newEmptySemester();
                break;
            case 1:
                loadSemester();
                break;
        }
    }
}
