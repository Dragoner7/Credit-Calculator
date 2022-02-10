package bme.creditcalc.io

import bme.creditcalc.model.Leckekonyv
import bme.creditcalc.ui.Window
import java.io.File
import javax.swing.SwingWorker

class LoadWorker(private val file : File) : SwingWorker<Leckekonyv, Nothing>() {
    val adapter = SaveLoadCommon.adapter
    override fun doInBackground(): Leckekonyv? {
        return adapter.fromJson(file.readText())
    }

    override fun done() {
        Window.switchLeckekonyv(get())
    }
}