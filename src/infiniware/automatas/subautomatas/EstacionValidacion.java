/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.automatas.subautomatas;

import infiniware.automatas.Automata;
import infiniware.automatas.sensores.Sensores;

/**
 *
 * @author jorge
 */
public class EstacionValidacion extends Estacion {

    final String ok;

    class Ocupada extends Estacion.Ocupada {

        public void postaccion(int accion) {
            boolean OK = Math.random() <= 0.9;
            automata.actualizar(entrada, false);
            automata.actualizar(salida, true);
            automata.actualizar(ok, OK);
            automata.log(EstacionValidacion.this.nombre + " ha terminado. Resultado: " + (OK ? "VALIDO" : "NO VALIDO"));
        }
    };

    public EstacionValidacion(String entrada, String salida, String ok) {
        super(entrada, salida);
        this.ok = ok;
    }

    public EstacionValidacion(String entrada, String salida) {
        this(entrada, salida, "OK");
    }
}
