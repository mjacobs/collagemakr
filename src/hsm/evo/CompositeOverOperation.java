package hsm.evo;

import hsm.image.CompositeAdaptor;

import java.awt.AlphaComposite;

public class CompositeOverOperation extends ParametrizedOperationAdaptor {

	static
	{
		OperationMetadata.getInstance().registerOperation(CompositeOverOperation.class);
	}
	
	public CompositeOverOperation()
	{
		super(new CompositeAdaptor(AlphaComposite.SrcOver));
	}
	
	public String toString()
	{
		return "Composite (over)";
	}
}
