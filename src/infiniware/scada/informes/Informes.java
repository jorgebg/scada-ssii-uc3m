
package infiniware.scada.informes;

import java.util.HashMap;
import java.util.LinkedHashMap;

import infiniware.Resultado;
import infiniware.scada.Scada;
import infiniware.scada.informes.modelos.Fabricacion;
import infiniware.scada.informes.modelos.Funcionamiento;
import infiniware.scada.modelos.ConjuntoGuardable;
import infiniware.scada.modelos.Guardable;

public class Informes extends ConjuntoGuardable{
	
	
	public Informes(Fabricacion fabricacion, Funcionamiento funcionamiento){
		HashMap<String, Guardable> map = new HashMap<String, Guardable>();
		map.put("fabricacion", fabricacion);
		put(1, map);
		HashMap<String, Guardable> map1 = new HashMap<String, Guardable>();
		map1.put("funcionamiento", funcionamiento);
		put(2,map1);
	}
	
	public Informes(){
		HashMap<String, Guardable> map = new HashMap<String, Guardable>();
		map.put("fabricacion", null);
		put(1, map);
		HashMap<String, Guardable> map1 = new HashMap<String, Guardable>();
		map1.put("funcionamiento", null);
		put(2,map1);
	}
    
	public static Informes generar(Scada scada){
		return null;
	}
	
	public Fabricacion getFabricacion(){
		return (Fabricacion) get(1).get("fabricacion");
	}
	
	public Funcionamiento getFuncionamiento(){
		return (Funcionamiento) get(2).get("funcionamiento");
	}
	
	public Resultado setFabricacion(Fabricacion fabricacion){
		HashMap<String, Guardable> map = new HashMap<String, Guardable>();
		map.put("fabricacion", fabricacion);
		put(1,map);
		return Resultado.CORRECTO;
	}
	
	public Resultado setFuncionamiento(Funcionamiento funcionamiento){
		HashMap<String, Guardable> map = new HashMap<String, Guardable>();
		map.put("funcionamiento", funcionamiento);
		put(2,map);
		return Resultado.CORRECTO;
	}
	

}
