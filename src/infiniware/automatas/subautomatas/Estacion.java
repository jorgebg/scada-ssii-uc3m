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

    
    class Ocupada extends Simulacion {

        @Override
        public long tiempo(int accion) {
            return parametros.get("tiempo");
        }


        @Override
        public void postaccion(int accion) {
            Sensores sensores = new Sensores(entrada, false);
            sensores.insertar(salida, true);
            automata.actualizar(sensores);
            automata.log(Estacion.this.nombre + " ha terminado.");
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
          put("tiempo", 3000);
        }});
    }
}