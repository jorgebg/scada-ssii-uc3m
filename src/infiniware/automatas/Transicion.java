package infiniware.automatas;

import infiniware.automatas.subautomatas.SubAutomata;
import infiniware.automatas.sensores.Sensores;

public class Transicion {

    public final int origen;
    public final String codicion;
    public final int destino;
    public final SubAutomata subautomata;

    public Transicion(SubAutomata subautomata, int origen, String condicion, int destino) {
        this.subautomata = subautomata;
        this.origen = origen;
        this.codicion = condicion;
        this.destino = destino;
    }

    public boolean cumple() {
        boolean cumple = true;
        char operacion = '+';
        boolean negacion = false;
        String buffer = "";
        for (char token : this.codicion.toCharArray()) {
            if (token == '!') {
                negacion = true;
            } else {
                if (esOperacion(token)) {
                    boolean valor = subautomata.automata.sensores.get(buffer);
                    buffer = "";
                    if (negacion) {
                        valor = !valor;
                        negacion = false;
                    }
                    cumple = operar(operacion, cumple, valor);
                } else {
                    buffer = buffer + token;
                }
            }
        }
        return cumple;
    }

    private boolean esOperacion(char token) {
        return token == '*' || token == '+';
    }

    private boolean operar(char token, boolean a, boolean b) {
        if (token == '+') {
            return a || b;
        }
        if (token == '*') {
            return a && b;
        }
        return false;
    }
}
