package infiniware.almacenamiento;

import java.io.IOException;

import infiniware.automatas.Automata;
import infiniware.scada.modelos.Parametros;

public class Configuracion extends Componente {

	protected String carpeta = "almacenamiento";
	protected String fichero = "configuracion.ser";
	
    public static void configurar(Automata automata) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void guardar(String nombre, Parametros parametros){
        try{
        	super.carpeta = this.carpeta;
        	super.fichero = this.fichero;
        	super.guardar(nombre, parametros);
        	System.out.println("Configuración " + nombre + " guardada correctamente");
        }catch (IOException e){
        	System.out.println("Ha habido errores guardando la configuración");
        }
    }

    public Parametros cargar(String nombre) {
    	Parametros parametros = new Parametros();
    	try{
        	super.carpeta = this.carpeta;
        	super.fichero = this.fichero;
        	parametros = super.cargar(nombre);
        	if(parametros != null){
        		System.out.println("Configuración " + nombre + " cargada correctamente");
        	}else{
        		System.out.println("Configuración " + nombre + " no encontrada");
        	}
        }catch (IOException e){
        	System.out.println("Ha habido errores cargando la configuración");
        }
		return parametros;
    }

}
