/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.automatas.subautomatas;

import infiniware.automatas.sensores.Sensores;
import infiniware.scada.modelos.Parametros;

/**
 *
 * @author jorge
 */
public abstract class CintaCapacidad extends Cinta {

    Boolean[] contenido;

    class Movimiento extends Cinta.Movimiento {

        static final int MOVER = 0;
        static final int MANIPULAR = 1;

        public Movimiento() {
            acciones = 2;
        }

        @Override
        public long tiempo(int accion) {
            switch (accion) {
                case MOVER:
                    return super.tiempo(accion);
                case MANIPULAR:
                    return 2000; //TODO nuevo parametro?
            }
            return -1;
        }

        @Override
        public void postaccion(int accion) {
            switch (accion) {
                case MOVER:
                    desplazar();
                    Sensores sensores = new Sensores();
                    sensores.insertar(salida, contenido[contenido.length - 1]);
                    if (entrada != null) {
                        sensores.insertar(entrada, false);
                    }                        
                    automata.actualizar(sensores);
                    break;
                case MANIPULAR:
                    manipular();
                    break;
            }
        }

        private void desplazar() {
            for (int i = contenido.length - 1; i > 0; i--) {
                contenido[i] = contenido[i - 1];
            }
            contenido[0] = false;
        }
    }

    protected int contar() {
        int n = 0;
        for (int i = 0; i < contenido.length; i++) {
            if (contenido[i]) {
                n++;
            }
        }
        return n;
    }

    protected abstract void manipular();

    public CintaCapacidad(String salida, String entrada) {
        super(salida, entrada);
        configurar(new Parametros() {

            {
                put("velocidad", 10000);
                put("longitud", 50);
                put("capacidad", 5);
            }
        });
        //parametros = new Parametros("velocidad", "longitud", "capacidad");
    }
    public CintaCapacidad(String salida) {
        this(salida, null);
    }

    public void configurar(Parametros parametros) {
        super.configurar(parametros);
        if (parametros.containsKey("capacidad")) {
            contenido = new Boolean[parametros.get("capacidad")];
            for (int i = 0; i < contenido.length; i++) {
                contenido[i] = false;
            }
        }
    }

    @Override
    public String toString() {
        String s = super.toString() + ": |";
        for (int i = 0; i < contenido.length; i++) {
            s += contenido[i] ? salida : " ";
        }
        s += "|";
        return s;
    }
}