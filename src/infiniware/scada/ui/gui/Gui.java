/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.scada.ui.gui;

import infiniware.scada.ui.Ui;
import infiniware.scada.ui.cli.Cli;
import java.awt.EventQueue;

/**
 *
 * @author jorge
 */
public class Gui extends Ui {

    public static final Gui INSTANCIA = new Gui();

    private Gui() {
    }

    public void run() {
        try {
            Ventana frame = new Ventana();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public void actualizar(char[] estados) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Thread iniciarProceso() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void detenerProceso() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
