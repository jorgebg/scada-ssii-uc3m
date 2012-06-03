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
import infiniware.scada.modelos.ConjuntoParametros;
import infiniware.scada.modelos.Guardable;
import infiniware.scada.modelos.Parametros;

public class Configuracion extends Componente {

	private String carpeta = "almacenamiento";
	
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

    public Resultado cargar(String nombre, ConjuntoParametros parametros) {
    	try{
        	File fi = new File(carpeta, nombre);
    		FileInputStream fis = new FileInputStream(fi);
    		InputStreamReader in = new InputStreamReader(fis);
    		BufferedReader br = new BufferedReader(in);
    		String key = br.readLine().trim().substring(0, 1);
    		while(key != null && key != "" ){
    			String value = br.readLine().trim();
    			StringTokenizer st = new StringTokenizer(value, ":");
    			String mapKey = st.nextToken();
    			String parametrosKey = st.nextToken().trim();
    			parametrosKey = parametrosKey.substring(1, parametrosKey.length());
    			String parametrosValue = st.nextToken().trim().substring(0, 1);
    			HashMap<String, Guardable> map = new HashMap<String, Guardable>();
    			Guardable param = new Parametros();
    			param.put(parametrosKey, Integer.valueOf(parametrosValue));
    			map.put(mapKey, param);
    			parametros.put(Integer.valueOf(key), map);   			
    			key = br.readLine();
    		}
        }catch (IOException e){
        	return Resultado.ERROR;
        }
    	return Resultado.CORRECTO;
    }

}
