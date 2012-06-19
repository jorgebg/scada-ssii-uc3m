package infiniware.scada;

import infiniware.Resultado;
import infiniware.automatas.Automata;
import infiniware.automatas.maestro.GestorSensores;
import infiniware.automatas.maestro.Maestro;
import infiniware.automatas.subautomatas.SubAutomata;
import infiniware.procesos.IProcesable;
import infiniware.remoto.Ethernet;
import infiniware.remoto.Registrador;
import infiniware.scada.informes.Informes;
import infiniware.scada.modelos.ConjuntoParametros;
import infiniware.scada.simulador.Simulador;
import infiniware.scada.ui.Ui;
import infiniware.scada.ui.gui.Gui;
import infiniware.scada.ui.cli.Cli;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Scada implements Ethernet, IProcesable, IScada, infiniware.automatas.maestro.IScada {

    public static final int CICLO = 200;
    Simulador simulador;
    GestorAlmacenamiento almacenamiento;
    public IMaestro maestro;
    long timestamp;
   // public final Acciones acciones = new Acciones();
    public final GestorSensores mapaSensores = new GestorSensores();
    public static final Scada INSTANCIA = new Scada();
    //public static Ui ui = Cli.INSTANCIA;
    public static Ui ui = Gui.INSTANCIA;
    /**
     * Estados de los automata: id {0..3} => estado {0..9} Para obtener el
     * nombre de los estados:
     *
     * for (int id = 0; id < estados.length; id++) { char estado = estados[id];
     * Automata.INSTANCIAS.get(id).subautomatas.decodificarEstado(estados[id]);
     * }
     *
     */
    char[] estados;

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
        simulador.ciclo();
        estados = maestro.ciclo();
        ui.actualizar(estados);
        sincronizar();
        
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
        maestro.arrancar();
        Informes.INSTANCIA.getFuncionamiento().addArranques();
    }

    /**
     * @UC 003
     */
    @Override
    public void configurar(ConjuntoParametros parametros) {
        //System.out.println(parametros);
        maestro.configurarAutomatas(parametros);
    }

    /**
     * @UC 004
     */
    @Override
    public Resultado guardarConfiguracion(String nombre, ConjuntoParametros parametros) {
        return almacenamiento.configuracion.guardar(nombre, parametros);
    }

    /**
     * @UC 005
     */
    @Override
    public Resultado cargarConfiguracion(String nombre, ConjuntoParametros parametros) {
        return almacenamiento.configuracion.cargar(nombre, parametros);
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
        maestro.parada();
        Informes.INSTANCIA.getFuncionamiento().addNormales();
        /*
         * acciones.add( new Runnable() { public void run() { maestro.parada();
         * }
                });
         */
    }

    /**
     * @UC 009
     */
    @Override
    public void emergencia() {
        maestro.emergencia();
           Informes.INSTANCIA.getFuncionamiento().addEmergencia();
    }

    /*
     * IProceso {{{
     */
    public Thread iniciarProceso() {
        ui.mostrar();
        simulador = Simulador.INSTANCIA;
        maestro = Maestro.INSTANCIA;
        /*
         * for (Automata automata : Automata.INSTANCIAS.values()) {
         * automata.imprimirTablaSensores(); }
        System.out.println();
         */
        new Thread(new Runnable() {

            public void run() {
                timestamp = System.currentTimeMillis();
                while (true) {
                    ciclo();
                }
            }
        }).start();
        return Registrador.thread;
    }

    @Override
    public void detenerProceso() {
        simulador = null;
        maestro = null;
        ui.ocultar();
    }
    /*
     * }}}
     */

    public void notificar(byte automata, char sensores) {
        this.mapaSensores.actualizar(automata, sensores);
    }
    static BufferedWriter logfile;

    public static void log(String msg) {
        ui.log(msg);
        System.out.println(msg);
        try {
            if (logfile == null) {
                FileWriter fstream = new FileWriter("log/" + new java.util.Date().getTime() + ".log");
                logfile = new BufferedWriter(fstream);
            }
            logfile.write(msg + "\n");
            logfile.flush();
        } catch (IOException ex) {
            System.err.println("Error al guardar el log");
        }
    }
}
