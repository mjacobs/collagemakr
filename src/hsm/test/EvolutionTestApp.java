package hsm.test;

import hsm.evo.Organism;
import hsm.image.ImageException;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

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
		Color _gridColor;
		
		public OrganismFrame(Color col)
		{
			_organism = null;
			_gridColor = col;
			
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
		
		public BufferedImage getCheckPattern(int size)
		{
			BufferedImage bi = new BufferedImage(size*2, size*2, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = bi.createGraphics();
			
			g2d.setColor(Color.white);
			g2d.fillRect(0, 0, size*2, size*2);
			g2d.setColor(_gridColor);
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
					_frame2.setOrganism(null);
					
					_frame2.setOrganism(Organism.randomOrganism());
				}
			}
		}

		public void keyReleased(KeyEvent arg0) {}

		public void keyTyped(KeyEvent arg0) {}
		
	}
	
	public EvolutionTestApp() throws ImageException
	{
		JFrame frame = new JFrame();
		int size = 500;
		OrganismFrame compFrame = new OrganismFrame(new Color(255, 220, 220));
		OrganismFrame compFrame2 = new OrganismFrame(new Color(220, 220, 255));
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
