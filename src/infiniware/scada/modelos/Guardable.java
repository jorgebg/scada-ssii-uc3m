package infiniware.scada.modelos;

import java.util.HashMap;
import java.util.Set;

/**
 * 
 * @author Infiniware
 * 
 * Clase abstracta que encapsula los 2 tipos de de objetos guardables, Informe y Parametros
 *
 */
public abstract class Guardable extends HashMap<String, Integer>{

	/**
	 * Metodo que extrae los elementos de un objeto guardable a partir de un array de claves
	 * 
	 * @param parametros
	 * @return
	 */
    public Guardable extraer(String[] elementos) {
        return null;
    }
}
