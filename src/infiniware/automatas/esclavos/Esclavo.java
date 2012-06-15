package infiniware.automatas.esclavos;

import infiniware.automatas.Automata;
import infiniware.automatas.GestorInstancias;
import infiniware.automatas.GestorSubAutomatas;
import infiniware.automatas.maestro.Maestro;
import infiniware.automatas.sensores.Sensores;
import java.rmi.RemoteException;

public abstract class Esclavo extends Automata implements IEsclavo {

    public enum Simulaciones { LimpiarCPD };
    
    protected IMaestro maestro;

    public void desconectar() {
        maestro = null;
        super.desconectar();
    }

    public void conectar() {
        this.maestro = this.<IMaestro>conectar(Maestro.INSTANCIA);
    }

    public void actualizar(Sensores sensores) {
        super.actualizar(sensores);
        notificar();
    }

    public void enlazar() {
        super.<IEsclavo>enlazar();
    }

    public void actualizar(String sensor, boolean estado) {
        super.actualizar(sensor, estado);
        notificar();
    }

    private void notificar() {
        boolean exito = true;
        do {
            try {
                System.out.println("Notificando:\n" + sensores);
                maestro.notificar(getId(), (char) sensores.codificar());
                exito = true;
            } catch (RemoteException ex) {
                exito = false;
                System.err.println("Error al notificar al maestro: ");
                conectar();
            }
        } while (!exito);
    }

    public void fallar() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void recuperar() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void simular(Simulaciones simulacion) {
        //Nada por defecto
    }
}
