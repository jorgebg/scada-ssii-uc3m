package infiniware.scada.ui;

import infiniware.scada.informes.Informes;
import infiniware.scada.modelos.ConjuntoParametros;
import infiniware.scada.modelos.Parametros;

public interface IUi {
    
    
    /**
     * @UC 001
     */
    void iniciar();
    
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
    /**
     * 
     * @param parametros Mapa tal que IdAutomata => NombreSubAutomata => Parametros
     */
    void configurar(ConjuntoParametros parametros);


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
     * @UC 008
     */
    void parada();
    
    /**
     * @UC 007
     */
    void limpiarCPD();

    /**
     * @UC 010
     */
    void provocarFalloEsclavo1();
    /**
     * @UC 011
     */
    void provocarFalloEsclavo2();
    /**
     * @UC 012
     */
    void provocarFalloEsclavo3();

    /**
     * @UC 013
     */
    void recuperarFalloEsclavo1();
    /**
     * @UC 014
     */
    void recuperarFalloEsclavo2();
    /**
     * @UC 015
     */
    void recuperarFalloEsclavo3();

    /**
     * Hay una implementacion de ejemplo en la clase infiniware.scada.ui.cli.Cli
     * @param estados Estados de los automata: id {0..3} => estado {0..9}
     * Para obtener el nombre de los estados:
     * 
     *  for (int id = 0; id < estados.length; id++) {
     *      char estado = estados[id];
     *      Automata.INSTANCIAS.get(id).subautomatas.decodificarEstado(estados[id]);
     *  }
     */
}
