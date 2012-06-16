/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.automatas.subautomatas;

import infiniware.automatas.Automata;
import infiniware.automatas.subautomatas.Robot.Transporte;
import infiniware.scada.modelos.Parametros;
import infiniware.scada.simulador.Simulacion;

/**
 *
 * @author jorge
 */
public class Robot1 extends Robot {

    class Reposo extends Robot.Reposo {
    }

    class TransporteEngranaje1 extends Transporte {

        public void postaccion(int accion) {
            switch (accion) {
                case RECOGER:
                    automata.actualizar("A", false);
                    break;
                case TRANSPORTAR:
                    automata.actualizar("D", true);
                    break;
            }
            super.postaccion(accion);
        }
    };

    class TransporteEje1 extends Transporte {

        @Override
        public void postaccion(int accion) {
            switch (accion) {
                case RECOGER:
                    automata.actualizar("B", false);
                    break;
                case TRANSPORTAR:
                    automata.actualizar("C", true);
                    break;
            }
            super.postaccion(accion);
        }
    };

    class TransporteEngranaje2 extends TransporteEngranaje1 {
    }

    class TransporteEje2 extends TransporteEje1 {
    }

    class TransporteConjuntoMontado extends Transporte {

        @Override
        public long tiempo(int accion) {
            switch (accion) {
                case RECOGER:
                    return parametros.get("tiempo-recogida");
                case TRANSPORTAR:
                    return parametros.get("tiempo-transporte-montado");
            }
            return -1;
        }

        @Override
        public void postaccion(int accion) {
            switch (accion) {
                case RECOGER:
                    automata.actualizar("E", false);
                    break;
                case TRANSPORTAR:
                    automata.actualizar("F", true);
                    break;
            }
            super.postaccion(accion);
        }
    }

    public Robot1() {
        super("R1");
        //parametros = super.parametros.mezclarTodo("tiempo-transporte-montado");
        super.parametros.put("tiempo-transporte-montado", 5000);
    }
}