package hsm.test;

import hsm.evo.Organism;
import hsm.image.ImageException;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
		
		public Organism getOrganism()
		{
			return _organism;
		}
		
		public void setOrganism(Organism org)
		{
			
			_organism = org;
			//_organism.printGenome();
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
	
	private class MatingListener implements KeyListener
	{
		OrganismFrame _frame1, _frame2;
		
		public MatingListener(OrganismFrame f1, OrganismFrame f2)
		{
			_frame1 = f1;
			_frame2 = f2;
		}

		public void keyPressed(KeyEvent arg0) {
			System.out.println(arg0);
			if (arg0.getKeyCode() == KeyEvent.VK_SPACE)
			{
				Organism frankie, johnny;
				
				frankie = _frame1.getOrganism();
				johnny = _frame2.getOrganism();
				
				if (frankie != null && johnny != null)
				{
					Organism franny = frankie.matedCopy(johnny);
					
					frankie.printGenome();
					johnny.printGenome();
					System.out.println("If they mated: ");
					franny.printGenome();
					
					_frame1.setOrganism(franny);
					_frame2.setOrganism(Organism.randomOrganism());
				}
			}
		}

		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public EvolutionTestApp() throws ImageException
	{
		JFrame frame = new JFrame();
		int size = 500;
		OrganismFrame compFrame = new OrganismFrame();
		OrganismFrame compFrame2 = new OrganismFrame();
		frame.add(compFrame);
		frame.add(compFrame2);
		frame.setLayout(null);
		frame.addKeyListener(new MatingListener(compFrame, compFrame2));
		
		compFrame.setBounds(0, 0, size, size);
		compFrame2.setBounds(size, 0, size, size);
		
		frame.setSize(size*2, size);
		frame.setVisible(true);

		System.out.println("initializing...");
		frame.setVisible(true);
		
		compFrame.setOrganism(Organism.randomOrganism());
		compFrame2.setOrganism(Organism.randomOrganism());
		System.out.println("Complete");
	}
	
	
	
	public static void main(String[] argv) throws ImageException
	{
		new EvolutionTestApp();
	}
}
