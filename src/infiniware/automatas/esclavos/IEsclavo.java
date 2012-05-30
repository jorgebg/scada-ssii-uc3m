package infiniware.automatas.esclavos;

import infiniware.remoto.Profibus;
import infiniware.scada.modelos.Parametros;
import java.rmi.RemoteException;
import java.util.Map;

public interface IEsclavo extends Profibus {
    public char ejecutar(char sensores, char mascara) throws RemoteException;
    public void inicializar(char sensores) throws RemoteException;
    public void configurar(Map<String, Parametros> parametros) throws RemoteException;
    public void fallar() throws RemoteException;
    public void recuperar() throws RemoteException;
}
