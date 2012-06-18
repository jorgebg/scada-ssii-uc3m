package infiniware.almacenamiento;

import infiniware.Resultado;
import infiniware.scada.informes.Informe;
import infiniware.scada.informes.Informes;
import infiniware.scada.informes.modelos.Fabricacion;
import infiniware.scada.informes.modelos.Funcionamiento;
import infiniware.scada.modelos.ConjuntoParametros;
import infiniware.scada.modelos.Guardable;
import infiniware.scada.modelos.Parametros;

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

/**
 * 
 * @author Infiniware
 * 
 * Clase encargada de guardar y cargar los informes de produccion
 *
 */
public class Produccion extends Componente {
	private String carpeta = "almacenamiento";
	private String fichero = "produccion.ser";

	
	/**
	 * Metodo que guarda los informes de produccion
	 * 
	 * @param informes
	 * @return resultado de la operacion
	 */
    public Resultado guardar(Informes informes){
        try{
        	File folder= new File(carpeta);
			folder.mkdirs();
			File fi = new File(folder, fichero);
			FileOutputStream fos = new FileOutputStream(fi);
			OutputStreamWriter out = new OutputStreamWriter(fos);
			Yaml yaml = new Yaml();
			yaml.dump(informes, out);
			out.close(); 
        	return Resultado.CORRECTO;
        }catch (IOException e){
        	return Resultado.ERROR;
        }
    }

    /**
     * Metodo que carga los informes de producion
     * 
     * @param informes
     * @return resultado de la operacion
     */
    public Resultado cargar(Informes informes) {
    	try{
        	File fi = new File(carpeta, fichero);
    		FileInputStream fis = new FileInputStream(fi);
    		InputStreamReader in = new InputStreamReader(fis);
    		BufferedReader br = new BufferedReader(in);
    		String key = br.readLine(); 	
			String value = br.readLine().trim();
			Fabricacion fab = leerFabricacion(value);
    		key = br.readLine();
    		value = br.readLine().trim();
    		Funcionamiento fun = leerFuncionamiento(value);
    		informes.setFabricacion(fab);
    		informes.setFuncionamiento(fun);
        }catch (IOException e){
        	return Resultado.ERROR;
        }
    	return Resultado.CORRECTO;
    }
    
    private Fabricacion leerFabricacion(String value){
    	StringTokenizer st = new StringTokenizer(value, ":");
		String correctos = st.nextToken();
		correctos = st.nextToken().trim();
		correctos = st.nextToken(",").trim();
		correctos = correctos.substring(2, correctos.length());
		String incorrectos = st.nextToken(":").trim();
		incorrectos = st.nextToken(":").trim();
		incorrectos = incorrectos.substring(0, 1);
		return new Fabricacion(Integer.valueOf(correctos), Integer.valueOf(incorrectos));
    }
    
    private Funcionamiento leerFuncionamiento(String value){
    	StringTokenizer st = new StringTokenizer(value, ":");
		String arranques = st.nextToken();
		arranques = st.nextToken().trim();
		arranques = st.nextToken(",").trim();
		arranques = arranques.substring(2, arranques.length());
		String normales = st.nextToken(",").trim();
		normales = normales.substring(10, normales.length());
		String emergencias = st.nextToken(":").trim();
		emergencias = st.nextToken(":").trim();
		emergencias = emergencias.substring(0, 1);
		return new Funcionamiento(Integer.valueOf(normales), Integer.valueOf(emergencias), Integer.valueOf(arranques));
    }
}
