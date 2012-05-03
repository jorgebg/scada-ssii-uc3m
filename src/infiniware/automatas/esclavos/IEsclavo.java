package infiniware.automatas.esclavos;

import infiniware.remoto.Profibus;
import java.rmi.RemoteException;

public interface IEsclavo extends Profibus {
    public void ciclo(char sensores) throws RemoteException;
    public void inicializar(char sensores) throws RemoteException;
}
