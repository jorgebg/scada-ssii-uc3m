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
public class Robot2 extends SubAutomata {

    Parametros parametros = new Parametros("tiempo-recogida", "tiempo-transporte", "tiempo-transporte-soldado");

    abstract class Transporte extends Simulacion {

        int acciones = 2;
        static final int RECOGER = 0;
        static final int TRANSPORTAR = 1;

        @Override
        public long tiempo(int accion) {
            switch (accion) {
                case RECOGER:
                    return parametros.get("tiempo-recogida");
                case TRANSPORTAR:
                    return parametros.get("tiempo-transporte");
            }
            return -1;
        }
    }

    class MueveConjuntoMontado extends Transporte {

        public void postsimular(int accion) {
            switch (accion) {
                case RECOGER:
                    automata.actualizar("G", false);
                    break;
                case TRANSPORTAR:
                    automata.actualizar("H", true);
                    break;
            }
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
        public void postsimular(int accion) {
            switch (accion) {
                case RECOGER:
                    automata.actualizar("I", false);
                    break;
                case TRANSPORTAR:
                    automata.actualizar("J", true);
                    break;
            }
        }
    }

    class MueveConjuntoValido extends Transporte {
        @Override
        public void postsimular(int accion) {
            switch (accion) {
                case RECOGER:
                    automata.actualizar("K", false);
                    break;
                case TRANSPORTAR:
                    automata.actualizar("L", true);
                    break;
            }
        }
    }
    class MueveConjuntoNoValido extends Transporte {
        @Override
        public void postsimular(int accion) {
            switch (accion) {
                case RECOGER:
                    automata.actualizar("K", false);
                    break;
                case TRANSPORTAR:
                    automata.actualizar("M", true);
                    break;
            }
        }
    }

    public Robot2(Automata automata) {
        super(automata);
    }
}