package infiniware.informes;

import static org.junit.Assert.*;
import infiniware.scada.informes.modelos.Funcionamiento;

import org.junit.Test;

public class FuncionamientoTest {

	int emergencia = 5;
	int arranques = 12;
	int normales = 40;
	String emer ="emergencia";
	String norm = "normales";
	String arranq = "arranques";
	
	@Test
	public void testFuncionamiento() {
		Funcionamiento fun = new Funcionamiento();
		Funcionamiento fun2 = new Funcionamiento(0, 0, 0);
		assertEquals(fun2, fun);
	}
	
	@Test
	public void testFuncionamientoFuncionamiento() {
		Funcionamiento fun = new Funcionamiento();
		Funcionamiento fun2 = new Funcionamiento(fun);
		assertEquals(fun2, fun);
	}
	
	@Test
	public void testFuncionamientoIntIntInt() {
		Funcionamiento fun = new Funcionamiento();
		Funcionamiento fun2 = new Funcionamiento(0, 0, 0);
		assertEquals(fun2, fun);
	}
	
	@Test
	public void testExtraerStringArray() {
		Funcionamiento fun = new Funcionamiento(normales, emergencia, arranques);
		Funcionamiento fun_test = new Funcionamiento();
		String[] array = {"error", norm};
		Funcionamiento fun_arr = (Funcionamiento) fun.extraer(array);
		fun_test.put("error", fun.get("error"));
		fun_test.put(norm, fun.get(norm));
		assertEquals(fun_test, fun_arr);
	}

	@Test
	public void testAddNormales() {
		Funcionamiento fun = new Funcionamiento(normales, emergencia, arranques);
		normales = fun.getNormales();
		normales ++;
		fun.addNormales();
		assertEquals(normales, fun.getNormales());
	}

	@Test
	public void testAddEmergencia() {
		Funcionamiento fun = new Funcionamiento(normales, emergencia, arranques);
		emergencia = fun.getEmergencia();
		emergencia ++;
		fun.addEmergencia();
		assertEquals(emergencia, fun.getEmergencia());
	}

	@Test
	public void testAddArranques() {
		Funcionamiento fun = new Funcionamiento(normales, emergencia, arranques);
		arranques = fun.getArranques();
		arranques ++;
		fun.addArranques();
		assertEquals(arranques, fun.getArranques());
	}

	@Test
	public void testGetNormales() {
		Funcionamiento fun = new Funcionamiento(normales, emergencia, arranques);
		assertEquals(normales, fun.getNormales());
	}

	@Test
	public void testGetArranques() {
		Funcionamiento fun = new Funcionamiento(normales, emergencia, arranques);
		assertEquals(arranques, fun.getArranques());
	}

	@Test
	public void testGetEmergencia() {
		Funcionamiento fun = new Funcionamiento(normales, emergencia, arranques);
		assertEquals(emergencia, fun.getEmergencia());
	}

}
