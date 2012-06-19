
package infiniware.scada.informes;

import java.util.HashMap;
import java.util.LinkedHashMap;

import infiniware.Resultado;
import infiniware.scada.Scada;
import infiniware.scada.informes.modelos.Fabricacion;
import infiniware.scada.informes.modelos.Funcionamiento;
import infiniware.scada.modelos.ConjuntoGuardable;
import infiniware.scada.modelos.Guardable;

/**
 * 
 * @author Infiniware
 * 
 * Clase agregada de Informe que contiene 2 informes uno de Fabricacion y otro de Funcionamiento
 *
 */
public class Informes extends ConjuntoGuardable{
	
        public static Informes INSTANCIA = new Informes(new Fabricacion(), new Funcionamiento());
    
	/**
	 * Constructor al que se le pasan un informe de fabricacion y otro de funcionamiento
	 * 
	 * @param fabricacion
	 * @param funcionamiento
	 */
	public Informes(Fabricacion fabricacion, Funcionamiento funcionamiento){
		HashMap<String, Guardable> map = new HashMap<String, Guardable>();
		map.put("fabricacion", fabricacion);
		put(1, map);
		HashMap<String, Guardable> map1 = new HashMap<String, Guardable>();
		map1.put("funcionamiento", funcionamiento);
		put(2,map1);
	}
	
	/**
	 * Constructor por defecto 
	 * 
	 */
	public Informes(){
		HashMap<String, Guardable> map = new HashMap<String, Guardable>();
		map.put("fabricacion", null);
		put(1, map);
		HashMap<String, Guardable> map1 = new HashMap<String, Guardable>();
		map1.put("funcionamiento", null);
		put(2,map1);
	}
    
	/**
	 * Metodo que genera los informes
	 * 
	 * @param scada
	 * @return
	 */
	public static Informes generar(Scada scada){
		return null;
	}
	
	/**
	 * Metodo que devuelve el informe de fabricacion
	 * 
	 * @return informe de fabricacion
	 */
	public Fabricacion getFabricacion(){
		return (Fabricacion) get(1).get("fabricacion");
	}
	
	/**
	 * Metodo que devuelve el informe de funcionamiento
	 * 
	 * @return informe de funcionamiento
	 */
	public Funcionamiento getFuncionamiento(){
		return (Funcionamiento) get(2).get("funcionamiento");
	}
	
	/**
	 * Metodo que establece el valor del informe de fabricacion
	 * 
	 * @param fabricacion
	 * @return resultado de la operacion
	 */
	public Resultado setFabricacion(Fabricacion fabricacion){
		HashMap<String, Guardable> map = new HashMap<String, Guardable>();
		map.put("fabricacion", fabricacion);
		put(1,map);
		return Resultado.CORRECTO;
	}
	
	/**
	 * Metodo que establece el valor del informe de funcionamiento
	 * 
	 * @param funcionamiento
	 * @return resultado de la operacion
	 */
	public Resultado setFuncionamiento(Funcionamiento funcionamiento){
		HashMap<String, Guardable> map = new HashMap<String, Guardable>();
		map.put("funcionamiento", funcionamiento);
		put(2,map);
		return Resultado.CORRECTO;
	}
	

}
