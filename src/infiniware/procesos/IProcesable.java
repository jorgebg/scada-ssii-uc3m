package infiniware.procesos;
/**
 * Hay cuatro procesos: SCADA & Maestro (implementado en el SCADA), Esclavo1, Esclavo2 y Esclavo3
 * @NFSR 008
 * @NFSR 009
 * @NFSR 010
 * @NFSR 011
 */
public interface IProcesable {
    
    public Thread iniciarProceso();

    public void detenerProceso();
}
