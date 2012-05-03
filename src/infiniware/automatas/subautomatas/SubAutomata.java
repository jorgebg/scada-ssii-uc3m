package infiniware.automatas.subautomatas;

import infiniware.automatas.Automata;
import infiniware.automatas.Transicion;
import infiniware.scada.modelos.Parametros;
import infiniware.scada.simulador.Simulacion;
import java.util.List;

public abstract class SubAutomata {

    public List<String> estados;
    public List<Transicion> transiciones;
    public final String nombre;
    public int estado = 0;
    public final Automata automata;
    Parametros parametros;
    static final String[] configuracion = new String[] {};

    public SubAutomata(Automata automata, String nombre) {
        this.automata = automata;
        this.nombre = nombre;
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
        this.parametros = parametros.get(configuracion);
    }
    
    public abstract void simular();
}