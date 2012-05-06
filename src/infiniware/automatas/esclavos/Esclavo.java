package infiniware.automatas.esclavos;

import infiniware.automatas.Automata;
import infiniware.automatas.GestorSubAutomatas;
import infiniware.automatas.maestro.Maestro;
import infiniware.automatas.sensores.Sensores;
import java.rmi.RemoteException;

public abstract class Esclavo extends Automata implements IEsclavo {

    protected IMaestro maestro;
    
    public void desconectar() {
        maestro = null;
        super.desconectar();
    }

    public void conectar() {
        this.maestro = this.<IMaestro>conectar(Maestro.INSTANCIA);
    }

    public void actualizar(Sensores postcondiciones) {
        super.actualizar(postcondiciones);
        notificar();
    }
    
    public void enlazar() {
        super.<IEsclavo>enlazar();
    }
    
    
    public void actualizar(String sensor, boolean estado) {
        super.actualizar(sensores);
        notificar();
    }

    private void notificar() {
       
        try {
            maestro.notificar(getId(), (char)sensores.codificar());
        } catch (RemoteException ex) {
            System.err.println("Error al notificar al maestro: ");
            ex.printStackTrace(System.err);
        }
    }


    public void fallar() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void recuperar() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
