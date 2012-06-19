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
 * Esta clase permite la animacion de la cinta de conjuntos validos
 * Realiza la carga de las imagenes necesarias en background
 * 
 * @author infiniware
 */
public class CokAnimation implements ActionListener, Animation, SlideAnimation {

	private static final int FRAMES_COK = 4;		 	//number of frames to load in the slides (COK)
	private static final String DIR_COK = "imgs/cintas/"; 	//direction of the slide images / (cok1-4.jpg)
	private static final int COK_MAX = 26553;			//Max size of the images
	private static final int PAUSE_TIME = 0;			//Lag time for image loading
	private final int PIECE_SIZE = 38;
	
	public static final int STOP = 0;
	public static final int MOVE = 1;
		
	private ImageIcon imgsCOK[]; //the images of the R1 movement (from CEN to EM)
	private ImageIcon imgsREP;
	
	private Cinta cok;
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
	 * Crea un objeto para la animacion de la cinta de conjuntos validos
	 * Carga las imagenes y componentes necesarios para la simulacion del
	 * transporte de conjuntos validos y sus propiedades
	 * @param timeCOK - tiempo en el que debe realizarse la animacion completa
	 */
	public CokAnimation(double timeCOK){
		this.speed = ImgLoader.calculateSpeed(timeCOK, CokAnimation.FRAMES_COK);
		this.pause = CokAnimation.PAUSE_TIME;
		
		this.imgsREP = new ImageIcon(CokAnimation.DIR_COK+"cok/cok1.jpg");
		this.state=CokAnimation.STOP;
		
		this.stop = false;
		this.emergencyStop = false;

		this.statusLabel = new JLabel(imgsREP);
		this.cok = new Cinta();
		this.cok.setLayout(null);
		
		this.imgPiece = new ImageIcon("imgs/estaticas/cv.png");
		this.piece0 = new JLabel(imgPiece);
		this.piece0.setBounds(17, 20, this.PIECE_SIZE, this.PIECE_SIZE);
		this.piece0.setVisible(false);
		this.piece1 = new JLabel(imgPiece);
		this.piece1.setBounds(17, 68, this.PIECE_SIZE, this.PIECE_SIZE);
		this.piece1.setVisible(false);
		this.pieceN = new JLabel("0", JLabel.CENTER);
		this.pieceN.setForeground(Color.WHITE);
		this.pieceN.setFont(new Font(this.pieceN.getFont().getName(), Font.BOLD, 24));
		this.pieceN.setBounds(3, 124, 67, 30);
		this.piecePFin = new JLabel(imgPiece);
		this.piecePFin.setBounds(17, 184, this.PIECE_SIZE, this.PIECE_SIZE);
		this.piecePFin.setVisible(false);
		this.pieceFin = new JLabel(imgPiece);
		this.pieceFin.setBounds(17, 232, this.PIECE_SIZE, this.PIECE_SIZE);
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
		this.speed = ImgLoader.calculateSpeed(time, CokAnimation.FRAMES_COK);
		this.pause = CokAnimation.PAUSE_TIME;

		if(this.timer == null)
			this.timer = new Timer(this.speed, this);
		else
			this.timer.setDelay(this.speed);
		
		this.timer.setInitialDelay(this.pause);
	}
	
	@Override
	public void createGUI(JPanel parentPanel, int with, int height){
		parentPanel.setLayout(null); //set layaout to absolute coordenates

		cok.setVisible(true);
		cok.setOpaque(true);
		cok.setBounds(0, 0, with, height);
		parentPanel.add(cok);	
		
		cok.addMouseListener(new ALCOK(this));
        this.cok.add(statusLabel);
        this.cok.add(piece0);
        this.cok.add(piece1);
        this.cok.add(pieceN);
        this.cok.add(piecePFin);
        this.cok.add(pieceFin);  
	}
	
	/**
	 * Tarea en background para la carga de imagenes utilizando
	 * una clase SwingWorker anonima
	 */	
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
	
	/**
	 * Clase que extiende de JPanel para representar la cinta de 
	 * conjuntos validos
	 * @author infiniware
	 */
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

	@Override
	public void emergencyStop() {
		timer.stop();
		this.emergencyStop = true;
	}
	
	private void stop() {
		timer.stop();
		this.stop = true;
	}
	
	public class ALCOK implements MouseListener{
		CokAnimation csim;
		int count = -1;
		boolean[] element;
		
		public ALCOK(CokAnimation simu){
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
		
		if(state == CokAnimation.STOP)
			this.stop();
		else
			if (slideWorker.isDone() && CokAnimation.FRAMES_COK > 1){
				timer.restart();
				this.stop = false;
				this.emergencyStop = false;
			}
	}
	
	public void start() {
		this.start(this.state);
	}

	@Override
	public int getState() {
		return this.state;
	}
	
	@Override
	public void updateElements(boolean[] elements) {
		if(elements.length > 6){
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
