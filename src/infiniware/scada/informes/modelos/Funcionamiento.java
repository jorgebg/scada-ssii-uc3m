package infiniware.scada.informes.modelos;

import infiniware.Resultado;
import infiniware.scada.informes.Informe;
import infiniware.scada.modelos.Guardable;

public class Funcionamiento  extends Informe{

	public Funcionamiento(){
		put("normales", 0);
		put("emergencia", 0);
		put("arranques", 0);
	}
	
	public Funcionamiento(int normales, int emergencia, int arranques){
		put("normales", normales);
		put("emergencia", emergencia);
		put("arranques", arranques);
	}
	
	public Funcionamiento(Funcionamiento funcionamiento){
		put("normales", funcionamiento.getNormales());
		put("emergencia", funcionamiento.getEmergencia());
		put("arranques", funcionamiento.getArranques());
	}
	
	public Guardable extraer(String[] parametros) {
        Funcionamiento nuevos = new Funcionamiento();
        for(String clave : parametros)
            nuevos.put(clave, get(clave));
        return nuevos;
    }
	
	public Resultado addNormales(){
		String key = "normales"; 
		int correctos = get(key);
		correctos ++;
		put(key, correctos);
		return Resultado.CORRECTO;
	}
	
	public Resultado addEmergencia(){
		String key = "emergencia"; 
		int correctos = get(key);
		correctos ++;
		put(key, correctos);
		return Resultado.CORRECTO;
	}
	
	public Resultado addArranques(){
		String key = "arranques"; 
		int correctos = get(key);
		correctos ++;
		put(key, correctos);
		return Resultado.CORRECTO;
	}
	
	public int getNormales(){
		return get("normales");
	}
	
	public int getArranques(){
		return get("arranques");
	}
	
	public int getEmergencia(){
		return get("emergencia");
	}
}
