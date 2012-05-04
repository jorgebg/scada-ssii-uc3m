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

    public char ejecutar(int index, Sensores sensores) {
        return ejecutar(index, sensores.codificar());
    }

    public char ejecutar(int index, char sensores) {
        checkIndex(index);
        try {
            return get(index).ejecutar(sensores);
        } catch (RemoteException ex) {
            System.err.println("Error al llamar remotamente a 'ciclo' en el esclavo " + index + ".");
            ex.printStackTrace(System.err);
        }
        return 0;
    }
    
    public void put(byte index, IEsclavo esclavo) {
        put((int)index, esclavo);
    }

}
