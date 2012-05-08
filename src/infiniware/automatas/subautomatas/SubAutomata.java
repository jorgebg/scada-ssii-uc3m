package infiniware.automatas.subautomatas;

import infiniware.automatas.Automata;
import infiniware.automatas.Transicion;
import infiniware.scada.modelos.Parametros;
import infiniware.scada.simulador.Simulacion;
import infiniware.scada.simulador.Simulaciones;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;

public abstract class SubAutomata {

    public List<String> estados;
    public List<Transicion> transiciones;
    public int estado = 0;
    Parametros parametros = new Parametros();
    Simulaciones simulaciones = new Simulaciones();
    public Automata automata;
    public String nombre;

    public SubAutomata() {
        Class subautomata = this.getClass();
        for (Class cls : subautomata.getDeclaredClasses()) {
            try {
                if (!Modifier.isAbstract(cls.getModifiers())
                        && Simulacion.class.isAssignableFrom(cls)) {
                    Simulacion simulacion = (Simulacion) cls.getDeclaredConstructor(new Class[] { subautomata }).newInstance(new Object[] { this });
                    simulaciones.put(cls.getSimpleName(), simulacion);
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }
    }

    public int ejecutar() {
        for (Transicion transicion : transiciones) {
            if (transicion.cumple()) {
                System.out.println(nombre + ": Transitando de [" + estados.get(estado) + "]" + " a [" + estados.get(transicion.destino) + "]");
                estado = transicion.destino;
                simular();
                break;
            }
        }
        return estado;
    }

    public void configurar(Parametros parametros) {
        this.parametros = parametros.extraer(parametros.keySet());
    }

    public void simular() {
        String estado = this.estados.get(this.estado);
        Simulacion simulacion = simulaciones.get(estado);
        if (simulacion != null) {
            System.out.println(nombre + ": Iniciando simulacion [" + simulacion.getClass().getSimpleName() + "]");
            simulacion.lanzar();
        }
    }
}