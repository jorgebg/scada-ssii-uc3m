package infiniware.scada.simulador;

import infiniware.scada.IMaestro;
import infiniware.automatas.maestro.Maestro;
import infiniware.scada.Scada;

public class Simulador implements ISimulador {

    public Scada scada = Scada.INSTANCIA;
    public IMaestro maestro = Maestro.INSTANCIA;
    public static Simulador INSTANCIA = new Simulador();

    private Simulador() {
    }

    public void ciclo() {
        //TODO
    }

    /**
     * @UC 010
     * @UC 011
     * @UC 012
     */
    @Override
    public void provocarFalloEsclavo(byte esclavo) {
        maestro.provocarFalloEsclavo(esclavo);
    }

    /**
     * @UC 013
     * @UC 014
     * @UC 015
     */
    @Override
    public void recuperarFalloEsclavo(byte esclavo) {
        maestro.recuperarFalloEsclavo(esclavo);
    }

    /**
     * @UC 007
     */
    @Override
    public void limpiarCPD() {
        scada.acciones.add(
                new Runnable() {
                    public void run() {
                        scada.maestro.limpiarCPD();
                    }
                });
    }
}
