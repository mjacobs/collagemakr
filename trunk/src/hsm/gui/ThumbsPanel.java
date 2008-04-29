package hsm.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import hsm.evo.Organism;
import hsm.evo.Population;
import hsm.tools.ImageUtils;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

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
		private JFileChooser _fc;
		
		private static final String GREAT_TEXT = "WANT!!!";
		private static final String GOOD_TEXT = "meh";
		private static final String BAD_TEXT = "do not want.";
		private static final String SAVE_COMPOSITION = "save 4 laturz!";
		private String SAVE_DIR = System.getProperty("user.home") + System.getProperty("path.seperator");
		private static final String VIEW_TREE = "i can has tree?";
		
		public ThumbLabel(Organism org)
		{
			super(new ImageIcon(makeThumb(org.getComposition().getImage(), THUMB_W, THUMB_H)));
			setBorder(BorderFactory.createLineBorder(Color.RED));
			_organism = org;
			_organism.setFitness(0.0);
			
			addMouseListener(this);
			
			_menuPopup = new JPopupMenu();
			
			JMenuItem menuItem = new JMenuItem(GREAT_TEXT);
			menuItem.addActionListener(this);
			_menuPopup.add(menuItem);
			
			menuItem = new JMenuItem(GOOD_TEXT);
			menuItem.addActionListener(this);
			_menuPopup.add(menuItem);
			
			menuItem = new JMenuItem(BAD_TEXT);
			menuItem.addActionListener(this);
			_menuPopup.add(menuItem);
			
			_menuPopup.addSeparator();
			
			menuItem = new JMenuItem(SAVE_COMPOSITION);
			menuItem.addActionListener(this);
			_menuPopup.add(menuItem);

			menuItem = new JMenuItem(VIEW_TREE);
			menuItem.addActionListener(this);
			_menuPopup.add(menuItem);
			
			_fc = new JFileChooser();
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
			if (itemClicked.getText().equals(GOOD_TEXT))
			{
				_organism.setFitness(1.0);
				this.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
			}
			else if (itemClicked.getText().equals(GREAT_TEXT))
			{
				_organism.setFitness(2.0);
				this.setBorder(BorderFactory.createLineBorder(Color.GREEN));
			}
			else if (itemClicked.getText().equals(BAD_TEXT))
			{
				_organism.setFitness(0.0);
				this.setBorder(BorderFactory.createLineBorder(Color.RED));
			}
			else if (itemClicked.getText().equals(SAVE_COMPOSITION))
			{
				File f = null;
				int returnVal = _fc.showSaveDialog(ThumbsPanel.this);
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            f = _fc.getSelectedFile();
		        } else {
		            f = new File(System.getProperty("user.home") + 
		            		System.getProperty("path.seperator") + 
		            		_organism.toString() + ".jpg");
		        }

				try
				{					
					ImageUtils.writeImage(_organism.getComposition().getImage(), f);
				} catch (IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else if (itemClicked.getText().equals(VIEW_TREE))
			{
				JFrame treeFrame = new JFrame();
				JTree treePane = new JTree(_organism.getComposition().getRoot().generateJTreeNode());
				treeFrame.setContentPane(new JScrollPane(treePane));
				treeFrame.setSize(300, 400);
				treeFrame.setVisible(true);
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
