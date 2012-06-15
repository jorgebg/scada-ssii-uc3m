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
    Thread simulacion;

    public SubAutomata() {
        Class subautomata = this.getClass();
        for (Class cls : subautomata.getDeclaredClasses()) {
            try {
                if (!Modifier.isAbstract(cls.getModifiers())
                        && Simulacion.class.isAssignableFrom(cls)) {
                    Simulacion simulacion = (Simulacion) cls.getDeclaredConstructor(new Class[]{subautomata}).newInstance(new Object[]{this});
                    simulaciones.put(cls.getSimpleName(), simulacion);
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }
        //System.out.println(simulaciones);
    }

    public int ejecutar() {
        //System.out.println(nombre + ": Estoy en [" + estados.get(estado) + "]");
        for (Transicion transicion : transiciones) {
            if (transicion.cumple()) {
                System.out.println(nombre + ": Transitando de [" + estados.get(estado) + "]" + " a [" + estados.get(transicion.destino) + "] cumpliendose " + transicion.codicion);
                estado = transicion.destino;
                break;
            } else {
                //System.out.println(nombre + ": No se cumple la transicion de [" + estados.get(transicion.origen) + "]" + " a [" + estados.get(transicion.destino) + "] siendo " + transicion.codicion + " falso (" + automata.sensores + ")");
            }
        }
        simular();
        return estado;
    }

    public void configurar(Parametros parametros) {
        //System.out.println("Configurando: " + parametros);
        if(this.parametros == null)
            this.parametros = new Parametros();
        this.parametros = parametros.mezclar(parametros.keySet());
    }

    public void simular() {
        if (this.simulacion == null || !this.simulacion.isAlive() || this.simulacion.isInterrupted()) {
            String estado = this.estados.get(this.estado);
            Simulacion simulacion = simulaciones.get(estado);
            if (simulacion != null) {
                System.out.println(nombre + ": Simulando [" + simulacion.getClass().getSimpleName() + "]");
                this.simulacion = simulacion.lanzar();
            }
        }
    }

    @Override
    public String toString() {
        return this.nombre;
    }
    
}