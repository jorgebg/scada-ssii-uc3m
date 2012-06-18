package infiniware.automatas.esclavos;

import infiniware.automatas.GestorSubAutomatas;
import infiniware.automatas.subautomatas.Cinta;
import infiniware.automatas.subautomatas.CintaSalida;
import infiniware.automatas.subautomatas.EstacionValidacion;
import infiniware.automatas.subautomatas.Robot2;

public class Esclavo3 extends Esclavo {

    public static final Esclavo3 INSTANCIA = new Esclavo3();

    public Esclavo3() {
        super();
        this.entradas = new String[]{"J", "L"};
        this.salida = "K";
        sensores.insertar("OK");
        sensores.insertar("L");
        this.subautomatas = new GestorSubAutomatas(this) {

            {
                instalar("COK", new CintaSalida("L", "N"));
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
        switch (simulacion) {
            case LimpiarCPD:
                this.subautomatas.<Robot2>buscar("R2").cpd.limpiar();
                break;
        }
    }

    @Override
    public char ejecutar(char sensores) {

        if (sensores != Character.MAX_VALUE) {
            if (this.sensores.get("L", sensores)) {
                ((CintaSalida) this.subautomatas.get("COK")).ponerConjuntoValido();
            }
        }

        return super.ejecutar(sensores);


    }
}
