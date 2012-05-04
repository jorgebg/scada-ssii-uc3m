/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.automatas.subautomatas;

import infiniware.automatas.Automata;
import infiniware.scada.modelos.Parametros;
import infiniware.scada.simulador.Simulacion;

/**
 *
 * @author jorge
 */
public class Cinta extends SubAutomata {

    Parametros parametros = new Parametros("velocidad", "longitud");
    final String salida;

    class Movimiento extends Simulacion {

        @Override
        public long tiempo(int accion) {
            return parametros.get("velocidad") / parametros.get("longitud");
        }

        @Override
        public void postaccion(int accion) {
            automata.actualizar(salida, true);
        }
    };

    public Cinta(Automata automata, String robot) {
        super(automata);
        this.salida = robot;
    }
}
