/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.automatas.subautomatas;

import infiniware.scada.modelos.Parametros;


/**
 *
 * @author jorge
 */
public class Cinta extends SubAutomata {

    static final String[] configuracion = new String[] {
        "longitud", "velocidad"
    };
    final String robot;
    
    /**
     * longitud
     * velocidad
     */
    public void simular() {
        String estado = this.estados.get(this.estado);
        if(estado.equals("Movimiento")) {
            automata.actualizar("R1", true);
            new Thread(new Runnable() {

                public void run() {
                    Thread.sleep(parametros.get("velocidad")/parametros.get("longitud"));
                    automata.actualizar("R1", false);
                }
            }).start();
        }
    }

    
}

