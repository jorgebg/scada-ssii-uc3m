package infiniware.automatas.sensores;

import infiniware.remoto.Profibus;
import java.util.*;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Adapter de TreeMap Synchronized
 */
public class Sensores {

    public final Map<String, Boolean> elementos = Collections.synchronizedMap(new TreeMap<String, Boolean>());
    public Sensores actualizados;

    public Sensores() {
        this(true);
    }

    public Sensores(boolean actualizados) {
        if (actualizados) {
            this.actualizados = new Sensores(false);
        }
    }

    public Sensores(String[] nombres) {
        this(nombres, true);
    }

    public Sensores(String[] nombres, boolean estado) {
        this();
        insertar(nombres, estado);
    }

    public Sensores(Sensores sensores) {
        this();
        insertar(sensores.elementos);
    }

    @Override
    public synchronized Sensores clone() {
        return new Sensores(this);
    }

    public synchronized boolean get(String sensor) {
        return elementos.get(sensor);
    }

    public synchronized boolean get(int i) {
        return (Boolean) values().toArray()[i];
    }

    public synchronized void set(int i, boolean estado) {
        String key = (String) keySet().toArray()[i];
        if (actualizados != null && estado == elementos.get(key)) {
            actualizados.set(key, estado);
        }
        elementos.put(key, estado);
    }

    public synchronized void set(String sensor, boolean estado) {
        //Si el sensor no existe, no actualizar nada
        if(!this.elementos.containsKey(sensor))
            return;
        if (actualizados != null && estado == elementos.get(sensor)) {
            actualizados.set(sensor, estado);
        }
        elementos.put(sensor, estado);
    }

    public synchronized Set<String> keySet() {
        return elementos.keySet();
    }

    public synchronized Collection<Boolean> values() {
        return elementos.values();
    }

    public synchronized int size() {
        return elementos.size();
    }

    public synchronized int codificar(Sensores sensores) {
        Sensores clone = clone();
        clone.actualizar(sensores);
        return clone.codificar();
    }
    
    public synchronized int codificar() {
        return Integer.parseInt(toBinaryString(), 2);
    }

    protected synchronized String toBinaryString() {
        char[] chars = new char[Math.max(Profibus.SIZE, size())];
        Arrays.fill(chars, '0');
        for (int i = 0; i < size(); i++) {
            chars[chars.length - 1 - i] = get(i) ? '1' : '0';
        }
        String str = new String(chars);
        return str;
    }

    public synchronized void actualizar(int sensores) {
        actualizar(Integer.toBinaryString(sensores));
    }

    public synchronized void actualizar(int sensores, int mascara) {
        actualizar(Integer.toBinaryString(sensores), Integer.toBinaryString(mascara));
    }

    public synchronized void actualizar(String sensores, String mascara) {
        char[] chars = sensores.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int index = chars.length - 1 - i;
            if (mascara.length() - 1 >= index && mascara.charAt(index) == '1') {
                set(i, chars[index] == '1');
            }
        }
    }

    public synchronized void actualizar(String sensores) {
        actualizar(sensores, StringUtils.repeat("1", sensores.length()));
    }

    public synchronized void actualizar(Sensores sensores) {
        for (String sensor : sensores.keySet()) {
            set(sensor, sensores.get(sensor));
        }
    }

    public void inicializar() {
        actualizar(0);
    }

    @Override
    /*
     * public String toString() { String sensores = ""; String valores = "";
     * String mascara = ""; for (Map.Entry<String, Boolean> entry :
     * elementos.entrySet()) { sensores += StringUtils.rightPad(entry.getKey(),
     * 3); valores += StringUtils.rightPad(entry.getValue() ? "1" : "0", 3); if
     * (actualizados != null) mascara +=
     * StringUtils.rightPad(actualizados.get(entry.getKey()) ? "1" : "0", 3); }
     * String resultado = sensores + "\n" + valores; if(!mascara.isEmpty()){
     * resultado += "\n" + mascara; } return resultado;
    }
     */
    public String toString() {
        String sensores = "";
        String valores = "";
        for (Map.Entry<String, Boolean> entry : elementos.entrySet()) {
            String valor = entry.getValue() ? "1" : "0";
            if (actualizados != null && actualizados.get(entry.getKey())) {
                valor += "*";
            }
            sensores += StringUtils.rightPad(entry.getKey(), 3);
            valores += StringUtils.rightPad(valor, 3);

        }
        String resultado = sensores + "\n" + valores;
        return resultado;
    }

    public void inicializar(char sensores) {
        this.inicializar();
        this.actualizar(sensores);
    }

    public void insertar(Sensores sensores) {
        this.elementos.putAll(sensores.elementos);
        if (actualizados != null) {
            actualizados.elementos.putAll(elementos);
        }
    }

    public void insertar(Map<String, Boolean> elementos) {
        this.elementos.putAll(elementos);
        if (actualizados != null) {
            actualizados.elementos.putAll(elementos);
        }
    }

    public void insertar(String[] nombres) {
        insertar(nombres, false);
    }
    
    public void insertar(String nombre) {
        insertar(new String[] { nombre }, false);
    }

    public void insertar(String[] nombres, boolean estado) {
        for (String nombre : nombres) {
            this.elementos.put(nombre, estado);
        }
        if (actualizados != null) {
            actualizados.elementos.putAll(elementos);
        }
    }
};