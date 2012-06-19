package infiniware.automatas.maestro;

import infiniware.automatas.Automata;
import infiniware.automatas.GestorSubAutomatas;
import infiniware.automatas.esclavos.*;
import infiniware.automatas.sensores.Sensores;
import infiniware.automatas.subautomatas.Cinta;
import infiniware.automatas.subautomatas.Robot2;
import infiniware.automatas.subautomatas.SubAutomata;
import infiniware.scada.Scada;
import infiniware.scada.modelos.ConjuntoParametros;
import infiniware.scada.modelos.Parametros;
import infiniware.scada.simulador.Simulacion;
import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.CORBA.portable.RemarshalException;

public class Maestro extends Automata implements infiniware.scada.IMaestro, infiniware.automatas.esclavos.IMaestro {

    private static class GestorNotificaciones extends ArrayList<Notificacion> {

        public void registrar(int automata, String sensor, boolean estado) {
            this.add(new Notificacion(automata, sensor, estado));
        }
    }

    private static class Notificacion {

        int automata;
        String sensor;
        boolean estado;

        private Notificacion(int automata, String sensor, boolean estado) {
            this.automata = automata;
            this.sensor = sensor;
            this.estado = estado;
        }
    }
    GestorEsclavos esclavos;
    char[] estados;
    public final GestorSensores mapaSensores;
    public static final Maestro INSTANCIA = new Maestro();
    Scada scada = Scada.INSTANCIA;
    /*
     * private Map<String, Boolean> entradasEsperadas; private Map<String,
     * Boolean> salidasPendientes;
     */
    private int ciclo;
    private Boolean parar = true;
    private Boolean arrancar = false;
    private GestorNotificaciones notificaciones;

    protected Maestro() {
        super();
        this.subautomatas = new GestorSubAutomatas(this) {

            {
                instalar("CT", new Cinta("G", "F"));
                instalar("R2", new Robot2());
            }
        };
        esclavos = new GestorEsclavos();
        mapaSensores = new GestorSensores();
        estados = new char[4];
        /*
         * entradasEsperadas = new HashMap<String, Boolean>(); salidasPendientes
         * = new HashMap<String, Boolean>();
         */
        notificaciones = new GestorNotificaciones();
    }

    public void notificar(String sensor, boolean estado) {
        notificar(0, sensor, estado);
    }

    public void notificar(int automata, String sensor, boolean estado) {
        System.out.println("Recibiendo: " + sensor + "=" + (estado ? "1" : "0"));
        synchronized (mapaSensores) {
            this.mapaSensores.set(sensor, estado);
        }
        this.notificaciones.registrar(automata, sensor, estado);
        //notificar();
    }
    /*
     * protected void notificar(byte automata) {
     *
     * for (Esclavo esclavo : Esclavo.INSTANCIAS.esclavos().values()) {
     *
     * synchronized (entradasEsperadas) { if (esclavo.tieneEntrada()) { for
     * (String entrada : esclavo.entradas) { if (entradaEsperada(entrada) &&
     * !mapaSensores.get(entrada)) { desmarcarEsperarEntrada(entrada); } } } }
     * synchronized (salidasPendientes) { if (esclavo.tieneSalida() &&
     * !salidaPendiente(esclavo.salida) && mapaSensores.get(esclavo.salida)) {
     * marcarSalidaPendiente(esclavo.salida); } }
     *
     * }
     * System.out.println("IN: " + (automata == 0 ? "M" : "E" + automata) + " =>
     * M:\n" + this.mapaSensores.automatas.get((int) automata) + "\n"); }
     */
    /*
     * infiniware.automatas.esclavos.IMaestro {{{
     */

    public void notificar(byte automata, char sensor) {
        System.out.println("IN: " + (automata == 0 ? "M" : "E" + automata) + " => M");
        Map.Entry<String, Boolean> decodificado = this.mapaSensores.automatas.get((int) automata).decodificar(sensor);
        notificar(automata, decodificado.getKey(), decodificado.getValue());
        if (decodificado.getKey().equals("F")) {
            ((Cinta) this.subautomatas.get("CT")).ponerConjuntoMontado();
        }

        //this.scada.notificar(automata, sensores);
    }
    /*
     * }}}
     */

    private void ejecutarAutomata(int id, List<Notificacion> notificaciones) {

        String nombre = "M";
        char estadoAnterior = estados[id];
        if (id != 0) {
            nombre = "E" + id;
        }
        boolean exito;
        //System.out.println("OUT: M => " + nombre + ":\n" + this.mapaSensores.automatas.get(id) + "\n");
        do {
            try {
                //REANUDAR
                if (id == 1) {
                    synchronized (arrancar) {
                        if (arrancar) {
                            esclavos.ejecutar(id, REANUDAR);
                            arrancar = false;
                        }
                    }
                }

                //PARAR
                if (id == 1) {
                    synchronized (parar) {
                        if (parar) {
                            esclavos.ejecutar(id, PARAR);
                            parar = false;
                        }
                    }
                }
                char codificacion;
                for (Notificacion notificacion : notificaciones) {
                    if (notificacion.automata != id) {
                        codificacion = (char) this.mapaSensores.automatas.get(id).codificar(notificacion.sensor, notificacion.estado);
                        if (id != 0) {
                            estados[id] = esclavos.ejecutar(id, codificacion);
                        } else {
                            estados[id] = ejecutar(codificacion);
                        }
                    }
                }

                //synchronized (this.mapaSensores) {

                System.out.println("OUT: M => " + nombre);
                //System.out.println("EEH" + (int)mascara + "\n" + this.mapaSensores.actualizados + "\n" + this.mapaSensores.automatas.get(id).actualizados);
                //}
                if (id == 0) {
                    estados[id] = ejecutar(EJECUTAR);
                } else {
                    estados[id] = esclavos.ejecutar(id, EJECUTAR);
                }


                exito = true;
            } catch (RemoteException ex) {
                exito = false;
                System.err.println("Error al llamar remotamente a 'ejecutar' en el esclavo " + id + ".");
                conectarEsclavo(id);
            }
        } while (!exito);


        if (estadoAnterior != estados[id]) {
            //System.out.println(Automata.INSTANCIAS.get(id).subautomatas.obtenerDiferenciaEstados(estadoAnterior, estados[id]));
            informarScada(id, estadoAnterior);
        }
        //System.out.println("M <= E" + id + ":" + this.mapaSensores.automatas.get(id));

    }

    /*
     * infiniware.scada.IMaestro {{{
     */
    public char[] ciclo() {
        synchronized (mapaSensores) {
            System.out.println("== CICLO #" + ciclo + " ==");
            /*
             * System.out.println("EE: " + entradasEsperadas);
             * System.out.println("SP: " + salidasPendientes);
             */
            System.out.println(mapaSensores);
            System.out.println("--------------------------");
            List<Notificacion> notificaciones;
            synchronized (this.notificaciones) {
                notificaciones = (List<Notificacion>) this.notificaciones.clone();
            }
            for (int id = 0; id < INSTANCIAS.size(); id++) {
                ejecutarAutomata(id, notificaciones);
            }
            synchronized (this.notificaciones) {
                for (Notificacion notificacion : notificaciones) {
                    this.notificaciones.remove(notificacion);
                }
            }
        }
        ciclo++;
        return estados;
    }

    public void inicializar() {
        //esclavos.get(1).i
    }

    public void emergencia() {
    }

    public void arrancar() {
        synchronized (arrancar) {
            arrancar = true;
        }
    }

    public void parada() {
        synchronized (parar) {
            parar = true;
        }
    }

    public void desconectar() {
        esclavos = null;
        super.desconectar();
    }

    public void conectar() {
        for (Esclavo esclavo : new Esclavo[]{
                    Esclavo1.INSTANCIA,
                    Esclavo2.INSTANCIA,
                    Esclavo3.INSTANCIA
                }) {
            this.conectarEsclavo(esclavo);
        }
        System.out.println("Conexion con los esclavos establecida.");
    }

    protected IEsclavo conectarEsclavo(int id) {
        return conectarEsclavo(Esclavo.INSTANCIAS.get(id));
    }

    protected IEsclavo conectarEsclavo(Automata automata) {
        return conectarEsclavo((Esclavo) automata);
    }

    protected IEsclavo conectarEsclavo(Esclavo automata) {
        IEsclavo iesclavo = this.<IEsclavo>conectar(automata);
        esclavos.put(automata.getId(), iesclavo);
        return iesclavo;
    }

    public void enlazar() {
        super.<IMaestro>enlazar();
    }
    /*
     * }}}
     */

    @Override
    public byte getId() {
        return 0;
    }

    public void configurarAutomatas(ConjuntoParametros parametros) {
        for (Integer key : parametros.keySet()) {
            HashMap<String, Parametros> automata = parametros.getParametros(key);
            if (key == 0) {
                configurar(automata);
            } else {
                try {
                    esclavos.get(automata).configurar(automata);
                } catch (RemoteException ex) {
                    System.err.println("Error al configurar el esclavo " + automata + ":");
                    ex.printStackTrace(System.err);
                }
            }
        }
    }

    public void provocarFalloEsclavo(byte esclavo) {
        try {
            esclavos.get(esclavo).fallar();
        } catch (RemoteException ex) {
            System.err.println("Error al comunicar con Esclavo" + esclavo + " para provocar fallo.");
            ex.printStackTrace(System.err);
        }
    }

    public void recuperarFalloEsclavo(byte esclavo) {
        try {
            esclavos.get(esclavo).recuperar();
        } catch (RemoteException ex) {
            System.err.println("Error al comunicar con Esclavo" + esclavo + " para recuperar fallo.");
            ex.printStackTrace(System.err);
        }
    }

    public void actualizar(String sensor, boolean estado) {
        super.actualizar(sensor, estado);
        notificar(sensor, estado);
    }

    public void simularLimpiezaCPD() {
        ((Robot2) this.subautomatas.get("R2")).cpd.limpiar();
    }

    private void informarScada(int id, char estadoAnterior) {
        Automata automata = Automata.INSTANCIAS.get(id);
        //System.out.println(""+id+"%"+(int)estadoAnterior+" "+(int)estados[id] + " " + automata.subautomatas.obtenerDiferenciaEstados(estadoAnterior, estados[id]));
        Map<String, String> estadosAnteriores = automata.subautomatas.decodificarNombreEstados(estadoAnterior);
        Map<String, String> estadosNuevos = automata.subautomatas.obtenerDiferenciaEstados(estadoAnterior, estados[id]);
        for (Map.Entry<String, String> estadoNuevo : estadosNuevos.entrySet()) {
            String nombreSubAutomata = estadoNuevo.getKey();
            String nombreEstadoNuevo = estadoNuevo.getValue();
            String nombreEstadoAnterior = estadosAnteriores.get(nombreSubAutomata);
            log(nombreSubAutomata + " ha pasado de [" + nombreEstadoAnterior + "] a [" + nombreEstadoNuevo + "]");
        }
    }
    /*
     * private boolean entradaEsperada(String entrada) { if
     * (entradasEsperadas.containsKey(entrada)) { return
     * entradasEsperadas.get(entrada); } else { return false; } }
     *
     * private boolean salidaPendiente(String salida) { if
     * (salidasPendientes.containsKey(salida)) { return
     * salidasPendientes.get(salida); } else { return false; } }
     *
     * private void marcarEsperarEntrada(String entrada) {
     * entradasEsperadas.put(entrada, true); }
     *
     * private void marcarSalidaPendiente(String salida) {
     * salidasPendientes.put(salida, true); }
     *
     * private void desmarcarEsperarEntrada(String entrada) {
     * entradasEsperadas.put(entrada, false); }
     *
     * private void desmarcarSalidaPendiente(String salida) {
     * salidasPendientes.put(salida, false); }
     */

    @Override
    public void log(String msg) {
        Scada.log(msg);
    }

    public void simularCinta(String nombre, boolean[] posiciones) throws RemoteException {
        Scada.ui.simularCinta(nombre, posiciones);
    }

    public void simularCaidaCPD() {
        Scada.ui.simularCaidaCPD();
    }

    public void simularLlenadoCPD() {
        Scada.ui.simularLlenadoCPD();
    }
}
