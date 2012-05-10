package infiniware.almacenamiento;

import infiniware.automatas.Automata;
import infiniware.scada.modelos.Parametros;

public abstract class Componente {
	
	protected String path;

    public static void configurar(Automata automata) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void guardar(String nombre, Parametros parametros) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Parametros cargar(String nombre) {
        throw new UnsupportedOperationException("Not yet implemented");
 
    }
}
