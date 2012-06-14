package infiniware.scada.ui.gui.view.animation;

public class AnimationController {

	private final double DEFAULT_ROBOT1_RECOGIDA = 5.0;
	private final double DEFAULT_ROBOT1_TRANS_CEN = 5.0;
	private final double DEFAULT_ROBOT1_TRANS_EM = 5.0;
	private final int DEFAULT_ROBOT1_STATE = Robot1Animation.CEN2EM;
	
	private final double DEFAULT_ROBOT2_RECOGIDA = 5.0;
	private final double DEFAULT_ROBOT2_TRANS_CT = 5.0;
	private final double DEFAULT_ROBOT2_TRANS_EV = 5.0;
	private final int DEFAULT_ROBOT2_STATE = Robot2Animation.CT2ES;
	
	private final double DEFAULT_CEN = 0.5;
	private final double DEFAULT_CEJ = 0.5;
	private final double DEFAULT_CT = 0.5;
	private final double DEFAULT_COK = 0.5;
	
	private Robot1Animation robot1;
	private Robot2Animation robot2;
	
	private CenAnimation cen;
	private CejAnimation cej;
	private CtAnimation ct;
	
	private CokAnimation cok;
	private CnokAnimation cnok;
	
	public AnimationController(){
		robot1 = new Robot1Animation(this.DEFAULT_ROBOT1_RECOGIDA,this.DEFAULT_ROBOT1_TRANS_CEN, this.DEFAULT_ROBOT1_TRANS_EM, this.DEFAULT_ROBOT1_STATE);
		robot2 = new Robot2Animation(this.DEFAULT_ROBOT2_RECOGIDA,this.DEFAULT_ROBOT2_TRANS_CT, this.DEFAULT_ROBOT2_TRANS_EV, this.DEFAULT_ROBOT2_STATE);
		
		cen = new CenAnimation(this.DEFAULT_CEN);
		cej = new CejAnimation(this.DEFAULT_CEJ);
		ct = new CtAnimation(this.DEFAULT_CT);
		cok = new CokAnimation(this.DEFAULT_COK);
		cnok = new CnokAnimation();
	}
	
	public void initAll(){
		robot1.init();
		robot2.init();
		
		cen.init();
		cej.init();
		ct.init();
		cok.init();
		cnok.init();
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
		this.robot1.start();
		this.robot2.start();
		
		this.cen.start();
		this.cej.start();
		this.ct.start();
		
		this.cok.start();
		this.cnok.start();
		
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
		
}
