package hsm.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import hsm.evo.Organism;


public class StateController
{
	private FlickrComposrApp _fApp;
	private Organism _org1, _org2;
	
	public StateController(FlickrComposrApp fApp)
	{
		_fApp = fApp;
		_org1 = Organism.randomOrganism();
		_org2 = Organism.randomOrganism();
		try
		{
			_fApp.setIms(new BufferedImage[] { 
					_org1.getComposition().getImage(), 
					_org2.getComposition().getImage() });
			_fApp.addActListeners(new ActionListener[] { 
					new controllerListener(1), 
					new controllerListener(2) });
		} catch (MakrGUIException e)
		{
			e.printStackTrace();
		}
	}
	
	public void updateState(int selectedOrganism)
	{
		
	}
	
	private class controllerListener implements ActionListener
	{
		private int selectedIm;
		
		public controllerListener(int i)
		{
			selectedIm = i;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			updateState(selectedIm);
		}		
	}
}
