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
	
	private double[] parametros;
	//Parametros 
	/*
	0 r1Recogida;
	1 r1TransEng;
	2 r1TransCM;

	3 r2Recogida;
	4 r2TrasnCM;
	5 r2TransCS;

	6 vCen;
	7 lCen;
	8 vCej;
	9 lCej;
	10 vCt;
	11 lCt;
	12 vCnok;
	13 lCnok;
	*/
	
	public AnimationController(){
		this.parametros = new double[14];
		
		parametros[0] = this.DEFAULT_ROBOT1_RECOGIDA;
		parametros[1] = this.DEFAULT_ROBOT1_TRANS_CEN;
		parametros[2] = this.DEFAULT_ROBOT1_TRANS_EM;

		parametros[3] = this.DEFAULT_ROBOT2_RECOGIDA;
		parametros[4] = this.DEFAULT_ROBOT2_TRANS_CT;
		parametros[5] = this.DEFAULT_ROBOT2_TRANS_EV;

		parametros[6] = DEFAULT_CEN_V;
		parametros[7] = DEFAULT_CEN_L;
		parametros[8] = DEFAULT_CEJ_V;
		parametros[9] = DEFAULT_CEJ_L;
		parametros[10] = DEFAULT_CT_V;
		parametros[11] = DEFAULT_CT_L;
		parametros[12] = DEFAULT_COK_V;
		parametros[13] = DEFAULT_COK_L;
	}
	
	
	public void init(){
		robot1 = new Robot1Animation(this.parametros[0],this.parametros[1], this.parametros[2]);
		robot2 = new Robot2Animation(this.parametros[3], this.parametros[4], this.parametros[5]);
		
		cen = new CenAnimation(AnimationController.convertSlideTime(parametros[6], parametros[7]));
		cej = new CejAnimation(AnimationController.convertSlideTime(parametros[8], parametros[9]));
		ct = new CtAnimation(AnimationController.convertSlideTime(parametros[10], parametros[11]));
		cok = new CokAnimation(AnimationController.convertSlideTime(parametros[12], parametros[13]));
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
	
	public void setParametros(double[] parameters){
		this.parametros = parameters;
		this.init();
	}
	public double[] getParametros(){
		return this.parametros;
	}
	
	public Robot1Animation getRobot1() {
		return robot1;
	}

	public Robot2Animation getRobot2() {
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
