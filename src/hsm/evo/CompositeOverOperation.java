package hsm.evo;

import java.awt.AlphaComposite;

public class CompositeOverOperation extends ParametrizedCompositeOperation {

	static
	{
		OperationMetadata.getInstance().registerOperation(CompositeOverOperation.class).lockDown();
	}
	
	public CompositeOverOperation()
	{
		super(AlphaComposite.SrcOver);
	}
	
	public String toString()
	{
		return "Composite (over)";
	}
}
