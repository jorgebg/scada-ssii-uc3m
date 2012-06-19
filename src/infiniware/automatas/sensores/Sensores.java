package infiniware.automatas.sensores;

import infiniware.remoto.Profibus;
import java.util.*;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 * Adapter de TreeMap Synchronized
 */
public class Sensores {

    public final Map<String, Boolean> elementos = Collections.synchronizedMap(new TreeMap<String, Boolean>());

    public Sensores() {
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
        elementos.put(key, estado);
    }

    public synchronized void set(String sensor, boolean estado) {
        //Si el sensor no existe, no actualizar nada
        if (!this.elementos.containsKey(sensor)) {
            return;
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
        //System.out.println("CODIFICANDO \n" + clone);
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
        String binary = StringUtils.leftPad(Integer.toBinaryString(sensores), this.elementos.size(), "0");
        //System.out.println(binary);
        actualizar(binary);
    }

    public synchronized void actualizar(String sensores) {
        char[] chars = sensores.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int index = chars.length - 1 - i;
            set(i, chars[index] == '1');
        }
    }

    public synchronized void actualizar(Sensores sensores) {
        for (String sensor : sensores.keySet()) {
            set(sensor, sensores.get(sensor));
        }
    }

    public synchronized void actualizar(char sensores, List<String> ignorar) {
        Map<String, Boolean> valoresIgnorar = new HashMap<String, Boolean>();
        for (String sensor : ignorar) {
            if (elementos.containsKey(sensor)) {
                boolean valor = get(sensor);
                valoresIgnorar.put(sensor, valor);
            }
        }
        actualizar(sensores);
        for (Map.Entry<String, Boolean> entry : valoresIgnorar.entrySet()) {
            set(entry.getKey(), entry.getValue());
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
     * resultado += "\n" + mascara; } return resultado; }
     */
    public String toString() {
        String sensores = "";
        String valores = "";
        for (Map.Entry<String, Boolean> entry : elementos.entrySet()) {
            String valor = entry.getValue() ? "1" : "0";
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
        insertar(sensores.elementos);
    }

    public void insertar(Map<String, Boolean> elementos) {
        for (Map.Entry<String, Boolean> entry : elementos.entrySet()) {
            String key = entry.getKey();
            Boolean value = entry.getValue();
            this.elementos.put(key, value.booleanValue());
        }
        //System.out.println(this);
    }

    public void insertar(String[] nombres) {
        insertar(nombres, false);
    }

    public void insertar(String nombre) {
        insertar(new String[]{nombre}, false);
    }

    public void insertar(String nombre, boolean estado) {
        insertar(new String[]{nombre}, estado);
    }

    public void insertar(String[] nombres, boolean estado) {
        for (String nombre : nombres) {
            this.elementos.put(nombre, estado);
        }
    }

    public boolean get(String sensor, char sensores) {
        Entry<String, Boolean> estado = this.decodificar(sensores);
        return estado.getKey().equals(sensor) && estado.getValue();
    }

    public int codificar(String sensor, boolean estado) {
        int codigo = -1;
        if (this.elementos.containsKey(sensor)) {
            codigo = 0;
            for (String key : elementos.keySet()) {
                if (key.equals(sensor)) {
                    break;
                } else {
                    codigo++;
                }
            }
            if (estado) {
                codigo += this.elementos.keySet().size();
            }
        }
        return codigo;
    }

    public Map.Entry<String, Boolean> decodificar(int codigo) {
        String sensor = null;
        boolean estado = false;
        if(codigo >= elementos.size() ) {
            codigo -= elementos.size();
            estado = true;
        }
        
        int i = 0;
            for (String key : elementos.keySet()) {
                if(i==codigo) {
                    sensor = key;
                    break;
                }
                else i++;
            }
        Map.Entry<String, Boolean> result = new ImmutablePair<String, Boolean>(sensor, estado);
        return result;
    }
    
    public Map.Entry<String, Boolean> decodificarYActualizar(int codigo) {
        Map.Entry<String, Boolean> decodificado = decodificar(codigo);
        this.set(decodificado.getKey(), decodificado.getValue());
        return decodificado;
    }
};
