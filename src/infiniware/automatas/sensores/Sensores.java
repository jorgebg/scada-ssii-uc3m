package infiniware.automatas.sensores;

import infiniware.remoto.Profibus;
import java.util.*;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Adapter de TreeMap
 * Synchronized
 */
public class Sensores {
    
    public final Map<String, Boolean> elementos = Collections.synchronizedMap(new TreeMap<String, Boolean>());
    public Sensores actualizados;
    
    public Sensores() {
        this(true);
    }
    
    public Sensores(boolean actualizados) {
        if(actualizados)
            this.actualizados = new Sensores(false);
    }
    
    public Sensores(String[] nombres) {
        this();
        insertar(nombres);
    }
    
    public Sensores(Sensores sensores) {
        this();
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
        if(actualizados!=null)
            actualizados.set(i, estado);
    }

    public void set(String sensor, boolean estado) {
        elementos.put(sensor, estado);
        if(actualizados!=null)
            actualizados.set(sensor, estado);
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
        if(actualizados!=null)
            actualizados.actualizar(sensores);
    }
    
    public void actualizar(int sensores, int mascara) {
        actualizar(Integer.toBinaryString(sensores), Integer.toBinaryString(mascara));
    }
    
    
    public void actualizar(String sensores, String mascara) {
        char[] chars = sensores.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int index = chars.length-1-i;
            if(mascara.charAt(index) == '1')
                set(i, chars[index] == '1');
        }
    }
    
    public void actualizar(String sensores) {
        actualizar(sensores, StringUtils.repeat("1", sensores.length()));
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
        if(actualizados != null)
            actualizados.elementos.putAll(elementos);
    }
    public void insertar(Map<String, Boolean> elementos) {
        this.elementos.putAll(elementos);
        if(actualizados != null)
            actualizados.elementos.putAll(elementos);
    }

    public void insertar(String[] nombres) {
        for(String nombre : nombres)
            this.elementos.put(nombre, false);
        if(actualizados != null)
            actualizados.elementos.putAll(elementos);
    }
    
};