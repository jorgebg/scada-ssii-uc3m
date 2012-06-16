package infiniware.almacenamiento;

import static org.junit.Assert.*;

import java.util.HashMap;

import infiniware.Resultado;
import infiniware.almacenamiento.Configuracion;
import infiniware.scada.modelos.ConjuntoParametros;
import infiniware.scada.modelos.Guardable;
import infiniware.scada.modelos.Parametros;
import org.junit.Test;

public class ConfiguracionTest {
	
	@Test
	public void testGuardar() {
		ConjuntoParametros parametros = new ConjuntoParametros();
		Parametros param = new Parametros();
		param.put("Parametro 1", 1);
		HashMap<String, Guardable> mapa = new HashMap<String, Guardable>();
		mapa.put("Automata 1", param);
		parametros.put(1, mapa);
		Configuracion configuracion = new Configuracion();
		String nombre = "Configuracion 1";
		Resultado res = configuracion.guardar(nombre, parametros);
		assertEquals(Resultado.CORRECTO, res);
	}

	@Test
	public void testCargar() {
		ConjuntoParametros parametros = new ConjuntoParametros();
		Parametros param = new Parametros();
		param.put("Parametro 1", 1);
		param.put("Parametro 2", 7);
		param.put("Parametro 3", 5);
		param.put("Parametro 4", 8);
		HashMap<String, Guardable> mapa = new HashMap<String, Guardable>();
		mapa.put("Automata 1", param);
		Parametros parame = new Parametros();
		parame.put("Parametro 1", 1);
		mapa.put("Automata 2", parame);
		Configuracion configuracion = new Configuracion();
		String nombre = "Configuracion 1";
		parametros.put(1 , mapa);
		param = new Parametros();
		param.put("Parametro 1", 1);
		param.put("Parametro 2", 7);
		param.put("Parametro 3", 5);
		param.put("Parametro 4", 8);
		mapa = new HashMap<String, Guardable>();
		mapa.put("Automata 1", param);
		parame = new Parametros();
		parame.put("Parametro 1", 1);
		mapa.put("Automata 2", parame);
		parametros.put(2 , mapa);
		Resultado res = configuracion.guardar(nombre, parametros);
		assertEquals(Resultado.CORRECTO, res);
		ConjuntoParametros parametros_test = new ConjuntoParametros();
		String nombre_erroneo =  "ERROR";
		res = configuracion.cargar(nombre_erroneo, parametros_test);
		HashMap<String, Guardable> ma = parametros_test.get(1);
		assertNull(ma);
		res = configuracion.cargar(nombre, parametros_test);
		assertEquals(parametros, parametros_test);
		assertEquals(Resultado.CORRECTO, res);
	}

}
