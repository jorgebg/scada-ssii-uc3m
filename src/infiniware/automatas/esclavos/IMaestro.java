package infiniware.automatas.esclavos;
import infiniware.remoto.Profibus;
import java.rmi.RemoteException;


public interface IMaestro extends Profibus {
    public void notificar(byte esclavo, char sensores) throws RemoteException;
    public void log(String s) throws RemoteException;
}
