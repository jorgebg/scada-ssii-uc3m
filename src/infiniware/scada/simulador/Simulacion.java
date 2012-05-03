package infiniware.scada.simulador;

import infiniware.automatas.Transicion;

public class Simulacion implements Runnable {

    private final Transicion transicion;

    public Simulacion(Transicion transicion) {
        this.transicion = transicion;
    }

    public void run() {
        transicion.subautomata.automata.actualizar(transicion.precondiciones);
        long tiempoSimulado = calcularTiempo();
        try {
            Thread.sleep(tiempoSimulado);
        } catch (InterruptedException ex) {
            System.err.println("Error al simular el tiempo: " + ex.getMessage());
        }
        
        transicion.subautomata.estado = transicion.destino;
        transicion.subautomata.automata.actualizar(transicion.postcondiciones);
    }

    private long calcularTiempo() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Thread lanzar() {
        Thread thread = new Thread(this);
        thread.start();
        return thread;
    }
}
