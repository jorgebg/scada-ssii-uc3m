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
public class CintaSalida extends CintaCapacidad {

    public CintaSalida(String salida, String entrada) {
        super(salida, entrada);
    }

    class Movimiento extends CintaCapacidad.Movimiento {
    }

    @Override
    protected void manipular() {
        if (contenido[contenido.length - 1]) {
            contenido[contenido.length - 1] = Math.random() > 0.75;
            if (!contenido[contenido.length - 1]) {
                automata.log("Se ha quitado un conjunto valido de " + CintaSalida.this.nombre + ". Hay " + contar() + "/" + contenido.length + " en la cinta.");
            }
        }
    }

    public CintaSalida(String salida) {
        super(salida);
    }

    public void ponerConjuntoValido() {
        contenido[0] = true;
    }
}