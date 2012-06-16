package infiniware.automatas.maestro;

import infiniware.automatas.Automata;
import infiniware.automatas.GestorSubAutomatas;
import infiniware.automatas.esclavos.*;
import infiniware.automatas.sensores.Sensores;
import infiniware.automatas.subautomatas.Cinta;
import infiniware.automatas.subautomatas.Robot2;
import infiniware.remoto.Profibus;
import infiniware.scada.Scada;
import infiniware.scada.modelos.ConjuntoParametros;
import infiniware.scada.modelos.Parametros;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Maestro extends Automata implements infiniware.scada.IMaestro, infiniware.automatas.esclavos.IMaestro {

    GestorEsclavos esclavos;
    char[] estados;
    public final GestorSensores mapaSensores;
    public static final Maestro INSTANCIA = new Maestro();
    Scada scada = Scada.INSTANCIA;

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
    }

    /*
     * infiniware.automatas.esclavos.IMaestro {{{
     */
    public void notificar(byte automata, char sensores) {
        this.mapaSensores.actualizar(automata, sensores);
        System.out.println((automata == 0 ? "M" : "E" + automata) + " => M:\n" + this.mapaSensores.automatas.get((int) automata) + "\n");
        this.scada.notificar(automata, sensores);
    }
    /*
     * }}}
     */

    /*
     * infiniware.scada.IMaestro {{{
     */
    public char[] ciclo(int sensores) {
        this.mapaSensores.actualizar(sensores);
        for (int id = 0; id < INSTANCIAS.size(); id++) {
            ejecutarAutomata(id);
        }
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
        for (Map.Entry<Integer, HashMap<String, Parametros>> automata : parametros.entrySet()) {
            if (automata.getKey() == 0) {
                configurar(automata.getValue());
            } else {
                try {
                    esclavos.get(automata).configurar(automata.getValue());
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
        notificar(getId(), (char) this.sensores.codificar());
    }

    public void actualizar(String sensor, boolean estado) {
        super.actualizar(sensor, estado);
        notificar(getId(), (char) this.sensores.codificar());
    }

    private void ejecutarAutomata(int id) {
        String nombre = "M";
        char estadoAnterior = estados[id];
        if (id != 0) {
            nombre = "E" + id;
        }
        char sensores = this.mapaSensores.codificar(id);
        char mascara = this.mapaSensores.codificarMascara(id);
        boolean exito;
        do {
            try {
                if (id == 0) {
                    estados[id] = ejecutar(sensores, mascara);
                } else {
                    estados[id] = esclavos.ejecutar(id, sensores, mascara);
                }
                exito = true;
            } catch (RemoteException ex) {
                exito = false;
                System.err.println("Error al llamar remotamente a 'ejecutar' en el esclavo " + id + ".");
                conectarEsclavo(id);
            }
        } while (!exito);

        System.out.println("M => " + nombre + ":\n" + this.mapaSensores.automatas.get(id) + "\n");

        if (estadoAnterior != estados[id]) {
            Scada.log(nombre + " ha cambiado de estado " + (int) estadoAnterior + " a " + (int) estados[id] + ": " + Automata.INSTANCIAS.get(id).subautomatas.obtenerDiferenciaEstados(estadoAnterior, estados[id]));
        }
        //System.out.println("M <= E" + id + ":" + this.mapaSensores.automatas.get(id));

    }

    public void limpiarCPD() {
        try {
            esclavos.get(3).simular(Esclavo.Simulaciones.LimpiarCPD);
        } catch (RemoteException ex) {
            System.err.println("Error al limpiar el CPD");
            ex.printStackTrace(System.err);
        }
    }
}
