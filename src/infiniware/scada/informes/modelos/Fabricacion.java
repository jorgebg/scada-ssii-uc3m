package infiniware.scada.informes.modelos;

import infiniware.Resultado;
import infiniware.scada.informes.Informe;
import infiniware.scada.modelos.Guardable;

/**
 * 
 * @author Infiniware
 * 
 * Clase que representa un informe de fabricacion
 *
 */
public class Fabricacion extends Informe{
	
	/**
	 * Constructor por defecto de la calse fabricacion
	 * 
	 */
	public Fabricacion(){
		put("correctos", 0);
		put("incorrectos", 0);
	}
	
	/**
	 * Constructor con parametros pedefinidos
	 * 
	 * @param correctos
	 * @param incorrectos
	 */
	public Fabricacion(int correctos, int incorrectos){
		put("correctos", correctos);
		put("incorrectos", incorrectos);
	}
	
	/**
	 * Constructor de copia
	 * 
	 * @param fabricacion
	 */
	public Fabricacion(Fabricacion fabricacion){
		put("correctos", fabricacion.getCorrectos());
		put("incorrectos", fabricacion.getIncorrectos());
	}
	
	/**
	 * Metodo que extrae los elementos del informe de acuerdo a un array de claves
	 * 
	 */
	public Guardable extraer(String[] elementos) {
        Fabricacion nuevos = new Fabricacion();
        for(String clave : elementos)
            nuevos.put(clave, get(clave));
        return nuevos;
    }
	
	/**
	 * Metodo que añade un elemento correcto al informe de fabricacion
	 * 
	 */
	public Resultado addCorrecto(){
		String key = "correctos"; 
		int correctos = get(key);
		correctos ++;
		put(key, correctos);
		return Resultado.CORRECTO;
	}
	
	/**
	 * Metodo que añade un elemento incorrecto al informe de fabricacion
	 * 
	 */
	public Resultado addIncorrecto(){
		String key = "incorrectos"; 
		int correctos = get(key);
		correctos ++;
		put(key, correctos);
		return Resultado.CORRECTO;
	}
	
	/**
	 * Metodo que devuelve los elementos correctos que se han fabricado
	 * 
	 * @return valor de elementos correctos
	 */
	public int getCorrectos(){
		return get("correctos");
	}
	
	/**
	 * Metodo que devuelve los elementos incorrectos que se han fabricado
	 * 
	 * @return valor de elementos incorrectos
	 */
	public int getIncorrectos(){
		return get("incorrectos");
	}
}
