package bme.creditcalc.ui;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PopupListener extends MouseAdapter {
    JPopupMenu popupToShow;
    Window window;

    public PopupListener(JPopupMenu jpopupmenu, Window window){
        popupToShow = jpopupmenu;
        this.window = window;
    }
    public void mousePressed(MouseEvent e) {
        popup(e);
    }

    public void mouseReleased(MouseEvent e) {
        popup(e);
    }

    private void popup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            popupToShow.show(e.getComponent(), e.getX(), e.getY());
            window.updatePopupLocation(e.getPoint());
        }
    }
}
