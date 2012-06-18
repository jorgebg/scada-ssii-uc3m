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

    protected IMaestro maestro;
    public String[] entradas;
    public String salida;

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
                char codificacion;
                synchronized (sensores) {
                    codificacion = (char) sensores.codificar();
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void recuperar() {
        throw new UnsupportedOperationException("Not supported yet.");
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

    @Override
    public char ejecutar(char sensores) {
        System.out.println("Recibo: " + (int) sensores);
        if (sensores != Character.MAX_VALUE) {
            if (tieneEntrada()) {
                for (String entrada : entradas) {
                    if (this.sensores.get(entrada, sensores) && !this.sensores.get(entrada)) {
                        this.sensores.set(entrada, true);
                    }
                }


            }
            if (tieneSalida() && !this.sensores.get(salida, sensores) && this.sensores.get(salida)) {
                this.sensores.set(salida, false);
            }
            /*
             * Sensores clone = this.sensores.clone();
             * clone.actualizar(sensores); System.out.println(" Recibo: \n" +
             * clone);
             */
        }

        return ejecutar();
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
