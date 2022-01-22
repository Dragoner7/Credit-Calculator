package bme.creditcalc.neptunreader

import java.io.File
import javax.swing.filechooser.FileFilter

class XLSXFileFilter : FileFilter() {
    override fun accept(f: File): Boolean {
        return f.isDirectory || f.name.contains(".xlsx")
    }

    override fun getDescription(): String {
        return "*.xlsx"
    }
}