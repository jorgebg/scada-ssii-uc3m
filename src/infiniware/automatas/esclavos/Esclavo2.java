package infiniware.automatas.esclavos;

import infiniware.automatas.GestorSubAutomatas;
import infiniware.automatas.subautomatas.Estacion;

public class Esclavo2 extends Esclavo {

    public static Esclavo2 INSTANCIA = new Esclavo2();
    public Esclavo2() {
        super(new GestorSubAutomatas() {
            {
                put("ES", new Estacion(INSTANCIA, "H", "I"));
            }
        });
    }

    @Override
    public byte getId() {
        return 2;
    }
}
