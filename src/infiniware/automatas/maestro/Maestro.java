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

    GestorSensores sensores;
    GestorEsclavos esclavos;
    Scada scada;
    char[] estados = new char[4];
    public static Maestro INSTANCIA = new Maestro();
    GestorSubAutomatas subautomatas = new GestorSubAutomatas() {

        {
            put("CT", new Cinta(INSTANCIA, "R2"));
            put("R2", new Robot2(INSTANCIA));
        }
    };

    /*
     * infiniware.automatas.esclavos.IMaestro {{{
     */
    public void notificar(byte esclavo, char sensores) {
        this.sensores.actualizar(esclavo, sensores);
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
        for (int id = 1; id < esclavos.size(); id++) {
            estados[id] = esclavos.ejecutar(id, this.sensores.get(id));
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
        esclavos = new GestorEsclavos();
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
        esclavos.get(esclavo).fallar();
    }

    public void recuperarFalloEsclavo(byte esclavo) {
        esclavos.get(esclavo).recuperar();
    }
}
