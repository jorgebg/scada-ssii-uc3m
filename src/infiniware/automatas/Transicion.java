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
        char operacion = 0;
        char fin = '\n';
        boolean negacion = false;
        String buffer = "";
        String condicion = this.codicion + fin;
        //System.out.println(codicion);
        //System.out.println(subautomata.estado + " " + origen);
        if (subautomata.estado != origen) {
            cumple = false;
        } else {
            for (char token : condicion.toCharArray()) {
                //System.out.println("{" + buffer + "}" + operacion);
                if (token == '!') {
                    negacion = true;
                } else {
                    if (esOperacion(token)) {
                        boolean valor = subautomata.automata.sensores.get(buffer);
                        if (negacion) {
                            valor = !valor;
                            negacion = false;
                        }
                        buffer = "";
                        if (operacion == 0) {
                            cumple = valor;
                        } else {
                            cumple = operar(operacion, cumple, valor);
                            if (!cumple) {
                                break;
                            }
                        }
                        operacion = token;
                    } else {
                        buffer = buffer + token;
                    }
                }
                //System.out.println(token + " [" + cumple + "]");
            }
        }
        return cumple;
    }

    private boolean esOperacion(char token) {
        return token == '*' || token == '+' || token == '\n';
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
