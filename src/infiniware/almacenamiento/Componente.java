package infiniware.almacenamiento;

import java.io.*;
import java.io.IOException;
import java.util.HashMap;

import infiniware.automatas.Automata;
import infiniware.scada.modelos.Parametros;

public abstract class Componente {
	
	protected String ruta;
	protected HashMap<String, Parametros> parametros;


    public void guardar(String nombre, Parametros parametros) {
    	try{
    		this.parametros.put(nombre, parametros);
			FileOutputStream fos = new FileOutputStream(ruta);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(this.parametros);
			out.close();
			System.out.println("Configuracion " + nombre + " guardada correctamente");
    		
    	}catch(IOException e){
    		System.out.println("Ha habido errores guardando el componente ");
    	}
        
    }

    public Parametros cargar(String nombre) {
        throw new UnsupportedOperationException("Not yet implemented");
 
    }
}
