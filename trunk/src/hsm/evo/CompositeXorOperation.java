package hsm.evo;

import hsm.global.Config;
import hsm.image.CompositeAdaptor;

import java.awt.AlphaComposite;

public class CompositeXorOperation extends ParametrizedOperationAdaptor {

	static
	{
		Config.getConfig().registerDouble("prob_compositexor", 1.0);
		OperationMetadata.getInstance().registerOperation(CompositeXorOperation.class);
		OperationMetadata.getInstance().setOperationProbability(CompositeXorOperation.class, Config.getConfig().getDouble("prob_compositexor"));
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
