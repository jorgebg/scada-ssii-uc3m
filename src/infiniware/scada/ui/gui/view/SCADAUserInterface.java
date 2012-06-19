package infiniware.scada.ui.gui.view;



import infiniware.almacenamiento.Configuracion;
import infiniware.almacenamiento.Produccion;
import infiniware.scada.Scada;
import infiniware.scada.informes.Informes;
import infiniware.scada.informes.modelos.Fabricacion;
import infiniware.scada.informes.modelos.Funcionamiento;
import infiniware.scada.modelos.ConjuntoGuardable;
import infiniware.scada.modelos.ConjuntoParametros;
import infiniware.scada.ui.gui.Gui;
import infiniware.scada.ui.gui.view.animation.AnimationController;
import infiniware.scada.ui.gui.view.animation.CnokAnimation;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.eclipse.wb.swing.FocusTraversalOnArray;

public class SCADAUserInterface extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtLongitudCen;			//CEN_l
	private JTextField txtVelocidadCen;			//CEN_v
	private JTextField txtCapacidadCen;			//CEN_c
	private JTextField txtLongitudCej;			//CEJ_l
	private JTextField txtVelocidadCej;			//CEJ_v
	private JTextField txtCapacidadCej;			//CEJ_c
	private JTextField txtLongitudCt;			//CT_l
	private JTextField txtVelocidadCt;			//CT_v
	private JTextField txtLongitudCok;			//COK_l
	private JTextField txtVelocidadCok;			//COK_v
	private JTextField txtLongitudCnok;			//CNOK_l
	private JTextField txtVelocidadCnok;		//CNOK_v
	private JTextField txtTiempoEm;				//EM_t
	private JTextField txtTiempoEs;				//ES_t
	private JTextField txtTiempoEv;				//EV_t
	private JTextField txtTRecogidaEnej;		//R1_tREn
	private JTextField txtTTransporteEnej;		//R1_tTEn
	private JTextField txtTTransporteCm1;		//R1_tTCM
	private JTextField txtTRecogidaCm;			//R2_tRCM
	private JTextField txtTTransporteCm2;		//R2_tTCM
	private JTextField txtTTransporteCs;		//R2_tTCS
	
	private JTextField txtConjuntosOk;			//COK
	private JTextField txtConjuntosNok;			//CNOK
	private JTextField txtConjuntosOkTotales;	//COKTotal
	private JTextField txtConjuntosNokTotales;	//CNOKTotal
	private JTextField txtParadasNormales;		//PN
	private JTextField txtParadasEmergencia;	//PE
	private JTextField txtArranques;			//ARR
	
	private Desktop desktop;
	
	public JTextArea logConsole;
    public Map<String, Double> mapaParametros;
    public Map<String, Integer> mapaInformes;
	
	public AnimationController ac;
	public double parametros[];
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SCADAUserInterface frame = new SCADAUserInterface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Crea la ventana de la GUI con todos sus componentes
	 */
	public SCADAUserInterface() {
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		setBounds(new Rectangle(0, 0, 1024, 1000));
		setSize(new Dimension(1280, 1000));
		setTitle("SCADA");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1280, 968);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);
		
		JMenuItem mntmGuardarParametros = new JMenuItem("Guardar Parametros");
		mnMenu.add(mntmGuardarParametros);
		
		JMenuItem mntmCargarParametros = new JMenuItem("Cargar Parametros");
		mnMenu.add(mntmCargarParametros);
		
		JMenu mnAyuda = new JMenu("Ayuda");
		menuBar.add(mnAyuda);
		
		JMenuItem mntmAyuda = new JMenuItem("Ayuda");
		mnAyuda.add(mntmAyuda);
		
		JMenuItem mntmManual = new JMenuItem("Manual");
		mnAyuda.add(mntmManual);
		
		contentPane = new JPanel();
		contentPane.setSize(new Dimension(1280, 1024));
		contentPane.setBackground(new Color(169, 169, 169));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Registro de Cambios", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 695, 1244, 203);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JButton btnStart = new JButton("Start");
		
		
		btnStart.setForeground(new Color(0, 128, 0));
		btnStart.setBounds(1078, 19, 144, 23);
		panel.add(btnStart);
		
		JButton btnStop = new JButton("Stop");
		btnStop.setForeground(new Color(255, 0, 0));
		btnStop.setBounds(1078, 53, 144, 23);
		panel.add(btnStop);
		
		//EMERGENCY STOP BUTTON
		JButton btnEmergencyStop = new JButton("Emergency Stop");
		
		btnEmergencyStop.setBackground(new Color(255, 0, 0));
		btnEmergencyStop.setForeground(new Color(0, 0, 0));
		btnEmergencyStop.setBounds(1078, 138, 144, 49);
		panel.add(btnEmergencyStop);
		
		this.logConsole = new JTextArea(1062,173);
		logConsole.setEditable(false);
		logConsole.setBounds(6, 19, 1062, 173);
		logConsole.setLineWrap(true);
		logConsole.setWrapStyleWord(true);
		logConsole.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		panel.add(logConsole);
		JScrollPane scrollPane = new JScrollPane(logConsole); 
		scrollPane.setBounds(6, 19, 1062, 173);
		panel.add(scrollPane);
		panel.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{btnStart, btnStop, btnEmergencyStop}));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 0, 1244, 689);
		contentPane.add(tabbedPane);
		
		JPanel panelSimulador = new JPanel();
		tabbedPane.addTab("Simulador", null, panelSimulador, null);
		panelSimulador.setLayout(null);
		
		//Creates the Simulator frame
		JPanel panelInstalacion = new JPanel();
		panelInstalacion.setBackground(Color.LIGHT_GRAY);
		panelInstalacion.setBounds(6, 6, 1068, 642);
		panelSimulador.add(panelInstalacion);
		panelInstalacion.setLayout(null);
		
		//CEN
		JPanel panel_CEN = new JPanel();
		panel_CEN.setBackground(Color.LIGHT_GRAY);
		panel_CEN.setBounds(842, 310, 73, 291);
		panelInstalacion.add(panel_CEN);
		panel_CEN.setLayout(null);
		//CEJ
		JPanel panel_CEJ = new JPanel();
		panel_CEJ.setBackground(Color.LIGHT_GRAY);
		panel_CEJ.setBounds(966, 310, 73, 291);
		panelInstalacion.add(panel_CEJ);
		
		//R1
		JPanel panel_R1 = new JPanel();
		panel_R1.setBackground(Color.LIGHT_GRAY);
		panel_R1.setBounds(691, 75, 371, 232);
		panelInstalacion.add(panel_R1);
		
		//EM
		JPanel panel_EM = new JPanel();
		panel_EM.setBackground(Color.LIGHT_GRAY);
		panel_EM.setBounds(814, 6, 210, 70);
		panelInstalacion.add(panel_EM);
		
		//ES
		JPanel panel_ES = new JPanel();
		panel_ES.setBackground(Color.LIGHT_GRAY);
		panel_ES.setBounds(75, 6, 210, 70);
		panelInstalacion.add(panel_ES);
		
		//EV
		JPanel panel_EV = new JPanel();
		panel_EV.setBackground(Color.LIGHT_GRAY);
		panel_EV.setBounds(6, 63, 70, 210);
		panelInstalacion.add(panel_EV);
		
		//R2
		JPanel panel_R2 = new JPanel();
		panel_R2.setBackground(Color.LIGHT_GRAY);
		panel_R2.setBounds(75, 75, 371, 232);
		panelInstalacion.add(panel_R2);
		
		//COK
		JPanel panel_COK = new JPanel();
		panel_COK.setBackground(Color.LIGHT_GRAY);
		panel_COK.setBounds(120, 310, 73, 291);
		panelInstalacion.add(panel_COK);
		
		//CNOK
		JPanel panel_CNOK = new JPanel();
		panel_CNOK.setBackground(Color.LIGHT_GRAY);
		panel_CNOK.setBounds(237, 310, 309, 288);
		panelInstalacion.add(panel_CNOK);
		
		//CT
		JPanel panel_CT = new JPanel();
		panel_CT.setBackground(Color.LIGHT_GRAY);
		panel_CT.setBounds(446, 104, 244, 63);
		panelInstalacion.add(panel_CT);
		
		JPanel panelControlAutomatas = new JPanel();
		panelControlAutomatas.setBackground(Color.LIGHT_GRAY);
		panelControlAutomatas.setBounds(1084, 6, 145, 642);
		panelSimulador.add(panelControlAutomatas);
		panelControlAutomatas.setLayout(null);
		
		JButton btnVaciarConjuntosDefectuosos = new JButton("Vaciar CNOK");
		
		btnVaciarConjuntosDefectuosos.setBounds(10, 537, 122, 38);
		panelControlAutomatas.add(btnVaciarConjuntosDefectuosos);
		
		//Fallo Esclavo1
		JSlider falloEsclavo1 = new JSlider();
		falloEsclavo1.setMaximum(1);
		falloEsclavo1.setBounds(10, 85, 122, 23);
		panelControlAutomatas.add(falloEsclavo1);
		
		//Fallo Esclavo 2
		JSlider falloEsclavo2 = new JSlider();
		falloEsclavo2.setMaximum(1);
		falloEsclavo2.setBounds(10, 183, 122, 23);
		panelControlAutomatas.add(falloEsclavo2);
		
		//Fallo Esclavo 3
		JSlider falloEsclavo3 = new JSlider();
		falloEsclavo3.setMaximum(1);
		falloEsclavo3.setBounds(10, 291, 122, 23);
		panelControlAutomatas.add(falloEsclavo3);
		
		JLabel lblEsclavo = new JLabel("Esclavo 1");
		lblEsclavo.setBounds(44, 60, 62, 14);
		panelControlAutomatas.add(lblEsclavo);
		
		JLabel lblEsclavo_1 = new JLabel("Esclavo 2");
		lblEsclavo_1.setBounds(44, 158, 62, 14);
		panelControlAutomatas.add(lblEsclavo_1);
		
		JLabel lblEsclavo_2 = new JLabel("Esclavo 3");
		lblEsclavo_2.setBounds(44, 266, 62, 14);
		panelControlAutomatas.add(lblEsclavo_2);
		
		JLabel lblOn = new JLabel("ON");
		lblOn.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblOn.setBounds(117, 108, 15, 14);
		panelControlAutomatas.add(lblOn);
		
		JLabel label_5 = new JLabel("ON");
		label_5.setFont(new Font("Tahoma", Font.PLAIN, 9));
		label_5.setBounds(117, 206, 15, 14);
		panelControlAutomatas.add(label_5);
		
		JLabel label_6 = new JLabel("ON");
		label_6.setFont(new Font("Tahoma", Font.PLAIN, 9));
		label_6.setBounds(114, 314, 21, 14);
		panelControlAutomatas.add(label_6);
		
		JLabel lblOff = new JLabel("OFF");
		lblOff.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblOff.setBounds(10, 108, 21, 14);
		panelControlAutomatas.add(lblOff);
		
		JLabel label_12 = new JLabel("OFF");
		label_12.setFont(new Font("Tahoma", Font.PLAIN, 9));
		label_12.setBounds(10, 206, 21, 14);
		panelControlAutomatas.add(label_12);
		
		JLabel label_13 = new JLabel("OFF");
		label_13.setFont(new Font("Tahoma", Font.PLAIN, 9));
		label_13.setBounds(10, 314, 21, 14);
		panelControlAutomatas.add(label_13);
		
		JPanel panelParametros = new JPanel();
		tabbedPane.addTab("Parametros", null, panelParametros, null);
		panelParametros.setLayout(null);
		
		JPanel panelEsclavo1 = new JPanel();
		panelEsclavo1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Esclavo 1", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelEsclavo1.setLayout(null);
		panelEsclavo1.setBackground(new Color(128, 128, 128));
		panelEsclavo1.setBounds(21, 11, 1208, 236);
		panelParametros.add(panelEsclavo1);
		
		JLabel label_20 = new JLabel("s");
		label_20.setBounds(755, 203, 38, 16);
		panelEsclavo1.add(label_20);
		
		JLabel lblTiempoEm = new JLabel("Tiempo EM (t1)");
		lblTiempoEm.setBounds(450, 203, 101, 16);
		panelEsclavo1.add(lblTiempoEm);
		
		txtTiempoEm = new JTextField();
		txtTiempoEm.setBounds(620, 197, 134, 28);
		panelEsclavo1.add(txtTiempoEm);
		txtTiempoEm.setText("3");
		txtTiempoEm.setHorizontalAlignment(SwingConstants.CENTER);
		txtTiempoEm.setColumns(10);
		
		JPanel panelCej = new JPanel();
		panelCej.setBounds(10, 36, 392, 156);
		panelEsclavo1.add(panelCej);
		panelCej.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Cinta de Ejes", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelCej.setLayout(null);
		panelCej.setBackground(UIManager.getColor("Button.background"));
		
		JLabel lblUds = new JLabel("uds");
		lblUds.setBounds(320, 115, 50, 16);
		panelCej.add(lblUds);
		
		JLabel lblMmin = new JLabel("m/s");
		lblMmin.setBounds(320, 75, 50, 16);
		panelCej.add(lblMmin);
		
		JLabel lblM = new JLabel("m");
		lblM.setBounds(320, 35, 50, 16);
		panelCej.add(lblM);
		
		JLabel lblLongitudCej = new JLabel("Longitud CEJ");
		lblLongitudCej.setBounds(30, 35, 101, 16);
		panelCej.add(lblLongitudCej);
		
		txtLongitudCej = new JTextField();
		txtLongitudCej.setBounds(185, 29, 134, 28);
		panelCej.add(txtLongitudCej);
		txtLongitudCej.setText("10");
		txtLongitudCej.setHorizontalAlignment(SwingConstants.CENTER);
		txtLongitudCej.setColumns(10);
		
		txtVelocidadCej = new JTextField();
		txtVelocidadCej.setBounds(185, 69, 134, 28);
		panelCej.add(txtVelocidadCej);
		txtVelocidadCej.setText("20");
		txtVelocidadCej.setHorizontalAlignment(SwingConstants.CENTER);
		txtVelocidadCej.setColumns(10);
		
		JLabel lblVelocidadCej = new JLabel("Velocidad CEJ");
		lblVelocidadCej.setBounds(30, 75, 101, 16);
		panelCej.add(lblVelocidadCej);
		
		JLabel lblCapacidadCej = new JLabel("Capacidad CEJ");
		lblCapacidadCej.setBounds(30, 115, 101, 16);
		panelCej.add(lblCapacidadCej);
		
		txtCapacidadCej = new JTextField();
		txtCapacidadCej.setBounds(185, 109, 134, 28);
		panelCej.add(txtCapacidadCej);
		txtCapacidadCej.setText("50");
		txtCapacidadCej.setHorizontalAlignment(SwingConstants.CENTER);
		txtCapacidadCej.setColumns(10);
		
		JPanel panelCen = new JPanel();
		panelCen.setBounds(412, 36, 392, 156);
		panelEsclavo1.add(panelCen);
		panelCen.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Cinta de Engranajes", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelCen.setBackground(UIManager.getColor("Button.background"));
		panelCen.setLayout(null);
		
		JLabel lblLongitudCen = new JLabel("Longitud CEN");
		lblLongitudCen.setBounds(30, 35, 101, 16);
		panelCen.add(lblLongitudCen);
		
		JLabel lblVelocidadCen = new JLabel("Velocidad CEN");
		lblVelocidadCen.setBounds(30, 75, 101, 16);
		panelCen.add(lblVelocidadCen);
		
		JLabel lblCapacidadCen = new JLabel("Capacidad CEN");
		lblCapacidadCen.setBounds(30, 115, 101, 16);
		panelCen.add(lblCapacidadCen);
		
		txtLongitudCen = new JTextField();
		txtLongitudCen.setHorizontalAlignment(SwingConstants.CENTER);
		txtLongitudCen.setText("10");
		txtLongitudCen.setBounds(185, 29, 134, 28);
		panelCen.add(txtLongitudCen);
		txtLongitudCen.setColumns(10);
		
		txtVelocidadCen = new JTextField();
		txtVelocidadCen.setText("20");
		txtVelocidadCen.setHorizontalAlignment(SwingConstants.CENTER);
		txtVelocidadCen.setColumns(10);
		txtVelocidadCen.setBounds(185, 69, 134, 28);
		panelCen.add(txtVelocidadCen);
		
		txtCapacidadCen = new JTextField();
		txtCapacidadCen.setText("50");
		txtCapacidadCen.setHorizontalAlignment(SwingConstants.CENTER);
		txtCapacidadCen.setColumns(10);
		txtCapacidadCen.setBounds(185, 109, 134, 28);
		panelCen.add(txtCapacidadCen);
		
		JLabel label_17 = new JLabel("m");
		label_17.setBounds(320, 35, 38, 16);
		panelCen.add(label_17);
		
		JLabel label_18 = new JLabel("m/s");
		label_18.setBounds(320, 75, 51, 16);
		panelCen.add(label_18);
		
		JLabel label_19 = new JLabel("uds");
		label_19.setBounds(320, 115, 38, 16);
		panelCen.add(label_19);
		
		JPanel panelRobot1 = new JPanel();
		panelRobot1.setBounds(812, 36, 386, 156);
		panelEsclavo1.add(panelRobot1);
		panelRobot1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Robot 1", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelRobot1.setBackground(UIManager.getColor("Button.background"));
		panelRobot1.setLayout(null);
		
		JLabel lblTRecogidaEnej = new JLabel("T Recogida EN-EJ");
		lblTRecogidaEnej.setBounds(30, 35, 144, 16);
		panelRobot1.add(lblTRecogidaEnej);
		
		JLabel lblTTransporteEnej = new JLabel("T Transporte EN-EJ");
		lblTTransporteEnej.setBounds(30, 75, 144, 16);
		panelRobot1.add(lblTTransporteEnej);
		
		JLabel lblTTransporteCm = new JLabel("T Transporte CM");
		lblTTransporteCm.setBounds(30, 115, 144, 16);
		panelRobot1.add(lblTTransporteCm);
		
		txtTRecogidaEnej = new JTextField();
		txtTRecogidaEnej.setText("5");
		txtTRecogidaEnej.setHorizontalAlignment(SwingConstants.CENTER);
		txtTRecogidaEnej.setColumns(10);
		txtTRecogidaEnej.setBounds(200, 29, 134, 28);
		panelRobot1.add(txtTRecogidaEnej);
		
		txtTTransporteEnej = new JTextField();
		txtTTransporteEnej.setText("5");
		txtTTransporteEnej.setHorizontalAlignment(SwingConstants.CENTER);
		txtTTransporteEnej.setColumns(10);
		txtTTransporteEnej.setBounds(200, 69, 134, 28);
		panelRobot1.add(txtTTransporteEnej);
		
		txtTTransporteCm1 = new JTextField();
		txtTTransporteCm1.setText("5");
		txtTTransporteCm1.setHorizontalAlignment(SwingConstants.CENTER);
		txtTTransporteCm1.setColumns(10);
		txtTTransporteCm1.setBounds(200, 109, 134, 28);
		panelRobot1.add(txtTTransporteCm1);
		
		JLabel lblS = new JLabel("s");
		lblS.setBounds(335, 35, 38, 16);
		panelRobot1.add(lblS);
		
		JLabel label = new JLabel("s");
		label.setBounds(335, 75, 38, 16);
		panelRobot1.add(label);
		
		JLabel label_1 = new JLabel("s");
		label_1.setBounds(335, 115, 38, 16);
		panelRobot1.add(label_1);
		
		JPanel panelEsclavo2 = new JPanel();
		panelEsclavo2.setBorder(new TitledBorder(null, "Esclavo 2", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panelEsclavo2.setBounds(21, 258, 343, 83);
		panelParametros.add(panelEsclavo2);
		panelEsclavo2.setLayout(null);
		panelEsclavo2.setBackground(Color.GRAY);
		
		JLabel lblTiempoEs = new JLabel("Tiempo ES (t2)");
		lblTiempoEs.setBounds(10, 36, 101, 16);
		panelEsclavo2.add(lblTiempoEs);
		
		txtTiempoEs = new JTextField();
		txtTiempoEs.setBounds(180, 30, 134, 28);
		panelEsclavo2.add(txtTiempoEs);
		txtTiempoEs.setText("3");
		txtTiempoEs.setHorizontalAlignment(SwingConstants.CENTER);
		txtTiempoEs.setColumns(10);
		
		JLabel label_21 = new JLabel("s");
		label_21.setBounds(315, 36, 38, 16);
		panelEsclavo2.add(label_21);
		
		JPanel panelMaestro = new JPanel();
		panelMaestro.setBorder(new TitledBorder(null, "Maestro", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panelMaestro.setLayout(null);
		panelMaestro.setBackground(Color.GRAY);
		panelMaestro.setBounds(21, 462, 1208, 188);
		panelParametros.add(panelMaestro);
		
		JPanel panelRobot2 = new JPanel();
		panelRobot2.setBounds(110, 22, 392, 156);
		panelMaestro.add(panelRobot2);
		panelRobot2.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Robot 2", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelRobot2.setBackground(UIManager.getColor("Button.background"));
		panelRobot2.setLayout(null);
		
		JLabel lblTRecogidaCm = new JLabel("T Recogida CM");
		lblTRecogidaCm.setBounds(30, 35, 144, 16);
		panelRobot2.add(lblTRecogidaCm);
		
		JLabel lblTTransporteCm_1 = new JLabel("T Transporte CM");
		lblTTransporteCm_1.setBounds(30, 75, 144, 16);
		panelRobot2.add(lblTTransporteCm_1);
		
		JLabel lblTTransporteCs = new JLabel("T Transporte CS");
		lblTTransporteCs.setBounds(30, 115, 144, 16);
		panelRobot2.add(lblTTransporteCs);
		
		txtTRecogidaCm = new JTextField();
		txtTRecogidaCm.setText("5");
		txtTRecogidaCm.setHorizontalAlignment(SwingConstants.CENTER);
		txtTRecogidaCm.setColumns(10);
		txtTRecogidaCm.setBounds(200, 30, 134, 28);
		panelRobot2.add(txtTRecogidaCm);
		
		txtTTransporteCm2 = new JTextField();
		txtTTransporteCm2.setText("5");
		txtTTransporteCm2.setHorizontalAlignment(SwingConstants.CENTER);
		txtTTransporteCm2.setColumns(10);
		txtTTransporteCm2.setBounds(200, 69, 134, 28);
		panelRobot2.add(txtTTransporteCm2);
		
		txtTTransporteCs = new JTextField();
		txtTTransporteCs.setText("5");
		txtTTransporteCs.setHorizontalAlignment(SwingConstants.CENTER);
		txtTTransporteCs.setColumns(10);
		txtTTransporteCs.setBounds(200, 109, 134, 28);
		panelRobot2.add(txtTTransporteCs);
		
		JLabel label_2 = new JLabel("s");
		label_2.setBounds(335, 35, 38, 16);
		panelRobot2.add(label_2);
		
		JLabel label_3 = new JLabel("s");
		label_3.setBounds(335, 75, 38, 16);
		panelRobot2.add(label_3);
		
		JLabel label_4 = new JLabel("s");
		label_4.setBounds(335, 115, 38, 16);
		panelRobot2.add(label_4);
		
		JPanel panelCt = new JPanel();
		panelCt.setBounds(713, 22, 392, 117);
		panelMaestro.add(panelCt);
		panelCt.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Cinta de Transporte", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelCt.setBackground(UIManager.getColor("Button.background"));
		panelCt.setLayout(null);
		
		JLabel lblLongitudCt = new JLabel("Longitud CT");
		lblLongitudCt.setBounds(30, 35, 101, 16);
		panelCt.add(lblLongitudCt);
		
		JLabel lblVelocidadCt = new JLabel("Velocidad CT");
		lblVelocidadCt.setBounds(30, 75, 101, 16);
		panelCt.add(lblVelocidadCt);
		
		txtLongitudCt = new JTextField();
		txtLongitudCt.setText("10");
		txtLongitudCt.setHorizontalAlignment(SwingConstants.CENTER);
		txtLongitudCt.setColumns(10);
		txtLongitudCt.setBounds(185, 29, 134, 28);
		panelCt.add(txtLongitudCt);
		
		txtVelocidadCt = new JTextField();
		txtVelocidadCt.setText("20");
		txtVelocidadCt.setHorizontalAlignment(SwingConstants.CENTER);
		txtVelocidadCt.setColumns(10);
		txtVelocidadCt.setBounds(185, 69, 134, 28);
		panelCt.add(txtVelocidadCt);
		
		JLabel label_8 = new JLabel("m");
		label_8.setBounds(320, 35, 38, 16);
		panelCt.add(label_8);
		
		JLabel label_9 = new JLabel("m/s");
		label_9.setBounds(320, 75, 51, 16);
		panelCt.add(label_9);
		
		JPanel panelEsclavo3 = new JPanel();
		panelEsclavo3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Esclavo 3", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelEsclavo3.setLayout(null);
		panelEsclavo3.setBackground(Color.GRAY);
		panelEsclavo3.setBounds(374, 258, 855, 193);
		panelParametros.add(panelEsclavo3);
		
		JPanel panelCnok = new JPanel();
		panelCnok.setBounds(24, 30, 392, 117);
		panelEsclavo3.add(panelCnok);
		panelCnok.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Cinta de Conjuntos Defectuosos", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelCnok.setLayout(null);
		panelCnok.setBackground(UIManager.getColor("Button.background"));
		
		JLabel label_7 = new JLabel("m");
		label_7.setBounds(320, 35, 38, 16);
		panelCnok.add(label_7);
		
		JLabel label_14 = new JLabel("m/s");
		label_14.setBounds(320, 75, 51, 16);
		panelCnok.add(label_14);
		
		JLabel lblLongitudCnok = new JLabel("Longitud CNOK");
		lblLongitudCnok.setBounds(30, 35, 111, 16);
		panelCnok.add(lblLongitudCnok);
		
		JLabel lblVelocidadCnok = new JLabel("Velocidad CNOK");
		lblVelocidadCnok.setBounds(30, 75, 111, 16);
		panelCnok.add(lblVelocidadCnok);
		
		txtLongitudCnok = new JTextField();
		txtLongitudCnok.setBounds(185, 29, 134, 28);
		panelCnok.add(txtLongitudCnok);
		txtLongitudCnok.setText("10");
		txtLongitudCnok.setEditable(false);
		txtLongitudCnok.setHorizontalAlignment(SwingConstants.CENTER);
		txtLongitudCnok.setColumns(10);
		
		txtVelocidadCnok = new JTextField();
		txtVelocidadCnok.setBounds(185, 69, 134, 28);
		panelCnok.add(txtVelocidadCnok);
		txtVelocidadCnok.setEditable(false);
		txtVelocidadCnok.setText("9.8");
		txtVelocidadCnok.setHorizontalAlignment(SwingConstants.CENTER);
		txtVelocidadCnok.setColumns(10);
		
		JPanel panelCok = new JPanel();
		panelCok.setBounds(426, 30, 392, 117);
		panelEsclavo3.add(panelCok);
		panelCok.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Cinta de Conjuntos Validos", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelCok.setBackground(UIManager.getColor("Button.background"));
		panelCok.setLayout(null);
		
		JLabel lblLongitudCok = new JLabel("Longitud COK");
		lblLongitudCok.setBounds(30, 35, 111, 16);
		panelCok.add(lblLongitudCok);
		
		JLabel lblVelocidadCok = new JLabel("Velocidad COK");
		lblVelocidadCok.setBounds(30, 75, 111, 16);
		panelCok.add(lblVelocidadCok);
		
		txtLongitudCok = new JTextField();
		txtLongitudCok.setText("10");
		txtLongitudCok.setHorizontalAlignment(SwingConstants.CENTER);
		txtLongitudCok.setColumns(10);
		txtLongitudCok.setBounds(185, 29, 134, 28);
		panelCok.add(txtLongitudCok);
		
		txtVelocidadCok = new JTextField();
		txtVelocidadCok.setText("10");
		txtVelocidadCok.setHorizontalAlignment(SwingConstants.CENTER);
		txtVelocidadCok.setColumns(10);
		txtVelocidadCok.setBounds(185, 69, 134, 28);
		panelCok.add(txtVelocidadCok);
		
		JLabel label_10 = new JLabel("m");
		label_10.setBounds(320, 35, 38, 16);
		panelCok.add(label_10);
		
		JLabel label_11 = new JLabel("m/s");
		label_11.setBounds(320, 75, 38, 16);
		panelCok.add(label_11);
		
		JLabel label_22 = new JLabel("s");
		label_22.setBounds(576, 160, 38, 16);
		panelEsclavo3.add(label_22);
		
		txtTiempoEv = new JTextField();
		txtTiempoEv.setBounds(440, 154, 134, 28);
		panelEsclavo3.add(txtTiempoEv);
		txtTiempoEv.setText("3");
		txtTiempoEv.setHorizontalAlignment(SwingConstants.CENTER);
		txtTiempoEv.setColumns(10);
		
		JLabel lblTiempoEv = new JLabel("Tiempo EV (t3)");
		lblTiempoEv.setBounds(271, 160, 101, 16);
		panelEsclavo3.add(lblTiempoEv);
		
		JLabel lblInfiware = new JLabel("InfiniWare");
		lblInfiware.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblInfiware.setFont(new Font("Papyrus", Font.PLAIN, 50));
		lblInfiware.setBounds(89, 352, 237, 83);
		panelParametros.add(lblInfiware);
		
		JPanel panelInformes = new JPanel();
		tabbedPane.addTab("Informes", null, panelInformes, null);
		panelInformes.setLayout(null);
		
		JPanel panelUltimaFabricacion = new JPanel();
		panelUltimaFabricacion.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Informe de la  Ultima Tanda de Fabricacion", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelUltimaFabricacion.setBackground(UIManager.getColor("Button.background"));
		panelUltimaFabricacion.setBounds(289, 19, 729, 160);
		panelInformes.add(panelUltimaFabricacion);
		panelUltimaFabricacion.setLayout(null);
		
		JLabel lblConjuntosCorrectos = new JLabel("Conjuntos Correctos");
		lblConjuntosCorrectos.setBounds(80, 50, 213, 16);
		panelUltimaFabricacion.add(lblConjuntosCorrectos);
		
		JLabel lblConjuntosDefectuosos = new JLabel("Conjuntos Defectuosos");
		lblConjuntosDefectuosos.setBounds(80, 100, 213, 16);
		panelUltimaFabricacion.add(lblConjuntosDefectuosos);
		
		txtConjuntosOk = new JTextField();
		txtConjuntosOk.setEditable(false);
		txtConjuntosOk.setHorizontalAlignment(SwingConstants.CENTER);
		txtConjuntosOk.setText("10");
		txtConjuntosOk.setBounds(540, 44, 134, 28);
		panelUltimaFabricacion.add(txtConjuntosOk);
		txtConjuntosOk.setColumns(10);
		
		txtConjuntosNok = new JTextField();
		txtConjuntosNok.setEditable(false);
		txtConjuntosNok.setText("1");
		txtConjuntosNok.setHorizontalAlignment(SwingConstants.CENTER);
		txtConjuntosNok.setColumns(10);
		txtConjuntosNok.setBounds(540, 94, 134, 28);
		panelUltimaFabricacion.add(txtConjuntosNok);
		
		JPanel panelFabricacion = new JPanel();
		panelFabricacion.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Informe de la Fabricacion Total", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelFabricacion.setBackground(UIManager.getColor("Button.background"));
		panelFabricacion.setBounds(289, 221, 729, 160);
		panelInformes.add(panelFabricacion);
		panelFabricacion.setLayout(null);
		
		JLabel lblConjuntosDefectuososTotal = new JLabel("Conjuntos Defectuosos");
		lblConjuntosDefectuososTotal.setBounds(80, 100, 213, 16);
		panelFabricacion.add(lblConjuntosDefectuososTotal);
		
		JLabel lblConjuntosCorrectosTotal = new JLabel("Conjuntos Correctos");
		lblConjuntosCorrectosTotal.setBounds(80, 50, 213, 16);
		panelFabricacion.add(lblConjuntosCorrectosTotal);
		
		txtConjuntosOkTotales = new JTextField();
		txtConjuntosOkTotales.setEditable(false);
		txtConjuntosOkTotales.setText("0");
		txtConjuntosOkTotales.setHorizontalAlignment(SwingConstants.CENTER);
		txtConjuntosOkTotales.setColumns(10);
		txtConjuntosOkTotales.setBounds(550, 44, 134, 28);
		panelFabricacion.add(txtConjuntosOkTotales);
		
		txtConjuntosNokTotales = new JTextField();
		txtConjuntosNokTotales.setText("0");
		txtConjuntosNokTotales.setHorizontalAlignment(SwingConstants.CENTER);
		txtConjuntosNokTotales.setEditable(false);
		txtConjuntosNokTotales.setColumns(10);
		txtConjuntosNokTotales.setBounds(550, 94, 134, 28);
		panelFabricacion.add(txtConjuntosNokTotales);
		
		JPanel panelFuncionamiento = new JPanel();
		panelFuncionamiento.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Informe de Funcionamiento del Sistema", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelFuncionamiento.setBackground(UIManager.getColor("Button.background"));
		panelFuncionamiento.setBounds(289, 423, 729, 210);
		panelInformes.add(panelFuncionamiento);
		panelFuncionamiento.setLayout(null);
		
		JLabel lblNumeroTotalDe = new JLabel("Numero Total de Paradas Normales");
		lblNumeroTotalDe.setBounds(80, 50, 285, 16);
		panelFuncionamiento.add(lblNumeroTotalDe);
		
		JLabel lblNumeroTotalDe_1 = new JLabel("Numero Total de Paradas de Emergencia");
		lblNumeroTotalDe_1.setBounds(80, 100, 285, 16);
		panelFuncionamiento.add(lblNumeroTotalDe_1);
		
		JLabel lblNumeroTotalDe_2 = new JLabel("Numero Total de Arranques");
		lblNumeroTotalDe_2.setBounds(80, 150, 213, 16);
		panelFuncionamiento.add(lblNumeroTotalDe_2);
		
		txtParadasNormales = new JTextField();
		txtParadasNormales.setText("0");
		txtParadasNormales.setHorizontalAlignment(SwingConstants.CENTER);
		txtParadasNormales.setEditable(false);
		txtParadasNormales.setColumns(10);
		txtParadasNormales.setBounds(550, 44, 134, 28);
		panelFuncionamiento.add(txtParadasNormales);
		
		txtParadasEmergencia = new JTextField();
		txtParadasEmergencia.setText("0");
		txtParadasEmergencia.setHorizontalAlignment(SwingConstants.CENTER);
		txtParadasEmergencia.setEditable(false);
		txtParadasEmergencia.setColumns(10);
		txtParadasEmergencia.setBounds(550, 94, 134, 28);
		panelFuncionamiento.add(txtParadasEmergencia);
		
		txtArranques = new JTextField();
		txtArranques.setText("0");
		txtArranques.setHorizontalAlignment(SwingConstants.CENTER);
		txtArranques.setEditable(false);
		txtArranques.setColumns(10);
		txtArranques.setBounds(550, 144, 134, 28);
		panelFuncionamiento.add(txtArranques);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		verticalStrut.setBounds(525, 686, 35, 8);
		contentPane.add(verticalStrut);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		verticalStrut_1.setBounds(525, 901, 35, 8);
		contentPane.add(verticalStrut_1);
		
		//TODO
		//Parameter Set
		this.mapaParametros = new HashMap<String, Double>();
		this.updateParameterMap();
		this.mapaInformes = new HashMap<String, Integer>();
		this.cargarInformes("");
		
		
		//AnimationController
		ac = new AnimationController(this.mapaParametros);
		
		ac.getCen().createGUI(panel_CEN, panel_CEN.getWidth(), panel_CEN.getHeight());
		ac.getCej().createGUI(panel_CEJ, panel_CEJ.getWidth(), panel_CEJ.getHeight());
		ac.getCt().createGUI(panel_CT, panel_CT.getWidth(), panel_CT.getHeight());
		ac.getCok().createGUI(panel_COK, panel_COK.getWidth(), panel_COK.getHeight());
		ac.getCnok().createGUI(panel_CNOK, panel_CNOK.getWidth(), panel_CNOK.getHeight());

		ac.getR1().createGUI(panel_R1, panel_R1.getWidth(), panel_R1.getHeight());
		ac.getR2().createGUI(panel_R2, panel_R2.getWidth(), panel_R2.getHeight());
		
		ac.getEv().createGUI(panel_EV, panel_EV.getWidth(), panel_EV.getHeight());
		ac.getEm().createGUI(panel_EM, panel_EM.getWidth(), panel_EM.getHeight());
		ac.getEs().createGUI(panel_ES, panel_ES.getWidth(), panel_ES.getHeight());
		
		if(Desktop.isDesktopSupported()){
		}
		
		//Mouse Listeners 
		//Start
		btnStart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				updateParameterMap(); 							//Load the changes in the parameters
				ac.init(mapaParametros); 						//update the values of the GUI Components
				editableParameters(false);						//lock the edition of parameters
				
				//Starts the system
				Scada.ui.arrancar();
			}
		});
		
		//Stop
		btnStop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//stops the system
				Scada.ui.parada();
				
				editableParameters(true); 	//unlock the edition of paramaters
				updateReportMap();			//updates the reports in the GUI	
			}
		});
		
		//Emergency Stop
		btnEmergencyStop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//ac.emergencyStopAll();
				Scada.ui.emergencia();
				
				logConsole.append("GUI: Activada la parada de emergencia\n");
			}
		});
		
		//Empty 
		btnVaciarConjuntosDefectuosos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ac.getCnok().start(CnokAnimation.EMTPY);
				Scada.ui.limpiarCPD();
				
				logConsole.append("GUI: Vaciado el contenedor de conjuntos no validos\n");
			}
		});
		
		//Manual
		mntmManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					logConsole.append("GUI: Abriendo el manual de usuario\n");
					File file = new File("resources/manual.pdf");
					Desktop.getDesktop().open(file);
				} catch(Exception ex) {
					logConsole.append("GUI: Error al cargar el fichero del manual de usuario");
					ex.printStackTrace();
				}
			}
		});
		
		//Ayuda
		mntmAyuda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					logConsole.append("GUI: Abriendo la ayuda\n");
					File file = new File("doc/index.html");
					Desktop.getDesktop().open(file);
				} catch(Exception ex) {
					logConsole.append("GUI: Error al cargar el fichero de ayuda\n");
					ex.printStackTrace();
				}
			}
		});
		
		//Guardar
		mntmGuardarParametros.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String inputValue = JOptionPane.showInputDialog("Por favor, inserte el nombre del archivo");
				updateReportMap();
				updateParameterMap();
				guardarInformes(inputValue);
				logConsole.append("GUI: Guardando informes y parametros\n");
			}
		});
		
		//Cargar
		mntmCargarParametros.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String inputValue = JOptionPane.showInputDialog("Por favor, inserte el nombre del archivo si desea cargar los parametros");
				cargarInformes(inputValue);
				updateReportMap();
				//updateParameterMap();
				logConsole.append("GUI: Cargando informes y parametros\n");
				
				
			}
		});
		
		//Fallo Automata1
		falloEsclavo1.addMouseListener(new ChangeAdapter(falloEsclavo1,1));
		
		//Fallo Automata2
		falloEsclavo2.addMouseListener(new ChangeAdapter(falloEsclavo2,2));
		
		//Fallo Automata2
		falloEsclavo3.addMouseListener(new ChangeAdapter(falloEsclavo3,3));
		
	}
	
	public void conigurarAutomatas(){
		ConjuntoParametros cp = Gui.deMapaAConjunto(this.mapaParametros);
		
	}
	
    /**
     * Metodo que carga los informes y parametros de un fichero de nombre "nombre"
     * 
     * @param nombre
     */
	public void cargarInformes(String nombre){
		//Informes
		try{
			Informes informes = new Informes();
			Produccion produccion = new Produccion();
			produccion.cargar(informes);
			HashMap<String, Integer> mapa = new HashMap<String, Integer>();
			mapa.put("COK", informes.getFabricacion().getCorrectos());
			mapa.put("CNOK", informes.getFabricacion().getIncorrectos());
			mapa.put("COKTotal", informes.getFabricacion().getCorrectos());
			mapa.put("CNOKTotal", informes.getFabricacion().getIncorrectos());
			mapa.put("PN", informes.getFuncionamiento().getNormales());
			mapa.put("PE", informes.getFuncionamiento().getEmergencia());
			mapa.put("ARR", informes.getFuncionamiento().getArranques());
			setMapaInformes(mapa);
		}catch(Exception e){
			logConsole.append("GUI: Error al cargar informe, informe autogenerado\n");
			Informes  informes = new Informes(new Fabricacion(), new Funcionamiento());
			Produccion produccion = new Produccion();
			produccion.guardar(informes);
			cargarInformes("");
		}
		
		//Parametros
		if(!nombre.equals("")){
			try{
				Configuracion configuracion = new Configuracion();
				ConjuntoParametros parametros = new ConjuntoParametros();
				configuracion.cargar(nombre, parametros);
				System.out.println(parametros.toString());
				Map<String, Double> mapa = Gui.deConjuntoAMapa(parametros);
				setMapaParametros(mapa);
			}catch(Exception e){
				logConsole.append("GUI: Error al cargar parametros, introduzca el nombre de un fichero existente\n");
			}
		}
	}
	
	/**
	 * Lanza el buscador por defecto
     */
    private void onLaunchBrowser(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onLaunchBrowser
        URI uri = null;
        try {
            uri = new URI("almacenamiento");
            if(desktop.isSupported(Desktop.Action.BROWSE))
            	desktop.browse(uri);
            else
            	logConsole.append("GUI: El sistema operativo no soporta el lanzamiento del buscador");
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
        catch(URISyntaxException use) {
            use.printStackTrace();
        }
    }//GEN-LAST:event_onLaunchBrowser

    /**
     * Metodo que guarda los informes y parametros en un fichero de nombre "nombre"
     * 
     * @param nombre
     */
	public void guardarInformes(String nombre) {
		//Informes
		Informes informes = new Informes();
		Produccion produccion = new Produccion();
		HashMap<String, Integer> mapa = (HashMap<String, Integer>) getMapaInformes();
		informes.setFabricacion(new Fabricacion(mapa.get("COKTotal"), mapa.get("CNOKTotal")));
		informes.setFuncionamiento(new Funcionamiento(mapa.get("PN"), mapa.get("PE"), mapa.get("ARR")));
		produccion.guardar(informes);
		
		//Parametros
		ConjuntoParametros parametros = Gui.deMapaAConjunto(getMapaParametros());
		Configuracion configuracion = new Configuracion();
		configuracion.guardar(nombre, parametros);
	}
	
	/**
	 * Permite modificar los valores de los informes y mostrarlos graficamente
	 * @param map Map<String, Integer>
	 */
	public void setMapaInformes(Map<String, Integer> map){
		this.mapaInformes = map;
		this.setGUIReport(mapaInformes);
	}
	
	/**
	 * Permite obtener los valores de los informes
	 * @return Map<String, Integer> mapa de informes
	 */
	public Map<String, Integer> getMapaInformes(){
		return this.mapaInformes;
	}
	
	private void updateReportMap(){
		mapaInformes.put("COK", Integer.valueOf(this.txtConjuntosOk.getText()));
		mapaInformes.put("CNOK", Integer.valueOf(this.txtConjuntosNok.getText()));
		mapaInformes.put("COKTotal", Integer.valueOf(this.txtConjuntosOkTotales.getText()));
		mapaInformes.put("CNOKTotal", Integer.valueOf(this.txtConjuntosNokTotales.getText()));
		mapaInformes.put("PN", Integer.valueOf(this.txtParadasNormales.getText()));
		mapaInformes.put("PE", Integer.valueOf(this.txtParadasEmergencia.getText()));
		mapaInformes.put("ARR", Integer.valueOf(this.txtArranques.getText()));
		
		logConsole.append("GUI: Los informes han sido actualizados\n");
	}
	
	private void setGUIReport(Map<String, Integer> map){
		this.txtConjuntosOk.setText(map.get("COK").toString());
		this.txtConjuntosNok.setText(map.get("CNOK").toString());
		this.txtConjuntosOkTotales.setText(map.get("COKTotal").toString());
		this.txtConjuntosNokTotales.setText(map.get("CNOKTotal").toString());
		this.txtParadasNormales.setText(map.get("PN").toString());
		this.txtParadasEmergencia.setText(map.get("PE").toString());
		this.txtArranques.setText(map.get("ARR").toString());
	}
	
	/**
	 * Permite obtener el mapa con los parametros
	 * @return Map<String, Double> mapa de parametros
	 */
	public Map<String, Double> getMapaParametros(){
		return this.mapaParametros;
	}
	
	/**
	 * Permite modificar el mapa de parametros y mostrar
	 * los valores graficamente
	 * @param map Map<String, Double>
	 */
	public void setMapaParametros(Map<String, Double> map){
		this.mapaParametros = map;
		this.setGUIParameters(mapaParametros);
	}
	
	private void setGUIParameters(Map<String, Double> map){
		//Cintas
		
		this.txtLongitudCen.setText(map.get("CEN_l").toString());
		this.txtVelocidadCen.setText(map.get("CEN_v").toString());
		this.txtCapacidadCen.setText(map.get("CEN_c").toString());

		this.txtLongitudCej.setText(map.get("CEJ_l").toString());
		this.txtVelocidadCej.setText(map.get("CEJ_v").toString());
		this.txtCapacidadCej.setText(map.get("CEJ_c").toString());

		this.txtLongitudCt.setText(map.get("CT_l").toString());
		this.txtVelocidadCt.setText(map.get("CT_v").toString());

		this.txtLongitudCok.setText(map.get("COK_l").toString());
		this.txtVelocidadCok.setText(map.get("COK_v").toString());

		this.txtLongitudCnok.setText(map.get("CNOK_l").toString());
		this.txtVelocidadCnok.setText(map.get("CNOK_v").toString());

		//Estaciones
		this.txtTiempoEm.setText(map.get("EM_t").toString());
		this.txtTiempoEs.setText(map.get("ES_t").toString());
		this.txtTiempoEv.setText(map.get("EV_t").toString());

		//Robot1
		this.txtTRecogidaEnej.setText(map.get("R1_tREn").toString());
		this.txtTTransporteEnej.setText(map.get("R1_tTEn").toString());
		this.txtTTransporteCm1.setText(map.get("R1_tTCM").toString());

		//Robot2
		this.txtTRecogidaCm.setText(map.get("R2_tRCM").toString());
		this.txtTTransporteCm2.setText(map.get("R2_tTCM").toString());
		this.txtTTransporteCs.setText(map.get("R2_tTCS").toString());
	}
	
	private void updateParameterMap(){
		//Cintas
		mapaParametros.put("CEN_l", Double.valueOf(this.txtLongitudCen.getText()));
		mapaParametros.put("CEN_v", Double.valueOf(this.txtVelocidadCen.getText()));
		mapaParametros.put("CEN_c", Double.valueOf(this.txtCapacidadCen.getText()));
		mapaParametros.put("CEJ_l", Double.valueOf(this.txtLongitudCej.getText()));
		mapaParametros.put("CEJ_v", Double.valueOf(this.txtVelocidadCej.getText()));
		mapaParametros.put("CEJ_c", Double.valueOf(this.txtCapacidadCej.getText()));
		mapaParametros.put("CT_l", Double.valueOf(this.txtLongitudCt.getText()));
		mapaParametros.put("CT_v", Double.valueOf(this.txtVelocidadCt.getText()));
		mapaParametros.put("COK_l", Double.valueOf(this.txtLongitudCok.getText()));
		mapaParametros.put("COK_v", Double.valueOf(this.txtVelocidadCok.getText()));
		mapaParametros.put("CNOK_l", Double.valueOf(this.txtLongitudCnok.getText()));
		mapaParametros.put("CNOK_v", Double.valueOf(this.txtVelocidadCnok.getText()));
		
		//Estaciones
		mapaParametros.put("EM_t", Double.valueOf(this.txtTiempoEm.getText()));
		mapaParametros.put("ES_t", Double.valueOf(this.txtTiempoEs.getText()));
		mapaParametros.put("EV_t", Double.valueOf(this.txtTiempoEv.getText()));
		
		//Robot1
		mapaParametros.put("R1_tREn", Double.valueOf(this.txtTRecogidaEnej.getText()));
		mapaParametros.put("R1_tTEn", Double.valueOf(this.txtTTransporteEnej.getText()));
		mapaParametros.put("R1_tTCM", Double.valueOf(this.txtTTransporteCm1.getText()));
		
		//Robot2
		mapaParametros.put("R2_tRCM", Double.valueOf(this.txtTRecogidaCm.getText()));
		mapaParametros.put("R2_tTCM", Double.valueOf(this.txtTTransporteCm2.getText()));
		mapaParametros.put("R2_tTCS", Double.valueOf(this.txtTTransporteCs.getText()));
		
		logConsole.append("GUI: Los parametros han sido actualizados\n");

	}
	
	private void editableParameters(boolean editable){
		txtLongitudCen.setEditable(editable);
		txtVelocidadCen.setEditable(editable);
		txtCapacidadCen.setEditable(editable);
		txtLongitudCej.setEditable(editable);
		txtVelocidadCej.setEditable(editable);
		txtCapacidadCej.setEditable(editable);
		txtLongitudCt.setEditable(editable);
		txtVelocidadCt.setEditable(editable);
		txtLongitudCok.setEditable(editable);
		txtVelocidadCok.setEditable(editable);
		txtTiempoEm.setEditable(editable);
		txtTiempoEs.setEditable(editable);
		txtTiempoEv.setEditable(editable);
		txtTRecogidaEnej.setEditable(editable);
		txtTTransporteEnej.setEditable(editable);
		txtTTransporteCm1.setEditable(editable);
		txtTRecogidaCm.setEditable(editable);
		txtTTransporteCm2.setEditable(editable);
		txtTTransporteCs.setEditable(editable);
		
		if(!editable)
			logConsole.append("GUI: La edicion de parametros de entrada ha sido deshabilitada\n");
		else
			logConsole.append("GUI: La edicion de parametros de entrada ha sido habilitada\n");
	}
	
	public class MenuActionAdapter implements ItemListener{
		public MenuActionAdapter(){
		}
		@Override
		public void itemStateChanged(ItemEvent e) {
			try {
				logConsole.append("GUI: Abriendo el manual de usuario");
			    File file = new File("resources/manual.pdf");
			    if (desktop.isSupported(Desktop.Action.OPEN))
			    	Desktop.getDesktop().open(file);		            
		        else
		        	logConsole.append("GUI: El sistema operativo no soporta la apertura de archivos\n");
			    
			} catch(Exception ex) {
				logConsole.append("GUI: Error al cargar el fichero del manual de usuario");
			    ex.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Clase que hereda de MouseListener que permite realizar las paradas
	 * de los automatas y sus recuperaciones desde el simulador grafico
	 * haciendo click.
	 * 
	 * @author infiniware
	 *
	 */
	public class ChangeAdapter implements MouseListener{
		JSlider falloAutomata;
		int automata;
		/**
		 * Crea un objeto ChangeAdapter
		 * @param falloEsclavo JSlider
		 * @param esclavo int
		 */
		public ChangeAdapter(JSlider falloEsclavo, int esclavo){
			this.falloAutomata = falloEsclavo;
			this.automata = esclavo;
		}
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			switch(automata){
			case(1):
				if(this.isActive()){
					Scada.ui.recuperarFalloEsclavo1();
					logConsole.append("GUI: Activada la recuperacion del fallo del automata esclavo 1\n");
				}else{
					Scada.ui.provocarFalloEsclavo1();
					logConsole.append("GUI: Activado el fallo del automata esclavo 1\n");					
				}
				break;
			case(2):
				if(this.isActive()){
					Scada.ui.recuperarFalloEsclavo2();
					logConsole.append("GUI: Activada la recuperacion del fallo del automata esclavo 2\n");
				}else{
					Scada.ui.provocarFalloEsclavo2();
					logConsole.append("GUI: Activado el fallo del automata esclavo 2\n");					
				}
				break;
			case(3):
				if(this.isActive()){
					Scada.ui.recuperarFalloEsclavo3();
					logConsole.append("GUI: Activada la recuperacion del fallo del automata esclavo 3\n");
				}else{
					Scada.ui.provocarFalloEsclavo3();
					logConsole.append("GUI: Activado el fallo del automata esclavo 3\n");					
				}
				break;
			default:
				logConsole.append("ERROR-GUI: Error en el Fallo de los Automatas.Automata recibido no valido\n");
				System.err.println("Invalid Slave given in ChangeAdapter(JSlider falloEsclavo,int esclavo) - SCADAUserInterface");
				break;
			
			}
		}
		
		private boolean isActive(){
			if(this.falloAutomata.getValue() == 0)
				return false;
			else
				return true;			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			
		}

	}
}
