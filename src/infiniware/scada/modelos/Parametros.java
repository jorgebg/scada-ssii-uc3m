package infiniware.scada.modelos;


public class Parametros extends Guardable {

	public Parametros(String... parametros) {
        mezclar(parametros);
    }
    
    public Guardable extraer(String[] parametros) {
        Parametros nuevos = new Parametros();
        for(String clave : parametros)
            nuevos.put(clave, get(clave));
        return nuevos;
    }

    public Parametros mezclar(String... parametros) {
        for(String clave : parametros)
            put(clave, null);
        return this;
    }

    @Override
    public Integer get(Object key) {
        Integer result = super.get(key);
        if(result == null)
            System.err.println("Error al obtener el parametro \"" + key + "\": No se han inicializado.");
        return result;
    }
    
    
}
