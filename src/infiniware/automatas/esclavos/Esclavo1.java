package infiniware.automatas.esclavos;

import infiniware.automatas.GestorSubAutomatas;
import infiniware.automatas.subautomatas.CintaCapacidad;
import infiniware.automatas.subautomatas.Estacion;
import infiniware.automatas.subautomatas.Robot1;

public class Esclavo1 extends Esclavo {

    public static final Esclavo1 INSTANCIA = new Esclavo1();
    public Esclavo1() {
        super();
        this.subautomatas = new GestorSubAutomatas(this) {

            {
                instalar("CEJ", new CintaCapacidad("A"));
                instalar("CEN", new CintaCapacidad("B"));
                instalar("EM", new Estacion(
                        new String[]{"C","D"},
                        "E")
                );
                instalar("R1", new Robot1());
            }
        };
    }

    @Override
    public byte getId() {
        return 1;
    }
}
