package infiniware.scada.ui.gui.view;

import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.ImageIcon;


/**
 * Esta clase carga las imagenes necesarias en memoria
 * para su uso posterior.
 * Tambien permite realizar conversiones para el calculo de 
 * la velocidad de los componentes graficos
 * @author infiniware
 */
public class ImgLoader {

	
	/**
	 * Calcula la velocidad que necesitan los temporizadores 
	 * teniendo en cuenta el tiempo necesario para realizar una 
	 * accion y el numero de frames
	 *  
	 * @param time - tiempo total requerido
	 * @param frames - numero total de frames
	 * @return velocidad entre frames
	 */
	public static int calculateSpeed(double time, int frames){
		return (int) Math.round(time/frames * 1000);
	}
	
	/**
	 * Carga de forma dinamica imagenes desde un directorio especifico
	 * 
	 * @param dir - direccion de las imagenes
	 * @param extension - extension de la imagen
	 * @param imageNum - numero de imagen
	 * @param max_size - tamanyo maximo
	 * @return un objeto de tipo ImageIcon preparado para ser guardado
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
