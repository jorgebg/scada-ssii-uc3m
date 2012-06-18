package infiniware.automatas.maestro;

import infiniware.automatas.Automata;
import infiniware.automatas.sensores.Sensores;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class GestorSensores extends Sensores {

    public Map<Integer, Sensores> automatas = Collections.synchronizedMap(new HashMap<Integer, Sensores>());

    public synchronized void actualizar(char sensores) {
        super.actualizar(sensores);
        for (Sensores automata : automatas.values()) {
            automata.actualizar(this);
        }
    }

    public synchronized char codificar(int id) {
        return (char) automatas.get(id).codificar(this);
    }

    public synchronized void instalar() {
        for (Automata automata : Automata.INSTANCIAS.values()) {
            insertar(automata.sensores);
            automatas.put((int) automata.getId(), automata.sensores.clone());
        }
    }

    public synchronized void actualizar(int id, char sensores) {
        Sensores automata = this.automatas.get(id);
        automata.actualizar(sensores);
        this.actualizar(automata);
    }

    @Override
    public String toString() {
        String resultado = super.toString();

        for (Sensores automata : this.automatas.values()) {
            resultado += "\n";
            for (String key : elementos.keySet()) {
                String valor = " ";
                if (automata.elementos.containsKey(key)) {
                    valor = automata.get(key) ? "1" : "0";
                }
                resultado += StringUtils.rightPad(valor, 3);
            }
        }

        return resultado;
    }
}
