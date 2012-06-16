package infiniware.informes;

import static org.junit.Assert.*;
import infiniware.scada.informes.modelos.Fabricacion;

import org.junit.Test;

public class FabricacionTest {

	int correctos = 5;
	int incorrectos = 12;
	String corr ="correctos";
	String inco = "incorrectos";
	
	@Test
	public void testExtraerStringArray() {
		Fabricacion fab = new Fabricacion(correctos, incorrectos);
		Fabricacion fab_test = new Fabricacion();
		String[] array = {"error", corr};
		Fabricacion fab_arr = (Fabricacion) fab.extraer(array);
		fab_test.put("error", fab.get("error"));
		fab_test.put(corr, fab.get(corr));
		assertEquals(fab_test, fab_arr);
	}

	@Test
	public void testFabricacion() {
		Fabricacion fab = new Fabricacion();
		Fabricacion fab_test = new Fabricacion(0, 0);
		assertEquals(fab_test, fab);
	}

	@Test
	public void testFabricacionIntInt() {
		Fabricacion fab = new Fabricacion();
		Fabricacion fab_test = new Fabricacion(0, 0);
		assertEquals(fab_test, fab);
	}

	@Test
	public void testFabricacionFabricacion() {
		Fabricacion fab = new Fabricacion(correctos, incorrectos);
		Fabricacion fab_test = new Fabricacion(fab);
		assertEquals(fab_test, fab);
	}

	@Test
	public void testAddCorrecto() {
		Fabricacion fab = new Fabricacion(correctos, incorrectos);
		correctos ++;
		fab.addCorrecto();
		assertEquals(correctos, fab.getCorrectos());
	}

	@Test
	public void testAddIncorrecto() {
		Fabricacion fab = new Fabricacion(correctos, incorrectos);
		incorrectos ++;
		fab.addIncorrecto();
		assertEquals(incorrectos, fab.getIncorrectos());
	}

	@Test
	public void testGetCorrectos() {
		Fabricacion fab = new Fabricacion(correctos, incorrectos);
		assertEquals(correctos, fab.getCorrectos());
	}

	@Test
	public void testGetIncorrectos() {
		Fabricacion fab = new Fabricacion(correctos, incorrectos);
		assertEquals(incorrectos, fab.getIncorrectos());
	}

}
