/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.scada.ui;

import infiniware.scada.IScada;
import infiniware.scada.Scada;
import infiniware.scada.informes.Informes;
import infiniware.scada.modelos.ConjuntoParametros;
import infiniware.scada.modelos.Parametros;
import infiniware.scada.simulador.ISimulador;
import infiniware.scada.simulador.Simulador;

/**
 *
 * @author jorge
 */
public abstract class Ui implements IUi {
    
    IScada scada = Scada.INSTANCIA;
    ISimulador simulador = Simulador.INSTANCIA;
    
    public void iniciar() {
        scada.iniciar();
    }

    public void arrancar() {
        scada.arrancar();
    }

    public Parametros cargarConfiguracion(String nombre) {
        return scada.cargarConfiguracion(nombre);
    }

    public void configurar(ConjuntoParametros parametros) {
        scada.configurar(parametros);
    }

    public void emergencia() {
        scada.emergencia();
    }

    public Informes generarInforme() {
        return scada.generarInforme();
    }

    public void guardarConfiguracion(String nombre, Parametros parametros) {
        scada.guardarConfiguracion(nombre, parametros);
    }

    public void parada() {
        scada.parada();
    }

    public void limpiarCPD() {
        simulador.limpiarCPD();
    }

    public void provocarFalloEsclavo1() {
        simulador.provocarFalloEsclavo((byte)2);
    }

    public void provocarFalloEsclavo2() {
        simulador.provocarFalloEsclavo((byte)2);
    }

    public void provocarFalloEsclavo3() {
        simulador.provocarFalloEsclavo((byte)3);
    }

    public void recuperarFalloEsclavo1() {
        simulador.recuperarFalloEsclavo((byte)1);
    }

    public void recuperarFalloEsclavo2() {
        simulador.recuperarFalloEsclavo((byte)2);
    }

    public void recuperarFalloEsclavo3() {
        simulador.recuperarFalloEsclavo((byte)3);
    }
    
    public abstract void actualizar(char[] estados);
}
