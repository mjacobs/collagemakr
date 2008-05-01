package hsm.gui;

import hsm.evo.Organism;
import hsm.evo.Population;
import hsm.global.Config;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class FlickrComposrApp
{

	//protected static final int NUM_IMS = 9;
	protected static final int NUM_IMS_W = 3;
	protected static final int NUM_IMS_H = 3;
	protected static final int NUM_CHOICES = 3;

	protected Population _currentPopulation;

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
	private ThumbsPanel thumbsPanel;
	private JMenu genMenu;
	private JMenuItem genMenuItem;
	private JMenuItem restartItem;

	public FlickrComposrApp()
	{
		_currentPopulation = Population.randomPopulation(NUM_IMS_H*NUM_IMS_W);
	}

	public void nextState()
	{
		if (thumbsPanel != null)
		{
			_currentPopulation = _currentPopulation.nextGeneration();
			
			jContentPane = null;
			jFrame.setContentPane(getJContentPane());
			jFrame.repaint();
			jContentPane.revalidate();
			jFrame.pack();
		}
	}

	private JFrame getJFrame()
	{
		if (jFrame == null)
		{
			jFrame = new JFrame();
			jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jFrame.setJMenuBar(getJJMenuBar());
			jFrame.setSize(Config.getConfig().getInt("canvas_height") + ThumbsPanel.getTHUMB_H() + 50, 400);
			jFrame.setContentPane(getJContentPane());
			jFrame.setTitle("Flickr Composr Application");

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
			makrTitle.setText("select favritz now, kthxbye!!1");
			makrTitle.setFont(new Font("Impact", 0, 40));
			makrTitle.setPreferredSize(new Dimension(232, 80));
			makrTitle.setHorizontalAlignment(SwingConstants.CENTER);
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(makrTitle, BorderLayout.NORTH);
			thumbsPanel = new ThumbsPanel(_currentPopulation, NUM_IMS_W, NUM_IMS_H);
			jContentPane.add(thumbsPanel, BorderLayout.CENTER);
			jFrame.pack();
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
			jJMenuBar.add(getGenerationMenu());
		}
		return jJMenuBar;
	}

	private JMenu getGenerationMenu()
	{
		if (genMenu == null)
		{
			genMenu = new JMenu();
			genMenu.setText("Operations");
			genMenu.add(getNextGenItem());
			genMenu.add(getRestartItem());
		}
		return genMenu;
	}

	private JMenuItem getNextGenItem()
	{
		if (genMenuItem == null)
		{
			genMenuItem = new JMenuItem();
			genMenuItem.setText("Next Generation");
			genMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					
					nextState();
				}
			});
		}
		return genMenuItem;
	}
	

	private JMenuItem getRestartItem()
	{
		if (restartItem == null)
		{
			restartItem = new JMenuItem();
			restartItem.setText("Restart");
			restartItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					_currentPopulation = Population.randomPopulation(NUM_IMS_H*NUM_IMS_W);
					jContentPane = null;
					jFrame.setContentPane(getJContentPane());
					jFrame.repaint();
					jContentPane.revalidate();
				}
			});
		}
		return restartItem;
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

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				FlickrComposrApp application = new FlickrComposrApp();
				application.getJFrame().setVisible(true);
				application.getJFrame().setSize(1050, 880);
			}
		});
	}

}