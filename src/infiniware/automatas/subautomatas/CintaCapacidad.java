/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.automatas.subautomatas;

import infiniware.automatas.Automata;
import infiniware.scada.modelos.Parametros;

/**
 *
 * @author jorge
 */
public class CintaCapacidad extends Cinta {

    
    Boolean[] contenido;

    class Movimiento extends Cinta.Movimiento {

        static final int MOVER = 0;
        static final int CARGAR = 1;

        public Movimiento() {
            acciones = 2;
        }

        @Override
        public long tiempo(int accion) {
            switch (accion) {
                case MOVER:
                    return super.tiempo(accion);
                case CARGAR:
                    return 2000; //TODO nuevo parametro?
            }
            return -1;
        }

        @Override
        public void postaccion(int accion) {
            switch (accion) {
                case MOVER:
                    desplazar();
                    if(contenido[contenido.length-1])
                        automata.actualizar(salida, true);
                        if(entrada!=null)
                            automata.actualizar(entrada, false);
                    break;
                case CARGAR:
                    contenido[0] = Math.random() > 0.75;
                    break;
            }
        }

        private void desplazar() {
            for (int i = contenido.length-1; i > 0; i--) {
                contenido[i] = contenido[i-1];
            }
            contenido[0] = false;
        }
    }

    public CintaCapacidad(String salida) {
        super( salida);
        configurar(new Parametros() {{
          put("velocidad", 1);
          put("longitud", 5);
          put("capacidad", 5);  
        }});
        //parametros = new Parametros("velocidad", "longitud", "capacidad");
    }

    public void configurar(Parametros parametros) {
        super.configurar(parametros);
        if(parametros.containsKey("capacidad")) {
            contenido = new Boolean[parametros.get("capacidad")];
            for (int i = 0; i < contenido.length; i++) {
                contenido[i] = true;
            }
        }
    }
}