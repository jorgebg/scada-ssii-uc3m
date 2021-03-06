package infiniware.automatas;

import infiniware.automatas.esclavos.Esclavo1;
import infiniware.automatas.esclavos.Esclavo2;
import infiniware.automatas.esclavos.Esclavo3;
import infiniware.automatas.maestro.Maestro;
import infiniware.automatas.sensores.Sensores;
import infiniware.automatas.subautomatas.CintaCapacidad;
import infiniware.automatas.subautomatas.CintaEntrada;
import infiniware.automatas.subautomatas.SubAutomata;
import infiniware.procesos.IProcesable;
import infiniware.remoto.IConexion;
import infiniware.remoto.IRegistrable;
import infiniware.remoto.Profibus;
import infiniware.remoto.Registrador;
import infiniware.scada.Scada;
import infiniware.scada.modelos.Parametros;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import sun.swing.StringUIClientPropertyKey;

public abstract class Automata implements Profibus, IProcesable, IConexion, IRegistrable {

	private String host = "127.0.0.1";
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
    private int ciclo;

    public static final char EJECUTAR = Character.MAX_VALUE;
    public static final char PARAR = EJECUTAR-1; 
    public static final char REANUDAR = EJECUTAR-2;
    protected Boolean emergencia = false;
    
    public abstract void log(String msg);

    public void imprimirTablaSensores() {
        Sensores clone = sensores.clone();
        String cabecera = "     ";
        for (String sensor : sensores.keySet()) {
            cabecera += StringUtils.rightPad(sensor, 3);
        }
        String filas = "";
        for (int i = 0; i < Math.pow(sensores.elementos.size(), 2); i++) {
            clone.actualizar(i);
            filas += StringUtils.rightPad(""+i, 5);
            for (boolean sensor : clone.values())
                filas += StringUtils.rightPad(sensor ? "1" :  "0", 3);
            filas += "\n";
        }
        String resultado = cabecera + "\n" + filas;
        System.out.println(resultado);
    }

    /*public enum Simulaciones { Fallar, Recuperar, LimpiarCPD };    
    public void simular(Simulaciones simulacion) {
        //Nada por defecto
    }*/

    public Automata() {
        sensores = new Sensores();
    }
    
    public void inicializar(char sensores) {
        this.sensores.inicializar(sensores);
    }

    public char ejecutar(char sensores) {
        
        if(!emergencia)
        switch (sensores) {
            case REANUDAR:
                recuperar();
                break;
            case EJECUTAR:
                if(!emergencia)
                ejecutar();
                break;
            default:
                Map.Entry<String, Boolean> decodificado = this.sensores.decodificarYActualizar(sensores);
                System.out.println("Decodificando: " + decodificado);
        }
        return subautomatas.codificarEstados();
    }
    

    public char ejecutar() {
        System.out.println("#"+ ciclo +" Ejecutando:\n" + sensores);
        for (SubAutomata subautomata : subautomatas.values()) {
            subautomata.ejecutar();
            // DEBUG {{{
            if(subautomata instanceof CintaCapacidad)
                System.out.println(subautomata);
            // }}}
        }
        ciclo++;
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


    public abstract byte getId();

    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return 1024 + getId();
    }

    public String getRemoteName() {
        return this.getClass().getSimpleName();
    }

    protected <T extends Profibus> T conectar(int automata) {
        return this.<T>conectar(INSTANCIAS.get(automata));
    }
    protected <T extends Profibus> T conectar(Automata automata) {
        Registry registry;
        T profibus = null;
        boolean errorMostrado = false;
        while (profibus == null) {
            try {
                registry = LocateRegistry.getRegistry(automata.getHost(), automata.getPort());
                profibus = (T) registry.lookup(automata.getRemoteName() + "");
            } catch (Exception e) {
                if (!errorMostrado) {
                    System.err.println("No se puede conectar con el automata \"" + automata.getRemoteName() + "\" desde \"" + getRemoteName() + "\". Tratando de reconectar.");
                    errorMostrado = true;
                }
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
            //System.out.println(parametros.getKey() + " " + this.subautomatas + this.subautomatas.get(parametros.getKey())+ " " + parametros.getValue());
            try {
            this.subautomatas.get(parametros.getKey()).configurar(parametros.getValue());
            } catch (Exception ex ) {
                System.err.println("Error al configurar: " + parametros.getKey() + " " + parametros.getValue());
            }
        }
    }

    public void actualizar(String[] sensores, boolean estado) {
        for (String e : sensores) {
            actualizar(e,estado);
        }
    }

    public void emergencia() {
        System.out.println("Emergencia");
        emergencia = true;
        for(SubAutomata subautomata : subautomatas.values()) {
            subautomata.emergencia = true;
        }
    }
    public void recuperar() {
        System.out.println("Reanudando");
        for(SubAutomata subautomata : subautomatas.values()) {
            subautomata.emergencia = false;
        }
        emergencia = false;
    }
}
