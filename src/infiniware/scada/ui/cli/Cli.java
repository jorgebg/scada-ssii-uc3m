/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.scada.ui.cli;

import infiniware.automatas.Automata;
import infiniware.procesos.IProcesable;
import infiniware.scada.ui.IUi;
import infiniware.scada.ui.Ui;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author jorge
 */
public class Cli extends Ui implements Runnable, IProcesable {

    public static final Cli INSTANCIA = new Cli();
    private Thread thread;

    private Cli() {
    }

    public void run() {
        Method[] methods = IUi.class.getDeclaredMethods();
        Arrays.sort(methods, new Comparator<Method>() {

            public int compare(Method m1, Method m2) {
                return m1.getName().compareTo(m2.getName());
            }
        });
        String ayuda = "Opciones disponibles:\n";
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            ayuda += "\t" + StringUtils.leftPad(i + "", 2, "0") + ": " + method.getName() + "\n";
        }


        System.out.println(ayuda);
        String linea = null;
        int accion;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("ui@scada: ");
            try {
                linea = in.readLine();
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
            accion = Integer.parseInt(linea);
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

    public void actualizar(char[] estados) {
        Map<String, Map<String, String>> arbol = new HashMap<String, Map<String, String>>();
        for (int id = 0; id < estados.length; id++) {
            Automata automata = Automata.INSTANCIAS.get(id);
            arbol.put(
                    automata.getRemoteName(),
                    automata.subautomatas.decodificarNombreEstados(estados[id]));
        }
        System.out.println(arbol);
    }
}
