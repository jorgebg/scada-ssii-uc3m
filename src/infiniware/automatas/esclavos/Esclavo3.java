package infiniware.automatas.esclavos;

import infiniware.automatas.GestorSubAutomatas;
import infiniware.automatas.subautomatas.Cinta;
import infiniware.automatas.subautomatas.EstacionOperario;

public class Esclavo3 extends Esclavo {

    public static Esclavo3 INSTANCIA = new Esclavo3();
    GestorSubAutomatas subautomatas = new GestorSubAutomatas() {

        {
            put("COK", new Cinta(INSTANCIA, "R2"));
            put("ES", new EstacionOperario(INSTANCIA, "J", "K"));
        }
    };

    @Override
    public byte getId() {
        return 3;
    }
}
