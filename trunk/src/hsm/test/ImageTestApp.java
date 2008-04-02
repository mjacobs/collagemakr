package hsm.test;

import hsm.image.FlickrSource;
import hsm.image.ImageException;
import hsm.image.ImageSource;
import hsm.tools.Extraktor;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.Timer;

public class ImageTestApp {

	private class ImageFrame extends JFrame
	{
		private static final long serialVersionUID = 1L;
		BufferedImage _image;
		
		public ImageFrame()
		{
			setSize(640, 480);
		}
		
		public void setImage(BufferedImage img)
		{
			_image = img;
			repaint();
		}
		
		public void paint(Graphics g)
		{
			Graphics2D g2d = (Graphics2D)g;
			
			super.paint(g);
			
			if (_image != null)
			{
				g2d.drawImage(_image, 0, 0, this);
			}
		}
	}
	
	private class ChangeImagePerformer implements ActionListener
	{
		ImageFrame _frame;
		ImageSource _src;
		Extraktor _ex;
		
		public ChangeImagePerformer(ImageFrame inFrame)
		{
			_ex = new Extraktor();
			_frame = inFrame;
			try {
				_src = new FlickrSource();
			} catch (ImageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void actionPerformed(ActionEvent evt) 
		{
    	 	_frame.setImage(_ex.getExtract(_src.getRandomImage().getImage()));
		}
		
	}
	
	public ImageTestApp() throws ImageException, InterruptedException
	{
		ImageFrame frame = new ImageFrame();
		
		frame.setVisible(true);
		
		int delay = 1000; // milliseconds
		ActionListener taskPerformer = new ChangeImagePerformer(frame);
		new Timer(delay, taskPerformer).start();
	}
	
	/**
	 * @param args
	 * @throws ImageException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws ImageException, InterruptedException {
		
		new ImageTestApp();
	}

}
