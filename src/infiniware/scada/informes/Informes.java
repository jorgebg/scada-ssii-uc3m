
package infiniware.scada.informes;

import infiniware.scada.Scada;
import infiniware.scada.informes.modelos.Acciones;
import infiniware.scada.informes.modelos.Produccion;
import infiniware.scada.informes.modelos.Incorrectos;

public class Informes {

    public Produccion produccion;
    public Acciones acciones;
    public Incorrectos incorrectos;
    
    public static Informes generar(Scada scada) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
}
