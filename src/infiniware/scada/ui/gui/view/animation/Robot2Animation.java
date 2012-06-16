package infiniware.scada.ui.gui.view.animation;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import infiniware.scada.ui.gui.view.ImgLoader;

public class Robot2Animation implements ActionListener, Animation {

	//States to be accessed from other classes
		public static final int REP =0;
		public static final int CT2ES = 1;	
		public static final int ES2EV = 2;	
		public static final int EV2COK = 3;
		public static final int EV2CNOK = 4;

		private static final int FRAMES_ROBOT = 24; 		//number of frames to load in the robots (R2)
		private static final int FRAMES_REP =4;				//number of frames from REP to CT (needed to calculate the time)
		private static final String DIR_R2 = "imgs/r2/" ; 	//direction of the R1 images / (ct2es1-24.jpg, es2ev1-24.jpg, ev2cok1-24.jpg, ev2cnok1-24.jpg)
		private static final int R2_MAX = 10220;			//Max size of the images
		private static final int N_STATES = 5;				//Max number of states	
		private static final int PAUSE_TIME = 0;			//Lag time for image loading
			
		private ImageIcon imgsR2_REP; 		//the image for the R2 REP state 
		private ImageIcon imgsR2_CT2ES[]; //the images of the R1 movement (from CT to ES)
		private ImageIcon imgsR2_ES2EV[]; //the images of the R1 movement (from ES to EV)
		private ImageIcon imgsR2_EV2COK[];  //the images of the R1 movement (from EV to COK)
		private ImageIcon imgsR2_EV2CNOK[];  //the images of the R1 movement (from EV to CNOK)
		
		private Robot2 robot2;
		
		private JLabel statusLabel;		//Used for suspended animation until the beginning of the animation
		
		private int loopslot = -1;  	//the current frame number
		
		Timer timer;        //the timer animating the images
		int speed;			//the current speed of the timer
		int pause;          //the length of the pause between revs
		
		int speedREP2CT;	//speed for going form REP to CT
		int speedCT2ES;		//speed for going from CT to ES
		int speedES2EV;		//speed for going from ES to EV 
		int speedEV2COK;	//speed for going from EV to COK or CNOK (= speedES2EV)
		
		private int state;	//internal state, allows the class to know wich path to perform (CT2ES, ES2EV, EV2COK, EV2CNOK)
		
		private boolean stop;
		private boolean emergencyStop;
		
		public Robot2Animation(double timeREP2CT, double timeCT2ES, double timeES2EV){
			this.speedREP2CT = ImgLoader.calculateSpeed(timeREP2CT, Robot2Animation.FRAMES_REP);
			this.speedCT2ES = ImgLoader.calculateSpeed(timeCT2ES, (Robot2Animation.FRAMES_ROBOT - Robot2Animation.FRAMES_REP));
			this.speedES2EV = ImgLoader.calculateSpeed(timeES2EV, Robot2Animation.FRAMES_ROBOT);
			this.speedEV2COK = this.speedES2EV;	//no time defined for this action.
			this.pause = Robot2Animation.PAUSE_TIME;
			
			this.state = Robot2Animation.REP;
			this.stop = false;
			this.emergencyStop = false;

			this.imgsR2_REP = new ImageIcon(Robot2Animation.DIR_R2+"cm/ct2es1.jpg");
			this.statusLabel = new JLabel(this.imgsR2_REP) ;
			this.robot2 = new Robot2();
		}

		public void init(){
			//select and change the speed
			this.speed = this.changeInitialSpeed(this.state);
			if(speed < 0){
				System.err.println("Invalid speed given to Robot2Animation.init()");
			}
			
			//create and start the timmer
			this.timer = new Timer(this.speed,this);
	        this.timer.start(); 
	        
	        //Start loading the images in the background.
	        this.robotWorker.execute();
		}
		
		public void createGUI(JPanel parentPanel, int with, int height){
			parentPanel.setLayout(null); //set layaout to absolute coordenates

			robot2.setVisible(true);
			robot2.setOpaque(true);
			robot2.setBounds(0, 0, with, height);
			parentPanel.add(robot2);	
			
			robot2.addMouseListener(new ALR2(this));
			robot2.add(statusLabel, BorderLayout.CENTER);
		}
		
		//Background task for loading images
		SwingWorker robotWorker = new SwingWorker<List<ImageIcon[]>, Void>(){
			@Override
			protected List<ImageIcon[]> doInBackground() throws Exception {
				final List<ImageIcon[]> innerList = new ArrayList<ImageIcon[]>();
				ImageIcon[] innerImgs;
				
				for(int j=0; j<Robot2Animation.N_STATES; j++){
					innerImgs = new ImageIcon[Robot2Animation.FRAMES_ROBOT];
					switch(j){
					case(Robot2Animation.CT2ES):
						for (int i = 0; i< Robot2Animation.FRAMES_ROBOT; i++){
							innerImgs[i] = ImgLoader.loadImage(Robot2Animation.DIR_R2, "cm/ct2es", i+1, Robot2Animation.R2_MAX);
						}
					innerList.add(innerImgs);
					break;
					case(Robot2Animation.ES2EV):
						for (int i = 0; i< Robot2Animation.FRAMES_ROBOT; i++){
							innerImgs[i] = ImgLoader.loadImage(Robot2Animation.DIR_R2, "cs/es2ev", i+1, Robot2Animation.R2_MAX);
						}
					innerList.add(innerImgs);
					break;
					case(Robot2Animation.EV2COK):
						for (int i = 0; i< Robot2Animation.FRAMES_ROBOT; i++){
							innerImgs[i] = ImgLoader.loadImage(Robot2Animation.DIR_R2, "cok/ev2cok", i+1, Robot2Animation.R2_MAX);
						}
					innerList.add(innerImgs);
					break;
					case(Robot2Animation.EV2CNOK):
						for (int i = 0; i< Robot2Animation.FRAMES_ROBOT; i++){
							innerImgs[i] = ImgLoader.loadImage(Robot2Animation.DIR_R2, "cnok/ev2cnok", i+1, Robot2Animation.R2_MAX);
						}
					innerList.add(innerImgs);
					break;
					}
				}
				return innerList;
			}

			@Override
			public void done(){
				//remove the "Loading Images..." label
				robot2.remove(statusLabel);
				loopslot = -1;
				try{
					List<ImageIcon[]> outerList = get();
					
					imgsR2_CT2ES = outerList.get(0);
					imgsR2_ES2EV = outerList.get(1);
					imgsR2_EV2COK = outerList.get(2);
					imgsR2_EV2CNOK = outerList.get(3);
					
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
		public class Robot2 extends JPanel{	
		
			public Robot2(){
				super(new BorderLayout());
			}
			
			protected void paintComponent(Graphics g){
				super.paintComponent(g);
			
				switch(state){
				case(Robot2Animation.REP):
					if(imgsR2_REP != null)
						imgsR2_REP.paintIcon(this, g, 0, 0);
					break;
				case(Robot2Animation.CT2ES):
					if(robotWorker.isDone() && (loopslot > -1) && (loopslot < Robot2Animation.FRAMES_ROBOT)){
						if(imgsR2_CT2ES != null && imgsR2_CT2ES[loopslot] != null){
							imgsR2_CT2ES[loopslot].paintIcon(this, g, 0, 0);
						}
					}
				break;
				case(Robot2Animation.ES2EV):
					if(robotWorker.isDone() && (loopslot > -1) && (loopslot < Robot2Animation.FRAMES_ROBOT)){
						if(imgsR2_ES2EV != null && imgsR2_ES2EV[loopslot] != null){
							imgsR2_ES2EV[loopslot].paintIcon(this, g, 0, 0);
						}
					}
				break;
				case(Robot2Animation.EV2COK):
					if(robotWorker.isDone() && (loopslot > -1) && (loopslot < Robot2Animation.FRAMES_ROBOT)){
						if(imgsR2_EV2COK != null && imgsR2_EV2COK[loopslot] != null){
							imgsR2_EV2COK[loopslot].paintIcon(this, g, 0, 0);
						}
					}
				break;
				case(Robot2Animation.EV2CNOK):
					if(robotWorker.isDone() && (loopslot > -1) && (loopslot < Robot2Animation.FRAMES_ROBOT)){
						if(imgsR2_EV2CNOK != null && imgsR2_EV2CNOK[loopslot] != null){
							imgsR2_EV2CNOK[loopslot].paintIcon(this, g, 0, 0);
						}
					}
				break;
				default:
					System.err.println("Invalid state give to Robot2Animation-Robot2.paintComponent(Graphics g)");
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
			if (!robotWorker.isDone()) 
				return; //If still loading, can't animate.
			
			if(!this.stop && !this.emergencyStop){
				loopslot++;	//increases the frame
				if (loopslot >= Robot2Animation.FRAMES_ROBOT){
					loopslot = 0; //restart the sequence
					this.resetSpeed();
				}
				
				//Change the speed if the robot has finished the REP2CEN/CEJ phase
				if(this.state == Robot2Animation.CT2ES)
					if(this.loopslot >= Robot2Animation.FRAMES_REP)
						this.updateSpeed();

				robot2.repaint();

				if (loopslot == Robot2Animation.FRAMES_ROBOT - 1)
					this.stop(); //executes only one iteration
					//this.timer.restart(); //loops to infinity
				
			}else if(!emergencyStop){
				loopslot=0;
				robot2.repaint();
			}
		}
		
		@Override
		public void start(int state) {
			if(state < 0 || state >= Robot2Animation.N_STATES){
				System.err.println("Invalid state give to Robot2Animation.start(int state)");
			}else{
				if(this.timer==null)
					this.init(); //if the timer is null the class is initialized
				
				if(this.state != state){
					this.state = state;
				}

				this.speed = this.changeInitialSpeed(this.state);
				this.timer.setDelay(this.speed);
				this.timer.start();
		
				
				if (robotWorker.isDone() && (Robot2Animation.FRAMES_ROBOT > 1)) {
					if(!this.emergencyStop)
						this.loopslot = 0; 	//if it was not an emergerncy stop, restart the loopslot
					
					this.stop = false;
					this.emergencyStop = false;
				}
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
		
		/**
		 * Changes the speed according with the presets speeds
		 * and the current state
		 * @param state
		 * @return the new speed or -1 if the given state was not correct
		 */
		private int changeInitialSpeed(int state){
			int newSpeed = -1;
			switch(state){
			case(Robot2Animation.REP):
				newSpeed = this.speedREP2CT;
			break;
			case(Robot2Animation.CT2ES):
				newSpeed = this.speedREP2CT;
			break;
			case(Robot2Animation.ES2EV):
				newSpeed = this.speedES2EV;
			break;
			case(Robot2Animation.EV2COK):
				newSpeed = this.speedEV2COK;
			break;
			case(Robot2Animation.EV2CNOK):
				newSpeed = this.speedEV2COK;
			break;
			default:
				System.err.println("Invalid state give to Robot2Animation.changeSpeed(int speed)");
				break;
			}
			return newSpeed;
		}
		
		/**
		 * Updates the speed when the frame sequence changes 
		 * from REM2CEN to CEN2EM or REP2CEJ to CEJ2EM
		 * needed because of the time difference between
		 * the two behaviors.
		 */
		private void updateSpeed(){
			this.speed = this.speedCT2ES;
			timer.setDelay(this.speed);
			timer.setInitialDelay(this.pause);
			timer.start();
		}
		
		/**
		 * Resets the speed when the frame sequence changes 
		 * from CEN2EM to REP2CEN or CEJ2EM to REP2CEJ
		 * needed because of the time difference between
		 * the two behaviors.
		 */
		private void resetSpeed(){
			this.speed = this.speedREP2CT;
			timer.setDelay(this.speed);
			timer.start();
		}
		
		@Override
		public int getState() {
			return this.state;
		}
		
		public class ALR2 implements MouseListener{
			Robot2Animation rsim;
			int count = -1;
			public ALR2(Robot2Animation simu){
				rsim = simu;
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {
				count++;
				if(count >= Robot2Animation.N_STATES){
					count = 0;
				}
				//System.out.println("Estado: "+rsim.getState());
				rsim.start(count);
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
