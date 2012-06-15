package infiniware.scada.ui.gui.view.animation;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import infiniware.scada.ui.gui.view.animation.Robot1Animation.ALR;
import infiniware.scada.ui.gui.view.animation.Robot1Animation.Robot1;

public interface Animation {

	public void init();
	
	public void createGUI(JPanel parentPanel, int with, int height);

	public void start(int state); 
        
    public int getState();
	
	public void emergencyStop();

}
