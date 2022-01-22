package bme.creditcalc.ui

import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JPopupMenu

class PopupListener(private val popupToShow: JPopupMenu) : MouseAdapter() {
    var lastPopupLocation: Point? = null
        private set

    override fun mousePressed(e: MouseEvent) {
        popup(e)
    }

    override fun mouseReleased(e: MouseEvent) {
        popup(e)
    }

    private fun popup(e: MouseEvent) {
        if (e.isPopupTrigger) {
            popupToShow.show(e.component, e.x, e.y)
            lastPopupLocation = e.point
        }
    }
}