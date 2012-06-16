package infiniware.almacenamiento;

import static org.junit.Assert.*;
import infiniware.Resultado;
import infiniware.scada.informes.Informes;
import infiniware.scada.informes.modelos.Fabricacion;
import infiniware.scada.informes.modelos.Funcionamiento;
import org.junit.Test;

public class ProduccionTest {

	Fabricacion fab = new Fabricacion(2, 9);
	Funcionamiento fun = new Funcionamiento(5, 9, 12);
	
	@Test
	public void testGuardar() {
		Informes inf = new Informes(fab, fun);
		Produccion produccion = new Produccion();
		Resultado res = produccion.guardar(inf);
		assertEquals(Resultado.CORRECTO, res);
	}

	@Test
	public void testCargar() {
		Informes inf = new Informes(fab, fun);
		Produccion produccion = new Produccion();
		Resultado res = produccion.guardar(inf);
		assertEquals(Resultado.CORRECTO, res);
		Informes inf_test = new Informes();
		res = produccion.cargar(inf_test);
		assertEquals(inf, inf_test);
		assertEquals(Resultado.CORRECTO, res);
	}

}
