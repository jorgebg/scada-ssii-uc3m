package infiniware.automatas.esclavos;

import infiniware.automatas.GestorSubAutomatas;
import infiniware.automatas.subautomatas.Cinta;
import infiniware.automatas.subautomatas.Estacion;
import infiniware.automatas.subautomatas.SubAutomata;
import java.util.HashMap;

public class Esclavo2 extends Esclavo {

    public static Esclavo2 INSTANCIA = new Esclavo2();
    GestorSubAutomatas subautomatas = new GestorSubAutomatas() {
        {
            put("ES", new Estacion(INSTANCIA, "H", "I"));
        }
    };

    @Override
    public byte getId() {
        return 2;
    }
}
