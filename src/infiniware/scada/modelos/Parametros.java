package infiniware.scada.modelos;

import java.util.HashMap;
import java.util.Set;

public class Parametros extends HashMap<String, Integer> {

    public Parametros(String... parametros) {
        mezclar(parametros);
    }
    public Parametros extraer(String[] parametros) {
        Parametros nuevos = new Parametros();
        for(String clave : parametros)
            nuevos.put(clave, get(clave));
        return nuevos;
    }

    public Parametros extraer(Set<String> parametros) {
        return extraer((String[])parametros.toArray(new String[parametros.size()]));
    }

    public Parametros mezclar(String... parametros) {
        for(String clave : parametros)
            put(clave, null);
        return this;
    }

    @Override
    public Integer get(Object key) {
        Integer result = super.get(key);
        if(result == null)
            System.err.println("Error al obtener el parametro \"" + key + "\": No se han inicializado.");
        return result;
    }
    
    
}
