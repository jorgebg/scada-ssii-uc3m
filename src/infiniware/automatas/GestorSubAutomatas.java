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
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

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
        //System.out.println(estados);
    }

    private void indizarEstados(int subautomata, List<Integer> valores) {
        /*
         * System.out.print("valores: "+valores); System.out.print(" nivel:
         * "+nivel); System.out.println(" size: "+size());
         */

        //System.out.println("estados #" + subautomata + " " + get(subautomata).estados);
        for (int estado = 0; estado < get(subautomata).estados.size(); estado++) {
            //System.out.println("Entrando en: #" + subautomata+"."+estado+" " +get(subautomata).nombre+"."+ get(subautomata).estados.get(estado) );
            valores.add(estado);
            if (subautomata >= size() - 1) {
                estados.add(new ArrayList<Integer>(valores));
            } else {
                indizarEstados(subautomata + 1, valores);
            }
            valores.remove(valores.size() - 1);
        }
        //System.out.println("cambio #" + subautomata);
    }

    public SubAutomata get(int id) {
        return (SubAutomata) values().toArray()[id];
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
            String nombreSubAutomata = (String) this.keySet().toArray()[id];
            String nombreEstado = get(id).estados.get(e);
            nombreEstados.put(nombreSubAutomata, nombreEstado);
            id++;
        }
        return nombreEstados;
    }

    public Map<String, String> obtenerDiferenciaEstados(char estado1, char estado2) {
        Map<String, String> nombreEstados = new HashMap<String, String>();
        List<Integer> listaEstado1 = estados.get(estado1);
        List<Integer> listaEstado2 = estados.get(estado2);
        int id = 0;
        for (Integer e : listaEstado1) {
            if (e.equals((listaEstado2.get(id)))) {
                String nombreSubAutomata = (String) this.keySet().toArray()[id];
                String nombreEstado = get(id).estados.get(e);
                nombreEstados.put(nombreSubAutomata, nombreEstado);
            }
            id++;
        }
        return nombreEstados;
    }

    public SubAutomata instalar(String key, SubAutomata value) {
        if (value.automata == null) {
            value.automata = this.automata;
        }
        if (value.nombre == null) {
            value.nombre = key;
        }
        return put(key, value);
    }

    public <T extends SubAutomata> T buscar(Object key) {
        SubAutomata subautomata = super.get(key);
        return (T)subautomata;
    }
    
    
}
