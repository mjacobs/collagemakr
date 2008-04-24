package hsm.gui;

import java.awt.Color;
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

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

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
	
	public LinkedList<Organism> getOrganismsList()
	{
		return orgs;
	}

	private void addOrganisms()
	{
		ThumbDragListener tdl = new ThumbDragListener();
		for (int i = 0; i < orgs.size(); i++)
		{
			Component tl = new ThumbLabel(orgs.get(i));
			tl.addMouseListener(tdl);
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
	      thumbHeight, BufferedImage.TYPE_4BYTE_ABGR);
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
			setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
			o.setFitness(0);
		}
		
		public Organism getOrganism()
		{
			return o;
		}
		
	}
	
	private class ThumbDragListener implements MouseListener
	{
		public void mouseClicked(MouseEvent e) {
			Organism o = ((ThumbLabel) e.getSource()).getOrganism();
			if (o.getFitness() == 1.0)
			{
				((ThumbLabel) e.getSource()).setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
				o.setFitness(0);
			}
			else
			{
				((ThumbLabel) e.getSource()).setBorder(BorderFactory.createLineBorder(Color.GREEN));
				o.setFitness(1.0);
			}
			largeDisplay.removeAll();
			largeDisplay.add(new JLabel(new ImageIcon(o.getComposition().getImage())));
			largeDisplay.repaint();
			largeDisplay.revalidate();
		}
		public void mouseEntered(MouseEvent e) { }
		public void mouseExited(MouseEvent e) {	}
		public void mousePressed(MouseEvent e) { }		
		public void mouseReleased(MouseEvent e) { }
	}

}
