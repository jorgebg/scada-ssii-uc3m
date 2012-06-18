package infiniware.scada.modelos;

import java.util.HashMap;
import java.util.Set;

/**
 * 
 * @author Infiniware
 * 
 * Clase que representa los parametros de un elemento
 *
 */
public class Parametros extends Guardable {

	/**
	 * Constructor por defecto para un conjunto de parametros
	 * 
	 * @param parametros
	 */
    public Parametros(String... parametros) {
        mezclarTodo(parametros);
    }
    
    /**
     * Metodo que mezcla los parametros de un array de claves
     * 
     * @param parametros
     * @return
     */
    public Parametros mezclar(String[] parametros) {
        Parametros nuevos = new Parametros();
        for(String clave : parametros)
            nuevos.put(clave, get(clave));
        return nuevos;
    }

    /**
     * Metodo que mezcla los parametros de un "Set" de claves
     * 
     * @param parametros
     * @return
     */
    public Parametros mezclar(Set<String> parametros) {
        return mezclar((String[])parametros.toArray(new String[parametros.size()]));
    }

    /**
     * Metodo que mezcla los parametros de un conjunto de claves de parametros
     * 
     * @param parametros
     * @return
     */
    public Parametros mezclarTodo(String... parametros) {
        for(String clave : parametros)
            put(clave, null);
        return this;
    }

    /**
     * Metodo que obtiene el valor de un parametro de acuerdo a su clave
     * 
     */
    @Override
    public Integer get(Object key) {
        Integer result = super.get(key);
        if(result == null)
            System.err.println("Error al obtener el parametro \"" + key + "\": No se han inicializado.");
        return result;
    }
    
    
}