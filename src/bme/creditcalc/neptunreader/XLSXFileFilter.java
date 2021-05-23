package bme.creditcalc.neptunreader;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class XLSXFileFilter extends FileFilter {
    @Override
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().contains(".xlsx");
    }

    @Override
    public String getDescription() {
        return "*.xlsx";
    }
}
