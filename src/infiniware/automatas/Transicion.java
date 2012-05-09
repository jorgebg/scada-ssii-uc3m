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
        if(subautomata.estado != origen)
            return false;
        boolean cumple = true;
        char operacion = 0;
        boolean negacion = false;
        String buffer = "";
        //System.out.println(codicion);
        for (char token : this.codicion.toCharArray()) {
            //System.out.println("{" + buffer + "}" + operacion);
            if (token == '!') {
                negacion = true;
            } else {
                if (esOperacion(token)) {
                    boolean valor = subautomata.automata.sensores.get(buffer);
                    buffer = "";
                    if (operacion == 0) {
                        operacion = token;
                        cumple = valor;
                    } else {
                        if (negacion) {
                            valor = !valor;
                            negacion = false;
                        }
                        cumple = operar(operacion, cumple, valor);
                        if(!cumple)
                            break;
                    }

                } else {
                    buffer = buffer + token;
                }
            }
            //System.out.println("[" + cumple + "]");
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
