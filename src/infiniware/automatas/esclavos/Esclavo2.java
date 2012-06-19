package infiniware.automatas.esclavos;

import infiniware.automatas.GestorSubAutomatas;
import infiniware.automatas.sensores.Sensores;
import infiniware.automatas.subautomatas.Cinta;
import infiniware.automatas.subautomatas.Estacion;

public class Esclavo2 extends Esclavo {

    public static final Esclavo2 INSTANCIA = new Esclavo2();

    public Esclavo2() {
        super();
        this.entradas = new String[]{"H"};
        this.salida = "I";
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
