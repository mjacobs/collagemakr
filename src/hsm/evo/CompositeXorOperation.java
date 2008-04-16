package hsm.evo;

import hsm.image.CompositeAdaptor;

import java.awt.AlphaComposite;

public class CompositeXorOperation extends ParametrizedOperationAdaptor {

	static
	{
		OperationMetadata.getInstance().registerOperation(CompositeXorOperation.class);
	}
	
	public CompositeXorOperation()
	{
		super(new CompositeAdaptor(AlphaComposite.Xor));
	}
	
	public String toString()
	{
		return "Composite (xor)";
	}
}
