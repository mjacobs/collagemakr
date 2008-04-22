package hsm.gui;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import hsm.evo.Organism;
import hsm.evo.Population;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ThumbsPanel extends JPanel
{
	private static final int THUMB_W = 100;
	private static final int THUMB_H = 100;
	private ThumbLabel in;
	private LinkedList<Organism> orgs;
	private JPanel largeDisplay;
	
	public ThumbsPanel(Population p, JPanel largeDisplay)
	{
		super(new GridLayout(1, p.getSize()));
		orgs = new LinkedList<Organism>(Arrays.asList(p.getOrganisms()));
		this.largeDisplay = largeDisplay;
		addOrganisms();
	}

	private void addOrganisms()
	{
		ThumbDragListener tdl = new ThumbDragListener();
		for (int i = 0; i < orgs.size(); i++)
		{
			Component tl = new ThumbLabel(orgs.get(i));
			tl.addMouseListener(tdl);
			tl.addMouseMotionListener(tdl);
			this.add(tl);
		}
	}

	public void reorder(ThumbLabel end, ThumbLabel start)
	{
		if (orgs.indexOf(start.getOrganism()) > orgs.indexOf(end.getOrganism()))
		{
			Organism o = orgs.remove(orgs.indexOf(start.getOrganism()));
			orgs.add(orgs.indexOf(end.getOrganism()), o);
		}
		else
		{
			orgs.add(orgs.indexOf(end.getOrganism())+1, start.getOrganism());
			orgs.remove(orgs.indexOf(start.getOrganism()));
		}
		
		removeAll();
		addOrganisms();
		revalidate();
	}
	
	private BufferedImage makeThumb(BufferedImage b, int thumbWidth, int thumbHeight)
	{
	    Image image = b;
	    	    
	    double thumbRatio = (double)thumbWidth / (double)thumbHeight;
	    int imageWidth = image.getWidth(null);
	    int imageHeight = image.getHeight(null);
	    double imageRatio = (double)imageWidth / (double)imageHeight;
	    if (thumbRatio < imageRatio) {
	      thumbHeight = (int)(thumbWidth / imageRatio);
	    } else {
	      thumbWidth = (int)(thumbHeight * imageRatio);
	    }
	    // draw original image to thumbnail image object and
	    // scale it to the new size on-the-fly
	    BufferedImage thumbImage = new BufferedImage(thumbWidth, 
	      thumbHeight, BufferedImage.TYPE_INT_RGB);
	    Graphics2D graphics2D = thumbImage.createGraphics();
	    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	      RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);
	    return thumbImage;
	}
	
	private class ThumbLabel extends JLabel
	{
		private Organism o;
		
		public ThumbLabel(Organism o)
		{
			super(new ImageIcon(makeThumb(o.getComposition().getImage(), THUMB_W, THUMB_H)));
			this.o = o;
		}
		
		public Organism getOrganism()
		{
			return o;
		}
		
	}
	
	private class ThumbDragListener implements MouseListener, MouseMotionListener
	{
		public void mouseClicked(MouseEvent e) {
			Organism o = ((ThumbLabel) e.getSource()).getOrganism();
			largeDisplay.removeAll();
			largeDisplay.add(new JLabel(new ImageIcon(o.getComposition().getImage())));
			largeDisplay.repaint();
			largeDisplay.revalidate();
		}
		public void mouseEntered(MouseEvent e) {
			//System.out.println("ENTER " + ((ThumbLabel)e.getComponent()).getID() + " at " + e.getX() + ", " + e.getY());
			in = (ThumbLabel) e.getSource();
		}
		public void mouseExited(MouseEvent e) {
			//System.out.println("EXIT " + ((ThumbLabel)e.getComponent()).getID() + " at " + e.getX() + ", " + e.getY());
			in = null;
		}
		public void mousePressed(MouseEvent e) { }
		public void mouseMoved(MouseEvent e) { }
		
		public void mouseDragged(MouseEvent e)
		{
			//System.out.println("DRAG " + ((ThumbLabel)e.getComponent()).getID() + " at " + e.getX() + ", " + e.getY());
			if (in == null)
				in = (ThumbLabel) e.getSource();
		}
		
		public void mouseReleased(MouseEvent e) 
		{ 
			//System.out.println("REL " + ((ThumbLabel)e.getComponent()).getID() + " at " + e.getX() + ", " + e.getY());
			if (in != null)
			{
				if (in != (ThumbLabel) e.getSource())
					reorder(in, (ThumbLabel) e.getSource());
				
				in = null;
			}
		}
	}

}
