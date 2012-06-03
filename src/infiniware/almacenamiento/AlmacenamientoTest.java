package infiniware.almacenamiento;

import java.io.IOException;
import java.util.HashMap;

import infiniware.Resultado;
import infiniware.scada.modelos.ConjuntoGuardable;
import infiniware.scada.modelos.ConjuntoParametros;
import infiniware.scada.modelos.Guardable;
import infiniware.scada.modelos.Parametros;

public class AlmacenamientoTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		ConjuntoParametros parametros = new ConjuntoParametros();
		Parametros para = new Parametros();
		para.put("Cinta 3", 4);
		HashMap<String, Guardable> map = new HashMap<String, Guardable>();
		map.put("Automata 1", para);
		parametros.put(1, map);
		Configuracion conf = new Configuracion();
		System.out.println(parametros.toString());
		String nombre = "Test1";
		Resultado res = conf.guardar(nombre, parametros);
		System.out.println(res);
		ConjuntoParametros paramet = new ConjuntoParametros();
		conf.cargar(nombre, paramet);
		System.out.println(paramet.toString());
		HashMap ma = paramet.get(1);
		Parametros par = (Parametros) ma.get("Automata 1");
		System.out.println(par.get("Cinta 3"));
	}

}
