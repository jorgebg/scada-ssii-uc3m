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
 * Esta clase permite la animacion de la cinta de engranajes
 * Realiza la carga de las imagenes necesarias en background
 * 
 * @author infiniware
 */
public class CenAnimation implements ActionListener, Animation, SlideAnimation{

	private static final int FRAMES_CEN = 4;		 		//number of frames to load in the slides (CEN, CEJ)
	private static final String DIR_CEN = "imgs/cintas/"; 	//direction of the slide images / (cen-4.jpg)
	private static final int CEN_MAX = 26510;				//Max size of the images
	private static final int PAUSE_TIME = 0;			//Lag time for image loading
	private final int PIECE_SIZE = 38;
	
	public static final int STOP = 0;
	public static final int MOVE = 1;
	
	private ImageIcon imgsCEN[]; //the images of the R1 movement (from CEN to EM)
	private ImageIcon imgsREP;
	
	private Cinta cen;
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
	int pause;          //the length of the pause between revs
	int speed;          //animation speed
		
	private boolean stop;
	private boolean emergencyStop;
	
	/**
	 * Crea un objeto para la animacion de la cinta de engranajes
	 * Carga las imagenes y componentes necesarios para la simulacion del
	 * transporte de engranajes y sus propiedades
	 * @param timeCEN - tiempo en el que debe realizarse la animacion completa
	 */
	public CenAnimation(double timeCEN){
		this.speed = ImgLoader.calculateSpeed(timeCEN, CenAnimation.FRAMES_CEN);
		this.pause = CenAnimation.PAUSE_TIME;
		
		this.state=CenAnimation.STOP;
		this.stop = false;
		this.emergencyStop = false;

		this.imgsREP = new ImageIcon(CenAnimation.DIR_CEN+"cen/cen1.jpg");
		this.statusLabel = new JLabel(this.imgsREP);
		this.cen = new Cinta();
		this.cen.setLayout(null);
		
		this.imgPiece = new ImageIcon("imgs/estaticas/eng.png");
		this.piece0 = new JLabel(imgPiece);
		this.piece0.setBounds(17, 232, this.PIECE_SIZE, this.PIECE_SIZE);
		this.piece0.setVisible(false);
		this.piece1 = new JLabel(imgPiece);
		this.piece1.setBounds(17, 184, this.PIECE_SIZE, this.PIECE_SIZE);
		this.piece1.setVisible(false);
		this.pieceN = new JLabel("0", JLabel.CENTER);
		this.pieceN.setForeground(Color.WHITE);
		this.pieceN.setFont(new Font(this.pieceN.getFont().getName(), Font.BOLD, 24));
		this.pieceN.setBounds(3, 124, 67, 30);
		this.piecePFin = new JLabel(imgPiece);
		this.piecePFin.setBounds(17, 68, this.PIECE_SIZE, this.PIECE_SIZE);
		this.piecePFin.setVisible(false);
		this.pieceFin = new JLabel(imgPiece);
		this.pieceFin.setBounds(17, 20, this.PIECE_SIZE, this.PIECE_SIZE);
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
		this.speed = ImgLoader.calculateSpeed(time, CenAnimation.FRAMES_CEN);
		this.pause = CenAnimation.PAUSE_TIME;

		if(this.timer == null)
			this.timer = new Timer(this.speed, this);
		else
			this.timer.setDelay(this.speed);
		
		this.timer.setInitialDelay(this.pause);
	}

	@Override
	public void createGUI(JPanel parentPanel, int with, int height){
		parentPanel.setLayout(null); //set layaout to absolute coordenates

		cen.setVisible(true);
		cen.setOpaque(true);
		cen.setBounds(0, 0, with, height);
		parentPanel.add(cen);	
		
		cen.addMouseListener(new ALCEN(this));
        this.cen.add(statusLabel);
        this.cen.add(piece0);
        this.cen.add(piece1);
        this.cen.add(pieceN);
        this.cen.add(piecePFin);
        this.cen.add(pieceFin);        
	}
	
	/**
	 * Tarea en background para la carga de imagenes utilizando
	 * una clase SwingWorker anonima
	 */
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
	
	/**
	 * Clase que extiende de JPanel para representar la cinta de engranajes
	 * @author infiniware
	 */
	@SuppressWarnings("serial")
	public class Cinta extends JPanel{	
	
		public Cinta(){
			super(new BorderLayout());
		}
		@Override
		protected void paintComponent(Graphics g){
			super.paintComponent(g);
			
			if(slideWorker.isDone() && (loopslot > -1) && (loopslot < CenAnimation.FRAMES_CEN)){
				if(imgsCEN != null && imgsCEN[loopslot] != null){
					imgsCEN[loopslot].paintIcon(this, g, 0, 0);
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
	@Override
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
		this.start(this.state);
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
	
	public class ALCEN implements MouseListener{
		CenAnimation csim;
		int count = -1;
		boolean[] element;
		
		public ALCEN(CenAnimation simu){
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
		
		if(state == CenAnimation.STOP)
			this.stop();
		else
			if (slideWorker.isDone() && CenAnimation.FRAMES_CEN > 1){
				//this.loopslot = 0;
				timer.restart();
				this.stop = false;
				this.emergencyStop = false;
			}
	}

	@Override
	public int getState() {
		return this.state;
	}

	@Override
	public void updateElements(boolean[] elements) {
		if(elements.length >= 6){
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
