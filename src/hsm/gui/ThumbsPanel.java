package hsm.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import hsm.evo.Organism;
import hsm.evo.Population;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
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
		JMenuItem menuItem;
		JPopupMenu popup = new JPopupMenu();
		menuItem = new JMenuItem("Accept");
		//menuItem.addActionListener();
		popup.add(menuItem);
		menuItem = new JMenuItem("Reject");
		//menuItem.addActionListener();
		popup.add(menuItem);
		
		for (int i = 0; i < orgs.size(); i++)
		{
			ThumbLabel tl = new ThumbLabel(orgs.get(i));
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
	
	private class ThumbLabel extends JLabel implements MouseListener, ActionListener
	{
		private Organism _organism;
		private JPopupMenu _menuPopup;
		
		public ThumbLabel(Organism org)
		{
			super(new ImageIcon(makeThumb(org.getComposition().getImage(), THUMB_W, THUMB_H)));
			setBorder(BorderFactory.createLineBorder(Color.BLUE));
			_organism = org;
			addMouseListener(this);
			
			_menuPopup = new JPopupMenu();
			JMenuItem menuItem = new JMenuItem("Accept");
			menuItem.addActionListener(this);
			_menuPopup.add(menuItem);
			menuItem = new JMenuItem("Reject");
			menuItem.addActionListener(this);
			_menuPopup.add(menuItem);
		}
		
		public Organism getOrganism()
		{
			return _organism;
		}
		public void mouseClicked(MouseEvent e)
		{
			Organism o = ((ThumbLabel) e.getSource()).getOrganism();
			largeDisplay.removeAll();
			largeDisplay.add(new JLabel(new ImageIcon(o.getComposition().getImage())));
			largeDisplay.repaint();
			largeDisplay.revalidate();	
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e)
		{
			maybeShowPopup(e);
		}
		public void mouseReleased(MouseEvent e)
		{
			maybeShowPopup(e);
		}
		public void actionPerformed(ActionEvent e)
		{
			JMenuItem itemClicked = (JMenuItem)e.getSource();
			if (itemClicked.getText().equals("Accept"))
			{
				_organism.setFitness(1.0);
				this.setBorder(BorderFactory.createLineBorder(Color.GREEN));
			}
			else
			{
				_organism.setFitness(0.0);
				this.setBorder(BorderFactory.createLineBorder(Color.RED));
			}
		}
		
		private void maybeShowPopup(MouseEvent e)
		{
			if (e.isPopupTrigger())
			{
				_menuPopup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}
}
