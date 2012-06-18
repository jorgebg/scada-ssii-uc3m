/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.scada.modelos;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;


/**
 * 
 * @author Infiniware
 * 
 * Clase agregada de Parametros que contiene los distintos parametros con los que trabaja el programa
 *
 */
public class ConjuntoParametros extends ConjuntoGuardable {

	/**
	 * Metodo que obtiene los parametros correspondientes a la clave "key"
	 * 
	 * @param key
	 * @return 
	 */
    public HashMap<String, Parametros> getParametros(Integer key) {
        HashMap<String, Guardable> original = get(key);
        HashMap<String, Parametros> nuevo = new HashMap<String, Parametros>();
        for (Entry<String, Guardable> entry : original.entrySet()) {
            nuevo.put(entry.getKey(), (Parametros)entry.getValue());
        }
        return nuevo;
    }     
}
