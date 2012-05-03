/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.procesos;

/**
 *
 * @author jorge
 */
public interface IProceso {
    public Thread iniciarProceso();
    public void detenerProceso();
}
