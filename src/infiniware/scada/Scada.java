package infiniware.scada;

import infiniware.automatas.Automata;
import infiniware.automatas.maestro.Maestro;
import infiniware.automatas.sensores.Sensores;
import infiniware.procesos.IProcesable;
import infiniware.remoto.Ethernet;
import infiniware.remoto.Registrador;
import infiniware.scada.informes.Informes;
import infiniware.scada.modelos.ConjuntoParametros;
import infiniware.scada.modelos.Parametros;
import infiniware.scada.simulador.Simulador;

public class Scada implements Ethernet, IProcesable, IScada {

    private static final int CICLO = 200;
    public Sensores sensores;
    Simulador simulador;
    GestorAlmacenamiento almacenamiento;
    public IMaestro maestro;
    long timestamp;
    boolean emergencia = false;
    public Acciones acciones;
    public static Scada INSTANCIA = new Scada();
    
    /**
     * Estados de los automata: id {0..3} => estado {0..9}
     * Para obtener el nombre de los estados:
     * 
     *  for (int id = 0; id < estados.length; id++) {
     *      char estado = estados[i];
     *      Automata.INSTANCIAS.get(id).estados.get(estado);
     *  }
     * 
     */
    char[] estados;

    private Scada() {
        acciones = new Acciones();
    }

    private void sincronizar() {
        long tiempoPendiente = CICLO - (System.currentTimeMillis() - timestamp);
        if (tiempoPendiente > 0) {
            try {
                Thread.sleep(tiempoPendiente);
            } catch (InterruptedException ex) {
                System.err.println("Error al sincronizar el proceso: " + ex.getMessage());
            }
        } else {
            System.err.println("Latencia: " + -tiempoPendiente + "ms");
        }
        timestamp = System.currentTimeMillis();
    }

    private void ciclo() {
        if (emergencia) {
            maestro.emergencia();
            this.emergencia = false;
        } else {
            for (Runnable accion : acciones) {
                accion.run();
            }
            simulador.ciclo();
            estados = maestro.ciclo(sensores);
            sincronizar();
        }
    }

    /**
     * @UC 001
     */
    @Override
    public void iniciar() {
        maestro.inicializar();
        arrancar();
    }

    /**
     * @UC 002
     */
    @Override
    public void arrancar() {
        timestamp = System.currentTimeMillis();
        while (true) {
            ciclo();
        }
    }

    /**
     * @UC 003
     */
    @Override
    public void configurar(ConjuntoParametros parametros) {
        maestro.configurarAutomatas(parametros);
    }
    
    /**
     * @UC 004
     */
    @Override
    public void guardarConfiguracion(String nombre, Parametros parametros) {
        almacenamiento.configuracion.guardar(nombre, parametros);
    }

    /**
     * @UC 005
     */
    @Override
    public Parametros cargarConfiguracion(String nombre) {
        return almacenamiento.configuracion.cargar(nombre);
    }

    /**
     * @UC 006
     */
    @Override
    public Informes generarInforme() {
        return Informes.generar(this);
    }




    /**
     * @UC 008
     */
    @Override
    public void parada() {
        acciones.add(
                new Runnable() {
                    public void run() {
                        maestro.parada();
                    }
                });
    }

    /**
     * @UC 009
     */
    @Override
    public void emergencia() {
        this.emergencia = true;
    }

    /* IProceso {{{ */
    public Thread iniciarProceso() {
        simulador = Simulador.INSTANCIA;
        maestro = Maestro.INSTANCIA;
        return Registrador.thread;
    }
    
    @Override
    public void detenerProceso() {
        simulador = null;
        maestro = null;
    }
    /* }}} */
}
