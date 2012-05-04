package infiniware.scada;

import infiniware.automatas.sensores.Sensores;
import infiniware.remoto.Ethernet;
import infiniware.scada.modelos.ConjuntoParametros;

public interface IMaestro extends Ethernet {

    public void inicializar();

    public void arrancar();

    public void detenerProceso();

    public void emergencia();

    public void configurarAutomatas(ConjuntoParametros parametros);

    public char[] ciclo(Sensores sensores);

    public void parada();

    public void desconectar();

    public void conectar();

    public void provocarFalloEsclavo(byte esclavo);

    public void recuperarFalloEsclavo(byte esclavo);
}
