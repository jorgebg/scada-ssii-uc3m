package infiniware.automatas.esclavos;

import infiniware.automatas.GestorSubAutomatas;
import infiniware.automatas.subautomatas.CintaCapacidad;
import infiniware.automatas.subautomatas.Estacion;
import infiniware.automatas.subautomatas.Robot1;

public class Esclavo1 extends Esclavo {

    public static Esclavo1 INSTANCIA = new Esclavo1();
    GestorSubAutomatas subautomatas = new GestorSubAutomatas() {
        {
            put("CEJ", new CintaCapacidad(INSTANCIA, "A"));
            put("CEN", new CintaCapacidad(INSTANCIA, "B"));
            put("EM", new Estacion(INSTANCIA, "C D", "E"));
            put("R1", new Robot1(INSTANCIA));
        }
    };

    @Override
    public byte getId() {
        return 1;
    }
}
