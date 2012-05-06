package infiniware.automatas.esclavos;

import infiniware.automatas.GestorSubAutomatas;
import infiniware.automatas.subautomatas.Estacion;

public class Esclavo2 extends Esclavo {

    public static Esclavo2 INSTANCIA = new Esclavo2();
    public Esclavo2() {
        super();
        this.subautomatas = new GestorSubAutomatas(this) {
            {
                instalar("ES", new Estacion("H", "I"));
            }
        };
    }

    @Override
    public byte getId() {
        return 2;
    }
}
