package infiniware.automatas.maestro;

import infiniware.automatas.Automata;
import infiniware.automatas.esclavos.*;
import infiniware.automatas.sensores.Sensores;
import infiniware.scada.Scada;
import infiniware.scada.modelos.Parametros;
import java.util.Map;

public class Maestro extends Automata implements infiniware.scada.IMaestro, infiniware.automatas.esclavos.IMaestro {

    GestorSensores sensores;
    GestorEsclavos esclavos;
    Scada scada;
    public static Maestro INSTANCIA = new Maestro();


    /*
     * infiniware.automatas.esclavos.IMaestro {{{
     */
    public void notificar(byte esclavo, char sensores) {
        this.sensores.actualizar(esclavo, sensores);
    }
    /*
     * }}}
     */

    /*
     * infiniware.scada.IMaestro {{{
     */
    public void ciclo(Sensores sensores) {
        this.sensores.actualizar(sensores);
        super.ciclo(sensores);
        for (int id = 1; id < esclavos.size(); id++) {
            esclavos.ciclo(id, this.sensores.get(id));
        }
    }

    public void inicializar() {
    }


    public void emergencia() {
    }

    public void arrancar() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void parada() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void desconectar() {
        esclavos = null;
        super.desconectar();
    }

    public void conectar() {
        esclavos = new GestorEsclavos();
        for (Esclavo esclavo : new Esclavo[]{
                    Esclavo1.INSTANCIA,
                    Esclavo2.INSTANCIA,
                    Esclavo3.INSTANCIA
                }) {
            IEsclavo iesclavo = this.<IEsclavo>conectar(esclavo);
            esclavos.put(esclavo.getId(), iesclavo);
        }
        System.out.println("Conexion con los esclavos establecida.");
    }

    public void enlazar() {
        super.<IMaestro>enlazar();
    }
    /*
     * }}}
     */

    @Override
    public byte getId() {
        return 0;
    }
}
