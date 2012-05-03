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
public class Robot1 extends SubAutomata {

    Parametros parametros = new Parametros("tiempo-recogida", "tiempo-transporte", "tiempo-transporte-montado");

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

    class TransporteEngranaje1 extends Transporte {

        public void postsimular(int accion) {
            switch (accion) {
                case RECOGER:
                    automata.actualizar("A", false);
                    break;
                case TRANSPORTAR:
                    automata.actualizar("D", true);
                    break;
            }
        }
    };

    class TransporteEje1 extends Transporte {

        @Override
        public void postsimular(int accion) {
            switch (accion) {
                case RECOGER:
                    automata.actualizar("B", false);
                    break;
                case TRANSPORTAR:
                    automata.actualizar("C", true);
                    break;
            }
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
        public void postsimular(int accion) {
            switch (accion) {
                case RECOGER:
                    automata.actualizar("E", false);
                    break;
                case TRANSPORTAR:
                    automata.actualizar("F", true);
                    break;
            }
        }
    }

    public Robot1(Automata automata) {
        super(automata);
    }
}