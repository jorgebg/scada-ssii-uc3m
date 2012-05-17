package infiniware.almacenamiento;

import static org.junit.Assert.*;
import infiniware.scada.modelos.Parametros;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProduccionTest {

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
		Produccion produccion = new Produccion();
		String nombre = "Producción 1";
		produccion.guardar(nombre, parametros);
		assertEquals("Producción " + nombre + " guardada correctamente\n", outContent.toString());
	}

	@Test
	public void testCargar() {
		Parametros parametros = new Parametros();
		parametros.put("Parametro 1", 1);
		Produccion produccion = new Produccion();
		String nombre = "Producción 1";
		produccion.guardar(nombre, parametros);
		Parametros parametros_test = produccion.cargar(nombre);
		String nombre_erroneo =  "ERROR";
		produccion.cargar(nombre_erroneo);
		assertEquals(parametros, parametros_test);
		assertEquals("Producción " + nombre + " guardada correctamente\n"+"Producción " + nombre + " cargada correctamente\n"+"Producción " + nombre_erroneo + " no encontrada\n", outContent.toString());
	}

}
