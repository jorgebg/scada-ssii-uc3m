package infiniware.scada.ui.gui.view.animation;

import infiniware.automatas.Automata;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AnimationController {

	private final double DEFAULT_ROBOT1_RECOGIDA = 5.0;
	private final double DEFAULT_ROBOT1_TRANS_CEN = 5.0;
	private final double DEFAULT_ROBOT1_TRANS_EM = 5.0;
	
	private final double DEFAULT_ROBOT2_RECOGIDA = 5.0;
	private final double DEFAULT_ROBOT2_TRANS_CT = 5.0;
	private final double DEFAULT_ROBOT2_TRANS_EV = 5.0;
	
	private final double DEFAULT_CEN_L = 10;
	private final double DEFAULT_CEJ_L = 10;
	private final double DEFAULT_CT_L = 10;
	private final double DEFAULT_COK_L = 10;
	
	private final double DEFAULT_CEN_V = 20;
	private final double DEFAULT_CEJ_V = 20;
	private final double DEFAULT_CT_V = 20;
	private final double DEFAULT_COK_V = 20;
	
	private Robot1Animation robot1;
	private Robot2Animation robot2;
	
	private CenAnimation cen;
	private CejAnimation cej;
	private CtAnimation ct;
	private CokAnimation cok;
	private CnokAnimation cnok;
	
	private EmAnimation em;
	private EmAnimation es;
	private EvAnimation ev;
	
	/**
	 * Crea un objeto de tipo AnimationController inicializando 
	 * todos los componentes necesarios para la simulacion grafica
	 * desde el mapa que se recibe como parametro
	 * Inicializa la carga de imagenes
	 * @param map - mapa de parametros
	 */
	public AnimationController(Map<String, Double> map){
		robot1 = new Robot1Animation(map.get("R1_tREn"),map.get("R1_tTEn"), map.get("R1_tTCM"));
		robot2 = new Robot2Animation(map.get("R2_tRCM"), map.get("R2_tTCM"), map.get("R2_tTCS"));
		
		cen = new CenAnimation(AnimationController.convertSlideTime(map.get("CEN_v"),map.get("CEN_l")));
		cej = new CejAnimation(AnimationController.convertSlideTime(map.get("CEJ_v"),map.get("CEJ_l")));
		ct = new CtAnimation(AnimationController.convertSlideTime(map.get("CT_v"), map.get("CT_l")));
		cok = new CokAnimation(AnimationController.convertSlideTime(map.get("COK_v"),map.get("COK_l")));
		cnok = new CnokAnimation();
		
		em = new EmAnimation();
		es = new EmAnimation();
		ev = new EvAnimation();
		
		robot1.init();
		robot2.init();
		
		cen.init();
		cej.init();
		ct.init();
		cok.init();
		cnok.init();
	}
	
	/**
	 * Actualiza los tiempos en los componentes del simulador grafico 
	 * por medio del mapa que se pasa como parametro
	 * @param map - mapa de parametros
	 */
	public void init(Map<String, Double> map){
		robot1.init(map.get("R1_tREn"),map.get("R1_tTEn"),map.get("R1_tTCM"));
		robot2.init(map.get("R2_tRCM"),map.get("R2_tTCM"), map.get("R2_tTCS"));
		
		cen.init(AnimationController.convertSlideTime(map.get("CEN_v"),map.get("CEN_l")));
		cej.init(AnimationController.convertSlideTime(map.get("CEJ_v"),map.get("CEJ_l")));
		ct.init(AnimationController.convertSlideTime(map.get("CT_v"),map.get("CT_l")));
		cok.init(AnimationController.convertSlideTime(map.get("COK_v"),map.get("COK_l")));

	}
	
	/**
	 * Permite calcular el tiempo que tarda una cinta en realizar un ciclo completo
	 * @param velocidad
	 * @param longitud
	 * @return tiempo que tarda la cita en reccorrer un ciclo completo
	 */
	public static double convertSlideTime(double velocidad, double longitud){
		return (velocidad/longitud);
	}

	/**
	 * Permite parar todos los elementos del simulador grafico
	 * por medio de una parada de emergencia
	 */
	public void emergencyStopAll(){
		this.robot1.emergencyStop();
		this.robot2.emergencyStop();
		
		this.cen.emergencyStop();
		this.cej.emergencyStop();
		this.ct.emergencyStop();
		this.cok.emergencyStop();
		
	}
	
	/**
	 * Permite arrancar todos los elemetos del simulad grafico 
	 * de forma simultanea
	 */
	public void startAll(){
		this.robot1.start();
		this.robot2.start();
		
		this.cen.start();
		this.cej.start();
		this.ct.start();
		
		this.cok.start();
	}

	/**
	 * Devuelve un objeto Robot1Animation
	 * @return robot1
	 */
	public Robot1Animation getR1() {
		return robot1;
	}

	/**
	 * Devuelve un objeto Robot2Animation
	 * @return robot2
	 */
	public Robot2Animation getR2() {
		return robot2;
	}

	/**
	 * Devuelve un objeto CenAnimation
	 * @return cen - cinta de engranajes
	 */
	public CenAnimation getCen() {
		return cen;
	}

	/**
	 * Devuelve un objeto CejAnimation
	 * @return cej - cinta de ejes
	 */
	public CejAnimation getCej() {
		return cej;
	}

	/**
	 * Devuelve un objeto CtAnimation
	 * @return ct - cinta de transporte
	 */
	public CtAnimation getCt() {
		return ct;
	}
	
	/**
	 * Devuelve un objeto CokAnimation
	 * @return cok - cinta de conjuntos validos
	 */
	public CokAnimation getCok() {
		return cok;
	}

	/**
	 * Devuelve un objeto CnokAnimation
	 * @return cnok - cinta de conjuntos no validos
	 */
	public CnokAnimation getCnok() {
		return cnok;
	}

	/**
	 * Devuelve un objeto EmAnimation
	 * @return em - estacion de montaje
	 */
	public EmAnimation getEm() {
		return em;
	}
	
	/**
	 * Devuelve un objeto EmAnimation
	 * @return es - estacion de soldadura
	 */
	public EmAnimation getEs() {
		return es;
	}

	/**
	 * Devuelve un objeto EvAnimation
	 * @return ev - estacion de validacion
	 */
	public EvAnimation getEv() {
		return ev;
	}
	
		
}
