package infiniware.automatas.maestro;

import infiniware.automatas.Automata;
import infiniware.automatas.GestorInstancias;
import infiniware.automatas.sensores.Sensores;

public class GestorSensores extends Sensores {

    GestorInstancias automatas = Automata.INSTANCIAS;

    @Override
    public void actualizar(String sensores) {
        super.actualizar(sensores);
        for (Automata automata : automatas.values()) {
            automata.actualizar(this);
        }
    }
}
