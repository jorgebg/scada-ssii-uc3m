package infiniware.scada.simulador;

import infiniware.automatas.subautomatas.SubAutomata;
import infiniware.scada.Scada;

public abstract class Simulacion implements Runnable {

    protected int acciones = 1;
    public SubAutomata subautomata;

    public void run() {
        for (int accion = 0; accion < acciones; accion++) {
            preaccion(accion);
            actuar(accion);
            postaccion(accion);
        }
    }

    public Thread lanzar() {
        Thread thread = new Thread(this);
        thread.start();
        return thread;
    }

    public long tiempo(int accion) {
        return 0;
    }

    public void preaccion(int accion) {
    }

    public void postaccion(int accion) {
    }

    protected void actuar(int accion) {
        long tiempoSimulado = tiempo(accion);
        if (tiempoSimulado > 0) {
            System.out.println(subautomata.nombre + ": simulacion [" + this.getClass().getSimpleName() + "] durmiendo: " + tiempoSimulado + "ms");
            dormir(tiempoSimulado);
            System.out.println(subautomata.nombre + ": simulacion [" + this.getClass().getSimpleName() + "] despertando");
        }
    }

    protected void dormir(long tiempo) {
        int tiempoPorIteracion = Scada.CICLO;
        long inicial, diferencia, tiempoTotal = 0;
        while (tiempoTotal < tiempo) {

            inicial = System.currentTimeMillis();
            try {
                Thread.sleep(tiempoPorIteracion);
            } catch (InterruptedException ex) {
                System.err.println("Error al dormir la simulacion: " + ex.getMessage());
            }
            if (!subautomata.emergencia) {
                diferencia = System.currentTimeMillis() - inicial;
                tiempoTotal += diferencia;
            } else {
                //inicial = System.currentTimeMillis();
                //System.out.println(this.subautomata.nombre + ".emergencia = " + subautomata.emergencia);
            }

            //System.out.println(this.subautomata.nombre + "[" + this.getClass().getSimpleName() + "] " + tiempoTotal + "/" + tiempo);
        }
    }

    public String obtenerNombre() {
        return this.getClass().getSimpleName();
    }
}
