package hsm.test;

import hsm.evo.RotateOperation;
import hsm.image.*;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * @author bjmoore
 * Testing application for ImageOperation testing
 * - testing individual operations
 */
public class OperationTestApp {
	
	private class LayerFrame extends JComponent
	{
		private static final long serialVersionUID = 1L;
		LayerImage _image;
		
		public LayerFrame()
		{

		}
		
		public void setImage(LayerImage img)
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
				g2d.drawImage(_image.getImage(), 
							  (int)Math.round(_image.getLocation().getX()), 
							  (int)Math.round(_image.getLocation().getY()), this);
				
				g2d.setColor(Color.GREEN);
				g2d.draw(_image.getBounds());
			}
		}
	}
	
	private class AnimationPerformer implements ActionListener
	{
		LayerFrame _frame;
		LayerImage _img1, _img2;
		float _ang = 0.0f;
		RenderingHints _hints;
		
		public AnimationPerformer(LayerFrame inFrame)
		{
			_frame = inFrame;
			try {
				FlickrSource src = new FlickrSource();
				_img1 = new LayerImage(src.getRandomImage().getImage(),new Point2D.Double(100, 100));
				_img2 = new LayerImage(src.getRandomImage().getImage());
			} catch (ImageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			_hints = new RenderingHints(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			_hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		}
		
		public void actionPerformed(ActionEvent evt) 
		{
//			AffineTransform af = AffineTransform.getRotateInstance(_ang, 320, 240);
//			
//			
//			ImageOperation op1 = new BufferedImageOpAdaptor(
//											new AffineTransformOp(af, _hints));
			
			HashMap<String, Double> props = new HashMap<String, Double>();
			props.put("angle", (double)_ang);
			props.put("x", 320.0);
			props.put("y", 240.0);
			
			ImageOperation op1 = new RotateOperation().initWithParameters(props);
			
			ImageOperation op2 = new CompositeAdaptor(AlphaComposite.Xor);
			
			_frame.setImage(op2.perform(op1.perform(_img1), _img2));
			
			_ang += 0.01f;
		}
		
	}
	
	public OperationTestApp() throws ImageException, InterruptedException
	{
		JFrame frame = new JFrame();
		LayerFrame layerFrame = new LayerFrame();
		frame.setContentPane(layerFrame);
		
		frame.setVisible(true);
		frame.setSize(640, 480);
		
		int delay = 100; // milliseconds
		ActionListener taskPerformer = new AnimationPerformer(layerFrame);
		new Timer(delay, taskPerformer).start();
	}
	
	/**
	 * @param args
	 * @throws ImageException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws ImageException, InterruptedException {
		
		new OperationTestApp();
	}

}
