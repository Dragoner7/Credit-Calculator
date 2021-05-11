package bme.creditcalc;

import bme.creditcalc.ui.Window;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        try {
            if(!(args.length >= 1 && args[0].equals("-nonative")))
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        Window window = new Window();
        window.pack();
        window.setVisible(true);
    }
}
