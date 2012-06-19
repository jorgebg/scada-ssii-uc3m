package infiniware.automatas.esclavos;

import infiniware.automatas.Automata;
import infiniware.automatas.GestorInstancias;
import infiniware.automatas.GestorSubAutomatas;
import infiniware.automatas.maestro.Maestro;
import infiniware.automatas.sensores.Sensores;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Esclavo extends Automata implements IEsclavo {

    public IMaestro maestro;
    public String[] entradas;
    public String salida;

    public void desconectar() {
        maestro = null;
        super.desconectar();
    }

    public void conectar() {
        this.maestro = this.<IMaestro>conectar(Maestro.INSTANCIA);
    }

    public void enlazar() {
        super.<IEsclavo>enlazar();
    }

    public void actualizar(String sensor, boolean estado) {
        super.actualizar(sensor, estado);
        notificar(sensor, estado);
    }

    private void notificar(String sensor, boolean estado) {
        boolean exito = true;
        do {
            try {
                System.out.println("Notificando: " + sensor + "=" + (estado?"1":"0"));
                char codificacion;
                synchronized (sensores) {
                    codificacion = (char) sensores.codificar(sensor, estado);
                }
                maestro.notificar(getId(), codificacion);

                exito = true;
            } catch (RemoteException ex) {
                exito = false;
                System.err.println("Error al notificar al maestro: ");
                conectar();
            }
        } while (!exito);
    }

    public void fallar() {
        emergencia();
    }


    @Override
    public void log(String msg) {
        System.out.println(msg);
        try {
            maestro.log(msg);
        } catch (RemoteException ex) {
            System.err.println("Error al comunicar log al maestro");
        }
    }


    public boolean tieneEntrada() {
        return entradas != null;
    }

    public boolean tieneSalida() {
        return salida != null;
    }

    @Override
    public abstract byte getId();

}
