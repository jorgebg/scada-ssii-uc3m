package infiniware.almacenamiento;

import infiniware.automatas.Automata;
import infiniware.scada.modelos.ConjuntoParametros;

public abstract class Componente {
	
	protected String path;

    public static void configurar(Automata automata) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void guardar(String nombre, ConjuntoParametros parametros) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public ConjuntoParametros cargar(String nombre) {
        throw new UnsupportedOperationException("Not yet implemented");
 
    }
}
