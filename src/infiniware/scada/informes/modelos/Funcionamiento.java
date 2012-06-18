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
public class Funcionamiento  extends Informe{

	/**
	 * Constructor por defecto de la calse funcioanamiento
	 * 
	 */
	public Funcionamiento(){
		put("normales", 0);
		put("emergencia", 0);
		put("arranques", 0);
	}
	
	/**
	 * Constructor con parametros pedefinidos
	 * 
	 * @param normales
	 * @param emergencia
	 * @param arranques
	 */
	public Funcionamiento(int normales, int emergencia, int arranques){
		put("normales", normales);
		put("emergencia", emergencia);
		put("arranques", arranques);
	}
	
	/**
	 * Constructor de copia
	 * 
	 * @param funcionamiento
	 */
	public Funcionamiento(Funcionamiento funcionamiento){
		put("normales", funcionamiento.getNormales());
		put("emergencia", funcionamiento.getEmergencia());
		put("arranques", funcionamiento.getArranques());
	}
	
	/**
	 * Metodo que extrae los elementos del informe de acuerdo a un array de claves
	 * 
	 */
	@Override
	public Guardable extraer(String[] elementos) {
        Funcionamiento nuevos = new Funcionamiento();
        for(String clave : elementos)
            nuevos.put(clave, get(clave));
        return nuevos;
    }
	
	/**
	 * Metodo que añade una parada normal al informe de funcionamiento
	 * 
	 */
	public Resultado addNormales(){
		String key = "normales"; 
		int correctos = get(key);
		correctos ++;
		put(key, correctos);
		return Resultado.CORRECTO;
	}
	
	/**
	 * Metodo que añade una parada de emergencia al informe de funcionamiento
	 * 
	 * @return
	 */
	public Resultado addEmergencia(){
		String key = "emergencia"; 
		int correctos = get(key);
		correctos ++;
		put(key, correctos);
		return Resultado.CORRECTO;
	}

	/**
	 * Metodo que añade un arranque al informe de funcionamiento
	 * 
	 * @return
	 */
	public Resultado addArranques(){
		String key = "arranques"; 
		int correctos = get(key);
		correctos ++;
		put(key, correctos);
		return Resultado.CORRECTO;
	}
	
	/**
	 * Metodo que devuelve las paradas normales que se han efectuado
	 * 
	 * @return valor de paradas normales
	 */
	public int getNormales(){
		return get("normales");
	}
	

	/**
	 * Metodo que devuelve los arranques que se han efectuado
	 * 
	 * @return valor de arranques
	 */
	public int getArranques(){
		return get("arranques");
	}
	
	/**
	 * Metodo que devuelve las paradas de emergencia que se han efectuado
	 * 
	 * @return valor de paradas de emergencia
	 */
	public int getEmergencia(){
		return get("emergencia");
	}
}
