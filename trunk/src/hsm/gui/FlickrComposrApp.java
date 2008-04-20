package hsm.gui;

import hsm.evo.Organism;
import hsm.evo.Population;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.Point;
import java.util.Vector;

public class FlickrComposrApp
{
	protected static final int NUM_IMS_W = 2;
	protected static final int NUM_IMS_H = 2;
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
	private Vector<String> imageTitles;
	protected ButtonGroup[] choice;

	public FlickrComposrApp()
	{
		_currentPopulation = Population.randomPopulation(NUM_IMS_W * NUM_IMS_H);
		choice = new ButtonGroup[NUM_CHOICES];
		imageTitles = new Vector<String>();
		for (int i = 0; i < NUM_CHOICES; i++)
			choice[i] = new ButtonGroup();

	}

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
			makrTitle.setText("chooz 3 plz!!!11");
			makrTitle.setFont(new Font("Impact", 0, 40));
			makrTitle.setPreferredSize(new Dimension(232, 80));
			makrTitle.setHorizontalAlignment(SwingConstants.CENTER);
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(makrTitle, BorderLayout.NORTH);

			TableModel dataModel = new ComposrTableModel(_currentPopulation);
			JTable table = new JTable(dataModel);
			table.setRowHeight(255);
			table.setColumnSelectionAllowed(true);
			table.setRowSelectionAllowed(true);

			table.setDefaultRenderer(Organism.class, new RankCellRenderer());

			JScrollPane jscrPane = new JScrollPane(table);
			JPanel votingPanel = new JPanel(new GridLayout(1,6));
			votingPanel.add(new JLabel("First:"));
			votingPanel.add(new JComboBox(new DefaultComboBoxModel(imageTitles)));
			votingPanel.add(new JLabel("Second:"));
			votingPanel.add(new JComboBox(new DefaultComboBoxModel(imageTitles)));
			votingPanel.add(new JLabel("Third:"));
			votingPanel.add(new JComboBox(new DefaultComboBoxModel(imageTitles)));
			jContentPane.add(jscrPane, BorderLayout.CENTER);
			jContentPane.add(votingPanel, BorderLayout.SOUTH);
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

	private class RankPanel extends JPanel
	{
		public RankPanel(Organism org, int row, int col)
		{
			   JPanel pnPanel0 = this;
			   GridBagLayout gbPanel0 = new GridBagLayout();
			   GridBagConstraints gbcPanel0 = new GridBagConstraints();
			   pnPanel0.setLayout( gbPanel0 );

			   JLabel lbLabel0 = new JLabel( ""  );
			   lbLabel0.setIcon( new ImageIcon( org.getComposition().getImage() ) );
			   gbcPanel0.gridx = 0;
			   gbcPanel0.gridy = 0;
			   gbcPanel0.gridwidth = 1;
			   gbcPanel0.gridheight = 1;
			   gbcPanel0.fill = GridBagConstraints.BOTH;
			   gbcPanel0.weightx = 1;
			   gbcPanel0.weighty = 1;
			   gbcPanel0.anchor = GridBagConstraints.NORTH;
			   gbPanel0.setConstraints( lbLabel0, gbcPanel0 );
			   pnPanel0.add( lbLabel0 );

			   JLabel lbLabel1 = new JLabel( "Composition " + (row * NUM_IMS_W + col) );
			   gbcPanel0.gridx = 0;
			   gbcPanel0.gridy = 1;
			   gbcPanel0.gridwidth = 1;
			   gbcPanel0.gridheight = 1;
			   gbcPanel0.fill = GridBagConstraints.BOTH;
			   gbcPanel0.weightx = 1;
			   gbcPanel0.weighty = 1;
			   gbcPanel0.anchor = GridBagConstraints.NORTH;
			   gbPanel0.setConstraints( lbLabel1, gbcPanel0 );
			   pnPanel0.add( lbLabel1 );
		}

		public RankPanel(Organism org, int row, int column, int j)
		{
			JLabel orgImage;

			orgImage = new JLabel("");
			orgImage.setIcon(new ImageIcon(org.getComposition()
					.getImage()));

			this.add(orgImage);
			
			GridBagLayout gbPanel0 = new GridBagLayout();
			GridBagConstraints gbcPanel0 = new GridBagConstraints();
			this.setLayout( gbPanel0 );

			gbcPanel0.gridx = 0;
			gbcPanel0.gridy = 0;
			gbcPanel0.gridwidth = 1;
			gbcPanel0.gridheight = 1;
			gbcPanel0.fill = GridBagConstraints.BOTH;
			gbcPanel0.weightx = 1;
			gbcPanel0.weighty = 1;
			gbcPanel0.anchor = GridBagConstraints.NORTH;
			gbPanel0.setConstraints( orgImage, gbcPanel0 );
			this.add( orgImage );


			JLabel lbLabel1 = new JLabel("Composition " + (row * NUM_IMS_W + column));
			gbcPanel0.gridx = 0;
			gbcPanel0.gridy = 1;
			gbcPanel0.gridwidth = 1;
			gbcPanel0.gridheight = 1;
			gbcPanel0.fill = GridBagConstraints.BOTH;
			gbcPanel0.weightx = 1;
			gbcPanel0.weighty = 1;
			gbcPanel0.anchor = GridBagConstraints.NORTH;
			gbPanel0.setConstraints( lbLabel1, gbcPanel0 );
			this.add( lbLabel1 );
		}
	}

	private class RankCellRenderer extends JPanel implements TableCellRenderer
	{

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column)
		{
			return new RankPanel((Organism) value, row, column);
		}

	}

	private class ComposrTableModel extends AbstractTableModel
	{
		Organism[][] pModel;

		public ComposrTableModel(Population population)
		{
			Organism[] orgs = population.getOrganisms();

			pModel = new Organism[NUM_IMS_H][NUM_IMS_W];

			for (int i = 0; i < NUM_IMS_H; i++)
			{
				for (int j = 0; j < NUM_IMS_W; j++)
				{
					pModel[i][j] = orgs[i * NUM_IMS_W + j];
					imageTitles.add("Composition " + (i * NUM_IMS_W + j));
				}
			}
		}

		public int getColumnCount()
		{
			return NUM_IMS_W;
		}

		public int getRowCount()
		{
			return NUM_IMS_H;
		}

		public Object getValueAt(int row, int col)
		{
			return (row > NUM_IMS_H) || (col > NUM_IMS_W) ? null
					: pModel[row][col];
		}

		@Override
		public Class getColumnClass(int column)
		{
			return getValueAt(0, column).getClass();
		}
	};

}