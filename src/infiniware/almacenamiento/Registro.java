package infiniware.almacenamiento;

import infiniware.scada.modelos.Parametros;

import java.io.IOException;

public class Registro extends Componente {
	
	protected String carpeta = "almacenamiento";
	protected String fichero = "registro.ser";

	public void guardar(String nombre, Parametros parametros){
        try{
        	super.carpeta = this.carpeta;
        	super.fichero = this.fichero;
        	super.guardar(nombre, parametros);
        	System.out.println("Registro " + nombre + " guardado correctamente");
        }catch (IOException e){
        	System.out.println("Ha habido errores guardando el registro");
        }
    }

    public Parametros cargar(String nombre) {
    	Parametros parametros = new Parametros();
    	try{
        	super.carpeta = this.carpeta;
        	super.fichero = this.fichero;
        	parametros = super.cargar(nombre);
        	if(parametros != null){
        		System.out.println("Registro " + nombre + " cargado correctamente");
        	}else{
        		System.out.println("Registro " + nombre + " no encontrado");
        	}
        }catch (IOException e){
        	System.out.println("Ha habido errores cargando el registro");
        }
		return parametros;
    }
}
