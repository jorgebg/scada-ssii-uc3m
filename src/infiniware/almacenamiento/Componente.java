package infiniware.almacenamiento;

import java.io.*;
import java.io.IOException;
import java.util.HashMap;

import infiniware.automatas.Automata;
import infiniware.scada.modelos.Parametros;

public abstract class Componente {
	
	protected String carpeta = "";
	protected String fichero = "";
	protected HashMap<String, Parametros> parametros = new HashMap<String, Parametros>();


    public void guardar(String nombre, Parametros parametros) throws IOException{
			this.parametros.put(nombre, parametros);
			File folder= new File(carpeta);
			folder.mkdirs();
			File fi = new File(carpeta, fichero);
			FileOutputStream fos = new FileOutputStream(fi);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(this.parametros);
			out.close();   
    }

    public Parametros cargar(String nombre) {
        throw new UnsupportedOperationException("Not yet implemented");
 
    }
}
