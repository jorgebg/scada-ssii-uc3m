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
    Scada scada = Scada.INSTANCIA;
    char[] estados = new char[4];
    public GestorSensores sensores;
    public static Maestro INSTANCIA = new Maestro();

    protected Maestro() {
        super();
        this.subautomatas = new GestorSubAutomatas(this) {

            {
                instalar("CT", new Cinta("R2"));
                instalar("R2", new Robot2());
            }
        };
        this.sensores = new GestorSensores();
        this.esclavos = new GestorEsclavos();
    }

    /*
     * infiniware.automatas.esclavos.IMaestro {{{
     */
    public void notificar(byte esclavo, char sensores) {
        this.sensores.actualizar(sensores);
    }
    /*
     * }}}
     */

    /*
     * infiniware.scada.IMaestro {{{
     */
    public char[] ciclo(Sensores sensores) {
        this.sensores.actualizar(sensores);
        estados[getId()] = ejecutar(sensores);
        char estado;
        for (int id = 1; id < esclavos.size(); id++) {
            estado = this.sensores.codificar(id);
            estados[id] = esclavos.ejecutar(id, estado);
        }
        return estados;
    }

    public void inicializar() {
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
            IEsclavo iesclavo = this.<IEsclavo>conectar(esclavo);
            esclavos.put(esclavo.getId(), iesclavo);
        }
        System.out.println("Conexion con los esclavos establecida.");
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

}
