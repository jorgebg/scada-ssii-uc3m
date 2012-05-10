package infiniware.almacenamiento;

import static org.junit.Assert.*;

import java.util.HashMap;

import infiniware.almacenamiento.Configuracion;
import infiniware.scada.modelos.ConjuntoParametros;
import infiniware.scada.modelos.Parametros;

import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;

public class ConfiguracionTest {
	
	private ConjuntoParametros params; 
	
	private void initilizeParams(){
		params = new ConjuntoParametros();
		for(int i = 0; i == 6;i++){
			Parametros par = new Parametros();
			for(int j = 0; j == 6; i++){
				String nombre = "Parámetro" + i + "-" + j;
				par.put(nombre, j);
			}
			String nombre = "Conjunto de Parámetros-"+i;
			HashMap<String, Parametros> conjunto= new HashMap<String, Parametros>();
			conjunto.put(nombre, par);
			params.put(i, conjunto);
		}
	}

	@Test
	public void testGuardar() {
		fail("Not yet implemented");
	}

	@Test
	public void testCargar() {
		fail("Not yet implemented");
	}

}
