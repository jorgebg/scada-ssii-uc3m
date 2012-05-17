package infiniware.almacenamiento;

import static org.junit.Assert.*;
import infiniware.scada.modelos.Parametros;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RegistroTest {

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
		Registro registro = new Registro();
		String nombre = "Registro 1";
		registro.guardar(nombre, parametros);
		assertEquals("Registro " + nombre + " guardado correctamente\n", outContent.toString());
	}

	@Test
	public void testCargar() {
		Parametros parametros = new Parametros();
		parametros.put("Parametro 1", 1);
		Registro registro = new Registro();
		String nombre = "Registro 1";
		registro.guardar(nombre, parametros);
		Parametros parametros_test = registro.cargar(nombre);
		String nombre_erroneo =  "ERROR";
		registro.cargar(nombre_erroneo);
		assertEquals(parametros, parametros_test);
		assertEquals("Registro " + nombre + " guardado correctamente\n"+"Registro " + nombre + " cargado correctamente\n"+"Registro " + nombre_erroneo + " no encontrado\n", outContent.toString());
	}

}
