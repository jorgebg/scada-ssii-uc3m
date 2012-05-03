/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.scada.ui.consola;

import infiniware.procesos.IProcesable;
import infiniware.scada.ui.IUi;
import infiniware.scada.ui.Ui;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author jorge
 */
public class Consola extends Ui implements Runnable, IProcesable {

    public static final Consola INSTANCIA = new Consola();
    private Thread thread;

    private Consola() {
    }

    public void run() {
        Method[] methods = IUi.class.getDeclaredMethods();
        Arrays.sort(methods, new Comparator<Method>() {
            public int compare(Method m1, Method m2) {
                return m1.getName().compareTo(m2.getName());
            }
        });
        String ayuda = "Opciones disponibles:";
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            ayuda += "\t" + StringUtils.leftPad(i+"", 2, "0") + ": " + method.getName() + "\n";
        }


        System.out.println(ayuda);
        int accion;
        while (true) {
            System.out.print("ui@scada: ");
            Scanner in = new Scanner(System.in);
            accion = Integer.parseInt(in.nextLine());
            try {
                System.out.println("Ejecutando [" + methods[accion].getName() + "]");
                methods[accion].invoke(this);
            } catch (InvocationTargetException ex) {
                System.err.println("Excepcion capturada: ");
                ex.printStackTrace(System.err);
            } catch (Exception e) {
                System.out.println("Sintaxis incorrecta.");
                System.out.println(ayuda);
            }

        }
    }

    public Thread iniciarProceso() {
        thread = new Thread(this);
        thread.start();
        return thread;
    }

    public void detenerProceso() {
        synchronized (INSTANCIA) {
            thread.notify();
        }
    }
}
