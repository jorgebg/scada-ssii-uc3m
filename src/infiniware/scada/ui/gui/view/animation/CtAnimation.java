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

public class CtAnimation implements ActionListener, Animation {

	private static final int FRAMES_CT = 4;		 	//number of frames to load in the slides (CT)
	private static final String DIR_CT = "imgs/cintas/"; 	//direction of the slide images / (ct1-4.jpg)
	private static final int CT_MAX = 24996;			//Max size of the images
	public static final int STOP = 0;
	public static final int MOVE = 1;
	private static final int PAUSE_TIME = 0;			//Lag time for image loading
	
	private ImageIcon imgsCT[]; //the images of the R1 movement (from CEN to EM)
	private ImageIcon imgsREP;
	
	private Cinta ct;
	private int state;
	
	private JLabel statusLabel;		//Used for suspended animation until the beginning of the animation
	private int loopslot = -1;  //the current frame number
	
	Timer timer;        //the timer animating the images
	int speed;          //animation speed
	int pause;          //the length of the pause between revs
		
	private boolean stop;
	private boolean emergencyStop;
	
	public CtAnimation(double timeCT){
		this.speed = ImgLoader.calculateSpeed(timeCT, CtAnimation.FRAMES_CT);
		this.pause = CtAnimation.PAUSE_TIME;		
		
		this.state=CtAnimation.STOP;
		this.stop = false;
		this.emergencyStop = false;
		
		this.imgsREP = new ImageIcon(CtAnimation.DIR_CT+"ct/ct1.jpg");
		this.statusLabel = new JLabel(this.imgsREP);
		this.ct = new Cinta();
	}
	
	public void init(){
		this.timer = new Timer(this.speed, this);
        this.timer.setInitialDelay(this.pause);
        //this.timer.start(); 
        
        //Start loading the images in the background.
        this.slideWorker.execute();
	}
	
	public void createGUI(JPanel parentPanel, int with, int height){
		parentPanel.setLayout(null); //set layaout to absolute coordenates

		ct.setVisible(true);
		ct.setOpaque(true);
		ct.setBounds(0, 0, with, height);
		parentPanel.add(ct);	
		
		ct.addMouseListener(new ALCT(this));
		this.ct.add(statusLabel);
	}
	
	
	//Background task for loading images
	SwingWorker slideWorker = new SwingWorker<ImageIcon[], Void>(){
		@Override
		protected ImageIcon[] doInBackground() throws Exception {
			ImageIcon[] innerImgs = new ImageIcon[CtAnimation.FRAMES_CT];
			
			for (int i = 0; i< CtAnimation.FRAMES_CT; i++){
				innerImgs[i] = ImgLoader.loadImage(CtAnimation.DIR_CT, "ct/ct", i+1, CtAnimation.CT_MAX);
			}
			return innerImgs;
		}

		@Override
		public void done(){
			//remove the "Loading Images..." label
			ct.remove(statusLabel);
			loopslot = -1;
			try{
				imgsCT = get();
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
	
			if(slideWorker.isDone() && (loopslot > -1) && (loopslot < CtAnimation.FRAMES_CT)){
				if(imgsCT != null && imgsCT[loopslot] != null){
					imgsCT[loopslot].paintIcon(this, g, 0, 0);
				}
			}else{
				imgsREP.paintIcon(this, g, 0, 0);
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
			if (loopslot >= CtAnimation.FRAMES_CT)
				loopslot = 0; //restart the sequence
			
			ct.repaint();

			if (loopslot == CtAnimation.FRAMES_CT - 1)
				this.timer.restart(); //loops to infinity
			
		}else if(!emergencyStop){
			loopslot=0;
			ct.repaint();
		}
	}
	
	private void start() {
		if (slideWorker.isDone() && CtAnimation.FRAMES_CT > 1){
			timer.restart();
			this.stop = false;
			this.emergencyStop = false;
		}
	}

	@Override
	public void emergencyStop() {
		timer.stop();
		this.emergencyStop = true;
	}
	
	private void stop() {
		timer.stop();
		this.stop = true;
	}
	
	public class ALCT implements MouseListener{
		CtAnimation csim;
		int count = -1;
		
		public ALCT(CtAnimation simu){
			csim = simu;
		}
		@Override
		public void mouseClicked(MouseEvent arg0) {
			if(csim.state==csim.STOP){
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

	@Override
	public void start(int state) {
		if(this.timer==null){
			this.init();
		}
		
		if(this.state != state)
			this.state=state;
		
		if(state == CtAnimation.STOP)
			this.stop();
		else
			this.start();
	}

	@Override
	public int getState() {
		return this.state;
	}

}
