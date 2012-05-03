package infiniware.scada;
import infiniware.automatas.sensores.Sensores;
import infiniware.remoto.Ethernet;
import infiniware.scada.modelos.Parametros;

public interface IMaestro extends Ethernet {
    public void inicializar();
    public void arrancar();
    public void detenerProceso();
    public void emergencia();

    public void configurar(Parametros parametros);
    public void ciclo(Sensores sensores);

    public void parada();

    public void desconectar();

    public void conectar();
}
