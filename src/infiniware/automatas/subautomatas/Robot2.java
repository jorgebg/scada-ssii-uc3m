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
public class Robot2 extends Robot {

    class Reposo extends Robot.Reposo {
    }
    
    class MueveConjuntoMontado extends Transporte {

        public void postaccion(int accion) {
            switch (accion) {
                case RECOGER:
                    automata.actualizar("G", false);
                    break;
                case TRANSPORTAR:
                    automata.actualizar("H", true);
                    break;
            }
            super.postaccion(accion);
        }
    };

    class MueveConjuntoSoldado extends Transporte {

        @Override
        public long tiempo(int accion) {
            switch (accion) {
                case RECOGER:
                    return parametros.get("tiempo-recogida");
                case TRANSPORTAR:
                    return parametros.get("tiempo-transporte-soldado");
            }
            return -1;
        }

        @Override
        public void postaccion(int accion) {
            switch (accion) {
                case RECOGER:
                    automata.actualizar("I", false);
                    break;
                case TRANSPORTAR:
                    automata.actualizar("J", true);
                    break;
            }
            super.postaccion(accion);
        }
    }

    class MueveConjuntoValido extends Transporte {

        @Override
        public void postaccion(int accion) {
            switch (accion) {
                case RECOGER:
                    automata.actualizar("K", false);
                    break;
                case TRANSPORTAR:
                    automata.actualizar("L", true);
                    break;
            }
            super.postaccion(accion);
        }
    }

    class MueveConjuntoNoValido extends Transporte {

        @Override
        public void postaccion(int accion) {
            switch (accion) {
                case RECOGER:
                    automata.actualizar("K", false);
                    break;
                case TRANSPORTAR:
                    automata.actualizar("M", true);
                    break;
            }
            super.postaccion(accion);
        }
    }

    public Robot2() {
        super("R2");
        //parametros = super.parametros.mezclarTodo("tiempo-transporte-soldado");
        super.parametros.put("tiempo-transporte-soldado", 2);
    }
}