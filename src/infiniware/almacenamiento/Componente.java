package infiniware.almacenamiento;

import java.io.*;

import infiniware.scada.modelos.ConjuntoGuardable;
import infiniware.scada.modelos.ConjuntoParametros;
import infiniware.Resultado;

public abstract class Componente {
	
	protected String carpeta = "";
	protected String fichero = "";


    public Resultado guardar(String nombre, ConjuntoGuardable parametros) throws IOException{
			File folder= new File(carpeta);
			folder.mkdirs();
			File fi = new File(folder, fichero);
			FileOutputStream fos = new FileOutputStream(fi);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(parametros);
			out.close(); 
			return Resultado.CORRECTO;
    }

    public Resultado cargar(String nombre, ConjuntoGuardable parametros) throws IOException{
    	File fi = new File(carpeta, fichero);
		FileInputStream fis = new FileInputStream(fi);
		ObjectInputStream in = new ObjectInputStream(fis);
		try{
			parametros = (ConjuntoParametros) in.readObject();
		}catch(ClassNotFoundException e){
			return Resultado.ERROR;
		}
		in.close();
		return Resultado.CORRECTO;
    }
}
