package infiniware.almacenamiento;

import infiniware.automatas.Automata;
import infiniware.scada.modelos.Parametros;

public class Configuracion extends Componente {

	protected String ruta = "/almacenamiento/configuracion.ser";
	
    public static void configurar(Automata automata) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void guardar(String nombre, Parametros parametros) {
        super.guardar(nombre, parametros);
    }

    public Parametros cargar(String nombre) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
