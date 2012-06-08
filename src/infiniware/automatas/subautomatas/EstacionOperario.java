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

        static final int PROCESAR = 0;
        static final int OPERARIO = 1;

        public Montaje() {
            acciones = 2;
        }

        public long tiempo(int accion) {
            switch (accion) {
                case PROCESAR:
                    return parametros.get("velocidad");
                case OPERARIO:
                    return (long) (Math.random() * 1000);
            }
            return -1;
        }

        public void preaccion(int accion) {
            if (accion == PROCESAR) {
                super.preaccion(accion);
            }
        }

        public void postaccion(int accion) {
            switch (accion) {
                case PROCESAR:
                    automata.actualizar(salida, true);
                case OPERARIO:
                    automata.actualizar("OK", Math.random() > 0.90);
            }
        }
    };

    public EstacionOperario(String entrada, String salida) {
        super(entrada, salida);
    }
}
