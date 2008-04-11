package hsm.evo;

import java.awt.AlphaComposite;

public class CompositeXorOperation extends ParametrizedCompositeOperation {

	static
	{
		OperationMetadata.getInstance().registerOperation(CompositeXorOperation.class);
	}
	
	public CompositeXorOperation()
	{
		super(AlphaComposite.Xor);
	}
	
	public String toString()
	{
		return "Composite (xor)";
	}
}
