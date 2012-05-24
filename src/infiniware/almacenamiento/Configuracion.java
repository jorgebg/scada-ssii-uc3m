package infiniware.almacenamiento;

import java.io.IOException;

import infiniware.Resultado;
import infiniware.scada.modelos.ConjuntoParametros;

public class Configuracion extends Componente {

	private String carpeta = "almacenamiento";
	private String fichero = "configuracion.ser";
	
    public Resultado guardar(String nombre, ConjuntoParametros parametros){
        try{
        	super.carpeta = this.carpeta;
        	super.fichero = this.fichero;
        	super.guardar(nombre, parametros);
        	return Resultado.CORRECTO;
        }catch (IOException e){
        	return Resultado.ERROR;
        }
    }

    public Resultado cargar(String nombre, ConjuntoParametros parametros) {
    	try{
        	super.carpeta = this.carpeta;
        	super.fichero = this.fichero;
        	Resultado res = super.cargar(nombre, parametros);
			if(res == Resultado.ERROR)
				return Resultado.ERROR;
        }catch (IOException e){
        	return Resultado.ERROR;
        }
    	return Resultado.CORRECTO;
    }

}
