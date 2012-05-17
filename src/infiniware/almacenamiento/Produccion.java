package infiniware.almacenamiento;

import infiniware.scada.modelos.Parametros;

import java.io.IOException;

public class Produccion extends Componente {
	protected String carpeta = "almacenamiento";
	protected String fichero = "produccion.ser";

	public void guardar(String nombre, Parametros parametros){
        try{
        	super.carpeta = this.carpeta;
        	super.fichero = this.fichero;
        	super.guardar(nombre, parametros);
        	System.out.println("Producción " + nombre + " guardada correctamente");
        }catch (IOException e){
        	System.out.println("Ha habido errores guardando la producción");
        }
    }

    public Parametros cargar(String nombre) {
    	Parametros parametros = new Parametros();
    	try{
        	super.carpeta = this.carpeta;
        	super.fichero = this.fichero;
        	parametros = super.cargar(nombre);
        	if(parametros != null){
        		System.out.println("Producción " + nombre + " cargada correctamente");
        	}else{
        		System.out.println("Producción " + nombre + " no encontrada");
        	}
        }catch (IOException e){
        	System.out.println("Ha habido errores cargando la producción");
        }
		return parametros;
    }
}
