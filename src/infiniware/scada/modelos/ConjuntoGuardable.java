package infiniware.scada.modelos;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 
 * @author Infiniware
 * 
 * Clase abstracta que encapsula los Informes y los ConjuntoParametros
 *
 */
public class ConjuntoGuardable extends HashMap<Integer, HashMap<String, Guardable>> implements Serializable{
	
}
