package infiniware.almacenamiento;

import java.io.*;
import java.util.HashMap;
import infiniware.scada.modelos.Parametros;

public abstract class Componente {
	
	protected String carpeta = "";
	protected String fichero = "";
	protected HashMap<String, Parametros> parametros = new HashMap<String, Parametros>();


    public void guardar(String nombre, Parametros parametros) throws IOException{
			this.parametros.put(nombre, parametros);
			File folder= new File(carpeta);
			folder.mkdirs();
			File fi = new File(folder, fichero);
			FileOutputStream fos = new FileOutputStream(fi);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(this.parametros);
			out.close();   
    }

    @SuppressWarnings("unchecked")
	public Parametros cargar(String nombre) throws IOException{
    	Parametros par = new Parametros();
    	File fi = new File(carpeta, fichero);
		FileInputStream fis = new FileInputStream(fi);
		ObjectInputStream in = new ObjectInputStream(fis);
		try{
			parametros = (HashMap<String, Parametros>) in.readObject();
		}catch(ClassNotFoundException e){
			System.out.println("Clase no encontrada");
		}
		in.close();
		par = parametros.get(nombre);
		return par;
    }
}
