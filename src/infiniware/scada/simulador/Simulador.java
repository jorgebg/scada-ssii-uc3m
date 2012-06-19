package infiniware.scada.simulador;

import infiniware.scada.IMaestro;
import infiniware.automatas.maestro.Maestro;
import infiniware.scada.Scada;

public class Simulador implements ISimulador {

    public Scada scada = Scada.INSTANCIA;
    public static Simulador INSTANCIA = new Simulador();

    private Simulador() {
    }

    public void ciclo() {
        //TODO
    }

    /**
     * @UC 010 @UC 011 @UC 012
     */
    @Override
    public void provocarFalloEsclavo(byte esclavo) {
        scada.maestro.provocarFalloEsclavo(esclavo);
    }

    /**
     * @UC 013 @UC 014 @UC 015
     */
    @Override
    public void recuperarFalloEsclavo(byte esclavo) {
        scada.maestro.recuperarFalloEsclavo(esclavo);
    }

    /**
     * @UC 007
     */
    @Override
    public void limpiarCPD() {
        scada.maestro.simularLimpiezaCPD();
    }
}
