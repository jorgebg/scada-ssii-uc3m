package infiniware.automatas.maestro;

import infiniware.automatas.Automata;
import infiniware.automatas.GestorSubAutomatas;
import infiniware.automatas.esclavos.*;
import infiniware.automatas.sensores.Sensores;
import infiniware.automatas.subautomatas.Cinta;
import infiniware.automatas.subautomatas.Robot2;
import infiniware.scada.Scada;
import infiniware.scada.modelos.ConjuntoParametros;
import infiniware.scada.modelos.Parametros;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class Maestro extends Automata implements infiniware.scada.IMaestro, infiniware.automatas.esclavos.IMaestro {

    GestorEsclavos esclavos;
    char[] estados;
    public final GestorSensores mapaSensores;
    public static final Maestro INSTANCIA = new Maestro();
    Scada scada = Scada.INSTANCIA;
    private Map<String, Boolean> entradasEsperadas;
    private Map<String, Boolean> salidasPendientes;
    private int ciclo;

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
        entradasEsperadas = new HashMap<String, Boolean>();
        salidasPendientes = new HashMap<String, Boolean>();
    }

    /*
     * infiniware.automatas.esclavos.IMaestro {{{
     */
    public void notificar(byte automata, char sensores) {
        this.mapaSensores.actualizar(automata, sensores);
        if (automata > 0) {
            Esclavo esclavo = (Esclavo) Esclavo.INSTANCIAS.get(automata);

            if (esclavo.tieneEntrada()) {
                for (String entrada : esclavo.entradas) {
                    if (entradaEsperada(entrada) && !mapaSensores.get(entrada)) {
                        desmarcarEsperarEntrada(entrada);
                    }
                }
            }
            if (esclavo.tieneSalida() && !salidaPendiente(esclavo.salida) && mapaSensores.get(esclavo.salida)) {
                marcarSalidaPendiente(esclavo.salida);
            }

        }
        System.out.println("IN: " + (automata == 0 ? "M" : "E" + automata) + " => M:\n" + this.mapaSensores.automatas.get((int) automata) + "\n");
        this.scada.notificar(automata, sensores);
    }
    /*
     * }}}
     */

    private void ejecutarAutomata(int id) {
        Esclavo esclavo = null;
        if (id > 0) {
            esclavo = (Esclavo) Esclavo.INSTANCIAS.get(id);
        }
        String nombre = "M";
        char estadoAnterior = estados[id];
        if (id != 0) {
            nombre = "E" + id;
        }
        boolean exito;
        //System.out.println("OUT: M => " + nombre + ":\n" + this.mapaSensores.automatas.get(id) + "\n");
        do {
            try {
                //synchronized (this.mapaSensores) {
                char codificacion = Character.MAX_VALUE;
                if (esclavo != null) {
                    if (esclavo.tieneEntrada()) {
                        for (String entrada : esclavo.entradas) {
                            if (!entradaEsperada(entrada) && mapaSensores.get(entrada)) {
                                codificacion = this.mapaSensores.codificar(id);
                                marcarEsperarEntrada(entrada);
                            }
                        }
                    }
                    if (esclavo.tieneSalida() && salidaPendiente(esclavo.salida) && !mapaSensores.get(esclavo.salida)) {
                        codificacion = this.mapaSensores.codificar(id);
                        desmarcarSalidaPendiente(esclavo.salida);
                    }
                } else {
                    codificacion = this.mapaSensores.codificar(id);
                }

                System.out.println("OUT: M =>" + nombre + ": " + (int) codificacion);
                //System.out.println("EEH" + (int)mascara + "\n" + this.mapaSensores.actualizados + "\n" + this.mapaSensores.automatas.get(id).actualizados);
                //}
                if (id == 0) {
                    estados[id] = ejecutar(codificacion);
                } else {
                    estados[id] = esclavos.ejecutar(id, codificacion);
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
            System.out.println("EE: " + entradasEsperadas);
            System.out.println("SP: " + salidasPendientes);
            System.out.println(mapaSensores);
            System.out.println("--------------------------");
            for (int id = 0; id < INSTANCIAS.size(); id++) {
                ejecutarAutomata(id);
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
        //TODO es necesario? Ahora mismo no se esta llamando de ningun sitio
    }

    public void parada() {
        //TODO es necesario? Ahora mismo no se esta llamando de ningun sitio
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

    public void actualizar(Sensores sensores) {
        super.actualizar(sensores);
        actualizar();
    }

    public void actualizar(String sensor, boolean estado) {
        super.actualizar(sensor, estado);
        actualizar();
    }

    public void actualizar() {
        notificar(getId(), (char) this.sensores.codificar());
    }

    public void limpiarCPD() {
        try {
            esclavos.get(3).simular(Esclavo.Simulaciones.LimpiarCPD);
        } catch (RemoteException ex) {
            System.err.println("Error al limpiar el CPD");
            ex.printStackTrace(System.err);
        }
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

    private boolean entradaEsperada(String entrada) {
        if (entradasEsperadas.containsKey(entrada)) {
            return entradasEsperadas.get(entrada);
        } else {
            return false;
        }
    }

    private boolean salidaPendiente(String salida) {
        if (salidasPendientes.containsKey(salida)) {
            return salidasPendientes.get(salida);
        } else {
            return false;
        }
    }

    private void marcarEsperarEntrada(String entrada) {
        entradasEsperadas.put(entrada, true);
    }

    private void marcarSalidaPendiente(String salida) {
        salidasPendientes.put(salida, true);
    }

    private void desmarcarEsperarEntrada(String entrada) {
        entradasEsperadas.put(entrada, false);
    }

    private void desmarcarSalidaPendiente(String salida) {
        salidasPendientes.put(salida, false);
    }

    @Override
    public void log(String msg) {
        Scada.log(msg);
    }
}
