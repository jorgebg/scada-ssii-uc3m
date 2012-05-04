/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.automatas;

import infiniware.automatas.subautomatas.SubAutomata;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author jorge
 */
public class GestorSubAutomatas extends HashMap<String, SubAutomata> {

    final List<List<Integer>> estados = new ArrayList<List<Integer>>();

    public void indizarEstados() {
        estados.clear();
        indizarEstados(0, new ArrayList<Integer>());
    }

    private void indizarEstados(int nivel, List<Integer> valores) {
        if (nivel < size()) {
            for (int subautomata = nivel; subautomata < this.size(); subautomata++) {
                for (int estado = 0; estado < get(subautomata).estados.size(); estado++) {
                    valores.add(estado);
                    indizarEstados(nivel + 1, valores);
                }
            }
        } else {
            estados.add(valores);
            valores.clear();
        }
    }

    public char codificarEstado() {
        List<Integer> estado = new ArrayList<Integer>();
        for (SubAutomata subautomata : values()) {
            estado.add(subautomata.estado);
        }
        return (char) estados.indexOf(estado);
    }
    
    public List<Integer> decodificarEstado(char estado) {
        return estados.get(estado);
    }
}
