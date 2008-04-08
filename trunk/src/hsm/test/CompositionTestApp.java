package hsm.test;

import hsm.image.BufferedImageOpAdaptor;
import hsm.image.CompositeAdaptor;
import hsm.image.Composition;
import hsm.image.ElementNode;
import hsm.image.ExpressionNode;
import hsm.image.FlickrSource;
import hsm.image.ImageException;
import hsm.image.ImageSource;
import hsm.image.OperationNode;
import hsm.tools.Extraktor;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

import javax.swing.JComponent;
import javax.swing.JFrame;

import com.jhlabs.image.OpacityFilter;

/**
 * @author bjmoore
 * Application for testing composition trees
 * - generating composition trees, evaluating, etc
 */
public class CompositionTestApp {
	private class CompositionFrame extends JComponent
	{
		private static final long serialVersionUID = 1L;
		Composition _composition;
		
		public CompositionFrame()
		{
			_composition = null;
		}
		
		public void setComposition(Composition comp)
		{
			_composition = comp;
			repaint();
		}
		
		public void paint(Graphics g)
		{
			Graphics2D g2d = (Graphics2D)g;
			
			super.paint(g);
			
			if (_composition != null)
			{
				g2d.drawImage(_composition.getImage(), 0, 0, this);
			}
		}
	}
	
	private ImageSource _source;
	private Extraktor _extract;
	
	public CompositionTestApp() throws ImageException
	{
		JFrame frame = new JFrame();
		CompositionFrame compFrame = new CompositionFrame();
		frame.setContentPane(compFrame);
		
		frame.setSize(640, 480);
		frame.setVisible(true);

		System.out.println("initializing...");
		_source = new FlickrSource();
		_extract = new Extraktor();
		frame.setVisible(true);
		
		compFrame.setComposition(new Composition(randomNode(3)));
		System.out.println("Complete");
	}
	
	private ExpressionNode randomNode(int maxDepth)
	{
		double r = Math.random();
		if (r > 0.99 || maxDepth == 0)
		{
			System.out.println("creating element");
			return new ElementNode(_extract.getExtract(_source.getRandomImage().getImage()));
		}
		else if (r > 0.5)
		{
			Composite comp;
			
			double r2 = Math.random();
			
			if (r2 > 0.6)
			{
				comp = AlphaComposite.DstOver;
			}
			else
			{
				comp = AlphaComposite.Xor;
			}
			
			System.out.println("creating composite operation");
			// choose random operation
			return new OperationNode(new CompositeAdaptor(comp), randomNode(maxDepth-1), randomNode(maxDepth-1));
		}
		else if (r > 0.2)
		{
			System.out.println("creating opacity operation");
			// choose random operation
			return new OperationNode(new BufferedImageOpAdaptor(new OpacityFilter((int)Math.round(Math.random()*0.8*256))), randomNode(maxDepth-1));
		}
		else
		{
			AffineTransform af;
			double r2 = Math.random();
			if (r2 > 1.0)
			{
				af = AffineTransform.getRotateInstance(Math.random()*2.0*Math.PI);
				System.out.println("creating rotate operation");
			}
			else if (r2 > 0.5)
			{
				af = AffineTransform.getTranslateInstance(Math.random()*500, Math.random()*500);
				System.out.println("creating translate operation");
			}
			else
			{
				System.out.println("creating scale operation");
				af = AffineTransform.getScaleInstance(Math.random()*1.5 + 0.5, Math.random()*1.5 + 0.5);
			}
			
			System.out.println(af.toString());
			
			
			// choose random operation
			return new OperationNode(new BufferedImageOpAdaptor(new AffineTransformOp(af, AffineTransformOp.TYPE_BILINEAR)), randomNode(maxDepth-1));
		}
	}
	
	public static void main(String[] argv) throws ImageException
	{
		new CompositionTestApp();
	}
}
