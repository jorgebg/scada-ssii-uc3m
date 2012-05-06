/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.automatas;

import infiniware.automatas.subautomatas.SubAutomata;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jorge
 */
public class GestorSubAutomatas extends HashMap<String, SubAutomata> {
    final Automata automata;
    final List<List<Integer>> estados = new ArrayList<List<Integer>>();

    public GestorSubAutomatas(Automata automata) {
        this.automata = automata;
    }
    
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

    public char codificarEstados() {
        List<Integer> estado = new ArrayList<Integer>();
        for (SubAutomata subautomata : values()) {
            estado.add(subautomata.estado);
        }
        return (char) estados.indexOf(estado);
    }
    
    public List<Integer> decodificarEstados(char estado) {
        return estados.get(estado);
    }

    public Map<String, String> decodificarNombreEstados(char estado) {
        Map<String, String> nombreEstados = new HashMap<String, String>();
        int id = 0;
        for (Integer e : estados.get(estado)) {
            String nombreSubAutomata = (String)this.keySet().toArray()[id];
            String nombreEstado = get(id).estados.get(e);
            nombreEstados.put(nombreSubAutomata, nombreEstado);
            id++;
        }
        return nombreEstados;
    }

    public SubAutomata instalar(String key, SubAutomata value) {
        if(value.automata == null)
            value.automata = this.automata;
        return put(key, value);
    }
    
    
}
