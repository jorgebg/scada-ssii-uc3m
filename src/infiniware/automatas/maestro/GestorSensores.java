package infiniware.automatas.maestro;

import infiniware.automatas.Automata;
import infiniware.automatas.esclavos.Esclavo;
import infiniware.automatas.sensores.Sensores;
import java.util.HashMap;
import java.util.Map;

public class GestorSensores extends Sensores {

    public Map<Integer,Sensores> esclavos;

    @Override
    public void actualizar(String sensores) {
        super.actualizar(sensores);
        for (Sensores esclavo : esclavos.values()) {
            esclavo.actualizar(this);
        }
    }
    
    
    public char codificar(int id) {
        return (char) esclavos.get(id).codificar();
    }

    public void putAll(Sensores sensores) {
        elementos.putAll(sensores.elementos);
    }
    
}
