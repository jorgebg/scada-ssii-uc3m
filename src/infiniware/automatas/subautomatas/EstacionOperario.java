/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.automatas.subautomatas;

import infiniware.automatas.Automata;

/**
 *
 * @author jorge
 */
public class EstacionOperario extends Estacion {

    class Montaje extends Estacion.Montaje {

        int acciones = 2;
        static final int PROCESAR = 0;
        static final int OPERARIO = 1;

        public long tiempo(int accion) {
            switch (accion) {
                case PROCESAR:
                    return parametros.get("velocidad");
                case OPERARIO:
                    return (long) (Math.random() * 1000);
            }
            return -1;
        }

        public void presimular(int accion) {
            if (accion == PROCESAR) {
                super.presimular(accion);
            }
        }

        public void postsimular(int accion) {
            switch (accion) {
                case PROCESAR:
                    automata.actualizar(salida, true);
                case OPERARIO:
                    automata.actualizar("OK", Math.random() > 0.90);
            }
        }
    };

    public EstacionOperario(Automata automata, String entrada, String salida) {
        super(automata, entrada, salida);
    }
}
