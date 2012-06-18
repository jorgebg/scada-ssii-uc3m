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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import infiniware.scada.ui.gui.view.ImgLoader;

/**
 * Esta clase permite la animacion del robot 1
 * 
 * @author infiniware
 */
public class Robot1Animation implements ActionListener, Animation {
	
	//States to be accessed from other classes
	public static final int REP = 0;
	public static final int CEN2EM = 1;	
	public static final int CEJ2EM = 2;	
	public static final int EM2CT = 3;

	private static final int FRAMES_ROBOT = 24; 		//number of frames to load in the robots (R1, R2)
	private static final int FRAMES_REP =4;				//number of frames from REP to CEN/CEJ (needed to calculate the time)
	private static final String DIR_R1 = "imgs/r1/" ; 	//direction of the R1 images / (cen2em1-24.jpg, cej2em1-24.jpg, em2ct1-24.jpg)
	private static final int R1_MAX = 10220;			//Max size of the images
	private static final int N_STATES = 4;				//Max number of states	
	private static final int PAUSE_TIME = 0;			//Lag time for image loading
	
	private ImageIcon imgsR1_CEN2EM[]; 	//the images of the R1 movement (from CEN to EM)
	private ImageIcon imgsR1_CEJ2EM[]; 	//the images of the R1 movement (from CEJ to EM)
	private ImageIcon imgsR1_EM2CT[];  	//the images of the R1 movement (from EM to CT)
	private ImageIcon imgsR1_REP;		//image for the REP state
	
	private Robot1 robot1;
	
	private JLabel statusLabel;		//Used for suspended animation until the beginning of the animation
	
	private int loopslot = -1;  	//the current frame number
	
	Timer timer;        //the timer animating the images
	int speed;			//the current speed of the timer
	int pause;          //the length of the pause between revs
	
	int speedREP2CEN;	//speed for going form REP to CEN/CEJ
	int speedCEN2EM;	//speed for going from CEN/CEJ to EM
	int speedEM2CT;		//speed for going from EM to CT
	
	private int state;	//internal state, allows the class to know wich path to perform (CEN2EM, CEJ2EM or EM2CT)
	
	private boolean stop;
	private boolean emergencyStop;
	
	/**
	 * Crea un objeto Robot1Animation y define los tiempos para cada comportamiento
	 * @param timeREP2CEN - Tiempo de recogida de ejes y engranajes
	 * @param timeCEN2EM - Tiempo de transporte de ejes y engranajes
	 * @param timeEM2CT - Tiempo de transporte de conjutnos montados
	 */
	public Robot1Animation(double timeREP2CEN, double timeCEN2EM, double timeEM2CT){
		this.speedREP2CEN = ImgLoader.calculateSpeed(timeREP2CEN, Robot1Animation.FRAMES_REP);
		this.speedCEN2EM = ImgLoader.calculateSpeed(timeCEN2EM, (Robot1Animation.FRAMES_ROBOT - Robot1Animation.FRAMES_REP));
		this.speedEM2CT = ImgLoader.calculateSpeed(timeEM2CT, Robot1Animation.FRAMES_ROBOT);
		this.pause = Robot1Animation.PAUSE_TIME;
		
		this.state = Robot1Animation.REP;
		this.stop = false;
		this.emergencyStop = false;		

		this.imgsR1_REP = new ImageIcon(Robot1Animation.DIR_R1+"en/cen2em1.jpg");
		this.statusLabel = new JLabel(this.imgsR1_REP) ;
		this.robot1 = new Robot1();
	}

	/**
	 * Inicializa el temporizador con la velocidad apropiada dependiendo del estado
	 * en el que se encuentre el robot y carga todas las imagenes en memoria
	 * Solo debe ser llamado una vez, de no ser asi hay riesgo de saturacion de la memoria
	 */
	@Override
	public void init(){
		//select and change the speed
		this.speed = this.changeInitialSpeed(this.state);
		if(speed < 0)
			System.err.println("Invalid speed given to Robot1Animation.init()");
			
		//create and start the timmer
		this.timer = new Timer(this.speed,this);
        
        //Start loading the images in the background.
        this.robotWorker.execute();
	}
	
	/**
	 * Inicializa el temporizador calculando la velocidad de cada accion con 
	 * los tiempos recibidos por parametro.
	 * Si el temporizador no ha sido inicializo se inicializa.
	 * Este metodo no realiza carga de imagenes por lo que las imagenes
	 * deben haber sido cargadas antes por medio del metodo init()
	 * @param timeR - tiempo de recogida de engranajes y ejes
	 * @param timeT1 - tiempo de transporte de engranajes y ejes
	 * @param timeT2 - tiempo de transporte de conjuntos montados
	 */
	public void init(double timeR, double timeT1, double timeT2){
		this.speedREP2CEN = ImgLoader.calculateSpeed(timeR, Robot1Animation.FRAMES_REP);
		this.speedCEN2EM = ImgLoader.calculateSpeed(timeT1, (Robot1Animation.FRAMES_ROBOT - Robot1Animation.FRAMES_REP));
		this.speedEM2CT = ImgLoader.calculateSpeed(timeT2, Robot1Animation.FRAMES_ROBOT);
		
		this.speed = this.changeInitialSpeed(this.state);
		this.pause = Robot1Animation.PAUSE_TIME;

		if(this.timer == null)
			this.timer = new Timer(this.speed, this);
		else
			this.timer.setDelay(this.speed);
		
		this.timer.setInitialDelay(this.pause);
	}
	
	@Override
	public void createGUI(JPanel parentPanel, int with, int height){
		parentPanel.setLayout(null); //set layaout to absolute coordenates

		robot1.setVisible(true);
		robot1.setOpaque(true);
		robot1.setBounds(0, 0, with, height);
		parentPanel.add(robot1);	
		
		//robot1.addMouseListener(new ALR(this));
		robot1.add(statusLabel);
	}

	/**
	 * Tarea en background para la carga de imagenes utilizando
	 * una clase SwingWorker anonima
	 */
	SwingWorker robotWorker = new SwingWorker<List<ImageIcon[]>, Void>(){
		@Override
		protected List<ImageIcon[]> doInBackground() throws Exception {
			final List<ImageIcon[]> innerList = new ArrayList<ImageIcon[]>();
			ImageIcon[] innerImgs;
			
			for(int j=Robot1Animation.CEN2EM; j<Robot1Animation.N_STATES; j++){
				innerImgs = new ImageIcon[Robot1Animation.FRAMES_ROBOT];
				switch(j){
				case(Robot1Animation.CEN2EM):
					for (int i = 0; i< Robot1Animation.FRAMES_ROBOT; i++){
						innerImgs[i] = ImgLoader.loadImage(Robot1Animation.DIR_R1, "en/cen2em", i+1, Robot1Animation.R1_MAX);
					}
				innerList.add(innerImgs);
				break;
				case(Robot1Animation.CEJ2EM):
					for (int i = 0; i< Robot1Animation.FRAMES_ROBOT; i++){
						innerImgs[i] = ImgLoader.loadImage(Robot1Animation.DIR_R1, "ej/cej2em", i+1, Robot1Animation.R1_MAX);
					}
				innerList.add(innerImgs);
				break;
				case(Robot1Animation.EM2CT):
					for (int i = 0; i< Robot1Animation.FRAMES_ROBOT; i++){
						innerImgs[i] = ImgLoader.loadImage(Robot1Animation.DIR_R1, "cm/em2ct", i+1, Robot1Animation.R1_MAX);
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
			robot1.remove(statusLabel);
			loopslot = -1;
			try{
				List<ImageIcon[]> outerList = get();
				
				imgsR1_CEN2EM = outerList.get(0);
				imgsR1_CEJ2EM = outerList.get(1);
				imgsR1_EM2CT = outerList.get(2);
				
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
	
	/**
	 * Clase que extiende de JPanel para representar el robot 1
	 * @author infiniware
	 */
	@SuppressWarnings("serial")
	public class Robot1 extends JPanel{	
	
		public Robot1(){
			super(new BorderLayout());
		}
		
		protected void paintComponent(Graphics g){
			super.paintComponent(g);
		
			switch(state){
			case(Robot1Animation.REP):
				if(imgsR1_REP != null )
					imgsR1_REP.paintIcon(this, g, 0, 0);
			break;
			case(Robot1Animation.CEN2EM):
				if(robotWorker.isDone() && (loopslot > -1) && (loopslot < Robot1Animation.FRAMES_ROBOT)){
					if(imgsR1_CEN2EM != null && imgsR1_CEN2EM[loopslot] != null){
						imgsR1_CEN2EM[loopslot].paintIcon(this, g, 0, 0);
					}
				}
			break;
			case(Robot1Animation.CEJ2EM):
				if(robotWorker.isDone() && (loopslot > -1) && (loopslot < Robot1Animation.FRAMES_ROBOT)){
					if(imgsR1_CEJ2EM != null && imgsR1_CEJ2EM[loopslot] != null){
						imgsR1_CEJ2EM[loopslot].paintIcon(this, g, 0, 0);
					}
				}
			break;
			case(Robot1Animation.EM2CT):
				if(robotWorker.isDone() && (loopslot > -1) && (loopslot < Robot1Animation.FRAMES_ROBOT)){
					if(imgsR1_EM2CT != null && imgsR1_EM2CT[loopslot] != null){
						imgsR1_EM2CT[loopslot].paintIcon(this, g, 0, 0);
					}
				}
			break;
			default:
				System.err.println("Invalid state give to Robot1Animation-Robot1.paintComponent(Graphics g)");
				break;

			}
		}
	}

	/**
	 * Maneja los eventos de tiempo y actualiza la variable looslots
	 * que se encarga de actualizar los frames.
	 * Si se encuentra en el ultimo frame de la animacion espera 
	 * hasta que se reciva una nueva accion
	 * 
	 * Maneja los eventos de parada y parada de emergencia.
	 * Si se realiza una parada de emergencia se para en el frame
	 * que se encuentre y continua desde ese punto cuando se 
	 * vuelva a activar
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (!robotWorker.isDone()) 
			return; //If still loading, can't animate.
		
		if(!this.stop && !this.emergencyStop){
			loopslot++;	//increases the frame
			if (loopslot >= Robot1Animation.FRAMES_ROBOT){
				loopslot = 0; //restart the sequence
				this.resetSpeed();
			}
			
			//Change the speed if the robot has finished the REP2CEN/CEJ phase
			if(this.state == Robot1Animation.CEN2EM || this.state == Robot1Animation.CEJ2EM)
				if(this.loopslot >= Robot1Animation.FRAMES_REP)
					this.updateSpeed();

			robot1.repaint();

			if (loopslot == Robot1Animation.FRAMES_ROBOT - 1)
				this.stop(); //executes only one iteration
				//this.timer.restart(); //loops to infinity
			
		}else if(!emergencyStop){
			loopslot=0;
			robot1.repaint();
		}
	}
	
	@Override
	public void start(int state) {
		if(state < 0 || state >= Robot1Animation.N_STATES){
			System.err.println("Invalid state give to Robot1Animation.start(int state)");
		}else{
			if(this.timer==null)
				this.init(); //if the timer is null the class is initialized
			
			if(this.state != state){
				this.state = state;
			}

			this.speed = this.changeInitialSpeed(this.state);
			this.timer.setDelay(this.speed);
			this.timer.start();
	
			
			if (robotWorker.isDone() && (Robot1Animation.FRAMES_ROBOT > 1)) {
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
		case(Robot1Animation.REP):
			newSpeed = this.speedREP2CEN;
			break;
		case(Robot1Animation.CEN2EM):
			newSpeed = this.speedREP2CEN;
		break;
		case(Robot1Animation.CEJ2EM):
			newSpeed = this.speedREP2CEN;
		break;
		case(Robot1Animation.EM2CT):
			newSpeed = this.speedEM2CT;
		break;
		default:
			System.err.println("Invalid state give to Robot1Animation.changeSpeed(int speed)");
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
		this.speed = this.speedCEN2EM;
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
		this.speed = this.speedREP2CEN;
		timer.setDelay(this.speed);
		timer.start();
	}
	
	public class ALR implements MouseListener{
		Robot1Animation rsim;
		int count = -1;
		public ALR(Robot1Animation simu){
			rsim = simu;
		}
		@Override
		public void mouseClicked(MouseEvent arg0) {
			count++;
			if(count >= Robot1Animation.N_STATES){
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

	@Override
	public int getState() {
		return this.state;
	}
}
