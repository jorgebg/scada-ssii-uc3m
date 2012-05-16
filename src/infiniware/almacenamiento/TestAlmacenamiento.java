package infiniware.almacenamiento;

import java.io.IOException;

import infiniware.scada.modelos.Parametros;

public class TestAlmacenamiento {

	/**
	 * @param args
	 */
	public static void main(String[] args){
		// TODO Auto-generated method stub		
		Parametros parametros = new Parametros();
		parametros.put("Parametro 1", 1);
		Configuracion configuracion = new Configuracion();
		configuracion.guardar("Configuracion 1", parametros);
		Parametros parametros_test = configuracion.cargar("Configuracion 1");
		if(parametros.equals(parametros_test)){
			System.out.println("Son iguales");
		}else{
			System.out.println("Son distintos");
		}

	}

}
