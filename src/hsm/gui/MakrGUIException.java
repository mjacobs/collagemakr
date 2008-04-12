package hsm.gui;


public class MakrGUIException extends Exception
{
	public MakrGUIException()
	{
		this("Unknown exception in CollageMakr GUI.");
	}

	public MakrGUIException(String m)
	{
		super(m);
	}
}
