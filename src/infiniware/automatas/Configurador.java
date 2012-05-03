package infiniware.automatas;

import infiniware.automatas.subautomatas.SubAutomata;
import java.io.*;
import java.util.*;
import org.yaml.snakeyaml.Yaml;

public class Configurador {

    public static final String ARCHIVO = "src/automatas.yaml";
    private static Map<String, Object> configuracion;

    private static void cargar() {
        if (configuracion == null) {
            InputStream input = null;
            File file = new File(ARCHIVO).getAbsoluteFile();
            try {
                input = new FileInputStream(file);
            } catch (FileNotFoundException ex) {
                System.err.println("No se ha encontrado el fichero \"" + file + "\"");
            }
            Yaml yaml = new Yaml();
            //configuracion = (Map<String, Map<String, Map<String, Map<String, String>>>>) yaml.load(input);
            configuracion = (Map<String, Object>) yaml.load(input);
            try {
                input.close();
            } catch (IOException ex) {
                System.err.println("Error al cerrar el fichero \"" + file + "\"");
            }
        }
    }

    public static void configurar() {
        cargar();
        for (String nombreAutomata : configuracion.keySet()) {
            Map<String, Object> configuracionAutomata = (Map<String, Object>) configuracion.get(nombreAutomata);
            Automata automata = Automata.INSTANCIAS.get(nombreAutomata);
            Map<String, SubAutomata> subautomatas = new HashMap<String, SubAutomata>();
            for (String nombreSubAutomata : configuracionAutomata.keySet()) {
                Map<String, Object> configuracionSubAutomata = (Map<String, Object>) configuracionAutomata.get(nombreSubAutomata);
                SubAutomata subautomata = automata.subautomatas.get(nombreSubAutomata);
                Set<String> sensores = new HashSet<String>();
                List<String> estados = new ArrayList<String>();
                List<Transicion> transiciones = new ArrayList<Transicion>();
                for (String origen : configuracionSubAutomata.keySet()) {
                    if (!estados.contains(origen)) {
                        estados.add(origen);
                    }
                    List<String> configuracionEstados = (List<String>)configuracionSubAutomata.get(origen);
                    for (String transicion : configuracionEstados) {
                        String[] partes = transicion.split("\\s+=>\\s+");
                        String condicion = partes[0];
                        String destino = partes[1];
                        if (!estados.contains(destino)) {
                            estados.add(destino);
                        }
                        transiciones.add(new Transicion(subautomata, estados.indexOf(origen), condicion, estados.indexOf(destino)));
                        for (String sensor : condicion.split("[^\\p{L}\\p{N}]")) {
                            if (!sensores.contains(sensor)) {
                                sensores.add(sensor);
                            }
                        }
                    }
                    subautomata.estados = estados;
                    subautomata.transiciones = transiciones;
                }
            }
            //set automata.sensores
        }
    }

}
