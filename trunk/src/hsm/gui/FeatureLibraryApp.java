package hsm.gui;

import hsm.image.DirectorySource;
import hsm.image.ImageLibrary;
import hsm.tools.IExtractor;
import hsm.tools.LuminanceExtractor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class FeatureLibraryApp {

	private class LibraryPanel extends JPanel
	{
		private static final long serialVersionUID = 8379879652065146824L;
		
		private ImageLibrary _library = null;
		
		public LibraryPanel()
		{
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			ImageLibrary lib = getLibrary();
			BufferedImage[] imgs = lib.getImageThumbs();
			
			for (int i=0; i<imgs.length; i++)
			{
				JLabel label = new JLabel();
				label.setIcon(new ImageIcon(imgs[i]));
				add(label);
			}
			
		}
		
		public ImageLibrary getLibrary()
		{
			if (_library == null)
			{
				_library = new ImageLibrary("data/features");
			}
			
			return _library;
		}

		@Override
		public Dimension getMaximumSize() {
			return new Dimension(128,1);
		}
		
		public Dimension getMinimumSize() {
			return new Dimension(1,1);
		}
	}
	
	private class FeatureSelectionPanel extends JPanel implements MouseListener, MouseMotionListener
	{
		private static final long serialVersionUID = 1957452539132027545L;
		BufferedImage _image = null;
		BufferedImage _extract = null;
		int _extractX = 0;
		int _extractY = 0;
		
		Point _downPoint = null;
		Rectangle _selectRect = null;
		
		IExtractor _extractor = new LuminanceExtractor();
		
		public FeatureSelectionPanel()
		{
			addMouseListener(this);
			addMouseMotionListener(this);
		}
		
		public void setImage(BufferedImage img)
		{
			_image = img;
			repaint();
		}
		
		public BufferedImage getExtract()
		{
			return _extract;
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			
			if (_image != null)
			{
				g.drawImage(_image, 0, 0, null);
			}
			
			if (_selectRect != null)
			{
				g.setColor(Color.GREEN);
				g.drawRect(_selectRect.x, _selectRect.y, 
						  _selectRect.width, _selectRect.height);	
			}
			
			if (_extract != null)
			{
				g.setColor(new Color(0, 0, 0, 200));
				g.fillRect(0, 0, _image.getWidth(), _image.getHeight());
				
				g.setColor(Color.WHITE);
				
				g.drawImage(_extract, _extractX, _extractY, null);
			}
		}

		public void mouseClicked(MouseEvent e) { }
		public void mouseEntered(MouseEvent e) { }
		public void mouseExited(MouseEvent e) { }

		public void mousePressed(MouseEvent e) {
			
			_extract = null;
			_downPoint = e.getPoint();
			_selectRect = new Rectangle(_downPoint);
			
			repaint();
		}

		public void mouseReleased(MouseEvent e) {
			_extract = _extractor.getExtract(_image.getSubimage(_selectRect.x, _selectRect.y, _selectRect.width, _selectRect.height));
			
			_extractX = _selectRect.x;
			_extractY = _selectRect.y;
			
			_selectRect = null;
			_downPoint = null;
			
			repaint();
		}

		public void mouseDragged(MouseEvent e) {
			_selectRect = new Rectangle(_downPoint);
			_selectRect.add(e.getPoint());
			
			repaint();
		}

		public void mouseMoved(MouseEvent e) { }
		
		
	}
	
	public FeatureLibraryApp()
	{
		JFrame mainWindow = new JFrame();
		JPanel contentPane = new JPanel();
		
		mainWindow.setVisible(true);
		
		mainWindow.setContentPane(contentPane);
		
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		FeatureSelectionPanel sp = new FeatureSelectionPanel();
		DirectorySource ds = new DirectorySource("data/flickr_local/");
		
		sp.setImage(ds.getRandomImage(null).getImage());
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(new LibraryPanel()), sp);
		contentPane.add(splitPane);
		
	}
	
	public static void main(String[] argv)
	{
		new FeatureLibraryApp();
	}
}
