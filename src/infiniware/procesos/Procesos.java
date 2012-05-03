/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infiniware.procesos;

import infiniware.automatas.esclavos.Esclavo1;
import infiniware.automatas.esclavos.Esclavo2;
import infiniware.automatas.esclavos.Esclavo3;
import infiniware.automatas.maestro.Maestro;
import infiniware.scada.Scada;
import infiniware.scada.ui.consola.Consola;

/**
 * Cuatro procesos: base (Scada & Maestro), esclavo1, esclavo2, esclavo3
 *
 * @author jorge
 */
public enum Procesos implements IProceso {

    base {

        public Thread iniciarProceso() {
            Thread thread = Maestro.INSTANCIA.iniciarProceso();
            Scada.INSTANCIA.iniciarProceso();
            ui.iniciarProceso();
            return thread;
        }

        public void detenerProceso() {
            Scada.INSTANCIA.detenerProceso();
            Maestro.INSTANCIA.detenerProceso();
        }
    },
    esclavo1 {

        public Thread iniciarProceso() {
            return Esclavo1.INSTANCIA.iniciarProceso();
        }

        public void detenerProceso() {
            Esclavo1.INSTANCIA.detenerProceso();
        }
    },
    esclavo2 {

        public Thread iniciarProceso() {
            return Esclavo2.INSTANCIA.iniciarProceso();
        }

        public void detenerProceso() {
            Esclavo2.INSTANCIA.detenerProceso();
        }
    },
    esclavo3 {

        public Thread iniciarProceso() {
            return Esclavo3.INSTANCIA.iniciarProceso();
        }

        public void detenerProceso() {
            Esclavo3.INSTANCIA.detenerProceso();
        }
    };
    private static Procesos proceso;
    private static Thread thread;
    private static IProcesable ui = Consola.INSTANCIA;

    public static void iniciar(String proceso) {
        iniciar(Procesos.valueOf(proceso));
    }

    public static void iniciar(Procesos proceso) {
        if (Procesos.proceso == null) {
            Procesos.proceso = proceso;
        } else {
            System.err.println("No se puede iniciar el proceso \"" + proceso + "\", ya hay un proceso en ejecucion: \"" + Procesos.proceso + "\"");
            System.exit(-1);
        }
        //Aseguramos de apagar al morir el proceso
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                detener();
            }
        });

        //Iniciamos la proceso
        System.out.println("Iniciando proceso \"" + Procesos.proceso + "\".");
        thread = Procesos.proceso.iniciarProceso();
        try {
            thread.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace(System.err);
        }
        System.out.println(thread.getState());
        detener(thread);
    }

    public static void detener() {
        detener(thread);
    }

    public static void detener(final Thread thread) {
        System.out.println("Deteniendo proceso \"" + Procesos.proceso + "\".");
        Procesos.proceso.detenerProceso();
        if (thread != null) {
            synchronized (thread) {
                thread.notify();
            }
        }
    }
}
