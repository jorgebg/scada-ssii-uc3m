package infiniware.scada.ui.gui.view.animation;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Esta clase permite la animacion de la estacion de validacion
 * 
 * @author infiniware
 */
public class EvAnimation implements Animation {
	private final String DIR_FREE = "imgs/estaticas/evFree.jpg";
	private final String DIR_BUSY = "imgs/estaticas/evBusy.jpg";
	
	private final int FREE = 0;
	private final int BUSY = 1;
	
	private int state;
	
	private ImageIcon eFree;
	private ImageIcon eBusy;
	private JLabel statusLabel;

	/**
	 * Crea un objeto estacion de validacion
	 */
	public EvAnimation(){
		eFree = new ImageIcon(this.DIR_FREE);
		eBusy = new ImageIcon(this.DIR_BUSY);
		
		this.state = this.FREE;
		statusLabel = new JLabel(this.eFree);
		
		statusLabel.addMouseListener(new ALEV(this));
	}
	
	/**
	 * No realiza ninguna accion
	 */
	@Override
	public void init() {
		//Do Nothing
	}

	@Override
	public void createGUI(JPanel parentPanel, int with, int height) {
		parentPanel.setLayout(null);
		
		statusLabel.setBounds(0, 0, with, height);
		parentPanel.add(statusLabel);
	}

	@Override
	public void start(int state) {
		if(this.state != state)
			this.state=state;
		
		if(state == this.BUSY)
			statusLabel.setIcon(eBusy);
		else
			statusLabel.setIcon(eFree);
		
		this.statusLabel.repaint();
	}

	public void start(){
		this.start(this.state);
	}
	
	@Override
	public int getState() {
		return this.state;
	}

	@Override
	public void emergencyStop() {
		//Do Nothing
	}
	
	public class ALEV implements MouseListener{
		EvAnimation csim;
		int count = -1;
		
		public ALEV(EvAnimation simu){
			csim = simu;
		}
		@Override
		public void mouseClicked(MouseEvent arg0) {
			if(csim.state==csim.FREE){
				csim.start(1);
			}else{
				csim.start(0);
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}
	}

}
