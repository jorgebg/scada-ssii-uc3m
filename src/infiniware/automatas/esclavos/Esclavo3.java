package infiniware.automatas.esclavos;

import infiniware.automatas.GestorSubAutomatas;
import infiniware.automatas.subautomatas.Cinta;
import infiniware.automatas.subautomatas.EstacionValidacion;
import infiniware.automatas.subautomatas.Robot2;

public class Esclavo3 extends Esclavo {

    public static final Esclavo3 INSTANCIA = new Esclavo3();
    public Esclavo3() {
        super();
        sensores.insertar("OK");
        this.subautomatas = new GestorSubAutomatas(this) {

            {
                instalar("COK", new Cinta("R2"));
                instalar("EV", new EstacionValidacion("J", "K"));
            }
        };
    }

    @Override
    public byte getId() {
        return 3;
    }

    @Override
    public void simular(Simulaciones simulacion) {
        switch(simulacion) {
            case LimpiarCPD:
                this.subautomatas.<Robot2>buscar("R2").cpd.limpiar();
                break;
        }
    }
    
    
    
}
