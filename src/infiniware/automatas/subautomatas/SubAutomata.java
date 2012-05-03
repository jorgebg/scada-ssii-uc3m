package infiniware.automatas.subautomatas;

import infiniware.automatas.Automata;
import infiniware.automatas.Transicion;
import infiniware.scada.modelos.Parametros;
import infiniware.scada.simulador.Simulacion;
import infiniware.scada.simulador.Simulaciones;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class SubAutomata {

    public List<String> estados;
    public List<Transicion> transiciones;
    public int estado = 0;
    Parametros parametros = new Parametros();
    Simulaciones simulaciones = new Simulaciones();
    
    public final Automata automata;

    public SubAutomata(Automata automata) {
        this.automata = automata;
        for(Class cls : this.getClass().getDeclaredClasses())
            try {
                if(!Modifier.isAbstract(cls.getModifiers()) && 
                    cls.isAssignableFrom(Simulacion.class))
                    simulaciones.put(cls.getSimpleName(), (Simulacion)cls.newInstance());
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    public int ciclo() {
        for (Transicion transicion : transiciones) {
            if (transicion.cumple()) {
                //new Simulacion(transicion).lanzar();
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
            simulacion.lanzar();
        }
    }
}