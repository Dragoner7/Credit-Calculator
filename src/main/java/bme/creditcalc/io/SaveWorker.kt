package bme.creditcalc.io

import bme.creditcalc.model.Leckekonyv
import java.io.File
import javax.swing.SwingWorker

class SaveWorker(private val file : File, private val leckekonyv: Leckekonyv) : SwingWorker<Unit, Nothing>() {
    val adapter = SaveLoadCommon.adapter
    override fun doInBackground() {
        file.writeText(adapter.toJson(leckekonyv))
    }
}