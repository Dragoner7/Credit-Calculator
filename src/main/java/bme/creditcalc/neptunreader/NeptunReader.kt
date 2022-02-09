package bme.creditcalc.neptunreader

import bme.creditcalc.model.Leckekonyv
import bme.creditcalc.model.Semester
import bme.creditcalc.model.Subject
import bme.creditcalc.ui.Window
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import javax.swing.SwingWorker

open class NeptunReader(var file: File, var model: Leckekonyv) : SwingWorker<Semester?, Any?>() {
    @Throws(IOException::class)
    private fun readXLSX(): Semester? {
        if (!XLSXFileFilter().accept(file)) {
            return null
        }
        val fis = FileInputStream(file)
        val workbook = XSSFWorkbook(fis)
        val sheet = workbook.getSheetAt(0)
        val date = workbook.getSheetName(0)
        val rowIt: Iterator<Row> = sheet.iterator()
        if (!basicFormatCheck(rowIt.next())) {
            return null
        }
        val result = Semester(date.substring(date.length - 7, date.length - 3).toInt(), date.substring(date.length - 1).toInt())
        for(preexistingSemester in model.semesters){
            if (result == preexistingSemester) {
                workbook.close()
                fis.close()
                return null
            }
        }
        while (rowIt.hasNext()) {
            val row = rowIt.next()
            val subject = Subject(row.getCell(1).toString(), row.getCell(2).toString().toInt().toDouble(), findGrade(row.getCell(7).toString()))
            result.addSubject(subject)
        }
        workbook.close()
        fis.close()
        return result
    }

    @Throws(Exception::class)
    override fun doInBackground(): Semester? {
        return readXLSX()
    }

    override fun done() {
        try {
            if (get() == null) {
                return
            }
            Window.addSemester(get())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun basicFormatCheck(header: Row): Boolean {
        return header.getCell(1).toString().contains("Tárgy") &&
                header.getCell(2).toString().contains("Kr.") &&
                header.getCell(7).toString().contains("Jegyek")
    }

    private fun findGrade(string: String): Int {
        val grades = IntArray(6)
        grades[0] = -1
        grades[1] = string.indexOf("Elégtelen")
        grades[2] = string.indexOf("Elégséges")
        grades[3] = string.indexOf("Közepes")
        grades[4] = string.indexOf("Jó")
        grades[5] = string.indexOf("Jeles")
        var highest = 0
        for (i in 0..5) {
            if (grades[i] > grades[highest]) {
                highest = i
            }
        }
        return highest
    }
}