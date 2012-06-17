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
	
	public void init(Map<String, Double> map){
		robot1.init(map.get("R1_tREn"),map.get("R1_tTEn"),map.get("R1_tTCM"));
		robot2.init(map.get("R2_tRCM"),map.get("R2_tTCM"), map.get("R2_tTCS"));
		
		cen.init(AnimationController.convertSlideTime(map.get("CEN_v"),map.get("CEN_l")));
		cej.init(AnimationController.convertSlideTime(map.get("CEJ_v"),map.get("CEJ_l")));
		ct.init(AnimationController.convertSlideTime(map.get("CT_v"),map.get("CT_l")));
		cok.init(AnimationController.convertSlideTime(map.get("COK_v"),map.get("COK_l")));

	}
	
	public static double convertSlideTime(double velocidad, double longitud){
		return (velocidad/longitud);
	}

	public void emergencyStopAll(){
		this.robot1.emergencyStop();
		this.robot2.emergencyStop();
		
		this.cen.emergencyStop();
		this.cej.emergencyStop();
		this.ct.emergencyStop();
		this.cok.emergencyStop();
		
	}
	
	public void startAll(){
		this.robot1.start(robot1.getState());
		this.robot2.start(robot1.getState());
		
		this.cen.start(CenAnimation.STOP);
		this.cej.start(CejAnimation.STOP);
		this.ct.start(CtAnimation.STOP);
		
		this.cok.start(CokAnimation.STOP);
		this.cnok.start(CnokAnimation.MOVE);
	}

	public Robot1Animation getR1() {
		return robot1;
	}

	public Robot2Animation getR2() {
		return robot2;
	}

	public CenAnimation getCen() {
		return cen;
	}

	public CejAnimation getCej() {
		return cej;
	}

	public CtAnimation getCt() {
		return ct;
	}
	
	public CokAnimation getCok() {
		return cok;
	}

	public CnokAnimation getCnok() {
		return cnok;
	}

	public EmAnimation getEm() {
		return em;
	}

	public EmAnimation getEs() {
		return es;
	}

	public EvAnimation getEv() {
		return ev;
	}
	
		
}
