package infiniware.almacenamiento;

import java.io.*;

import infiniware.scada.modelos.ConjuntoGuardable;
import infiniware.scada.modelos.ConjuntoParametros;
import infiniware.Resultado;

/**
 * 
 * @author Infiniware
 * 
 * Clase abstracta que encapsula los 2 componentes de almacenaje, Configuracion y Produccion
 *
 */
public abstract class Componente {
	
	protected String carpeta = "";
	protected String fichero = "";

}
