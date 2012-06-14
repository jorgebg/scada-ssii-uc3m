/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.scada.ui.gui;

import infiniware.automatas.Automata;
import infiniware.scada.Scada;
import infiniware.scada.ui.Ui;
import infiniware.scada.ui.cli.Cli;
import infiniware.scada.ui.gui.view.SCADAUserInterface;
import infiniware.scada.ui.gui.view.animation.Animation;
import java.awt.EventQueue;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author jorge
 */
public class Gui extends Ui implements Runnable{

    public static final Gui INSTANCIA = new Gui();
    private SCADAUserInterface frame;
    private Thread thread;

    private Gui() {
    }

    public void run() {
        try {
            frame = new SCADAUserInterface();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public void actualizar(char[] estados) {
        for (int id = 0; id < estados.length; id++) {
            Automata automata = Automata.INSTANCIAS.get(id);
            Map<String, String> estadosSubautomatas = automata.subautomatas.decodificarNombreEstados(estados[id]);
            for (Map.Entry<String, String> entry : estadosSubautomatas.entrySet()) {
                String subautomata = entry.getKey();
                String estadoScada = entry.getValue();
                int estadoGui = obtenerEstadoGui(subautomata, estadoScada);
                Animation animacion = obtenerAnimacionSubautomata(subautomata);
                if(animacion.getState()!=estadoGui)
                    animacion.start(estadoGui);
            }
        }
        //MapUtils.debugPrint(System.out, "Estados", arbol);
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
    
    private Animation obtenerAnimacionSubautomata(String automata) {
        String methodName = "get"+StringUtils.capitalize(automata.toLowerCase());
        Animation animation = null;
        try {
        Method method = frame.ac.getClass().getMethod(methodName);
        animation = (Animation) method.invoke(frame.ac);
        } catch (Exception e) {
        System.err.println("Error al llamar al metodo '"+methodName+"'");
        }
        return animation;
    }

    private int obtenerEstadoGui(String nombre, String estadoScada) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
