package bme.creditcalc.neptunreader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import bme.creditcalc.Semester;
import bme.creditcalc.Subject;
import bme.creditcalc.SemesterList;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

public class NeptunReader extends SwingWorker<Semester, Object> {
    File file;
    SemesterList model;

    public NeptunReader(File file, SemesterList model){
        this.file = file;
        this.model = model;
    }
    private Semester readXLSX() throws IOException {
        //File excelFile = new File(path);
        if(!(new XLSXFileFilter()).accept(file)){
            return null;
        }
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        String date = workbook.getSheetName(0);
        Iterator<Row> rowIt = sheet.iterator();

        if(!basicFormatCheck(rowIt.next())){
            return null;
        }
        Semester result = new Semester(Integer.parseInt(date.substring(date.length() -7, date.length() - 3)) , Integer.parseInt(date.substring(date.length() -1)));
        for(int i = 0; i < model.getSize(); ++i){
            if(result.equals(model.getElementAt(i))){
                workbook.close();
                fis.close();
                return null;
            }
        }

        while(rowIt.hasNext()) {
            Row row = rowIt.next();
            Subject subject = new Subject(row.getCell(1).toString(), Integer.parseInt(row.getCell(2).toString()), findGrade(row.getCell(7).toString()));
            result.addSubject(subject);
        }

        workbook.close();
        fis.close();
        return result;
    }
    private static int findGrade(String string){
        int[] grades = new int[6];
        grades[0] = -1;
        grades[1] = string.indexOf("Elégtelen");
        grades[2] = string.indexOf("Elégséges");
        grades[3] = string.indexOf("Közepes");
        grades[4] = string.indexOf("Jó");
        grades[5] = string.indexOf("Jeles");
        int highest = 0;
        for(int i = 0; i <= 5; ++i){
            if(grades[i] > grades[highest]){
                highest = i;
            }
        }
        return highest;
    }

    @Override
    protected Semester doInBackground() throws Exception {
        return readXLSX();
    }

    @Override
    protected void done() {
        try {
            if(get() == null){
                return;
            }
            model.addElement(get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected boolean basicFormatCheck(Row header){
        return header.getCell(1).toString().contains("Tárgy") &&
                header.getCell(2).toString().contains("Kr.") &&
                header.getCell(7).toString().contains("Jegyek");
    }
}
