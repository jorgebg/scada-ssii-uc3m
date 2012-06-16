package infiniware.scada.modelos;

import java.util.HashMap;
import java.util.Set;

public abstract class Guardable extends HashMap<String, Integer>{

    public Guardable extraer(String[] parametros) {
        return null;
    }

    public Guardable extraer(Set<String> parametros) {
        return extraer((String[])parametros.toArray(new String[parametros.size()]));
    }
}
