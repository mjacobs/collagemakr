package hsm.test;

import hsm.evo.Composition;
import hsm.image.ImageException;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;

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
			_composition.printTree();
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
	
	public CompositionTestApp() throws ImageException
	{
		Composition comp = Composition.randomComposition();
		
		JFrame frame = new JFrame();
		JFrame treeFrame = new JFrame();
		
		CompositionFrame compFrame = new CompositionFrame();
		frame.setContentPane(compFrame);
		
		JTree compTree = new JTree(comp.getRoot().generateJTreeNode());
		treeFrame.setContentPane(new JScrollPane(compTree));
		
		frame.setSize(640, 480);
		frame.setVisible(true);

		System.out.println("initializing...");
		frame.setVisible(true);
		treeFrame.setVisible(true);
		
		compFrame.setComposition(comp);
		System.out.println("Complete");
	}
	
	
	
	public static void main(String[] argv) throws ImageException
	{
		new CompositionTestApp();
	}
}
