package infiniware.almacenamiento;

import static org.junit.Assert.*;
import java.io.*;
import java.util.HashMap;
import infiniware.almacenamiento.Configuracion;
import infiniware.scada.modelos.Parametros;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

public class ConfiguracionTest {
		
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	    System.setErr(null);
	}
	
	@Test
	public void testGuardar() {
		Parametros parametros = new Parametros();
		parametros.put("Parametro 1", 1);
		Configuracion configuracion = new Configuracion();
		configuracion.guardar("Configuracion 1", parametros);
		//System.out.println("Configuracion " + "Parametro 1" + " guardada correctamente");
		assertEquals("Configuracion " + "Parametro 1" + " guardada correctamente", outContent.toString());
	}

	@Test
	public void testCargar() {
		Parametros parametros = new Parametros();
		parametros.put("Parametro 1", 1);
		Configuracion configuracion = new Configuracion();
		configuracion.guardar("Configuracion 1", parametros);
		Parametros parametros_test = configuracion.cargar("Configuracion 1");
		assertEquals(parametros, parametros_test);
	}

}
