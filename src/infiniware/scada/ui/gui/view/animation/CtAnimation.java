package infiniware.scada.ui.gui.view.animation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
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

/**
 * Esta clase permite la animacion de la cinta de transporte
 * Realiza la carga de las imagenes necesarias en background
 * 
 * @author infiniware
 */
public class CtAnimation implements ActionListener, Animation, SlideAnimation {

	private static final int FRAMES_CT = 4;		 	//number of frames to load in the slides (CT)
	private static final String DIR_CT = "imgs/cintas/"; 	//direction of the slide images / (ct1-4.jpg)
	private static final int CT_MAX = 24996;			//Max size of the images
	private static final int PAUSE_TIME = 0;			//Lag time for image loading
	private final int PIECE_SIZE = 38;

	public static final int STOP = 0;
	public static final int MOVE = 1;
	
	private ImageIcon imgsCT[]; //the images of the R1 movement (from CEN to EM)
	private ImageIcon imgsREP;
	
	private Cinta ct;
	private int state;
	
	private JLabel statusLabel;		//Used for suspended animation until the beginning of the animation
	
	private ImageIcon imgPiece;
	private JLabel piece0;
	private JLabel piece1;
	private JLabel pieceN;
	private JLabel piecePFin;
	private JLabel pieceFin;
	
	private int loopslot = -1;  //the current frame number
	
	Timer timer;        //the timer animating the images
	int speed;          //animation speed
	int pause;          //the length of the pause between revs
		
	private boolean stop;
	private boolean emergencyStop;
	
	/**
	 * Crea un objeto para la animacion de la cinta de transporte
	 * Carga las imagenes y componentes necesarios para la simulacion del
	 * transporte de conjuntos montados y sus propiedades
	 * @param timeCT - tiempo en el que debe realizarse la animacion completa
	 */
	public CtAnimation(double timeCT){
		this.speed = ImgLoader.calculateSpeed(timeCT, CtAnimation.FRAMES_CT);
		this.pause = CtAnimation.PAUSE_TIME;		
		
		this.state=CtAnimation.STOP;
		this.stop = false;
		this.emergencyStop = false;
		
		this.imgsREP = new ImageIcon(CtAnimation.DIR_CT+"ct/ct1.jpg");
		this.statusLabel = new JLabel(this.imgsREP);
		this.ct = new Cinta();
		this.ct.setLayout(null);
		
		this.imgPiece = new ImageIcon("imgs/estaticas/cm.png");
		this.piece0 = new JLabel(imgPiece);
		this.piece0.setBounds(197, 12, this.PIECE_SIZE, this.PIECE_SIZE);
		this.piece0.setVisible(false);
		this.piece1 = new JLabel(imgPiece);
		this.piece1.setBounds(155, 12, this.PIECE_SIZE, this.PIECE_SIZE);
		this.piece1.setVisible(false);
		this.pieceN = new JLabel("0", JLabel.CENTER);
		this.pieceN.setForeground(Color.WHITE);
		this.pieceN.setFont(new Font(this.pieceN.getFont().getName(), Font.BOLD, 24));
		this.pieceN.setBounds(89, 12, 66, 30);
		this.piecePFin = new JLabel(imgPiece);
		this.piecePFin.setBounds(57, 12, this.PIECE_SIZE, this.PIECE_SIZE);
		this.piecePFin.setVisible(false);
		this.pieceFin = new JLabel(imgPiece);
		this.pieceFin.setBounds(15, 12, this.PIECE_SIZE, this.PIECE_SIZE);
		this.pieceFin.setVisible(false);
	}
	
	/**
	 * Inicializa el temporizador con la velocidad definida 
	 * al crear el objeto y carga las imagenes en memoria.
	 * Solo debe ser llamado una vez, de no ser asi hay riesgo de saturacion de la memoria
	 */
	@Override
	public void init(){
		this.timer = new Timer(this.speed, this);
        this.timer.setInitialDelay(this.pause);
        //this.timer.start(); 
        
        //Start loading the images in the background.
        this.slideWorker.execute();
	}
	
	/**
	 * Inicializa el temporizador calculando la velocidad el tiempos recibido por parametro.
	 * Si el temporizador no ha sido inicializo se inicializa.
	 * Este metodo no realiza carga de imagenes por lo que las imagenes
	 * deben haber sido cargadas antes por medio del metodo init()
	 * @param time - tiempo total del recorrido
	 */
	public void init(double time){
		this.speed = ImgLoader.calculateSpeed(time, CtAnimation.FRAMES_CT);
		this.pause = CtAnimation.PAUSE_TIME;

		if(this.timer == null)
			this.timer = new Timer(this.speed, this);
		else
			this.timer.setDelay(this.speed);
		
		this.timer.setInitialDelay(this.pause);
	}
	
	@Override
	public void createGUI(JPanel parentPanel, int with, int height){
		parentPanel.setLayout(null); //set layaout to absolute coordenates

		ct.setVisible(true);
		ct.setOpaque(true);
		ct.setBounds(0, 0, with, height);
		parentPanel.add(ct);	
		
		ct.addMouseListener(new ALCT(this));
		this.ct.add(statusLabel);
		this.ct.add(piece0);
        this.ct.add(piece1);
        this.ct.add(pieceN);
        this.ct.add(piecePFin);
        this.ct.add(pieceFin); 
	}
	
	/**
	 * Tarea en background para la carga de imagenes utilizando
	 * una clase SwingWorker anonima
	 */
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
	
	/**
	 * Clase que extiende de JPanel para representar la cinta de transporte
	 * @author infiniware
	 */
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
		boolean[] element;
		
		public ALCT(CtAnimation simu){
			csim = simu;
			element = new boolean[8];
		}
		@Override
		public void mouseClicked(MouseEvent arg0) {
			count++;
			
			if(count == 0){
				csim.start(1);
				element[count]=true;
			}else if(count>0 && count < element.length){
				element[count-1] = false;
				element[count]=true;
			}else if(count == element.length){
				element[count-2]=false;
				element[count-1]=true;
				csim.start(0);
			}else{
				element[element.length-1] =false;
				count =-1;
			}
			csim.updateElements(element);
			
			
			/*if(csim.state==csim.STOP){
				csim.start(1);
			}else{
				csim.start(0);
			}*/
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
	
	@Override
	public void updateElements(boolean[] elements) {
		
		if(elements.length == 2){
			this.piece0.setVisible(elements[0]);
			this.piece1.setVisible(elements[1]);
			this.pieceFin.setVisible(elements[elements.length-1]);
			this.piecePFin.setVisible(elements[elements.length-2]);
			this.pieceN.setText(this.getElementNumber(elements));
			
		}
	}
	
	private String getElementNumber(boolean[] elements){
		int count=0;
		for (int i=2; i<elements.length-2;i++){
			if(elements[i])
				count++;
		}
		return String.valueOf(count);
	}
	
}
