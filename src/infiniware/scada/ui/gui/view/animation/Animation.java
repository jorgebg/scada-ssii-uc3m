package infiniware.scada.ui.gui.view.animation;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import infiniware.scada.ui.gui.view.animation.Robot1Animation.ALR;
import infiniware.scada.ui.gui.view.animation.Robot1Animation.Robot1;

public interface Animation {
	
	/**
	 * Inicializacion del temporizador y carga de las imagenes 
	 * en memoria.
	 * Solo debe ser llamado una vez, puesto que consume muchos recursos
	 */
	public void init();
	
	/**
	 * Crea los componentes graficos y los anyade al panel definido por
	 * parametro para que se muestren con tamanyo determinado
	 * @param parentPanel - panel que aloja los componentes graficos
	 * @param with - ancho del componente
	 * @param height - alto del componente
	 */
	public void createGUI(JPanel parentPanel, int with, int height);

	/**
	 * Arranca un componente de animacion en un estado determinado
	 * @param state - estado en el que se iniciara el componente
	 */
	public void start(int state); 
        
	/**
	 * Devuelve el estado actual del componente
	 * @return estado del componetne (int)
	 */
    public int getState();
	
    /**
     * Realiza una parada de emergencia en el componente
     */
	public void emergencyStop();

}
