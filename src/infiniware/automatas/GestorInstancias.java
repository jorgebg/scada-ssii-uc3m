/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.automatas;

import infiniware.automatas.esclavos.Esclavo;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jorge
 */
public class GestorInstancias extends HashMap<String, Automata> {

    private Map<String, Esclavo> esclavos;

    public Automata get(int id) {
        if (id == 0) {
            return get("Maestro");
        } else {
            return get("Esclavo" + id);
        }
    }

    public Map<String, Esclavo> esclavos() {
        if (esclavos == null) {
            calcularEsclavos();
        }
        return esclavos;
    }

    private void calcularEsclavos() {
        esclavos = new HashMap<String, Esclavo>();
        for (Map.Entry<String, Automata> entry : entrySet()) {
            if (entry.getValue() instanceof Esclavo) {
                esclavos.put(entry.getKey(), (Esclavo) entry.getValue());
            }

        }
    }
}
