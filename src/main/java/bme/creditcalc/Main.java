package bme.creditcalc;

import bme.creditcalc.ui.Window;

public class Main {
    public static void main(String[] args) {
        Window.getInstance().setVisible(true);
        Window.getInstance().setLocationRelativeTo(null);
    }
}
