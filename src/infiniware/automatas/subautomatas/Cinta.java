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
    final String robot;

    class Movimiento extends Simulacion {

        @Override
        public long tiempo(int accion) {
            return parametros.get("velocidad") / parametros.get("longitud");
        }

        @Override
        public void presimular(int accion) {
            automata.actualizar(robot, true);
        }

        @Override
        public void postsimular(int accion) {
            automata.actualizar(robot, false);
        }
    };

    public Cinta(Automata automata, String robot) {
        super(automata);
        this.robot = robot;
    }
}
