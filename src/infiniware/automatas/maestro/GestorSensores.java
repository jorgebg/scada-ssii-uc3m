package infiniware.automatas.maestro;

import infiniware.automatas.Automata;
import infiniware.automatas.sensores.Sensores;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GestorSensores extends Sensores {

    public Map<Integer, Sensores> automatas = Collections.synchronizedMap(new HashMap<Integer, Sensores>());

    public void actualizar(char sensores) {
        super.actualizar(sensores);
        for (Sensores esclavo : automatas.values()) {
            esclavo.actualizar(this);
        }
    }

    public char codificar(int id) {
        return (char) automatas.get(id).codificar();
    }

    public void instalar() {
        for (Automata automata : Automata.INSTANCIAS.values()) {
            insertar(automata.sensores);
            automatas.put((int) automata.getId(), automata.sensores.clone());
        }
    }
    
    public void actualizar(int id, char sensores) {
        Sensores automata = this.automatas.get(id);
        automata.actualizar(sensores);
        this.actualizar(automata);
    }
}
