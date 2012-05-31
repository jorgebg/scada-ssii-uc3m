/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.automatas.subautomatas;

import infiniware.automatas.Automata;
import infiniware.automatas.sensores.Sensores;
import infiniware.scada.modelos.Parametros;
import infiniware.scada.simulador.Simulacion;

/**
 *
 * @author jorge
 */

public class Estacion extends SubAutomata {

    final String[] entrada;
    final String salida;

    
    class Montaje extends Simulacion {

        @Override
        public long tiempo(int accion) {
            return parametros.get("velocidad");
        }

        @Override
        public void preaccion(int accion) {
            Sensores sensores = new Sensores(entrada);
            automata.actualizar(sensores);
        }

        @Override
        public void postaccion(int accion) {
            automata.actualizar(salida, true);
        }
    };

    public Estacion(String entrada, String salida) {
        this(new String[] { entrada }, salida);
    }
    
    
    public Estacion(String[] entrada, String salida) {
        super();
        this.entrada = entrada;
        this.salida = salida;
        //parametros = new Parametros("tiempo");
        configurar(new Parametros() {{
          put("tiempo", 2);
        }});
    }
}