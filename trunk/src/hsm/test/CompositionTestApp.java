package hsm.test;

import hsm.image.CompositeAdaptor;
import hsm.image.Composition;
import hsm.image.ElementNode;
import hsm.image.ExpressionNode;
import hsm.image.FlickrSource;
import hsm.image.ImageException;
import hsm.image.ImageSource;
import hsm.image.OperationNode;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.JFrame;

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
	
	public CompositionTestApp() throws ImageException
	{
		JFrame frame = new JFrame();
		CompositionFrame compFrame = new CompositionFrame();
		frame.setContentPane(compFrame);
		
		frame.setSize(640, 480);
		frame.setVisible(true);

		System.out.println("initializing...");
		_source = new FlickrSource();
		frame.setVisible(true);
		
		compFrame.setComposition(new Composition(randomNode()));
		
	}
	
	private ExpressionNode randomNode()
	{
		if (Math.random() <= 0.5)
		{
			System.out.println("creating element");
			return new ElementNode(_source.getRandomImage().getImage());
		}
		else
		{
			System.out.println("creating operation");
			// choose random operation
			return new OperationNode(new CompositeAdaptor(), randomNode(), randomNode());
		}
	}
	
	public static void main(String[] argv) throws ImageException
	{
		new CompositionTestApp();
	}
}
