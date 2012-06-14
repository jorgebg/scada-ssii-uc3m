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

public class CokAnimation implements ActionListener, Animation {

	private static final int FRAMES_COK = 4;		 	//number of frames to load in the slides (COK)
	private static final String DIR_COK = "imgs/cintas/"; 	//direction of the slide images / (cok1-4.jpg)
	private static final int COK_MAX = 10200;			//Max size of the images
	
		
	private ImageIcon imgsCOK[]; //the images of the R1 movement (from CEN to EM)
	
	private Cinta cok;
	
	private JLabel statusLabel;		//Used for suspended animation until the beginning of the animation
	
	private int loopslot = -1;  //the current frame number
	
	Timer timer;        //the timer animating the images
	int speed;          //animation speed
	int pause;          //the length of the pause between revs
		
	private boolean stop;
	private boolean emergencyStop;
	
	public CokAnimation(double timeCOK){
		this.speed = ImgLoader.calculateSpeed(timeCOK, CokAnimation.FRAMES_COK);
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

		cok = new Cinta();
		cok.setVisible(true);
		cok.setOpaque(true);
		cok.setBounds(0, 0, with, height);
		parentPanel.add(cok);	
		
		cok.addMouseListener(new ALCOK(this));
		
		ImageIcon stopedRobot = new ImageIcon(CokAnimation.DIR_COK+"cok/cok1.jpg");
		statusLabel = new JLabel(stopedRobot,JLabel.CENTER);
		cok.add(statusLabel, BorderLayout.CENTER);
	}
	
	
	//Background task for loading images
	SwingWorker slideWorker = new SwingWorker<ImageIcon[], Void>(){
		@Override
		protected ImageIcon[] doInBackground() throws Exception {
			ImageIcon[] innerImgs = new ImageIcon[CokAnimation.FRAMES_COK];
			
			for (int i = 0; i< CokAnimation.FRAMES_COK; i++){
				innerImgs[i] = ImgLoader.loadImage(CokAnimation.DIR_COK, "cok/cok", i+1, CokAnimation.COK_MAX);
			}
			return innerImgs;
		}

		@Override
		public void done(){
			//remove the "Loading Images..." label
			cok.remove(statusLabel);
			loopslot = -1;
			try{
				imgsCOK = get();
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
	
			if(slideWorker.isDone() && (loopslot > -1) && (loopslot < CokAnimation.FRAMES_COK)){
				if(imgsCOK != null && imgsCOK[loopslot] != null){
					imgsCOK[loopslot].paintIcon(this, g, 0, 0);
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
			if (loopslot >= CokAnimation.FRAMES_COK)
				loopslot = 0; //restart the sequence
			
			cok.repaint();

			if (loopslot == CokAnimation.FRAMES_COK - 1)
				this.timer.restart(); //loops to infinity
			
		}else if(!emergencyStop){
			loopslot=0;
			cok.repaint();
		}
	}
	
	public void start() {
		if(this.timer==null){
			this.init();
		}
		
		if (slideWorker.isDone() && CokAnimation.FRAMES_COK > 1){
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
	
	public class ALCOK implements MouseListener{
		CokAnimation csim;
		int count = -1;
		
		public ALCOK(CokAnimation simu){
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
