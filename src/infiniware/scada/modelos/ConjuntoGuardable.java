package infiniware.scada.modelos;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ConjuntoGuardable extends HashMap<Integer, HashMap<String, Guardable>> implements Serializable{
	
	public ConjuntoGuardable(){
		super();
	}
	
	public LinkedHashMap<Integer, HashMap<String, Guardable>> toLinkedHashMap(){
		Object[] array = keySet().toArray();
		LinkedHashMap<Integer, HashMap<String, Guardable>> map = new LinkedHashMap<Integer, HashMap<String,Guardable>>();
		for(int i=0; i < array.length; i++){
			map.put((Integer) array[i], get(array[i]));
		}
		return map;
	}
}
