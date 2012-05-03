package infiniware.automatas.maestro;

import infiniware.automatas.sensores.Sensores;
import java.util.ArrayList;

class GestorSensores extends ArrayList<Sensores> {

    private static final int MAESTRO = 0;
    private static final int ESCLAVO1 = 1;
    private static final int ESCLAVO2 = 2;
    private static final int ESCLAVO3 = 3;

    public char codificar() {
        return get(MAESTRO).codificar();
    }

    public void actualizar(byte idAutomata, char sensores) {
        Sensores automata = get(idAutomata);
        automata.actualizar(sensores);
        if (idAutomata != MAESTRO) {
            actualizar(automata);
        }
    }

    public void actualizar(char sensores) {
        get(MAESTRO).actualizar(sensores);
    }

    public void actualizar(int sensores) {
        get(MAESTRO).actualizar(sensores);
    }

    public void actualizar(Sensores sensores) {
        get(MAESTRO).actualizar(sensores);
    }
}
