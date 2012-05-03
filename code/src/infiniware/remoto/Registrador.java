package infiniware.remoto;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class Registrador {

    public static Thread thread;
    private static Registry registry;
    private static Map<String, IRegistrable> enlaces = new HashMap<String, IRegistrable>();
    private static final Object monitor = new Object();

    public static <T extends Remote> void enlazar(IRegistrable remoto) throws RemoteException {
        String name = remoto.getRemoteName();
        if (enlaces.containsKey(name)) {
            System.err.println("No se puede enlazar \"" + name + "\": ya esta enlazado.");
        } else {
            T stub = (T) UnicastRemoteObject.exportObject((Remote) remoto, remoto.getPort());
            if (registry == null) {
                crearRegistro(remoto);
            }
            registry.rebind(name, stub);
            enlaces.put(name, remoto);
        }
    }

    public static void desenlazar(IRegistrable remoto) throws RemoteException, NotBoundException {
        desenlazar(remoto, true);
    }

    public static void desenlazar(IRegistrable remoto, boolean finalizar) throws RemoteException, NotBoundException {
        String name = remoto.getRemoteName();
        if (!enlaces.containsKey(name)) {
            System.err.println("No se puede desenlazar \"" + name + "\": no esta enlazado.");
        } else {
            registry.unbind(name);
            UnicastRemoteObject.unexportObject((Remote) enlaces.get(name), true);
            enlaces.remove(name);
        }
        if (finalizar) {
            synchronized (monitor) {
                monitor.notify();
            }
        }
    }

    public static Thread crearRegistro(final IRegistrable remoto) throws RemoteException {
        registry = LocateRegistry.createRegistry(remoto.getPort());
        thread = new Thread(new Runnable() {
            public void run() {
                try {
                    synchronized (monitor) {
                        monitor.wait();
                    }
                }catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
            }
        }, "Infiniware RMI Registry Thread");
        thread.start();
        System.out.println("Servicio de registro RMI creado para \""+remoto.getRemoteName()+"\" en "+remoto.getHost()+":"+remoto.getPort());
        return thread;
    }
}
