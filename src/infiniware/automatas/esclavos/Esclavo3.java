package infiniware.automatas.esclavos;

import infiniware.automatas.GestorSubAutomatas;
import infiniware.automatas.subautomatas.Cinta;
import infiniware.automatas.subautomatas.EstacionOperario;

public class Esclavo3 extends Esclavo {

    public static Esclavo3 INSTANCIA = new Esclavo3();
    public Esclavo3() {
        super();
        this.subautomatas = new GestorSubAutomatas(this) {

            {
                instalar("COK", new Cinta("R2"));
                instalar("EV", new EstacionOperario("J", "K"));
            }
        };
    }

    @Override
    public byte getId() {
        return 3;
    }
}
