package infiniware.automatas;

import infiniware.automatas.esclavos.Esclavo1;
import infiniware.automatas.esclavos.Esclavo2;
import infiniware.automatas.esclavos.Esclavo3;
import infiniware.automatas.maestro.Maestro;
import infiniware.automatas.sensores.Sensores;
import infiniware.automatas.subautomatas.SubAutomata;
import infiniware.procesos.IProcesable;
import infiniware.remoto.IConexion;
import infiniware.remoto.IRegistrable;
import infiniware.remoto.Profibus;
import infiniware.remoto.Registrador;
import infiniware.scada.modelos.Parametros;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

public abstract class Automata implements Profibus, IProcesable, IConexion, IRegistrable {

    public final Sensores sensores;
    public GestorSubAutomatas subautomatas;
    public static final GestorInstancias INSTANCIAS = new GestorInstancias() {

        {
            put("Maestro", Maestro.INSTANCIA);
            put("Esclavo1", Esclavo1.INSTANCIA);
            put("Esclavo2", Esclavo2.INSTANCIA);
            put("Esclavo3", Esclavo3.INSTANCIA);
        }
    };

    public Automata() {
        sensores = new Sensores();
    }
    
    public void inicializar(char sensores) {
        this.sensores.inicializar(sensores);
    }

    public char ejecutar(char sensores) {
        this.sensores.actualizar(sensores);
        return ejecutar();
    }

    public char ejecutar(Sensores sensores) {
        this.sensores.actualizar(sensores);
        return ejecutar();
    }

    public char ejecutar() {
        for (SubAutomata subautomata : subautomatas.values()) {
            subautomata.ejecutar();
        }
        return subautomatas.codificarEstados();
    }

    /*
     * IProcesable {{{
     */
    public void detenerProceso() {
        desconectar();
    }

    public Thread iniciarProceso() {
        enlazar();
        conectar();
        return Registrador.thread;
    }
    /*
     * }}}
     */

    public <T extends Remote> void enlazar() {
        try {
            infiniware.remoto.Registrador.<T>enlazar(this);
        } catch (Exception e) {
            System.err.println("Error al registrar \"" + this.getRemoteName() + "\"");
            e.printStackTrace(System.err);
        }
        System.out.println("Automata \"" + getRemoteName() + "\" registrado en " + getHost() + ":" + getPort());
    }

    public void actualizar(Sensores sensores) {
        this.sensores.actualizar(sensores);
    }

    public abstract byte getId();

    public String getHost() {
        return "127.0.0.1";
    }

    public int getPort() {
        return 1024 + getId();
    }

    public String getRemoteName() {
        return this.getClass().getSimpleName();
    }

    protected <T extends Profibus> T conectar(Automata automata) {
        Registry registry;
        T profibus = null;
        while (profibus == null) {
            try {
                registry = LocateRegistry.getRegistry(automata.getHost(), automata.getPort());
                profibus = (T) registry.lookup(automata.getRemoteName() + "");
            } catch (Exception e) {
                System.err.println("No se puede conectar con el automata \"" + automata.getRemoteName() + "\" desde \"" + getRemoteName() + "\". Tratando de reconectar.");
                //e.printStackTrace(System.err);
                try {
                    Thread.currentThread().sleep(500);
                    //e.printStackTrace(System.err);
                } catch (InterruptedException es) {
                    System.err.println("Error al esperar a la reconexion con el automata \"" + automata.getRemoteName() + "\" desde \"" + getRemoteName() + "\".");
                }
            }
        }
        System.out.println("Conexion con el automata \"" + automata.getRemoteName() + "\" desde \"" + getRemoteName() + "\" establecida.");
        return profibus;
    }

    public void desconectar() {
        try {
            Registrador.desenlazar(this);
        } catch (Exception ex) {
            System.err.println("Error al desconectar \"" + getRemoteName() + "\"");
            ex.printStackTrace(System.err);
        }
        System.err.println("\"" + getRemoteName() + "\" desconectado.");
    }

    public void actualizar(String sensor, boolean estado) {
        this.sensores.set(sensor, estado);
    }
    
    
    public void configurar(Map<String,Parametros> parametrosSubautomatas) {
        for (Map.Entry<String, Parametros> parametros : parametrosSubautomatas.entrySet()) {
            this.subautomatas.get(parametros.getKey()).configurar(parametros.getValue());
        }
    }
    
}
