package hsm.evo;

import hsm.image.CompositeAdaptor;

import java.awt.AlphaComposite;

public class CompositeXorOperation extends ParametrizedOperationAdaptor {

	static
	{
		OperationMetadata.getInstance().registerOperation(CompositeXorOperation.class);
		OperationMetadata.getInstance().setOperationProbability(CompositeXorOperation.class, 1.0);
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
