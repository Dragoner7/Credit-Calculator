package bme.creditcalc.ui;

import bme.creditcalc.model.Semester;
import bme.creditcalc.model.Subject;
import bme.creditcalc.neptunreader.NeptunReader;
import bme.creditcalc.model.Leckekonyv;
import bme.creditcalc.neptunreader.XLSXFileFilter;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

public class Window extends JFrame{

    private static Window instance = new Window();
    public static Window getInstance(){
        return instance;
    }

    private final JPanel mainPanel;
    private Leckekonyv leckekonyv = new Leckekonyv();
    private JTable table;
    private JLabel creditLabel;
    private JLabel averageLabel;
    private JLabel creditIndexLabel;

    protected Window(){
        super("Credit Calculator");
        mainPanel = new JPanel(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        initializeWindow();
        setResizable(false);
        pack();
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

        leckekonyv.addListDataListener(new ListDataListener() {
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
        table.setDefaultRenderer(String.class, new SemesterTableCellRenderer(table.getDefaultRenderer(String.class)));
        table.setDefaultRenderer(Integer.class, new SemesterTableCellRenderer(table.getDefaultRenderer(Integer.class)));
        table.setDefaultRenderer(Boolean.class, new SemesterTableCellRenderer(table.getDefaultRenderer(Boolean.class)));
        table.setDefaultRenderer(Double.class, new SemesterTableCellRenderer(table.getDefaultRenderer(Double.class)));
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
        JComboBox<Semester> comboBox = new JComboBox<>(leckekonyv);
        Dimension dim = comboBox.getPreferredSize();
        dim.width = 400;
        comboBox.setPreferredSize(dim);
        topPanel.add(comboBox);

        leckekonyv.addSelectedChangedListener(e -> updateTableModel());
        //leckekonyv.addSelectedChangedListener(e->comboBox.invalidate());

        JButton newButton = new JButton("New");
        topPanel.add(newButton);
        newButton.addActionListener(e->addSemesterDialog());
        JButton deleteButton = new JButton("Delete");
        topPanel.add(deleteButton);
        deleteButton.addActionListener(e->deleteLastSemester());
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

        addSubjectMenuItem.addActionListener(e-> addNewSubject());
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

        JMenuItem AdvancedCalculatorMenuItem = new JMenuItem("Advanced");
        calculateMenu.add(AdvancedCalculatorMenuItem);

        AdvancedCalculatorMenuItem.addActionListener(e -> {
            AdvancedCalculator dialog = new AdvancedCalculator(this);
            dialog.setVisible(true);
            dialog.setLocationRelativeTo(this);
        });
    }

    private void updateTableModel(){
        Semester selected = (Semester) leckekonyv.getSelectedItem();
        if(selected != null){
            table.setModel(selected.getView());
        } else {
            table.setModel(new DefaultTableModel());
        }
        updateAverages();

    }
    private void updateAverages(){
        Semester selected = (Semester) leckekonyv.getSelectedItem();
        if(selected != null) {
            creditLabel.setText("Credits: " + selected.sumCredit(false, false));
            averageLabel.setText("Average: " + selected.calculateAverage(false, false));
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
        try{
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
            if(selectedSemester < 1 || selectedSemester > 2){
                throw new NullPointerException();
            }
            addSemester(new Semester(selectedYear, selectedSemester));
        } catch (NullPointerException ignored){}

    }

    public void deleteLastSemester(){
        leckekonyv.removeElement(leckekonyv.getSelectedItem());
    }

    public void addSemester(Semester semester){
        SemesterTable semesterTable = new SemesterTable();
        semester.attachView(semesterTable);
        semesterTable.setModel(semester);
        leckekonyv.addElement(semester);
    }

    private void loadSemester() {
        JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
        fc.setFileFilter(new XLSXFileFilter());
        //fc.changeToParentDirectory();
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            (new NeptunReader(file, leckekonyv)).execute();
        }
        //updateTableModel();
    }

    public void addNewSubject(){
        Semester currentSemester = (Semester) leckekonyv.getSelectedItem();
        if(currentSemester != null){
            currentSemester.addSubject(new Subject("New Subject", 0, 1));
        }
    }

    private void addSubject(int row){
        Semester currentSemester = (Semester) leckekonyv.getSelectedItem();
        if(currentSemester != null){
            currentSemester.addSubjectAt(row, new Subject("New Subject", 0, 1));
        }
    }

    public void deleteSubject(int row){
        Semester currentSemester = (Semester) leckekonyv.getSelectedItem();
        if(currentSemester != null){
            currentSemester.removeSubjectAt(row);
        }
    }

    private void calculateCollageDialog(){
        double tk = 4;
        double plusPoints = 0;
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
            double points = leckekonyv.collagePoints(tk, plusPoints);
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

    public Leckekonyv getLeckekonyv() {
        return leckekonyv;
    }
}
