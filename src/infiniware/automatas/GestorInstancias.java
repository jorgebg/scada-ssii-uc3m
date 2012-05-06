/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.automatas;

import java.util.HashMap;

/**
 *
 * @author jorge
 */
public class GestorInstancias extends HashMap<String, Automata> {
    
        public Automata get(int id) {
            if(id==0) return get("Maestro");
            else return get("Esclavo"+id);
        }
}
