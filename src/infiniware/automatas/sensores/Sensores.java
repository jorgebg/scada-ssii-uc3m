package infiniware.automatas.sensores;

import infiniware.remoto.Profibus;
import java.util.*;

/**
 * Adapter de TreeMap
 * Synchronized
 */
public class Sensores {
    
    public final Map<String, Boolean> elementos = Collections.synchronizedMap(new TreeMap<String, Boolean>());
    
    public Sensores() {
        
    }
    
    public Sensores(String[] nombres) {
        insertar(nombres);
    }
    
    public Sensores(Sensores sensores) {
        insertar(sensores.elementos);
    }
    
    @Override
    public Sensores clone() {
        return new Sensores(this);
    }
    
    public boolean get(String sensor) {
        return elementos.get(sensor);
    }
    
    public boolean get(int i) {
        return (Boolean)values().toArray()[i];
    }
    
    public void set(int i, boolean estado) {
        elementos.put((String)keySet().toArray()[i], estado);
    }

    public void set(String sensor, boolean estado) {
        elementos.put(sensor, estado);
    }
    
    public Set<String> keySet() {
        return elementos.keySet();
    }
    
    public Collection<Boolean> values() {
        return elementos.values();
    }
    
    public int size() {
        return elementos.size();
    }
    
    public int codificar() {
        return Integer.parseInt(toBinaryString(), 2);
    }
    
    protected String toBinaryString() {
        char[] chars = new char[Math.max(Profibus.SIZE, size())];
        Arrays.fill(chars, '0');
        for (int i = 0; i < size(); i++) {
            chars[chars.length-1-i] = get(i) ? '1' : '0';
        }
        String str = new String(chars);
        return str;
    }

    
    public void actualizar(int sensores) {
        actualizar(Integer.toBinaryString(sensores));
    }
    
    
    public void actualizar(String sensores) {
        char[] chars = sensores.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            set(i, chars[chars.length-1-i] == '1');
        }
    }
    
    public void actualizar(Sensores sensores) {
        for(String sensor : sensores.keySet()) {
            set(sensor, sensores.get(sensor));
        }
    }
    
    public void inicializar() {
        actualizar(0);
    }

    @Override
    public String toString() {
        return "[" + toBinaryString() + "] " + elementos;
    }

    public void inicializar(char sensores) {
        this.inicializar();
        this.actualizar(sensores);
    }


    public void insertar(Sensores sensores) {
        this.elementos.putAll(sensores.elementos);
    }
    public void insertar(Map<String, Boolean> elementos) {
        this.elementos.putAll(elementos);
    }

    public void insertar(String[] nombres) {
        for(String nombre : nombres)
            this.elementos.put(nombre, false);
    }
    
};