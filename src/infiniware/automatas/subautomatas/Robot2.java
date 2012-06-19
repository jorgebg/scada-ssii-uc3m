/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.automatas.subautomatas;

import infiniware.automatas.Automata;
import infiniware.automatas.maestro.Maestro;
import infiniware.scada.informes.Informes;
import infiniware.scada.modelos.Parametros;

/**
 *
 * @author jorge
 */
public class Robot2 extends Robot {
    public final CPD cpd = new CPD();

    class Reposo extends Robot.Reposo {
    }
    
        private Maestro comoMaestro() {
            return (Maestro) automata;
        }
    class MueveConjuntoMontado extends Transporte {

        public void postaccion(int accion) {
            switch (accion) {
                case RECOGER:
                    comoMaestro().actualizar("G", false);
                    break;
                case TRANSPORTAR:
                    comoMaestro().actualizar("H", true);
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
                    comoMaestro().actualizar("I", false);
                    comoMaestro().log("Se ha recogido un conjunto soldado de ES");
                    break;
                case TRANSPORTAR:
                    comoMaestro().actualizar("J", true);
                    comoMaestro().log("Se ha puesto un conjunto soldado en EV");
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
                    comoMaestro().actualizar("K", false);
                    comoMaestro().log("Se ha recogido un conjunto valido de EV");
                    break;
                case TRANSPORTAR:
                    comoMaestro().actualizar("L", true);
                    comoMaestro().log("Se ha depositado un conjunto valido en COK");
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
                    comoMaestro().actualizar("K", false);
                    comoMaestro().log("Se ha recogido un conjunto no valido de EV");
                    break;
                case TRANSPORTAR:
                    cpd.incrementar();
                    comoMaestro().log("Se ha despositado un conjunto no valido en CNOK");
                    break;
            }
            super.postaccion(accion);
        }
    }

    public Robot2() {
        super("R2");
        //parametros = super.parametros.mezclarTodo("tiempo-transporte-soldado");
        super.parametros.put("tiempo-transporte-soldado", 5000);
    }
    
    public class CPD {
        static final String sensor = "M";
        int capacidad = 10;
        int invalidos = 0;
        public void incrementar() {
            if(invalidos < capacidad) {
                invalidos++;
                comoMaestro().addIncorrecto();
                comoMaestro().log("Añadido conjunto al CPD ("+invalidos+"/"+capacidad+")");
            }
            if(invalidos >= capacidad) {
                comoMaestro().actualizar(sensor, true);
                comoMaestro().log("Se ha llenado el CPD");
                comoMaestro().simularLlenadoCPD();    
            }
            else
                comoMaestro().simularCaidaCPD();
        }
        public void limpiar() {
            invalidos = 0;
            comoMaestro().actualizar(sensor, false);
            comoMaestro().log("Se ha vaciado el CPD");
        }
    }
}