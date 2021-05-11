package bme.creditcalc.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PopupListener extends MouseAdapter {
    private JPopupMenu popupToShow;
    private Point lastPopupLocation;

    public PopupListener(JPopupMenu jpopupmenu){
        popupToShow = jpopupmenu;
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
            lastPopupLocation = e.getPoint();
        }
    }

    public Point getLastPopupLocation() {
        return lastPopupLocation;
    }
}
