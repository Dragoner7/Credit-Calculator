package bme.creditcalc.io

import java.io.File
import javax.swing.filechooser.FileFilter

class ExtensionBasedFileFilter(private val extension : String = "") : FileFilter() {
    override fun accept(f: File): Boolean {
        return f.isDirectory || f.name.contains(extension)
    }

    override fun getDescription(): String {
        return "*$extension"
    }
}