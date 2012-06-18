/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.scada.ui.gui;

import infiniware.automatas.Automata;
import infiniware.scada.modelos.ConjuntoParametros;
import infiniware.scada.modelos.Guardable;
import infiniware.scada.modelos.Parametros;
import infiniware.scada.ui.Ui;
import infiniware.scada.ui.gui.view.SCADAUserInterface;
import infiniware.scada.ui.gui.view.animation.Animation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTextArea;
import javax.swing.text.ParagraphView;

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
        return thread;
    }

    public void ocultar() {
        synchronized (INSTANCIA) {
            thread.notify();
        }
    }

    private Animation obtenerAnimacionSubautomata(String automata) {
        String methodName = "get" + StringUtils.capitalize(automata.toLowerCase());
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
    /**
     * Metodo que convierte un mapa de la interfaz a un ConjuntoParametros
     * 
     * @param mapa
     * @return ConjuntoParametros
     */
    public static ConjuntoParametros deMapaAConjunto(Map<String, Double> mapa){
    	Parametros CENparametros = new Parametros();
    	ConjuntoParametros conjunto = new ConjuntoParametros();
    	
    	//Esclavo 1
    		//Cinta Engranajes
    	CENparametros.put("velocidad", mapa.get("CEN_v").intValue());
    	CENparametros.put("longitud", mapa.get("CEN_l").intValue());
    	CENparametros.put("capacidad", mapa.get("CEN_c").intValue());
    	HashMap<String, Guardable> es1map = new HashMap<String, Guardable>();
    	es1map.put("CEN", CENparametros);
    		//Cinta Ejes
    	Parametros CEJparametros = new Parametros();
    	CEJparametros.put("velocidad", mapa.get("CEJ_v").intValue());
    	CEJparametros.put("longitud", mapa.get("CEJ_l").intValue());
    	CEJparametros.put("capacidad", mapa.get("CEJ_c").intValue());
    	es1map.put("CEJ", CEJparametros);
    		//Robot 1
    	Parametros R1parametros = new Parametros();
    	R1parametros.put("TRecogidaEnej", mapa.get("R1_tREn").intValue());
    	R1parametros.put("TTransporteEnej", mapa.get("R1_tTEn").intValue());
    	R1parametros.put("TTransporteCm1", mapa.get("R1_tTCM").intValue());
    	es1map.put("R1", R1parametros);
    		//Estacion de montaje
    	Parametros EMparametros = new Parametros();
    	EMparametros.put("TiempoEm", mapa.get("EM_t").intValue());
    	es1map.put("EM", EMparametros);
    	conjunto.put(1, es1map);

    	//Esclavo 2
    		//Estacion de soldadura
    	Parametros ESparametros = new Parametros();
    	ESparametros.put("TiempoEs", mapa.get("ES_t").intValue());
    	HashMap<String, Guardable> es2map = new HashMap<String, Guardable>();
    	es2map.put("ES", ESparametros);
    	conjunto.put(2, es2map);
    	
    	//Esclavo 3
    		//Estacion de evaluacion
    	Parametros EVparametros = new Parametros();
    	EVparametros.put("TiempoEv", mapa.get("EV_t").intValue());
    	HashMap<String, Guardable> es3map = new HashMap<String, Guardable>();
    	es3map.put("EV", EVparametros);
    		//Cinta OK
    	Parametros COKparametros = new Parametros();
    	COKparametros.put("velocidad", mapa.get("COK_v").intValue());
    	COKparametros.put("longitud", mapa.get("COK_l").intValue());
    	es3map.put("COK", COKparametros);
    		//Cinta no OK
    	Parametros CNOKparametros = new Parametros();
    	CNOKparametros.put("velocidad", mapa.get("CNOK_v").intValue());
    	CNOKparametros.put("longitud", mapa.get("CNOK_l").intValue());
    	es3map.put("CNOK", CNOKparametros);
    	conjunto.put(3, es3map);
    	
    	//Master
    		//Cinta transporte
    	HashMap<String, Guardable> masmap = new HashMap<String, Guardable>();
    	Parametros CTparametros = new Parametros();
    	CTparametros.put("velocidad", mapa.get("CT_v").intValue());
    	CTparametros.put("longitud", mapa.get("CT_l").intValue());
    	masmap.put("CT", CTparametros);
    		//Robot 2
    	Parametros R2parametros = new Parametros();
    	R2parametros.put("TRecogidaCm", mapa.get("R2_tRCM").intValue());
    	R2parametros.put("TTransporteCm2", mapa.get("R2_tTCM").intValue());
    	R2parametros.put("TTransporteCs", mapa.get("R2_tTCS").intValue());
    	masmap.put("R2", R2parametros);
    	conjunto.put(0, masmap);
    	
    	return conjunto;
    }
    
    
    @Override
    public void log(String msg) {
        JTextArea log = frame.logConsole;
        log.append(msg+"\n");
        //log.setText(log.getText() + msg+"\n");
        log.setCaretPosition(log.getDocument().getLength());
    }
}
