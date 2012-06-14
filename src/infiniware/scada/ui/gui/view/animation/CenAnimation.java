package infiniware.scada.ui.gui.view.animation;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import infiniware.scada.ui.gui.view.ImgLoader;


public class CenAnimation implements ActionListener, Animation{

	private static final int FRAMES_CEN = 4;		 	//number of frames to load in the slides (CEN, CEJ)
	private static final String DIR_CEN = "imgs/cintas/"; 	//direction of the slide images / (cen-4.jpg)
	private static final int CEN_MAX = 10200;			//Max size of the images
	
	private ImageIcon imgsCEN[]; //the images of the R1 movement (from CEN to EM)
	
	private Cinta cen;
	
	private JLabel statusLabel;		//Used for suspended animation until the beginning of the animation
	
	private int loopslot = -1;  //the current frame number
	
	Timer timer;        //the timer animating the images
	int pause;          //the length of the pause between revs
	int speed;          //animation speed
		
	private boolean stop;
	private boolean emergencyStop;
	
	public CenAnimation(double timeCEN){
		this.speed = ImgLoader.calculateSpeed(timeCEN, CenAnimation.FRAMES_CEN);
		this.stop = false;
		this.emergencyStop = false;
	}
	
	public void init(){
		this.timer = new Timer(this.speed, this);
        this.timer.setInitialDelay(this.pause);
        this.timer.start(); 
        
        //Start loading the images in the background.
        this.slideWorker.execute();
	}
	
	public void createGUI(JPanel parentPanel, int with, int height){
		parentPanel.setLayout(null); //set layaout to absolute coordenates

		cen = new Cinta();
		cen.setVisible(true);
		cen.setOpaque(true);
		cen.setBounds(0, 0, with, height);
		parentPanel.add(cen);	
		
		cen.addMouseListener(new ALCEN(this));
		
		ImageIcon stopedRobot = new ImageIcon(CenAnimation.DIR_CEN+"cen/cen1.jpg");
		statusLabel = new JLabel(stopedRobot,JLabel.CENTER);
		cen.add(statusLabel, BorderLayout.CENTER);
	}
	
	
	//Background task for loading images
	SwingWorker slideWorker = new SwingWorker<ImageIcon[], Void>(){
		@Override
		protected ImageIcon[] doInBackground() throws Exception {
			ImageIcon[] innerImgs = new ImageIcon[CenAnimation.FRAMES_CEN];
			
			for (int i = 0; i< CenAnimation.FRAMES_CEN; i++){
				innerImgs[i] = ImgLoader.loadImage(CenAnimation.DIR_CEN, "cen/cen", i+1, CenAnimation.CEN_MAX);
			}
			return innerImgs;
		}

		@Override
		public void done(){
			//remove the "Loading Images..." label
			cen.remove(statusLabel);
			loopslot = -1;
			try{
				imgsCEN = get();
			}catch (InterruptedException ignore) {}
			catch (java.util.concurrent.ExecutionException e) {
				String why = null;
				Throwable cause = e.getCause();
				if (cause != null) {
					why = cause.getMessage();
				} else {
					why = e.getMessage();
				}
				System.err.println("Error retrieving file: " + why);
			}
		}
	};
	
	@SuppressWarnings("serial")
	public class Cinta extends JPanel{	
	
		public Cinta(){
			super(new BorderLayout());
		}
		
		protected void paintComponent(Graphics g){
			super.paintComponent(g);
	
			if(slideWorker.isDone() && (loopslot > -1) && (loopslot < CenAnimation.FRAMES_CEN)){
				if(imgsCEN != null && imgsCEN[loopslot] != null){
					imgsCEN[loopslot].paintIcon(this, g, 0, 0);
				}
			}
			
		}
	}

	/**
	 * Handle timer event. Update the looslost (frame number)
	 * If it's the last frame, stops until a new action is recieved
	 * 
	 * Handles also the stop and the emergency stop.
	 * If the emergencyStop is used, stop in the actual frame
	 * and continues from that poin the the start event arrives
	 */
	public void actionPerformed(ActionEvent e) {
		if (!slideWorker.isDone()) 
			return; //If still loading, can't animate.
		
		if(!this.stop && !this.emergencyStop){
			loopslot++;	//increases the frame
			if (loopslot >= CenAnimation.FRAMES_CEN)
				loopslot = 0; //restart the sequence
			
			cen.repaint();

			if (loopslot == CenAnimation.FRAMES_CEN - 1)
				this.timer.restart(); //loops to infinity
			
		}else if(!emergencyStop){
			loopslot=0;
			cen.repaint();
		}
	}
	
	public void start() {
		if(this.timer==null){
			this.init();
		}
		
		if (slideWorker.isDone() && CenAnimation.FRAMES_CEN > 1){
			//this.loopslot = 0;
			timer.restart();
			this.stop = false;
			this.emergencyStop = false;
		}
	}

	public void emergencyStop() {
		timer.stop();
		this.emergencyStop = true;
	}
	
	public void stop() {
		timer.stop();
		this.stop = true;
	}
	
	public class ALCEN implements MouseListener{
		CenAnimation csim;
		int count = -1;
		public ALCEN(CenAnimation simu){
			csim = simu;
		}
		@Override
		public void mouseClicked(MouseEvent arg0) {
			if(!csim.emergencyStop){
				csim.emergencyStop();
			}else{
				csim.start();
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
