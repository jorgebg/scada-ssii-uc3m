package infiniware.automatas.maestro;

import infiniware.automatas.esclavos.IEsclavo;
import infiniware.automatas.sensores.Sensores;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

class GestorEsclavos extends HashMap<Integer, IEsclavo> {

    private boolean checkIndex(int index) {
        if (index < 1 || index > 3) {
            System.err.println("Id de esclavo no valido (" + index + ")");
            return false;
        }
        return true;
    }

    public void ciclo(int index, Sensores sensores) {
        ciclo(index, sensores.codificar());
    }

    public void ciclo(int index, char sensores) {
        checkIndex(index);
        try {
            get(index).ciclo(sensores);
        } catch (RemoteException ex) {
            System.err.println("Error al llamar remotamente a 'ciclo' en el esclavo " + index + ".");
            ex.printStackTrace(System.err);
        }
    }
    
    public void put(byte index, IEsclavo esclavo) {
        put((int)index, esclavo);
    }

}
