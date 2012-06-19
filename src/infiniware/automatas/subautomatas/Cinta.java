/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.automatas.subautomatas;

import infiniware.automatas.Automata;
import infiniware.automatas.esclavos.Esclavo;
import infiniware.automatas.esclavos.IMaestro;
import infiniware.automatas.sensores.Sensores;
import infiniware.scada.modelos.Parametros;
import infiniware.scada.simulador.Simulacion;
import java.rmi.RemoteException;

/**
 *
 * @author jorge
 */
public class Cinta extends SubAutomata {

    final String salida;
    final String entrada;

    class Movimiento extends Simulacion {

        @Override
        public long tiempo(int accion) {
            return parametros.get("velocidad") / parametros.get("longitud");
        }

        @Override
        public void postaccion(int accion) {
            automata.actualizar(salida, true);
            if (entrada != null) {
                automata.actualizar(entrada, false);
            }
            automata.log(Cinta.this.nombre + " ha transportado de " + entrada + " a " + salida);

            //simulacion cinta
            simularCinta(simularContenido());
        }
    };

    class Reposo extends Simulacion {

        @Override
        public void postaccion(int accion) {
            simularCinta(simularContenido());
        }
    }

    protected void simularCinta(boolean[] contenido) {

        try {
            if (automata instanceof Esclavo) {
                ((Esclavo) automata).maestro.simularCinta(nombre, contenido);
            } else {
                ((IMaestro) automata).simularCinta(nombre, contenido);
            }
        } catch (RemoteException ex) {
            System.err.println("Error al comunicar la simulacion de la cinta " + nombre);
        }
    }

    public Cinta(String salida, String entrada) {
        super();
        this.salida = salida;
        this.entrada = entrada;
        //parametros = new Parametros("velocidad", "longitud");
        configurar(new Parametros() {

            {
                put("velocidad", 5000);
                put("longitud", 10);
            }
        });
    }

    public Cinta(String salida) {
        this(salida, null);
    }

    private boolean[] simularContenido() {

        boolean[] contenido = new boolean[entrada != null ? 2 : 1];
        int posicion = 0;
        if (entrada != null) {
            contenido[posicion++] = automata.sensores.get(entrada);
        }
        contenido[posicion++] = automata.sensores.get(salida);
        return contenido;
    }
    
    
    public void ponerConjuntoMontado() {
        //System.out.println("SIMULANDOOOOOOOOOOOOO");
        boolean[] contenido = new boolean[entrada != null ? 2 : 1];
        int posicion = 0;
        if (entrada != null) {
            contenido[posicion++] = true;
        }
        contenido[posicion++] = automata.sensores.get(salida);
    
        simularCinta(simularContenido());
    }
}
