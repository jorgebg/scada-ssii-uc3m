package infiniware.scada.informes.modelos;

import infiniware.Resultado;
import infiniware.scada.informes.Informe;
import infiniware.scada.modelos.Guardable;

public class Fabricacion extends Informe{
	
	public Fabricacion(){
		put("correctos", 0);
		put("incorrectos", 0);
	}
	
	public Fabricacion(int correctos, int incorrectos){
		put("correctos", correctos);
		put("incorrectos", incorrectos);
	}
	
	public Fabricacion(Fabricacion fabricacion){
		put("correctos", fabricacion.getCorrectos());
		put("incorrectos", fabricacion.getIncorrectos());
	}
	
	public Guardable extraer(String[] parametros) {
        Fabricacion nuevos = new Fabricacion();
        for(String clave : parametros)
            nuevos.put(clave, get(clave));
        return nuevos;
    }
	
	public Resultado addCorrecto(){
		String key = "correctos"; 
		int correctos = get(key);
		correctos ++;
		put(key, correctos);
		return Resultado.CORRECTO;
	}
	
	public Resultado addIncorrecto(){
		String key = "incorrectos"; 
		int correctos = get(key);
		correctos ++;
		put(key, correctos);
		return Resultado.CORRECTO;
	}
	
	public int getCorrectos(){
		return get("correctos");
	}
	
	public int getIncorrectos(){
		return get("incorrectos");
	}
}
