/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.automatas.subautomatas;

import infiniware.scada.modelos.Parametros;
import infiniware.scada.simulador.Simulacion;

/**
 *
 * @author jorge
 */
public abstract class Robot extends SubAutomata {

    final String robot;

    public Robot(String robot) {
        super();
        this.robot = robot;
        configurar(new Parametros() {{
          put("tiempo-recogida", 5000);
          put("tiempo-transporte", 5000);
        }});
        //parametros = new Parametros("tiempo-recogida", "tiempo-transporte");
        //System.out.println(this.simulaciones);
    }

    abstract class Reposo extends Simulacion {

        @Override
        public void postaccion(int accion) {
            //System.out.println(robot + true);
            automata.actualizar(robot, true);
            //System.out.println(robot + automata.sensores);
        }
    }

    abstract class Transporte extends Simulacion {

        static final int RECOGER = 0;
        static final int TRANSPORTAR = 1;

        public Transporte() {
            acciones = 2;
        }

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

        @Override
        public void preaccion(int accion) {
            if (accion == 0) {
                automata.actualizar(robot, false);
            }
        }

        @Override
        public void postaccion(int accion) {
            if (accion == acciones - 1) {
                automata.actualizar(robot, true);
            }
        }
    }
}
