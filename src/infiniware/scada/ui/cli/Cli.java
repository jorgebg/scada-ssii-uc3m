/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.scada.ui.cli;

import infiniware.automatas.Automata;
import infiniware.scada.Scada;
import infiniware.scada.ui.IUi;
import infiniware.scada.ui.Ui;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author jorge
 */
public class Cli extends Ui implements Runnable {

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
            
            try {
                accion = Integer.parseInt(linea);
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

    public Thread mostrar() {
        thread = new Thread(this);
        thread.start();
        return thread;
    }

    public void ocultar() {
        synchronized (INSTANCIA) {
            thread.notify();
        }
    }

    public void actualizar(char[] estados) {
        Map<String, Map<String, String>> arbol = new LinkedHashMap<String, Map<String, String>>();
        for (int id = 0; id < estados.length; id++) {
            Automata automata = Automata.INSTANCIAS.get(id);
            arbol.put(
                    automata.getRemoteName(),
                    automata.subautomatas.decodificarNombreEstados(estados[id]));
        }
        Yaml arbolyaml = new Yaml();
        System.out.println(arbolyaml.dump(arbol));
        System.out.println(Scada.INSTANCIA.mapaSensores);
        if(Scada.INSTANCIA.mapaSensores.get("M")) {
            System.out.println("Limpiando CPD automaticamente");
            limpiarCPD();
        }
        System.out.println();
        //MapUtils.debugPrint(System.out, "Estados", arbol);
    }

    @Override
    public void log(String msg) {
        System.out.println(msg);
    }
}
