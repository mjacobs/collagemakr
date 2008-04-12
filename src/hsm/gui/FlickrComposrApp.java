package hsm.gui;

import hsm.evo.Organism;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.Dimension;

public class FlickrComposrApp
{
	private StateController sController = null;
	private JFrame jFrame = null;
	private JPanel jContentPane = null;
	private JMenuBar jJMenuBar = null;
	private JMenu fileMenu = null;
	private JMenu helpMenu = null;
	private JMenuItem exitMenuItem = null;
	private JMenuItem aboutMenuItem = null;
	private JDialog aboutDialog = null;
	private JPanel aboutContentPane = null;
	private JLabel aboutVersionLabel = null;
	private JLabel makrTitle = null;
	private JPanel insidePanel = null;
	private JLabel im1label = null;
	private JLabel im2label = null;
	private BufferedImage im1 = null;
	private BufferedImage im2 = null;
	private JPanel lPanel = null;
	private JPanel rPanel = null;
	private JButton im2Button = null;
	private JButton im1Button = null;

	/**
	 * This method initializes lPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getLPanel()
	{
		if (lPanel == null)
		{
			lPanel = new JPanel();
			im1label = new JLabel(new ImageIcon(getBlankImage()));
			lPanel.setLayout(new BoxLayout(getLPanel(), BoxLayout.Y_AXIS));
			lPanel.add(getCenteredPanel(im1label), null);
			lPanel.add(getCenteredPanel(getIm1Button()), null);
		}
		return lPanel;
	}

	/**
	 * This method initializes rPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getRPanel()
	{
		if (rPanel == null)
		{
			rPanel = new JPanel();
			im2label = new JLabel(new ImageIcon(getBlankImage()));
			rPanel.setLayout(new BoxLayout(getRPanel(), BoxLayout.Y_AXIS));
			rPanel.add(getCenteredPanel(im2label), null);
			rPanel.add(getCenteredPanel(getIm2Button()), null);
		}
		return rPanel;
	}

	private JPanel getCenteredPanel(Component c)
	{
		JPanel p = getCenteredPanel();
		p.add(c);
		return p;
	}

	private JPanel getCenteredPanel()
	{
		JPanel cPanel = new JPanel();
		cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.X_AXIS));
		return cPanel;
	}

	/**
	 * This method initializes im2Button
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getIm2Button()
	{
		if (im2Button == null)
		{
			im2Button = new JButton();
			im2Button.setText("chooz plz!");
		}
		return im2Button;
	}

	/**
	 * This method initializes im1Button
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getIm1Button()
	{
		if (im1Button == null)
		{
			im1Button = new JButton();
			// im1Button.addActionListener(new )
			im1Button.setText("chooz plz!");
		}
		return im1Button;
	}

	/**
	 * This method initializes jFrame
	 * 
	 * @return javax.swing.JFrame
	 */
	private JFrame getJFrame()
	{
		if (jFrame == null)
		{			
			jFrame = new JFrame();
			jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jFrame.setJMenuBar(getJJMenuBar());
			jFrame.setSize(800, 600);
			jFrame.setContentPane(getJContentPane());
			jFrame.setTitle("Application");
			

			if (sController == null)
				sController = new StateController(this);
		}
		return jFrame;
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane()
	{
		if (jContentPane == null)
		{
			makrTitle = new JLabel();
			makrTitle.setText("flickr image composr!!!11");
			makrTitle.setFont(new Font("Impact", 0, 40));
			makrTitle.setPreferredSize(new Dimension(232, 80));
			makrTitle.setHorizontalAlignment(JLabel.CENTER);
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(makrTitle, BorderLayout.NORTH);
			jContentPane.add(getInsidePanel(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jJMenuBar
	 * 
	 * @return javax.swing.JMenuBar
	 */
	private JMenuBar getJJMenuBar()
	{
		if (jJMenuBar == null)
		{
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getFileMenu());
			jJMenuBar.add(getHelpMenu());
		}
		return jJMenuBar;
	}

	/**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getFileMenu()
	{
		if (fileMenu == null)
		{
			fileMenu = new JMenu();
			fileMenu.setText("File");
			fileMenu.add(getExitMenuItem());
		}
		return fileMenu;
	}

	/**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getHelpMenu()
	{
		if (helpMenu == null)
		{
			helpMenu = new JMenu();
			helpMenu.setText("Help");
			helpMenu.add(getAboutMenuItem());
		}
		return helpMenu;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getExitMenuItem()
	{
		if (exitMenuItem == null)
		{
			exitMenuItem = new JMenuItem();
			exitMenuItem.setText("Exit");
			exitMenuItem.addActionListener(new ActionListener()
			{

				public void actionPerformed(ActionEvent e)
				{
					System.exit(0);
				}
			});
		}
		return exitMenuItem;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getAboutMenuItem()
	{
		if (aboutMenuItem == null)
		{
			aboutMenuItem = new JMenuItem();
			aboutMenuItem.setText("About");
			aboutMenuItem.addActionListener(new ActionListener()
			{

				public void actionPerformed(ActionEvent e)
				{
					JDialog aboutDialog = getAboutDialog();
					aboutDialog.pack();
					Point loc = getJFrame().getLocation();
					loc.translate(20, 20);
					aboutDialog.setLocation(loc);
					aboutDialog.setVisible(true);
				}
			});
		}
		return aboutMenuItem;
	}

	/**
	 * This method initializes aboutDialog
	 * 
	 * @return javax.swing.JDialog
	 */
	private JDialog getAboutDialog()
	{
		if (aboutDialog == null)
		{
			aboutDialog = new JDialog(getJFrame(), true);
			aboutDialog.setTitle("About");
			aboutDialog.setContentPane(getAboutContentPane());
		}
		return aboutDialog;
	}

	/**
	 * This method initializes aboutContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getAboutContentPane()
	{
		if (aboutContentPane == null)
		{
			aboutContentPane = new JPanel();
			aboutContentPane.setLayout(new BorderLayout());
			aboutContentPane.add(getAboutVersionLabel(), BorderLayout.CENTER);
		}
		return aboutContentPane;
	}

	/**
	 * This method initializes aboutVersionLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getAboutVersionLabel()
	{
		if (aboutVersionLabel == null)
		{
			aboutVersionLabel = new JLabel();
			aboutVersionLabel.setText("Version 1.0");
			aboutVersionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return aboutVersionLabel;
	}

	/**
	 * This method initializes insidePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getInsidePanel()
	{
		if (insidePanel == null)
		{
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(1);
			gridLayout.setColumns(2);
			insidePanel = new JPanel();
			insidePanel.setLayout(gridLayout);
			insidePanel.add(getLPanel(), null);
			insidePanel.add(getRPanel(), null);
		}
		return insidePanel;
	}

	private BufferedImage getBlankImage()
	{
		return new BufferedImage(375, 375, BufferedImage.TYPE_3BYTE_BGR);
	}

	public void setIm1(BufferedImage im1)
	{
		this.im1 = im1;
	}

	public void setIm2(BufferedImage im2)
	{
		this.im2 = im2;
	}
	
	public void addActListener1(ActionListener al)
	{
		this.im1Button.addActionListener(al);
	}
	
	public void addActListener2(ActionListener al)
	{
		this.im2Button.addActionListener(al);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				FlickrComposrApp application = new FlickrComposrApp();
				application.getJFrame().setVisible(true);
			}
		});
	}

	public void setIms(BufferedImage[] bufferedImages) throws MakrGUIException
	{
		if (bufferedImages.length == 2)
		{
			this.im1 = bufferedImages[0];
			this.im2 = bufferedImages[1];
		}
		else
		{
			throw new MakrGUIException("Incorrect number of images attempted to be set.");
		}
	}

	public void addActListeners(ActionListener[] actionListeners) throws MakrGUIException
	{
		if (actionListeners.length == 2)
		{
			im1Button.addActionListener(actionListeners[0]);
			im1Button.addActionListener(actionListeners[1]);
		}
		else
		{
			throw new MakrGUIException("Incorrect number of ActionListeners attempted to be set.");
		}
	}

}
