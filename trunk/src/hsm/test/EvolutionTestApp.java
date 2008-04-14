package hsm.test;

import hsm.evo.Organism;
import hsm.image.ImageException;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * @author bjmoore
 * Application for testing composition trees
 * - generating composition trees, evaluating, etc
 */
public class EvolutionTestApp {
	private class OrganismFrame extends JComponent implements MouseListener
	{
		private static final long serialVersionUID = 1L;
		Organism _organism;
		
		public OrganismFrame()
		{
			_organism = null;
			
			addMouseListener(this);
		}
		
		public void setOrganism(Organism org)
		{
			
			_organism = org;
			_organism.printGenome();
			repaint();
		}
		
		public void paint(Graphics g)
		{
			Graphics2D g2d = (Graphics2D)g;
			
			super.paint(g);
			
			if (_organism != null)
			{
				g2d.drawImage(_organism.getComposition().getImage(), 0, 0, this);
			}
		}

		public void mouseClicked(MouseEvent e) {
			System.out.println("Mutating organism...");
			if (_organism != null)
			{
				setOrganism(_organism.mutatedCopy());
			}
		}

		public void mouseEntered(MouseEvent e) {}

		public void mouseExited(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {}

		public void mouseReleased(MouseEvent e) {}

	}
	
	public EvolutionTestApp() throws ImageException
	{
		JFrame frame = new JFrame();
		OrganismFrame compFrame = new OrganismFrame();
		frame.add(compFrame);
		
		frame.setSize(640, 480);
		frame.setVisible(true);

		System.out.println("initializing...");
		frame.setVisible(true);
		
		compFrame.setOrganism(Organism.randomOrganism());
		System.out.println("Complete");
	}
	
	
	
	public static void main(String[] argv) throws ImageException
	{
		new EvolutionTestApp();
	}
}
