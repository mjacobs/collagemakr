package hsm.test;

import hsm.evo.LinearMotionBlurOperation;
import hsm.evo.PosterizeOperation;
import hsm.evo.RotateOperation;
import hsm.image.*;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
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
		
		public BufferedImage getCheckPattern(int size)
		{
			BufferedImage bi = new BufferedImage(size*2, size*2, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = bi.createGraphics();
			
			g2d.setColor(Color.white);
			g2d.fillRect(0, 0, size*2, size*2);
			g2d.setColor(Color.gray);
			g2d.fillRect(0, 0, size, size);
			g2d.fillRect(size, size, size, size);
			
			return bi;
		}
		
		public void paint(Graphics g)
		{
			Graphics2D g2d = (Graphics2D)g;
			
			super.paint(g);
			BufferedImage check = getCheckPattern(10);
			g2d.setPaint(new TexturePaint(check, new Rectangle(0, 0, check.getWidth(), check.getHeight())));
			//g2d.setColor(Color.WHITE);
			g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
			
			if (_image != null)
			{
				g2d.drawImage(_image.getFlattenedImage(), 
							  0,0, this);
				
				g2d.setColor(Color.GREEN);
				g2d.draw(_image.getBounds());
			}
		}
	}
	
	private class AnimationPerformer implements ActionListener
	{
		LayerFrame _frame;
		LayerImage _img1, _img2;
		float _ang = 1.0f;
		int _step = 0;
		RenderingHints _hints;
		
		public AnimationPerformer(LayerFrame inFrame)
		{
			_frame = inFrame;
			
			DirectorySource src = new DirectorySource("data/samples");
			_img1 = new LayerImage(src.getRandomImage().getImage(),new Point2D.Double(100, 100));
			_img2 = new LayerImage(src.getRandomImage().getImage());
		
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
			props.put("levels", 1.0);
			
			ImageOperation op1 = new PosterizeOperation().initWithParameters(props);
			
			ImageOperation op2 = new CompositeAdaptor(AlphaComposite.SrcOver);
			
			_frame.setImage(op2.perform(op1.perform(_img1), _img2));
			
			//_ang += 0.01f;
			_step++;
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
