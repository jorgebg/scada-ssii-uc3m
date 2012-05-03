package infiniware.scada.modelos;

import java.util.HashMap;

public class Parametros extends HashMap<String, Integer> {
    public Parametros get(String... parametros) {
        Parametros nuevos = new Parametros();
        for(String clave : parametros)
            nuevos.put(clave, get(clave));
        return nuevos;
    }
}
