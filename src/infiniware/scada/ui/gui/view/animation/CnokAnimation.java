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

public class CnokAnimation implements ActionListener, Animation {

	private static final int FRAMES_CNOK = 8;		 		//number of frames to load in the slides CNOK
	private static final String DIR_CNOK = "imgs/cintas/"; 	//direction of the slide images / (cnok1-9.jpg)
	private static final int CNOK_MAX = 20632;				//Max size of the images
	private final int GravitySpeed = 200;			
	
	public static final int EMTPY=0;
	public static final int FULL=1;
	public static final int MOVE=2;
	
	private ImageIcon imgsCNOK[]; 					//the images of the movement from CNOK to NOKContainer
	private ImageIcon emptyConteiner;
	//private ImageIcon normalConteiner;
	private ImageIcon fullConteiner;
	
	private Cinta cnok;
	private int state;
	
	private JLabel statusLabel;		//Used for suspended animation until the beginning of the animation
	private int loopslot = -1;  //the current frame number
	
	Timer timer;        //the timer animating the images
	int pause;          //the length of the pause between revs
	int speed;          //animation speed
		
	private boolean stop;
	private boolean emergencyStop;
	
	public CnokAnimation(){
		this.speed = this.GravitySpeed;
		this.stop = false;
		this.emergencyStop = false;
		
		this.state=CnokAnimation.EMTPY;
		
		this.emptyConteiner =new ImageIcon("imgs/estaticas/cnokNull.jpg");
		//this.normalConteiner =new ImageIcon("imgs/estaticas/cnok0.jpg");
		this.fullConteiner =new ImageIcon("imgs/estaticas/cnok1.jpg");
		this.statusLabel = new JLabel(this.emptyConteiner);
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

		cnok = new Cinta();
		cnok.setVisible(true);
		cnok.setOpaque(true);
		cnok.setBounds(0, 0, with, height);
		parentPanel.add(cnok);	
		
		cnok.addMouseListener(new ALCNOK(this));
		cnok.add(statusLabel);
	}
	
	
	//Background task for loading images
	SwingWorker slideWorker = new SwingWorker<ImageIcon[], Void>(){
		@Override
		protected ImageIcon[] doInBackground() throws Exception {
			ImageIcon[] innerImgs = new ImageIcon[CnokAnimation.FRAMES_CNOK];
			
			for (int i = 0; i< CnokAnimation.FRAMES_CNOK; i++){
				innerImgs[i] = ImgLoader.loadImage(CnokAnimation.DIR_CNOK, "cnok/cnok", i+1, CnokAnimation.CNOK_MAX);
			}
			return innerImgs;
		}

		@Override
		public void done(){
			//remove the "Loading Images..." label
			cnok.remove(statusLabel);
			loopslot = -1;
			try{
				imgsCNOK = get();
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
	
			switch(state){
			case(CnokAnimation.EMTPY):
				if(emptyConteiner != null)
					emptyConteiner.paintIcon(this,g,0,0);
			break;
			case(CnokAnimation.FULL):
				if(fullConteiner != null)
					fullConteiner.paintIcon(this,g,0,0);
			break;
			case(CnokAnimation.MOVE):
				if(slideWorker.isDone() && (loopslot > -1) && (loopslot < CnokAnimation.FRAMES_CNOK)){
					if(imgsCNOK != null && imgsCNOK[loopslot] != null){
						imgsCNOK[loopslot].paintIcon(this, g, 0, 0);
					}
				}
				break;
			default:
				System.err.println("Invalid state given CnokAnimation-Cinta.paintComponent()");
				break;
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
			if (loopslot >= CnokAnimation.FRAMES_CNOK)
				loopslot = 0; //restart the sequence
			
			cnok.repaint();

			if (loopslot == CnokAnimation.FRAMES_CNOK - 1)
				//this.timer.restart(); //loops to infinity
				this.timer.stop();
			
		}else if(!emergencyStop){
			loopslot=0;
			cnok.repaint();
		}
	}
	
	private void start() {
		if (slideWorker.isDone() && CnokAnimation.FRAMES_CNOK > 1){
			this.loopslot = 0;
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
	
	public class ALCNOK implements MouseListener{
		CnokAnimation csim;
		int count = -1;
		public ALCNOK(CnokAnimation simu){
			csim = simu;
		}
		@Override
		public void mouseClicked(MouseEvent arg0) {
			count++;
			switch(count){
			case(0):
				csim.start(csim.EMTPY);
			break;
			case(1):
				csim.start(csim.MOVE);
			break;
			case(2):
				csim.start(CnokAnimation.FULL);
			break;
			default:
				count=-1;
				break;
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

		if(state == CnokAnimation.MOVE)
			this.start();
		else
			cnok.repaint();

	}

	@Override
	public int getState() {
		return this.state;
	}

}

	