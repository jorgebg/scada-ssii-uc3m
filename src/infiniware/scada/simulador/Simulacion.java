package infiniware.scada.simulador;

public abstract class Simulacion implements Runnable {

    int acciones = 1;

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

    public void postaccion(int accion){
        
    }

    protected void actuar(int accion) {
        long tiempoSimulado = tiempo(accion);
        dormir(tiempoSimulado);
    }

    protected void dormir(long tiempo) {
        try {
            Thread.sleep(tiempo);
        } catch (InterruptedException ex) {
            System.err.println("Error al dormir la simulacion: " + ex.getMessage());
        }
    }
}
