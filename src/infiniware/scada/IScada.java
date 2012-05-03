package infiniware.scada;

import infiniware.scada.informes.Informes;
import infiniware.scada.modelos.Parametros;

public interface IScada {

    /**
     * @UC 002
     */
    void arrancar();

    /**
     * @UC 005
     */
    Parametros cargarConfiguracion(String nombre);

    /**
     * @UC 003
     */
    void configurar(Parametros parametros);

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
    void guardarConfiguracion(String nombre, Parametros parametros);

    /**
     * @UC 001
     */
    void iniciar();


    /**
     * @UC 008
     */
    void parada();
    
}
