package infiniware.scada.ui.gui.view.animation;

public interface SlideAnimation {

	/**
	 * Actualiza los elementos moviles de las cintas 
	 * (ejes, engranajes, conjutos montados y conjuntos validos)
	 * permite actualizar la posicion de los elementos por medio 
	 * de un array de boolean que se recibe por parametro
	 * el tamanyo minimo del array debe ser 6
	 * @param elements
	 */
	public void updateElements(boolean[] elements);
}
