/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.scada.ui.gui;

import infiniware.automatas.Automata;
import infiniware.automatas.subautomatas.CintaCapacidad;
import infiniware.scada.ui.Ui;
import infiniware.scada.ui.gui.view.SCADAUserInterface;
import infiniware.scada.ui.gui.view.animation.Animation;
import infiniware.scada.ui.gui.view.animation.SlideAnimation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author jorge
 */
public class Gui extends Ui implements Runnable {

    public static final Gui INSTANCIA = new Gui();
    public static final Map<String, Map<String, Integer>> mapaEstadosGui = generarMapaEstadosGui();
    private SCADAUserInterface frame;
    private Thread thread;
    private boolean iniciada;

    private Gui() {
    }

    public void run() {
        try {
            frame = new SCADAUserInterface();
            frame.setVisible(true);
            iniciada = true;
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
                Animation animacion = obtenerAnimacionSubAutomata(subautomata);
                if (animacion.getState() != estadoGui) {
                    animacion.start(estadoGui);
                    System.out.println("Llamando Animation " + subautomata + " Estado: " + estadoScada + " " + estadoGui);
                }
            }
        }
    }

    public Thread mostrar() {

        thread = new Thread(this);
        thread.start();
        System.out.println("Esperando a que se inicie la GUI...");
        while(!iniciada)
        {
            try {
                Thread.currentThread().sleep(500);
            } catch (InterruptedException ex) {
                System.err.println("Error al esperar la GUI");
            }
        }
        System.out.println("GUI iniciada");
        return thread;
    }

    public void ocultar() {
        synchronized (INSTANCIA) {
            thread.notify();
        }
    }

    private Animation obtenerAnimacionSubAutomata(String subautomata) {
        String methodName = "get" + StringUtils.capitalize(subautomata.toLowerCase());
        Animation animation = null;
        try {
            Method method = frame.ac.getClass().getMethod(methodName);
            animation = (Animation) method.invoke(frame.ac);
        } catch (Exception e) {
            System.err.println("Error al llamar al metodo '" + methodName + "'");
        }
        return animation;
    }

    private int obtenerEstadoGui(String subautomata, String estadoScada) {
        return mapaEstadosGui.get(subautomata).get(estadoScada);
    }

    // Subautomata1 : { Estado1: 0, Estado2: 1, ... }, Subautomata2: { Estado1: 0, ... }
    private static Map<String, Map<String, Integer>> generarMapaEstadosGui() {
        Map<String, String[]> mapaBase = new HashMap<String, String[]>() {

            {
                put("CEJ", new String[]{
                            "Reposo",
                            "Movimiento"});

                put("CEN", new String[]{
                            "Reposo",
                            "Movimiento"});

                put("EM", new String[]{
                            "Reposo",
                            "Ocupada"});

                put("R1", new String[]{
                            "Reposo, EsperaEje, EsperaEngranaje, EsperaMontaje",
                            "TransporteEngranaje1, TransporteEngranaje2",
                            "TransporteEje1, TransporteEje2",
                            "TransporteConjuntoMontado"});

                put("ES", new String[]{
                            "Reposo",
                            "Ocupada"});


                put("COK", new String[]{
                            "Reposo",
                            "Movimiento"});

                put("EV", new String[]{
                            "Reposo",
                            "Ocupada"});


                put("CT", new String[]{
                            "Reposo",
                            "Movimiento"});

                put("R2", new String[]{
                            "Reposo",
                            "MueveConjuntoMontado",
                            "MueveConjuntoSoldado",
                            "MueveConjuntoValido",
                            "MueveConjuntoNoValido"});

            }
        };
        Map<String, Map<String, Integer>> mapa = new HashMap<String, Map<String, Integer>>();
        for (Map.Entry<String, String[]> entry : mapaBase.entrySet()) {
            int indice = 0;
            String subautomata = entry.getKey();
            String[] estadosSubautomata = entry.getValue();

            Map<String, Integer> mapaEstados = new HashMap<String, Integer>();

            for (String estadosIndice : estadosSubautomata) {
                String[] estados = estadosIndice.split(", ");
                for (int i = 0; i < estados.length; i++) {
                    mapaEstados.put(estados[i], indice);
                }
                indice++;
                mapa.put(subautomata, mapaEstados);
            }
        }
        return mapa;
    }
// mapaEstadosGui
/*
CEJ:
0: Reposo
1: Movimiento

CEN
0: Reposo
1: Movimiento

EM
0: Reposo
1: Ocupada

R1
0: Reposo, EsperaEje, EsperaEngranaje, EsperaMontaje
1: TransporteEngranaje1, TransporteEngranaje2
2: TransporteEje1, TransporteEje2
3: TransporteConjuntoMontado

ES
0: Reposo
1: Ocupada


COK
0: Reposo
1: Movimiento

EV
0: Reposo
1: Ocupada


CT
0: Reposo
1: Movimiento

R2
0: Reposo
1: MueveConjuntoMontado
2: MueveConjuntoSoldado
3: MueveConjuntoValido
4: MueveConjuntoNoValido
*/

    @Override
    public void log(String msg) {
        JTextArea log = frame.logConsole;
        log.append(msg+"\n");
        //log.setText(log.getText() + msg+"\n");
        log.setCaretPosition(log.getDocument().getLength());
    }

    @Override
    public void simularCinta(String nombre, boolean[] posiciones) {
        System.out.println("Simulando cinta ["+nombre+"]: "+CintaCapacidad.toString(nombre, posiciones, "O"));
        Animation animation = this.obtenerAnimacionSubAutomata(nombre);
        //if(animation instanceof SlideAnimation) {
        //    ((SlideAnimation) animation).updateElements(posiciones);
        //}
        //else System.err.println("El Animation de ["+nombre+"] no es una SlideAnimation");
        try {
            ((SlideAnimation) animation).updateElements(posiciones);
        } catch (ClassCastException ex) {
            System.err.println("El Animation de ["+nombre+"] no es una SlideAnimation");
        }
        
    }
    
    
}
