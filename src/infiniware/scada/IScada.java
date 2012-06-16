package infiniware.scada;

import infiniware.scada.informes.Informes;
import infiniware.scada.modelos.ConjuntoParametros;
import infiniware.Resultado;

public interface IScada {

    /**
     * @UC 002
     */
    void arrancar();

    /**
     * @UC 005
     */
    Resultado cargarConfiguracion(String nombre, ConjuntoParametros parametros);

    /**
     * @UC 003
     */
    void configurar(ConjuntoParametros parametros);

    void detenerProceso();

    /**
     * @UC 009
     */
    void emergencia();

    /**
     * @UC 006
     */
    Informes generarInforme();

    /**
     * @UC 004
     */
    Resultado guardarConfiguracion(String nombre, ConjuntoParametros parametros);

    /**
     * @UC 001
     */
    void iniciar();


    /**
     * @UC 008
     */
    void parada();
    
}
