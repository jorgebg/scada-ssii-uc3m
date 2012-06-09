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
public class EstacionValidacion extends Estacion {

    class Ocupada extends Estacion.Ocupada {

        static final int PROCESAR = 0;
        static final int OPERARIO = 1;

        public Ocupada() {
            acciones = 2;
        }

        public long tiempo(int accion) {
            switch (accion) {
                case PROCESAR:
                    return parametros.get("tiempo");
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
                    boolean ok = Math.random() > 0.10;
                    System.out.println("Operario: pieza " + (ok ? "VALIDA" : "INVALIDA"));
                    automata.actualizar("OK", ok);
                    super.postaccion(accion);
            }
        }
    };

    public EstacionValidacion(String entrada, String salida) {
        super(entrada, salida);
    }
}
