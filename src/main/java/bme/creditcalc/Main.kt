package bme.creditcalc

import bme.creditcalc.ui.Window
import com.github.weisj.darklaf.LafManager
import com.github.weisj.darklaf.theme.DarculaTheme


fun main(){
    LafManager.install(DarculaTheme())
    Window.isVisible = true
    Window.setLocationRelativeTo(null)
}