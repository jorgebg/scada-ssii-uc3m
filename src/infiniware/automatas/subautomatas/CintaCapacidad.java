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


    Parametros parametros = new Parametros("velocidad", "longitud", "capacidad");
    
    class Movimiento extends Cinta.Movimiento {
        

        @Override
        public void presimular(int accion) {
            super.presimular(accion);
            //TODO controlar capacidad
        }

        @Override
        public void postsimular(int accion) {
            super.postsimular(accion);
            //TODO controlar capacidad
        }
    }
    
    public CintaCapacidad(Automata automata, String robot) {
        super(automata, robot);
    }
}