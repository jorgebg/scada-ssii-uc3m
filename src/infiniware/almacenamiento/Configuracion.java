package infiniware.almacenamiento;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.yaml.snakeyaml.Yaml;

import infiniware.Resultado;
import infiniware.Sistema;
import infiniware.scada.modelos.ConjuntoParametros;
import infiniware.scada.modelos.Guardable;
import infiniware.scada.modelos.Parametros;

/**
 * 
 * @author Infiniware
 * 
 * Clase encargada de guardar y cargar los parametros de las ditintas configuraciones
 *
 */
public class Configuracion extends Componente {

	private String carpeta = "almacenamiento";
	
	/**
	 * Metodo que guarda los parametros en un fichero de nombre "nombre"
	 * 
	 * @param nombre
	 * @param parametros
	 * @return resultado de la operacion
	 */
    public Resultado guardar(String nombre, ConjuntoParametros parametros){
        try{
        	File folder= new File(carpeta);
			folder.mkdirs();
			File fi = new File(folder, nombre);
			FileOutputStream fos = new FileOutputStream(fi);
			OutputStreamWriter out = new OutputStreamWriter(fos);
			Yaml yaml = new Yaml();
			yaml.dump(parametros, out);
			out.close(); 
        	return Resultado.CORRECTO;
        }catch (IOException e){
        	return Resultado.ERROR;
        }
    }

    
    /**
     * Metodo que carga los parametros de un fichero de nombre "nombre"
     * 
     * @param nombre
     * @param parametros
     * @return resultado de la operacion
     */
    public Resultado cargar(String nombre, ConjuntoParametros parametros) {
    	try{
        	File fi = new File(carpeta, nombre);
    		FileInputStream fis = new FileInputStream(fi);
    		InputStreamReader in = new InputStreamReader(fis);
    		BufferedReader br = new BufferedReader(in);
    		String key = br.readLine();
    		HashMap<String, Guardable> map = null;
			int intKey = 0;
    		while(key != null && key != "" ){
    			String value = "";
    			boolean keychanged = true;
    			try{
    				value = key.trim();
    				key = key.trim().substring(0, 1);
    				intKey = Integer.valueOf(key);
    				value = br.readLine().trim();
    			}catch(Exception e){
    				keychanged = false;
    			}
    			Guardable param = new Parametros();
    			StringTokenizer st = new StringTokenizer(value, ":");
    			String mapKey = st.nextToken();
    			String parametro = st.nextToken(",").trim();
    			parametro = parametro.substring(3);
    			while (!parametro.endsWith("}")){
    				StringTokenizer st1 = new StringTokenizer(parametro, ":");
    		    	String nombr = st1.nextToken().trim();
    		    	String valor = st1.nextToken().substring(1);
    				param.put(nombr, Integer.valueOf(valor));
    				parametro = st.nextToken(",").trim();
    			}
    			parametro = parametro.substring(0, parametro.length()-1);
    			StringTokenizer st1 = new StringTokenizer(parametro, ":");
		    	String nombr = st1.nextToken().trim();
		    	String valor = st1.nextToken().substring(1);
				param.put(nombr, Integer.valueOf(valor));
				if(keychanged){
					map = new HashMap<String, Guardable>();	
				}
    			map.put(mapKey, param);
    			parametros.put(intKey, map);
    			key = br.readLine();
    		}
        }catch (IOException e){
        	return Resultado.ERROR;
        }
    	return Resultado.CORRECTO;
    }
    
}
