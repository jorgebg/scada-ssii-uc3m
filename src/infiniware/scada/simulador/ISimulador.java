/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.scada.simulador;

/**
 *
 * @author jorge
 */
public interface ISimulador {

    /**
     * @UC 007
     */
    void limpiarCPD();

    /**
     * @UC 010
     * @UC 011
     * @UC 012
     */
    void provocarFalloEsclavo(byte esclavo);

    /**
     * @UC 013
     * @UC 014
     * @UC 015
     */
    void recuperarFalloEsclavo(byte esclavo);
    
}
