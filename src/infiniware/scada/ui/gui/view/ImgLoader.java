package infiniware.scada.ui.gui.view;

import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.ImageIcon;


/**
 * The class loads images in memory for later usage
 * @author sohrab
 */
public class ImgLoader {

	
	/**
	 * Calculates the speed needed for timers taking into account
	 * the time requiered to perform the operation and the number
	 * of available frames.
	 *  
	 * @param time
	 * @param frames
	 * @return the speed between frames
	 */
	public static int calculateSpeed(double time, int frames){
		return (int) Math.round(time/frames * 1000);
	}
	
	/**
	 * Loads a dynamic image from the specified directory 
	 * 
	 * @param dir
	 * @param extension
	 * @param imageNum
	 * @param max_size
	 * @return and ImageIcon to be stored in an array
	 */
	@SuppressWarnings("unused")
	public static ImageIcon loadImage(String dir, String extension, int imageNum, int max_size){
		
		String path = dir + extension + imageNum + ".jpg";
		int count = 0;

		BufferedInputStream imgStream;
		try {
			imgStream = new BufferedInputStream(
					new FileInputStream(path));
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't find stream from file: " + path);
			return null;
		} 

		if (imgStream != null) {
			byte buf[] = new byte[max_size];
			try {
				count = imgStream.read(buf);
				imgStream.close();
			} catch (java.io.IOException ioe) {
				System.err.println("Couldn't read stream from file: " + path);
				return null;
			}

			if (count <= 0) {
				System.err.println("Empty file: " + path);
				return null;
			}
			return new ImageIcon(Toolkit.getDefaultToolkit().createImage(buf));
		
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
}
