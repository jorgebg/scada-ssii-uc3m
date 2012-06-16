package infiniware.informes;

import static org.junit.Assert.*;
import infiniware.Resultado;
import infiniware.scada.informes.Informes;
import infiniware.scada.informes.modelos.Fabricacion;
import infiniware.scada.informes.modelos.Funcionamiento;

import org.junit.Test;

public class InformesTest {
	
	Fabricacion fab = new Fabricacion(2, 9);
	Funcionamiento fun = new Funcionamiento(5, 9, 12);

	@Test
	public void testSetFabricacion() {
		Informes inf = new Informes(fab, null);
		Informes inf_test = new Informes();
		Resultado res = inf_test.setFabricacion(fab);
		assertEquals(inf_test, inf);
		assertEquals(Resultado.CORRECTO, res);
	}

	@Test
	public void testSetFuncionamiento() {
		Informes inf = new Informes(null, fun);
		Informes inf_test = new Informes();
		Resultado res = inf_test.setFuncionamiento(fun);
		assertEquals(Resultado.CORRECTO, res);
		assertEquals(inf_test, inf);
	}
	
	@Test
	public void testInformesFabricacionFuncionamiento() {
		Informes inf = new Informes(fab, fun);
		Informes inf_test = new Informes();
		inf_test.setFabricacion(fab);
		inf_test.setFuncionamiento(fun);
		assertEquals(inf_test, inf);
	}

	@Test
	public void testInformes() {
		Informes inf = new Informes(null, null);
		Informes inf_test = new Informes();
		assertEquals(inf_test, inf);
	}

	@Test
	public void testGetFabricacion() {
		Informes inf = new Informes(fab, fun);
		Fabricacion fab_test = inf.getFabricacion();
		assertEquals(fab, fab_test);
	}

	@Test
	public void testGetFuncionamiento() {
		Informes inf = new Informes(fab, fun);
		Funcionamiento fun_test = inf.getFuncionamiento();
		assertEquals(fun, fun_test);
	}

}
