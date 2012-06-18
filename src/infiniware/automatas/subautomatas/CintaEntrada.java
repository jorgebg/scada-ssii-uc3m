/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.automatas.subautomatas;

import infiniware.automatas.sensores.Sensores;
import infiniware.scada.modelos.Parametros;

/**
 *
 * @author jorge
 */
public class CintaEntrada extends CintaCapacidad {

    class Movimiento extends CintaCapacidad.Movimiento {
        
    }
    
    @Override
    protected void manipular() {
        contenido[0] = Math.random() < 0.75;
        if (contenido[0]) {
            automata.log("Se ha puesto un " + (salida.equals("A") ? "engranaje" : "eje") + " en " + CintaEntrada.this.nombre + ". Hay " + contar() + "/" + contenido.length + " en la cinta.");
        }
    }

    public CintaEntrada(String salida) {
        super(salida);
    }
}