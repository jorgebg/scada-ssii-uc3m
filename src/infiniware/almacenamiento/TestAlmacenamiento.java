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

	}

}
